package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getLong("FILM_ID"),
                resultSet.getString("FILM_NAME"),
                resultSet.getString("FILM_DESCRIPTION"),
                resultSet.getDate("RELEASE_DATE").toLocalDate(),
                resultSet.getInt("DURATION"),
                new MPA(resultSet.getLong("MPA_ID"),resultSet.getString("MPA_NAME"))
        );
    }

    @Override
    public List<Film> getFilmList() {
        String sqlQuery = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN MPA MR ON MR.MPA_ID = F.MPA_ID " +
                "LEFT JOIN GENRES G ON G.GENRE_ID = FG.GENRE_ID ";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
    }

    //ss
    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setLong(3, film.getMpa().getId());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("MERGE INTO FILMS (FILM_ID, FILM_NAME, DESCRIPTION,MPA_ID, RELEASE_DATE, DURATION)" +
                        " VALUES (?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration()
        );
//
//        if (film.getGenres() != null) {
//            List<Genre> genres = removeGenreDuplicates(film);
//            genreStorage.deleteGenresByFilm(film.getId());
//            genreStorage.assignGenreToFilm(film.getId(), genres);
//        }

        return film;
    }

    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN MPA MP ON MP.MPA_ID = F.MPA_ID " +
                "LEFT JOIN GENRES G ON G.GENRE_ID = FG.GENRE_ID " +
                "WHERE F.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Film with this "+id+" not found"));
    }

    public void addLikes(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES ( ?,? )";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    public void deleteLikes(long filmId, long userId){
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }
}
