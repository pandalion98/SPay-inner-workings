package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class MstConfigurationResponseData {
    private CacheMetaData[] caches;
    private String id;
    private Recommendation[] recommendations;

    public String getId() {
        return this.id;
    }

    public Recommendation[] getRecommendations() {
        return this.recommendations;
    }

    public CacheMetaData[] getCaches() {
        return this.caches;
    }
}
