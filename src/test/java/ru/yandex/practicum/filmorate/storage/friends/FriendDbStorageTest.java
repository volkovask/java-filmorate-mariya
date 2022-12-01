package ru.yandex.practicum.filmorate.storage.friends;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendDbStorageTest {
    private final FriendStorage friendDbStorage;

    @Test
    @Order(1)
    void getUserFriendsTest() {
        final int userId = 1;
        final int expectedFriendSize = 2; //пользователю с id = 1 добавлено 2 друга в data.sql
        final List<User> userFriendsList = new ArrayList<>(friendDbStorage.getUserFriends(userId));
        assertThat(userFriendsList)
                .hasSize(expectedFriendSize);
    }

    @Test
    @Order(2)
    void createFriendTest() {
        final int userId = 3; //сейчас у пользователя нет друзей
        final int friendId = 1;
        friendDbStorage.createFriend(userId, friendId);
        final int userFriendListSize = friendDbStorage.getUserFriends(userId).size();
        final int expectedSize = 1;
        assertThat(userFriendListSize)
                .isEqualTo(expectedSize);
    }

    @Test
    @Order(3)
    void deleteFriendTest() {
        final int userId = 3;
        final int friendId = 1;
        friendDbStorage.deleteFriend(userId, friendId);
        final int userFiendSize = friendDbStorage.getUserFriends(userId).size();
        final int expectedSize = 0;
        assertThat(userFiendSize)
                .isEqualTo(expectedSize);
    }

    @Test
    @Order(4)
    void getCommonFriendsTest() {
        final int userId = 1;
        final int friendId = 2;
        final int expectedCommonFriendId = 3;
        final List<User> commonFriendsList = new ArrayList<>(friendDbStorage.getCommonFriends(userId, friendId));
        final int expectedSize = 1;
        assertThat(commonFriendsList)
                .hasSize(expectedSize);
        final int actualCommonFriendId = commonFriendsList.get(0).getId();
        assertThat(actualCommonFriendId)
                .isEqualTo(expectedCommonFriendId);
    }
}
