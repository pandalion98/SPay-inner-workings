package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class zzg implements OnClickListener {
    private final Intent mIntent;
    private final Fragment zzPt;
    private final int zzPu;
    private final Activity zzoi;

    public zzg(Activity activity, Intent intent, int i) {
        this.zzoi = activity;
        this.zzPt = null;
        this.mIntent = intent;
        this.zzPu = i;
    }

    public zzg(Fragment fragment, Intent intent, int i) {
        this.zzoi = null;
        this.zzPt = fragment;
        this.mIntent = intent;
        this.zzPu = i;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            if (this.mIntent != null && this.zzPt != null) {
                this.zzPt.startActivityForResult(this.mIntent, this.zzPu);
            } else if (this.mIntent != null) {
                this.zzoi.startActivityForResult(this.mIntent, this.zzPu);
            }
            dialogInterface.dismiss();
        } catch (ActivityNotFoundException e) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}
