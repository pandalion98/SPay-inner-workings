package com.sec.android.app.hwmoduletest;

import android.app.Activity;
import android.app.BarBeamException;
import android.app.BarBeamFactory;
import android.app.Hop;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.BarcodeEmulTest.BeamTask;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;

public class BeamActivity extends Activity {
    private static final String CLASS_NAME = "BeamActivity";
    public static final String POSITION_KEY = "position-task";
    public static final String TASK_KEY = "beam-task";
    private static BarBeamFactory beamFactory = null;
    private byte[][] barcodeByte;
    private ImageView barcodeImage;
    private byte[] barcodeTemp;
    private BeamTask beamTask;
    private String[] dataId;
    boolean hadError;
    Handler handler = new Handler();
    private Hop[] hopSequenceArray;
    private int[] imageId;
    private int position;
    private String[] textId;
    private TextView viewStatus;

    public BeamActivity() {
        Hop hop = new Hop(0, 50, 50, 20, 31);
        Hop hop2 = new Hop(20, 20, 50, 10, 31);
        Hop hop3 = new Hop(2, 50, 50, 12, 31);
        Hop hop4 = new Hop(8, 50, 50, 10, 31);
        Hop hop5 = new Hop(12, 30, 50, 10, 31);
        Hop hop6 = new Hop(1, 50, 50, 16, 31);
        Hop hop7 = new Hop(14, 30, 50, 10, 31);
        Hop hop8 = new Hop(4, 50, 50, 10, 31);
        Hop hop9 = new Hop(10, 40, 50, 10, 31);
        Hop hop10 = new Hop(6, 50, 50, 10, 31);
        this.hopSequenceArray = new Hop[]{hop, hop2, hop3, hop4, hop5, hop6, hop7, hop8, hop9, hop10};
        this.imageId = new int[]{C0268R.drawable.upca, C0268R.drawable.ean, C0268R.drawable.ean8, C0268R.drawable.code39, C0268R.drawable.code128, C0268R.drawable.intervaled, C0268R.drawable.codebar, C0268R.drawable.gs1databar};
        this.textId = new String[]{"UPC-A", "EAN-13", "EAN-8", "Code 39", "Code 128", "Interleaved 2 of 5", "Codebar", "GS1 Databar"};
        this.dataId = new String[]{"123456789012", "1234567890128", "12345670", "Code 39", "Code 128", "12345678", "A1234567890A", "(01)00614141999996"};
        this.barcodeByte = new byte[][]{new byte[]{-1, -84, -37, HwModuleTest.ID_2ND_LCD, 92, -99, 66, -82, -37, -117, HwModuleTest.ID_TSP_HOVERING, 100, -102, -2}, new byte[]{-1, -83, -112, -79, NVAccessor.NV_VALUE_NOTEST, -11, -70, -83, -59, -115, 50, 77, -70, -1}, new byte[]{-1, -21, 54, -56, 87, HwModuleTest.ID_IRIS_LED, -59, 123, -79, -85, -1}, new byte[]{-1, -9, 68, 81, HwModuleTest.ID_SENSORHUB, 81, NVAccessor.NV_VALUE_ENTER, -43, HwModuleTest.ID_SPEN_HOVERING, HwModuleTest.ID_IR_LED, 71, 87, 81, HwModuleTest.ID_IR_LED, HwModuleTest.ID_SPEN_HOVERING, 84, 116, 87, 68, 95, -1}, new byte[]{-1, -53, 123, -71, FactoryTestPhone.OEM_MISC_GET_GRIP_SENSOR_INFO, -81, 101, 55, -78, 54, 50, 99, NVAccessor.NV_VALUE_ENTER, -104, -39, 56, -89, -1}, new byte[]{-1, -11, HwModuleTest.ID_LOOPBACK, 76, -106, -78, -51, 90, -103, HwModuleTest.ID_TOF_CAM, -1}, new byte[]{-1, -23, -75, 77, 90, 77, 84, -76, -83, 106, 90, 86, 84, -75, 86, 83, 111, -1}, new byte[]{-34, -69, -56, 7, -90, 122, HwModuleTest.ID_IR_LED, -67, -38, HwModuleTest.ID_MLC, -116, -10, -28, Byte.MIN_VALUE, -93, -127, -21}};
        this.position = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.activity_beam);
        this.barcodeImage = (ImageView) findViewById(C0268R.C0269id.imageBarcode);
        this.viewStatus = (TextView) findViewById(C0268R.C0269id.textStatus);
        ((Button) findViewById(C0268R.C0269id.buttonStart)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BeamActivity.this.StartBeaming();
            }
        });
        ((Button) findViewById(C0268R.C0269id.buttonStop)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BeamActivity.this.StopBeaming();
            }
        });
        this.beamTask = (BeamTask) getIntent().getExtras().get(TASK_KEY);
        this.position = getIntent().getIntExtra(POSITION_KEY, 0);
        ((TextView) findViewById(C0268R.C0269id.textDigits)).setText(this.dataId[this.position]);
        ((TextView) findViewById(C0268R.C0269id.textSymbology)).setText(this.textId[this.position]);
        this.barcodeTemp = this.barcodeByte[this.position];
        this.barcodeImage.setImageResource(this.imageId[this.position]);
        try {
            beamFactory = new BarBeamFactory(this, this.hopSequenceArray);
        } catch (BarBeamException e1) {
            e1.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        StopBeaming();
        super.onPause();
    }

    public void StartBeaming() {
        boolean result = false;
        try {
            result = beamFactory.StartBarBeamFactory(this.barcodeTemp);
        } catch (BarBeamException e) {
            e.printStackTrace();
        }
        this.viewStatus.setText("Beaming...");
        StringBuilder sb = new StringBuilder();
        sb.append("StartBarBeamFactory-- : ");
        sb.append(result);
        LtUtil.log_d(CLASS_NAME, "StartBeaming", sb.toString());
    }

    public void StopBeaming() {
        boolean result = false;
        StringBuilder sb = new StringBuilder();
        sb.append("StopBarBeamFactory ++ ");
        sb.append(false);
        LtUtil.log_d(CLASS_NAME, "StopBeaming", sb.toString());
        try {
            result = beamFactory.StopBarBeamFactory();
        } catch (BarBeamException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("exception in StopBarBeamFactory");
            sb2.append(e);
            LtUtil.log_d(CLASS_NAME, "StopBeaming", sb2.toString());
        }
        this.viewStatus.setText("");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("StopBarBeamFactory -- ");
        sb3.append(result);
        LtUtil.log_d(CLASS_NAME, "StopBeaming", sb3.toString());
    }
}
