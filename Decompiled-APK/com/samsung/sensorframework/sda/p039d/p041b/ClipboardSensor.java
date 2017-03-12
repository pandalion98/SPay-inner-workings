package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.p036c.p038b.ClipboardProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.c */
public class ClipboardSensor extends AbstractPushSensor {
    private static ClipboardSensor Kp;
    private static final Object lock;
    private ClipboardSensor Kq;
    private final ClipboardManager Kr;

    /* renamed from: com.samsung.sensorframework.sda.d.b.c.a */
    class ClipboardSensor implements OnPrimaryClipChangedListener {
        final /* synthetic */ ClipboardSensor Ks;

        ClipboardSensor(ClipboardSensor clipboardSensor) {
            this.Ks = clipboardSensor;
        }

        public void onPrimaryClipChanged() {
            this.Ks.m1618a(this.Ks.Kr.getPrimaryClip());
        }
    }

    static {
        lock = new Object();
    }

    public static ClipboardSensor bc(Context context) {
        if (Kp == null) {
            synchronized (lock) {
                if (Kp == null) {
                    Kp = new ClipboardSensor(context);
                }
            }
        }
        return Kp;
    }

    private ClipboardSensor(Context context) {
        super(context);
        this.Kr = (ClipboardManager) this.HR.getSystemService("clipboard");
        this.Kq = new ClipboardSensor(this);
    }

    public void gY() {
        super.gY();
        Kp = null;
    }

    public String he() {
        return "ClipboardSensor";
    }

    public int getSensorType() {
        return 5024;
    }

    protected void m1618a(ClipData clipData) {
        m1613a(((ClipboardProcessor) super.hi()).m1562a(System.currentTimeMillis(), this.Id.gS(), clipData));
    }

    protected IntentFilter[] hC() {
        return null;
    }

    protected boolean hc() {
        this.Kr.addPrimaryClipChangedListener(this.Kq);
        return true;
    }

    protected void hd() {
        this.Kr.removePrimaryClipChangedListener(this.Kq);
    }

    protected void m1619a(Context context, Intent intent) {
    }
}
