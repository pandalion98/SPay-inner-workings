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
 *  java.security.AlgorithmParametersSpi
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class AlgorithmParametersSpi
extends java.security.AlgorithmParametersSpi {
    GOST3410ParameterSpec currentSpec;

    protected byte[] engineGetEncoded() {
        GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.currentSpec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.currentSpec.getDigestParamSetOID()), new ASN1ObjectIdentifier(this.currentSpec.getEncryptionParamSetOID()));
        try {
            byte[] arrby = gOST3410PublicKeyAlgParameters.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error encoding GOST3410Parameters");
        }
    }

    protected byte[] engineGetEncoded(String string) {
        if (this.isASN1FormatString(string) || string.equalsIgnoreCase("X.509")) {
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
        if (!(algorithmParameterSpec instanceof GOST3410ParameterSpec)) {
            throw new InvalidParameterSpecException("GOST3410ParameterSpec required to initialise a GOST3410 algorithm parameters object");
        }
        this.currentSpec = (GOST3410ParameterSpec)algorithmParameterSpec;
    }

    protected void engineInit(byte[] arrby) {
        try {
            this.currentSpec = GOST3410ParameterSpec.fromPublicKeyAlg(new GOST3410PublicKeyAlgParameters((ASN1Sequence)ASN1Primitive.fromByteArray(arrby)));
            return;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("Not a valid GOST3410 Parameter encoding.");
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IOException("Not a valid GOST3410 Parameter encoding.");
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
        return "GOST3410 Parameters";
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == GOST3410PublicKeyParameterSetSpec.class) {
            return this.currentSpec;
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to GOST3410 parameters object.");
    }
}

