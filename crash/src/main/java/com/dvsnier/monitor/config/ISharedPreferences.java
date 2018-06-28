package com.dvsnier.monitor.config;

import android.support.annotation.NonNull;

/**
 * storage interface
 * Created by dovsnier on 2018/6/27.
 */
public interface ISharedPreferences {

    /**
     * the edit mode
     *
     * @return {@see ISharedPreferences}
     */
    ISharedPreferences onEdit();

    /**
     * the store string data
     *
     * @param key   the key
     * @param value the value
     * @return {@see ISharedPreferences}
     */
    ISharedPreferences putString(@NonNull String key, String value);

    /**
     * the store long data
     *
     * @param key   the key
     * @param value the value
     * @return {@see ISharedPreferences}
     */
    ISharedPreferences putLong(@NonNull String key, long value);

    /**
     * the store commit resource
     */
    void commit();
}
