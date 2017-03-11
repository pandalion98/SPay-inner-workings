package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;

public class DERExternal extends ASN1Primitive {
    private ASN1Primitive dataValueDescriptor;
    private ASN1ObjectIdentifier directReference;
    private int encoding;
    private ASN1Primitive externalContent;
    private ASN1Integer indirectReference;

    public DERExternal(ASN1EncodableVector aSN1EncodableVector) {
        int i = 0;
        ASN1Primitive objFromVector = getObjFromVector(aSN1EncodableVector, 0);
        if (objFromVector instanceof ASN1ObjectIdentifier) {
            this.directReference = (ASN1ObjectIdentifier) objFromVector;
            i = 1;
            objFromVector = getObjFromVector(aSN1EncodableVector, 1);
        }
        if (objFromVector instanceof ASN1Integer) {
            this.indirectReference = (ASN1Integer) objFromVector;
            i++;
            objFromVector = getObjFromVector(aSN1EncodableVector, i);
        }
        if (!(objFromVector instanceof DERTaggedObject)) {
            this.dataValueDescriptor = objFromVector;
            i++;
            objFromVector = getObjFromVector(aSN1EncodableVector, i);
        }
        if (aSN1EncodableVector.size() != i + 1) {
            throw new IllegalArgumentException("input vector too large");
        } else if (objFromVector instanceof DERTaggedObject) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject) objFromVector;
            setEncoding(dERTaggedObject.getTagNo());
            this.externalContent = dERTaggedObject.getObject();
        } else {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        }
    }

    public DERExternal(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, int i, ASN1Primitive aSN1Primitive2) {
        setDirectReference(aSN1ObjectIdentifier);
        setIndirectReference(aSN1Integer);
        setDataValueDescriptor(aSN1Primitive);
        setEncoding(i);
        setExternalContent(aSN1Primitive2.toASN1Primitive());
    }

    public DERExternal(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Integer aSN1Integer, ASN1Primitive aSN1Primitive, DERTaggedObject dERTaggedObject) {
        this(aSN1ObjectIdentifier, aSN1Integer, aSN1Primitive, dERTaggedObject.getTagNo(), dERTaggedObject.toASN1Primitive());
    }

    private ASN1Primitive getObjFromVector(ASN1EncodableVector aSN1EncodableVector, int i) {
        if (aSN1EncodableVector.size() > i) {
            return aSN1EncodableVector.get(i).toASN1Primitive();
        }
        throw new IllegalArgumentException("too few objects in input vector");
    }

    private void setDataValueDescriptor(ASN1Primitive aSN1Primitive) {
        this.dataValueDescriptor = aSN1Primitive;
    }

    private void setDirectReference(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.directReference = aSN1ObjectIdentifier;
    }

    private void setEncoding(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("invalid encoding value: " + i);
        }
        this.encoding = i;
    }

    private void setExternalContent(ASN1Primitive aSN1Primitive) {
        this.externalContent = aSN1Primitive;
    }

    private void setIndirectReference(ASN1Integer aSN1Integer) {
        this.indirectReference = aSN1Integer;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERExternal)) {
            return false;
        }
        if (this == aSN1Primitive) {
            return true;
        }
        DERExternal dERExternal = (DERExternal) aSN1Primitive;
        return (this.directReference == null || (dERExternal.directReference != null && dERExternal.directReference.equals(this.directReference))) ? (this.indirectReference == null || (dERExternal.indirectReference != null && dERExternal.indirectReference.equals(this.indirectReference))) ? (this.dataValueDescriptor == null || (dERExternal.dataValueDescriptor != null && dERExternal.dataValueDescriptor.equals(this.dataValueDescriptor))) ? this.externalContent.equals(dERExternal.externalContent) : false : false : false;
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.directReference != null) {
            byteArrayOutputStream.write(this.directReference.getEncoded(ASN1Encoding.DER));
        }
        if (this.indirectReference != null) {
            byteArrayOutputStream.write(this.indirectReference.getEncoded(ASN1Encoding.DER));
        }
        if (this.dataValueDescriptor != null) {
            byteArrayOutputStream.write(this.dataValueDescriptor.getEncoded(ASN1Encoding.DER));
        }
        byteArrayOutputStream.write(new DERTaggedObject(true, this.encoding, this.externalContent).getEncoded(ASN1Encoding.DER));
        aSN1OutputStream.writeEncoded(32, 8, byteArrayOutputStream.toByteArray());
    }

    int encodedLength() {
        return getEncoded().length;
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

    public int hashCode() {
        int i = 0;
        if (this.directReference != null) {
            i = this.directReference.hashCode();
        }
        if (this.indirectReference != null) {
            i ^= this.indirectReference.hashCode();
        }
        if (this.dataValueDescriptor != null) {
            i ^= this.dataValueDescriptor.hashCode();
        }
        return i ^ this.externalContent.hashCode();
    }

    boolean isConstructed() {
        return true;
    }
}
