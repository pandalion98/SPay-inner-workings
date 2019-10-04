/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassCastException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;

public class BEROctetString
extends ASN1OctetString {
    private static final int MAX_LENGTH = 1000;
    private ASN1OctetString[] octs;

    public BEROctetString(byte[] arrby) {
        super(arrby);
    }

    public BEROctetString(ASN1OctetString[] arraSN1OctetString) {
        super(BEROctetString.toBytes(arraSN1OctetString));
        this.octs = arraSN1OctetString;
    }

    static BEROctetString fromSequence(ASN1Sequence aSN1Sequence) {
        ASN1OctetString[] arraSN1OctetString = new ASN1OctetString[aSN1Sequence.size()];
        Enumeration enumeration = aSN1Sequence.getObjects();
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arraSN1OctetString[n2] = (ASN1OctetString)enumeration.nextElement();
            n2 = n3;
        }
        return new BEROctetString(arraSN1OctetString);
    }

    /*
     * Enabled aggressive block sorting
     */
    private Vector generateOcts() {
        Vector vector = new Vector();
        int n2 = 0;
        while (n2 < this.string.length) {
            int n3 = n2 + 1000 > this.string.length ? this.string.length : n2 + 1000;
            byte[] arrby = new byte[n3 - n2];
            System.arraycopy((Object)this.string, (int)n2, (Object)arrby, (int)0, (int)arrby.length);
            vector.addElement((Object)new DEROctetString(arrby));
            n2 += 1000;
        }
        return vector;
    }

    private static byte[] toBytes(ASN1OctetString[] arraSN1OctetString) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 != arraSN1OctetString.length; ++i2) {
            try {
                byteArrayOutputStream.write(((DEROctetString)arraSN1OctetString[i2]).getOctets());
            }
            catch (ClassCastException classCastException) {
                throw new IllegalArgumentException(arraSN1OctetString[i2].getClass().getName() + " found in input should only contain DEROctetString");
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("exception converting octets " + iOException.toString());
            }
            continue;
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.write(36);
        aSN1OutputStream.write(128);
        Enumeration enumeration = this.getObjects();
        while (enumeration.hasMoreElements()) {
            aSN1OutputStream.writeObject((ASN1Encodable)enumeration.nextElement());
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    @Override
    int encodedLength() {
        Enumeration enumeration = this.getObjects();
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            n2 += ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().encodedLength();
        }
        return 2 + (n2 + 2);
    }

    public Enumeration getObjects() {
        if (this.octs == null) {
            return this.generateOcts().elements();
        }
        return new Enumeration(){
            int counter = 0;

            public boolean hasMoreElements() {
                return this.counter < BEROctetString.this.octs.length;
            }

            public Object nextElement() {
                ASN1OctetString[] arraSN1OctetString = BEROctetString.this.octs;
                int n2 = this.counter;
                this.counter = n2 + 1;
                return arraSN1OctetString[n2];
            }
        };
    }

    @Override
    public byte[] getOctets() {
        return this.string;
    }

    @Override
    boolean isConstructed() {
        return true;
    }

}

