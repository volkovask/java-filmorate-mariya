# java-filmorate
Template repository for Filmorate project.

1. Получить список названий всех фильмов
SELECT film_name
FROM films

2. Получить список всех пользователей
SELECT * 
FROM users

3. Получить топ-10 популярных фильмов
SELECT f.film_name
FROM films AS f
JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_name
ORDER BY COUNT(l.user_id) DESK
LIMIT (10)

4. Получить список друзей пользователя с id = 2
SELECT u.user_email,
u2.user_email
FROM users AS u
JOIN friends AS f ON u.user_id = f.user_id
JOIN users AS u2 ON f.friend_id = u2.user_id
WHERE u.user_id = 2
ORDER BY u2.user_email DESC
LIMIT (10)
