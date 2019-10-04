/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.DTLSReplayWindow;
import org.bouncycastle.crypto.tls.TlsCipher;

class DTLSEpoch {
    private final TlsCipher cipher;
    private final int epoch;
    private final DTLSReplayWindow replayWindow = new DTLSReplayWindow();
    private long sequenceNumber = 0L;

    DTLSEpoch(int n2, TlsCipher tlsCipher) {
        if (n2 < 0) {
            throw new IllegalArgumentException("'epoch' must be >= 0");
        }
        if (tlsCipher == null) {
            throw new IllegalArgumentException("'cipher' cannot be null");
        }
        this.epoch = n2;
        this.cipher = tlsCipher;
    }

    long allocateSequenceNumber() {
        long l2 = this.sequenceNumber;
        this.sequenceNumber = 1L + l2;
        return l2;
    }

    TlsCipher getCipher() {
        return this.cipher;
    }

    int getEpoch() {
        return this.epoch;
    }

    DTLSReplayWindow getReplayWindow() {
        return this.replayWindow;
    }

    long getSequenceNumber() {
        return this.sequenceNumber;
    }
}

