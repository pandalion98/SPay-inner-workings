package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.provider.MediaStore.Video.Media;

/* renamed from: com.samsung.sensorframework.sda.d.a.p */
public class VideoContentReaderSensor extends AbstractContentReaderSensor {
    private static final String[] Jz;
    private static VideoContentReaderSensor Ke;

    static {
        Jz = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
    }

    public static VideoContentReaderSensor aZ(Context context) {
        if (Ke == null) {
            synchronized (lock) {
                if (Ke == null) {
                    Ke = new VideoContentReaderSensor(context);
                }
            }
        }
        return Ke;
    }

    private VideoContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Ke = null;
    }

    protected String[] hb() {
        return Jz;
    }

    public int getSensorType() {
        return 5020;
    }

    protected String[] hj() {
        return new String[]{Media.EXTERNAL_CONTENT_URI.toString(), Media.INTERNAL_CONTENT_URI.toString()};
    }

    protected String[] hk() {
        return new String[]{"_display_name", "datetaken", "_size", "height", "width"};
    }

    protected String he() {
        return "VideoContentReaderSensor";
    }
}
