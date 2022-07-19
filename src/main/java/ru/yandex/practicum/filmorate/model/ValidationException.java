package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationException extends RuntimeException {
    private final String message;
}
