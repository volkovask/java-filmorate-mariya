package ru.yandex.practicum.filmorate.model;

public class IdGenerator {
    private Integer id;

    public IdGenerator() {
        this.id = 0;
    }

    public Integer generate() {
        return ++id;
    }
}
