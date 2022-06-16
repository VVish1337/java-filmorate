package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {
    InMemoryFilmStorage filmStorage;

    @Autowired
    FilmService(InMemoryFilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll(){
        return filmStorage.getAll();
    }

    public Film create(Film film){
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getById(long filmId) {
        if(filmStorage.getFilmsMap().containsKey(filmId)){
            return filmStorage.getFilmsMap().get(filmId);
        }else{
            throw new NotFoundException("");
        }
    }

    public List<Film> addLikes(long filmId,long userId){
        if(filmStorage.getFilmsMap().containsKey(filmId)){
            filmStorage.getFilmsMap().get(filmId).getLikes().add(userId);
            return filmStorage.getAll();
        }else{
            throw new NotFoundException("");
        }
    }

    public List<Film> deleteLikes(long filmId,long userId){
        if(filmStorage.getFilmsMap().containsKey(filmId)){
            filmStorage.getFilmsMap().get(filmId).getLikes().remove(userId);
            return filmStorage.getAll();
        }else{
            throw new NotFoundException("");
        }
    }



}
