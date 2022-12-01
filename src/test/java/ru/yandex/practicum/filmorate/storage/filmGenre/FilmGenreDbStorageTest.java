package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmGenreDbStorageTest {
    private final FilmGenreStorage filmGenreDbStorage;

    @Test
    @Order(1)
    void getFilmGenresTest() {
        final int filmId = 1; //у фильма 2 жанра в data.sql
        final Set<Genre> filmGenres = filmGenreDbStorage.getFilmGenres(filmId);
        final int expectedSize = 2;
        assertThat(filmGenres)
                .hasSize(expectedSize);

    }

    @Test
    @Order(2)
    void addFilmGenreTest() {
        final int filmId = 2;
        final Set<Genre> filmGenres = filmGenreDbStorage.getFilmGenres(filmId);
        assertThat(filmGenres)
                .isEmpty();
        final Set<Integer> newGenreSet = new HashSet<>(Set.of(1, 2, 5, 6));
        final int expectedSize = newGenreSet.size();
        for(Integer genreId : newGenreSet) {
            filmGenreDbStorage.addFilmGenre(filmId, genreId);
        }
        final Set<Genre> actualFilmGenre = filmGenreDbStorage.getFilmGenres(filmId);
        assertThat(actualFilmGenre)
                .hasSize(expectedSize);
    }

    @Test
    @Order(3)
    void deleteFilmGenresTest() {
        final int filmId = 2;
        final Set<Genre> filmGenres = filmGenreDbStorage.getFilmGenres(filmId);
        final int genreSize = 4; //были добавлены в предыдущем тесте
        assertThat(filmGenres)
                .hasSize(genreSize);
        filmGenreDbStorage.deleteFilmGenres(filmId);
        final Set<Genre> actualFilmGenres = filmGenreDbStorage.getFilmGenres(filmId);
        assertThat(actualFilmGenres)
                .isEmpty();
    }
}
