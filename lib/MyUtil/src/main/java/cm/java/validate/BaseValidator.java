package cm.java.validate;

public abstract class BaseValidator {

    public abstract boolean isValid(String value) throws ValidatorException;
}
