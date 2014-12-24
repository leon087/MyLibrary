package cm.android.validate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import cm.android.util.ObjectUtil;

public class MyValidate {

    private static final Logger logger = LoggerFactory.getLogger("validate");

    private List<BaseValidator> mValidators = ObjectUtil.newArrayList();

    /**
     * Add a new validator for fields attached
     */
    public void addValidator(BaseValidator validator) {
        mValidators.add(validator);
    }

    public boolean isValid(String source) {
        for (BaseValidator validator : mValidators) {
            try {
                if (!validator.isValid(source)) {
                    return false;
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }
}
