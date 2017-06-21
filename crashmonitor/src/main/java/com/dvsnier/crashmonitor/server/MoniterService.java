package com.dvsnier.crashmonitor.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dvsnier.crashmonitor.R;
import com.dvsnier.crashmonitor.utils.CrashHandler;


/**
 * the current modules or log recoder server is test version program
 *
 * @author dovsnier
 * @version 0.0.1
 * @since JDK 1.7
 */
public class MoniterService extends Service {

    public final String TAG = this.getClass().getSimpleName();
    private boolean DEBUG;
    private final IBinder myBinder = new MyBinder();
    private final CrashHandler crashHandler = CrashHandler.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        initializedMonitorEnvironment();
    }

    protected void initializedMonitorEnvironment() {
        DEBUG = getResources().getBoolean(R.bool.debug_monitor_server);
        initializedCrashServer();
    }

    private void initializedCrashServer() {
        crashHandler.init(getApplicationContext());
        if (DEBUG) {
            Log.i(TAG, "the current log monitor server is started");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (DEBUG) {
//            Log.i(TAG, "the current log monitor server is running that is ready...");
//        }
        Log.i(TAG, "the current log monitor server is running that is ready...");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO the current stage no provide onBind method //return null;
        return myBinder;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) {
            Log.i(TAG, "the current log monitor server is destroy.");
        }
        super.onDestroy();
    }

    /**
     * the current MoniterService Proxy
     */
    public class MyBinder extends Binder {

        public MoniterService getInstance() {
            return MoniterService.this;
        }
    }
}

