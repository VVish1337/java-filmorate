package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Repository
public class GenreDbStorage implements GenreStorage{

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

    @Override
    public List<Genre> getGenresOfFilm(long filmId) {
        String sql = "SELECT * FROM GENRES LEFT JOIN FILM_GENRES FG ON GENRES.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre, filmId);
    }

    @Override
    public void addGenreToFilm(Long filmId, TreeSet<Genre> genres) {
        for (Genre genre : genres) {
            genre.setName(getById(genre.getId()).getName());
            String sql = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    @Override
    public void deleteGenreOfFilm(Long filmId) {
        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
