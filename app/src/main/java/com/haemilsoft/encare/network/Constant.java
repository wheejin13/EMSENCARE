package com.haemilsoft.encare.network;

import android.os.Environment;

import com.haemilsoft.encare.BuildConfig;

public class Constant {
    public static final String COM_CD = "0000"; // TODO : PUSH Token 등록 시 사용할 계획으로 보였으나 현재 필요없음. 패키지 명으로 대체할 방법을 찾아야됨.
//    public static final String AUTH_URL = "http://emsm.haemilsoft.com/pages/login.aspx";
    public static final String AUTH_URL = "http://emsmv3.haemilsoft.com/pages/login.aspx";
//    public static final String DOWNLOAD_URL = "http://emsm.haemilsoft.com/";
    public static final String DOWNLOAD_URL = "http://emsmv3.haemilsoft.com/";
    public static final String DOWNLOAD_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    public static final String PROVIDER_AUTHORITIES = BuildConfig.APPLICATION_ID + ".fileprovider";
}
