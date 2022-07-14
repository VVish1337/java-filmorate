package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.sql.Date;




@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    @Email(message = "{Email is not valid}")
    @NotEmpty(message = "{Email is empty}")
    private String email;
    private String name;
    @NotEmpty(message = "{Login is empty}")
    private String login;
    @Past(message = "{Birthday can't be in future}")
    private Date birthday;
    //private final Set<Long> friends = new HashSet<>();
}