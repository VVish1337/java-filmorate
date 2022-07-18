package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre getById(long id);

    List<Genre> getGenreList();

    List<Genre> getGenresOfFilm(long id);

    void addGenreToFilm(Long filmId, Set<Genre> genres);

    void deleteGenreOfFilm(Long filmId);
}
