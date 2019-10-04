/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.util.Hashtable
 *  javax.crypto.KeyAgreementSpi
 *  javax.crypto.SecretKey
 *  javax.crypto.ShortBufferException
 *  javax.crypto.interfaces.DHPrivateKey
 *  javax.crypto.interfaces.DHPublicKey
 *  javax.crypto.spec.DHParameterSpec
 *  javax.crypto.spec.SecretKeySpec
 *  org.bouncycastle.util.Integers
 *  org.bouncycastle.util.Strings
 */
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
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.jcajce.provider.asymmetric.dh.BCDHPublicKey;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;

public class KeyAgreementSpi
extends javax.crypto.KeyAgreementSpi {
    private static final Hashtable algorithms = new Hashtable();
    private BigInteger g;
    private BigInteger p;
    private BigInteger result;
    private BigInteger x;

    static {
        Integer n2 = Integers.valueOf((int)64);
        Integer n3 = Integers.valueOf((int)192);
        Integer n4 = Integers.valueOf((int)128);
        Integer n5 = Integers.valueOf((int)256);
        algorithms.put((Object)"DES", (Object)n2);
        algorithms.put((Object)"DESEDE", (Object)n3);
        algorithms.put((Object)"BLOWFISH", (Object)n4);
        algorithms.put((Object)"AES", (Object)n5);
    }

    private byte[] bigIntToBytes(BigInteger bigInteger) {
        int n2 = (7 + this.p.bitLength()) / 8;
        byte[] arrby = bigInteger.toByteArray();
        if (arrby.length == n2) {
            return arrby;
        }
        if (arrby[0] == 0 && arrby.length == n2 + 1) {
            byte[] arrby2 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
            return arrby2;
        }
        byte[] arrby3 = new byte[n2];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)(arrby3.length - arrby.length), (int)arrby.length);
        return arrby3;
    }

    protected Key engineDoPhase(Key key, boolean bl) {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        if (!(key instanceof DHPublicKey)) {
            throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
        }
        DHPublicKey dHPublicKey = (DHPublicKey)key;
        if (!dHPublicKey.getParams().getG().equals((Object)this.g) || !dHPublicKey.getParams().getP().equals((Object)this.p)) {
            throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
        }
        if (bl) {
            this.result = ((DHPublicKey)key).getY().modPow(this.x, this.p);
            return null;
        }
        this.result = ((DHPublicKey)key).getY().modPow(this.x, this.p);
        return new BCDHPublicKey(this.result, dHPublicKey.getParams());
    }

    protected int engineGenerateSecret(byte[] arrby, int n2) {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        byte[] arrby2 = this.bigIntToBytes(this.result);
        if (arrby.length - n2 < arrby2.length) {
            throw new ShortBufferException("DHKeyAgreement - buffer too short");
        }
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        return arrby2.length;
    }

    protected SecretKey engineGenerateSecret(String string) {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        String string2 = Strings.toUpperCase((String)string);
        byte[] arrby = this.bigIntToBytes(this.result);
        if (algorithms.containsKey((Object)string2)) {
            byte[] arrby2 = new byte[(Integer)algorithms.get((Object)string2) / 8];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
            if (string2.startsWith("DES")) {
                DESParameters.setOddParity(arrby2);
            }
            return new SecretKeySpec(arrby2, string);
        }
        return new SecretKeySpec(arrby, string);
    }

    protected byte[] engineGenerateSecret() {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        return this.bigIntToBytes(this.result);
    }

    protected void engineInit(Key key, SecureRandom secureRandom) {
        BigInteger bigInteger;
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
        }
        DHPrivateKey dHPrivateKey = (DHPrivateKey)key;
        this.p = dHPrivateKey.getParams().getP();
        this.g = dHPrivateKey.getParams().getG();
        this.result = bigInteger = dHPrivateKey.getX();
        this.x = bigInteger;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        BigInteger bigInteger;
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
        }
        DHPrivateKey dHPrivateKey = (DHPrivateKey)key;
        if (algorithmParameterSpec != null) {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
            }
            DHParameterSpec dHParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            this.p = dHParameterSpec.getP();
            this.g = dHParameterSpec.getG();
        } else {
            this.p = dHPrivateKey.getParams().getP();
            this.g = dHPrivateKey.getParams().getG();
        }
        this.result = bigInteger = dHPrivateKey.getX();
        this.x = bigInteger;
    }
}

