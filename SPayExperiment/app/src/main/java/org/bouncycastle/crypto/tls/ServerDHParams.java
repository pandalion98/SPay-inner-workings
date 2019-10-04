/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.tls.TlsDHUtils;

public class ServerDHParams {
    protected DHPublicKeyParameters publicKey;

    public ServerDHParams(DHPublicKeyParameters dHPublicKeyParameters) {
        if (dHPublicKeyParameters == null) {
            throw new IllegalArgumentException("'publicKey' cannot be null");
        }
        this.publicKey = dHPublicKeyParameters;
    }

    public static ServerDHParams parse(InputStream inputStream) {
        BigInteger bigInteger = TlsDHUtils.readDHParameter(inputStream);
        BigInteger bigInteger2 = TlsDHUtils.readDHParameter(inputStream);
        return new ServerDHParams(TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), new DHParameters(bigInteger, bigInteger2))));
    }

    public void encode(OutputStream outputStream) {
        DHParameters dHParameters = this.publicKey.getParameters();
        BigInteger bigInteger = this.publicKey.getY();
        TlsDHUtils.writeDHParameter(dHParameters.getP(), outputStream);
        TlsDHUtils.writeDHParameter(dHParameters.getG(), outputStream);
        TlsDHUtils.writeDHParameter(bigInteger, outputStream);
    }

    public DHPublicKeyParameters getPublicKey() {
        return this.publicKey;
    }
}

