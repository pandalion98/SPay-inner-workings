/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidParameterSpecException
 *  javax.crypto.spec.IvParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.bouncycastle.util.Arrays;

public class IvAlgorithmParameters
extends BaseAlgorithmParameters {
    private byte[] iv;

    protected byte[] engineGetEncoded() {
        return this.engineGetEncoded("ASN.1");
    }

    protected byte[] engineGetEncoded(String string) {
        if (this.isASN1FormatString(string)) {
            return new DEROctetString(this.engineGetEncoded("RAW")).getEncoded();
        }
        if (string.equals((Object)"RAW")) {
            return Arrays.clone(this.iv);
        }
        return null;
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) {
        if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
            throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
        }
        this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
    }

    protected void engineInit(byte[] arrby) {
        if (arrby.length % 8 != 0 && arrby[0] == 4 && arrby[1] == -2 + arrby.length) {
            arrby = ((ASN1OctetString)ASN1Primitive.fromByteArray(arrby)).getOctets();
        }
        this.iv = Arrays.clone(arrby);
    }

    protected void engineInit(byte[] arrby, String string) {
        if (this.isASN1FormatString(string)) {
            try {
                this.engineInit(((ASN1OctetString)ASN1Primitive.fromByteArray(arrby)).getOctets());
                return;
            }
            catch (Exception exception) {
                throw new IOException("Exception decoding: " + (Object)((Object)exception));
            }
        }
        if (string.equals((Object)"RAW")) {
            this.engineInit(arrby);
            return;
        }
        throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString() {
        return "IV Parameters";
    }

    @Override
    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class class_) {
        if (class_ == IvParameterSpec.class) {
            return new IvParameterSpec(this.iv);
        }
        throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
    }
}

