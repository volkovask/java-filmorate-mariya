package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private static final String DESCRIPTION_NORMAL = "Scrat is a fictional character in the Ice Age franchise.";
    private static final String DESCRIPTION_OVER_200 = "Scrat is a fictional character in the Ice Age franchise. " +
            "He is a saber-toothed squirrel who is obsessed with collecting acorns, " +
            "constantly putting his life in danger to obtain and defend them. " +
            "Scrat's storylines are mostly independent of those of the Herd, though the two do intersect at times.";
    private static final LocalDate RELEASE_DATE_NORMAL = LocalDate.of(2005, 12, 18);
    private static final LocalDate RELEASE_DATE_NOT_NORMAL = LocalDate.MIN;

    @Test
    void validationName() {
        FilmController filmController = new FilmController();
        Film film = new Film("", DESCRIPTION_NORMAL, RELEASE_DATE_NORMAL, 120);
        assertThrows(ValidationException.class, () -> filmController.validation(film), "No exception");
    }

    @Test
    void validationDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film("name", DESCRIPTION_OVER_200, RELEASE_DATE_NORMAL, 120);
        assertThrows(ValidationException.class, () -> filmController.validation(film), "No exception");
    }

    @Test
    void validationReleaseDate() {
        FilmController filmController = new FilmController();
        Film film = new Film("name", DESCRIPTION_NORMAL, RELEASE_DATE_NOT_NORMAL, 120);
        assertThrows(ValidationException.class, () -> filmController.validation(film), "No exception");
    }

    @Test
    void validationDuration() {
        FilmController filmController = new FilmController();
        Film film = new Film("name", DESCRIPTION_NORMAL, RELEASE_DATE_NORMAL, -120);
        assertThrows(ValidationException.class, () -> filmController.validation(film), "No exception");
    }
}