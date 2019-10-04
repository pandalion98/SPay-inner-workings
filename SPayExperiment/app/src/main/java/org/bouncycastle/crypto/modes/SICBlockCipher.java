/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.SkippingStreamCipher;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;

public class SICBlockCipher
extends StreamBlockCipher
implements SkippingStreamCipher {
    private byte[] IV;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    private byte[] counter;
    private byte[] counterOut;

    public SICBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = this.cipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.counter = new byte[this.blockSize];
        this.counterOut = new byte[this.blockSize];
        this.byteCount = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void adjustCounter(long l2) {
        long l3;
        int n2;
        block13 : {
            long l4;
            long l5;
            block12 : {
                long l6;
                block10 : {
                    long l7;
                    block9 : {
                        block11 : {
                            block7 : {
                                block8 : {
                                    l4 = 0L;
                                    if (l2 < l4) break block7;
                                    l6 = (l2 + (long)this.byteCount) / (long)this.blockSize;
                                    if (l6 <= 255L) break block8;
                                    l7 = l6;
                                    break block9;
                                }
                                this.incrementCounter((int)l6);
                                break block10;
                            }
                            l3 = (-l2 - (long)this.byteCount) / (long)this.blockSize;
                            if (l3 <= 255L) break block11;
                            l5 = l3;
                            break block12;
                        }
                        while (l4 != l3) {
                            this.decrementCounter();
                            ++l4;
                        }
                        break block13;
                    }
                    for (int i2 = 5; i2 >= 1; --i2) {
                        long l8 = 1L << i2 * 8;
                        while (l7 >= l8) {
                            this.incrementCounterPow2(i2);
                            l7 -= l8;
                        }
                    }
                    this.incrementCounter((int)l7);
                }
                this.byteCount = (int)(l2 + (long)this.byteCount - l6 * (long)this.blockSize);
                return;
            }
            for (int i3 = i2; i3 >= 1; --i3) {
                long l9 = 1L << i3 * 8;
                while (l5 > l9) {
                    this.decrementCounterPow2(i3);
                    l5 -= l9;
                }
            }
            while (l4 != l5) {
                this.decrementCounter();
                ++l4;
            }
        }
        if ((n2 = (int)(l2 + (long)this.byteCount + l3 * (long)this.blockSize)) >= 0) {
            this.byteCount = 0;
            return;
        }
        this.decrementCounter();
        this.byteCount = n2 + this.blockSize;
    }

    private void decrementCounter() {
        if (this.counter[0] == 0) {
            int n2 = -1 + this.counter.length;
            boolean bl = false;
            for (int i2 = n2; i2 > 0; --i2) {
                if (this.counter[i2] == 0) continue;
                bl = true;
            }
            if (!bl) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        for (int i3 = -1 + this.counter.length; i3 >= 0; --i3) {
            byte by;
            byte[] arrby = this.counter;
            arrby[i3] = by = (byte)(-1 + arrby[i3]);
            if (by != -1) break;
        }
    }

    private void decrementCounterPow2(int n2) {
        if (this.counter[n2] == 0) {
            int n3 = this.counter.length - (n2 + 1);
            boolean bl = false;
            for (int i2 = n3; i2 > 0; --i2) {
                if (this.counter[i2] == 0) continue;
                bl = true;
            }
            if (!bl) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        for (int i3 = this.counter.length - (n2 + 1); i3 >= 0; --i3) {
            byte by;
            byte[] arrby = this.counter;
            arrby[i3] = by = (byte)(-1 + arrby[i3]);
            if (by != -1) break;
        }
    }

    private void incrementCounter() {
        for (int i2 = -1 + this.counter.length; i2 >= 0; --i2) {
            byte by;
            byte[] arrby = this.counter;
            arrby[i2] = by = (byte)(1 + arrby[i2]);
            if (by != 0) break;
        }
    }

    private void incrementCounter(int n2) {
        byte by = this.counter[-1 + this.counter.length];
        byte[] arrby = this.counter;
        int n3 = -1 + this.counter.length;
        arrby[n3] = (byte)(n2 + arrby[n3]);
        if (by != 0 && this.counter[-1 + this.counter.length] < by) {
            this.incrementCounterPow2(1);
        }
    }

    private void incrementCounterPow2(int n2) {
        for (int i2 = this.counter.length - (n2 + 1); i2 >= 0; --i2) {
            byte by;
            byte[] arrby = this.counter;
            arrby[i2] = by = (byte)(1 + arrby[i2]);
            if (by != 0) break;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected byte calculateByte(byte by) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
            byte[] arrby = this.counterOut;
            int n2 = this.byteCount;
            this.byteCount = n2 + 1;
            return (byte)(by ^ arrby[n2]);
        }
        byte[] arrby = this.counterOut;
        int n3 = this.byteCount;
        this.byteCount = n3 + 1;
        byte by2 = (byte)(by ^ arrby[n3]);
        if (this.byteCount != this.counter.length) return by2;
        this.byteCount = 0;
        this.incrementCounter();
        return by2;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/SIC";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public long getPosition() {
        byte[] arrby = new byte[this.IV.length];
        System.arraycopy((Object)this.counter, (int)0, (Object)arrby, (int)0, (int)arrby.length);
        for (int i2 = -1 + arrby.length; i2 >= 1; --i2) {
            int n2 = (255 & arrby[i2]) - (255 & this.IV[i2]);
            if (n2 < 0) {
                int n3 = i2 - 1;
                arrby[n3] = (byte)(-1 + arrby[n3]);
                n2 += 256;
            }
            arrby[i2] = (byte)n2;
        }
        return Pack.bigEndianToLong((byte[])arrby, (int)(-8 + arrby.length)) * (long)this.blockSize + (long)this.byteCount;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            System.arraycopy((Object)parametersWithIV.getIV(), (int)0, (Object)this.IV, (int)0, (int)this.IV.length);
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(true, parametersWithIV.getParameters());
            }
            this.reset();
            return;
        }
        throw new IllegalArgumentException("SIC mode requires ParametersWithIV");
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.processBytes(arrby, n2, this.blockSize, arrby2, n3);
        return this.blockSize;
    }

    @Override
    public void reset() {
        System.arraycopy((Object)this.IV, (int)0, (Object)this.counter, (int)0, (int)this.counter.length);
        this.cipher.reset();
        this.byteCount = 0;
    }

    @Override
    public long seekTo(long l2) {
        this.reset();
        return this.skip(l2);
    }

    @Override
    public long skip(long l2) {
        this.adjustCounter(l2);
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        return l2;
    }
}

