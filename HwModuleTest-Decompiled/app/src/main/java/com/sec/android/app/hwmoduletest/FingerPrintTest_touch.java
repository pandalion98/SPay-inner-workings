package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.synaptics.fingerprint.DeviceInfo;
import com.synaptics.fingerprint.Fingerprint;
import com.synaptics.fingerprint.FingerprintCore.EventListener;
import com.synaptics.fingerprint.FingerprintEvent;
import com.synaptics.fingerprint.SensorTest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class FingerPrintTest_touch extends BaseActivity implements EventListener {
    private static final String CLASS_NAME = "FingerPrintTest_touch";
    /* access modifiers changed from: private */
    public static String scriptID;
    private final int OPERATION_NOTIFY_PLACED_AND_WAIT = 11;
    private final int OPERATION_REQUEST_DEVICEINFO = 8;
    private final int OPERATION_REQUEST_NORMALSCAN_DATA = 101;
    private final int OPERATION_REQUEST_SNR_DATA = 103;
    private final int STATE_NORMALSCAN = 1014;
    private final int STATE_NORMALSCAN_FINISH = 1015;
    private final int STATE_SNR_NOTERM_SCAN = 1016;
    private final int STATE_SNR_SCAN_FINISH = 1019;
    private final int STATE_SNR_TERM_SCAN_PROCESSING = 1018;
    private final int STATE_SNR_TERM_SCAN_READY = 1017;
    private final String TAG = CLASS_NAME;
    private final String USER_ID = "Test";
    Handler handler = new Handler();
    /* access modifiers changed from: private */
    public ImageView iv_snrimage;
    /* access modifiers changed from: private */
    public Runnable mClosePutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_touch.this.tv_msg1.setVisibility(8);
            FingerPrintTest_touch.this.tv_msg2.setVisibility(0);
            FingerPrintTest_touch.this.m_gotermbutton.setVisibility(8);
        }
    };
    private DeviceInfo mDeviceInfo;
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    /* access modifiers changed from: private */
    public FingerPrintSNRData mFingerPrintSNRData;
    /* access modifiers changed from: private */
    public Fingerprint mFingerprint;
    /* access modifiers changed from: private */
    public Semaphore mLock = new Semaphore(1);
    private FingerPrintResultView_touch mResultView;
    private FingerPrintResultView_touch mSNRZoneView;
    private Runnable mShowPutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_touch.this.tv_msg1.setText(FingerPrintTest_touch.this.m_PopupMessage);
            FingerPrintTest_touch.this.tv_msg1.setVisibility(0);
            FingerPrintTest_touch.this.tv_msg2.setVisibility(8);
            FingerPrintTest_touch.this.m_gotermbutton.setText("O K");
            FingerPrintTest_touch.this.m_gotermbutton.setVisibility(0);
        }
    };
    private String mTest;
    private Thread mThread;
    /* access modifiers changed from: private */
    public Runnable mUpdateImageRunnable = new Runnable() {
        public void run() {
            Bitmap bitmap = BitmapFactory.decodeFile("/data/SNR.bmp");
            FingerPrintTest_touch.this.iv_snrimage.setVisibility(0);
            FingerPrintTest_touch.this.iv_snrimage.setImageBitmap(bitmap);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateMessageRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_touch.this.tv_msg2.setText(FingerPrintTest_touch.this.m_Message);
        }
    };
    private Runnable mUpdateNormalDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_touch.this.mFingerPrintNormalData.isReadDone()) {
                FingerPrintTest_touch.this.updateNormalScanResult_new();
                return;
            }
            FingerPrintTest_touch.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_touch.this.tv_result.setVisibility(0);
            FingerPrintTest_touch.this.tv_result.setText(FingerPrintTest_touch.this.m_ResultMessage);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateResultMessageRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_touch.this.m_ResultMessage.isEmpty()) {
                FingerPrintTest_touch.this.tv_result.setVisibility(0);
                FingerPrintTest_touch.this.tv_result.setText(FingerPrintTest_touch.this.m_ResultMessage);
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateSNRDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_touch.this.mFingerPrintSNRData.isReadDone()) {
                FingerPrintTest_touch.this.updateSNRResult();
                return;
            }
            FingerPrintTest_touch.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_touch.this.tv_result.setVisibility(0);
            FingerPrintTest_touch.this.tv_result.setText(FingerPrintTest_touch.this.m_ResultMessage);
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
    /* access modifiers changed from: private */
    public int m_result = 0;
    private String sdk_version;
    /* access modifiers changed from: private */
    public TextView tv_msg1;
    /* access modifiers changed from: private */
    public TextView tv_msg2;
    /* access modifiers changed from: private */
    public TextView tv_result;

    private static class FingerPrintNormalData {
        private static final int ITEM_COUNT = 4;
        private static final int ITEM_COUNT_NEW_SYNAPTICS = 8;
        private int isDone = 0;
        private NormalItem mItemAFE;
        private NormalItem mItemAllowedCounts;
        private NormalItem mItemFlooredCol;
        private NormalItem mItemFlooredPixel;
        private NormalItem mItemFlooredRow;
        private NormalItem mItemMaxNegDelta;
        private NormalItem mItemMaxPosDelta;
        private NormalItem mItemMaximumDelta;
        private NormalItem mItemMinimumDelta;
        private NormalItem mItemPeggedCol;
        private NormalItem mItemPeggedPixel;
        private NormalItem mItemPeggedRow;
        private NormalItem mItemRAM;
        private NormalItem mItemTotal2DDeltaFail;
        private NormalItem mItemVariance;
        private String mSpecResultFile;

        private static class NormalItem {
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

        public boolean isReadDone() {
            return this.isDone == 8;
        }

        public void addPeggedRow(int value) {
            this.mItemPeggedRow = new NormalItem();
            addItem(this.mItemPeggedRow, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_ROW_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_ROW_MAX))));
            this.isDone++;
        }

        public void addPeggedCol(int value) {
            this.mItemPeggedCol = new NormalItem();
            addItem(this.mItemPeggedCol, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_COL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PEGGED_PIXEL_COL_MAX))));
            this.isDone++;
        }

        public void addFlooredRow(int value) {
            this.mItemFlooredRow = new NormalItem();
            addItem(this.mItemFlooredRow, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_ROW_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_ROW_MAX))));
            this.isDone++;
        }

        public void addFlooredCol(int value) {
            this.mItemFlooredCol = new NormalItem();
            addItem(this.mItemFlooredCol, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_COL_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLOORED_PIXEL_COL_MAX))));
            this.isDone++;
        }

        public void addRAM(int value) {
            this.mItemRAM = new NormalItem();
            addItem(this.mItemRAM, value, new Spec(Integer.valueOf(0), Integer.valueOf(0)));
            this.isDone++;
        }

        public void addVariance(int value) {
            int mVarianceMax;
            int mVarianceMin;
            this.mItemVariance = new NormalItem();
            if (Integer.parseInt(FingerPrintTest_touch.scriptID) >= 6015) {
                mVarianceMin = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MIN_DC);
                mVarianceMax = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MAX_DC);
            } else {
                mVarianceMin = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MIN);
                mVarianceMax = com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_VARIANCE_MAX);
            }
            addItem(this.mItemVariance, value, new Spec(Integer.valueOf(mVarianceMin), Integer.valueOf(mVarianceMax)));
            this.isDone++;
        }

        public void addAFE(int value) {
            this.mItemAFE = new NormalItem();
            addItem(this.mItemAFE, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFE_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFE_MAX))));
            this.isDone++;
        }

        public void addAllowedCounts(int value) {
            this.mItemAllowedCounts = new NormalItem();
            addItem(this.mItemAllowedCounts, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_ALLOWED_COUNTS_MIN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_ALLOWED_COUNTS_MAX))));
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
    }

    private static class FingerPrintSNRData {
        private static final int ITEM_COUNT = 14;
        private static final int ITEM_COUNT_VIPER2 = 11;
        private int isDone = 0;
        private SNRItem mItemDPCol;
        private SNRItem mItemDPRow;
        private SNRItem mItemNoise;
        private SNRItem mItemSNR;
        private SNRItem mItemSignal;
        private String[] mSNRZone = new String[6];
        private String mSpecResultFile;

        private static class SNRItem {
            private boolean mPass;
            private Spec<Float> mSpec;
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

            public String getValue() {
                return this.mValue;
            }

            public boolean getPass() {
                return this.mPass;
            }

            public Spec<Float> getSpec() {
                return this.mSpec;
            }
        }

        public boolean isReadDone() {
            return this.isDone == 11;
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
    }

    private static class Spec<T> {
        T lower = null;
        T upper = null;

        public Spec(T l, T u) {
            this.lower = l;
            this.upper = u;
        }
    }

    public FingerPrintTest_touch() {
        super(CLASS_NAME);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3 && this.m_gotermbutton.isShown()) {
            Log.d(CLASS_NAME, "onKeyDown() - Homekey pressed");
            Log.d(CLASS_NAME, "onKeyDown() - excuteOperation(OPERATION_NOTIFY_PLACED_AND_WAIT) ");
            this.m_CurrentOperation = 1018;
            excuteOperation(11);
            this.handler.post(this.mClosePutFingerDialogRunnable);
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void updateNormalScanResult_new() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.m_ScreenWidth, this.m_ScreenHeight / 3);
        this.mResultView.setRowCol(9, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        int row_next = 0 + 1;
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(row_next, 0, "RAM Test");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintNormalData.getRAMSpec().lower);
        sb.append("");
        fingerPrintResultView_touch.setTextData(row_next, 2, sb.toString());
        this.mResultView.setTextData(row_next, 3, this.mFingerPrintNormalData.getRAMValue());
        int row_next2 = row_next + 1;
        this.mResultView.setTextData(row_next, 4, this.mFingerPrintNormalData.getRAMPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next2, 0, "Variance Test");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintNormalData.getVarianceSpec().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintNormalData.getVarianceSpec().upper);
        fingerPrintResultView_touch2.setTextData(row_next2, 2, sb2.toString());
        this.mResultView.setTextData(row_next2, 3, this.mFingerPrintNormalData.getVarianceValue());
        int row_next3 = row_next2 + 1;
        this.mResultView.setTextData(row_next2, 4, this.mFingerPrintNormalData.getVariancePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next3, 0, "AFE Balance Test");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintNormalData.getAFESpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintNormalData.getAFESpec().upper);
        fingerPrintResultView_touch3.setTextData(row_next3, 2, sb3.toString());
        this.mResultView.setTextData(row_next3, 3, this.mFingerPrintNormalData.getAFEValue());
        int row_next4 = row_next3 + 1;
        this.mResultView.setTextData(row_next3, 4, this.mFingerPrintNormalData.getAFEPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next4, 0, "PeggedPxl");
        this.mResultView.setTextData(row_next4, 1, "Row");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintNormalData.getPeggedRowSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintNormalData.getPeggedRowSpec().upper);
        fingerPrintResultView_touch4.setTextData(row_next4, 2, sb4.toString());
        this.mResultView.setTextData(row_next4, 3, this.mFingerPrintNormalData.getPeggedRowValue());
        int row_next5 = row_next4 + 1;
        this.mResultView.setTextData(row_next4, 4, this.mFingerPrintNormalData.getPeggedRowPass() ? "PASS" : "FAIL");
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
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append("ACTION_POINTER_DOWN index : ");
            sb.append(index);
            Log.d(str, sb.toString());
            float x = event.getX(index);
            float y = event.getY(index);
            if (x > this.m_gotermbutton.getX() && x < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y > this.m_gotermbutton.getY() && y < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                Log.d(CLASS_NAME, "Event.ACTION_POINTER_2_DOWN pos is inside OK button ");
                this.m_gotermbutton.setPressed(true);
            }
        } else if (event.getActionMasked() == 6) {
            int index2 = (action & 65280) >> 8;
            String str2 = CLASS_NAME;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ACTION_POINTER_UP index : ");
            sb2.append(index2);
            Log.d(str2, sb2.toString());
            float x2 = event.getX(index2);
            float y2 = event.getY(index2);
            if (x2 > this.m_gotermbutton.getX() && x2 < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y2 > this.m_gotermbutton.getY() && y2 < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                Log.d(CLASS_NAME, "Event.ACTION_POINTER_2_UP pos is inside OK button ");
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
                FingerPrintTest_touch.this.m_CurrentOperation = 1018;
                FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mClosePutFingerDialogRunnable);
                FingerPrintTest_touch.this.excuteOperation(11);
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
        setContentView(C0268R.layout.fingerprint_test_touch);
        initLayout();
        Log.i(CLASS_NAME, "onCreate");
        this.mFingerprint = new Fingerprint(this);
        this.mFingerprint.registerListener(this);
        this.sdk_version = this.mFingerprint.getVersion();
        String str = CLASS_NAME;
        StringBuilder sb = new StringBuilder();
        sb.append("Version : ");
        sb.append(this.sdk_version);
        Log.i(str, sb.toString());
        this.mTest = getIntent().getStringExtra("Action");
        String str2 = CLASS_NAME;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getSensorStatus : ");
        sb2.append(this.mFingerprint.getSensorStatus());
        Log.i(str2, sb2.toString());
        if (this.mFingerprint.getSensorStatus() == 0) {
            Log.i(CLASS_NAME, "getSensorStatus : STATUS_OK");
            if ("NORMALDATA".equals(this.mTest)) {
                this.m_CurrentOperation = 1014;
                excuteOperation(101);
            } else if ("SNR".equals(this.mTest)) {
                this.m_CurrentOperation = 1016;
                excuteOperation(103);
            } else if ("SENSORINFO".equals(this.mTest)) {
                excuteOperation(8);
            }
        } else {
            Log.i(CLASS_NAME, "getSensorStatus : STATUS_FAIL");
            this.m_ResultMessage = "Fingerprint initializing Fail";
            this.handler.post(this.mUpdateResultMessageRunnable);
        }
    }

    public void onEvent(FingerprintEvent event) {
        String result_MT;
        String result_Cal;
        if (event != null) {
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append("onEventsCB(): Event Id = ");
            sb.append(event.eventId);
            Log.i(str, sb.toString());
            int i = event.eventId;
            if (i == 405) {
                Log.i(CLASS_NAME, "Event is : EVT_DEVICE_INFO");
                this.mDeviceInfo = (DeviceInfo) event.eventData;
                if (!"SENSORINFO".equals(this.mTest) || this.mDeviceInfo == null) {
                    Log.i(CLASS_NAME, "mDeviceInfo is null");
                    return;
                }
                this.m_ResultMessage = "FwVersion : ";
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.m_ResultMessage);
                sb2.append(this.mDeviceInfo.fwVersion);
                this.m_ResultMessage = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.m_ResultMessage);
                sb3.append("\nFlex ID : ");
                this.m_ResultMessage = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.m_ResultMessage);
                sb4.append(this.mDeviceInfo.flexId);
                this.m_ResultMessage = sb4.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.m_ResultMessage);
                sb5.append("\nProduct ID : ");
                this.m_ResultMessage = sb5.toString();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(this.m_ResultMessage);
                sb6.append(this.mDeviceInfo.productId);
                this.m_ResultMessage = sb6.toString();
                String sdk_split = this.sdk_version.substring(this.sdk_version.lastIndexOf(".") + 1, this.sdk_version.length());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("sdk_split : ");
                sb7.append(sdk_split);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb7.toString());
                if (Integer.parseInt(sdk_split) >= 159) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(this.m_ResultMessage);
                    sb8.append("\nSensor ID : ");
                    this.m_ResultMessage = sb8.toString();
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(this.m_ResultMessage);
                    sb9.append(this.mDeviceInfo.sensorDeviceId);
                    this.m_ResultMessage = sb9.toString();
                }
                StringBuilder sb10 = new StringBuilder();
                sb10.append("moduleTest/CalTest Value : ");
                sb10.append(this.mDeviceInfo.testInfo);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb10.toString());
                String str2 = "";
                int moduleTest = this.mDeviceInfo.testInfo & 3;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("moduleTest_result : ");
                sb11.append(moduleTest);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb11.toString());
                if (moduleTest == 0) {
                    result_MT = "Not tested";
                } else if (moduleTest == 3) {
                    result_MT = "Pass";
                } else {
                    result_MT = "Fail";
                }
                StringBuilder sb12 = new StringBuilder();
                sb12.append(this.m_ResultMessage);
                sb12.append("\nModule Test : ");
                this.m_ResultMessage = sb12.toString();
                StringBuilder sb13 = new StringBuilder();
                sb13.append(this.m_ResultMessage);
                sb13.append(result_MT);
                this.m_ResultMessage = sb13.toString();
                String str3 = "";
                int calTest = this.mDeviceInfo.testInfo & 12;
                StringBuilder sb14 = new StringBuilder();
                sb14.append("calTest_result : ");
                sb14.append(calTest);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb14.toString());
                if (calTest == 4) {
                    result_Cal = "WOF";
                } else if (calTest == 8) {
                    result_Cal = "BaseLine";
                } else if (calTest == 12) {
                    result_Cal = "Pass";
                } else {
                    result_Cal = "Fail";
                }
                StringBuilder sb15 = new StringBuilder();
                sb15.append(this.m_ResultMessage);
                sb15.append("\nCalibration : ");
                this.m_ResultMessage = sb15.toString();
                StringBuilder sb16 = new StringBuilder();
                sb16.append(this.m_ResultMessage);
                sb16.append(result_Cal);
                this.m_ResultMessage = sb16.toString();
                this.m_Message = "Sensor Infomation";
                this.handler.post(this.mUpdateMessageRunnable);
                this.handler.post(this.mUpdateResultMessageRunnable);
            } else if (i == 2001) {
                Log.i(CLASS_NAME, "Event is : EVT_SNSR_TEST_SCRIPT_START");
                this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
                this.handler.post(this.mUpdateMessageRunnable);
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        FingerPrintTest_touch.this.excuteOperation(11);
                    }
                }, 100);
            } else if (i != 2004) {
                switch (i) {
                    case Fingerprint.EVT_SNSR_TEST_PUT_STIMULUS_ON_SENSOR /*2025*/:
                        Log.i(CLASS_NAME, "Event is : EVT_SNSR_TEST_PUT_STIMULUS_ON_SENSOR");
                        this.m_CurrentOperation = 1017;
                        this.m_PopupMessage = "Put stimulus on the sensor\nand press OK or HomeButton";
                        this.handler.post(this.mShowPutFingerDialogRunnable);
                        return;
                    case Fingerprint.EVT_SNSR_TEST_REMOVE_STIMULUS_FROM_SENSOR /*2026*/:
                        Log.i(CLASS_NAME, "Event is : EVT_SNSR_TEST_REMOVE_STIMULUS_FROM_SENSOR");
                        excuteOperation(11);
                        return;
                    default:
                        Log.i(CLASS_NAME, "Event is : UNKNOWN_EVT_MESSAGE");
                        return;
                }
            } else {
                Log.i(CLASS_NAME, "Event is : EVT_SNSR_TEST_SCRIPT_END");
                if (this.m_CurrentOperation == 1014) {
                    this.m_Message = "Normal data scan finished";
                    this.handler.post(this.mUpdateMessageRunnable);
                    readNormalScanLogFile();
                    this.handler.post(this.mUpdateNormalDataGraphRunnable);
                    return;
                }
                excuteOperation(11);
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        if (FingerPrintTest_touch.this.m_CurrentOperation == 1018) {
                            FingerPrintTest_touch.this.m_Message = "SNR Test data scan finished";
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateMessageRunnable);
                            FingerPrintTest_touch.this.readSNRLogFile();
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateSNRDataGraphRunnable);
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateImageRunnable);
                        } else if (FingerPrintTest_touch.this.m_CurrentOperation == 1016 || FingerPrintTest_touch.this.m_CurrentOperation == 1017) {
                            FingerPrintTest_touch.this.m_Message = "Test data scan canceled\nDue to no action";
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateMessageRunnable);
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mClosePutFingerDialogRunnable);
                        } else {
                            FingerPrintTest_touch.this.m_Message = "Script end";
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateMessageRunnable);
                            FingerPrintTest_touch.this.handler.post(FingerPrintTest_touch.this.mUpdateResultMessageRunnable);
                        }
                    }
                }, 100);
            }
        }
    }

    /* access modifiers changed from: private */
    public void excuteOperation(final int operation) {
        this.mThread = new Thread(new Runnable() {
            public void run() {
                try {
                    if (!FingerPrintTest_touch.this.mLock.tryAcquire(500, TimeUnit.MILLISECONDS)) {
                        Log.e(FingerPrintTest_touch.CLASS_NAME, "mLock.tryAcquire( fail!");
                        return;
                    }
                    if (operation == 101) {
                        SensorTest snrTest = new SensorTest();
                        int opt = 0 | 65536;
                        int dataLogopt = 268435456 | 0 | 1;
                        snrTest.scriptId = 6011;
                        snrTest.options = opt;
                        snrTest.dataLogOpt = dataLogopt;
                        snrTest.unitId = 0;
                        String str = FingerPrintTest_touch.CLASS_NAME;
                        StringBuilder sb = new StringBuilder();
                        sb.append("SensorTest[");
                        sb.append(snrTest.scriptId);
                        sb.append(", ");
                        sb.append(snrTest.options);
                        sb.append(", ");
                        sb.append(snrTest.dataLogOpt);
                        sb.append(", ");
                        sb.append(snrTest.unitId);
                        sb.append("]");
                        Log.i(str, sb.toString());
                        if (FingerPrintTest_touch.this.mFingerprint.request(11, snrTest) == 0) {
                            Log.d(FingerPrintTest_touch.CLASS_NAME, "SensorTest OPERATION_REQUEST_NORMALSCAN_DATA Success ");
                        } else {
                            Log.e(FingerPrintTest_touch.CLASS_NAME, "SensorTest OPERATION_REQUEST_NORMALSCAN_DATA FAIL!! ");
                        }
                    } else if (operation == 103) {
                        SensorTest snrTest2 = new SensorTest();
                        int opt2 = 65536 | 0 | 524288;
                        int dataLogopt2 = 268435456 | 0 | 1 | 2 | 256;
                        snrTest2.scriptId = 6010;
                        snrTest2.options = opt2;
                        snrTest2.dataLogOpt = dataLogopt2;
                        snrTest2.unitId = 0;
                        String str2 = FingerPrintTest_touch.CLASS_NAME;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("SensorTest[");
                        sb2.append(snrTest2.scriptId);
                        sb2.append(", ");
                        sb2.append(snrTest2.options);
                        sb2.append(", ");
                        sb2.append(snrTest2.dataLogOpt);
                        sb2.append(", ");
                        sb2.append(snrTest2.unitId);
                        sb2.append("]");
                        Log.i(str2, sb2.toString());
                        if (FingerPrintTest_touch.this.mFingerprint.request(11, snrTest2) == 0) {
                            Log.d(FingerPrintTest_touch.CLASS_NAME, "SensorTest VCS_REQUEST_COMMAND_SENSOR_TEST Success ");
                        } else {
                            Log.e(FingerPrintTest_touch.CLASS_NAME, "SensorTest VCS_REQUEST_COMMAND_SENSOR_TEST FAIL!! ");
                        }
                    } else if (operation == 8) {
                        FingerPrintTest_touch.this.m_result = FingerPrintTest_touch.this.mFingerprint.request(12, null);
                    } else if (operation == 11) {
                        FingerPrintTest_touch.this.m_result = FingerPrintTest_touch.this.mFingerprint.notify(1, null);
                    }
                    FingerPrintTest_touch.this.mLock.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.mThread.start();
    }

    private void sendSensorStatusLog() {
        if (this.m_result == 358) {
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor status : VCS_SENSOR_STATUS_INITIALISING [");
            sb.append(this.m_result);
            sb.append("]");
            Log.d(str, sb.toString());
        } else if (this.m_result == 360) {
            String str2 = CLASS_NAME;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Sensor status : VCS_SENSOR_STATUS_OUT_OF_ORDER [");
            sb2.append(this.m_result);
            sb2.append("]");
            Log.d(str2, sb2.toString());
        } else if (this.m_result == 46) {
            String str3 = CLASS_NAME;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Sensor status : VCS_SENSOR_STATUS_WORKING [");
            sb3.append(this.m_result);
            sb3.append("]");
            Log.d(str3, sb3.toString());
        } else if (this.m_result == 0) {
            String str4 = CLASS_NAME;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Sensor status : VCS_SENSOR_STATUS_OK [");
            sb4.append(this.m_result);
            sb4.append("]");
            Log.d(str4, sb4.toString());
        }
    }

    private void readNormalScanLogFile() {
        String path = "/data/SynSnsrTest.log";
        boolean flexIDPass = true;
        if (new File(path).exists()) {
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(" file Exist");
            Log.i(str, sb.toString());
            String fis = null;
            try {
                fis = new FileInputStream(path);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String str2 = "";
                while (true) {
                    fis = bufferReader.readLine();
                    String readStr = fis;
                    if (fis != null) {
                        String address = "";
                        String data = "";
                        String pass = "";
                        try {
                            String[] splitStr = readStr.split(",");
                            address = splitStr[0].trim();
                            data = splitStr[1].trim();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if ("Script Type".equals(address)) {
                            scriptID = data;
                            String str3 = CLASS_NAME;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("readNormalScanLogFile : scriptID(");
                            sb2.append(scriptID);
                            sb2.append(")");
                            Log.d(str3, sb2.toString());
                        } else if ("9030".equals(address)) {
                            this.mFingerPrintNormalData.addRAM(Integer.parseInt(data));
                            Log.d(CLASS_NAME, "readNormalScanLogFile : RAM");
                        } else if ("3460".equals(address)) {
                            this.mFingerPrintNormalData.addVariance(Integer.parseInt(data));
                            Log.d(CLASS_NAME, "readNormalScanLogFile : Variance");
                        } else if ("8700".equals(address)) {
                            this.mFingerPrintNormalData.addAFE(Integer.parseInt(data));
                            Log.d(CLASS_NAME, "readNormalScanLogFile : AFE");
                        } else if ("7030".equals(address)) {
                            this.mFingerPrintNormalData.addAllowedCounts(Integer.parseInt(data));
                            Log.d(CLASS_NAME, "readNormalScanLogFile : Allowed Counts");
                        }
                        if ("130".equals(address)) {
                            if (pass.contains("FAIL")) {
                                flexIDPass = false;
                            }
                        } else if ("6070".equals(address)) {
                            if (!flexIDPass) {
                                this.mFingerPrintNormalData.addPeggedRow(44444444);
                            } else {
                                this.mFingerPrintNormalData.addPeggedRow(Integer.parseInt(data));
                            }
                        } else if ("6170".equals(address)) {
                            this.mFingerPrintNormalData.addPeggedCol(Integer.parseInt(data));
                        } else if ("6110".equals(address)) {
                            this.mFingerPrintNormalData.addFlooredRow(Integer.parseInt(data));
                        } else if ("6210".equals(address)) {
                            this.mFingerPrintNormalData.addFlooredCol(Integer.parseInt(data));
                        }
                    } else {
                        fis.close();
                        try {
                            fis.close();
                            return;
                        } catch (IOException e2) {
                            LtUtil.log_e(e2);
                            return;
                        }
                    }
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fis != null) {
                    fis.close();
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e4) {
                        LtUtil.log_e(e4);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void readSNRLogFile() {
        String path = "/data/SynSnsrTest.log";
        if (new File(path).exists()) {
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(" file Exist");
            Log.i(str, sb.toString());
            String fis = null;
            try {
                fis = new FileInputStream(path);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String str2 = "";
                while (true) {
                    fis = bufferReader.readLine();
                    String readStr = fis;
                    if (fis != null) {
                        String address = "";
                        String data = "";
                        String str3 = "";
                        try {
                            String[] splitStr = readStr.split(",");
                            address = splitStr[0].trim();
                            data = splitStr[1].trim();
                            String pass = splitStr[2].trim();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if ("4030".equals(address)) {
                            this.mFingerPrintSNRData.addSignal(Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4040".equals(address)) {
                            this.mFingerPrintSNRData.addNoise(Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4050".equals(address)) {
                            this.mFingerPrintSNRData.addSNR(Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4080".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(0, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4110".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(1, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4140".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(2, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4170".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(3, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4200".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(4, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("4230".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(5, Float.parseFloat(data) / 10.0f, 1);
                        } else if ("6560".equals(address)) {
                            this.mFingerPrintSNRData.addDeltaPixelRow(Float.parseFloat(data), 0);
                        } else if ("6600".equals(address)) {
                            this.mFingerPrintSNRData.addDeltaPixelCol(Float.parseFloat(data), 0);
                            break;
                        }
                    }
                }
                try {
                    fis.close();
                } catch (IOException e2) {
                    LtUtil.log_e(e2);
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fis != null) {
                    fis.close();
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e4) {
                        LtUtil.log_e(e4);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(CLASS_NAME, "onPause");
        super.onPause();
        if (this.mFingerprint != null) {
            Log.i(CLASS_NAME, "onPause() - mFingerprint.notify() ");
            this.m_result = this.mFingerprint.notify(2, null);
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    if (FingerPrintTest_touch.this.mFingerprint != null) {
                        LtUtil.log_i(FingerPrintTest_touch.CLASS_NAME, "onPause()", "mFingerprint.cleanUp()");
                        try {
                            FingerPrintTest_touch.this.mLock.acquire();
                            FingerPrintTest_touch.this.mFingerprint.cancel();
                            FingerPrintTest_touch.this.mFingerprint.cleanUp();
                            FingerPrintTest_touch.this.mFingerprint = null;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FingerPrintTest_touch.this.mLock.release();
                    }
                }
            }, 60);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
