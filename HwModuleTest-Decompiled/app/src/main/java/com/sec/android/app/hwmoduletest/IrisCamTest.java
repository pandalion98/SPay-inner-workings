package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;

public class IrisCamTest extends BaseActivity {
    private final int IRIS_CAM = 1;

    public IrisCamTest() {
        super("IrisCamTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-1);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        startIrisCamTest(1);
    }

    private void startIrisCamTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request: ");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startIrisCamTest", sb.toString());
        if (VERSION.SDK_INT < 23 || Feature.getBoolean(Feature.SUPPORT_IRIS_FOR_AADHAR_ONLY)) {
            intent.setClassName("com.sec.android.iriscameratest", "com.sec.android.iriscameratest.IrisCameraTest");
        } else {
            intent.setClassName("com.sec.factory.iris.usercamera", "com.sec.android.app.iris.usertest.UserTest");
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
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "IrisCamTest Finish");
        finish();
    }
}
