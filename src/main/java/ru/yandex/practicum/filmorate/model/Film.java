package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.positiveDateValidator.CustomPositiveDuration;
import ru.yandex.practicum.filmorate.validators.realiseDateValidator.CustomReleaseDateMin;

import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @CustomReleaseDateMin
    private LocalDate releaseDate;

    @CustomPositiveDuration
    private Duration duration;

    public Long getDuration() {
        return duration.toMinutes();
    }

    public void setDuration(Long durationOfMinutes) {
        this.duration = Duration.ofMinutes(durationOfMinutes);
    }
}
