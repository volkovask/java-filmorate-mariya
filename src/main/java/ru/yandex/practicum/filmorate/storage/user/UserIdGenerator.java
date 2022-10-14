package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {
    private Integer id;

    public UserIdGenerator() {
        this.id = 0;
    }

    public Integer generate() {
        return ++id;
    }
}
