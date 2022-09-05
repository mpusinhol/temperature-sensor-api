package com.mpusinhol.temperaturesensorapi.validation.validator;

import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
import com.mpusinhol.temperaturesensorapi.exception.InvalidAggregationModeException;
import com.mpusinhol.temperaturesensorapi.validation.ValidAggregationMode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AggregationModeValidator implements ConstraintValidator<ValidAggregationMode, String> {

    public static final String MESSAGE = "Aggregate must be one of " + Arrays.toString(AggregationMode.values());

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StringUtils.isEmpty(s)) {
                return true;
            }
            AggregationMode.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAggregationModeException(MESSAGE);
        }

        return true;
    }
}
