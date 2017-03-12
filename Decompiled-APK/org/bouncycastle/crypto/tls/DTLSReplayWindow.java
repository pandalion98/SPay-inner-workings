package org.bouncycastle.crypto.tls;

class DTLSReplayWindow {
    private static final long VALID_SEQ_MASK = 281474976710655L;
    private static final long WINDOW_SIZE = 64;
    private long bitmap;
    private long latestConfirmedSeq;

    DTLSReplayWindow() {
        this.latestConfirmedSeq = -1;
        this.bitmap = 0;
    }

    void reportAuthenticated(long j) {
        if ((VALID_SEQ_MASK & j) != j) {
            throw new IllegalArgumentException("'seq' out of range");
        } else if (j <= this.latestConfirmedSeq) {
            r0 = this.latestConfirmedSeq - j;
            if (r0 < WINDOW_SIZE) {
                this.bitmap = (1 << ((int) r0)) | this.bitmap;
            }
        } else {
            r0 = j - this.latestConfirmedSeq;
            if (r0 >= WINDOW_SIZE) {
                this.bitmap = 1;
            } else {
                this.bitmap <<= (int) r0;
                this.bitmap |= 1;
            }
            this.latestConfirmedSeq = j;
        }
    }

    void reset() {
        this.latestConfirmedSeq = -1;
        this.bitmap = 0;
    }

    boolean shouldDiscard(long j) {
        if ((VALID_SEQ_MASK & j) != j) {
            return true;
        }
        if (j <= this.latestConfirmedSeq) {
            long j2 = this.latestConfirmedSeq - j;
            if (j2 >= WINDOW_SIZE) {
                return true;
            }
            if (((1 << ((int) j2)) & this.bitmap) != 0) {
                return true;
            }
        }
        return false;
    }
}
