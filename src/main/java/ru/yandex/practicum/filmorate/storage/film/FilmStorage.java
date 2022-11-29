package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film create(Film film);
    Film update(Film film);
    Film getFilmById(Integer id);
    Collection<Film> getAllFilms();
    void addLike(int filmId, int userId);
    public void deleteLike(int filmId, int userId);
    public int getAllLikes(int filmId);
    public Collection<Film> getPopularFilms(int count);
}