package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaStorage mpaDbStorage;

    @Test
    void getAllMpa() {
        final Collection<Mpa> mpas = mpaDbStorage.getAllMpa();
        final int expectedSize = 5;
        assertThat(mpas)
                .hasSize(expectedSize);
    }

    @Test
    void getMpaById() {
        final int mpaId = 2;
        final String mpaName = "PG";
        final Optional<Mpa> optionalMpa = Optional.ofNullable(mpaDbStorage.getMpaById(mpaId));
        assertThat(optionalMpa)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", mpaId))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", mpaName));
    }
}
