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
 *  java.security.SecureRandom
 *  java.util.Hashtable
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSalt;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class ISO9796d2PSSSigner
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
    private int hLen;
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private int preMStart;
    private byte[] preSig;
    private int preTLength;
    private SecureRandom random;
    private byte[] recoveredMessage;
    private int saltLength;
    private byte[] standardSalt;
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

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2) {
        this(asymmetricBlockCipher, digest, n2, false);
    }

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2, boolean bl) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
        this.saltLength = n2;
        if (bl) {
            this.trailer = 188;
            return;
        }
        Integer n3 = (Integer)trailerMap.get((Object)digest.getAlgorithmName());
        if (n3 != null) {
            this.trailer = n3;
            return;
        }
        throw new IllegalArgumentException("no valid trailer for digest");
    }

    private void ItoOSP(int n2, byte[] arrby) {
        arrby[0] = (byte)(n2 >>> 24);
        arrby[1] = (byte)(n2 >>> 16);
        arrby[2] = (byte)(n2 >>> 8);
        arrby[3] = (byte)(n2 >>> 0);
    }

    private void LtoOSP(long l2, byte[] arrby) {
        arrby[0] = (byte)(l2 >>> 56);
        arrby[1] = (byte)(l2 >>> 48);
        arrby[2] = (byte)(l2 >>> 40);
        arrby[3] = (byte)(l2 >>> 32);
        arrby[4] = (byte)(l2 >>> 24);
        arrby[5] = (byte)(l2 >>> 16);
        arrby[6] = (byte)(l2 >>> 8);
        arrby[7] = (byte)(l2 >>> 0);
    }

    private void clearBlock(byte[] arrby) {
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            arrby[i2] = 0;
        }
    }

    private boolean isSameAs(byte[] arrby, byte[] arrby2) {
        boolean bl = true;
        if (this.messageLength != arrby2.length) {
            bl = false;
        }
        boolean bl2 = bl;
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            if (arrby[i2] == arrby2[i2]) continue;
            bl2 = false;
        }
        return bl2;
    }

    private byte[] maskGeneratorFunction1(byte[] arrby, int n2, int n3, int n4) {
        int n5;
        byte[] arrby2 = new byte[n4];
        byte[] arrby3 = new byte[this.hLen];
        byte[] arrby4 = new byte[4];
        this.digest.reset();
        for (n5 = 0; n5 < n4 / this.hLen; ++n5) {
            this.ItoOSP(n5, arrby4);
            this.digest.update(arrby, n2, n3);
            this.digest.update(arrby4, 0, arrby4.length);
            this.digest.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * this.hLen), (int)this.hLen);
        }
        if (n5 * this.hLen < n4) {
            this.ItoOSP(n5, arrby4);
            this.digest.update(arrby, n2, n3);
            this.digest.update(arrby4, 0, arrby4.length);
            this.digest.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * this.hLen), (int)(arrby2.length - n5 * this.hLen));
        }
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] generateSignature() {
        byte[] arrby;
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        byte[] arrby3 = new byte[8];
        this.LtoOSP(8 * this.messageLength, arrby3);
        this.digest.update(arrby3, 0, arrby3.length);
        this.digest.update(this.mBuf, 0, this.messageLength);
        this.digest.update(arrby2, 0, arrby2.length);
        if (this.standardSalt != null) {
            arrby = this.standardSalt;
        } else {
            arrby = new byte[this.saltLength];
            this.random.nextBytes(arrby);
        }
        this.digest.update(arrby, 0, arrby.length);
        byte[] arrby4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby4, 0);
        int n2 = 2;
        if (this.trailer == 188) {
            n2 = 1;
        }
        int n3 = -1 + (this.block.length - this.messageLength - arrby.length - this.hLen - n2);
        this.block[n3] = 1;
        System.arraycopy((Object)this.mBuf, (int)0, (Object)this.block, (int)(n3 + 1), (int)this.messageLength);
        System.arraycopy((Object)arrby, (int)0, (Object)this.block, (int)(n3 + 1 + this.messageLength), (int)arrby.length);
        byte[] arrby5 = this.maskGeneratorFunction1(arrby4, 0, arrby4.length, this.block.length - this.hLen - n2);
        for (int i2 = 0; i2 != arrby5.length; ++i2) {
            byte[] arrby6 = this.block;
            arrby6[i2] = (byte)(arrby6[i2] ^ arrby5[i2]);
        }
        System.arraycopy((Object)arrby4, (int)0, (Object)this.block, (int)(this.block.length - this.hLen - n2), (int)this.hLen);
        if (this.trailer == 188) {
            this.block[-1 + this.block.length] = -68;
        } else {
            this.block[-2 + this.block.length] = (byte)(this.trailer >>> 8);
            this.block[-1 + this.block.length] = (byte)this.trailer;
        }
        byte[] arrby7 = this.block;
        arrby7[0] = (byte)(127 & arrby7[0]);
        byte[] arrby8 = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        this.messageLength = 0;
        return arrby8;
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
        RSAKeyParameters rSAKeyParameters;
        int n2;
        int n3 = this.saltLength;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            RSAKeyParameters rSAKeyParameters2 = (RSAKeyParameters)parametersWithRandom.getParameters();
            if (bl) {
                this.random = parametersWithRandom.getRandom();
            }
            rSAKeyParameters = rSAKeyParameters2;
            n2 = n3;
        } else if (cipherParameters instanceof ParametersWithSalt) {
            ParametersWithSalt parametersWithSalt = (ParametersWithSalt)cipherParameters;
            RSAKeyParameters rSAKeyParameters3 = (RSAKeyParameters)parametersWithSalt.getParameters();
            this.standardSalt = parametersWithSalt.getSalt();
            int n4 = this.standardSalt.length;
            if (this.standardSalt.length != this.saltLength) {
                throw new IllegalArgumentException("Fixed salt is of wrong length");
            }
            rSAKeyParameters = rSAKeyParameters3;
            n2 = n4;
        } else {
            rSAKeyParameters = (RSAKeyParameters)cipherParameters;
            if (bl) {
                this.random = new SecureRandom();
            }
            n2 = n3;
        }
        this.cipher.init(bl, rSAKeyParameters);
        this.keyBits = rSAKeyParameters.getModulus().bitLength();
        this.block = new byte[(7 + this.keyBits) / 8];
        this.mBuf = this.trailer == 188 ? new byte[-1 + (-1 + (this.block.length - this.digest.getDigestSize() - n2))] : new byte[-2 + (-1 + (this.block.length - this.digest.getDigestSize() - n2))];
        this.reset();
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        if (this.mBuf != null) {
            this.clearBlock(this.mBuf);
        }
        if (this.recoveredMessage != null) {
            this.clearBlock(this.recoveredMessage);
            this.recoveredMessage = null;
        }
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            this.clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    @Override
    public void update(byte by) {
        if (this.preSig == null && this.messageLength < this.mBuf.length) {
            byte[] arrby = this.mBuf;
            int n2 = this.messageLength;
            this.messageLength = n2 + 1;
            arrby[n2] = by;
            return;
        }
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        if (this.preSig == null) {
            while (n3 > 0 && this.messageLength < this.mBuf.length) {
                this.update(arrby[n2]);
                ++n2;
                --n3;
            }
        }
        if (n3 > 0) {
            this.digest.update(arrby, n2, n3);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void updateWithRecoveredMessage(byte[] arrby) {
        int n2;
        byte[] arrby2;
        int n3;
        byte[] arrby3;
        int n4;
        block9 : {
            n2 = 1;
            arrby2 = this.cipher.processBlock(arrby, 0, arrby.length);
            if (arrby2.length < (7 + this.keyBits) / 8) {
                byte[] arrby4 = new byte[(7 + this.keyBits) / 8];
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)(arrby4.length - arrby2.length), (int)arrby2.length);
                this.clearBlock(arrby2);
                arrby2 = arrby4;
            }
            if ((188 ^ 255 & arrby2[-1 + arrby2.length]) == 0) {
                n4 = n2;
            } else {
                int n5 = (255 & arrby2[-2 + arrby2.length]) << 8 | 255 & arrby2[-1 + arrby2.length];
                Integer n6 = (Integer)trailerMap.get((Object)this.digest.getAlgorithmName());
                if (n6 == null) {
                    throw new IllegalArgumentException("unrecognised hash in signature");
                }
                if (n5 != n6) {
                    throw new IllegalStateException("signer initialised with wrong digest for trailer " + n5);
                }
                n4 = 2;
            }
            byte[] arrby5 = new byte[this.hLen];
            this.digest.doFinal(arrby5, 0);
            arrby3 = this.maskGeneratorFunction1(arrby2, arrby2.length - this.hLen - n4, this.hLen, arrby2.length - this.hLen - n4);
            for (int i2 = 0; i2 != arrby3.length; ++i2) {
                arrby2[i2] = (byte)(arrby2[i2] ^ arrby3[i2]);
            }
            arrby2[0] = (byte)(127 & arrby2[0]);
            int n7 = 0;
            do {
                if (n7 == arrby2.length || arrby2[n7] == n2) {
                    n3 = n7 + 1;
                    if (n3 >= arrby2.length) {
                        this.clearBlock(arrby2);
                    }
                    if (n3 <= n2) break;
                    break block9;
                }
                ++n7;
            } while (true);
            n2 = 0;
        }
        this.fullMessage = n2;
        this.recoveredMessage = new byte[arrby3.length - n3 - this.saltLength];
        System.arraycopy((Object)arrby2, (int)n3, (Object)this.recoveredMessage, (int)0, (int)this.recoveredMessage.length);
        System.arraycopy((Object)this.recoveredMessage, (int)0, (Object)this.mBuf, (int)0, (int)this.recoveredMessage.length);
        this.preSig = arrby;
        this.preBlock = arrby2;
        this.preMStart = n3;
        this.preTLength = n4;
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
        byte[] arrby2 = new byte[this.hLen];
        this.digest.doFinal(arrby2, 0);
        if (this.preSig == null) {
            this.updateWithRecoveredMessage(arrby);
        } else if (!Arrays.areEqual((byte[])this.preSig, (byte[])arrby)) {
            throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
        }
        byte[] arrby3 = this.preBlock;
        int n2 = this.preMStart;
        int n3 = this.preTLength;
        this.preSig = null;
        this.preBlock = null;
        byte[] arrby4 = new byte[8];
        this.LtoOSP(8 * this.recoveredMessage.length, arrby4);
        this.digest.update(arrby4, 0, arrby4.length);
        if (this.recoveredMessage.length != 0) {
            this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        this.digest.update(arrby2, 0, arrby2.length);
        this.digest.update(arrby3, n2 + this.recoveredMessage.length, this.saltLength);
        byte[] arrby5 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby5, 0);
        int n4 = arrby3.length - n3 - arrby5.length;
        boolean bl = true;
        for (int i2 = 0; i2 != arrby5.length; ++i2) {
            if (arrby5[i2] == arrby3[n4 + i2]) continue;
            bl = false;
        }
        this.clearBlock(arrby3);
        this.clearBlock(arrby5);
        if (!bl) {
            this.fullMessage = false;
            this.clearBlock(this.recoveredMessage);
            return false;
        }
        if (this.messageLength != 0) {
            if (!this.isSameAs(this.mBuf, this.recoveredMessage)) {
                this.clearBlock(this.mBuf);
                return false;
            }
            this.messageLength = 0;
        }
        this.clearBlock(this.mBuf);
        return true;
        catch (Exception exception) {
            return false;
        }
    }
}

