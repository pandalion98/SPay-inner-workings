/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class ASN1Enumerated
extends ASN1Primitive {
    private static ASN1Enumerated[] cache = new ASN1Enumerated[12];
    byte[] bytes;

    public ASN1Enumerated(int n2) {
        this.bytes = BigInteger.valueOf((long)n2).toByteArray();
    }

    public ASN1Enumerated(BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }

    public ASN1Enumerated(byte[] arrby) {
        this.bytes = arrby;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static ASN1Enumerated fromOctetString(byte[] arrby) {
        ASN1Enumerated aSN1Enumerated;
        if (arrby.length > 1) {
            return new ASN1Enumerated(Arrays.clone((byte[])arrby));
        }
        if (arrby.length == 0) {
            throw new IllegalArgumentException("ENUMERATED has zero length");
        }
        int n2 = 255 & arrby[0];
        if (n2 >= cache.length) {
            return new ASN1Enumerated(Arrays.clone((byte[])arrby));
        }
        ASN1Enumerated aSN1Enumerated2 = cache[n2];
        if (aSN1Enumerated2 != null) return aSN1Enumerated2;
        ASN1Enumerated[] arraSN1Enumerated = cache;
        arraSN1Enumerated[n2] = aSN1Enumerated = new ASN1Enumerated(Arrays.clone((byte[])arrby));
        return aSN1Enumerated;
    }

    public static ASN1Enumerated getInstance(Object object) {
        if (object == null || object instanceof ASN1Enumerated) {
            return (ASN1Enumerated)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1Enumerated aSN1Enumerated = (ASN1Enumerated)ASN1Enumerated.fromByteArray((byte[])object);
                return aSN1Enumerated;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Enumerated getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1Enumerated) {
            return ASN1Enumerated.getInstance(aSN1Primitive);
        }
        return ASN1Enumerated.fromOctetString(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Enumerated)) {
            return false;
        }
        ASN1Enumerated aSN1Enumerated = (ASN1Enumerated)aSN1Primitive;
        return Arrays.areEqual((byte[])this.bytes, (byte[])aSN1Enumerated.bytes);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(10, this.bytes);
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
    }

    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((byte[])this.bytes);
    }

    @Override
    boolean isConstructed() {
        return false;
    }
}

