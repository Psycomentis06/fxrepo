package com.github.psycomentis06.fxrepomain.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = RgbaColorStringValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RgbaColorString {
    String message() default  "Invalid RGB value. Should be in the format rgba(r,g,b,a)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
