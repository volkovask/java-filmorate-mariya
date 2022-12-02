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
import java.util.Collection;
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

    @Override
    public Collection<Integer> getFilmRecommendation(Integer id) {
        String sqlQuery = "select similar_user_likes.film_id from " +
                "likes similar_user_likes join " +
                "(select similar_user.user_id " +
                "from likes " +
                "join likes similar_user on likes.film_id = similar_user.film_id " +
                "and likes.user_id <> similar_user.user_id " +
                "where likes.user_id = ? " +
                "group by likes.user_id, similar_user.user_id " +
                "order by COUNT(likes.film_id) desc " +
                "limit 1) similar_user on similar_user.user_id = similar_user_likes.user_id " +
                "left join likes l2 on similar_user_likes.film_id = l2.film_id and l2.user_id = ? " +
                "where l2.film_id is null";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, id, id);
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
