package com.mpusinhol.temperaturesensorapi.validation;

import com.mpusinhol.temperaturesensorapi.validation.validator.AggregationModeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AggregationModeValidator.class)
public @interface ValidAggregationMode {
    String message() default "Aggregate must be one of DAILY, HOURLY or NONE"; //Only constants allowed, unfortunately
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
