package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyEncoder;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.IESEngine;
import org.bouncycastle.crypto.generators.DHKeyPairGenerator;
import org.bouncycastle.crypto.generators.EphemeralKeyPairGenerator;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHKeyParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.parsers.DHIESPublicKeyParser;
import org.bouncycastle.jcajce.provider.asymmetric.util.DHUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.IESUtil;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.interfaces.IESKey;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Strings;

public class IESCipher extends CipherSpi {
    private ByteArrayOutputStream buffer;
    private boolean dhaesMode;
    private IESEngine engine;
    private AlgorithmParameters engineParam;
    private IESParameterSpec engineSpec;
    private final JcaJceHelper helper;
    private AsymmetricKeyParameter key;
    private AsymmetricKeyParameter otherKeyParameter;
    private SecureRandom random;
    private int state;

    /* renamed from: org.bouncycastle.jcajce.provider.asymmetric.dh.IESCipher.1 */
    class C07551 implements KeyEncoder {
        C07551() {
        }

        public byte[] getEncoded(AsymmetricKeyParameter asymmetricKeyParameter) {
            Object obj = new byte[((((DHKeyParameters) asymmetricKeyParameter).getParameters().getP().bitLength() + 7) / 8)];
            Object asUnsignedByteArray = BigIntegers.asUnsignedByteArray(((DHPublicKeyParameters) asymmetricKeyParameter).getY());
            if (asUnsignedByteArray.length > obj.length) {
                throw new IllegalArgumentException("Senders's public key longer than expected.");
            }
            System.arraycopy(asUnsignedByteArray, 0, obj, obj.length - asUnsignedByteArray.length, asUnsignedByteArray.length);
            return obj;
        }
    }

    public static class IES extends IESCipher {
        public IES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }

    public static class IESwithAES extends IESCipher {
        public IESwithAES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest()), new PaddedBufferedBlockCipher(new AESEngine())));
        }
    }

    public static class IESwithDESede extends IESCipher {
        public IESwithDESede() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest()), new PaddedBufferedBlockCipher(new DESedeEngine())));
        }
    }

    public IESCipher(IESEngine iESEngine) {
        this.helper = new BCJcaJceHelper();
        this.state = -1;
        this.buffer = new ByteArrayOutputStream();
        this.engineParam = null;
        this.engineSpec = null;
        this.dhaesMode = false;
        this.otherKeyParameter = null;
        this.engine = iESEngine;
    }

    public int engineDoFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        Object engineDoFinal = engineDoFinal(bArr, i, i2);
        System.arraycopy(engineDoFinal, 0, bArr2, i3, engineDoFinal.length);
        return engineDoFinal.length;
    }

    public byte[] engineDoFinal(byte[] bArr, int i, int i2) {
        if (i2 != 0) {
            this.buffer.write(bArr, i, i2);
        }
        byte[] toByteArray = this.buffer.toByteArray();
        this.buffer.reset();
        CipherParameters iESWithCipherParameters = new IESWithCipherParameters(this.engineSpec.getDerivationV(), this.engineSpec.getEncodingV(), this.engineSpec.getMacKeySize(), this.engineSpec.getCipherKeySize());
        DHParameters parameters = ((DHKeyParameters) this.key).getParameters();
        if (this.otherKeyParameter != null) {
            try {
                if (this.state == 1 || this.state == 3) {
                    this.engine.init(true, this.otherKeyParameter, this.key, iESWithCipherParameters);
                } else {
                    this.engine.init(false, this.key, this.otherKeyParameter, iESWithCipherParameters);
                }
                return this.engine.processBlock(toByteArray, 0, toByteArray.length);
            } catch (Exception e) {
                throw new BadPaddingException(e.getMessage());
            }
        } else if (this.state == 1 || this.state == 3) {
            AsymmetricCipherKeyPairGenerator dHKeyPairGenerator = new DHKeyPairGenerator();
            dHKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, parameters));
            try {
                this.engine.init(this.key, iESWithCipherParameters, new EphemeralKeyPairGenerator(dHKeyPairGenerator, new C07551()));
                return this.engine.processBlock(toByteArray, 0, toByteArray.length);
            } catch (Exception e2) {
                throw new BadPaddingException(e2.getMessage());
            }
        } else if (this.state == 2 || this.state == 4) {
            try {
                this.engine.init(this.key, iESWithCipherParameters, new DHIESPublicKeyParser(((DHKeyParameters) this.key).getParameters()));
                return this.engine.processBlock(toByteArray, 0, toByteArray.length);
            } catch (InvalidCipherTextException e3) {
                throw new BadPaddingException(e3.getMessage());
            }
        } else {
            throw new IllegalStateException("IESCipher not initialised");
        }
    }

    public int engineGetBlockSize() {
        return this.engine.getCipher() != null ? this.engine.getCipher().getBlockSize() : 0;
    }

    public byte[] engineGetIV() {
        return null;
    }

    public int engineGetKeySize(Key key) {
        if (key instanceof DHKey) {
            return ((DHKey) key).getParams().getP().bitLength();
        }
        throw new IllegalArgumentException("not a DH key");
    }

    public int engineGetOutputSize(int i) {
        int macSize = this.engine.getMac().getMacSize();
        if (this.key != null) {
            int bitLength = (((DHKey) this.key).getParams().getP().bitLength() / 8) + 1;
            if (this.engine.getCipher() != null) {
                if (this.state == 1 || this.state == 3) {
                    i = this.engine.getCipher().getOutputSize(i);
                } else if (this.state == 2 || this.state == 4) {
                    i = this.engine.getCipher().getOutputSize((i - macSize) - bitLength);
                } else {
                    throw new IllegalStateException("cipher not initialised");
                }
            }
            if (this.state == 1 || this.state == 3) {
                return (bitLength + (macSize + this.buffer.size())) + i;
            }
            if (this.state == 2 || this.state == 4) {
                return ((this.buffer.size() - macSize) - bitLength) + i;
            }
            throw new IllegalStateException("IESCipher not initialised");
        }
        throw new IllegalStateException("cipher not initialised");
    }

    public AlgorithmParameters engineGetParameters() {
        if (this.engineParam == null && this.engineSpec != null) {
            try {
                this.engineParam = this.helper.createAlgorithmParameters("IES");
                this.engineParam.init(this.engineSpec);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
        return this.engineParam;
    }

    public void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec = null;
        if (algorithmParameters != null) {
            try {
                algorithmParameterSpec = algorithmParameters.getParameterSpec(IESParameterSpec.class);
            } catch (Exception e) {
                throw new InvalidAlgorithmParameterException("cannot recognise parameters: " + e.toString());
            }
        }
        this.engineParam = algorithmParameters;
        engineInit(i, key, algorithmParameterSpec, secureRandom);
    }

    public void engineInit(int i, Key key, SecureRandom secureRandom) {
        try {
            engineInit(i, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("can't handle supplied parameter spec");
        }
    }

    public void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec == null) {
            this.engineSpec = IESUtil.guessParameterSpec(this.engine);
        } else if (algorithmParameterSpec instanceof IESParameterSpec) {
            this.engineSpec = (IESParameterSpec) algorithmParameterSpec;
        } else {
            throw new InvalidAlgorithmParameterException("must be passed IES parameters");
        }
        IESKey iESKey;
        if (i == 1 || i == 3) {
            if (key instanceof DHPublicKey) {
                this.key = DHUtil.generatePublicKeyParameter((PublicKey) key);
            } else if (key instanceof IESKey) {
                iESKey = (IESKey) key;
                this.key = DHUtil.generatePublicKeyParameter(iESKey.getPublic());
                this.otherKeyParameter = DHUtil.generatePrivateKeyParameter(iESKey.getPrivate());
            } else {
                throw new InvalidKeyException("must be passed recipient's public DH key for encryption");
            }
        } else if (i != 2 && i != 4) {
            throw new InvalidKeyException("must be passed EC key");
        } else if (key instanceof DHPrivateKey) {
            this.key = DHUtil.generatePrivateKeyParameter((PrivateKey) key);
        } else if (key instanceof IESKey) {
            iESKey = (IESKey) key;
            this.otherKeyParameter = DHUtil.generatePublicKeyParameter(iESKey.getPublic());
            this.key = DHUtil.generatePrivateKeyParameter(iESKey.getPrivate());
        } else {
            throw new InvalidKeyException("must be passed recipient's private DH key for decryption");
        }
        this.random = secureRandom;
        this.state = i;
        this.buffer.reset();
    }

    public void engineSetMode(String str) {
        String toUpperCase = Strings.toUpperCase(str);
        if (toUpperCase.equals("NONE")) {
            this.dhaesMode = false;
        } else if (toUpperCase.equals("DHAES")) {
            this.dhaesMode = true;
        } else {
            throw new IllegalArgumentException("can't support mode " + str);
        }
    }

    public void engineSetPadding(String str) {
        String toUpperCase = Strings.toUpperCase(str);
        if (!toUpperCase.equals("NOPADDING") && !toUpperCase.equals("PKCS5PADDING") && !toUpperCase.equals("PKCS7PADDING")) {
            throw new NoSuchPaddingException("padding not available with IESCipher");
        }
    }

    public int engineUpdate(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        this.buffer.write(bArr, i, i2);
        return 0;
    }

    public byte[] engineUpdate(byte[] bArr, int i, int i2) {
        this.buffer.write(bArr, i, i2);
        return null;
    }
}
