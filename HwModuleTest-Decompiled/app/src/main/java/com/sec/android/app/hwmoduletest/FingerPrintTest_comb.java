package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.SemRequestCallback;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
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
import com.sec.xmldata.support.Support.Properties;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.Semaphore;

public class FingerPrintTest_comb extends BaseActivity {
    private static final String CLASS_NAME = "FingerPrintTest_comb";
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END = 285212676;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END_GOOGLE = 10009;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START = 285212675;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START_GOOGLE = 10008;
    public static final int NORMAL_SCAN_READ_RETRY_COUNT = 5;
    public static final int OUT_BUFFER_SIZE = 30720;
    public static final int SENSOR_TEST_NORMALSCAN_COMMAND = 100103;
    public static final int SENSOR_TEST_SNR_FINAL_COMMAND_EGIS = 100107;
    public static final int SENSOR_TEST_SNR_ORG_COMMAND_EGIS = 100106;
    /* access modifiers changed from: private */
    public static String mFingerPrint_Vendor;
    /* access modifiers changed from: private */
    public static String scriptID;
    /* access modifiers changed from: private */
    public static boolean supportNewItems_Egistec = false;
    private final int FACTORY_TEST_EVT_SNSR_TEST_PUT = 285212677;
    /* access modifiers changed from: private */
    public int IDENTIFY_FAILURE_SENSOR_CHANGED = 59;
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
            FingerPrintTest_comb.this.tv_msg1.setVisibility(8);
            FingerPrintTest_comb.this.tv_msg2.setVisibility(0);
            FingerPrintTest_comb.this.m_gotermbutton.setVisibility(8);
        }
    };
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    /* access modifiers changed from: private */
    public FingerPrintSNRData mFingerPrintSNRData;
    private FingerprintManager mFingerprintManager;
    private Semaphore mLock = new Semaphore(1);
    private FingerPrintResultView_touch mResultView;
    /* access modifiers changed from: private */
    public FingerPrintResultView_touch mSNRZoneView;
    /* access modifiers changed from: private */
    public Runnable mShowPutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_comb.this.tv_msg1.setText(FingerPrintTest_comb.this.m_PopupMessage);
            FingerPrintTest_comb.this.tv_msg1.setVisibility(0);
            FingerPrintTest_comb.this.tv_msg2.setVisibility(8);
            FingerPrintTest_comb.this.m_gotermbutton.setText("O K");
            FingerPrintTest_comb.this.m_gotermbutton.setVisibility(0);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mShowWarningPopupRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_comb.this.showWarningPopup();
        }
    };
    private String mTest;
    private Thread mThread;
    private IBinder mToken;
    /* access modifiers changed from: private */
    public Runnable mUpdateImageRunnable = new Runnable() {
        public void run() {
            Bitmap bitmap = null;
            if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                bitmap = BitmapFactory.decodeFile("/data/egis_after_rubber.bmp");
                FingerPrintTest_comb.this.mSNRZoneView.setVisibility(4);
            } else if ("SYNAPTICS".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                bitmap = BitmapFactory.decodeFile("/data/SNR.bmp");
            }
            FingerPrintTest_comb.this.iv_snrimage.setVisibility(0);
            FingerPrintTest_comb.this.iv_snrimage.setImageBitmap(bitmap);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateMessageRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_comb.this.tv_msg2.setText(FingerPrintTest_comb.this.m_Message);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateNormalDataGraphRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_comb.this.mFingerPrintNormalData.isReadDone()) {
                FingerPrintTest_comb.this.m_ResultMessage = "Cannot found Data log!!";
                FingerPrintTest_comb.this.tv_result.setVisibility(0);
                FingerPrintTest_comb.this.tv_result.setText(FingerPrintTest_comb.this.m_ResultMessage);
            } else if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                FingerPrintTest_comb.this.updateNormalScanResult_egis();
            } else if ("SYNAPTICS".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                FingerPrintTest_comb.this.updateNormalScanResult_synaptics_new();
            }
        }
    };
    private Runnable mUpdateResultMessageRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_comb.this.m_ResultMessage.isEmpty()) {
                FingerPrintTest_comb.this.tv_result.setVisibility(0);
                FingerPrintTest_comb.this.tv_result.setText(FingerPrintTest_comb.this.m_ResultMessage);
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateSNRDataGraphRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_comb.this.mFingerPrintSNRData.isReadDone()) {
                FingerPrintTest_comb.this.m_ResultMessage = "Cannot found Data log!!";
                FingerPrintTest_comb.this.tv_result.setVisibility(0);
                FingerPrintTest_comb.this.tv_result.setText(FingerPrintTest_comb.this.m_ResultMessage);
            } else if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                FingerPrintTest_comb.this.updateSNRResult_egis();
            } else {
                FingerPrintTest_comb.this.updateSNRResult();
            }
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

    private static class FingerPrintNormalData {
        private static final int ITEM_COUNT_NEW_SYNAPTICS = 15;
        private static final int ITEM_COUNT_NEW_SYNAPTICS_USER = 5;
        private static final int ITEM_COUNT_SYNAPTICS = 4;
        private int ITEM_COUNT_EGIS = 10;
        private int isDone = 0;
        private NormalItem mItemAFE;
        private NormalItem mItemAllowedCounts;
        private NormalItem mItemBadBadPixel;
        private NormalItem mItemBadPixel;
        private NormalItem mItemBlackBadPixel;
        private NormalItem mItemBlackDot;
        private NormalItem mItemCalResult;
        private NormalItem mItemEEPROM;
        private NormalItem mItemFlooredCol;
        private NormalItem mItemFlooredPixel;
        private NormalItem mItemFlooredRow;
        private NormalItem mItemImageDeviation;
        private NormalItem mItemMTPTest;
        private NormalItem mItemMaxNegDelta;
        private NormalItem mItemMaxPosDelta;
        private NormalItem mItemMaximumDelta;
        private NormalItem mItemMinimumDelta;
        private NormalItem mItemPeggedCol;
        private NormalItem mItemPeggedPixel;
        private NormalItem mItemPeggedRow;
        private NormalItem mItemRAM;
        private NormalItem mItemReset;
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
            boolean z = false;
            if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                if (FingerPrintTest_comb.supportNewItems_Egistec) {
                    this.ITEM_COUNT_EGIS = 6;
                }
                if (this.isDone == this.ITEM_COUNT_EGIS) {
                    z = true;
                }
                return z;
            } else if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
                if (this.isDone == 15) {
                    z = true;
                }
                return z;
            } else {
                if (this.isDone == 5) {
                    z = true;
                }
                return z;
            }
        }

        public void addPeggedRow(int value) {
            this.mItemPeggedRow = new NormalItem();
            addItem(this.mItemPeggedRow, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_ROW_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_ROW_MAX))));
            this.isDone++;
        }

        public void addPeggedCol(int value) {
            this.mItemPeggedCol = new NormalItem();
            addItem(this.mItemPeggedCol, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_COL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_COL_MAX))));
            this.isDone++;
        }

        public void addFlooredRow(int value) {
            this.mItemFlooredRow = new NormalItem();
            addItem(this.mItemFlooredRow, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_ROW_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_ROW_MAX))));
            this.isDone++;
        }

        public void addFlooredCol(int value) {
            this.mItemFlooredCol = new NormalItem();
            addItem(this.mItemFlooredCol, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_COL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_COL_MAX))));
            this.isDone++;
        }

        public void addRAM(int value) {
            this.mItemRAM = new NormalItem();
            addItem(this.mItemRAM, value, new Spec<>(Integer.valueOf(0), Integer.valueOf(0)));
            this.isDone++;
        }

        public void addVariance(int value) {
            int mVarianceMax;
            int mVarianceMin;
            this.mItemVariance = new NormalItem();
            if (Integer.parseInt(FingerPrintTest_comb.scriptID) >= 6015) {
                mVarianceMin = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MIN_DC);
                mVarianceMax = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MAX_DC);
            } else {
                mVarianceMin = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MIN);
                mVarianceMax = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MAX);
            }
            addItem(this.mItemVariance, value, new Spec<>(Integer.valueOf(mVarianceMin), Integer.valueOf(mVarianceMax)));
            this.isDone++;
        }

        public void addAFE(int value) {
            this.mItemAFE = new NormalItem();
            addItem(this.mItemAFE, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFE_MAX))));
            this.isDone++;
        }

        public void addAllowedCounts(int value) {
            this.mItemAllowedCounts = new NormalItem();
            addItem(this.mItemAllowedCounts, value, new Spec<>(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_ALLOWED_COUNTS_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_ALLOWED_COUNTS_MAX))));
            this.isDone++;
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

        public String getPeggedRowValue() {
            return this.mItemPeggedRow.getValue();
        }

        public String getPeggedColValue() {
            return this.mItemPeggedCol.getValue();
        }

        public String getFlooredRowValue() {
            return this.mItemFlooredRow.getValue();
        }

        public String getFlooredColValue() {
            return this.mItemFlooredCol.getValue();
        }

        public boolean getPeggedRowPass() {
            return this.mItemPeggedRow.getPass();
        }

        public boolean getPeggedColPass() {
            return this.mItemPeggedCol.getPass();
        }

        public boolean getFlooredRowPass() {
            return this.mItemFlooredRow.getPass();
        }

        public boolean getFlooredColPass() {
            return this.mItemFlooredCol.getPass();
        }

        public Spec<Integer> getPeggedRowSpec() {
            return this.mItemPeggedRow.getSpec();
        }

        public Spec<Integer> getPeggedColSpec() {
            return this.mItemPeggedCol.getSpec();
        }

        public Spec<Integer> getFlooredRowSpec() {
            return this.mItemFlooredRow.getSpec();
        }

        public Spec<Integer> getFlooredColSpec() {
            return this.mItemFlooredCol.getSpec();
        }

        public String getRAMValue() {
            return this.mItemRAM.getValue();
        }

        public String getVarianceValue() {
            return this.mItemVariance.getValue();
        }

        public String getAFEValue() {
            return this.mItemAFE.getValue();
        }

        public String getAllowedCountsValue() {
            return this.mItemAllowedCounts.getValue();
        }

        public boolean getRAMPass() {
            return this.mItemRAM.getPass();
        }

        public boolean getVariancePass() {
            return this.mItemVariance.getPass();
        }

        public boolean getAFEPass() {
            return this.mItemAFE.getPass();
        }

        public boolean getAllowedCountsPass() {
            return this.mItemAllowedCounts.getPass();
        }

        public Spec<Integer> getRAMSpec() {
            return this.mItemRAM.getSpec();
        }

        public Spec<Integer> getVarianceSpec() {
            return this.mItemVariance.getSpec();
        }

        public Spec<Integer> getAFESpec() {
            return this.mItemAFE.getSpec();
        }

        public Spec<Integer> getAllowedCountsSpec() {
            return this.mItemAllowedCounts.getSpec();
        }

        public String getBlackDotValue() {
            return this.mItemBlackDot.getValue();
        }

        public String getWhiteDotValue() {
            return this.mItemWhiteDot.getValue();
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

        public boolean getBlackDotPass() {
            return this.mItemBlackDot.getPass();
        }

        public boolean getWhiteDotPass() {
            return this.mItemWhiteDot.getPass();
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

        public boolean getTotalBadPixelPass() {
            return this.mItemBadBadPixel.getPass();
        }

        public boolean getImageDeviationPass() {
            return this.mItemImageDeviation.getPass();
        }

        public boolean getMTPPass() {
            return this.mItemMTPTest.getPass();
        }

        public Spec<Integer> getBlackDotSpec() {
            return this.mItemBlackDot.getSpec();
        }

        public Spec<Integer> getWhiteDotSpec() {
            return this.mItemWhiteDot.getSpec();
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
            boolean z = false;
            if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                if (FingerPrintTest_comb.supportNewItems_Egistec) {
                    this.ITEM_COUNT_EGIS = 5;
                }
                if (this.isDone == this.ITEM_COUNT_EGIS) {
                    z = true;
                }
                return z;
            }
            if (this.isDone == 11) {
                z = true;
            }
            return z;
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

    private boolean registerClient() {
        this.mFingerprintManager = (FingerprintManager) getSystemService("fingerprint");
        if (this.mFingerprintManager == null) {
            return false;
        }
        return true;
    }

    public void unregisterClient() {
    }

    public FingerPrintTest_comb() {
        super(CLASS_NAME);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3 && this.m_gotermbutton.isShown()) {
            LtUtil.log_d(CLASS_NAME, "onKeyDown()", "Homekey pressed");
            this.m_CurrentOperation = 1018;
            this.handler.post(this.mClosePutFingerDialogRunnable);
            if ("EGISTEC".equals(mFingerPrint_Vendor)) {
                runSensorTest(100107);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void updateNormalScanResult_egis() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 4);
        if (supportNewItems_Egistec) {
            this.mResultView.setRowCol(7, 6);
            this.mResultView.setTextData(0, 0, "Item");
            this.mResultView.setTextData(0, 2, "Spec");
            this.mResultView.setTextData(0, 4, "Result");
            int rowNum = 0 + 1;
            this.mResultView.setTextData(rowNum, 0, "Black dot test");
            this.mResultView.setTextData(rowNum, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().lower);
            sb.append("~");
            sb.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().upper);
            fingerPrintResultView_touch.setTextData(rowNum, 2, sb.toString());
            this.mResultView.setTextData(rowNum, 3, this.mFingerPrintNormalData.getBlackBadPixelValue());
            this.mResultView.setTextData(rowNum, 4, this.mFingerPrintNormalData.getBlackBadPixelPass() ? "PASS" : "FAIL");
            int rowNum2 = rowNum + 1;
            this.mResultView.setTextData(rowNum2, 0, "White dot test");
            this.mResultView.setTextData(rowNum2, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().lower);
            sb2.append("~");
            sb2.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().upper);
            fingerPrintResultView_touch2.setTextData(rowNum2, 2, sb2.toString());
            this.mResultView.setTextData(rowNum2, 3, this.mFingerPrintNormalData.getWhiteBadPixelValue());
            this.mResultView.setTextData(rowNum2, 4, this.mFingerPrintNormalData.getWhiteBadPixelPass() ? "PASS" : "FAIL");
            int rowNum3 = rowNum2 + 1;
            this.mResultView.setTextData(rowNum3, 0, "Bad pixel test");
            this.mResultView.setTextData(rowNum3, 1, "Total bad pixel");
            FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().lower);
            sb3.append("~");
            sb3.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().upper);
            fingerPrintResultView_touch3.setTextData(rowNum3, 2, sb3.toString());
            this.mResultView.setTextData(rowNum3, 3, this.mFingerPrintNormalData.getTotalBadPixelValue());
            this.mResultView.setTextData(rowNum3, 4, this.mFingerPrintNormalData.getTotalBadPixelPass() ? "PASS" : "FAIL");
            int rowNum4 = rowNum3 + 1;
            this.mResultView.setTextData(rowNum4, 0, "Reset test");
            FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(this.mFingerPrintNormalData.getResetSpec()[0]);
            fingerPrintResultView_touch4.setTextData(rowNum4, 2, sb4.toString());
            this.mResultView.setTextData(rowNum4, 3, this.mFingerPrintNormalData.getResetValue());
            this.mResultView.setTextData(rowNum4, 4, this.mFingerPrintNormalData.getResetPass() ? "PASS" : "FAIL");
            int rowNum5 = rowNum4 + 1;
            this.mResultView.setTextData(rowNum5, 0, "MTP test");
            FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(this.mFingerPrintNormalData.getMTPSpec()[0]);
            fingerPrintResultView_touch5.setTextData(rowNum5, 2, sb5.toString());
            this.mResultView.setTextData(rowNum5, 3, this.mFingerPrintNormalData.getMTPValue());
            this.mResultView.setTextData(rowNum5, 4, this.mFingerPrintNormalData.getMTPPass() ? "PASS" : "FAIL");
            int rowNum6 = rowNum5 + 1;
            this.mResultView.setTextData(rowNum6, 0, "Cal Result");
            FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("");
            sb6.append(this.mFingerPrintNormalData.getCalResultSpec()[0]);
            fingerPrintResultView_touch6.setTextData(rowNum6, 2, sb6.toString());
            this.mResultView.setTextData(rowNum6, 3, this.mFingerPrintNormalData.getCalResultValue());
            this.mResultView.setTextData(rowNum6, 4, this.mFingerPrintNormalData.getCalResultPass() ? "PASS" : "FAIL");
            int rowNum7 = rowNum6 + 1;
            return;
        }
        this.mResultView.setRowCol(11, 6);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 3, "Spec");
        this.mResultView.setTextData(0, 4, "Result");
        int rowNum8 = 0 + 1;
        this.mResultView.setTextData(rowNum8, 0, "NS");
        this.mResultView.setTextData(rowNum8, 1, "Black dot test");
        this.mResultView.setTextData(rowNum8, 2, "Total bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintNormalData.getBlackBadPixelSpec().upper);
        fingerPrintResultView_touch7.setTextData(rowNum8, 3, sb7.toString());
        this.mResultView.setTextData(rowNum8, 4, this.mFingerPrintNormalData.getBlackBadPixelValue());
        this.mResultView.setTextData(rowNum8, 5, this.mFingerPrintNormalData.getBlackBadPixelPass() ? "PASS" : "FAIL");
        int rowNum9 = rowNum8 + 1;
        this.mResultView.setTextData(rowNum9, 2, "Cont. bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mFingerPrintNormalData.getBlackDotSpec().lower);
        sb8.append("~");
        sb8.append(this.mFingerPrintNormalData.getBlackDotSpec().upper);
        fingerPrintResultView_touch8.setTextData(rowNum9, 3, sb8.toString());
        this.mResultView.setTextData(rowNum9, 4, this.mFingerPrintNormalData.getBlackDotValue());
        this.mResultView.setTextData(rowNum9, 5, this.mFingerPrintNormalData.getBlackDotPass() ? "PASS" : "FAIL");
        int rowNum10 = rowNum9 + 1;
        this.mResultView.setTextData(rowNum10, 1, "White dot test");
        this.mResultView.setTextData(rowNum10, 2, "Total bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch9 = this.mResultView;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().lower);
        sb9.append("~");
        sb9.append(this.mFingerPrintNormalData.getWhiteBadPixelSpec().upper);
        fingerPrintResultView_touch9.setTextData(rowNum10, 3, sb9.toString());
        this.mResultView.setTextData(rowNum10, 4, this.mFingerPrintNormalData.getWhiteBadPixelValue());
        this.mResultView.setTextData(rowNum10, 5, this.mFingerPrintNormalData.getWhiteBadPixelPass() ? "PASS" : "FAIL");
        int rowNum11 = rowNum10 + 1;
        this.mResultView.setTextData(rowNum11, 2, "Cont. bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch10 = this.mResultView;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(this.mFingerPrintNormalData.getWhiteDotSpec().lower);
        sb10.append("~");
        sb10.append(this.mFingerPrintNormalData.getWhiteDotSpec().upper);
        fingerPrintResultView_touch10.setTextData(rowNum11, 3, sb10.toString());
        this.mResultView.setTextData(rowNum11, 4, this.mFingerPrintNormalData.getWhiteDotValue());
        this.mResultView.setTextData(rowNum11, 5, this.mFingerPrintNormalData.getWhiteDotPass() ? "PASS" : "FAIL");
        int rowNum12 = rowNum11 + 1;
        this.mResultView.setTextData(rowNum12, 1, "Bad pixel test");
        this.mResultView.setTextData(rowNum12, 2, "Total bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch11 = this.mResultView;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().lower);
        sb11.append("~");
        sb11.append(this.mFingerPrintNormalData.getTotalBadPixelSpec().upper);
        fingerPrintResultView_touch11.setTextData(rowNum12, 3, sb11.toString());
        this.mResultView.setTextData(rowNum12, 4, this.mFingerPrintNormalData.getTotalBadPixelValue());
        this.mResultView.setTextData(rowNum12, 5, this.mFingerPrintNormalData.getTotalBadPixelPass() ? "PASS" : "FAIL");
        int rowNum13 = rowNum12 + 1;
        this.mResultView.setTextData(rowNum13, 2, "Cont. bad pixel");
        FingerPrintResultView_touch fingerPrintResultView_touch12 = this.mResultView;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(this.mFingerPrintNormalData.getBadPixelSpec().lower);
        sb12.append("~");
        sb12.append(this.mFingerPrintNormalData.getBadPixelSpec().upper);
        fingerPrintResultView_touch12.setTextData(rowNum13, 3, sb12.toString());
        this.mResultView.setTextData(rowNum13, 4, this.mFingerPrintNormalData.getBadPixelValue());
        this.mResultView.setTextData(rowNum13, 5, this.mFingerPrintNormalData.getBadPixelPass() ? "PASS" : "FAIL");
        int rowNum14 = rowNum13 + 1;
        this.mResultView.setTextData(rowNum14, 1, "Reset test");
        FingerPrintResultView_touch fingerPrintResultView_touch13 = this.mResultView;
        StringBuilder sb13 = new StringBuilder();
        sb13.append("");
        sb13.append(this.mFingerPrintNormalData.getResetSpec()[0]);
        fingerPrintResultView_touch13.setTextData(rowNum14, 3, sb13.toString());
        this.mResultView.setTextData(rowNum14, 4, this.mFingerPrintNormalData.getResetValue());
        this.mResultView.setTextData(rowNum14, 5, this.mFingerPrintNormalData.getResetPass() ? "PASS" : "FAIL");
        int rowNum15 = rowNum14 + 1;
        this.mResultView.setTextData(rowNum15, 1, "EEPROM test");
        FingerPrintResultView_touch fingerPrintResultView_touch14 = this.mResultView;
        StringBuilder sb14 = new StringBuilder();
        sb14.append("");
        sb14.append(this.mFingerPrintNormalData.getEEPROMSpec()[0]);
        fingerPrintResultView_touch14.setTextData(rowNum15, 3, sb14.toString());
        this.mResultView.setTextData(rowNum15, 4, this.mFingerPrintNormalData.getEEPROMValue());
        this.mResultView.setTextData(rowNum15, 5, this.mFingerPrintNormalData.getEEPROMPass() ? "PASS" : "FAIL");
        int rowNum16 = rowNum15 + 1;
        this.mResultView.setTextData(rowNum16, 1, "Cal Result");
        FingerPrintResultView_touch fingerPrintResultView_touch15 = this.mResultView;
        StringBuilder sb15 = new StringBuilder();
        sb15.append("");
        sb15.append(this.mFingerPrintNormalData.getCalResultSpec()[0]);
        fingerPrintResultView_touch15.setTextData(rowNum16, 3, sb15.toString());
        this.mResultView.setTextData(rowNum16, 4, this.mFingerPrintNormalData.getCalResultValue());
        this.mResultView.setTextData(rowNum16, 5, this.mFingerPrintNormalData.getCalResultPass() ? "PASS" : "FAIL");
        int rowNum17 = rowNum16 + 1;
        this.mResultView.setTextData(rowNum17, 1, "Image Deviation");
        FingerPrintResultView_touch fingerPrintResultView_touch16 = this.mResultView;
        StringBuilder sb16 = new StringBuilder();
        sb16.append(this.mFingerPrintNormalData.getImageDeviationSpec().lower);
        sb16.append("~");
        sb16.append(this.mFingerPrintNormalData.getImageDeviationSpec().upper);
        fingerPrintResultView_touch16.setTextData(rowNum17, 3, sb16.toString());
        this.mResultView.setTextData(rowNum17, 4, this.mFingerPrintNormalData.getImageDeviationValue());
        this.mResultView.setTextData(rowNum17, 5, this.mFingerPrintNormalData.getImageDeviationPass() ? "PASS" : "FAIL");
        int rowNum18 = rowNum17 + 1;
    }

    /* access modifiers changed from: private */
    public void updateSNRResult_egis() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 4);
        if (supportNewItems_Egistec) {
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

    /* access modifiers changed from: private */
    public void updateNormalScanResult_synaptics_new() {
        int row_limit;
        int row_next;
        if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
            row_limit = 9;
        } else {
            row_limit = 6;
        }
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 3);
        this.mResultView.setRowCol(row_limit, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        int row_next2 = 0 + 1;
        this.mResultView.setTextData(0, 3, "Result");
        if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
            this.mResultView.setTextData(row_next2, 0, "RAM Test");
            FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mFingerPrintNormalData.getRAMSpec().lower);
            sb.append("");
            fingerPrintResultView_touch.setTextData(row_next2, 2, sb.toString());
            this.mResultView.setTextData(row_next2, 3, this.mFingerPrintNormalData.getRAMValue());
            int row_next3 = row_next2 + 1;
            this.mResultView.setTextData(row_next2, 4, this.mFingerPrintNormalData.getRAMPass() ? "PASS" : "FAIL");
            row_next2 = row_next3;
        }
        this.mResultView.setTextData(row_next2, 0, "Variance Test");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintNormalData.getVarianceSpec().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintNormalData.getVarianceSpec().upper);
        fingerPrintResultView_touch2.setTextData(row_next2, 2, sb2.toString());
        this.mResultView.setTextData(row_next2, 3, this.mFingerPrintNormalData.getVarianceValue());
        int row_next4 = row_next2 + 1;
        this.mResultView.setTextData(row_next2, 4, this.mFingerPrintNormalData.getVariancePass() ? "PASS" : "FAIL");
        if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
            this.mResultView.setTextData(row_next4, 0, "AFE Balance Test");
            FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mFingerPrintNormalData.getAFESpec().lower);
            sb3.append("~");
            sb3.append(this.mFingerPrintNormalData.getAFESpec().upper);
            fingerPrintResultView_touch3.setTextData(row_next4, 2, sb3.toString());
            this.mResultView.setTextData(row_next4, 3, this.mFingerPrintNormalData.getAFEValue());
            row_next = row_next4 + 1;
            this.mResultView.setTextData(row_next4, 4, this.mFingerPrintNormalData.getAFEPass() ? "PASS" : "FAIL");
        } else {
            row_next = row_next4;
        }
        this.mResultView.setTextData(row_next, 0, "PeggedPxl");
        this.mResultView.setTextData(row_next, 1, "Row");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintNormalData.getPeggedRowSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintNormalData.getPeggedRowSpec().upper);
        fingerPrintResultView_touch4.setTextData(row_next, 2, sb4.toString());
        this.mResultView.setTextData(row_next, 3, this.mFingerPrintNormalData.getPeggedRowValue());
        int row_next5 = row_next + 1;
        this.mResultView.setTextData(row_next, 4, this.mFingerPrintNormalData.getPeggedRowPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next5, 1, "Col");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintNormalData.getPeggedColSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintNormalData.getPeggedColSpec().upper);
        fingerPrintResultView_touch5.setTextData(row_next5, 2, sb5.toString());
        this.mResultView.setTextData(row_next5, 3, this.mFingerPrintNormalData.getPeggedColValue());
        int row_next6 = row_next5 + 1;
        this.mResultView.setTextData(row_next5, 4, this.mFingerPrintNormalData.getPeggedColPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next6, 0, "FlooredPxl");
        this.mResultView.setTextData(row_next6, 1, "Row");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(this.mFingerPrintNormalData.getFlooredRowSpec().lower);
        sb6.append("~");
        sb6.append(this.mFingerPrintNormalData.getFlooredRowSpec().upper);
        fingerPrintResultView_touch6.setTextData(row_next6, 2, sb6.toString());
        this.mResultView.setTextData(row_next6, 3, this.mFingerPrintNormalData.getFlooredRowValue());
        int row_next7 = row_next6 + 1;
        this.mResultView.setTextData(row_next6, 4, this.mFingerPrintNormalData.getFlooredRowPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next7, 1, "Col");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintNormalData.getFlooredColSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintNormalData.getFlooredColSpec().upper);
        fingerPrintResultView_touch7.setTextData(row_next7, 2, sb7.toString());
        this.mResultView.setTextData(row_next7, 3, this.mFingerPrintNormalData.getFlooredColValue());
        int row_next8 = row_next7 + 1;
        this.mResultView.setTextData(row_next7, 4, this.mFingerPrintNormalData.getFlooredColPass() ? "PASS" : "FAIL");
        if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
            this.mResultView.setTextData(row_next8, 0, "Pxl Unif(A)");
            this.mResultView.setTextData(row_next8, 1, "Counts");
            FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(this.mFingerPrintNormalData.getAllowedCountsSpec().lower);
            sb8.append("~");
            sb8.append(this.mFingerPrintNormalData.getAllowedCountsSpec().upper);
            fingerPrintResultView_touch8.setTextData(row_next8, 2, sb8.toString());
            this.mResultView.setTextData(row_next8, 3, this.mFingerPrintNormalData.getAllowedCountsValue());
            int i = row_next8 + 1;
            this.mResultView.setTextData(row_next8, 4, this.mFingerPrintNormalData.getAllowedCountsPass() ? "PASS" : "FAIL");
            return;
        }
    }

    /* access modifiers changed from: private */
    public void updateSNRResult() {
        this.mSNRZoneView.setVisibility(0);
        this.mSNRZoneView.setCanvasSize(this.m_ScreenWidth / 2, this.m_ScreenHeight / 4);
        this.mSNRZoneView.setTextSize(this.m_ScreenWidth / 32);
        this.mSNRZoneView.setRowCol(2, 3);
        for (int i = 0; i < 6; i++) {
            this.mSNRZoneView.setTextData(i / 3, i % 3, this.mFingerPrintSNRData.getSNRZoneValue(i));
        }
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 4);
        this.mResultView.setRowCol(6, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(1, 0, "Delta Pixel(Max)");
        this.mResultView.setTextData(1, 1, "Row");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintSNRData.getDeltaPixelRowSpec().lower);
        sb.append("~");
        sb.append(this.mFingerPrintSNRData.getDeltaPixelRowSpec().upper);
        fingerPrintResultView_touch.setTextData(1, 2, sb.toString());
        this.mResultView.setTextData(1, 3, this.mFingerPrintSNRData.getDeltaPixelRowValue());
        this.mResultView.setTextData(1, 4, this.mFingerPrintSNRData.getDeltaPixelRowPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(2, 1, "Col");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintSNRData.getDeltaPixelColSpec().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintSNRData.getDeltaPixelColSpec().upper);
        fingerPrintResultView_touch2.setTextData(2, 2, sb2.toString());
        this.mResultView.setTextData(2, 3, this.mFingerPrintSNRData.getDeltaPixelColValue());
        this.mResultView.setTextData(2, 4, this.mFingerPrintSNRData.getDeltaPixelColPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(3, 0, "Signal");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintSNRData.getSignalSpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintSNRData.getSignalSpec().upper);
        fingerPrintResultView_touch3.setTextData(3, 2, sb3.toString());
        this.mResultView.setTextData(3, 3, this.mFingerPrintSNRData.getSignalValue());
        this.mResultView.setTextData(3, 4, this.mFingerPrintSNRData.getSignalPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(4, 0, "Noise");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().upper);
        fingerPrintResultView_touch4.setTextData(4, 2, sb4.toString());
        this.mResultView.setTextData(4, 3, this.mFingerPrintSNRData.getNoiseValue());
        this.mResultView.setTextData(4, 4, this.mFingerPrintSNRData.getNoisePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(5, 0, "SNR");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintSNRData.getSNRSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintSNRData.getSNRSpec().upper);
        fingerPrintResultView_touch5.setTextData(5, 2, sb5.toString());
        this.mResultView.setTextData(5, 3, this.mFingerPrintSNRData.getSNRValue());
        this.mResultView.setTextData(5, 4, this.mFingerPrintSNRData.getSNRPass() ? "PASS" : "FAIL");
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
                FingerPrintTest_comb.this.m_CurrentOperation = 1018;
                FingerPrintTest_comb.this.handler.post(FingerPrintTest_comb.this.mClosePutFingerDialogRunnable);
                if ("EGISTEC".equals(FingerPrintTest_comb.mFingerPrint_Vendor)) {
                    FingerPrintTest_comb.this.runSensorTest(100107);
                }
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
        if (!registerClient()) {
            LtUtil.log_d(CLASS_NAME, "onResume", "sensorStatus has been failed ");
            this.m_ResultMessage = "Fingerprint initializing Fail";
            this.handler.post(this.mUpdateResultMessageRunnable);
        } else if ("NORMALDATA".equals(this.mTest)) {
            this.m_CurrentOperation = 1014;
            this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
            this.handler.post(this.mUpdateMessageRunnable);
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    int result = FingerPrintTest_comb.this.runSensorTest(100103);
                    if (result == FingerPrintTest_comb.this.IDENTIFY_FAILURE_SENSOR_CHANGED) {
                        FingerPrintTest_comb.this.STATE_REPLACEMENT_WARNING = true;
                        LtUtil.log_d(FingerPrintTest_comb.CLASS_NAME, "onResume", "STATE_REPLACEMENT_WARNING = true ");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("SENSOR_TEST_NORMALSCAN_COMMAND Success / result : ");
                    sb.append(result);
                    LtUtil.log_d(FingerPrintTest_comb.CLASS_NAME, "onResume", sb.toString());
                }
            }, 500);
        } else if ("SNR".equals(this.mTest)) {
            this.m_CurrentOperation = 1016;
            if ("EGISTEC".equals(mFingerPrint_Vendor)) {
                runSensorTest(100106);
            }
            LtUtil.log_d(CLASS_NAME, "onResume", "SENSOR_TEST_SNR_ORG_COMMAND Success ");
        } else if ("SENSORINFO".equals(this.mTest)) {
            this.m_Message = "Sensor Infomation";
            this.m_ResultMessage = this.mFingerprintManager.semGetSensorInfo();
            StringBuilder sb = new StringBuilder();
            sb.append("semGetSensorInfo : ");
            sb.append(this.m_ResultMessage);
            LtUtil.log_d(CLASS_NAME, "onResume", sb.toString());
            this.handler.post(this.mUpdateMessageRunnable);
            this.handler.post(this.mUpdateResultMessageRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void readNormalScanLog_egis() {
        String MTPTest;
        Log.i(CLASS_NAME, "Test : read Normal scan log-EGIS");
        String path = "/data/egis_sensor_test_NS.log";
        if (Integer.parseInt(this.os_version_array[1]) >= 6 && !FactoryTest.isFactoryBinary()) {
            path = "/data/fpSnrTest/egis_sensor_test_NS.log";
        }
        String path2 = path;
        File lfile = new File(path2);
        String readStr = "";
        if (lfile.exists()) {
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(path2);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis2));
                String readLine = bufferReader.readLine();
                readStr = readLine;
                if (readLine != null) {
                    String str = CLASS_NAME;
                    StringBuilder sb = new StringBuilder();
                    sb.append(" normal result : ");
                    sb.append(readStr);
                    Log.i(str, sb.toString());
                }
                try {
                    fis2.close();
                } catch (IOException e) {
                    IOException iOException = e;
                    LtUtil.log_e(e);
                }
                try {
                    bufferReader.close();
                } catch (IOException e2) {
                    IOException iOException2 = e2;
                    LtUtil.log_e(e2);
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e4) {
                        IOException iOException3 = e4;
                        LtUtil.log_e(e4);
                    }
                }
                if (0 != 0) {
                    null.close();
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e5) {
                        IOException iOException4 = e5;
                        LtUtil.log_e(e5);
                    }
                }
                if (0 != 0) {
                    try {
                        null.close();
                    } catch (IOException e6) {
                        IOException iOException5 = e6;
                        LtUtil.log_e(e6);
                    }
                }
                throw th2;
            }
        } else {
            byte[] outbuffer = new byte[30720];
            int result = this.mFingerprintManager.semGetSensorTestResult(outbuffer);
            if (result == -1) {
                LtUtil.log_d(CLASS_NAME, "readNormalScanLog", "error, semGetSensorTestResult -1");
                return;
            }
            String readStr2 = new String(outbuffer);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("result(out length) : ");
            sb2.append(result);
            LtUtil.log_d(CLASS_NAME, "readNormalScanLog", sb2.toString());
            readStr = readStr2.trim();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("normal result : ");
            sb3.append(readStr);
            LtUtil.log_d(CLASS_NAME, "readNormalScanLog", sb3.toString());
        }
        if (!TextUtils.isEmpty(readStr)) {
            String str2 = "";
            String str3 = "";
            String str4 = "";
            String str5 = "";
            String str6 = "";
            String str7 = "";
            String str8 = "";
            String str9 = "";
            String str10 = "";
            String str11 = "";
            String str12 = "";
            try {
                String[] splitStr = readStr.split(",");
                if (supportNewItems_Egistec) {
                    String blackBadPixel = splitStr[0].trim();
                    this.mFingerPrintNormalData.addBlackBadPixel(Integer.parseInt(blackBadPixel));
                    String str13 = CLASS_NAME;
                    StringBuilder sb4 = new StringBuilder();
                    String str14 = path2;
                    try {
                        sb4.append("blackBadPixel : ");
                        sb4.append(blackBadPixel);
                        Log.d(str13, sb4.toString());
                        String whiteBadPixel = splitStr[2].trim();
                        this.mFingerPrintNormalData.addWhiteBadPixel(Integer.parseInt(whiteBadPixel));
                        String str15 = CLASS_NAME;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("whiteBadPixel : ");
                        sb5.append(whiteBadPixel);
                        Log.d(str15, sb5.toString());
                        String totalBadPixel = splitStr[4].trim();
                        this.mFingerPrintNormalData.addBadBadPixel(Integer.parseInt(totalBadPixel));
                        String str16 = CLASS_NAME;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("totalBadPixel : ");
                        sb6.append(totalBadPixel);
                        Log.d(str16, sb6.toString());
                        String resetTest = splitStr[6].trim();
                        this.mFingerPrintNormalData.addResetTest(Integer.parseInt(resetTest));
                        String str17 = CLASS_NAME;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("resetTest : ");
                        sb7.append(resetTest);
                        Log.d(str17, sb7.toString());
                        MTPTest = splitStr[7].trim();
                    } catch (IndexOutOfBoundsException e7) {
                        e = e7;
                        File file = lfile;
                        e.printStackTrace();
                    }
                    try {
                        this.mFingerPrintNormalData.addMTPTest(Integer.parseInt(MTPTest));
                        String str18 = CLASS_NAME;
                        StringBuilder sb8 = new StringBuilder();
                        File file2 = lfile;
                        try {
                            sb8.append("MTPTest : ");
                            sb8.append(MTPTest);
                            Log.d(str18, sb8.toString());
                            String calResult = splitStr[8].trim();
                            this.mFingerPrintNormalData.addCalResult(Integer.parseInt(calResult));
                            String str19 = CLASS_NAME;
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append("calResult : ");
                            sb9.append(calResult);
                            Log.d(str19, sb9.toString());
                            String str20 = MTPTest;
                        } catch (IndexOutOfBoundsException e8) {
                            e = e8;
                            String str21 = MTPTest;
                            e.printStackTrace();
                        }
                    } catch (IndexOutOfBoundsException e9) {
                        e = e9;
                        File file3 = lfile;
                        String str22 = MTPTest;
                        e.printStackTrace();
                    }
                } else {
                    File file4 = lfile;
                    try {
                        String blackBadPixel2 = splitStr[0].trim();
                        this.mFingerPrintNormalData.addBlackBadPixel(Integer.parseInt(blackBadPixel2));
                        String str23 = CLASS_NAME;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("blackBadPixel : ");
                        sb10.append(blackBadPixel2);
                        Log.d(str23, sb10.toString());
                        String blackDot = splitStr[1].trim();
                        this.mFingerPrintNormalData.addBlackDot(Integer.parseInt(blackDot));
                        String str24 = CLASS_NAME;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("blackDot : ");
                        sb11.append(blackDot);
                        Log.d(str24, sb11.toString());
                        String whiteBadPixel2 = splitStr[3].trim();
                        this.mFingerPrintNormalData.addWhiteBadPixel(Integer.parseInt(whiteBadPixel2));
                        String str25 = CLASS_NAME;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("whiteBadPixel : ");
                        sb12.append(whiteBadPixel2);
                        Log.d(str25, sb12.toString());
                        String whiteDot = splitStr[4].trim();
                        this.mFingerPrintNormalData.addWhiteDot(Integer.parseInt(whiteDot));
                        String str26 = CLASS_NAME;
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("whiteDot : ");
                        sb13.append(whiteDot);
                        Log.d(str26, sb13.toString());
                        String totalBadPixel2 = splitStr[6].trim();
                        this.mFingerPrintNormalData.addBadBadPixel(Integer.parseInt(totalBadPixel2));
                        String str27 = CLASS_NAME;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("totalBadPixel : ");
                        sb14.append(totalBadPixel2);
                        Log.d(str27, sb14.toString());
                        String BadPixel = splitStr[7].trim();
                        this.mFingerPrintNormalData.addBadPixel(Integer.parseInt(BadPixel));
                        String str28 = CLASS_NAME;
                        StringBuilder sb15 = new StringBuilder();
                        sb15.append("BadPixel : ");
                        sb15.append(BadPixel);
                        Log.d(str28, sb15.toString());
                        String resetTest2 = splitStr[9].trim();
                        this.mFingerPrintNormalData.addResetTest(Integer.parseInt(resetTest2));
                        String str29 = CLASS_NAME;
                        StringBuilder sb16 = new StringBuilder();
                        sb16.append("resetTest : ");
                        sb16.append(resetTest2);
                        Log.d(str29, sb16.toString());
                        String eepromTest = splitStr[10].trim();
                        this.mFingerPrintNormalData.addEEPROMTest(Integer.parseInt(eepromTest));
                        String str30 = CLASS_NAME;
                        StringBuilder sb17 = new StringBuilder();
                        sb17.append("eepromTest : ");
                        sb17.append(eepromTest);
                        Log.d(str30, sb17.toString());
                        String calResult2 = splitStr[11].trim();
                        this.mFingerPrintNormalData.addCalResult(Integer.parseInt(calResult2));
                        String str31 = CLASS_NAME;
                        StringBuilder sb18 = new StringBuilder();
                        sb18.append("calResult : ");
                        sb18.append(calResult2);
                        Log.d(str31, sb18.toString());
                        String imageDeviation = splitStr[12].trim();
                        try {
                            this.mFingerPrintNormalData.addImageDeviation((int) Float.parseFloat(imageDeviation));
                            String str32 = CLASS_NAME;
                            StringBuilder sb19 = new StringBuilder();
                            sb19.append("imageDeviation : ");
                            sb19.append(imageDeviation);
                            Log.d(str32, sb19.toString());
                            String str33 = imageDeviation;
                        } catch (IndexOutOfBoundsException e10) {
                            e = e10;
                            String str34 = imageDeviation;
                            e.printStackTrace();
                        }
                    } catch (IndexOutOfBoundsException e11) {
                        e = e11;
                        e.printStackTrace();
                    }
                }
            } catch (IndexOutOfBoundsException e12) {
                e = e12;
                String str35 = path2;
                File file5 = lfile;
                e.printStackTrace();
            }
        } else {
            File file6 = lfile;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x028a A[SYNTHETIC, Splitter:B:40:0x028a] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0298 A[SYNTHETIC, Splitter:B:47:0x0298] */
    /* JADX WARNING: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readSNRLogFile_egis() {
        /*
            r21 = this;
            r1 = r21
            java.lang.String r0 = "FingerPrintTest_comb"
            java.lang.String r2 = "Read SNR log-EGIS"
            android.util.Log.i(r0, r2)
            java.lang.String r2 = "/data/egis_sensor_test_SNR.log"
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            r3 = r0
            boolean r0 = r3.exists()
            if (r0 == 0) goto L_0x02a2
            java.lang.String r0 = "FingerPrintTest_comb"
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
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r4 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r6.<init>(r4)     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r0.<init>(r6)     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r5 = r0
            java.lang.String r0 = ""
            java.lang.String r6 = r5.readLine()     // Catch:{ IOException -> 0x0280, all -> 0x0279 }
            r7 = r6
            if (r6 == 0) goto L_0x0271
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
            java.lang.String[] r0 = r7.split(r0)     // Catch:{ IndexOutOfBoundsException -> 0x0266 }
            boolean r15 = supportNewItems_Egistec     // Catch:{ IndexOutOfBoundsException -> 0x0266 }
            r16 = 3
            r17 = 2
            r18 = 0
            r19 = r2
            if (r15 == 0) goto L_0x014d
            r15 = r0[r18]     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            java.lang.String r15 = r15.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            r6 = r15
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r15 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            int r2 = java.lang.Integer.parseInt(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            r15.addInterruptPin(r2)     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            r15.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0148, IOException -> 0x0143, all -> 0x013d }
            r20 = r3
            java.lang.String r3 = "interruptPin : "
            r15.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r15.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 21
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r14 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r14)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addPlacement_test(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "placement_test : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r14)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 1
            r3 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r8)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addSignal_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "signal :"
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = r0[r17]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r9 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r9)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addNoise_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "noise : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r9)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = r0[r16]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r10)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addSNR_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "snr : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            goto L_0x0263
        L_0x013d:
            r0 = move-exception
            r20 = r3
            r2 = r0
            goto L_0x0296
        L_0x0143:
            r0 = move-exception
            r20 = r3
            goto L_0x0285
        L_0x0148:
            r0 = move-exception
            r20 = r3
            goto L_0x026b
        L_0x014d:
            r20 = r3
            r2 = r0[r18]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r6 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            int r3 = java.lang.Integer.parseInt(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2.addInterruptPin(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "interruptPin : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 1
            r3 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r8)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addSignal_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "signal :"
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = r0[r17]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r9 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r9)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addNoise_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "noise : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r9)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = r0[r16]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r10)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addSNR_Egis(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "snr : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 4
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            float r3 = java.lang.Float.parseFloat(r11)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r15 = 1
            r2.addStandardDeviation(r3, r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "standard_deviation : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 23
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r12 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            int r3 = java.lang.Integer.parseInt(r12)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2.addOverwriteCalData(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "overwriteCalData : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r12)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2 = 25
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r13 = r2
            com.sec.android.app.hwmoduletest.FingerPrintTest_comb$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            int r3 = java.lang.Integer.parseInt(r13)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r2.addCorrectEEPROMver(r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r2 = "FingerPrintTest_comb"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r15 = "correctEEPROMver : "
            r3.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            r3.append(r13)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            java.lang.String r3 = r3.toString()     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
            android.util.Log.d(r2, r3)     // Catch:{ IndexOutOfBoundsException -> 0x0264 }
        L_0x0263:
            goto L_0x0275
        L_0x0264:
            r0 = move-exception
            goto L_0x026b
        L_0x0266:
            r0 = move-exception
            r19 = r2
            r20 = r3
        L_0x026b:
            r0.printStackTrace()     // Catch:{ IOException -> 0x026f }
            goto L_0x0275
        L_0x026f:
            r0 = move-exception
            goto L_0x0285
        L_0x0271:
            r19 = r2
            r20 = r3
        L_0x0275:
            r4.close()     // Catch:{ IOException -> 0x028e }
            goto L_0x028d
        L_0x0279:
            r0 = move-exception
            r19 = r2
            r20 = r3
            r2 = r0
            goto L_0x0296
        L_0x0280:
            r0 = move-exception
            r19 = r2
            r20 = r3
        L_0x0285:
            r0.printStackTrace()     // Catch:{ all -> 0x0294 }
            if (r4 == 0) goto L_0x02a6
            r4.close()     // Catch:{ IOException -> 0x028e }
        L_0x028d:
            goto L_0x02a6
        L_0x028e:
            r0 = move-exception
            r2 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
            goto L_0x028d
        L_0x0294:
            r0 = move-exception
            r2 = r0
        L_0x0296:
            if (r4 == 0) goto L_0x02a1
            r4.close()     // Catch:{ IOException -> 0x029c }
            goto L_0x02a1
        L_0x029c:
            r0 = move-exception
            r3 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x02a1:
            throw r2
        L_0x02a2:
            r19 = r2
            r20 = r3
        L_0x02a6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.FingerPrintTest_comb.readSNRLogFile_egis():void");
    }

    /* access modifiers changed from: private */
    public void readNormalScanLog_synaptics() {
        String path = "/data/SynSnsrTest.log";
        if (Integer.parseInt(VERSION.RELEASE.replace("ver:", "").trim().split("")[1]) >= 6 && !FactoryTest.isFactoryBinary()) {
            path = "/data/fpSnrTest/SynSnsrTest.log";
        }
        FileInputStream fis = null;
        BufferedReader bufferReader = null;
        InputStream is = null;
        try {
            if (new File(path).exists()) {
                String str = CLASS_NAME;
                StringBuilder sb = new StringBuilder();
                sb.append(path);
                sb.append(" file Exist");
                Log.i(str, sb.toString());
                fis = new FileInputStream(path);
                bufferReader = new BufferedReader(new InputStreamReader(fis));
            } else {
                byte[] outbuffer = new byte[30720];
                String str2 = "";
                int result = this.mFingerprintManager.semGetSensorTestResult(outbuffer);
                if (result == -1) {
                    LtUtil.log_d(CLASS_NAME, "readNormalScanLog", "error, semGetSensorTestResult -1");
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            LtUtil.log_e(e);
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                            LtUtil.log_e(e2);
                        }
                    }
                    if (bufferReader != null) {
                        try {
                            bufferReader.close();
                        } catch (IOException e3) {
                            LtUtil.log_e(e3);
                        }
                    }
                    return;
                }
                String readStr = new String(outbuffer);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("result(out length) : ");
                sb2.append(result);
                LtUtil.log_d(CLASS_NAME, "readNormalScanLog", sb2.toString());
                String readStr2 = readStr.trim();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("normal result : ");
                sb3.append(readStr2);
                LtUtil.log_d(CLASS_NAME, "readNormalScanLog", sb3.toString());
                is = new ByteArrayInputStream(readStr2.getBytes());
                bufferReader = new BufferedReader(new InputStreamReader(is));
            }
            String str3 = "";
            while (true) {
                String readLine = bufferReader.readLine();
                String readStr3 = readLine;
                if (readLine == null) {
                    break;
                }
                String address = "";
                String data = "";
                String str4 = "";
                try {
                    String[] splitStr = readStr3.split(",");
                    address = splitStr[0].trim();
                    data = splitStr[1].trim();
                    String pass = splitStr[2].trim();
                } catch (IndexOutOfBoundsException e4) {
                    e4.printStackTrace();
                }
                if ("Script Type".equals(address)) {
                    scriptID = data;
                    String str5 = CLASS_NAME;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("readNormalScanLog_synaptics : scriptID(");
                    sb4.append(scriptID);
                    sb4.append(")");
                    Log.d(str5, sb4.toString());
                } else if ("factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
                    if ("9030".equals(address)) {
                        this.mFingerPrintNormalData.addRAM(Integer.parseInt(data));
                        Log.d(CLASS_NAME, "readNormalScanLog_synaptics : RAM");
                    } else if ("3460".equals(address)) {
                        this.mFingerPrintNormalData.addVariance(Integer.parseInt(data));
                        Log.d(CLASS_NAME, "readNormalScanLog_synaptics : Variance");
                    } else if ("8700".equals(address)) {
                        this.mFingerPrintNormalData.addAFE(Integer.parseInt(data));
                        Log.d(CLASS_NAME, "readNormalScanLog_synaptics : AFE");
                    } else if ("7030".equals(address)) {
                        this.mFingerPrintNormalData.addAllowedCounts(Integer.parseInt(data));
                        Log.d(CLASS_NAME, "readNormalScanLog_synaptics : Allowed Counts");
                    }
                } else if ("3460".equals(address)) {
                    this.mFingerPrintNormalData.addVariance(Integer.parseInt(data));
                    Log.d(CLASS_NAME, "readNormalScanLog_synaptics : Variance");
                }
                if ("6070".equals(address)) {
                    this.mFingerPrintNormalData.addPeggedRow(Integer.parseInt(data));
                } else if ("6170".equals(address)) {
                    this.mFingerPrintNormalData.addPeggedCol(Integer.parseInt(data));
                } else if ("6110".equals(address)) {
                    this.mFingerPrintNormalData.addFlooredRow(Integer.parseInt(data));
                } else if ("6210".equals(address)) {
                    this.mFingerPrintNormalData.addFlooredCol(Integer.parseInt(data));
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e5) {
                    LtUtil.log_e(e5);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e6) {
                    LtUtil.log_e(e6);
                }
            }
            try {
                bufferReader.close();
            } catch (IOException e7) {
                LtUtil.log_e(e7);
            }
        } catch (IOException e8) {
            e8.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e9) {
                    LtUtil.log_e(e9);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e10) {
                    LtUtil.log_e(e10);
                }
            }
            if (bufferReader != null) {
                bufferReader.close();
            }
        } catch (Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e11) {
                    LtUtil.log_e(e11);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e12) {
                    LtUtil.log_e(e12);
                }
            }
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e13) {
                    LtUtil.log_e(e13);
                }
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(CLASS_NAME, "onPause");
        super.onPause();
        unregisterClient();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public int runSensorTest(int commandId) {
        return this.mFingerprintManager.semRunSensorTest(commandId, 0, new SemRequestCallback() {
            /* JADX WARNING: Code restructure failed: missing block: B:10:0x0073, code lost:
                if ("EGISTEC".equals(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$400()) == false) goto L_0x007a;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x0075, code lost:
                com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2300(r4.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x007a, code lost:
                r4.this$0.handler.post(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2400(r4.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x008d, code lost:
                if (com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2000(r4.this$0) == false) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:14:0x008f, code lost:
                r4.this$0.handler.post(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2500(r4.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:16:0x00a5, code lost:
                if (com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$1600(r4.this$0) != 1018) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x00a7, code lost:
                r4.this$0.handler.postDelayed(new com.sec.android.app.hwmoduletest.FingerPrintTest_comb.C012611.C01271(r4), 100);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:18:0x00b6, code lost:
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.CLASS_NAME, "runSensorTest", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$002(r4.this$0, "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test");
                r4.this$0.handler.post(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2100(r4.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:4:0x0030, code lost:
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.CLASS_NAME, "runSensorTest", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
             */
            /* JADX WARNING: Code restructure failed: missing block: B:5:0x0041, code lost:
                if (com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$1600(r4.this$0) != 1014) goto L_0x009d;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:6:0x0043, code lost:
                com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$002(r4.this$0, "Normal data scan finished");
                r4.this$0.handler.post(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2100(r4.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:7:0x0061, code lost:
                if ("SYNAPTICS".equals(com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$400()) == false) goto L_0x0069;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:8:0x0063, code lost:
                com.sec.android.app.hwmoduletest.FingerPrintTest_comb.access$2200(r4.this$0);
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onRequested(int r5) {
                /*
                    r4 = this;
                    switch(r5) {
                        case 10008: goto L_0x00b6;
                        case 10009: goto L_0x0030;
                        default: goto L_0x0003;
                    }
                L_0x0003:
                    switch(r5) {
                        case 285212675: goto L_0x00b6;
                        case 285212676: goto L_0x0030;
                        case 285212677: goto L_0x0011;
                        default: goto L_0x0006;
                    }
                L_0x0006:
                    java.lang.String r0 = "FingerPrintTest_comb"
                    java.lang.String r1 = "runSensorTest"
                    java.lang.String r2 = "Event is : UNKNOWN_EVT_MESSAGE"
                    com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r1, r2)
                    goto L_0x00d4
                L_0x0011:
                    java.lang.String r0 = "FingerPrintTest_comb"
                    java.lang.String r1 = "runSensorTest"
                    java.lang.String r2 = "Event is : FACTORY_TEST_EVT_SNSR_TEST"
                    com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r1, r2)
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.String r1 = "Put stimulus on the sensor\nand press OK or HomeButton"
                    r0.m_PopupMessage = r1
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.Runnable r1 = r1.mShowPutFingerDialogRunnable
                    r0.post(r1)
                    goto L_0x00d4
                L_0x0030:
                    java.lang.String r0 = "FingerPrintTest_comb"
                    java.lang.String r1 = "runSensorTest"
                    java.lang.String r2 = "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END"
                    com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r1, r2)
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    int r0 = r0.m_CurrentOperation
                    r1 = 1014(0x3f6, float:1.421E-42)
                    if (r0 != r1) goto L_0x009d
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.String r1 = "Normal data scan finished"
                    r0.m_Message = r1
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.Runnable r1 = r1.mUpdateMessageRunnable
                    r0.post(r1)
                    java.lang.String r0 = "SYNAPTICS"
                    java.lang.String r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.mFingerPrint_Vendor
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto L_0x0069
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    r0.readNormalScanLog_synaptics()
                    goto L_0x007a
                L_0x0069:
                    java.lang.String r0 = "EGISTEC"
                    java.lang.String r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.mFingerPrint_Vendor
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto L_0x007a
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    r0.readNormalScanLog_egis()
                L_0x007a:
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.Runnable r1 = r1.mUpdateNormalDataGraphRunnable
                    r0.post(r1)
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    boolean r0 = r0.STATE_REPLACEMENT_WARNING
                    if (r0 == 0) goto L_0x00d4
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.Runnable r1 = r1.mShowWarningPopupRunnable
                    r0.post(r1)
                    goto L_0x00d4
                L_0x009d:
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    int r0 = r0.m_CurrentOperation
                    r1 = 1018(0x3fa, float:1.427E-42)
                    if (r0 != r1) goto L_0x00d4
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb$11$1 r1 = new com.sec.android.app.hwmoduletest.FingerPrintTest_comb$11$1
                    r1.<init>()
                    r2 = 100
                    r0.postDelayed(r1, r2)
                    goto L_0x00d4
                L_0x00b6:
                    java.lang.String r0 = "FingerPrintTest_comb"
                    java.lang.String r1 = "runSensorTest"
                    java.lang.String r2 = "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START"
                    com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r1, r2)
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.String r1 = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test"
                    r0.m_Message = r1
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r0 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    android.os.Handler r0 = r0.handler
                    com.sec.android.app.hwmoduletest.FingerPrintTest_comb r1 = com.sec.android.app.hwmoduletest.FingerPrintTest_comb.this
                    java.lang.Runnable r1 = r1.mUpdateMessageRunnable
                    r0.post(r1)
                L_0x00d4:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.FingerPrintTest_comb.C012611.onRequested(int):void");
            }
        });
    }
}
