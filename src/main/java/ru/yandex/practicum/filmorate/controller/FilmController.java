package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate EARN_RELEASE_DATE = LocalDate.of(1895, 12, 18);
    private static final Integer MAX_DESCRIPTION_LENGTH = 200;
    private static final Map<Integer, Film> films = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        save(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            log.error("id не может быть путстым");
            throw new ValidationException("id не может быть пустым");
        }
        if (film.getId() < 0) {
            log.error("id должен быть больше нуля");
            throw new ValidationException("id должен быть больше нуля");
        }
        validation(film);
        films.put(film.getId(), film);
        return film;
    }

    public void save(Film film) {
        validation(film);
        film.setId(idGenerator.generate());
        films.put(film.getId(), film);
    }

    private Film validation(Film film) {
        if (film.getName().isBlank()) {
            log.error("Имя не может быть пустым");
            throw new ValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Длина описания составляет: " + film.getDescription().length() +
                    ". Максимальная длина описания: " + MAX_DESCRIPTION_LENGTH + " символов.");
            throw new ValidationException("Длина описания составляет: " + film.getDescription().length() +
                    ". Максимальная длина описания: " + MAX_DESCRIPTION_LENGTH + " символов.");
        }
        if (film.getReleaseDate().isBefore(EARN_RELEASE_DATE)) {
            log.error("Дата релиза должна быть позже " + EARN_RELEASE_DATE);
            throw new ValidationException("Дата релиза должна быть позже " + EARN_RELEASE_DATE);
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность должна быть больше нуля");
            throw new ValidationException("Продолжительность должна быть больше нуля");
        }
        return film;
    }
}
