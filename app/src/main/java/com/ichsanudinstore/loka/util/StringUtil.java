package com.ichsanudinstore.loka.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:48 PM
 */
public class StringUtil {
    public static boolean isEmailValid(String mEmail) {

        if (!isEmpty(mEmail)) {
            String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mEmail);
            return matcher.matches();
        }

        return false;
    }

    public static boolean isEmpty(String mValue) {
        return mValue == null || mValue.trim().isEmpty();
    }
}
