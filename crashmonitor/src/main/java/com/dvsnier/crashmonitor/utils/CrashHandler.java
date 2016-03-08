package com.dvsnier.crashmonitor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
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
 * @version 1.2.5
 * @since jdk 1.7
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    private static String directory = "";
    /* the current runtime mode*/
    protected static boolean DEBUG = false;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /* the context object*/
    protected Context context;
    private static CrashHandler crashHandler;
    /* the current storage strategy*/
    protected StorageStrategy storageState;

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
     * @param context {@link Context}
     * @version 0.0.2
     */
    public final void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        storageState = StorageStrategy.STRATEGY_NONE;
        inspectionAndInitializedFileSystem(context);
    }

    /**
     * the init global default uncaught exception handler <br>
     * 2015-5-13
     *
     * @param context      {@link Context}
     * @param storageState {@link StorageStrategy}
     * @version 0.0.3
     */
    public final void init(Context context, StorageStrategy storageState) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.storageState = storageState;
        inspectionAndInitializedFileSystem(context, storageState);
    }

    /**
     * the stop crash handle instance
     *
     * @version 1.0.0
     */
    public final void stop() {
        storageState = StorageStrategy.STRATEGY_NONE;
        if (null != crashHandler) {
            crashHandler = null;
        }
    }


    /**
     * to inspection and to initiation crash directory structure<br>
     * 2015-5-13
     *
     * @param context {@link Context}
     * @version 1.0.5
     */
    protected void inspectionAndInitializedFileSystem(Context context) {
        String appAbsolutePath = dispatchPath();
        File fileDate = obtainFile(appAbsolutePath);
        directory = fileDate.getAbsolutePath();
        if (DEBUG) {
            Log.i(CrashHandler.class.getSimpleName(), "the current crash path is " + directory);
        }
    }

    private File obtainFile(String appAbsolutePath) {
        return inspectionLegalityPath(inspectionLegalityPath(appAbsolutePath), getPrintToDirectoryTime());
    }

    /**
     * to inspection and to initiation crash directory structure<br>
     * 2015-5-13
     *
     * @param context      {@link Context}
     * @param storageState {@link StorageStrategy}
     * @version 1.0.6
     */
    protected void inspectionAndInitializedFileSystem(Context context, StorageStrategy storageState) {
        if (null == storageState) {
            throw new IllegalArgumentException("the current storage strategy must not be empty.");
        }
        String appAbsolutePath = dispatchPath();
        File fileDate = obtainFile(appAbsolutePath);
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

    private String dispatchPath() {
        String path = null;
        if (storageState == StorageStrategy.STRATEGY_NONE || storageState == StorageStrategy.STRATEGY_NO_RECOMMEND) {
            path = interruptPath();
        } else {
            path = interruptPath(storageState);
        }
//        Log.d(TAG, "the current inspection path is " + path);
        return path;
    }

    private String interruptPath(StorageStrategy storageState) {
        String appAbsolutePath = null;
        if (storageState == StorageStrategy.STRATEGY_INTERNAL) {
            appAbsolutePath = context.getDir("crash", Context.MODE_PRIVATE).toString();
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
     * @param throwable {@link Throwable}
     * @return true said developers to handle the exception, is responsible for the submission for the system to deal with by default
     * @version 0.0.2
     */
    private boolean handleException(final Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        try {
            Throwable cause = throwable.getCause();
            cause.printStackTrace(printWriter);
        } catch (NullPointerException e) {
        } finally {
            printWriter.close();
        }
        final String result = writer.toString();
        final String message = throwable.getMessage();
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, context.getResources().getString(R.string.crash_error), Toast.LENGTH_LONG).show();
                String fileName = obtainFileName();
                File file = new File(directory, fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        file.setWritable(Boolean.TRUE);
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

    private String obtainFileName() {
        return "crash_" + getPrintToFileTime() + ".log";
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
     * @version 0.0.2
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPrintToFileTime() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
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
     * @version 0.0.2
     */
    public void setStorageState(StorageStrategy storageState) {
        this.storageState = storageState;
        init(context, storageState);
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