package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Film implements Comparable<Film> {
    private long id;
    @NotEmpty(message = "{Name is empty}")
    private String name;
    @Size(max = 200, message = "{Description size too long.Max size 200 symbols.}")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "{Duration must be positive}")
    private int duration;
    private final Set<Long> likes=new HashSet<>();
    private MPA mpa;

    @Override
    public int compareTo(Film o) {
        return -1*(likes.size() - o.likes.size());
    }
}