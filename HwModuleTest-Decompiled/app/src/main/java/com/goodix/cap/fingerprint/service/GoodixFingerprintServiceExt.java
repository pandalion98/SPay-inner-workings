package com.goodix.cap.fingerprint.service;

import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.cap.fingerprint.IGoodixFingerprintDaemon;
import android.hardware.cap.fingerprint.IGoodixFingerprintDaemonCallback;
import android.hardware.cap.fingerprint.IGoodixFingerprintDaemonCallback.Stub;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.util.Log;
import com.goodix.cap.fingerprint.Constants;
import com.goodix.cap.fingerprint.GFConfig;
import com.goodix.cap.fingerprint.GFDevice;
import com.goodix.cap.fingerprint.utils.FingerprintUtils;
import com.goodix.cap.fingerprint.utils.TestParamEncoder;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GoodixFingerprintServiceExt implements DeathRecipient {
    private static final String GOODIXFINGERPRINTD = "android.hardware.cap.fingerprint.IGoodixFingerprintDaemon";
    public static final String MANAGE_FINGERPRINT = "android.permission.MANAGE_FINGERPRINT";
    private static final String TAG = "GoodixFingerprintServiceExt";
    private static final int TEST_GET_CONFIG_MAX_RETRY_TIMES = 20;
    public static final String USE_FINGERPRINT = "android.permission.USE_FINGERPRINT";
    private static GoodixFingerprintServiceExt sServiceInstance;
    private AppOpsManager mAppOps = null;
    /* access modifiers changed from: private */
    public GoodixFingerprintCallbackExt mCallback = null;
    private HashMap<IBinder, GoodixFingerprintCallbackExt> mCallbackHashMap = new HashMap<>();
    /* access modifiers changed from: private */
    public GFConfig mConfig = new GFConfig();
    /* access modifiers changed from: private */
    public Context mContext = null;
    private IGoodixFingerprintDaemon mDaemon = null;
    private IGoodixFingerprintDaemonCallback mDaemonCallback = new Stub() {
        public void onEnrollResult(long deviceId, final int fingerId, int groupId, final int remaining) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollResult fingerId = ");
            sb.append(fingerId);
            sb.append(", remaining = ");
            sb.append(remaining);
            Log.d(str, sb.toString());
            GoodixFingerprintServiceExt.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback == null) {
                        String str = GoodixFingerprintServiceExt.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintServiceExt.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintServiceExt.this.mContext);
                    GoodixFingerprintServiceExt.this.mCallback.onEnrollResult(fingerId, remaining);
                    GoodixFingerprintServiceExt.this.mUntrustedFingerprintId = fingerId;
                }
            });
        }

        public void onEnrollSecResult(long deviceId, int fingerId, int groupId, int remaining, int width, int height, byte[] result, int length) {
            String str = GoodixFingerprintServiceExt.TAG;
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
            Bitmap bitmap = GoodixFingerprintServiceExt.this.convertToBitmap(bArr, bWidth, bHeight);
            C00862 r9 = r0;
            Handler access$300 = GoodixFingerprintServiceExt.this.mHandler;
            final int i6 = bWidth;
            int i7 = bHeight;
            int i8 = bWidth;
            final Bitmap bitmap2 = bitmap;
            C00862 r0 = new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback == null) {
                        String str = GoodixFingerprintServiceExt.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintServiceExt.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintServiceExt.this.mContext);
                    GoodixFingerprintServiceExt.this.mCallback.onEnrollSecResult(fingerIndex, progressRemain, i6, bHeight, bitmap2, dataLength);
                    GoodixFingerprintServiceExt.this.mUntrustedFingerprintId = fingerIndex;
                }
            };
            access$300.post(r9);
        }

        public void onCaptureInfo(long deviceId, int width, int height, int imgQuality, int score, byte[] rawData, byte[] bmpData) {
            String str = GoodixFingerprintServiceExt.TAG;
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
            Handler access$300 = GoodixFingerprintServiceExt.this.mHandler;
            final byte[] bArr3 = bArr;
            final int i4 = i;
            final int i5 = i2;
            final byte[] bArr4 = bArr2;
            final long j = deviceId;
            C00873 r0 = r3;
            final int i6 = i3;
            final int i7 = score;
            C00873 r3 = new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback == null) {
                        String str = GoodixFingerprintServiceExt.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onEnrollResult mCallback  = ");
                        sb.append(GoodixFingerprintServiceExt.this.mCallback);
                        Log.d(str, sb.toString());
                        return;
                    }
                    Bitmap rawBmp = GoodixFingerprintServiceExt.this.convertToBitmap(bArr3, i4, i5);
                    Bitmap bmp = GoodixFingerprintServiceExt.this.convertToBitmap(bArr4, i4, i5);
                    FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintServiceExt.this.mContext);
                    GoodixFingerprintServiceExt.this.mCallback.onCaptureInfo(j, rawBmp, bArr3, i4, i5, bmp, bArr4, i6, i7);
                }
            };
            access$300.post(r0);
        }

        public void onEventInfo(int eventId, byte[] eventData, int length) {
        }

        public void onAcquired(long deviceId, final int acquiredInfo) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAcquired acquiredInfo = ");
            sb.append(acquiredInfo);
            Log.d(str, sb.toString());
            GoodixFingerprintServiceExt.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback != null) {
                        GoodixFingerprintServiceExt.this.mCallback.onAcquired(acquiredInfo);
                    }
                }
            });
        }

        public void onAuthenticated(long deviceId, final int fingerId, int groupId, byte[] hat) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticated fingerId = ");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            GoodixFingerprintServiceExt.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback != null) {
                        if (fingerId != 0) {
                            GoodixFingerprintServiceExt.this.mCallback.onAuthenticationSucceeded(fingerId);
                            FingerprintUtils.vibrateFingerprintSuccess(GoodixFingerprintServiceExt.this.mContext);
                        } else {
                            GoodixFingerprintServiceExt.this.mCallback.onAuthenticationFailed();
                            FingerprintUtils.vibrateFingerprintError(GoodixFingerprintServiceExt.this.mContext);
                        }
                    }
                }
            });
        }

        public void onError(long deviceId, final int error) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onError error = ");
            sb.append(error);
            Log.d(str, sb.toString());
            GoodixFingerprintServiceExt.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback != null) {
                        GoodixFingerprintServiceExt.this.mCallback.onError(error);
                    }
                }
            });
        }

        public void onRemoved(long deviceId, final int fingerId, int groupId) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onRemoved fingerId = ");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            GoodixFingerprintServiceExt.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GoodixFingerprintServiceExt.this.mCallback != null) {
                        GoodixFingerprintServiceExt.this.mCallback.onRemoved(fingerId);
                        GoodixFingerprintServiceExt.this.mUntrustedFingerprintId = 0;
                    }
                }
            });
        }

        public void onTestCmd(long deviceId, int cmdId, byte[] result) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onTestCmd ");
            sb.append(Constants.testCmdIdToString(cmdId));
            Log.d(str, sb.toString());
            if (26 == cmdId || 27 == cmdId) {
                HashMap<Integer, Object> testResult = TestResultParser.parser(result);
                if (testResult != null) {
                    if (!testResult.containsKey(Integer.valueOf(100)) || ((Integer) testResult.get(Integer.valueOf(100))).intValue() <= 0) {
                        if (testResult.containsKey(Integer.valueOf(101))) {
                            GoodixFingerprintServiceExt.this.mConfig.mChipType = ((Integer) testResult.get(Integer.valueOf(101))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(102))) {
                            GoodixFingerprintServiceExt.this.mConfig.mChipSeries = ((Integer) testResult.get(Integer.valueOf(102))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(800))) {
                            GoodixFingerprintServiceExt.this.mConfig.mMaxFingers = ((Integer) testResult.get(Integer.valueOf(800))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_MAX_FINGERS_PER_USER))) {
                            GoodixFingerprintServiceExt.this.mConfig.mMaxFingersPerUser = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_MAX_FINGERS_PER_USER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_KEY_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportKeyMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_KEY_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FF_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportFFMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_POWER_KEY_FEATURE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportPowerKeyFeature = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_POWER_KEY_FEATURE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_UNTRUSTED_ENROLL))) {
                            GoodixFingerprintServiceExt.this.mConfig.mForbiddenUntrustedEnroll = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_UNTRUSTED_ENROLL))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_ENROLL_DUPLICATE_FINGERS))) {
                            GoodixFingerprintServiceExt.this.mConfig.mForbiddenEnrollDuplicateFingers = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FORBIDDEN_ENROLL_DUPLICATE_FINGERS))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_BIO_ASSAY))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportBioAssay = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_BIO_ASSAY))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_PERFORMANCE_DUMP))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportPerformanceDump = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_PERFORMANCE_DUMP))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_NAV_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportNavMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_NAV_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_DOUBLE_CLICK_TIME))) {
                            GoodixFingerprintServiceExt.this.mConfig.mNavDoubleClickTime = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_DOUBLE_CLICK_TIME))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_LONG_PRESS_TIME))) {
                            GoodixFingerprintServiceExt.this.mConfig.mNavLongPressTime = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_NAV_LONG_PRESS_TIME))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_ENROLLING_MIN_TEMPLATES))) {
                            GoodixFingerprintServiceExt.this.mConfig.mEnrollingMinTemplates = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_ENROLLING_MIN_TEMPLATES))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_QUALITY_THRESHOLD))) {
                            GoodixFingerprintServiceExt.this.mConfig.mValidImageQualityThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_QUALITY_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_AREA_THRESHOLD))) {
                            GoodixFingerprintServiceExt.this.mConfig.mValidImageAreaThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_VALID_IMAGE_AREA_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_DUPLICATE_FINGER_OVERLAY_SCORE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mDuplicateFingerOverlayScore = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_DUPLICATE_FINGER_OVERLAY_SCORE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_INCREASE_RATE_BETWEEN_STITCH_INFO))) {
                            GoodixFingerprintServiceExt.this.mConfig.mIncreaseRateBetweenStitchInfo = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_INCREASE_RATE_BETWEEN_STITCH_INFO))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_AUTHENTICATE_FAIL_RETRY_COUNT))) {
                            GoodixFingerprintServiceExt.this.mConfig.mScreenOnAuthenticateFailRetryCount = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_AUTHENTICATE_FAIL_RETRY_COUNT))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_AUTHENTICATE_FAIL_RETRY_COUNT))) {
                            GoodixFingerprintServiceExt.this.mConfig.mScreenOffAuthenticateFailRetryCount = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_AUTHENTICATE_FAIL_RETRY_COUNT))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_VALID_TOUCH_FRAME_THRESHOLD))) {
                            GoodixFingerprintServiceExt.this.mConfig.mScreenOnValidTouchFrameThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_ON_VALID_TOUCH_FRAME_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_VALID_TOUCH_FRAME_THRESHOLD))) {
                            GoodixFingerprintServiceExt.this.mConfig.mScreenOffValidTouchFrameThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SCREEN_OFF_VALID_TOUCH_FRAME_THRESHOLD))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IMAGE_QUALITY_THRESHOLD_FOR_MISTAKE_TOUCH))) {
                            GoodixFingerprintServiceExt.this.mConfig.mImageQualityThresholdForMistakeTouch = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IMAGE_QUALITY_THRESHOLD_FOR_MISTAKE_TOUCH))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_AUTHENTICATE_ORDER))) {
                            GoodixFingerprintServiceExt.this.mConfig.mAuthenticateOrder = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_AUTHENTICATE_ORDER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_FF_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mReissueKeyDownWhenEntryFfMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_IMAGE_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mReissueKeyDownWhenEntryImageMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_IMAGE_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SENSOR_BROKEN_CHECK))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportSensorBrokenCheck = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SENSOR_BROKEN_CHECK))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_SENSOR))) {
                            GoodixFingerprintServiceExt.this.mConfig.mBrokenPixelThresholdForDisableSensor = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_SENSOR))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_STUDY))) {
                            GoodixFingerprintServiceExt.this.mConfig.mBrokenPixelThresholdForDisableStudy = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_STUDY))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_BAD_POINT_TEST_MAX_FRAME_NUMBER))) {
                            GoodixFingerprintServiceExt.this.mConfig.mBadPointTestMaxFrameNumber = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_BAD_POINT_TEST_MAX_FRAME_NUMBER))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REPORT_KEY_EVENT_ONLY_ENROLL_AUTHENTICATE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mReportKeyEventOnlyEnrollAuthenticate = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REPORT_KEY_EVENT_ONLY_ENROLL_AUTHENTICATE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_IMAGE_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mRequireDownAndUpInPairsForImageMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_IMAGE_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_FF_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mRequireDownAndUpInPairsForFFMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_FF_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_KEY_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mRequireDownAndUpInPairsForKeyMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_KEY_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_NAV_MODE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mRequireDownAndUpInPairsForNavMode = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_NAV_MODE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SET_SPI_SPEED_IN_TEE))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportSetSpiSpeedInTEE = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_SET_SPI_SPEED_IN_TEE))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FRR_ANALYSIS))) {
                            GoodixFingerprintServiceExt.this.mConfig.mSupportFrrAnalysis = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SUPPORT_FRR_ANALYSIS))).intValue();
                        }
                        if (testResult.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_TEMPLATE_UPDATE_SAVE_THRESHOLD))) {
                            GoodixFingerprintServiceExt.this.mConfig.mTemplateUpateSaveThreshold = ((Integer) testResult.get(Integer.valueOf(TestResultParser.TEST_TOKEN_TEMPLATE_UPDATE_SAVE_THRESHOLD))).intValue();
                        }
                        if (GoodixFingerprintServiceExt.this.mConfig.mMaxFingers < GoodixFingerprintServiceExt.this.mConfig.mMaxFingersPerUser) {
                            GoodixFingerprintServiceExt.this.mConfig.mMaxFingers = GoodixFingerprintServiceExt.this.mConfig.mMaxFingersPerUser;
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
            String str2 = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onTestCmd mCallback= ");
            sb2.append(GoodixFingerprintServiceExt.this.mCallback);
            Log.d(str2, sb2.toString());
            if (GoodixFingerprintServiceExt.this.mCallback != null) {
                GoodixFingerprintServiceExt.this.mCallback.onTestCmd(cmdId, result);
            }
        }

        public void onHbdData(long deviceId, int heartBeatRate, int status, int[] displayData, int[] rawData) {
            Log.d(GoodixFingerprintServiceExt.TAG, "onHbdData");
            if (GoodixFingerprintServiceExt.this.mCallback != null) {
                GoodixFingerprintServiceExt.this.mCallback.onHbdData(heartBeatRate, status, displayData, rawData);
            }
        }

        public void onDump(long deviceId, int cmdId, byte[] data) {
            Log.d(GoodixFingerprintServiceExt.TAG, "onDump");
            if (GoodixFingerprintServiceExt.this.mDumpCallback != null) {
                try {
                    GoodixFingerprintServiceExt.this.mDumpCallback.onDump(cmdId, data);
                } catch (RemoteException e) {
                }
            }
        }

        public void onAuthenticatedFido(long devId, int fingerId, byte[] uvtData) {
            String str = GoodixFingerprintServiceExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticatedFido, fingerId:");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            if (GoodixFingerprintServiceExt.this.mCallback != null) {
                GoodixFingerprintServiceExt.this.mCallback.onAuthenticateFido(fingerId, uvtData);
            } else {
                Log.e(GoodixFingerprintServiceExt.TAG, "mCallback is null");
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
    private IBinder mToken = null;
    /* access modifiers changed from: private */
    public int mUntrustedFingerprintId = 0;

    public GoodixFingerprintServiceExt(Context context) {
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mOpPackageName = getAppOpPackageName();
        this.mOpUseFingerprint = getOpUseFingerprint();
    }

    public static GoodixFingerprintServiceExt getInstance(Context context) {
        if (sServiceInstance == null) {
            synchronized (GoodixFingerprintServiceExt.class) {
                if (sServiceInstance == null) {
                    sServiceInstance = new GoodixFingerprintServiceExt(context);
                }
            }
        }
        return sServiceInstance;
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
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(opPackageName);
            sb.append(" can not use fingerprint");
            Log.d(str, sb.toString());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public IGoodixFingerprintDaemon getFingerprintDaemon() {
        Log.d(TAG, "getFingerprintDaemon()");
        if (this.mDaemon == null) {
            try {
                this.mDaemon = IGoodixFingerprintDaemon.Stub.asInterface((IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{GOODIXFINGERPRINTD}));
                if (this.mDaemon != null) {
                    this.mDaemon.asBinder().linkToDeath(this, 0);
                }
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
        Log.d(TAG, "getFingerprintDaemon() success.");
        return this.mDaemon;
    }

    public void initCallback(IBinder token, GoodixFingerprintCallbackExt callback, String opPackageName) {
        Log.d(TAG, "initCallback");
        if (canUseFingerprint(opPackageName)) {
            this.mCallback = callback;
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "testCmd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        if (daemon.testCmd(cmdId, param) != 0 && 26 == cmdId && GoodixFingerprintServiceExt.this.mGetConfigRetryCount < 20) {
                            GoodixFingerprintServiceExt.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    GoodixFingerprintServiceExt.this.testCmd(token, 26, null, GoodixFingerprintServiceExt.this.mOpPackageName);
                                    GoodixFingerprintServiceExt.this.mGetConfigRetryCount = GoodixFingerprintServiceExt.this.mGetConfigRetryCount + 1;
                                    String str = GoodixFingerprintServiceExt.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("CMD_TEST_GET_CONFIG retry count : ");
                                    sb.append(GoodixFingerprintServiceExt.this.mGetConfigRetryCount);
                                    Log.i(str, sb.toString());
                                }
                            }, 1000);
                        }
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "testCmd RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "setSafeClass: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.setSafeClass(safeClass);
                        GoodixFingerprintServiceExt.this.mDevice.mSafeClass = safeClass;
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "setSafeClass RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "navigate: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.navigate(navMode);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "navigate RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopNavigation: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopNavigation();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopNavigation RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "enableFingerprintModule: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.enableFingerprintModule(enable ? (byte) 1 : 0);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "enableFingerprintModule RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "cameraCapture: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.cameraCapture();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "cameraCapture RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopCameraCapture: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopCameraCapture();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopCameraCapture RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "enableFfFeature: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.enableFfFeature(enable ? (byte) 1 : 0);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "enableFfFeature RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "screenOn: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.screenOn();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "screenOn RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "screenOff: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.screenOff();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "screenOff RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "startHbd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.startHbd();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "startHbd RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopHbd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.stopHbd();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "stopHbd RemoteException");
                    }
                }
            });
        }
    }

    public void dump(IGoodixFingerprintDumpCallback callback, String opPackageName) {
        Log.d(TAG, "dump");
        this.mDumpCallback = callback;
        dumpCmd(1000, null, opPackageName);
    }

    public void cancelDump(String opPackageName) {
        Log.d(TAG, "cancelDump");
        this.mDumpCallback = null;
        dumpCmd(1001, null, opPackageName);
    }

    public void dumpCmd(final int cmdId, final byte[] param, String opPackageName) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpCmd ");
        sb.append(cmdId);
        Log.d(str, sb.toString());
        if (canUseFingerprint(opPackageName)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "dumpCmd: no goodixfingeprintd!");
                        return;
                    }
                    try {
                        daemon.dumpCmd(cmdId, param);
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "dumpCmd RemoteException");
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

    public void untrustedEnroll(final IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintServiceExt.this.testCmd(token, 34, null, GoodixFingerprintServiceExt.this.mOpPackageName);
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
                    GoodixFingerprintServiceExt.this.testCmd(token, 25, null, GoodixFingerprintServiceExt.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedAuthenticate(final IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintServiceExt.this.testCmd(token, 35, null, GoodixFingerprintServiceExt.this.mOpPackageName);
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
                    GoodixFingerprintServiceExt.this.testCmd(token, 25, null, GoodixFingerprintServiceExt.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedRemove(final IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName) {
        if (!canUseFingerprint(opPackageName)) {
            Log.w(TAG, "Calling not granted permission to use fingerprint");
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    GoodixFingerprintServiceExt.this.testCmd(token, 36, null, GoodixFingerprintServiceExt.this.mOpPackageName);
                }
            });
        }
    }

    public void untrustedRemoveForSec(IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName, int groupId, int fingerId) {
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "pauseEnroll: service not found!");
                        return;
                    }
                    try {
                        daemon.pauseEnroll();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "pauseEnroll RemoteException");
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
                    IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                    if (daemon == null) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "resumeEnroll: service not found!");
                        return;
                    }
                    try {
                        daemon.resumeEnroll();
                    } catch (RemoteException e) {
                        Log.e(GoodixFingerprintServiceExt.TAG, "resumeEnroll RemoteException");
                    }
                }
            });
        }
    }

    public void untrustedPauseEnroll(IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName) {
        Log.d(TAG, "untrustedPauseEnroll");
        testCmd(token, 55, null, opPackageName);
    }

    public void untrustedResumeEnroll(IBinder token, GoodixFingerprintCallbackExt receiver, String opPackageName) {
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

    public void openHal() {
        this.mHandler.post(new Runnable() {
            public void run() {
                IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                if (daemon == null) {
                    Log.e(GoodixFingerprintServiceExt.TAG, "pauseEnroll: service not found!");
                    return;
                }
                Log.e(GoodixFingerprintServiceExt.TAG, " daemon.openHal();");
                try {
                    daemon.openHal();
                } catch (RemoteException e) {
                    Log.e(GoodixFingerprintServiceExt.TAG, "pauseEnroll RemoteException");
                }
            }
        });
    }

    public void closeHal() {
        this.mHandler.post(new Runnable() {
            public void run() {
                IGoodixFingerprintDaemon daemon = GoodixFingerprintServiceExt.this.getFingerprintDaemon();
                if (daemon == null) {
                    Log.e(GoodixFingerprintServiceExt.TAG, "closeHal: service not found!");
                    return;
                }
                try {
                    daemon.closeHal();
                } catch (RemoteException e) {
                    Log.e(GoodixFingerprintServiceExt.TAG, "pauseEnroll RemoteException");
                }
            }
        });
    }

    public void startUntouchTest() {
        Log.d(TAG, "startUntouchTest");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startUntouchTest: service not found!");
            return;
        }
        try {
            daemon.startUntouchTest();
        } catch (RemoteException e) {
            Log.e(TAG, "startUntouchTest RemoteException");
        }
    }

    public void startTouchPrepareTest() {
        Log.d(TAG, "startTouchPrepareTest");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startTouchPrepareTest: service not found!");
            return;
        }
        try {
            daemon.startTouchPrepareTest();
        } catch (RemoteException e) {
            Log.e(TAG, "startTouchPrepareTest RemoteException");
        }
    }

    public void startTouchTest() {
        Log.d(TAG, "startTouchTest");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startTouchTest: service not found!");
            return;
        }
        try {
            daemon.startTouchTest();
        } catch (RemoteException e) {
            Log.e(TAG, "startTouchTest RemoteException");
        }
    }

    public void startOtpCheckTest() {
        Log.d(TAG, "startOtpCheckTest");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startOtpCheckTest: service not found!");
            return;
        }
        try {
            daemon.startOtpCheckTest();
        } catch (RemoteException e) {
            Log.e(TAG, "startOtpCheckTest RemoteException");
        }
    }

    public void startSensorInfoTest() {
        Log.d(TAG, "startSensorInfoTest");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startSensorInfoTest: service not found!");
            return;
        }
        try {
            daemon.startSensorInfoTest();
        } catch (RemoteException e) {
            Log.e(TAG, "startSensorInfoTest RemoteException");
        }
    }

    public String startGetVersion() {
        String versionStr = "";
        Log.d(TAG, "startGetVersion");
        IGoodixFingerprintDaemon daemon = getFingerprintDaemon();
        if (daemon == null) {
            Log.e(TAG, "startGetVersion: service not found!");
            return "";
        }
        try {
            byte[] version = daemon.startGetVersion();
            for (int i = 0; i < version.length; i += 4) {
                StringBuilder sb = new StringBuilder();
                sb.append(versionStr);
                sb.append((char) version[i]);
                versionStr = sb.toString();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "startGetVersion RemoteException");
        }
        return versionStr;
    }
}
