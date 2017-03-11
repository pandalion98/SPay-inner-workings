package org.bouncycastle.asn1;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.io.IOException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class ASN1Boolean extends ASN1Primitive {
    public static final ASN1Boolean FALSE;
    private static final byte[] FALSE_VALUE;
    public static final ASN1Boolean TRUE;
    private static final byte[] TRUE_VALUE;
    private byte[] value;

    static {
        TRUE_VALUE = new byte[]{(byte) -1};
        FALSE_VALUE = new byte[]{(byte) 0};
        FALSE = new ASN1Boolean(false);
        TRUE = new ASN1Boolean(true);
    }

    public ASN1Boolean(boolean z) {
        this.value = z ? TRUE_VALUE : FALSE_VALUE;
    }

    ASN1Boolean(byte[] bArr) {
        if (bArr.length != 1) {
            throw new IllegalArgumentException("byte value should have 1 byte in it");
        } else if (bArr[0] == null) {
            this.value = FALSE_VALUE;
        } else if ((bArr[0] & GF2Field.MASK) == GF2Field.MASK) {
            this.value = TRUE_VALUE;
        } else {
            this.value = Arrays.clone(bArr);
        }
    }

    static ASN1Boolean fromOctetString(byte[] bArr) {
        if (bArr.length == 1) {
            return bArr[0] == null ? FALSE : (bArr[0] & GF2Field.MASK) == GF2Field.MASK ? TRUE : new ASN1Boolean(bArr);
        } else {
            throw new IllegalArgumentException("BOOLEAN value should have 1 byte in it");
        }
    }

    public static ASN1Boolean getInstance(int i) {
        return i != 0 ? TRUE : FALSE;
    }

    public static ASN1Boolean getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Boolean)) {
            return (ASN1Boolean) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (ASN1Boolean) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct boolean from byte[]: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Boolean getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        Object object = aSN1TaggedObject.getObject();
        return (z || (object instanceof ASN1Boolean)) ? getInstance(object) : fromOctetString(((ASN1OctetString) object).getOctets());
    }

    public static ASN1Boolean getInstance(boolean z) {
        return z ? TRUE : FALSE;
    }

    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        return (aSN1Primitive instanceof ASN1Boolean) && this.value[0] == ((ASN1Boolean) aSN1Primitive).value[0];
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(1, this.value);
    }

    int encodedLength() {
        return 3;
    }

    public int hashCode() {
        return this.value[0];
    }

    boolean isConstructed() {
        return false;
    }

    public boolean isTrue() {
        return this.value[0] != null;
    }

    public String toString() {
        return this.value[0] != null ? PaymentFramework.CONFIG_VALUE_TRUE : PaymentFramework.CONFIG_VALUE_FALSE;
    }
}
