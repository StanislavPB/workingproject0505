package org.workinkexceptiondemo.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class OurValidator implements ConstraintValidator<OurValidation, String> {
    
    private String defaultMessage;


    @Override
    public void initialize(OurValidation constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
    }

    // idValid - реализуемая логика проверки пароля
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            addConstraintViolation(context, defaultMessage + "\n Password cannot be null");
            return false;
        }

        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add("Длина пароля должна быть не менее 8 символов");
        }

        // проверка на то, что пароль содержит хотя бы одну большую букву

        // проверка на то, что пароль содержит хотя бы одну цифру

        //----------

        if (!errors.isEmpty()){
            addConstraintViolation(context, defaultMessage);
            for (String error : errors){
                addConstraintViolation(context,error);
            }
            return false;
        }

        return true;

    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message){
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
