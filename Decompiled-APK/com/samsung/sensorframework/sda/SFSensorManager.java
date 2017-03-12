package com.samsung.sensorframework.sda;

import android.content.Context;
import android.util.SparseArray;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p030a.GlobalConfig;
import com.samsung.sensorframework.sda.p039d.SensorInterface;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import com.samsung.sensorframework.sda.p042e.AbstractSensorTask;
import com.samsung.sensorframework.sda.p042e.PullSensorTask;
import com.samsung.sensorframework.sda.p042e.PushSensorTask;
import com.samsung.sensorframework.sda.p042e.Subscription;
import com.samsung.sensorframework.sda.p042e.SubscriptionList;

/* renamed from: com.samsung.sensorframework.sda.a */
public class SFSensorManager {
    private static SFSensorManager HQ;
    private static final Object lock;
    private final Context HR;
    private final SparseArray<AbstractSensorTask> HS;
    private final SubscriptionList HT;
    private final GlobalConfig HU;

    static {
        lock = new Object();
    }

    public static SFSensorManager aN(Context context) {
        if (HQ == null) {
            synchronized (lock) {
                if (HQ == null) {
                    HQ = new SFSensorManager(context);
                }
            }
        }
        return HQ;
    }

    private SFSensorManager(Context context) {
        if (context == null) {
            throw new SDAException(8012, "Invalid parameter, context object passed is null");
        }
        this.HR = context;
        this.HS = new SparseArray();
        this.HT = new SubscriptionList();
        this.HU = GlobalConfig.gO();
    }

    private AbstractSensorTask ab(int i) {
        SensorInterface a = SensorUtils.m1639a(i, this.HR);
        if (SensorUtils.an(a.getSensorType())) {
            return new PullSensorTask(a);
        }
        return new PushSensorTask(a);
    }

    public synchronized int m1508a(int i, SensorDataListener sensorDataListener) {
        AbstractSensorTask abstractSensorTask;
        try {
            abstractSensorTask = (AbstractSensorTask) this.HS.get(i);
            if (abstractSensorTask == null) {
                abstractSensorTask = ab(i);
                this.HS.put(i, abstractSensorTask);
            }
            Log.m285d("SFSensorManager", "subscribeToSensorData() subscribing listener to sensor: " + SensorUtils.ap(i));
        } catch (Exception e) {
            throw new SDAException(8001, "Invalid sensor type: " + SensorUtils.ap(i) + " (Check permissions? Check hardware availability?)" + " Error message: " + e.getMessage());
        }
        return this.HT.m1646b(new Subscription(abstractSensorTask, sensorDataListener));
    }

    public synchronized void ac(int i) {
        Subscription av = this.HT.av(i);
        if (av != null) {
            AbstractSensorTask hF = av.hF();
            av.unregister();
            if (hF.isStopped()) {
                this.HS.remove(hF.getSensorType());
            }
            Log.m285d("SFSensorManager", "unsubscribeFromSensorData() unsubscribing from sensor: " + SensorUtils.ap(hF.getSensorType()));
        } else {
            throw new SDAException(8007, "Un-Mapped subscription id: " + i);
        }
    }

    public void m1509a(int i, String str, Object obj) {
        SensorUtils.m1639a(i, this.HR).m1571c(str, obj);
        Log.m285d("SFSensorManager", "sensor config updated, sensor: " + SensorUtils.ap(i) + " config: " + str + " value: " + obj.toString());
    }

    public void m1510b(String str, Object obj) {
        this.HU.setParameter(str, obj);
    }
}
