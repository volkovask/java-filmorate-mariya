package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private static final String MPA_ID = "mpa_id";
    private static final String MPA_NAME = "mpa_name";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAllMpa() {
        final String sqlQuery = "select * from mpa order by mpa_id";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        try {
            final String sqlQuery = "select * from mpa where mpa_id = ?";
            log.debug("Запрашиваем mpa с id = {}", mpaId);
            return jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            log.error("MPA rating с id = {} не найден", mpaId);
            throw new MpaNotFoundException(String.format("MPA rating с id = %s не найден", mpaId));
        }
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt(MPA_ID))
                .name(rs.getString(MPA_NAME))
                .build();
    }
}
