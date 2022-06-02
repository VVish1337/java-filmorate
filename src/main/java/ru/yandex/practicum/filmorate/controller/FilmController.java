package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends FilmorateController<Film> {

    @Getter
    private final HashMap<Integer,Film> filmsMap = new HashMap<>();
    private static int filmId = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmorateController.class);

    @Override
    public List<Film> getAll() {
        log.info("Get film list");
        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Film create(@Valid @RequestBody Film film) {
        film.setId(filmId);
        filmsMap.put(filmIdGenerator(), dataValidator(film));
        log.info("Film added");
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        if(filmsMap.get(film.getId())!=null){
            filmsMap.put(film.getId(), dataValidator(film));
            log.info("Film updated");
        }else {
            log.error("Update error.Film with this id not found");
            throw new ValidationException("Update error.Film with this id not found");
        }
        return film;
    }

    @Override
    public Film dataValidator(Film film) {
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))){
            log.error("Date release is Before than 12.28.1895");
            throw new ValidationException("Date release is Before than 12.28.1895");
        }
        return film;
    }

    private int filmIdGenerator(){
        return filmId++;
    }
}