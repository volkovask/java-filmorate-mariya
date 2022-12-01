insert into genres (genre_name) select 'Комедия'        where not exists (select * from genres where genre_name = 'Комедия');
insert into genres (genre_name) select 'Драма'          where not exists (select * from genres where genre_name = 'Драма');
insert into genres (genre_name) select 'Мультфильм'     where not exists (select * from genres where genre_name = 'Мультфильм');
insert into genres (genre_name) select 'Триллер'        where not exists (select * from genres where genre_name = 'Триллер');
insert into genres (genre_name) select 'Документальный' where not exists (select * from genres where genre_name = 'Документальный');
insert into genres (genre_name) select 'Боевик'         where not exists (select * from genres where genre_name = 'Боевик');

insert into mpa (mpa_name) select 'G'     where not exists (select * from mpa where mpa_name = 'G');
insert into mpa (mpa_name) select 'PG'    where not exists (select * from mpa where mpa_name = 'PG');
insert into mpa (mpa_name) select 'PG-13' where not exists (select * from mpa where mpa_name = 'PG-13');
insert into mpa (mpa_name) select 'R'     where not exists (select * from mpa where mpa_name = 'R');
insert into mpa (mpa_name) select 'NC-17' where not exists (select * from mpa where mpa_name = 'NC-17');

insert into films (film_name, description, release_date, duration, mpa_id) select '11 друзей Оушена', 'Описание фильма 11 друзей Оушена', '2001-12-05', 116, 3 where not exists (select * from films where film_id = 1);
insert into films (film_name, description, release_date, duration, mpa_id) select 'Красный воробей', 'Описание фильма Красный воробей', '2018-02-26', 140, 4 where not exists (select * from films where film_id = 2);
insert into films (film_name, description, release_date, duration, mpa_id) select 'Престиж', 'Описание фильма Престиж', '2006-10-17', 125, 3 where not exists (select * from films where film_id = 3);
insert into films (film_name, description, release_date, duration, mpa_id) select 'Солт', 'Описание фильма Солт', '2010-07-19', 100, 3 where not exists (select * from films where film_id = 4);

insert into users (user_email, user_login, user_name, birthday) select 'maxim@yandex.ru', 'Max', 'Maxim Smirnov', '1975-09-21' where not exists (select * from users where user_id = 1);
insert into users (user_email, user_login, user_name, birthday) select 'vasya@yandex.ru', 'Vasya', 'Vasily Solomonov', '1987-08-02' where not exists (select * from users where user_id = 2);
insert into users (user_email, user_login, user_name, birthday) select 'svetlana@yandex.ru', 'Svetlana', 'Svetlana Samokhina', '1993-12-12' where not exists (select * from users where user_id = 3);

insert into film_genre values (2, 1);
insert into film_genre values (4, 1);
insert into film_genre values (3, 3);
insert into film_genre values (4, 4);
insert into film_genre values (5, 4);

insert into friends values (1, 2);
insert into friends values (1, 3);
insert into friends values (2, 3);

insert into likes values (1, 1);
insert into likes values (1, 3);
insert into likes values (3, 1);
insert into likes values (3, 2);
insert into likes values (3, 3);
insert into likes values (2, 3);