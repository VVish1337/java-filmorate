package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static int filmId = 1;
    @Getter
    private final HashMap<Integer, Film> filmsMap = new HashMap<>();
    private final DataValidator objectValidator = new DataValidator();

    @GetMapping
    public List<Film> getAll() {
        log.info("Get film list");
        return new ArrayList<>(filmsMap.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        objectValidator.validateFilm(film);
        film.setId(filmId);
        filmsMap.put(filmIdGenerator(), film);
        log.info("Film added");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        objectValidator.validateFilm(film);
        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);
            log.info("Film updated");
        } else {
            log.error("Update error.Film with this id not found");
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Update error.Film with this id not found");
        }
        return film;
    }

    private int filmIdGenerator() {
        log.info("Film id generated" + filmId);
        return filmId++;
    }
}