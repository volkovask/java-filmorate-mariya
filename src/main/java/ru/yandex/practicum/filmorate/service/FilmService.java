package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms());
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void createLike(Integer filmId, Integer userId) {
        validId(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        validId(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new FilmNotFoundException(String.format("Некорректный параметр count: %d", count));
        }
        return filmStorage.getPopularFilms(count);
    }

    private void validId(Integer filmId, Integer userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException(String.format("Некорректный id: %d", filmId));
        }
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", userId));
        }
    }
}
