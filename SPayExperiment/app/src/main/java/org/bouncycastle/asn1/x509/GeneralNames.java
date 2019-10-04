/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;

public class GeneralNames
extends ASN1Object {
    private final GeneralName[] names;

    private GeneralNames(ASN1Sequence aSN1Sequence) {
        this.names = new GeneralName[aSN1Sequence.size()];
        for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
            this.names[i2] = GeneralName.getInstance(aSN1Sequence.getObjectAt(i2));
        }
    }

    public GeneralNames(GeneralName generalName) {
        this.names = new GeneralName[]{generalName};
    }

    public GeneralNames(GeneralName[] arrgeneralName) {
        this.names = arrgeneralName;
    }

    public static GeneralNames fromExtensions(Extensions extensions, ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return GeneralNames.getInstance(extensions.getExtensionParsedValue(aSN1ObjectIdentifier));
    }

    public static GeneralNames getInstance(Object object) {
        if (object instanceof GeneralNames) {
            return (GeneralNames)object;
        }
        if (object != null) {
            return new GeneralNames(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static GeneralNames getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return GeneralNames.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public GeneralName[] getNames() {
        GeneralName[] arrgeneralName = new GeneralName[this.names.length];
        System.arraycopy((Object)this.names, (int)0, (Object)arrgeneralName, (int)0, (int)this.names.length);
        return arrgeneralName;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.names);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("GeneralNames:");
        stringBuffer.append(string);
        for (int i2 = 0; i2 != this.names.length; ++i2) {
            stringBuffer.append("    ");
            stringBuffer.append((Object)this.names[i2]);
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }
}

