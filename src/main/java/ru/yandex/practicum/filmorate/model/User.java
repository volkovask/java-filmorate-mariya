package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Positive
    private Integer id;
    @Email
    private String email;
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
