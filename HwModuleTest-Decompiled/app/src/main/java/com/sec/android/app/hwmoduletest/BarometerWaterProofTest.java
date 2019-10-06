package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.Circle;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.SensorTestMenu;
import com.sec.xmldata.support.XMLDataStorage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BarometerWaterProofTest extends BaseActivity implements OnClickListener {
    public static final String CLASS_NAME = "BarometerWaterProofTest";
    private static final int FINISH_ONE_CYCLE = 2;
    private static final int GET_INIT_REF = 3;
    private static final int GET_READ = 0;
    private static final String L_LEAK_TEXT = "L-Leak : ";
    private static final String S_LEAK_TEXT = "S-Leak : ";
    private static final int UPDATE_CURRENT = 1;
    /* access modifiers changed from: private */
    public WaterProofTestAdapter mAdaptor;
    /* access modifiers changed from: private */
    public float[] mBaromSensorValues = new float[2];
    /* access modifiers changed from: private */
    public CalculatePressure mCalculatePressure;
    /* access modifiers changed from: private */
    public Button mControlButton;
    /* access modifiers changed from: private */
    public int mCount;
    /* access modifiers changed from: private */
    public TextView mCurrentSensorValue;
    /* access modifiers changed from: private */
    public TestStage mCurrentStage = TestStage.INIT_STAGE;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    float value = BarometerWaterProofTest.this.mBaromSensorValues[0];
                    int access$1100 = BarometerWaterProofTest.this.mCount + 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Read");
                    sb.append(String.valueOf(BarometerWaterProofTest.this.mCount));
                    HistoryItem item = new HistoryItem(access$1100, sb.toString(), value, String.valueOf(System.currentTimeMillis()));
                    BarometerWaterProofTest.this.mValueList.add(item);
                    if (BarometerWaterProofTest.this.mSaveData != null) {
                        BarometerWaterProofTest.this.mSaveData.write(item.getAllData());
                    }
                    if (BarometerWaterProofTest.this.mSavePreference != null) {
                        BarometerWaterProofTest.this.mSavePreference.write(BarometerWaterProofTest.this.mCount + 2, value);
                    }
                    BarometerWaterProofTest.this.mAdaptor.notifyDataSetChanged();
                    int access$11002 = BarometerWaterProofTest.this.mCount;
                    if (access$11002 == 1) {
                        BarometerWaterProofTest.this.mCalculatePressure.setValue(value, 1);
                        if (!BarometerWaterProofTest.this.mCalculatePressure.checkInitialLLeak()) {
                            BarometerWaterProofTest.this.mLLeak.append("-");
                            BarometerWaterProofTest.this.mLLeak.setTextColor(-65536);
                            if (!FactoryTest.isFactoryBinary()) {
                                BarometerWaterProofTest.this.runFinishStage((20 - BarometerWaterProofTest.this.mCount) * 1000);
                            } else {
                                BarometerWaterProofTest.this.runFinishStage(0);
                            }
                        }
                    } else if (access$11002 != 20) {
                        switch (access$11002) {
                            case 10:
                                BarometerWaterProofTest.this.mCalculatePressure.setValue(value, 2);
                                break;
                            case 11:
                                BarometerWaterProofTest.this.mCalculatePressure.setValue(value, 3);
                                break;
                        }
                    } else {
                        BarometerWaterProofTest.this.mCalculatePressure.setValue(value, 4);
                        BarometerWaterProofTest.this.runFinishStage(0);
                        BarometerWaterProofTest.this.setLeakageResult();
                    }
                    BarometerWaterProofTest.this.mCount = BarometerWaterProofTest.this.mCount + 1;
                    return;
                case 1:
                    BarometerWaterProofTest.this.mCurrentSensorValue.setText(String.valueOf(BarometerWaterProofTest.this.mBaromSensorValues[0]));
                    return;
                case 2:
                    BarometerWaterProofTest.this.mControlButton.setText(C0268R.string.finish_button);
                    return;
                case 3:
                    switch (msg.arg1) {
                        case 0:
                            BarometerWaterProofTest.this.mRefValues = new float[]{-1.0f, -1.0f, -1.0f};
                            BarometerWaterProofTest.this.mRefValues[msg.arg1] = BarometerWaterProofTest.this.mBaromSensorValues[0];
                            return;
                        case 1:
                            BarometerWaterProofTest.this.mRefValues[msg.arg1] = BarometerWaterProofTest.this.mBaromSensorValues[0];
                            return;
                        case 2:
                            BarometerWaterProofTest.this.mRefValues[msg.arg1] = BarometerWaterProofTest.this.mBaromSensorValues[0];
                            if (BarometerWaterProofTest.this.mRefValues[0] <= 0.0f || BarometerWaterProofTest.this.mRefValues[1] <= 0.0f || BarometerWaterProofTest.this.mRefValues[2] <= 0.0f) {
                                LtUtil.log_e(BarometerWaterProofTest.CLASS_NAME, "Initial Stage Errors", "");
                                return;
                            }
                            float initValue = ((BarometerWaterProofTest.this.mRefValues[0] + BarometerWaterProofTest.this.mRefValues[1]) + BarometerWaterProofTest.this.mRefValues[2]) / 3.0f;
                            String firstTimeStamp = String.valueOf(System.currentTimeMillis());
                            HistoryItem refItem = new HistoryItem(0, "Ref", initValue, firstTimeStamp);
                            BarometerWaterProofTest.this.mValueList.add(refItem);
                            if (BarometerWaterProofTest.this.mSaveData != null) {
                                BarometerWaterProofTest.this.mSaveData.close();
                                BarometerWaterProofTest.this.mSaveData = null;
                            }
                            BarometerWaterProofTest.this.mSaveData = new SaveData(firstTimeStamp);
                            if (BarometerWaterProofTest.this.mSaveData.open()) {
                                BarometerWaterProofTest.this.mSaveData.write(refItem.getAllData());
                            }
                            if (BarometerWaterProofTest.this.mSavePreference != null) {
                                BarometerWaterProofTest.this.mSavePreference = null;
                            }
                            BarometerWaterProofTest.this.mSavePreference = new SavePreference();
                            BarometerWaterProofTest.this.mSavePreference.write(0, initValue);
                            if (BarometerWaterProofTest.this.mCalculatePressure != null) {
                                BarometerWaterProofTest.this.mCalculatePressure = null;
                            }
                            BarometerWaterProofTest.this.mCalculatePressure = new CalculatePressure();
                            BarometerWaterProofTest.this.mCalculatePressure.setValue(initValue, 0);
                            BarometerWaterProofTest.this.mCurrentStage = TestStage.RELASE_STAGE;
                            BarometerWaterProofTest.this.mAdaptor.notifyDataSetChanged();
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("CurrentStage = ");
                            sb2.append(BarometerWaterProofTest.this.mCurrentStage.toString());
                            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "GET_INIT_REF", sb2.toString());
                            BarometerWaterProofTest.this.mControlButton.setText(BarometerWaterProofTest.this.mCurrentStage.getStageNameID());
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView mLLeak;
    private ListView mListView;
    private Sensor mPressureSensor;
    /* access modifiers changed from: private */
    public float[] mRefValues;
    private TextView mSLeak;
    /* access modifiers changed from: private */
    public SaveData mSaveData;
    /* access modifiers changed from: private */
    public SavePreference mSavePreference;
    private SensorTestListener mSensorListener;
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public List<HistoryItem> mValueList = new ArrayList();
    private boolean supportBarometer = true;

    private static class CalculatePressure {
        public static final float L_LEAK_INITIAL_CHECK_SPEC = 3.0f;
        public static final float L_LEAK_SPEC = 10.0f;
        public static final float S_LEAK_SPEC = 10.0f;
        private float[] mPressureValues;

        private CalculatePressure() {
            this.mPressureValues = new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
        }

        public void setValue(float f, int index) {
            this.mPressureValues[index] = f;
            StringBuilder sb = new StringBuilder();
            sb.append("index=");
            sb.append(index);
            sb.append(", value=");
            sb.append(f);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CalculatePressure:setValue", sb.toString());
        }

        public float getLLeak() {
            float result = -1.0f;
            if (this.mPressureValues[0] > 1.0f && this.mPressureValues[1] > 1.0f && this.mPressureValues[2] > 1.0f) {
                float v1 = this.mPressureValues[2] - this.mPressureValues[1];
                float v2 = this.mPressureValues[0] - this.mPressureValues[1];
                float v3 = v1 / v2;
                result = v3 * 100.0f;
                StringBuilder sb = new StringBuilder();
                sb.append("v1=");
                sb.append(String.valueOf(v1));
                sb.append(", v2=");
                sb.append(String.valueOf(v2));
                sb.append(", v3=");
                sb.append(String.valueOf(v3));
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CalculatePressure:getLLeak", sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("value=");
            sb2.append(result);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CalculatePressure:getLLeak", sb2.toString());
            return result;
        }

        public float getSLeak() {
            float result = -1.0f;
            if (this.mPressureValues[0] > 1.0f && this.mPressureValues[1] > 1.0f && this.mPressureValues[3] > 1.0f && this.mPressureValues[4] > 1.0f) {
                float v1 = this.mPressureValues[4] - this.mPressureValues[3];
                float v2 = this.mPressureValues[0] - this.mPressureValues[1];
                float v3 = v1 / v2;
                result = v3 * 100.0f;
                StringBuilder sb = new StringBuilder();
                sb.append("v1=");
                sb.append(String.valueOf(v1));
                sb.append(", v2=");
                sb.append(String.valueOf(v2));
                sb.append(", v3=");
                sb.append(String.valueOf(v3));
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CalculatePressure:getSLeak", sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("value=");
            sb2.append(result);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CalculatePressure:getSLeak", sb2.toString());
            return result;
        }

        public boolean checkInitialLLeak() {
            if (this.mPressureValues[0] <= 1.0f || this.mPressureValues[1] <= 1.0f || this.mPressureValues[0] - this.mPressureValues[1] <= 3.0f) {
                return false;
            }
            return true;
        }
    }

    private static class HistoryItem {
        private float mRawValue;
        private String mTag;
        private String mTime;
        private String mTimeStamp;
        private String mValue;

        public HistoryItem(int time, String tag, float value, String timeStamp) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(time));
            sb.append("sec");
            this.mTime = sb.toString();
            this.mTag = tag;
            this.mValue = String.valueOf(value);
            this.mRawValue = value;
            this.mTimeStamp = timeStamp;
        }

        public float getRawValue() {
            return this.mRawValue;
        }

        public String getTime() {
            return this.mTime;
        }

        public String getTag() {
            return this.mTag;
        }

        public String getValue() {
            return this.mValue;
        }

        public String getTimeStamp() {
            return this.mTimeStamp;
        }

        public String getAllData() {
            StringBuilder sb = new StringBuilder();
            sb.append(getTime());
            sb.append(",");
            sb.append(getTag());
            sb.append(",");
            sb.append(getValue());
            sb.append(",");
            sb.append(getTimeStamp());
            sb.append("\n");
            return sb.toString();
        }
    }

    private static class SaveData {
        private static final String DefaultFileName = "BarometerWaterProof";
        private static final String DefaultPath = "/data/log/";
        private static final String POST_FIX = ".txt";
        private FileOutputStream mFileOutPutStream;
        private String mFilePath;

        SaveData(String fileName) {
            if (!FactoryTest.isFactoryBinary()) {
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SaveData", "In user BIN, Do not write logging in the file");
                return;
            }
            this.mFilePath = makeFileName(fileName);
            StringBuilder sb = new StringBuilder();
            sb.append("fileName = ");
            sb.append(this.mFilePath);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SaveData", sb.toString());
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
            if (r5.mFileOutPutStream == null) goto L_0x003b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0033, code lost:
            r0 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
            if (r5.mFileOutPutStream != null) goto L_0x0033;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean open() {
            /*
                r5 = this;
                boolean r0 = android.os.FactoryTest.isFactoryBinary()
                r1 = 1
                if (r0 != 0) goto L_0x0011
                java.lang.String r0 = "BarometerWaterProofTest"
                java.lang.String r2 = "SaveData"
                java.lang.String r3 = "In user BIN, Do not write logging in the file"
                com.sec.android.app.hwmoduletest.support.LtUtil.log_d(r0, r2, r3)
                return r1
            L_0x0011:
                r0 = 0
                java.lang.String r2 = r5.mFilePath
                if (r2 == 0) goto L_0x003b
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x002b }
                java.io.File r3 = new java.io.File     // Catch:{ FileNotFoundException -> 0x002b }
                java.lang.String r4 = r5.mFilePath     // Catch:{ FileNotFoundException -> 0x002b }
                r3.<init>(r4)     // Catch:{ FileNotFoundException -> 0x002b }
                r2.<init>(r3, r1)     // Catch:{ FileNotFoundException -> 0x002b }
                r5.mFileOutPutStream = r2     // Catch:{ FileNotFoundException -> 0x002b }
                java.io.FileOutputStream r1 = r5.mFileOutPutStream
                if (r1 == 0) goto L_0x003b
                goto L_0x0033
            L_0x0029:
                r1 = move-exception
                goto L_0x0035
            L_0x002b:
                r1 = move-exception
                com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r1)     // Catch:{ all -> 0x0029 }
                java.io.FileOutputStream r1 = r5.mFileOutPutStream
                if (r1 == 0) goto L_0x003b
            L_0x0033:
                r0 = 1
                goto L_0x003b
            L_0x0035:
                java.io.FileOutputStream r2 = r5.mFileOutPutStream
                if (r2 == 0) goto L_0x003a
                r0 = 1
            L_0x003a:
                throw r1
            L_0x003b:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.BarometerWaterProofTest.SaveData.open():boolean");
        }

        /* access modifiers changed from: protected */
        public boolean write(String data) {
            if (!FactoryTest.isFactoryBinary()) {
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SaveData", "In user BIN, Do not write logging in the file");
                return true;
            }
            boolean result = false;
            if (this.mFileOutPutStream != null) {
                try {
                    this.mFileOutPutStream.write(data.getBytes());
                    result = true;
                } catch (IOException e) {
                    LtUtil.log_e(e);
                }
            }
            return result;
        }

        /* access modifiers changed from: protected */
        public boolean close() {
            if (!FactoryTest.isFactoryBinary()) {
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SaveData", "In user BIN, Do not write logging in the file");
                return true;
            }
            boolean result = false;
            if (this.mFileOutPutStream != null) {
                try {
                    this.mFileOutPutStream.flush();
                    this.mFileOutPutStream.close();
                    result = true;
                } catch (IOException e) {
                    LtUtil.log_e(e);
                }
            }
            return result;
        }

        private String makeFileName(String filename) {
            File dir = new File(DefaultPath);
            if (!dir.isDirectory()) {
                dir.mkdir();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("/data/log/BarometerWaterProof");
            sb.append(filename);
            sb.append(POST_FIX);
            File file = new File(sb.toString());
            int count = 0;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("/data/log/BarometerWaterProof");
            sb2.append(filename);
            sb2.append(POST_FIX);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", sb2.toString());
            if (dir.exists()) {
                String[] arr = dir.list();
                String str = BarometerWaterProofTest.CLASS_NAME;
                String str2 = "CreateFile";
                StringBuilder sb3 = new StringBuilder();
                sb3.append("arr : ");
                sb3.append(arr != null ? Arrays.toString(arr) : "null");
                LtUtil.log_d(str, str2, sb3.toString());
                if (file.exists()) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("file : ");
                    sb4.append(file);
                    LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", sb4.toString());
                    if (arr != null) {
                        int count2 = 0;
                        for (String str3 : arr) {
                            if (str3.length() > 5) {
                                String substring = str3.substring(0, str3.length() - 4);
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(DefaultFileName);
                                sb5.append(filename);
                                if (substring.startsWith(sb5.toString())) {
                                    count2++;
                                }
                            }
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("count : ");
                            sb6.append(count2);
                            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", sb6.toString());
                        }
                        count = count2;
                    }
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("fileFullPath : /data/log/BarometerWaterProof");
                    sb7.append(filename);
                    sb7.append("_");
                    sb7.append(count);
                    sb7.append(POST_FIX);
                    LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", sb7.toString());
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("/data/log/BarometerWaterProof");
                    sb8.append(filename);
                    sb8.append("_");
                    sb8.append(count);
                    sb8.append(POST_FIX);
                    return sb8.toString();
                }
                StringBuilder sb9 = new StringBuilder();
                sb9.append("fileFullPath : /data/log/BarometerWaterProof");
                sb9.append(filename);
                sb9.append(POST_FIX);
                LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", sb9.toString());
                StringBuilder sb10 = new StringBuilder();
                sb10.append("/data/log/BarometerWaterProof");
                sb10.append(filename);
                sb10.append(POST_FIX);
                return sb10.toString();
            }
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "CreateFile", "FAILED");
            return null;
        }
    }

    private class SavePreference {
        private static final String PRIVATE_FILE = "barometerData";
        private float[] mData;
        private DecimalFormat mFormat;

        private SavePreference() {
            this.mData = new float[]{-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
            this.mFormat = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        }

        /* access modifiers changed from: protected */
        public boolean write(int index, float value) {
            float[] fArr;
            StringBuilder sb = new StringBuilder();
            sb.append("index[");
            sb.append(index);
            sb.append("]=");
            sb.append(value);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SavePreference:write", sb.toString());
            this.mFormat.applyPattern("0.00");
            if (index <= -1 || index >= 23) {
                return false;
            }
            this.mData[index] = value;
            String stringData = "";
            for (float f : this.mData) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(stringData);
                sb2.append(this.mFormat.format((double) f));
                sb2.append(",");
                stringData = sb2.toString();
            }
            return saveToPrivateFiles(stringData.substring(0, stringData.length() - 1));
        }

        private boolean saveToPrivateFiles(String data) {
            File file = BarometerWaterProofTest.this.getFileStreamPath(PRIVATE_FILE);
            if (file == null || data == null) {
                return false;
            }
            String path = file.getPath();
            StringBuilder sb = new StringBuilder();
            sb.append("path = ");
            sb.append(path);
            LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "SavePreference:saveToPrivateFiles", sb.toString());
            return Kernel.writeToPathNsync(path, data.getBytes());
        }
    }

    private class SensorTestListener implements SensorEventListener {
        private SensorTestListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 6) {
                if (BarometerWaterProofTest.this.mCurrentStage == TestStage.RELASE_STAGE) {
                    if (!BarometerWaterProofTest.this.mCurrentStage.mTouchedTHD) {
                        BarometerWaterProofTest.this.mCurrentStage.mTouchedTHD = ((HistoryItem) BarometerWaterProofTest.this.mValueList.get(0)).getRawValue() - event.values[0] > 3.0f;
                    }
                    if (BarometerWaterProofTest.this.mCurrentStage.mTouchedTHD && BarometerWaterProofTest.this.mBaromSensorValues[0] < event.values[0]) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Ref = ");
                        sb.append(((HistoryItem) BarometerWaterProofTest.this.mValueList.get(0)).getRawValue());
                        sb.append("Curr = ");
                        sb.append(event.values[0]);
                        sb.append(", Past = ");
                        sb.append(BarometerWaterProofTest.this.mBaromSensorValues[0]);
                        LtUtil.log_d(BarometerWaterProofTest.CLASS_NAME, "onSensorChanged", sb.toString());
                        BarometerWaterProofTest.this.runMeasureStage();
                        TestStage.RELASE_STAGE.mTouchedTHD = false;
                    }
                }
                BarometerWaterProofTest.this.mBaromSensorValues = (float[]) event.values.clone();
                BarometerWaterProofTest.this.mHandler.sendEmptyMessage(1);
            }
        }
    }

    private enum TestStage {
        ;
        
        private int mButtonStringID;
        private String mStageName;
        public boolean mTouchedTHD;

        static {
            INIT_STAGE = new TestStage("INIT_STAGE", 0, "INIT_STAGE", FactoryTest.isFactoryBinary() ? C0268R.string.reference_button : C0268R.string.user_reference_button);
            RELASE_STAGE = new TestStage("RELASE_STAGE", 1, "RELASE_STAGE", FactoryTest.isFactoryBinary() ? C0268R.string.release_button : C0268R.string.user_release_button);
            MEASURING_STAGE = new TestStage("MEASURING_STAGE", 2, "MEASURING_STAGE", FactoryTest.isFactoryBinary() ? C0268R.string.measuring : C0268R.string.user_measuring);
            $VALUES = new TestStage[]{INIT_STAGE, RELASE_STAGE, MEASURING_STAGE};
        }

        private TestStage(String id, int stringID) {
            this.mTouchedTHD = false;
            this.mStageName = id;
            this.mButtonStringID = stringID;
        }

        /* access modifiers changed from: 0000 */
        public int getStageNameID() {
            return this.mButtonStringID;
        }

        public String toString() {
            return this.mStageName;
        }
    }

    private static class WaterProofTestAdapter extends ArrayAdapter<HistoryItem> {
        public WaterProofTestAdapter(Context context, int resource, List<HistoryItem> items) {
            super(context, resource, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TableLayout layout;
            HistoryItem item = (HistoryItem) getItem(position);
            if (convertView == null) {
                layout = (TableLayout) LayoutInflater.from(getContext()).inflate(C0268R.layout.barometer_waterproof_test_list_item, parent, false);
            } else {
                layout = (TableLayout) convertView;
            }
            TextView tagView = (TextView) layout.findViewById(C0268R.C0269id.measured_tag);
            TextView pressureView = (TextView) layout.findViewById(C0268R.C0269id.measured_pressure);
            TextView timeStampView = (TextView) layout.findViewById(C0268R.C0269id.measured_time_stamp);
            ((TextView) layout.findViewById(C0268R.C0269id.measured_time)).setText(item.getTime());
            tagView.setText(item.getTag());
            pressureView.setText(item.getValue());
            timeStampView.setText(item.getTimeStamp());
            return layout;
        }
    }

    public void runFinishStage(int waitingTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("waitingTime = ");
        sb.append(waitingTime);
        LtUtil.log_d(CLASS_NAME, "runFinishStage", sb.toString());
        this.mCurrentStage = TestStage.INIT_STAGE;
        if (this.mSaveData != null) {
            this.mSaveData.close();
            this.mSaveData = null;
        }
        if (this.mSavePreference != null) {
            this.mSavePreference = null;
        }
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(2);
        if (waitingTime > 0) {
            this.mHandler.sendEmptyMessageDelayed(2, (long) waitingTime);
        } else {
            this.mControlButton.setText(C0268R.string.finish_button);
        }
    }

    public void setLeakageResult() {
        TextView textView = this.mLLeak;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mCalculatePressure.getLLeak());
        sb.append("%");
        textView.setText(sb.toString());
        this.mLLeak.setTextColor(getResources().getColor(C0268R.color.violet));
        TextView textView2 = this.mSLeak;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mCalculatePressure.getSLeak());
        sb2.append("%");
        textView2.setText(sb2.toString());
        this.mSLeak.setTextColor(getResources().getColor(C0268R.color.violet));
    }

    public BarometerWaterProofTest() {
        super(CLASS_NAME);
    }

    public void onCreate(Bundle savedInstanceState) {
        if (!XMLDataStorage.instance().wasCompletedParsing()) {
            XMLDataStorage.instance().parseXML(this);
        }
        super.onCreate(savedInstanceState);
        if ("false".equals(SensorTestMenu.getEnabled(SensorTestMenu.NAME_BAROMETER))) {
            this.supportBarometer = false;
        }
        if (this.supportBarometer) {
            setContentView(C0268R.layout.barometer_waterproof_test);
            initUI();
            initSensor();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.supportBarometer) {
            startPressureSensor();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.supportBarometer) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(2);
        if (this.mSaveData != null) {
            this.mSaveData.close();
            this.mSaveData = null;
        }
        if (this.mSavePreference != null) {
            this.mSavePreference = null;
        }
        super.onDestroy();
    }

    private void startPressureSensor() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mPressureSensor, 0);
    }

    private void runBarometerTest() {
        this.mCount = 0;
        for (int i = 1; i < 22; i++) {
            this.mHandler.sendEmptyMessageDelayed(0, (long) (1000 * i));
        }
    }

    private void initSensor() {
        this.mSensorListener = new SensorTestListener();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mPressureSensor = this.mSensorManager.getDefaultSensor(6);
    }

    private void initUI() {
        this.mAdaptor = new WaterProofTestAdapter(this, C0268R.layout.barometer_waterproof_test_list_item, this.mValueList);
        this.mListView = (ListView) findViewById(C0268R.C0269id.data_list);
        this.mListView.setAdapter(this.mAdaptor);
        this.mCurrentSensorValue = (TextView) findViewById(C0268R.C0269id.current_data);
        this.mControlButton = (Button) findViewById(C0268R.C0269id.control_button);
        this.mControlButton.setOnClickListener(this);
        this.mControlButton.setText(this.mCurrentStage.getStageNameID());
        ((Button) findViewById(C0268R.C0269id.reset_button)).setOnClickListener(this);
        this.mLLeak = (TextView) findViewById(C0268R.C0269id.l_leak);
        this.mSLeak = (TextView) findViewById(C0268R.C0269id.s_leak);
        if (!FactoryTest.isFactoryBinary()) {
            this.mCurrentSensorValue.setVisibility(8);
            ((TableLayout) findViewById(C0268R.C0269id.table_list_title)).setVisibility(8);
            ((TableLayout) findViewById(C0268R.C0269id.leakage_view)).setVisibility(8);
            this.mListView.setVisibility(8);
            int radius = (int) (NPixelForOneMM() * 25.0f);
            Point point = getWindowSize();
            Circle circle = (Circle) findViewById(C0268R.C0269id.circle);
            circle.setPoint(((float) point.x) / 2.0f, ((float) point.y) / 2.0f);
            circle.setRadius((float) radius);
            circle.setVisibility(0);
            circle.setColor(-1);
        }
    }

    private Point getWindowSize() {
        Display display = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        display.getRealSize(outpoint);
        StringBuilder sb = new StringBuilder();
        sb.append("SCREEN_WIDTH : ");
        sb.append(outpoint.x);
        sb.append(", SCREEN_HEIGHT : ");
        sb.append(outpoint.y);
        LtUtil.log_d(CLASS_NAME, "onCreate", sb.toString());
        return outpoint;
    }

    private float NPixelForOneMM() {
        float nPixelForOneMM = TypedValue.applyDimension(5, 1.0f, getResources().getDisplayMetrics());
        StringBuilder sb = new StringBuilder();
        sb.append("nPixelForOneMM = ");
        sb.append(nPixelForOneMM);
        LtUtil.log_d(CLASS_NAME, "NPixelForOneMM", sb.toString());
        return nPixelForOneMM;
    }

    public void onClick(View view) {
        int i = 0;
        switch (view.getId()) {
            case C0268R.C0269id.control_button /*2131296281*/:
                if (this.mCurrentStage == TestStage.INIT_STAGE) {
                    this.mControlButton.setEnabled(false);
                    this.mControlButton.setText("Getting Ref...");
                    while (true) {
                        int i2 = i;
                        if (i2 < 3) {
                            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, i2, -1), (long) (1000 * i2));
                            i = i2 + 1;
                        } else {
                            return;
                        }
                    }
                } else if (this.mCurrentStage == TestStage.RELASE_STAGE) {
                    runMeasureStage();
                    return;
                } else {
                    return;
                }
            case C0268R.C0269id.reset_button /*2131296282*/:
                this.mCurrentStage = TestStage.INIT_STAGE;
                this.mValueList.clear();
                this.mSLeak.setText("");
                this.mLLeak.setText("");
                if (this.mSaveData != null) {
                    this.mSaveData.close();
                    this.mSaveData = null;
                }
                this.mHandler.removeMessages(0);
                this.mHandler.removeMessages(3);
                this.mHandler.removeMessages(2);
                this.mControlButton.setEnabled(true);
                TestStage.RELASE_STAGE.mTouchedTHD = false;
                this.mAdaptor.notifyDataSetChanged();
                StringBuilder sb = new StringBuilder();
                sb.append("CurrentStage = ");
                sb.append(this.mCurrentStage.toString());
                LtUtil.log_d(CLASS_NAME, "onClick", sb.toString());
                this.mControlButton.setText(this.mCurrentStage.getStageNameID());
                return;
            default:
                LtUtil.log_d(CLASS_NAME, "onClick", "default");
                return;
        }
    }

    /* access modifiers changed from: private */
    public void runMeasureStage() {
        this.mControlButton.setEnabled(false);
        HistoryItem releaseItem = new HistoryItem(0, "Release", this.mBaromSensorValues[0], String.valueOf(System.currentTimeMillis()));
        this.mValueList.add(releaseItem);
        if (this.mSavePreference != null) {
            this.mSavePreference.write(1, this.mBaromSensorValues[0]);
        }
        if (this.mSaveData != null) {
            this.mSaveData.write(releaseItem.getAllData());
        }
        this.mCurrentStage = TestStage.MEASURING_STAGE;
        runBarometerTest();
        this.mAdaptor.notifyDataSetChanged();
        StringBuilder sb = new StringBuilder();
        sb.append("CurrentStage = ");
        sb.append(this.mCurrentStage.toString());
        LtUtil.log_d(CLASS_NAME, "runMeasureStage", sb.toString());
        this.mControlButton.setText(this.mCurrentStage.getStageNameID());
    }
}
