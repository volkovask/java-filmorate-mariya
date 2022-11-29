package ru.yandex.practicum.filmorate.storage.filmGenre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenreStorage {
    void addFilmGenre(int filmId, int genreId);
    void deleteFilmGenres(int filmId);
    Set<Genre> getFilmGenres(int filmId);
}
