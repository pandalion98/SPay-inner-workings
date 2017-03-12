package com.samsung.android.fingerprint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager.FingerprintClientSpecBuilder;
import com.samsung.android.fingerprint.IFingerprintClient.Stub;

public class FingerprintIdentifyDialog extends Dialog implements Callback {
    private static final String TAG = "FPMS_FingerprintIdentifyDialog";
    private Context mContext = null;
    private OnDismissListener mDismissListener;
    private IFingerprintClient mFingerprintClient = new Stub() {
        public void onFingerprintEvent(FingerprintEvent evt) throws RemoteException {
            FingerprintEvent event = evt;
            if (event != null && FingerprintIdentifyDialog.this.mHandler != null) {
                FingerprintIdentifyDialog.this.mHandler.sendMessage(Message.obtain(FingerprintIdentifyDialog.this.mHandler, event.eventId, event));
            }
        }
    };
    private FingerprintManager mFm;
    private Handler mHandler;
    private FingerprintListener mListener;
    private String mOwnName;
    private int mSecurityLevel;
    private IBinder mToken;

    public interface FingerprintListener {
        void onEvent(FingerprintEvent fingerprintEvent);
    }

    public boolean handleMessage(Message msg) {
        FingerprintEvent event = msg.obj;
        if (event != null) {
            this.mListener.onEvent(event);
            if (event.eventId == 13) {
                dismiss();
            }
        } else {
            Log.e(TAG, "handleMessage: Invaild event");
        }
        return true;
    }

    private void constructFingerprintIdentifyDialog(Context context, FingerprintListener listener, int securityLevel, String ownName) {
        this.mContext = context;
        this.mListener = listener;
        this.mSecurityLevel = securityLevel;
        this.mHandler = new Handler(this);
        this.mOwnName = ownName;
        this.mFm = FingerprintManager.getInstance(this.mContext, this.mSecurityLevel, ownName);
        registerClient();
    }

    public FingerprintIdentifyDialog(Context context, FingerprintListener listener, int securityLevel) {
        super(context);
        constructFingerprintIdentifyDialog(context, listener, securityLevel, null);
    }

    public FingerprintIdentifyDialog(Context context, FingerprintListener listener, int securityLevel, String ownName) {
        super(context);
        constructFingerprintIdentifyDialog(context, listener, securityLevel, ownName);
    }

    public IBinder getToken() {
        return this.mToken;
    }

    private void registerClient() {
        if (this.mFm != null) {
            this.mToken = this.mFm.registerClient(this.mFingerprintClient, new FingerprintClientSpecBuilder("com.samsung.android.fingerprint.FingerprintIdentifyDialog").setSecurityLevel(this.mSecurityLevel).setOwnName(this.mOwnName).build());
            if (this.mToken == null) {
                Log.e(TAG, "Token value is null");
            }
        }
    }

    private void unregistreClient() {
        if (this.mFm != null && this.mToken != null) {
            this.mFm.unregisterClient(this.mToken);
            this.mToken = null;
        }
    }

    public void dismiss() {
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss(null);
        }
        unregistreClient();
        if (this.mFm != null) {
            this.mFm.notifyAppActivityState(4, null);
        }
        super.dismiss();
    }

    public void show() {
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mDismissListener = listener;
        super.setOnDismissListener(listener);
    }
}
