package ru.yandex.practicum.filmorate.validators.positiveDateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveDurationValidator.class)
public @interface CustomPositiveDuration {
    String message() default "Продолжительность должна быть положительным числом";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
