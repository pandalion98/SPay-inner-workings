/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Exception;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.InMemoryRepresentable;

public class DERExternalParser
implements ASN1Encodable,
InMemoryRepresentable {
    private ASN1StreamParser _parser;

    public DERExternalParser(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        try {
            DERExternal dERExternal = new DERExternal(this._parser.readVector());
            return dERExternal;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ASN1Exception(illegalArgumentException.getMessage(), illegalArgumentException);
        }
    }

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
            throw new ASN1ParsingException("unable to get DER object", iOException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ASN1ParsingException("unable to get DER object", illegalArgumentException);
        }
    }
}

