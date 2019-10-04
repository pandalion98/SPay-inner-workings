/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;

public class Recommendation {
    private String id;
    private String name;
    private Sequence[] sequences;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Sequence[] getSequences() {
        return this.sequences;
    }
}

