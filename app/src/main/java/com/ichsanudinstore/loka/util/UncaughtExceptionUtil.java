package com.ichsanudinstore.loka.util;

import android.content.Intent;

import com.ichsanudinstore.loka.LokaApp;
import com.ichsanudinstore.loka.activity.UncaughtExceptionActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/15/2019 4:24 PM
 */
public class UncaughtExceptionUtil implements Thread.UncaughtExceptionHandler {

    public UncaughtExceptionUtil() {
        super();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        Intent intent = new Intent(LokaApp.getInstance().getApplicationContext(), UncaughtExceptionActivity.class);
        String s = stackTrace.toString();
        intent.putExtra("uncaughtException", "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        LokaApp.getInstance().getApplicationContext().startActivity(intent);
    }
}
