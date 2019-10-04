/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.AlgorithmParametersSpi
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.asn1.pkcs.DHParameter;

public class AlgorithmParametersSpi
extends java.security.AlgorithmParametersSpi {
    DHParameterSpec currentSpec;

    protected byte[] engineGetEncoded() {
        DHParameter dHParameter = new DHParameter(this.currentSpec.getP(), this.currentSpec.getG(), this.currentSpec.getL());
        try {
            byte[] arrby = dHParameter.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error encoding DHParameters");
        }
    }

    protected byte[] engineGetEncoded(String string) {
        if (this.isASN1FormatString(string)) {
            return this.engineGetEncoded();
        }
        return null;
    }

    protected AlgorithmParameterSpec engineGetParameterSpec(Class class_) {
        if (class_ == null) {
            throw new NullPointerException("argument to getParameterSpec must not be null");
        }
        return this.localEngineGetParameterSpec(class_);
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
        if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidParameterSpecException("DHParameterSpec required to initialise a Diffie-Hellman algorithm parameters object");
        }
        this.currentSpec = (DHParameterSpec)algorithmParameterSpec;
    }

    protected void engineInit(byte[] arrby) {
        try {
            DHParameter dHParameter = DHParameter.getInstance(arrby);
            if (dHParameter.getL() != null) {
                this.currentSpec = new DHParameterSpec(dHParameter.getP(), dHParameter.getG(), dHParameter.getL().intValue());
                return;
            }
            this.currentSpec = new DHParameterSpec(dHParameter.getP(), dHParameter.getG());
            return;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("Not a valid DH Parameter encoding.");
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IOException("Not a valid DH Parameter encoding.");
        }
    }

    protected void engineInit(byte[] arrby, String string) {
        if (this.isASN1FormatString(string)) {
            this.engineInit(arrby);
            return;
        }
        throw new IOException("Unknown parameter format " + string);
    }

    protected String engineToString() {
        return "Diffie-Hellman Parameters";
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == DHParameterSpec.class) {
            return this.currentSpec;
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to DH parameters object.");
    }
}

