package com.samsung.sensorframework.sda.p039d;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.Arrays;

/* renamed from: com.samsung.sensorframework.sda.d.a */
public abstract class AbstractSensor implements SensorInterface {
    protected final Context HR;
    protected final SensorConfig Id;
    protected final int Im;
    protected boolean Ji;
    protected final Object Jj;
    protected Handler Jk;
    protected HandlerThread Jl;

    protected abstract boolean hc();

    protected abstract void hd();

    protected abstract String he();

    public AbstractSensor(Context context) {
        this(context, null);
    }

    public AbstractSensor(Context context, Integer num) {
        this.HR = context;
        this.Jj = new Object();
        if (num != null) {
            this.Id = SensorUtils.ao(num.intValue());
            this.Im = num.intValue();
        } else {
            this.Id = SensorUtils.ao(getSensorType());
            this.Im = getSensorType();
        }
        this.Jl = new HandlerThread("SensorHandlerThread:" + this.Im);
        this.Jl.start();
        this.Jk = new Handler(this.Jl.getLooper());
    }

    public void gY() {
        Log.m285d(he(), "destroySensor()");
        this.Jk = null;
        if (this.Jl != null && this.Jl.isAlive()) {
            this.Jl.quit();
        }
        this.Jl = null;
    }

    protected Handler gZ() {
        return this.Jk;
    }

    protected boolean m1573h(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return true;
        }
        for (String ci : strArr) {
            if (!ci(ci)) {
                return false;
            }
        }
        return true;
    }

    protected boolean ci(String str) {
        return this.HR.checkCallingOrSelfPermission(str) == 0;
    }

    protected void ha() {
        if (!m1573h(hb())) {
            String str = "Null";
            if (hb() != null) {
                str = Arrays.toString(hb());
            }
            throw new SDAException(8000, "Sensor: " + SensorUtils.ap(getSensorType()) + "; Required permissions not granted: " + str);
        }
    }

    protected String[] hb() {
        return null;
    }

    public boolean hf() {
        return this.Ji;
    }

    public void m1572c(String str, Object obj) {
        if (this.Id.bR(str)) {
            this.Id.setParameter(str, obj);
            return;
        }
        throw new SDAException(8005, "Invalid sensor config, key: " + str + " value: " + obj);
    }

    public Object cj(String str) {
        if (this.Id.bR(str)) {
            return this.Id.getParameter(str);
        }
        throw new SDAException(8005, "Invalid sensor config, key: " + str);
    }

    private boolean hg() {
        if (this.Id.bR("RAW_DATA")) {
            return ((Boolean) this.Id.getParameter("RAW_DATA")).booleanValue();
        }
        return true;
    }

    private boolean hh() {
        if (this.Id.bR("EXTRACT_FEATURES")) {
            return ((Boolean) this.Id.getParameter("EXTRACT_FEATURES")).booleanValue();
        }
        return false;
    }

    protected AbstractProcessor hi() {
        try {
            boolean hg = hg();
            boolean hh = hh();
            if (hg || hh) {
                return AbstractProcessor.m1531a(this.HR, getSensorType(), hg, hh);
            }
            throw new SDAException(8007, "No data requested from processor");
        } catch (SDAException e) {
            e.printStackTrace();
            return null;
        }
    }
}
