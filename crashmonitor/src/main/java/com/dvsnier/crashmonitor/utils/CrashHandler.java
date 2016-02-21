package com.dvsnier.crashmonitor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.dvsnier.crashmonitor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

/**
 * <pre>
 * CrashHandler
 * </pre>
 *
 * @author lizw
 * @version 1.2.2
 * @since jdk 1.7
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    private static String directory = "";
    private static boolean DEBUG = false;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context context;
    private static CrashHandler crashHandler;
    private StorageStrategy storageState;

    private CrashHandler() {
    }

    /**
     * the single instance mode<br>
     * 2015-5-13
     *
     * @return
     * @version 0.0.1
     */
    public static CrashHandler getInstance() {
        synchronized (CrashHandler.class) {
            if (null == crashHandler) {
                crashHandler = new CrashHandler();
            }
        }
        return crashHandler;
    }

    /**
     * the init global default uncaught exception handler <br>
     * 2015-5-13
     *
     * @version 0.0.2
     */
    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        storageState = StorageStrategy.STRATEGY_NONE;
        makeFile(context);
    }

    /**
     * to make crash directory structure<br>
     * 2015-5-13
     *
     * @version 1.0.4
     */
    protected void makeFile(Context context) {
        String appAbsolutePath = inspectionPath();
        File fileDate = inspectionLegalityPath(inspectionLegalityPath(appAbsolutePath), getPrintToDirectoryTime());
        directory = fileDate.getAbsolutePath();
        if (DEBUG) {
            Log.i(CrashHandler.class.getSimpleName(), "the current crash path is " + directory);
        }
    }

    private File inspectionLegalityPath(String appAbsolutePath) {
        if (null == appAbsolutePath || "".equals(appAbsolutePath) || "null".equals(appAbsolutePath)) {
            throw new IllegalArgumentException("appAbsolutePath must can not null, or that must can not recommend");
        }
        File file = new File(appAbsolutePath);
        if (!file.exists()) {
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

    private String inspectionPath() {
        String path = inspectionAbsolutePath();
//        Log.d(TAG, "the current inspection path is " + path);
        return path;
    }

    private String inspectionAbsolutePath() {
        String appAbsolutePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                appAbsolutePath = context.getExternalFilesDir("crash").toString();
                storageState = StorageStrategy.STRATEGY_EXTERNAL;
            } catch (NullPointerException e) {
                appAbsolutePath = context.getDir("crash", Context.MODE_PRIVATE).toString();
                storageState = StorageStrategy.STRATEGY_NO_RECOMMEND;
            }
        } else {
            appAbsolutePath = context.getDir("crash", Context.MODE_PRIVATE).toString();
            storageState = StorageStrategy.STRATEGY_INTERNAL;
        }
        return appAbsolutePath;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //TODO the default uncaught handle, maybe your decide
//            uncaughtHandle();
            //the current default handle is closed application
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * the custom handle exception <br>
     * 2015-5-13
     *
     * @param ex {@link Throwable}
     * @return true said developers to handle the exception, is responsible for the submission for the system to deal with by default
     * @version 0.0.1
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        try {
            Throwable cause = ex.getCause();
            cause.printStackTrace(printWriter);
        } catch (NullPointerException e) {
        } finally {
            printWriter.close();
        }
        final String result = writer.toString();
        final String message = ex.getMessage();
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, context.getResources().getString(R.string.crash_error), Toast.LENGTH_LONG).show();
                String fileName = "crash_" + getPrintToFileTime() + ".log";
                File file = new File(directory, fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file, true);
                    fos.write(("=>" + "date = " + getPrintToTextTime() + "\r\n" + "=>msgs = " + message).getBytes());
                    fos.write(result.getBytes());
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Looper.loop();
            }
        }.start();
        return true;
    }


    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 1.0.1
     */
    @Deprecated
    public static String getPrintToFileTime(String format) {
        String date = "";
        SimpleDateFormat sdf = null;
        String DEFAULT_FORMAT = "yyyy_MMdd_hhmm_ss";
        if (null != format && !"".equals(format)) {
            try {
                sdf = new SimpleDateFormat(format);
            } catch (Exception e) {
                sdf = new SimpleDateFormat(DEFAULT_FORMAT);
            }
        } else {
            sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        }
        date = sdf.format(System.currentTimeMillis());
        return date;
    }


    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.1
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPrintToFileTime() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_hhmm");
        date = sdf.format(System.currentTimeMillis());
        return date;
    }

    /**
     * get the current system date<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.2
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPrintToTextTime() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = sdf.format(System.currentTimeMillis());
        return date;
    }

    /**
     * get the current system date with month and day<br>
     * 2015-4-28
     *
     * @return the current system date
     * @version 0.0.1
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPrintToDirectoryTime() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        date = sdf.format(System.currentTimeMillis());
        return date;
    }

    /**
     * gets the current equipment abnormal storage condition
     *
     * @return the current equipment abnormal storage condition
     * @version 0.0.1
     */
    public StorageStrategy getStorageState() {
        return storageState;
    }

    /**
     * set the current equipment abnormal storage condition
     *
     * @param storageState {@link StorageStrategy}
     * @version 0.0.1
     */
    public void setStorageState(StorageStrategy storageState) {
        this.storageState = storageState;
    }

    /**
     * identify storage strategy
     * 2015-12-28
     *
     * @version 0.0.2
     */
    public enum StorageStrategy {
        STRATEGY_INTERNAL, STRATEGY_EXTERNAL, STRATEGY_NONE, STRATEGY_NO_RECOMMEND
    }
}