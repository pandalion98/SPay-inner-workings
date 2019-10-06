package com.sec.android.app.hwmoduletest;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class TspFailPop extends Activity {
    public boolean onTouchEvent(MotionEvent event) {
        setResult(0);
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(C0268R.layout.tsp_fail_pop);
    }
}
