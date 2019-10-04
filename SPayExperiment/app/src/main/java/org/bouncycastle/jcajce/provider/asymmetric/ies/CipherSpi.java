/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.PrintStream
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
 *  javax.crypto.interfaces.DHPrivateKey
 *  javax.crypto.interfaces.DHPublicKey
 */
package org.bouncycastle.jcajce.provider.asymmetric.ies;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.IESEngine;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.DHUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.IESKey;
import org.bouncycastle.jce.spec.IESParameterSpec;

public class CipherSpi
extends javax.crypto.CipherSpi {
    private Class[] availableSpecs = new Class[]{IESParameterSpec.class};
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private IESEngine cipher;
    private AlgorithmParameters engineParam = null;
    private IESParameterSpec engineParams = null;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private int state = -1;

    public CipherSpi(IESEngine iESEngine) {
        this.cipher = iESEngine;
    }

    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        if (n2 != 0) {
            this.buffer.write(arrby, n, n2);
        }
        try {
            byte[] arrby3 = this.buffer.toByteArray();
            this.buffer.reset();
            byte[] arrby4 = this.cipher.processBlock(arrby3, 0, arrby3.length);
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)n3, (int)arrby4.length);
            int n4 = arrby4.length;
            return n4;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        if (n2 != 0) {
            this.buffer.write(arrby, n, n2);
        }
        try {
            byte[] arrby2 = this.buffer.toByteArray();
            this.buffer.reset();
            byte[] arrby3 = this.cipher.processBlock(arrby2, 0, arrby2.length);
            return arrby3;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new BadPaddingException(invalidCipherTextException.getMessage());
        }
    }

    protected int engineGetBlockSize() {
        return 0;
    }

    protected byte[] engineGetIV() {
        return null;
    }

    protected int engineGetKeySize(Key key) {
        if (!(key instanceof IESKey)) {
            throw new IllegalArgumentException("must be passed IE key");
        }
        IESKey iESKey = (IESKey)key;
        if (iESKey.getPrivate() instanceof DHPrivateKey) {
            return ((DHPrivateKey)iESKey.getPrivate()).getX().bitLength();
        }
        if (iESKey.getPrivate() instanceof ECPrivateKey) {
            return ((ECPrivateKey)iESKey.getPrivate()).getD().bitLength();
        }
        throw new IllegalArgumentException("not an IE key!");
    }

    protected int engineGetOutputSize(int n) {
        if (this.state == 1 || this.state == 3) {
            return 20 + (n + this.buffer.size());
        }
        if (this.state == 2 || this.state == 4) {
            return -20 + (n + this.buffer.size());
        }
        throw new IllegalStateException("cipher not initialised");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParam != null || this.engineParams == null) return this.engineParam;
        try {
            this.engineParam = this.helper.createAlgorithmParameters("IES");
            this.engineParam.init((AlgorithmParameterSpec)this.engineParams);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParam;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        AlgorithmParameterSpec algorithmParameterSpec;
        if (algorithmParameters == null) {
            algorithmParameterSpec = null;
        } else {
            block6 : {
                for (int i = 0; i != this.availableSpecs.length; ++i) {
                    try {
                        AlgorithmParameterSpec algorithmParameterSpec2;
                        algorithmParameterSpec = algorithmParameterSpec2 = algorithmParameters.getParameterSpec(this.availableSpecs[i]);
                        break block6;
                    }
                    catch (Exception exception) {
                        continue;
                    }
                }
                algorithmParameterSpec = null;
            }
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + algorithmParameters.toString());
            }
        }
        this.engineParam = algorithmParameters;
        this.engineInit(n, key, algorithmParameterSpec, secureRandom);
    }

    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        if (n == 1 || n == 3) {
            try {
                this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
                return;
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                // empty catch block
            }
        }
        throw new IllegalArgumentException("can't handle null parameter spec in IES");
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        IESKey iESKey;
        AlgorithmParameterSpec algorithmParameterSpec2;
        AsymmetricKeyParameter asymmetricKeyParameter;
        AsymmetricKeyParameter asymmetricKeyParameter2;
        if (!(key instanceof IESKey)) {
            throw new InvalidKeyException("must be passed IES key");
        }
        if (algorithmParameterSpec == null && (n == 1 || n == 3)) {
            byte[] arrby = new byte[16];
            byte[] arrby2 = new byte[16];
            if (secureRandom == null) {
                secureRandom = new SecureRandom();
            }
            secureRandom.nextBytes(arrby);
            secureRandom.nextBytes(arrby2);
            algorithmParameterSpec2 = new IESParameterSpec(arrby, arrby2, 128);
        } else {
            if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
                throw new InvalidAlgorithmParameterException("must be passed IES parameters");
            }
            algorithmParameterSpec2 = algorithmParameterSpec;
        }
        if ((iESKey = (IESKey)key).getPublic() instanceof DHPublicKey) {
            asymmetricKeyParameter2 = DHUtil.generatePublicKeyParameter(iESKey.getPublic());
            asymmetricKeyParameter = DHUtil.generatePrivateKeyParameter(iESKey.getPrivate());
        } else {
            asymmetricKeyParameter2 = ECUtil.generatePublicKeyParameter(iESKey.getPublic());
            asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(iESKey.getPrivate());
        }
        this.engineParams = (IESParameterSpec)algorithmParameterSpec2;
        IESParameters iESParameters = new IESParameters(this.engineParams.getDerivationV(), this.engineParams.getEncodingV(), this.engineParams.getMacKeySize());
        this.state = n;
        this.buffer.reset();
        switch (n) {
            default: {
                System.out.println("eeek!");
                return;
            }
            case 1: 
            case 3: {
                this.cipher.init(true, asymmetricKeyParameter, asymmetricKeyParameter2, iESParameters);
                return;
            }
            case 2: 
            case 4: 
        }
        this.cipher.init(false, asymmetricKeyParameter, asymmetricKeyParameter2, iESParameters);
    }

    protected void engineSetMode(String string) {
        throw new IllegalArgumentException("can't support mode " + string);
    }

    protected void engineSetPadding(String string) {
        throw new NoSuchPaddingException(string + " unavailable with RSA.");
    }

    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        this.buffer.write(arrby, n, n2);
        return 0;
    }

    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        this.buffer.write(arrby, n, n2);
        return null;
    }

    public static class IES
    extends CipherSpi {
        public IES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }

}

