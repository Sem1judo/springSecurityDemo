package com.example.springsecuritydemo.validation.validation.userType;

import com.example.springsecuritydemo.model.dto.TypeUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TypeUserValidator implements ConstraintValidator<ValidTypeUser, TypeUser> {

    private TypeUser[] subset;

    @Override
    public void initialize(ValidTypeUser constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(TypeUser value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}



