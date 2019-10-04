/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.asn1.eac;

import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.eac.BidirectionalMap;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.util.Integers;

public class CertificateHolderAuthorization
extends ASN1Object {
    static BidirectionalMap AuthorizationRole;
    public static final int CVCA = 192;
    public static final int DV_DOMESTIC = 128;
    public static final int DV_FOREIGN = 64;
    public static final int IS = 0;
    public static final int RADG3 = 1;
    public static final int RADG4 = 2;
    static Hashtable ReverseMap;
    static Hashtable RightsDecodeMap;
    public static final ASN1ObjectIdentifier id_role_EAC;
    DERApplicationSpecific accessRights;
    ASN1ObjectIdentifier oid;

    static {
        id_role_EAC = EACObjectIdentifiers.bsi_de.branch("3.1.2.1");
        RightsDecodeMap = new Hashtable();
        AuthorizationRole = new BidirectionalMap();
        ReverseMap = new Hashtable();
        RightsDecodeMap.put((Object)Integers.valueOf((int)2), (Object)"RADG4");
        RightsDecodeMap.put((Object)Integers.valueOf((int)1), (Object)"RADG3");
        AuthorizationRole.put((Object)Integers.valueOf((int)192), "CVCA");
        AuthorizationRole.put((Object)Integers.valueOf((int)128), "DV_DOMESTIC");
        AuthorizationRole.put((Object)Integers.valueOf((int)64), "DV_FOREIGN");
        AuthorizationRole.put((Object)Integers.valueOf((int)0), "IS");
    }

    public CertificateHolderAuthorization(ASN1ObjectIdentifier aSN1ObjectIdentifier, int n2) {
        this.setOid(aSN1ObjectIdentifier);
        this.setAccessRights((byte)n2);
    }

    public CertificateHolderAuthorization(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 76) {
            this.setPrivateData(new ASN1InputStream(dERApplicationSpecific.getContents()));
        }
    }

    public static int GetFlag(String string) {
        Integer n2 = (Integer)AuthorizationRole.getReverse(string);
        if (n2 == null) {
            throw new IllegalArgumentException("Unknown value " + string);
        }
        return n2;
    }

    public static String GetRoleDescription(int n2) {
        return (String)AuthorizationRole.get((Object)Integers.valueOf((int)n2));
    }

    private void setAccessRights(byte by) {
        byte[] arrby = new byte[]{by};
        this.accessRights = new DERApplicationSpecific(EACTags.getTag(83), arrby);
    }

    private void setOid(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.oid = aSN1ObjectIdentifier;
    }

    private void setPrivateData(ASN1InputStream aSN1InputStream) {
        ASN1Primitive aSN1Primitive = aSN1InputStream.readObject();
        if (aSN1Primitive instanceof ASN1ObjectIdentifier) {
            this.oid = (ASN1ObjectIdentifier)aSN1Primitive;
            ASN1Primitive aSN1Primitive2 = aSN1InputStream.readObject();
            if (aSN1Primitive2 instanceof DERApplicationSpecific) {
                this.accessRights = (DERApplicationSpecific)aSN1Primitive2;
                return;
            }
        } else {
            throw new IllegalArgumentException("no Oid in CerticateHolderAuthorization");
        }
        throw new IllegalArgumentException("No access rights in CerticateHolderAuthorization");
    }

    public int getAccessRights() {
        return 255 & this.accessRights.getContents()[0];
    }

    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.oid);
        aSN1EncodableVector.add(this.accessRights);
        return new DERApplicationSpecific(76, aSN1EncodableVector);
    }
}

