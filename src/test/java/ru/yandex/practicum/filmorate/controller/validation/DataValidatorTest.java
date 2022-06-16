package ru.yandex.practicum.filmorate.controller.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataValidatorTest {
//
//    private static UserController userController;
//    private static FilmController filmController;
//    private static Validator validator;
//    private final String LONG_DESCRIPTION = "Lorem ipsum dolor sit amet, " +
//            "consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
//            "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, " +
//            "nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, " +
//            "sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, " +
//            "vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. " +
//            "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum s" +
//            "emper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, " +
//            "eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. " +
//            "Phasellus viverra nulla ut metus varius laoreet. " +
//            "Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel " +
//            "augue. Curabitur ullamcorper ultricies nisi. " +
//            "Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero" +
//            "sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. " +
//            "Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. " +
//            "Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. " +
//            "Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,wwww";
//
//    @BeforeAll
//    static void initializeControllers() {
//        userController = new UserController();
//        filmController = new FilmController();
//        validator = Validation.buildDefaultValidatorFactory().getValidator();
//    }
//
//    @Test
//    public void validationUserSizeEquals() {
//        User user = new User(2, "123ail.ru", "", "antada",
//                LocalDate.of(2024, 11, 1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertEquals(3, violations.size());
//    }
//
//    @Test
//    public void validationFilmSizeEquals() {
//        Film film = new Film(1, "", LONG_DESCRIPTION,
//                LocalDate.of(2011, 2, 2), -10);
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        assertEquals(3, violations.size());
//    }
//
//    @Test
//    public void validateFilmThrowsException() {
//        ValidationException x = assertThrows(ValidationException.class, () ->
//                filmController.create(new Film(1, "ssr", "sss www ss",
//                        LocalDate.of(1111, 2, 2), 40)));
//        assertEquals(HttpStatus.BAD_REQUEST, x.getStatus());
//    }
//
//    @Test
//    public void validateUserThrowsException() {
//        ValidationException x = assertThrows(ValidationException.class, () -> userController.create(
//                new User(1, "123@mail.ru", "ant aa", "ant",
//                        LocalDate.of(2010, 11, 1))));
//        assertEquals(HttpStatus.BAD_REQUEST, x.getStatus());
//    }
}