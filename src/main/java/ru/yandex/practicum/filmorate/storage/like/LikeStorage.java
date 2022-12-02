package ru.yandex.practicum.filmorate.storage.like;

import java.util.Collection;

public interface LikeStorage {
        void addLike(int filmId, int userId);
        void deleteLike(int filmId, int userId);
        Integer getCountLikesByFilm(int filmId);

    Collection<Integer> getFilmRecommendation(Integer id);
}
