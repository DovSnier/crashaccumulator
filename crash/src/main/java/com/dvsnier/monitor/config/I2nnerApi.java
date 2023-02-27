package com.dvsnier.monitor.config;

import androidx.annotation.NonNull;

/**
 * internal private api, unstable, and subsequent possible culling.
 * Created by dovsnier on 2016/1/11.
 */
public interface I2nnerApi {

    /**
     * initialize some sdk information
     */
    void onSdkInfo();

    /**
     * record default device storage strategy
     */
    void onStorageStrategy();

    /**
     * record the exception information to the storage device
     *
     * @param alias the file name
     */
    void writeAbnormityInfo(@NonNull String alias);
}
