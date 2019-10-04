/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;

public class BERTaggedObjectParser
implements ASN1TaggedObjectParser {
    private boolean _constructed;
    private ASN1StreamParser _parser;
    private int _tagNumber;

    BERTaggedObjectParser(boolean bl, int n2, ASN1StreamParser aSN1StreamParser) {
        this._constructed = bl;
        this._tagNumber = n2;
        this._parser = aSN1StreamParser;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        return this._parser.readTaggedObject(this._constructed, this._tagNumber);
    }

    @Override
    public ASN1Encodable getObjectParser(int n2, boolean bl) {
        if (bl) {
            if (!this._constructed) {
                throw new IOException("Explicit tags must be constructed (see X.690 8.14.2)");
            }
            return this._parser.readObject();
        }
        return this._parser.readImplicit(this._constructed, n2);
    }

    @Override
    public int getTagNo() {
        return this._tagNumber;
    }

    public boolean isConstructed() {
        return this._constructed;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            ASN1Primitive aSN1Primitive = this.getLoadedObject();
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException(iOException.getMessage());
        }
    }
}

