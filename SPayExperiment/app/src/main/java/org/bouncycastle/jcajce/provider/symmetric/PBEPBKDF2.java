/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.InvalidParameterSpecException
 *  java.security.spec.KeySpec
 *  javax.crypto.SecretKey
 *  javax.crypto.spec.PBEKeySpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseSecretKeyFactory;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
import org.bouncycastle.jcajce.spec.PBKDF2KeySpec;

public class PBEPBKDF2 {
    private PBEPBKDF2() {
    }

    public static class AlgParams
    extends BaseAlgorithmParameters {
        PBKDF2Params params;

        protected byte[] engineGetEncoded() {
            try {
                byte[] arrby = this.params.getEncoded("DER");
                return arrby;
            }
            catch (IOException iOException) {
                throw new RuntimeException("Oooops! " + iOException.toString());
            }
        }

        protected byte[] engineGetEncoded(String string) {
            if (this.isASN1FormatString(string)) {
                return this.engineGetEncoded();
            }
            return null;
        }

        protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PBKDF2 PBE parameters algorithm parameters object");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            this.params = new PBKDF2Params(pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
        }

        protected void engineInit(byte[] arrby) {
            this.params = PBKDF2Params.getInstance(ASN1Primitive.fromByteArray(arrby));
        }

        protected void engineInit(byte[] arrby, String string) {
            if (this.isASN1FormatString(string)) {
                this.engineInit(arrby);
                return;
            }
            throw new IOException("Unknown parameters format in PBKDF2 parameters object");
        }

        protected String engineToString() {
            return "PBKDF2 Parameters";
        }

        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
            if (class_ == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getSalt(), this.params.getIterationCount().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PBKDF2 PBE parameters object.");
        }
    }

    public static class BasePBKDF2
    extends BaseSecretKeyFactory {
        private int scheme;

        public BasePBKDF2(String string, int n) {
            super(string, PKCSObjectIdentifiers.id_PBKDF2);
            this.scheme = n;
        }

        private int getDigestCode(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
            if (aSN1ObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3411Hmac)) {
                return 6;
            }
            if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.id_hmacWithSHA1)) {
                return 1;
            }
            throw new InvalidKeySpecException("Invalid KeySpec: unknown PRF algorithm " + aSN1ObjectIdentifier);
        }

        @Override
        protected SecretKey engineGenerateSecret(KeySpec keySpec) {
            if (keySpec instanceof PBEKeySpec) {
                PBEKeySpec pBEKeySpec = (PBEKeySpec)keySpec;
                if (pBEKeySpec.getSalt() == null) {
                    throw new InvalidKeySpecException("missing required salt");
                }
                if (pBEKeySpec.getIterationCount() <= 0) {
                    throw new InvalidKeySpecException("positive iteration count required: " + pBEKeySpec.getIterationCount());
                }
                if (pBEKeySpec.getKeyLength() <= 0) {
                    throw new InvalidKeySpecException("positive key length required: " + pBEKeySpec.getKeyLength());
                }
                if (pBEKeySpec.getPassword().length == 0) {
                    throw new IllegalArgumentException("password empty");
                }
                if (pBEKeySpec instanceof PBKDF2KeySpec) {
                    int n = this.getDigestCode(((PBKDF2KeySpec)pBEKeySpec).getPrf().getAlgorithm());
                    int n2 = pBEKeySpec.getKeyLength();
                    CipherParameters cipherParameters = PBE.Util.makePBEMacParameters(pBEKeySpec, this.scheme, n, n2);
                    return new BCPBEKey(this.algName, this.algOid, this.scheme, n, n2, -1, pBEKeySpec, cipherParameters);
                }
                int n = pBEKeySpec.getKeyLength();
                CipherParameters cipherParameters = PBE.Util.makePBEMacParameters(pBEKeySpec, this.scheme, 1, n);
                return new BCPBEKey(this.algName, this.algOid, this.scheme, 1, n, -1, pBEKeySpec, cipherParameters);
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }

    public static class Mappings
    extends AlgorithmProvider {
        private static final String PREFIX = PBEPBKDF2.class.getName();

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.PBKDF2", PREFIX + "$AlgParams");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters." + PKCSObjectIdentifiers.id_PBKDF2, "PBKDF2");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2", PREFIX + "$PBKDF2withUTF8");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory." + PKCSObjectIdentifiers.id_PBKDF2, "PBKDF2");
        }
    }

    public static class PBKDF2withUTF8
    extends BasePBKDF2 {
        public PBKDF2withUTF8() {
            super("PBKDF2", 5);
        }
    }

}

