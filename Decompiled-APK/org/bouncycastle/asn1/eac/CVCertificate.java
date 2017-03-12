package org.bouncycastle.asn1.eac;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.i18n.LocalizedMessage;

public class CVCertificate extends ASN1Object {
    public static String ReferenceEncoding;
    private static int bodyValid;
    private static int signValid;
    public static final byte version_1 = (byte) 0;
    private CertificateBody certificateBody;
    private byte[] signature;
    private int valid;

    static {
        bodyValid = 1;
        signValid = 2;
        ReferenceEncoding = LocalizedMessage.DEFAULT_ENCODING;
    }

    public CVCertificate(ASN1InputStream aSN1InputStream) {
        initFrom(aSN1InputStream);
    }

    private CVCertificate(DERApplicationSpecific dERApplicationSpecific) {
        setPrivateData(dERApplicationSpecific);
    }

    public CVCertificate(CertificateBody certificateBody, byte[] bArr) {
        this.certificateBody = certificateBody;
        this.signature = bArr;
        this.valid |= bodyValid;
        this.valid |= signValid;
    }

    public static CVCertificate getInstance(Object obj) {
        if (obj instanceof CVCertificate) {
            return (CVCertificate) obj;
        }
        if (obj == null) {
            return null;
        }
        try {
            return new CVCertificate(DERApplicationSpecific.getInstance(obj));
        } catch (Throwable e) {
            throw new ASN1ParsingException("unable to parse data: " + e.getMessage(), e);
        }
    }

    private void initFrom(ASN1InputStream aSN1InputStream) {
        while (true) {
            ASN1Primitive readObject = aSN1InputStream.readObject();
            if (readObject != null) {
                if (!(readObject instanceof DERApplicationSpecific)) {
                    break;
                }
                setPrivateData((DERApplicationSpecific) readObject);
            } else {
                return;
            }
        }
        throw new IOException("Invalid Input Stream for creating an Iso7816CertificateStructure");
    }

    private void setPrivateData(DERApplicationSpecific dERApplicationSpecific) {
        this.valid = 0;
        if (dERApplicationSpecific.getApplicationTag() == 33) {
            ASN1InputStream aSN1InputStream = new ASN1InputStream(dERApplicationSpecific.getContents());
            while (true) {
                ASN1Primitive readObject = aSN1InputStream.readObject();
                if (readObject == null) {
                    return;
                }
                if (readObject instanceof DERApplicationSpecific) {
                    DERApplicationSpecific dERApplicationSpecific2 = (DERApplicationSpecific) readObject;
                    switch (dERApplicationSpecific2.getApplicationTag()) {
                        case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
                            this.signature = dERApplicationSpecific2.getContents();
                            this.valid |= signValid;
                            break;
                        case EACTags.CERTIFICATE_CONTENT_TEMPLATE /*78*/:
                            this.certificateBody = CertificateBody.getInstance(dERApplicationSpecific2);
                            this.valid |= bodyValid;
                            break;
                        default:
                            throw new IOException("Invalid tag, not an Iso7816CertificateStructure :" + dERApplicationSpecific2.getApplicationTag());
                    }
                }
                throw new IOException("Invalid Object, not an Iso7816CertificateStructure");
            }
        }
        throw new IOException("not a CARDHOLDER_CERTIFICATE :" + dERApplicationSpecific.getApplicationTag());
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
        return new Flags(this.certificateBody.getCertificateHolderAuthorization().getAccessRights() & 31);
    }

    public int getHolderAuthorizationRole() {
        return this.certificateBody.getCertificateHolderAuthorization().getAccessRights() & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256;
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

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.valid != (signValid | bodyValid)) {
            return null;
        }
        aSN1EncodableVector.add(this.certificateBody);
        try {
            aSN1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.signature)));
            return new DERApplicationSpecific(33, aSN1EncodableVector);
        } catch (IOException e) {
            throw new IllegalStateException("unable to convert signature!");
        }
    }
}
