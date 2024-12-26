package ru.yandex.practicum.filmorate.validators.positiveDateValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class PositiveDurationValidator implements ConstraintValidator<CustomPositiveDuration, Duration> {

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        return !duration.isNegative() && !duration.isZero();
    }
}
