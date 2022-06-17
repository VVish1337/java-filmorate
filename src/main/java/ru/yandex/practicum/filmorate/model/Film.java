package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Film {

    @NotEmpty(message = "{Name is empty}")
    private final String name;
    @Size(max = 200, message = "{Description size too long.Max size 200 symbols.}")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "{Duration must be positive}")
    private final int duration;
    private final Set<Long> likes;
    private long id;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public Set<Long> getLikes() {
        return likes;
    }
}