package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice.HallIC;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.TestCase;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HallICTest extends BaseActivity implements OnClickListener {
    protected static final String CLASS_NAME = "HallICTest";
    public static final int FONT_COLOR_FAILD = -65536;
    public static final int FONT_COLOR_PASS = -16776961;
    protected static final int KEY_TIMER_EXPIRED = 1;
    protected static final int MILLIS_IN_SEC = Integer.valueOf(TestCase.getString(TestCase.HALLIC_TEST_MILLIS_SEC)).intValue();
    public static final int TESTCASE_CLOSE_CHECK = 3;
    public static final int TESTCASE_DONE = 8;
    public static final int TESTCASE_ONCREATE_RELEASE_CHECK = 0;
    public static final int TESTCASE_OPEN_CHECK = 4;
    public static final int TESTCASE_RELEASE_CHECK = 2;
    public static final int TESTCASE_RELEASE_CHECK2 = 7;
    public static final int TESTCASE_START_RELEASE_CHECK2 = 5;
    public static final int TESTCASE_WORKING_CHECK = 1;
    public static final int TESTCASE_WORKING_CHECK2 = 6;
    private final int KEYCODE_FOLDER_CLOSE = 235;
    private final int KEYCODE_FOLDER_OPEN = 234;
    private CountDownTimer _timer;
    private Button mBtnExit;
    private long mCurrentTime = 0;
    private HallICPoint mHallICPoint;
    private HallICPoint mHallICPoint2;
    private TextView mHallIcTestIndex1;
    private TextView mHallIcTestIndex2;
    private TableLayout mHallIcTestTable;
    private TextView mHallIcTestView;
    private TextView mHallIcTestView2;
    private boolean mIsHallICTestAllPass = false;
    /* access modifiers changed from: private */
    public boolean mIsPressedBackkey = false;
    private boolean mIsReleasePass = false;
    private boolean mIsReleasePass2 = false;
    private boolean mIsWorkingPass = false;
    private boolean mIsWorkingPass2 = false;
    private TextView mReleaseTextView;
    private TextView mReleaseTextView2;
    private Timer mSYSFS_FolderChcekingTimer;
    protected Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                HallICTest.this.mIsPressedBackkey = false;
                LtUtil.log_i(HallICTest.CLASS_NAME, "handleMessage", "KEY_TIMER_EXPIRED");
                if (!HallICTest.this.isFinishing()) {
                    HallICTest.this.CheckingFolderState();
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST_2ND)) {
                        HallICTest.this.CheckingFolderState2();
                    }
                    HallICTest.this.mTimerHandler.sendEmptyMessageDelayed(1, (long) HallICTest.MILLIS_IN_SEC);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Vibrator mVibrator;
    private TextView mWorkingTextView;
    private TextView mWorkingTextView2;
    private TimerTask mmyTask;
    private Object multiDisplayClassInstance;
    private int nHallICTestState = 0;
    private int nHallICTestState2 = 3;
    private float overLength;
    private float resolution_height;
    private float resolution_width;

    public HallICTest() {
        super(CLASS_NAME);
    }

    private void reset() {
        this.nHallICTestState = 0;
        this.nHallICTestState2 = 3;
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER) || Feature.getBoolean(Feature.SUPPORT_BOOK_COVER)) {
            this.nHallICTestState = 3;
        }
        this.mWorkingTextView.setText("");
        this.mWorkingTextView2.setText("");
        this.mReleaseTextView.setText("");
        this.mReleaseTextView2.setText("");
        if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
            this.mHallICPoint.setColor(-16777216);
        }
        if (TestCase.getEnabled(TestCase.HALL_IC_TEST_2ND)) {
            this.mHallICPoint2.setColor(-16777216);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(CLASS_NAME, "onCreate", null);
        super.onCreate(savedInstanceState);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(C0268R.layout.hallictest);
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            fixUsingScreen(true);
        }
        HallICTestInit();
        CheckingFolderState();
        if (Feature.getBoolean(Feature.SUPPORT_BOOK_COVER) || Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            startTimer();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (isOqcsbftt) {
            NVAccessor.setNV(404, NVAccessor.NV_VALUE_ENTER);
        }
    }

    public void onPause() {
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            fixUsingScreen(false);
        }
        super.onPause();
    }

    public void onDestroy() {
        if (Feature.getBoolean(Feature.SUPPORT_BOOK_COVER) || Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            stopTimer();
        }
        super.onDestroy();
    }

    private void fixUsingScreen(boolean value) {
        try {
            Class<?> configurationClass = Class.forName("android.content.res.Configuration");
            int currentDisplayDeviceType = configurationClass.getDeclaredField("semDisplayDeviceType").getInt(getResources().getConfiguration());
            Class<?> multiDisplayClass = Class.forName("com.samsung.android.multidisplay.MultiDisplayManager");
            this.multiDisplayClassInstance = multiDisplayClass.newInstance();
            multiDisplayClass.getMethod("setForcedDefaultDisplayDevice", new Class[]{Context.class, Integer.TYPE, Boolean.TYPE}).invoke(this.multiDisplayClassInstance, new Object[]{this, Integer.valueOf(currentDisplayDeviceType), Boolean.valueOf(value)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("keycode: ");
        sb.append(String.valueOf(keyCode));
        LtUtil.log_i(CLASS_NAME, "onKeyDown", sb.toString());
        if (keyCode == 3) {
            return true;
        }
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER) && (keyCode == 0 || keyCode == 234 || keyCode == 235)) {
            LtUtil.log_d(CLASS_NAME, Feature.SUPPORT_DUAL_LCD_FOLDER, "Temporary checking flip status");
            CheckingFolderState();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("keycode: ");
        sb.append(String.valueOf(keyCode));
        LtUtil.log_i(CLASS_NAME, "onKeyUp", sb.toString());
        if (keyCode == 3) {
            return true;
        }
        reset();
        return super.onKeyUp(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void startTimer() {
        LtUtil.log_i(CLASS_NAME, "startTimer", null);
        this.mTimerHandler.sendEmptyMessageDelayed(1, (long) MILLIS_IN_SEC);
    }

    /* access modifiers changed from: protected */
    public void stopTimer() {
        LtUtil.log_i(CLASS_NAME, "stopTimer", null);
        this.mTimerHandler.removeMessages(1);
    }

    public void onExit() {
        StringBuilder sb = new StringBuilder();
        sb.append("mIsHallICTestAllPass: ");
        sb.append(this.mIsHallICTestAllPass);
        LtUtil.log_i(CLASS_NAME, "onExit", sb.toString());
        if (!this.mIsHallICTestAllPass) {
            setResult(0);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HallICTest.this.mVibrator.cancel();
                HallICTest.this.finish();
            }
        }, 1000);
    }

    public void CheckingFolderState() {
        HallIC nCheckingCurrentFolderState = HallIC.getHallICState();
        StringBuilder sb = new StringBuilder();
        sb.append("nHallICTestState: ");
        sb.append(this.nHallICTestState);
        LtUtil.log_i(CLASS_NAME, "CheckingFolderState", sb.toString());
        switch (this.nHallICTestState) {
            case 0:
                if (nCheckingCurrentFolderState != HallIC.FOLDER_STATE_OPEN) {
                    this.nHallICTestState = 1;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-16777216);
                        break;
                    }
                } else {
                    return;
                }
                break;
            case 1:
                if (nCheckingCurrentFolderState == HallIC.FOLDER_STATE_OPEN) {
                    this.mIsWorkingPass = true;
                    DisplayResult(1);
                    this.nHallICTestState = 2;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-65536);
                        break;
                    }
                }
                break;
            case 2:
                if (nCheckingCurrentFolderState == HallIC.FOLDER_STATE_CLOSE) {
                    this.mIsReleasePass = true;
                    DisplayResult(2);
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-16777216);
                    }
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST_2ND)) {
                        this.nHallICTestState = 5;
                        break;
                    }
                }
                break;
            case 3:
                if (nCheckingCurrentFolderState == HallIC.FOLDER_STATE_CLOSE) {
                    this.mIsWorkingPass = true;
                    DisplayResult(1);
                    this.nHallICTestState = 4;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-65536);
                        break;
                    }
                }
                break;
            case 4:
                if (nCheckingCurrentFolderState == HallIC.FOLDER_STATE_OPEN) {
                    this.mIsReleasePass = true;
                    DisplayResult(2);
                    this.nHallICTestState = 8;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-16777216);
                        break;
                    }
                }
                break;
        }
    }

    public void CheckingFolderState2() {
        HallIC nCheckingCurrentFolderState2 = HallIC.getHallICState2();
        StringBuilder sb = new StringBuilder();
        sb.append("nHallICTestState2: ");
        sb.append(this.nHallICTestState2);
        LtUtil.log_i(CLASS_NAME, "CheckingFolderState2", sb.toString());
        switch (this.nHallICTestState2) {
            case 3:
                if (nCheckingCurrentFolderState2 == HallIC.FOLDER_STATE_CLOSE) {
                    this.mIsWorkingPass2 = true;
                    DisplayResult(6);
                    this.nHallICTestState2 = 4;
                    this.mHallICPoint2.setColor(-65536);
                    return;
                }
                return;
            case 4:
                if (nCheckingCurrentFolderState2 == HallIC.FOLDER_STATE_OPEN) {
                    this.mIsReleasePass2 = true;
                    DisplayResult(7);
                    this.nHallICTestState2 = 8;
                    this.mHallICPoint2.setColor(-16777216);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void HallICTestInit() {
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", Feature.SUPPORT_DUAL_LCD_FOLDER);
            ((TextView) findViewById(C0268R.C0269id.hallic_test)).setVisibility(8);
            ((TextView) findViewById(C0268R.C0269id.working_title)).setText(getString(C0268R.string.hallic_folder_close));
            ((TextView) findViewById(C0268R.C0269id.release_title)).setText(getString(C0268R.string.hallic_folder_open));
        }
        this.mHallIcTestTable = (TableLayout) findViewById(C0268R.C0269id.hallic_test_table);
        this.mHallIcTestView = (TextView) findViewById(C0268R.C0269id.hallic_test);
        this.mWorkingTextView = (TextView) findViewById(C0268R.C0269id.working_text);
        this.mReleaseTextView = (TextView) findViewById(C0268R.C0269id.release_text);
        this.mHallIcTestView2 = (TextView) findViewById(C0268R.C0269id.hallic_test2);
        this.mWorkingTextView2 = (TextView) findViewById(C0268R.C0269id.working_text2);
        this.mReleaseTextView2 = (TextView) findViewById(C0268R.C0269id.release_text2);
        this.mHallIcTestIndex1 = (TextView) findViewById(C0268R.C0269id.hallic_1st);
        this.mHallIcTestIndex2 = (TextView) findViewById(C0268R.C0269id.hallic_2nd);
        this.mBtnExit = (Button) findViewById(C0268R.C0269id.exit);
        this.mBtnExit.setOnClickListener(this);
        int viewStartY = Integer.valueOf(TestCase.getString(TestCase.HALL_IC_TEST)).intValue();
        if (viewStartY != 0) {
            this.mHallIcTestTable.setGravity(51);
            this.mHallIcTestView.setPadding(0, viewStartY, 0, 0);
        }
        if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
            this.resolution_width = (float) getResources().getDisplayMetrics().widthPixels;
            this.resolution_height = (float) getResources().getDisplayMetrics().heightPixels;
            PointF point = TestCase.getViewPointF(TestCase.HALL_IC_TEST);
            float pxX = point.x;
            float pxY = point.y;
            String unit = TestCase.getAttribute(TestCase.HALL_IC_TEST, "unit", "mm");
            StringBuilder sb = new StringBuilder();
            sb.append("unit: ");
            sb.append(unit);
            LtUtil.log_d(CLASS_NAME, "HallICTestInit", sb.toString());
            if ("mm".equalsIgnoreCase(unit)) {
                pxX = TypedValue.applyDimension(5, point.x, getResources().getDisplayMetrics());
                pxY = TypedValue.applyDimension(5, point.y, getResources().getDisplayMetrics());
            }
            float radius = TypedValue.applyDimension(5, 5.0f, getResources().getDisplayMetrics());
            this.mHallICPoint = (HallICPoint) findViewById(C0268R.C0269id.hallicpoint);
            this.mHallICPoint.setVisibility(0);
            this.mHallICPoint.setRadius(radius);
            this.mHallICPoint.setPoint(pxX, pxY);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("radius = ");
            sb2.append(radius);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("pxX = ");
            sb3.append(pxX);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("pxY = ");
            sb4.append(pxY);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb4.toString());
        }
        if (TestCase.getEnabled(TestCase.HALL_IC_TEST_2ND)) {
            this.mHallICPoint2 = (HallICPoint) findViewById(C0268R.C0269id.hallicpoint2);
            this.mHallICPoint2.setVisibility(0);
            PointF point2 = TestCase.getViewPointF(TestCase.HALL_IC_TEST_2ND);
            float pxX2 = point2.x;
            float pxY2 = point2.y;
            String unit2 = TestCase.getAttribute(TestCase.HALL_IC_TEST_2ND, "unit", "mm");
            StringBuilder sb5 = new StringBuilder();
            sb5.append("unit2: ");
            sb5.append(unit2);
            LtUtil.log_d(CLASS_NAME, "HallICTestInit", sb5.toString());
            if ("mm".equalsIgnoreCase(unit2)) {
                pxX2 = TypedValue.applyDimension(5, point2.x, getResources().getDisplayMetrics());
                pxY2 = TypedValue.applyDimension(5, point2.y, getResources().getDisplayMetrics());
            }
            float radius2 = TypedValue.applyDimension(5, 5.0f, getResources().getDisplayMetrics());
            this.mHallICPoint2.setRadius(radius2);
            this.mHallICPoint2.setPoint(pxX2, pxY2);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("radius2 = ");
            sb6.append(radius2);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb6.toString());
            StringBuilder sb7 = new StringBuilder();
            sb7.append("pxX2 = ");
            sb7.append(pxX2);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb7.toString());
            StringBuilder sb8 = new StringBuilder();
            sb8.append("pxY2 = ");
            sb8.append(pxY2);
            LtUtil.log_i(CLASS_NAME, "HallICTestInit", sb8.toString());
        } else {
            this.mHallIcTestIndex1.setVisibility(8);
            this.mHallIcTestIndex2.setVisibility(8);
            this.mHallIcTestView2.setVisibility(8);
            this.mWorkingTextView2.setVisibility(8);
            this.mReleaseTextView2.setVisibility(8);
            ((TextView) findViewById(C0268R.C0269id.working_title2)).setVisibility(8);
            ((TextView) findViewById(C0268R.C0269id.release_title2)).setVisibility(8);
            this.mIsReleasePass2 = true;
            this.mIsWorkingPass2 = true;
        }
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.nHallICTestState = 0;
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER) || Feature.getBoolean(Feature.SUPPORT_BOOK_COVER)) {
            this.nHallICTestState = 3;
        }
        C02413 r2 = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                LtUtil.log_i(HallICTest.CLASS_NAME, "onFinish", "onFinish Countdown Timer End");
                HallICTest.this.mIsPressedBackkey = false;
                HallICTest.this.mVibrator.cancel();
                HallICTest.this.finish();
            }
        };
        this._timer = r2;
    }

    private void DisplayResult(int nCurrentTest) {
        StringBuilder sb = new StringBuilder();
        sb.append(" mIsWorkingPass = ");
        sb.append(this.mIsWorkingPass);
        sb.append(", mIsReleasePass : ");
        sb.append(this.mIsReleasePass);
        sb.append(" mIsWorkingPass2 = ");
        sb.append(this.mIsWorkingPass2);
        sb.append(", mIsReleasePass2 : ");
        sb.append(this.mIsReleasePass2);
        LtUtil.log_i(CLASS_NAME, "DisplayResult", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("nCurrentTest = ");
        sb2.append(nCurrentTest);
        LtUtil.log_i(CLASS_NAME, "DisplayResult", sb2.toString());
        if (!this.mIsHallICTestAllPass) {
            if (nCurrentTest == 1) {
                if (this.mIsWorkingPass) {
                    this.mWorkingTextView.setText("PASS");
                    this.mWorkingTextView.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mWorkingTextView.setText("FAIL");
                    this.mWorkingTextView.setTextColor(-65536);
                }
            }
            if (nCurrentTest == 2) {
                if (this.mIsReleasePass) {
                    this.mReleaseTextView.setText("PASS");
                    this.mReleaseTextView.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mReleaseTextView.setText("FAIL");
                    this.mReleaseTextView.setTextColor(-65536);
                }
            }
            if (nCurrentTest == 6) {
                if (this.mIsWorkingPass2) {
                    this.mWorkingTextView2.setText("PASS");
                    this.mWorkingTextView2.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mWorkingTextView2.setText("FAIL");
                    this.mWorkingTextView2.setTextColor(-65536);
                }
            }
            if (nCurrentTest == 7) {
                if (this.mIsReleasePass2) {
                    this.mReleaseTextView2.setText("PASS");
                    this.mReleaseTextView2.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mReleaseTextView2.setText("FAIL");
                    this.mReleaseTextView2.setTextColor(-65536);
                }
            }
            if (isOqcsbftt && this.mIsWorkingPass && this.mIsReleasePass && this.mIsWorkingPass2 && this.mIsReleasePass2) {
                NVAccessor.setNV(404, NVAccessor.NV_VALUE_PASS);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LtUtil.log_i(CLASS_NAME, "onConfigurationChanged", "onConfigurationChanged");
        CheckingFolderState();
    }

    private String readOneLine(String filepath) {
        String result = "";
        BufferedReader result2 = null;
        StringBuilder sb = new StringBuilder();
        sb.append("filepath: ");
        sb.append(filepath);
        LtUtil.log_i(CLASS_NAME, "readOneLine", sb.toString());
        try {
            BufferedReader buf = new BufferedReader(new FileReader(filepath), 8096);
            result2 = buf.readLine();
            if (result2 != null) {
                result = result2.trim();
            }
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (result2 != null) {
                result2.close();
            }
        } finally {
            if (result2 != null) {
                try {
                    result2.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        if (result == null) {
            return "";
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        StringBuilder sb = new StringBuilder();
        sb.append("putInt State:");
        sb.append(this.nHallICTestState);
        LtUtil.log_i(CLASS_NAME, "onSaveInstanceState", sb.toString());
        outState.putInt("nHallICTestState", this.nHallICTestState);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        StringBuilder sb = new StringBuilder();
        sb.append("now State:");
        sb.append(this.nHallICTestState);
        LtUtil.log_i(CLASS_NAME, "onRestoreInstanceState", sb.toString());
        this.nHallICTestState = savedInstanceState.getInt("nHallICTestState");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getInt State:");
        sb2.append(this.nHallICTestState);
        LtUtil.log_i(CLASS_NAME, "onRestoreInstanceState", sb2.toString());
    }

    public void onClick(View v) {
        if (v.getId() == C0268R.C0269id.exit) {
            finish();
        }
    }
}
