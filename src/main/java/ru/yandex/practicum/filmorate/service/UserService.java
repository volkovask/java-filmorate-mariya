package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers().values();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public void createFriends(Integer userId, Integer friendId) {
        validId(userId, friendId);
        userStorage.getAllUsers().get(userId).getFriends().add(friendId);
        userStorage.getAllUsers().get(friendId).getFriends().add(userId);
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        validId(userId, friendId);
        userStorage.getAllUsers().get(userId).getFriends().remove(friendId);
        userStorage.getAllUsers().get(friendId).getFriends().remove(userId);
    }

    public List<User> getUserFriends(Integer id) {
        if (id <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", id));
        }
        List<User> userFriends = new ArrayList<>();
        for (Integer friendId : userStorage.getAllUsers().get(id).getFriends()) {
            userFriends.add(userStorage.getAllUsers().get(friendId));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        validId(userId, friendId);
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : userStorage.getAllUsers().get(userId).getFriends())
            if (userStorage.getAllUsers().get(friendId).getFriends().contains(id)) {
                commonFriends.add(userStorage.getAllUsers().get(id));
            }
        return commonFriends;
    }

    private void validId(Integer userId, Integer friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", userId));
        }
        if (friendId <= 0) {
            throw new UserNotFoundException(String.format("Некорректный id: %d", friendId));
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", friendId));
        }
    }
}
