package ru.yandex.practicum.filmorate.controller.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class DataValidator {

    public void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Login contains whitespaces");
            throw new ValidationException("Login contains whitespaces");
        }
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Date release is Before than 12.28.1895");
            throw new ValidationException("Date release is Before than 12.28.1895");
        }
    }
}