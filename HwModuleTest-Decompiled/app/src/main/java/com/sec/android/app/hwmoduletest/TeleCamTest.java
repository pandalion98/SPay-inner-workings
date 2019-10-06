package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;

public class TeleCamTest extends BaseActivity {
    private final int FINISH_CAM_IMAGE_VIEW = 99;
    private final int REQUEST_CAM_IMAGE_VIEW = 99;
    private final int TELE_CAM = 1;
    private int mTestitem = 0;

    public TeleCamTest() {
        super("TeleCamTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-16777216);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        this.mTestitem = 1;
        startTeleCamTest(1);
    }

    private void startTeleCamTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request=");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startTeleCamTest", sb.toString());
        this.mTestitem = request;
        if ("camera".equalsIgnoreCase(Feature.getString(Feature.DEVICE_TYPE))) {
            LtUtil.log_d(this.CLASS_NAME, "run ", "com.samsung.difactorycamera");
            intent.setClassName("com.samsung.difactorycamera", "com.samsung.difactorycamera.Camera");
        } else {
            intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.Camera");
            intent.putExtra("ois_test", true);
        }
        intent.putExtra("camera_id", 2);
        intent.putExtra("camcorder_preview_test", true);
        intent.putExtra("postview_test", true);
        try {
            startActivityForResult(intent, request);
        } catch (Exception e) {
            LtUtil.log_e(e);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("request = ");
        sb.append(requestCode);
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb.toString());
        if (requestCode != 1) {
            if (requestCode == 99) {
                finish();
            }
        } else if (resultCode == -1) {
            String filePath = intent.getStringExtra("data_filepath");
            if (filePath != null) {
                startActivityForResult(new Intent(this, CameraImageView.class).putExtra("bg_filepath", filePath).putExtra("frontcam", false), 99);
            } else {
                LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "file path null");
                finish();
            }
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
