package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class InMemoryUserStorage implements UserStorage {
    private final UserIdGenerator userIdGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    @Autowired
    public InMemoryUserStorage(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
    }

    @Override
    public User create(User user) {
        save(user);
        return user;
    }

    @Override
    public User update(User user) {
        if ((user.getId() == null) || (user.getId() < 0)) {
            throw new ValidationException("id не может быть путстым или меньше нуля");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден и не может быть обновлен",
                    user.getId()));
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        if (id == null) {
            throw new ValidationException("id не может быть пустым");
        }
        if (id <= 0) {
            throw new ValidationException("id должн быть больше нуля");
        }
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void save(User user) {
        validate(user);
        user.setId(userIdGenerator.generate());
        users.put(user.getId(), user);
    }

    private User validate(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        }
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}
