package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@AllArgsConstructor
@Getter
public class User {
    @Setter
    private int id;
    @Email(message = "{Email is not valid}")
    @NotEmpty(message = "{Email is empty}")
    private String email;
    @NotEmpty(message = "{Login is empty}")
    private String login;
    @Setter
    private String name;
    @Past(message = "{Birthday can't be in future}")
    private LocalDate birthday;
}