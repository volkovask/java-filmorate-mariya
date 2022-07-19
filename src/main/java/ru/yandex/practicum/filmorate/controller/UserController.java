package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        save(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if ((user.getId() == null) || (user.getId() < 0)) {
            log.error("id не может быть путстым или меньше нуля");
            throw new ValidationException("id не может быть путстым или меньше нуля");
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    private void save(User user) {
        user.setId(idGenerator.generate());
        users.put(user.getId(), user);
    }

    User validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не должен быть пустым и содержать пробелы");
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        }
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}
