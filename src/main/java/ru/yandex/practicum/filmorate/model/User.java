package ru.yandex.practicum.filmorate.model;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User {
    private long id;
    @Email(message = "{Email is not valid}")
    @NotEmpty(message = "{Email is empty}")
    private final String email;
    @NotEmpty(message = "{Login is empty}")
    private String login;
    private String name;
    @Past(message = "{Birthday can't be in future}")
    private final LocalDate birthday;
    private Set<Long> friends;

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Set<Long> getFriends() {
        return friends;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}