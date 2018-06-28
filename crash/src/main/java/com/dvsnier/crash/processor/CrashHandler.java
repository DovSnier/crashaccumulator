package com.dvsnier.crash.processor;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dvsnier.crash.IUncaughtExceptionHandler;
import com.dvsnier.crash.R;
import com.dvsnier.monitor.common.BaseHandler;
import com.dvsnier.monitor.common.FileHandler;
import com.dvsnier.monitor.utils.SimpleDateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * <pre>
 * CrashHandler
 * </pre>
 *
 * @author lizw
 * @version 1.2.6
 * @since jdk 1.7
 */
public class CrashHandler extends BaseHandler implements Thread.UncaughtExceptionHandler {

    protected Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    protected IUncaughtExceptionHandler uncaughtHandler;
    protected static CrashHandler crashHandler;
    protected FileHandler handler;
    protected String tips;

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
        if (null == crashHandler) {
            synchronized (CrashHandler.class) {
                if (null == crashHandler) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    @Override
    public final void initialize(@NonNull Context context) {
        super.initialize(context);
        execute(context, null);
    }

    @Override
    public final void initialize(@NonNull Context context, @Nullable StorageStrategy storageState) {
        super.initialize(context, storageState);
        execute(context, storageState);
    }

    @Override
    public final void shutdown() {
        super.shutdown();
        storageStrategy = StorageStrategy.STRATEGY_NONE;
        if (null != handler) {
            handler.shutdown();
            handler = null;
        }
        if (null != crashHandler) {
            crashHandler = null;
            context = null;
        }
    }

    protected void execute(@NonNull Context context, @Nullable StorageStrategy storageStrategy) {
        Thread.setDefaultUncaughtExceptionHandler(this);
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.storageStrategy = storageStrategy;
        onStorageStrategy();
    }

    private void onStorageStrategy() {
        if (null == handler) {
            handler = new FileHandler(getContext(), getStorageState(), isDebug());
        }
        //noinspection ConstantConditions
        if (handler instanceof ITask) {
            ((ITask) handler).execute();
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (null != uncaughtHandler) {
                uncaughtHandler.uncaughtException(thread, ex);
            }
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
        if (null == throwable || null == handler) {
            return false;
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        try {
            Throwable cause = throwable.getCause();
            if (null != cause) {
                cause.printStackTrace(printWriter);
//                Class<? extends Throwable> clazz = throwable.getClass();
//                printWriter.write(clazz.getSimpleName());
            }
        } catch (NullPointerException e) {
        } finally {
            printWriter.close();
        }
        final String result = writer.toString();
        final String type = throwable.getClass().getSimpleName();
        final String message = throwable.getMessage();
        new Thread() {

            @Override
            public void run() {
                if (null == handler) return;
                Looper.prepare();
                String value;
                if (null != getTips() && !"".equals(getTips())) {
                    value = getTips();
                } else {
                    value = context.getResources().getString(R.string.crash_error);
                }
                Toast.makeText(context, value, Toast.LENGTH_LONG).show();
                final String fileName = SimpleDateUtil.obtainFileName();
                File file = new File(handler.getDirectory(), fileName);
                if (!file.exists()) {
                    try {
                        //noinspection ResultOfMethodCallIgnored
                        file.createNewFile();
                        //noinspection ResultOfMethodCallIgnored
                        file.setWritable(Boolean.TRUE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.writeAbnormityInfo(fileName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file, true);
                    String content = "=>date = " + SimpleDateUtil.getPrintToTextTime() + "\r\n"
                            + "=>type = " + String.format(Locale.getDefault(), "%-9s", type) + "\r\n"
                            + "=>msgs = " + String.format(Locale.getDefault(), "%-9s", message);
                    fos.write(content.getBytes());
                    fos.write(result.getBytes());
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != fos) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Looper.loop();
            }
        }.start();
        return true;
    }

    public IUncaughtExceptionHandler getUncaughtHandler() {
        return uncaughtHandler;
    }

    public void setUncaughtHandler(IUncaughtExceptionHandler uncaughtHandler) {
        this.uncaughtHandler = uncaughtHandler;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}