package com.dvsnier.monitor.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.dvsnier.crash.R;
import com.dvsnier.crash.processor.ITask;
import com.dvsnier.crash.processor.StorageStrategy;

import java.io.File;

/**
 * the notification handler
 * Created by dovsnier on 2020/7/15.
 */
public class NotificationHandler extends BaseHandler implements INotificationTips, ITask {

    protected int identifier;
    protected NotificationManager notificationManager;


    public NotificationHandler() {
        super();
    }

    public NotificationHandler(@NonNull Context context, @Nullable StorageStrategy storageStrategy) {
        super(context, storageStrategy);
    }

    public NotificationHandler(@NonNull Context context, @Nullable StorageStrategy storageStrategy, boolean debug) {
        super(context, storageStrategy);
        this.DEBUG = debug;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        identifier = 0;
        if (null != notificationManager) {
            notificationManager = null;
        }
    }

    @Override
    public void execute() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (DEBUG) {
            Log.i(TAG, "the current notification is ready.");
        }
    }

    @Override
    public void onNotifyMsg(String type, String content, File file) {
        // https://developer.android.google.cn/reference/android/support/v4/content/FileProvider
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(getUri(file), "text/plain");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        int icon = getIdentifier("ic_launcher", "mipmap");
        String title = getString(getIdentifier("app_name", "string"));
        String contentTitle = String.format("%s", type);
        String contentText = String.format("%s%s", title, getString(R.string.crash_error_tips));
        String channel_name = getString(R.string.crash_channel_name);
        String channel_id = getString(R.string.crash_channel_id);
        String group_name = getString(R.string.crash_group_name);
        String group_id = getString(R.string.crash_group_id);
        String channel_describe = getString(R.string.crash_channel_describe);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, channel_name,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(channel_describe);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(group_id, group_name);
            notificationChannel.setGroup(group_id);

            if (null != notificationManager) {
                notificationManager.createNotificationChannelGroup(notificationChannelGroup);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        if (null != notificationManager) {
            notificationManager.notify(identifier++, notification);
        }
    }

    public String getString(int resId) {
        if (null != context) {
            return context.getString(resId);
        }
        return null;
    }

    public int getIdentifier(String name, String defType) {
        if (null != context) {
            return context.getResources().getIdentifier(name, defType, context.getPackageName());
        }
        return -1;
    }

    public Uri getUri(File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
                if (null != packageInfo && null != packageInfo.providers && packageInfo.providers.length > 0) {
                    for (int i = 0; i < packageInfo.providers.length; i++) {
                        if ("android.support.v4.content.FileProvider".equals(packageInfo.providers[i].name)) {
                            uri = FileProvider.getUriForFile(context, packageInfo.providers[i].authority, file);
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            uri = Uri.fromFile(file);
        }
        if (DEBUG) {
            Log.d(TAG, String.format("the current crash uri is %s", uri.toString()));
        }
        return uri;
    }


    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
