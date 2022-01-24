package com.haemilsoft.encare.fms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.haemilsoft.encare.BuildConfig;
import com.haemilsoft.encare.R;

import java.util.Map;
import java.util.Random;

public class EMSNotificationManager {

    private static final String GROUP_KEY_WORK_PUSH = BuildConfig.APPLICATION_ID + ".EMS_PUSH";
    private static final String CHANNEL_NAME = "EMS PUSH ALARM";
    private static final String CHANNEL_ID = "EMS_PUSH_ALARM";
    private static final int SUMMARY_ID = 191992317;

    private static EMSNotificationManager INSTANCE;
    private static Context CONTEXT;
    private static NotificationManager MANAGER;

    private EMSNotificationManager(Context context) {
        if (context != null) {
            CONTEXT = context;
            MANAGER = (NotificationManager) CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);

            // 채널에 대한 정보는 SharedPref 에서 저장 하고 있다.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[] {100, 200, 100, 200});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                MANAGER.createNotificationChannel(channel);
            }
        }
    }

    public static EMSNotificationManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (EMSNotificationManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new EMSNotificationManager(context);
            }
        }
        return INSTANCE;
    }

    public void send(Map<String, String> message) {
        if (message != null) {
            MANAGER.notify(getNewNotificationId(), buildNotification(message));
            MANAGER.notify(SUMMARY_ID, buildSummary());
        }
    }

    private int getNewNotificationId() {
        return new Random().nextInt(2147483600);
    }

    private Notification buildNotification(Map<String, String> message) {
        return new NotificationCompat.Builder(CONTEXT, CHANNEL_ID)
                .setContentTitle(message.get("title"))
                .setContentText(message.get("msg"))
                .setSmallIcon(R.mipmap.noti_img)
                .setSound(getDefaultSound())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup(GROUP_KEY_WORK_PUSH)
                .build();
    }

    private Notification buildSummary() {
        return new NotificationCompat.Builder(CONTEXT, CHANNEL_ID)
                .setSmallIcon(R.mipmap.noti_img)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setSummaryText(CONTEXT.getString(R.string.push_group_summary)))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_KEY_WORK_PUSH)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();
    }

    private Uri getDefaultSound() {
        return RingtoneManager.getActualDefaultRingtoneUri(CONTEXT, RingtoneManager.TYPE_NOTIFICATION);
    }

    private long[] getDefaultVibrate() {
        return new long[] { 0, 3000, 100, 300};
    }

    /*
     * TODO : User에 따른 사운드 조정이 필요한지는 추후 시스템 검토 필요.
     */
    private Uri getSound(Map<String, String> message) {
        String audio1 = message.get("audio1");
        String audio2 = message.get("audio2");
        String audio3 = message.get("audio3");

        String mode = "0";

        AudioManager am = (AudioManager)CONTEXT.getSystemService(Context.AUDIO_SERVICE);
        switch(am.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mode = audio3;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                if (audio2.equals("2")) {
                    am.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, 0);
                }
                mode = audio2;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                if (audio1.equals("1") || audio1.equals("2")) {
                    am.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, 0);
                }
                mode = audio1;
        }

        if (mode.equals("2")) {
            return RingtoneManager.getActualDefaultRingtoneUri(CONTEXT, RingtoneManager.TYPE_NOTIFICATION);
        } else
            return null;
    }
}
