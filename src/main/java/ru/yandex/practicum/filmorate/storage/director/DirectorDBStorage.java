package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDBStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String TABLE_NAME_DIRECTORS = "directors";
    private static final String DIRECTOR_ID = "director_id";
    private static final String DIRECTOR_NAME = "director_name";

    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME_DIRECTORS)
                .usingGeneratedKeyColumns(DIRECTOR_ID);
        final Integer directorId = simpleJdbcInsert.executeAndReturnKey(director.toMap()).intValue();
        log.debug("Присвоен id = {}", directorId);
        director.setId(directorId);
        log.debug("Добавлен режисер {}", director.getName());

        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        try {
            getDirectorById(director.getId());
        } catch (EmptyResultDataAccessException e) {
            log.error("Режисер с id = {} не найден и не может быть обновлен", director.getId());
            throw new DirectorNotFoundException(String.format("Режисер с id = %s не найден и не может быть обновлен",
                    director.getId()));
        }
        System.out.println(director);
        final String sqlQuery = "update directors set " +
                "director_name = ? " +
                "where director_id = ?";
        jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId());
        log.debug("Режисер с id = {} обновлен", director.getId());
        return director;
    }

    @Override
    public Director getDirectorById(int id) {
        if (id <= 0) {
            log.error("Некорректное значение id = {}", id);
            throw new DirectorNotFoundException(String.format("Режисер с id %s не найден", id));
        }
        try {
            final String sql = "select * from directors where director_id = ?";
            return jdbcTemplate.queryForObject(sql, this::makeDirector, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Директор с id = {}", id);
            throw new DirectorNotFoundException(String.format("Режисер с id %s не найден", id));
        }
    }

    @Override
    public Collection<Director> getAll() {
        final String sqlQuery = "select * from directors";
        log.debug("Получаем список всех режисеров");
        try {
            return jdbcTemplate.query(sqlQuery, this::makeDirector);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("В базе данных нет режисеров");
        }
    }

    @Override
    public void deleteDirector(int directorId) {
        final String sqlQuery = "delete from directors where director_id = ?";
        log.debug("Удаляем режисера с id = {}", directorId);
        jdbcTemplate.update(sqlQuery, directorId);

    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt(DIRECTOR_ID))
                .name(rs.getString(DIRECTOR_NAME))
                .build();
    }
}
