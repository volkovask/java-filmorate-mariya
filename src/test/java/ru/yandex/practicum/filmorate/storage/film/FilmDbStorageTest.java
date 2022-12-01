package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private static final String FILM_NAME = "Константин";
    private static final String CORRECTED_FILM_DESCRIPTION = "Описание фильма Константин";
    private static final String UNCORRECTED_FILM_DESCRIPTION = "Некорректное опиисание фильма Константин";
    private static final LocalDate FILM_RELEASE_DATE = LocalDate.of(2005, 2, 7);
    private static final Integer FILM_DURATION = 121;
    private static final Integer R_ID = 4;
    private static final String R_NAME = "R";

    private final FilmStorage filmDbStorage;

    @Test
    @Order(1)
    void testCreate() {
        final Film newFilm = Film.builder()
                .name(FILM_NAME)
                .description(UNCORRECTED_FILM_DESCRIPTION)
                .releaseDate(FILM_RELEASE_DATE)
                .duration(FILM_DURATION)
                .mpa(new Mpa(R_ID, R_NAME))
                .genres(new HashSet<>())
                .build();

        final Optional<Film> optionalFilm = Optional.ofNullable(filmDbStorage.create(newFilm));
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", FILM_NAME))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("description", UNCORRECTED_FILM_DESCRIPTION));
    }

    @Order(2)
    @Test
    void testUpdate() {
        final Film filmForUpdate = filmDbStorage.getFilmById(5);
        assertThat(filmForUpdate)
                .hasFieldOrPropertyWithValue("description", UNCORRECTED_FILM_DESCRIPTION);
        filmForUpdate.setDescription(CORRECTED_FILM_DESCRIPTION);
        final Film updateFilm = filmDbStorage.update(filmForUpdate);
        assertThat(updateFilm)
                .hasFieldOrPropertyWithValue("description", CORRECTED_FILM_DESCRIPTION);
    }

    @Test
    @Order(3)
    void testGetFilmById() {
        final Optional<Film> optionalFilm = Optional.ofNullable(filmDbStorage.getFilmById(2));
        final int expId = 2;
        final String expName = "Красный воробей";
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", expId))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", expName));
    }

    @Test
    @Order(4)
    void testGetAllFilms() {
        final Collection<Film> films = filmDbStorage.getAllFilms();
        final Integer expCountFilm = 5;
        assertThat(films)
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    @Order(5)
    void testGetPopularFilms() {
        final int lengthTopList = 3;
        final String firstFilm = "Престиж";
        final String secondFilm = "11 друзей Оушена";
        final String thirdFilm = "Красный воробей";

        final List<Film> popularList = new ArrayList<>(filmDbStorage.getPopularFilms(lengthTopList));
        final int lengthTopListAct = popularList.size();
        final String firstFilmAct = popularList.get(0).getName();
        final String secondFilmAct = popularList.get(1).getName();
        final String thirdFilmAct = popularList.get(2).getName();

        AssertionsForClassTypes.assertThat(lengthTopListAct)
                .isEqualTo(lengthTopList);
        AssertionsForClassTypes.assertThat(firstFilmAct)
                .isEqualTo(firstFilm);
        AssertionsForClassTypes.assertThat(secondFilmAct)
                .isEqualTo(secondFilm);
        AssertionsForClassTypes.assertThat(thirdFilmAct)
                .isEqualTo(thirdFilm);
    }
}
