/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Sequence {
    private String config;
    private int idle;
    private String key;
    private String mstSequenceId;
    private Next next;
    private int transmit;

    public Sequence(String string, String string2, int n2, int n3, String string3) {
        this.key = string;
        this.mstSequenceId = string2;
        this.transmit = n2;
        this.idle = n3;
        this.config = string3;
    }

    public String getConfig() {
        return this.config;
    }

    public int getIdle() {
        return this.idle;
    }

    public String getKey() {
        return this.key;
    }

    public String getMstSequenceId() {
        return this.mstSequenceId;
    }

    public int getNextDelay() {
        if (this.next != null) {
            return this.next.delay;
        }
        return -1;
    }

    public String getNextSequence() {
        if (this.next != null) {
            return this.next.sequence;
        }
        return null;
    }

    public int getTransmit() {
        return this.transmit;
    }

    public String toString() {
        return "Sequence{key='" + this.key + '\'' + ", mstSequenceId='" + this.mstSequenceId + '\'' + ", transmit=" + this.transmit + ", idle=" + this.idle + ", config='" + this.config + '\'' + ", next=" + this.next + '}';
    }

    private static class Next {
        private int delay;
        private String sequence;

        private Next() {
        }
    }

}

