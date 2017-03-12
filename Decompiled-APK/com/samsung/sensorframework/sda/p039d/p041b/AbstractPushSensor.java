package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.SensorDataListener;
import com.samsung.sensorframework.sda.p030a.GlobalConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p039d.AbstractSensor;

/* renamed from: com.samsung.sensorframework.sda.d.b.a */
public abstract class AbstractPushSensor extends AbstractSensor implements PushSensor {
    protected SensorDataListener Kl;
    protected BroadcastReceiver Km;

    /* renamed from: com.samsung.sensorframework.sda.d.b.a.1 */
    class AbstractPushSensor extends BroadcastReceiver {
        final /* synthetic */ AbstractPushSensor Kn;

        AbstractPushSensor(AbstractPushSensor abstractPushSensor) {
            this.Kn = abstractPushSensor;
        }

        public void onReceive(Context context, Intent intent) {
            if (this.Kn.Ji) {
                this.Kn.m1612a(context, intent);
            } else {
                Log.m285d(this.Kn.he(), "BroadcastReceiver.onReceive() called while not sensing.");
            }
        }
    }

    protected abstract void m1612a(Context context, Intent intent);

    protected abstract IntentFilter[] hC();

    public AbstractPushSensor(Context context) {
        super(context);
        this.Km = new AbstractPushSensor(this);
    }

    public void m1614a(SensorDataListener sensorDataListener) {
        ha();
        if (this.Ji) {
            Log.m285d(he(), "sensing already sensing");
            throw new SDAException(8003, "sensor already sensing");
        }
        this.Kl = sensorDataListener;
        hc();
        IntentFilter[] hC = hC();
        if (hC == null || hC.length <= 0) {
            Log.m285d(he(), "getIntentFilters() returned null");
        } else {
            String str = null;
            if (GlobalConfig.gO() != null) {
                str = GlobalConfig.gO().gR();
            }
            Log.m285d(he(), " intentBroadcasterPermission: " + str);
            for (IntentFilter intentFilter : hC) {
                if (intentFilter == null || intentFilter.countActions() <= 0) {
                    Log.m285d(he(), "Intent filter is null or countActions() is zero");
                } else {
                    for (int i = 0; i < intentFilter.countActions(); i++) {
                        Log.m285d(he(), "Registering receiver for: " + intentFilter.getAction(i));
                    }
                    this.HR.registerReceiver(this.Km, intentFilter, str, gZ());
                }
            }
        }
        this.Ji = true;
        Log.m285d(he(), "Sensing started.");
    }

    public void m1615b(SensorDataListener sensorDataListener) {
        ha();
        if (this.Ji) {
            hd();
            try {
                IntentFilter[] hC = hC();
                if (hC != null && hC.length > 0) {
                    this.HR.unregisterReceiver(this.Km);
                }
            } catch (IllegalArgumentException e) {
            }
            this.Ji = false;
            Log.m285d(he(), "Sensing stopped.");
            return;
        }
        Log.m285d(he(), "sensor not sensing");
        throw new SDAException(8004, "sensor not sensing");
    }

    protected void m1613a(SensorData sensorData) {
        if (this.Kl != null) {
            this.Kl.m1530a(sensorData);
        }
    }
}
