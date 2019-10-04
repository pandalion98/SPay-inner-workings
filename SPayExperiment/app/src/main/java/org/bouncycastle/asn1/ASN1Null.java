/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;

public abstract class ASN1Null
extends ASN1Primitive {
    public static ASN1Null getInstance(Object object) {
        if (object instanceof ASN1Null) {
            return (ASN1Null)object;
        }
        if (object != null) {
            try {
                ASN1Null aSN1Null = ASN1Null.getInstance(ASN1Primitive.fromByteArray((byte[])object));
                return aSN1Null;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct NULL from byte[]: " + iOException.getMessage());
            }
            catch (ClassCastException classCastException) {
                throw new IllegalArgumentException("unknown object in getInstance(): " + object.getClass().getName());
            }
        }
        return null;
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        return aSN1Primitive instanceof ASN1Null;
    }

    @Override
    abstract void encode(ASN1OutputStream var1);

    @Override
    public int hashCode() {
        return -1;
    }

    public String toString() {
        return "NULL";
    }
}

