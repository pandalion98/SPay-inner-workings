package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import egistec.optical.csa.client.api.Fingerprint;
import egistec.optical.csa.client.api.Fingerprint.FingerprintEventListener;
import java.util.Locale;

public class FingerprintCalTest_egisOptical extends BaseActivity {
    private final int TEST_BRIGHTNESS = TestResultParser.TEST_TOKEN_LOCAL_BIG_BAD_PIXEL_NUM;
    private Button mButtonExit;
    /* access modifiers changed from: private */
    public Button mButtonStart;
    private FingerprintEventListener mFingerPrintEventListenerOptical;
    private FingerPrintSNRData mFingerPrintSNRData;
    private Fingerprint mFingerprintOptical;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private IBinder mIBinder = null;
    private int mIntPrevPanelBrightness = -1;
    private ImageView mIvFingerCircle;
    private ImageView mIvFingerRect;
    private ImageView mIvSnrImage;
    private BroadcastReceiver mReceiver = null;
    private FingerPrintResultView_touch mResultView;
    /* access modifiers changed from: private */
    public STEP mSTEP = STEP.NOTEST;
    private int mScreenHeight;
    private int mScreenWidth;
    private String mStringBrightnessNode;
    /* access modifiers changed from: private */
    public TextView mTvCalStatus;
    private TextView mTvCurrentStep;

    private class FingerPrintEventListenerOptical implements FingerprintEventListener {
        private FingerPrintEventListenerOptical() {
        }

        public void onFingerprintEvent(int eventId, Object evt) {
            if (eventId != 2020) {
                switch (eventId) {
                    case 285212675:
                        LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                        FingerprintCalTest_egisOptical.this.updateStep();
                        return;
                    case 285212676:
                        LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                        FingerprintCalTest_egisOptical.this.mSTEP = STEP.RESULT;
                        FingerprintCalTest_egisOptical.this.updateStep();
                        return;
                    default:
                        switch (eventId) {
                            case 285212678:
                                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_PUT_WKBOX");
                                FingerprintCalTest_egisOptical.this.mSTEP = STEP.WKBOX;
                                FingerprintCalTest_egisOptical.this.mButtonStart.setVisibility(0);
                                FingerprintCalTest_egisOptical.this.updateStep();
                                return;
                            case 285212679:
                                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_PUT_BKBOX");
                                FingerprintCalTest_egisOptical.this.mSTEP = STEP.BKBOX;
                                FingerprintCalTest_egisOptical.this.mButtonStart.setVisibility(0);
                                FingerprintCalTest_egisOptical.this.updateStep();
                                return;
                            case 285212680:
                                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_PUT_CHART");
                                FingerprintCalTest_egisOptical.this.mSTEP = STEP.CHART;
                                FingerprintCalTest_egisOptical.this.mButtonStart.setVisibility(0);
                                FingerprintCalTest_egisOptical.this.updateStep();
                                return;
                            case 285212681:
                                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", "Event is : FACTORY_TEST_EVT_SNSR_TEST_WAIT_COMMAND");
                                FingerprintCalTest_egisOptical.this.verifySensorState(Fingerprint.SENSOR_TEST_SNR_UNINIT_COMMAND, 0, 1, 0, 0);
                                return;
                            default:
                                StringBuilder sb = new StringBuilder();
                                sb.append("Event is : UNKNOWN_EVT_MESSAGE, event id : ");
                                sb.append(eventId);
                                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", sb.toString());
                                return;
                        }
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Event is : EVT_ERROR, ");
                sb2.append(FingerprintCalTest_egisOptical.this.mSTEP.name());
                sb2.append(" Fail");
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onFingerprintEvent", sb2.toString());
                FingerprintCalTest_egisOptical.this.mSTEP = STEP.RESULT;
                FingerprintCalTest_egisOptical.this.updateStep();
            }
        }
    }

    public class FingerPrintSNRData {
        private int ITEM_COUNT_EGIS = 13;
        private int isDone = 0;
        private SNRItem mItemBadPixelBlock;
        private SNRItem mItemBadPixelConsecutive;
        private SNRItem mItemBadPixelTotal;
        private SNRItem mItemCalibrationSave;
        private SNRItem mItemCet;
        private SNRItem mItemFeaX1;
        private SNRItem mItemFeaX2;
        private SNRItem mItemFeaY1;
        private SNRItem mItemFeaY2;
        private SNRItem mItemMFactor;
        private SNRItem mItemNoise_Egis;
        private SNRItem mItemSNR_Egis;
        private SNRItem mItemSignal_Egis;
        private String mSpecResultFile;

        private class SNRItem {
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

        public FingerPrintSNRData() {
        }

        public boolean isReadDone() {
            if (this.isDone != this.ITEM_COUNT_EGIS) {
                return false;
            }
            this.isDone = 0;
            return true;
        }

        public void addCet(float value, int precision) {
            this.mItemCet = new SNRItem();
            addItem(this.mItemCet, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CET_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_CET_MAX_EGIS))), precision);
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

        public void addMFactor(float value, int precision) {
            this.mItemMFactor = new SNRItem();
            addItem(this.mItemMFactor, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_M_FACTOR_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_M_FACTOR_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addFeaX1(float value, int precision) {
            this.mItemFeaX1 = new SNRItem();
            addItem(this.mItemFeaX1, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_X_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_X_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addFeaX2(float value, int precision) {
            this.mItemFeaX2 = new SNRItem();
            addItem(this.mItemFeaX2, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_X_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_X_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addFeaY1(float value, int precision) {
            this.mItemFeaY1 = new SNRItem();
            addItem(this.mItemFeaY1, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_Y_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_Y_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addFeaY2(float value, int precision) {
            this.mItemFeaY2 = new SNRItem();
            addItem(this.mItemFeaY2, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_Y_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_FEA_Y_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addBadPixelBlock(float value, int precision) {
            this.mItemBadPixelBlock = new SNRItem();
            addItem(this.mItemBadPixelBlock, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_BLOCK_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_BLOCK_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addBadPixelConsecutive(float value, int precision) {
            this.mItemBadPixelConsecutive = new SNRItem();
            addItem(this.mItemBadPixelConsecutive, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addBadPixelTotal(float value, int precision) {
            this.mItemBadPixelTotal = new SNRItem();
            addItem(this.mItemBadPixelTotal, value, new Spec(Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_TOTAL_MIN_EGIS)), Float.valueOf(com.sec.xmldata.support.Support.Spec.getFloat(com.sec.xmldata.support.Support.Spec.FINGERPRINT_BAD_PIXEL_TOTAL_MAX_EGIS))), precision);
            this.isDone++;
        }

        public void addCalibrationSave(int value) {
            this.mItemCalibrationSave = new SNRItem();
            addItem(this.mItemCalibrationSave, value, new int[]{0});
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
            if (value < ((Float) spec.lower).floatValue() || value > ((Float) spec.upper).floatValue()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Fail - SNR / result(");
                sb2.append(value);
                sb2.append(") spec(");
                sb2.append(spec.lower);
                sb2.append("~");
                sb2.append(spec.upper);
                sb2.append(")");
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onCreate", sb2.toString());
            } else {
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
            if (!pass) {
                String temp = "";
                for (int i2 = 0; i2 < spec.length; i2++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(spec[i2]);
                    temp = sb.toString();
                    if (i2 < spec.length - 1) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(temp);
                        sb2.append(",");
                        temp = sb2.toString();
                    }
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Fail - SNR / result(");
                sb3.append(value);
                sb3.append(") spec(");
                sb3.append(temp);
                sb3.append(")");
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onCreate", sb3.toString());
            }
            item.setSpecArray(spec);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(value);
            item.setValue(sb4.toString());
            item.setPass(pass);
        }

        public String getCetValue() {
            return this.mItemCet.getValue();
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

        public String getMFactorValue() {
            return this.mItemMFactor.getValue();
        }

        public String getFeaX1Value() {
            return this.mItemFeaX1.getValue();
        }

        public String getFeaX2Value() {
            return this.mItemFeaX2.getValue();
        }

        public String getFeaY1Value() {
            return this.mItemFeaY1.getValue();
        }

        public String getFeaY2Value() {
            return this.mItemFeaY2.getValue();
        }

        public String getBadPixelBlockValue() {
            return this.mItemBadPixelBlock.getValue();
        }

        public String getBadPixelConsecutiveValue() {
            return this.mItemBadPixelConsecutive.getValue();
        }

        public String getBadPixelTotalValue() {
            return this.mItemBadPixelTotal.getValue();
        }

        public String getCalibrationSaveValue() {
            return this.mItemCalibrationSave.getValue();
        }

        public boolean getCetPass() {
            return this.mItemCet.getPass();
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

        public boolean getMFactorPass() {
            return this.mItemMFactor.getPass();
        }

        public boolean getFeaX1Pass() {
            return this.mItemFeaX1.getPass();
        }

        public boolean getFeaX2Pass() {
            return this.mItemFeaX2.getPass();
        }

        public boolean getFeaY1Pass() {
            return this.mItemFeaY1.getPass();
        }

        public boolean getFeaY2Pass() {
            return this.mItemFeaY2.getPass();
        }

        public boolean getBadPixelBlockPass() {
            return this.mItemBadPixelBlock.getPass();
        }

        public boolean getBadPixelConsecutivePass() {
            return this.mItemBadPixelConsecutive.getPass();
        }

        public boolean getBadPixelTotalPass() {
            return this.mItemBadPixelTotal.getPass();
        }

        public boolean getCalibrationSavePass() {
            return this.mItemCalibrationSave.getPass();
        }

        public Spec<Float> getCetSpec() {
            return this.mItemCet.getSpec();
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

        public Spec<Float> getMFactorSpec() {
            return this.mItemMFactor.getSpec();
        }

        public Spec<Float> getFeaX1Spec() {
            return this.mItemFeaX1.getSpec();
        }

        public Spec<Float> getFeaX2Spec() {
            return this.mItemFeaX2.getSpec();
        }

        public Spec<Float> getFeaY1Spec() {
            return this.mItemFeaY1.getSpec();
        }

        public Spec<Float> getFeaY2Spec() {
            return this.mItemFeaY2.getSpec();
        }

        public Spec<Float> getBadPixelBlockSpec() {
            return this.mItemBadPixelBlock.getSpec();
        }

        public Spec<Float> getBadPixelConsecutiveSpec() {
            return this.mItemBadPixelConsecutive.getSpec();
        }

        public Spec<Float> getBadPixelTotalSpec() {
            return this.mItemBadPixelTotal.getSpec();
        }

        public int[] getCalibrationSaveSpec() {
            return this.mItemCalibrationSave.getSpecArray();
        }

        public boolean processData() {
            boolean result = false;
            if (this.mItemSignal_Egis.getPass() && this.mItemNoise_Egis.getPass() && this.mItemSNR_Egis.getPass() && this.mItemMFactor.getPass()) {
                result = true;
            }
            if (result) {
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "processData", "Pass");
                this.mSpecResultFile = "PASS";
            } else {
                this.mSpecResultFile = "FAIL";
            }
            return result;
        }

        private float round(float t, int decimal) {
            return ((float) Math.round(((double) t) * Math.pow(10.0d, (double) decimal))) / ((float) Math.pow(10.0d, (double) decimal));
        }
    }

    public enum STEP {
        WKBOX,
        BKBOX,
        CHART,
        RESULT,
        NOTEST;

        public String getTitle() {
            if (this == WKBOX) {
                return "WHITE Calibration";
            }
            if (this == BKBOX) {
                return "BLACK Calibration";
            }
            if (this == CHART) {
                return "CHARTER Test";
            }
            if (this == RESULT) {
                return "SNR Test";
            }
            return null;
        }

        public int getCmdId() {
            if (this == WKBOX) {
                return Fingerprint.SENSOR_TEST_SNR_WKBOX_ON_COMMAND;
            }
            if (this == BKBOX) {
                return Fingerprint.SENSOR_TEST_SNR_BKBOX_ON_COMMAND;
            }
            if (this == CHART) {
                return Fingerprint.SENSOR_TEST_SNR_CHART_ON_COMMAND;
            }
            return -1;
        }

        public STEP getNextStep() {
            if (this == NOTEST) {
                return WKBOX;
            }
            if (this == WKBOX) {
                return BKBOX;
            }
            if (this == BKBOX) {
                return CHART;
            }
            if (this == CHART) {
                return RESULT;
            }
            return null;
        }

        public STEP getStepByString(String stepName) {
            if (WKBOX.name().equals(stepName)) {
                return WKBOX;
            }
            if (BKBOX.name().equals(stepName)) {
                return BKBOX;
            }
            if (CHART.name().equals(stepName)) {
                return CHART;
            }
            if (RESULT.name().equals(stepName)) {
                return RESULT;
            }
            return null;
        }
    }

    public static class Spec<T> {
        public T lower = null;
        public T upper = null;

        public Spec(T l, T u) {
            this.lower = l;
            this.upper = u;
        }
    }

    public FingerprintCalTest_egisOptical() {
        super("FingerprintCalTest_egisOptical");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_d(this.CLASS_NAME, "onCreate", "");
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.fingerprint_cal_egis_optical);
        LtUtil.setRemoveSystemUI(getWindow(), true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LtUtil.log_d(this.CLASS_NAME, "onResume", "");
        super.onResume();
        initialize();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_d(this.CLASS_NAME, "onPause", "");
        if (this.mIntPrevPanelBrightness != -1) {
            LtUtil.log_d(this.CLASS_NAME, "onPause", "back to previous brightness");
            String str = this.mStringBrightnessNode;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mIntPrevPanelBrightness);
            sb.append("");
            Kernel.write(str, sb.toString());
        }
        super.onPause();
        if (this.mFingerprintOptical != null) {
            this.mFingerprintOptical.cancel();
            this.mFingerprintOptical = null;
        }
        UnregisterReceiver();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void initialize() {
        LtUtil.log_d(this.CLASS_NAME, "initialize", "");
        this.mFingerprintOptical = Fingerprint.create(this);
        if (this.mFingerprintOptical != null) {
            this.mFingerPrintEventListenerOptical = new FingerPrintEventListenerOptical();
            this.mFingerprintOptical.setEventListener(this.mFingerPrintEventListenerOptical);
            int sensorStatus = this.mFingerprintOptical.getSensorStatus();
        }
        this.mIvFingerCircle = (ImageView) findViewById(C0268R.C0269id.fingerprint_area_image);
        this.mIvFingerRect = (ImageView) findViewById(C0268R.C0269id.fingerprint_area_rect);
        this.mButtonStart = (Button) findViewById(C0268R.C0269id.button_start);
        this.mButtonExit = (Button) findViewById(C0268R.C0269id.button_exit);
        this.mTvCurrentStep = (TextView) findViewById(C0268R.C0269id.current_step);
        this.mTvCalStatus = (TextView) findViewById(C0268R.C0269id.cal_status);
        this.mResultView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_resultview);
        this.mIvSnrImage = (ImageView) findViewById(C0268R.C0269id.fingerprint_snrimage);
        this.mTvCurrentStep.setText("White Calibration");
        this.mButtonStart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onClick", "Start Button");
                if (FingerprintCalTest_egisOptical.this.mSTEP.getCmdId() != -1) {
                    FingerprintCalTest_egisOptical.this.mTvCalStatus.setText(C0268R.string.RUNNING);
                    FingerprintCalTest_egisOptical.this.mButtonStart.setVisibility(4);
                    FingerprintCalTest_egisOptical.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            FingerprintCalTest_egisOptical.this.verifySensorState(FingerprintCalTest_egisOptical.this.mSTEP.getCmdId());
                        }
                    }, 500);
                }
            }
        });
        this.mButtonExit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onClick", "Exit Button");
                FingerprintCalTest_egisOptical.this.finish();
            }
        });
        if (!drawFingerprintArea()) {
            LtUtil.log_d(this.CLASS_NAME, "initialize", "Can not read fingerprint position");
            this.mTvCurrentStep.setText("Can not read fingerprint position");
            this.mButtonStart.setVisibility(4);
            this.mTvCalStatus.setText(C0268R.string.FAIL);
            this.mTvCalStatus.setTextColor(-65536);
            return;
        }
        this.mFingerPrintSNRData = new FingerPrintSNRData();
        RegisterReceiver();
        String buildHardware = Build.HARDWARE;
        if (buildHardware == null || !buildHardware.contains("qcom")) {
            this.mStringBrightnessNode = Kernel.LCD_BACKLIGHT_BRIGHTNESS_LSI;
        } else {
            this.mStringBrightnessNode = Kernel.LCD_BACKLIGHT_BRIGHTNESS_QUALCOMM;
        }
        try {
            this.mIntPrevPanelBrightness = Integer.parseInt(Kernel.read(this.mStringBrightnessNode));
            Kernel.write(this.mStringBrightnessNode, "319");
            LtUtil.log_d(this.CLASS_NAME, "initialize", "read previous brightness and set 319 for testing");
        } catch (NumberFormatException ne) {
            LtUtil.log_e(ne);
        }
        verifySensorState(Fingerprint.SENSOR_TEST_SNR_INIT_COMMAND, 0, 1, 0, 0);
    }

    /* access modifiers changed from: private */
    public int verifySensorState(int cmd) {
        StringBuilder sb = new StringBuilder();
        sb.append("cmd : ");
        sb.append(cmd);
        LtUtil.log_d(this.CLASS_NAME, "verifySensorState", sb.toString());
        return this.mFingerprintOptical.verifySensorState(cmd, 0, 0, 0, 0);
    }

    /* access modifiers changed from: private */
    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        StringBuilder sb = new StringBuilder();
        sb.append("cmd : ");
        sb.append(cmd);
        LtUtil.log_d(this.CLASS_NAME, "verifySensorState", sb.toString());
        return this.mFingerprintOptical.verifySensorState(cmd, sId, opt, logOpt, uId);
    }

    /* access modifiers changed from: private */
    public void updateStep() {
        StringBuilder sb = new StringBuilder();
        sb.append("mSTEP : ");
        sb.append(this.mSTEP);
        LtUtil.log_d(this.CLASS_NAME, "updateStep", sb.toString());
        this.mTvCurrentStep.setText(this.mSTEP.getTitle());
        this.mTvCalStatus.setText(C0268R.string.READY);
        if (this.mSTEP == STEP.RESULT) {
            if (this.mIntPrevPanelBrightness != -1) {
                LtUtil.log_d(this.CLASS_NAME, "updateStep", "back to previous brightness");
                String str = this.mStringBrightnessNode;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mIntPrevPanelBrightness);
                sb2.append("");
                Kernel.write(str, sb2.toString());
            }
            ((TextView) findViewById(C0268R.C0269id.touchGuide)).setVisibility(8);
            ((AbsoluteLayout) findViewById(C0268R.C0269id.fingerprint_area)).setVisibility(8);
            ((FrameLayout) findViewById(C0268R.C0269id.background)).setBackgroundColor(-1);
            ((TextView) findViewById(C0268R.C0269id.main_title)).setTextColor(-16777216);
            this.mTvCurrentStep.setTextColor(-16777216);
            readSNRLogFile_egis();
            if (this.mFingerPrintSNRData.isReadDone()) {
                updateSNRResult_egis();
                if (this.mFingerPrintSNRData.processData()) {
                    this.mTvCalStatus.setText(C0268R.string.PASS);
                    this.mTvCalStatus.setTextColor(-16776961);
                    return;
                }
                this.mTvCalStatus.setText(C0268R.string.FAIL);
                this.mTvCalStatus.setTextColor(-65536);
                return;
            }
            this.mTvCurrentStep.setText("Cannot found Data log!!");
            this.mTvCalStatus.setText(C0268R.string.FAIL);
            this.mTvCalStatus.setTextColor(-65536);
        }
    }

    private void updateSNRResult_egis() {
        LtUtil.log_d(this.CLASS_NAME, "updateSNRResult_egis", "");
        LayoutParams params = this.mIvSnrImage.getLayoutParams();
        params.height = this.mScreenHeight / 6;
        this.mIvSnrImage.setLayoutParams(params);
        Bitmap bitmap = BitmapFactory.decodeFile("/data/egis_after_rubber.bmp");
        this.mIvSnrImage.setVisibility(0);
        this.mIvSnrImage.setImageBitmap(bitmap);
        this.mResultView.setVisibility(0);
        this.mResultView.setCanvasSize(this.mScreenWidth, this.mScreenHeight / 4);
        this.mResultView.setRowCol(14, 4);
        this.mResultView.setTextData(0, 0, "Item");
        this.mResultView.setTextData(0, 1, "Spec");
        this.mResultView.setTextData(0, 2, "Result");
        int rowNum = 0 + 1;
        this.mResultView.setTextData(rowNum, 0, "CET");
        this.mResultView.setTextData(rowNum, 1, "NA");
        this.mResultView.setTextData(rowNum, 2, this.mFingerPrintSNRData.getCetValue());
        this.mResultView.setTextData(rowNum, 3, "NA");
        int rowNum2 = rowNum + 1;
        this.mResultView.setTextData(rowNum2, 0, "Signal");
        FingerPrintResultView_touch fingerPrintResultView_touch = this.mResultView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mFingerPrintSNRData.getSignalSpec_Egis().lower);
        sb.append("~");
        sb.append(this.mFingerPrintSNRData.getSignalSpec_Egis().upper);
        fingerPrintResultView_touch.setTextData(rowNum2, 1, sb.toString());
        this.mResultView.setTextData(rowNum2, 2, this.mFingerPrintSNRData.getSignalValue_Egis());
        this.mResultView.setTextData(rowNum2, 3, this.mFingerPrintSNRData.getSignalPass_Egis() ? "PASS" : "FAIL");
        int rowNum3 = rowNum2 + 1;
        this.mResultView.setTextData(rowNum3, 0, "Noise");
        FingerPrintResultView_touch fingerPrintResultView_touch2 = this.mResultView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().lower);
        sb2.append("~");
        sb2.append(this.mFingerPrintSNRData.getNoiseSpec_Egis().upper);
        fingerPrintResultView_touch2.setTextData(rowNum3, 1, sb2.toString());
        this.mResultView.setTextData(rowNum3, 2, this.mFingerPrintSNRData.getNoiseValue_Egis());
        this.mResultView.setTextData(rowNum3, 3, this.mFingerPrintSNRData.getNoisePass_Egis() ? "PASS" : "FAIL");
        int rowNum4 = rowNum3 + 1;
        this.mResultView.setTextData(rowNum4, 0, "SNR");
        FingerPrintResultView_touch fingerPrintResultView_touch3 = this.mResultView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mFingerPrintSNRData.getSNRSpec_Egis().lower);
        sb3.append("~");
        sb3.append(this.mFingerPrintSNRData.getSNRSpec_Egis().upper);
        fingerPrintResultView_touch3.setTextData(rowNum4, 1, sb3.toString());
        this.mResultView.setTextData(rowNum4, 2, this.mFingerPrintSNRData.getSNRValue_Egis());
        this.mResultView.setTextData(rowNum4, 3, this.mFingerPrintSNRData.getSNRPass_Egis() ? "PASS" : "FAIL");
        int rowNum5 = rowNum4 + 1;
        this.mResultView.setTextData(rowNum5, 0, "M Factor");
        FingerPrintResultView_touch fingerPrintResultView_touch4 = this.mResultView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.mFingerPrintSNRData.getMFactorSpec().lower);
        sb4.append("~");
        sb4.append(this.mFingerPrintSNRData.getMFactorSpec().upper);
        fingerPrintResultView_touch4.setTextData(rowNum5, 1, sb4.toString());
        this.mResultView.setTextData(rowNum5, 2, this.mFingerPrintSNRData.getMFactorValue());
        this.mResultView.setTextData(rowNum5, 3, this.mFingerPrintSNRData.getMFactorPass() ? "PASS" : "FAIL");
        int rowNum6 = rowNum5 + 1;
    }

    private boolean drawFingerprintArea() {
        LtUtil.log_d(this.CLASS_NAME, "drawFingerprintArea", "");
        Display mDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        this.mScreenWidth = outpoint.x;
        this.mScreenHeight = outpoint.y;
        String raw_pos = LtUtil.readFromFile("/sys/class/fingerprint/fingerprint/position");
        StringBuilder sb = new StringBuilder();
        sb.append(" raw value for pos(mm):");
        sb.append(raw_pos);
        LtUtil.log_d(this.CLASS_NAME, "drawFingerprintArea", sb.toString());
        String[] pos = raw_pos.split(",");
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        try {
            float btmToBtm = TypedValue.applyDimension(5, Float.parseFloat(pos[0]), mDisplayMetrics);
            float xOffset = TypedValue.applyDimension(5, Float.parseFloat(pos[1]), mDisplayMetrics);
            float applyDimension = TypedValue.applyDimension(5, Float.parseFloat(pos[2]), mDisplayMetrics);
            float fpHeight = TypedValue.applyDimension(5, Float.parseFloat(pos[3]), mDisplayMetrics);
            float uiCircleWidth = TypedValue.applyDimension(5, Float.parseFloat(pos[6]), mDisplayMetrics);
            float uiCircleHeight = TypedValue.applyDimension(5, Float.parseFloat(pos[7]), mDisplayMetrics);
            float outerBoxSize = TypedValue.applyDimension(5, 22.0f, mDisplayMetrics);
            AbsoluteLayout.LayoutParams ivFingerCircleParams = (AbsoluteLayout.LayoutParams) this.mIvFingerCircle.getLayoutParams();
            ivFingerCircleParams.width = (int) uiCircleWidth;
            ivFingerCircleParams.height = (int) uiCircleHeight;
            ivFingerCircleParams.x = (int) (((((float) this.mScreenWidth) * 0.5f) - (uiCircleWidth * 0.5f)) + xOffset);
            ivFingerCircleParams.y = (int) (((float) this.mScreenHeight) - ((btmToBtm + (uiCircleHeight * 0.5f)) + (fpHeight * 0.5f)));
            this.mIvFingerCircle.setLayoutParams(ivFingerCircleParams);
            AbsoluteLayout.LayoutParams ivFingerRectParams = (AbsoluteLayout.LayoutParams) this.mIvFingerRect.getLayoutParams();
            ivFingerRectParams.width = (int) outerBoxSize;
            ivFingerRectParams.height = (int) outerBoxSize;
            ivFingerRectParams.x = (int) (((((float) this.mScreenWidth) * 0.5f) - (outerBoxSize * 0.5f)) + xOffset);
            ivFingerRectParams.y = (int) (((float) this.mScreenHeight) - ((btmToBtm + (outerBoxSize * 0.5f)) + (0.5f * fpHeight)));
            this.mIvFingerRect.setLayoutParams(ivFingerRectParams);
            return true;
        } catch (NumberFormatException ne) {
            LtUtil.log_e(ne);
            return false;
        } catch (IndexOutOfBoundsException ie) {
            LtUtil.log_e(ie);
            return false;
        }
    }

    private void RegisterReceiver() {
        LtUtil.log_d(this.CLASS_NAME, "RegisterReceiver", "RegisterReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.factory.common.ACTION_REQUEST_FP_TEST_STATUS");
        filter.addAction("com.sec.factory.aporiented.athandler.ACTION_SNR_CAL_OPTICAL");
        filter.addAction("com.sec.factory.aporiented.athandler.ACTION_SNR_CAL_OPTICAL_STOP");
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                StringBuilder sb = new StringBuilder();
                sb.append(" Action:");
                sb.append(action);
                LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onReceive", sb.toString());
                if ("com.sec.factory.common.ACTION_REQUEST_FP_TEST_STATUS".equals(action)) {
                    String result = "";
                    STEP stepToCheck = FingerprintCalTest_egisOptical.this.mSTEP.getStepByString(intent.getStringExtra("STEP"));
                    if (stepToCheck != null) {
                        if (stepToCheck == FingerprintCalTest_egisOptical.this.mSTEP) {
                            if (FingerprintCalTest_egisOptical.this.isRunningTest()) {
                                result = "NG";
                            } else {
                                result = "OK";
                            }
                        } else if (stepToCheck != FingerprintCalTest_egisOptical.this.mSTEP.getNextStep()) {
                            result = "NG";
                        } else if (FingerprintCalTest_egisOptical.this.isRunningTest()) {
                            result = "WAIT";
                        } else {
                            result = "NG";
                        }
                    }
                    Intent replyIntent = new Intent("com.sec.factory.common.ACTION_REPLY_FP_TEST_STATUS");
                    replyIntent.putExtra("RESULT", result);
                    FingerprintCalTest_egisOptical.this.sendBroadcast(replyIntent);
                } else if ("com.sec.factory.aporiented.athandler.ACTION_SNR_CAL_OPTICAL".equals(action)) {
                    FingerprintCalTest_egisOptical.this.mSTEP = FingerprintCalTest_egisOptical.this.mSTEP.getStepByString(intent.getStringExtra("STEP"));
                    FingerprintCalTest_egisOptical.this.updateStep();
                    LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onReceive", "performClick by broadcasting");
                    FingerprintCalTest_egisOptical.this.mButtonStart.performClick();
                } else if ("com.sec.factory.aporiented.athandler.ACTION_SNR_CAL_OPTICAL_STOP".equals(action)) {
                    LtUtil.log_d(FingerprintCalTest_egisOptical.this.CLASS_NAME, "onReceive", "ACTION_SNR_CAL_OPTICAL_STOP, finish");
                    FingerprintCalTest_egisOptical.this.finish();
                }
            }
        };
        registerReceiver(this.mReceiver, filter);
    }

    private void UnregisterReceiver() {
        if (this.mReceiver != null) {
            LtUtil.log_d(this.CLASS_NAME, "UnregisterReceiver", "UnregisterReceiver");
            unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public boolean isRunningTest() {
        LtUtil.log_d(this.CLASS_NAME, "isRunningTest", "");
        String calStatus = this.mTvCalStatus.getText().toString();
        String runningString = getResources().getString(C0268R.string.RUNNING);
        StringBuilder sb = new StringBuilder();
        sb.append("calStatus : ");
        sb.append(calStatus);
        LtUtil.log_d(this.CLASS_NAME, "isRunningTest", sb.toString());
        return calStatus.equals(runningString);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0309, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x030a, code lost:
        r22 = r3;
        r23 = r5;
        r25 = r6;
        r24 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0313, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0314, code lost:
        r22 = r3;
        r23 = r5;
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x031b, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x031c, code lost:
        r22 = r3;
        r23 = r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x036f A[SYNTHETIC, Splitter:B:101:0x036f] */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x037f A[SYNTHETIC, Splitter:B:108:0x037f] */
    /* JADX WARNING: Removed duplicated region for block: B:115:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0313 A[ExcHandler: all (r0v37 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:14:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x031b A[ExcHandler: IOException (e java.io.IOException), Splitter:B:14:0x0083] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readSNRLogFile_egis() {
        /*
            r29 = this;
            r1 = r29
            java.lang.String r0 = r1.CLASS_NAME
            java.lang.String r2 = "readSNRLogFile_egis"
            java.lang.String r3 = "Read SNR log-EGIS"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r2, r3)
            java.lang.String r2 = "/data/egis_sensor_test_SNR.log"
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            r3 = r0
            boolean r0 = r3.exists()
            if (r0 == 0) goto L_0x0389
            java.lang.String r0 = r1.CLASS_NAME
            java.lang.String r4 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r2)
            java.lang.String r6 = " file Exist"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r4, r5)
            r4 = 0
            r0 = 0
            r5 = r0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            r0.<init>(r2)     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            r4 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            r6.<init>(r4)     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            r0.<init>(r6)     // Catch:{ IOException -> 0x0365, all -> 0x035c }
            r5 = r0
            java.lang.String r0 = ""
            java.lang.String r6 = r5.readLine()     // Catch:{ IOException -> 0x0354, all -> 0x034b }
            r7 = r6
            if (r6 == 0) goto L_0x0341
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
            java.lang.String r0 = ","
            java.lang.String[] r0 = r7.split(r0)     // Catch:{ IndexOutOfBoundsException -> 0x032b }
            r20 = 0
            r21 = r2
            r2 = r0[r20]     // Catch:{ IndexOutOfBoundsException -> 0x0321, IOException -> 0x031b, all -> 0x0313 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x0321, IOException -> 0x031b, all -> 0x0313 }
            r6 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x0309, IOException -> 0x031b, all -> 0x0313 }
            r22 = r3
            float r3 = java.lang.Float.parseFloat(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0301, IOException -> 0x02fc, all -> 0x02f6 }
            r23 = r5
            r5 = 1
            r2.addCet(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02f0 }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02f0 }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02f0 }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02f0 }
            r24 = r7
            java.lang.String r7 = "cet :"
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 1
            r3 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r8 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r8)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addSignal_Egis(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "signal :"
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r8)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 2
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r9 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r9)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addNoise_Egis(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "noise : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r9)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 3
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r10 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r10)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addSNR_Egis(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "snr : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r10)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 4
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r11 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r11)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addMFactor(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "mFactor : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r11)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 5
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r12 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r12)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addFeaX1(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "feaX1 : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r12)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 6
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r13 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r13)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addFeaX2(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "feaX2 : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r13)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 7
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r14 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r14)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addFeaY1(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "feaY1 : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r14)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 8
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r15 = r2
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r2 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            float r3 = java.lang.Float.parseFloat(r15)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5 = 1
            r2.addFeaY2(r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r3 = "readSNRLogFile_egis"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r7 = "feaY2 : "
            r5.append(r7)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r5.append(r15)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r5 = r5.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r2, r3, r5)     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            r2 = 9
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ec }
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r3 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            float r5 = java.lang.Float.parseFloat(r2)     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            r7 = 1
            r3.addBadPixelBlock(r5, r7)     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            java.lang.String r3 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            java.lang.String r5 = "readSNRLogFile_egis"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            r7.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02e4 }
            r25 = r6
            java.lang.String r6 = "badPixelBlock : "
            r7.append(r6)     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            r7.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            java.lang.String r6 = r7.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r3, r5, r6)     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            r3 = 10
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02dd }
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r5 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            float r6 = java.lang.Float.parseFloat(r3)     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            r7 = 1
            r5.addBadPixelConsecutive(r6, r7)     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            java.lang.String r5 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            java.lang.String r6 = "readSNRLogFile_egis"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            r7.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02d2 }
            r26 = r2
            java.lang.String r2 = "badPixelConsecutive : "
            r7.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            r7.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            java.lang.String r2 = r7.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r5, r6, r2)     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            r2 = 11
            r2 = r0[r2]     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            java.lang.String r2 = r2.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02c9 }
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r5 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            float r6 = java.lang.Float.parseFloat(r2)     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            r7 = 1
            r5.addBadPixelTotal(r6, r7)     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            java.lang.String r5 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            java.lang.String r6 = "readSNRLogFile_egis"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            r7.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02c3 }
            r27 = r3
            java.lang.String r3 = "badPixelTotal : "
            r7.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            r7.append(r2)     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            java.lang.String r3 = r7.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r5, r6, r3)     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            r3 = 12
            r3 = r0[r3]     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            java.lang.String r3 = r3.trim()     // Catch:{ IndexOutOfBoundsException -> 0x02ba }
            com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical$FingerPrintSNRData r5 = r1.mFingerPrintSNRData     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            int r6 = java.lang.Integer.parseInt(r3)     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            r5.addCalibrationSave(r6)     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            java.lang.String r5 = r1.CLASS_NAME     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            java.lang.String r6 = "readSNRLogFile_egis"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            r7.<init>()     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            r28 = r0
            java.lang.String r0 = "calibrationSave : "
            r7.append(r0)     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            r7.append(r3)     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            java.lang.String r0 = r7.toString()     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r5, r6, r0)     // Catch:{ IndexOutOfBoundsException -> 0x02b4 }
            goto L_0x0347
        L_0x02b4:
            r0 = move-exception
            r18 = r2
            r19 = r3
            goto L_0x02bd
        L_0x02ba:
            r0 = move-exception
            r18 = r2
        L_0x02bd:
            r16 = r26
            r17 = r27
            goto L_0x0336
        L_0x02c3:
            r0 = move-exception
            r27 = r3
            r18 = r2
            goto L_0x02cc
        L_0x02c9:
            r0 = move-exception
            r27 = r3
        L_0x02cc:
            r16 = r26
            r17 = r27
            goto L_0x0336
        L_0x02d2:
            r0 = move-exception
            r26 = r2
            r27 = r3
            r16 = r26
            r17 = r27
            goto L_0x0336
        L_0x02dd:
            r0 = move-exception
            r26 = r2
            r16 = r26
            goto L_0x0336
        L_0x02e4:
            r0 = move-exception
            r26 = r2
            r25 = r6
            r16 = r26
            goto L_0x0336
        L_0x02ec:
            r0 = move-exception
            r25 = r6
            goto L_0x0336
        L_0x02f0:
            r0 = move-exception
            r25 = r6
            r24 = r7
            goto L_0x0336
        L_0x02f6:
            r0 = move-exception
            r23 = r5
            r2 = r0
            goto L_0x037d
        L_0x02fc:
            r0 = move-exception
            r23 = r5
            goto L_0x036a
        L_0x0301:
            r0 = move-exception
            r23 = r5
            r25 = r6
            r24 = r7
            goto L_0x0336
        L_0x0309:
            r0 = move-exception
            r22 = r3
            r23 = r5
            r25 = r6
            r24 = r7
            goto L_0x0336
        L_0x0313:
            r0 = move-exception
            r22 = r3
            r23 = r5
            r2 = r0
            goto L_0x037d
        L_0x031b:
            r0 = move-exception
            r22 = r3
            r23 = r5
            goto L_0x036a
        L_0x0321:
            r0 = move-exception
            r22 = r3
            r23 = r5
            r24 = r7
            r25 = r6
            goto L_0x0336
        L_0x032b:
            r0 = move-exception
            r21 = r2
            r22 = r3
            r23 = r5
            r24 = r7
            r25 = r6
        L_0x0336:
            r0.printStackTrace()     // Catch:{ IOException -> 0x033d, all -> 0x033a }
            goto L_0x0347
        L_0x033a:
            r0 = move-exception
            r2 = r0
            goto L_0x037d
        L_0x033d:
            r0 = move-exception
            r5 = r23
            goto L_0x036a
        L_0x0341:
            r21 = r2
            r22 = r3
            r23 = r5
        L_0x0347:
            r4.close()     // Catch:{ IOException -> 0x0373 }
            goto L_0x0372
        L_0x034b:
            r0 = move-exception
            r21 = r2
            r22 = r3
            r23 = r5
            r2 = r0
            goto L_0x037d
        L_0x0354:
            r0 = move-exception
            r21 = r2
            r22 = r3
            r23 = r5
            goto L_0x036a
        L_0x035c:
            r0 = move-exception
            r21 = r2
            r22 = r3
            r2 = r0
            r23 = r5
            goto L_0x037d
        L_0x0365:
            r0 = move-exception
            r21 = r2
            r22 = r3
        L_0x036a:
            r0.printStackTrace()     // Catch:{ all -> 0x0379 }
            if (r4 == 0) goto L_0x038d
            r4.close()     // Catch:{ IOException -> 0x0373 }
        L_0x0372:
            goto L_0x038d
        L_0x0373:
            r0 = move-exception
            r2 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
            goto L_0x0372
        L_0x0379:
            r0 = move-exception
            r2 = r0
            r23 = r5
        L_0x037d:
            if (r4 == 0) goto L_0x0388
            r4.close()     // Catch:{ IOException -> 0x0383 }
            goto L_0x0388
        L_0x0383:
            r0 = move-exception
            r3 = r0
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x0388:
            throw r2
        L_0x0389:
            r21 = r2
            r22 = r3
        L_0x038d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.FingerprintCalTest_egisOptical.readSNRLogFile_egis():void");
    }
}
