package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j

public class InMemoryUserStorage {
//    private static long userId = 1;
//    @Getter
//    private final HashMap<Long, User> usersMap = new HashMap<>();
//    private final DataValidator objectValidator = new DataValidator();
//
//    @Override
//    public List<User> getUserList() {
//        log.info("Get user list");
//        return new ArrayList<>(usersMap.values());
//    }
//
//    @Override
//    public User create(User user) {
//        objectValidator.validateUser(user);
//        if (user.getName().isBlank()) {
//            user.setName(user.getLogin());
//            log.info("User name was empty.User name was set by login:" + user.getName());
//        }
//        user.setId(userId);
//        usersMap.put(userIdGenerator(), user);
//        log.info("User created:" + user.getName());
//        return user;
//    }
//
//    @Override
//    public User update(User user) {
//        objectValidator.validateUser(user);
//        if (usersMap.containsKey(user.getId())) {
//            usersMap.put(user.getId(), user);
//        } else {
//            throw new NotFoundException("Update error.User id incorrect.");
//        }
//        return user;
//    }
//
//    @Override
//    public User getUserById(long userId) {
//        if (usersMap.containsKey(userId)) {
//            return usersMap.get(userId);
//        } else {
//            throw new NotFoundException("User with this id not found" + userId);
//        }
//    }
//
//    private long userIdGenerator() {
//        log.info("User id generated:" + userId);
//        return userId++;
//    }
}