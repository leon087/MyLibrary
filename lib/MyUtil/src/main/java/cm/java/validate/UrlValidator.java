package cm.java.validate;

import java.util.regex.Pattern;

import cm.java.util.Patterns;

public class UrlValidator extends BaseValidator {

    private static final Pattern WEB_URL_PATTERN = Patterns.WEB_URL;

    @Override
    public boolean isValid(String url) {
        return WEB_URL_PATTERN.matcher(url).matches();
    }
}
