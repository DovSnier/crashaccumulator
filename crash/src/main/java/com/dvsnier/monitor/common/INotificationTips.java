package com.dvsnier.monitor.common;

import java.io.File;

/**
 * the simple notify Tips
 * Created by dovsnier on 2020/7/15.
 */
public interface INotificationTips {

    /**
     * the exception message notification
     *
     * @param type    the exception type
     * @param content the tips content
     * @param file    the resource file
     */
    void onNotifyMsg(String type, String content, File file);
}
