package org.workinkexceptiondemo.service.util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailCheck {
    public static void main(String[] args) {

        EmailValidator validator = EmailValidator.getInstance();

        System.out.println(validator.isValid("googlemap@gmail.com"));
    }
}
