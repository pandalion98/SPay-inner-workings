package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
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
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import egistec.csa.client.api.Fingerprint.FingerprintEventListener;
import egistec.optical.csa.client.api.Fingerprint;
import java.util.Locale;
import java.util.concurrent.Semaphore;

public class FingerPrintTest_egis extends BaseActivity {
    private static final String CLASS_NAME = "FingerPrintTest_egis";
    public static final int SENSOR_TEST_NORMALSCAN_COMMAND = 100103;
    public static final int SENSOR_TEST_SNR_FINAL_COMMAND_EGIS = 100107;
    public static final int SENSOR_TEST_SNR_ORG_COMMAND_EGIS = 100106;
    private static String mFingerPrintName;
    private static String mFingerPrint_Vendor;
    private static boolean mIsFingerprintTouchSide = false;
    /* access modifiers changed from: private */
    public static boolean mIsOptical = false;
    private static String scriptID;
    /* access modifiers changed from: private */
    public static boolean supportDefectBadPixel = false;
    /* access modifiers changed from: private */
    public static boolean supportNewItems_Egistec = false;
    /* access modifiers changed from: private */
    public int IDENTIFY_FAILURE_SENSOR_CHANGED = 59;
    private int SENSOR_OK = 2004;
    private final int STATE_NORMAL_SCAN = 1014;
    /* access modifiers changed from: private */
    public boolean STATE_REPLACEMENT_WARNING = false;
    private final int STATE_SNR = 1016;
    private final int STATE_SNR_SCAN = 1018;
    private final String USER_ID = "Test";
    Handler handler = new Handler();
    /* access modifiers changed from: private */
    public ImageView iv_snrimage;
    /* access modifiers changed from: private */
    public Runnable mClosePutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_egis.this.tv_msg1.setVisibility(8);
            FingerPrintTest_egis.this.tv_msg2.setVisibility(0);
            FingerPrintTest_egis.this.m_gotermbutton.setVisibility(8);
        }
    };
    private FingerprintEventListener mFingerPrintEventListener;
    private Fingerprint.FingerprintEventListener mFingerPrintEventListenerOptical;
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    /* access modifiers changed from: private */
    public FingerPrintSNRData mFingerPrintSNRData;
    private egistec.csa.client.api.Fingerprint mFingerprint;
    private Fingerprint mFingerprintOptical;
    private Semaphore mLock = new Semaphore(1);
    private FingerPrintResultView_touch mResultView;
    /* access modifiers changed from: private */
    public FingerPrintResultView_touch mSNRZoneView;
    /* access modifiers changed from: private */
    public Runnable mShowPutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_egis.this.tv_msg1.setText(FingerPrintTest_egis.this.m_PopupMessage);
            FingerPrintTest_egis.this.tv_msg1.setVisibility(0);
            FingerPrintTest_egis.this.tv_msg2.setVisibility(8);
            FingerPrintTest_egis.this.m_gotermbutton.setText("O K");
            FingerPrintTest_egis.this.m_gotermbutton.setVisibility(0);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mShowWarningPopupRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_egis.this.showWarningPopup();
        }
    };
    private String mTest;
    private Thread mThread;
    private IBinder mToken;
    /* access modifiers changed from: private */
    public Runnable mUpdateImageRunnable = new Runnable() {
        public void run() {
            Bitmap bitmap = BitmapFactory.decodeFile("/data/egis_after_rubber.bmp");
            FingerPrintTest_egis.this.mSNRZoneView.setVisibility(4);
            FingerPrintTest_egis.this.iv_snrimage.setVisibility(0);
            FingerPrintTest_egis.this.iv_snrimage.setImageBitmap(bitmap);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateMessageRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_egis.this.tv_msg2.setText(FingerPrintTest_egis.this.m_Message);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateNormalDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_egis.this.mFingerPrintNormalData.isReadDone()) {
                FingerPrintTest_egis.this.updateNormalScanResult_egis();
                return;
            }
            FingerPrintTest_egis.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_egis.this.tv_result.setVisibility(0);
            FingerPrintTest_egis.this.tv_result.setText(FingerPrintTest_egis.this.m_ResultMessage);
        }
    };
    private Runnable mUpdateResultMessageRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_egis.this.m_ResultMessage.isEmpty()) {
                FingerPrintTest_egis.this.tv_result.setVisibility(0);
                FingerPrintTest_egis.this.tv_result.setText(FingerPrintTest_egis.this.m_ResultMessage);
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateSNRDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_egis.this.mFingerPrintSNRData.isReadDone()) {
                FingerPrintTest_egis.this.updateSNRResult_egis();
                return;
            }
            FingerPrintTest_egis.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_egis.this.tv_result.setVisibility(0);
            FingerPrintTest_egis.this.tv_result.setText(FingerPrintTest_egis.this.m_ResultMessage);
        }
    };
    /* access modifiers changed from: private */
    public int m_CurrentOperation = 0;
    /* access modifiers changed from: private */
    public String m_Message = "Ready";
    /* access modifiers changed from: private */
    public String m_PopupMessage = "";
    private int m_PreviousOperation = 0;
    /* access modifiers changed from: private */
    public String m_ResultMessage = "";
    private int m_ScreenHeight = 0;
    private int m_ScreenWidth = 0;
    /* access modifiers changed from: private */
    public Button m_gotermbutton;
    private int m_result = 0;
    private String os_version;
    private String[] os_version_array;
    /* access modifiers changed from: private */
    public TextView tv_msg1;
    /* access modifiers changed from: private */
    public TextView tv_msg2;
    /* access modifiers changed from: private */
    public TextView tv_result;

    private class FingerPrintEventListener implements FingerprintEventListener {
        private FingerPrintEventListener() {
        }

        public void onFingerprintEvent(int eventId, Object evt) {
            switch (eventId) {
                case 285212675:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                    FingerPrintTest_egis.this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
                    FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                    return;
                case 285212676:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                    if (FingerPrintTest_egis.this.m_CurrentOperation == 1014) {
                        FingerPrintTest_egis.this.m_Message = "Normal data scan finished";
                        FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                        FingerPrintTest_egis.this.readNormalScanLogFile_egis();
                        FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateNormalDataGraphRunnable);
                        if (FingerPrintTest_egis.this.STATE_REPLACEMENT_WARNING) {
                            FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mShowWarningPopupRunnable);
                            return;
                        }
                        return;
                    } else if (FingerPrintTest_egis.this.m_CurrentOperation == 1018) {
                        FingerPrintTest_egis.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                FingerPrintTest_egis.this.m_Message = "SNR Test data scan finished";
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                                FingerPrintTest_egis.this.readSNRLogFile_egis();
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateSNRDataGraphRunnable);
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateImageRunnable);
                            }
                        }, 100);
                        return;
                    } else {
                        return;
                    }
                case 285212677:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST");
                    FingerPrintTest_egis.this.m_PopupMessage = "Put stimulus on the sensor\nand press OK or HomeButton";
                    FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mShowPutFingerDialogRunnable);
                    return;
                default:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : UNKNOWN_EVT_MESSAGE");
                    return;
            }
        }
    }

    private class FingerPrintEventListenerOptical implements Fingerprint.FingerprintEventListener {
        private FingerPrintEventListenerOptical() {
        }

        public void onFingerprintEvent(int eventId, Object evt) {
            switch (eventId) {
                case 285212675:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                    FingerPrintTest_egis.this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
                    FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                    return;
                case 285212676:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                    if (FingerPrintTest_egis.this.m_CurrentOperation == 1014) {
                        FingerPrintTest_egis.this.m_Message = "Normal data scan finished";
                        FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                        FingerPrintTest_egis.this.readNormalScanLogFile_egis();
                        FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateNormalDataGraphRunnable);
                        if (FingerPrintTest_egis.this.STATE_REPLACEMENT_WARNING) {
                            FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mShowWarningPopupRunnable);
                            return;
                        }
                        return;
                    } else if (FingerPrintTest_egis.this.m_CurrentOperation == 1018) {
                        FingerPrintTest_egis.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                FingerPrintTest_egis.this.m_Message = "SNR Test data scan finished";
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateMessageRunnable);
                                FingerPrintTest_egis.this.readSNRLogFile_egis();
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateSNRDataGraphRunnable);
                                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mUpdateImageRunnable);
                            }
                        }, 100);
                        return;
                    } else {
                        return;
                    }
                default:
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onFingerprintEvent", "Event is : UNKNOWN_EVT_MESSAGE");
                    return;
            }
        }
    }

    private static class FingerPrintNormalData {
        private int ITEM_COUNT_EGIS = 10;
        private int isDone = 0;
        private NormalItem mItemAFE;
        private NormalItem mItemAllowedCounts;
        private NormalItem mItemBadBadPixel;
        private NormalItem mItemBadPixel;
        private NormalItem mItemBlackBadPixel;
        private NormalItem mItemBlackDot;
        private NormalItem mItemCalResult;
        private NormalItem mItemDefectBadPixel;
        private NormalItem mItemDefectPixel;
        private NormalItem mItemEEPROM;
        private NormalItem mItemFlashTest;
        private NormalItem mItemFlooredCol;
        private NormalItem mItemFlooredPixel;
        private NormalItem mItemFlooredRow;
        private NormalItem mItemHistoryCheck;
        private NormalItem mItemImageDeviation;
        private NormalItem mItemMTPTest;
        private NormalItem mItemMaxNegDelta;
        private NormalItem mItemMaxPosDelta;
        private NormalItem mItemMaximumDelta;
        private NormalItem mItemMinimumDelta;
        private NormalItem mItemOtpTest;
        private NormalItem mItemPeggedCol;
        private NormalItem mItemPeggedPixel;
        private NormalItem mItemPeggedRow;
        private NormalItem mItemRAM;
        private NormalItem mItemRegisterTest;
        private NormalItem mItemReset;
        private NormalItem mItemTestPatternTest;
        private NormalItem mItemTotal2DDeltaFail;
        private NormalItem mItemVariance;
        private NormalItem mItemWhiteBadPixel;
        private NormalItem mItemWhiteDot;
        private String mSpecResultFile;

        private static class NormalItem {
            private boolean mPass;
            private Spec<Integer> mSpec;
            private int[] mSpecArray;
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

            public void setSpecArray(int[] spec) {
                this.mSpecArray = spec;
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

            public int[] getSpecArray() {
                return this.mSpecArray;
            }
        }

        public boolean isReadDone() {
            if (FingerPrintTest_egis.mIsOptical) {
                this.ITEM_COUNT_EGIS = 6;
            } else if (FingerPrintTest_egis.supportNewItems_Egistec) {
                this.ITEM_COUNT_EGIS = 6;
            } else if (FingerPrintTest_egis.supportDefectBadPixel) {
                this.ITEM_COUNT_EGIS = 7;
            }
            return this.isDone == this.ITEM_COUNT_EGIS;
        }

        public void addBlackDot(int value) {
            this.mItemBlackDot = new NormalItem();
            addItem(this.mItemBlackDot, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BLACK_DOT_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BLACK_DOT_MAX))));
            this.isDone++;
        }

        public void addWhiteDot(int value) {
            this.mItemWhiteDot = new NormalItem();
            addItem(this.mItemWhiteDot, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_WHITE_DOT_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_WHITE_DOT_MAX))));
            this.isDone++;
        }

        public void addDefectPixel(int value) {
            this.mItemDefectPixel = new NormalItem();
            addItem(this.mItemDefectPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DEFECT_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DEFECT_PIXEL_MAX))));
            this.isDone++;
        }

        public void addBadPixel(int value) {
            this.mItemBadPixel = new NormalItem();
            addItem(this.mItemBadPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_MAX))));
            this.isDone++;
        }

        public void addBlackBadPixel(int value) {
            this.mItemBlackBadPixel = new NormalItem();
            addItem(this.mItemBlackBadPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MAX))));
            this.isDone++;
        }

        public void addWhiteBadPixel(int value) {
            this.mItemWhiteBadPixel = new NormalItem();
            addItem(this.mItemWhiteBadPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MAX))));
            this.isDone++;
        }

        public void addDefectBadPixel(int value) {
            this.mItemDefectBadPixel = new NormalItem();
            addItem(this.mItemDefectBadPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MAX))));
            this.isDone++;
        }

        public void addBadBadPixel(int value) {
            this.mItemBadBadPixel = new NormalItem();
            addItem(this.mItemBadBadPixel, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MAX))));
            this.isDone++;
        }

        public void addResetTest(int value) {
            this.mItemReset = new NormalItem();
            addItem(this.mItemReset, value, new int[]{0});
            this.isDone++;
        }

        public void addEEPROMTest(int value) {
            this.mItemEEPROM = new NormalItem();
            addItem(this.mItemEEPROM, value, new int[]{0});
            this.isDone++;
        }

        public void addCalResult(int value) {
            this.mItemCalResult = new NormalItem();
            addItem(this.mItemCalResult, value, new int[]{0});
            this.isDone++;
        }

        public void addMTPTest(int value) {
            this.mItemMTPTest = new NormalItem();
            addItem(this.mItemMTPTest, value, new int[]{0});
            this.isDone++;
        }

        public void addHistoryCheck(int value) {
            this.mItemHistoryCheck = new NormalItem();
            addItem(this.mItemHistoryCheck, value, new int[]{0});
            this.isDone++;
        }

        public void addRegisterTest(int value) {
            this.mItemRegisterTest = new NormalItem();
            addItem(this.mItemRegisterTest, value, new int[]{0});
            this.isDone++;
        }

        public void addTestPatternTest(int value) {
            this.mItemTestPatternTest = new NormalItem();
            addItem(this.mItemTestPatternTest, value, new int[]{0});
            this.isDone++;
        }

        public void addTestOtpTest(int value) {
            this.mItemOtpTest = new NormalItem();
            addItem(this.mItemOtpTest, value, new int[]{0});
            this.isDone++;
        }

        public void addTestFlashTest(int value) {
            this.mItemFlashTest = new NormalItem();
            addItem(this.mItemFlashTest, value, new int[]{0});
            this.isDone++;
        }

        public void addImageDeviation(int value) {
            this.mItemImageDeviation = new NormalItem();
            addItem(this.mItemImageDeviation, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_IMAGE_DEVIATION_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_IMAGE_DEVIATION_MAX))));
            this.isDone++;
        }

        private void addItem(NormalItem item, int value, Spec<Integer> spec) {
            String strValue = String.valueOf(value);
            boolean pass = false;
            if (value >= ((Integer) spec.lower).intValue() && value <= ((Integer) spec.upper).intValue()) {
                pass = true;
            }
            item.setSpec(spec);
            item.setValue(strValue);
            item.setPass(pass);
        }

        private void addItem(NormalItem item, int value, int[] spec) {
            boolean pass = false;
            int i = 0;
            while (true) {
                if (i >= spec.length) {
                    break;
                } else if (value == spec[i]) {
                    pass = true;
                    break;
                } else {
                    i++;
                }
            }
            item.setSpecArray(spec);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(value);
            item.setValue(sb.toString());
            item.setPass(pass);
        }

        public String getBlackDotValue() {
            return this.mItemBlackDot.getValue();
        }

        public String getWhiteDotValue() {
            return this.mItemWhiteDot.getValue();
        }

        public String getDefectPixelValue() {
            return this.mItemDefectPixel.getValue();
        }

        public String getBadPixelValue() {
            return this.mItemBadPixel.getValue();
        }

        public String getBlackBadPixelValue() {
            return this.mItemBlackBadPixel.getValue();
        }

        public String getWhiteBadPixelValue() {
            return this.mItemWhiteBadPixel.getValue();
        }

        public String getDefectBadPixelValue() {
            return this.mItemDefectBadPixel.getValue();
        }

        public String getTotalBadPixelValue() {
            return this.mItemBadBadPixel.getValue();
        }

        public String getResetValue() {
            return this.mItemReset.getValue();
        }

        public String getEEPROMValue() {
            return this.mItemEEPROM.getValue();
        }

        public String getCalResultValue() {
            return this.mItemCalResult.getValue();
        }

        public String getImageDeviationValue() {
            return this.mItemImageDeviation.getValue();
        }

        public String getMTPValue() {
            return this.mItemMTPTest.getValue();
        }

        public String getHistoryCheckValue() {
            return this.mItemHistoryCheck.getValue();
        }

        public String getRegisterTestValue() {
            return this.mItemRegisterTest.getValue();
        }

        public String getTestPatternTestValue() {
            return this.mItemTestPatternTest.getValue();
        }

        public String getOtpTestValue() {
            return this.mItemOtpTest.getValue();
        }

        public String getFlashTestValue() {
            return this.mItemFlashTest.getValue();
        }

        public boolean getBlackDotPass() {
            return this.mItemBlackDot.getPass();
        }

        public boolean getWhiteDotPass() {
            return this.mItemWhiteDot.getPass();
        }

        public boolean getDefectPixelPass() {
            return this.mItemDefectPixel.getPass();
        }

        public boolean getBadPixelPass() {
            return this.mItemBadPixel.getPass();
        }

        public boolean getResetPass() {
            return this.mItemReset.getPass();
        }

        public boolean getEEPROMPass() {
            return this.mItemEEPROM.getPass();
        }

        public boolean getCalResultPass() {
            return this.mItemCalResult.getPass();
        }

        public boolean getBlackBadPixelPass() {
            return this.mItemBlackBadPixel.getPass();
        }

        public boolean getWhiteBadPixelPass() {
            return this.mItemWhiteBadPixel.getPass();
        }

        public boolean getDefectBadPixelPass() {
            return this.mItemDefectBadPixel.getPass();
        }

        public boolean getTotalBadPixelPass() {
            return this.mItemBadBadPixel.getPass();
        }

        public boolean getImageDeviationPass() {
            return this.mItemImageDeviation.getPass();
        }

        public boolean getMTPPass() {
            return this.mItemMTPTest.getPass();
        }

        public boolean getHistoryCheckPass() {
            return this.mItemHistoryCheck.getPass();
        }

        public boolean getRegisterTestPass() {
            return this.mItemRegisterTest.getPass();
        }

        public boolean getTestPatternTestPass() {
            return this.mItemTestPatternTest.getPass();
        }

        public boolean getOtpTestPass() {
            return this.mItemOtpTest.getPass();
        }

        public boolean getFlashTestPass() {
            return this.mItemFlashTest.getPass();
        }

        public Spec<Integer> getBlackDotSpec() {
            return this.mItemBlackDot.getSpec();
        }

        public Spec<Integer> getWhiteDotSpec() {
            return this.mItemWhiteDot.getSpec();
        }

        public Spec<Integer> getDefectPixelSpec() {
            return this.mItemDefectPixel.getSpec();
        }

        public Spec<Integer> getBadPixelSpec() {
            return this.mItemBadPixel.getSpec();
        }

        public Spec<Integer> getBlackBadPixelSpec() {
            return this.mItemBlackBadPixel.getSpec();
        }

        public Spec<Integer> getWhiteBadPixelSpec() {
            return this.mItemWhiteBadPixel.getSpec();
        }

        public Spec<Integer> getDefectBadPixelSpec() {
            return this.mItemDefectBadPixel.getSpec();
        }

        public Spec<Integer> getTotalBadPixelSpec() {
            return this.mItemBadBadPixel.getSpec();
        }

        public int[] getResetSpec() {
            return this.mItemReset.getSpecArray();
        }

        public int[] getEEPROMSpec() {
            return this.mItemEEPROM.getSpecArray();
        }

        public int[] getCalResultSpec() {
            return this.mItemCalResult.getSpecArray();
        }

        public int[] getMTPSpec() {
            return this.mItemMTPTest.getSpecArray();
        }

        public int[] getHistoryCheckSpec() {
            return this.mItemHistoryCheck.getSpecArray();
        }

        public int[] getRegisterTestSpec() {
            return this.mItemRegisterTest.getSpecArray();
        }

        public int[] getTestPatternTestSpec() {
            return this.mItemTestPatternTest.getSpecArray();
        }

        public int[] getOtpTestSpec() {
            return this.mItemOtpTest.getSpecArray();
        }

        public int[] getFlashTestSpec() {
            return this.mItemFlashTest.getSpecArray();
        }

        public Spec<Integer> getImageDeviationSpec() {
            return this.mItemImageDeviation.getSpec();
        }
    }

    private static class FingerPrintSNRData {
        private static final int ITEM_COUNT = 14;
        private static final int ITEM_COUNT_VIPER2 = 11;
        private int ITEM_COUNT_EGIS = 7;
        private int isDone = 0;
        private SNRItem mItemCorrectEEPROMver;
        private SNRItem mItemDPCol;
        private SNRItem mItemDPRow;
        private SNRItem mItemInterruptPin;
        private SNRItem mItemNoise;
        private SNRItem mItemNoise_Egis;
        private SNRItem mItemOverwriteCalData;
        private SNRItem mItemPlacement_test;
        private SNRItem mItemSNR;
        private SNRItem mItemSNR_Egis;
        private SNRItem mItemSignal;
        private SNRItem mItemSignal_Egis;
        private SNRItem mItemStandardDeviation;
        private String[] mSNRZone = new String[6];
        private String mSpecResultFile;

        private static class SNRItem {
            private boolean mPass;
            private Spec<Float> mSpec;
            private int[] mSpecArray;
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

            public void setSpec(Spec<Float> spec) {
                this.mSpec = spec;
            }

            public void setSpecArray(int[] spec) {
                this.mSpecArray = spec;
            }

            public String getValue() {
                return this.mValue;
            }

            public boolean getPass() {
                return this.mPass;
            }

            public Spec<Float> getSpec() {
                return this.mSpec;
            }

            public int[] getSpecArray() {
                return this.mSpecArray;
            }
        }

        public boolean isReadDone() {
            if (FingerPrintTest_egis.supportNewItems_Egistec || FingerPrintTest_egis.supportDefectBadPixel) {
                this.ITEM_COUNT_EGIS = 5;
            }
            return this.isDone == this.ITEM_COUNT_EGIS;
        }

        public void addSNRZone(int index, float value, int precision) {
            String[] strArr = this.mSNRZone;
            StringBuilder sb = new StringBuilder();
            sb.append("%.");
            sb.append(precision);
            sb.append("f");
            strArr[index] = String.format(sb.toString(), new Object[]{Float.valueOf(value)});
            this.isDone++;
        }

        public void addSignal(float value, int precision) {
            this.mItemSignal = new SNRItem();
            addItem(this.mItemSignal, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_SPEC_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_SPEC_MAX))), precision);
            this.isDone++;
        }

        public void addNoise(float value, int precision) {
            this.mItemNoise = new SNRItem();
            addItem(this.mItemNoise, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_SPEC_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_SPEC_MAX))), precision);
            this.isDone++;
        }

        public void addSNR(float value, int precision) {
            this.mItemSNR = new SNRItem();
            addItem(this.mItemSNR, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_SPEC_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_SPEC_MAX))), precision);
            this.isDone++;
        }

        public void addDeltaPixelRow(float value, int precision) {
            this.mItemDPRow = new SNRItem();
            addItem(this.mItemDPRow, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DELTA_PIXEL_ROW_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DELTA_PIXEL_ROW_MAX))), precision);
            this.isDone++;
        }

        public void addDeltaPixelCol(float value, int precision) {
            this.mItemDPCol = new SNRItem();
            addItem(this.mItemDPCol, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DELTA_PIXEL_COL_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_DELTA_PIXEL_COL_MAX))), precision);
            this.isDone++;
        }

        public void addInterruptPin(int value) {
            this.mItemInterruptPin = new SNRItem();
            addItem(this.mItemInterruptPin, value, new int[]{0});
            this.isDone++;
        }

        public void addSignal_Egis(float value, int precision) {
            this.mItemSignal_Egis = new SNRItem();
            addItem(this.mItemSignal_Egis, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addNoise_Egis(float value, int precision) {
            this.mItemNoise_Egis = new SNRItem();
            addItem(this.mItemNoise_Egis, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addSNR_Egis(float value, int precision) {
            this.mItemSNR_Egis = new SNRItem();
            addItem(this.mItemSNR_Egis, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addStandardDeviation(float value, int precision) {
            this.mItemStandardDeviation = new SNRItem();
            addItem(this.mItemStandardDeviation, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_STANDARD_DEVIATION_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_STANDARD_DEVIATION_MAX))), precision);
            this.isDone++;
        }

        public void addPlacement_test(float value, int precision) {
            this.mItemPlacement_test = new SNRItem();
            addItem(this.mItemPlacement_test, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PLACEMENT_TEST_MIN)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PLACEMENT_TEST_MAX))), precision);
            this.isDone++;
        }

        public void addOverwriteCalData(int value) {
            this.mItemOverwriteCalData = new SNRItem();
            addItem(this.mItemOverwriteCalData, value, new int[]{0, 2});
            this.isDone++;
        }

        public void addCorrectEEPROMver(int value) {
            this.mItemCorrectEEPROMver = new SNRItem();
            addItem(this.mItemCorrectEEPROMver, value, new int[]{0});
            this.isDone++;
        }

        private void addItem(SNRItem item, float value, Spec<Float> spec, int precision) {
            Locale locale = Locale.ENGLISH;
            StringBuilder sb = new StringBuilder();
            sb.append("%.");
            sb.append(precision);
            sb.append("f");
            String strValue = String.format(locale, sb.toString(), new Object[]{Float.valueOf(value)});
            boolean pass = false;
            if (value >= ((Float) spec.lower).floatValue() && value <= ((Float) spec.upper).floatValue()) {
                pass = true;
            }
            item.setSpec(spec);
            item.setValue(strValue);
            item.setPass(pass);
        }

        private void addItem(SNRItem item, int value, int[] spec) {
            boolean pass = false;
            int i = 0;
            while (true) {
                if (i >= spec.length) {
                    break;
                } else if (value == spec[i]) {
                    pass = true;
                    break;
                } else {
                    i++;
                }
            }
            item.setSpecArray(spec);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(value);
            item.setValue(sb.toString());
            item.setPass(pass);
        }

        public String getSNRZoneValue(int index) {
            return this.mSNRZone[index];
        }

        public String getDeltaPixelRowValue() {
            return this.mItemDPRow.getValue();
        }

        public String getDeltaPixelColValue() {
            return this.mItemDPCol.getValue();
        }

        public String getSignalValue() {
            return this.mItemSignal.getValue();
        }

        public String getNoiseValue() {
            return this.mItemNoise.getValue();
        }

        public String getSNRValue() {
            return this.mItemSNR.getValue();
        }

        public boolean getDeltaPixelRowPass() {
            return this.mItemDPRow.getPass();
        }

        public boolean getDeltaPixelColPass() {
            return this.mItemDPCol.getPass();
        }

        public boolean getSignalPass() {
            return this.mItemSignal.getPass();
        }

        public boolean getNoisePass() {
            return this.mItemNoise.getPass();
        }

        public boolean getSNRPass() {
            return this.mItemSNR.getPass();
        }

        public Spec<Float> getDeltaPixelRowSpec() {
            return this.mItemDPRow.getSpec();
        }

        public Spec<Float> getDeltaPixelColSpec() {
            return this.mItemDPCol.getSpec();
        }

        public Spec<Float> getSignalSpec() {
            return this.mItemSignal.getSpec();
        }

        public Spec<Float> getNoiseSpec() {
            return this.mItemNoise.getSpec();
        }

        public Spec<Float> getSNRSpec() {
            return this.mItemSNR.getSpec();
        }

        public String getInterruptPinValue() {
            return this.mItemInterruptPin.getValue();
        }

        public String getStandardDeviationValue() {
            return this.mItemStandardDeviation.getValue();
        }

        public String getSignalValue_Egis() {
            return this.mItemSignal_Egis.getValue();
        }

        public String getNoiseValue_Egis() {
            return this.mItemNoise_Egis.getValue();
        }

        public String getSNRValue_Egis() {
            return this.mItemSNR_Egis.getValue();
        }

        public String getPlacementValue_Egis() {
            return this.mItemPlacement_test.getValue();
        }

        public String getOverwriteCalDataValue() {
            return this.mItemOverwriteCalData.getValue();
        }

        public String getCorrectEEPROMverValue() {
            return this.mItemCorrectEEPROMver.getValue();
        }

        public boolean getInterruptPinPass() {
            return this.mItemInterruptPin.getPass();
        }

        public boolean getStandardDeviationPass() {
            return this.mItemStandardDeviation.getPass();
        }

        public boolean getSignalPass_Egis() {
            return this.mItemSignal_Egis.getPass();
        }

        public boolean getNoisePass_Egis() {
            return this.mItemNoise_Egis.getPass();
        }

        public boolean getSNRPass_Egis() {
            return this.mItemSNR_Egis.getPass();
        }

        public boolean getPlacementPass_Egis() {
            return this.mItemPlacement_test.getPass();
        }

        public boolean getOverwriteCalDataPass() {
            return this.mItemOverwriteCalData.getPass();
        }

        public boolean getCorrectEEPROMverPass() {
            return this.mItemCorrectEEPROMver.getPass();
        }

        public int[] getInterruptPinSpec() {
            return this.mItemInterruptPin.getSpecArray();
        }

        public Spec<Float> getStandardDeviationSpec() {
            return this.mItemStandardDeviation.getSpec();
        }

        public Spec<Float> getSignalSpec_Egis() {
            return this.mItemSignal_Egis.getSpec();
        }

        public Spec<Float> getNoiseSpec_Egis() {
            return this.mItemNoise_Egis.getSpec();
        }

        public Spec<Float> getSNRSpec_Egis() {
            return this.mItemSNR_Egis.getSpec();
        }

        public Spec<Float> getPlacementSpec_Egis() {
            return this.mItemPlacement_test.getSpec();
        }

        public int[] getOverwriteCalDataSpec() {
            return this.mItemOverwriteCalData.getSpecArray();
        }

        public int[] getCorrectEEPROMverSpec() {
            return this.mItemCorrectEEPROMver.getSpecArray();
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

    public FingerPrintTest_egis() {
        super(CLASS_NAME);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == 3 || (keyCode == 26 && mIsFingerprintTouchSide)) && this.m_gotermbutton.isShown()) {
            StringBuilder sb = new StringBuilder();
            sb.append("keycode : ");
            sb.append(keyCode);
            LtUtil.log_d(CLASS_NAME, "onKeyDown()", sb.toString());
            this.m_CurrentOperation = 1018;
            this.handler.post(this.mClosePutFingerDialogRunnable);
            verifySensorState(100107);
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void updateNormalScanResult_egis() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 4);
        if (mIsOptical) {
            this.mResultView.setRowCol(8, 6);
            this.mResultView.setTextData(0, 0, "Item");
            this.mResultView.setTextData(0, 2, "Spec");
            this.mResultView.setTextData(0, 3, "Result");
            int rowNum = 0 + 1;
            this.mResultView.setTextData(rowNum, 0, "History Check");
            FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(this.mFingerPrintNormalData.getHistoryCheckSpec()[0]);
            fingerPrintResultView_touch.setTextData(rowNum, 2, sb.toString());
            this.mResultView.setTextData(rowNum, 3, this.mFingerPrintNormalData.getHistoryCheckValue());
            this.mResultView.setTextData(rowNum, 4, this.mFingerPrintNormalData.getHistoryCheckPass() ? "PASS" : "FAIL");
            int rowNum2 = rowNum + 1;
            this.mResultView.setTextData(rowNum2, 0, "Register test");
            FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.mFingerPrintNormalData.getRegisterTestSpec()[0]);
            fingerPrintResultView_touch2.setTextData(rowNum2, 2, sb2.toString());
            this.mResultView.setTextData(rowNum2, 3, this.mFingerPrintNormalData.getRegisterTestValue());
            this.mResultView.setTextData(rowNum2, 4, this.mFingerPrintNormalData.getRegisterTestPass() ? "PASS" : "FAIL");
            int rowNum3 = rowNum2 + 1;
            this.mResultView.setTextData(rowNum3, 0, "OTP test");
            FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(this.mFingerPrintNormalData.getOtpTestSpec()[0]);
            fingerPrintResultView_touch3.setTextData(rowNum3, 2, sb3.toString());
            this.mResultView.setTextData(rowNum3, 3, this.mFingerPrintNormalData.getOtpTestValue());
            this.mResultView.setTextData(rowNum3, 4, this.mFingerPrintNormalData.getOtpTestPass() ? "PASS" : "FAIL");
            int rowNum4 = rowNum3 + 1;
            this.mResultView.setTextData(rowNum4, 0, "Flash test");
            this.mResultView.setTextData(rowNum4, 2, "NA");
            this.mResultView.setTextData(rowNum4, 3, this.mFingerPrintNormalData.getFlashTestValue());
            this.mResultView.setTextData(rowNum4, 4, "NA");
            int rowNum5 = rowNum4 + 1;
            this.mResultView.setTextData(rowNum5, 0, "Test Pattern test");
            FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.mFingerPrintNormalData.getTestPatternTestSpec()[0]);
            fingerPrintResultView_touch4.setTextData(rowNum5, 2, sb4.toString());
            this.mResultView.setTextData(rowNum5, 3, this.mFingerPrintNormalData.getTestPatternTestValue());
            this.mResultView.setTextData(rowNum5, 4, this.mFingerPrintNormalData.getTestPatternTestPass() ? "PASS" : "FAIL");
            int rowNum6 = rowNum5 + 1;
            this.mResultView.setTextData(rowNum6, 0, "Reset test");
            FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(this.mFingerPrintNormalData.getResetSpec()[0]);
            fingerPrintResultView_touch5.setTextData(rowNum6, 2, sb5.toString());
            this.mResultView.setTextData(rowNum6, 3, this.mFingerPrintNormalData.getResetValue());
            this.mResultView.setTextData(rowNum6, 4, this.mFingerPrintNormalData.getResetPass() ? "PASS" : "FAIL");
            int rowNum7 = rowNum6 + 1;
        } else if (supportNewItems_Egistec || supportDefectBadPixel) {
            if (supportDefectBadPixel) {
                this.mResultView.setRowCol(8, 6);
            } else {
                this.mResultView.setRowCol(7, 6);
            }
            this.mResultView.setTextData(0, 0, "Item");
            this.mResultView.setTextData(0, 2, "Spec");
            this.mResultView.setTextData(0, 4, "Result");
            int rowNum8 = 0 + 1;
            this.mResultView.setTextData(rowNum8, 0, "Black dot test");
            this.mResultView.setTextData(rowNum8, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().lower);
            sb6.append("~");
            sb6.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().upper);
            fingerPrintResultView_touch6.setTextData(rowNum8, 2, sb6.toString());
            this.mResultView.setTextData(rowNum8, 3, this.mFingerPrintNormalData.getBlackBadPixelValue());
            this.mResultView.setTextData(rowNum8, 4, this.mFingerPrintNormalData.getBlackBadPixelPass() ? "PASS" : "FAIL");
            int rowNum9 = rowNum8 + 1;
            this.mResultView.setTextData(rowNum9, 0, "White dot test");
            this.mResultView.setTextData(rowNum9, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
            StringBuilder sb7 = new StringBuilder();
            sb7.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().lower);
            sb7.append("~");
            sb7.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().upper);
            fingerPrintResultView_touch7.setTextData(rowNum9, 2, sb7.toString());
            this.mResultView.setTextData(rowNum9, 3, this.mFingerPrintNormalData.getWhiteBadPixelValue());
            this.mResultView.setTextData(rowNum9, 4, this.mFingerPrintNormalData.getWhiteBadPixelPass() ? "PASS" : "FAIL");
            int rowNum10 = rowNum9 + 1;
            if (supportDefectBadPixel) {
                this.mResultView.setTextData(rowNum10, 0, "Defect pixel test");
                this.mResultView.setTextData(rowNum10, 1, "Total bad pixel");
                FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
                StringBuilder sb8 = new StringBuilder();
                sb8.append(this.mFingerPrintNormalData.getDefectBadPixelSpec().lower);
                sb8.append("~");
                sb8.append(this.mFingerPrintNormalData.getDefectBadPixelSpec().upper);
                fingerPrintResultView_touch8.setTextData(rowNum10, 2, sb8.toString());
                this.mResultView.setTextData(rowNum10, 3, this.mFingerPrintNormalData.getDefectBadPixelValue());
                this.mResultView.setTextData(rowNum10, 4, this.mFingerPrintNormalData.getDefectBadPixelPass() ? "PASS" : "FAIL");
                rowNum10++;
            }
            this.mResultView.setTextData(rowNum10, 0, "Bad pixel test");
            this.mResultView.setTextData(rowNum10, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch9 = this.mResultView;
            StringBuilder sb9 = new StringBuilder();
            sb9.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().lower);
            sb9.append("~");
            sb9.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().upper);
            fingerPrintResultView_touch9.setTextData(rowNum10, 2, sb9.toString());
            this.mResultView.setTextData(rowNum10, 3, this.mFingerPrintNormalData.getTotalBadPixelValue());
            this.mResultView.setTextData(rowNum10, 4, this.mFingerPrintNormalData.getTotalBadPixelPass() ? "PASS" : "FAIL");
            int rowNum11 = rowNum10 + 1;
            this.mResultView.setTextData(rowNum11, 0, "Reset test");
            FingerPrintResultView_touch fingerPrintResultView_touch10 = this.mResultView;
            StringBuilder sb10 = new StringBuilder();
            sb10.append("");
            sb10.append(this.mFingerPrintNormalData.getResetSpec()[0]);
            fingerPrintResultView_touch10.setTextData(rowNum11, 2, sb10.toString());
            this.mResultView.setTextData(rowNum11, 3, this.mFingerPrintNormalData.getResetValue());
            this.mResultView.setTextData(rowNum11, 4, this.mFingerPrintNormalData.getResetPass() ? "PASS" : "FAIL");
            int rowNum12 = rowNum11 + 1;
            this.mResultView.setTextData(rowNum12, 0, "MTP test");
            FingerPrintResultView_touch fingerPrintResultView_touch11 = this.mResultView;
            StringBuilder sb11 = new StringBuilder();
            sb11.append("");
            sb11.append(this.mFingerPrintNormalData.getMTPSpec()[0]);
            fingerPrintResultView_touch11.setTextData(rowNum12, 2, sb11.toString());
            this.mResultView.setTextData(rowNum12, 3, this.mFingerPrintNormalData.getMTPValue());
            this.mResultView.setTextData(rowNum12, 4, this.mFingerPrintNormalData.getMTPPass() ? "PASS" : "FAIL");
            int rowNum13 = rowNum12 + 1;
            this.mResultView.setTextData(rowNum13, 0, "Cal Result");
            FingerPrintResultView_touch fingerPrintResultView_touch12 = this.mResultView;
            StringBuilder sb12 = new StringBuilder();
            sb12.append("");
            sb12.append(this.mFingerPrintNormalData.getCalResultSpec()[0]);
            fingerPrintResultView_touch12.setTextData(rowNum13, 2, sb12.toString());
            this.mResultView.setTextData(rowNum13, 3, this.mFingerPrintNormalData.getCalResultValue());
            this.mResultView.setTextData(rowNum13, 4, this.mFingerPrintNormalData.getCalResultPass() ? "PASS" : "FAIL");
            int rowNum14 = rowNum13 + 1;
        } else {
            this.mResultView.setRowCol(11, 6);
            this.mResultView.setTextData(0, 0, "Item");
            this.mResultView.setTextData(0, 3, "Spec");
            this.mResultView.setTextData(0, 4, "Result");
            int rowNum15 = 0 + 1;
            this.mResultView.setTextData(rowNum15, 0, "NS");
            this.mResultView.setTextData(rowNum15, 1, "Black dot test");
            this.mResultView.setTextData(rowNum15, 2, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch13 = this.mResultView;
            StringBuilder sb13 = new StringBuilder();
            sb13.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().lower);
            sb13.append("~");
            sb13.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().upper);
            fingerPrintResultView_touch13.setTextData(rowNum15, 3, sb13.toString());
            this.mResultView.setTextData(rowNum15, 4, this.mFingerPrintNormalData.getBlackBadPixelValue());
            this.mResultView.setTextData(rowNum15, 5, this.mFingerPrintNormalData.getBlackBadPixelPass() ? "PASS" : "FAIL");
            int rowNum16 = rowNum15 + 1;
            this.mResultView.setTextData(rowNum16, 2, "Cont. bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch14 = this.mResultView;
            StringBuilder sb14 = new StringBuilder();
            sb14.append(this.mFingerPrintNormalData.getBlackDotSpec().lower);
            sb14.append("~");
            sb14.append(this.mFingerPrintNormalData.getBlackDotSpec().upper);
            fingerPrintResultView_touch14.setTextData(rowNum16, 3, sb14.toString());
            this.mResultView.setTextData(rowNum16, 4, this.mFingerPrintNormalData.getBlackDotValue());
            this.mResultView.setTextData(rowNum16, 5, this.mFingerPrintNormalData.getBlackDotPass() ? "PASS" : "FAIL");
            int rowNum17 = rowNum16 + 1;
            this.mResultView.setTextData(rowNum17, 1, "White dot test");
            this.mResultView.setTextData(rowNum17, 2, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch15 = this.mResultView;
            StringBuilder sb15 = new StringBuilder();
            sb15.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().lower);
            sb15.append("~");
            sb15.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().upper);
            fingerPrintResultView_touch15.setTextData(rowNum17, 3, sb15.toString());
            this.mResultView.setTextData(rowNum17, 4, this.mFingerPrintNormalData.getWhiteBadPixelValue());
            this.mResultView.setTextData(rowNum17, 5, this.mFingerPrintNormalData.getWhiteBadPixelPass() ? "PASS" : "FAIL");
            int rowNum18 = rowNum17 + 1;
            this.mResultView.setTextData(rowNum18, 2, "Cont. bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch16 = this.mResultView;
            StringBuilder sb16 = new StringBuilder();
            sb16.append(this.mFingerPrintNormalData.getWhiteDotSpec().lower);
            sb16.append("~");
            sb16.append(this.mFingerPrintNormalData.getWhiteDotSpec().upper);
            fingerPrintResultView_touch16.setTextData(rowNum18, 3, sb16.toString());
            this.mResultView.setTextData(rowNum18, 4, this.mFingerPrintNormalData.getWhiteDotValue());
            this.mResultView.setTextData(rowNum18, 5, this.mFingerPrintNormalData.getWhiteDotPass() ? "PASS" : "FAIL");
            int rowNum19 = rowNum18 + 1;
            this.mResultView.setTextData(rowNum19, 1, "Bad pixel test");
            this.mResultView.setTextData(rowNum19, 2, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch17 = this.mResultView;
            StringBuilder sb17 = new StringBuilder();
            sb17.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().lower);
            sb17.append("~");
            sb17.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().upper);
            fingerPrintResultView_touch17.setTextData(rowNum19, 3, sb17.toString());
            this.mResultView.setTextData(rowNum19, 4, this.mFingerPrintNormalData.getTotalBadPixelValue());
            this.mResultView.setTextData(rowNum19, 5, this.mFingerPrintNormalData.getTotalBadPixelPass() ? "PASS" : "FAIL");
            int rowNum20 = rowNum19 + 1;
            this.mResultView.setTextData(rowNum20, 2, "Cont. bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch18 = this.mResultView;
            StringBuilder sb18 = new StringBuilder();
            sb18.append(this.mFingerPrintNormalData.getBadPixelSpec().lower);
            sb18.append("~");
            sb18.append(this.mFingerPrintNormalData.getBadPixelSpec().upper);
            fingerPrintResultView_touch18.setTextData(rowNum20, 3, sb18.toString());
            this.mResultView.setTextData(rowNum20, 4, this.mFingerPrintNormalData.getBadPixelValue());
            this.mResultView.setTextData(rowNum20, 5, this.mFingerPrintNormalData.getBadPixelPass() ? "PASS" : "FAIL");
            int rowNum21 = rowNum20 + 1;
            this.mResultView.setTextData(rowNum21, 1, "Reset test");
            FingerPrintResultView_touch fingerPrintResultView_touch19 = this.mResultView;
            StringBuilder sb19 = new StringBuilder();
            sb19.append("");
            sb19.append(this.mFingerPrintNormalData.getResetSpec()[0]);
            fingerPrintResultView_touch19.setTextData(rowNum21, 3, sb19.toString());
            this.mResultView.setTextData(rowNum21, 4, this.mFingerPrintNormalData.getResetValue());
            this.mResultView.setTextData(rowNum21, 5, this.mFingerPrintNormalData.getResetPass() ? "PASS" : "FAIL");
            int rowNum22 = rowNum21 + 1;
            this.mResultView.setTextData(rowNum22, 1, "EEPROM test");
            FingerPrintResultView_touch fingerPrintResultView_touch20 = this.mResultView;
            StringBuilder sb20 = new StringBuilder();
            sb20.append("");
            sb20.append(this.mFingerPrintNormalData.getEEPROMSpec()[0]);
            fingerPrintResultView_touch20.setTextData(rowNum22, 3, sb20.toString());
            this.mResultView.setTextData(rowNum22, 4, this.mFingerPrintNormalData.getEEPROMValue());
            this.mResultView.setTextData(rowNum22, 5, this.mFingerPrintNormalData.getEEPROMPass() ? "PASS" : "FAIL");
            int rowNum23 = rowNum22 + 1;
            this.mResultView.setTextData(rowNum23, 1, "Cal Result");
            FingerPrintResultView_touch fingerPrintResultView_touch21 = this.mResultView;
            StringBuilder sb21 = new StringBuilder();
            sb21.append("");
            sb21.append(this.mFingerPrintNormalData.getCalResultSpec()[0]);
            fingerPrintResultView_touch21.setTextData(rowNum23, 3, sb21.toString());
            this.mResultView.setTextData(rowNum23, 4, this.mFingerPrintNormalData.getCalResultValue());
            this.mResultView.setTextData(rowNum23, 5, this.mFingerPrintNormalData.getCalResultPass() ? "PASS" : "FAIL");
            int rowNum24 = rowNum23 + 1;
            this.mResultView.setTextData(rowNum24, 1, "Image Deviation");
            FingerPrintResultView_touch fingerPrintResultView_touch22 = this.mResultView;
            StringBuilder sb22 = new StringBuilder();
            sb22.append(this.mFingerPrintNormalData.getImageDeviationSpec().lower);
            sb22.append("~");
            sb22.append(this.mFingerPrintNormalData.getImageDeviationSpec().upper);
            fingerPrintResultView_touch22.setTextData(rowNum24, 3, sb22.toString());
            this.mResultView.setTextData(rowNum24, 4, this.mFingerPrintNormalData.getImageDeviationValue());
            this.mResultView.setTextData(rowNum24, 5, this.mFingerPrintNormalData.getImageDeviationPass() ? "PASS" : "FAIL");
            int rowNum25 = rowNum24 + 1;
        }
    }

    /* access modifiers changed from: private */
    public void updateSNRResult_egis() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 4);
        if (supportNewItems_Egistec || supportDefectBadPixel) {
            this.mResultView.setRowCol(8, 4);
            this.mResultView.setTextData(0, 0, "Item");
            this.mResultView.setTextData(0, 1, "Spec");
            this.mResultView.setTextData(0, 2, "Result");
            int rowNum = 0 + 1;
            this.mResultView.setTextData(rowNum, 0, "Interrupt pin test");
            FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(this.mFingerPrintSNRData.getInterruptPinSpec()[0]);
            fingerPrintResultView_touch.setTextData(rowNum, 1, sb.toString());
            this.mResultView.setTextData(rowNum, 2, this.mFingerPrintSNRData.getInterruptPinValue());
            this.mResultView.setTextData(rowNum, 3, this.mFingerPrintSNRData.getInterruptPinPass() ? "PASS" : "FAIL");
            int rowNum2 = rowNum + 1;
            this.mResultView.setTextData(rowNum2, 0, "Placemet Test");
            FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mFingerPrintSNRData.getPlacementSpec_Egis().lower);
            sb2.append("~");
            sb2.append(this.mFingerPrintSNRData.getPlacementSpec_Egis().upper);
            fingerPrintResultView_touch2.setTextData(rowNum2, 1, sb2.toString());
            this.mResultView.setTextData(rowNum2, 2, this.mFingerPrintSNRData.getPlacementValue_Egis());
            this.mResultView.setTextData(rowNum2, 3, this.mFingerPrintSNRData.getPlacementPass_Egis() ? "PASS" : "FAIL");
            int rowNum3 = rowNum2 + 1;
            this.mResultView.setTextData(rowNum3, 0, "Signal");
            FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mFingerPrintSNRData.getSignalSpec_Egis().lower);
            sb3.append("~");
            sb3.append(this.mFingerPrintSNRData.getSignalSpec_Egis().upper);
            fingerPrintResultView_touch3.setTextData(rowNum3, 1, sb3.toString());
            this.mResultView.setTextData(rowNum3, 2, this.mFingerPrintSNRData.getSignalValue_Egis());
            this.mResultView.setTextData(rowNum3, 3, this.mFingerPrintSNRData.getSignalPass_Egis() ? "PASS" : "FAIL");
            int rowNum4 = rowNum3 + 1;
            this.mResultView.setTextData(rowNum4, 0, "Noise");
            FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().lower);
            sb4.append("~");
            sb4.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().upper);
            fingerPrintResultView_touch4.setTextData(rowNum4, 1, sb4.toString());
            this.mResultView.setTextData(rowNum4, 2, this.mFingerPrintSNRData.getNoiseValue_Egis());
            this.mResultView.setTextData(rowNum4, 3, this.mFingerPrintSNRData.getNoisePass_Egis() ? "PASS" : "FAIL");
            int rowNum5 = rowNum4 + 1;
            this.mResultView.setTextData(rowNum5, 0, "SNR");
            FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(this.mFingerPrintSNRData.getSNRSpec_Egis().lower);
            sb5.append("~");
            sb5.append(this.mFingerPrintSNRData.getSNRSpec_Egis().upper);
            fingerPrintResultView_touch5.setTextData(rowNum5, 1, sb5.toString());
            this.mResultView.setTextData(rowNum5, 2, this.mFingerPrintSNRData.getSNRValue_Egis());
            this.mResultView.setTextData(rowNum5, 3, this.mFingerPrintSNRData.getSNRPass_Egis() ? "PASS" : "FAIL");
            int rowNum6 = rowNum5 + 1;
            return;
        }
        this.mResultView.setRowCol(8, 4);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 1, "Spec");
        this.mResultView.setTextData(0, 2, "Result");
        int rowNum7 = 0 + 1;
        this.mResultView.setTextData(rowNum7, 0, "Interrupt pin test");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("");
        sb6.append(this.mFingerPrintSNRData.getInterruptPinSpec()[0]);
        fingerPrintResultView_touch6.setTextData(rowNum7, 1, sb6.toString());
        this.mResultView.setTextData(rowNum7, 2, this.mFingerPrintSNRData.getInterruptPinValue());
        this.mResultView.setTextData(rowNum7, 3, this.mFingerPrintSNRData.getInterruptPinPass() ? "PASS" : "FAIL");
        int rowNum8 = rowNum7 + 1;
        this.mResultView.setTextData(rowNum8, 0, "Signal");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintSNRData.getSignalSpec_Egis().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintSNRData.getSignalSpec_Egis().upper);
        fingerPrintResultView_touch7.setTextData(rowNum8, 1, sb7.toString());
        this.mResultView.setTextData(rowNum8, 2, this.mFingerPrintSNRData.getSignalValue_Egis());
        this.mResultView.setTextData(rowNum8, 3, this.mFingerPrintSNRData.getSignalPass_Egis() ? "PASS" : "FAIL");
        int rowNum9 = rowNum8 + 1;
        this.mResultView.setTextData(rowNum9, 0, "Noise");
        FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().lower);
        sb8.append("~");
        sb8.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().upper);
        fingerPrintResultView_touch8.setTextData(rowNum9, 1, sb8.toString());
        this.mResultView.setTextData(rowNum9, 2, this.mFingerPrintSNRData.getNoiseValue_Egis());
        this.mResultView.setTextData(rowNum9, 3, this.mFingerPrintSNRData.getNoisePass_Egis() ? "PASS" : "FAIL");
        int rowNum10 = rowNum9 + 1;
        this.mResultView.setTextData(rowNum10, 0, "SNR");
        FingerPrintResultView_touch fingerPrintResultView_touch9 = this.mResultView;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(this.mFingerPrintSNRData.getSNRSpec_Egis().lower);
        sb9.append("~");
        sb9.append(this.mFingerPrintSNRData.getSNRSpec_Egis().upper);
        fingerPrintResultView_touch9.setTextData(rowNum10, 1, sb9.toString());
        this.mResultView.setTextData(rowNum10, 2, this.mFingerPrintSNRData.getSNRValue_Egis());
        this.mResultView.setTextData(rowNum10, 3, this.mFingerPrintSNRData.getSNRPass_Egis() ? "PASS" : "FAIL");
        int rowNum11 = rowNum10 + 1;
        this.mResultView.setTextData(rowNum11, 0, "Standard Deviation");
        FingerPrintResultView_touch fingerPrintResultView_touch10 = this.mResultView;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(this.mFingerPrintSNRData.getStandardDeviationSpec().lower);
        sb10.append("~");
        sb10.append(this.mFingerPrintSNRData.getStandardDeviationSpec().upper);
        fingerPrintResultView_touch10.setTextData(rowNum11, 1, sb10.toString());
        this.mResultView.setTextData(rowNum11, 2, this.mFingerPrintSNRData.getStandardDeviationValue());
        this.mResultView.setTextData(rowNum11, 3, this.mFingerPrintSNRData.getStandardDeviationPass() ? "PASS" : "FAIL");
        int rowNum12 = rowNum11 + 1;
        this.mResultView.setTextData(rowNum12, 0, "Overwrite Cal Data");
        FingerPrintResultView_touch fingerPrintResultView_touch11 = this.mResultView;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(this.mFingerPrintSNRData.getOverwriteCalDataSpec()[0]);
        sb11.append(",");
        sb11.append(this.mFingerPrintSNRData.getOverwriteCalDataSpec()[1]);
        fingerPrintResultView_touch11.setTextData(rowNum12, 1, sb11.toString());
        this.mResultView.setTextData(rowNum12, 2, this.mFingerPrintSNRData.getOverwriteCalDataValue());
        this.mResultView.setTextData(rowNum12, 3, this.mFingerPrintSNRData.getOverwriteCalDataPass() ? "PASS" : "FAIL");
        int rowNum13 = rowNum12 + 1;
        this.mResultView.setTextData(rowNum13, 0, "Correct EEPROM ver");
        this.mResultView.setTextData(rowNum13, 1, Integer.toString(this.mFingerPrintSNRData.getCorrectEEPROMverSpec()[0]));
        this.mResultView.setTextData(rowNum13, 2, this.mFingerPrintSNRData.getCorrectEEPROMverValue());
        this.mResultView.setTextData(rowNum13, 3, this.mFingerPrintSNRData.getCorrectEEPROMverPass() ? "PASS" : "FAIL");
        int rowNum14 = rowNum13 + 1;
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
            if (x > this.m_gotermbutton.getX() && x < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y > this.m_gotermbutton.getY() && y < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                LtUtil.log_d(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_DOWN pos is inside OK button ");
                this.m_gotermbutton.setPressed(true);
            }
        } else if (event.getActionMasked() == 6) {
            int index2 = (action & 65280) >> 8;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ACTION_POINTER_UP index : ");
            sb2.append(index2);
            LtUtil.log_d(CLASS_NAME, "onTouchEvent", sb2.toString());
            float x2 = event.getX(index2);
            float y2 = event.getY(index2);
            if (x2 > this.m_gotermbutton.getX() && x2 < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y2 > this.m_gotermbutton.getY() && y2 < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                LtUtil.log_d(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_UP pos is inside OK button ");
                this.m_gotermbutton.setPressed(false);
                this.m_gotermbutton.callOnClick();
            } else if (this.m_gotermbutton.isPressed()) {
                this.m_gotermbutton.setPressed(false);
            }
        }
        return super.onTouchEvent(event);
    }

    private void initLayout() {
        Display mDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        this.m_ScreenWidth = outpoint.x;
        this.m_ScreenHeight = outpoint.y;
        this.mFingerPrintNormalData = new FingerPrintNormalData();
        this.mFingerPrintSNRData = new FingerPrintSNRData();
        this.mResultView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_resultview);
        this.mSNRZoneView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_snrzoneview);
        this.iv_snrimage = (ImageView) findViewById(C0268R.C0269id.fingerprint_snrimage);
        this.mResultView.setVisibility(8);
        this.mSNRZoneView.setVisibility(8);
        this.m_gotermbutton = new Button(this);
        this.m_gotermbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FingerPrintTest_egis.this.m_CurrentOperation = 1018;
                FingerPrintTest_egis.this.handler.post(FingerPrintTest_egis.this.mClosePutFingerDialogRunnable);
                FingerPrintTest_egis.this.verifySensorState(100107);
            }
        });
        LayoutParams params = this.iv_snrimage.getLayoutParams();
        params.height = this.m_ScreenHeight / 4;
        params.width = this.m_ScreenWidth / 2;
        this.iv_snrimage.setLayoutParams(params);
        RelativeLayout lContainerLayout = new RelativeLayout(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        RelativeLayout.LayoutParams lButtonParams = new RelativeLayout.LayoutParams(this.m_ScreenWidth / 3, this.m_ScreenHeight / 8);
        lButtonParams.addRule(12);
        lButtonParams.addRule(14);
        lButtonParams.bottomMargin = this.m_ScreenHeight / 3;
        this.m_gotermbutton.setTextSize(25.0f);
        this.m_gotermbutton.setLayoutParams(lButtonParams);
        this.m_gotermbutton.setVisibility(8);
        this.m_gotermbutton.setGravity(17);
        lContainerLayout.addView(this.m_gotermbutton);
        addContentView(lContainerLayout, new RelativeLayout.LayoutParams(-1, -1));
        this.tv_msg1 = (TextView) findViewById(C0268R.C0269id.fingerprint_message1);
        this.tv_msg2 = (TextView) findViewById(C0268R.C0269id.fingerprint_message2);
        this.tv_result = (TextView) findViewById(C0268R.C0269id.result_text);
        this.tv_msg2.setText("Ready");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0268R.layout.fingerprint_test_comb);
        mFingerPrint_Vendor = Kernel.read(Kernel.FINGERPRINT_VENDOR);
        StringBuilder sb = new StringBuilder();
        sb.append("mFingerPrint_Vendor : ");
        sb.append(mFingerPrint_Vendor);
        sb.append("/");
        LtUtil.log_d(CLASS_NAME, "onCreate", sb.toString());
        supportNewItems_Egistec = Feature.getBoolean(Feature.FINGERPRINT_EGISTEC_SUPPORT_NEWITEMS, false);
        mFingerPrintName = Kernel.read(Kernel.FINGERPRINT_CHIP_NAME);
        if ("ET523".equalsIgnoreCase(mFingerPrintName) || "ET603".equalsIgnoreCase(mFingerPrintName)) {
            supportDefectBadPixel = true;
        }
        mIsFingerprintTouchSide = "google_touch_display_ultrasonic".contains("google_touch_side");
        mIsOptical = mFingerPrintName.contains("ET71");
        initLayout();
        LtUtil.log_d(CLASS_NAME, "onCreate", "onCreate");
        this.mTest = getIntent().getStringExtra("Action");
        this.os_version = VERSION.RELEASE.replace("ver:", "").trim();
        this.os_version_array = this.os_version.split("");
        if (Integer.parseInt(this.os_version_array[1]) >= 6) {
            this.IDENTIFY_FAILURE_SENSOR_CHANGED = 64;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        int sensorStatus = -1;
        if (mIsOptical) {
            this.mFingerprintOptical = Fingerprint.create(this);
            if (this.mFingerprintOptical != null) {
                this.mFingerPrintEventListenerOptical = new FingerPrintEventListenerOptical();
                this.mFingerprintOptical.setEventListener(this.mFingerPrintEventListenerOptical);
                sensorStatus = this.mFingerprintOptical.getSensorStatus();
            }
        } else {
            this.mFingerprint = egistec.csa.client.api.Fingerprint.create(this);
            if (this.mFingerprint != null) {
                this.mFingerPrintEventListener = new FingerPrintEventListener();
                this.mFingerprint.setEventListener(this.mFingerPrintEventListener);
                sensorStatus = this.mFingerprint.getSensorStatus();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("sensorStatus value : ");
        sb.append(sensorStatus);
        LtUtil.log_d(CLASS_NAME, "onResume", sb.toString());
        if (sensorStatus != this.SENSOR_OK) {
            LtUtil.log_d(CLASS_NAME, "onResume", "sensorStatus has been failed ");
            this.m_ResultMessage = "Fingerprint initializing Fail";
            this.handler.post(this.mUpdateResultMessageRunnable);
        } else if ("NORMALDATA".equals(this.mTest)) {
            this.m_CurrentOperation = 1014;
            this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
            this.handler.post(this.mUpdateMessageRunnable);
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    int result = FingerPrintTest_egis.this.verifySensorState(100103);
                    if (result == FingerPrintTest_egis.this.IDENTIFY_FAILURE_SENSOR_CHANGED) {
                        FingerPrintTest_egis.this.STATE_REPLACEMENT_WARNING = true;
                        LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onResume", "STATE_REPLACEMENT_WARNING = true ");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("SENSOR_TEST_NORMALSCAN_COMMAND Success / result : ");
                    sb.append(result);
                    LtUtil.log_d(FingerPrintTest_egis.CLASS_NAME, "onResume", sb.toString());
                }
            }, 500);
        } else if ("SNR".equals(this.mTest)) {
            this.m_CurrentOperation = 1016;
            verifySensorState(100106);
            LtUtil.log_d(CLASS_NAME, "onResume", "SENSOR_TEST_SNR_ORG_COMMAND Success ");
        } else if ("SENSORINFO".equals(this.mTest)) {
            this.m_Message = "Sensor Infomation";
            if (mIsOptical) {
                this.m_ResultMessage = this.mFingerprintOptical.getSensorInfo();
            } else {
                this.m_ResultMessage = this.mFingerprint.getSensorInfo();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getSensorInfo : ");
            sb2.append(this.m_ResultMessage);
            LtUtil.log_d(CLASS_NAME, "onResume", sb2.toString());
            this.handler.post(this.mUpdateMessageRunnable);
            this.handler.post(this.mUpdateResultMessageRunnable);
        }
    }

    /* access modifiers changed from: private */
    public int verifySensorState(int cmd) {
        if (mIsOptical) {
            if (this.mFingerprintOptical != null) {
                return this.mFingerprintOptical.verifySensorState(cmd, 0, 0, 0, 0);
            }
        } else if (this.mFingerprint != null) {
            return this.mFingerprint.verifySensorState(cmd, 0, 0, 0, 0);
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x06b3 A[SYNTHETIC, Splitter:B:194:0x06b3] */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x06c1 A[SYNTHETIC, Splitter:B:201:0x06c1] */
    /* JADX WARNING: Removed duplicated region for block: B:208:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readNormalScanLogFile_egis() {
        /*
            r47 = this;
            r1 = r47
            java.lang.String r0 = "FingerPrintTest_egis"
            java.lang.String r2 = "Test : read Normal scan log-EGIS"
            android.util.Log.i(r0, r2)
            java.lang.String r0 = "/data/egis_sensor_test_NS.log"
            java.lang.String[] r2 = r1.os_version_array
            r3 = 1
            r2 = r2[r3]
            int r2 = java.lang.Integer.parseInt(r2)
            r4 = 6
            if (r2 < r4) goto L_0x001f
            boolean r2 = android.os.FactoryTest.isFactoryBinary()
            if (r2 != 0) goto L_0x001f
            java.lang.String r0 = "/data/fpSnrTest/egis_sensor_test_NS.log"
        L_0x001f:
            r2 = r0
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            r5 = r0
            boolean r0 = r5.exists()
            if (r0 == 0) goto L_0x06cb
            r6 = 0
            r0 = 0
            r7 = r0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r6 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r8.<init>(r6)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r0.<init>(r8)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r7 = r0
            java.lang.String r0 = ""
            java.lang.String r8 = r7.readLine()     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r9 = r8
            if (r8 == 0) goto L_0x069a
            java.lang.String r0 = "FingerPrintTest_egis"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r8.<init>()     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            java.lang.String r10 = " normal result : "
            r8.append(r10)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            r8.append(r9)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            android.util.Log.i(r0, r8)     // Catch:{ IOException -> 0x06a9, all -> 0x06a2 }
            java.lang.String r0 = ""
            r8 = r0
            java.lang.String r0 = ""
            r10 = r0
            java.lang.String r0 = ""
            r11 = r0
            java.lang.String r0 = ""
            r12 = r0
            java.lang.String r0 = ""
            r13 = r0
            java.lang.String r0 = ""
            r14 = r0
            java.lang.String r0 = ""
            r15 = r0
            java.lang.String r0 = ""
            r16 = r0
            java.lang.String r0 = ""
            r17 = r0
            java.lang.String r0 = ""
            r18 = r0
            java.lang.String r0 = ""
            r19 = r0
            java.lang.String r0 = ""
            r20 = r0
            java.lang.String r0 = ""
            r21 = r0
            java.lang.String r0 = ""
            r22 = r0
            java.lang.String r0 = ""
            r23 = r0
            java.lang.String r0 = ""
            r24 = r0
            java.lang.String r0 = ""
            r25 = r0
            java.lang.String r0 = ""
            r26 = r0
            java.lang.String r0 = ","
            java.lang.String[] r0 = r9.split(r0)     // Catch:{ IndexOutOfBoundsException -> 0x068f }
            boolean r27 = mIsOptical     // Catch:{ IndexOutOfBoundsException -> 0x068f }
            r28 = 3
            r29 = 2
            r30 = 4
            r31 = 0
            if (r27 == 0) goto L_0x0253
            r4 = r0[r31]     // Catch:{ IndexOutOfBoundsException -> 0x068f }
            java.lang.String r4 = r4.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068f }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r3 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0248 }
            r32 = r2
            int r2 = java.lang.Integer.parseInt(r4)     // Catch:{ IndexOutOfBoundsException -> 0x023f, IOException -> 0x023a, all -> 0x0234 }
            r3.addHistoryCheck(r2)     // Catch:{ IndexOutOfBoundsException -> 0x023f, IOException -> 0x023a, all -> 0x0234 }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x023f, IOException -> 0x023a, all -> 0x0234 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x023f, IOException -> 0x023a, all -> 0x0234 }
            r33 = r5
            java.lang.String r5 = "historyCheck : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            r2 = 1
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x022d }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r3 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0222 }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0222 }
            r3.addRegisterTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0222 }
            java.lang.String r3 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0222 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0222 }
            r34 = r4
            java.lang.String r4 = "registerTest : "
            r5.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            java.lang.String r4 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            android.util.Log.d(r3, r4)     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            r3 = r0[r29]     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0219 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x020c }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x020c }
            r4.addTestOtpTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x020c }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x020c }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x020c }
            r35 = r2
            java.lang.String r2 = "otpTest : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            r2 = r0[r28]     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0201 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x01f2 }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x01f2 }
            r4.addTestFlashTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x01f2 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x01f2 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x01f2 }
            r36 = r3
            java.lang.String r3 = "flashTest : "
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            java.lang.String r3 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            android.util.Log.d(r4, r3)     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            r3 = r0[r30]     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x01e5 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x01d4 }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x01d4 }
            r4.addTestPatternTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x01d4 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x01d4 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x01d4 }
            r37 = r2
            java.lang.String r2 = "testPatternTest : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            r2 = 5
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x01c5 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x01bf }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x01bf }
            r4.addResetTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x01bf }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x01bf }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x01bf }
            r38 = r3
            java.lang.String r3 = "resetTest : "
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x01b0 }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x01b0 }
            java.lang.String r3 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x01b0 }
            android.util.Log.d(r4, r3)     // Catch:{ IndexOutOfBoundsException -> 0x01b0 }
            r41 = r2
            r2 = r19
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            r24 = r38
            goto L_0x0649
        L_0x01b0:
            r0 = move-exception
            r17 = r2
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            r24 = r38
            goto L_0x0694
        L_0x01bf:
            r0 = move-exception
            r38 = r3
            r17 = r2
            goto L_0x01c8
        L_0x01c5:
            r0 = move-exception
            r38 = r3
        L_0x01c8:
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            r24 = r38
            goto L_0x0694
        L_0x01d4:
            r0 = move-exception
            r37 = r2
            r38 = r3
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            r24 = r38
            goto L_0x0694
        L_0x01e5:
            r0 = move-exception
            r37 = r2
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            goto L_0x0694
        L_0x01f2:
            r0 = move-exception
            r37 = r2
            r36 = r3
            r22 = r34
            r23 = r35
            r25 = r36
            r26 = r37
            goto L_0x0694
        L_0x0201:
            r0 = move-exception
            r36 = r3
            r22 = r34
            r23 = r35
            r25 = r36
            goto L_0x0694
        L_0x020c:
            r0 = move-exception
            r35 = r2
            r36 = r3
            r22 = r34
            r23 = r35
            r25 = r36
            goto L_0x0694
        L_0x0219:
            r0 = move-exception
            r35 = r2
            r22 = r34
            r23 = r35
            goto L_0x0694
        L_0x0222:
            r0 = move-exception
            r35 = r2
            r34 = r4
            r22 = r34
            r23 = r35
            goto L_0x0694
        L_0x022d:
            r0 = move-exception
            r34 = r4
            r22 = r34
            goto L_0x0694
        L_0x0234:
            r0 = move-exception
            r33 = r5
            r2 = r0
            goto L_0x06bf
        L_0x023a:
            r0 = move-exception
            r33 = r5
            goto L_0x06ae
        L_0x023f:
            r0 = move-exception
            r34 = r4
            r33 = r5
            r22 = r34
            goto L_0x0694
        L_0x0248:
            r0 = move-exception
            r32 = r2
            r34 = r4
            r33 = r5
            r22 = r34
            goto L_0x0694
        L_0x0253:
            r32 = r2
            r33 = r5
            boolean r2 = supportNewItems_Egistec     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            if (r2 == 0) goto L_0x0373
            r2 = r0[r31]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBlackBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "blackBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r29]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addWhiteBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "whiteBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r30]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r15 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBadBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "totalBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r4]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r3 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            int r4 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            r3.addResetTest(r4)     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            java.lang.String r3 = "FingerPrintTest_egis"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            r4.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            java.lang.String r5 = "resetTest : "
            r4.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            r4.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            java.lang.String r4 = r4.toString()     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            android.util.Log.d(r3, r4)     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            r3 = 7
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x036c }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0361 }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0361 }
            r4.addMTPTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0361 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0361 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0361 }
            r39 = r2
            java.lang.String r2 = "MTPTest : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            r2 = 8
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0358 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0352 }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0352 }
            r4.addCalResult(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0352 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0352 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0352 }
            r40 = r3
            java.lang.String r3 = "calResult : "
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0349 }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0349 }
            java.lang.String r3 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0349 }
            android.util.Log.d(r4, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0349 }
            r41 = r39
            r21 = r40
            goto L_0x0649
        L_0x0349:
            r0 = move-exception
            r19 = r2
            r17 = r39
            r21 = r40
            goto L_0x0694
        L_0x0352:
            r0 = move-exception
            r40 = r3
            r19 = r2
            goto L_0x035b
        L_0x0358:
            r0 = move-exception
            r40 = r3
        L_0x035b:
            r17 = r39
            r21 = r40
            goto L_0x0694
        L_0x0361:
            r0 = move-exception
            r39 = r2
            r40 = r3
            r17 = r39
            r21 = r40
            goto L_0x0694
        L_0x036c:
            r0 = move-exception
            r39 = r2
            r17 = r39
            goto L_0x0694
        L_0x0373:
            boolean r2 = supportDefectBadPixel     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            if (r2 == 0) goto L_0x04b6
            r2 = r0[r31]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBlackBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "blackBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r29]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addWhiteBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "whiteBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r30]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r13 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r13)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addDefectBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "defectBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r13)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r4]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r15 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBadBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r4 = "totalBadPixel : "
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = 8
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r3 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            int r4 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            r3.addResetTest(r4)     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            java.lang.String r3 = "FingerPrintTest_egis"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            r4.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            java.lang.String r5 = "resetTest : "
            r4.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            r4.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            java.lang.String r4 = r4.toString()     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            android.util.Log.d(r3, r4)     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            r3 = 9
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x04af }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x04a4 }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x04a4 }
            r4.addMTPTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x04a4 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x04a4 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x04a4 }
            r41 = r2
            java.lang.String r2 = "MTPTest : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            r2 = 10
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x049b }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0495 }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0495 }
            r4.addCalResult(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0495 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0495 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0495 }
            r42 = r3
            java.lang.String r3 = "calResult : "
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x048c }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x048c }
            java.lang.String r3 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x048c }
            android.util.Log.d(r4, r3)     // Catch:{ IndexOutOfBoundsException -> 0x048c }
            r21 = r42
            goto L_0x0649
        L_0x048c:
            r0 = move-exception
            r19 = r2
            r17 = r41
            r21 = r42
            goto L_0x0694
        L_0x0495:
            r0 = move-exception
            r42 = r3
            r19 = r2
            goto L_0x049e
        L_0x049b:
            r0 = move-exception
            r42 = r3
        L_0x049e:
            r17 = r41
            r21 = r42
            goto L_0x0694
        L_0x04a4:
            r0 = move-exception
            r41 = r2
            r42 = r3
            r17 = r41
            r21 = r42
            goto L_0x0694
        L_0x04af:
            r0 = move-exception
            r41 = r2
            r17 = r41
            goto L_0x0694
        L_0x04b6:
            r2 = r0[r31]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBlackBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "blackBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = 1
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r10)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBlackDot(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "blackDot : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r28]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addWhiteBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "whiteBadPixel : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r30]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r12 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r12)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addWhiteDot(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r5 = "whiteDot : "
            r3.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r12)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = r0[r4]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r15 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r2 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            int r3 = java.lang.Integer.parseInt(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2.addBadBadPixel(r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r4 = "totalBadPixel : "
            r3.append(r4)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            r2 = 7
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x068d }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r3 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            int r4 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            r3.addBadPixel(r4)     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            java.lang.String r3 = "FingerPrintTest_egis"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            r4.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            java.lang.String r5 = "BadPixel : "
            r4.append(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            r4.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            java.lang.String r4 = r4.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            android.util.Log.d(r3, r4)     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            r3 = 9
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0687 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x067d }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x067d }
            r4.addResetTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x067d }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x067d }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x067d }
            r43 = r2
            java.lang.String r2 = "resetTest : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            r2 = 10
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0675 }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0669 }
            int r5 = java.lang.Integer.parseInt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0669 }
            r4.addEEPROMTest(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0669 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0669 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0669 }
            r44 = r3
            java.lang.String r3 = "eepromTest : "
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            java.lang.String r3 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            android.util.Log.d(r4, r3)     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            r3 = 11
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x065f }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x0659 }
            int r5 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0659 }
            r4.addCalResult(r5)     // Catch:{ IndexOutOfBoundsException -> 0x0659 }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0659 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0659 }
            r45 = r2
            java.lang.String r2 = "calResult : "
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            r5.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            java.lang.String r2 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            android.util.Log.d(r4, r2)     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            r2 = 12
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x064f }
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintNormalData r4 = r1.mFingerPrintNormalData     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            float r5 = java.lang.Float.parseFloat(r2)     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            int r5 = (int) r5     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            r4.addImageDeviation(r5)     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            java.lang.String r4 = "FingerPrintTest_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            r46 = r0
            java.lang.String r0 = "imageDeviation : "
            r5.append(r0)     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            r5.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            java.lang.String r0 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            android.util.Log.d(r4, r0)     // Catch:{ IndexOutOfBoundsException -> 0x064b }
            r20 = r2
            r2 = r3
            r16 = r43
            r41 = r44
            r18 = r45
        L_0x0649:
            goto L_0x069e
        L_0x064b:
            r0 = move-exception
            r20 = r2
            goto L_0x0650
        L_0x064f:
            r0 = move-exception
        L_0x0650:
            r19 = r3
            r16 = r43
            r17 = r44
            r18 = r45
            goto L_0x0694
        L_0x0659:
            r0 = move-exception
            r45 = r2
            r19 = r3
            goto L_0x0662
        L_0x065f:
            r0 = move-exception
            r45 = r2
        L_0x0662:
            r16 = r43
            r17 = r44
            r18 = r45
            goto L_0x0694
        L_0x0669:
            r0 = move-exception
            r45 = r2
            r44 = r3
            r16 = r43
            r17 = r44
            r18 = r45
            goto L_0x0694
        L_0x0675:
            r0 = move-exception
            r44 = r3
            r16 = r43
            r17 = r44
            goto L_0x0694
        L_0x067d:
            r0 = move-exception
            r43 = r2
            r44 = r3
            r16 = r43
            r17 = r44
            goto L_0x0694
        L_0x0687:
            r0 = move-exception
            r43 = r2
            r16 = r43
            goto L_0x0694
        L_0x068d:
            r0 = move-exception
            goto L_0x0694
        L_0x068f:
            r0 = move-exception
            r32 = r2
            r33 = r5
        L_0x0694:
            r0.printStackTrace()     // Catch:{ IOException -> 0x0698 }
            goto L_0x069e
        L_0x0698:
            r0 = move-exception
            goto L_0x06ae
        L_0x069a:
            r32 = r2
            r33 = r5
        L_0x069e:
            r6.close()     // Catch:{ IOException -> 0x06b7 }
            goto L_0x06b6
        L_0x06a2:
            r0 = move-exception
            r32 = r2
            r33 = r5
            r2 = r0
            goto L_0x06bf
        L_0x06a9:
            r0 = move-exception
            r32 = r2
            r33 = r5
        L_0x06ae:
            r0.printStackTrace()     // Catch:{ all -> 0x06bd }
            if (r6 == 0) goto L_0x06cf
            r6.close()     // Catch:{ IOException -> 0x06b7 }
        L_0x06b6:
            goto L_0x06cf
        L_0x06b7:
            r0 = move-exception
            r2 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
            goto L_0x06b6
        L_0x06bd:
            r0 = move-exception
            r2 = r0
        L_0x06bf:
            if (r6 == 0) goto L_0x06ca
            r6.close()     // Catch:{ IOException -> 0x06c5 }
            goto L_0x06ca
        L_0x06c5:
            r0 = move-exception
            r3 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x06ca:
            throw r2
        L_0x06cb:
            r32 = r2
            r33 = r5
        L_0x06cf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.FingerPrintTest_egis.readNormalScanLogFile_egis():void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0292 A[SYNTHETIC, Splitter:B:44:0x0292] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x02a0 A[SYNTHETIC, Splitter:B:51:0x02a0] */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readSNRLogFile_egis() {
        /*
            r21 = this;
            r1 = r21
            java.lang.String r0 = "FingerPrintTest_egis"
            java.lang.String r2 = "Read SNR log-EGIS"
            android.util.Log.i(r0, r2)
            java.lang.String r2 = "/data/egis_sensor_test_SNR.log"
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            r3 = r0
            boolean r0 = r3.exists()
            if (r0 == 0) goto L_0x02aa
            java.lang.String r0 = "FingerPrintTest_egis"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r2)
            java.lang.String r5 = " file Exist"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r0, r4)
            r4 = 0
            r0 = 0
            r5 = r0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r4 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r6.<init>(r4)     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r0.<init>(r6)     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r5 = r0
            java.lang.String r0 = ""
            java.lang.String r6 = r5.readLine()     // Catch:{ IOException -> 0x0288, all -> 0x0281 }
            r7 = r6
            if (r6 == 0) goto L_0x0279
            java.lang.String r0 = ""
            r6 = r0
            java.lang.String r0 = ""
            r8 = r0
            java.lang.String r0 = ""
            r9 = r0
            java.lang.String r0 = ""
            r10 = r0
            java.lang.String r0 = ""
            r11 = r0
            java.lang.String r0 = ""
            r12 = r0
            java.lang.String r0 = ""
            r13 = r0
            java.lang.String r0 = ""
            r14 = r0
            java.lang.String r0 = ","
            java.lang.String[] r0 = r7.split(r0)     // Catch:{ IndexOutOfBoundsException -> 0x026e }
            boolean r15 = supportNewItems_Egistec     // Catch:{ IndexOutOfBoundsException -> 0x026e }
            r16 = 3
            r17 = 2
            r18 = 0
            r19 = r2
            if (r15 != 0) goto L_0x01a4
            boolean r15 = supportDefectBadPixel     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            if (r15 == 0) goto L_0x007c
            r20 = r3
            goto L_0x01a6
        L_0x007c:
            r15 = r0[r18]     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            java.lang.String r15 = r15.trim()     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            r6 = r15
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r15 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            int r2 = java.lang.Integer.parseInt(r6)     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            r15.addInterruptPin(r2)     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            r15.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x019f, IOException -> 0x019a, all -> 0x0194 }
            r20 = r3
            java.lang.String r3 = "interruptPin : "
            r15.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r15.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 1
            r3 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r8)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addSignal_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "signal :"
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = r0[r17]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r9 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r9)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addNoise_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "noise : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r9)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = r0[r16]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r10)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addSNR_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "snr : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 4
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r11)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addStandardDeviation(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "standard_deviation : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 23
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r12 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            int r3 = java.lang.Integer.parseInt(r12)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2.addOverwriteCalData(r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "overwriteCalData : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r12)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 25
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r13 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            int r3 = java.lang.Integer.parseInt(r13)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2.addCorrectEEPROMver(r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "correctEEPROMver : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r13)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            goto L_0x026b
        L_0x0194:
            r0 = move-exception
            r20 = r3
            r2 = r0
            goto L_0x029e
        L_0x019a:
            r0 = move-exception
            r20 = r3
            goto L_0x028d
        L_0x019f:
            r0 = move-exception
            r20 = r3
            goto L_0x0273
        L_0x01a4:
            r20 = r3
        L_0x01a6:
            r2 = r0[r18]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r6 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            int r3 = java.lang.Integer.parseInt(r6)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2.addInterruptPin(r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "interruptPin : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 21
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r14 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r14)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addPlacement_test(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "placement_test : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r14)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = 1
            r3 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r8)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addSignal_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "signal :"
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = r0[r17]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r9 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r9)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addNoise_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "noise : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r9)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r2 = r0[r16]     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_egis$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            float r3 = java.lang.Float.parseFloat(r10)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r15 = 1
            r2.addSNR_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r2 = "FingerPrintTest_egis"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r15 = "snr : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x026c }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x026c }
        L_0x026b:
            goto L_0x027d
        L_0x026c:
            r0 = move-exception
            goto L_0x0273
        L_0x026e:
            r0 = move-exception
            r19 = r2
            r20 = r3
        L_0x0273:
            r0.printStackTrace()     // Catch:{ IOException -> 0x0277 }
            goto L_0x027d
        L_0x0277:
            r0 = move-exception
            goto L_0x028d
        L_0x0279:
            r19 = r2
            r20 = r3
        L_0x027d:
            r4.close()     // Catch:{ IOException -> 0x0296 }
            goto L_0x0295
        L_0x0281:
            r0 = move-exception
            r19 = r2
            r20 = r3
            r2 = r0
            goto L_0x029e
        L_0x0288:
            r0 = move-exception
            r19 = r2
            r20 = r3
        L_0x028d:
            r0.printStackTrace()     // Catch:{ all -> 0x029c }
            if (r4 == 0) goto L_0x02ae
            r4.close()     // Catch:{ IOException -> 0x0296 }
        L_0x0295:
            goto L_0x02ae
        L_0x0296:
            r0 = move-exception
            r2 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
            goto L_0x0295
        L_0x029c:
            r0 = move-exception
            r2 = r0
        L_0x029e:
            if (r4 == 0) goto L_0x02a9
            r4.close()     // Catch:{ IOException -> 0x02a4 }
            goto L_0x02a9
        L_0x02a4:
            r0 = move-exception
            r3 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x02a9:
            throw r2
        L_0x02aa:
            r19 = r2
            r20 = r3
        L_0x02ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.FingerPrintTest_egis.readSNRLogFile_egis():void");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(CLASS_NAME, "onPause");
        if (this.mFingerprintOptical != null) {
            this.mFingerprintOptical.cancel();
            this.mFingerprintOptical = null;
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
