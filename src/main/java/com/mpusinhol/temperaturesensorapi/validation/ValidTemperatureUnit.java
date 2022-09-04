package com.mpusinhol.temperaturesensorapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TemperatureUnitValidator.class)
public @interface ValidTemperatureUnit {
    String message() default "Temperature unit must be either CELSIUS, KELVIN OR FAHRENHEIT"; //Only constants allowed, unfortunately
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
