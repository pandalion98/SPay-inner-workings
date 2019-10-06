package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import java.util.Locale;

public class FrontCamTest extends BaseActivity {
    private final int CAMERA_ID = (Feature.getBoolean(Feature.SUPPORT_REAR_CAMERA) ? 1 : 0);
    private final int DUAL_CAMERA_ID = 3;
    private final int FINISH_CAM_DUAL_TEST = 2;
    private final int FINISH_CAM_IMAGE_VIEW = 99;
    private final int FINISH_CAM_NORMAL_TEST = 1;
    private final int FINISH_CAM_WITH_COMPANION_IC_TEST = 3;
    private final int FINISH_FRONT_CAM_TEST = 0;
    private final int REQUEST_CAM_DUAL_TEST = 2;
    private final int REQUEST_CAM_IMAGE_VIEW = 99;
    private final int REQUEST_CAM_NORMAL_TEST = 1;
    private final int REQUEST_CAM_WITH_COMPANION_IC_TEST = 3;
    private String mTestCase;
    private int mTestitem = 0;

    public FrontCamTest() {
        super("FrontCamTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTestCase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_FRONT_CAM);
        if (this.mTestCase != null) {
            this.mTestCase = this.mTestCase.toUpperCase(Locale.ENGLISH);
        } else {
            this.mTestCase = "";
        }
        View view = new View(this);
        view.setBackgroundColor(-16777216);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        startFrontCamTest(1);
    }

    private void startFrontCamTest(int request) {
        Intent intent = new Intent();
        StringBuilder sb = new StringBuilder();
        sb.append("request=");
        sb.append(request);
        LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", sb.toString());
        this.mTestitem = request;
        switch (request) {
            case 0:
                LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", "FINISH_FRONT_CAM_TEST");
                finish();
                return;
            case 1:
                LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", "REQUEST_CAM_NORMAL_TEST");
                intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.Camera");
                intent.putExtra("camera_id", this.CAMERA_ID);
                intent.putExtra("ois_test", true);
                intent.putExtra("camcorder_preview_test", true);
                intent.putExtra("postview_test", true);
                try {
                    startActivityForResult(intent, request);
                    return;
                } catch (Exception e) {
                    LtUtil.log_e(e);
                    return;
                }
            case 2:
                LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", "REQUEST_CAM_DUAL_TEST");
                intent.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.Camera");
                intent.putExtra("camera_id", 3);
                intent.putExtra("ois_test", true);
                intent.putExtra("camcorder_preview_test", true);
                intent.putExtra("postview_test", true);
                try {
                    startActivityForResult(intent, request);
                    return;
                } catch (Exception e2) {
                    LtUtil.log_e(e2);
                    return;
                }
            case 3:
                if (this.mTestCase.contains("CAM_WITH_COMPANION_IC")) {
                    LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", "REQUEST_CAM_WITH_COMPANION_IC_TEST");
                    Intent i = new Intent();
                    i.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.Camera");
                    i.putExtra("camera_id", this.CAMERA_ID);
                    i.putExtra("ois_test", true);
                    i.putExtra("camcorder_preview_test", true);
                    i.putExtra("postview_test", true);
                    i.putExtra("companion_ic_on", true);
                    try {
                        startActivityForResult(i, request);
                        return;
                    } catch (Exception e3) {
                        LtUtil.log_e(e3);
                        return;
                    }
                } else {
                    startFrontCamTest(request + 1);
                    return;
                }
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("not defined Request: ");
                sb2.append(request);
                LtUtil.log_d(this.CLASS_NAME, "startFrontCamTest", sb2.toString());
                startFrontCamTest(0);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("request = ");
        sb.append(requestCode);
        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb.toString());
        if (requestCode != 99) {
            switch (requestCode) {
                case 1:
                    if (resultCode != -1) {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "TEST FAIL");
                        startFrontCamTest(0);
                        break;
                    } else {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "FINISH_CAM_NORMAL_TEST");
                        String filePath = intent.getStringExtra("data_filepath");
                        if (filePath == null) {
                            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "Result FAIL");
                            startFrontCamTest(0);
                            break;
                        } else {
                            startActivityForResult(new Intent(this, CameraImageView.class).putExtra("bg_filepath", filePath).putExtra("frontcam", true), 99);
                            break;
                        }
                    }
                case 2:
                    if (resultCode != -1) {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "TEST FAIL");
                        startFrontCamTest(0);
                        break;
                    } else {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "FINISH_CAM_DUAL_TEST");
                        String filePath2 = intent.getStringExtra("data_filepath");
                        if (filePath2 == null) {
                            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "Result FAIL");
                            startFrontCamTest(0);
                            break;
                        } else {
                            startActivityForResult(new Intent(this, CameraImageView.class).putExtra("bg_filepath", filePath2).putExtra("frontcam", true), 99);
                            break;
                        }
                    }
                case 3:
                    if (resultCode != -1) {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "TEST FAIL");
                        startFrontCamTest(0);
                        break;
                    } else {
                        LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "FINISH_CAM_WITH_COMPANION_IC_TEST");
                        String filePath3 = intent.getStringExtra("data_filepath");
                        if (filePath3 == null) {
                            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "Result FAIL");
                            startFrontCamTest(0);
                            break;
                        } else {
                            startActivityForResult(new Intent(this, CameraImageView.class).putExtra("bg_filepath", filePath3).putExtra("frontcam", true), 99);
                            break;
                        }
                    }
                default:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("not defined Request: ");
                    sb2.append(requestCode);
                    LtUtil.log_d(this.CLASS_NAME, "onActivityResult", sb2.toString());
                    startFrontCamTest(0);
                    break;
            }
        } else if (resultCode == -1) {
            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "FINISH_CAM_IMAGE_VIEW");
            if (this.mTestitem == 1) {
                if (ModuleDevice.instance(this).isFrontDualCamera()) {
                    LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "REQUEST_CAM_DUAL_TEST");
                    startFrontCamTest(2);
                } else {
                    startFrontCamTest(3);
                }
            } else if (this.mTestitem == 3) {
                startFrontCamTest(0);
            } else {
                startFrontCamTest(3);
            }
        } else {
            LtUtil.log_d(this.CLASS_NAME, "onActivityResult", "Result FAIL");
            startFrontCamTest(0);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
