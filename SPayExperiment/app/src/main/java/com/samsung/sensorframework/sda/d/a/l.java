/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioRecord
 *  android.media.MediaRecorder
 *  android.os.Environment
 *  java.io.File
 *  java.lang.Boolean
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.q;
import com.samsung.sensorframework.sda.d.a.b;
import java.io.File;
import java.util.ArrayList;

public class l
extends b {
    private static l JW;
    private static final String[] Jz;
    private static Object lock;
    private MediaRecorder JR;
    protected AudioRecord JS;
    protected a JT;
    private ArrayList<Double> JU;
    private ArrayList<Long> JV;
    private q JX;
    private boolean JY = false;
    private String fileName;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.RECORD_AUDIO"};
    }

    private l(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static l aW(Context context) {
        if (JW == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JW == null) {
                    JW = new l(context);
                }
            }
        }
        return JW;
    }

    private boolean hv() {
        String string;
        String string2;
        block4 : {
            try {
                string = Environment.getExternalStorageDirectory().getAbsolutePath();
                string2 = (String)this.Id.getParameter("AUDIO_FILES_DIRECTORY");
                if (string2 != null) break block4;
                string2 = "s-label/raw_audio_files";
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        }
        String string3 = string + "/" + string2;
        File file = new File(string3);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.fileName = string3 + "/" + "Audio_" + this.Jn + ".3gpp";
        this.JR = new MediaRecorder();
        this.JR.setAudioSource(1);
        this.JR.setOutputFormat(1);
        this.JR.setAudioEncoder(1);
        this.JR.setAudioSamplingRate(8000);
        this.JR.setOutputFile(this.fileName);
        this.JR.prepare();
        this.JR.start();
        return true;
    }

    private static void sleep(long l2) {
        try {
            Thread.sleep((long)l2);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    @Override
    public void gY() {
        super.gY();
        JW = null;
    }

    @Override
    public int getSensorType() {
        return 5005;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        this.JU = new ArrayList();
        this.JV = new ArrayList();
        if (((Boolean)this.Id.getParameter("ENABLE_AUDIO_STREAMING")).booleanValue()) {
            return this.hy();
        }
        return this.hw();
    }

    @Override
    protected void hd() {
        if (((Boolean)this.Id.getParameter("ENABLE_AUDIO_STREAMING")).booleanValue()) {
            this.hz();
            return;
        }
        this.hx();
    }

    @Override
    protected String he() {
        return "MicrophoneSensor";
    }

    @Override
    protected com.samsung.sensorframework.sda.b.a hl() {
        return this.JX;
    }

    @Override
    protected void hm() {
        double[] arrd = new double[this.JU.size()];
        long[] arrl = new long[this.JV.size()];
        for (int i2 = 0; i2 < this.JU.size() && i2 < this.JV.size(); ++i2) {
            arrd[i2] = (Double)this.JU.get(i2);
            arrl[i2] = (Long)this.JV.get(i2);
        }
        this.JX = ((com.samsung.sensorframework.sda.c.a.b)this.hi()).a(this.Jn, arrd, arrl, this.fileName, this.Id.gS());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean hw() {
        boolean bl = this.JY = this.hv();
        boolean bl2 = false;
        if (!bl) return bl2;
        try {
            new Thread(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                public void run() {
                    l.this.JR.getMaxAmplitude();
                    while (l.this.hf()) {
                        MediaRecorder mediaRecorder;
                        MediaRecorder mediaRecorder2 = mediaRecorder = l.this.JR;
                        synchronized (mediaRecorder2) {
                            if (l.this.JY) {
                                l.this.JU.add((Object)l.this.JR.getMaxAmplitude());
                                l.this.JV.add((Object)System.currentTimeMillis());
                            }
                        }
                        l.sleep(30L);
                    }
                    return;
                }
            }.start();
            return true;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void hx() {
        MediaRecorder mediaRecorder;
        MediaRecorder mediaRecorder2 = mediaRecorder = this.JR;
        synchronized (mediaRecorder2) {
            try {
                this.JR.stop();
                this.JR.release();
                this.JY = false;
                if (!((Boolean)this.Id.getParameter("SAVE_RAW_AUDIO_FILES")).booleanValue()) {
                    new File(this.fileName).delete();
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
    }

    protected boolean hy() {
        int n2 = 2 * AudioRecord.getMinBufferSize((int)8000, (int)16, (int)2);
        this.JS = new AudioRecord(1, 8000, 16, 2, n2);
        this.JS.startRecording();
        this.JT = new a(n2);
        this.JT.start();
        return true;
    }

    protected void hz() {
        this.JS.stop();
        this.JT.hA();
        this.JS.release();
    }

    protected class a
    extends Thread {
        int Ka;
        boolean Kb;

        public a(int n2) {
            this.Ka = n2;
        }

        public void hA() {
            while (this.Kb) {
                l.sleep(10L);
            }
        }

        public void run() {
            this.Kb = true;
            long l2 = 1000L * System.currentTimeMillis();
            long l3 = 1L;
            block6 : do {
                if (l.this.JS.getRecordingState() != 3) break;
                short[] arrs = new short[this.Ka];
                int n2 = l.this.JS.read(arrs, 0, this.Ka);
                int n3 = 0;
                do {
                    if (n3 >= n2) continue block6;
                    double d2 = (double)arrs[n3] / 32767.0;
                    l.this.JU.add((Object)d2);
                    long l4 = l2 + (long)(125.0 * (double)l3);
                    l.this.JV.add((Object)l4);
                    ++l3;
                    ++n3;
                } while (true);
                break;
            } while (true);
            try {
                this.Kb = false;
                return;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    }

}

