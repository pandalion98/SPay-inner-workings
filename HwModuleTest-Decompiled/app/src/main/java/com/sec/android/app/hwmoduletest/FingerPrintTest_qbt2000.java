package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000.ScanData;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000.TestType;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.android.app.hwmoduletest.support.Status;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Spec;
import java.util.HashMap;
import java.util.Objects;

public class FingerPrintTest_qbt2000 extends BaseActivity {
    private final String CLASS_NAME = "FingerPrintTest_qbt2000";
    private final long SnrStartButtonDelayForManual = 1000;
    /* access modifiers changed from: private */
    public boolean isRunning = false;
    /* access modifiers changed from: private */
    public AbsoluteLayout mAbsoluteLayout;
    /* access modifiers changed from: private */
    public Button mBtnExit;
    private Button mBtnStartRight;
    /* access modifiers changed from: private */
    public CommonFingerprint_qbt2000 mCommonFingerprint_qbt2000;
    private Handler mHandler = new Handler();
    private ImageView mIvDeadMaskImage;
    private ImageView mIvFingerArea;
    private ImageView mIvFingerRect;
    private ImageView mIvSnrImage;
    private Status mNormalScanResult = Status.NOTEST;
    private BroadcastReceiver mReceiver = null;
    private Status mSnrScanResult = Status.NOTEST;
    private TableLayout mTableLayout;
    private TextView mTvScanStatus;
    private TextView mTvSubTitle;
    private Boolean mTzEnabled = Boolean.valueOf(false);
    private final int tableTotalColumn = 5;

    public FingerPrintTest_qbt2000() {
        super("FingerPrintTest_qbt2000");
    }

    /* access modifiers changed from: private */
    public void RegisterReceiver() {
        LtUtil.log_d("FingerPrintTest_qbt2000", "RegisterReceiver", "RegisterReceiver");
        IntentFilter mFilter = new IntentFilter();
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_SUCCESS");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FAIL");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNRc_SCAN_SUCCESS");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_FAIL");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_READY");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_TIMEOUT");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_START_TZ");
        Objects.requireNonNull(this.mCommonFingerprint_qbt2000);
        mFilter.addAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FINISH_TZ");
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                StringBuilder sb = new StringBuilder();
                sb.append("Action: ");
                sb.append(action);
                LtUtil.log_d("FingerPrintTest_qbt2000", "onReceive", sb.toString());
                Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_SUCCESS")) {
                    HashMap<String, ScanData> scanDataHashMap = FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanDataMap(TestType.NORMALSCAN);
                    FingerPrintTest_qbt2000.this.updateScanResult(TestType.NORMALSCAN, scanDataHashMap);
                    if (FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getFinalResultfromScanData(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanItemList(TestType.NORMALSCAN), scanDataHashMap)) {
                        FingerPrintTest_qbt2000.this.updateStatus(Status.PASS);
                        FingerPrintTest_qbt2000.this.setScanResult(TestType.NORMALSCAN, Status.PASS);
                    } else {
                        FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                        FingerPrintTest_qbt2000.this.setScanResult(TestType.NORMALSCAN, Status.FAIL_SPECOUT);
                    }
                } else {
                    Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                    if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FAIL")) {
                        FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                        FingerPrintTest_qbt2000.this.setScanResult(TestType.NORMALSCAN, Status.FAIL_SCAN);
                    } else {
                        Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                        if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_READY")) {
                            FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.startSNR();
                            return;
                        }
                        Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                        if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNRc_SCAN_SUCCESS")) {
                            HashMap<String, ScanData> scanDataHashMap2 = FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanDataMap(TestType.SNRSCAN);
                            FingerPrintTest_qbt2000.this.updateScanResult(TestType.SNRSCAN, scanDataHashMap2);
                            FingerPrintTest_qbt2000.this.showSnrImg();
                            FingerPrintTest_qbt2000.this.showDeadMaskImg();
                            if (FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getFinalResultfromScanData(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanItemList(TestType.SNRSCAN), scanDataHashMap2)) {
                                FingerPrintTest_qbt2000.this.updateStatus(Status.PASS);
                                FingerPrintTest_qbt2000.this.setScanResult(TestType.SNRSCAN, Status.PASS);
                            } else {
                                FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                                FingerPrintTest_qbt2000.this.setScanResult(TestType.SNRSCAN, Status.FAIL_SPECOUT);
                                FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.copySnrImg();
                            }
                        } else {
                            Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                            if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_FAIL")) {
                                FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                                FingerPrintTest_qbt2000.this.setScanResult(TestType.SNRSCAN, Status.FAIL_SCAN);
                                FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.copySnrImg();
                            } else {
                                Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                                if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_TIMEOUT")) {
                                    FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                                    FingerPrintTest_qbt2000.this.setScanResult(TestType.SNRSCAN, Status.FAIL_CALLBACK_TIMEOUT);
                                } else {
                                    Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                                    if (!action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_START_TZ")) {
                                        Objects.requireNonNull(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000);
                                        if (action.equals("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FINISH_TZ")) {
                                            HashMap<String, ScanData> scanDataHashMap3 = FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanDataMap(TestType.NORMALSCAN);
                                            FingerPrintTest_qbt2000.this.updateScanResult(TestType.NORMALSCAN, scanDataHashMap3);
                                            if (FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getFinalResultfromScanData(FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.getScanItemList(TestType.NORMALSCAN), scanDataHashMap3)) {
                                                FingerPrintTest_qbt2000.this.updateStatus(Status.PASS);
                                                FingerPrintTest_qbt2000.this.setScanResult(TestType.NORMALSCAN, Status.PASS);
                                                if (BaseActivity.isOqcsbftt) {
                                                    NVAccessor.setNV(409, NVAccessor.NV_VALUE_PASS);
                                                }
                                            } else {
                                                FingerPrintTest_qbt2000.this.updateStatus(Status.FAIL);
                                                FingerPrintTest_qbt2000.this.setScanResult(TestType.NORMALSCAN, Status.FAIL_SPECOUT);
                                            }
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                FingerPrintTest_qbt2000.this.mBtnExit.setVisibility(0);
                FingerPrintTest_qbt2000.this.stopTest();
            }
        };
        registerReceiver(this.mReceiver, mFilter);
    }

    private void UnregisterReceiver() {
        if (this.mReceiver != null) {
            LtUtil.log_d("FingerPrintTest_qbt2000", "UnregisterReceiver", "UnregisterReceiver");
            unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void init() {
        this.mTableLayout = (TableLayout) findViewById(C0268R.C0269id.tableLayout);
        this.mAbsoluteLayout = (AbsoluteLayout) findViewById(C0268R.C0269id.absoluteLayout);
        this.mTvSubTitle = (TextView) findViewById(C0268R.C0269id.subTitle);
        this.mTvScanStatus = (TextView) findViewById(C0268R.C0269id.scanStatus);
        this.mIvSnrImage = (ImageView) findViewById(C0268R.C0269id.fingerprint_snr_image);
        this.mIvDeadMaskImage = (ImageView) findViewById(C0268R.C0269id.fingerprint_deadmask_image);
        this.mIvFingerArea = (ImageView) findViewById(C0268R.C0269id.fingerprint_area_image);
        this.mIvFingerRect = (ImageView) findViewById(C0268R.C0269id.fingerprint_area_rect);
        this.mBtnExit = (Button) findViewById(C0268R.C0269id.exitButton);
        this.mBtnStartRight = (Button) findViewById(C0268R.C0269id.startButtonRight);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.fingerprint_test_qbt2000);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        init();
        this.mTzEnabled = Boolean.valueOf(getIntent().getBooleanExtra("tz_enabled", false));
        StringBuilder sb = new StringBuilder();
        sb.append(" tz_enabled:");
        sb.append(this.mTzEnabled);
        LtUtil.log_d("FingerPrintTest_qbt2000", "onCreate", sb.toString());
        this.mCommonFingerprint_qbt2000 = new CommonFingerprint_qbt2000(this, "FingerPrintTest_qbt2000", this.mTzEnabled.booleanValue());
        if (getIntent().getStringExtra("Action").equals("NORMALDATA")) {
            startScanTest(TestType.NORMALSCAN);
        } else {
            startScanTest(TestType.SNRSCAN);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (isOqcsbftt) {
            NVAccessor.setNV(409, NVAccessor.NV_VALUE_ENTER);
        }
    }

    private void startScanTest(TestType testType) {
        StringBuilder sb = new StringBuilder();
        sb.append(" testType:");
        sb.append(testType.name());
        LtUtil.log_d("FingerPrintTest_qbt2000", "startScanTest", sb.toString());
        drawTable(testType);
        if (testType == TestType.NORMALSCAN) {
            this.mTvSubTitle.setText(C0268R.string.fingerprint_normal_scan);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    FingerPrintTest_qbt2000.this.isRunning = true;
                    FingerPrintTest_qbt2000.this.updateStatus(Status.RUNNING);
                    FingerPrintTest_qbt2000.this.RegisterReceiver();
                    FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.startNormalScan();
                }
            }, 100);
            return;
        }
        this.mTvSubTitle.setText(C0268R.string.fingerprint_snr_scan);
        drawFpRect();
        drawSnrStartBtn();
        this.mAbsoluteLayout.setVisibility(0);
        this.mBtnExit.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.isRunning) {
            StringBuilder sb = new StringBuilder();
            sb.append("NR:");
            sb.append(this.mNormalScanResult.name());
            sb.append(" ,SNR:");
            sb.append(this.mSnrScanResult.name());
            LtUtil.log_d("FingerPrintTest_qbt2000", "onPause", sb.toString());
            stopTest();
        }
        this.mHandler.removeCallbacksAndMessages(null);
        finish();
    }

    /* access modifiers changed from: private */
    public void stopTest() {
        LtUtil.log_d("FingerPrintTest_qbt2000", "stopTest", "stopTest");
        this.mCommonFingerprint_qbt2000.endCheckTimeOut();
        this.mCommonFingerprint_qbt2000.disableListeners();
        this.mCommonFingerprint_qbt2000.endFpManger();
        UnregisterReceiver();
        this.isRunning = false;
    }

    public void drawTable(TestType testType) {
        String[] itemNameList;
        StringBuilder sb = new StringBuilder();
        sb.append(" testType:");
        sb.append(testType.name());
        LtUtil.log_d("FingerPrintTest_qbt2000", "drawTable", sb.toString());
        for (String itemName : this.mCommonFingerprint_qbt2000.getScanItemList(testType)) {
            TableRow mRow = new TableRow(this);
            mRow.setBackgroundColor(-16777216);
            for (int i = 0; i < 5; i++) {
                TextView mTvItem = (TextView) getLayoutInflater().inflate(C0268R.layout.finger_print_qbt2000_table_text_view_data_row, mRow, false);
                if (i == 1) {
                    mTvItem.setText(itemName);
                }
                mRow.addView(mTvItem);
            }
            this.mTableLayout.addView(mRow);
        }
    }

    private void drawSnrStartBtn() {
        float btnWidth_mm = Spec.getFloat(Kernel.ULTRASONIC_RIGHT_START_BTN_SIZE_WIDTH_MM);
        float btnHeight_mm = Spec.getFloat(Kernel.ULTRASONIC_RIGHT_START_BTN_SIZE_HEIGHT_MM);
        float btnPosX_mm = Spec.getFloat(Kernel.ULTRASONIC_RIGHT_START_BTN_POSITION_X_MM) - (btnWidth_mm * 0.5f);
        float btnPosY_mm = Spec.getFloat(Kernel.ULTRASONIC_RIGHT_START_BTN_POSITION_Y_MM) - (0.5f * btnHeight_mm);
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        float btnWidth_px = TypedValue.applyDimension(5, btnWidth_mm, mDisplayMetrics);
        float btnHeight_px = TypedValue.applyDimension(5, btnHeight_mm, mDisplayMetrics);
        float btnPosX_px = TypedValue.applyDimension(5, btnPosX_mm, mDisplayMetrics);
        float btnPosY_px = TypedValue.applyDimension(5, btnPosY_mm, mDisplayMetrics);
        StringBuilder sb = new StringBuilder();
        sb.append(" btnWidth_px:");
        sb.append(btnWidth_px);
        sb.append(" btnHeight_px:");
        sb.append(btnHeight_px);
        sb.append(" btnPosX_px:");
        sb.append(btnPosX_px);
        sb.append(" btnPosY_px:");
        sb.append(btnPosY_px);
        LtUtil.log_d("FingerPrintTest_qbt2000", "drawSnrStartBtn", sb.toString());
        this.mBtnStartRight.setTextSize(1, 12.0f);
        this.mBtnStartRight.setLayoutParams(new LayoutParams((int) btnWidth_px, (int) btnHeight_px, (int) btnPosX_px, (int) btnPosY_px));
        this.mBtnStartRight.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    LtUtil.log_d("FingerPrintTest_qbt2000", "onStartButtonTouched", "onStartButtonTouched");
                    FingerPrintTest_qbt2000.this.mAbsoluteLayout.setVisibility(8);
                    FingerPrintTest_qbt2000.this.mBtnExit.setVisibility(4);
                    FingerPrintTest_qbt2000.this.isRunning = true;
                    FingerPrintTest_qbt2000.this.updateStatus(Status.RUNNING);
                    FingerPrintTest_qbt2000.this.RegisterReceiver();
                    LtUtil.log_d("FingerPrintTest_qbt2000", "onStartButtonTouched", " SnrStartButtonDelayForManual:1000");
                    Sleep.sleep(1000);
                    FingerPrintTest_qbt2000.this.mCommonFingerprint_qbt2000.startSNRPrepare();
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showSnrImg() {
        String path = Kernel.getFilePath(Kernel.ULTRASONIC_FP_SNR_IMAGE);
        StringBuilder sb = new StringBuilder();
        sb.append(" path:");
        sb.append(path);
        LtUtil.log_d("FingerPrintTest_qbt2000", "showSnrImg", sb.toString());
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            LtUtil.log_d("FingerPrintTest_qbt2000", "showSnrImg", " FAIL - decodeFile");
            return;
        }
        this.mIvSnrImage.setImageBitmap(bitmap);
        this.mIvSnrImage.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void showDeadMaskImg() {
        String path = Kernel.getFilePath(Kernel.ULTRASONIC_FP_DEAD_MASK_IMAGE);
        StringBuilder sb = new StringBuilder();
        sb.append(" path:");
        sb.append(path);
        LtUtil.log_d("FingerPrintTest_qbt2000", "showDeadMaskImg", sb.toString());
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            LtUtil.log_d("FingerPrintTest_qbt2000", "showDeadMaskImg", " FAIL - decodeFile");
            return;
        }
        this.mIvDeadMaskImage.setImageBitmap(bitmap);
        this.mIvDeadMaskImage.setVisibility(0);
    }

    private void drawFpRect() {
        String[] pos;
        Display mDisplay = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        int mScreenWidth = outpoint.x;
        int mScreenHeight = outpoint.y;
        StringBuilder sb = new StringBuilder();
        sb.append("Screen Size, Width:");
        sb.append(mScreenWidth);
        sb.append(" Height:");
        sb.append(mScreenHeight);
        LtUtil.log_d("FingerPrintTest_qbt2000", "drawFpRect", sb.toString());
        String raw_pos = Kernel.read(Kernel.ULTRASONIC_FP_POSITION);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" raw value for pos(mm):");
        sb2.append(raw_pos);
        LtUtil.log_d("FingerPrintTest_qbt2000", "drawFpRect", sb2.toString());
        if (raw_pos != null) {
            pos = raw_pos.split(",");
        } else {
            LtUtil.log_d("FingerPrintTest_qbt2000", "drawFpRect", "raw_pos is null, set pos to default values");
            pos = new String[]{"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};
        }
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        float fpWidth = TypedValue.applyDimension(5, Float.parseFloat(pos[2]), mDisplayMetrics);
        float fpHeight = TypedValue.applyDimension(5, Float.parseFloat(pos[3]), mDisplayMetrics);
        float distance_from_left = ((((float) mScreenWidth) * 0.5f) + TypedValue.applyDimension(5, Float.parseFloat(pos[1]), mDisplayMetrics)) - (0.5f * fpWidth);
        float distance_from_bottom = TypedValue.applyDimension(5, Float.parseFloat(pos[0]), mDisplayMetrics);
        float distance_from_top = (((float) mScreenHeight) - distance_from_bottom) - fpHeight;
        float margin = TypedValue.applyDimension(5, 1.0f, mDisplayMetrics);
        Display display = mDisplay;
        StringBuilder sb3 = new StringBuilder();
        Point point = outpoint;
        sb3.append(" fpWidth:");
        sb3.append(fpWidth);
        sb3.append(" fpHeight:");
        sb3.append(fpHeight);
        sb3.append(" distance_from_left:");
        sb3.append(distance_from_left);
        sb3.append(" distance_from_bottom:");
        sb3.append(distance_from_bottom);
        sb3.append(" distance_from_top:");
        sb3.append(distance_from_top);
        sb3.append(" margin:");
        sb3.append(margin);
        LtUtil.log_d("FingerPrintTest_qbt2000", "drawFpRect", sb3.toString());
        int i = mScreenWidth;
        LayoutParams params = new LayoutParams((int) fpWidth, (int) fpHeight, (int) distance_from_left, (int) distance_from_top);
        this.mIvFingerArea.setLayoutParams(params);
        LayoutParams layoutParams = params;
        this.mIvFingerRect.setLayoutParams(new LayoutParams((int) ((2.0f * margin) + fpWidth), (int) ((2.0f * margin) + fpHeight), (int) (distance_from_left - margin), (int) (distance_from_top - margin)));
    }

    /* access modifiers changed from: private */
    public void updateScanResult(TestType testType, HashMap<String, ScanData> scanDataHashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(" testType: ");
        sb.append(testType.name());
        sb.append(" Table Row: ");
        sb.append(this.mTableLayout.getChildCount());
        LtUtil.log_d("FingerPrintTest_qbt2000", "updateScanResult", sb.toString());
        for (int index = 1; index < this.mTableLayout.getChildCount(); index++) {
            TableRow mTableRow = (TableRow) this.mTableLayout.getChildAt(index);
            String itemName = ((TextView) mTableRow.getChildAt(1)).getText().toString();
            if (scanDataHashMap.containsKey(itemName)) {
                ScanData scanData = (ScanData) scanDataHashMap.get(itemName);
                if (scanData.getCategory() != null) {
                    ((TextView) mTableRow.getChildAt(0)).setText(scanData.getCategory());
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(scanData.getSpecMin());
                sb2.append(" ~ ");
                sb2.append(scanData.getSpecMax());
                String specStr = sb2.toString();
                if (specStr.contains(Properties.PROPERTIES_DEFAULT_STRING)) {
                    specStr = "-";
                } else if (specStr.contains("9999 ~ -9999")) {
                    specStr = "TBD";
                }
                ((TextView) mTableRow.getChildAt(2)).setText(specStr);
                ((TextView) mTableRow.getChildAt(3)).setText(scanData.getValue());
                if (scanData.getResult() == Status.PASS || scanData.getResult() == null) {
                    ((TextView) mTableRow.getChildAt(4)).setText(C0268R.string.PASS);
                    ((TextView) mTableRow.getChildAt(4)).setTextColor(-16776961);
                } else if (scanData.getResult() == Status.NOTEST) {
                    ((TextView) mTableRow.getChildAt(4)).setText("-");
                } else {
                    ((TextView) mTableRow.getChildAt(4)).setText(C0268R.string.FAIL);
                    ((TextView) mTableRow.getChildAt(4)).setTextColor(-65536);
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" ItemName: ");
                sb3.append(itemName);
                sb3.append(" ,Value: ");
                sb3.append(scanData.getValue());
                sb3.append(" ,Spec: ");
                sb3.append(specStr);
                LtUtil.log_d("FingerPrintTest_qbt2000", "updateScanResult", sb3.toString());
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Not Exist Test Item in the Table: ");
                sb4.append(itemName);
                LtUtil.log_d("FingerPrintTest_qbt2000", "updateScanResult", sb4.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void setScanResult(TestType testType, Status status) {
        switch (testType) {
            case NORMALSCAN:
                this.mNormalScanResult = status;
                break;
            case SNRSCAN:
                this.mSnrScanResult = status;
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(testType.name());
        sb.append(": ");
        sb.append(status.name());
        LtUtil.log_d("FingerPrintTest_qbt2000", "setScanResult", sb.toString());
    }

    /* access modifiers changed from: private */
    public void updateStatus(Status status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" status:");
        sb.append(status.name());
        LtUtil.log_d("FingerPrintTest_qbt2000", "updateStatus", sb.toString());
        switch (status) {
            case PASS:
                this.mTvScanStatus.setTextColor(-16776961);
                this.mTvScanStatus.setText(C0268R.string.PASS);
                return;
            case FAIL:
                this.mTvScanStatus.setTextColor(-65536);
                this.mTvScanStatus.setText(C0268R.string.FAIL);
                return;
            case RUNNING:
                this.mTvScanStatus.setTextColor(getResources().getColor(C0268R.color.forest_green));
                this.mTvScanStatus.setText(C0268R.string.RUNNING);
                return;
            case READY:
                this.mTvScanStatus.setTextColor(-16777216);
                this.mTvScanStatus.setText(C0268R.string.READY);
                return;
            default:
                return;
        }
    }

    public void onExitButtonClicked(View v) {
        LtUtil.log_d("FingerPrintTest_qbt2000", "onExitButtonClicked", "onExitButtonClicked");
        finish();
    }
}
