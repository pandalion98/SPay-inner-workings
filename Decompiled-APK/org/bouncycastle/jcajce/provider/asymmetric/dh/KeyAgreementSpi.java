package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;

public class KeyAgreementSpi extends javax.crypto.KeyAgreementSpi {
    private static final Hashtable algorithms;
    private BigInteger f271g;
    private BigInteger f272p;
    private BigInteger result;
    private BigInteger f273x;

    static {
        algorithms = new Hashtable();
        Integer valueOf = Integers.valueOf(64);
        Integer valueOf2 = Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256);
        Integer valueOf3 = Integers.valueOf(X509KeyUsage.digitalSignature);
        Integer valueOf4 = Integers.valueOf(SkeinMac.SKEIN_256);
        algorithms.put("DES", valueOf);
        algorithms.put("DESEDE", valueOf2);
        algorithms.put("BLOWFISH", valueOf3);
        algorithms.put("AES", valueOf4);
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        int bitLength = (this.f272p.bitLength() + 7) / 8;
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray.length == bitLength) {
            return toByteArray;
        }
        if (toByteArray[0] == null && toByteArray.length == bitLength + 1) {
            Object obj = new byte[(toByteArray.length - 1)];
            System.arraycopy(toByteArray, 1, obj, 0, obj.length);
            return obj;
        }
        obj = new byte[bitLength];
        System.arraycopy(toByteArray, 0, obj, obj.length - toByteArray.length, toByteArray.length);
        return obj;
    }

    protected Key engineDoPhase(Key key, boolean z) {
        if (this.f273x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        } else if (key instanceof DHPublicKey) {
            DHPublicKey dHPublicKey = (DHPublicKey) key;
            if (!dHPublicKey.getParams().getG().equals(this.f271g) || !dHPublicKey.getParams().getP().equals(this.f272p)) {
                throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
            } else if (z) {
                this.result = ((DHPublicKey) key).getY().modPow(this.f273x, this.f272p);
                return null;
            } else {
                this.result = ((DHPublicKey) key).getY().modPow(this.f273x, this.f272p);
                return new BCDHPublicKey(this.result, dHPublicKey.getParams());
            }
        } else {
            throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
        }
    }

    protected int engineGenerateSecret(byte[] bArr, int i) {
        if (this.f273x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        Object bigIntToBytes = bigIntToBytes(this.result);
        if (bArr.length - i < bigIntToBytes.length) {
            throw new ShortBufferException("DHKeyAgreement - buffer too short");
        }
        System.arraycopy(bigIntToBytes, 0, bArr, i, bigIntToBytes.length);
        return bigIntToBytes.length;
    }

    protected SecretKey engineGenerateSecret(String str) {
        if (this.f273x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        String toUpperCase = Strings.toUpperCase(str);
        Object bigIntToBytes = bigIntToBytes(this.result);
        if (!algorithms.containsKey(toUpperCase)) {
            return new SecretKeySpec(bigIntToBytes, str);
        }
        Object obj = new byte[(((Integer) algorithms.get(toUpperCase)).intValue() / 8)];
        System.arraycopy(bigIntToBytes, 0, obj, 0, obj.length);
        if (toUpperCase.startsWith("DES")) {
            DESParameters.setOddParity(obj);
        }
        return new SecretKeySpec(obj, str);
    }

    protected byte[] engineGenerateSecret() {
        if (this.f273x != null) {
            return bigIntToBytes(this.result);
        }
        throw new IllegalStateException("Diffie-Hellman not initialised.");
    }

    protected void engineInit(Key key, SecureRandom secureRandom) {
        if (key instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey) key;
            this.f272p = dHPrivateKey.getParams().getP();
            this.f271g = dHPrivateKey.getParams().getG();
            BigInteger x = dHPrivateKey.getX();
            this.result = x;
            this.f273x = x;
            return;
        }
        throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
    }

    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (key instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey) key;
            if (algorithmParameterSpec == null) {
                this.f272p = dHPrivateKey.getParams().getP();
                this.f271g = dHPrivateKey.getParams().getG();
            } else if (algorithmParameterSpec instanceof DHParameterSpec) {
                DHParameterSpec dHParameterSpec = (DHParameterSpec) algorithmParameterSpec;
                this.f272p = dHParameterSpec.getP();
                this.f271g = dHParameterSpec.getG();
            } else {
                throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
            }
            BigInteger x = dHPrivateKey.getX();
            this.result = x;
            this.f273x = x;
            return;
        }
        throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
    }
}
