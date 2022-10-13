package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;

@Component
public class FilmIdGenerator {
    private Integer id;

    public FilmIdGenerator() {
        this.id = 0;
    }

    public Integer generate() {
        return ++id;
    }
}
