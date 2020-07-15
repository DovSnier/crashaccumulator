package com.dvsnier.crash;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.dvsnier.crash.server.MonitorService;
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
        //noinspection ConstantConditions
        if (null != context) {
            Intent intent = new Intent(context, MonitorService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    /**
     * initializing crash monitoring
     *
     * @param context {@see Context}
     * @param mode    true: the debug mode,otherwise release mode
     */
    public static void initialize(@NonNull Context context, boolean mode) {
        Crash.context = context;
        //noinspection ConstantConditions
        if (null != context) {
            Intent intent = new Intent(context, MonitorService.class);
            intent.putExtra(MonitorService.KEY_MODE, mode);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
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
        //noinspection ConstantConditions
        if (null != context) {
            Intent intent = new Intent(context, MonitorService.class);
            intent.putExtra(MonitorService.KEY_MODE, mode);
            intent.putExtra(MonitorService.KEY_TIPS, tips);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    /**
     * to closed server monitor
     */
    public static void shutdown() {
        if (null != context) {
            Intent intent = new Intent(context, MonitorService.class);
            context.stopService(intent);
        }
        context = null;
    }
}
