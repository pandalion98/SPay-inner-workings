package org.bouncycastle.crypto.encodings;

import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class ISO9796d1Encoding implements AsymmetricBlockCipher {
    private static final BigInteger SIX;
    private static final BigInteger SIXTEEN;
    private static byte[] inverse;
    private static byte[] shadows;
    private int bitSize;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private BigInteger modulus;
    private int padBits;

    static {
        SIXTEEN = BigInteger.valueOf(16);
        SIX = BigInteger.valueOf(6);
        shadows = new byte[]{(byte) 14, (byte) 3, (byte) 5, (byte) 8, (byte) 9, (byte) 4, (byte) 2, (byte) 15, (byte) 0, (byte) 13, (byte) 11, (byte) 6, (byte) 7, (byte) 10, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 1};
        inverse = new byte[]{(byte) 8, (byte) 15, (byte) 6, (byte) 1, (byte) 5, (byte) 2, (byte) 11, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 3, (byte) 4, (byte) 13, (byte) 10, (byte) 14, (byte) 9, (byte) 0, (byte) 7};
    }

    public ISO9796d1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.padBits = 0;
        this.engine = asymmetricBlockCipher;
    }

    private static byte[] convertOutputDecryptOnly(BigInteger bigInteger) {
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray[0] != null) {
            return toByteArray;
        }
        Object obj = new byte[(toByteArray.length - 1)];
        System.arraycopy(toByteArray, 1, obj, 0, obj.length);
        return obj;
    }

    private byte[] decodeBlock(byte[] bArr, int i, int i2) {
        int i3 = 0;
        int i4 = (this.bitSize + 13) / 16;
        BigInteger bigInteger = new BigInteger(1, this.engine.processBlock(bArr, i, i2));
        if (!bigInteger.mod(SIXTEEN).equals(SIX)) {
            if (this.modulus.subtract(bigInteger).mod(SIXTEEN).equals(SIX)) {
                bigInteger = this.modulus.subtract(bigInteger);
            } else {
                throw new InvalidCipherTextException("resulting integer iS or (modulus - iS) is not congruent to 6 mod 16");
            }
        }
        byte[] convertOutputDecryptOnly = convertOutputDecryptOnly(bigInteger);
        if ((convertOutputDecryptOnly[convertOutputDecryptOnly.length - 1] & 15) != 6) {
            throw new InvalidCipherTextException("invalid forcing byte in block");
        }
        convertOutputDecryptOnly[convertOutputDecryptOnly.length - 1] = (byte) (((convertOutputDecryptOnly[convertOutputDecryptOnly.length - 1] & GF2Field.MASK) >>> 4) | (inverse[(convertOutputDecryptOnly[convertOutputDecryptOnly.length - 2] & GF2Field.MASK) >> 4] << 4));
        convertOutputDecryptOnly[0] = (byte) ((shadows[(convertOutputDecryptOnly[1] & GF2Field.MASK) >>> 4] << 4) | shadows[convertOutputDecryptOnly[1] & 15]);
        int i5 = 0;
        int i6 = 0;
        int i7 = 1;
        for (int length = convertOutputDecryptOnly.length - 1; length >= convertOutputDecryptOnly.length - (i4 * 2); length -= 2) {
            int i8 = (shadows[(convertOutputDecryptOnly[length] & GF2Field.MASK) >>> 4] << 4) | shadows[convertOutputDecryptOnly[length] & 15];
            if (((convertOutputDecryptOnly[length - 1] ^ i8) & GF2Field.MASK) != 0) {
                if (i6 == 0) {
                    i7 = (convertOutputDecryptOnly[length - 1] ^ i8) & GF2Field.MASK;
                    i5 = length - 1;
                    i6 = 1;
                } else {
                    throw new InvalidCipherTextException("invalid tsums in block");
                }
            }
        }
        convertOutputDecryptOnly[i5] = (byte) 0;
        byte[] bArr2 = new byte[((convertOutputDecryptOnly.length - i5) / 2)];
        while (i3 < bArr2.length) {
            bArr2[i3] = convertOutputDecryptOnly[((i3 * 2) + i5) + 1];
            i3++;
        }
        this.padBits = i7 - 1;
        return bArr2;
    }

    private byte[] encodeBlock(byte[] bArr, int i, int i2) {
        Object obj = new byte[((this.bitSize + 7) / 8)];
        int i3 = this.padBits + 1;
        int i4 = (this.bitSize + 13) / 16;
        int i5 = 0;
        while (i5 < i4) {
            if (i5 > i4 - i2) {
                System.arraycopy(bArr, (i + i2) - (i4 - i5), obj, obj.length - i4, i4 - i5);
            } else {
                System.arraycopy(bArr, i, obj, obj.length - (i5 + i2), i2);
            }
            i5 += i2;
        }
        for (i5 = obj.length - (i4 * 2); i5 != obj.length; i5 += 2) {
            byte b = obj[(obj.length - i4) + (i5 / 2)];
            obj[i5] = (byte) ((shadows[(b & GF2Field.MASK) >>> 4] << 4) | shadows[b & 15]);
            obj[i5 + 1] = b;
        }
        i5 = obj.length - (i2 * 2);
        obj[i5] = (byte) (i3 ^ obj[i5]);
        obj[obj.length - 1] = (byte) ((obj[obj.length - 1] << 4) | 6);
        i5 = 8 - ((this.bitSize - 1) % 8);
        if (i5 != 8) {
            obj[0] = (byte) (obj[0] & (GF2Field.MASK >>> i5));
            obj[0] = (byte) ((X509KeyUsage.digitalSignature >>> i5) | obj[0]);
            i5 = 0;
        } else {
            obj[0] = null;
            obj[1] = (byte) (obj[1] | X509KeyUsage.digitalSignature);
            i5 = 1;
        }
        return this.engine.processBlock(obj, i5, obj.length - i5);
    }

    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        return this.forEncryption ? (inputBlockSize + 1) / 2 : inputBlockSize;
    }

    public int getOutputBlockSize() {
        int outputBlockSize = this.engine.getOutputBlockSize();
        return this.forEncryption ? outputBlockSize : (outputBlockSize + 1) / 2;
    }

    public int getPadBits() {
        return this.padBits;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = cipherParameters instanceof ParametersWithRandom ? (RSAKeyParameters) ((ParametersWithRandom) cipherParameters).getParameters() : (RSAKeyParameters) cipherParameters;
        this.engine.init(z, cipherParameters);
        this.modulus = rSAKeyParameters.getModulus();
        this.bitSize = this.modulus.bitLength();
        this.forEncryption = z;
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        return this.forEncryption ? encodeBlock(bArr, i, i2) : decodeBlock(bArr, i, i2);
    }

    public void setPadBits(int i) {
        if (i > 7) {
            throw new IllegalArgumentException("padBits > 7");
        }
        this.padBits = i;
    }
}
