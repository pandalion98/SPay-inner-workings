package com.android.internal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.android.internal.os.SMProviderContract;

public class ShutdownActivity extends Activity {
    private static final String TAG = "ShutdownActivity";
    private boolean mConfirm;
    private boolean mReboot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.mReboot = "android.intent.action.REBOOT".equals(intent.getAction());
        this.mConfirm = intent.getBooleanExtra("android.intent.extra.KEY_CONFIRM", false);
        Slog.i(TAG, "onCreate(): confirm=" + this.mConfirm);
        final boolean systemRequest = intent.getBooleanExtra("android.intent.extra.SYSTEM_REQUEST", false);
        final String rebootReason = intent.getStringExtra("android.intent.extra.REBOOT_REASON");
        setFullscreen(true);
        setRotationAnimation(2);
        Thread thr = new Thread(TAG) {
            public void run() {
                IPowerManager pm = Stub.asInterface(ServiceManager.getService(SMProviderContract.KEY_POWER));
                try {
                    if (ShutdownActivity.this.mReboot) {
                        pm.reboot(ShutdownActivity.this.mConfirm, rebootReason, false);
                    } else if (systemRequest) {
                        pm.systemShutdown();
                    } else {
                        pm.shutdown(ShutdownActivity.this.mConfirm, false);
                    }
                } catch (RemoteException e) {
                }
            }
        };
        thr.start();
        finish();
        try {
            thr.join();
        } catch (InterruptedException e) {
        }
    }

    private void setFullscreen(boolean on) {
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= 1024;
        } else {
            winParams.flags &= -1025;
        }
        win.setAttributes(winParams);
    }

    private void setRotationAnimation(int rotationAnimation) {
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        winParams.rotationAnimation = rotationAnimation;
        win.setAttributes(winParams);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        Slog.i(TAG, "back button pressed");
        return false;
    }
}
