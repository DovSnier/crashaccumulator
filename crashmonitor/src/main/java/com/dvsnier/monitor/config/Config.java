package com.dvsnier.monitor.config;

/**
 * to configure the environment variables set<br/>
 * Created by DovSnier on 2016/5/5.
 *
 * @version 0.0.1
 * @since JDK 1.7
 */
public class Config {

    /* the current config name */
    public static String DVS_CONFIG_NAME = "dvs_config";
    /* the current sdk version */
    public static final String DVS_CONFIG_VERSION = "0.0.3";


    public static class Key {

        public static String KEY_SDK_VERSION = "sdk_version";
        public static String KEY_ENVIRONMENT_MODE = "mode";
        public static String KEY_STRATEGY = "strategy";
        public static String KEY_LAST_TIME = "last_time";
        public static String KEY_LAST_NAME = "last_name";

    }
}
