package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private final UserController userController = new UserController();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void createThrowsException(){
        ValidationException x = assertThrows(ValidationException.class, () ->userController.create(
                new User(1,"123@mail.ru","ant aa","ant",
                        LocalDate.of(2010,11,1))));
        assertEquals("Login contains whitespaces",x.getMessage());
    }

    @Test
    public void updateThrowsException(){
        userController.create(new User(0,"123@mail.ru","antada","ant",
                LocalDate.of(2010,11,1)));
        ValidationException x = assertThrows(ValidationException.class, () ->userController.update(
                new User(2,"123@mail.ru","antaa","antada",
                        LocalDate.of(2010,11,1))));
        assertEquals("Update error.User id incorrect.",x.getMessage());
    }

    @Test
    public void createAssert(){
        User user =  new User(2,"123@mail.ru","antaa","antada",
                LocalDate.of(2010,11,1));
        userController.create(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        assertEquals(users.size(),userController.getAll().size());
        assertEquals(users,userController.getAll());
    }

    @Test
    public void updateAssert(){
        User user =  new User(1,"123@mail.ru","antaa","antada",
                LocalDate.of(2010,11,1));
        User userUpd =  new User(1,"123@mail.ru","antaa(upd)","antada",
                LocalDate.of(2010,11,1));
        userController.create(user);
        List<User> users = new ArrayList<>();
        users.add(userUpd);
        userController.update(userUpd);
        assertEquals(users.size(),userController.getAll().size());
        assertEquals(users,userController.getAll());
    }

    @Test
    public void validationAssertSizeEquals(){
        User user =  new User(2,"123ail.ru","","antada",
                LocalDate.of(2024,11,1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3,violations.size());
    }
}