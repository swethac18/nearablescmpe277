package com.estimote.notification;

import android.app.Application;

import com.estimote.cloud_plugin.common.EstimoteCloudCredentials;
import com.estimote.internal_plugins_api.cloud.CloudCredentials;
import com.estimote.notification.estimote.NotificationsManager;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class Tracker4Pet extends Application {

    public CloudCredentials cloudCredentials = new EstimoteCloudCredentials("beacon-notifier-2eq", "ab789a4f075641489c55a75e91ed6ffd");
    private NotificationsManager notificationsManager;

    public void enableBeaconNotifications() {
        notificationsManager = new NotificationsManager(this);
    }


    public NotificationsManager getNotificiationsManager() {
        return notificationsManager;
    }

}
