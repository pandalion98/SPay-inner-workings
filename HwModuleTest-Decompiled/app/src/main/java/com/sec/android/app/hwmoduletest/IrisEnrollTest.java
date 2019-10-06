package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class IrisEnrollTest extends BaseActivity {
    private final int IRIS_ENROLL = 3;

    public IrisEnrollTest() {
        super("IrisEnrollTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-1);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        startIrisEnrollTest(3);
    }

    private void startIrisEnrollTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request: ");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startIrisEnrollTest", sb.toString());
        intent.setClassName("com.sec.factory.camera", "com.sec.android.app.iris.enrolltest.IrisEnrollTest");
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
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "IrisEnrollTest Finish");
        finish();
    }
}
