/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.ConstructedOctetStream;
import org.bouncycastle.util.io.Streams;

public class BEROctetStringParser
implements ASN1OctetStringParser {
    private ASN1StreamParser _parser;

    BEROctetStringParser(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        return new BEROctetString(Streams.readAll((InputStream)this.getOctetStream()));
    }

    @Override
    public InputStream getOctetStream() {
        return new ConstructedOctetStream(this._parser);
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

