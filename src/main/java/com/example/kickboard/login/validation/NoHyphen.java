package com.example.kickboard.login.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoHyphenValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoHyphen {

    String message() default "Phone number should not contain Hyphen";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
