package de.flojc.fastlane.request;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `Request` class is an abstract class that provides methods for validating fields in a request object, checking for
 * blank or null values and applying specific validation rules for certain fields.
 */
public abstract class Request {

    /**
     * The function validates that all fields in a class are not blank and throws a BackendException if any field is blank.
     */
    public void validateFieldsNotBlank() throws BackendException {
        Field[] fields = getFieldsOfClass();
        for (Field field : fields) {
            processField(field, true);
        }
    }

    /**
     * The function validates that all fields in a class are not null or blank.
     */
    public void validateFieldsNotNullOrBlank() throws BackendException {
        Field[] fields = getFieldsOfClass();
        for (Field field : fields) {
            processField(field, false);
        }
    }

    /**
     * The function processes a field by retrieving its value, validating it either for blankness or in general, and
     * throwing an exception if necessary.
     *
     * @param field The "field" parameter is of type "Field" and represents a field in a class. It is used to access the
     * value of the field and retrieve its name.
     * @param blankCheckOnly The "blankCheckOnly" parameter is a boolean value that determines whether only blank check
     * validation should be performed on the field. If it is set to true, only blank check validation will be performed. If
     * it is set to false, full validation will be performed on the field.
     */
    private void processField(Field field, boolean blankCheckOnly) throws BackendException {
        Object value;
        try {
            value = field.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String fieldName = field.getName();
        if (blankCheckOnly) {
            validateFieldNotBlank(value, fieldName);
        } else {
            validateField(value, fieldName);
        }
    }

    /**
     * The function validates a field value and throws exceptions if the value is invalid or null.
     *
     * @param value The `value` parameter represents the value of a field that needs to be validated. It can be of any
     * type, depending on the field being validated.
     * @param fieldName The `fieldName` parameter is a String that represents the name of the field being validated.
     */
    private void validateField(Object value, String fieldName) throws BackendException {
        boolean valueIsInvalidOrNullOrEmpty = false;

        if (value == null) {
            valueIsInvalidOrNullOrEmpty = true;
        } else {
            validateFieldNotBlank(value, fieldName);
            if (value instanceof String string) {
                if (fieldName.equals("password")) {
                    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(string);
                    valueIsInvalidOrNullOrEmpty = !matcher.matches();

                }
            }
        }

        if (valueIsInvalidOrNullOrEmpty) {
            if (this.getClass().equals(AuthenticationRequest.class)) {
                throw new AccessDeniedException("");
            } else {
                if (fieldName.equals("password")) {
                    throw new BackendException(ErrorCode.PASSWORD_INADEQUATE);
                } else {
                    throw new BackendException(ErrorCode.UNSET_FIELD, fieldName);
                }
            }
        }
    }

    /**
     * The function returns an array of all the fields (variables) declared in the current class.
     *
     * @return An array of Field objects representing the fields of the class.
     */
    private Field[] getFieldsOfClass() {
        return this.getClass().getDeclaredFields();
    }

    /**
     * The function validates whether a field is blank or not and throws a BackendException if it is.
     *
     * @param value The value is the object that needs to be validated. It can be of any type, but the code specifically
     * checks if it is a String or a Character.
     * @param fieldName The `fieldName` parameter is a string that represents the name of the field being validated.
     */
    private void validateFieldNotBlank(Object value, String fieldName) throws BackendException {
        boolean isBlank = false;

        if (value instanceof String string) {
            isBlank = string.isBlank();
        }

        if (value instanceof Character character) {
            isBlank = Character.isWhitespace(character);
        }

        if (isBlank) {
            throw new BackendException(ErrorCode.BLANK_FIELD, fieldName);
        }
    }
}
