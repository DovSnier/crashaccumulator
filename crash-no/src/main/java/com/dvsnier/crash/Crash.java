package com.dvsnier.crash;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.dvsnier.monitor.common.ITag;


/**
 * crash monitoring
 * Created by dovsnier on 2018/6/27.
 */
public final class Crash implements ITag {

    protected static Context context;

    /**
     * initializing crash monitoring
     *
     * @param context {@see Context}
     */
    public static void initialize(@NonNull Context context) {
        Crash.context = context;
        // no development phase log records are provided in release mode
    }

    /**
     * initializing crash monitoring
     *
     * @param context {@see Context}
     * @param mode    true: the debug mode,otherwise release mode
     */
    public static void initialize(@NonNull Context context, boolean mode) {
        Crash.context = context;
        if (mode)
            Log.d(TAG, "no development phase log records are provided in release mode");
    }

    /**
     * initializing crash monitoring
     *
     * @param context {@see Context}
     * @param mode    true: the debug mode,otherwise release mode
     * @param tips    the standard abnormal text hints
     */
    public static void initialize(@NonNull Context context, boolean mode, String tips) {
        Crash.context = context;
        if (mode)
            Log.d(TAG, "no development phase log records are provided in release mode");
    }

    /**
     * to closed server monitor
     */
    public static void shutdown() {
        context = null;
        // no development phase log records are provided in release mode
    }
}
