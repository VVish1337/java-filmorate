package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;

    public FilmService(FilmDbStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLikes(long filmId,long userId){
        filmStorage.addLikes(filmId,userId);
    }

    public void deleteLikes(long filmId, long userId){
        filmStorage.deleteLikes(filmId,userId);
    }
//    public List<Film> addLikes(long filmId, long userId) {
//        if (getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
//            getFilmById(filmId).getLikes().add(userId);
//            return filmStorage.getFilmList();
//        } else {
//            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
//        }
//    }
//
//    public List<Film> deleteLikes(long filmId, long userId) {
//        if (getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
//            getFilmById(filmId).getLikes().remove(userId);
//            return filmStorage.getFilmList();
//        } else {
//            throw new NotFoundException("User id:" + userId + " or film id:" + filmId + " not found");
//        }
//    }
//
//    public List<Film> getPopularFilmList(long count) {
//        if (count == 10) {
//            return filmStorage.getFilmList().stream()
//                    .sorted(Film::compareTo)
//                    .limit(10).collect(Collectors.toList());
//        } else {
//            return filmStorage.getFilmList().stream()
//                    .sorted(Film::compareTo)
//                    .limit(count).collect(Collectors.toList());
//        }
//    }
}