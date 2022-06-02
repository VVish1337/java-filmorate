package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private final FilmController filmController = new FilmController();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void createThrowsException() {
        ValidationException x = assertThrows(ValidationException.class, () ->
                filmController.create(new Film(1, "ssr", "sss www ss",
                        LocalDate.of(1111, 2, 2), 40)));
        assertEquals("Date release is Before than 12.28.1895", x.getMessage());
    }

    @Test
    public void updateThrowsException() {
        filmController.create(new Film(1, "ssr", "sss www ss",
                LocalDate.of(2011, 2, 2), 40));
        ValidationException x = assertThrows(ValidationException.class, () ->
                filmController.update(new Film(13, "ssr", "sss www ss",
                        LocalDate.of(2011, 2, 2), 40)));
        assertEquals("Update error.Film with this id not found", x.getMessage());
    }

    @Test
    public void createAssert() {
        Film film = new Film(3, "ssr", "sss www ss",
                LocalDate.of(2011, 2, 2), 40);
        filmController.create(film);
        List<Film> films = new ArrayList<>();
        films.add(film);
        assertEquals(films.size(), filmController.getAll().size());
        assertEquals(films, filmController.getAll());
    }

    @Test
    public void updateAssert() {
        Film film = new Film(1, "ssr", "sss www ss",
                LocalDate.of(2011, 2, 2), 40);
        Film filmUpd = new Film(1, "ssr(upd)", "sss www ss",
                LocalDate.of(2011, 2, 2), 40);
        filmController.create(film);
        List<Film> films = new ArrayList<>();
        films.add(filmUpd);
        filmController.update(filmUpd);
        assertEquals(films.size(), filmController.getAll().size());
        assertEquals(films, filmController.getAll());
    }

    @Test
    public void validationAssertSizeEquals() {
        String description = "Lorem ipsum dolor sit amet, " +
                "consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, " +
                "nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, " +
                "sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, " +
                "vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. " +
                "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum s" +
                "emper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, " +
                "eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. " +
                "Phasellus viverra nulla ut metus varius laoreet. " +
                "Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel " +
                "augue. Curabitur ullamcorper ultricies nisi. " +
                "Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero" +
                "sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. " +
                "Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. " +
                "Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. " +
                "Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,wwww";
        Film film = new Film(1, "", description,
                LocalDate.of(2011, 2, 2), -10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(3, violations.size());
    }
}