package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    final GenreStorage genreDbStorage;

    @Test
    void getAllGenres() {
        final Collection<Genre> genres = genreDbStorage.getAllGenres();
        final int expectedSize = 6;
        assertThat(genres)
                .hasSize(expectedSize);
    }

    @Test
    void getGenreByIdTest() {
        final int genreId = 3;
        final String genreName = "Мультфильм";
        final Optional<Genre> optionalGenre = Optional.ofNullable(genreDbStorage.getGenreById(genreId));
        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", genreId))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", genreName));
    }
}
