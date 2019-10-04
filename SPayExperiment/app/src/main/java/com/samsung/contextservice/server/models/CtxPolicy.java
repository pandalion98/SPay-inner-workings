/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;

public class CtxPolicy
extends JsonWraper {
    private int contextCacheQuotaPerDay;
    private boolean enableContextSensing;
    private boolean enableContextSensingOnLowBattery;
    private boolean enableContextSensingOnNoConnection;
    private int locationQuotaPerDay;
    private int maxTriggerDelay;
    private long minContextCallDelay;
    private int triggerGlobalQuotaPerDay;

    public static CtxPolicy getDefault() {
        CtxPolicy ctxPolicy = new CtxPolicy();
        ctxPolicy.setEnableContextSensing(true);
        ctxPolicy.setEnableContextSensingOnLowBattery(false);
        ctxPolicy.setEnableContextSensingOnNoConnection(false);
        ctxPolicy.setContextCacheQuotaPerDay(2);
        ctxPolicy.setMinContextCallDelay(3600000L);
        ctxPolicy.setMaxTriggerDelay(3600);
        ctxPolicy.setTriggerGlobalQuotaPerDay(10);
        ctxPolicy.setLocationQuotaPerDay(20);
        return ctxPolicy;
    }

    public int getContextCacheQuotaPerDay() {
        return this.contextCacheQuotaPerDay;
    }

    public boolean getEnableContextSensing() {
        return this.enableContextSensing;
    }

    public boolean getEnableContextSensingOnLowBattery() {
        return this.enableContextSensingOnLowBattery;
    }

    public boolean getEnableContextSensingOnNoConnection() {
        return this.enableContextSensingOnNoConnection;
    }

    public int getLocationQuotaPerDay() {
        return this.locationQuotaPerDay;
    }

    public int getMaxTriggerDelay() {
        return this.maxTriggerDelay;
    }

    public long getMinContextCallDelay() {
        return 60L * (1000L * this.minContextCallDelay);
    }

    public int getTriggerGlobalQuotaPerDay() {
        return this.triggerGlobalQuotaPerDay;
    }

    public void setContextCacheQuotaPerDay(int n2) {
        this.contextCacheQuotaPerDay = n2;
    }

    public void setEnableContextSensing(boolean bl) {
        this.enableContextSensing = bl;
    }

    public void setEnableContextSensingOnLowBattery(boolean bl) {
        this.enableContextSensingOnLowBattery = bl;
    }

    public void setEnableContextSensingOnNoConnection(boolean bl) {
        this.enableContextSensingOnNoConnection = bl;
    }

    public void setLocationQuotaPerDay(int n2) {
        this.locationQuotaPerDay = n2;
    }

    public void setMaxTriggerDelay(int n2) {
        this.maxTriggerDelay = n2;
    }

    public void setMinContextCallDelay(long l2) {
        this.minContextCallDelay = l2;
    }

    public void setTriggerGlobalQuotaPerDay(int n2) {
        this.triggerGlobalQuotaPerDay = n2;
    }
}

