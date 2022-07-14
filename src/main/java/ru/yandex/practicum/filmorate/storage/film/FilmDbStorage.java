package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    public Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getLong("FILM_ID"),
                resultSet.getString("FILM_NAME"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getDate("RELEASE_DATE"),
                resultSet.getInt("DURATION"),
                new MPA(resultSet.getLong("MPA_ID"),resultSet.getString("MPA_NAME")),
                new ArrayList<>(genreStorage.getGenresOfFilm(resultSet.getLong("FILM_ID"))));
    }

    @Override
    public List<Film> getFilmList() {
        String sqlQuery = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN MPA MR ON MR.MPA_ID = F.MPA_ID " +
                "LEFT JOIN GENRES G ON G.GENRE_ID = FG.GENRE_ID ";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }


    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if(film.getGenres()!=null){
  //          removeGenreDuplicates(film);
            genreStorage.deleteGenreOfFilm(film.getId());
            TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
            genres.addAll(film.getGenres());
            for(Genre genre : genres){
                genre.setName(genreStorage.getById((genre.getId())).getName());
                String sql = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("MERGE INTO FILMS (FIlM_ID,FILM_NAME, DESCRIPTION,RELEASE_DATE, DURATION,MPA_ID)" +
                        " VALUES (?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        if(film.getGenres()!=null){
 //           removeGenreDuplicates(film);
            TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
            genres.addAll(film.getGenres());
//            genreStorage.deleteGenreOfFilm(film.getId());
            for(Genre genre : genres){
                genre.setName(genreStorage.getById((genre.getId())).getName());
                String sql = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }

            //genreStorage.addGenreToFilm(film.getId(),film.getGenres());
        }
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "SELECT * FROM FILMS F " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN MPA MP ON MP.MPA_ID = F.MPA_ID " +
                "LEFT JOIN GENRES G ON G.GENRE_ID = FG.GENRE_ID " +
                "WHERE F.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Film with this id:"+id+" not found"));
    }

    public void addLikes(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES ( ?,? )";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    public void deleteLikes(long filmId, long userId){
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    @Override
    public List<Film> getPopularFilmList(Long count) {
        String sql = "SELECT F.FILM_ID FROM LIKES L " +
                "RIGHT JOIN FILMS F ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.FILM_ID) DESC LIMIT ?";
        List<Long> filmIds = jdbcTemplate.queryForList(sql, Long.class, count);
        return filmIds
                .stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    private List<Genre> removeGenreDuplicates(Film film) {
        film.setGenres(film.getGenres()
                .stream()
                .distinct()
                .collect(Collectors.toList()));
        return film.getGenres();
    }
}
