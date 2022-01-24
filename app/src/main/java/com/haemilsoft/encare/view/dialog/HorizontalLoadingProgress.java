package com.haemilsoft.encare.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.haemilsoft.encare.R;

public class HorizontalLoadingProgress extends ProgressDialog {
    public HorizontalLoadingProgress(Context context) {
        super(context);

        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        setMessage(context.getString(R.string.download_progress_message));
        setIndeterminate(false);
        setMax(100);
    }
}
