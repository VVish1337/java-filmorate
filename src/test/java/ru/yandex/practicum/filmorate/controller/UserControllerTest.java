package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void init() {
        userController = new UserController();
    }

    @Test
    public void updateThrowsException() {
        userController.create(new User(0, "123@mail.ru", "antada", "ant",
                LocalDate.of(2010, 11, 1)));
        ValidationException x = assertThrows(ValidationException.class, () -> userController.update(
                new User(2, "123@mail.ru", "antaa", "antada",
                        LocalDate.of(2010, 11, 1))));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, x.getStatus());
    }

    @Test
    public void createUserEqualsList() {
        User user = new User(2, "123@mail.ru", "antaa", "antada",
                LocalDate.of(2010, 11, 1));
        userController.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        assertEquals(users.size(), userController.getAll().size());
        assertEquals(users, userController.getAll());
    }

    @Test
    public void updateUserEqualsList() {
        User user = new User(1, "123@mail.ru", "antaa", "antada",
                LocalDate.of(2010, 11, 1));
        User userUpd = new User(1, "123@mail.ru", "antaa(upd)", "antada",
                LocalDate.of(2010, 11, 1));
        userController.create(user);
        List<User> users = new ArrayList<>();
        users.add(userUpd);
        userController.update(userUpd);
        assertEquals(users.size(), userController.getAll().size());
        assertEquals(users, userController.getAll());
    }
}