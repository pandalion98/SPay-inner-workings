/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.eac;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.asn1.eac.PublicKeyDataObject;

public class CVCertificateRequest
extends ASN1Object {
    public static byte[] ZeroArray;
    private static int bodyValid;
    private static int signValid;
    int ProfileId;
    byte[] certificate = null;
    private CertificateBody certificateBody;
    byte[] encoded;
    byte[] encodedAuthorityReference;
    private byte[] innerSignature = null;
    PublicKeyDataObject iso7816PubKey = null;
    ASN1ObjectIdentifier keyOid = null;
    private byte[] outerSignature = null;
    protected String overSignerReference = null;
    ASN1ObjectIdentifier signOid = null;
    String strCertificateHolderReference;
    private int valid;

    static {
        bodyValid = 1;
        signValid = 2;
        ZeroArray = new byte[]{0};
    }

    private CVCertificateRequest(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 103) {
            ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
            this.initCertBody(DERApplicationSpecific.getInstance(aSN1Sequence.getObjectAt(0)));
            this.outerSignature = DERApplicationSpecific.getInstance(aSN1Sequence.getObjectAt(-1 + aSN1Sequence.size())).getContents();
            return;
        }
        this.initCertBody(dERApplicationSpecific);
    }

    public static CVCertificateRequest getInstance(Object object) {
        if (object instanceof CVCertificateRequest) {
            return (CVCertificateRequest)object;
        }
        if (object != null) {
            try {
                CVCertificateRequest cVCertificateRequest = new CVCertificateRequest(DERApplicationSpecific.getInstance(object));
                return cVCertificateRequest;
            }
            catch (IOException iOException) {
                throw new ASN1ParsingException("unable to parse data: " + iOException.getMessage(), iOException);
            }
        }
        return null;
    }

    private void initCertBody(DERApplicationSpecific dERApplicationSpecific) {
        if (dERApplicationSpecific.getApplicationTag() == 33) {
            Enumeration enumeration = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16)).getObjects();
            block4 : while (enumeration.hasMoreElements()) {
                DERApplicationSpecific dERApplicationSpecific2 = DERApplicationSpecific.getInstance(enumeration.nextElement());
                switch (dERApplicationSpecific2.getApplicationTag()) {
                    default: {
                        throw new IOException("Invalid tag, not an CV Certificate Request element:" + dERApplicationSpecific2.getApplicationTag());
                    }
                    case 78: {
                        this.certificateBody = CertificateBody.getInstance(dERApplicationSpecific2);
                        this.valid |= bodyValid;
                        continue block4;
                    }
                    case 55: 
                }
                this.innerSignature = dERApplicationSpecific2.getContents();
                this.valid |= signValid;
            }
        } else {
            throw new IOException("not a CARDHOLDER_CERTIFICATE in request:" + dERApplicationSpecific.getApplicationTag());
        }
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

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certificateBody);
        try {
            aSN1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.innerSignature)));
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to convert signature!");
        }
        return new DERApplicationSpecific(33, aSN1EncodableVector);
    }
}

