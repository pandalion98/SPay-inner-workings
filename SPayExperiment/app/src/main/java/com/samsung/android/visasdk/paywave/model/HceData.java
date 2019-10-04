/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.StaticParams;

public class HceData {
    private DynParams dynParams;
    private StaticParams staticParams;

    public DynParams getDynParams() {
        return this.dynParams;
    }

    public StaticParams getStaticParams() {
        return this.staticParams;
    }

    public void setDynParams(DynParams dynParams) {
        this.dynParams = dynParams;
    }

    public void setStaticParams(StaticParams staticParams) {
        this.staticParams = staticParams;
    }
}

