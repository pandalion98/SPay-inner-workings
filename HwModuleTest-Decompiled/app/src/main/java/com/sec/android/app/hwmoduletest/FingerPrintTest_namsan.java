package com.sec.android.app.hwmoduletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
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
import com.synaptics.fingerprint.namsan.DeviceInfo;
import com.synaptics.fingerprint.namsan.Fingerprint;
import com.synaptics.fingerprint.namsan.FingerprintCore.EventListener;
import com.synaptics.fingerprint.namsan.FingerprintEvent;
import com.synaptics.fingerprint.namsan.SensorTest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class FingerPrintTest_namsan extends BaseActivity implements EventListener {
    private static final String CLASS_NAME = "FingerPrintTest_namsan";
    private static String scriptID;
    private final long BACK_KEY_EVENT_TIMELAG = 2000;
    private final int OPERATION_REQUEST_DEVICEINFO = 100;
    private final int OPERATION_REQUEST_NORMALSCAN_DATA = 101;
    private final int OPERATION_REQUEST_SHUTDOWN = 103;
    private final int OPERATION_REQUEST_SNR_DATA = 102;
    private final int STATE_NORMALSCAN = 1014;
    private final int STATE_NORMALSCAN_FINISH = 1015;
    private final int STATE_SHUTDOWN = 3001;
    private final int STATE_SNR_SCAN = 1016;
    private final int STATE_SNR_SCAN_PROCESSING = 1018;
    private final String TAG = CLASS_NAME;
    private final String USER_ID = "Test";
    Handler handler = new Handler();
    private boolean isFinished = false;
    /* access modifiers changed from: private */
    public ImageView iv_snrimage;
    private Runnable mCleanupFingerprintRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsan.this.cleanUpFingerprint();
            FingerPrintTest_namsan.this.finish();
        }
    };
    /* access modifiers changed from: private */
    public Runnable mClosePutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsan.this.tv_msg1.setVisibility(8);
            FingerPrintTest_namsan.this.tv_msg2.setVisibility(0);
            FingerPrintTest_namsan.this.m_gotermbutton.setVisibility(8);
        }
    };
    private DeviceInfo mDeviceInfo;
    /* access modifiers changed from: private */
    public FingerPrintNormalData mFingerPrintNormalData;
    /* access modifiers changed from: private */
    public FingerPrintSNRData mFingerPrintSNRData;
    /* access modifiers changed from: private */
    public Fingerprint mFingerprint;
    private boolean mIsLongPress = false;
    /* access modifiers changed from: private */
    public Semaphore mLock = new Semaphore(1);
    private long mPrevBackKeyEventTime = -1;
    private FingerPrintResultView_touch mResultView;
    private FingerPrintResultView_touch mSNRZoneView;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    /* access modifiers changed from: private */
    public Runnable mShowPutFingerDialogRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsan.this.tv_msg1.setText(FingerPrintTest_namsan.this.m_PopupMessage);
            FingerPrintTest_namsan.this.tv_msg1.setVisibility(0);
            FingerPrintTest_namsan.this.tv_msg2.setVisibility(8);
            FingerPrintTest_namsan.this.m_gotermbutton.setText("O K");
            FingerPrintTest_namsan.this.m_gotermbutton.setVisibility(0);
        }
    };
    private String mTest;
    private Thread mThread;
    /* access modifiers changed from: private */
    public Runnable mUpdateImageRunnable = new Runnable() {
        public void run() {
            Bitmap bitmap = BitmapFactory.decodeFile("/data/SNR.bmp");
            FingerPrintTest_namsan.this.iv_snrimage.setVisibility(0);
            FingerPrintTest_namsan.this.iv_snrimage.setImageBitmap(bitmap);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateMessageRunnable = new Runnable() {
        public void run() {
            FingerPrintTest_namsan.this.tv_msg2.setText(FingerPrintTest_namsan.this.m_Message);
        }
    };
    private Runnable mUpdateNormalDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_namsan.this.mFingerPrintNormalData.isReadDone()) {
                FingerPrintTest_namsan.this.updateNormalScanResult();
                return;
            }
            FingerPrintTest_namsan.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_namsan.this.tv_result.setVisibility(0);
            FingerPrintTest_namsan.this.tv_result.setText(FingerPrintTest_namsan.this.m_ResultMessage);
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateResultMessageRunnable = new Runnable() {
        public void run() {
            if (!FingerPrintTest_namsan.this.m_ResultMessage.isEmpty()) {
                FingerPrintTest_namsan.this.tv_result.setVisibility(0);
                FingerPrintTest_namsan.this.tv_result.setText(FingerPrintTest_namsan.this.m_ResultMessage);
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mUpdateSNRDataGraphRunnable = new Runnable() {
        public void run() {
            if (FingerPrintTest_namsan.this.mFingerPrintSNRData.isReadDone()) {
                FingerPrintTest_namsan.this.updateSNRResult();
                return;
            }
            FingerPrintTest_namsan.this.m_ResultMessage = "Cannot found Data log!!";
            FingerPrintTest_namsan.this.tv_result.setVisibility(0);
            FingerPrintTest_namsan.this.tv_result.setText(FingerPrintTest_namsan.this.m_ResultMessage);
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
    /* access modifiers changed from: private */
    public Button m_gotermbutton;
    private String sdk_version;
    /* access modifiers changed from: private */
    public TextView tv_msg1;
    /* access modifiers changed from: private */
    public TextView tv_msg2;
    /* access modifiers changed from: private */
    public TextView tv_result;

    private static class FingerPrintNormalData {
        private static final int ITEM_COUNT = 19;
        private static final int ZONE_COUNT = 6;
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
            StringBuilder sb = new StringBuilder();
            sb.append("FingerPrintNormalData count : ");
            sb.append(this.isDone);
            LtUtil.log_d(FingerPrintTest_namsan.CLASS_NAME, "isReadDone", sb.toString());
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
            if (value >= ((Integer) spec.lower).intValue() && value <= ((Integer) spec.upper).intValue()) {
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

    private static class FingerPrintSNRData {
        private static final int ITEM_COUNT = 28;
        private static final int ZONE_COUNT = 6;
        private int isDone = 0;
        private SNRItem mItemNoise;
        private SNRItem mItemNoiseZoneMax;
        private SNRItem mItemPresent;
        private SNRItem mItemRatioZoneMin;
        private SNRItem mItemSNR;
        private SNRItem mItemSNRZoneMin;
        private SNRItem mItemSignal;
        private SNRItem mItemSignalZoneMin;
        private int[] mNoiseZone = new int[6];
        private int[] mRatioZone = new int[6];
        private String[] mSNRZone = new String[6];
        private int[] mSignalZone = new int[6];
        private String mSpecResultFile;
        private int noise_count = 0;
        private int noise_max = -1;
        private int overall = 0;
        private int ratio_count = 0;
        private int ratio_min = 99999;
        private int ratio_result = -1;
        private int signal_count = 0;
        private int signal_min = 99999;
        private int snr_count = 0;
        private int snr_min = 99999;

        private static class SNRItem {
            private Spec<Float> mFloatSpec;
            private Spec<Integer> mIntSpec;
            private boolean mPass;
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

            public void setFloatSpec(Spec<Float> spec) {
                this.mFloatSpec = spec;
            }

            public void setIntSpec(Spec<Integer> spec) {
                this.mIntSpec = spec;
            }

            public String getValue() {
                return this.mValue;
            }

            public boolean getPass() {
                return this.mPass;
            }

            public Spec<Float> getFloatSpec() {
                return this.mFloatSpec;
            }

            public Spec<Integer> getIntSpec() {
                return this.mIntSpec;
            }
        }

        public boolean isReadDone() {
            StringBuilder sb = new StringBuilder();
            sb.append("FingerPrintSNRData count : ");
            sb.append(this.isDone);
            LtUtil.log_d(FingerPrintTest_namsan.CLASS_NAME, "isReadDone", sb.toString());
            return this.isDone == 28;
        }

        public void addRatioZone(int index, int value) {
            this.mRatioZone[index] = value;
            this.ratio_count++;
            if (this.ratio_min > this.mRatioZone[index]) {
                this.ratio_min = this.mRatioZone[index];
            }
            if (this.ratio_count == 6) {
                this.mItemRatioZoneMin = new SNRItem();
                this.ratio_result = this.ratio_min;
                addItem(this.mItemRatioZoneMin, this.ratio_result, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RATIO_ZONE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_RATIO_ZONE_MAX_NAMSAN))));
            }
            this.isDone++;
        }

        public void addSignalZone(int index, int value) {
            this.mSignalZone[index] = value;
            this.signal_count++;
            if (this.signal_min > this.mSignalZone[index]) {
                this.signal_min = this.mSignalZone[index];
            }
            if (this.signal_count == 6) {
                this.mItemSignalZoneMin = new SNRItem();
                addItem(this.mItemSignalZoneMin, this.signal_min, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_ZONE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_ZONE_MAX_NAMSAN))));
            }
            this.isDone++;
        }

        public void addNoiseZone(int index, int value) {
            this.mNoiseZone[index] = value;
            this.noise_count++;
            if (this.noise_max < this.mNoiseZone[index]) {
                this.noise_max = this.mNoiseZone[index];
            }
            if (this.noise_count == 6) {
                this.mItemNoiseZoneMax = new SNRItem();
                addItem(this.mItemNoiseZoneMax, this.noise_max, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_ZONE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_ZONE_MAX_NAMSAN))));
            }
            this.isDone++;
        }

        public void addSNRZone(int index, int value) {
            this.mSNRZone[index] = String.valueOf(value);
            this.snr_count++;
            if (this.snr_min > Integer.parseInt(this.mSNRZone[index])) {
                this.snr_min = Integer.parseInt(this.mSNRZone[index]);
            }
            if (this.snr_count == 6) {
                this.mItemSNRZoneMin = new SNRItem();
                addItem(this.mItemSNRZoneMin, this.snr_min, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_ZONE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_ZONE_MAX_NAMSAN))));
            }
            this.isDone++;
        }

        public void addSignal(int value) {
            this.mItemSignal = new SNRItem();
            addItem(this.mItemSignal, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SIGNAL_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addNoise(int value) {
            this.mItemNoise = new SNRItem();
            addItem(this.mItemNoise, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_NOISE_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addSNR(int value) {
            this.mItemSNR = new SNRItem();
            addItem(this.mItemSNR, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_SNR_MAX_NAMSAN))));
            this.isDone++;
        }

        public void addPresent(int value) {
            this.overall = value;
            this.mItemPresent = new SNRItem();
            addItem(this.mItemPresent, value, new Spec(Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PRESENT_MIN_NAMSAN)), Integer.valueOf(com.sec.xmldata.support.Support.Spec.getInt(com.sec.xmldata.support.Support.Spec.FINGERPRINT_PRESENT_MAX_NAMSAN))));
            this.isDone++;
        }

        private void addItem(SNRItem item, int value, Spec<Integer> spec) {
            String strValue = String.valueOf(value);
            boolean pass = false;
            if (value >= ((Integer) spec.lower).intValue() && value <= ((Integer) spec.upper).intValue()) {
                pass = true;
            }
            item.setIntSpec(spec);
            item.setValue(strValue);
            item.setPass(pass);
        }

        public String getPresentValue() {
            return this.mItemPresent.getValue();
        }

        public String getRatioZoneMinValue() {
            return this.mItemRatioZoneMin.getValue();
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

        public String getSignalZoneMinValue() {
            return this.mItemSignalZoneMin.getValue();
        }

        public String getNoiseZoneMaxValue() {
            return this.mItemNoiseZoneMax.getValue();
        }

        public String getSNRZoneMinValue() {
            return this.mItemSNRZoneMin.getValue();
        }

        public String getSNRZoneValue(int index) {
            return this.mSNRZone[index];
        }

        public boolean getPresentPass() {
            return this.mItemPresent.getPass();
        }

        public boolean getRatioZoneMinPass() {
            return this.mItemRatioZoneMin.getPass();
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

        public boolean getSignalZoneMinPass() {
            return this.mItemSignalZoneMin.getPass();
        }

        public boolean getNoiseZoneMaxPass() {
            return this.mItemNoiseZoneMax.getPass();
        }

        public boolean getSNRZoneMinPass() {
            return this.mItemSNRZoneMin.getPass();
        }

        public Spec<Integer> getPresentSpec() {
            return this.mItemPresent.getIntSpec();
        }

        public Spec<Integer> getRatioZoneMinSpec() {
            return this.mItemRatioZoneMin.getIntSpec();
        }

        public Spec<Integer> getSignalSpec() {
            return this.mItemSignal.getIntSpec();
        }

        public Spec<Integer> getNoiseSpec() {
            return this.mItemNoise.getIntSpec();
        }

        public Spec<Integer> getSNRSpec() {
            return this.mItemSNR.getIntSpec();
        }

        public Spec<Integer> getSignalZoneMinSpec() {
            return this.mItemSignalZoneMin.getIntSpec();
        }

        public Spec<Integer> getNoiseZoneMaxSpec() {
            return this.mItemNoiseZoneMax.getIntSpec();
        }

        public Spec<Integer> getSNRZoneMinSpec() {
            return this.mItemSNRZoneMin.getIntSpec();
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

    public FingerPrintTest_namsan() {
        super(CLASS_NAME);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3) {
            if (this.m_gotermbutton.isShown()) {
                LtUtil.log_i(CLASS_NAME, "onKeyDown", "Homekey pressed");
                this.m_CurrentOperation = 1018;
                excuteOperation(102);
                this.handler.post(this.mClosePutFingerDialogRunnable);
            }
        } else if (keyCode == 4) {
            StringBuilder sb = new StringBuilder();
            sb.append("KEYCODE_BACK => Prev : ");
            sb.append(this.mPrevBackKeyEventTime);
            sb.append(", Curr : ");
            sb.append(event.getEventTime());
            sb.append(" => Time Lag : ");
            sb.append(event.getEventTime() - this.mPrevBackKeyEventTime);
            sb.append(" [");
            sb.append(2000);
            sb.append("]");
            LtUtil.log_d(CLASS_NAME, "onKeyDown", sb.toString());
            if (!this.mIsLongPress) {
                this.mIsLongPress = event.isLongPress();
            }
            if (!this.mIsLongPress) {
                if (this.mPrevBackKeyEventTime == -1) {
                    this.mPrevBackKeyEventTime = event.getEventTime();
                } else if (event.getEventTime() - this.mPrevBackKeyEventTime < 2000) {
                    shutdownFingerprint();
                } else {
                    this.mPrevBackKeyEventTime = event.getEventTime();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

    /* access modifiers changed from: private */
    public void updateSNRResult() {
        this.mSNRZoneView.setVisibility(0);
        this.mSNRZoneView.setCanvasSize(this.mScreenWidth / 2, this.mScreenHeight / 4);
        this.mSNRZoneView.setTextSize(this.mScreenWidth / 32);
        this.mSNRZoneView.setRowCol(2, 3);
        for (int i = 0; i < 6; i++) {
            this.mSNRZoneView.setTextData(i / 3, i % 3, this.mFingerPrintSNRData.getSNRZoneValue(i));
        }
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.mScreenWidth, this.mScreenHeight / 4);
        this.mResultView.setRowCol(9, 5);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 2, "Spec");
        int row_next = 0 + 1;
        this.mResultView.setTextData(0, 3, "Result");
        this.mResultView.setTextData(row_next, 0, "Imperfection");
        this.mResultView.setTextData(row_next, 1, "Present");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintSNRData.getPresentSpec().lower);
        sb.append("~");
        sb.append(this.mFingerPrintSNRData.getPresentSpec().upper);
        fingerPrintResultView_touch.setTextData(row_next, 2, sb.toString());
        this.mResultView.setTextData(row_next, 3, this.mFingerPrintSNRData.getPresentValue());
        int row_next2 = row_next + 1;
        this.mResultView.setTextData(row_next, 4, this.mFingerPrintSNRData.getPresentPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next2, 1, "Zone ratio");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintSNRData.getRatioZoneMinSpec().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintSNRData.getRatioZoneMinSpec().upper);
        fingerPrintResultView_touch2.setTextData(row_next2, 2, sb2.toString());
        this.mResultView.setTextData(row_next2, 3, this.mFingerPrintSNRData.getRatioZoneMinValue());
        int row_next3 = row_next2 + 1;
        this.mResultView.setTextData(row_next2, 4, this.mFingerPrintSNRData.getRatioZoneMinPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next3, 0, "Signal");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintSNRData.getSignalSpec().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintSNRData.getSignalSpec().upper);
        fingerPrintResultView_touch3.setTextData(row_next3, 2, sb3.toString());
        this.mResultView.setTextData(row_next3, 3, this.mFingerPrintSNRData.getSignalValue());
        int row_next4 = row_next3 + 1;
        this.mResultView.setTextData(row_next3, 4, this.mFingerPrintSNRData.getSignalPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next4, 0, "Noise");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintSNRData.getNoiseSpec().upper);
        fingerPrintResultView_touch4.setTextData(row_next4, 2, sb4.toString());
        this.mResultView.setTextData(row_next4, 3, this.mFingerPrintSNRData.getNoiseValue());
        int row_next5 = row_next4 + 1;
        this.mResultView.setTextData(row_next4, 4, this.mFingerPrintSNRData.getNoisePass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next5, 0, "SNR");
        FingerPrintResultView_touch fingerPrintResultView_touch5 = this.mResultView;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.mFingerPrintSNRData.getSNRSpec().lower);
        sb5.append("~");
        sb5.append(this.mFingerPrintSNRData.getSNRSpec().upper);
        fingerPrintResultView_touch5.setTextData(row_next5, 2, sb5.toString());
        this.mResultView.setTextData(row_next5, 3, this.mFingerPrintSNRData.getSNRValue());
        int row_next6 = row_next5 + 1;
        this.mResultView.setTextData(row_next5, 4, this.mFingerPrintSNRData.getSNRPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next6, 0, "Zone Signal(min)");
        FingerPrintResultView_touch fingerPrintResultView_touch6 = this.mResultView;
        StringBuilder sb6 = new StringBuilder();
        sb6.append(this.mFingerPrintSNRData.getSignalZoneMinSpec().lower);
        sb6.append("~");
        sb6.append(this.mFingerPrintSNRData.getSignalZoneMinSpec().upper);
        fingerPrintResultView_touch6.setTextData(row_next6, 2, sb6.toString());
        this.mResultView.setTextData(row_next6, 3, this.mFingerPrintSNRData.getSignalZoneMinValue());
        int row_next7 = row_next6 + 1;
        this.mResultView.setTextData(row_next6, 4, this.mFingerPrintSNRData.getSignalZoneMinPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next7, 0, "Zone Noise(max)");
        FingerPrintResultView_touch fingerPrintResultView_touch7 = this.mResultView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.mFingerPrintSNRData.getNoiseZoneMaxSpec().lower);
        sb7.append("~");
        sb7.append(this.mFingerPrintSNRData.getNoiseZoneMaxSpec().upper);
        fingerPrintResultView_touch7.setTextData(row_next7, 2, sb7.toString());
        this.mResultView.setTextData(row_next7, 3, this.mFingerPrintSNRData.getNoiseZoneMaxValue());
        int row_next8 = row_next7 + 1;
        this.mResultView.setTextData(row_next7, 4, this.mFingerPrintSNRData.getNoiseZoneMaxPass() ? "PASS" : "FAIL");
        this.mResultView.setTextData(row_next8, 0, "Zone SNR(min)");
        FingerPrintResultView_touch fingerPrintResultView_touch8 = this.mResultView;
        StringBuilder sb8 = new StringBuilder();
        sb8.append(this.mFingerPrintSNRData.getSNRZoneMinSpec().lower);
        sb8.append("~");
        sb8.append(this.mFingerPrintSNRData.getSNRZoneMinSpec().upper);
        fingerPrintResultView_touch8.setTextData(row_next8, 2, sb8.toString());
        this.mResultView.setTextData(row_next8, 3, this.mFingerPrintSNRData.getSNRZoneMinValue());
        int i2 = row_next8 + 1;
        this.mResultView.setTextData(row_next8, 4, this.mFingerPrintSNRData.getSNRZoneMinPass() ? "PASS" : "FAIL");
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (event.getActionMasked() == 5) {
            int index = (action & 65280) >> 8;
            StringBuilder sb = new StringBuilder();
            sb.append("ACTION_POINTER_DOWN index : ");
            sb.append(index);
            LtUtil.log_i(CLASS_NAME, "onTouchEvent", sb.toString());
            float x = event.getX(index);
            float y = event.getY(index);
            if (x > this.m_gotermbutton.getX() && x < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y > this.m_gotermbutton.getY() && y < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                LtUtil.log_i(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_DOWN pos is inside OK button ");
                this.m_gotermbutton.setPressed(true);
            }
        } else if (event.getActionMasked() == 6) {
            int index2 = (action & 65280) >> 8;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ACTION_POINTER_UP index : ");
            sb2.append(index2);
            LtUtil.log_i(CLASS_NAME, "onTouchEvent", sb2.toString());
            float x2 = event.getX(index2);
            float y2 = event.getY(index2);
            if (x2 > this.m_gotermbutton.getX() && x2 < this.m_gotermbutton.getX() + ((float) this.m_gotermbutton.getWidth()) && y2 > this.m_gotermbutton.getY() && y2 < this.m_gotermbutton.getY() + ((float) this.m_gotermbutton.getHeight())) {
                LtUtil.log_i(CLASS_NAME, "onTouchEvent", "Event.ACTION_POINTER_2_UP pos is inside OK button ");
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
        this.mScreenWidth = outpoint.x;
        this.mScreenHeight = outpoint.y;
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
                FingerPrintTest_namsan.this.m_CurrentOperation = 1018;
                FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mClosePutFingerDialogRunnable);
                FingerPrintTest_namsan.this.excuteOperation(102);
            }
        });
        LayoutParams params = this.iv_snrimage.getLayoutParams();
        params.height = this.mScreenHeight / 4;
        params.width = this.mScreenWidth / 2;
        this.iv_snrimage.setLayoutParams(params);
        RelativeLayout lContainerLayout = new RelativeLayout(this);
        lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        RelativeLayout.LayoutParams lButtonParams = new RelativeLayout.LayoutParams(this.mScreenWidth / 3, this.mScreenHeight / 8);
        lButtonParams.addRule(12);
        lButtonParams.addRule(14);
        lButtonParams.bottomMargin = this.mScreenHeight / 3;
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
        LtUtil.log_i(CLASS_NAME, "onCreate", "onCreate");
        this.mFingerprint = new Fingerprint(this, this);
        this.mFingerprint.registerListener(this);
        this.sdk_version = this.mFingerprint.getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("Version : ");
        sb.append(this.sdk_version);
        LtUtil.log_i(CLASS_NAME, "onCreate", sb.toString());
        this.mTest = getIntent().getStringExtra("Action");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getSensorStatus : ");
        sb2.append(this.mFingerprint.getSensorStatus());
        LtUtil.log_i(CLASS_NAME, "onCreate", sb2.toString());
        if (this.mFingerprint.getSensorStatus() == 0) {
            LtUtil.log_i(CLASS_NAME, "onCreate", "getSensorStatus : STATUS_OK");
            if ("NORMALDATA".equals(this.mTest)) {
                this.m_CurrentOperation = 1014;
                excuteOperation(101);
            } else if ("SNR".equals(this.mTest)) {
                this.m_CurrentOperation = 1016;
                excuteOperation(101);
            } else if ("SENSORINFO".equals(this.mTest)) {
                excuteOperation(100);
            }
        } else {
            LtUtil.log_i(CLASS_NAME, "onCreate", "getSensorStatus : STATUS_FAIL");
            this.m_ResultMessage = "Fingerprint initializing Fail";
            this.handler.post(this.mUpdateResultMessageRunnable);
        }
    }

    public void onEvent(FingerprintEvent event) {
        String result_MT;
        if (event != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Event Id = ");
            sb.append(event.eventId);
            LtUtil.log_i(CLASS_NAME, "onEvent", sb.toString());
            int i = event.eventId;
            if (i != 405) {
                switch (i) {
                    case 411:
                        LtUtil.log_i(CLASS_NAME, "onEvent", "Event is : EVT_SNSR_TEST_SCRIPT_START");
                        if (!this.isFinished) {
                            this.m_Message = "Script start\nPlease wait...\nDon't exit until finish\n\nIf not response in 5 SEC,\nPlease restart test";
                            this.handler.post(this.mUpdateMessageRunnable);
                            return;
                        }
                        return;
                    case 412:
                        LtUtil.log_i(CLASS_NAME, "onEvent", "Event is : EVT_SNSR_TEST_SCRIPT_END");
                        if (this.m_CurrentOperation == 1014) {
                            this.m_Message = "Normal data scan finished";
                            this.handler.post(this.mUpdateMessageRunnable);
                            readNormalScanLogFile();
                            this.handler.post(this.mUpdateNormalDataGraphRunnable);
                            return;
                        } else if (this.isFinished) {
                            LtUtil.log_d(CLASS_NAME, "onEvent", "test end, cleanup Fingerprint");
                            this.handler.post(this.mCleanupFingerprintRunnable);
                            return;
                        } else {
                            this.handler.postDelayed(new Runnable() {
                                public void run() {
                                    if (FingerPrintTest_namsan.this.m_CurrentOperation == 1018) {
                                        FingerPrintTest_namsan.this.m_Message = "SNR Test data scan finished";
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mUpdateMessageRunnable);
                                        FingerPrintTest_namsan.this.readSNRLogFile();
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mUpdateSNRDataGraphRunnable);
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mUpdateImageRunnable);
                                    } else if (FingerPrintTest_namsan.this.m_CurrentOperation == 1016) {
                                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "onEvent", "PUT_STIMULUS_ON_SENSOR");
                                        FingerPrintTest_namsan.this.m_PopupMessage = "Put stimulus on the sensor\nand press OK or HomeButton";
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mShowPutFingerDialogRunnable);
                                    } else {
                                        FingerPrintTest_namsan.this.m_Message = "Script end";
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mUpdateMessageRunnable);
                                        FingerPrintTest_namsan.this.handler.post(FingerPrintTest_namsan.this.mUpdateResultMessageRunnable);
                                    }
                                }
                            }, 100);
                            return;
                        }
                    default:
                        LtUtil.log_i(CLASS_NAME, "onEvent", "Event is : UNKNOWN_EVT_MESSAGE");
                        return;
                }
            } else {
                LtUtil.log_i(CLASS_NAME, "onEvent", "Event is : EVT_DEVICE_INFO");
                this.mDeviceInfo = (DeviceInfo) event.eventData;
                if (!"SENSORINFO".equals(this.mTest) || this.mDeviceInfo == null) {
                    LtUtil.log_i(CLASS_NAME, "onEvent", "mDeviceInfo is null");
                    return;
                }
                this.m_ResultMessage = "FwVersion : ";
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.m_ResultMessage);
                sb2.append(this.mDeviceInfo.fwVersion);
                this.m_ResultMessage = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.m_ResultMessage);
                sb3.append("\nConfigId1 : ");
                this.m_ResultMessage = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.m_ResultMessage);
                sb4.append(this.mDeviceInfo.cfgId1);
                this.m_ResultMessage = sb4.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.m_ResultMessage);
                sb5.append("\nConfigId2 : ");
                this.m_ResultMessage = sb5.toString();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(this.m_ResultMessage);
                sb6.append(this.mDeviceInfo.cfgId2);
                this.m_ResultMessage = sb6.toString();
                String sdk_split = this.sdk_version.substring(this.sdk_version.lastIndexOf(".") + 1, this.sdk_version.length());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("sdk_split : ");
                sb7.append(sdk_split);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb7.toString());
                StringBuilder sb8 = new StringBuilder();
                sb8.append("moduleTest/CalTest Value : ");
                sb8.append(this.mDeviceInfo.testInfo);
                LtUtil.log_d(CLASS_NAME, "mDeviceInfo", sb8.toString());
                String str = "";
                int moduleTest = this.mDeviceInfo.testInfo & 3;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("moduleTest_result : ");
                sb9.append(moduleTest);
                LtUtil.log_i(CLASS_NAME, "mDeviceInfo", sb9.toString());
                if (moduleTest == 0) {
                    result_MT = "Not tested";
                } else if (moduleTest == 3) {
                    result_MT = "Pass";
                } else {
                    result_MT = "Fail";
                }
                StringBuilder sb10 = new StringBuilder();
                sb10.append(this.m_ResultMessage);
                sb10.append("\nModule Test : ");
                this.m_ResultMessage = sb10.toString();
                StringBuilder sb11 = new StringBuilder();
                sb11.append(this.m_ResultMessage);
                sb11.append(result_MT);
                this.m_ResultMessage = sb11.toString();
                StringBuilder sb12 = new StringBuilder();
                sb12.append(this.m_ResultMessage);
                sb12.append("\nUID : ");
                this.m_ResultMessage = sb12.toString();
                StringBuilder sb13 = new StringBuilder();
                sb13.append(this.m_ResultMessage);
                sb13.append(byteArrayToHex(this.mDeviceInfo.serialNumber));
                this.m_ResultMessage = sb13.toString();
                this.m_Message = "Sensor Infomation";
                this.handler.post(this.mUpdateMessageRunnable);
                this.handler.post(this.mUpdateResultMessageRunnable);
            }
        }
    }

    private String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(ba.length * 2);
        for (int x = 0; x < ba.length; x++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Integer.toHexString(0xff & ba[x] : ");
            sb2.append(Integer.toHexString(ba[x] & 255));
            LtUtil.log_i(CLASS_NAME, "byteArrayToHex", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("0");
            sb3.append(Integer.toHexString(ba[x] & 255));
            String hexNumber = sb3.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("hexNumber : ");
            sb4.append(hexNumber);
            LtUtil.log_i(CLASS_NAME, "byteArrayToHex", sb4.toString());
            sb.append(hexNumber.substring(hexNumber.length() - 2));
            sb.append(" ");
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public void excuteOperation(final int operation) {
        this.mThread = new Thread(new Runnable() {
            public void run() {
                try {
                    if (!FingerPrintTest_namsan.this.mLock.tryAcquire(500, TimeUnit.MILLISECONDS)) {
                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "mLock.tryAcquire( fail!");
                        return;
                    }
                    if (operation == 101) {
                        SensorTest snrTest = new SensorTest();
                        int dataLogopt = 0 | 1 | 2 | 64;
                        snrTest.scriptId = 6011;
                        snrTest.options = 16;
                        snrTest.dataLogOpt = dataLogopt;
                        snrTest.unitId = 0;
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
                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", sb.toString());
                        if (FingerPrintTest_namsan.this.mFingerprint.request(11, snrTest) == 0) {
                            LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_NORMALSCAN_DATA Success ");
                        } else {
                            LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_NORMALSCAN_DATA FAIL!! ");
                        }
                    } else if (operation == 102) {
                        SensorTest snrTest2 = new SensorTest();
                        int dataLogopt2 = 0 | 1 | 2 | 64 | 4096;
                        snrTest2.scriptId = 6012;
                        snrTest2.options = 16;
                        snrTest2.dataLogOpt = dataLogopt2;
                        snrTest2.unitId = 0;
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
                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", sb2.toString());
                        if (FingerPrintTest_namsan.this.mFingerprint.request(11, snrTest2) == 0) {
                            LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_SNR_DATA Success ");
                        } else {
                            LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_SNR_DATA FAIL!! ");
                        }
                    } else if (operation == 103) {
                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_SHUTDOWN");
                        SensorTest shutdownTest = new SensorTest();
                        shutdownTest.scriptId = 6666;
                        FingerPrintTest_namsan.this.mFingerprint.request(11, shutdownTest);
                    } else if (operation == 100) {
                        LtUtil.log_i(FingerPrintTest_namsan.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_DEVICEINFO");
                        FingerPrintTest_namsan.this.mFingerprint.request(12, null);
                    }
                    FingerPrintTest_namsan.this.mLock.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.mThread.start();
    }

    private void readNormalScanLogFile() {
        String path = "/data/SynSnsrTest.log";
        if (new File(path).exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(" file Exist");
            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", sb.toString());
            String fis = null;
            try {
                fis = new FileInputStream(path);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String str = "";
                while (true) {
                    fis = bufferReader.readLine();
                    String readStr = fis;
                    if (fis != null) {
                        String address = "";
                        String data = "";
                        String str2 = "";
                        try {
                            String[] splitStr = readStr.split(",");
                            address = splitStr[0].trim();
                            data = splitStr[1].trim();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if ("14200".equals(address)) {
                            this.mFingerPrintNormalData.addRxDelta(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addRxDelta");
                        } else if ("13840".equals(address)) {
                            this.mFingerPrintNormalData.addAFEBalance(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addAFEBalance");
                        } else if ("14170".equals(address)) {
                            this.mFingerPrintNormalData.addOpenShortRXRange(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addOpenShortRXRange");
                        } else if ("14160".equals(address)) {
                            this.mFingerPrintNormalData.addOpenShortRxImage(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addOpenShortRxImage");
                        } else if ("14030".equals(address)) {
                            this.mFingerPrintNormalData.addOpenShortTxSum(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addOpenShortTxSum");
                        } else if ("14040".equals(address)) {
                            this.mFingerPrintNormalData.addOpenShortTxShort(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addOpenShortTxShort");
                        } else if ("14050".equals(address)) {
                            this.mFingerPrintNormalData.addOpenShortRxShort(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addOpenShortRxShort");
                        } else if ("6070".equals(address)) {
                            this.mFingerPrintNormalData.addRxMaxPixelFail(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addRxMaxPixelFail");
                        } else if ("6170".equals(address)) {
                            this.mFingerPrintNormalData.addTxMaxPixelFail(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addTxMaxPixelFail");
                        } else if ("7030".equals(address)) {
                            this.mFingerPrintNormalData.addAreaMaxPixelFail(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addAreaMaxPixelFail");
                        } else if ("14670".equals(address)) {
                            this.mFingerPrintNormalData.addTotalBadPixelCount(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addTotalBadPixelCount");
                        } else if ("25030".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalOverall(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalOverall");
                        } else if ("25040".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(0, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 0");
                        } else if ("25050".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(1, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 1");
                        } else if ("25060".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(2, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 2");
                        } else if ("25070".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(3, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 3");
                        } else if ("25080".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(4, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 4");
                        } else if ("25090".equals(address)) {
                            this.mFingerPrintNormalData.addMFGCalZoneRatio(5, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addMFGCalZoneRatio 5");
                        } else if ("31060".equals(address)) {
                            this.mFingerPrintNormalData.addFlashCheckSum(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readNormalScanLogFile", "addFlashCheckSum");
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
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(" file Exist");
            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", sb.toString());
            String fis = null;
            try {
                fis = new FileInputStream(path);
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                String str = "";
                while (true) {
                    fis = bufferReader.readLine();
                    String readStr = fis;
                    if (fis != null) {
                        String address = "";
                        String data = "";
                        String str2 = "";
                        try {
                            String[] splitStr = readStr.split(",");
                            address = splitStr[0].trim();
                            data = splitStr[1].trim();
                            String pass = splitStr[2].trim();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if ("14840".equals(address)) {
                            this.mFingerPrintSNRData.addSignal(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignal");
                        } else if ("14850".equals(address)) {
                            this.mFingerPrintSNRData.addNoise(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoise");
                        } else if ("14830".equals(address)) {
                            this.mFingerPrintSNRData.addSNR(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNR");
                        } else if ("14660".equals(address)) {
                            this.mFingerPrintSNRData.addPresent(Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addPresent");
                        } else if ("14670".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(0, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 0");
                        } else if ("14680".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(1, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 1");
                        } else if ("14690".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(2, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 2");
                        } else if ("14700".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(3, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 3");
                        } else if ("14710".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(4, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 4");
                        } else if ("14720".equals(address)) {
                            this.mFingerPrintSNRData.addRatioZone(5, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addRatioZone 5");
                        } else if ("14860".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(0, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 0");
                        } else if ("14890".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(1, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 1");
                        } else if ("14920".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(2, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 2");
                        } else if ("14950".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(3, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 3");
                        } else if ("14980".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(4, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 4");
                        } else if ("15010".equals(address)) {
                            this.mFingerPrintSNRData.addSNRZone(5, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSNRZone 5");
                        } else if ("14870".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(0, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 0");
                        } else if ("14900".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(1, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 1");
                        } else if ("14930".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(2, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 2");
                        } else if ("14960".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(3, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 3");
                        } else if ("14990".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(4, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 4");
                        } else if ("15020".equals(address)) {
                            this.mFingerPrintSNRData.addSignalZone(5, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addSignalZone 5");
                        } else if ("14880".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(0, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 0");
                        } else if ("14910".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(1, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 1");
                        } else if ("14940".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(2, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 2");
                        } else if ("14970".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(3, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 3");
                        } else if ("15000".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(4, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 4");
                        } else if ("15030".equals(address)) {
                            this.mFingerPrintSNRData.addNoiseZone(5, Integer.parseInt(data));
                            LtUtil.log_i(CLASS_NAME, "readSNRLogFile", "addNoiseZone 5");
                        }
                    } else {
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

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_i(CLASS_NAME, "onPause", "onPause");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void shutdownFingerprint() {
        if (this.mFingerprint != null) {
            LtUtil.log_i(CLASS_NAME, "shutdownFingerprint", "");
            this.m_CurrentOperation = 3001;
            this.isFinished = true;
            excuteOperation(103);
        }
    }

    public void cleanUpFingerprint() {
        if (this.mFingerprint != null) {
            LtUtil.log_i(CLASS_NAME, "cleanUpFingerprint", "cleanUp");
            try {
                this.mLock.acquire();
                this.mFingerprint.cancel();
                this.mFingerprint.cleanUp();
                this.mFingerprint = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mLock.release();
        }
    }
}
