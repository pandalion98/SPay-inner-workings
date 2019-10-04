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
 *  java.security.spec.DSAParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.DSAParameter;

public class AlgorithmParametersSpi
extends java.security.AlgorithmParametersSpi {
    DSAParameterSpec currentSpec;

    protected byte[] engineGetEncoded() {
        DSAParameter dSAParameter = new DSAParameter(this.currentSpec.getP(), this.currentSpec.getQ(), this.currentSpec.getG());
        try {
            byte[] arrby = dSAParameter.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error encoding DSAParameters");
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
        if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
            throw new InvalidParameterSpecException("DSAParameterSpec required to initialise a DSA algorithm parameters object");
        }
        this.currentSpec = (DSAParameterSpec)algorithmParameterSpec;
    }

    protected void engineInit(byte[] arrby) {
        try {
            DSAParameter dSAParameter = DSAParameter.getInstance(ASN1Primitive.fromByteArray(arrby));
            this.currentSpec = new DSAParameterSpec(dSAParameter.getP(), dSAParameter.getQ(), dSAParameter.getG());
            return;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("Not a valid DSA Parameter encoding.");
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IOException("Not a valid DSA Parameter encoding.");
        }
    }

    protected void engineInit(byte[] arrby, String string) {
        if (this.isASN1FormatString(string) || string.equalsIgnoreCase("X.509")) {
            this.engineInit(arrby);
            return;
        }
        throw new IOException("Unknown parameter format " + string);
    }

    protected String engineToString() {
        return "DSA Parameters";
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == DSAParameterSpec.class) {
            return this.currentSpec;
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to DSA parameters object.");
    }
}

