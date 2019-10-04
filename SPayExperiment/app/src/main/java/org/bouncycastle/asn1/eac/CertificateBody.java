/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.eac;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.eac.CertificateHolderAuthorization;
import org.bouncycastle.asn1.eac.CertificateHolderReference;
import org.bouncycastle.asn1.eac.CertificationAuthorityReference;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.asn1.eac.PackedDate;
import org.bouncycastle.asn1.eac.PublicKeyDataObject;

public class CertificateBody
extends ASN1Object {
    private static final int CAR = 2;
    private static final int CEfD = 32;
    private static final int CExD = 64;
    private static final int CHA = 16;
    private static final int CHR = 8;
    private static final int CPI = 1;
    private static final int PK = 4;
    public static final int profileType = 127;
    public static final int requestType = 13;
    private DERApplicationSpecific certificateEffectiveDate;
    private DERApplicationSpecific certificateExpirationDate;
    private CertificateHolderAuthorization certificateHolderAuthorization;
    private DERApplicationSpecific certificateHolderReference;
    private DERApplicationSpecific certificateProfileIdentifier;
    private int certificateType = 0;
    private DERApplicationSpecific certificationAuthorityReference;
    private PublicKeyDataObject publicKey;
    ASN1InputStream seq;

    private CertificateBody(DERApplicationSpecific dERApplicationSpecific) {
        this.setIso7816CertificateBody(dERApplicationSpecific);
    }

    public CertificateBody(DERApplicationSpecific dERApplicationSpecific, CertificationAuthorityReference certificationAuthorityReference, PublicKeyDataObject publicKeyDataObject, CertificateHolderReference certificateHolderReference, CertificateHolderAuthorization certificateHolderAuthorization, PackedDate packedDate, PackedDate packedDate2) {
        this.setCertificateProfileIdentifier(dERApplicationSpecific);
        this.setCertificationAuthorityReference(new DERApplicationSpecific(2, certificationAuthorityReference.getEncoded()));
        this.setPublicKey(publicKeyDataObject);
        this.setCertificateHolderReference(new DERApplicationSpecific(32, certificateHolderReference.getEncoded()));
        this.setCertificateHolderAuthorization(certificateHolderAuthorization);
        try {
            this.setCertificateEffectiveDate(new DERApplicationSpecific(false, 37, new DEROctetString(packedDate.getEncoding())));
            this.setCertificateExpirationDate(new DERApplicationSpecific(false, 36, new DEROctetString(packedDate2.getEncoding())));
            return;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("unable to encode dates: " + iOException.getMessage());
        }
    }

    public static CertificateBody getInstance(Object object) {
        if (object instanceof CertificateBody) {
            return (CertificateBody)object;
        }
        if (object != null) {
            return new CertificateBody(DERApplicationSpecific.getInstance(object));
        }
        return null;
    }

    private ASN1Primitive profileToASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certificateProfileIdentifier);
        aSN1EncodableVector.add(this.certificationAuthorityReference);
        aSN1EncodableVector.add(new DERApplicationSpecific(false, 73, this.publicKey));
        aSN1EncodableVector.add(this.certificateHolderReference);
        aSN1EncodableVector.add(this.certificateHolderAuthorization);
        aSN1EncodableVector.add(this.certificateEffectiveDate);
        aSN1EncodableVector.add(this.certificateExpirationDate);
        return new DERApplicationSpecific(78, aSN1EncodableVector);
    }

    private ASN1Primitive requestToASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certificateProfileIdentifier);
        aSN1EncodableVector.add(new DERApplicationSpecific(false, 73, this.publicKey));
        aSN1EncodableVector.add(this.certificateHolderReference);
        return new DERApplicationSpecific(78, aSN1EncodableVector);
    }

    private void setCertificateEffectiveDate(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 37) {
            this.certificateEffectiveDate = dERApplicationSpecific;
            this.certificateType = 32 | this.certificateType;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EFFECTIVE_DATE tag :" + EACTags.encodeTag(dERApplicationSpecific));
    }

    private void setCertificateExpirationDate(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 36) {
            this.certificateExpirationDate = dERApplicationSpecific;
            this.certificateType = 64 | this.certificateType;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EXPIRATION_DATE tag");
    }

    private void setCertificateHolderAuthorization(CertificateHolderAuthorization certificateHolderAuthorization) {
        this.certificateHolderAuthorization = certificateHolderAuthorization;
        this.certificateType = 16 | this.certificateType;
    }

    private void setCertificateHolderReference(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 32) {
            this.certificateHolderReference = dERApplicationSpecific;
            this.certificateType = 8 | this.certificateType;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.CARDHOLDER_NAME tag");
    }

    private void setCertificateProfileIdentifier(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 41) {
            this.certificateProfileIdentifier = dERApplicationSpecific;
            this.certificateType = 1 | this.certificateType;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.INTERCHANGE_PROFILE tag :" + EACTags.encodeTag(dERApplicationSpecific));
    }

    private void setCertificationAuthorityReference(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 2) {
            this.certificationAuthorityReference = dERApplicationSpecific;
            this.certificateType = 2 | this.certificateType;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.ISSUER_IDENTIFICATION_NUMBER tag");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setIso7816CertificateBody(DERApplicationSpecific dERApplicationSpecific) {
        ASN1Primitive aSN1Primitive;
        block12 : {
            DERApplicationSpecific dERApplicationSpecific2;
            if (dERApplicationSpecific.getApplicationTag() != 78) {
                throw new IOException("Bad tag : not an iso7816 CERTIFICATE_CONTENT_TEMPLATE");
            }
            ASN1InputStream aSN1InputStream = new ASN1InputStream(dERApplicationSpecific.getContents());
            block9 : do {
                if ((aSN1Primitive = aSN1InputStream.readObject()) == null) {
                    return;
                }
                if (!(aSN1Primitive instanceof DERApplicationSpecific)) break block12;
                dERApplicationSpecific2 = (DERApplicationSpecific)aSN1Primitive;
                switch (dERApplicationSpecific2.getApplicationTag()) {
                    case 41: {
                        this.setCertificateProfileIdentifier(dERApplicationSpecific2);
                        continue block9;
                    }
                    case 2: {
                        this.setCertificationAuthorityReference(dERApplicationSpecific2);
                        continue block9;
                    }
                    case 73: {
                        this.setPublicKey(PublicKeyDataObject.getInstance(dERApplicationSpecific2.getObject(16)));
                        continue block9;
                    }
                    case 32: {
                        this.setCertificateHolderReference(dERApplicationSpecific2);
                        continue block9;
                    }
                    case 76: {
                        this.setCertificateHolderAuthorization(new CertificateHolderAuthorization(dERApplicationSpecific2));
                        continue block9;
                    }
                    case 37: {
                        this.setCertificateEffectiveDate(dERApplicationSpecific2);
                        continue block9;
                    }
                    case 36: {
                        this.setCertificateExpirationDate(dERApplicationSpecific2);
                        continue block9;
                    }
                }
                break;
            } while (true);
            this.certificateType = 0;
            throw new IOException("Not a valid iso7816 DERApplicationSpecific tag " + dERApplicationSpecific2.getApplicationTag());
        }
        throw new IOException("Not a valid iso7816 content : not a DERApplicationSpecific Object :" + EACTags.encodeTag(dERApplicationSpecific) + (Object)aSN1Primitive.getClass());
    }

    private void setPublicKey(PublicKeyDataObject publicKeyDataObject) {
        this.publicKey = PublicKeyDataObject.getInstance(publicKeyDataObject);
        this.certificateType = 4 | this.certificateType;
    }

    public PackedDate getCertificateEffectiveDate() {
        if ((32 & this.certificateType) == 32) {
            return new PackedDate(this.certificateEffectiveDate.getContents());
        }
        return null;
    }

    public PackedDate getCertificateExpirationDate() {
        if ((64 & this.certificateType) == 64) {
            return new PackedDate(this.certificateExpirationDate.getContents());
        }
        throw new IOException("certificate Expiration Date not set");
    }

    public CertificateHolderAuthorization getCertificateHolderAuthorization() {
        if ((16 & this.certificateType) == 16) {
            return this.certificateHolderAuthorization;
        }
        throw new IOException("Certificate Holder Authorisation not set");
    }

    public CertificateHolderReference getCertificateHolderReference() {
        return new CertificateHolderReference(this.certificateHolderReference.getContents());
    }

    public DERApplicationSpecific getCertificateProfileIdentifier() {
        return this.certificateProfileIdentifier;
    }

    public int getCertificateType() {
        return this.certificateType;
    }

    public CertificationAuthorityReference getCertificationAuthorityReference() {
        if ((2 & this.certificateType) == 2) {
            return new CertificationAuthorityReference(this.certificationAuthorityReference.getContents());
        }
        throw new IOException("Certification authority reference not set");
    }

    public PublicKeyDataObject getPublicKey() {
        return this.publicKey;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            if (this.certificateType == 127) {
                return this.profileToASN1Object();
            }
            if (this.certificateType == 13) {
                ASN1Primitive aSN1Primitive = this.requestToASN1Object();
                return aSN1Primitive;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }
}

