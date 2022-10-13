package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate EARN_RELEASE_DATE = LocalDate.of(1895, 12, 18);
    private static final Integer MAX_DESCRIPTION_LENGTH = 200;
    private final FilmIdGenerator filmIdGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(FilmIdGenerator filmIdGenerator) {
        this.filmIdGenerator = filmIdGenerator;
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    @Override
    public Film create(Film film) {
        save(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("id не может быть пустым");
        }
        if (film.getId() < 0) {
            throw new ValidationException("id должен быть больше нуля");
        }
        validation(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (id == null) {
            throw new ValidationException("id не может быть пустым");
        }
        if (id <= 0) {
            throw new ValidationException("id должн быть больше нуля");
        }
        if (!films.containsKey(id)) {
            throw new UserNotFoundException(String.format("Фильм с id %d не найден", id));
        }
        return films.get(id);
    }

    private void save(Film film) {
        validation(film);
        film.setId(filmIdGenerator.generate());
        films.put(film.getId(), film);
    }

    private Film validation(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Длина описания составляет: " + film.getDescription().length() +
                    ". Максимальная длина описания: " + MAX_DESCRIPTION_LENGTH + " символов.");
        }
        if (film.getReleaseDate().isBefore(EARN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза должна быть позже " + EARN_RELEASE_DATE);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность должна быть больше нуля");
        }
        return film;
    }
}
