package com.estimote.notification.estimote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.estimote.notification.MainActivity;
import com.estimote.notification.Tracker4Pet;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class NotificationsManager {

    private Context context;
    private NotificationManager notificationManager;
    private Notification notification;
    private int notificationId = 1;
    public static int OutOfRangeCounter = 0;
    public static boolean continuePlaying = false;
    public static ProximityObserver proximityObserver = null;
    public static MediaPlayer mediaPlayer = null;

    public static HashMap<Integer, Double> Entry = new HashMap<>();
    public static HashMap<Integer, Double> Exit = new HashMap<>();



    public NotificationsManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.notification = buildNotification("Tracker ALERT", "Your loved one is out of range!!");

    }

    private Notification buildNotification(String title, String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel contentChannel = new NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(contentChannel);
        }

        return new NotificationCompat.Builder(context, "content_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }

    public void lostAndFound(String beaconName) {
        proximityObserver = null;
        proximityObserver =
                new ProximityObserverBuilder(context, ((Tracker4Pet) context).cloudCredentials)
                        .withOnErrorAction(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();

        ProximityZone zone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "blue")
                .inNearRange()
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {

                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {

                        return null;
                    }
                })
                .create();

    }
    public void stopMonitoring() {
        proximityObserver = null;
    }
    public void startMonitoring() {
        startMonitoring(9999);
    }
    public void startMonitoring(float distance) {
         proximityObserver =
                new ProximityObserverBuilder(context, ((Tracker4Pet) context).cloudCredentials)
                        .withOnErrorAction(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();

        ProximityZone zone = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "blue")
                .inCustomRange(distance * 2.5)
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {

                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        if (MainActivity.isTracking) {
                            OutOfRangeCounter++;
                            notificationManager.notify(notificationId, notification);
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.setLooping(true);
                                mediaPlayer.start();
                            }

                        }
                        return null;
                    }
                })
                .create();

        ProximityZone zone1 = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "blue")
                .inCustomRange(2)
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        if (!Entry.containsKey(0))
                            Entry.put(0,0.0);
                        Entry.put(0, Entry.get(0) + 1);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Toast.makeText(context, Entry.toString(), Toast.LENGTH_LONG).show();
                        if (!Exit.containsKey(0)) {
                            Exit.put(0,0.0);
                        }
                        Exit.put(0, Exit.get(0) + 1);
                        return null;
                    }
                })
                .create();

        ProximityZone zone2 = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "blue")
                .inCustomRange(5)
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        if (!Entry.containsKey(4))
                            Entry.put(4,0.0);
                        Entry.put(4, Entry.get(4) + 1);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Toast.makeText(context, Entry.keySet().toString(), Toast.LENGTH_LONG).show();
                        if (!Exit.containsKey(4)) {
                            Exit.put(4,0.0);
                        }
                        Exit.put(4, Exit.get(0) + 1);
                        return null;
                    }
                })
                .create();

        ProximityZone zone3 = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "blue")
                .inCustomRange(10)
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        if (!Entry.containsKey(10))
                            Entry.put(10,0.0);
                        Entry.put(10, Entry.get(10) + 1);
                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Toast.makeText(context, Entry.keySet().toString(), Toast.LENGTH_LONG).show();
                        if (!Exit.containsKey(10)) {
                            Exit.put(10,0.0);
                        }
                        Exit.put(10, Exit.get(0) + 1);
                        return null;
                    }
                })
                .create();

        List<ProximityZone> zones = new ArrayList<ProximityZone>();
        zones.add(zone);
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        proximityObserver.addProximityZones(zones);
        proximityObserver.start();


    }

}
