/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERTaggedObject;

public class DERExternal
extends ASN1Primitive {
    private ASN1Primitive dataValueDescriptor;
    private ASN1ObjectIdentifier directReference;
    private int encoding;
    private ASN1Primitive externalContent;
    private ASN1Integer indirectReference;

    public DERExternal(ASN1EncodableVector aSN1EncodableVector) {
        ASN1Primitive aSN1Primitive = this.getObjFromVector(aSN1EncodableVector, 0);
        boolean bl = aSN1Primitive instanceof ASN1ObjectIdentifier;
        int n2 = 0;
        if (bl) {
            this.directReference = (ASN1ObjectIdentifier)aSN1Primitive;
            n2 = 1;
            aSN1Primitive = this.getObjFromVector(aSN1EncodableVector, n2);
        }
        if (aSN1Primitive instanceof ASN1Integer) {
            this.indirectReference = (ASN1Integer)aSN1Primitive;
            aSN1Primitive = this.getObjFromVector(aSN1EncodableVector, ++n2);
        }
        if (!(aSN1Primitive instanceof DERTaggedObject)) {
            this.dataValueDescriptor = aSN1Primitive;
            aSN1Primitive = this.getObjFromVector(aSN1EncodableVector, ++n2);
        }
        if (aSN1EncodableVector.size() != n2 + 1) {
            throw new IllegalArgumentException("input vector too large");
        }
        if (!(aSN1Primitive instanceof DERTaggedObject)) {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        }
        DERTaggedObject dERTaggedObject = (DERTaggedObject)aSN1Primitive;
        this.setEncoding(dERTaggedObject.getTagNo());
        this.externalContent = dERTaggedObject.getObject();
    }

    public DERExternal(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, int n2, ASN1Primitive aSN1Primitive2) {
        this.setDirectReference(aSN1ObjectIdentifier);
        this.setIndirectReference(aSN1Integer);
        this.setDataValueDescriptor(aSN1Primitive);
        this.setEncoding(n2);
        this.setExternalContent(aSN1Primitive2.toASN1Primitive());
    }

    public DERExternal(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, DERTaggedObject dERTaggedObject) {
        this(aSN1ObjectIdentifier, aSN1Integer, aSN1Primitive, dERTaggedObject.getTagNo(), dERTaggedObject.toASN1Primitive());
    }

    private ASN1Primitive getObjFromVector(ASN1EncodableVector aSN1EncodableVector, int n2) {
        if (aSN1EncodableVector.size() <= n2) {
            throw new IllegalArgumentException("too few objects in input vector");
        }
        return aSN1EncodableVector.get(n2).toASN1Primitive();
    }

    private void setDataValueDescriptor(ASN1Primitive aSN1Primitive) {
        this.dataValueDescriptor = aSN1Primitive;
    }

    private void setDirectReference(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.directReference = aSN1ObjectIdentifier;
    }

    private void setEncoding(int n2) {
        if (n2 < 0 || n2 > 2) {
            throw new IllegalArgumentException("invalid encoding value: " + n2);
        }
        this.encoding = n2;
    }

    private void setExternalContent(ASN1Primitive aSN1Primitive) {
        this.externalContent = aSN1Primitive;
    }

    private void setIndirectReference(ASN1Integer aSN1Integer) {
        this.indirectReference = aSN1Integer;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        DERExternal dERExternal;
        block5 : {
            block4 : {
                if (!(aSN1Primitive instanceof DERExternal)) break block4;
                if (this == aSN1Primitive) {
                    return true;
                }
                dERExternal = (DERExternal)aSN1Primitive;
                if ((this.directReference == null || dERExternal.directReference != null && dERExternal.directReference.equals(this.directReference)) && (this.indirectReference == null || dERExternal.indirectReference != null && dERExternal.indirectReference.equals(this.indirectReference)) && (this.dataValueDescriptor == null || dERExternal.dataValueDescriptor != null && dERExternal.dataValueDescriptor.equals(this.dataValueDescriptor))) break block5;
            }
            return false;
        }
        return this.externalContent.equals(dERExternal.externalContent);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.directReference != null) {
            byteArrayOutputStream.write(this.directReference.getEncoded("DER"));
        }
        if (this.indirectReference != null) {
            byteArrayOutputStream.write(this.indirectReference.getEncoded("DER"));
        }
        if (this.dataValueDescriptor != null) {
            byteArrayOutputStream.write(this.dataValueDescriptor.getEncoded("DER"));
        }
        byteArrayOutputStream.write(new DERTaggedObject(true, this.encoding, this.externalContent).getEncoded("DER"));
        aSN1OutputStream.writeEncoded(32, 8, byteArrayOutputStream.toByteArray());
    }

    @Override
    int encodedLength() {
        return this.getEncoded().length;
    }

    public ASN1Primitive getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }

    public ASN1ObjectIdentifier getDirectReference() {
        return this.directReference;
    }

    public int getEncoding() {
        return this.encoding;
    }

    public ASN1Primitive getExternalContent() {
        return this.externalContent;
    }

    public ASN1Integer getIndirectReference() {
        return this.indirectReference;
    }

    @Override
    public int hashCode() {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = this.directReference;
        int n2 = 0;
        if (aSN1ObjectIdentifier != null) {
            n2 = this.directReference.hashCode();
        }
        if (this.indirectReference != null) {
            n2 ^= this.indirectReference.hashCode();
        }
        if (this.dataValueDescriptor != null) {
            n2 ^= this.dataValueDescriptor.hashCode();
        }
        return n2 ^ this.externalContent.hashCode();
    }

    @Override
    boolean isConstructed() {
        return true;
    }
}

