package com.android.internal.app;

import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import com.android.internal.R;
import com.android.internal.app.AlertController.AlertParams;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;

public class ResolverGuideActivity extends AlertActivity {
    private static final boolean DEBUG = false;
    private static final String EXTRA_SAFE_FORWARD = "extra_safe_forward";
    private static final String TAG = "ResolverGuideActivity";
    private boolean mSafeForwardingMode;

    private Intent makeMyIntent() {
        Intent originalIntent = getIntent();
        if (originalIntent == null) {
            return null;
        }
        Intent myIntent = (Intent) originalIntent.getExtra("android.intent.extra.INTENT", originalIntent);
        this.mSafeForwardingMode = ((Boolean) originalIntent.getExtra(EXTRA_SAFE_FORWARD, Boolean.valueOf(false))).booleanValue();
        myIntent.setFlags(myIntent.getFlags() & -8388609);
        return myIntent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        super.onCreate(savedInstanceState);
        int appUserId = UserHandle.myUserId();
        if (PersonaManager.isBBCContainer(appUserId)) {
            Log.d(TAG, "KEA is selected. Just start activity. UserId = " + appUserId);
            Intent intent = makeMyIntent();
            if (intent != null) {
                safelyStartActivity(intent);
            }
            finish();
            return;
        }
        AlertParams ap = this.mAlertParams;
        ap.mTitle = getResources().getText(R.string.resolver_guide_title);
        ap.mMessage = getResources().getText(R.string.clearDefaultHintMsg);
        if (UserHandle.myUserId() >= 100) {
            ap.mMessage = getResources().getText(R.string.clearDefaultHintMsg_knox);
        }
        ap.mPositiveButtonText = getResources().getText(R.string.ok);
        ap.mPositiveButtonListener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) || !ResolverGuideActivity.this.handlePendingIntent()) {
                    Intent intent = ResolverGuideActivity.this.makeMyIntent();
                    if (intent != null) {
                        ResolverGuideActivity.this.safelyStartActivity(intent);
                    }
                    ResolverGuideActivity.this.dismiss();
                }
            }
        };
        ap.mCancelable = false;
        setFinishOnTouchOutside(false);
        setupAlert();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
        if ((getIntent().getFlags() & 268435456) != 0 && !isChangingConfigurations()) {
            finish();
        }
    }

    public void onButtonClick(View v) {
        if (!PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) || !handlePendingIntent()) {
            Intent intent = makeMyIntent();
            if (intent != null) {
                safelyStartActivity(intent);
            }
            dismiss();
        }
    }

    protected boolean handlePendingIntent() {
        return false;
    }

    public void safelyStartActivity(Intent intent) {
        if (this.mSafeForwardingMode) {
            try {
                startActivityAsCaller(intent, null, false, DeviceRootKeyServiceManager.ERR_SERVICE_ERROR);
                return;
            } catch (RuntimeException e) {
                String launchedFromPackage;
                try {
                    launchedFromPackage = ActivityManagerNative.getDefault().getLaunchedFromPackage(getActivityToken());
                } catch (RemoteException e2) {
                    launchedFromPackage = "??";
                }
                Log.d(TAG, "Unable to launch  package " + launchedFromPackage + ", while running in " + ActivityThread.currentProcessName(), e);
                return;
            }
        }
        startActivity(intent, null);
    }
}
