package com.dvsnier.monitor.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * simple date formatting tool
 * Created by dovsnier on 2016/3/15.
 */
public class SimpleDateUtil {

    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 1.0.1
     */
    @SuppressLint("SimpleDateFormat")
    @Deprecated
    public static String getPrintToFileTime(String format) {
        SimpleDateFormat sdf = null;
        String DEFAULT_FORMAT = "yyyy_MMdd_HHmm_ss";
        if (null != format && !"".equals(format)) {
            try {
                sdf = new SimpleDateFormat(format);
            } catch (Exception e) {
                sdf = new SimpleDateFormat(DEFAULT_FORMAT);
            }
        } else {
            sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        }
        return sdf.format(System.currentTimeMillis());
    }


    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.2
     */
    public static String getPrintToFileTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.2
     */
    public static String getPrintToTextTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * get the current system date with month and day<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.1
     */
    public static String getPrintToDirectoryTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        return sdf.format(System.currentTimeMillis());
    }

    @Deprecated
    public static String obtainFileName() {
        return "crash_" + getPrintToFileTime() + ".log";
    }

    public static String obtainFileName(Context context, MIMEType type) {
        if (null != context) {
            int identifier = context.getResources().getIdentifier("mime_type", "string", context.getPackageName());
            if (identifier > 0) {
                String value = context.getString(identifier);
                if (MIMEType.TYPE_TEXT.toString().equals(value)) {
                    return "crash_" + getPrintToFileTime() + ".txt";
                }
            }
            if (type == MIMEType.TYPE_TEXT) {
                return "crash_" + getPrintToFileTime() + ".txt";
            }
        }
        return "crash_" + getPrintToFileTime() + ".log";
    }
}
