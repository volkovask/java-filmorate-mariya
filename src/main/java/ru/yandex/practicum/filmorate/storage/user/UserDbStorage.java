package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        validation(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        final Integer userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(userId);
        log.debug("Добавлен новый пользователь с id = {}", userId);
        return user;
    }

    @Override
    public User update(User user) {
        try {
            getUserById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь с id = {} не найден и не может быть обновлен", user.getId());
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден и не может быть обновлен",
                    user.getId()));
        }
        final String sqlQuery = "update users set " +
                "user_email = ?, " +
                "user_login = ?, " +
                "user_name = ?, " +
                "birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("Пользователь с id = {} обновлен", user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId <= 0) {
            log.error("Передан некорректный userId = {}", userId);
            throw new UserNotFoundException(String.format("Передан некорректный userId = %s", userId));
        }
        final String sqlQuery = "select * from users where user_id = ?";
        log.debug("Запрашиваем данные пользователя с id = {} из базы данных", userId);
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        final String sqlQuery = "select * from users";
        log.debug("Запрашиваем всех пользователей");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    private void validation(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Логин \"{}\" не должен содержать пробелы", user.getLogin());
            throw new ValidationException(String.format("Логин \"%s\" н должен содержать пробелы", user.getLogin()));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
