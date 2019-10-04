/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class DERApplicationSpecific
extends ASN1Primitive {
    private final boolean isConstructed;
    private final byte[] octets;
    private final int tag;

    public DERApplicationSpecific(int n2, ASN1Encodable aSN1Encodable) {
        this(true, n2, aSN1Encodable);
    }

    public DERApplicationSpecific(int n2, ASN1EncodableVector aSN1EncodableVector) {
        this.tag = n2;
        this.isConstructed = true;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 != aSN1EncodableVector.size(); ++i2) {
            try {
                byteArrayOutputStream.write(((ASN1Object)aSN1EncodableVector.get(i2)).getEncoded("DER"));
            }
            catch (IOException iOException) {
                throw new ASN1ParsingException("malformed object: " + (Object)((Object)iOException), iOException);
            }
        }
        this.octets = byteArrayOutputStream.toByteArray();
    }

    public DERApplicationSpecific(int n2, byte[] arrby) {
        this(false, n2, arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    public DERApplicationSpecific(boolean bl, int n2, ASN1Encodable aSN1Encodable) {
        ASN1Primitive aSN1Primitive = aSN1Encodable.toASN1Primitive();
        byte[] arrby = aSN1Primitive.getEncoded("DER");
        boolean bl2 = bl || aSN1Primitive instanceof ASN1Set || aSN1Primitive instanceof ASN1Sequence;
        this.isConstructed = bl2;
        this.tag = n2;
        if (bl) {
            this.octets = arrby;
            return;
        }
        int n3 = this.getLengthOfHeader(arrby);
        byte[] arrby2 = new byte[arrby.length - n3];
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)arrby2.length);
        this.octets = arrby2;
    }

    DERApplicationSpecific(boolean bl, int n2, byte[] arrby) {
        this.isConstructed = bl;
        this.tag = n2;
        this.octets = arrby;
    }

    public static DERApplicationSpecific getInstance(Object object) {
        if (object == null || object instanceof DERApplicationSpecific) {
            return (DERApplicationSpecific)object;
        }
        if (object instanceof byte[]) {
            try {
                DERApplicationSpecific dERApplicationSpecific = DERApplicationSpecific.getInstance(ASN1Primitive.fromByteArray((byte[])object));
                return dERApplicationSpecific;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct object from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getLengthOfHeader(byte[] arrby) {
        int n2 = 255 & arrby[1];
        if (n2 == 128 || n2 <= 127) {
            return 2;
        }
        int n3 = n2 & 127;
        if (n3 > 4) {
            throw new IllegalStateException("DER length more than 4 bytes: " + n3);
        }
        return n3 + 2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] replaceTagNumber(int n2, byte[] arrby) {
        int n3;
        if ((31 & arrby[0]) == 31) {
            int n4 = 255 & arrby[1];
            if ((n4 & 127) == 0) {
                throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
            }
            int n5 = n4;
            n3 = 2;
            int n6 = 0;
            while (n5 >= 0 && (n5 & 128) != 0) {
                n6 = (n6 | n5 & 127) << 7;
                int n7 = n3 + 1;
                int n8 = 255 & arrby[n3];
                n3 = n7;
                n5 = n8;
            }
            n6 | n5 & 127;
        } else {
            n3 = 1;
        }
        byte[] arrby2 = new byte[1 + (arrby.length - n3)];
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)1, (int)(-1 + arrby2.length));
        arrby2[0] = (byte)n2;
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        block3 : {
            block2 : {
                if (!(aSN1Primitive instanceof DERApplicationSpecific)) break block2;
                DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific)aSN1Primitive;
                if (this.isConstructed == dERApplicationSpecific.isConstructed && this.tag == dERApplicationSpecific.tag && Arrays.areEqual((byte[])this.octets, (byte[])dERApplicationSpecific.octets)) break block3;
            }
            return false;
        }
        return true;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        int n2 = 64;
        if (this.isConstructed) {
            n2 = 96;
        }
        aSN1OutputStream.writeEncoded(n2, this.tag, this.octets);
    }

    @Override
    int encodedLength() {
        return StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length) + this.octets.length;
    }

    public int getApplicationTag() {
        return this.tag;
    }

    public byte[] getContents() {
        return this.octets;
    }

    public ASN1Primitive getObject() {
        return new ASN1InputStream(this.getContents()).readObject();
    }

    public ASN1Primitive getObject(int n2) {
        if (n2 >= 31) {
            throw new IOException("unsupported tag number");
        }
        byte[] arrby = this.getEncoded();
        byte[] arrby2 = this.replaceTagNumber(n2, arrby);
        if ((32 & arrby[0]) != 0) {
            arrby2[0] = (byte)(32 | arrby2[0]);
        }
        return new ASN1InputStream(arrby2).readObject();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int hashCode() {
        int n2;
        if (this.isConstructed) {
            n2 = 1;
            do {
                return n2 ^ this.tag ^ Arrays.hashCode((byte[])this.octets);
                break;
            } while (true);
        }
        n2 = 0;
        return n2 ^ this.tag ^ Arrays.hashCode((byte[])this.octets);
    }

    @Override
    public boolean isConstructed() {
        return this.isConstructed;
    }
}

