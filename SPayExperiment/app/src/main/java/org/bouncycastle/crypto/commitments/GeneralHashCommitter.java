/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.commitments;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Commitment;
import org.bouncycastle.crypto.Committer;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Arrays;

public class GeneralHashCommitter
implements Committer {
    private final int byteLength;
    private final Digest digest;
    private final SecureRandom random;

    public GeneralHashCommitter(ExtendedDigest extendedDigest, SecureRandom secureRandom) {
        this.digest = extendedDigest;
        this.byteLength = extendedDigest.getByteLength();
        this.random = secureRandom;
    }

    private byte[] calculateCommitment(byte[] arrby, byte[] arrby2) {
        byte[] arrby3 = new byte[this.digest.getDigestSize()];
        this.digest.update(arrby, 0, arrby.length);
        this.digest.update(arrby2, 0, arrby2.length);
        this.digest.update((byte)(arrby2.length >>> 8));
        this.digest.update((byte)arrby2.length);
        this.digest.doFinal(arrby3, 0);
        return arrby3;
    }

    @Override
    public Commitment commit(byte[] arrby) {
        if (arrby.length > this.byteLength / 2) {
            throw new DataLengthException("Message to be committed to too large for digest.");
        }
        byte[] arrby2 = new byte[this.byteLength - arrby.length];
        this.random.nextBytes(arrby2);
        return new Commitment(arrby2, this.calculateCommitment(arrby2, arrby));
    }

    @Override
    public boolean isRevealed(Commitment commitment, byte[] arrby) {
        if (arrby.length + commitment.getSecret().length != this.byteLength) {
            throw new DataLengthException("Message and witness secret lengths do not match.");
        }
        byte[] arrby2 = this.calculateCommitment(commitment.getSecret(), arrby);
        return Arrays.constantTimeAreEqual((byte[])commitment.getCommitment(), (byte[])arrby2);
    }
}

