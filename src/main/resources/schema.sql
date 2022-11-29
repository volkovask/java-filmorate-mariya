drop all objects;
create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment
        primary key,
    GENRE_NAME CHARACTER VARYING(50)
);
create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER auto_increment
        primary key,
    MPA_NAME CHARACTER VARYING(50)
);
create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    FILM_NAME  CHARACTER VARYING(64),
    DESCRIPTION  CHARACTER VARYING(200),
    DURATION     INTEGER,
    MPA_ID INTEGER,
    RELEASE_DATE   DATE
);
create table IF NOT EXISTS USERS
(
    USER_ID    INTEGER auto_increment
        primary key,
    USER_EMAIL CHARACTER VARYING(64),
    USER_LOGIN CHARACTER VARYING(64),
    USER_NAME  CHARACTER VARYING(64),
    BIRTHDAY   DATE
);
create table IF NOT EXISTS FRIENDS
(
    USER_ID      INTEGER,
    FRIEND_ID    INTEGER
);
create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "LIKES_PK" primary key (USER_ID, FILM_ID)
);
create table IF NOT EXISTS FILM_GENRE
(
    GENRE_ID INTEGER,
    FILM_ID  INTEGER
);

alter table films add constraint if not exists FILMS_MPA_RATING_FK foreign key (MPA_ID) references MPA (MPA_ID);
alter table FILM_GENRE add constraint if not exists FILM_GENRE_Films_FK foreign key (FILM_ID) references FILMS (FILM_ID);
alter table FILM_GENRE add constraint if not exists FILM_GENRE_GENRE_FK foreign key (GENRE_ID) references GENRES (GENRE_ID);
alter table FRIENDS add constraint if not exists FRIENDS_USERS_FK foreign key (FRIEND_ID) references USERS (USER_ID);
alter table FRIENDS add constraint if not exists FRIENDS_USERS2_FK foreign key (USER_ID) references USERS (USER_ID);
alter table LIKES add constraint if not exists LIKES_films_FK foreign key (FILM_ID) references FILMS(FILM_ID);
alter table LIKES add constraint if not exists LIKES_USERS_FK foreign key (USER_ID) references USERS(USER_ID);

