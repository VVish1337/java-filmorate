package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUserList();

    User create(User user);

    User update(User user);

    User getUserById(long userId);
}