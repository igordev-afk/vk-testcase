package ru.wwerlosh.vktestcase.handlers.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotBlankAndNotNullValidator implements ConstraintValidator<AtLeastOneNotBlankAndNotNull, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        try {
            for (java.lang.reflect.Field field : value.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                if (fieldValue != null && !String.valueOf(fieldValue).isBlank()) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error while accessing fields", e);
        }
        return false;
    }
}
