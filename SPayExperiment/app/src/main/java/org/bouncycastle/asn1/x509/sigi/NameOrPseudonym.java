/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509.sigi;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.DirectoryString;

public class NameOrPseudonym
extends ASN1Object
implements ASN1Choice {
    private ASN1Sequence givenName;
    private DirectoryString pseudonym;
    private DirectoryString surname;

    public NameOrPseudonym(String string) {
        this(new DirectoryString(string));
    }

    private NameOrPseudonym(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        if (!(aSN1Sequence.getObjectAt(0) instanceof ASN1String)) {
            throw new IllegalArgumentException("Bad object encountered: " + (Object)aSN1Sequence.getObjectAt(0).getClass());
        }
        this.surname = DirectoryString.getInstance(aSN1Sequence.getObjectAt(0));
        this.givenName = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public NameOrPseudonym(DirectoryString directoryString) {
        this.pseudonym = directoryString;
    }

    public NameOrPseudonym(DirectoryString directoryString, ASN1Sequence aSN1Sequence) {
        this.surname = directoryString;
        this.givenName = aSN1Sequence;
    }

    public static NameOrPseudonym getInstance(Object object) {
        if (object == null || object instanceof NameOrPseudonym) {
            return (NameOrPseudonym)object;
        }
        if (object instanceof ASN1String) {
            return new NameOrPseudonym(DirectoryString.getInstance(object));
        }
        if (object instanceof ASN1Sequence) {
            return new NameOrPseudonym((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public DirectoryString[] getGivenName() {
        DirectoryString[] arrdirectoryString = new DirectoryString[this.givenName.size()];
        int n2 = 0;
        Enumeration enumeration = this.givenName.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arrdirectoryString[n2] = DirectoryString.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arrdirectoryString;
    }

    public DirectoryString getPseudonym() {
        return this.pseudonym;
    }

    public DirectoryString getSurname() {
        return this.surname;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.pseudonym != null) {
            return this.pseudonym.toASN1Primitive();
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.surname);
        aSN1EncodableVector.add(this.givenName);
        return new DERSequence(aSN1EncodableVector);
    }
}

