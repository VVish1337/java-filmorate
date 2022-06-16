package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        if (userStorage.getUsersMap().containsKey(userId) && userStorage.getUsersMap().containsKey(friendId)) {
            userStorage.getUsersMap().get(userId).getFriends().add(friendId);
            userStorage.getUsersMap().get(friendId).getFriends().add(userId);
            return userStorage.getAll();
        } else {
            throw new NotFoundException("sss");
        }
    }

    public List<User> deleteFriend(long userId, long friendId) {
        if (userStorage.getUsersMap().containsKey(userId) && userStorage.getUsersMap().containsKey(friendId)) {
            userStorage.getUsersMap().get(userId).getFriends().remove(friendId);
            userStorage.getUsersMap().get(friendId).getFriends().remove(userId);
            return userStorage.getAll();
        } else {
            throw new NotFoundException("");
        }
    }

    public List<User> getUserFriends(long userId) {
        if (userStorage.getUsersMap().containsKey(userId)) {
            List<User> users = new ArrayList<>();
            for (Long id : userStorage.getUsersMap().get(userId).getFriends()) {
                users.add(userStorage.getUsersMap().get(id));
            }
            return users;
        } else {
            throw new NotFoundException("User with this id not found"+userId);
        }
    }

    public List<User> getUserCommonFriends(long userId, long otherId) {
        if (userStorage.getUsersMap().containsKey(userId) && userStorage.getUsersMap().containsKey(otherId)
                && userStorage.getUsersMap().get(userId).getFriends().size() > 0
                && userStorage.getUsersMap().get(otherId).getFriends().size() > 0
        ) {
            Set<Long> commonIds = null;
            commonIds.addAll(userStorage.getUsersMap().get(userId).getFriends());
            commonIds.addAll(userStorage.getUsersMap().get(otherId).getFriends());
            List<User> commonUsersMap = new ArrayList<>();
            for (Long id : commonIds) {
                commonUsersMap.add(userStorage.getUsersMap().get(id));
            }
            return commonUsersMap;
        } else {
            throw new NotFoundException("");
        }
    }
}
