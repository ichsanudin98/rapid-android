package com.ichsanudinstore.loka.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ichsanudinstore.loka.config.Constant;

import java.io.Closeable;
import java.util.Objects;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:43 PM
 */

public class SharedPreferencesUtil implements Closeable {
    private static final transient String TAG = SharedPreferencesUtil.class.getSimpleName();

    private static SharedPreferencesUtil INSTANCE;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferencesUtil() {
    }

    public static SharedPreferencesUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesUtil();
        }

        return INSTANCE;
    }

    private static <T> T get(String key, Class<T> mClass, T mDefaultValue) {
        try (SharedPreferencesUtil mSharedPreferencesUtil = getInstance()) {
            return mSharedPreferencesUtil.getValue(key, mClass, mDefaultValue);
        }
    }

    private static <T> T get(String key, Class<T> mClass) {
        return get(key, mClass, null);
    }

    public static <T> T get(Constant.SharedPreferenceKey mSharedPreferenceKey, Class<T> mClass, T mDefaultValue) {
        return get(mSharedPreferenceKey.name(), mClass, mDefaultValue);
    }

    public static <T> T get(Constant.SharedPreferenceKey mSharedPreferenceKey, Class<T> mClass) {
        return get(mSharedPreferenceKey, mClass, null);
    }

    public static <T> boolean contain(String key) {
        try (SharedPreferencesUtil mSharedPreferencesUtil = getInstance()) {
            return mSharedPreferencesUtil.mSharedPreferences.contains(key);
        }
    }

    public static <T> boolean contain(Constant.SharedPreferenceKey mSharedPreferenceKey) {
        return contain(mSharedPreferenceKey.name());
    }

    @SuppressLint("CommitPrefEdits")
    public void begin() {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }
    }

    public <T> void put(Constant.SharedPreferenceKey mSharedPreferenceKey, @NonNull T mValue) {
        if (this.mEditor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        if (mValue instanceof String) {
            this.mEditor.putString(mSharedPreferenceKey.name(), (String) mValue);
        } else if (mValue instanceof Integer) {
            this.mEditor.putInt(mSharedPreferenceKey.name(), (Integer) mValue);
        } else if (mValue instanceof Float) {
            this.mEditor.putFloat(mSharedPreferenceKey.name(), (Float) mValue);
        } else if (mValue instanceof Long) {
            this.mEditor.putLong(mSharedPreferenceKey.name(), (Long) mValue);
        } else if (mValue instanceof Boolean) {
            this.mEditor.putBoolean(mSharedPreferenceKey.name(), (Boolean) mValue);
        } else {
            Log.e(TAG, "Unsupported mValue type.");
        }
    }

    public void remove(String key) {
        if (this.mEditor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.mEditor.remove(key);
    }

    public void remove(Constant.SharedPreferenceKey mSharedPreferenceKey) {
        remove(mSharedPreferenceKey.name());
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> mClass, T mDefaultValue) {
        if (Objects.equals(String.class, mClass)) {
            return (T) this.mSharedPreferences.getString(key, mDefaultValue != null ? (String) mDefaultValue : null);
        } else if (Objects.equals(Integer.class, mClass)) {
            return (T) Integer.valueOf(this.mSharedPreferences.getInt(key, mDefaultValue != null ? (Integer) mDefaultValue : -9999));
        } else if (Objects.equals(Float.class, mClass)) {
            return (T) Float.valueOf(this.mSharedPreferences.getFloat(key, mDefaultValue != null ? (Float) mDefaultValue : -9999F));
        } else if (Objects.equals(Long.class, mClass)) {
            return (T) Long.valueOf(this.mSharedPreferences.getLong(key, mDefaultValue != null ? (Long) mDefaultValue : -9999L));
        } else if (Objects.equals(Boolean.class, mClass)) {
            return (T) Boolean.valueOf(this.mSharedPreferences.getBoolean(key, mDefaultValue != null && (Boolean) mDefaultValue));
        } else {
            return null;
        }
    }

    private <T> T getValue(String key, Class<T> mClass) {
        return getValue(key, mClass, null);
    }

    private <T> T getValue(Constant.SharedPreferenceKey mSharedPreferenceKey, Class<T> mClass, T mDefaultValue) {
        return getValue(mSharedPreferenceKey.name(), mClass, mDefaultValue);
    }

    private <T> T getValue(Constant.SharedPreferenceKey mSharedPreferenceKey, Class<T> mClass) {
        return getValue(mSharedPreferenceKey, mClass, null);
    }

    private <T> boolean containValue(String key) {
        return this.mSharedPreferences.contains(key);
    }

    private <T> boolean containValue(Constant.SharedPreferenceKey mSharedPreferenceKey) {
        return containValue(mSharedPreferenceKey.name());
    }

    private void clearUnknownKey() {
        for (String key : this.mSharedPreferences.getAll().keySet()) {
            try {
                Constant.SharedPreferenceKey.valueOf(key);
            } catch (IllegalArgumentException ex) {
                this.remove(key);
            }
        }
    }

    public void commit() {
        if (this.mEditor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.mEditor.apply();

    }

    @Override
    public void close() {
        this.mEditor = null;
    }

    public void setSharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }
}