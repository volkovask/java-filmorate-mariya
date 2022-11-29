package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    private static final LocalDate EARN_RELEASE_DATE = LocalDate.of(1895, 12, 18);
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaDbStorage mpaDbStorage;

    @Override
    public Film create(Film film) {
        validation(film);
        film.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        final Integer filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        log.debug("Присвоен id = {}", filmId);
        film.setId(filmId);
        log.debug("Добавлен фильм {}", film.getName());
        addFilmGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());
        validation(film);
        film.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()));
        final String sqlQuery = "update films set " +
                "film_name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.debug("Фильм с id {} обновлен", film.getId());
        deleteFilmGenre(film);
        addFilmGenre(film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (id <= 0) {
            log.error("Некорректное значение id = {}", id);
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        try {
            final String sql = "select * from films f left join mpa m on m.mpa_id = f.mpa_id where film_id = ?";
            return jdbcTemplate.queryForObject(sql, this::makeFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Фильм с id = {}", id);
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден", id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sqlQuery = "select * from films f " +
                "left join mpa m on f.mpa_id = m.mpa_id";
        log.debug("Получаем все фильмы");
        try {
            return jdbcTemplate.query(sqlQuery, this::makeFilm);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("В базе данных нет фильмов");
        }
    }

    private void addFilmGenre(Film film) {
        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres.isEmpty()) {
            log.debug("У фильма {} нет жанров", film.getName());
        } else {
            for (Genre genre : filmGenres) {
                filmGenreStorage.addFilmGenre(film.getId(), genre.getId());
            }
            log.debug("Фильму с id = {} добавлены жанры", film.getId());
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        final String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        log.debug("Добавляем лайк фильму с id = {} от пользователя с id = {}", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        final String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        log.debug("Удаляем лайк фильму с id = {} от пользователя с id = {}", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public int getAllLikes(int filmId) {
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
    public Collection<Film> getPopularFilms(int count) {
        final String sqlQuery = "select f.film_id, f.film_name, f.description, f.release_date, " +
                "f.duration, f.mpa_id, m.mpa_name from films f " +
                "left join mpa m on m.mpa_id = f.mpa_id " +
                "left join likes l on l.film_id = f.film_id " +
                "group by f.film_id " +
                "order by count(l.film_id) desc limit ?";
        log.debug("Запрашиваем наиболее популярные фильмы из БД в количестве {}.", count);
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    private int getLikesSum(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("count");
    }

    private void deleteFilmGenre(Film film) {
        filmGenreStorage.deleteFilmGenres(film.getId());
        log.debug("У фильма {} удалены жанры", film.getName());
    }

    private Film validation(Film film) {
        if (film.getReleaseDate().isBefore(EARN_RELEASE_DATE)) {
            log.error("Некорректная дата релиза фильма");
            throw new ValidationException("Некорректная дата релиза фильма");
        }
        return film;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(filmGenreStorage.getFilmGenres(rs.getInt("film_id")))
                .likes(Collections.singleton(getAllLikes(rs.getInt("film_id"))))
                .build();
    }
}
