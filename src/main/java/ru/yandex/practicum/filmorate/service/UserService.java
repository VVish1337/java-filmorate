package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDbStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> addFriend(long userId, long friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).getFriends().add(friendId);
            getUserById(friendId).getFriends().add(userId);
            return userStorage.getUserList();
        } else {
            throw new NotFoundException("User id:" + userId + " or friend id:" + friendId + " not found");
        }
    }

    public List<User> deleteFriend(long userId, long friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).getFriends().remove(friendId);
            getUserById(friendId).getFriends().remove(userId);
            return userStorage.getUserList();
        } else {
            throw new NotFoundException("User id:" + userId + " or friend id:" + friendId + " not found");
        }
    }

    public List<User> getUserFriends(long userId) {
        if (getUserById(userId) != null) {
            return getUserById(userId).getFriends().stream()
                    .map(this::getUserById)
                    .collect(Collectors.toCollection(ArrayList<User>::new));
        } else {
            throw new NotFoundException("User with this id not found" + userId);
        }
    }

    public List<User> getUserCommonFriends(long userId, long otherId) {
        if (getUserById(userId) != null && getUserById(otherId) != null) {
            Set<Long> commonIds = new HashSet<>(getUserById(userId).getFriends());
            return commonIds.stream()
                    .distinct()
                    .filter(getUserById(otherId).getFriends()::contains)
                    .map(this::getUserById)
                    .collect(Collectors.toCollection(ArrayList<User>::new));
        } else {
            throw new NotFoundException("User id:" + userId + " or other id:" + otherId + " not found");
        }
    }
}