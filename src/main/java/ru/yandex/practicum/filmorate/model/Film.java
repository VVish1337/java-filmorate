package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Film{
    private long id;
    @NotEmpty(message = "{Name is empty}")
    private String name;
    @Size(max = 200, message = "{Description size too long.Max size 200 symbols.}")
    private String description;
    private Date releaseDate;
    @Positive(message = "{Duration must be positive}")
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}