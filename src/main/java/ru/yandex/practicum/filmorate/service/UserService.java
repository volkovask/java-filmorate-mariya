package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
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
        friendStorage.createFriend(userId, friendId);
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getUserFriends(Integer id) {
        return friendStorage.getUserFriends(id);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
