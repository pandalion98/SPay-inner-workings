/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.parsers;

import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;

public class DHIESPublicKeyParser
implements KeyParser {
    private DHParameters dhParams;

    public DHIESPublicKeyParser(DHParameters dHParameters) {
        this.dhParams = dHParameters;
    }

    @Override
    public AsymmetricKeyParameter readKey(InputStream inputStream) {
        byte[] arrby = new byte[(7 + this.dhParams.getP().bitLength()) / 8];
        inputStream.read(arrby, 0, arrby.length);
        return new DHPublicKeyParameters(new BigInteger(1, arrby), this.dhParams);
    }
}

