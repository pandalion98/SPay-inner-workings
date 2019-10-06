package com.sec.android.app.hwmoduletest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class TouchTestFail extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TouchTestFail.this.finish();
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(C0268R.layout.touch_test_fail);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(), 1000);
    }
}
