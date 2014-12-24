package cm.android.validate;

public abstract class BaseValidator {

    public abstract boolean isValid(String value) throws ValidatorException;
}
