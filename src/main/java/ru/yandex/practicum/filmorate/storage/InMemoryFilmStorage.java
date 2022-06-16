package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static long filmId = 1;
    @Getter
    private final HashMap<Long, Film> filmsMap = new HashMap<>();
    private final DataValidator objectValidator = new DataValidator();

    @Override
    public List<Film> getAll() {
        log.info("Get film list");
        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Film create(Film film) {
        objectValidator.validateFilm(film);
        film.setId(filmId);
        filmsMap.put(filmIdGenerator(), film);
        log.info("Film added");
        return film;
    }

    @Override
    public Film update(Film film) {
        objectValidator.validateFilm(film);
        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);
            log.info("Film updated");
        } else {
            log.error("Update error.Film with this id not found");
            throw new ValidationException( "Update error.Film with this id not found");
        }
        return film;
    }

    private long filmIdGenerator() {
        log.info("Film id generated" + filmId);
        return filmId++;
    }
}
