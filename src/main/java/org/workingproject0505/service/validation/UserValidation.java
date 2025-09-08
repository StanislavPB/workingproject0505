package org.workingproject0505.service.validation;

import org.springframework.stereotype.Service;
import org.workingproject0505.dto.UserRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserValidation {
    //Regex: только латиница, цифры и допустимые спецсимволы
    private static final Pattern LATIN_PATTERN = Pattern.compile("^[A-Za-z0-9._!-]+$");

    //Regex: только латиница, цифры и допустимые спецсимволы для email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._@-]+$");

    public List<String> validate(UserRequestDto request) {

        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("Данные пользователя не могут быть пустыми");
        }

        // username
        validateField(errors, request.getUserName(), 3, "Имя пользователя");

        if (!LATIN_PATTERN.matcher(request.getUserName()).matches()) {
            errors.add("Имя пользователя содержит недопустимые символы");
        }

        // email

        validateField(errors, request.getEmail(), 5, "Email");

        if (!EMAIL_PATTERN.matcher(request.getUserName()).matches()) {
            errors.add("Email содержит недопустимые символы");
        }

        // password

        validateField(errors, request.getPassword(), 8, "Пароль");

        return errors;

    }

    private void validateField(List<String> errors, String stringField, int fieldLengthMin, String fieldName) {
        if (stringField == null || stringField.isBlank()) {
            errors.add(fieldName + " не может быть пустым или состоять только из пробелов");
        }

        if (stringField.length() < fieldLengthMin) {
            errors.add(fieldName + " должно содержать не менее 3 символов");
        }

    }
}
