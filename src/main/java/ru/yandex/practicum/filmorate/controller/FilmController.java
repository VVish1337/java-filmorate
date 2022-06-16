package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }



    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
       return filmService.update(film);
    }

    @GetMapping("{filmId}")
    public Film getById(@PathVariable long filmId){
        return filmService.getById(filmId);
    }

    @PutMapping("{filmId}/like/{userId}")
    public List<Film> addLikes(@PathVariable long filmId,@PathVariable long userId){
        return filmService.addLikes(filmId,userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public List<Film> deleteLikes(@PathVariable long filmId,@PathVariable long userId){
        return filmService.deleteLikes(filmId,userId);
    }
}