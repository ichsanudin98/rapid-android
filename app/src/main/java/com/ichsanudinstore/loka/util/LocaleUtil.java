package com.ichsanudinstore.loka.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;

import com.ichsanudinstore.loka.config.Constant;

import java.util.Locale;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:43 PM
 */

public class LocaleUtil {
    public static String getString(Context context, @StringRes int id) {
        Resources res = context.getResources();

        String language = SharedPreferencesUtil.get(Constant.SharedPreferenceKey.LANGUAGE, String.class,
                Build.VERSION.SDK_INT >= 24 ? res.getConfiguration().getLocales().get(0).getDisplayLanguage() :
                        res.getConfiguration().locale.getDisplayLanguage());

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context.getString(id);
    }

    public static String[] getStringArray(Context context, @ArrayRes int id) {
        Resources res = context.getResources();

        String language = SharedPreferencesUtil.get(Constant.SharedPreferenceKey.LANGUAGE, String.class,
                Build.VERSION.SDK_INT >= 24 ? res.getConfiguration().getLocales().get(0).getDisplayLanguage() :
                        res.getConfiguration().locale.getDisplayLanguage());

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context.getResources().getStringArray(id);
    }
}
