package com.samsung.sensorframework.sda.p042e;

import com.samsung.sensorframework.sda.SensorDataListener;

/* renamed from: com.samsung.sensorframework.sda.e.d */
public class Subscription {
    private final AbstractSensorTask KQ;
    private final SensorDataListener KR;

    public Subscription(AbstractSensorTask abstractSensorTask, SensorDataListener sensorDataListener) {
        this.KQ = abstractSensorTask;
        this.KR = sensorDataListener;
    }

    public AbstractSensorTask hF() {
        return this.KQ;
    }

    public SensorDataListener hG() {
        return this.KR;
    }

    public boolean hH() {
        return this.KQ.m1641c(this.KR);
    }

    public void unregister() {
        this.KQ.m1642d(this.KR);
    }

    public boolean m1645a(Subscription subscription) {
        return this.KQ == subscription.hF() && this.KR == subscription.hG();
    }
}
