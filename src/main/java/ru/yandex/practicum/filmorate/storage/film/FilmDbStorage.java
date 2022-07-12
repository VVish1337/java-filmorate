package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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
        return null;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
           stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(long id) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID=?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm,id);
        if(films.size() !=0){
            //TODO not found
        }
        return films.get(0);
    }
}
