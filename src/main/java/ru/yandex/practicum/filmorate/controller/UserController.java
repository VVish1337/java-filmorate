package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends FilmorateController<User> {

    private final HashMap<Integer, User> usersMap = new HashMap<>();
    private static int userId = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> getAll() {
        log.info("Get user list");
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        user.setId(userId);
        usersMap.put(userIdGenerator(), dataValidator(user));
        log.info("User created:"+user.getName());
        return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        if(usersMap.get(user.getId())!=null){
            usersMap.put(user.getId(), dataValidator(user));
        }else {
            throw new  ValidationException("Update error.User id incorrect.");
        }
        return user;
    }

    @Override
    public User dataValidator(User user){
        if(user.getLogin().contains(" ")){
            log.error("Login contains whitespaces");
            throw new ValidationException("Login contains whitespaces");
        }
        if(user.getName().isBlank()){
            user.setName(user.getLogin());
            log.info("User name was empty.User name was set by login:"+user.getName());
        }
        return user;
    }

    private int userIdGenerator(){
        return userId++;
    }
}