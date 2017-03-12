package org.bouncycastle.asn1.eac;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.crypto.tls.CipherSuite;

public class CVCertificateRequest extends ASN1Object {
    public static byte[] ZeroArray;
    private static int bodyValid;
    private static int signValid;
    int ProfileId;
    byte[] certificate;
    private CertificateBody certificateBody;
    byte[] encoded;
    byte[] encodedAuthorityReference;
    private byte[] innerSignature;
    PublicKeyDataObject iso7816PubKey;
    ASN1ObjectIdentifier keyOid;
    private byte[] outerSignature;
    protected String overSignerReference;
    ASN1ObjectIdentifier signOid;
    String strCertificateHolderReference;
    private int valid;

    static {
        bodyValid = 1;
        signValid = 2;
        ZeroArray = new byte[]{(byte) 0};
    }

    private CVCertificateRequest(DERApplicationSpecific dERApplicationSpecific) {
        this.innerSignature = null;
        this.outerSignature = null;
        this.signOid = null;
        this.keyOid = null;
        this.certificate = null;
        this.overSignerReference = null;
        this.iso7816PubKey = null;
        if (dERApplicationSpecific.getApplicationTag() == CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256) {
            ASN1Sequence instance = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
            initCertBody(DERApplicationSpecific.getInstance(instance.getObjectAt(0)));
            this.outerSignature = DERApplicationSpecific.getInstance(instance.getObjectAt(instance.size() - 1)).getContents();
            return;
        }
        initCertBody(dERApplicationSpecific);
    }

    public static CVCertificateRequest getInstance(Object obj) {
        if (obj instanceof CVCertificateRequest) {
            return (CVCertificateRequest) obj;
        }
        if (obj == null) {
            return null;
        }
        try {
            return new CVCertificateRequest(DERApplicationSpecific.getInstance(obj));
        } catch (Throwable e) {
            throw new ASN1ParsingException("unable to parse data: " + e.getMessage(), e);
        }
    }

    private void initCertBody(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 33) {
            Enumeration objects = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16)).getObjects();
            while (objects.hasMoreElements()) {
                DERApplicationSpecific instance = DERApplicationSpecific.getInstance(objects.nextElement());
                switch (instance.getApplicationTag()) {
                    case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
                        this.innerSignature = instance.getContents();
                        this.valid |= signValid;
                        break;
                    case EACTags.CERTIFICATE_CONTENT_TEMPLATE /*78*/:
                        this.certificateBody = CertificateBody.getInstance(instance);
                        this.valid |= bodyValid;
                        break;
                    default:
                        throw new IOException("Invalid tag, not an CV Certificate Request element:" + instance.getApplicationTag());
                }
            }
            return;
        }
        throw new IOException("not a CARDHOLDER_CERTIFICATE in request:" + dERApplicationSpecific.getApplicationTag());
    }

    public CertificateBody getCertificateBody() {
        return this.certificateBody;
    }

    public byte[] getInnerSignature() {
        return this.innerSignature;
    }

    public byte[] getOuterSignature() {
        return this.outerSignature;
    }

    public PublicKeyDataObject getPublicKey() {
        return this.certificateBody.getPublicKey();
    }

    public boolean hasOuterSignature() {
        return this.outerSignature != null;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certificateBody);
        try {
            aSN1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.innerSignature)));
            return new DERApplicationSpecific(33, aSN1EncodableVector);
        } catch (IOException e) {
            throw new IllegalStateException("unable to convert signature!");
        }
    }
}
