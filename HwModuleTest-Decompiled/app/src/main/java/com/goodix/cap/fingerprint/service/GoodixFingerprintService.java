package com.goodix.cap.fingerprint.service;

import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.cap.fingerprint.IGoodixFingerprintDaemon;
import android.hardware.cap.fingerprint.IGoodixFingerprintDaemonCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.util.Log;
import com.goodix.cap.fingerprint.Constants;
import com.goodix.cap.fingerprint.GFConfig;
import com.goodix.cap.fingerprint.GFDevice;
import com.goodix.cap.fingerprint.service.IGoodixFingerprintInterface.Stub;
import com.goodix.cap.fingerprint.utils.FingerprintUtils;
import com.goodix.cap.fingerprint.utils.TestParamEncoder;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GoodixFingerprintService extends Stub implements DeathRecipient {
    private static final String GOODIXFINGERPRINTD = "android.hardware.cap.fingerprint.IGoodixFingerprintDaemon";
    public static final String MANAGE_FINGERPRINT = "android.permission.MANAGE_FINGERPRINT";
    private static final String TAG = "GoodixFingerprintService";
    private static final int TEST_GET_CONFIG_MAX_RETRY_TIMES = 20;
    public static final String USE_FINGERPRINT = "android.permission.USE_FINGERPRINT";
    private AppOpsManager mAppOps = null;
    /* access modifiers changed from: private */
    public IGoodixFingerprintCallback mCallback = null;
    /* access modifiers changed from: private */
    public HashMap<IBinder, IGoodixFingerprintCallback> mCallbackHashMap = new HashMap<>();
    /* access modifiers changed from: private */
    public GFConfig mConfig = new GFConfig();
    /* access modifiers changed from: private */
    public Context mContext = null;
    private IGoodixFingerprintDaemon mDaemon = null;
    private IGoodixFingerprintDaemonCallback mDaemonCallback = new IGoodixFingerprintDaemonCallback.Stub() {
        public void onEnrollResult(long deviceId, final int fingerId, int groupId, final int remaining) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollResult fingerId = ");
            sb.append(fingerId);
            sb.append(", remaining = ");
            sb.append(remaining);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback == null) {
                        String str = GoodixFingerprintService.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintService.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    try {
                        FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintService.this.mContext);
                        GoodixFingerprintService.this.mCallback.onEnrollResult(fingerId, remaining);
                        GoodixFingerprintService.this.mUntrustedFingerprintId = fingerId;
                    } catch (RemoteException e) {
                    }
                }
            });
        }

        public void onEnrollSecResult(long deviceId, int fingerId, int groupId, int remaining, int width, int height, byte[] result, int length) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollSecResult fingerId = ");
            int i = fingerId;
            sb.append(i);
            sb.append(", remaining = ");
            int i2 = remaining;
            sb.append(i2);
            sb.append(",width=");
            int i3 = width;
            sb.append(i3);
            sb.append(",height=");
            int i4 = height;
            sb.append(i4);
            sb.append(", result.length = ");
            byte[] bArr = result;
            sb.append(bArr.length);
            sb.append(",length = ");
            int i5 = length;
            sb.append(i5);
            Log.d(str, sb.toString());
            final int fingerIndex = i;
            final int progressRemain = i2;
            final int dataLength = i5;
            int bWidth = i3;
            final int bHeight = i4;
            Bitmap bitmap = GoodixFingerprintService.this.convertToBitmap(bArr, bWidth, bHeight);
            C00552 r9 = r0;
            Handler access$300 = GoodixFingerprintService.this.mHandler;
            final int i6 = bWidth;
            int i7 = bHeight;
            int i8 = bWidth;
            final Bitmap bitmap2 = bitmap;
            C00552 r0 = new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback == null) {
                        String str = GoodixFingerprintService.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintService.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    try {
                        FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintService.this.mContext);
                        GoodixFingerprintService.this.mCallback.onEnrollSecResult(fingerIndex, progressRemain, i6, bHeight, bitmap2, dataLength);
                        GoodixFingerprintService.this.mUntrustedFingerprintId = fingerIndex;
                    } catch (RemoteException e) {
                    }
                }
            };
            access$300.post(r9);
        }

        public void onCaptureInfo(long deviceId, int width, int height, int imgQuality, int score, byte[] rawData, byte[] bmpData) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCaptureInfo =  width = ");
            int i = width;
            sb.append(i);
            sb.append(" height = ");
            int i2 = height;
            sb.append(i2);
            sb.append(" rawData =");
            byte[] bArr = rawData;
            sb.append(bArr);
            sb.append(" bmpData=");
            byte[] bArr2 = bmpData;
            sb.append(bArr2);
            sb.append(" imgQuality =");
            int i3 = imgQuality;
            sb.append(i3);
            Log.d(str, sb.toString());
            Handler access$300 = GoodixFingerprintService.this.mHandler;
            final byte[] bArr3 = bArr;
            final int i4 = i;
            final int i5 = i2;
            final byte[] bArr4 = bArr2;
            final long j = deviceId;
            C00563 r0 = r3;
            final int i6 = i3;
            final int i7 = score;
            C00563 r3 = new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback == null) {
                        String str = GoodixFingerprintService.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintService.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    try {
                        Bitmap rawBmp = GoodixFingerprintService.this.convertToBitmap(bArr3, i4, i5);
                        Bitmap bmp = GoodixFingerprintService.this.convertToBitmap(bArr4, i4, i5);
                        FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintService.this.mContext);
                        GoodixFingerprintService.this.mCallback.onCaptureInfo(j, rawBmp, bArr3, i4, i5, bmp, bArr4, i6, i7);
                    } catch (RemoteException e) {
                    }
                }
            };
            access$300.post(r0);
        }

        public void onEventInfo(int eventId, byte[] eventData, int length) {
        }

        public void onAcquired(long deviceId, final int acquiredInfo) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAcquired acquiredInfo = ");
            sb.append(acquiredInfo);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback != null) {
                        try {
                            GoodixFingerprintService.this.mCallback.onAcquired(acquiredInfo);
                        } catch (RemoteException e) {
                        }
                    }
                }
            });
        }

        public void onAuthenticated(long deviceId, final int fingerId, int groupId, byte[] hat) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticated fingerId = ");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback != null) {
                        try {
                            if (fingerId != 0) {
                                GoodixFingerprintService.this.mCallback.onAuthenticationSucceeded(fingerId);
                                FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintService.this.mContext);
                            } else {
                                GoodixFingerprintService.this.mCallback.onAuthenticationFailed();
                                FingerprintUtils.vibrateFingerprintError(GoodixFingerprintService.this.mContext);
                            }
                        } catch (RemoteException e) {
                        }
                    }
                }
            });
        }

        public void onError(long deviceId, final int error) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onError error = ");
            sb.append(error);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback != null) {
                        try {
                            GoodixFingerprintService.this.mCallback.onError(error);
                        } catch (RemoteException e) {
                        }
                    }
                }
            });
        }

        public void onRemoved(long deviceId, final int fingerId, int groupId) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRemoved fingerId = ");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
                    if (GoodixFingerprintService.this.mCallback != null) {
                        try {
                            GoodixFingerprintService.this.mCallback.onRemoved(fingerId);
                            GoodixFingerprintService.this.mUntrustedFingerprintId = 0;
                        } catch (RemoteException e) {
                        }
                    }
                }
            });
        }

        public void onTestCmd(long deviceId, int cmdId, byte[] result) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onTestCmd ");
            sb.append(Constants.testCmdIdToString(cmdId));
            Log.d(str, sb.toString());
            if (26 == cmdId || 27 == cmdId) {
                HashMap<Integer, Object> testResult = TestResultParser.parser(result);
                if (testResult != null) {
                    if (!testResult.containsKey(Integer.valueOf(100)) || ((Integer) testResult.get(Integer.valueOf(100))).intValue() <= 0) {
                        if (testResult.containsKey(Integer.valueOf(101))) {
                            GoodixFingerprintService.this.mConfig.mChipType = ((Integer) testResult.get(Integer.valueOf(101))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(102))) {
                            GoodixFingerprintService.this.mConfig.mChipSeries = ((Integer) testResult.get(Integer.valueOf(102))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(800))) {
                            GoodixFingerprintService.this.mConfig.mMaxFingers = ((Integer) testResult.get(Integer.valueOf(800))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_MAX_FINGERS_PER_USER))) {
                            GoodixFingerprintService.this.mConfig.mMaxFingersPerUser = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_MAX_FINGERS_PER_USER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_KEY_MODE))) {
                            GoodixFingerprintService.this.mConfig.mSupportKeyMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_KEY_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FF_MODE))) {
                            GoodixFingerprintService.this.mConfig.mSupportFFMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_POWER_KEY_FEATURE))) {
                            GoodixFingerprintService.this.mConfig.mSupportPowerKeyFeature = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_POWER_KEY_FEATURE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_UNTRUSTED_ENROLL))) {
                            GoodixFingerprintService.this.mConfig.mForbiddenUntrustedEnroll = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_UNTRUSTED_ENROLL))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_ENROLL_DUPLICATE_FINGERS))) {
                            GoodixFingerprintService.this.mConfig.mForbiddenEnrollDuplicateFingers = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_ENROLL_DUPLICATE_FINGERS))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_BIO_ASSAY))) {
                            GoodixFingerprintService.this.mConfig.mSupportBioAssay = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_BIO_ASSAY))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_PERFORMANCE_DUMP))) {
                            GoodixFingerprintService.this.mConfig.mSupportPerformanceDump = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_PERFORMANCE_DUMP))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_NAV_MODE))) {
                            GoodixFingerprintService.this.mConfig.mSupportNavMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_NAV_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_DOUBLE_CLICK_TIME))) {
                            GoodixFingerprintService.this.mConfig.mNavDoubleClickTime = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_DOUBLE_CLICK_TIME))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_LONG_PRESS_TIME))) {
                            GoodixFingerprintService.this.mConfig.mNavLongPressTime = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_LONG_PRESS_TIME))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_ENROLLING_MIN_TEMPLATES))) {
                            GoodixFingerprintService.this.mConfig.mEnrollingMinTemplates = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_ENROLLING_MIN_TEMPLATES))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_QUALITY_THRESHOLD))) {
                            GoodixFingerprintService.this.mConfig.mValidImageQualityThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_QUALITY_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_AREA_THRESHOLD))) {
                            GoodixFingerprintService.this.mConfig.mValidImageAreaThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_AREA_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_DUPLICATE_FINGER_OVERLAY_SCORE))) {
                            GoodixFingerprintService.this.mConfig.mDuplicateFingerOverlayScore = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_DUPLICATE_FINGER_OVERLAY_SCORE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_INCREASE_RATE_BETWEEN_STITCH_INFO))) {
                            GoodixFingerprintService.this.mConfig.mIncreaseRateBetweenStitchInfo = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_INCREASE_RATE_BETWEEN_STITCH_INFO))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_AUTHENTICATE_FAIL_RETRY_COUNT))) {
                            GoodixFingerprintService.this.mConfig.mScreenOnAuthenticateFailRetryCount = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_AUTHENTICATE_FAIL_RETRY_COUNT))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_AUTHENTICATE_FAIL_RETRY_COUNT))) {
                            GoodixFingerprintService.this.mConfig.mScreenOffAuthenticateFailRetryCount = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_AUTHENTICATE_FAIL_RETRY_COUNT))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_VALID_TOUCH_FRAME_THRESHOLD))) {
                            GoodixFingerprintService.this.mConfig.mScreenOnValidTouchFrameThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_VALID_TOUCH_FRAME_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_VALID_TOUCH_FRAME_THRESHOLD))) {
                            GoodixFingerprintService.this.mConfig.mScreenOffValidTouchFrameThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_VALID_TOUCH_FRAME_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IMAGE_QUALITY_THRESHOLD_FOR_MISTAKE_TOUCH))) {
                            GoodixFingerprintService.this.mConfig.mImageQualityThresholdForMistakeTouch = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IMAGE_QUALITY_THRESHOLD_FOR_MISTAKE_TOUCH))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_AUTHENTICATE_ORDER))) {
                            GoodixFingerprintService.this.mConfig.mAuthenticateOrder = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_AUTHENTICATE_ORDER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_FF_MODE))) {
                            GoodixFingerprintService.this.mConfig.mReissueKeyDownWhenEntryFfMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_IMAGE_MODE))) {
                            GoodixFingerprintService.this.mConfig.mReissueKeyDownWhenEntryImageMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_IMAGE_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SENSOR_BROKEN_CHECK))) {
                            GoodixFingerprintService.this.mConfig.mSupportSensorBrokenCheck = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SENSOR_BROKEN_CHECK))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_SENSOR))) {
                            GoodixFingerprintService.this.mConfig.mBrokenPixelThresholdForDisableSensor = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_SENSOR))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_STUDY))) {
                            GoodixFingerprintService.this.mConfig.mBrokenPixelThresholdForDisableStudy = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_STUDY))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BAD_POINT_TEST_MAX_FRAME_NUMBER))) {
                            GoodixFingerprintService.this.mConfig.mBadPointTestMaxFrameNumber = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BAD_POINT_TEST_MAX_FRAME_NUMBER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REPORT_KEY_EVENT_ONLY_ENROLL_AUTHENTICATE))) {
                            GoodixFingerprintService.this.mConfig.mReportKeyEventOnlyEnrollAuthenticate = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REPORT_KEY_EVENT_ONLY_ENROLL_AUTHENTICATE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_IMAGE_MODE))) {
                            GoodixFingerprintService.this.mConfig.mRequireDownAndUpInPairsForImageMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_IMAGE_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_FF_MODE))) {
                            GoodixFingerprintService.this.mConfig.mRequireDownAndUpInPairsForFFMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_KEY_MODE))) {
                            GoodixFingerprintService.this.mConfig.mRequireDownAndUpInPairsForKeyMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_KEY_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_NAV_MODE))) {
                            GoodixFingerprintService.this.mConfig.mRequireDownAndUpInPairsForNavMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_NAV_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SET_SPI_SPEED_IN_TEE))) {
                            GoodixFingerprintService.this.mConfig.mSupportSetSpiSpeedInTEE = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SET_SPI_SPEED_IN_TEE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FRR_ANALYSIS))) {
                            GoodixFingerprintService.this.mConfig.mSupportFrrAnalysis = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FRR_ANALYSIS))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_TEMPLATE_UPDATE_SAVE_THRESHOLD))) {
                            GoodixFingerprintService.this.mConfig.mTemplateUpateSaveThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_TEMPLATE_UPDATE_SAVE_THRESHOLD))).intValue();
                        }
                        if (GoodixFingerprintService.this.mConfig.mMaxFingers < GoodixFingerprintService.this.mConfig.mMaxFingersPerUser) {
                            GoodixFingerprintService.this.mConfig.mMaxFingers = GoodixFingerprintService.this.mConfig.mMaxFingersPerUser;
                        }
                        if (26 == cmdId) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
            if (GoodixFingerprintService.this.mCallback != null) {
                try {
                    GoodixFingerprintService.this.mCallback.onTestCmd(cmdId, result);
                } catch (RemoteException e) {
                }
            }
        }

        public void onHbdData(long deviceId, int heartBeatRate, int status, int[] displayData, int[] rawData) {
            Log.d(GoodixFingerprintService.TAG, "onHbdData");
            GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
            if (GoodixFingerprintService.this.mCallback != null) {
                try {
                    GoodixFingerprintService.this.mCallback.onHbdData(heartBeatRate, status, displayData, rawData);
                } catch (RemoteException e) {
                }
            }
        }

        public void onDump(long deviceId, int cmdId, byte[] data) {
            Log.d(GoodixFingerprintService.TAG, "onDump");
            if (GoodixFingerprintService.this.mDumpCallback != null) {
                try {
                    GoodixFingerprintService.this.mDumpCallback.onDump(cmdId, data);
                } catch (RemoteException e) {
                }
            }
        }

        public void onAuthenticatedFido(long devId, int fingerId, byte[] uvtData) {
            String str = GoodixFingerprintService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticatedFido, fingerId:");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            GoodixFingerprintService.this.mCallback = (IGoodixFingerprintCallback) GoodixFingerprintService.this.mCallbackHashMap.get(GoodixFingerprintService.this.mToken);
            if (GoodixFingerprintService.this.mCallback != null) {
                try {
                    GoodixFingerprintService.this.mCallback.onAuthenticateFido(fingerId, uvtData);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(GoodixFingerprintService.TAG, "mCallback is null");
            }
        }
    };
    /* access modifiers changed from: private */
    public GFDevice mDevice = new GFDevice();
    /* access modifiers changed from: private */
    public IGoodixFingerprintDumpCallback mDumpCallback = null;
    /* access modifiers changed from: private */
    public int mGetConfigRetryCount = 0;
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mOpPackageName = null;
    private int mOpUseFingerprint = -1;
    /* access modifiers changed from: private */
    public IBinder mToken = null;
    /* access modifiers changed from: private */
    public int mUntrustedFingerprintId = 0;

    public GoodixFingerprintService(Context context) {
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mOpPackageName = getAppOpPackageName();
        this.mOpUseFingerprint = getOpUseFingerprint();
        testCmd(null, 26, null, this.mOpPackageName);
        this.mGetConfigRetryCount++;
    }

    private String getAppOpPackageName() {
        String opPackageName = null;
        if (this.mContext == null) {
            return null;
        }
        try {
            Method getOpPackageName = this.mContext.getClass().getMethod("getOpPackageName", new Class[0]);
            getOpPackageName.setAccessible(true);
            opPackageName = (String) getOpPackageName.invoke(this.mContext, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
        }
        return opPackageName;
    }

    private int getOpUseFingerprint() {
        try {
            return AppOpsManager.class.getDeclaredField("OP_USE_FINGERPRINT").getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            return -1;
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkPermission(String permission) {
        Context context = this.mContext;
        StringBuilder sb = new StringBuilder();
        sb.append("Must have ");
        sb.append(permission);
        sb.append(" permission.");
        context.enforceCallingOrSelfPermission(permission, sb.toString());
    }

    private boolean canUseFingerprint(String opPackageName) {
        checkPermission("android.permission.USE_FINGERPRINT");
        boolean z = false;
        try {
            Method noteOp = this.mAppOps.getClass().getMethod("noteOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class});
            noteOp.setAccessible(true);
            if (((Integer) noteOp.invoke(this.mAppOps, new Object[]{Integer.valueOf(this.mOpUseFingerprint), Integer.valueOf(Binder.getCallingUid()), opPackageName})).intValue() == 0) {
                z = true;
            }
            return z;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public IGoodixFingerprintDaemon getFingerprintDaemon() {
        Log.d(TAG, "getFingerprintDaemon()");
        if (this.mDaemon == null) {
            try {
                this.mDaemon = IGoodixFingerprintDaemon.Stub.asInterface((IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{GOODIXFINGERPRINTD}));
                this.mDaemon.asBinder().linkToDeath(this, 0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (RemoteException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
            if (this.mDaemon != null) {
                try {
                    this.mDaemon.asBinder().linkToDeath(this, 0);
                    this.mDaemon.testInit(this.mDaemonCallback);
                } catch (RemoteException e6) {
                }
            }
        }
        return this.mDaemon;
    }

    public void initCallback(IBinder token, IGoodixFingerprintCallback callback, String opPackageName) {
        Log.d(TAG, "initCallback");
        if (canUseFingerprint(opPackageName)) {
            this.mCallbackHashMap.put(token, callback);
        }
    }

    public void testCmd(final IBinder token, final int cmdId, final byte[] param, String opPackageName) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("testCmd ");
        sb.append(Constants.testCmdIdToString(cmdId));
        Log.d(str, sb.toString());
        if (canUseFingerprint(opPackageName)) {
            this.mToken = token;
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "testCmd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        if (daemon.testCmd(cmdId, param) != 0 && 26 == cmdId && GoodixFingerprintService.this.mGetConfigRetryCount < 20) {
                            GoodixFingerprintService.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    GoodixFingerprintService.this.testCmd(token, 26, null, GoodixFingerprintService.this.mOpPackageName);
                                    GoodixFingerprintService.this.mGetConfigRetryCount = GoodixFingerprintService.this.mGetConfigRetryCount + 1;
                                    String str = GoodixFingerprintService.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("CMD_TEST_GET_CONFIG retry count : ");
                                    sb.append(GoodixFingerprintService.this.mGetConfigRetryCount);
                                    Log.i(str, sb.toString());
                                }
                            }, 1000);
                        }
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "testCmd RemoteException");
                    }
                }
            });
        }
    }

    public int testSync(IBinder token, int cmdId, byte[] param, String opPackageName) {
        Log.d(TAG, "testSync");
        if (!canUseFingerprint(opPackageName)) {
            return 0;
        }
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "testSync: no goodixfingeprintd!");
            return 0;
        }
        try {
            return daemon.testCmd(cmdId, param);
        } catch (RemoteException e) {
            Log.e(TAG, "testSync RemoteException");
            return 0;
        }
    }

    public void setSafeClass(final int safeClass, String opPackageName) {
        Log.d(TAG, "setSafeClass");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "setSafeClass: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.setSafeClass(safeClass);
                        GoodixFingerprintService.this.mDevice.mSafeClass = safeClass;
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "setSafeClass RemoteException");
                    }
                }
            });
        }
    }

    public void navigate(final int navMode, String opPackageName) {
        Log.d(TAG, "navigate");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "navigate: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.navigate(navMode);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "navigate RemoteException");
                    }
                }
            });
        }
    }

    public void stopNavigation(String opPackageName) {
        Log.d(TAG, "stopNavigation");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "stopNavigation: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopNavigation();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "stopNavigation RemoteException");
                    }
                }
            });
        }
    }

    public void enableFingerprintModule(final boolean enable, String opPackageName) {
        Log.d(TAG, "enableFingerprintModule");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "enableFingerprintModule: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.enableFingerprintModule(enable ? (byte) 1 : 0);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "enableFingerprintModule RemoteException");
                    }
                }
            });
        }
    }

    public void cameraCapture(String opPackageName) {
        Log.d(TAG, "cameraCapture");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "cameraCapture: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.cameraCapture();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "cameraCapture RemoteException");
                    }
                }
            });
        }
    }

    public void stopCameraCapture(String opPackageName) {
        Log.d(TAG, "stopCameraCapture");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "stopCameraCapture: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopCameraCapture();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "stopCameraCapture RemoteException");
                    }
                }
            });
        }
    }

    public void enableFfFeature(final boolean enable, String opPackageName) {
        Log.d(TAG, "enableFfFeature");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "enableFfFeature: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.enableFfFeature(enable ? (byte) 1 : 0);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "enableFfFeature RemoteException");
                    }
                }
            });
        }
    }

    public void screenOn(String opPackageName) {
        Log.d(TAG, "screenOn");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "screenOn: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.screenOn();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "screenOn RemoteException");
                    }
                }
            });
        }
    }

    public void screenOff(String opPackageName) {
        Log.d(TAG, "screenOff");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "screenOff: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.screenOff();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "screenOff RemoteException");
                    }
                }
            });
        }
    }

    public GFConfig getConfig(String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            return null;
        }
        return new GFConfig(this.mConfig);
    }

    public GFDevice getDevice(String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            return null;
        }
        return new GFDevice(this.mDevice);
    }

    public void startHbd(IBinder token, String opPackageName) {
        Log.d(TAG, "startHbd");
        if (canUseFingerprint(opPackageName)) {
            this.mToken = token;
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "startHbd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.startHbd();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "startHbd RemoteException");
                    }
                }
            });
        }
    }

    public void stopHbd(IBinder token, String opPackageName) {
        Log.d(TAG, "stopHbd");
        if (canUseFingerprint(opPackageName)) {
            this.mToken = token;
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "stopHbd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopHbd();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "stopHbd RemoteException");
                    }
                }
            });
        }
    }

    public void dump(IGoodixFingerprintDumpCallback callback, String opPackageName) throws RemoteException {
        Log.d(TAG, "dump");
        this.mDumpCallback = callback;
        dumpCmd(1000, null, opPackageName);
    }

    public void cancelDump(String opPackageName) throws RemoteException {
        Log.d(TAG, "cancelDump");
        this.mDumpCallback = null;
        dumpCmd(1001, null, opPackageName);
    }

    public void dumpCmd(final int cmdId, final byte[] param, String opPackageName) throws RemoteException {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpCmd ");
        sb.append(cmdId);
        Log.d(str, sb.toString());
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "dumpCmd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.dumpCmd(cmdId, param);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "dumpCmd RemoteException");
                    }
                }
            });
        }
    }

    public void binderDied() {
        Log.v(TAG, "binderDied");
        this.mDaemon = null;
    }

    public int authenticateFido(IBinder token, byte[] aaid, byte[] finalChallenge, String opPackageName) {
        Log.d(TAG, "authenticateFido");
        if (!canUseFingerprint(opPackageName)) {
            return -1;
        }
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "authenticateFido:no goodixfingerprintd");
            return -1;
        }
        try {
            return daemon.authenticateFido(0, aaid, finalChallenge);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int stopAuthenticateFido(IBinder token, String opPackageName) {
        Log.d(TAG, "cancelAuthenticateFido");
        if (!canUseFingerprint(opPackageName)) {
            return -1;
        }
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "cancelAuthenticateFido:no goodixfingerprintd");
            return -1;
        }
        try {
            return daemon.stopAuthenticateFido();
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int isIdValid(IBinder token, int fingerId, String opPackageName) {
        Log.d(TAG, "isIdInvalid");
        if (!canUseFingerprint(opPackageName)) {
            return -1;
        }
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "isIdInvalid:no goodixfingerprintd");
            return -1;
        }
        try {
            return daemon.isIdValid(0, fingerId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int[] getIdList(IBinder token, String opPackageName) {
        Log.d(TAG, "getIdList");
        if (!canUseFingerprint(opPackageName)) {
            return null;
        }
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "getIdList:no goodixfingerprintd");
            return null;
        }
        try {
            return daemon.getIdList(0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int invokeFidoCommand(IBinder token, byte[] inBuf, byte[] outBuf) {
        Log.d(TAG, "invokeFidoCommand");
        this.mToken = token;
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "invokeFidoCommand:no goodixfingerprintd");
            return -1;
        }
        try {
            return daemon.invokeFidoCommand(inBuf, outBuf);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void untrustedEnroll(final IBinder token, IGoodixFingerprintCallback receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.testCmd(token, 34, null, GoodixFingerprintService.this.mOpPackageName);
                }
            });
        }
    }

    public void cancelUntrustedEnrollment(final IBinder token, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.testCmd(token, 25, null, GoodixFingerprintService.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedAuthenticate(final IBinder token, IGoodixFingerprintCallback receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.testCmd(token, 35, null, GoodixFingerprintService.this.mOpPackageName);
                }
            });
        }
    }

    public void cancelUntrustedAuthentication(final IBinder token, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.testCmd(token, 25, null, GoodixFingerprintService.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedRemove(final IBinder token, IGoodixFingerprintCallback receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintService.this.testCmd(token, 36, null, GoodixFingerprintService.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedRemoveForSec(IBinder token, IGoodixFingerprintCallback receiver, String opPackageName, int groupId, int fingerId) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
            return;
        }
        byte[] byteArray = new byte[16];
        int offset = TestParamEncoder.encodeInt32(byteArray, TestParamEncoder.encodeInt32(byteArray, 0, TestResultParser.TEST_TOKEN_UNTRUSTED_REMOVE_GROUP_ID, groupId), TestResultParser.TEST_TOKEN_UNTRUSTED_REMOVE_FINGERPRINT_ID, fingerId);
        testCmd(token, 54, byteArray, opPackageName);
    }

    public boolean hasEnrolledUntrustedFingerprint(String opPackageName) {
        boolean z = false;
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
            return false;
        }
        if (this.mUntrustedFingerprintId != 0) {
            z = true;
        }
        return z;
    }

    public int getEnrolledUntrustedFingerprint(String opPackageName) {
        if (canUseFingerprint(opPackageName)) {
            return this.mUntrustedFingerprintId;
        }
        Log.w(TAG, "Calling not granted permission to use fingerprint");
        return 0;
    }

    public void pauseEnroll(String opPackageName) {
        Log.d(TAG, "pauseEnroll");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "pauseEnroll: service not found!");
                        return;
                    }
                    try {
                        daemon.pauseEnroll();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "pauseEnroll RemoteException");
                    }
                }
            });
        }
    }

    public void resumeEnroll(String opPackageName) {
        Log.d(TAG, "resumeEnroll");
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintService.TAG, "resumeEnroll: service not found!");
                        return;
                    }
                    try {
                        daemon.resumeEnroll();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintService.TAG, "resumeEnroll RemoteException");
                    }
                }
            });
        }
    }

    public void untrustedPauseEnroll(IBinder token, IGoodixFingerprintCallback receiver, String opPackageName) {
        Log.d(TAG, "untrustedPauseEnroll");
        testCmd(token, 55, null, opPackageName);
    }

    public void untrustedResumeEnroll(IBinder token, IGoodixFingerprintCallback receiver, String opPackageName) {
        Log.d(TAG, "untrustedResumeEnroll");
        testCmd(token, 56, null, opPackageName);
    }

    /* access modifiers changed from: private */
    public Bitmap convertToBitmap(byte[] result, int width, int height) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("convertToBitmap width=");
        sb.append(width);
        sb.append(",height=");
        sb.append(height);
        sb.append(" data=");
        sb.append(result);
        Log.d(str, sb.toString());
        int length = width * height;
        int[] display = new int[length];
        for (int i = 0; i < length; i++) {
            int pixel = result[i] & 255;
            display[i] = -16777216 | (pixel << 16) | (pixel << 8) | pixel;
        }
        return Bitmap.createBitmap(display, width, height, Config.ARGB_8888);
    }

    private Bitmap convertDataToBmp8(byte[] data, int width, int height) {
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

    public void openHal() throws RemoteException {
        this.mHandler.post(new Runnable() {
            public void run() {
                IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                if (daemon == null) {
                    Log.e(GoodixFingerprintService.TAG, "pauseEnroll: service not found!");
                    return;
                }
                Log.e(GoodixFingerprintService.TAG, " daemon.openHal();");
                try {
                    daemon.openHal();
                } catch (RemoteException e) {
                    Log.e(GoodixFingerprintService.TAG, "pauseEnroll RemoteException");
                }
            }
        });
    }

    public void closeHal() throws RemoteException {
        this.mHandler.post(new Runnable() {
            public void run() {
                IGoodixFingerprintDaemon daemon = GoodixFingerprintService.this.getFingerprintDaemon();
                if (daemon == null) {
                    Log.e(GoodixFingerprintService.TAG, "closeHal: service not found!");
                    return;
                }
                try {
                    daemon.closeHal();
                } catch (RemoteException e) {
                    Log.e(GoodixFingerprintService.TAG, "pauseEnroll RemoteException");
                }
            }
        });
    }
}
