package cm.java.validate;

import cm.java.util.Utils;

public class NotEmptyValidator extends BaseValidator {

    @Override
    public boolean isValid(String text) {
        return !Utils.isEmpty(text);
    }
}
