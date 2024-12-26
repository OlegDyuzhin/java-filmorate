package ru.yandex.practicum.filmorate.validators.realiseDateValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<CustomReleaseDateMin, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return localDate.isAfter(minDate);
    }
}
