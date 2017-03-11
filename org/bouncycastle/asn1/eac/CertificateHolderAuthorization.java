package org.bouncycastle.asn1.eac;

import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Integers;

public class CertificateHolderAuthorization extends ASN1Object {
    static BidirectionalMap AuthorizationRole = null;
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
        RightsDecodeMap.put(Integers.valueOf(RADG4), "RADG4");
        RightsDecodeMap.put(Integers.valueOf(RADG3), "RADG3");
        AuthorizationRole.put(Integers.valueOf(CVCA), "CVCA");
        AuthorizationRole.put(Integers.valueOf(DV_DOMESTIC), "DV_DOMESTIC");
        AuthorizationRole.put(Integers.valueOf(DV_FOREIGN), "DV_FOREIGN");
        AuthorizationRole.put(Integers.valueOf(IS), "IS");
    }

    public CertificateHolderAuthorization(ASN1ObjectIdentifier aSN1ObjectIdentifier, int i) {
        setOid(aSN1ObjectIdentifier);
        setAccessRights((byte) i);
    }

    public CertificateHolderAuthorization(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 76) {
            setPrivateData(new ASN1InputStream(dERApplicationSpecific.getContents()));
        }
    }

    public static int GetFlag(String str) {
        Integer num = (Integer) AuthorizationRole.getReverse(str);
        if (num != null) {
            return num.intValue();
        }
        throw new IllegalArgumentException("Unknown value " + str);
    }

    public static String GetRoleDescription(int i) {
        return (String) AuthorizationRole.get(Integers.valueOf(i));
    }

    private void setAccessRights(byte b) {
        byte[] bArr = new byte[RADG3];
        bArr[IS] = b;
        this.accessRights = new DERApplicationSpecific(EACTags.getTag(83), bArr);
    }

    private void setOid(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.oid = aSN1ObjectIdentifier;
    }

    private void setPrivateData(ASN1InputStream aSN1InputStream) {
        ASN1Primitive readObject = aSN1InputStream.readObject();
        if (readObject instanceof ASN1ObjectIdentifier) {
            this.oid = (ASN1ObjectIdentifier) readObject;
            readObject = aSN1InputStream.readObject();
            if (readObject instanceof DERApplicationSpecific) {
                this.accessRights = (DERApplicationSpecific) readObject;
                return;
            }
            throw new IllegalArgumentException("No access rights in CerticateHolderAuthorization");
        }
        throw new IllegalArgumentException("no Oid in CerticateHolderAuthorization");
    }

    public int getAccessRights() {
        return this.accessRights.getContents()[IS] & GF2Field.MASK;
    }

    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.oid);
        aSN1EncodableVector.add(this.accessRights);
        return new DERApplicationSpecific(76, aSN1EncodableVector);
    }
}
