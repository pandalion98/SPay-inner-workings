package com.sec.android.app.hwmoduletest;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GyroscopeDisplay extends BaseActivity {
    private static final String GYRO_DPS_VALUE_2000 = "2000";
    private static final String GYRO_DPS_VALUE_250 = "250";
    private static final String GYRO_DPS_VALUE_500 = "500";
    private final float TEXT_SCALING_COOR = 57.32484f;
    /* access modifiers changed from: private */
    public HistoryAdaptor mAdaptor;
    /* access modifiers changed from: private */
    public int mCount = 0;
    private TextView mGyroDisplayTitleText;
    private Sensor mGyroSensor;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mIsSubGyro;
    private ListView mListView;
    private SensorTestListener mSensorListener;
    private SensorManager mSensorManager;
    private Sensor mSubGyroSensor;
    private Timer mTimer;
    /* access modifiers changed from: private */
    public List<HistoryItem> mValueList = new ArrayList();
    /* access modifiers changed from: private */

    /* renamed from: mX */
    public float f9mX;
    /* access modifiers changed from: private */

    /* renamed from: mY */
    public float f10mY;
    /* access modifiers changed from: private */

    /* renamed from: mZ */
    public float f11mZ;

    public static class AlertDialogFragment extends DialogFragment {
        public static AlertDialogFragment newInstance(int title) {
            AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("title", title);
            alertDialogFragment.setArguments(bundle);
            return alertDialogFragment;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new Builder(getActivity()).setTitle(getArguments().getInt("title")).setMessage("Choose DPS").setPositiveButton("250 dps", new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).writeDpsValue(GyroscopeDisplay.GYRO_DPS_VALUE_250);
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).startGyroDisplay();
                    AlertDialogFragment.this.dismiss();
                }
            }).setNeutralButton("500 dps", new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).writeDpsValue(GyroscopeDisplay.GYRO_DPS_VALUE_500);
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).startGyroDisplay();
                    AlertDialogFragment.this.dismiss();
                }
            }).setNegativeButton("2000 dps", new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).writeDpsValue(GyroscopeDisplay.GYRO_DPS_VALUE_2000);
                    ((GyroscopeDisplay) AlertDialogFragment.this.getActivity()).startGyroDisplay();
                    AlertDialogFragment.this.dismiss();
                }
            }).create();
        }
    }

    private static class HistoryAdaptor extends ArrayAdapter<HistoryItem> {
        public HistoryAdaptor(Context context, int resource, List<HistoryItem> items) {
            super(context, resource, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout;
            HistoryItem item = (HistoryItem) getItem(position);
            if (convertView == null) {
                layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(C0268R.layout.gyroscope_display_item, parent, false);
            } else {
                layout = (LinearLayout) convertView;
            }
            String countString = item.getCount();
            String xVal = item.getValueX();
            String yVal = item.getValueY();
            String zVal = item.getValueZ();
            TextView idView = (TextView) layout.findViewById(C0268R.C0269id.measured_value_x);
            TextView itemView = (TextView) layout.findViewById(C0268R.C0269id.measured_value_y);
            TextView resultView = (TextView) layout.findViewById(C0268R.C0269id.measured_value_z);
            ((TextView) layout.findViewById(C0268R.C0269id.measured_count)).setText(countString);
            idView.setText(xVal);
            itemView.setText(yVal);
            resultView.setText(zVal);
            idView.setTextColor(-65536);
            itemView.setTextColor(-16711936);
            resultView.setTextColor(-16776961);
            return layout;
        }
    }

    private static class HistoryItem {
        private String countvalue;
        private String xvalue;
        private String yvalue;
        private String zvalue;

        public HistoryItem(int count, float x, float y, float z) {
            this.countvalue = String.valueOf(count);
            this.xvalue = String.valueOf(x);
            this.yvalue = String.valueOf(y);
            this.zvalue = String.valueOf(z);
        }

        public String getCount() {
            return this.countvalue;
        }

        public String getValueX() {
            return this.xvalue;
        }

        public String getValueY() {
            return this.yvalue;
        }

        public String getValueZ() {
            return this.zvalue;
        }
    }

    private class SensorTestListener implements SensorEventListener {
        private SensorTestListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type != 4) {
                if (type == 65689 && GyroscopeDisplay.this.mIsSubGyro) {
                    GyroscopeDisplay.this.getValueFromSensor(event.values[0] * 57.32484f, event.values[1] * 57.32484f, event.values[2] * 57.32484f);
                }
            } else if (!GyroscopeDisplay.this.mIsSubGyro) {
                GyroscopeDisplay.this.getValueFromSensor(event.values[0] * 57.32484f, event.values[1] * 57.32484f, event.values[2] * 57.32484f);
            }
        }
    }

    static /* synthetic */ int access$304(GyroscopeDisplay x0) {
        int i = x0.mCount + 1;
        x0.mCount = i;
        return i;
    }

    public GyroscopeDisplay() {
        super("GyroscopeDisplay");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_display);
        this.mIsSubGyro = getIntent().getBooleanExtra("sub_gyro", false);
        StringBuilder sb = new StringBuilder();
        sb.append(" mIsSubGyro : ");
        sb.append(this.mIsSubGyro);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        initTextView();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSensorListener = new SensorTestListener();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSubGyroSensor = this.mSensorManager.getDefaultSensor(65689);
        startGyroDisplay();
    }

    public void onDestroy() {
        super.onDestroy();
        if (!TestCase.getEnabled(TestCase.SUPPORT_GYRO_SET_DPS)) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    /* access modifiers changed from: private */
    public void startGyroDisplay() {
        if (this.mTimer == null) {
            this.mValueList.clear();
        }
        this.mSensorManager.registerListener(this.mSensorListener, this.mGyroSensor, 2);
        if (this.mSubGyroSensor != null) {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSubGyroSensor, 2);
        }
        displayValueFromSensor();
    }

    /* access modifiers changed from: private */
    public void writeDpsValue(String dpsValue) {
        Kernel.write(Kernel.GYRO_SENSOR_DPS_SELECT, dpsValue);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        writeDpsValue("0");
        super.onPause();
    }

    private void displayDialog() {
        AlertDialogFragment.newInstance(C0268R.string.gyro_display).show(getFragmentManager(), "dialog");
    }

    /* access modifiers changed from: private */
    public void getValueFromSensor(float x, float y, float z) {
        this.f9mX = x;
        this.f10mY = y;
        this.f11mZ = z;
        StringBuilder sb = new StringBuilder();
        sb.append("x=");
        sb.append(this.f9mX);
        sb.append(", y=");
        sb.append(this.f10mY);
        sb.append(", z=");
        sb.append(this.f11mZ);
        LtUtil.log_d(this.CLASS_NAME, "getValueFromSensor", sb.toString());
    }

    private void displayValueFromSensor() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    if (GyroscopeDisplay.this.mCount < 60) {
                        GyroscopeDisplay.this.mHandler.post(new Runnable() {
                            public void run() {
                                GyroscopeDisplay.this.mValueList.add(new HistoryItem(GyroscopeDisplay.access$304(GyroscopeDisplay.this), GyroscopeDisplay.this.f9mX, GyroscopeDisplay.this.f10mY, GyroscopeDisplay.this.f11mZ));
                                GyroscopeDisplay.this.mAdaptor.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }, 0, 2000);
        }
    }

    private void initTextView() {
        this.mAdaptor = new HistoryAdaptor(this, C0268R.layout.gyroscope_display_item, this.mValueList);
        this.mListView = (ListView) findViewById(C0268R.C0269id.hist_nv_list);
        this.mListView.setAdapter(this.mAdaptor);
        this.mGyroDisplayTitleText = (TextView) findViewById(C0268R.C0269id.tv_sub_gyro_disp_title);
        if (this.mIsSubGyro) {
            this.mGyroDisplayTitleText.setText("SUB GYROSCOPE TEST");
        }
    }
}
