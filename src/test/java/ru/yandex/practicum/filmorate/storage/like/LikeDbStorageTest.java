package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikeDbStorageTest {
    final LikeStorage likeDbStorage;

    @Test
    @Order(1)
    void getCountLikesByFilmTest() {
        final int filmId = 3;
        final int actualLikes = likeDbStorage.getCountLikesByFilm(filmId);
        final int expectedLikes = 3;
        assertThat(actualLikes)
                .isEqualTo(expectedLikes);
    }

    @Order(2)
    @Test
    void addLikeTest() {
        final int filmId = 4; //у фильма нет лайков
        final int userId = 1;
        likeDbStorage.addLike(filmId, userId);
        final int actualLikes = likeDbStorage.getCountLikesByFilm(filmId);
        final int expectedLikes = 1;
        assertThat(actualLikes)
                .isEqualTo(expectedLikes);
    }

    @Test
    @Order(3)
    void deleteLikeTest() {
        final int filmId = 3;
        final int userId = 1;
        likeDbStorage.deleteLike(filmId, userId);
        final int actualLikes = likeDbStorage.getCountLikesByFilm(filmId);
        final int expectedLikes = 2;
        assertThat(actualLikes)
                .isEqualTo(expectedLikes);
    }
}
