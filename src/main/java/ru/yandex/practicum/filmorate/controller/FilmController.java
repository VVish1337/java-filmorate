package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilmList() {
        return filmService.getFilmList();
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
    public Film getFilmById(@PathVariable long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void addLikes(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLikes(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void deleteLikes(@PathVariable long filmId, @PathVariable long userId) {
        filmService.deleteLikes(filmId, userId);
    }

    @GetMapping("popular")
    public List<Film> getPopularFilmList(
            @RequestParam(value = "count", defaultValue = "10", required = false) long count) {
        return filmService.getPopularFilmList(count);
    }
}