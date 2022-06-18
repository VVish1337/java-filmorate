package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    FilmService(InMemoryFilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getById(long filmId) {
        if (filmStorage.getFilmsMap().containsKey(filmId)) {
            return filmStorage.getFilmsMap().get(filmId);
        } else {
            throw new NotFoundException("Film id:" + filmId + " not found");
        }
    }

    public List<Film> addLikes(long filmId, long userId) {
        if (getById(filmId) != null && userService.getById(userId) != null) {
            getById(filmId).getLikes().add(userId);
            return filmStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
        }
    }

    public List<Film> deleteLikes(long filmId, long userId) {
        if (getById(filmId) != null && userService.getById(userId) != null) {
            getById(filmId).getLikes().remove(userId);
            return filmStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
        }
    }

    public List<Film> getPopularFilmList(long count) {
        if (count == 10) {
            return filmStorage.getFilmsMap().values().stream()
                    .sorted(Film::compareTo)
                    .limit(10).collect(Collectors.toList());
        } else {
            return filmStorage.getFilmsMap().values().stream()
                    .sorted(Film::compareTo)
                    .limit(count).collect(Collectors.toList());
        }
    }
}