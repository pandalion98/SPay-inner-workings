package com.americanexpress.mobilepayments.hceclient.context;

public class PersistedDataContext {
    private String dataContext;
    private String hceDataContext;
    private String metaData;

    public String getDataContext() {
        return this.dataContext;
    }

    public void setDataContext(String str) {
        this.dataContext = str;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public void setMetaData(String str) {
        this.metaData = str;
    }

    public String getHceDataContext() {
        return this.hceDataContext;
    }

    public void setHceDataContext(String str) {
        this.hceDataContext = str;
    }
}
