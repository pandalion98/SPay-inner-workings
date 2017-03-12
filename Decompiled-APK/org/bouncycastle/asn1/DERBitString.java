package org.bouncycastle.asn1;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class DERBitString extends ASN1Primitive implements ASN1String {
    private static final char[] table;
    protected byte[] data;
    protected int padBits;

    static {
        table = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    protected DERBitString(byte b, int i) {
        this.data = new byte[1];
        this.data[0] = b;
        this.padBits = i;
    }

    public DERBitString(int i) {
        this.data = getBytes(i);
        this.padBits = getPadBits(i);
    }

    public DERBitString(ASN1Encodable aSN1Encodable) {
        this.data = aSN1Encodable.toASN1Primitive().getEncoded(ASN1Encoding.DER);
        this.padBits = 0;
    }

    public DERBitString(byte[] bArr) {
        this(bArr, 0);
    }

    public DERBitString(byte[] bArr, int i) {
        this.data = bArr;
        this.padBits = i;
    }

    static DERBitString fromInputStream(int i, InputStream inputStream) {
        if (i < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int read = inputStream.read();
        byte[] bArr = new byte[(i - 1)];
        if (bArr.length == 0 || Streams.readFully(inputStream, bArr) == bArr.length) {
            return new DERBitString(bArr, read);
        }
        throw new EOFException("EOF encountered in middle of BIT STRING");
    }

    static DERBitString fromOctetString(byte[] bArr) {
        if (bArr.length < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int i = bArr[0];
        byte[] bArr2 = new byte[(bArr.length - 1)];
        if (bArr2.length != 0) {
            System.arraycopy(bArr, 1, bArr2, 0, bArr.length - 1);
        }
        return new DERBitString(bArr2, i);
    }

    protected static byte[] getBytes(int i) {
        int i2 = 4;
        int i3 = 3;
        while (i3 >= 1 && ((GF2Field.MASK << (i3 * 8)) & i) == 0) {
            i2--;
            i3--;
        }
        byte[] bArr = new byte[i2];
        for (i3 = 0; i3 < i2; i3++) {
            bArr[i3] = (byte) ((i >> (i3 * 8)) & GF2Field.MASK);
        }
        return bArr;
    }

    public static DERBitString getInstance(Object obj) {
        if (obj == null || (obj instanceof DERBitString)) {
            return (DERBitString) obj;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERBitString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof DERBitString)) ? getInstance(object) : fromOctetString(((ASN1OctetString) object).getOctets());
    }

    protected static int getPadBits(int i) {
        int i2;
        int i3 = 0;
        for (i2 = 3; i2 >= 0; i2--) {
            if (i2 != 0) {
                if ((i >> (i2 * 8)) != 0) {
                    i3 = (i >> (i2 * 8)) & GF2Field.MASK;
                    break;
                }
            } else if (i != 0) {
                i3 = i & GF2Field.MASK;
                break;
            }
        }
        if (i3 == 0) {
            return 7;
        }
        i2 = 1;
        while (true) {
            i3 <<= 1;
            if ((i3 & GF2Field.MASK) == 0) {
                return 8 - i2;
            }
            i2++;
        }
    }

    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERBitString)) {
            return false;
        }
        DERBitString dERBitString = (DERBitString) aSN1Primitive;
        return this.padBits == dERBitString.padBits && Arrays.areEqual(this.data, dERBitString.data);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        Object obj = new byte[(getBytes().length + 1)];
        obj[0] = (byte) getPadBits();
        System.arraycopy(getBytes(), 0, obj, 1, obj.length - 1);
        aSN1OutputStream.writeEncoded(3, obj);
    }

    int encodedLength() {
        return ((StreamUtil.calculateBodyLength(this.data.length + 1) + 1) + this.data.length) + 1;
    }

    public byte[] getBytes() {
        return this.data;
    }

    public int getPadBits() {
        return this.padBits;
    }

    public String getString() {
        StringBuffer stringBuffer = new StringBuffer("#");
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            for (int i = 0; i != toByteArray.length; i++) {
                stringBuffer.append(table[(toByteArray[i] >>> 4) & 15]);
                stringBuffer.append(table[toByteArray[i] & 15]);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            throw new RuntimeException("internal error encoding BitString");
        }
    }

    public int hashCode() {
        return this.padBits ^ Arrays.hashCode(this.data);
    }

    public int intValue() {
        int i = 0;
        int i2 = 0;
        while (i != this.data.length && i != 4) {
            i2 |= (this.data[i] & GF2Field.MASK) << (i * 8);
            i++;
        }
        return i2;
    }

    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return getString();
    }
}
