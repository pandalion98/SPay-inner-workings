/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Pack
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MaxBytesExceededException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.SkippingStreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.Strings;

public class Salsa20Engine
implements SkippingStreamCipher {
    public static final int DEFAULT_ROUNDS = 20;
    private static final int STATE_SIZE = 16;
    protected static final byte[] sigma = Strings.toByteArray((String)"expand 32-byte k");
    protected static final byte[] tau = Strings.toByteArray((String)"expand 16-byte k");
    private int cW0;
    private int cW1;
    private int cW2;
    protected int[] engineState = new int[16];
    private int index = 0;
    private boolean initialised = false;
    private byte[] keyStream = new byte[64];
    protected int rounds;
    protected int[] x = new int[16];

    public Salsa20Engine() {
        this(20);
    }

    public Salsa20Engine(int n2) {
        if (n2 <= 0 || (n2 & 1) != 0) {
            throw new IllegalArgumentException("'rounds' must be a positive, even number");
        }
        this.rounds = n2;
    }

    private boolean limitExceeded() {
        int n2;
        this.cW0 = n2 = 1 + this.cW0;
        boolean bl = false;
        if (n2 == 0) {
            int n3;
            this.cW1 = n3 = 1 + this.cW1;
            bl = false;
            if (n3 == 0) {
                int n4;
                this.cW2 = n4 = 1 + this.cW2;
                int n5 = n4 & 32;
                bl = false;
                if (n5 != 0) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    private boolean limitExceeded(int n2) {
        int n3 = this.cW0 = n2 + this.cW0;
        boolean bl = false;
        if (n3 < n2) {
            int n4 = this.cW0;
            bl = false;
            if (n4 >= 0) {
                int n5;
                this.cW1 = n5 = 1 + this.cW1;
                bl = false;
                if (n5 == 0) {
                    int n6;
                    this.cW2 = n6 = 1 + this.cW2;
                    int n7 = n6 & 32;
                    bl = false;
                    if (n7 != 0) {
                        bl = true;
                    }
                }
            }
        }
        return bl;
    }

    private void resetLimitCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }

    protected static int rotl(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    public static void salsaCore(int n2, int[] arrn, int[] arrn2) {
        if (arrn.length != 16) {
            throw new IllegalArgumentException();
        }
        if (arrn2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (n2 % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        }
        int n3 = arrn[0];
        int n4 = arrn[1];
        int n5 = arrn[2];
        int n6 = arrn[3];
        int n7 = arrn[4];
        int n8 = arrn[5];
        int n9 = arrn[6];
        int n10 = arrn[7];
        int n11 = arrn[8];
        int n12 = arrn[9];
        int n13 = arrn[10];
        int n14 = arrn[11];
        int n15 = arrn[12];
        int n16 = arrn[13];
        int n17 = arrn[14];
        int n18 = arrn[15];
        while (n2 > 0) {
            int n19 = n7 ^ Salsa20Engine.rotl(n3 + n15, 7);
            int n20 = n11 ^ Salsa20Engine.rotl(n19 + n3, 9);
            int n21 = n15 ^ Salsa20Engine.rotl(n20 + n19, 13);
            int n22 = n3 ^ Salsa20Engine.rotl(n21 + n20, 18);
            int n23 = n12 ^ Salsa20Engine.rotl(n8 + n4, 7);
            int n24 = n16 ^ Salsa20Engine.rotl(n23 + n8, 9);
            int n25 = n4 ^ Salsa20Engine.rotl(n24 + n23, 13);
            int n26 = n8 ^ Salsa20Engine.rotl(n25 + n24, 18);
            int n27 = n17 ^ Salsa20Engine.rotl(n13 + n9, 7);
            int n28 = n5 ^ Salsa20Engine.rotl(n27 + n13, 9);
            int n29 = n9 ^ Salsa20Engine.rotl(n28 + n27, 13);
            int n30 = n13 ^ Salsa20Engine.rotl(n29 + n28, 18);
            int n31 = n6 ^ Salsa20Engine.rotl(n18 + n14, 7);
            int n32 = n10 ^ Salsa20Engine.rotl(n31 + n18, 9);
            int n33 = n14 ^ Salsa20Engine.rotl(n32 + n31, 13);
            int n34 = n18 ^ Salsa20Engine.rotl(n33 + n32, 18);
            n4 = n25 ^ Salsa20Engine.rotl(n22 + n31, 7);
            n5 = n28 ^ Salsa20Engine.rotl(n4 + n22, 9);
            n6 = n31 ^ Salsa20Engine.rotl(n5 + n4, 13);
            n3 = n22 ^ Salsa20Engine.rotl(n6 + n5, 18);
            n9 = n29 ^ Salsa20Engine.rotl(n26 + n19, 7);
            n10 = n32 ^ Salsa20Engine.rotl(n9 + n26, 9);
            n7 = n19 ^ Salsa20Engine.rotl(n10 + n9, 13);
            n8 = n26 ^ Salsa20Engine.rotl(n7 + n10, 18);
            n14 = n33 ^ Salsa20Engine.rotl(n30 + n23, 7);
            n11 = n20 ^ Salsa20Engine.rotl(n14 + n30, 9);
            n12 = n23 ^ Salsa20Engine.rotl(n11 + n14, 13);
            n13 = n30 ^ Salsa20Engine.rotl(n12 + n11, 18);
            n15 = n21 ^ Salsa20Engine.rotl(n34 + n27, 7);
            n16 = n24 ^ Salsa20Engine.rotl(n15 + n34, 9);
            n17 = n27 ^ Salsa20Engine.rotl(n16 + n15, 13);
            n18 = n34 ^ Salsa20Engine.rotl(n17 + n16, 18);
            n2 -= 2;
        }
        arrn2[0] = n3 + arrn[0];
        arrn2[1] = n4 + arrn[1];
        arrn2[2] = n5 + arrn[2];
        arrn2[3] = n6 + arrn[3];
        arrn2[4] = n7 + arrn[4];
        arrn2[5] = n8 + arrn[5];
        arrn2[6] = n9 + arrn[6];
        arrn2[7] = n10 + arrn[7];
        arrn2[8] = n11 + arrn[8];
        arrn2[9] = n12 + arrn[9];
        arrn2[10] = n13 + arrn[10];
        arrn2[11] = n14 + arrn[11];
        arrn2[12] = n15 + arrn[12];
        arrn2[13] = n16 + arrn[13];
        arrn2[14] = n17 + arrn[14];
        arrn2[15] = n18 + arrn[15];
    }

    protected void advanceCounter() {
        int n2;
        int[] arrn = this.engineState;
        arrn[8] = n2 = 1 + arrn[8];
        if (n2 == 0) {
            int[] arrn2 = this.engineState;
            arrn2[9] = 1 + arrn2[9];
        }
    }

    protected void advanceCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 > 0) {
            int[] arrn = this.engineState;
            arrn[9] = n2 + arrn[9];
        }
        int n4 = this.engineState[8];
        int[] arrn = this.engineState;
        arrn[8] = n3 + arrn[8];
        if (n4 != 0 && this.engineState[8] < n4) {
            int[] arrn2 = this.engineState;
            arrn2[9] = 1 + arrn2[9];
        }
    }

    protected void generateKeyStream(byte[] arrby) {
        Salsa20Engine.salsaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian((int[])this.x, (byte[])arrby, (int)0);
    }

    @Override
    public String getAlgorithmName() {
        String string = "Salsa20";
        if (this.rounds != 20) {
            string = string + "/" + this.rounds;
        }
        return string;
    }

    protected long getCounter() {
        return (long)this.engineState[9] << 32 | 0xFFFFFFFFL & (long)this.engineState[8];
    }

    protected int getNonceSize() {
        return 8;
    }

    @Override
    public long getPosition() {
        return 64L * this.getCounter() + (long)this.index;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] arrby = parametersWithIV.getIV();
        if (arrby == null || arrby.length != this.getNonceSize()) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " requires exactly " + this.getNonceSize() + " bytes of IV");
        }
        CipherParameters cipherParameters2 = parametersWithIV.getParameters();
        if (cipherParameters2 == null) {
            if (!this.initialised) {
                throw new IllegalStateException(this.getAlgorithmName() + " KeyParameter can not be null for first initialisation");
            }
            this.setKey(null, arrby);
        } else {
            if (!(cipherParameters2 instanceof KeyParameter)) {
                throw new IllegalArgumentException(this.getAlgorithmName() + " Init parameters must contain a KeyParameter (or null for re-init)");
            }
            this.setKey(((KeyParameter)cipherParameters2).getKey(), arrby);
        }
        this.reset();
        this.initialised = true;
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.limitExceeded(n3)) {
            throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            arrby2[i2 + n4] = (byte)(this.keyStream[this.index] ^ arrby[i2 + n2]);
            this.index = 63 & 1 + this.index;
            if (this.index != 0) continue;
            this.advanceCounter();
            this.generateKeyStream(this.keyStream);
        }
        return n3;
    }

    @Override
    public void reset() {
        this.index = 0;
        this.resetLimitCounter();
        this.resetCounter();
        this.generateKeyStream(this.keyStream);
    }

    protected void resetCounter() {
        int[] arrn = this.engineState;
        this.engineState[9] = 0;
        arrn[8] = 0;
    }

    protected void retreatCounter() {
        int n2;
        if (this.engineState[8] == 0 && this.engineState[9] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] arrn = this.engineState;
        arrn[8] = n2 = -1 + arrn[8];
        if (n2 == -1) {
            int[] arrn2 = this.engineState;
            arrn2[9] = -1 + arrn2[9];
        }
    }

    protected void retreatCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 != 0) {
            if ((0xFFFFFFFFL & (long)this.engineState[9]) < (0xFFFFFFFFL & (long)n2)) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            int[] arrn = this.engineState;
            arrn[9] = arrn[9] - n2;
        }
        if ((0xFFFFFFFFL & (long)this.engineState[8]) >= (0xFFFFFFFFL & (long)n3)) {
            int[] arrn = this.engineState;
            arrn[8] = arrn[8] - n3;
            return;
        }
        if (this.engineState[9] != 0) {
            int[] arrn = this.engineState;
            arrn[9] = -1 + arrn[9];
            int[] arrn2 = this.engineState;
            arrn2[8] = arrn2[8] - n3;
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }

    @Override
    public byte returnByte(byte by) {
        if (this.limitExceeded()) {
            throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
        }
        byte by2 = (byte)(by ^ this.keyStream[this.index]);
        this.index = 63 & 1 + this.index;
        if (this.index == 0) {
            this.advanceCounter();
            this.generateKeyStream(this.keyStream);
        }
        return by2;
    }

    @Override
    public long seekTo(long l2) {
        this.reset();
        return this.skip(l2);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setKey(byte[] arrby, byte[] arrby2) {
        int n2 = 16;
        if (arrby != null) {
            byte[] arrby3;
            if (arrby.length != n2 && arrby.length != 32) {
                throw new IllegalArgumentException(this.getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
            this.engineState[1] = Pack.littleEndianToInt((byte[])arrby, (int)0);
            this.engineState[2] = Pack.littleEndianToInt((byte[])arrby, (int)4);
            this.engineState[3] = Pack.littleEndianToInt((byte[])arrby, (int)8);
            this.engineState[4] = Pack.littleEndianToInt((byte[])arrby, (int)12);
            if (arrby.length == 32) {
                arrby3 = sigma;
            } else {
                arrby3 = tau;
                n2 = 0;
            }
            this.engineState[11] = Pack.littleEndianToInt((byte[])arrby, (int)n2);
            this.engineState[12] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 4));
            this.engineState[13] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 8));
            this.engineState[14] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 12));
            this.engineState[0] = Pack.littleEndianToInt((byte[])arrby3, (int)0);
            this.engineState[5] = Pack.littleEndianToInt((byte[])arrby3, (int)4);
            this.engineState[10] = Pack.littleEndianToInt((byte[])arrby3, (int)8);
            this.engineState[15] = Pack.littleEndianToInt((byte[])arrby3, (int)12);
        }
        this.engineState[6] = Pack.littleEndianToInt((byte[])arrby2, (int)0);
        this.engineState[7] = Pack.littleEndianToInt((byte[])arrby2, (int)4);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public long skip(long l2) {
        long l3 = 0L;
        if (l2 >= l3) {
            long l4;
            if (l2 >= 64L) {
                long l5 = l2 / 64L;
                this.advanceCounter(l5);
                l4 = l2 - l5 * 64L;
            } else {
                l4 = l2;
            }
            int n2 = this.index;
            this.index = 63 & this.index + (int)l4;
            if (this.index < n2) {
                this.advanceCounter();
            }
        } else {
            long l6 = -l2;
            if (l6 >= 64L) {
                long l7 = l6 / 64L;
                this.retreatCounter(l7);
                l6 -= l7 * 64L;
            }
            while (l3 < l6) {
                if (this.index == 0) {
                    this.retreatCounter();
                }
                this.index = 63 & -1 + this.index;
                ++l3;
            }
        }
        this.generateKeyStream(this.keyStream);
        return l2;
    }
}

