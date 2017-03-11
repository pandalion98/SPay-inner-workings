package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;

/* renamed from: com.samsung.sensorframework.sda.d.a.j */
public class ImageContentReaderSensor extends AbstractContentReaderSensor {
    private static ImageContentReaderSensor JK;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
    }

    public static ImageContentReaderSensor aU(Context context) {
        if (JK == null) {
            synchronized (lock) {
                if (JK == null) {
                    JK = new ImageContentReaderSensor(context);
                }
            }
        }
        return JK;
    }

    private ImageContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        JK = null;
    }

    protected String[] hb() {
        return Jz;
    }

    public int getSensorType() {
        return 5019;
    }

    protected String[] hj() {
        return new String[]{Media.EXTERNAL_CONTENT_URI.toString(), Media.INTERNAL_CONTENT_URI.toString()};
    }

    protected String[] hk() {
        if (VERSION.SDK_INT >= 16) {
            return new String[]{"_display_name", "datetaken", "_size", "height", "width", "orientation"};
        }
        return new String[]{"_display_name", "datetaken", "_size", "orientation"};
    }

    protected String he() {
        return "ImageContentReaderSensor";
    }
}
