package com.haemilsoft.encare.network;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.haemilsoft.encare.R;
import com.haemilsoft.encare.utils.IToast;
import com.haemilsoft.encare.view.dialog.LoadingProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by swseo on 2020-12-09.
 */

public class FileDownloadAsyncTask extends AsyncTask<String, Integer, String> {
    private final LoadingProgressDialog _LoadingProgress;
    private final WeakReference<Context> _Context;
    private final String _FileExtension;

    public FileDownloadAsyncTask(Context context, String fileExtension) {
        _Context = new WeakReference<>(context);
        _FileExtension = fileExtension;
        _LoadingProgress = new LoadingProgressDialog(_Context.get());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _LoadingProgress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL fileDownloadUrl = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) fileDownloadUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = fileDownloadUrl.openStream();
            FileOutputStream fos = new FileOutputStream(new File(Constant.DOWNLOAD_DIRECTORY + params[1]));

            int read;
            while ((read = is.read()) != -1) {
                fos.write(read);
            }

            fos.flush();
            fos.close();
            is.close();
            conn.disconnect();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return Constant.DOWNLOAD_DIRECTORY + params[1];
    }

    @Override
    protected void onPostExecute(String downloadPath) {
        super.onPostExecute(downloadPath);

        _LoadingProgress.dismiss();

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri = FileProvider.getUriForFile(_Context.get(), Constant.PROVIDER_AUTHORITIES, new File(downloadPath));
            } else {
                fileUri = Uri.fromFile(new File(downloadPath));
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(fileUri, IntentFileType.get(_FileExtension));
            _Context.get().startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            IToast.show(_Context.get(), _Context.get().getString(R.string.file_open_err_msg));
        } catch (Exception ex) {
            ex.printStackTrace();
            IToast.show(_Context.get(), _Context.get().getString(R.string.unexpected_err_found));
        }
    }
}

