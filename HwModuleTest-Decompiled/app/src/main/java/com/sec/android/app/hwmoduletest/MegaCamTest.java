package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class MegaCamTest extends BaseActivity {
    private final int FINISH_CAM_IMAGE_VIEW = 99;
    private final int MEGA_CAM = 1;
    private final int REQUEST_CAM_IMAGE_VIEW = 99;
    private int mTestitem = 0;

    public MegaCamTest() {
        super("MegaCamTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-16777216);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        this.mTestitem = 1;
        startMegaCamTest(1);
    }

    private void startMegaCamTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request=");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startMegaCamTest", sb.toString());
        this.mTestitem = request;
        Intent i = new Intent();
        i.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.CameraTestActivity");
        i.putExtra("testtype", "HW");
        i.putExtra("arg1", "mega");
        startActivityForResult(intent, request);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("request = ");
        sb.append(requestCode);
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb.toString());
        if (requestCode != 1) {
            if (requestCode == 99) {
                if (ModuleDevice.instance(this).isDualCamera()) {
                    LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "start tele cam");
                    Intent intentTele = new Intent();
                    intentTele.setClass(this, TeleCamTest.class);
                    startActivity(intentTele);
                }
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
