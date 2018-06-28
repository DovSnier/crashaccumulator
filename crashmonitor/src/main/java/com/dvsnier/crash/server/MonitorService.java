package com.dvsnier.crash.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dvsnier.crash.BuildConfig;
import com.dvsnier.crash.R;
import com.dvsnier.crash.processor.CrashHandler;
import com.dvsnier.crash.processor.ICrash;
import com.dvsnier.monitor.common.ITag;


/**
 * the current modules or log recorder server is test version program
 *
 * @author dovsnier
 * @version 0.0.1
 * @since JDK 1.7
 */
public class MonitorService extends Service implements IMonitor, ITag {

    //    protected final String TAG = this.getClass().getSimpleName();
    protected static boolean DEBUG;
    private final IBinder myBinder = new MyBinder();
    private final ICrash crash = CrashHandler.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        onMonitorEnvironment();
        onCrashServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            DEBUG = intent.getBooleanExtra(KEY_MODE, MONITOR_DEFAULT_MODE);
            String tips = intent.getStringExtra(KEY_TIPS);
            crash.setDebug(DEBUG);
            if (crash instanceof CrashHandler && null != tips && !"".equals(tips)) {
                ((CrashHandler) crash).setTips(tips);
            }
        }
        if (DEBUG) {
            Log.i(TAG, getResources().getString(R.string.crash_started_describe));
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // the current stage no provide onBind method
        return myBinder;
    }

    @Override
    public void onDestroy() {
        if (null != crash) {
            crash.shutdown();
        }
        if (DEBUG) {
            Log.i(TAG, getResources().getString(R.string.crash_destroy_describe));
        }
        super.onDestroy();
    }

    @Override
    public void onMonitorEnvironment() {
        DEBUG = getResources().getBoolean(R.bool.debug_monitor_server);
    }

    @Override
    public void onCrashServer() {
        crash.initialize(getApplicationContext());
        if (DEBUG) {
            Log.i(TAG, String.format("%1$s%2$s", getResources().getString(R.string.crash_describe), BuildConfig.DVS_CONFIG_VERSION));
        }
    }

    /**
     * the current MonitorService Proxy
     */
    public class MyBinder extends Binder {

        public MonitorService getInstance() {
            return MonitorService.this;
        }
    }
}

