package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptsForTests.sql")
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;

    private Film getTestFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("TestFilm");
        film.setDescription("TestDescription");
        film.setReleaseDate(new Date(2000, 1, 1));
        film.setMpa(new Mpa(1, "G"));
        film.setGenres(Set.of(new Genre(1, "Комедия")));
        return film;
    }

    List<Genre> allGenresList() {
        List<Genre> allGenres = new ArrayList<>();
        allGenres.add(new Genre(1, "Комедия"));
        allGenres.add(new Genre(2, "Драма"));
        allGenres.add(new Genre(3, "Мультфильм"));
        allGenres.add(new Genre(4, "Фантастика"));
        allGenres.add(new Genre(5, "Боевик"));
        allGenres.add(new Genre(6, "Детектив"));
        return allGenres;
    }

    @Test
    void getGenreById() {
        assertThat(genreDbStorage.getById(1).getName()).isEqualTo("Комедия");
        assertThat(genreDbStorage.getById(2).getName()).isEqualTo("Драма");
        assertThat(genreDbStorage.getById(3).getName()).isEqualTo("Мультфильм");
        assertThat(genreDbStorage.getById(4).getName()).isEqualTo("Фантастика");
        assertThat(genreDbStorage.getById(5).getName()).isEqualTo("Боевик");
        assertThat(genreDbStorage.getById(6).getName()).isEqualTo("Детектив");
    }

    @Test
    void getGenresOfFilm() {
        filmDbStorage.create(getTestFilm());
        assertThat(genreDbStorage.getGenresOfFilm(getTestFilm().getId()))
                .isEqualTo(new ArrayList<>(getTestFilm().getGenres()));
    }

    @Test
    void getGenreList() {
        assertThat(genreDbStorage.getGenreList()).isEqualTo(allGenresList());
    }

    @Test
    void addGenreToFilm() {
        filmDbStorage.create(getTestFilm());
        Set<Genre> testGenres = Set.of(new Genre(2, "Драма"),
                new Genre(6, "Детектив"));
        genreDbStorage.addGenreToFilm(getTestFilm().getId(), testGenres);
        List<Genre> listGenres = new ArrayList<>(testGenres);
        assertThat(genreDbStorage.getGenresOfFilm(getTestFilm().getId()))
                .contains(listGenres.get(0), listGenres.get(1));
        assertThat(genreDbStorage.getGenresOfFilm(getTestFilm().getId()))
                .hasSize(3);
    }

    @Test
    void deleteGenresByGenreTest() {
        Film film = getTestFilm();
        film.setGenres(Set.of(new Genre(1, "Комедия"),
                new Genre(6, "Детектив")));
        genreDbStorage.deleteGenreOfFilm(film.getId());
        assertThat(genreDbStorage.getGenresOfFilm(film.getId())).isEmpty();
    }
}