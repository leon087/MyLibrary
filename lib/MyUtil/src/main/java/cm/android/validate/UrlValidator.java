package cm.android.validate;

import android.util.Patterns;

import java.util.regex.Pattern;

public class UrlValidator extends BaseValidator {

    private static final Pattern WEB_URL_PATTERN = Patterns.WEB_URL;

    @Override
    public boolean isValid(String url) {
        return WEB_URL_PATTERN.matcher(url).matches();
    }
}
