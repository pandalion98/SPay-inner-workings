/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.util.Arrays;

public class ASN1Boolean
extends ASN1Primitive {
    public static final ASN1Boolean FALSE;
    private static final byte[] FALSE_VALUE;
    public static final ASN1Boolean TRUE;
    private static final byte[] TRUE_VALUE;
    private byte[] value;

    static {
        TRUE_VALUE = new byte[]{-1};
        FALSE_VALUE = new byte[]{0};
        FALSE = new ASN1Boolean(false);
        TRUE = new ASN1Boolean(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ASN1Boolean(boolean bl) {
        byte[] arrby = bl ? TRUE_VALUE : FALSE_VALUE;
        this.value = arrby;
    }

    ASN1Boolean(byte[] arrby) {
        if (arrby.length != 1) {
            throw new IllegalArgumentException("byte value should have 1 byte in it");
        }
        if (arrby[0] == 0) {
            this.value = FALSE_VALUE;
            return;
        }
        if ((255 & arrby[0]) == 255) {
            this.value = TRUE_VALUE;
            return;
        }
        this.value = Arrays.clone((byte[])arrby);
    }

    static ASN1Boolean fromOctetString(byte[] arrby) {
        if (arrby.length != 1) {
            throw new IllegalArgumentException("BOOLEAN value should have 1 byte in it");
        }
        if (arrby[0] == 0) {
            return FALSE;
        }
        if ((255 & arrby[0]) == 255) {
            return TRUE;
        }
        return new ASN1Boolean(arrby);
    }

    public static ASN1Boolean getInstance(int n2) {
        if (n2 != 0) {
            return TRUE;
        }
        return FALSE;
    }

    public static ASN1Boolean getInstance(Object object) {
        if (object == null || object instanceof ASN1Boolean) {
            return (ASN1Boolean)object;
        }
        if (object instanceof byte[]) {
            byte[] arrby = (byte[])object;
            try {
                ASN1Boolean aSN1Boolean = (ASN1Boolean)ASN1Boolean.fromByteArray(arrby);
                return aSN1Boolean;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct boolean from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Boolean getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1Boolean) {
            return ASN1Boolean.getInstance(aSN1Primitive);
        }
        return ASN1Boolean.fromOctetString(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    public static ASN1Boolean getInstance(boolean bl) {
        if (bl) {
            return TRUE;
        }
        return FALSE;
    }

    @Override
    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        boolean bl = aSN1Primitive instanceof ASN1Boolean;
        boolean bl2 = false;
        if (bl) {
            byte by = this.value[0];
            byte by2 = ((ASN1Boolean)aSN1Primitive).value[0];
            bl2 = false;
            if (by == by2) {
                bl2 = true;
            }
        }
        return bl2;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(1, this.value);
    }

    @Override
    int encodedLength() {
        return 3;
    }

    @Override
    public int hashCode() {
        return this.value[0];
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public boolean isTrue() {
        byte by = this.value[0];
        boolean bl = false;
        if (by != 0) {
            bl = true;
        }
        return bl;
    }

    public String toString() {
        if (this.value[0] != 0) {
            return "TRUE";
        }
        return "FALSE";
    }
}

