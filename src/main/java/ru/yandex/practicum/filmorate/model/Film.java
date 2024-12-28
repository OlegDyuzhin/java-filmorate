package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.realiseDateValidator.CustomReleaseDateMin;

import java.time.LocalDate;


@Data
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @CustomReleaseDateMin(message = "Дата релиза менее 1895-12-28", value = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
