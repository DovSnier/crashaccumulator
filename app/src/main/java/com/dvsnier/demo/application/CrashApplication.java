package com.dvsnier.demo.application;

import android.app.Application;

import com.dvsnier.crash.Crash;

/**
 * Crash Application
 * Created by dovsnier on 2018/6/28.
 */
public class CrashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crash.initialize(this);
//        Crash.initialize(this, true);
//        Crash.initialize(this, false);
//        Crash.initialize(this, true, "测试崩溃提示语...");
    }

}
