package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.validation.DataValidator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userStorage;
    private final DataValidator dataValidator;

    @Autowired
    public UserService(UserDbStorage userStorage, DataValidator dataValidator) {
        this.userStorage = userStorage;
        this.dataValidator = dataValidator;
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User create(User user) {
        dataValidator.validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUserExists(user.getId());
        dataValidator.validateUser(user);
        return userStorage.update(user);
    }

    public User getUserById(long userId) {
//        checkUserExists(userId);
        return userStorage.getUserById(userId);
    }

    public void addFriend(long userId,long friendId){
        checkUserExists(userId);
        checkUserExists(friendId);
        userStorage.addFriend(userId,friendId);
    }

    public void deleteFriend(long userId,long friendId)
    {
        checkUserExists(userId);
        checkUserExists(friendId);
        userStorage.deleteFriend(userId,friendId);
    }

    public List<User> getUserFriends(long userId) {
        checkUserExists(userId);
        return userStorage.getUserFriends(userId);
    }

    public List<User> getUserCommonFriends(long userId, long otherId) {
        Set<User> userFriends = new HashSet<>(getUserFriends(userId));
        Set<User> otherUserFriends = new HashSet<>(getUserFriends(otherId));
        userFriends.retainAll(otherUserFriends);
        log.info("sss"+userFriends);
            return new ArrayList<>(userFriends);
            //TODO fix common friends
    }

    private void checkUserExists(Long userId) {
        userStorage.getUserById(userId);
    }
}