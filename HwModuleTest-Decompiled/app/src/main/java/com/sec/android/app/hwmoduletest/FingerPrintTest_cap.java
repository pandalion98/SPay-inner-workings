package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.goodix.cap.fingerprint.sec.GoodixFpCaptureInfo;
import com.goodix.cap.fingerprint.sec.GoodixFpEnrollStatus;
import com.goodix.cap.fingerprint.sec.GoodixFpIdentifyResult;
import com.goodix.cap.fingerprint.sec.p001mt.GFTestManager;
import com.goodix.cap.fingerprint.sec.p001mt.TestResultNotify;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import java.io.File;
import java.util.concurrent.Semaphore;

public class FingerPrintTest_cap extends BaseActivity {
    private static final String CLASS_NAME = "FingerPrintTest_cap";
    public static final int MSG_CAPTURE_FAIL = 8;
    public static final int MSG_CAPTURE_SUCCESS = 7;
    public static final int MSG_ENROLL_SUCCESS = 9;
    public static final int MSG_IDENTIFY_FAILED = 12;
    public static final int MSG_IDENTIFY_SUCCESS = 11;
    public static final int MSG_PREPARE_TOUCH = 10;
    public static final int MSG_SHOW_NORMAL_SCAN_READ_DONE_FAIL_RESULT = 3;
    public static final int MSG_SHOW_NORMAL_SCAN_RESULT = 2;
    public static final int MSG_SHOW_SENSOR_INFO = 14;
    public static final int MSG_SHOW_SNR_READ_DONE_FAIL_RESULT = 6;
    public static final int MSG_SHOW_SNR_RESULT = 5;
    public static final int MSG_START_NO_TOUCH_TEST = 1;
    public static final int MSG_START_SNR_TEST = 4;
    public static final int MSG_START_VERIFY_TEST = 13;
    private int IDENTIFY_FAILURE_SENSOR_CHANGED = 59;
    public final int STATE_NONE = 1000;
    public final int STATE_NORMALSCAN = 1001;
    public final int STATE_NORMALSCAN_FINISH = 1002;
    public final int STATE_NORMAL_FAIL_AND_STOP = 2001;
    private final int STATE_NORMAL_SCAN = 1014;
    private boolean STATE_REPLACEMENT_WARNING = false;
    private final int STATE_SNR = 1016;
    public final int STATE_SNR_FAIL_AND_STOP = 2004;
    public final int STATE_SNR_NOTERM_SCAN = 1015;
    private final int STATE_SNR_SCAN = 1018;
    public final int STATE_SNR_SCAN_FINISH = 1018;
    public final int STATE_SNR_TERM_SCAN_PROCESSING = 1017;
    public final int STATE_SNR_TERM_SCAN_READY = 1016;
    private final String USER_ID = "Test";
    /* access modifiers changed from: private */
    public Button mBtnGoTerm;
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    /* access modifiers changed from: private */
    public FingerPrintSNRData mFingerPrintSNRData;
    /* access modifiers changed from: private */
    public FingerPrintSensorInfo mFingerPrintSensorInfo;
    /* access modifiers changed from: private */
    public GFTestManager mGFTestManager;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(msg.what);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "handleMessage", sb.toString());
            int i = msg.what;
            if (i == 10) {
                FingerPrintTest_cap.this.mTvMsg1.setText("SNR Test Data Scan");
                FingerPrintTest_cap.this.mTvMsg2.setText("Prepare to Touch Test...");
                FingerPrintTest_cap.this.mResultView.setVisibility(8);
                FingerPrintTest_cap.this.mSNRZoneView.setVisibility(8);
                FingerPrintTest_cap.this.mTvResult.setVisibility(8);
                FingerPrintTest_cap.this.mGFTestManager.startTouchIntRstTest();
            } else if (i != 14) {
                switch (i) {
                    case 2:
                        FingerPrintTest_cap.this.mTvMsg1.setText("");
                        FingerPrintTest_cap.this.mTvMsg2.setText("Normal Data Scan finished");
                        FingerPrintTest_cap.this.updateNormalScanResult();
                        FingerPrintTest_cap.this.mTvResult.setVisibility(0);
                        if (FingerPrintTest_cap.this.mFingerPrintNormalData.processData()) {
                            FingerPrintTest_cap.this.mTvResult.setTextColor(-16776961);
                            FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.PASS);
                        } else {
                            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "handleMessage", "Fail - NormalData Spec Out");
                            FingerPrintTest_cap.this.mTvResult.setTextColor(-65536);
                            FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.FAIL);
                        }
                        FingerPrintTest_cap.this.mFingerPrintNormalData.saveNormalDataResult();
                        return;
                    case 3:
                        FingerPrintTest_cap.this.mTvResult.setVisibility(0);
                        FingerPrintTest_cap.this.mTvResult.setTextColor(-65536);
                        FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.FAIL);
                        return;
                    case 4:
                        FingerPrintTest_cap.this.mTvMsg2.setText("Put stimulus on the sensor\nand press OK or HomeButton");
                        FingerPrintTest_cap.this.mBtnGoTerm.setText("O K");
                        FingerPrintTest_cap.this.mBtnGoTerm.setVisibility(0);
                        return;
                    case 5:
                        FingerPrintTest_cap.this.mTvMsg2.setText("SNR data scan finished");
                        FingerPrintTest_cap.this.updateSNRResult();
                        Bitmap bitmap = BitmapFactory.decodeFile("/data/log/touch_test.bmp");
                        FingerPrintTest_cap.this.mSNRZoneView.setVisibility(4);
                        FingerPrintTest_cap.this.mIvSnrImage.setVisibility(0);
                        FingerPrintTest_cap.this.mIvSnrImage.setImageBitmap(bitmap);
                        FingerPrintTest_cap.this.mTvResult.setVisibility(0);
                        if (FingerPrintTest_cap.this.mFingerPrintSNRData.processData()) {
                            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "handleMessage", "processdata pass");
                            FingerPrintTest_cap.this.mTvResult.setTextColor(-16776961);
                            FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.PASS);
                        } else {
                            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "handleMessage", "processdata fail");
                            FingerPrintTest_cap.this.mTvResult.setTextColor(-65536);
                            FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.FAIL);
                        }
                        FingerPrintTest_cap.this.mFingerPrintSNRData.saveSNRDataResult();
                        return;
                    case 6:
                        FingerPrintTest_cap.this.mTvResult.setVisibility(0);
                        FingerPrintTest_cap.this.mTvResult.setTextColor(-65536);
                        FingerPrintTest_cap.this.mTvResult.setText(C0268R.string.FAIL);
                        return;
                    default:
                        return;
                }
            } else {
                StringBuilder strResult = new StringBuilder();
                strResult.append("UI Product ID : ");
                strResult.append(FingerPrintTest_cap.this.mFingerPrintSensorInfo.getProductId());
                strResult.append("\nModule Test : ");
                strResult.append(FingerPrintTest_cap.this.mFingerPrintSensorInfo.getIsModulePass());
                strResult.append("\nCalibration : ");
                strResult.append(FingerPrintTest_cap.this.mFingerPrintSensorInfo.getCalibration());
                strResult.append("\nUID : ");
                strResult.append(FingerPrintTest_cap.this.mFingerPrintSensorInfo.getUid());
                FingerPrintTest_cap.this.mTvMsg2.setText("Sensor Infomation");
                FingerPrintTest_cap.this.mTvResult.setText(strResult.toString());
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView mIvSnrImage;
    private Semaphore mLock = new Semaphore(1);
    /* access modifiers changed from: private */
    public FingerPrintResultView_touch mResultView;
    /* access modifiers changed from: private */
    public FingerPrintResultView_touch mSNRZoneView;
    private String mStrMsg = "Ready";
    private String mTestName;
    private TestResultNotify mTestResultNotify = new TestResultNotify() {
        public void onCaptureFingerLeaved(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onCaptureFingerLeaved", sb.toString());
            FingerPrintTest_cap.this.mHandler.sendEmptyMessageDelayed(13, 1000);
        }

        public void onCaptureNotify(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onCaptureNotify", sb.toString());
            GoodixFpCaptureInfo enrollCaptureInfo = (GoodixFpCaptureInfo) _objResult;
            Message message = FingerPrintTest_cap.this.mHandler.obtainMessage();
            message.arg1 = enrollCaptureInfo.quality;
            switch (cmdId) {
                case 4004:
                    LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onCaptureNotify", "CAPTURE_SUCCESS");
                    message.what = 7;
                    message.obj = enrollCaptureInfo.bitmap;
                    FingerPrintTest_cap.this.mHandler.sendMessage(message);
                    return;
                case 4005:
                    LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onCaptureNotify", "CAPTURE_FAILED");
                    message.what = 8;
                    message.obj = enrollCaptureInfo.bitmap;
                    FingerPrintTest_cap.this.mHandler.sendMessage(message);
                    return;
                default:
                    return;
            }
        }

        public void onEnrollStatus(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onEnrollStatus", sb.toString());
            GoodixFpEnrollStatus enrollStatus = (GoodixFpEnrollStatus) _objResult;
            Message message = FingerPrintTest_cap.this.mHandler.obtainMessage();
            switch (cmdId) {
                case 5001:
                    LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onEnrollStatus", "onEnrollStatus : ENROLL_STATUS");
                    return;
                case 5002:
                    LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onEnrollStatus", "onEnrollStatus : ENROLL_SUCCESS");
                    message.what = 9;
                    message.obj = enrollStatus.coverageBitmap;
                    FingerPrintTest_cap.this.mHandler.sendMessage(message);
                    return;
                default:
                    return;
            }
        }

        public void onIdentifyStatus(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onIdentifyStatus", sb.toString());
            GoodixFpIdentifyResult identifyStatus = (GoodixFpIdentifyResult) _objResult;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("userId = ");
            sb2.append(identifyStatus.userId);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onIdentifyStatus", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("result = ");
            sb3.append(identifyStatus.result);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onIdentifyStatus", sb3.toString());
            Message message = FingerPrintTest_cap.this.mHandler.obtainMessage();
            switch (cmdId) {
                case 6001:
                    message.what = 11;
                    FingerPrintTest_cap.this.mHandler.sendMessage(message);
                    return;
                case 6002:
                    message.what = 12;
                    FingerPrintTest_cap.this.mHandler.sendMessage(message);
                    return;
                default:
                    return;
            }
        }

        public void onGetSensorInfoNotify(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", _objResult : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoStatus", sb.toString());
            if (cmdId == 923) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", "CMD_GET_SENSOR_INFO_START");
            } else if (cmdId == 924) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", "CMD_GET_SENSOR_INFO_END");
                FingerPrintTest_cap.this.mHandler.sendEmptyMessage(14);
            } else if (cmdId == 914) {
                int nModulePass = ((Integer) _objResult).intValue();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("CMD_GET_INFO_IS_MODULE_PASS : ");
                sb2.append(nModulePass);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb2.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setModulePass(nModulePass);
            } else if (cmdId == 915) {
                int nTCode = ((Integer) _objResult).intValue();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("CMD_GET_INFO_TCODE : ");
                sb3.append(nTCode);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb3.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setTCode(nTCode);
            } else if (cmdId == 916) {
                int nDac = ((Integer) _objResult).intValue();
                StringBuilder sb4 = new StringBuilder();
                sb4.append("CMD_GET_INFO_DAC : ");
                sb4.append(nDac);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb4.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setDac(nDac);
            } else if (cmdId == 927) {
                String strProdId = (String) _objResult;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("CMD_GET_INFO_PRODUCT_ID : ");
                sb5.append(strProdId);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb5.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setProductId(strProdId);
            } else if (cmdId == 928) {
                String strUid = (String) _objResult;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("CMD_GET_INFO_UID : ");
                sb6.append(strUid);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb6.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setUid(strUid);
            } else if (cmdId == 929) {
                int nCal = ((Integer) _objResult).intValue();
                StringBuilder sb7 = new StringBuilder();
                sb7.append("CMD_GET_INFO_CALIBRATION : ");
                sb7.append(nCal);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb7.toString());
                FingerPrintTest_cap.this.mFingerPrintSensorInfo.setCalibration(nCal);
            } else {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("unknown : ");
                sb8.append(cmdId);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onGetSensorInfoNotify", sb8.toString());
            }
        }

        public void onOTPCheckNotify(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onOTPCheckNotify", sb.toString());
            if (cmdId == 32) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onOTPCheckNotify", "CMD_OTP_CHECK_TEST ");
            }
            FingerPrintTest_cap.this.mFingerPrintNormalData.addOTP(_objResult.toString());
        }

        public void onTouchResultNotify(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", sb.toString());
            if (cmdId == 919) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_TEST_START");
            } else if (cmdId == 920) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_TEST_END");
                if (FingerPrintTest_cap.this.mFingerPrintSNRData.isReadDone()) {
                    FingerPrintTest_cap.this.mHandler.sendEmptyMessage(5);
                } else {
                    FingerPrintTest_cap.this.mHandler.sendEmptyMessage(6);
                }
            } else if (cmdId == 24) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_INT_TEST");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addPinOpenShortINT(_objResult.toString());
            } else if (cmdId == 23) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_RST_TEST");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addPinOpenShortRST(_objResult.toString());
            } else if (cmdId == 904) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_NOISE");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addNoise(String.format("%.2f", new Object[]{Float.valueOf(Float.parseFloat(_objResult.toString()))}));
            } else if (cmdId == 905) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_SNR");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addSNR(String.format("%.2f", new Object[]{Float.valueOf(Float.parseFloat(_objResult.toString()))}));
            } else if (cmdId == 906) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_TOTAL_PIXEL");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addDeltaPixelTotal(_objResult.toString());
            } else if (cmdId == 907) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_LOCAL_PIXEL");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addDeltaPixelLocal(_objResult.toString());
            } else if (cmdId == 908) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_DATA_DIFFERENCE");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addDataDifference(_objResult.toString());
            } else if (cmdId == 912) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_RUBBER_PLACEMENT");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addRubberPlacement(String.format("%.2f", new Object[]{Float.valueOf(Float.parseFloat(_objResult.toString()))}));
            } else if (cmdId == 909) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_PIXEL_DOWN");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addPixelKeyDown(_objResult.toString());
            } else if (cmdId == 910) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "-CMD_TOUCH_PIXEL_UP");
            } else if (cmdId == 913) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_PERFORMANCE");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addPerformance(_objResult.toString());
            } else if (cmdId == 911) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "-CMD_TOUCH_FALSE_IRQ");
            } else if (cmdId == 930) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_SATURATION_MAX");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addSaturationMaxUp(_objResult.toString());
            } else if (cmdId == 931) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_SATURATION_MIN");
                FingerPrintTest_cap.this.mFingerPrintSNRData.addSaturationMaxDown(_objResult.toString());
            } else if (cmdId == 902) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_WAITING");
                FingerPrintTest_cap.this.mHandler.sendEmptyMessage(4);
            } else if (cmdId == 903) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", "CMD_TOUCH_WAITING_FAILED");
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unknown : ");
                sb2.append(cmdId);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onTouchResultNotify", sb2.toString());
            }
        }

        public void onUnTouchResultNotify(int cmdId, Object _objResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("CMD ID : ");
            sb.append(cmdId);
            sb.append(", result : ");
            sb.append(_objResult);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", sb.toString());
            if (cmdId == 917) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", "CMD_UNTOUCH_TEST_START");
            } else if (cmdId == 918) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", "CMD_UNTOUCH_TEST_END");
                if (FingerPrintTest_cap.this.mFingerPrintNormalData.isReadDone()) {
                    FingerPrintTest_cap.this.mHandler.sendEmptyMessage(2);
                } else {
                    FingerPrintTest_cap.this.mHandler.sendEmptyMessage(3);
                }
            } else if (cmdId == 901) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", "CMD_UN_TOUCH_LOCAL_PIXEL");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addLocalPx(_objResult.toString());
            } else if (cmdId == 8) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_UN_TOUCH_SPI_TEST");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addSPI(_objResult.toString());
            } else if (cmdId == 900) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_UN_TOUCH_TOTAL_PIXEL");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addTotalPx(_objResult.toString());
            } else if (cmdId == 32) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_OTP_CHECK_TEST");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addOTP(_objResult.toString());
            } else if (cmdId == 925) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_FDT_ZONE_8x1_PIXEL_NUM");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addFDT8x1Zone(_objResult.toString());
            } else if (cmdId == 926) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_FDT_ZONE_8x7_PIXEL_NUM");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addFDT8x7Zone(_objResult.toString());
            } else if (cmdId == 72) {
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", " CMD_UN_TOUCH_PIXEL_SHORT");
                FingerPrintTest_cap.this.mFingerPrintNormalData.addBadPixelStreak(_objResult.toString());
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unknown : ");
                sb2.append(cmdId);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onUnTouchResultNotify", sb2.toString());
            }
        }
    };
    private Thread mThread;
    private IBinder mToken;
    /* access modifiers changed from: private */
    public TextView mTvMsg1;
    /* access modifiers changed from: private */
    public TextView mTvMsg2;
    /* access modifiers changed from: private */
    public TextView mTvResult;
    private String m_PopupMessage = "";
    private String m_ResultMessage = "";

    private class FingerPrintNormalData {
        private static final int ITEM_COUNT = 7;
        private NormalItem mItemBadPixelStreak;
        private NormalItem mItemFDT8x1Zone;
        private NormalItem mItemFDT8x7Zone;
        private NormalItem mItemLocalPx;
        private NormalItem mItemOTP;
        private NormalItem mItemSPI;
        private NormalItem mItemTotalPx;
        private String mStrProcessResult;
        private int mnReadCnt;

        private class NormalItem {
            private boolean mPass;
            private Spec<Integer> mSpec;
            private String mValue;

            public NormalItem() {
                this.mValue = "0";
                this.mPass = false;
            }

            public NormalItem(String value, boolean pass) {
                this.mValue = value;
                this.mPass = pass;
            }

            public void setValue(String value) {
                this.mValue = value;
            }

            public void setPass(boolean pass) {
                this.mPass = pass;
            }

            public void setSpec(Spec<Integer> spec) {
                this.mSpec = spec;
            }

            public String getValue() {
                return this.mValue;
            }

            public boolean getPass() {
                return this.mPass;
            }

            public Spec<Integer> getSpec() {
                return this.mSpec;
            }
        }

        private FingerPrintNormalData() {
            this.mnReadCnt = 0;
        }

        public void reset() {
            this.mnReadCnt = 0;
        }

        public boolean isReadDone() {
            boolean isDone = this.mnReadCnt == 7;
            StringBuilder sb = new StringBuilder();
            sb.append("nrsc : ");
            sb.append(isDone);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "isReadDone", sb.toString());
            return isDone;
        }

        public void addSPI(String _strValue) {
            this.mItemSPI = new NormalItem();
            addItem(this.mItemSPI, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIN_OPEN_SHORT_SPI)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIN_OPEN_SHORT_SPI))));
            this.mnReadCnt++;
        }

        public void addTotalPx(String _strValue) {
            this.mItemTotalPx = new NormalItem();
            addItem(this.mItemTotalPx, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MAX))));
            this.mnReadCnt++;
        }

        public void addLocalPx(String _strValue) {
            this.mItemLocalPx = new NormalItem();
            addItem(this.mItemLocalPx, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MAX))));
            this.mnReadCnt++;
        }

        public void addOTP(String _strValue) {
            this.mItemOTP = new NormalItem();
            addItem(this.mItemOTP, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_OTP)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_OTP))));
            this.mnReadCnt++;
        }

        public void addFDT8x1Zone(String _strValue) {
            this.mItemFDT8x1Zone = new NormalItem();
            addItem(this.mItemFDT8x1Zone, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MAX))));
            this.mnReadCnt++;
        }

        public void addFDT8x7Zone(String _strValue) {
            this.mItemFDT8x7Zone = new NormalItem();
            addItem(this.mItemFDT8x7Zone, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MAX))));
            this.mnReadCnt++;
        }

        public void addBadPixelStreak(String _strValue) {
            this.mItemBadPixelStreak = new NormalItem();
            addItem(this.mItemBadPixelStreak, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MAX))));
            this.mnReadCnt++;
        }

        private void addItem(NormalItem item, String _strValue, Spec<Integer> spec) {
            int value = Integer.parseInt(_strValue);
            boolean pass = false;
            if (value >= ((Integer) spec.lower).intValue() && value <= ((Integer) spec.upper).intValue()) {
                pass = true;
            }
            item.setSpec(spec);
            item.setValue(_strValue);
            item.setPass(pass);
        }

        public String getSPIValue() {
            return this.mItemSPI.getValue();
        }

        public String getTotalPxValue() {
            return this.mItemTotalPx.getValue();
        }

        public String getLocalPxValue() {
            return this.mItemLocalPx.getValue();
        }

        public String getOTPValue() {
            return this.mItemOTP.getValue();
        }

        public String getFDT8x1ZoneValue() {
            return this.mItemFDT8x1Zone.getValue();
        }

        public String getFDT8x7ZoneValue() {
            return this.mItemFDT8x7Zone.getValue();
        }

        public String getBadPixelStreakValue() {
            return this.mItemBadPixelStreak.getValue();
        }

        public boolean getSPIPass() {
            return this.mItemSPI.getPass();
        }

        public boolean getTotalPxPass() {
            return this.mItemTotalPx.getPass();
        }

        public boolean getLocalPxPass() {
            return this.mItemLocalPx.getPass();
        }

        public boolean getOTPPass() {
            return this.mItemOTP.getPass();
        }

        public boolean getFDT8x1ZonePass() {
            return this.mItemFDT8x1Zone.getPass();
        }

        public boolean getFDT8x7ZonePass() {
            return this.mItemFDT8x7Zone.getPass();
        }

        public boolean getBadPixelStreakPass() {
            return this.mItemBadPixelStreak.getPass();
        }

        public Spec<Integer> getSPISpec() {
            return this.mItemSPI.getSpec();
        }

        public Spec<Integer> getTotalPxSpec() {
            return this.mItemTotalPx.getSpec();
        }

        public Spec<Integer> getLocalPxSpec() {
            return this.mItemLocalPx.getSpec();
        }

        public Spec<Integer> getOTPSpec() {
            return this.mItemOTP.getSpec();
        }

        public Spec<Integer> getFDT8x1ZoneSpec() {
            return this.mItemFDT8x1Zone.getSpec();
        }

        public Spec<Integer> getFDT8x7ZoneSpec() {
            return this.mItemFDT8x7Zone.getSpec();
        }

        public Spec<Integer> getBadPixelStreakSpec() {
            return this.mItemBadPixelStreak.getSpec();
        }

        public boolean processData() {
            if (getSPIPass() && getTotalPxPass() && getLocalPxPass() && getOTPPass() && getFDT8x1ZonePass() && getFDT8x7ZonePass() && getBadPixelStreakPass()) {
                this.mStrProcessResult = "PASS";
                return true;
            }
            this.mStrProcessResult = "FAIL";
            return false;
        }

        public void saveNormalDataResult() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mStrProcessResult);
            sb.append(",");
            sb.append(this.mItemOTP.getValue());
            sb.append(",");
            sb.append(this.mItemSPI.getValue());
            sb.append(",");
            sb.append(this.mItemTotalPx.getValue());
            sb.append(",");
            sb.append(this.mItemLocalPx.getValue());
            sb.append(",");
            sb.append(this.mItemFDT8x1Zone.getValue());
            sb.append(",");
            sb.append(this.mItemFDT8x7Zone.getValue());
            sb.append(",");
            sb.append(this.mItemBadPixelStreak.getValue());
            Kernel.writeToPathNsync(new File("/data/log/FingerNormalData.log").getPath(), sb.toString());
        }
    }

    private class FingerPrintSNRData {
        private static final int ITEM_COUNT = 12;
        private SNRItem mItemDataDifference;
        private SNRItem mItemDeltaPixelLocal;
        private SNRItem mItemDeltaPixelTotal;
        private SNRItem mItemNoise;
        private SNRItem mItemPerformance;
        private SNRItem mItemPinOpenShortINT;
        private SNRItem mItemPinOpenShortRST;
        private SNRItem mItemPixelKeyDown;
        private SNRItem mItemRubberPlacement;
        private SNRItem mItemSNR;
        private SNRItem mItemSaturationMaxDown;
        private SNRItem mItemSaturationMaxUp;
        private String mStrProcessResult;
        private int mnReadCnt;

        private class SNRItem {
            private Spec<Float> mFloatSpec;
            private boolean mPass;
            private Spec<Integer> mSpec;
            private String mValue;

            public SNRItem() {
                this.mValue = "0";
                this.mPass = false;
            }

            public SNRItem(String value, boolean pass) {
                this.mValue = value;
                this.mPass = pass;
            }

            public void setValue(String value) {
                this.mValue = value;
            }

            public void setPass(boolean pass) {
                this.mPass = pass;
            }

            public void setSpec(Spec<Integer> spec) {
                this.mSpec = spec;
            }

            public void setFloatSpec(Spec<Float> spec) {
                this.mFloatSpec = spec;
            }

            public String getValue() {
                return this.mValue;
            }

            public boolean getPass() {
                return this.mPass;
            }

            public Spec<Integer> getSpec() {
                return this.mSpec;
            }

            public Spec<Float> getFloatSpec() {
                return this.mFloatSpec;
            }
        }

        private FingerPrintSNRData() {
            this.mnReadCnt = 0;
        }

        public void reset() {
            this.mnReadCnt = 0;
        }

        public boolean isReadDone() {
            boolean isDone = this.mnReadCnt == 12;
            StringBuilder sb = new StringBuilder();
            sb.append("snr : ");
            sb.append(isDone);
            LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "isReadDone", sb.toString());
            return isDone;
        }

        private void addItem(SNRItem item, String _strValue, Spec<Integer> spec) {
            int value = Integer.parseInt(_strValue);
            boolean pass = false;
            if (value >= ((Integer) spec.lower).intValue() && value <= ((Integer) spec.upper).intValue()) {
                pass = true;
            }
            item.setSpec(spec);
            item.setValue(_strValue);
            item.setPass(pass);
        }

        private void addFloatItem(SNRItem item, String _strValue, Spec<Float> spec) {
            float value = Float.parseFloat(_strValue);
            boolean pass = false;
            if (value >= ((Float) spec.lower).floatValue() && value <= ((Float) spec.upper).floatValue()) {
                pass = true;
            }
            item.setFloatSpec(spec);
            item.setValue(_strValue);
            item.setPass(pass);
        }

        public void addDeltaPixelTotal(String _strValue) {
            this.mItemDeltaPixelTotal = new SNRItem();
            addItem(this.mItemDeltaPixelTotal, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MAX))));
            this.mnReadCnt++;
        }

        public void addDeltaPixelLocal(String _strValue) {
            this.mItemDeltaPixelLocal = new SNRItem();
            addItem(this.mItemDeltaPixelLocal, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MAX))));
            this.mnReadCnt++;
        }

        public void addNoise(String _strValue) {
            this.mItemNoise = new SNRItem();
            addFloatItem(this.mItemNoise, _strValue, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_NOISE_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_NOISE_MAX))));
            this.mnReadCnt++;
        }

        public void addPinOpenShortINT(String _strValue) {
            this.mItemPinOpenShortINT = new SNRItem();
            addItem(this.mItemPinOpenShortINT, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_INT)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_INT))));
            this.mnReadCnt++;
        }

        public void addPinOpenShortRST(String _strValue) {
            this.mItemPinOpenShortRST = new SNRItem();
            addItem(this.mItemPinOpenShortRST, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_RST)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_RST))));
            this.mnReadCnt++;
        }

        public void addSNR(String _strValue) {
            this.mItemSNR = new SNRItem();
            addFloatItem(this.mItemSNR, _strValue, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SNR_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SNR_MAX))));
            this.mnReadCnt++;
        }

        public void addPixelKeyDown(String _strValue) {
            this.mItemPixelKeyDown = new SNRItem();
            addItem(this.mItemPixelKeyDown, _strValue, new Spec(Integer.valueOf(0), Integer.valueOf(0)));
            this.mnReadCnt++;
        }

        public void addPerformance(String _strValue) {
            this.mItemPerformance = new SNRItem();
            addItem(this.mItemPerformance, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PERFORMANCE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_PERFORMANCE_MAX))));
            this.mnReadCnt++;
        }

        public void addDataDifference(String _strValue) {
            this.mItemDataDifference = new SNRItem();
            addItem(this.mItemDataDifference, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MAX))));
            this.mnReadCnt++;
        }

        public void addRubberPlacement(String _strValue) {
            this.mItemRubberPlacement = new SNRItem();
            addFloatItem(this.mItemRubberPlacement, _strValue, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MAX))));
            this.mnReadCnt++;
        }

        public void addSaturationMaxUp(String _strValue) {
            this.mItemSaturationMaxUp = new SNRItem();
            addItem(this.mItemSaturationMaxUp, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MAX))));
            this.mnReadCnt++;
        }

        public void addSaturationMaxDown(String _strValue) {
            this.mItemSaturationMaxDown = new SNRItem();
            addItem(this.mItemSaturationMaxDown, _strValue, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MAX))));
            this.mnReadCnt++;
        }

        public String getDeltaPixelTotalValue() {
            return this.mItemDeltaPixelTotal.getValue();
        }

        public String getDeltaPixelLocalValue() {
            return this.mItemDeltaPixelLocal.getValue();
        }

        public String getNoiseValue() {
            return this.mItemNoise.getValue();
        }

        public String getPinOpenShortINTValue() {
            return this.mItemPinOpenShortINT.getValue();
        }

        public String getPinOpenShortRSTValue() {
            return this.mItemPinOpenShortRST.getValue();
        }

        public String getSNRValue() {
            return this.mItemSNR.getValue();
        }

        public String getPixelKeyDownValue() {
            return this.mItemPixelKeyDown.getValue();
        }

        public String getPerformanceValue() {
            return this.mItemPerformance.getValue();
        }

        public String getDataDifferenceValue() {
            return this.mItemDataDifference.getValue();
        }

        public String getRubberPlacementValue() {
            return this.mItemRubberPlacement.getValue();
        }

        public String getSaturationMaxUpValue() {
            return this.mItemSaturationMaxUp.getValue();
        }

        public String getSaturationMaxDownValue() {
            return this.mItemSaturationMaxDown.getValue();
        }

        public boolean getDeltaPixelTotalPass() {
            return this.mItemDeltaPixelTotal.getPass();
        }

        public boolean getDeltaPixelLocalPass() {
            return this.mItemDeltaPixelLocal.getPass();
        }

        public boolean getNoisePass() {
            return this.mItemNoise.getPass();
        }

        public boolean getPinOpenShortINTPass() {
            return this.mItemPinOpenShortINT.getPass();
        }

        public boolean getPinOpenShortRSTPass() {
            return this.mItemPinOpenShortRST.getPass();
        }

        public boolean getSNRPass() {
            return this.mItemSNR.getPass();
        }

        public boolean getPixelKeyDownPass() {
            return this.mItemPixelKeyDown.getPass();
        }

        public boolean getPerformancePass() {
            return this.mItemPerformance.getPass();
        }

        public boolean getDataDifferencePass() {
            return this.mItemDataDifference.getPass();
        }

        public boolean getRubberPlacementPass() {
            return this.mItemRubberPlacement.getPass();
        }

        public boolean getSaturationMaxUpPass() {
            return this.mItemSaturationMaxUp.getPass();
        }

        public boolean getSaturationMaxDownPass() {
            return this.mItemSaturationMaxDown.getPass();
        }

        public Spec<Integer> getDeltaPixelTotalSpec() {
            return this.mItemDeltaPixelTotal.getSpec();
        }

        public Spec<Integer> getDeltaPixelLocalSpec() {
            return this.mItemDeltaPixelLocal.getSpec();
        }

        public Spec<Float> getNoiseSpec() {
            return this.mItemNoise.getFloatSpec();
        }

        public Spec<Integer> getPinOpenShortINTSpec() {
            return this.mItemPinOpenShortINT.getSpec();
        }

        public Spec<Integer> getPinOpenShortRSTSpec() {
            return this.mItemPinOpenShortRST.getSpec();
        }

        public Spec<Float> getSNRSpec() {
            return this.mItemSNR.getFloatSpec();
        }

        public Spec<Integer> getPixelKeyDownSpec() {
            return this.mItemPixelKeyDown.getSpec();
        }

        public Spec<Integer> getPerformanceSpec() {
            return this.mItemPerformance.getSpec();
        }

        public Spec<Integer> getDataDifferenceSpec() {
            return this.mItemDataDifference.getSpec();
        }

        public Spec<Float> getRubberPlacementSpec() {
            return this.mItemRubberPlacement.getFloatSpec();
        }

        public Spec<Integer> getSaturationMaxUpSpec() {
            return this.mItemSaturationMaxUp.getSpec();
        }

        public Spec<Integer> getSaturationMaxDownSpec() {
            return this.mItemSaturationMaxDown.getSpec();
        }

        public boolean processData() {
            if (getDeltaPixelTotalPass() && getDeltaPixelLocalPass() && getNoisePass() && getPinOpenShortINTPass() && getPinOpenShortRSTPass() && getSNRPass() && getPixelKeyDownPass() && getPerformancePass() && getDataDifferencePass() && getRubberPlacementPass() && getSaturationMaxUpPass() && getSaturationMaxDownPass()) {
                this.mStrProcessResult = "PASS";
                return true;
            }
            this.mStrProcessResult = "FAIL";
            return false;
        }

        public void saveSNRDataResult() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mStrProcessResult);
            sb.append(",");
            sb.append(this.mItemDeltaPixelTotal.getValue());
            sb.append(",");
            sb.append(this.mItemDeltaPixelLocal.getValue());
            sb.append(",");
            sb.append(this.mItemNoise.getValue());
            sb.append(",");
            sb.append(this.mItemPinOpenShortINT.getValue());
            sb.append(",");
            sb.append(this.mItemPinOpenShortRST.getValue());
            sb.append(",");
            sb.append(this.mItemSNR.getValue());
            sb.append(",");
            sb.append(this.mItemPixelKeyDown.getValue());
            sb.append(",");
            sb.append(this.mItemPerformance.getValue());
            sb.append(",");
            sb.append(this.mItemDataDifference.getValue());
            sb.append(",");
            sb.append(this.mItemSaturationMaxUp.getValue());
            sb.append(",");
            sb.append(this.mItemSaturationMaxDown.getValue());
            Kernel.writeToPathNsync(new File("/data/log/FingerSNRData.log").getPath(), sb.toString());
        }
    }

    public class FingerPrintSensorInfo {
        private String mStrProductId;
        private String mStrUid;
        private int mnCalibration;
        private int mnDac;
        private int mnIsModulePass;
        private int mnTCode;

        public FingerPrintSensorInfo() {
        }

        public void setModulePass(int _nIsModulePass) {
            this.mnIsModulePass = _nIsModulePass;
        }

        public void setTCode(int _nTCode) {
            this.mnTCode = _nTCode;
        }

        public void setDac(int _nDac) {
            this.mnDac = _nDac;
        }

        public void setProductId(String _strProductId) {
            this.mStrProductId = _strProductId;
        }

        public void setUid(String _strUid) {
            this.mStrUid = _strUid;
        }

        public void setCalibration(int _nCalibration) {
            this.mnCalibration = _nCalibration;
        }

        public String getIsModulePass() {
            if (this.mnIsModulePass == 0) {
                return "PASS";
            }
            return "FAIL";
        }

        public int getTCode() {
            return this.mnTCode;
        }

        public int getDac() {
            return this.mnDac;
        }

        public String getProductId() {
            return this.mStrProductId;
        }

        public String getUid() {
            return this.mStrUid;
        }

        public String getCalibration() {
            if (this.mnCalibration == 0) {
                return "PASS";
            }
            return "FAIL";
        }
    }

    private static class Spec<T> {
        T lower = null;
        T upper = null;

        public Spec(T l, T u) {
            this.lower = l;
            this.upper = u;
        }
    }

    public void showWarningPopup() {
        LtUtil.log_d(CLASS_NAME, "showWarningPopup", "show popup");
        Point screenRect = new Point();
        try {
            getWindowManager().getDefaultDisplay().getSize(screenRect);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        View layout = getLayoutInflater().inflate(C0268R.layout.fingerprint_warning_diag, (ViewGroup) findViewById(C0268R.C0269id.llToast));
        layout.setMinimumWidth((screenRect.x * 4) / 5);
        TextView text2 = (TextView) layout.findViewById(C0268R.C0269id.warning_text2);
        TextView text3 = (TextView) layout.findViewById(C0268R.C0269id.warning_text3);
        ((TextView) layout.findViewById(C0268R.C0269id.warning_text1)).setText("You have replaced fingerprint sensor.");
        text2.setText("- Delete the previous fingerprint information using backup password.");
        text3.setText("- Do Factory reset under customer's agreement if you cannot delete.");
        FingerPrintTest_warningDiag diag = new FingerPrintTest_warningDiag(this, layout);
        diag.setClassName(CLASS_NAME);
        diag.show();
    }

    public FingerPrintTest_cap() {
        super(CLASS_NAME);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0268R.layout.fingerprint_test_comb);
        initLayout();
        this.mTestName = getIntent().getStringExtra("Action");
        LtUtil.log_d(CLASS_NAME, "onCreate", this.mTestName);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mGFTestManager = new GFTestManager(this);
        this.mGFTestManager.registerResultNotify(this.mTestResultNotify);
        if ("NORMALDATA".equals(this.mTestName)) {
            this.mTvMsg2.setText("Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test");
            LtUtil.log_d(CLASS_NAME, "onResume", "startOTPCheckTest");
            this.mGFTestManager.startUnTouchTest();
        } else if ("SNR".equals(this.mTestName)) {
            LtUtil.log_d(CLASS_NAME, "onResume", "MSG_PREPARE_TOUCH");
            this.mHandler.sendEmptyMessageDelayed(10, 1000);
        } else if ("SENSORINFO".equals(this.mTestName)) {
            this.mTvMsg2.setText("Sensor Infomation");
            LtUtil.log_d(CLASS_NAME, "onResume", "startGetSensorInfo");
            this.mGFTestManager.startGetSensorInfo();
            StringBuilder sb = new StringBuilder();
            sb.append("getSensorInfo : ");
            sb.append(this.m_ResultMessage);
            LtUtil.log_d(CLASS_NAME, "onResume", sb.toString());
            this.mTvResult.setVisibility(0);
            this.mTvResult.setText(this.m_ResultMessage);
        } else {
            LtUtil.log_d(CLASS_NAME, "onResume", "sensorStatus has been failed ");
            this.mTvResult.setText("Fingerprint initializing Fail");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3 && this.mBtnGoTerm.isShown()) {
            LtUtil.log_d(CLASS_NAME, "onKeyDown()", "Homekey pressed");
            this.mTvMsg1.setVisibility(8);
            this.mTvMsg2.setVisibility(0);
            this.mBtnGoTerm.setVisibility(8);
            LtUtil.log_d(CLASS_NAME, "onKeyDown", "startTouchTest");
            this.mGFTestManager.startTouchTest();
            this.mTvMsg2.setText("SIGNAL LOSS data scanning...");
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void updateNormalScanResult() {
        LtUtil.log_d(CLASS_NAME, "updateNormalScanResult", "");
        Display display = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        display.getRealSize(outpoint);
        int nScreenWidth = outpoint.x;
        int nScreenHeight = outpoint.y;
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(nScreenWidth, nScreenHeight / 4);
        this.mResultView.setRowCol(8, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(1, 0, "OTP Check");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintNormalData.getOTPSpec().lower);
        sb.append("");
        fingerPrintResultView_touch.setTextData(1, 2, sb.toString());
        this.mResultView.setTextData(1, 3, this.mFingerPrintNormalData.getOTPValue());
        this.mResultView.setTextData(1, 4, this.mFingerPrintNormalData.getOTPPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(2, 0, "Pin Open Short");
        this.mResultView.setTextData(2, 1, "SPI");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintNormalData.getSPISpec().lower);
        sb2.append("");
        fingerPrintResultView_touch2.setTextData(2, 2, sb2.toString());
        this.mResultView.setTextData(2, 3, this.mFingerPrintNormalData.getSPIValue());
        this.mResultView.setTextData(2, 4, this.mFingerPrintNormalData.getSPIPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(3, 0, "Pixel Open Short");
        this.mResultView.setTextData(3, 1, "Total");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintNormalData.getTotalPxSpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintNormalData.getTotalPxSpec().upper);
        fingerPrintResultView_touch3.setTextData(3, 2, sb3.toString());
        this.mResultView.setTextData(3, 3, this.mFingerPrintNormalData.getTotalPxValue());
        this.mResultView.setTextData(3, 4, this.mFingerPrintNormalData.getTotalPxPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(4, 1, "Local");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintNormalData.getLocalPxSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintNormalData.getLocalPxSpec().upper);
        fingerPrintResultView_touch4.setTextData(4, 2, sb4.toString());
        this.mResultView.setTextData(4, 3, this.mFingerPrintNormalData.getLocalPxValue());
        this.mResultView.setTextData(4, 4, this.mFingerPrintNormalData.getLocalPxPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(5, 0, "FDT Pixel");
        this.mResultView.setTextData(5, 1, "8*1 FDT Zone");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintNormalData.getFDT8x1ZoneSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintNormalData.getFDT8x1ZoneSpec().upper);
        fingerPrintResultView_touch5.setTextData(5, 2, sb5.toString());
        this.mResultView.setTextData(5, 3, this.mFingerPrintNormalData.getFDT8x1ZoneValue());
        this.mResultView.setTextData(5, 4, this.mFingerPrintNormalData.getFDT8x1ZonePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(6, 1, "8*7 FDT Zone");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(this.mFingerPrintNormalData.getFDT8x7ZoneSpec().lower);
        sb6.append("~");
        sb6.append(this.mFingerPrintNormalData.getFDT8x7ZoneSpec().upper);
        fingerPrintResultView_touch6.setTextData(6, 2, sb6.toString());
        this.mResultView.setTextData(6, 3, this.mFingerPrintNormalData.getFDT8x7ZoneValue());
        this.mResultView.setTextData(6, 4, this.mFingerPrintNormalData.getFDT8x7ZonePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(7, 0, "BadPixelStreak");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintNormalData.getBadPixelStreakSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintNormalData.getBadPixelStreakSpec().upper);
        fingerPrintResultView_touch7.setTextData(7, 2, sb7.toString());
        this.mResultView.setTextData(7, 3, this.mFingerPrintNormalData.getBadPixelStreakValue());
        this.mResultView.setTextData(7, 4, this.mFingerPrintNormalData.getBadPixelStreakPass() ? "PASS" : "FAIL");
    }

    /* access modifiers changed from: private */
    public void updateSNRResult() {
        LtUtil.log_d(CLASS_NAME, "updateSNRResult", "");
        Display display = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        display.getRealSize(outpoint);
        int nScreenWidth = outpoint.x;
        int nScreenHeight = outpoint.y;
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(nScreenWidth, nScreenHeight / 3);
        this.mResultView.setRowCol(15, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        int row_next = 0 + 1;
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(row_next, 0, "Delta Pixel");
        this.mResultView.setTextData(row_next, 1, "Total");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintSNRData.getDeltaPixelTotalSpec().lower);
        sb.append("~");
        sb.append(this.mFingerPrintSNRData.getDeltaPixelTotalSpec().upper);
        fingerPrintResultView_touch.setTextData(row_next, 2, sb.toString());
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintSNRData.getDeltaPixelTotalValue());
        sb2.append("");
        fingerPrintResultView_touch2.setTextData(row_next, 3, sb2.toString());
        int row_next2 = row_next + 1;
        this.mResultView.setTextData(row_next, 4, this.mFingerPrintSNRData.getDeltaPixelTotalPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next2, 1, "Local");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintSNRData.getDeltaPixelLocalSpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintSNRData.getDeltaPixelLocalSpec().upper);
        fingerPrintResultView_touch3.setTextData(row_next2, 2, sb3.toString());
        this.mResultView.setTextData(row_next2, 3, this.mFingerPrintSNRData.getDeltaPixelLocalValue());
        int row_next3 = row_next2 + 1;
        this.mResultView.setTextData(row_next2, 4, this.mFingerPrintSNRData.getDeltaPixelLocalPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next3, 0, "Noise");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().upper);
        fingerPrintResultView_touch4.setTextData(row_next3, 2, sb4.toString());
        this.mResultView.setTextData(row_next3, 3, this.mFingerPrintSNRData.getNoiseValue());
        int row_next4 = row_next3 + 1;
        this.mResultView.setTextData(row_next3, 4, this.mFingerPrintSNRData.getNoisePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next4, 0, "Pin Open Short");
        this.mResultView.setTextData(row_next4, 1, "INT");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintSNRData.getPinOpenShortINTSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintSNRData.getPinOpenShortINTSpec().upper);
        fingerPrintResultView_touch5.setTextData(row_next4, 2, sb5.toString());
        this.mResultView.setTextData(row_next4, 3, this.mFingerPrintSNRData.getPinOpenShortINTValue());
        int row_next5 = row_next4 + 1;
        this.mResultView.setTextData(row_next4, 4, this.mFingerPrintSNRData.getPinOpenShortINTPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next5, 1, "RST");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(this.mFingerPrintSNRData.getPinOpenShortRSTSpec().lower);
        sb6.append("~");
        sb6.append(this.mFingerPrintSNRData.getPinOpenShortRSTSpec().upper);
        fingerPrintResultView_touch6.setTextData(row_next5, 2, sb6.toString());
        this.mResultView.setTextData(row_next5, 3, this.mFingerPrintSNRData.getPinOpenShortRSTValue());
        int row_next6 = row_next5 + 1;
        this.mResultView.setTextData(row_next5, 4, this.mFingerPrintSNRData.getPinOpenShortRSTPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next6, 0, "SNR");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintSNRData.getSNRSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintSNRData.getSNRSpec().upper);
        fingerPrintResultView_touch7.setTextData(row_next6, 2, sb7.toString());
        this.mResultView.setTextData(row_next6, 3, this.mFingerPrintSNRData.getSNRValue());
        int row_next7 = row_next6 + 1;
        this.mResultView.setTextData(row_next6, 4, this.mFingerPrintSNRData.getSNRPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next7, 0, "Pixel Key");
        this.mResultView.setTextData(row_next7, 1, "Pixel Down");
        FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mFingerPrintSNRData.getPixelKeyDownSpec().lower);
        sb8.append("~");
        sb8.append(this.mFingerPrintSNRData.getPixelKeyDownSpec().upper);
        fingerPrintResultView_touch8.setTextData(row_next7, 2, sb8.toString());
        this.mResultView.setTextData(row_next7, 3, this.mFingerPrintSNRData.getPixelKeyDownValue());
        int row_next8 = row_next7 + 1;
        this.mResultView.setTextData(row_next7, 4, this.mFingerPrintSNRData.getPixelKeyDownPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next8, 0, "Performance");
        FingerPrintResultView_touch fingerPrintResultView_touch9 = this.mResultView;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(this.mFingerPrintSNRData.getPerformanceSpec().lower);
        sb9.append("~");
        sb9.append(this.mFingerPrintSNRData.getPerformanceSpec().upper);
        fingerPrintResultView_touch9.setTextData(row_next8, 2, sb9.toString());
        this.mResultView.setTextData(row_next8, 3, this.mFingerPrintSNRData.getPerformanceValue());
        int row_next9 = row_next8 + 1;
        this.mResultView.setTextData(row_next8, 4, this.mFingerPrintSNRData.getPerformancePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next9, 0, "Data Difference");
        FingerPrintResultView_touch fingerPrintResultView_touch10 = this.mResultView;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(this.mFingerPrintSNRData.getDataDifferenceSpec().lower);
        sb10.append("~");
        sb10.append(this.mFingerPrintSNRData.getDataDifferenceSpec().upper);
        fingerPrintResultView_touch10.setTextData(row_next9, 2, sb10.toString());
        this.mResultView.setTextData(row_next9, 3, this.mFingerPrintSNRData.getDataDifferenceValue());
        int row_next10 = row_next9 + 1;
        this.mResultView.setTextData(row_next9, 4, this.mFingerPrintSNRData.getDataDifferencePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next10, 0, "Rubber Placement");
        FingerPrintResultView_touch fingerPrintResultView_touch11 = this.mResultView;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(this.mFingerPrintSNRData.getRubberPlacementSpec().lower);
        sb11.append("~");
        sb11.append(this.mFingerPrintSNRData.getRubberPlacementSpec().upper);
        fingerPrintResultView_touch11.setTextData(row_next10, 2, sb11.toString());
        this.mResultView.setTextData(row_next10, 3, this.mFingerPrintSNRData.getRubberPlacementValue());
        int row_next11 = row_next10 + 1;
        this.mResultView.setTextData(row_next10, 4, this.mFingerPrintSNRData.getRubberPlacementPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next11, 0, "Saturation");
        this.mResultView.setTextData(row_next11, 1, "Max Up");
        FingerPrintResultView_touch fingerPrintResultView_touch12 = this.mResultView;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(this.mFingerPrintSNRData.getSaturationMaxUpSpec().lower);
        sb12.append("~");
        sb12.append(this.mFingerPrintSNRData.getSaturationMaxUpSpec().upper);
        fingerPrintResultView_touch12.setTextData(row_next11, 2, sb12.toString());
        this.mResultView.setTextData(row_next11, 3, this.mFingerPrintSNRData.getSaturationMaxUpValue());
        int row_next12 = row_next11 + 1;
        this.mResultView.setTextData(row_next11, 4, this.mFingerPrintSNRData.getSaturationMaxUpPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next12, 1, "Min Down");
        FingerPrintResultView_touch fingerPrintResultView_touch13 = this.mResultView;
        StringBuilder sb13 = new StringBuilder();
        sb13.append(this.mFingerPrintSNRData.getSaturationMaxDownSpec().lower);
        sb13.append("~");
        sb13.append(this.mFingerPrintSNRData.getSaturationMaxDownSpec().upper);
        fingerPrintResultView_touch13.setTextData(row_next12, 2, sb13.toString());
        this.mResultView.setTextData(row_next12, 3, this.mFingerPrintSNRData.getSaturationMaxDownValue());
        int i = row_next12 + 1;
        this.mResultView.setTextData(row_next12, 4, this.mFingerPrintSNRData.getSaturationMaxDownPass() ? "PASS" : "FAIL");
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (event.getActionMasked() == 5) {
            int index = (action & 65280) >> 8;
            StringBuilder sb = new StringBuilder();
            sb.append("ACTION_POINTER_DOWN index : ");
            sb.append(index);
            LtUtil.log_d(CLASS_NAME, "onTouchEvent", sb.toString());
            float x = event.getX(index);
            float y = event.getY(index);
            if (x > this.mBtnGoTerm.getX() && x < this.mBtnGoTerm.getX() + ((float) this.mBtnGoTerm.getWidth()) && y > this.mBtnGoTerm.getY() && y < this.mBtnGoTerm.getY() + ((float) this.mBtnGoTerm.getHeight())) {
                LtUtil.log_d(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_DOWN pos is inside OK button ");
                this.mBtnGoTerm.setPressed(true);
            }
        } else if (event.getActionMasked() == 6) {
            int index2 = (action & 65280) >> 8;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ACTION_POINTER_UP index : ");
            sb2.append(index2);
            LtUtil.log_d(CLASS_NAME, "onTouchEvent", sb2.toString());
            float x2 = event.getX(index2);
            float y2 = event.getY(index2);
            if (x2 > this.mBtnGoTerm.getX() && x2 < this.mBtnGoTerm.getX() + ((float) this.mBtnGoTerm.getWidth()) && y2 > this.mBtnGoTerm.getY() && y2 < this.mBtnGoTerm.getY() + ((float) this.mBtnGoTerm.getHeight())) {
                LtUtil.log_d(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_UP pos is inside OK button ");
                this.mBtnGoTerm.setPressed(false);
                this.mBtnGoTerm.callOnClick();
            } else if (this.mBtnGoTerm.isPressed()) {
                this.mBtnGoTerm.setPressed(false);
            }
        }
        return super.onTouchEvent(event);
    }

    private void initLayout() {
        Display mDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        int nScreenWidth = outpoint.x;
        int nScreenHeight = outpoint.y;
        this.mFingerPrintNormalData = new FingerPrintNormalData();
        this.mFingerPrintSNRData = new FingerPrintSNRData();
        this.mFingerPrintSensorInfo = new FingerPrintSensorInfo();
        this.mResultView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_resultview);
        this.mSNRZoneView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_snrzoneview);
        this.mIvSnrImage = (ImageView) findViewById(C0268R.C0269id.fingerprint_snrimage);
        this.mResultView.setVisibility(8);
        this.mSNRZoneView.setVisibility(8);
        this.mBtnGoTerm = new Button(this);
        this.mBtnGoTerm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FingerPrintTest_cap.this.mTvMsg1.setVisibility(8);
                FingerPrintTest_cap.this.mTvMsg2.setVisibility(0);
                FingerPrintTest_cap.this.mBtnGoTerm.setVisibility(8);
                LtUtil.log_d(FingerPrintTest_cap.CLASS_NAME, "onClick", "startTouchTest");
                FingerPrintTest_cap.this.mGFTestManager.startTouchTest();
            }
        });
        LayoutParams params = this.mIvSnrImage.getLayoutParams();
        params.height = nScreenHeight / 4;
        params.width = nScreenWidth / 2;
        this.mIvSnrImage.setLayoutParams(params);
        RelativeLayout lContainerLayout = new RelativeLayout(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        RelativeLayout.LayoutParams lButtonParams = new RelativeLayout.LayoutParams(nScreenWidth / 3, nScreenHeight / 8);
        lButtonParams.addRule(12);
        lButtonParams.addRule(14);
        lButtonParams.bottomMargin = nScreenHeight / 3;
        this.mBtnGoTerm.setTextSize(25.0f);
        this.mBtnGoTerm.setLayoutParams(lButtonParams);
        this.mBtnGoTerm.setVisibility(8);
        this.mBtnGoTerm.setGravity(17);
        lContainerLayout.addView(this.mBtnGoTerm);
        addContentView(lContainerLayout, new RelativeLayout.LayoutParams(-1, -1));
        this.mTvMsg1 = (TextView) findViewById(C0268R.C0269id.fingerprint_message1);
        this.mTvMsg2 = (TextView) findViewById(C0268R.C0269id.fingerprint_message2);
        this.mTvResult = (TextView) findViewById(C0268R.C0269id.result_text);
        this.mTvMsg2.setText("Ready");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_d(CLASS_NAME, "onPause", "");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_d(CLASS_NAME, "onDestroy", "cancelTest");
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(10);
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(6);
        this.mHandler.removeMessages(14);
        this.mGFTestManager.cancelTest();
        super.onDestroy();
    }
}
