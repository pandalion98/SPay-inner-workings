package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;

public class CtxPolicy extends JsonWraper {
    private int contextCacheQuotaPerDay;
    private boolean enableContextSensing;
    private boolean enableContextSensingOnLowBattery;
    private boolean enableContextSensingOnNoConnection;
    private int locationQuotaPerDay;
    private int maxTriggerDelay;
    private long minContextCallDelay;
    private int triggerGlobalQuotaPerDay;

    public boolean getEnableContextSensing() {
        return this.enableContextSensing;
    }

    public boolean getEnableContextSensingOnLowBattery() {
        return this.enableContextSensingOnLowBattery;
    }

    public boolean getEnableContextSensingOnNoConnection() {
        return this.enableContextSensingOnNoConnection;
    }

    public int getMaxTriggerDelay() {
        return this.maxTriggerDelay;
    }

    public int getTriggerGlobalQuotaPerDay() {
        return this.triggerGlobalQuotaPerDay;
    }

    public int getLocationQuotaPerDay() {
        return this.locationQuotaPerDay;
    }

    public long getMinContextCallDelay() {
        return (this.minContextCallDelay * 1000) * 60;
    }

    public int getContextCacheQuotaPerDay() {
        return this.contextCacheQuotaPerDay;
    }

    public void setEnableContextSensing(boolean z) {
        this.enableContextSensing = z;
    }

    public void setEnableContextSensingOnLowBattery(boolean z) {
        this.enableContextSensingOnLowBattery = z;
    }

    public void setEnableContextSensingOnNoConnection(boolean z) {
        this.enableContextSensingOnNoConnection = z;
    }

    public void setMaxTriggerDelay(int i) {
        this.maxTriggerDelay = i;
    }

    public void setTriggerGlobalQuotaPerDay(int i) {
        this.triggerGlobalQuotaPerDay = i;
    }

    public void setLocationQuotaPerDay(int i) {
        this.locationQuotaPerDay = i;
    }

    public void setMinContextCallDelay(long j) {
        this.minContextCallDelay = j;
    }

    public void setContextCacheQuotaPerDay(int i) {
        this.contextCacheQuotaPerDay = i;
    }

    public static CtxPolicy getDefault() {
        CtxPolicy ctxPolicy = new CtxPolicy();
        ctxPolicy.setEnableContextSensing(true);
        ctxPolicy.setEnableContextSensingOnLowBattery(false);
        ctxPolicy.setEnableContextSensingOnNoConnection(false);
        ctxPolicy.setContextCacheQuotaPerDay(2);
        ctxPolicy.setMinContextCallDelay(3600000);
        ctxPolicy.setMaxTriggerDelay(3600);
        ctxPolicy.setTriggerGlobalQuotaPerDay(10);
        ctxPolicy.setLocationQuotaPerDay(20);
        return ctxPolicy;
    }
}
