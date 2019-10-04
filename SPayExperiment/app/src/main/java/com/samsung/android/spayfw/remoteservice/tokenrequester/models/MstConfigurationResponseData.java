/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Recommendation;

public class MstConfigurationResponseData {
    private CacheMetaData[] caches;
    private String id;
    private Recommendation[] recommendations;

    public CacheMetaData[] getCaches() {
        return this.caches;
    }

    public String getId() {
        return this.id;
    }

    public Recommendation[] getRecommendations() {
        return this.recommendations;
    }
}

