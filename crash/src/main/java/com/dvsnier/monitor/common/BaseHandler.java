package com.dvsnier.monitor.common;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dvsnier.crash.processor.ICrash;
import com.dvsnier.crash.processor.StorageStrategy;

/**
 * BaseHandler
 * Created by DovSnier on 2016/5/5.
 */
public abstract class BaseHandler implements ICrash, ITag {

    /* the current class identification*/
//    protected final String TAG = this.getClass().getSimpleName();
    /* the current runtime mode*/
    protected boolean DEBUG;
    /* the context object*/
    protected Context context;
    /* the current storage strategy*/
    protected StorageStrategy storageStrategy;

    public BaseHandler() {
    }

    public BaseHandler(@NonNull Context context, @Nullable StorageStrategy storageStrategy) {
        this.context = context;
        this.storageStrategy = storageStrategy;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean isDebug() {
        return DEBUG;
    }

    @Override
    public void setDebug(boolean debug) {
        this.DEBUG = debug;
    }

    /**
     * gets the current equipment abnormal storage condition
     *
     * @return the current equipment abnormal storage condition
     * @version 0.0.1
     */
    public StorageStrategy getStorageState() {
        return storageStrategy;
    }

    /**
     * set the current equipment abnormal storage condition
     *
     * @param storageState {@link StorageStrategy}
     * @version 0.0.2
     */
    public void setStorageState(StorageStrategy storageState) {
        this.storageStrategy = storageState;
        initialize(context, storageState);
    }

    @CallSuper
    @Override
    public void initialize(@NonNull Context context) {
        this.context = context;
    }

    @CallSuper
    @Override
    public void initialize(@NonNull Context context, @Nullable StorageStrategy storageState) {
        this.context = context;
    }

    @CallSuper
    @Override
    public void shutdown() {
    }
}
