/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class DERBitString
extends ASN1Primitive
implements ASN1String {
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected byte[] data;
    protected int padBits;

    protected DERBitString(byte by, int n2) {
        this.data = new byte[1];
        this.data[0] = by;
        this.padBits = n2;
    }

    public DERBitString(int n2) {
        this.data = DERBitString.getBytes(n2);
        this.padBits = DERBitString.getPadBits(n2);
    }

    public DERBitString(ASN1Encodable aSN1Encodable) {
        this.data = aSN1Encodable.toASN1Primitive().getEncoded("DER");
        this.padBits = 0;
    }

    public DERBitString(byte[] arrby) {
        this(arrby, 0);
    }

    public DERBitString(byte[] arrby, int n2) {
        this.data = arrby;
        this.padBits = n2;
    }

    static DERBitString fromInputStream(int n2, InputStream inputStream) {
        if (n2 < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int n3 = inputStream.read();
        byte[] arrby = new byte[n2 - 1];
        if (arrby.length != 0 && Streams.readFully((InputStream)inputStream, (byte[])arrby) != arrby.length) {
            throw new EOFException("EOF encountered in middle of BIT STRING");
        }
        return new DERBitString(arrby, n3);
    }

    static DERBitString fromOctetString(byte[] arrby) {
        if (arrby.length < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        byte by = arrby[0];
        byte[] arrby2 = new byte[-1 + arrby.length];
        if (arrby2.length != 0) {
            System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)(-1 + arrby.length));
        }
        return new DERBitString(arrby2, (int)by);
    }

    protected static byte[] getBytes(int n2) {
        byte[] arrby;
        int n3 = 4;
        int n4 = 3;
        do {
            if (n4 < 1 || (n2 & 255 << n4 * 8) != 0) {
                arrby = new byte[n3];
                for (int i2 = 0; i2 < n3; ++i2) {
                    arrby[i2] = (byte)(255 & n2 >> i2 * 8);
                }
                break;
            }
            --n3;
            --n4;
        } while (true);
        return arrby;
    }

    public static DERBitString getInstance(Object object) {
        if (object == null || object instanceof DERBitString) {
            return (DERBitString)object;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERBitString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERBitString) {
            return DERBitString.getInstance(aSN1Primitive);
        }
        return DERBitString.fromOctetString(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    /*
     * Enabled aggressive block sorting
     */
    protected static int getPadBits(int n2) {
        int n3 = 3;
        do {
            block8 : {
                int n4;
                block6 : {
                    block7 : {
                        n4 = 0;
                        if (n3 < 0) break block6;
                        if (n3 == 0) break block7;
                        if (n2 >> n3 * 8 == 0) break block8;
                        n4 = 255 & n2 >> n3 * 8;
                        break block6;
                    }
                    if (n2 == 0) break block8;
                    n4 = n2 & 255;
                }
                if (n4 != 0) break;
                return 7;
            }
            --n3;
        } while (true);
        int n5 = 1;
        while (((n4 <<= 1) & 255) != 0) {
            ++n5;
        }
        return 8 - n5;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        block3 : {
            block2 : {
                if (!(aSN1Primitive instanceof DERBitString)) break block2;
                DERBitString dERBitString = (DERBitString)aSN1Primitive;
                if (this.padBits == dERBitString.padBits && Arrays.areEqual((byte[])this.data, (byte[])dERBitString.data)) break block3;
            }
            return false;
        }
        return true;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        byte[] arrby = new byte[1 + this.getBytes().length];
        arrby[0] = (byte)this.getPadBits();
        System.arraycopy((Object)this.getBytes(), (int)0, (Object)arrby, (int)1, (int)(-1 + arrby.length));
        aSN1OutputStream.writeEncoded(3, arrby);
    }

    @Override
    int encodedLength() {
        return 1 + (1 + StreamUtil.calculateBodyLength(1 + this.data.length) + this.data.length);
    }

    public byte[] getBytes() {
        return this.data;
    }

    public int getPadBits() {
        return this.padBits;
    }

    @Override
    public String getString() {
        StringBuffer stringBuffer;
        stringBuffer = new StringBuffer("#");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ASN1OutputStream aSN1OutputStream = new ASN1OutputStream((OutputStream)byteArrayOutputStream);
        try {
            aSN1OutputStream.writeObject(this);
        }
        catch (IOException iOException) {
            throw new RuntimeException("internal error encoding BitString");
        }
        byte[] arrby = byteArrayOutputStream.toByteArray();
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            stringBuffer.append(table[15 & arrby[i2] >>> 4]);
            stringBuffer.append(table[15 & arrby[i2]]);
        }
        return stringBuffer.toString();
    }

    @Override
    public int hashCode() {
        return this.padBits ^ Arrays.hashCode((byte[])this.data);
    }

    public int intValue() {
        int n2 = 0;
        for (int i2 = 0; i2 != this.data.length && i2 != 4; ++i2) {
            n2 |= (255 & this.data[i2]) << i2 * 8;
        }
        return n2;
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return this.getString();
    }
}

