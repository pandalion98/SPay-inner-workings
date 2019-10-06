package com.goodix.cap.fingerprint.sec;

import android.content.Context;
import android.util.Log;
import com.goodix.cap.fingerprint.ext.ExtGoodixFpManager;
import com.goodix.cap.fingerprint.ext.GoodixEventListener;

public class GoodixFpManager {
    private static final String TAG = "GoodixFpManager";
    private Context mContext;
    private GoodixEventListener mEventListener = new GoodixEventListener() {
        public void onEvent(int eventId, Object eventData) {
            if (GoodixFpManager.this.mFpEventListener != null) {
                switch (eventId) {
                    case 4004:
                    case 4005:
                        GoodixFpManager.this.mFpEventListener.onFingerprintEvent(eventId, (GoodixFpCaptureInfo) eventData);
                        return;
                    case 5001:
                    case 5002:
                        GoodixFpManager.this.mFpEventListener.onFingerprintEvent(eventId, (GoodixFpEnrollStatus) eventData);
                        return;
                    case 5003:
                        GoodixFpManager.this.mFpEventListener.onFingerprintEvent(eventId, Integer.valueOf(5101));
                        return;
                    case 6001:
                    case 6002:
                        GoodixFpManager.this.mFpEventListener.onFingerprintEvent(eventId, (GoodixFpIdentifyResult) eventData);
                        return;
                    default:
                        GoodixFpManager.this.mFpEventListener.onFingerprintEvent(eventId, eventData);
                        return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public GoodixFpEventListener mFpEventListener = null;
    ExtGoodixFpManager mGoodixFpManager;

    public GoodixFpManager(Context context) {
        this.mContext = context;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mContext=");
        sb.append(this.mContext);
        Log.d(str, sb.toString());
        this.mGoodixFpManager = new ExtGoodixFpManager(context);
        this.mGoodixFpManager.setEventListener(this.mEventListener);
    }

    public String[] getUserIdList() {
        Log.d(TAG, "getUserIdList");
        return this.mGoodixFpManager.getUserIdList();
    }

    public int enroll(String userId, int index) {
        Log.d(TAG, "enroll");
        this.mGoodixFpManager.enroll(userId, index);
        return 0;
    }

    public int identify(String userId) {
        Log.d(TAG, "identify");
        this.mGoodixFpManager.identify(userId);
        return 0;
    }

    public int cancel() {
        Log.d(TAG, "cancel");
        return this.mGoodixFpManager.cancel();
    }

    public void enableMT(boolean enable) {
        this.mGoodixFpManager.enableMT(enable);
    }

    public void setEventListener(GoodixFpEventListener listener) {
        Log.d(TAG, "setEventListener");
        this.mFpEventListener = listener;
    }

    public String getVersion() {
        return this.mGoodixFpManager.getVersion();
    }

    public int setEnrollSession(boolean flag) {
        return this.mGoodixFpManager.setEnrollSession(flag);
    }

    public int release() {
        return 0;
    }

    public int setAccuracyLevel(int level) {
        return 0;
    }

    public int[] getFingerprintIndexList(String userId) {
        Log.d(TAG, "getFingerprintIndexList");
        return this.mGoodixFpManager.getFingerprintIndexList(userId);
    }

    public int getEnrollRepeatCount() {
        Log.d(TAG, "getEnrollRepeatCount");
        return this.mGoodixFpManager.getEnrollRepeatCount();
    }

    public byte[] getFingerprintId(String userId, int index) {
        Log.d(TAG, "getFingerprintId");
        return this.mGoodixFpManager.getFingerprintId(userId, index);
    }

    public void remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove userId=");
        sb.append(userId);
        sb.append(" index =");
        sb.append(index);
        Log.d(str, sb.toString());
        this.mGoodixFpManager.remove(userId, index);
    }

    public int request(int code, Object data) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("request() code = ");
        sb.append(code);
        Log.d(str, sb.toString());
        return this.mGoodixFpManager.request(code, data);
    }

    public void setPassword(String userId, byte[] pwdHash) {
        Log.d(TAG, "setPassword");
        this.mGoodixFpManager.setPassword(userId, pwdHash);
    }

    public void verifyPassword(String userId, byte[] pwdHash) {
        Log.d(TAG, "VerifyPassword");
        this.mGoodixFpManager.verifyPassword(userId, pwdHash);
    }

    public String getSensorInfo() {
        return this.mGoodixFpManager.getSensorInfo();
    }

    public int getSensorStatus() {
        return -1;
    }
}
