/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralNames;

public class DistributionPointName
extends ASN1Object
implements ASN1Choice {
    public static final int FULL_NAME = 0;
    public static final int NAME_RELATIVE_TO_CRL_ISSUER = 1;
    ASN1Encodable name;
    int type;

    public DistributionPointName(int n2, ASN1Encodable aSN1Encodable) {
        this.type = n2;
        this.name = aSN1Encodable;
    }

    public DistributionPointName(ASN1TaggedObject aSN1TaggedObject) {
        this.type = aSN1TaggedObject.getTagNo();
        if (this.type == 0) {
            this.name = GeneralNames.getInstance(aSN1TaggedObject, false);
            return;
        }
        this.name = ASN1Set.getInstance(aSN1TaggedObject, false);
    }

    public DistributionPointName(GeneralNames generalNames) {
        this(0, generalNames);
    }

    private void appendObject(StringBuffer stringBuffer, String string, String string2, String string3) {
        stringBuffer.append("    ");
        stringBuffer.append(string2);
        stringBuffer.append(":");
        stringBuffer.append(string);
        stringBuffer.append("    ");
        stringBuffer.append("    ");
        stringBuffer.append(string3);
        stringBuffer.append(string);
    }

    public static DistributionPointName getInstance(Object object) {
        if (object == null || object instanceof DistributionPointName) {
            return (DistributionPointName)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new DistributionPointName((ASN1TaggedObject)object);
        }
        throw new IllegalArgumentException("unknown object in factory: " + object.getClass().getName());
    }

    public static DistributionPointName getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DistributionPointName.getInstance(ASN1TaggedObject.getInstance(aSN1TaggedObject, true));
    }

    public ASN1Encodable getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.type, this.name);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        String string = System.getProperty((String)"line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DistributionPointName: [");
        stringBuffer.append(string);
        if (this.type == 0) {
            this.appendObject(stringBuffer, string, "fullName", this.name.toString());
        } else {
            this.appendObject(stringBuffer, string, "nameRelativeToCRLIssuer", this.name.toString());
        }
        stringBuffer.append("]");
        stringBuffer.append(string);
        return stringBuffer.toString();
    }
}

