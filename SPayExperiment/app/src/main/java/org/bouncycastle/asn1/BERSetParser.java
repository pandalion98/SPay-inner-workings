/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.BERSet;

public class BERSetParser
implements ASN1SetParser {
    private ASN1StreamParser _parser;

    BERSetParser(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        return new BERSet(this._parser.readVector());
    }

    @Override
    public ASN1Encodable readObject() {
        return this._parser.readObject();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            ASN1Primitive aSN1Primitive = this.getLoadedObject();
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException(iOException.getMessage(), iOException);
        }
    }
}

