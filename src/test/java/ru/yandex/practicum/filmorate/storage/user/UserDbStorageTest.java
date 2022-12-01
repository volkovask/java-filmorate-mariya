package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDbStorageTest {
    private final static String USER_EMAIL = "semenovamn@live.ru";
    private final static String USER_LOGIN = "Mariya_S";
    private final static String USER_NAME = "Мария Семенова";
    private final static String NEW_USER_NAME = "Mariya Semenova";
    private final static LocalDate USER_BIRTHDAY = LocalDate.of(1990, 12, 29);
    private final UserStorage userDbStorage;

    @Test
    @Order(1)
    void getAllUsersTest() {
        final int actualSize = userDbStorage.getAllUsers().size();
        final int expectedSize = 3;
        assertThat(actualSize)
                .isEqualTo(expectedSize);
    }

    @Test
    @Order(2)
    void createUserTest() {
        final int expectedId = 4; //3 пользователя уже добавлены через data.sql
        final User newUser = User.builder()
                .email(USER_EMAIL)
                .login(USER_LOGIN)
                .name(USER_NAME)
                .birthday(USER_BIRTHDAY)
                .build();
        final Optional<User> optionalUser = Optional.ofNullable(userDbStorage.createUser(newUser));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", USER_NAME))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedId));
    }

    @Test
    @Order(3)
    void updateUserTest() {
        User userForUpdate = userDbStorage.getUserById(4);
        assertThat(userForUpdate)
                .hasFieldOrPropertyWithValue("name", USER_NAME);
        userForUpdate.setName(NEW_USER_NAME);
        final User userAfterUpdate = userDbStorage.updateUser(userForUpdate);
        assertThat(userAfterUpdate)
                .hasFieldOrPropertyWithValue("name", NEW_USER_NAME);
    }

    @Test
    @Order(4)
    void getUserByIdTest() {
        final Optional<User> optionalUser = Optional.ofNullable(userDbStorage.getUserById(4));
        final int expectedId = 4;
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedId))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", USER_LOGIN));
    }
}
