package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.MicrophoneData;
import com.samsung.sensorframework.sda.p036c.p037a.AudioProcessor;
import java.io.File;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.d.a.l */
public class MicrophoneSensor extends AbstractPullSensor {
    private static MicrophoneSensor JW;
    private static final String[] Jz;
    private static Object lock;
    private MediaRecorder JR;
    protected AudioRecord JS;
    protected MicrophoneSensor JT;
    private ArrayList<Double> JU;
    private ArrayList<Long> JV;
    private MicrophoneData JX;
    private boolean JY;
    private String fileName;

    /* renamed from: com.samsung.sensorframework.sda.d.a.l.1 */
    class MicrophoneSensor extends Thread {
        final /* synthetic */ MicrophoneSensor JZ;

        MicrophoneSensor(MicrophoneSensor microphoneSensor) {
            this.JZ = microphoneSensor;
        }

        public void run() {
            this.JZ.JR.getMaxAmplitude();
            while (this.JZ.hf()) {
                synchronized (this.JZ.JR) {
                    if (this.JZ.JY) {
                        this.JZ.JU.add(Double.valueOf((double) this.JZ.JR.getMaxAmplitude()));
                        this.JZ.JV.add(Long.valueOf(System.currentTimeMillis()));
                    }
                }
                MicrophoneSensor.sleep(30);
            }
        }
    }

    /* renamed from: com.samsung.sensorframework.sda.d.a.l.a */
    protected class MicrophoneSensor extends Thread {
        final /* synthetic */ MicrophoneSensor JZ;
        int Ka;
        boolean Kb;

        public MicrophoneSensor(MicrophoneSensor microphoneSensor, int i) {
            this.JZ = microphoneSensor;
            this.Ka = i;
        }

        public void run() {
            try {
                this.Kb = true;
                long currentTimeMillis = 1000 * System.currentTimeMillis();
                long j = 1;
                while (this.JZ.JS.getRecordingState() == 3) {
                    short[] sArr = new short[this.Ka];
                    int read = this.JZ.JS.read(sArr, 0, this.Ka);
                    for (int i = 0; i < read; i++) {
                        this.JZ.JU.add(Double.valueOf(((double) sArr[i]) / 32767.0d));
                        this.JZ.JV.add(Long.valueOf(((long) (((double) j) * 125.0d)) + currentTimeMillis));
                        j++;
                    }
                }
                this.Kb = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void hA() {
            while (this.Kb) {
                MicrophoneSensor.sleep(10);
            }
        }
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.RECORD_AUDIO"};
    }

    public static MicrophoneSensor aW(Context context) {
        if (JW == null) {
            synchronized (lock) {
                if (JW == null) {
                    JW = new MicrophoneSensor(context);
                }
            }
        }
        return JW;
    }

    private MicrophoneSensor(Context context) {
        super(context);
        this.JY = false;
    }

    public void gY() {
        super.gY();
        JW = null;
    }

    protected String[] hb() {
        return Jz;
    }

    protected String he() {
        return "MicrophoneSensor";
    }

    private boolean hv() {
        try {
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String str = (String) this.Id.getParameter("AUDIO_FILES_DIRECTORY");
            if (str == null) {
                str = "s-label/raw_audio_files";
            }
            str = absolutePath + "/" + str;
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            this.fileName = str + "/" + "Audio_" + this.Jn + ".3gpp";
            this.JR = new MediaRecorder();
            this.JR.setAudioSource(1);
            this.JR.setOutputFormat(1);
            this.JR.setAudioEncoder(1);
            this.JR.setAudioSamplingRate(8000);
            this.JR.setOutputFile(this.fileName);
            this.JR.prepare();
            this.JR.start();
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    protected boolean hw() {
        this.JY = hv();
        if (!this.JY) {
            return false;
        }
        try {
            new MicrophoneSensor(this).start();
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    protected void hx() {
        synchronized (this.JR) {
            try {
                this.JR.stop();
                this.JR.release();
                this.JY = false;
                if (!((Boolean) this.Id.getParameter("SAVE_RAW_AUDIO_FILES")).booleanValue()) {
                    new File(this.fileName).delete();
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    protected boolean hy() {
        int minBufferSize = AudioRecord.getMinBufferSize(8000, 16, 2) * 2;
        this.JS = new AudioRecord(1, 8000, 16, 2, minBufferSize);
        this.JS.startRecording();
        this.JT = new MicrophoneSensor(this, minBufferSize);
        this.JT.start();
        return true;
    }

    protected void hz() {
        this.JS.stop();
        this.JT.hA();
        this.JS.release();
    }

    protected boolean hc() {
        this.JU = new ArrayList();
        this.JV = new ArrayList();
        if (((Boolean) this.Id.getParameter("ENABLE_AUDIO_STREAMING")).booleanValue()) {
            return hy();
        }
        return hw();
    }

    private static void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hd() {
        if (((Boolean) this.Id.getParameter("ENABLE_AUDIO_STREAMING")).booleanValue()) {
            hz();
        } else {
            hx();
        }
    }

    public int getSensorType() {
        return 5005;
    }

    protected SensorData hl() {
        return this.JX;
    }

    protected void hm() {
        double[] dArr = new double[this.JU.size()];
        long[] jArr = new long[this.JV.size()];
        int i = 0;
        while (i < this.JU.size() && i < this.JV.size()) {
            dArr[i] = ((Double) this.JU.get(i)).doubleValue();
            jArr[i] = ((Long) this.JV.get(i)).longValue();
            i++;
        }
        this.JX = ((AudioProcessor) hi()).m1533a(this.Jn, dArr, jArr, this.fileName, this.Id.gS());
    }
}
