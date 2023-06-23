package com.github.psycomentis06.fxrepomain.validator;

import com.github.psycomentis06.fxrepomain.util.Color;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RgbaColorStringValidator implements ConstraintValidator<RgbaColorString, String> {
    @Override
    public void initialize(RgbaColorString constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Color.isRgba(s);
    }
}
