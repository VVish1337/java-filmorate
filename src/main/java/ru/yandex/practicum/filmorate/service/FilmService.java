package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    @Autowired
    FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        if (filmStorage.getFilmsMap().containsKey(filmId) && userStorage.getUsersMap().containsKey(userId)) {
            filmStorage.getFilmsMap().get(filmId).getLikes().add(userId);
            return filmStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
        }
    }

    public List<Film> deleteLikes(long filmId, long userId) {
        if (filmStorage.getFilmsMap().containsKey(filmId) && userStorage.getUsersMap().containsKey(userId)) {
            filmStorage.getFilmsMap().get(filmId).getLikes().remove(userId);
            return filmStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
        }
    }

    public List<Film> getPopularFilmList(long count) {
        ArrayList<Film> sortedListFilms = new ArrayList<>(filmStorage.getFilmsMap().values());
        if (count == 10) {
            Comparator<Film> comparator = Comparator.comparingLong(a -> a.getLikes().size());
            sortedListFilms.sort(comparator.reversed());
            return sortedListFilms.stream().limit(10).collect(Collectors.toList());
        } else {
            Comparator<Film> comparator = Comparator.comparingLong(a -> a.getLikes().size());
            sortedListFilms.sort(comparator.reversed());
            return sortedListFilms.stream().limit(count).collect(Collectors.toList());
        }
    }
}