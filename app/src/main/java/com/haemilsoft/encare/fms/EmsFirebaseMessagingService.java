package com.haemilsoft.encare.fms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.haemilsoft.encare.BuildConfig;
import com.haemilsoft.encare.R;
import com.haemilsoft.encare.utils.LOG;
import com.haemilsoft.encare.utils.SharedPref;
import com.haemilsoft.encare.view.MainActivity;

import java.util.Map;
import java.util.Random;

/**
 * Created by swseo on 2020-12-09.
 */

public class EmsFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        LOG.i("new token : " + token);
        SharedPref.getInstance(getApplicationContext()).setString(SharedPref.PREF_PUSH_TOKEN, token);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            EMSNotificationManager.getInstance(getApplicationContext()).send(remoteMessage.getData());
        }
    }
}
