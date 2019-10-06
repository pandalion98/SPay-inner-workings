package com.goodix.cap.fingerprint.ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.CancellationSignal;
import android.util.Log;
import com.goodix.cap.fingerprint.Constants;
import com.goodix.cap.fingerprint.sec.GoodixFpCaptureInfo;
import com.goodix.cap.fingerprint.sec.GoodixFpEnrollStatus;
import com.goodix.cap.fingerprint.sec.GoodixFpIdentifyResult;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt.UntrustedAuthenticationCallback;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt.UntrustedEnrollmentCallback;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt.UntrustedRemovalCallback;
import com.goodix.cap.fingerprint.utils.TestParamEncoder;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.HwModuleTest;
import egis.client.api.EgisFingerprint;
import java.util.HashMap;
import java.util.List;

public class ExtGoodixFpManager {
    private static final int ENROLL_STATE_PAUSE = 1;
    private static final int ENROLL_STATE_RESUME = 0;
    private static final int STATE_ENROLL = 1;
    private static final int STATE_IDENTIFY = 2;
    private static final int STATE_IDLE = 0;
    private static final String TAG = "ExtGoodixFpManager";
    private static HashMap<Integer, String> mChipInfo = new HashMap<>();
    private static int mCurState = 0;
    /* access modifiers changed from: private */
    public static int mEnrollBadTrial = 0;
    /* access modifiers changed from: private */
    public static int mEnrollProgress = 0;
    private static int mEnrollState = 0;
    /* access modifiers changed from: private */
    public static int mEnrollSuccessTrial = 0;
    /* access modifiers changed from: private */
    public static int mEnrollingGroupId = -1;
    /* access modifiers changed from: private */
    public static int mEnrollingIndex = -1;
    /* access modifiers changed from: private */
    public static String mEnrollingUserId = null;
    private UntrustedAuthenticationCallback mAuthenticateCallback = new UntrustedAuthenticationCallback() {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticationError errorCode=");
            sb.append(errorCode);
            Log.d(str, sb.toString());
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticationHelp helpCode=");
            sb.append(helpCode);
            Log.d(str, sb.toString());
        }

        public void onAuthenticationSucceeded(int fingerId) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticationSucceeded fingerId=");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            ExtGoodixFpManager.this.mIndentifyResult = new GoodixFpIdentifyResult();
            ExtGoodixFpManager.this.mIndentifyResult.userId = ExtGoodixFpManager.this.mIdentifyUserID;
            ExtGoodixFpManager.this.mFpUserState.loadFingerprintsForUser(ExtGoodixFpManager.this.mIndentifyResult.userId);
            ExtGoodixFpManager.this.mIndentifyResult.index = ExtGoodixFpManager.this.mFpUserState.getIndexByFingerID(fingerId);
            ExtGoodixFpManager.this.mIndentifyResult.indexes = new int[1];
            ExtGoodixFpManager.this.mIndentifyResult.indexes[0] = ExtGoodixFpManager.this.mIndentifyResult.index;
            ExtGoodixFpManager.this.mFpEventListener.onEvent(6001, ExtGoodixFpManager.this.mIndentifyResult);
        }

        public void onAuthenticationFailed() {
            Log.d(ExtGoodixFpManager.TAG, "onAuthenticationFailed");
        }

        public void onAuthenticationCaptureBmpResult(GoodixImageInfo captureInfo) {
            Log.d(ExtGoodixFpManager.TAG, "onAuthenticationCaptureBmpResult CaptureSuccess ..... ");
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo = new GoodixFpCaptureInfo();
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo.bitmap = captureInfo.bmp;
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo.rawbitmap = captureInfo.rawBmp;
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo.width = captureInfo.width;
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo.height = captureInfo.height;
            ExtGoodixFpManager.this.mAuthenticateCaptureInfo.quality = captureInfo.imgQuality;
            if (ExtGoodixFpManager.this.mIdentifySuccess) {
                ExtGoodixFpManager.this.mIdentifySuccess = false;
            }
        }

        public void onAuthenticationAcquired(int acquireInfo) {
            switch (acquireInfo) {
                case 4001:
                    String str = ExtGoodixFpManager.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onAuthenticationAcquired CAPTURE_READY = ");
                    sb.append(acquireInfo);
                    Log.d(str, sb.toString());
                    ExtGoodixFpManager.this.mCaptureSuccess = false;
                    ExtGoodixFpManager.this.mCaptureFail = false;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4001, null);
                    return;
                case 4002:
                    String str2 = ExtGoodixFpManager.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onAuthenticationAcquired CAPTURE_STARTED = ");
                    sb2.append(acquireInfo);
                    Log.d(str2, sb2.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4002, null);
                    return;
                case 4003:
                    String str3 = ExtGoodixFpManager.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onAuthenticationAcquired CAPTURE_COMPLETED = ");
                    sb3.append(acquireInfo);
                    Log.d(str3, sb3.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4003, null);
                    return;
                case 4004:
                    String str4 = ExtGoodixFpManager.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("onAuthenticationAcquired CAPTURE_SUCCESS = ");
                    sb4.append(acquireInfo);
                    Log.d(str4, sb4.toString());
                    ExtGoodixFpManager.access$208();
                    ExtGoodixFpManager.this.mCaptureSuccess = true;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4004, ExtGoodixFpManager.this.mAuthenticateCaptureInfo);
                    return;
                case 4005:
                    String str5 = ExtGoodixFpManager.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("onAuthenticationAcquired CAPTURE_FAILED = ");
                    sb5.append(acquireInfo);
                    Log.d(str5, sb5.toString());
                    ExtGoodixFpManager.access$108();
                    ExtGoodixFpManager.this.mCaptureSuccess = false;
                    ExtGoodixFpManager.this.mCaptureFail = true;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4005, ExtGoodixFpManager.this.mAuthenticateCaptureInfo);
                    return;
                case 4006:
                    String str6 = ExtGoodixFpManager.TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("onAuthenticationAcquired CAPTURE_FINGER_LEAVED = ");
                    sb6.append(acquireInfo);
                    Log.d(str6, sb6.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4006, null);
                    if (GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
                        ExtGoodixFpManager.this.controlDevice(0);
                        return;
                    }
                    return;
                default:
                    switch (acquireInfo) {
                        case 6001:
                            ExtGoodixFpManager.this.mIdentifySuccess = true;
                            return;
                        case 6002:
                            break;
                        default:
                            switch (acquireInfo) {
                                case 6103:
                                case 6104:
                                    break;
                                default:
                                    return;
                            }
                    }
                    String str7 = ExtGoodixFpManager.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("onAuthenticationAcquired IDENTIFY_FAILED = ");
                    sb7.append(acquireInfo);
                    Log.d(str7, sb7.toString());
                    ExtGoodixFpManager.this.mIdentifySuccess = false;
                    ExtGoodixFpManager.this.mIndentifyResult = new GoodixFpIdentifyResult();
                    ExtGoodixFpManager.this.mIndentifyResult.userId = ExtGoodixFpManager.this.mIdentifyUserID;
                    ExtGoodixFpManager.this.mIndentifyResult.index = -1;
                    ExtGoodixFpManager.this.mIndentifyResult.indexes = new int[1];
                    ExtGoodixFpManager.this.mIndentifyResult.indexes[0] = ExtGoodixFpManager.this.mIndentifyResult.index;
                    if (acquireInfo == 6002) {
                        ExtGoodixFpManager.this.mIndentifyResult.result = 6103;
                        ExtGoodixFpManager.this.mFpEventListener.onEvent(6002, ExtGoodixFpManager.this.mIndentifyResult);
                        return;
                    }
                    ExtGoodixFpManager.this.mIndentifyResult.result = acquireInfo;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(6002, ExtGoodixFpManager.this.mIndentifyResult);
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public GoodixFpCaptureInfo mAuthenticateCaptureInfo;
    private CancellationSignal mAuthenticationCancel;
    private boolean mCaptureDone = false;
    /* access modifiers changed from: private */
    public boolean mCaptureFail = false;
    /* access modifiers changed from: private */
    public boolean mCaptureSuccess = false;
    private Context mContext;
    /* access modifiers changed from: private */
    public GoodixFpCaptureInfo mEnrollCaptureInfo;
    private UntrustedEnrollmentCallback mEnrollmentCallback = new UntrustedEnrollmentCallback() {
        public void onEnrollmentProgress(int fingerId, int progress) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentProgress fingerId = ");
            sb.append(fingerId);
            sb.append("  progress=");
            sb.append(progress);
            Log.d(str, sb.toString());
            ExtGoodixFpManager.mEnrollProgress = progress;
            GoodixFpEnrollStatus enrollStatus = new GoodixFpEnrollStatus();
            enrollStatus.progress = progress;
            enrollStatus.badTrial = ExtGoodixFpManager.mEnrollBadTrial;
            enrollStatus.successTrial = ExtGoodixFpManager.mEnrollSuccessTrial;
            enrollStatus.totalTrial = ExtGoodixFpManager.mEnrollBadTrial + ExtGoodixFpManager.mEnrollSuccessTrial;
        }

        public void onEnrollmentProgress(int fingerId, int progress, byte[] result) {
            byte[] bArr = result;
            ExtGoodixFpManager.mEnrollProgress = progress;
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentProgress : ");
            sb.append(ExtGoodixFpManager.mEnrollProgress);
            Log.d(str, sb.toString());
        }

        public void onEnrollmentResultInfo(GoodixEnrollInfo enrollInfo) {
            ExtGoodixFpManager.mEnrollProgress = enrollInfo.remaining;
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentResultInfo : ");
            sb.append(ExtGoodixFpManager.mEnrollProgress);
            Log.d(str, sb.toString());
            GoodixFpEnrollStatus enrollStatus = new GoodixFpEnrollStatus();
            enrollStatus.progress = 100 - ((enrollInfo.remaining * 100) / ExtGoodixFpManager.this.getEnrollCountMin());
            enrollStatus.badTrial = ExtGoodixFpManager.mEnrollBadTrial;
            enrollStatus.successTrial = ExtGoodixFpManager.mEnrollSuccessTrial;
            enrollStatus.totalTrial = ExtGoodixFpManager.mEnrollBadTrial + ExtGoodixFpManager.mEnrollSuccessTrial;
            enrollStatus.coverageBitmap = enrollInfo.bitmap;
            String str2 = ExtGoodixFpManager.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onEnrollmentResultInfo fingerId = ");
            sb2.append(enrollInfo.fingerId);
            sb2.append("  progress=");
            sb2.append(enrollStatus.progress);
            sb2.append("% enrollStatus.badTrial = ");
            sb2.append(enrollStatus.badTrial);
            sb2.append(" enrollStatus.successTrial = ");
            sb2.append(enrollStatus.successTrial);
            Log.d(str2, sb2.toString());
            ExtGoodixFpManager.this.mFpEventListener.onEvent(5001, enrollStatus);
            if (enrollInfo.remaining == 0) {
                ExtGoodixFpManager.this.mFpEventListener.onEvent(5002, enrollStatus);
                ExtGoodixFpManager.this.mFpUserState.addFingerprint(ExtGoodixFpManager.mEnrollingUserId, ExtGoodixFpManager.mEnrollingIndex, enrollInfo.fingerId, ExtGoodixFpManager.mEnrollingGroupId);
                if (!GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
                    ExtGoodixFpManager.this.controlDevice(0);
                }
            }
        }

        public void onEnrollmentCaptureBmpResult(GoodixImageInfo captureInfo) {
            Log.d(ExtGoodixFpManager.TAG, "onEnrollmentCaptureBmpResult  ....... ");
            ExtGoodixFpManager.this.mEnrollCaptureInfo = new GoodixFpCaptureInfo();
            ExtGoodixFpManager.this.mEnrollCaptureInfo.bitmap = captureInfo.bmp;
            ExtGoodixFpManager.this.mEnrollCaptureInfo.rawbitmap = captureInfo.rawBmp;
            ExtGoodixFpManager.this.mEnrollCaptureInfo.width = captureInfo.width;
            ExtGoodixFpManager.this.mEnrollCaptureInfo.height = captureInfo.height;
            ExtGoodixFpManager.this.mEnrollCaptureInfo.quality = captureInfo.imgQuality;
            if (!ExtGoodixFpManager.this.mCaptureFail && ExtGoodixFpManager.this.mCaptureSuccess) {
                ExtGoodixFpManager.this.mCaptureSuccess = false;
            }
        }

        public void onEnrollmentHelp(int helpMsgId, String helpString) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentHelp helpMsgId = ");
            sb.append(helpMsgId);
            Log.d(str, sb.toString());
        }

        public void onEnrollmentError(int errMsgId, String errString) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentError errMsgId = ");
            sb.append(errMsgId);
            Log.d(str, sb.toString());
        }

        public void onEnrollmentAcquired(int acquireInfo) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentAcquired acquireInfo = ");
            sb.append(acquireInfo);
            Log.d(str, sb.toString());
            switch (acquireInfo) {
                case 4001:
                    String str2 = ExtGoodixFpManager.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onEnrollmentAcquired CAPTURE_READY = ");
                    sb2.append(acquireInfo);
                    Log.d(str2, sb2.toString());
                    ExtGoodixFpManager.this.mCaptureSuccess = false;
                    ExtGoodixFpManager.this.mCaptureFail = false;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4001, null);
                    return;
                case 4002:
                    String str3 = ExtGoodixFpManager.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onEnrollmentAcquired CAPTURE_STARTED = ");
                    sb3.append(acquireInfo);
                    Log.d(str3, sb3.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4002, null);
                    return;
                case 4003:
                    String str4 = ExtGoodixFpManager.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("onEnrollmentAcquired CAPTURE_COMPLETED = ");
                    sb4.append(acquireInfo);
                    Log.d(str4, sb4.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4003, null);
                    return;
                case 4004:
                    String str5 = ExtGoodixFpManager.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("onEnrollmentAcquired CAPTURE_SUCCESS = ");
                    sb5.append(acquireInfo);
                    Log.d(str5, sb5.toString());
                    ExtGoodixFpManager.access$208();
                    ExtGoodixFpManager.this.mCaptureSuccess = true;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4004, ExtGoodixFpManager.this.mEnrollCaptureInfo);
                    return;
                case 4005:
                    String str6 = ExtGoodixFpManager.TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("onEnrollmentAcquired CAPTURE_FAILED = ");
                    sb6.append(acquireInfo);
                    Log.d(str6, sb6.toString());
                    ExtGoodixFpManager.access$108();
                    ExtGoodixFpManager.this.mCaptureSuccess = false;
                    ExtGoodixFpManager.this.mCaptureFail = true;
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4005, ExtGoodixFpManager.this.mEnrollCaptureInfo);
                    return;
                case 4006:
                    String str7 = ExtGoodixFpManager.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("onEnrollmentAcquired CAPTURE_FINGER_LEAVED = ");
                    sb7.append(acquireInfo);
                    Log.d(str7, sb7.toString());
                    ExtGoodixFpManager.this.mFpEventListener.onEvent(4006, null);
                    if (GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
                        ExtGoodixFpManager.this.controlDevice(0);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private CancellationSignal mEnrollmentCancel;
    /* access modifiers changed from: private */
    public GoodixEventListener mFpEventListener = null;
    /* access modifiers changed from: private */
    public GoodixFpsUserState mFpUserState;
    private GoodixFingerprintManagerExt mGoodixFingerprintManager = null;
    /* access modifiers changed from: private */
    public boolean mIdentifySuccess = false;
    /* access modifiers changed from: private */
    public String mIdentifyUserID = null;
    /* access modifiers changed from: private */
    public GoodixFpIdentifyResult mIndentifyResult;
    private UntrustedRemovalCallback mRemoveCallback = new UntrustedRemovalCallback() {
        public void onRemovalSucceeded(int fingerId) {
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRemovalSucceeded fingerId = ");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            ExtGoodixFpManager.this.mFpEventListener.onEvent(9005, null);
        }

        public void onRemovalError(int errMsgId, CharSequence errString) {
            Log.d(ExtGoodixFpManager.TAG, "onRemovalFailed");
            String str = ExtGoodixFpManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRemovalFailed errMsgId = ");
            sb.append(errMsgId);
            sb.append(" errString = ");
            sb.append(errString);
            Log.d(str, sb.toString());
        }
    };

    static /* synthetic */ int access$108() {
        int i = mEnrollBadTrial;
        mEnrollBadTrial = i + 1;
        return i;
    }

    static /* synthetic */ int access$208() {
        int i = mEnrollSuccessTrial;
        mEnrollSuccessTrial = i + 1;
        return i;
    }

    static {
        mChipInfo.put(Integer.valueOf(13), "gw32j1");
        mChipInfo.put(Integer.valueOf(10), "gw32k");
    }

    public ExtGoodixFpManager(Context context) {
        this.mContext = context;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mContext=");
        sb.append(this.mContext);
        Log.d(str, sb.toString());
        this.mGoodixFingerprintManager = new GoodixFingerprintManagerExt(context);
        this.mFpUserState = new GoodixFpsUserState(this.mContext);
        GoodixSharePref.getInstance().init(context);
    }

    /* access modifiers changed from: private */
    public void controlDevice(int flag) {
    }

    public String[] getUserIdList() {
        Log.d(TAG, "getUserIdList");
        String[] userId = null;
        List<GoodixUserInfo> uis = this.mFpUserState.getUserInfo();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getUserIdList uis.size()=");
        sb.append(uis.size());
        Log.d(str, sb.toString());
        if (uis.size() > 0) {
            userId = new String[uis.size()];
            for (int i = 0; i < uis.size(); i++) {
                userId[i] = ((GoodixUserInfo) uis.get(i)).getUserId().toString();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getUserIdList userId[");
                sb2.append(i);
                sb2.append("]=");
                sb2.append(userId[i]);
                Log.d(str2, sb2.toString());
            }
        }
        return userId;
    }

    public int enroll(String userId, int index) {
        Log.d(TAG, "enroll");
        mCurState = 1;
        mEnrollState = 0;
        mEnrollingIndex = index;
        mEnrollBadTrial = 0;
        mEnrollSuccessTrial = 0;
        mEnrollingUserId = userId;
        controlDevice(1);
        if (GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
            mEnrollingGroupId = this.mFpUserState.addUserInfo(userId);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MT enroll mEnrollingGroupId=");
            sb.append(mEnrollingGroupId);
            Log.d(str, sb.toString());
            setActiveGroup(10000);
        } else {
            if (this.mFpUserState.isUserIdExist(userId)) {
                mEnrollingGroupId = this.mFpUserState.getGroupIdForUser(userId);
            } else {
                mEnrollingGroupId = this.mFpUserState.addUserInfo(userId);
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("enroll mEnrollingGroupId=");
            sb2.append(mEnrollingGroupId);
            Log.d(str2, sb2.toString());
            if (mEnrollingGroupId >= 0) {
                setActiveGroup(mEnrollingGroupId);
            }
            Log.d(TAG, "after setActiveGroup");
        }
        this.mEnrollmentCancel = new CancellationSignal();
        this.mGoodixFingerprintManager.untrustedEnroll(this.mEnrollmentCancel, this.mEnrollmentCallback);
        return 0;
    }

    public int identify(String userId) {
        int groupId;
        Log.d(TAG, "identify");
        mCurState = 2;
        this.mIdentifyUserID = userId;
        this.mFpUserState.getFingerprints(userId);
        controlDevice(1);
        if (GoodixSharePref.getInstance().getBoolean("enable_mt_flag", false)) {
            int groupId2 = this.mFpUserState.addUserInfo(userId);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MT identify groupId=");
            sb.append(groupId2);
            Log.d(str, sb.toString());
            setActiveGroup(10000);
        } else {
            if (this.mFpUserState.isUserIdExist(userId)) {
                groupId = this.mFpUserState.getGroupIdForUser(userId);
            } else {
                groupId = this.mFpUserState.addUserInfo(userId);
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("identify groupId=");
            sb2.append(groupId);
            Log.d(str2, sb2.toString());
            if (groupId >= 0) {
                setActiveGroup(groupId);
            }
        }
        this.mAuthenticationCancel = new CancellationSignal();
        this.mGoodixFingerprintManager.untrustedAuthenticate(this.mAuthenticationCancel, this.mAuthenticateCallback);
        return 0;
    }

    public int cancel() {
        Log.d(TAG, "cancel");
        if (mCurState == 1) {
            if (this.mEnrollmentCancel != null && !this.mEnrollmentCancel.isCanceled()) {
                Log.d(TAG, "cancel enrollment");
                this.mEnrollmentCancel.cancel();
                this.mFpEventListener.onEvent(5003, Integer.valueOf(5101));
                this.mEnrollmentCancel = null;
            }
            mEnrollState = 0;
        } else if (mCurState == 2 && this.mAuthenticationCancel != null && !this.mAuthenticationCancel.isCanceled()) {
            Log.d(TAG, "cancel identification");
            this.mAuthenticationCancel.cancel();
            GoodixFpIdentifyResult identifyResult = new GoodixFpIdentifyResult();
            identifyResult.userId = this.mIdentifyUserID;
            identifyResult.index = -1;
            identifyResult.indexes = new int[1];
            identifyResult.indexes[0] = -1;
            identifyResult.result = 6101;
            this.mFpEventListener.onEvent(6002, identifyResult);
            this.mAuthenticationCancel = null;
        }
        this.mIdentifyUserID = null;
        mCurState = 0;
        controlDevice(0);
        return 0;
    }

    public void enableMT(boolean enable) {
        GoodixSharePref.getInstance().init(this.mContext);
        GoodixSharePref.getInstance().putBoolean("enable_mt_flag", enable);
    }

    private void setActiveGroup(int groupId) {
        byte[] byteArray = new byte[8];
        this.mGoodixFingerprintManager.initGroupIdValue(groupId);
        int offset = TestParamEncoder.encodeInt32(byteArray, 0, TestResultParser.TEST_TOKEN_UNTRUSTED_SET_ACTIVE_GROUP, groupId);
        this.mGoodixFingerprintManager.testCmd(53, byteArray);
    }

    public void setEventListener(GoodixEventListener listener) {
        Log.d(TAG, "setEventListener");
        this.mFpEventListener = listener;
    }

    public String getVersion() {
        return Constants.RELEASE_VERSION;
    }

    public int setEnrollSession(boolean flag) {
        this.mGoodixFingerprintManager.setEnrollSession(flag);
        return this.mGoodixFingerprintManager.setEnrollSession(flag);
    }

    public int release() {
        return 0;
    }

    public int setAccuracyLevel(int level) {
        return 0;
    }

    public int[] getFingerprintIndexList(String userId) {
        Log.d(TAG, "getFingerprintIndexList");
        List<GoodixFpInfo> fps = this.mFpUserState.getFingerprints(userId);
        int[] fpIndex = null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getFingerprintIndexList fps.size()=");
        sb.append(fps.size());
        Log.d(str, sb.toString());
        if (fps.size() > 0) {
            fpIndex = new int[fps.size()];
            for (int i = 0; i < fps.size(); i++) {
                fpIndex[i] = ((GoodixFpInfo) fps.get(i)).getFingerIndex();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getFingerprintIndexList fpIndex[");
                sb2.append(i);
                sb2.append("]=");
                sb2.append(fpIndex[i]);
                Log.d(str2, sb2.toString());
            }
        }
        return fpIndex;
    }

    public int getEnrollRepeatCount() {
        Log.d(TAG, "getEnrollRepeatCount");
        return 20;
    }

    public byte[] getFingerprintId(String userId, int index) {
        Log.d(TAG, "getFingerprintId");
        List<GoodixFpInfo> fps = this.mFpUserState.getFingerprints(userId);
        byte[] fid = null;
        if (fps.size() > 0) {
            for (int i = 0; i < fps.size(); i++) {
                if (index == ((GoodixFpInfo) fps.get(i)).getFingerIndex()) {
                    int fingerId = ((GoodixFpInfo) fps.get(i)).getFingerId();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getFingerprintId fingerId = ");
                    sb.append(fingerId);
                    Log.d(str, sb.toString());
                    fid = int2bytes(fingerId);
                }
            }
        }
        return fid;
    }

    public void remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove userId=");
        sb.append(userId);
        sb.append(" index =");
        sb.append(index);
        Log.d(str, sb.toString());
        byte[] fid = getFingerprintId(userId, index);
        if (fid != null) {
            int fingerId = byte2int(fid);
            int groupId = this.mFpUserState.getGroupIdForUser(userId);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("remove userId=");
            sb2.append(userId);
            Log.d(str2, sb2.toString());
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("remove index=");
            sb3.append(index);
            Log.d(str3, sb3.toString());
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("remove fid=");
            sb4.append(fid);
            Log.d(str4, sb4.toString());
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("remove fingerId=");
            sb5.append(fingerId);
            Log.d(str5, sb5.toString());
            String str6 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("remove groupId=");
            sb6.append(groupId);
            Log.d(str6, sb6.toString());
            this.mGoodixFingerprintManager.untrustedRemoveForSec(groupId, fingerId, this.mRemoveCallback);
            this.mFpUserState.removeFingerprint(userId, index);
            return;
        }
        this.mFpEventListener.onEvent(9006, null);
    }

    private static byte[] int2bytes(int res) {
        return new byte[]{(byte) (res & 255), (byte) ((res >> 8) & 255), (byte) ((res >> 16) & 255), (byte) (res >>> 24)};
    }

    private static int byte2int(byte[] res) {
        return (res[0] & 255) | ((res[1] << 8) & 65280) | ((res[2] << HwModuleTest.ID_BLACK) >>> 8) | (res[3] << HwModuleTest.ID_BLACK);
    }

    /* access modifiers changed from: private */
    public int getEnrollCountMin() {
        if (getProperty("gf.debug.open_swipe_enroll", "0").equals(EgisFingerprint.MAJOR_VERSION)) {
            return 3;
        }
        return 20;
    }

    private String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            return (String) c.getMethod("get", new Class[]{String.class, String.class}).invoke(c, new Object[]{key, "unknown"});
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
        }
        return value;
    }

    public int request(int code, Object data) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("request() code = ");
        sb.append(code);
        Log.d(str, sb.toString());
        switch (code) {
            case 2001:
                this.mGoodixFingerprintManager.untrustedPauseEnroll();
                mEnrollState = 1;
                break;
            case 2002:
                this.mGoodixFingerprintManager.untrustedResumeEnroll();
                mEnrollState = 0;
                break;
        }
        return 0;
    }

    public void setPassword(String userId, byte[] pwdHash) {
        Log.d(TAG, "setPassword");
        new String(pwdHash);
    }

    public void verifyPassword(String userId, byte[] pwdHash) {
        Log.d(TAG, "VerifyPassword");
        new String(pwdHash);
    }

    public String getSensorInfo() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mChipType info =  ");
        sb.append(this.mGoodixFingerprintManager.getConfig().mChipType);
        sb.append("  ");
        sb.append((String) mChipInfo.get(Integer.valueOf(this.mGoodixFingerprintManager.getConfig().mChipType)));
        Log.d(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("gf-cap-");
        sb2.append(this.mGoodixFingerprintManager.getConfig().mChipSeries);
        sb2.append("-");
        sb2.append((String) mChipInfo.get(Integer.valueOf(this.mGoodixFingerprintManager.getConfig().mChipType)));
        return sb2.toString();
    }

    public int getSensorStatus() {
        return -1;
    }

    private Bitmap convertDataToBmp(byte[] data, int width, int height) {
        Log.d(TAG, "convertDataToBmp start");
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("convertToBitmap width=");
        sb.append(width);
        sb.append(",height=");
        sb.append(height);
        sb.append(" data=");
        sb.append(data);
        Log.d(str, sb.toString());
        int length = width * height;
        int[] display = new int[length];
        for (int i = 0; i < length; i++) {
            int pixel = data[i] & 255;
            display[i] = -16777216 | (pixel << 16) | (pixel << 8) | pixel;
        }
        return Bitmap.createBitmap(display, width, height, Config.ARGB_8888);
    }
}
