package com.sec.android.app.hwmoduletest;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class FingerPrintTest_warningDiag implements OnClickListener {
    private String CLASS_NAME = "FingerPrintTest_warningDiag";
    private Context mContext;
    private Dialog mDialog;

    public FingerPrintTest_warningDiag(Context context, View view) {
        this.mContext = context;
        Builder builder = new Builder(context, 16973939);
        builder.setView(view);
        builder.setPositiveButton("Confirm", this);
        builder.setCancelable(false);
        this.mDialog = builder.create();
        this.mDialog.setCanceledOnTouchOutside(false);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            dismiss();
        }
    }

    public void show() {
        if (this.mDialog != null) {
            this.mDialog.show();
        }
    }

    public void dismiss() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
    }

    public void setClassName(String name) {
        this.CLASS_NAME = name;
    }
}
