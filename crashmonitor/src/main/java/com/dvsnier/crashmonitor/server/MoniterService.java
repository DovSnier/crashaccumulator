package com.dvsnier.crashmonitor.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dvsnier.crashmonitor.utils.CrashHandler;


/**
 * the current modules or log recoder server is test version program
 *
 * @author dovsnier
 * @version 0.0.1
 * @since JDK 1.7
 */
public class MoniterService extends Service {

    public static final String TAG = "MoniterService";
    private static boolean DEBUG = false;
    private final IBinder myBinder = new MyBinder();
    private final CrashHandler crashHandler = CrashHandler.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        initializedMoniterEnvironment();
    }

    private void initializedCrashServer() {
        crashHandler.init(getApplicationContext());
        if (DEBUG) {
            Log.i(TAG, "the current log monito server is started");
        }
    }

    protected void initializedMoniterEnvironment() {
        initializedCrashServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (DEBUG) {
//            Log.i(TAG, "the current log monito server is running that is ready...");
//        }
        Log.i(TAG, "the current log monito server is running that is ready...");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) {
            Log.i(TAG, "the current log monito server is destroy.");
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO the current stage no provide onBind method //return null;
        return myBinder;
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

