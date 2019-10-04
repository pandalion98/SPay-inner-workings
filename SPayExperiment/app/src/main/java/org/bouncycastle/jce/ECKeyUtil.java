/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.KeyFactory
 *  java.security.NoSuchAlgorithmException
 *  java.security.NoSuchProviderException
 *  java.security.PrivateKey
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.security.Security
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.X509EncodedKeySpec
 */
package org.bouncycastle.jce;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECKeyUtil {
    public static PrivateKey privateToExplicitParameters(PrivateKey privateKey, String string) {
        Provider provider = Security.getProvider((String)string);
        if (provider == null) {
            throw new NoSuchProviderException("cannot find provider: " + string);
        }
        return ECKeyUtil.privateToExplicitParameters(privateKey, provider);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PrivateKey privateToExplicitParameters(PrivateKey privateKey, Provider provider) {
        try {
            X9ECParameters x9ECParameters;
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(privateKey.getEncoded()));
            if (privateKeyInfo.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                throw new UnsupportedEncodingException("cannot convert GOST key to explicit parameters.");
            }
            X962Parameters x962Parameters = X962Parameters.getInstance(privateKeyInfo.getAlgorithmId().getParameters());
            if (x962Parameters.isNamedCurve()) {
                X9ECParameters x9ECParameters2 = ECUtil.getNamedCurveByOid(ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters()));
                x9ECParameters = new X9ECParameters(x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH());
            } else {
                if (!x962Parameters.isImplicitlyCA()) return privateKey;
                x9ECParameters = new X9ECParameters(BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getG(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getN(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getH());
            }
            X962Parameters x962Parameters2 = new X962Parameters(x9ECParameters);
            PrivateKeyInfo privateKeyInfo2 = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters2), privateKeyInfo.parsePrivateKey());
            return KeyFactory.getInstance((String)privateKey.getAlgorithm(), (Provider)provider).generatePrivate((KeySpec)new PKCS8EncodedKeySpec(privateKeyInfo2.getEncoded()));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw noSuchAlgorithmException;
        }
        catch (Exception exception) {
            throw new UnexpectedException(exception);
        }
    }

    public static PublicKey publicToExplicitParameters(PublicKey publicKey, String string) {
        Provider provider = Security.getProvider((String)string);
        if (provider == null) {
            throw new NoSuchProviderException("cannot find provider: " + string);
        }
        return ECKeyUtil.publicToExplicitParameters(publicKey, provider);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PublicKey publicToExplicitParameters(PublicKey publicKey, Provider provider) {
        try {
            X9ECParameters x9ECParameters;
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded()));
            if (subjectPublicKeyInfo.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                throw new IllegalArgumentException("cannot convert GOST key to explicit parameters.");
            }
            X962Parameters x962Parameters = X962Parameters.getInstance(subjectPublicKeyInfo.getAlgorithmId().getParameters());
            if (x962Parameters.isNamedCurve()) {
                X9ECParameters x9ECParameters2 = ECUtil.getNamedCurveByOid(ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters()));
                x9ECParameters = new X9ECParameters(x9ECParameters2.getCurve(), x9ECParameters2.getG(), x9ECParameters2.getN(), x9ECParameters2.getH());
            } else {
                if (!x962Parameters.isImplicitlyCA()) return publicKey;
                x9ECParameters = new X9ECParameters(BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getG(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getN(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getH());
            }
            X962Parameters x962Parameters2 = new X962Parameters(x9ECParameters);
            SubjectPublicKeyInfo subjectPublicKeyInfo2 = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters2), subjectPublicKeyInfo.getPublicKeyData().getBytes());
            return KeyFactory.getInstance((String)publicKey.getAlgorithm(), (Provider)provider).generatePublic((KeySpec)new X509EncodedKeySpec(subjectPublicKeyInfo2.getEncoded()));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw noSuchAlgorithmException;
        }
        catch (Exception exception) {
            throw new UnexpectedException(exception);
        }
    }

    private static class UnexpectedException
    extends RuntimeException {
        private Throwable cause;

        UnexpectedException(Throwable throwable) {
            super(throwable.toString());
            this.cause = throwable;
        }

        public Throwable getCause() {
            return this.cause;
        }
    }

}

