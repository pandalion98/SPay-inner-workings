/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Constructor
 *  java.security.InvalidKeyException
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  javax.crypto.SecretKey
 *  javax.crypto.SecretKeyFactorySpi
 *  javax.crypto.spec.SecretKeySpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.lang.reflect.Constructor;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

public class BaseSecretKeyFactory
extends SecretKeyFactorySpi
implements PBE {
    protected String algName;
    protected ASN1ObjectIdentifier algOid;

    protected BaseSecretKeyFactory(String string, ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.algName = string;
        this.algOid = aSN1ObjectIdentifier;
    }

    protected SecretKey engineGenerateSecret(KeySpec keySpec) {
        if (keySpec instanceof SecretKeySpec) {
            return (SecretKey)keySpec;
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }

    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class class_) {
        if (class_ == null) {
            throw new InvalidKeySpecException("keySpec parameter is null");
        }
        if (secretKey == null) {
            throw new InvalidKeySpecException("key parameter is null");
        }
        if (SecretKeySpec.class.isAssignableFrom(class_)) {
            return new SecretKeySpec(secretKey.getEncoded(), this.algName);
        }
        try {
            Constructor constructor = class_.getConstructor(new Class[]{byte[].class});
            Object[] arrobject = new Object[]{secretKey.getEncoded()};
            KeySpec keySpec = (KeySpec)constructor.newInstance(arrobject);
            return keySpec;
        }
        catch (Exception exception) {
            throw new InvalidKeySpecException(exception.toString());
        }
    }

    protected SecretKey engineTranslateKey(SecretKey secretKey) {
        if (secretKey == null) {
            throw new InvalidKeyException("key parameter is null");
        }
        if (!secretKey.getAlgorithm().equalsIgnoreCase(this.algName)) {
            throw new InvalidKeyException("Key not of type " + this.algName + ".");
        }
        return new SecretKeySpec(secretKey.getEncoded(), this.algName);
    }
}

