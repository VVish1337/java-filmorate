package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private static long userId = 1;
    @Getter
    private final HashMap<Long, User> usersMap = new HashMap<>();
    private final DataValidator objectValidator = new DataValidator();
    private final List<User> friends = new ArrayList<>();

    @Override
    public List<User> getAll() {
        log.info("Get user list");
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User create(User user) {
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

    @Override
    public User update(User user) {
        objectValidator.validateUser(user);
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
        } else {
            throw new ValidationException("Update error.User id incorrect.");
        }
        return user;
    }

    private long userIdGenerator() {
        log.info("User id generated:" + userId);
        return userId++;
    }
}
