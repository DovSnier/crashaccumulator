package com.dvsnier.monitor.config;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

/**
 * SharedPreferences
 * Created by dovsnier on 2018/6/27.
 */
public class SharedPreferences implements ISharedPreferences {

    protected Context context;
    protected android.content.SharedPreferences sharedPreferences;
    protected android.content.SharedPreferences.Editor editor;

    private SharedPreferences() {
    }

    public SharedPreferences(@NonNull Context context) {
        this.context = context;
        //noinspection ConstantConditions
        if (null != context)
            sharedPreferences = this.context.getSharedPreferences(Config.DVS_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    public static ISharedPreferences obtain(@NonNull Context context) {
        return new SharedPreferences(context);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public ISharedPreferences onEdit() {
        if (null == editor) {
            editor = sharedPreferences.edit();
        }
        return this;
    }

    @Override
    public ISharedPreferences putString(@NonNull String key, String value) {
        onEdit();
        editor.putString(key, value);
        return this;
    }

    @Override
    public ISharedPreferences putLong(@NonNull String key, long value) {
        onEdit();
        editor.putLong(key, value);
        return this;
    }

    @Override
    public void commit() {
        onEdit();
        editor.commit();
    }
}
