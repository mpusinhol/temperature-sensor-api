package com.mpusinhol.temperaturesensorapi.validation;

import com.mpusinhol.temperaturesensorapi.exception.InvalidTemperatureUnitException;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TemperatureUnitValidator implements ConstraintValidator<ValidTemperatureUnit, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            TemperatureUnit.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = "Temperature unit is mandatory and must be one of " + Arrays.toString(TemperatureUnit.values());
            throw new InvalidTemperatureUnitException(message);
        }

        return true;
    }
}
