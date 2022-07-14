package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUserList();

    User create(User user);

    User update(User user);

    User getUserById(long userId);

    void addFriend(long userId,long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getUserFriends(long userId);

    List<User> getUserCommonFriends(long userId,long otherId);
}