package com.ichsanudinstore.loka.util;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/21/2019 2:59 PM
 */
public class IntentUtil {
    public static Intent generalGoTo(AppCompatActivity from, Class<?> to, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent(from, to);
        if (isFinish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (bundle != null)
            intent.putExtra("bundle", bundle);
        return intent;
    }

    public static void goTo(AppCompatActivity from, Intent intent) {
        from.startActivity(intent);
    }
}
