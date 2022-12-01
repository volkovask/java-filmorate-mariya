package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaRatingService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAllMpaRating() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaRatingById(Integer mpaRatingId) {
        return mpaStorage.getMpaById(mpaRatingId);
    }
}
