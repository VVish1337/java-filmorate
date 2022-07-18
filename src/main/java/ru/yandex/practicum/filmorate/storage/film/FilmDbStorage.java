package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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

    @Override
    public List<Film> getFilmList() {
        String sqlQuery = "SELECT*FROM films AS f LEFT JOIN mpa mr ON mr.mpa_id = f.mpa_id ";
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
        String sqlQuery = "INSERT INTO films (film_name, description, release_date, duration, mpa_id) " +
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
        jdbcTemplate.update("MERGE INTO films (fiLm_id,film_name, description,release_date, duration,mpa_id)" +
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
        String sqlQuery = "SELECT * FROM films f " +
                "LEFT JOIN mpa mp ON mp.mpa_id = f.mpa_id " +
                "WHERE f.film_id = ?";
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::makeFilm, id));
        for (Film film : films) {
            film.setGenres(new LinkedHashSet<>(genreStorage.getGenresOfFilm(film.getId())));
        }
        System.out.println(films);
        return films
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Film with this id:"+id+" not found"));
    }

    public void addLikes(long filmId, long userId) {
        String sqlQuery = "INSERT INTO likes(film_id, user_id) VALUES ( ?,? )";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    public void deleteLikes(long filmId, long userId){
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    @Override
    public List<Film> getPopularFilmList(Long count) {
        String sql = "SELECT f.film_id FROM likes l " +
                "RIGHT JOIN films f ON f.film_id = l.film_id " +
                "GROUP BY f.film_id ORDER BY COUNT(l.film_id) DESC LIMIT ?";
        List<Long> filmIds = jdbcTemplate.queryForList(sql, Long.class, count);
        return filmIds
                .stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    public List<Long> getFilmLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Set<Genre> genre = new TreeSet(Comparator.comparingLong(Genre::getId));
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE"),
                rs.getInt("DURATION"),
                new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")), genre);
    }
}
