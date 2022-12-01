package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void addLike(int filmId, int userId) {
        validId(filmId, userId);
        final String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        log.debug("Добавляем лайк фильму с id = {} от пользователя с id = {}", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        validId(filmId, userId);
        final String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        log.debug("Удаляем лайк фильму с id = {} от пользователя с id = {}", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Integer getCountLikesByFilm(int filmId) {
        final String sqlQuery = "select count(user_id) as count from likes where film_id = ?";
        log.debug("Запрашиваем количество лайков к фильму с id = {}", filmId);
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlQuery, this::getLikesSum, filmId));
        } catch (NullPointerException e) {
            log.error("У фильма с id = {} лайки не найдены", filmId);
            throw new LikeNotFoundException(String.format("У фильма с id = %d лайки не найдены", filmId));
        }
    }

    private int getLikesSum(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("count");
    }

    private void validId(Integer filmId, Integer userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException(String.format("Некорректный id: %d", filmId));
        }
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", userId));
        }
    }
}
