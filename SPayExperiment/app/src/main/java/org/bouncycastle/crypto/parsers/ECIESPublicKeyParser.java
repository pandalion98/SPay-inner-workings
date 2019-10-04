/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.parsers;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECIESPublicKeyParser
implements KeyParser {
    private ECDomainParameters ecParams;

    public ECIESPublicKeyParser(ECDomainParameters eCDomainParameters) {
        this.ecParams = eCDomainParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public AsymmetricKeyParameter readKey(InputStream inputStream) {
        byte[] arrby;
        int n2 = inputStream.read();
        switch (n2) {
            default: {
                throw new IOException("Sender's public key has invalid point encoding 0x" + Integer.toString((int)n2, (int)16));
            }
            case 0: {
                throw new IOException("Sender's public key invalid.");
            }
            case 2: 
            case 3: {
                arrby = new byte[1 + (7 + this.ecParams.getCurve().getFieldSize()) / 8];
                break;
            }
            case 4: 
            case 6: 
            case 7: {
                arrby = new byte[1 + 2 * ((7 + this.ecParams.getCurve().getFieldSize()) / 8)];
            }
        }
        arrby[0] = (byte)n2;
        inputStream.read(arrby, 1, -1 + arrby.length);
        return new ECPublicKeyParameters(this.ecParams.getCurve().decodePoint(arrby), this.ecParams);
    }
}

