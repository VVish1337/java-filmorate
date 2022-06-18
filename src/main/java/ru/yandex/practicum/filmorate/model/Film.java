package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@Getter
public class Film implements Comparable<Film> {
    @NotEmpty(message = "{Name is empty}")
    private final String name;
    @Size(max = 200, message = "{Description size too long.Max size 200 symbols.}")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "{Duration must be positive}")
    private final int duration;
    private final Set<Long> likes=new HashSet<>();
    @Setter
    private long id;

    @Override
    public int compareTo(Film o) {
        return -1*(likes.size() - o.likes.size());
    }
}