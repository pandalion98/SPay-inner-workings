package com.samsung.android.visasdk.paywave.model;

public class HceData {
    private DynParams dynParams;
    private StaticParams staticParams;

    public DynParams getDynParams() {
        return this.dynParams;
    }

    public void setDynParams(DynParams dynParams) {
        this.dynParams = dynParams;
    }

    public StaticParams getStaticParams() {
        return this.staticParams;
    }

    public void setStaticParams(StaticParams staticParams) {
        this.staticParams = staticParams;
    }
}
