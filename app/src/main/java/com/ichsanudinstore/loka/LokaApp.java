package com.ichsanudinstore.loka;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.ModelMigration;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;
import com.ichsanudinstore.loka.util.UncaughtExceptionUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/15/2019 9:32 AM
 */
public class LokaApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static LokaApp lokaApp;
    private AppCompatActivity appCompatActivity;

    public LokaApp() {
        super();
    }

    public static LokaApp getInstance() {
        return lokaApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lokaApp = this;
        SharedPreferencesUtil.getInstance().setSharedPreferences(this.getSharedPreferences(Constant.Application.NAME, Context.MODE_PRIVATE));
        Realm.init(this);
        RealmConfiguration mConfiguration = new RealmConfiguration.Builder()
                .name("loka_db.realm")
                .schemaVersion(1)
                .migration(new ModelMigration())
                .build();
        Realm.setDefaultConfiguration(mConfiguration);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionUtil());
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof AppCompatActivity) {
            LokaApp.this.appCompatActivity = (AppCompatActivity) activity;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public AppCompatActivity getAppCompatActivity() {
        return this.appCompatActivity;
    }
}
