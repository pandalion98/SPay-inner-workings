package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Sequence {
    private String config;
    private int idle;
    private String key;
    private String mstSequenceId;
    private Next next;
    private int transmit;

    private static class Next {
        private int delay;
        private String sequence;

        private Next() {
        }
    }

    public Sequence(String str, String str2, int i, int i2, String str3) {
        this.key = str;
        this.mstSequenceId = str2;
        this.transmit = i;
        this.idle = i2;
        this.config = str3;
    }

    public String toString() {
        return "Sequence{key='" + this.key + '\'' + ", mstSequenceId='" + this.mstSequenceId + '\'' + ", transmit=" + this.transmit + ", idle=" + this.idle + ", config='" + this.config + '\'' + ", next=" + this.next + '}';
    }

    public String getKey() {
        return this.key;
    }

    public String getMstSequenceId() {
        return this.mstSequenceId;
    }

    public int getTransmit() {
        return this.transmit;
    }

    public int getIdle() {
        return this.idle;
    }

    public String getConfig() {
        return this.config;
    }

    public String getNextSequence() {
        if (this.next != null) {
            return this.next.sequence;
        }
        return null;
    }

    public int getNextDelay() {
        if (this.next != null) {
            return this.next.delay;
        }
        return -1;
    }
}
