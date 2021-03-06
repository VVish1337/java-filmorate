package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> getFilmList();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(long id);

    List<Film> getPopularFilmList(Long count);


}