/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DefiniteLengthInputStream;

public class DEROctetStringParser
implements ASN1OctetStringParser {
    private DefiniteLengthInputStream stream;

    DEROctetStringParser(DefiniteLengthInputStream definiteLengthInputStream) {
        this.stream = definiteLengthInputStream;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        return new DEROctetString(this.stream.toByteArray());
    }

    @Override
    public InputStream getOctetStream() {
        return this.stream;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            ASN1Primitive aSN1Primitive = this.getLoadedObject();
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException("IOException converting stream to byte array: " + iOException.getMessage(), iOException);
        }
    }
}

