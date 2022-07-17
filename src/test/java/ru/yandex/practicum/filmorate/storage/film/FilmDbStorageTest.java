package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptsForTests.sql")
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

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

    @BeforeEach
    public void createFilmBefore(){
        filmDbStorage.create(getTestFilm());
    }

    private User getTestUser() {
        return new User(1L, "test@mail.ru",
                "testUser",
                "testusername", new Date(1999, 1, 1));
    }

    @Test
    void createFilm() {
        Film film = getTestFilm();
        assertThat(filmDbStorage.getFilmList())
                .contains(film)
                .hasSize(1);
    }

    @Test
    void getFilmById() {
        Film film = getTestFilm();
        long filmId = film.getId();
        assertThat(filmDbStorage.getFilmById(filmId)).isEqualTo(film);
    }

    @Test
    void getFilms() {
        Film film = getTestFilm();
        Film film2 = filmDbStorage.create(getTestFilm());
        List<Film> filmList = filmDbStorage.getFilmList();
        assertThat(filmList)
                .contains(film, film2)
                .hasSize(2);
    }

    @Test
    void updateFilm() {
        Film film = getTestFilm();
        film.setName("(upd)FilmName");
        film.setDescription("(upd)FilmDescription");
        film.setReleaseDate(new Date(1970, 1, 1));
        film.setDuration(40);
        filmDbStorage.update(film);
        Long filmId = film.getId();
        assertThat(filmDbStorage.getFilmById(filmId))
                .hasFieldOrPropertyWithValue("name", "(upd)FilmName")
                .hasFieldOrPropertyWithValue("description", "(upd)FilmDescription")
                .hasFieldOrPropertyWithValue("releaseDate", new Date(1970, 1, 1))
                .hasFieldOrPropertyWithValue("duration", 40);
        assertThat(filmDbStorage.getFilmList()).hasSize(1);
    }

    @Test
    void addAndGetLikes() {
        Film film = getTestFilm();
        User user = userDbStorage.create(getTestUser());
        filmDbStorage.addLikes(film.getId(), user.getId());
        assertThat(filmDbStorage.getFilmLikes(film.getId())).contains(user.getId());
        assertThat(filmDbStorage.getFilmLikes(film.getId())).hasSize(1);
    }

    @Test
    void removeLikes() {
        Film film = getTestFilm();
        User user = userDbStorage.create(getTestUser());
        filmDbStorage.addLikes(film.getId(), user.getId());
        filmDbStorage.deleteLikes(film.getId(), user.getId());
        assertThat(filmDbStorage.getFilmLikes(film.getId())).doesNotContain(user.getId());
        assertThat(filmDbStorage.getFilmLikes(film.getId())).hasSize(0);
    }

    @Test
    void getPopularFilm() {
        Film film = getTestFilm();
        Film film2 = filmDbStorage.create(getTestFilm());
        User user = userDbStorage.create(getTestUser());
        filmDbStorage.addLikes(film2.getId(), user.getId());
        assertThat(filmDbStorage.getPopularFilmList(10L)).isEqualTo(List.of(film2, film));
    }
}