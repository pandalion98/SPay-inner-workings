/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.NoSuchPaddingException
 *  javax.crypto.SecretKey
 *  javax.crypto.ShortBufferException
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 *  javax.crypto.spec.RC2ParameterSpec
 *  javax.crypto.spec.RC5ParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseWrapCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

public class BaseStreamCipher
extends BaseWrapCipher
implements PBE {
    private Class[] availableSpecs = new Class[]{RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class};
    private StreamCipher cipher;
    private int ivLength = 0;
    private ParametersWithIV ivParam;
    private String pbeAlgorithm = null;
    private PBEParameterSpec pbeSpec = null;

    protected BaseStreamCipher(StreamCipher streamCipher, int n) {
        this.cipher = streamCipher;
        this.ivLength = n;
    }

    @Override
    protected int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        if (n2 != 0) {
            this.cipher.processBytes(arrby, n, n2, arrby2, n3);
        }
        this.cipher.reset();
        return n2;
    }

    @Override
    protected byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        if (n2 != 0) {
            byte[] arrby2 = this.engineUpdate(arrby, n, n2);
            this.cipher.reset();
            return arrby2;
        }
        this.cipher.reset();
        return new byte[0];
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override
    protected byte[] engineGetIV() {
        if (this.ivParam != null) {
            return this.ivParam.getIV();
        }
        return null;
    }

    @Override
    protected int engineGetKeySize(Key key) {
        return 8 * key.getEncoded().length;
    }

    @Override
    protected int engineGetOutputSize(int n) {
        return n;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.pbeSpec != null) {
            try {
                AlgorithmParameters algorithmParameters = this.createParametersInstance(this.pbeAlgorithm);
                algorithmParameters.init((AlgorithmParameterSpec)this.pbeSpec);
                return algorithmParameters;
            }
            catch (Exception exception) {
                return null;
            }
        }
        return this.engineParams;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
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
        this.engineInit(n, key, algorithmParameterSpec, secureRandom);
        this.engineParams = algorithmParameters;
    }

    @Override
    protected void engineInit(int n, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidKeyException(invalidAlgorithmParameterException.getMessage());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        CipherParameters cipherParameters;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
        }
        if (key instanceof BCPBEKey) {
            CipherParameters cipherParameters2;
            BCPBEKey bCPBEKey = (BCPBEKey)key;
            this.pbeAlgorithm = bCPBEKey.getOID() != null ? bCPBEKey.getOID().getId() : bCPBEKey.getAlgorithm();
            if (bCPBEKey.getParam() != null) {
                cipherParameters2 = bCPBEKey.getParam();
                this.pbeSpec = new PBEParameterSpec(bCPBEKey.getSalt(), bCPBEKey.getIterationCount());
            } else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                cipherParameters2 = PBE.Util.makePBEParameters(bCPBEKey, algorithmParameterSpec, this.cipher.getAlgorithmName());
                this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
            }
            if (bCPBEKey.getIvSize() != 0) {
                this.ivParam = (ParametersWithIV)cipherParameters2;
            }
            cipherParameters = cipherParameters2;
        } else if (algorithmParameterSpec == null) {
            cipherParameters = new KeyParameter(key.getEncoded());
        } else {
            ParametersWithIV parametersWithIV;
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) throw new InvalidAlgorithmParameterException("unknown parameter type.");
            this.ivParam = parametersWithIV = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
            cipherParameters = parametersWithIV;
        }
        if (this.ivLength != 0 && !(cipherParameters instanceof ParametersWithIV)) {
            ParametersWithIV parametersWithIV;
            if (secureRandom == null) {
                secureRandom = new SecureRandom();
            }
            if (n != 1) {
                if (n != 3) throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
            byte[] arrby = new byte[this.ivLength];
            secureRandom.nextBytes(arrby);
            this.ivParam = parametersWithIV = new ParametersWithIV(cipherParameters, arrby);
            cipherParameters = parametersWithIV;
        }
        switch (n) {
            default: {
                throw new InvalidParameterException("unknown opmode " + n + " passed");
            }
            case 1: 
            case 3: {
                try {
                    this.cipher.init(true, cipherParameters);
                    return;
                }
                catch (Exception exception) {
                    throw new InvalidKeyException(exception.getMessage());
                }
            }
            case 2: 
            case 4: 
        }
        this.cipher.init(false, cipherParameters);
    }

    @Override
    protected void engineSetMode(String string) {
        if (!string.equalsIgnoreCase("ECB")) {
            throw new IllegalArgumentException("can't support mode " + string);
        }
    }

    @Override
    protected void engineSetPadding(String string) {
        if (!string.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException("Padding " + string + " unknown.");
        }
    }

    @Override
    protected int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        try {
            this.cipher.processBytes(arrby, n, n2, arrby2, n3);
            return n2;
        }
        catch (DataLengthException dataLengthException) {
            throw new ShortBufferException(dataLengthException.getMessage());
        }
    }

    @Override
    protected byte[] engineUpdate(byte[] arrby, int n, int n2) {
        byte[] arrby2 = new byte[n2];
        this.cipher.processBytes(arrby, n, n2, arrby2, 0);
        return arrby2;
    }
}

