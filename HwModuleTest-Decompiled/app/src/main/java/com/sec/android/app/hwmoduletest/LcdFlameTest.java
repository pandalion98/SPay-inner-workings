package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings.System;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.goodix.cap.fingerprint.Constants;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import com.sec.xmldata.support.Support.TestCase;
import java.io.FileWriter;
import java.io.IOException;

public class LcdFlameTest extends BaseActivity {
    private static final int MAX = 255;
    private static final int MIN = 0;
    private static final String OFF = "0";

    /* renamed from: ON */
    private static final String f17ON = "1";
    private static final String TAG = "Lcdflametest";
    static LinearLayout mLayout;
    int colorIndex = 0;
    COLOR[] colors = COLOR.values();
    private PowerManager mPowerManager;

    enum COLOR {
        BLACK(Color.rgb(0, 0, 0), true),
        GRAY(Color.rgb(63, 63, 63), true),
        WHITE(Color.rgb(LcdFlameTest.MAX, LcdFlameTest.MAX, LcdFlameTest.MAX), true),
        SEMI_BLACK(Color.rgb(30, 30, 30), true);
        
        private int color;
        boolean enable;

        private COLOR(int _color, boolean _enable) {
            this.color = _color;
            this.enable = _enable;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int localColor) {
            this.color = localColor;
        }

        public void setEnable(boolean flag) {
            this.enable = flag;
        }

        public boolean getEnable() {
            return this.enable;
        }
    }

    public class MyView extends View {
        private final float[] coordi_X = new float[3];
        private final float[] coordi_Y = new float[3];
        private final int mScreenHeight;
        private final int mScreenWidth;

        public MyView(Context context) {
            super(context);
            this.mScreenWidth = LcdFlameTest.this.getWindowManager().getDefaultDisplay().getWidth();
            this.mScreenHeight = LcdFlameTest.this.getWindowManager().getDefaultDisplay().getHeight();
            init();
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5.0f);
            paint.setStyle(Style.STROKE);
            paint.setColor(COLOR.BLACK.getColor());
            float px = TypedValue.applyDimension(5, 5.0f, getResources().getDisplayMetrics());
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    canvas.drawCircle(this.coordi_X[i], this.coordi_Y[j], px, paint);
                }
            }
        }

        private void init() {
            for (int i = 1; i < 4; i++) {
                this.coordi_X[i - 1] = (((float) (this.mScreenWidth * i)) * 1.0f) / 4.0f;
                this.coordi_Y[i - 1] = (((float) (this.mScreenHeight * i)) * 1.0f) / 4.0f;
            }
        }
    }

    public LcdFlameTest() {
        super("LcdFlameTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mLayout = new LinearLayout(this);
        String str = "";
        String[] mSpecVal = Spec.getString(Spec.BLACK_LCD_BRIGHTNESS).split(",");
        COLOR.SEMI_BLACK.setColor(Color.rgb(Integer.parseInt(mSpecVal[0]), Integer.parseInt(mSpecVal[1]), Integer.parseInt(mSpecVal[2])));
        mLayout.setBackgroundColor(COLOR.BLACK.getColor());
        mLayout.setGravity(17);
        this.mPowerManager = (PowerManager) getApplicationContext().getSystemService("power");
        if (this.mPowerManager != null) {
            setButtonBrightnessLimit(0);
            setBrightness(MAX);
        }
        if (Feature.getBoolean(Feature.IS_DBLC_FUNCTION, false)) {
            TextView textView = new TextView(this);
            textView.setTextColor(Color.rgb(1, 1, 1));
            textView.setText(".");
            mLayout.addView(textView);
        }
        setContentView(mLayout);
        mLayout.addView(new MyView(this));
        LtUtil.setRemoveSystemUI(getWindow(), true);
        writeFile("/sys/class/mdnie/mdnie/cabc", OFF);
        if (TestCase.getEnabled(TestCase.IS_BACKLIGHT_ON_FOR_GREEN_LCD)) {
            writeFile(Kernel.PATH_BACKLIGHT_ON_FOR_GREEN_LCD, "1");
        }
        if (mIsDualScreenFeatureEnabled && !getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
            LtUtil.log_d(this.CLASS_NAME, "onCreate", "start Second Activity");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass(getBaseContext(), getClass());
            intent.putExtra(BaseActivity.SECOND_ACTIVITY, true);
            intent.putExtra("color", this.colors[this.colorIndex].getColor());
            SDualScreenActivity.makeIntent(this, intent, DualScreen.SUB, SDualScreenActivity.FLAG_COUPLED_TASK_EXPAND_MODE);
            startActivityForResult(intent, this.colors[this.colorIndex].getColor());
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (this.mPowerManager != null) {
            setButtonBrightnessLimit(MAX);
        }
        if (TestCase.getEnabled(TestCase.IS_BACKLIGHT_ON_FOR_GREEN_LCD)) {
            writeFile(Kernel.PATH_BACKLIGHT_ON_FOR_GREEN_LCD, OFF);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (TestCase.getEnabled(TestCase.IS_BACKLIGHT_ON_FOR_GREEN_LCD)) {
            writeFile(Kernel.PATH_BACKLIGHT_ON_FOR_GREEN_LCD, "1");
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (TestCase.getEnabled(TestCase.IS_BACKLIGHT_ON_FOR_GREEN_LCD)) {
            writeFile(Kernel.PATH_BACKLIGHT_ON_FOR_GREEN_LCD, OFF);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode) {
            case 24:
                finish();
                break;
            case Constants.CMD_TEST_CANCEL /*25*/:
                this.colorIndex = (this.colorIndex + 1) % 4;
                while (!this.colors[this.colorIndex].getEnable()) {
                    this.colorIndex = (this.colorIndex + 1) % 4;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("set next color = ");
                sb.append(this.colors[this.colorIndex].name());
                LtUtil.log_i(this.CLASS_NAME, "onKeyDown", sb.toString());
                mLayout.setBackgroundColor(this.colors[this.colorIndex].getColor());
                break;
            case 26:
                finish();
                break;
        }
        return true;
    }

    private void setBrightness(int brightness) {
        ModulePower.instance(getApplicationContext()).setBrightness(brightness);
    }

    public int getBrightness() {
        return System.getInt(getContentResolver(), "screen_brightness", 0);
    }

    public void setButtonBrightnessLimit(int brightness) {
        this.mPowerManager.setButtonBrightnessLimit(brightness);
    }

    private void writeFile(String filepath, String value) {
        String str;
        StringBuilder sb;
        FileWriter fw = null;
        try {
            fw = new FileWriter(filepath);
            fw.write(value);
            try {
                fw.close();
                return;
            } catch (IOException e) {
                str = TAG;
                sb = new StringBuilder();
            }
            sb.append(filepath);
            sb.append("IOException");
            Log.e(str, sb.toString());
        } catch (IOException e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(filepath);
            sb2.append("IOException");
            Log.e(str2, sb2.toString());
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e3) {
                    str = TAG;
                    sb = new StringBuilder();
                }
            }
        } catch (Throwable th) {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e4) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(filepath);
                    sb3.append("IOException");
                    Log.e(TAG, sb3.toString());
                }
            }
            throw th;
        }
    }
}
