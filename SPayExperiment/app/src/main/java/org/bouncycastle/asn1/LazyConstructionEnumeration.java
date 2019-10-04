/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;

class LazyConstructionEnumeration
implements Enumeration {
    private ASN1InputStream aIn;
    private Object nextObj;

    public LazyConstructionEnumeration(byte[] arrby) {
        this.aIn = new ASN1InputStream(arrby, true);
        this.nextObj = this.readObject();
    }

    private Object readObject() {
        try {
            ASN1Primitive aSN1Primitive = this.aIn.readObject();
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException("malformed DER construction: " + (Object)((Object)iOException), iOException);
        }
    }

    public boolean hasMoreElements() {
        return this.nextObj != null;
    }

    public Object nextElement() {
        Object object = this.nextObj;
        this.nextObj = this.readObject();
        return object;
    }
}

