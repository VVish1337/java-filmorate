package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.Date;
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

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        Date releaseDate = rs.getDate("RELEASE_DATE");
        int duration = rs.getInt("DURATION");
        MPA mpa = new MPA(rs.getLong("MPA_ID"), rs.getString("MPA_NAME"));
        Set<Genre> genre = new TreeSet(Comparator.comparingLong(Genre::getId));
        return new Film(id, name, description, releaseDate, duration, mpa, genre);
    }

    @Override
    public List<Film> getFilmList() {
        String sqlQuery = "SELECT*FROM FILMS AS F LEFT JOIN MPA MR ON MR.MPA_ID = F.MPA_ID ";
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::makeFilm));
        Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
        for (Film film : films) {
            genres.addAll(genreStorage.getGenresOfFilm(film.getId()));
                film.setGenres(new LinkedHashSet<>(genreStorage.getGenresOfFilm(film.getId())));
        }
        return films;
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
        film.setId(keyHolder.getKey().longValue());
        if(film.getGenres()!=null){
            genreStorage.addGenreToFilm(film.getId(),film.getGenres());
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
            genreStorage.deleteGenreOfFilm(film.getId());
            genreStorage.addGenreToFilm(film.getId(),film.getGenres());
        }
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "SELECT * FROM FILMS F " +
                "LEFT JOIN MPA MP ON MP.MPA_ID = F.MPA_ID " +
                "WHERE F.FILM_ID = ?";
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::makeFilm, id));
        for (Film film : films) {
//            for (Genre genres1:genreStorage.getGenresOfFilm(film.getId())){
            film.setGenres(new LinkedHashSet<>(genreStorage.getGenresOfFilm(film.getId())));

        }
        System.out.println(films);
        return films
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

//    private Film removeGenreDuplicates(Film film) {
//        film.setGenres(film.getGenres()
//                .stream()
//                .distinct()
//                .collect(Collectors.toList()));
//        return film;
//    }
}
