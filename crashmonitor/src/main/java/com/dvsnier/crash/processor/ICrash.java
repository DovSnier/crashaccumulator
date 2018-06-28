package com.dvsnier.crash.processor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dvsnier.monitor.common.IDebug;

/**
 * the abstract class of crash
 * Created by dovsnier on 2018/6/27.
 */
public interface ICrash extends IDebug {

    String CRASH = "crash";

    /**
     * the init global default uncaught exception handler <br>
     * 2015-5-13
     *
     * @param context {@see Context}
     * @version 0.0.2
     */
    void initialize(@NonNull Context context);

    /**
     * the init global default uncaught exception handler <br>
     * 2015-5-13
     *
     * @param context      {@link Context}
     * @param storageState {@link StorageStrategy}
     * @version 0.0.3
     */
    void initialize(@NonNull Context context, @Nullable StorageStrategy storageState);

    /**
     * the stop crash handle instance
     *
     * @version 1.0.0
     */
    void shutdown();
}
