package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Disabled
public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
    }

    @Test
    public void updateThrowsException() {
        filmController.create(new Film(1, "ssr", "sss www ss",
                LocalDate.of(2011, 2, 2), 40));
        ValidationException x = assertThrows(ValidationException.class, () ->
                filmController.update(new Film(13, "ssr", "sss www ss",
                        LocalDate.of(2011, 2, 2), 40)));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, x.getStatus());
    }

    @Test
    public void createFilmEqualsList() {
        Film film = new Film(3, "ssr", "ssswwwss",
                LocalDate.of(2011, 2, 2), 40);
        filmController.create(film);
        List<Film> films = new ArrayList<>();
        films.add(film);
        assertEquals(films.size(), filmController.getAll().size());
        assertEquals(films, filmController.getAll());
    }

    @Test
    public void updateFilmEqualsList() {
        Film film = new Film(1, "ssr", "sss www ss",
                LocalDate.of(2011, 2, 2), 40);
        Film filmUpd = new Film(1, "ssr(upd)", "sss www ss",
                LocalDate.of(2011, 2, 2), 40);
        filmController.create(film);
        List<Film> films = new ArrayList<>();
        films.add(filmUpd);
        filmController.update(filmUpd);
        assertEquals(films.size(), filmController.getAll().size());
        assertEquals(films, filmController.getAll());
    }
}