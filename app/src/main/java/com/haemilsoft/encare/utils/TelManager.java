package com.haemilsoft.encare.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

public class TelManager {

    @SuppressLint("HardwareIds")
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static synchronized String GetDeviceId(Context context) {
        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (deviceId == null) deviceId = tm.getDeviceId();
            if (deviceId == null) deviceId = "";

            return deviceId;
        }

        return "";
    }

    @SuppressLint("HardwareIds")
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static synchronized String GetPhoneNumber(Context context) {
        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return StringUtils.ConvertToPhoneNumberFormat(tm.getLine1Number());
        }

        return "";
    }
}
