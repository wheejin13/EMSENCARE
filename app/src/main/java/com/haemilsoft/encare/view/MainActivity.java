package com.haemilsoft.encare.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.haemilsoft.encare.R;
import com.haemilsoft.encare.network.Constant;
import com.haemilsoft.encare.network.FileDownloadAsyncTask;
import com.haemilsoft.encare.network.IntentFileType;
import com.haemilsoft.encare.utils.IToast;
import com.haemilsoft.encare.utils.LOG;
import com.haemilsoft.encare.utils.PermissionCheckHelper;
import com.haemilsoft.encare.utils.SharedPref;
import com.haemilsoft.encare.utils.StringUtils;
import com.haemilsoft.encare.utils.TelManager;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.os.Build.VERSION_CODES.M;
import static com.haemilsoft.encare.utils.SharedPref.PREF_DEVICE_ID;
import static com.haemilsoft.encare.utils.SharedPref.PREF_PHONE_NUM;
import static com.haemilsoft.encare.utils.SharedPref.PREF_PUSH_TOKEN;

public class MainActivity extends AppCompatActivity {

    private WebView _Webview;
    private final Handler _MainHandler = new Handler();
    private static final long _MessageDelayedTime = 300L;
    private long _BackPressedTime = 0;
    private SharedPref _SharedPref;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        _Webview = findViewById(R.id.webview);
        _Webview.addJavascriptInterface(new WebViewInterface(), "android");
        _Webview.setWebViewClient(new WebViewClient());

        WebSettings webSetting = _Webview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setTextZoom(100);

        _SharedPref = SharedPref.getInstance(getApplicationContext());

        if (PermissionCheckHelper.lackOfBasicPermissions(getApplicationContext())) {
            PermissionCheckHelper.requestPermissions(this);
        } else {
            LOG.i("AUTH_URL CALL");
            setDeviceInfo();
            _Webview.loadUrl(Constant.AUTH_URL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > _BackPressedTime + 2000) {
            _BackPressedTime = System.currentTimeMillis();
            toast(getString(R.string.toast_back_key_pressed));
            return;
        }

        if (System.currentTimeMillis() <= _BackPressedTime + 2000) {
            _BackPressedTime = 0;
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 모든 권한에 대해 승인하였을 경우.
        if (requestCode == PermissionCheckHelper.PERMISSION_REQUEST_CODE
                && PermissionCheckHelper.hasAllPermissionsGranted(grantResults)) {
            LOG.i("AUTH_URL CALL");
            setDeviceInfo();
            _Webview.loadUrl(Constant.AUTH_URL);
        }
        // 권한이 하나라도 거부당했을 경우.
        else {
            AtomicBoolean checkIfNeededToShowRequestPermissionRationale = new AtomicBoolean(false);

            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT >= M) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        checkIfNeededToShowRequestPermissionRationale.set(true);
                        break;
                    }
                }
            }

            if (!checkIfNeededToShowRequestPermissionRationale.get()) {
                toast(getString(R.string.permission_check_msg));
            }
        }
    }

    @SuppressLint("HardwareIds")
    private void setDeviceInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                PermissionCheckHelper.requestPermissions(this);
            }
        }

        if (_SharedPref.getString(PREF_PHONE_NUM, "").isEmpty()) {
            _SharedPref.setString(PREF_PHONE_NUM, TelManager.GetPhoneNumber(getApplicationContext()));
        }
        if (_SharedPref.getString(PREF_DEVICE_ID, "").isEmpty()) {
            _SharedPref.setString(PREF_DEVICE_ID, TelManager.GetDeviceId(getApplicationContext()));
        }
    }

    private class WebViewInterface {

        public WebViewInterface() {}

        @android.webkit.JavascriptInterface
        public void closeApp() {
            _MainHandler.post(() -> {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            });
        }

        /** 다운로드 엑셀 파일.         *
         * @param pageUrl : 함수를 호출한 페이지 URL.
         * @param fileName : 엑셀 파일 이름.
         */
        @android.webkit.JavascriptInterface
        public void downloadExcel(@NonNull final String pageUrl, @NonNull final String fileName) {
            if(!pageUrl.equals("")) {
                _MainHandler.postDelayed(() -> {
                    final String address = Constant.DOWNLOAD_URL + pageUrl;
                    FileDownloadAsyncTask task = new FileDownloadAsyncTask(getApplicationContext(), IntentFileType.FILE_TYPE_EXCEL);
                    task.execute(address, fileName);
                }, _MessageDelayedTime);
            }
        }

        /**
         * 웹으로 회사코드, PUSH TOKEN 정보를 보내준다.
         */
        @android.webkit.JavascriptInterface
        public void callAndroidInfo() {
            _MainHandler.post(() -> {
                LOG.i("callAndroidInfo call");

                _Webview.loadUrl("javascript:getAndroidInfo('" + Constant.COM_CD
                        + "', '" + _SharedPref.getString(PREF_PUSH_TOKEN, "")
                        + "', '" + _SharedPref.getString(PREF_PHONE_NUM, "")
                        + "', '" + _SharedPref.getString(PREF_DEVICE_ID, "") + "')");
            });
        }


        /**
         * 현재 사용중이지는 않음.
         * @param pref_key : 요청할 pref key 값.
         * @param callFunc : 웹에서 수행할 함수명.
         */
        @android.webkit.JavascriptInterface
        public void callAndroidInfoReturn(@NonNull final String pref_key, @NonNull final String callFunc) {
                _Webview.loadUrl("javascript:" + callFunc +
                        "('" + _SharedPref.getString(pref_key, "") + "')");
        }
    }

    protected void toast(String msg) {
        IToast.show(MainActivity.this, msg);
    }
}