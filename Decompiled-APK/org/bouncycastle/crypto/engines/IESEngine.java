package org.bouncycastle.crypto.engines;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.EphemeralKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.generators.EphemeralKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Pack;

public class IESEngine {
    private byte[] IV;
    byte[] f158V;
    BasicAgreement agree;
    BufferedBlockCipher cipher;
    boolean forEncryption;
    DerivationFunction kdf;
    private EphemeralKeyPairGenerator keyPairGenerator;
    private KeyParser keyParser;
    Mac mac;
    byte[] macBuf;
    IESParameters param;
    CipherParameters privParam;
    CipherParameters pubParam;

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = null;
    }

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac, BufferedBlockCipher bufferedBlockCipher) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = bufferedBlockCipher;
    }

    private byte[] decryptBlock(byte[] bArr, int i, int i2) {
        if (i2 <= this.param.getMacKeySize() / 8) {
            throw new InvalidCipherTextException("Length of input must be greater than the MAC");
        }
        byte[] bArr2;
        byte[] bArr3;
        int i3;
        Object obj;
        if (this.cipher == null) {
            Object obj2 = new byte[((i2 - this.f158V.length) - this.mac.getMacSize())];
            bArr2 = new byte[(this.param.getMacKeySize() / 8)];
            obj = new byte[(obj2.length + bArr2.length)];
            this.kdf.generateBytes(obj, 0, obj.length);
            if (this.f158V.length != 0) {
                System.arraycopy(obj, 0, bArr2, 0, bArr2.length);
                System.arraycopy(obj, bArr2.length, obj2, 0, obj2.length);
            } else {
                System.arraycopy(obj, 0, obj2, 0, obj2.length);
                System.arraycopy(obj, obj2.length, bArr2, 0, bArr2.length);
            }
            bArr3 = new byte[obj2.length];
            for (i3 = 0; i3 != obj2.length; i3++) {
                bArr3[i3] = (byte) (bArr[(this.f158V.length + i) + i3] ^ obj2[i3]);
            }
            i3 = obj2.length;
        } else {
            obj = new byte[(((IESWithCipherParameters) this.param).getCipherKeySize() / 8)];
            Object obj3 = new byte[(this.param.getMacKeySize() / 8)];
            Object obj4 = new byte[(obj.length + obj3.length)];
            this.kdf.generateBytes(obj4, 0, obj4.length);
            System.arraycopy(obj4, 0, obj, 0, obj.length);
            System.arraycopy(obj4, obj.length, obj3, 0, obj3.length);
            if (this.IV != null) {
                this.cipher.init(false, new ParametersWithIV(new KeyParameter(obj), this.IV));
            } else {
                this.cipher.init(false, new KeyParameter(obj));
            }
            bArr3 = new byte[this.cipher.getOutputSize((i2 - this.f158V.length) - this.mac.getMacSize())];
            i3 = this.cipher.processBytes(bArr, i + this.f158V.length, (i2 - this.f158V.length) - this.mac.getMacSize(), bArr3, 0);
            i3 += this.cipher.doFinal(bArr3, i3);
            obj4 = obj3;
        }
        byte[] encodingV = this.param.getEncodingV();
        byte[] bArr4 = new byte[4];
        if (!(this.f158V.length == 0 || encodingV == null)) {
            Pack.intToBigEndian(encodingV.length * 8, bArr4, 0);
        }
        int i4 = i + i2;
        byte[] copyOfRange = Arrays.copyOfRange(bArr, i4 - this.mac.getMacSize(), i4);
        byte[] bArr5 = new byte[copyOfRange.length];
        this.mac.init(new KeyParameter(bArr2));
        this.mac.update(bArr, this.f158V.length + i, (i2 - this.f158V.length) - bArr5.length);
        if (encodingV != null) {
            this.mac.update(encodingV, 0, encodingV.length);
        }
        if (this.f158V.length != 0) {
            this.mac.update(bArr4, 0, bArr4.length);
        }
        this.mac.doFinal(bArr5, 0);
        if (Arrays.constantTimeAreEqual(copyOfRange, bArr5)) {
            return Arrays.copyOfRange(bArr3, 0, i3);
        }
        throw new InvalidCipherTextException("Invalid MAC.");
    }

    private byte[] encryptBlock(byte[] bArr, int i, int i2) {
        byte[] bArr2;
        Object obj;
        Object obj2;
        Object obj3;
        if (this.cipher == null) {
            Object obj4 = new byte[i2];
            bArr2 = new byte[(this.param.getMacKeySize() / 8)];
            obj3 = new byte[(obj4.length + bArr2.length)];
            this.kdf.generateBytes(obj3, 0, obj3.length);
            if (this.f158V.length != 0) {
                System.arraycopy(obj3, 0, bArr2, 0, bArr2.length);
                System.arraycopy(obj3, bArr2.length, obj4, 0, obj4.length);
            } else {
                System.arraycopy(obj3, 0, obj4, 0, obj4.length);
                System.arraycopy(obj3, i2, bArr2, 0, bArr2.length);
            }
            obj = new byte[i2];
            for (int i3 = 0; i3 != i2; i3++) {
                obj[i3] = (byte) (bArr[i + i3] ^ obj4[i3]);
            }
        } else {
            obj2 = new byte[(((IESWithCipherParameters) this.param).getCipherKeySize() / 8)];
            Object obj5 = new byte[(this.param.getMacKeySize() / 8)];
            obj3 = new byte[(obj2.length + obj5.length)];
            this.kdf.generateBytes(obj3, 0, obj3.length);
            System.arraycopy(obj3, 0, obj2, 0, obj2.length);
            System.arraycopy(obj3, obj2.length, obj5, 0, obj5.length);
            if (this.IV != null) {
                this.cipher.init(true, new ParametersWithIV(new KeyParameter(obj2), this.IV));
            } else {
                this.cipher.init(true, new KeyParameter(obj2));
            }
            obj = new byte[this.cipher.getOutputSize(i2)];
            int processBytes = this.cipher.processBytes(bArr, i, i2, obj, 0);
            i2 = processBytes + this.cipher.doFinal(obj, processBytes);
            bArr2 = obj5;
        }
        byte[] encodingV = this.param.getEncodingV();
        byte[] bArr3 = new byte[4];
        if (!(this.f158V.length == 0 || encodingV == null)) {
            Pack.intToBigEndian(encodingV.length * 8, bArr3, 0);
        }
        Object obj6 = new byte[this.mac.getMacSize()];
        this.mac.init(new KeyParameter(bArr2));
        this.mac.update(obj, 0, obj.length);
        if (encodingV != null) {
            this.mac.update(encodingV, 0, encodingV.length);
        }
        if (this.f158V.length != 0) {
            this.mac.update(bArr3, 0, bArr3.length);
        }
        this.mac.doFinal(obj6, 0);
        obj2 = new byte[((this.f158V.length + i2) + obj6.length)];
        System.arraycopy(this.f158V, 0, obj2, 0, this.f158V.length);
        System.arraycopy(obj, 0, obj2, this.f158V.length, i2);
        System.arraycopy(obj6, 0, obj2, this.f158V.length + i2, obj6.length);
        return obj2;
    }

    private void extractParams(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            this.IV = ((ParametersWithIV) cipherParameters).getIV();
            this.param = (IESParameters) ((ParametersWithIV) cipherParameters).getParameters();
            return;
        }
        this.IV = null;
        this.param = (IESParameters) cipherParameters;
    }

    public BufferedBlockCipher getCipher() {
        return this.cipher;
    }

    public Mac getMac() {
        return this.mac;
    }

    public void init(AsymmetricKeyParameter asymmetricKeyParameter, CipherParameters cipherParameters, KeyParser keyParser) {
        this.forEncryption = false;
        this.privParam = asymmetricKeyParameter;
        this.keyParser = keyParser;
        extractParams(cipherParameters);
    }

    public void init(AsymmetricKeyParameter asymmetricKeyParameter, CipherParameters cipherParameters, EphemeralKeyPairGenerator ephemeralKeyPairGenerator) {
        this.forEncryption = true;
        this.pubParam = asymmetricKeyParameter;
        this.keyPairGenerator = ephemeralKeyPairGenerator;
        extractParams(cipherParameters);
    }

    public void init(boolean z, CipherParameters cipherParameters, CipherParameters cipherParameters2, CipherParameters cipherParameters3) {
        this.forEncryption = z;
        this.privParam = cipherParameters;
        this.pubParam = cipherParameters2;
        this.f158V = new byte[0];
        extractParams(cipherParameters3);
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        if (this.forEncryption) {
            if (this.keyPairGenerator != null) {
                EphemeralKeyPair generate = this.keyPairGenerator.generate();
                this.privParam = generate.getKeyPair().getPrivate();
                this.f158V = generate.getEncodedPublicKey();
            }
        } else if (this.keyParser != null) {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr, i, i2);
            try {
                this.pubParam = this.keyParser.readKey(byteArrayInputStream);
                this.f158V = Arrays.copyOfRange(bArr, i, (i2 - byteArrayInputStream.available()) + i);
            } catch (Throwable e) {
                throw new InvalidCipherTextException("unable to recover ephemeral public key: " + e.getMessage(), e);
            }
        }
        this.agree.init(this.privParam);
        byte[] asUnsignedByteArray = BigIntegers.asUnsignedByteArray(this.agree.getFieldSize(), this.agree.calculateAgreement(this.pubParam));
        if (this.f158V.length != 0) {
            byte[] concatenate = Arrays.concatenate(this.f158V, asUnsignedByteArray);
            Arrays.fill(asUnsignedByteArray, (byte) 0);
            asUnsignedByteArray = concatenate;
        }
        try {
            this.kdf.init(new KDFParameters(asUnsignedByteArray, this.param.getDerivationV()));
            concatenate = this.forEncryption ? encryptBlock(bArr, i, i2) : decryptBlock(bArr, i, i2);
            Arrays.fill(asUnsignedByteArray, (byte) 0);
            return concatenate;
        } catch (Throwable th) {
            Arrays.fill(asUnsignedByteArray, (byte) 0);
        }
    }
}
