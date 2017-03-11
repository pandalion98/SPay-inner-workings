package org.bouncycastle.crypto.tls;

class DTLSEpoch {
    private final TlsCipher cipher;
    private final int epoch;
    private final DTLSReplayWindow replayWindow;
    private long sequenceNumber;

    DTLSEpoch(int i, TlsCipher tlsCipher) {
        this.replayWindow = new DTLSReplayWindow();
        this.sequenceNumber = 0;
        if (i < 0) {
            throw new IllegalArgumentException("'epoch' must be >= 0");
        } else if (tlsCipher == null) {
            throw new IllegalArgumentException("'cipher' cannot be null");
        } else {
            this.epoch = i;
            this.cipher = tlsCipher;
        }
    }

    long allocateSequenceNumber() {
        long j = this.sequenceNumber;
        this.sequenceNumber = 1 + j;
        return j;
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
