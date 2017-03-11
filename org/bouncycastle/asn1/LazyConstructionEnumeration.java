package org.bouncycastle.asn1;

import java.util.Enumeration;

class LazyConstructionEnumeration implements Enumeration {
    private ASN1InputStream aIn;
    private Object nextObj;

    public LazyConstructionEnumeration(byte[] bArr) {
        this.aIn = new ASN1InputStream(bArr, true);
        this.nextObj = readObject();
    }

    private Object readObject() {
        try {
            return this.aIn.readObject();
        } catch (Throwable e) {
            throw new ASN1ParsingException("malformed DER construction: " + e, e);
        }
    }

    public boolean hasMoreElements() {
        return this.nextObj != null;
    }

    public Object nextElement() {
        Object obj = this.nextObj;
        this.nextObj = readObject();
        return obj;
    }
}
