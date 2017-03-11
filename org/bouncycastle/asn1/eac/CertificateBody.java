package org.bouncycastle.asn1.eac;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;

public class CertificateBody extends ASN1Object {
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
    private int certificateType;
    private DERApplicationSpecific certificationAuthorityReference;
    private PublicKeyDataObject publicKey;
    ASN1InputStream seq;

    private CertificateBody(DERApplicationSpecific dERApplicationSpecific) {
        this.certificateType = 0;
        setIso7816CertificateBody(dERApplicationSpecific);
    }

    public CertificateBody(DERApplicationSpecific dERApplicationSpecific, CertificationAuthorityReference certificationAuthorityReference, PublicKeyDataObject publicKeyDataObject, CertificateHolderReference certificateHolderReference, CertificateHolderAuthorization certificateHolderAuthorization, PackedDate packedDate, PackedDate packedDate2) {
        this.certificateType = 0;
        setCertificateProfileIdentifier(dERApplicationSpecific);
        setCertificationAuthorityReference(new DERApplicationSpecific((int) CAR, certificationAuthorityReference.getEncoded()));
        setPublicKey(publicKeyDataObject);
        setCertificateHolderReference(new DERApplicationSpecific((int) CEfD, certificateHolderReference.getEncoded()));
        setCertificateHolderAuthorization(certificateHolderAuthorization);
        try {
            setCertificateEffectiveDate(new DERApplicationSpecific(false, 37, new DEROctetString(packedDate.getEncoding())));
            setCertificateExpirationDate(new DERApplicationSpecific(false, 36, new DEROctetString(packedDate2.getEncoding())));
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to encode dates: " + e.getMessage());
        }
    }

    public static CertificateBody getInstance(Object obj) {
        return obj instanceof CertificateBody ? (CertificateBody) obj : obj != null ? new CertificateBody(DERApplicationSpecific.getInstance(obj)) : null;
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
            this.certificateType |= CEfD;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EFFECTIVE_DATE tag :" + EACTags.encodeTag(dERApplicationSpecific));
    }

    private void setCertificateExpirationDate(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 36) {
            this.certificateExpirationDate = dERApplicationSpecific;
            this.certificateType |= CExD;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EXPIRATION_DATE tag");
    }

    private void setCertificateHolderAuthorization(CertificateHolderAuthorization certificateHolderAuthorization) {
        this.certificateHolderAuthorization = certificateHolderAuthorization;
        this.certificateType |= CHA;
    }

    private void setCertificateHolderReference(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == CEfD) {
            this.certificateHolderReference = dERApplicationSpecific;
            this.certificateType |= CHR;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.CARDHOLDER_NAME tag");
    }

    private void setCertificateProfileIdentifier(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 41) {
            this.certificateProfileIdentifier = dERApplicationSpecific;
            this.certificateType |= CPI;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.INTERCHANGE_PROFILE tag :" + EACTags.encodeTag(dERApplicationSpecific));
    }

    private void setCertificationAuthorityReference(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == CAR) {
            this.certificationAuthorityReference = dERApplicationSpecific;
            this.certificateType |= CAR;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.ISSUER_IDENTIFICATION_NUMBER tag");
    }

    private void setIso7816CertificateBody(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 78) {
            ASN1InputStream aSN1InputStream = new ASN1InputStream(dERApplicationSpecific.getContents());
            while (true) {
                ASN1Primitive readObject = aSN1InputStream.readObject();
                if (readObject == null) {
                    return;
                }
                if (readObject instanceof DERApplicationSpecific) {
                    DERApplicationSpecific dERApplicationSpecific2 = (DERApplicationSpecific) readObject;
                    switch (dERApplicationSpecific2.getApplicationTag()) {
                        case CAR /*2*/:
                            setCertificationAuthorityReference(dERApplicationSpecific2);
                            break;
                        case CEfD /*32*/:
                            setCertificateHolderReference(dERApplicationSpecific2);
                            break;
                        case EACTags.APPLICATION_EXPIRATION_DATE /*36*/:
                            setCertificateExpirationDate(dERApplicationSpecific2);
                            break;
                        case EACTags.APPLICATION_EFFECTIVE_DATE /*37*/:
                            setCertificateEffectiveDate(dERApplicationSpecific2);
                            break;
                        case EACTags.INTERCHANGE_PROFILE /*41*/:
                            setCertificateProfileIdentifier(dERApplicationSpecific2);
                            break;
                        case EACTags.CARDHOLDER_PUBLIC_KEY_TEMPLATE /*73*/:
                            setPublicKey(PublicKeyDataObject.getInstance(dERApplicationSpecific2.getObject(CHA)));
                            break;
                        case EACTags.CERTIFICATE_HOLDER_AUTHORIZATION_TEMPLATE /*76*/:
                            setCertificateHolderAuthorization(new CertificateHolderAuthorization(dERApplicationSpecific2));
                            break;
                        default:
                            this.certificateType = 0;
                            throw new IOException("Not a valid iso7816 DERApplicationSpecific tag " + dERApplicationSpecific2.getApplicationTag());
                    }
                }
                throw new IOException("Not a valid iso7816 content : not a DERApplicationSpecific Object :" + EACTags.encodeTag(dERApplicationSpecific) + readObject.getClass());
            }
        }
        throw new IOException("Bad tag : not an iso7816 CERTIFICATE_CONTENT_TEMPLATE");
    }

    private void setPublicKey(PublicKeyDataObject publicKeyDataObject) {
        this.publicKey = PublicKeyDataObject.getInstance(publicKeyDataObject);
        this.certificateType |= PK;
    }

    public PackedDate getCertificateEffectiveDate() {
        return (this.certificateType & CEfD) == CEfD ? new PackedDate(this.certificateEffectiveDate.getContents()) : null;
    }

    public PackedDate getCertificateExpirationDate() {
        if ((this.certificateType & CExD) == CExD) {
            return new PackedDate(this.certificateExpirationDate.getContents());
        }
        throw new IOException("certificate Expiration Date not set");
    }

    public CertificateHolderAuthorization getCertificateHolderAuthorization() {
        if ((this.certificateType & CHA) == CHA) {
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
        if ((this.certificateType & CAR) == CAR) {
            return new CertificationAuthorityReference(this.certificationAuthorityReference.getContents());
        }
        throw new IOException("Certification authority reference not set");
    }

    public PublicKeyDataObject getPublicKey() {
        return this.publicKey;
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            return this.certificateType == profileType ? profileToASN1Object() : this.certificateType == requestType ? requestToASN1Object() : null;
        } catch (IOException e) {
            return null;
        }
    }
}
