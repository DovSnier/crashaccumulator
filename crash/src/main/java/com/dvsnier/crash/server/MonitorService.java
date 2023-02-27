package com.dvsnier.crash.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
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
    private boolean isInitialized;
    private int identifier;

    @Override
    public void onCreate() {
        super.onCreate();
        onMonitorEnvironment();
        onCrashServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            boolean debug = intent.getBooleanExtra(KEY_MODE, MONITOR_DEFAULT_MODE);
            if (debug) {
                //noinspection ConstantConditions
                DEBUG = debug;
            }
            String tips = intent.getStringExtra(KEY_TIPS);
            crash.setDebug(DEBUG);
            if (crash instanceof CrashHandler && null != tips && !"".equals(tips)) {
                ((CrashHandler) crash).setTips(tips);
            }
        }
        if (!isInitialized) {
            crash.initialize(getApplicationContext());
            isInitialized = true;
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
        isInitialized = false;
        if (null != crash) {
            crash.shutdown();
        }
        stopForeground(true);
        if (DEBUG) {
            Log.i(TAG, getResources().getString(R.string.crash_destroy_describe));
        }
        super.onDestroy();
    }

    @Override
    public void onMonitorEnvironment() {
        int identifier = getResources().getIdentifier("debug_monitor_server", "bool", getPackageName());
        if (identifier > 0) {
            DEBUG = getResources().getBoolean(identifier);
        }
    }

    @Override
    public void onCrashServer() {
        isInitialized = false;
        if (DEBUG) {
            Log.i(TAG, String.format("%1$s %2$s", getResources().getString(R.string.crash_describe), BuildConfig.DVS_CONFIG_VERSION));
        }
        int icon = getResources().getIdentifier("ic_launcher", "mipmap", getPackageName());
        String title = getString(getResources().getIdentifier("app_name", "string", getPackageName()));
        String channel_name = getString(R.string.crash_channel_name);
        String channel_id = getString(R.string.crash_channel_id);
        String group_name = getString(R.string.crash_group_name);
        String group_id = getString(R.string.crash_group_id);
        String crash_channel_describe = getString(R.string.crash_channel_describe);
        String crash_content_describe = getString(R.string.crash_content_describe);
        identifier = getResources().getInteger(R.integer.crash_notification_identifier);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, channel_name,
                    NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(crash_channel_describe);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(group_id, group_name);
            notificationChannel.setGroup(group_id);

            if (null != notificationManager) {
                notificationManager.createNotificationChannelGroup(notificationChannelGroup);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentTitle(TextUtils.isEmpty(title) ? channel_name : title)
                .setContentText(crash_content_describe)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(identifier++, notification);
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

