package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;


@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getLong("GENRE_ID"),
                resultSet.getString("GENRE_NAME"));
    }

    @Override
    public Genre getById(long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Genre with " + id + " not found"));
    }

    @Override
    public List<Genre> getGenreList() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre);
    }

    @Override
    public List<Genre> getGenresOfFilm(long filmId) {
        String sql = "SELECT * FROM genres LEFT JOIN film_genres fg ON genres.genre_id = fg.genre_id WHERE film_id = ?";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre, filmId);
    }

    @Override
    public void addGenreToFilm(Long filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            String sql = "MERGE INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            genre.setName(getById(genre.getId()).getName());
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    @Override
    public void deleteGenreOfFilm(Long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}