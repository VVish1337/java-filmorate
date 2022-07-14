package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final DataValidator dataValidator;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, DataValidator dataValidator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.dataValidator = dataValidator;
    }
//    private final InMemoryUserStorage userStorage;
//
//    @Autowired
//    FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
//        this.filmStorage = filmStorage;
//        this.userStorage = userStorage;
//    }

    public List<Film> getFilmList() {
        return filmStorage.getFilmList();
    }

    public Film create(Film film) {
        dataValidator.validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        dataValidator.validateFilm(film);
        return filmStorage.update(film);
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLikes(long filmId,long userId){
        checkUserAndFilmExists(filmId,userId);
        filmStorage.addLikes(filmId,userId);
    }

    public void deleteLikes(long filmId, long userId){
        checkUserAndFilmExists(filmId,userId);
        filmStorage.deleteLikes(filmId,userId);
    }

    private void checkUserAndFilmExists(long filmId, long userId){
        getFilmById(filmId);
        userStorage.getUserById(userId);
    }

    public List<Film> getPopularFilmList(Long count){
        return filmStorage.getPopularFilmList(count);
    }
}