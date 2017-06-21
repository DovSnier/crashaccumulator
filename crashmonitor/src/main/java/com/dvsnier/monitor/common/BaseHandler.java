package com.dvsnier.monitor.common;

import android.content.Context;

/**
 * Created by DovSnier on 2016/5/5.
 */
public class BaseHandler {

    /* the current class identification*/
    protected final String TAG = this.getClass().getSimpleName();
    /* the current runtime mode*/
    protected boolean DEBUG;
    /* the context object*/
    protected Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
