package com.example.safeT.login.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoHyphenValidator implements ConstraintValidator<NoHyphen, String> {

    @Override
    public void initialize(NoHyphen constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return !value.contains("-");
    }
}
