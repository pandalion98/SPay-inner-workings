/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Hashtable
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class ISO9796d2Signer
implements SignerWithRecovery {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private static Hashtable trailerMap = new Hashtable();
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private boolean fullMessage;
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private byte[] preSig;
    private byte[] recoveredMessage;
    private int trailer;

    static {
        trailerMap.put((Object)"RIPEMD128", (Object)Integers.valueOf((int)13004));
        trailerMap.put((Object)"RIPEMD160", (Object)Integers.valueOf((int)12748));
        trailerMap.put((Object)"SHA-1", (Object)Integers.valueOf((int)13260));
        trailerMap.put((Object)"SHA-256", (Object)Integers.valueOf((int)13516));
        trailerMap.put((Object)"SHA-384", (Object)Integers.valueOf((int)14028));
        trailerMap.put((Object)"SHA-512", (Object)Integers.valueOf((int)13772));
        trailerMap.put((Object)"Whirlpool", (Object)Integers.valueOf((int)14284));
    }

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, boolean bl) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        if (bl) {
            this.trailer = 188;
            return;
        }
        Integer n2 = (Integer)trailerMap.get((Object)digest.getAlgorithmName());
        if (n2 != null) {
            this.trailer = n2;
            return;
        }
        throw new IllegalArgumentException("no valid trailer for digest");
    }

    private void clearBlock(byte[] arrby) {
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            arrby[i2] = 0;
        }
    }

    private boolean isSameAs(byte[] arrby, byte[] arrby2) {
        boolean bl;
        boolean bl2 = true;
        if (this.messageLength > this.mBuf.length) {
            if (this.mBuf.length > arrby2.length) {
                bl2 = false;
            }
            bl = bl2;
            for (int i2 = 0; i2 != this.mBuf.length; ++i2) {
                if (arrby[i2] == arrby2[i2]) continue;
                bl = false;
            }
        } else {
            if (this.messageLength != arrby2.length) {
                bl2 = false;
            }
            bl = bl2;
            for (int i3 = 0; i3 != arrby2.length; ++i3) {
                if (arrby[i3] == arrby2[i3]) continue;
                bl = false;
            }
        }
        return bl;
    }

    private boolean returnFalse(byte[] arrby) {
        this.clearBlock(this.mBuf);
        this.clearBlock(arrby);
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] generateSignature() {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7 = this.digest.getDigestSize();
        if (this.trailer == 188) {
            n3 = 8;
            n5 = -1 + (this.block.length - n7);
            this.digest.doFinal(this.block, n5);
            this.block[-1 + this.block.length] = -68;
        } else {
            n3 = 16;
            n5 = -2 + (this.block.length - n7);
            this.digest.doFinal(this.block, n5);
            this.block[-2 + this.block.length] = (byte)(this.trailer >>> 8);
            this.block[-1 + this.block.length] = (byte)this.trailer;
        }
        if ((n4 = 4 + (n3 + 8 * (n7 + this.messageLength)) - this.keyBits) > 0) {
            int n8 = this.messageLength - (n4 + 7) / 8;
            n6 = 96;
            int n9 = n5 - n8;
            System.arraycopy((Object)this.mBuf, (int)0, (Object)this.block, (int)n9, (int)n8);
            n2 = n9;
        } else {
            n6 = 64;
            int n10 = n5 - this.messageLength;
            System.arraycopy((Object)this.mBuf, (int)0, (Object)this.block, (int)n10, (int)this.messageLength);
            n2 = n10;
        }
        if (n2 - 1 > 0) {
            for (int i2 = n2 - 1; i2 != 0; --i2) {
                this.block[i2] = -69;
            }
            byte[] arrby = this.block;
            int n11 = n2 - 1;
            arrby[n11] = (byte)(1 ^ arrby[n11]);
            this.block[0] = 11;
            byte[] arrby2 = this.block;
            arrby2[0] = (byte)(n6 | arrby2[0]);
        } else {
            this.block[0] = 10;
            byte[] arrby = this.block;
            arrby[0] = (byte)(n6 | arrby[0]);
        }
        byte[] arrby = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        return arrby;
    }

    @Override
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }

    @Override
    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = (RSAKeyParameters)cipherParameters;
        this.cipher.init(bl, rSAKeyParameters);
        this.keyBits = rSAKeyParameters.getModulus().bitLength();
        this.block = new byte[(7 + this.keyBits) / 8];
        this.mBuf = this.trailer == 188 ? new byte[-2 + (this.block.length - this.digest.getDigestSize())] : new byte[-3 + (this.block.length - this.digest.getDigestSize())];
        this.reset();
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        if (this.recoveredMessage != null) {
            this.clearBlock(this.recoveredMessage);
        }
        this.recoveredMessage = null;
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
        if (this.messageLength < this.mBuf.length) {
            this.mBuf[this.messageLength] = by;
        }
        this.messageLength = 1 + this.messageLength;
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        while (n3 > 0 && this.messageLength < this.mBuf.length) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
        this.digest.update(arrby, n2, n3);
        this.messageLength = n3 + this.messageLength;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void updateWithRecoveredMessage(byte[] arrby) {
        int n2;
        int n3;
        int n4;
        byte[] arrby2 = this.cipher.processBlock(arrby, 0, arrby.length);
        if ((64 ^ 192 & arrby2[0]) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        if ((12 ^ 15 & arrby2[-1 + arrby2.length]) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        }
        if ((188 ^ 255 & arrby2[-1 + arrby2.length]) == 0) {
            n2 = 1;
        } else {
            int n5 = (255 & arrby2[-2 + arrby2.length]) << 8 | 255 & arrby2[-1 + arrby2.length];
            Integer n6 = (Integer)trailerMap.get((Object)this.digest.getAlgorithmName());
            if (n6 == null) {
                throw new IllegalArgumentException("unrecognised hash in signature");
            }
            if (n5 != n6) {
                throw new IllegalStateException("signer initialised with wrong digest for trailer " + n5);
            }
            n2 = 2;
        }
        int n7 = 0;
        do {
            if (n7 == arrby2.length || (10 ^ 15 & arrby2[n7]) == 0) {
                n3 = n7 + 1;
                n4 = arrby2.length - n2 - this.digest.getDigestSize();
                if (n4 - n3 > 0) break;
                throw new InvalidCipherTextException("malformed block");
            }
            ++n7;
        } while (true);
        if ((32 & arrby2[0]) == 0) {
            this.fullMessage = true;
            this.recoveredMessage = new byte[n4 - n3];
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.recoveredMessage, (int)0, (int)this.recoveredMessage.length);
        } else {
            this.fullMessage = false;
            this.recoveredMessage = new byte[n4 - n3];
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.recoveredMessage, (int)0, (int)this.recoveredMessage.length);
        }
        this.preSig = arrby;
        this.preBlock = arrby2;
        this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
        this.messageLength = this.recoveredMessage.length;
        System.arraycopy((Object)this.recoveredMessage, (int)0, (Object)this.mBuf, (int)0, (int)this.recoveredMessage.length);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public boolean verifySignature(byte[] arrby) {
        byte[] arrby3;
        byte[] arrby2;
        int n2;
        int n3;
        int n4;
        block19 : {
            if (this.preSig == null) {
                byte[] arrby4;
                arrby2 = arrby4 = this.cipher.processBlock(arrby, 0, arrby.length);
            }
            if (!Arrays.areEqual((byte[])this.preSig, (byte[])arrby)) {
                throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
            }
            byte[] arrby5 = this.preBlock;
            this.preSig = null;
            this.preBlock = null;
            arrby2 = arrby5;
            if ((64 ^ 192 & arrby2[0]) != 0) {
                return this.returnFalse(arrby2);
            }
            if ((12 ^ 15 & arrby2[-1 + arrby2.length]) != 0) {
                return this.returnFalse(arrby2);
            }
            if ((188 ^ 255 & arrby2[-1 + arrby2.length]) == 0) {
                n4 = 1;
            } else {
                int n5 = (255 & arrby2[-2 + arrby2.length]) << 8 | 255 & arrby2[-1 + arrby2.length];
                Integer n6 = (Integer)trailerMap.get((Object)this.digest.getAlgorithmName());
                if (n6 == null) throw new IllegalArgumentException("unrecognised hash in signature");
                if (n5 != n6) {
                    throw new IllegalStateException("signer initialised with wrong digest for trailer " + n5);
                }
                n4 = 2;
            }
            break block19;
            catch (Exception exception) {
                return false;
            }
        }
        int n7 = 0;
        do {
            if (n7 == arrby2.length || (10 ^ 15 & arrby2[n7]) == 0) {
                n3 = n7 + 1;
                arrby3 = new byte[this.digest.getDigestSize()];
                n2 = arrby2.length - n4 - arrby3.length;
                if (n2 - n3 > 0) break;
                return this.returnFalse(arrby2);
            }
            ++n7;
        } while (true);
        if ((32 & arrby2[0]) == 0) {
            this.fullMessage = true;
            if (this.messageLength > n2 - n3) {
                return this.returnFalse(arrby2);
            }
            this.digest.reset();
            this.digest.update(arrby2, n3, n2 - n3);
            this.digest.doFinal(arrby3, 0);
            boolean bl = true;
            for (int i2 = 0; i2 != arrby3.length; ++i2) {
                int n8 = n2 + i2;
                arrby2[n8] = (byte)(arrby2[n8] ^ arrby3[i2]);
                if (arrby2[n2 + i2] == 0) continue;
                bl = false;
            }
            if (!bl) {
                return this.returnFalse(arrby2);
            }
            this.recoveredMessage = new byte[n2 - n3];
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.recoveredMessage, (int)0, (int)this.recoveredMessage.length);
        } else {
            this.fullMessage = false;
            this.digest.doFinal(arrby3, 0);
            boolean bl = true;
            for (int i3 = 0; i3 != arrby3.length; ++i3) {
                int n9 = n2 + i3;
                arrby2[n9] = (byte)(arrby2[n9] ^ arrby3[i3]);
                if (arrby2[n2 + i3] == 0) continue;
                bl = false;
            }
            if (!bl) {
                return this.returnFalse(arrby2);
            }
            this.recoveredMessage = new byte[n2 - n3];
            System.arraycopy((Object)arrby2, (int)n3, (Object)this.recoveredMessage, (int)0, (int)this.recoveredMessage.length);
        }
        if (this.messageLength != 0 && !this.isSameAs(this.mBuf, this.recoveredMessage)) {
            return this.returnFalse(arrby2);
        }
        this.clearBlock(this.mBuf);
        this.clearBlock(arrby2);
        return true;
    }
}

