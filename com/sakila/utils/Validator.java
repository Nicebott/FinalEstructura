package com.sakila.utils;

import java.util.regex.Pattern;

/**
 * Clase para validar datos usando expresiones regulares
 * @author Nicolas Zierow Fermin
 */
public class Validator {
    // Patrones de expresiones regulares
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    private static final Pattern SSN_PATTERN = Pattern.compile("^[0-9]{3}-[0-9]{2}-[0-9]{4}$");
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^[0-9]{5}(-[0-9]{4})?$");
    
    /**
     * Valida un formato de fecha (YYYY-MM-DD)
     * @param date Fecha a validar
     * @return true si es válida, false en caso contrario
     */
    public static boolean validateDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        return DATE_PATTERN.matcher(date).matches();
    }
    
    /**
     * Valida un formato de correo electrónico
     * @param email Correo electrónico a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida un formato de teléfono (XXX-XXX-XXXX)
     * @param phone Teléfono a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Valida un formato de número de seguro social (XXX-XX-XXXX)
     * @param ssn Número de seguro social a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateSSN(String ssn) {
        if (ssn == null || ssn.isEmpty()) {
            return false;
        }
        return SSN_PATTERN.matcher(ssn).matches();
    }
    
    /**
     * Valida un formato de código postal (XXXXX o XXXXX-XXXX)
     * @param postalCode Código postal a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validatePostalCode(String postalCode) {
        if (postalCode == null || postalCode.isEmpty()) {
            return false;
        }
        return POSTAL_CODE_PATTERN.matcher(postalCode).matches();
    }
}
