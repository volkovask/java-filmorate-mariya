package ru.yandex.practicum.filmorate.storage.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class FriendDbStorage implements FriendStorage {
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_LOGIN = "user_login";
    private static final String USER_NAME = "user_name";
    private static final String USER_BIRTHDAY = "birthday";
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriend(Integer userId, Integer friendId) {
        validation(userId);
        validation(friendId);
        final String sqlQuery = "insert into friends (user_id, friend_id) values(?, ?)";
        log.debug("Добавляем пользователю с id = {} друга с id = {}", userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        validation(userId);
        validation(friendId);
        final String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        log.debug("Удаляем у пользователя с id = {} друга с id = {}", userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        validation(userId);
        final String sqlQuery = "select * from users u left join friends f on u.user_id = f.friend_id " +
                "where f.user_id = ?";
        log.debug("Запрашиваем друзей пользователя с id = {}", userId);
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        validation(userId);
        validation(friendId);
        final String sqlQuery = "select * from users where user_id = " +
                "(select friend_id from friends where user_id = ? intersect " +
                "select friend_id from friends where user_id = ?)";
        log.debug("Запрашиваем общих друзей пользователей с id = {} и id = {}", userId, friendId);
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId, friendId);
    }

    private void validation(Integer userId) {
        if (userId <= 0) {
            log.error("Указан некорректный id: {}", userId);
            throw new UserNotFoundException(String.format("Некорректный id: %d", userId));
        }
        try {
            userStorage.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt(USER_ID))
                .email(rs.getString(USER_EMAIL))
                .login(rs.getString(USER_LOGIN))
                .name(rs.getString(USER_NAME))
                .birthday(rs.getDate(USER_BIRTHDAY).toLocalDate())
                .build();
    }
}
