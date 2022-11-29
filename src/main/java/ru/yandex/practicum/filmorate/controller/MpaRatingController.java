package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @GetMapping
    public Collection<Mpa> getAllMpaRating() {
        return mpaRatingService.getAllMpaRating();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpaRatingById(@PathVariable Integer mpaId) {
        return mpaRatingService.getMpaRatingById(mpaId);
    }
}
