package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getById(long userId) {
        if (userStorage.getUsersMap().containsKey(userId)) {
            return userStorage.getUsersMap().get(userId);
        } else {
            throw new NotFoundException("sss");
        }
    }

    public List<User> addFriend(long userId, long friendId) {
        if (getById(userId) != null && getById(friendId) != null) {
            getById(userId).getFriends().add(friendId);
            getById(friendId).getFriends().add(userId);
            return userStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or friend id:" + friendId + " not found");
        }
    }

    public List<User> deleteFriend(long userId, long friendId) {
        if (getById(userId) != null && getById(friendId) != null) {
            getById(userId).getFriends().remove(friendId);
            getById(friendId).getFriends().remove(userId);
            return userStorage.getAll();
        } else {
            throw new NotFoundException("User id:" + userId + " or friend id:" + friendId + " not found");
        }
    }

    public List<User> getUserFriends(long userId) {
        if (getById(userId) != null) {
            return getById(userId).getFriends()
                    .stream()
                    .map(this::getById)
                    .collect(Collectors.toCollection(ArrayList<User>::new));
        } else {
            throw new NotFoundException("User with this id not found" + userId);
        }
    }

    public List<User> getUserCommonFriends(long userId, long otherId) {
        if (getById(userId) != null && getById(otherId) != null) {
            Set<Long> commonIds = new HashSet<>(getById(userId).getFriends());
            return commonIds.stream()
                    .distinct()
                    .filter(getById(otherId).getFriends()::contains)
                    .map(this::getById)
                    .collect(Collectors.toCollection(ArrayList<User>::new));
        } else {
            throw new NotFoundException("User id:" + userId + " or other id:" + otherId + " not found");
        }
    }
}