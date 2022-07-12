package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Email(message = "{Email is not valid}")
    @NotEmpty(message = "{Email is empty}")
    private String email;
    @NotEmpty(message = "{Login is empty}")
    private String login;
    private String name;
    @Past(message = "{Birthday can't be in future}")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}