package com.dvsnier.crash.server;

/**
 * the monitor
 * Created by dovsnier on 2017/12/09.
 */
public interface IMonitor {

    String KEY_MODE = "MODE";
    String KEY_TIPS = "TIPS";
    boolean MONITOR_DEFAULT_MODE = false;

    /**
     * the initialized monitor environment
     */
    void onMonitorEnvironment();

    /**
     * the initialized crash server
     */
    void onCrashServer();
}
