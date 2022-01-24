package com.haemilsoft.encare.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

/**
 * Created by ssw on 2016-09-29.
 */

public class LOG {

    private static final boolean DEBUGGABLE = true;
    private static final boolean JSONLOGGABLE = true;
    private static final String TAG = "[EMS]";

    /** Log Level Error **/
    public static void e() {
        if (DEBUGGABLE) Log.e(TAG, buildLogMsg(""));
    }

    public static void e(final String message) {
        if (DEBUGGABLE) Log.e(TAG, buildLogMsg(message));
    }

    public static void e(final String TAG, final String message) {
        if (DEBUGGABLE) Log.e(TAG, buildLogMsg(message));
    }

    /** Log Level Warning **/
    public static void w() {
        if (DEBUGGABLE) Log.w(TAG, buildLogMsg(""));
    }

    public static void w(final String message) {
        if (DEBUGGABLE) Log.w(TAG, buildLogMsg(message));
    }

    public static void w(final String TAG, final String message) {
        if (DEBUGGABLE) Log.w(TAG, buildLogMsg(message));
    }

    /** Log Level Information **/
    public static void i() {
        if (DEBUGGABLE) Log.i(TAG, buildLogMsg(""));
    }

    public static void i(final String message) {
        if (DEBUGGABLE) Log.i(TAG, buildLogMsg(message));
    }

    public static void i(final String TAG, final String message) {
        if (DEBUGGABLE) Log.i(TAG, buildLogMsg(message));
    }

    /** Log Level Debug **/
    public static void d() {
        if (DEBUGGABLE) Log.d(TAG, buildLogMsg(""));
    }

    public static void d(final String message) {
        if (DEBUGGABLE) Log.d(TAG, buildLogMsg(message));
    }

    public static void d(final String TAG, final String message) {
        if (DEBUGGABLE) Log.d(TAG, buildLogMsg(message));
    }

    /** Log Level Verbose **/
    public static void v() {
        if (DEBUGGABLE) Log.v(TAG, buildLogMsg(""));
    }

    public static void v(final String message) {
        if (DEBUGGABLE) Log.v(TAG, buildLogMsg(message));
    }

    public static void v(final String TAG, final String message) {
        if (DEBUGGABLE) Log.v(TAG, buildLogMsg(message));
    }

    private static String buildLogMsg(final String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(ste.getFileName().replace(".java", "")).append("::").append(ste.getMethodName()).append("()").append("] ").append(message);
//        FileLogging.Write(sb.toString());
        return sb.toString();
    }

    public static void printJsonMsg(final String message) {
        if (JSONLOGGABLE) {
            if (message != null) {
                try {
                    LOG.i("Response Message : ");
                    String jsonData = (new JSONObject(message)).toString(3);
                    StringTokenizer tokenizer = new StringTokenizer(jsonData, "\n");
                    while (tokenizer.hasMoreTokens()) {
                        LOG.i(tokenizer.nextToken());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LOG.e("received msg is null");
            }
        }
    }
}
