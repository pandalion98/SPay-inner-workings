package org.bouncycastle.crypto.modes;

import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import java.util.Vector;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class OCBBlockCipher implements AEADBlockCipher {
    private static final int BLOCK_SIZE = 16;
    private byte[] Checksum;
    private byte[] KtopInput;
    private Vector f198L;
    private byte[] L_Asterisk;
    private byte[] L_Dollar;
    private byte[] OffsetHASH;
    private byte[] OffsetMAIN;
    private byte[] OffsetMAIN_0;
    private byte[] Stretch;
    private byte[] Sum;
    private boolean forEncryption;
    private byte[] hashBlock;
    private long hashBlockCount;
    private int hashBlockPos;
    private BlockCipher hashCipher;
    private byte[] initialAssociatedText;
    private byte[] macBlock;
    private int macSize;
    private byte[] mainBlock;
    private long mainBlockCount;
    private int mainBlockPos;
    private BlockCipher mainCipher;

    public OCBBlockCipher(BlockCipher blockCipher, BlockCipher blockCipher2) {
        this.KtopInput = null;
        this.Stretch = new byte[24];
        this.OffsetMAIN_0 = new byte[BLOCK_SIZE];
        this.OffsetMAIN = new byte[BLOCK_SIZE];
        if (blockCipher == null) {
            throw new IllegalArgumentException("'hashCipher' cannot be null");
        } else if (blockCipher.getBlockSize() != BLOCK_SIZE) {
            throw new IllegalArgumentException("'hashCipher' must have a block size of 16");
        } else if (blockCipher2 == null) {
            throw new IllegalArgumentException("'mainCipher' cannot be null");
        } else if (blockCipher2.getBlockSize() != BLOCK_SIZE) {
            throw new IllegalArgumentException("'mainCipher' must have a block size of 16");
        } else if (blockCipher.getAlgorithmName().equals(blockCipher2.getAlgorithmName())) {
            this.hashCipher = blockCipher;
            this.mainCipher = blockCipher2;
        } else {
            throw new IllegalArgumentException("'hashCipher' and 'mainCipher' must be the same algorithm");
        }
    }

    protected static byte[] OCB_double(byte[] bArr) {
        byte[] bArr2 = new byte[BLOCK_SIZE];
        bArr2[15] = (byte) ((CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA >>> ((1 - shiftLeft(bArr, bArr2)) << 3)) ^ bArr2[15]);
        return bArr2;
    }

    protected static void OCB_extend(byte[] bArr, int i) {
        bArr[i] = VerifyPINApdu.P2_PLAINTEXT;
        while (true) {
            i++;
            if (i < BLOCK_SIZE) {
                bArr[i] = (byte) 0;
            } else {
                return;
            }
        }
    }

    protected static int OCB_ntz(long j) {
        if (j == 0) {
            return 64;
        }
        int i = 0;
        while ((1 & j) == 0) {
            i++;
            j >>>= 1;
        }
        return i;
    }

    protected static int shiftLeft(byte[] bArr, byte[] bArr2) {
        int i = BLOCK_SIZE;
        int i2 = 0;
        while (true) {
            i--;
            if (i < 0) {
                return i2;
            }
            int i3 = bArr[i] & GF2Field.MASK;
            bArr2[i] = (byte) (i2 | (i3 << 1));
            i2 = (i3 >>> 7) & 1;
        }
    }

    protected static void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 15; i >= 0; i--) {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
    }

    protected void clear(byte[] bArr) {
        if (bArr != null) {
            Arrays.fill(bArr, (byte) 0);
        }
    }

    public int doFinal(byte[] bArr, int i) {
        int i2;
        byte[] bArr2 = null;
        if (!this.forEncryption) {
            if (this.mainBlockPos < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            this.mainBlockPos -= this.macSize;
            bArr2 = new byte[this.macSize];
            System.arraycopy(this.mainBlock, this.mainBlockPos, bArr2, 0, this.macSize);
        }
        if (this.hashBlockPos > 0) {
            OCB_extend(this.hashBlock, this.hashBlockPos);
            updateHASH(this.L_Asterisk);
        }
        if (this.mainBlockPos > 0) {
            if (this.forEncryption) {
                OCB_extend(this.mainBlock, this.mainBlockPos);
                xor(this.Checksum, this.mainBlock);
            }
            xor(this.OffsetMAIN, this.L_Asterisk);
            byte[] bArr3 = new byte[BLOCK_SIZE];
            this.hashCipher.processBlock(this.OffsetMAIN, 0, bArr3, 0);
            xor(this.mainBlock, bArr3);
            if (bArr.length < this.mainBlockPos + i) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy(this.mainBlock, 0, bArr, i, this.mainBlockPos);
            if (!this.forEncryption) {
                OCB_extend(this.mainBlock, this.mainBlockPos);
                xor(this.Checksum, this.mainBlock);
            }
        }
        xor(this.Checksum, this.OffsetMAIN);
        xor(this.Checksum, this.L_Dollar);
        this.hashCipher.processBlock(this.Checksum, 0, this.Checksum, 0);
        xor(this.Checksum, this.Sum);
        this.macBlock = new byte[this.macSize];
        System.arraycopy(this.Checksum, 0, this.macBlock, 0, this.macSize);
        int i3 = this.mainBlockPos;
        if (this.forEncryption) {
            if (bArr.length < (i + i3) + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            System.arraycopy(this.macBlock, 0, bArr, i + i3, this.macSize);
            i2 = this.macSize + i3;
        } else if (Arrays.constantTimeAreEqual(this.macBlock, bArr2)) {
            i2 = i3;
        } else {
            throw new InvalidCipherTextException("mac check in OCB failed");
        }
        reset(false);
        return i2;
    }

    public String getAlgorithmName() {
        return this.mainCipher.getAlgorithmName() + "/OCB";
    }

    protected byte[] getLSub(int i) {
        while (i >= this.f198L.size()) {
            this.f198L.addElement(OCB_double((byte[]) this.f198L.lastElement()));
        }
        return (byte[]) this.f198L.elementAt(i);
    }

    public byte[] getMac() {
        return Arrays.clone(this.macBlock);
    }

    public int getOutputSize(int i) {
        int i2 = this.mainBlockPos + i;
        return this.forEncryption ? i2 + this.macSize : i2 < this.macSize ? 0 : i2 - this.macSize;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.mainCipher;
    }

    public int getUpdateOutputSize(int i) {
        int i2 = this.mainBlockPos + i;
        if (!this.forEncryption) {
            if (i2 < this.macSize) {
                return 0;
            }
            i2 -= this.macSize;
        }
        return i2 - (i2 % BLOCK_SIZE);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        byte[] nonce;
        int macSize;
        boolean z2 = this.forEncryption;
        this.forEncryption = z;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            macSize = aEADParameters.getMacSize();
            if (macSize < 64 || macSize > X509KeyUsage.digitalSignature || macSize % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + macSize);
            }
            this.macSize = macSize / 8;
            CipherParameters key = aEADParameters.getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            byte[] iv = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = BLOCK_SIZE;
            byte[] bArr = iv;
            Object obj = (KeyParameter) parametersWithIV.getParameters();
            nonce = bArr;
        } else {
            throw new IllegalArgumentException("invalid parameters passed to OCB");
        }
        this.hashBlock = new byte[BLOCK_SIZE];
        this.mainBlock = new byte[(z ? BLOCK_SIZE : this.macSize + BLOCK_SIZE)];
        if (nonce == null) {
            nonce = new byte[0];
        }
        if (nonce.length > 15) {
            throw new IllegalArgumentException("IV must be no more than 15 bytes");
        }
        if (key != null) {
            this.hashCipher.init(true, key);
            this.mainCipher.init(z, key);
            this.KtopInput = null;
        } else if (z2 != z) {
            throw new IllegalArgumentException("cannot change encrypting state without providing key.");
        }
        this.L_Asterisk = new byte[BLOCK_SIZE];
        this.hashCipher.processBlock(this.L_Asterisk, 0, this.L_Asterisk, 0);
        this.L_Dollar = OCB_double(this.L_Asterisk);
        this.f198L = new Vector();
        this.f198L.addElement(OCB_double(this.L_Dollar));
        int processNonce = processNonce(nonce);
        int i = processNonce % 8;
        processNonce /= 8;
        if (i == 0) {
            System.arraycopy(this.Stretch, processNonce, this.OffsetMAIN_0, 0, BLOCK_SIZE);
        } else {
            macSize = processNonce;
            for (processNonce = 0; processNonce < BLOCK_SIZE; processNonce++) {
                int i2 = this.Stretch[macSize] & GF2Field.MASK;
                macSize++;
                this.OffsetMAIN_0[processNonce] = (byte) ((i2 << i) | ((this.Stretch[macSize] & GF2Field.MASK) >>> (8 - i)));
            }
        }
        this.hashBlockPos = 0;
        this.mainBlockPos = 0;
        this.hashBlockCount = 0;
        this.mainBlockCount = 0;
        this.OffsetHASH = new byte[BLOCK_SIZE];
        this.Sum = new byte[BLOCK_SIZE];
        System.arraycopy(this.OffsetMAIN_0, 0, this.OffsetMAIN, 0, BLOCK_SIZE);
        this.Checksum = new byte[BLOCK_SIZE];
        if (this.initialAssociatedText != null) {
            processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    public void processAADByte(byte b) {
        this.hashBlock[this.hashBlockPos] = b;
        int i = this.hashBlockPos + 1;
        this.hashBlockPos = i;
        if (i == this.hashBlock.length) {
            processHashBlock();
        }
    }

    public void processAADBytes(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            this.hashBlock[this.hashBlockPos] = bArr[i + i3];
            int i4 = this.hashBlockPos + 1;
            this.hashBlockPos = i4;
            if (i4 == this.hashBlock.length) {
                processHashBlock();
            }
        }
    }

    public int processByte(byte b, byte[] bArr, int i) {
        this.mainBlock[this.mainBlockPos] = b;
        int i2 = this.mainBlockPos + 1;
        this.mainBlockPos = i2;
        if (i2 != this.mainBlock.length) {
            return 0;
        }
        processMainBlock(bArr, i);
        return BLOCK_SIZE;
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = 0;
        if (bArr.length < i + i2) {
            throw new DataLengthException("Input buffer too short");
        }
        for (int i5 = 0; i5 < i2; i5++) {
            this.mainBlock[this.mainBlockPos] = bArr[i + i5];
            int i6 = this.mainBlockPos + 1;
            this.mainBlockPos = i6;
            if (i6 == this.mainBlock.length) {
                processMainBlock(bArr2, i3 + i4);
                i4 += BLOCK_SIZE;
            }
        }
        return i4;
    }

    protected void processHashBlock() {
        long j = this.hashBlockCount + 1;
        this.hashBlockCount = j;
        updateHASH(getLSub(OCB_ntz(j)));
        this.hashBlockPos = 0;
    }

    protected void processMainBlock(byte[] bArr, int i) {
        if (bArr.length < i + BLOCK_SIZE) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.forEncryption) {
            xor(this.Checksum, this.mainBlock);
            this.mainBlockPos = 0;
        }
        byte[] bArr2 = this.OffsetMAIN;
        long j = this.mainBlockCount + 1;
        this.mainBlockCount = j;
        xor(bArr2, getLSub(OCB_ntz(j)));
        xor(this.mainBlock, this.OffsetMAIN);
        this.mainCipher.processBlock(this.mainBlock, 0, this.mainBlock, 0);
        xor(this.mainBlock, this.OffsetMAIN);
        System.arraycopy(this.mainBlock, 0, bArr, i, BLOCK_SIZE);
        if (!this.forEncryption) {
            xor(this.Checksum, this.mainBlock);
            System.arraycopy(this.mainBlock, BLOCK_SIZE, this.mainBlock, 0, this.macSize);
            this.mainBlockPos = this.macSize;
        }
    }

    protected int processNonce(byte[] bArr) {
        int i = 0;
        byte[] bArr2 = new byte[BLOCK_SIZE];
        System.arraycopy(bArr, 0, bArr2, bArr2.length - bArr.length, bArr.length);
        bArr2[0] = (byte) (this.macSize << 4);
        int length = 15 - bArr.length;
        bArr2[length] = (byte) (bArr2[length] | 1);
        length = bArr2[15] & 63;
        bArr2[15] = (byte) (bArr2[15] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256);
        if (this.KtopInput == null || !Arrays.areEqual(bArr2, this.KtopInput)) {
            Object obj = new byte[BLOCK_SIZE];
            this.KtopInput = bArr2;
            this.hashCipher.processBlock(this.KtopInput, 0, obj, 0);
            System.arraycopy(obj, 0, this.Stretch, 0, BLOCK_SIZE);
            while (i < 8) {
                this.Stretch[i + BLOCK_SIZE] = (byte) (obj[i] ^ obj[i + 1]);
                i++;
            }
        }
        return length;
    }

    public void reset() {
        reset(true);
    }

    protected void reset(boolean z) {
        this.hashCipher.reset();
        this.mainCipher.reset();
        clear(this.hashBlock);
        clear(this.mainBlock);
        this.hashBlockPos = 0;
        this.mainBlockPos = 0;
        this.hashBlockCount = 0;
        this.mainBlockCount = 0;
        clear(this.OffsetHASH);
        clear(this.Sum);
        System.arraycopy(this.OffsetMAIN_0, 0, this.OffsetMAIN, 0, BLOCK_SIZE);
        clear(this.Checksum);
        if (z) {
            this.macBlock = null;
        }
        if (this.initialAssociatedText != null) {
            processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    protected void updateHASH(byte[] bArr) {
        xor(this.OffsetHASH, bArr);
        xor(this.hashBlock, this.OffsetHASH);
        this.hashCipher.processBlock(this.hashBlock, 0, this.hashBlock, 0);
        xor(this.Sum, this.hashBlock);
    }
}
