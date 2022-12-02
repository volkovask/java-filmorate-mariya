package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {

    public Director createDirector(Director director);

    public void updateDirector();

    public Director getDirectorById();

    public Collection<Director> getAll();

    public void deleteDirector(int directorId);

}
