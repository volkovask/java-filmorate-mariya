package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {

    Director createDirector(Director director);

    Director updateDirector(Director director);


    Director getDirectorById(int id);

    Collection<Director> getAll();

     void deleteDirector(int directorId);

}
