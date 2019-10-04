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
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.DEROctetString;

public class BERConstructedOctetString
extends BEROctetString {
    private static final int MAX_LENGTH = 1000;
    private Vector octs;

    public BERConstructedOctetString(Vector vector) {
        super(BERConstructedOctetString.toBytes(vector));
        this.octs = vector;
    }

    public BERConstructedOctetString(ASN1Encodable aSN1Encodable) {
        this(aSN1Encodable.toASN1Primitive());
    }

    public BERConstructedOctetString(ASN1Primitive aSN1Primitive) {
        super(BERConstructedOctetString.toByteArray(aSN1Primitive));
    }

    public BERConstructedOctetString(byte[] arrby) {
        super(arrby);
    }

    public static BEROctetString fromSequence(ASN1Sequence aSN1Sequence) {
        Vector vector = new Vector();
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
        return new BERConstructedOctetString(vector);
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

    private static byte[] toByteArray(ASN1Primitive aSN1Primitive) {
        try {
            byte[] arrby = aSN1Primitive.getEncoded();
            return arrby;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("Unable to encode object");
        }
    }

    private static byte[] toBytes(Vector vector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 != vector.size(); ++i2) {
            try {
                byteArrayOutputStream.write(((DEROctetString)vector.elementAt(i2)).getOctets());
            }
            catch (ClassCastException classCastException) {
                throw new IllegalArgumentException(vector.elementAt(i2).getClass().getName() + " found in input should only contain DEROctetString");
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("exception converting octets " + iOException.toString());
            }
            continue;
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Enumeration getObjects() {
        if (this.octs == null) {
            return this.generateOcts().elements();
        }
        return this.octs.elements();
    }

    @Override
    public byte[] getOctets() {
        return this.string;
    }
}

