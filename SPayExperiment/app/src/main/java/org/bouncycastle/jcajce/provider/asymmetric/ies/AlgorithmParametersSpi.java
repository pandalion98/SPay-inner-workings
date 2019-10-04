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
 */
package org.bouncycastle.jcajce.provider.asymmetric.ies;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.jce.spec.IESParameterSpec;

public class AlgorithmParametersSpi
extends java.security.AlgorithmParametersSpi {
    IESParameterSpec currentSpec;

    protected byte[] engineGetEncoded() {
        try {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(new DEROctetString(this.currentSpec.getDerivationV()));
            aSN1EncodableVector.add(new DEROctetString(this.currentSpec.getEncodingV()));
            aSN1EncodableVector.add(new ASN1Integer(this.currentSpec.getMacKeySize()));
            byte[] arrby = new DERSequence(aSN1EncodableVector).getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error encoding IESParameters");
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
        if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
            throw new InvalidParameterSpecException("IESParameterSpec required to initialise a IES algorithm parameters object");
        }
        this.currentSpec = (IESParameterSpec)algorithmParameterSpec;
    }

    protected void engineInit(byte[] arrby) {
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(arrby);
            this.currentSpec = new IESParameterSpec(((ASN1OctetString)aSN1Sequence.getObjectAt(0)).getOctets(), ((ASN1OctetString)aSN1Sequence.getObjectAt(0)).getOctets(), ((ASN1Integer)aSN1Sequence.getObjectAt(0)).getValue().intValue());
            return;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("Not a valid IES Parameter encoding.");
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IOException("Not a valid IES Parameter encoding.");
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
        return "IES Parameters";
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == IESParameterSpec.class) {
            return this.currentSpec;
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
    }
}

