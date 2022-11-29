package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getAllGenres() {
        final String sqlQuery = "select * from genres order by genre_id";
        log.debug("Запрашиваем все жанры из базы данных");
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getGenreById(Integer id) {
        if (id <= 0) {
            log.error("Некорректное значение id = {}", id);
            throw new GenreNotFoundException(String.format("Некорректное значение id = %s", id));
        }
        try {
            final String sqlQuery = "select * from genres where genre_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Жанр с id {} не найден", id);
            throw new GenreNotFoundException(String.format("Жанр с id %d не найден", id));
        }
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
