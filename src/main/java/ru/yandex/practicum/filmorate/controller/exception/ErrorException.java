package ru.yandex.practicum.filmorate.controller.exception;

public class ErrorException extends RuntimeException{
    public ErrorException(String message) {
        super(message);
    }
}
