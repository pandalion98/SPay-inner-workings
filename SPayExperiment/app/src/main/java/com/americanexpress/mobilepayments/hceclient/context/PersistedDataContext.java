/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.context;

public class PersistedDataContext {
    private String dataContext;
    private String hceDataContext;
    private String metaData;

    public String getDataContext() {
        return this.dataContext;
    }

    public String getHceDataContext() {
        return this.hceDataContext;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public void setDataContext(String string) {
        this.dataContext = string;
    }

    public void setHceDataContext(String string) {
        this.hceDataContext = string;
    }

    public void setMetaData(String string) {
        this.metaData = string;
    }
}

