/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.CipherSpi
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.interfaces.DHKey
 *  javax.crypto.interfaces.DHPrivateKey
 *  javax.crypto.interfaces.DHPublicKey
 *  javax.crypto.spec.DHParameterSpec
 *  org.bouncycastle.jcajce.provider.asymmetric.util.DHUtil
 *  org.bouncycastle.jcajce.provider.asymmetric.util.IESUtil
 *  org.bouncycastle.jcajce.util.BCJcaJceHelper
 *  org.bouncycastle.jcajce.util.JcaJceHelper
 *  org.bouncycastle.jce.interfaces.IESKey
 *  org.bouncycastle.jce.spec.IESParameterSpec
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
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
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyEncoder;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.Mac;
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

public class IESCipher
extends CipherSpi {
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private boolean dhaesMode = false;
    private IESEngine engine;
    private AlgorithmParameters engineParam = null;
    private IESParameterSpec engineSpec = null;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private AsymmetricKeyParameter key;
    private AsymmetricKeyParameter otherKeyParameter = null;
    private SecureRandom random;
    private int state = -1;

    public IESCipher(IESEngine iESEngine) {
        this.engine = iESEngine;
    }

    public int engineDoFinal(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        byte[] arrby3 = this.engineDoFinal(arrby, n2, n3);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n4, (int)arrby3.length);
        return arrby3.length;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] engineDoFinal(byte[] arrby, int n2, int n3) {
        if (n3 != 0) {
            this.buffer.write(arrby, n2, n3);
        }
        byte[] arrby2 = this.buffer.toByteArray();
        this.buffer.reset();
        IESWithCipherParameters iESWithCipherParameters = new IESWithCipherParameters(this.engineSpec.getDerivationV(), this.engineSpec.getEncodingV(), this.engineSpec.getMacKeySize(), this.engineSpec.getCipherKeySize());
        DHParameters dHParameters = ((DHKeyParameters)this.key).getParameters();
        if (this.otherKeyParameter != null) {
            try {
                if (this.state == 1 || this.state == 3) {
                    this.engine.init(true, this.otherKeyParameter, this.key, iESWithCipherParameters);
                    do {
                        return this.engine.processBlock(arrby2, 0, arrby2.length);
                        break;
                    } while (true);
                }
                this.engine.init(false, this.key, this.otherKeyParameter, iESWithCipherParameters);
                return this.engine.processBlock(arrby2, 0, arrby2.length);
            }
            catch (Exception exception) {
                throw new BadPaddingException(exception.getMessage());
            }
        }
        if (this.state == 1 || this.state == 3) {
            DHKeyPairGenerator dHKeyPairGenerator = new DHKeyPairGenerator();
            dHKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, dHParameters));
            EphemeralKeyPairGenerator ephemeralKeyPairGenerator = new EphemeralKeyPairGenerator(dHKeyPairGenerator, new KeyEncoder(){

                @Override
                public byte[] getEncoded(AsymmetricKeyParameter asymmetricKeyParameter) {
                    byte[] arrby = new byte[(7 + ((DHKeyParameters)asymmetricKeyParameter).getParameters().getP().bitLength()) / 8];
                    byte[] arrby2 = BigIntegers.asUnsignedByteArray((BigInteger)((DHPublicKeyParameters)asymmetricKeyParameter).getY());
                    if (arrby2.length > arrby.length) {
                        throw new IllegalArgumentException("Senders's public key longer than expected.");
                    }
                    System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(arrby.length - arrby2.length), (int)arrby2.length);
                    return arrby;
                }
            });
            try {
                this.engine.init(this.key, (CipherParameters)iESWithCipherParameters, ephemeralKeyPairGenerator);
                return this.engine.processBlock(arrby2, 0, arrby2.length);
            }
            catch (Exception exception) {
                throw new BadPaddingException(exception.getMessage());
            }
        }
        if (this.state != 2) {
            if (this.state != 4) throw new IllegalStateException("IESCipher not initialised");
        }
        try {
            this.engine.init(this.key, (CipherParameters)iESWithCipherParameters, new DHIESPublicKeyParser(((DHKeyParameters)this.key).getParameters()));
            return this.engine.processBlock(arrby2, 0, arrby2.length);
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    public int engineGetBlockSize() {
        if (this.engine.getCipher() != null) {
            return this.engine.getCipher().getBlockSize();
        }
        return 0;
    }

    public byte[] engineGetIV() {
        return null;
    }

    public int engineGetKeySize(Key key) {
        if (key instanceof DHKey) {
            return ((DHKey)key).getParams().getP().bitLength();
        }
        throw new IllegalArgumentException("not a DH key");
    }

    /*
     * Enabled aggressive block sorting
     */
    public int engineGetOutputSize(int n2) {
        int n3 = this.engine.getMac().getMacSize();
        if (this.key == null) {
            throw new IllegalStateException("cipher not initialised");
        }
        int n4 = 1 + ((DHKey)this.key).getParams().getP().bitLength() / 8;
        if (this.engine.getCipher() != null) {
            if (this.state == 1 || this.state == 3) {
                n2 = this.engine.getCipher().getOutputSize(n2);
            } else {
                if (this.state != 2 && this.state != 4) {
                    throw new IllegalStateException("cipher not initialised");
                }
                n2 = this.engine.getCipher().getOutputSize(n2 - n3 - n4);
            }
        }
        if (this.state == 1 || this.state == 3) {
            return n2 + (n4 + (n3 + this.buffer.size()));
        }
        if (this.state != 2 && this.state != 4) {
            throw new IllegalStateException("IESCipher not initialised");
        }
        return n2 + (this.buffer.size() - n3 - n4);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public AlgorithmParameters engineGetParameters() {
        if (this.engineParam != null || this.engineSpec == null) return this.engineParam;
        try {
            this.engineParam = this.helper.createAlgorithmParameters("IES");
            this.engineParam.init((AlgorithmParameterSpec)this.engineSpec);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParam;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void engineInit(int n2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec = null;
        if (algorithmParameters != null) {
            AlgorithmParameterSpec algorithmParameterSpec2;
            algorithmParameterSpec = algorithmParameterSpec2 = algorithmParameters.getParameterSpec(IESParameterSpec.class);
        }
        this.engineParam = algorithmParameters;
        this.engineInit(n2, key, algorithmParameterSpec, secureRandom);
        return;
        catch (Exception exception) {
            throw new InvalidAlgorithmParameterException("cannot recognise parameters: " + exception.toString());
        }
    }

    public void engineInit(int n2, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n2, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new IllegalArgumentException("can't handle supplied parameter spec");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineInit(int n2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec == null) {
            this.engineSpec = IESUtil.guessParameterSpec((IESEngine)this.engine);
        } else {
            if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
                throw new InvalidAlgorithmParameterException("must be passed IES parameters");
            }
            this.engineSpec = (IESParameterSpec)algorithmParameterSpec;
        }
        if (n2 == 1 || n2 == 3) {
            if (key instanceof DHPublicKey) {
                this.key = DHUtil.generatePublicKeyParameter((PublicKey)((PublicKey)key));
            } else {
                if (!(key instanceof IESKey)) {
                    throw new InvalidKeyException("must be passed recipient's public DH key for encryption");
                }
                IESKey iESKey = (IESKey)key;
                this.key = DHUtil.generatePublicKeyParameter((PublicKey)iESKey.getPublic());
                this.otherKeyParameter = DHUtil.generatePrivateKeyParameter((PrivateKey)iESKey.getPrivate());
            }
        } else {
            if (n2 != 2 && n2 != 4) {
                throw new InvalidKeyException("must be passed EC key");
            }
            if (key instanceof DHPrivateKey) {
                this.key = DHUtil.generatePrivateKeyParameter((PrivateKey)((PrivateKey)key));
            } else {
                if (!(key instanceof IESKey)) {
                    throw new InvalidKeyException("must be passed recipient's private DH key for decryption");
                }
                IESKey iESKey = (IESKey)key;
                this.otherKeyParameter = DHUtil.generatePublicKeyParameter((PublicKey)iESKey.getPublic());
                this.key = DHUtil.generatePrivateKeyParameter((PrivateKey)iESKey.getPrivate());
            }
        }
        this.random = secureRandom;
        this.state = n2;
        this.buffer.reset();
    }

    public void engineSetMode(String string) {
        String string2 = Strings.toUpperCase((String)string);
        if (string2.equals((Object)"NONE")) {
            this.dhaesMode = false;
            return;
        }
        if (string2.equals((Object)"DHAES")) {
            this.dhaesMode = true;
            return;
        }
        throw new IllegalArgumentException("can't support mode " + string);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineSetPadding(String string) {
        String string2 = Strings.toUpperCase((String)string);
        if (string2.equals((Object)"NOPADDING") || string2.equals((Object)"PKCS5PADDING") || string2.equals((Object)"PKCS7PADDING")) {
            return;
        }
        throw new NoSuchPaddingException("padding not available with IESCipher");
    }

    public int engineUpdate(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        this.buffer.write(arrby, n2, n3);
        return 0;
    }

    public byte[] engineUpdate(byte[] arrby, int n2, int n3) {
        this.buffer.write(arrby, n2, n3);
        return null;
    }

    public static class IES
    extends IESCipher {
        public IES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }

    public static class IESwithAES
    extends IESCipher {
        public IESwithAES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest()), new PaddedBufferedBlockCipher(new AESEngine())));
        }
    }

    public static class IESwithDESede
    extends IESCipher {
        public IESwithDESede() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest()), new PaddedBufferedBlockCipher(new DESedeEngine())));
        }
    }

}

