package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Director {
    private Integer id;
    @NotNull
    @NotEmpty
    private String name;

    public Map<String, Object> toMap() {
        Map<String, Object> directorMap = new HashMap<>();
        directorMap.put("director_name", name);
        return directorMap;
    }
}