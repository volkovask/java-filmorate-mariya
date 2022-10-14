package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms().values());
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (!filmStorage.getAllFilms().containsKey(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильм с id %d не найден и не может быть обновлен",
                    film.getId()));
        }
        return filmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void createLike(Integer filmId, Integer userId) {
        validId(filmId, userId);
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        validId(filmId, userId);
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        if (count <= 0) {
            throw new FilmNotFoundException(String.format("Некорректный параметр count: %d", count));
        }
        return filmStorage.getAllFilms().values().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validId(Integer filmId, Integer userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException(String.format("Некорректный id: %d", filmId));
        }
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", userId));
        }
        if (!filmStorage.getAllFilms().containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
    }
}
