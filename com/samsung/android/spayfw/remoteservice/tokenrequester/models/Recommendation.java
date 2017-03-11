package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Recommendation {
    private String id;
    private String name;
    private Sequence[] sequences;

    public Sequence[] getSequences() {
        return this.sequences;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
