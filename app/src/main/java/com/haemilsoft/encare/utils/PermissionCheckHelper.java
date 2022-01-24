package com.haemilsoft.encare.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by ssw on 2017-03-03.
 */

public class PermissionCheckHelper {

    // 반드시 설정이 되어 있어야 하는 앱의 기본 권한들
	private static final String[] BASIC_PERMISSION = new String[] {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    // 기본 권한 중 설정이 해제가 되어 있는 권한들.
    private static ArrayList<String> PERMISSION_TO_REQUEST;

    // Permission요청 시 사용될 requestCode.
    public static final int PERMISSION_REQUEST_CODE = 100;

    // Permissions에 해당하는 각 Permission의 설정 여부를 return한다.
    public static boolean lackOfBasicPermissions(Context context) {
        if (hasPermissionToRequest())
            PERMISSION_TO_REQUEST.clear();

        for (String permission : BASIC_PERMISSION) {
            if (lacksPermission(context, permission)) {
                setPermissionToRequest(permission);
            }
        }

        return hasPermissionToRequest();
    }

    /* 확인 요청한 모든 Permission의 설정 여부를 체크하여 return한다.
     * 하나라도 DENIED된 상태라면 false.
     */
    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // OS에서 다이얼로그를 띄워 Permission 설정 요청을 하여 User에게 알려준다.
    public static void requestPermissions(Activity activity) {
        if (activity != null) {
            if (hasPermissionToRequest()) {
                ActivityCompat.requestPermissions(activity, PERMISSION_TO_REQUEST.toArray(new String[PERMISSION_TO_REQUEST.size()]), PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(activity, BASIC_PERMISSION, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private static boolean hasPermissionToRequest() {
        return PERMISSION_TO_REQUEST != null && PERMISSION_TO_REQUEST.size() > 0;
    }

    private static void setPermissionToRequest(String permission) {
        if (PERMISSION_TO_REQUEST == null) {
            PERMISSION_TO_REQUEST = new ArrayList<>();
        }

        if (!PERMISSION_TO_REQUEST.contains(permission)) {
            PERMISSION_TO_REQUEST.add(permission);
        }
    }

    // Permission 설정 여부를 return한다.
    private static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }
}
