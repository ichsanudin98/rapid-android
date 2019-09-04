package com.ichsanudinstore.loka.util;

import com.ichsanudinstore.loka.config.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 5:23 PM
 */
public class RestUtil {
    private static String timestamp = null;

    public static String generateTimestamp() {
        if (timestamp == null)
            timestamp = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        return timestamp;
    }

    public static void resetTimestamp() {
        timestamp = null;
    }

    public static String generateSecurityCode(String data) {
        if (timestamp == null)
            timestamp = generateTimestamp();

        return HashUtil.SHA256(Constant.Application.SALT + data + timestamp);
    }
}
