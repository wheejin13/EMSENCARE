package com.haemilsoft.encare.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

//    public static final String PREF_CHECK_SAVE_ID = "USER_CHECK_SAVE_ID";
//    public static final String PREF_USER_ID = "USER_ID";
//    public static final String PREF_USER_PWD = "USER_PWD";

    public static final String PREF_PHONE_NUM = "pnum";
    public static final String PREF_DEVICE_ID = "deviceId";
    public static final String PREF_PUSH_TOKEN  = "token";

    private final String PREF_NAME = "EMS_SHARED_PREF";

    private final SharedPreferences mPref;
    private final SharedPreferences.Editor mEditor;

    private static SharedPref INSTANCE;

    public static synchronized SharedPref getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SharedPref(context);
        return INSTANCE;
    }

    private SharedPref(Context context) {
        mPref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public String getString(String key, String dftValue) {
        try {
            return mPref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public int getInt(String key, int dftValue) {
        try {
            return mPref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public boolean getBoolean(String key, boolean dftValue) {
        try {
            return mPref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public boolean contains(String key) {
        return mPref.contains(key);
    }

//    public void saveLoginInfo(String id, String pwd) {
//        this.setBoolean(PREF_CHECK_SAVE_ID, true);
//        this.setString(PREF_USER_ID, id);
//        this.setString(PREF_USER_PWD, pwd);
//    }
}
