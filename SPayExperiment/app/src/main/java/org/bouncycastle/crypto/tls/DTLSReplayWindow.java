/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

class DTLSReplayWindow {
    private static final long VALID_SEQ_MASK = 0xFFFFFFFFFFFFL;
    private static final long WINDOW_SIZE = 64L;
    private long bitmap = 0L;
    private long latestConfirmedSeq = -1L;

    DTLSReplayWindow() {
    }

    /*
     * Enabled aggressive block sorting
     */
    void reportAuthenticated(long l2) {
        if ((0xFFFFFFFFFFFFL & l2) != l2) {
            throw new IllegalArgumentException("'seq' out of range");
        }
        if (l2 <= this.latestConfirmedSeq) {
            long l3 = this.latestConfirmedSeq - l2;
            if (l3 < 64L) {
                this.bitmap |= 1L << (int)l3;
            }
            return;
        }
        long l4 = l2 - this.latestConfirmedSeq;
        if (l4 >= 64L) {
            this.bitmap = 1L;
        } else {
            this.bitmap <<= (int)l4;
            this.bitmap = 1L | this.bitmap;
        }
        this.latestConfirmedSeq = l2;
    }

    void reset() {
        this.latestConfirmedSeq = -1L;
        this.bitmap = 0L;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean shouldDiscard(long l2) {
        long l3;
        return (0xFFFFFFFFFFFFL & l2) != l2 || l2 <= this.latestConfirmedSeq && ((l3 = this.latestConfirmedSeq - l2) >= 64L || (this.bitmap & 1L << (int)l3) != 0L);
    }
}

