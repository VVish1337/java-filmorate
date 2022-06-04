package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private static int userId = 1;
    private final HashMap<Integer, User> usersMap = new HashMap<>();
    private final DataValidator objectValidator = new DataValidator();

    @GetMapping
    public List<User> getAll() {
        log.info("Get user list");
        return new ArrayList<>(usersMap.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        objectValidator.validateUser(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("User name was empty.User name was set by login:" + user.getName());
        }
        user.setId(userId);
        usersMap.put(userIdGenerator(), user);
        log.info("User created:" + user.getName());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        objectValidator.validateUser(user);
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
        } else {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Update error.User id incorrect.");
        }
        return user;
    }

    private int userIdGenerator() {
        log.info("User id generated:" + userId);
        return userId++;
    }
}