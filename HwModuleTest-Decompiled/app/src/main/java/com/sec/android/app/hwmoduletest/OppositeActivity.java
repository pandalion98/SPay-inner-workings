package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;

public class OppositeActivity extends BaseActivity {
    private LinearLayout mLayout;
    /* access modifiers changed from: private */
    public String mLogText = "";
    /* access modifiers changed from: private */
    public TextView mTitle;

    public OppositeActivity() {
        super("OppositeActivity");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLogText = getResources().getString(C0268R.string.app_name);
        this.mLayout = new LinearLayout(this);
        this.mLayout.setBackgroundResource(C0268R.color.white);
        this.mTitle = new TextView(this);
        this.mTitle.setText("HwModuleTest");
        this.mLayout.addView(this.mTitle);
        setContentView(this.mLayout);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    private void debugKeyEvent(int keyCode, KeyEvent event) {
        TextView textView = this.mTitle;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mLogText);
        sb.append("\nkeyCode: ");
        sb.append(KeyEvent.keyCodeToString(keyCode));
        sb.append("\nKeyAction: ");
        sb.append(KeyEvent.actionToString(event.getAction()));
        textView.setText(sb.toString());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                OppositeActivity.this.mTitle.setText(OppositeActivity.this.mLogText);
            }
        }, 500);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        debugKeyEvent(keyCode, event);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        debugKeyEvent(keyCode, event);
        return true;
    }
}
