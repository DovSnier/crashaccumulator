package com.dvsnier.monitor.common;

import android.content.Context;

/**
 * Created by DovSnier on 2016/5/5.
 */
public class BaseHandler {

    /* the current class identification*/
    protected final String TAG = getClass().getSimpleName();
    /* the current runtime mode*/
    protected static boolean DEBUG = false;
    /* the context object*/
    protected Context context;
}
