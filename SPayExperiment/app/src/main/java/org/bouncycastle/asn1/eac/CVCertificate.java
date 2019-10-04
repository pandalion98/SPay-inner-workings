/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.asn1.eac;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.asn1.eac.CertificateHolderAuthorization;
import org.bouncycastle.asn1.eac.CertificateHolderReference;
import org.bouncycastle.asn1.eac.CertificationAuthorityReference;
import org.bouncycastle.asn1.eac.Flags;
import org.bouncycastle.asn1.eac.PackedDate;

public class CVCertificate
extends ASN1Object {
    public static String ReferenceEncoding;
    private static int bodyValid;
    private static int signValid;
    public static final byte version_1;
    private CertificateBody certificateBody;
    private byte[] signature;
    private int valid;

    static {
        bodyValid = 1;
        signValid = 2;
        ReferenceEncoding = "ISO-8859-1";
    }

    public CVCertificate(ASN1InputStream aSN1InputStream) {
        this.initFrom(aSN1InputStream);
    }

    private CVCertificate(DERApplicationSpecific dERApplicationSpecific) {
        this.setPrivateData(dERApplicationSpecific);
    }

    public CVCertificate(CertificateBody certificateBody, byte[] arrby) {
        this.certificateBody = certificateBody;
        this.signature = arrby;
        this.valid |= bodyValid;
        this.valid |= signValid;
    }

    public static CVCertificate getInstance(Object object) {
        if (object instanceof CVCertificate) {
            return (CVCertificate)object;
        }
        if (object != null) {
            try {
                CVCertificate cVCertificate = new CVCertificate(DERApplicationSpecific.getInstance(object));
                return cVCertificate;
            }
            catch (IOException iOException) {
                throw new ASN1ParsingException("unable to parse data: " + iOException.getMessage(), iOException);
            }
        }
        return null;
    }

    private void initFrom(ASN1InputStream aSN1InputStream) {
        ASN1Primitive aSN1Primitive;
        while ((aSN1Primitive = aSN1InputStream.readObject()) != null) {
            if (aSN1Primitive instanceof DERApplicationSpecific) {
                this.setPrivateData((DERApplicationSpecific)aSN1Primitive);
                continue;
            }
            throw new IOException("Invalid Input Stream for creating an Iso7816CertificateStructure");
        }
    }

    private void setPrivateData(DERApplicationSpecific dERApplicationSpecific) {
        this.valid = 0;
        if (dERApplicationSpecific.getApplicationTag() == 33) {
            ASN1Primitive aSN1Primitive;
            ASN1InputStream aSN1InputStream = new ASN1InputStream(dERApplicationSpecific.getContents());
            block4 : while ((aSN1Primitive = aSN1InputStream.readObject()) != null) {
                if (aSN1Primitive instanceof DERApplicationSpecific) {
                    DERApplicationSpecific dERApplicationSpecific2 = (DERApplicationSpecific)aSN1Primitive;
                    switch (dERApplicationSpecific2.getApplicationTag()) {
                        default: {
                            throw new IOException("Invalid tag, not an Iso7816CertificateStructure :" + dERApplicationSpecific2.getApplicationTag());
                        }
                        case 78: {
                            this.certificateBody = CertificateBody.getInstance(dERApplicationSpecific2);
                            this.valid |= bodyValid;
                            continue block4;
                        }
                        case 55: 
                    }
                    this.signature = dERApplicationSpecific2.getContents();
                    this.valid |= signValid;
                    continue;
                }
                throw new IOException("Invalid Object, not an Iso7816CertificateStructure");
            }
        } else {
            throw new IOException("not a CARDHOLDER_CERTIFICATE :" + dERApplicationSpecific.getApplicationTag());
        }
    }

    public CertificationAuthorityReference getAuthorityReference() {
        return this.certificateBody.getCertificationAuthorityReference();
    }

    public CertificateBody getBody() {
        return this.certificateBody;
    }

    public int getCertificateType() {
        return this.certificateBody.getCertificateType();
    }

    public PackedDate getEffectiveDate() {
        return this.certificateBody.getCertificateEffectiveDate();
    }

    public PackedDate getExpirationDate() {
        return this.certificateBody.getCertificateExpirationDate();
    }

    public ASN1ObjectIdentifier getHolderAuthorization() {
        return this.certificateBody.getCertificateHolderAuthorization().getOid();
    }

    public Flags getHolderAuthorizationRights() {
        return new Flags(31 & this.certificateBody.getCertificateHolderAuthorization().getAccessRights());
    }

    public int getHolderAuthorizationRole() {
        return 192 & this.certificateBody.getCertificateHolderAuthorization().getAccessRights();
    }

    public CertificateHolderReference getHolderReference() {
        return this.certificateBody.getCertificateHolderReference();
    }

    public int getRole() {
        return this.certificateBody.getCertificateHolderAuthorization().getAccessRights();
    }

    public byte[] getSignature() {
        return this.signature;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.valid != (signValid | bodyValid)) {
            return null;
        }
        aSN1EncodableVector.add(this.certificateBody);
        try {
            aSN1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.signature)));
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to convert signature!");
        }
        return new DERApplicationSpecific(33, aSN1EncodableVector);
    }
}

