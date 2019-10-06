package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;

public class IrisLedTest extends BaseActivity {
    private final int IRIS_LED = 2;

    public IrisLedTest() {
        super("IrisLedTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-1);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        startIrisLedTest(2);
    }

    private void startIrisLedTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request: ");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startIrisLedTest", sb.toString());
        if (VERSION.SDK_INT < 23 || Feature.getBoolean(Feature.SUPPORT_IRIS_FOR_AADHAR_ONLY)) {
            intent.setClassName("com.sec.android.irisledtest", "com.sec.android.irisledtest.IrisLedTest");
        } else {
            intent.setClassName("com.sec.factory.camera", "com.sec.android.app.iris.ledtest.IrisLedTest");
        }
        intent.putExtra("test_type", "hwtest");
        try {
            startActivityForResult(intent, request);
        } catch (Exception e) {
            LtUtil.log_e(e);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("request: ");
        sb.append(requestCode);
        sb.append(", resultCode: ");
        sb.append(resultCode);
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb.toString());
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "IrisLedTest Finish");
        finish();
    }
}
