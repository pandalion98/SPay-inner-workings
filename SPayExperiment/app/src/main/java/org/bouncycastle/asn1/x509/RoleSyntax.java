/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class RoleSyntax
extends ASN1Object {
    private GeneralNames roleAuthority;
    private GeneralName roleName;

    public RoleSyntax(String string) {
        if (string == null) {
            string = "";
        }
        this(new GeneralName(6, string));
    }

    /*
     * Enabled aggressive block sorting
     */
    private RoleSyntax(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        int n2 = 0;
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("Unknown tag in RoleSyntax");
                }
                case 0: {
                    this.roleAuthority = GeneralNames.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 1: {
                    this.roleName = GeneralName.getInstance(aSN1TaggedObject, true);
                }
            }
            ++n2;
        }
        return;
    }

    public RoleSyntax(GeneralName generalName) {
        this(null, generalName);
    }

    public RoleSyntax(GeneralNames generalNames, GeneralName generalName) {
        if (generalName == null || generalName.getTagNo() != 6 || ((ASN1String)((Object)generalName.getName())).getString().equals((Object)"")) {
            throw new IllegalArgumentException("the role name MUST be non empty and MUST use the URI option of GeneralName");
        }
        this.roleAuthority = generalNames;
        this.roleName = generalName;
    }

    public static RoleSyntax getInstance(Object object) {
        if (object instanceof RoleSyntax) {
            return (RoleSyntax)object;
        }
        if (object != null) {
            return new RoleSyntax(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public GeneralNames getRoleAuthority() {
        return this.roleAuthority;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String[] getRoleAuthorityAsString() {
        if (this.roleAuthority == null) {
            return new String[0];
        }
        GeneralName[] arrgeneralName = this.roleAuthority.getNames();
        String[] arrstring = new String[arrgeneralName.length];
        int n2 = 0;
        while (n2 < arrgeneralName.length) {
            ASN1Encodable aSN1Encodable = arrgeneralName[n2].getName();
            arrstring[n2] = aSN1Encodable instanceof ASN1String ? ((ASN1String)((Object)aSN1Encodable)).getString() : aSN1Encodable.toString();
            ++n2;
        }
        return arrstring;
    }

    public GeneralName getRoleName() {
        return this.roleName;
    }

    public String getRoleNameAsString() {
        return ((ASN1String)((Object)this.roleName.getName())).getString();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.roleAuthority != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.roleAuthority));
        }
        aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.roleName));
        return new DERSequence(aSN1EncodableVector);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Name: " + this.getRoleNameAsString() + " - Auth: ");
        if (this.roleAuthority == null || this.roleAuthority.getNames().length == 0) {
            stringBuffer.append("N/A");
            do {
                return stringBuffer.toString();
                break;
            } while (true);
        }
        String[] arrstring = this.getRoleAuthorityAsString();
        stringBuffer.append('[').append(arrstring[0]);
        for (int i2 = 1; i2 < arrstring.length; ++i2) {
            stringBuffer.append(", ").append(arrstring[i2]);
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }
}

