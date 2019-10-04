/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.util.Arrays
 */
package com.squareup.okhttp.internal.spdy;

import java.util.Arrays;

public final class Settings {
    static final int CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
    static final int COUNT = 10;
    static final int CURRENT_CWND = 5;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65536;
    static final int DOWNLOAD_BANDWIDTH = 2;
    static final int DOWNLOAD_RETRANS_RATE = 6;
    static final int ENABLE_PUSH = 2;
    static final int FLAG_CLEAR_PREVIOUSLY_PERSISTED_SETTINGS = 1;
    static final int FLOW_CONTROL_OPTIONS = 10;
    static final int FLOW_CONTROL_OPTIONS_DISABLED = 1;
    static final int HEADER_TABLE_SIZE = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int MAX_FRAME_SIZE = 5;
    static final int MAX_HEADER_LIST_SIZE = 6;
    static final int PERSISTED = 2;
    static final int PERSIST_VALUE = 1;
    static final int ROUND_TRIP_TIME = 3;
    static final int UPLOAD_BANDWIDTH = 1;
    private int persistValue;
    private int persisted;
    private int set;
    private final int[] values = new int[10];

    void clear() {
        this.persisted = 0;
        this.persistValue = 0;
        this.set = 0;
        Arrays.fill((int[])this.values, (int)0);
    }

    int flags(int n2) {
        boolean bl = this.isPersisted(n2);
        int n3 = 0;
        if (bl) {
            n3 = 2;
        }
        if (this.persistValue(n2)) {
            n3 |= 1;
        }
        return n3;
    }

    int get(int n2) {
        return this.values[n2];
    }

    int getClientCertificateVectorSize(int n2) {
        if ((256 & this.set) != 0) {
            n2 = this.values[8];
        }
        return n2;
    }

    int getCurrentCwnd(int n2) {
        if ((32 & this.set) != 0) {
            n2 = this.values[5];
        }
        return n2;
    }

    int getDownloadBandwidth(int n2) {
        if ((4 & this.set) != 0) {
            n2 = this.values[2];
        }
        return n2;
    }

    int getDownloadRetransRate(int n2) {
        if ((64 & this.set) != 0) {
            n2 = this.values[6];
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    boolean getEnablePush(boolean bl) {
        if ((4 & this.set) != 0) {
            int n2 = this.values[2];
            if (n2 != 1) return false;
            return true;
        }
        if (!bl) return false;
        return true;
    }

    int getHeaderTableSize() {
        if ((2 & this.set) != 0) {
            return this.values[1];
        }
        return -1;
    }

    int getInitialWindowSize(int n2) {
        if ((128 & this.set) != 0) {
            n2 = this.values[7];
        }
        return n2;
    }

    int getMaxConcurrentStreams(int n2) {
        if ((16 & this.set) != 0) {
            n2 = this.values[4];
        }
        return n2;
    }

    int getMaxFrameSize(int n2) {
        if ((32 & this.set) != 0) {
            n2 = this.values[5];
        }
        return n2;
    }

    int getMaxHeaderListSize(int n2) {
        if ((64 & this.set) != 0) {
            n2 = this.values[6];
        }
        return n2;
    }

    int getRoundTripTime(int n2) {
        if ((8 & this.set) != 0) {
            n2 = this.values[3];
        }
        return n2;
    }

    int getUploadBandwidth(int n2) {
        if ((2 & this.set) != 0) {
            n2 = this.values[1];
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean isFlowControlDisabled() {
        int n2 = (1024 & this.set) != 0 ? this.values[10] : 0;
        int n3 = n2 & 1;
        boolean bl = false;
        if (n3 == 0) return bl;
        return true;
    }

    boolean isPersisted(int n2) {
        return (1 << n2 & this.persisted) != 0;
    }

    boolean isSet(int n2) {
        return (1 << n2 & this.set) != 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    void merge(Settings settings) {
        int n2 = 0;
        while (n2 < 10) {
            if (settings.isSet(n2)) {
                this.set(n2, settings.flags(n2), settings.get(n2));
            }
            ++n2;
        }
        return;
    }

    boolean persistValue(int n2) {
        return (1 << n2 & this.persistValue) != 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    Settings set(int n2, int n3, int n4) {
        if (n2 >= this.values.length) {
            return this;
        }
        int n5 = 1 << n2;
        this.set = n5 | this.set;
        this.persistValue = (n3 & 1) != 0 ? n5 | this.persistValue : (this.persistValue &= ~n5);
        this.persisted = (n3 & 2) != 0 ? n5 | this.persisted : (this.persisted &= ~n5);
        this.values[n2] = n4;
        return this;
    }

    int size() {
        return Integer.bitCount((int)this.set);
    }
}

