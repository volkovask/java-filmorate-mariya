package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    @Size(max = MAX_DESCRIPTION_LENGTH,
            message = "Максимальная длина описания составляет " + MAX_DESCRIPTION_LENGTH + " символов")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "Длительность фильма должна быть больше нуля")
    private Integer duration;
    @NotNull(message = "Рейтинг mpa не может быть пустым")
    private Mpa mpa;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Director director;

    public Map<String, Object> toMap() {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("film_name", name);
        filmMap.put("description", description);
        filmMap.put("release_date", releaseDate);
        filmMap.put("duration", duration);
        filmMap.put("mpa_id", mpa.getId());
        return filmMap;
    }
}
