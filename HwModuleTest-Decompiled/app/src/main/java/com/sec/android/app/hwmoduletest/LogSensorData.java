package com.sec.android.app.hwmoduletest;

import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Formatter;

public class LogSensorData {
    private static final String TAG = "LogSensorData";
    String mFileName;
    String mFilePreFix;
    private FileOutputStream mFos;
    private boolean mIsStarting;
    long[] mSystemTime;
    private String mWriteLine;

    public LogSensorData() {
        this.mSystemTime = new long[2];
        this.mFos = null;
        this.mWriteLine = null;
        this.mIsStarting = false;
        this.mFileName = null;
        this.mFilePreFix = null;
        this.mFilePreFix = "AirMotion";
        createLogFile();
    }

    public LogSensorData(String str) {
        this.mSystemTime = new long[2];
        this.mFos = null;
        this.mWriteLine = null;
        this.mIsStarting = false;
        this.mFileName = null;
        this.mFilePreFix = null;
        this.mFilePreFix = str;
        createLogFile();
    }

    private void createLogFile() {
        this.mIsStarting = false;
        Calendar c = Calendar.getInstance();
        Formatter form = new Formatter();
        form.format("-%04d%02d%02d-%02d%02d%02d", new Object[]{Integer.valueOf(c.get(1)), Integer.valueOf(c.get(2) + 1), Integer.valueOf(c.get(5)), Integer.valueOf(c.get(11)), Integer.valueOf(c.get(12)), Integer.valueOf(c.get(13))});
        StringBuffer sbfileName = new StringBuffer();
        sbfileName.append("/storage/sdcard0/");
        sbfileName.append(getFilePrefix());
        sbfileName.append(form.toString());
        sbfileName.append(".txt");
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sbfileName ");
        sb.append(sbfileName);
        Log.d(str, sb.toString());
        File fp = new File(sbfileName.toString());
        if (!fp.exists()) {
            try {
                fp.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            this.mFos = new FileOutputStream(fp, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.mFileName = sbfileName.toString();
    }

    /* access modifiers changed from: 0000 */
    public String getFileName() {
        return this.mFileName;
    }

    /* access modifiers changed from: 0000 */
    public void writeBodyTempSensorData(int count, String[] value) {
        String strData;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Count"));
            sb.append(CalculateSpace("Body temp."));
            sb.append(CalculateSpace("To"));
            sb.append(CalculateSpace("Ta"));
            sb.append(CalculateSpace("Raw To"));
            sb.append("Raw Ta\n");
            strData = sb.toString();
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strData);
        sb2.append(CalculateSpace(String.valueOf(count)));
        sb2.append(CalculateSpace(value[3]));
        sb2.append(CalculateSpace(value[4]));
        sb2.append(CalculateSpace(value[2]));
        sb2.append(CalculateSpace(value[1]));
        sb2.append(CalculateSpace(value[0]));
        String strData2 = sb2.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(String value1, int value2, int value3) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append(CalculateSpace("Direction"));
            sb.append(CalculateSpace("Scale"));
            sb.append("Angle\n");
            strData = sb.toString();
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strData);
        sb2.append(CalculateSpace(form.toString()));
        sb2.append(CalculateSpace(String.valueOf(intervalTime)));
        sb2.append(CalculateSpace(String.valueOf(value1)));
        sb2.append(CalculateSpace(String.valueOf(value2)));
        sb2.append(CalculateSpace(String.valueOf(value3)));
        String strData2 = sb2.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(String value1, int value2) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append(CalculateSpace("Direction"));
            sb.append("Angle\n");
            strData = sb.toString();
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strData);
        sb2.append(CalculateSpace(form.toString()));
        sb2.append(CalculateSpace(String.valueOf(intervalTime)));
        sb2.append(CalculateSpace(String.valueOf(value1)));
        sb2.append(CalculateSpace(String.valueOf(value2)));
        String strData2 = sb2.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(float value1, float value2, float value3, int value4) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append(CalculateSpace("Zdelta"));
            sb.append(CalculateSpace("Peak to Peak"));
            sb.append(CalculateSpace("Valid cnt"));
            sb.append("Angle\n");
            strData = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mIsStarting");
            sb2.append(this.mIsStarting);
            sb2.append("strData");
            sb2.append(strData);
            Log.i("gesture sensor test mode 2", sb2.toString());
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb3 = new StringBuilder();
        sb3.append(strData);
        sb3.append(CalculateSpace(form.toString()));
        sb3.append(CalculateSpace(String.valueOf(intervalTime)));
        sb3.append(CalculateSpace(String.valueOf(value1)));
        sb3.append(CalculateSpace(String.valueOf(value2)));
        sb3.append(CalculateSpace(String.valueOf(value3)));
        sb3.append(CalculateSpace(String.valueOf(value4)));
        String strData2 = sb3.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(String value1, int value2, int value3, int value4, int value5, int value6, int value7, int value8, String gesture_count) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append(CalculateSpace("Direction"));
            sb.append(CalculateSpace("Cross_Angle"));
            sb.append(CalculateSpace("Count"));
            sb.append(CalculateSpace("Max.sum_NSWE"));
            sb.append(CalculateSpace("Enter_Angle"));
            sb.append(CalculateSpace("Exit_Angle"));
            sb.append(CalculateSpace("Enter_Mag"));
            sb.append(CalculateSpace("Exit_Mag"));
            sb.append("Gesture Count\n");
            strData = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mIsStarting");
            sb2.append(this.mIsStarting);
            sb2.append("strData");
            sb2.append(strData);
            Log.i("gesture sensor test mode 2", sb2.toString());
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb3 = new StringBuilder();
        sb3.append(strData);
        sb3.append(CalculateSpace(form.toString()));
        sb3.append(CalculateSpace(String.valueOf(intervalTime)));
        sb3.append(CalculateSpace(String.valueOf(value1)));
        sb3.append(CalculateSpace(String.valueOf(value2)));
        sb3.append(CalculateSpace(String.valueOf(value3)));
        sb3.append(CalculateSpace(String.valueOf(value4)));
        sb3.append(CalculateSpace(String.valueOf(value5)));
        sb3.append(CalculateSpace(String.valueOf(value6)));
        sb3.append(CalculateSpace(String.valueOf(value7)));
        sb3.append(CalculateSpace(String.valueOf(value8)));
        sb3.append(gesture_count);
        String strData2 = sb3.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public String CalculateSpace(String value) {
        int spaceCount = 20 - value.length();
        String result = value;
        for (int i = 0; i < spaceCount; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            sb.append(" ");
            result = sb.toString();
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(float value1) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append("Zmean\n");
            strData = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mIsStarting");
            sb2.append(this.mIsStarting);
            sb2.append("strData");
            sb2.append(strData);
            Log.i("gesture sensor test mode 1", sb2.toString());
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb3 = new StringBuilder();
        sb3.append(strData);
        sb3.append(CalculateSpace(form.toString()));
        sb3.append(CalculateSpace(String.valueOf(intervalTime)));
        sb3.append(CalculateSpace(String.valueOf(value1)));
        String strData2 = sb3.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeSensorData(float value1, float value2, float value3, float value4) {
        String strData;
        long intervalTime;
        new String();
        if (!this.mIsStarting) {
            StringBuilder sb = new StringBuilder();
            sb.append(CalculateSpace("Time"));
            sb.append(CalculateSpace("Interval"));
            sb.append(CalculateSpace("Ch A"));
            sb.append(CalculateSpace("Ch B"));
            sb.append(CalculateSpace("Ch C"));
            sb.append("Ch D\n");
            strData = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mIsStarting");
            sb2.append(this.mIsStarting);
            sb2.append("strData");
            sb2.append(strData);
            Log.i("gesture sensor count delta", sb2.toString());
            this.mIsStarting = true;
        } else {
            strData = "";
        }
        Time time = new Time();
        long curTime = System.currentTimeMillis();
        time.set(curTime);
        Formatter form = new Formatter();
        form.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(time.hour), Integer.valueOf(time.minute), Integer.valueOf(time.second)});
        this.mSystemTime[1] = curTime;
        if (this.mSystemTime[0] == 0) {
            intervalTime = 0;
        } else {
            intervalTime = this.mSystemTime[1] - this.mSystemTime[0];
        }
        this.mSystemTime[0] = this.mSystemTime[1];
        StringBuilder sb3 = new StringBuilder();
        sb3.append(strData);
        sb3.append(CalculateSpace(form.toString()));
        sb3.append(CalculateSpace(String.valueOf(intervalTime)));
        sb3.append(CalculateSpace(String.valueOf(value1)));
        sb3.append(CalculateSpace(String.valueOf(value2)));
        sb3.append(CalculateSpace(String.valueOf(value3)));
        sb3.append(CalculateSpace(String.valueOf(value4)));
        String strData2 = sb3.toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(strData2);
        stringBuffer.append("\n");
        this.mWriteLine = stringBuffer.toString();
        byte[] theByteArray = this.mWriteLine.getBytes();
        try {
            if (this.mFos != null) {
                this.mFos.write(theByteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void stop() {
        try {
            if (this.mFos != null) {
                this.mFos.close();
                this.mFos = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "file close error");
        }
        this.mWriteLine = null;
    }

    private String getFilePrefix() {
        return this.mFilePreFix;
    }
}
