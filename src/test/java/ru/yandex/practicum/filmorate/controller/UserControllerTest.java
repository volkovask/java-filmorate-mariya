package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void validateLogin() {
        final UserController userController = new UserController();
        final User user = new User("email@ya.ru", "", LocalDate.of(1995, 10, 25));
        assertThrows(ValidationException.class, () -> userController.save(user), "No exception");

        final User user2 = new User("email@ya.ru", "Login 12", LocalDate.of(1995, 10, 25));
        assertThrows(ValidationException.class, () -> userController.save(user), "No exception");
    }

    @Test
    void validateName() {
        final UserController userController = new UserController();
        final User user = new User("email@ya.ru", "login12", LocalDate.of(1995, 10, 25));
        userController.save(user);
        assertTrue(user.getName().contains(user.getLogin()), "Имя не подставилось");
    }
}