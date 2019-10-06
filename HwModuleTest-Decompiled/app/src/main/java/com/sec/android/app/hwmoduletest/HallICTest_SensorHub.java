package com.sec.android.app.hwmoduletest;

import android.graphics.PointF;
import android.hardware.scontext.SContext;
import android.hardware.scontext.SContextEvent;
import android.hardware.scontext.SContextHallSensor;
import android.hardware.scontext.SContextListener;
import android.hardware.scontext.SContextManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.TestCase;

public class HallICTest_SensorHub extends BaseActivity implements SContextListener, OnClickListener {
    protected static final String CLASS_NAME = "HallICTest_SensorHub";
    public static final int FONT_COLOR_FAILD = -65536;
    public static final int FONT_COLOR_PASS = -16776961;
    public static final int STATE_CLOSE = 1;
    public static final int STATE_OPEN = 0;
    public static final int TESTCASE_CLOSE_CHECK = 1;
    public static final int TESTCASE_DONE = 5;
    public static final int TESTCASE_OPEN_CHECK = 2;
    private int currentState = -1;
    private Button mBtnExit;
    private HallICPoint mHallICPoint;
    private HallICPoint mHallICPoint2;
    private TextView mHallIcTestIndex1;
    private TextView mHallIcTestIndex2;
    private TableLayout mHallIcTestTable;
    private TextView mHallIcTestView;
    private TextView mHallIcTestView2;
    private boolean mIsHallICTestAllPass = false;
    private boolean mIsReleasePass = false;
    private boolean mIsReleasePass2 = false;
    private boolean mIsWorkingPass = false;
    private boolean mIsWorkingPass2 = false;
    private TextView mReleaseTextView;
    private TextView mReleaseTextView2;
    private SContextManager mSContextManager;
    private Vibrator mVibrator;
    private TextView mWorkingTextView;
    private TextView mWorkingTextView2;
    private int nHallICTestState = 1;
    private float overLength;
    private float resolution_height;
    private float resolution_width;

    public HallICTest_SensorHub() {
        super(CLASS_NAME);
    }

    private void reset() {
        this.nHallICTestState = 1;
        this.mWorkingTextView.setText("");
        this.mWorkingTextView2.setText("");
        this.mReleaseTextView.setText("");
        this.mReleaseTextView2.setText("");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(CLASS_NAME, "onCreate", null);
        super.onCreate(savedInstanceState);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(C0268R.layout.hallictest);
        this.mSContextManager = (SContextManager) getSystemService("scontext");
        this.mSContextManager.registerListener(this, 43);
        HallICTestInit();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        this.mSContextManager.unregisterListener(this, 43);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("keycode: ");
        sb.append(String.valueOf(keyCode));
        LtUtil.log_i(CLASS_NAME, "onKeyDown", sb.toString());
        if (keyCode == 3) {
            return true;
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

    public void onSContextChanged(SContextEvent event) {
        SContext scontext = event.scontext;
        SContextHallSensor hallSensor = event.getHallSensorContext();
        if (scontext.getType() == 43) {
            short hallType = hallSensor.getType();
            if (hallType == 1) {
                this.currentState = 0;
            } else if (hallType == 0) {
                this.currentState = 1;
            }
            check_state();
        }
    }

    public void check_state() {
        StringBuilder sb = new StringBuilder();
        sb.append("hallIC state : ");
        sb.append(this.currentState);
        LtUtil.log_i(CLASS_NAME, "CheckingFolderState", sb.toString());
        switch (this.nHallICTestState) {
            case 1:
                if (this.currentState == 1) {
                    this.mIsWorkingPass = true;
                    DisplayResult();
                    this.nHallICTestState = 2;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-65536);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                if (this.currentState == 0) {
                    this.mIsReleasePass = true;
                    DisplayResult();
                    this.nHallICTestState = 5;
                    if (TestCase.getEnabled(TestCase.HALL_IC_TEST)) {
                        this.mHallICPoint.setColor(-16777216);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void HallICTestInit() {
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
    }

    private void DisplayResult() {
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
        if (!this.mIsHallICTestAllPass) {
            if (this.nHallICTestState == 1) {
                if (this.mIsWorkingPass) {
                    this.mWorkingTextView.setText("PASS");
                    this.mWorkingTextView.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mWorkingTextView.setText("FAIL");
                    this.mWorkingTextView.setTextColor(-65536);
                }
            }
            if (this.nHallICTestState == 2) {
                if (this.mIsReleasePass) {
                    this.mReleaseTextView.setText("PASS");
                    this.mReleaseTextView.setTextColor(-16776961);
                    this.mVibrator.vibrate(700);
                } else {
                    this.mReleaseTextView.setText("FAIL");
                    this.mReleaseTextView.setTextColor(-65536);
                }
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == C0268R.C0269id.exit) {
            finish();
        }
    }
}
