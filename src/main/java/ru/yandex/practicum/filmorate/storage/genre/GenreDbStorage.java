package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getLong("GENRE_ID"),resultSet.getString("GENRE_NAME"));
    }

    @Override
    public Genre getById(long id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Genre with "+id+" not found"));
    }

    @Override
    public List<Genre> getGenreList() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre);
    }
}
