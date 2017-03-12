package com.squareup.okhttp.internal.spdy;

import java.util.Arrays;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;

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
    private final int[] values;

    public Settings() {
        this.values = new int[FLOW_CONTROL_OPTIONS];
    }

    void clear() {
        this.persisted = 0;
        this.persistValue = 0;
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    Settings set(int i, int i2, int i3) {
        if (i < this.values.length) {
            int i4 = UPLOAD_BANDWIDTH << i;
            this.set |= i4;
            if ((i2 & UPLOAD_BANDWIDTH) != 0) {
                this.persistValue |= i4;
            } else {
                this.persistValue &= i4 ^ -1;
            }
            if ((i2 & PERSISTED) != 0) {
                this.persisted = i4 | this.persisted;
            } else {
                this.persisted = (i4 ^ -1) & this.persisted;
            }
            this.values[i] = i3;
        }
        return this;
    }

    boolean isSet(int i) {
        if (((UPLOAD_BANDWIDTH << i) & this.set) != 0) {
            return true;
        }
        return false;
    }

    int get(int i) {
        return this.values[i];
    }

    int flags(int i) {
        int i2 = 0;
        if (isPersisted(i)) {
            i2 = PERSISTED;
        }
        if (persistValue(i)) {
            return i2 | UPLOAD_BANDWIDTH;
        }
        return i2;
    }

    int size() {
        return Integer.bitCount(this.set);
    }

    int getUploadBandwidth(int i) {
        return (PERSISTED & this.set) != 0 ? this.values[UPLOAD_BANDWIDTH] : i;
    }

    int getHeaderTableSize() {
        return (PERSISTED & this.set) != 0 ? this.values[UPLOAD_BANDWIDTH] : -1;
    }

    int getDownloadBandwidth(int i) {
        return (MAX_CONCURRENT_STREAMS & this.set) != 0 ? this.values[PERSISTED] : i;
    }

    boolean getEnablePush(boolean z) {
        int i;
        if ((MAX_CONCURRENT_STREAMS & this.set) != 0) {
            i = this.values[PERSISTED];
        } else if (z) {
            boolean z2 = true;
        } else {
            i = 0;
        }
        if (i == UPLOAD_BANDWIDTH) {
            return true;
        }
        return false;
    }

    int getRoundTripTime(int i) {
        return (CLIENT_CERTIFICATE_VECTOR_SIZE & this.set) != 0 ? this.values[ROUND_TRIP_TIME] : i;
    }

    int getMaxConcurrentStreams(int i) {
        return (16 & this.set) != 0 ? this.values[MAX_CONCURRENT_STREAMS] : i;
    }

    int getCurrentCwnd(int i) {
        return (32 & this.set) != 0 ? this.values[MAX_FRAME_SIZE] : i;
    }

    int getMaxFrameSize(int i) {
        return (32 & this.set) != 0 ? this.values[MAX_FRAME_SIZE] : i;
    }

    int getDownloadRetransRate(int i) {
        return (64 & this.set) != 0 ? this.values[MAX_HEADER_LIST_SIZE] : i;
    }

    int getMaxHeaderListSize(int i) {
        return (64 & this.set) != 0 ? this.values[MAX_HEADER_LIST_SIZE] : i;
    }

    int getInitialWindowSize(int i) {
        return (X509KeyUsage.digitalSignature & this.set) != 0 ? this.values[INITIAL_WINDOW_SIZE] : i;
    }

    int getClientCertificateVectorSize(int i) {
        return (SkeinMac.SKEIN_256 & this.set) != 0 ? this.values[CLIENT_CERTIFICATE_VECTOR_SIZE] : i;
    }

    boolean isFlowControlDisabled() {
        int i;
        if ((SkeinMac.SKEIN_1024 & this.set) != 0) {
            i = this.values[FLOW_CONTROL_OPTIONS];
        } else {
            i = 0;
        }
        if ((i & UPLOAD_BANDWIDTH) != 0) {
            return true;
        }
        return false;
    }

    boolean persistValue(int i) {
        if (((UPLOAD_BANDWIDTH << i) & this.persistValue) != 0) {
            return true;
        }
        return false;
    }

    boolean isPersisted(int i) {
        if (((UPLOAD_BANDWIDTH << i) & this.persisted) != 0) {
            return true;
        }
        return false;
    }

    void merge(Settings settings) {
        for (int i = 0; i < FLOW_CONTROL_OPTIONS; i += UPLOAD_BANDWIDTH) {
            if (settings.isSet(i)) {
                set(i, settings.flags(i), settings.get(i));
            }
        }
    }
}
