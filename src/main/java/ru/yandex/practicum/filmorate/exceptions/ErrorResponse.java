package ru.yandex.practicum.filmorate.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    public final String error;
    private final String description;
}
