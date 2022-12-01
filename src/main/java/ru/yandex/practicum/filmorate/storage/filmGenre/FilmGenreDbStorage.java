package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private static final String GENRE_ID = "genre_id";
    private static final String GENRE_NAME = "genre_name";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        final String sqlQuery = "insert into film_genre (film_id, genre_id) values(?, ?)";
        log.debug("Добавляем жанры для фильма с id = {}", filmId);
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        final String sqlQuery = "delete from film_genre where film_id = ?";
        log.debug("Удаляем жанры у фильма с id = {}", filmId);
        try {
            jdbcTemplate.update(sqlQuery, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("У фильма нет жанров");
        }
    }

    @Override
    public Set<Genre> getFilmGenres(int filmId) {
        final String sqlQuery = "select g.genre_id, g.genre_name from film_genre fg " +
                "left join genres g on fg.genre_id = g.genre_id " +
                "where film_id = ?";
        log.debug("Получаем жанры для фильма с id = {}", filmId);
        return jdbcTemplate.query(sqlQuery, this::makeGenreSet, filmId);
    }

    private Set<Genre> makeGenreSet(ResultSet rs) throws SQLException {
        Set<Genre> genreSet = new HashSet<>();
        while (rs.next()) {
            genreSet.add(new Genre(rs.getInt(GENRE_ID), rs.getString(GENRE_NAME)));
        }
        return genreSet;
    }
}
