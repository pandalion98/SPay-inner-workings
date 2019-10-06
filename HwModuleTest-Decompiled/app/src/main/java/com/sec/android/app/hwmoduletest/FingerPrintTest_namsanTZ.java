package com.sec.android.app.hwmoduletest;

import android.graphics.Point;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.SemRequestCallback;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

public class FingerPrintTest_namsanTZ extends BaseActivity {
    private static final String CLASS_NAME = "FingerPrintTest_namsanTZ";
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END_GOOGLE = 10009;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START_GOOGLE = 10008;
    public static final int NORMAL_SCAN_READ_RETRY_COUNT = 5;
    public static final int OUT_BUFFER_SIZE = 30720;
    public static final int SENSOR_TEST_NORMALSCAN_COMMAND = 100103;
    private static String mFingerPrint_Vendor;
    private static String scriptID;
    /* access modifiers changed from: private */
    public int IDENTIFY_FAILURE_SENSOR_CHANGED = 59;
    private final int STATE_NORMAL_SCAN = 1014;
    /* access modifiers changed from: private */
    public boolean STATE_REPLACEMENT_WARNING = false;
    private final String USER_ID = "Test";
    Handler handler = new Handler();
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    private FingerprintManager mFingerprintManager;
    private Semaphore mLock = new Semaphore(1);
    private FingerPrintResultView_touch mResultView;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    /* access modifiers changed from: private */
    public Runnable mShowWarningPopupRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsanTZ.this.showWarningPopup();
        }
    };
    private String mTest;
    private Thread mThread;
    private IBinder mToken;
    /* access modifiers changed from: private */
    public Runnable mUpdateMessageRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsanTZ.this.tv_msg2.setText(FingerPrintTest_namsanTZ.this.m_Message);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateNormalDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_namsanTZ.this.mFingerPrintNormalData.isReadDone()) {
                FingerPrintTest_namsanTZ.this.updateNormalScanResult();
                if (BaseActivity.isOqcsbftt && FingerPrintTest_namsanTZ.this.mFingerPrintNormalData.getAllPass()) {
                    NVAccessor.setNV(409, NVAccessor.NV_VALUE_PASS);
                    return;
                }
                return;
            }
            FingerPrintTest_namsanTZ.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_namsanTZ.this.tv_result.setVisibility(0);
            FingerPrintTest_namsanTZ.this.tv_result.setText(FingerPrintTest_namsanTZ.this.m_ResultMessage);
        }
    };
    private Runnable mUpdateResultMessageRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_namsanTZ.this.m_ResultMessage.isEmpty()) {
                FingerPrintTest_namsanTZ.this.tv_result.setVisibility(0);
                FingerPrintTest_namsanTZ.this.tv_result.setText(FingerPrintTest_namsanTZ.this.m_ResultMessage);
            }
        }
    };
    /* access modifiers changed from: private */
    public int m_CurrentOperation = 0;
    /* access modifiers changed from: private */
    public String m_Message = "Ready";
    private String m_PopupMessage = "";
    private int m_PreviousOperation = 0;
    /* access modifiers changed from: private */
    public String m_ResultMessage = "";
    private int m_result = 0;
    private String os_version;
    private String[] os_version_array;
    private TextView tv_msg1;
    /* access modifiers changed from: private */
    public TextView tv_msg2;
    /* access modifiers changed from: private */
    public TextView tv_result;

    private static class FingerPrintNormalData {
        private static final int ITEM_COUNT = 19;
        private static final int ZONE_COUNT = 6;
        private boolean isAllpass = true;
        private int isDone = 0;
        private NormalItem mItemAFEBalance;
        private NormalItem mItemAreaMaxPixelFail;
        private NormalItem mItemFlashCheckSum;
        private NormalItem mItemMFGCalOverall;
        private NormalItem mItemMFGCalZoneMax;
        private NormalItem mItemOpenShortRXRange;
        private NormalItem mItemOpenShortRxImage;
        private NormalItem mItemOpenShortRxShort;
        private NormalItem mItemOpenShortTxShort;
        private NormalItem mItemOpenShortTxSum;
        private NormalItem mItemRxDelta;
        private NormalItem mItemRxMaxPixelFail;
        private NormalItem mItemTotalBadPixelCount;
        private NormalItem mItemTxMaxPixelFail;
        private int[] mMFGCalZone = new int[6];
        private String mSpecResultFile;
        private int mfgcal_count = 0;
        private int mfgcal_max = -1;

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
            return this.isDone == 19;
        }

        public void addRxDelta(int value) {
            this.mItemRxDelta = new NormalItem();
            addItem(this.mItemRxDelta, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RX_DELTA_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RX_DELTA_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addAFEBalance(int value) {
            this.mItemAFEBalance = new NormalItem();
            addItem(this.mItemAFEBalance, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFEBALANCE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AFEBALANCE_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addFlashCheckSum(int value) {
            this.mItemFlashCheckSum = new NormalItem();
            addItem(this.mItemFlashCheckSum, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLASHCHECKSUM_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FLASHCHECKSUM_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addOpenShortRXRange(int value) {
            this.mItemOpenShortRXRange = new NormalItem();
            addItem(this.mItemOpenShortRXRange, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_RANGE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_RANGE_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addOpenShortRxImage(int value) {
            this.mItemOpenShortRxImage = new NormalItem();
            addItem(this.mItemOpenShortRxImage, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_IMAGE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_IMAGE_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addOpenShortTxSum(int value) {
            this.mItemOpenShortTxSum = new NormalItem();
            addItem(this.mItemOpenShortTxSum, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_TX_SUM_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_TX_SUM_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addOpenShortTxShort(int value) {
            this.mItemOpenShortTxShort = new NormalItem();
            addItem(this.mItemOpenShortTxShort, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_TX_SHORT_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_TX_SHORT_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addOpenShortRxShort(int value) {
            this.mItemOpenShortRxShort = new NormalItem();
            addItem(this.mItemOpenShortRxShort, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_SHORT_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_OPEN_SHORT_RX_SHORT_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addRxMaxPixelFail(int value) {
            this.mItemRxMaxPixelFail = new NormalItem();
            addItem(this.mItemRxMaxPixelFail, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RX_MAX_PIXEL_FAIL_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RX_MAX_PIXEL_FAIL_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addTxMaxPixelFail(int value) {
            this.mItemTxMaxPixelFail = new NormalItem();
            addItem(this.mItemTxMaxPixelFail, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_TX_MAX_PIXEL_FAIL_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_TX_MAX_PIXEL_FAIL_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addAreaMaxPixelFail(int value) {
            this.mItemAreaMaxPixelFail = new NormalItem();
            addItem(this.mItemAreaMaxPixelFail, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AREA_MAX_PIXEL_FAIL_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_AREA_MAX_PIXEL_FAIL_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addTotalBadPixelCount(int value) {
            this.mItemTotalBadPixelCount = new NormalItem();
            addItem(this.mItemTotalBadPixelCount, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addMFGCalOverall(int value) {
            this.mItemMFGCalOverall = new NormalItem();
            addItem(this.mItemMFGCalOverall, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_MFG_CAL_OVERALL_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_MFG_CAL_OVERALL_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addMFGCalZoneRatio(int index, int value) {
            this.mMFGCalZone[index] = value;
            this.mfgcal_count++;
            if (this.mfgcal_max < this.mMFGCalZone[index]) {
                this.mfgcal_max = this.mMFGCalZone[index];
            }
            if (this.mfgcal_count == 6) {
                this.mItemMFGCalZoneMax = new NormalItem();
                addItem(this.mItemMFGCalZoneMax, this.mfgcal_max, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_MFG_CAL_ZONE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_MFG_CAL_ZONE_MAX_NAMSAN))));
            }
            this.isDone++;
        }

        private void addItem(NormalItem item, int value, Spec<Integer> spec) {
            String strValue = String.valueOf(value);
            boolean pass = false;
            if (value < ((Integer) spec.lower).intValue() || value > ((Integer) spec.upper).intValue()) {
                this.isAllpass = false;
            } else {
                pass = true;
            }
            item.setSpec(spec);
            item.setValue(strValue);
            item.setPass(pass);
        }

        public String getRxDeltaValue() {
            return this.mItemRxDelta.getValue();
        }

        public String getAFEBalanceValue() {
            return this.mItemAFEBalance.getValue();
        }

        public String getFlashCheckSumValue() {
            return this.mItemFlashCheckSum.getValue();
        }

        public String getOpenShortRXRangeValue() {
            return this.mItemOpenShortRXRange.getValue();
        }

        public String getOpenShortRxImageValue() {
            return this.mItemOpenShortRxImage.getValue();
        }

        public String getOpenShortTxSumValue() {
            return this.mItemOpenShortTxSum.getValue();
        }

        public String getOpenShortTxShortValue() {
            return this.mItemOpenShortTxShort.getValue();
        }

        public String getOpenShortRxShortValue() {
            return this.mItemOpenShortRxShort.getValue();
        }

        public String getRxMaxPixelFailValue() {
            return this.mItemRxMaxPixelFail.getValue();
        }

        public String getTxMaxPixelFailValue() {
            return this.mItemTxMaxPixelFail.getValue();
        }

        public String getAreaMaxPixelFailValue() {
            return this.mItemAreaMaxPixelFail.getValue();
        }

        public String getTotalBadPixelCountValue() {
            return this.mItemTotalBadPixelCount.getValue();
        }

        public String getMFGCalOverallValue() {
            return this.mItemMFGCalOverall.getValue();
        }

        public String getMFGCalZoneMaxValue() {
            return this.mItemMFGCalZoneMax.getValue();
        }

        public boolean getRxDeltaPass() {
            return this.mItemRxDelta.getPass();
        }

        public boolean getAFEBalancePass() {
            return this.mItemAFEBalance.getPass();
        }

        public boolean getFlashCheckSumPass() {
            return this.mItemFlashCheckSum.getPass();
        }

        public boolean getOpenShortRXRangePass() {
            return this.mItemOpenShortRXRange.getPass();
        }

        public boolean getOpenShortRxImagePass() {
            return this.mItemOpenShortRxImage.getPass();
        }

        public boolean getOpenShortTxSumPass() {
            return this.mItemOpenShortTxSum.getPass();
        }

        public boolean getOpenShortTxShortPass() {
            return this.mItemOpenShortTxShort.getPass();
        }

        public boolean getOpenShortRxShortPass() {
            return this.mItemOpenShortRxShort.getPass();
        }

        public boolean getRxMaxPixelFailPass() {
            return this.mItemRxMaxPixelFail.getPass();
        }

        public boolean getTxMaxPixelFailPass() {
            return this.mItemTxMaxPixelFail.getPass();
        }

        public boolean getAreaMaxPixelFailPass() {
            return this.mItemAreaMaxPixelFail.getPass();
        }

        public boolean getTotalBadPixelCountPass() {
            return this.mItemTotalBadPixelCount.getPass();
        }

        public boolean getMFGCalOverallPass() {
            return this.mItemMFGCalOverall.getPass();
        }

        public boolean getMFGCalZoneMaxPass() {
            return this.mItemMFGCalZoneMax.getPass();
        }

        public boolean getAllPass() {
            return this.isAllpass;
        }

        public Spec<Integer> getRxDeltaSpec() {
            return this.mItemRxDelta.getSpec();
        }

        public Spec<Integer> getAFEBalanceSpec() {
            return this.mItemAFEBalance.getSpec();
        }

        public Spec<Integer> getFlashCheckSumSpec() {
            return this.mItemFlashCheckSum.getSpec();
        }

        public Spec<Integer> getOpenShortRXRangeSpec() {
            return this.mItemOpenShortRXRange.getSpec();
        }

        public Spec<Integer> getOpenShortRxImageSpec() {
            return this.mItemOpenShortRxImage.getSpec();
        }

        public Spec<Integer> getOpenShortTxSumSpec() {
            return this.mItemOpenShortTxSum.getSpec();
        }

        public Spec<Integer> getOpenShortTxShortSpec() {
            return this.mItemOpenShortTxShort.getSpec();
        }

        public Spec<Integer> getOpenShortRxShortSpec() {
            return this.mItemOpenShortRxShort.getSpec();
        }

        public Spec<Integer> getRxMaxPixelFailSpec() {
            return this.mItemRxMaxPixelFail.getSpec();
        }

        public Spec<Integer> getTxMaxPixelFailSpec() {
            return this.mItemTxMaxPixelFail.getSpec();
        }

        public Spec<Integer> getAreaMaxPixelFailSpec() {
            return this.mItemAreaMaxPixelFail.getSpec();
        }

        public Spec<Integer> getTotalBadPixelCountSpec() {
            return this.mItemTotalBadPixelCount.getSpec();
        }

        public Spec<Integer> getMFGCalOverallSpec() {
            return this.mItemMFGCalOverall.getSpec();
        }

        public Spec<Integer> getMFGCalZoneMaxSpec() {
            return this.mItemMFGCalZoneMax.getSpec();
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

    public FingerPrintTest_namsanTZ() {
        super(CLASS_NAME);
    }

    /* access modifiers changed from: private */
    public void updateNormalScanResult() {
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.mScreenWidth, this.mScreenHeight / 3);
        this.mResultView.setRowCol(16, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        int row_next = 0 + 1;
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(row_next, 0, "Rx Delta Test");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintNormalData.getRxDeltaSpec().lower);
        sb.append("~");
        sb.append(this.mFingerPrintNormalData.getRxDeltaSpec().upper);
        fingerPrintResultView_touch.setTextData(row_next, 2, sb.toString());
        this.mResultView.setTextData(row_next, 3, this.mFingerPrintNormalData.getRxDeltaValue());
        int row_next2 = row_next + 1;
        this.mResultView.setTextData(row_next, 4, this.mFingerPrintNormalData.getRxDeltaPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next2, 0, "AFE Balance Test");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintNormalData.getAFEBalanceSpec().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintNormalData.getAFEBalanceSpec().upper);
        fingerPrintResultView_touch2.setTextData(row_next2, 2, sb2.toString());
        this.mResultView.setTextData(row_next2, 3, this.mFingerPrintNormalData.getAFEBalanceValue());
        int row_next3 = row_next2 + 1;
        this.mResultView.setTextData(row_next2, 4, this.mFingerPrintNormalData.getAFEBalancePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next3, 0, "MFG cal Test");
        this.mResultView.setTextData(row_next3, 1, "Overall");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintNormalData.getMFGCalOverallSpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintNormalData.getMFGCalOverallSpec().upper);
        fingerPrintResultView_touch3.setTextData(row_next3, 2, sb3.toString());
        this.mResultView.setTextData(row_next3, 3, this.mFingerPrintNormalData.getMFGCalOverallValue());
        int row_next4 = row_next3 + 1;
        this.mResultView.setTextData(row_next3, 4, this.mFingerPrintNormalData.getMFGCalOverallPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next4, 1, "Zone ratio");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintNormalData.getMFGCalZoneMaxSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintNormalData.getMFGCalZoneMaxSpec().upper);
        fingerPrintResultView_touch4.setTextData(row_next4, 2, sb4.toString());
        this.mResultView.setTextData(row_next4, 3, this.mFingerPrintNormalData.getMFGCalZoneMaxValue());
        int row_next5 = row_next4 + 1;
        this.mResultView.setTextData(row_next4, 4, this.mFingerPrintNormalData.getMFGCalZoneMaxPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next5, 0, "Flash CheckSum Test");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintNormalData.getFlashCheckSumSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintNormalData.getFlashCheckSumSpec().upper);
        fingerPrintResultView_touch5.setTextData(row_next5, 2, sb5.toString());
        this.mResultView.setTextData(row_next5, 3, this.mFingerPrintNormalData.getFlashCheckSumValue());
        int row_next6 = row_next5 + 1;
        this.mResultView.setTextData(row_next5, 4, this.mFingerPrintNormalData.getFlashCheckSumPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next6, 0, "Open/Short");
        this.mResultView.setTextData(row_next6, 1, "TX short");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(this.mFingerPrintNormalData.getOpenShortTxShortSpec().lower);
        sb6.append("~");
        sb6.append(this.mFingerPrintNormalData.getOpenShortTxShortSpec().upper);
        fingerPrintResultView_touch6.setTextData(row_next6, 2, sb6.toString());
        this.mResultView.setTextData(row_next6, 3, this.mFingerPrintNormalData.getOpenShortTxShortValue());
        int row_next7 = row_next6 + 1;
        this.mResultView.setTextData(row_next6, 4, this.mFingerPrintNormalData.getOpenShortTxShortPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next7, 1, "RX short");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintNormalData.getOpenShortRxShortSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintNormalData.getOpenShortRxShortSpec().upper);
        fingerPrintResultView_touch7.setTextData(row_next7, 2, sb7.toString());
        this.mResultView.setTextData(row_next7, 3, this.mFingerPrintNormalData.getOpenShortRxShortValue());
        int row_next8 = row_next7 + 1;
        this.mResultView.setTextData(row_next7, 4, this.mFingerPrintNormalData.getOpenShortRxShortPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next8, 1, "Rx Range");
        FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mFingerPrintNormalData.getOpenShortRXRangeSpec().lower);
        sb8.append("~");
        sb8.append(this.mFingerPrintNormalData.getOpenShortRXRangeSpec().upper);
        fingerPrintResultView_touch8.setTextData(row_next8, 2, sb8.toString());
        this.mResultView.setTextData(row_next8, 3, this.mFingerPrintNormalData.getOpenShortRXRangeValue());
        int row_next9 = row_next8 + 1;
        this.mResultView.setTextData(row_next8, 4, this.mFingerPrintNormalData.getOpenShortRXRangePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next9, 1, "Rx Image");
        FingerPrintResultView_touch fingerPrintResultView_touch9 = this.mResultView;
        StringBuilder sb9 = new StringBuilder();
        sb9.append(this.mFingerPrintNormalData.getOpenShortRxImageSpec().lower);
        sb9.append("~");
        sb9.append(this.mFingerPrintNormalData.getOpenShortRxImageSpec().upper);
        fingerPrintResultView_touch9.setTextData(row_next9, 2, sb9.toString());
        this.mResultView.setTextData(row_next9, 3, this.mFingerPrintNormalData.getOpenShortRxImageValue());
        int row_next10 = row_next9 + 1;
        this.mResultView.setTextData(row_next9, 4, this.mFingerPrintNormalData.getOpenShortRxImagePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next10, 1, "TX Sum");
        FingerPrintResultView_touch fingerPrintResultView_touch10 = this.mResultView;
        StringBuilder sb10 = new StringBuilder();
        sb10.append(this.mFingerPrintNormalData.getOpenShortTxSumSpec().lower);
        sb10.append("~");
        sb10.append(this.mFingerPrintNormalData.getOpenShortTxSumSpec().upper);
        fingerPrintResultView_touch10.setTextData(row_next10, 2, sb10.toString());
        this.mResultView.setTextData(row_next10, 3, this.mFingerPrintNormalData.getOpenShortTxSumValue());
        int row_next11 = row_next10 + 1;
        this.mResultView.setTextData(row_next10, 4, this.mFingerPrintNormalData.getOpenShortTxSumPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next11, 0, "Pixel test");
        this.mResultView.setTextData(row_next11, 1, "Total bad pixel count");
        FingerPrintResultView_touch fingerPrintResultView_touch11 = this.mResultView;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(this.mFingerPrintNormalData.getTotalBadPixelCountSpec().lower);
        sb11.append("~");
        sb11.append(this.mFingerPrintNormalData.getTotalBadPixelCountSpec().upper);
        fingerPrintResultView_touch11.setTextData(row_next11, 2, sb11.toString());
        this.mResultView.setTextData(row_next11, 3, this.mFingerPrintNormalData.getTotalBadPixelCountValue());
        int row_next12 = row_next11 + 1;
        this.mResultView.setTextData(row_next11, 4, this.mFingerPrintNormalData.getTotalBadPixelCountPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next12, 1, "Rx max pixel fail");
        FingerPrintResultView_touch fingerPrintResultView_touch12 = this.mResultView;
        StringBuilder sb12 = new StringBuilder();
        sb12.append(this.mFingerPrintNormalData.getRxMaxPixelFailSpec().lower);
        sb12.append("~");
        sb12.append(this.mFingerPrintNormalData.getRxMaxPixelFailSpec().upper);
        fingerPrintResultView_touch12.setTextData(row_next12, 2, sb12.toString());
        this.mResultView.setTextData(row_next12, 3, this.mFingerPrintNormalData.getRxMaxPixelFailValue());
        int row_next13 = row_next12 + 1;
        this.mResultView.setTextData(row_next12, 4, this.mFingerPrintNormalData.getRxMaxPixelFailPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next13, 1, "Tx max pixel fail");
        FingerPrintResultView_touch fingerPrintResultView_touch13 = this.mResultView;
        StringBuilder sb13 = new StringBuilder();
        sb13.append(this.mFingerPrintNormalData.getTxMaxPixelFailSpec().lower);
        sb13.append("~");
        sb13.append(this.mFingerPrintNormalData.getTxMaxPixelFailSpec().upper);
        fingerPrintResultView_touch13.setTextData(row_next13, 2, sb13.toString());
        this.mResultView.setTextData(row_next13, 3, this.mFingerPrintNormalData.getTxMaxPixelFailValue());
        int row_next14 = row_next13 + 1;
        this.mResultView.setTextData(row_next13, 4, this.mFingerPrintNormalData.getTxMaxPixelFailPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next14, 1, "Area max pixel fail");
        FingerPrintResultView_touch fingerPrintResultView_touch14 = this.mResultView;
        StringBuilder sb14 = new StringBuilder();
        sb14.append(this.mFingerPrintNormalData.getAreaMaxPixelFailSpec().lower);
        sb14.append("~");
        sb14.append(this.mFingerPrintNormalData.getAreaMaxPixelFailSpec().upper);
        fingerPrintResultView_touch14.setTextData(row_next14, 2, sb14.toString());
        this.mResultView.setTextData(row_next14, 3, this.mFingerPrintNormalData.getAreaMaxPixelFailValue());
        int i = row_next14 + 1;
        this.mResultView.setTextData(row_next14, 4, this.mFingerPrintNormalData.getAreaMaxPixelFailPass() ? "PASS" : "FAIL");
    }

    private void initLayout() {
        Display mDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        this.mScreenWidth = outpoint.x;
        this.mScreenHeight = outpoint.y;
        this.mFingerPrintNormalData = new FingerPrintNormalData();
        this.mResultView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_resultview);
        this.mResultView.setVisibility(8);
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
        if (isOqcsbftt) {
            NVAccessor.setNV(409, NVAccessor.NV_VALUE_ENTER);
        }
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
                    int result = FingerPrintTest_namsanTZ.this.runSensorTest(100103);
                    if (result == FingerPrintTest_namsanTZ.this.IDENTIFY_FAILURE_SENSOR_CHANGED) {
                        FingerPrintTest_namsanTZ.this.STATE_REPLACEMENT_WARNING = true;
                        LtUtil.log_d(FingerPrintTest_namsanTZ.CLASS_NAME, "onResume", "STATE_REPLACEMENT_WARNING = true ");
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("SENSOR_TEST_NORMALSCAN_COMMAND Success / result : ");
                    sb.append(result);
                    LtUtil.log_d(FingerPrintTest_namsanTZ.CLASS_NAME, "onResume", sb.toString());
                }
            }, 500);
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
    public void readNormalScanLog() {
        BufferedReader bufferReader;
        String path = "/data/fpSnrTest/SynSnsrTest.log";
        boolean fis = null;
        try {
            fis = new File(path).exists();
            if (fis) {
                StringBuilder sb = new StringBuilder();
                sb.append(path);
                sb.append(" file Exist");
                LtUtil.log_i(CLASS_NAME, "readNormalScanLog", sb.toString());
                fis = new FileInputStream(path);
                bufferReader = new BufferedReader(new InputStreamReader(fis));
            } else {
                byte[] outbuffer = new byte[30720];
                String str = "";
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
                bufferReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(readStr2.getBytes())));
            }
            String str2 = "";
            while (true) {
                String readLine = bufferReader.readLine();
                String readStr3 = readLine;
                if (readLine == null) {
                    break;
                }
                String address = "";
                String data = "";
                String str3 = "";
                try {
                    String[] splitStr = readStr3.split(",");
                    address = splitStr[0].trim();
                    data = splitStr[1].trim();
                } catch (IndexOutOfBoundsException e2) {
                    e2.printStackTrace();
                }
                if ("14200".equals(address)) {
                    this.mFingerPrintNormalData.addRxDelta(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addRxDelta");
                } else if ("13840".equals(address)) {
                    this.mFingerPrintNormalData.addAFEBalance(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addAFEBalance");
                } else if ("14170".equals(address)) {
                    this.mFingerPrintNormalData.addOpenShortRXRange(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addOpenShortRXRange");
                } else if ("14160".equals(address)) {
                    this.mFingerPrintNormalData.addOpenShortRxImage(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addOpenShortRxImage");
                } else if ("14030".equals(address)) {
                    this.mFingerPrintNormalData.addOpenShortTxSum(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addOpenShortTxSum");
                } else if ("14040".equals(address)) {
                    this.mFingerPrintNormalData.addOpenShortTxShort(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addOpenShortTxShort");
                } else if ("14050".equals(address)) {
                    this.mFingerPrintNormalData.addOpenShortRxShort(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addOpenShortRxShort");
                } else if ("6070".equals(address)) {
                    this.mFingerPrintNormalData.addRxMaxPixelFail(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addRxMaxPixelFail");
                } else if ("6170".equals(address)) {
                    this.mFingerPrintNormalData.addTxMaxPixelFail(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addTxMaxPixelFail");
                } else if ("7030".equals(address)) {
                    this.mFingerPrintNormalData.addAreaMaxPixelFail(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addAreaMaxPixelFail");
                } else if ("14670".equals(address)) {
                    this.mFingerPrintNormalData.addTotalBadPixelCount(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addTotalBadPixelCount");
                } else if ("25030".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalOverall(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalOverall");
                } else if ("25040".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(0, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 0");
                } else if ("25050".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(1, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 1");
                } else if ("25060".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(2, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 2");
                } else if ("25070".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(3, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 3");
                } else if ("25080".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(4, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 4");
                } else if ("25090".equals(address)) {
                    this.mFingerPrintNormalData.addMFGCalZoneRatio(5, Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addMFGCalZoneRatio 5");
                } else if ("31060".equals(address)) {
                    this.mFingerPrintNormalData.addFlashCheckSum(Integer.parseInt(data));
                    LtUtil.log_i(CLASS_NAME, "readNormalScanLog", "addFlashCheckSum");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e3) {
                    LtUtil.log_e(e3);
                }
            }
        } catch (IOException e4) {
            e4.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e5) {
                    LtUtil.log_e(e5);
                }
            }
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
            public void onRequested(int msgId) {
                switch (msgId) {
                    case FingerPrintTest_namsanTZ.FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START_GOOGLE /*10008*/:
                        LtUtil.log_d(FingerPrintTest_namsanTZ.CLASS_NAME, "runSensorTest", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                        FingerPrintTest_namsanTZ.this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
                        FingerPrintTest_namsanTZ.this.handler.post(FingerPrintTest_namsanTZ.this.mUpdateMessageRunnable);
                        return;
                    case FingerPrintTest_namsanTZ.FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END_GOOGLE /*10009*/:
                        LtUtil.log_d(FingerPrintTest_namsanTZ.CLASS_NAME, "runSensorTest", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                        if (FingerPrintTest_namsanTZ.this.m_CurrentOperation == 1014) {
                            FingerPrintTest_namsanTZ.this.m_Message = "Normal data scan finished";
                            FingerPrintTest_namsanTZ.this.handler.post(FingerPrintTest_namsanTZ.this.mUpdateMessageRunnable);
                            FingerPrintTest_namsanTZ.this.readNormalScanLog();
                            FingerPrintTest_namsanTZ.this.handler.post(FingerPrintTest_namsanTZ.this.mUpdateNormalDataGraphRunnable);
                            if (FingerPrintTest_namsanTZ.this.STATE_REPLACEMENT_WARNING) {
                                FingerPrintTest_namsanTZ.this.handler.post(FingerPrintTest_namsanTZ.this.mShowWarningPopupRunnable);
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        LtUtil.log_d(FingerPrintTest_namsanTZ.CLASS_NAME, "onFingerprintEvent", "Event is : UNKNOWN_EVT_MESSAGE");
                        return;
                }
            }
        });
    }
}
