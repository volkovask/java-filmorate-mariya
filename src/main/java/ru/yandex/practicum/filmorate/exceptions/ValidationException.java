package ru.yandex.practicum.filmorate.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationException extends RuntimeException {
    private final String message;
}
