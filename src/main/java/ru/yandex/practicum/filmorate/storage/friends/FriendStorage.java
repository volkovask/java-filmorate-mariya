package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    void createFriend(Integer userId, Integer friendId);
    void deleteFriend(Integer userId, Integer friendId);
    Collection<User> getUserFriends(Integer userId);
    Collection<User> getCommonFriends(Integer userId, Integer friendId);
}
