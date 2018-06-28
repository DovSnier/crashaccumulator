package com.dvsnier.monitor.common;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dvsnier.crash.processor.ITask;
import com.dvsnier.crash.processor.StorageStrategy;
import com.dvsnier.monitor.config.Config;
import com.dvsnier.monitor.config.I2nnerApi;
import com.dvsnier.monitor.config.ISharedPreferences;
import com.dvsnier.monitor.config.SharedPreferences;
import com.dvsnier.monitor.utils.SimpleDateUtil;

import java.io.File;

/**
 * file operation tool
 * Created by DovSnier on 2016/5/5.
 */
public class FileHandler extends BaseHandler implements I2nnerApi, ITask {

    protected String directory;
    protected ISharedPreferences sharedPreferences;

    public FileHandler() {
    }

    public FileHandler(@NonNull Context context, @Nullable StorageStrategy storageStrategy) {
        super(context, storageStrategy);
    }

    public FileHandler(@NonNull Context context, @Nullable StorageStrategy storageStrategy, boolean debug) {
        super(context, storageStrategy);
        this.DEBUG = debug;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (null != sharedPreferences) {
            sharedPreferences.commit();
            sharedPreferences = null;
            context = null;
            storageStrategy = StorageStrategy.STRATEGY_NONE;
        }
    }

    @Override
    public void execute() {
        onSdkInfo();
        inspectionAndInitializedFileSystem();
    }

    @Override
    public void onSdkInfo() {
        if (null == sharedPreferences)
            sharedPreferences = SharedPreferences.obtain(context);
        sharedPreferences.putString(Config.Key.KEY_SDK_VERSION, Config.DVS_CONFIG_VERSION).putString(Config.Key.KEY_ENVIRONMENT_MODE, DEBUG ? "debug" : "release").commit();
    }

    @Override
    public void onStorageStrategy() {
        if (null == sharedPreferences)
            sharedPreferences = SharedPreferences.obtain(context);
        sharedPreferences.putString(Config.Key.KEY_STRATEGY, null != this.storageStrategy ? this.storageStrategy.toString() : StorageStrategy.STRATEGY_NONE.toString()).commit();
    }

    @Override
    public void writeAbnormityInfo(@NonNull String alias) {
        if (null == sharedPreferences)
            sharedPreferences = SharedPreferences.obtain(context);
        sharedPreferences.putString(Config.Key.KEY_LAST_NAME, alias).putLong(Config.Key.KEY_LAST_TIME, System.currentTimeMillis()).commit();
    }

    /**
     * to inspection and to initiation crash directory structure<br>
     * 2015-5-13
     *
     * @version 1.0.6
     */
    protected void inspectionAndInitializedFileSystem() {
        if (null == storageStrategy) {
            this.storageStrategy = StorageStrategy.STRATEGY_NONE;
        }
        onStorageStrategy();
        directory = obtainFile(dispatchPath()).getAbsolutePath();
        if (DEBUG) {
            Log.i(TAG, "the current crash path is " + directory);
        }
    }

    private File inspectionLegalityPath(String appAbsolutePath) {
        if (null == appAbsolutePath || "".equals(appAbsolutePath) || "null".equals(appAbsolutePath)) {
            throw new IllegalArgumentException("appAbsolutePath must can not null, or that must can not recommend");
        }
        File file = new File(appAbsolutePath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        return file;
    }

    private File inspectionLegalityPath(File directory, String fileName) {
        if (null == directory) {
            throw new IllegalArgumentException("directory must can not null");
        }
        if (null == fileName || "".equals(fileName) || "null".equals(fileName)) {
            throw new IllegalArgumentException("fileName must can not null, or that must can not recommend");
        }
        File file = new File(directory, fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private String dispatchPath() {
        String path = null;
        if (storageStrategy == StorageStrategy.STRATEGY_NONE || storageStrategy == StorageStrategy.STRATEGY_NO_RECOMMEND) {
            path = interruptPath();
        } else {
            path = interruptPath(storageStrategy);
        }
//        if (DEBUG)
//            Log.d(TAG, "the current inspection path is " + path);
        return path;
    }

    private String interruptPath(@Nullable StorageStrategy storageState) {
        String appAbsolutePath = null;
        if (storageState == StorageStrategy.STRATEGY_INTERNAL) {
            appAbsolutePath = context.getDir(CRASH, Context.MODE_PRIVATE).toString();
        } else if (storageState == StorageStrategy.STRATEGY_EXTERNAL) {
            appAbsolutePath = interruptPath();
        } else {
            // nothing to do
        }
        return appAbsolutePath;
    }

    private String interruptPath() {
        String appAbsolutePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                //noinspection ConstantConditions
                appAbsolutePath = context.getExternalFilesDir(CRASH).toString();
                storageStrategy = StorageStrategy.STRATEGY_EXTERNAL;
            } catch (NullPointerException e) {
                appAbsolutePath = context.getDir(CRASH, Context.MODE_PRIVATE).toString();
                storageStrategy = StorageStrategy.STRATEGY_NO_RECOMMEND;
            }
        } else {
            appAbsolutePath = context.getDir(CRASH, Context.MODE_PRIVATE).toString();
            storageStrategy = StorageStrategy.STRATEGY_INTERNAL;
        }
        return appAbsolutePath;
    }

    private File obtainFile(@NonNull String appAbsolutePath) {
        return inspectionLegalityPath(inspectionLegalityPath(appAbsolutePath), SimpleDateUtil.getPrintToDirectoryTime());
    }

    public String getDirectory() {
        return directory;
    }
}
