package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.jce.X509KeyUsage;

public class BEROctetString extends ASN1OctetString {
    private static final int MAX_LENGTH = 1000;
    private ASN1OctetString[] octs;

    /* renamed from: org.bouncycastle.asn1.BEROctetString.1 */
    class C07071 implements Enumeration {
        int counter;

        C07071() {
            this.counter = 0;
        }

        public boolean hasMoreElements() {
            return this.counter < BEROctetString.this.octs.length;
        }

        public Object nextElement() {
            ASN1OctetString[] access$000 = BEROctetString.this.octs;
            int i = this.counter;
            this.counter = i + 1;
            return access$000[i];
        }
    }

    public BEROctetString(byte[] bArr) {
        super(bArr);
    }

    public BEROctetString(ASN1OctetString[] aSN1OctetStringArr) {
        super(toBytes(aSN1OctetStringArr));
        this.octs = aSN1OctetStringArr;
    }

    static BEROctetString fromSequence(ASN1Sequence aSN1Sequence) {
        ASN1OctetString[] aSN1OctetStringArr = new ASN1OctetString[aSN1Sequence.size()];
        Enumeration objects = aSN1Sequence.getObjects();
        int i = 0;
        while (objects.hasMoreElements()) {
            int i2 = i + 1;
            aSN1OctetStringArr[i] = (ASN1OctetString) objects.nextElement();
            i = i2;
        }
        return new BEROctetString(aSN1OctetStringArr);
    }

    private Vector generateOcts() {
        Vector vector = new Vector();
        int i = 0;
        while (i < this.string.length) {
            byte[] bArr = new byte[((i + MAX_LENGTH > this.string.length ? this.string.length : i + MAX_LENGTH) - i)];
            System.arraycopy(this.string, i, bArr, 0, bArr.length);
            vector.addElement(new DEROctetString(bArr));
            i += MAX_LENGTH;
        }
        return vector;
    }

    private static byte[] toBytes(ASN1OctetString[] aSN1OctetStringArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != aSN1OctetStringArr.length) {
            try {
                byteArrayOutputStream.write(((DEROctetString) aSN1OctetStringArr[i]).getOctets());
                i++;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(aSN1OctetStringArr[i].getClass().getName() + " found in input should only contain DEROctetString");
            } catch (IOException e2) {
                throw new IllegalArgumentException("exception converting octets " + e2.toString());
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.write(36);
        aSN1OutputStream.write((int) X509KeyUsage.digitalSignature);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    int encodedLength() {
        Enumeration objects = getObjects();
        int i = 0;
        while (objects.hasMoreElements()) {
            i = ((ASN1Encodable) objects.nextElement()).toASN1Primitive().encodedLength() + i;
        }
        return (i + 2) + 2;
    }

    public Enumeration getObjects() {
        return this.octs == null ? generateOcts().elements() : new C07071();
    }

    public byte[] getOctets() {
        return this.string;
    }

    boolean isConstructed() {
        return true;
    }
}
