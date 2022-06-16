package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;



@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable long userId){
        return userService.getById(userId);
    }

    @PutMapping("{userId}/friends/common/{friendId}")
    public List<User> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/common/{friendId}")
    public List<User> deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public List<User> getUserFriends(@PathVariable long userId){
        return userService.getUserFriends(userId);
    }

    @GetMapping("{userId}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable long userId,
                                           @PathVariable long otherId){
        return userService.getUserCommonFriends(userId,otherId);
    }
}