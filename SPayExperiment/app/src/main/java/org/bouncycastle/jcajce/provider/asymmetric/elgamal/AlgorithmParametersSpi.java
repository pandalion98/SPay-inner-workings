/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public class AlgorithmParametersSpi
extends BaseAlgorithmParameters {
    ElGamalParameterSpec currentSpec;

    protected byte[] engineGetEncoded() {
        ElGamalParameter elGamalParameter = new ElGamalParameter(this.currentSpec.getP(), this.currentSpec.getG());
        try {
            byte[] arrby = elGamalParameter.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error encoding ElGamalParameters");
        }
    }

    protected byte[] engineGetEncoded(String string) {
        if (this.isASN1FormatString(string) || string.equalsIgnoreCase("X.509")) {
            return this.engineGetEncoded();
        }
        return null;
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
        if (!(algorithmParameterSpec instanceof ElGamalParameterSpec) && !(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidParameterSpecException("DHParameterSpec required to initialise a ElGamal algorithm parameters object");
        }
        if (algorithmParameterSpec instanceof ElGamalParameterSpec) {
            this.currentSpec = (ElGamalParameterSpec)algorithmParameterSpec;
            return;
        }
        DHParameterSpec dHParameterSpec = (DHParameterSpec)algorithmParameterSpec;
        this.currentSpec = new ElGamalParameterSpec(dHParameterSpec.getP(), dHParameterSpec.getG());
    }

    protected void engineInit(byte[] arrby) {
        try {
            ElGamalParameter elGamalParameter = ElGamalParameter.getInstance(ASN1Primitive.fromByteArray(arrby));
            this.currentSpec = new ElGamalParameterSpec(elGamalParameter.getP(), elGamalParameter.getG());
            return;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("Not a valid ElGamal Parameter encoding.");
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IOException("Not a valid ElGamal Parameter encoding.");
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
        return "ElGamal Parameters";
    }

    @Override
    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == ElGamalParameterSpec.class) {
            return this.currentSpec;
        }
        if (class_ == DHParameterSpec.class) {
            return new DHParameterSpec(this.currentSpec.getP(), this.currentSpec.getG());
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
    }
}

