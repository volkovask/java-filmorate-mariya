package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    private Integer id;
    @NotNull
    @NotEmpty
    @Email(message = "Указан некорректный email адрес")
    private String email;
    @NotNull
    @NotEmpty
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private final Map<Integer, Boolean> friends = new HashMap<>();

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_email", email);
        userMap.put("user_login", login);
        userMap.put("user_name", name);
        userMap.put("birthday", birthday);
        return userMap;
    }
}
