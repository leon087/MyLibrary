package cm.android.validate;

import cm.android.util.Utils;

public class NotEmptyValidator extends BaseValidator {

    @Override
    public boolean isValid(String text) {
        return !Utils.isEmpty(text);
    }
}
