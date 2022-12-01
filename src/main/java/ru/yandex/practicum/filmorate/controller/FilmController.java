package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void createLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        filmService.createLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}
