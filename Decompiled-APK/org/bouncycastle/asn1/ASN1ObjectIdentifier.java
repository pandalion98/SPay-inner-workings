package org.bouncycastle.asn1;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class ASN1ObjectIdentifier extends ASN1Primitive {
    private static final long LONG_LIMIT = 72057594037927808L;
    private static ASN1ObjectIdentifier[][] cache;
    private byte[] body;
    String identifier;

    static {
        cache = new ASN1ObjectIdentifier[SkeinMac.SKEIN_256][];
    }

    public ASN1ObjectIdentifier(String str) {
        if (str == null) {
            throw new IllegalArgumentException("'identifier' cannot be null");
        } else if (isValidIdentifier(str)) {
            this.identifier = str;
        } else {
            throw new IllegalArgumentException("string " + str + " not an OID");
        }
    }

    ASN1ObjectIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str) {
        if (isValidBranchID(str, 0)) {
            this.identifier = aSN1ObjectIdentifier.getId() + "." + str;
            return;
        }
        throw new IllegalArgumentException("string " + str + " not a valid OID branch");
    }

    ASN1ObjectIdentifier(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        Object obj = 1;
        BigInteger bigInteger = null;
        long j = 0;
        for (int i = 0; i != bArr.length; i++) {
            int i2 = bArr[i] & GF2Field.MASK;
            if (j <= LONG_LIMIT) {
                j += (long) (i2 & CertificateBody.profileType);
                if ((i2 & X509KeyUsage.digitalSignature) == 0) {
                    if (obj != null) {
                        if (j < 40) {
                            stringBuffer.append(LLVARUtil.EMPTY_STRING);
                        } else if (j < 80) {
                            stringBuffer.append(LLVARUtil.PLAIN_TEXT);
                            j -= 40;
                        } else {
                            stringBuffer.append(LLVARUtil.HEX_STRING);
                            j -= 80;
                        }
                        obj = null;
                    }
                    stringBuffer.append('.');
                    stringBuffer.append(j);
                    j = 0;
                } else {
                    j <<= 7;
                }
            } else {
                if (bigInteger == null) {
                    bigInteger = BigInteger.valueOf(j);
                }
                bigInteger = bigInteger.or(BigInteger.valueOf((long) (i2 & CertificateBody.profileType)));
                if ((i2 & X509KeyUsage.digitalSignature) == 0) {
                    Object obj2;
                    Object obj3;
                    if (obj != null) {
                        stringBuffer.append(LLVARUtil.HEX_STRING);
                        obj = bigInteger.subtract(BigInteger.valueOf(80));
                        obj2 = null;
                    } else {
                        obj3 = obj;
                        BigInteger bigInteger2 = bigInteger;
                        obj2 = obj3;
                    }
                    stringBuffer.append('.');
                    stringBuffer.append(obj);
                    j = 0;
                    obj3 = obj2;
                    bigInteger = null;
                    obj = obj3;
                } else {
                    bigInteger = bigInteger.shiftLeft(7);
                }
            }
        }
        this.identifier = stringBuffer.toString();
        this.body = Arrays.clone(bArr);
    }

    private void doOutput(ByteArrayOutputStream byteArrayOutputStream) {
        OIDTokenizer oIDTokenizer = new OIDTokenizer(this.identifier);
        int parseInt = Integer.parseInt(oIDTokenizer.nextToken()) * 40;
        String nextToken = oIDTokenizer.nextToken();
        if (nextToken.length() <= 18) {
            writeField(byteArrayOutputStream, Long.parseLong(nextToken) + ((long) parseInt));
        } else {
            writeField(byteArrayOutputStream, new BigInteger(nextToken).add(BigInteger.valueOf((long) parseInt)));
        }
        while (oIDTokenizer.hasMoreTokens()) {
            String nextToken2 = oIDTokenizer.nextToken();
            if (nextToken2.length() <= 18) {
                writeField(byteArrayOutputStream, Long.parseLong(nextToken2));
            } else {
                writeField(byteArrayOutputStream, new BigInteger(nextToken2));
            }
        }
    }

    static ASN1ObjectIdentifier fromOctetString(byte[] bArr) {
        if (bArr.length < 3) {
            return new ASN1ObjectIdentifier(bArr);
        }
        int i = bArr[bArr.length - 2] & GF2Field.MASK;
        int i2 = bArr[bArr.length - 1] & CertificateBody.profileType;
        synchronized (cache) {
            ASN1ObjectIdentifier[] aSN1ObjectIdentifierArr;
            ASN1ObjectIdentifier[] aSN1ObjectIdentifierArr2 = cache[i];
            if (aSN1ObjectIdentifierArr2 == null) {
                aSN1ObjectIdentifierArr2 = new ASN1ObjectIdentifier[X509KeyUsage.digitalSignature];
                cache[i] = aSN1ObjectIdentifierArr2;
                aSN1ObjectIdentifierArr = aSN1ObjectIdentifierArr2;
            } else {
                aSN1ObjectIdentifierArr = aSN1ObjectIdentifierArr2;
            }
            ASN1ObjectIdentifier aSN1ObjectIdentifier = aSN1ObjectIdentifierArr[i2];
            if (aSN1ObjectIdentifier == null) {
                aSN1ObjectIdentifier = new ASN1ObjectIdentifier(bArr);
                aSN1ObjectIdentifierArr[i2] = aSN1ObjectIdentifier;
                return aSN1ObjectIdentifier;
            } else if (Arrays.areEqual(bArr, aSN1ObjectIdentifier.getBody())) {
                return aSN1ObjectIdentifier;
            } else {
                int i3 = (i + 1) & GF2Field.MASK;
                aSN1ObjectIdentifierArr2 = cache[i3];
                if (aSN1ObjectIdentifierArr2 == null) {
                    aSN1ObjectIdentifierArr2 = new ASN1ObjectIdentifier[X509KeyUsage.digitalSignature];
                    cache[i3] = aSN1ObjectIdentifierArr2;
                    aSN1ObjectIdentifierArr = aSN1ObjectIdentifierArr2;
                } else {
                    aSN1ObjectIdentifierArr = aSN1ObjectIdentifierArr2;
                }
                aSN1ObjectIdentifier = aSN1ObjectIdentifierArr[i2];
                if (aSN1ObjectIdentifier == null) {
                    aSN1ObjectIdentifier = new ASN1ObjectIdentifier(bArr);
                    aSN1ObjectIdentifierArr[i2] = aSN1ObjectIdentifier;
                    return aSN1ObjectIdentifier;
                } else if (Arrays.areEqual(bArr, aSN1ObjectIdentifier.getBody())) {
                    return aSN1ObjectIdentifier;
                } else {
                    i = (i2 + 1) & CertificateBody.profileType;
                    aSN1ObjectIdentifier = aSN1ObjectIdentifierArr[i];
                    if (aSN1ObjectIdentifier == null) {
                        aSN1ObjectIdentifier = new ASN1ObjectIdentifier(bArr);
                        aSN1ObjectIdentifierArr[i] = aSN1ObjectIdentifier;
                        return aSN1ObjectIdentifier;
                    }
                    return !Arrays.areEqual(bArr, aSN1ObjectIdentifier.getBody()) ? new ASN1ObjectIdentifier(bArr) : aSN1ObjectIdentifier;
                }
            }
        }
    }

    public static ASN1ObjectIdentifier getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1ObjectIdentifier)) {
            return (ASN1ObjectIdentifier) obj;
        }
        if ((obj instanceof ASN1Encodable) && (((ASN1Encodable) obj).toASN1Primitive() instanceof ASN1ObjectIdentifier)) {
            return (ASN1ObjectIdentifier) ((ASN1Encodable) obj).toASN1Primitive();
        }
        if (obj instanceof byte[]) {
            try {
                return (ASN1ObjectIdentifier) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct object identifier from byte[]: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1ObjectIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof ASN1ObjectIdentifier)) ? getInstance(object) : fromOctetString(ASN1OctetString.getInstance(aSN1TaggedObject.getObject()).getOctets());
    }

    private static boolean isValidBranchID(String str, int i) {
        int length = str.length();
        boolean z = false;
        while (true) {
            length--;
            if (length < i) {
                return z;
            }
            char charAt = str.charAt(length);
            if (LLVARUtil.EMPTY_STRING <= charAt && charAt <= '9') {
                z = true;
            } else if (charAt != '.' || !z) {
                return false;
            } else {
                z = false;
            }
        }
    }

    private static boolean isValidIdentifier(String str) {
        if (str.length() < 3 || str.charAt(1) != '.') {
            return false;
        }
        char charAt = str.charAt(0);
        return (charAt < LLVARUtil.EMPTY_STRING || charAt > LLVARUtil.HEX_STRING) ? false : isValidBranchID(str, 2);
    }

    private void writeField(ByteArrayOutputStream byteArrayOutputStream, long j) {
        byte[] bArr = new byte[9];
        int i = 8;
        bArr[8] = (byte) (((int) j) & CertificateBody.profileType);
        while (j >= 128) {
            j >>= 7;
            i--;
            bArr[i] = (byte) ((((int) j) & CertificateBody.profileType) | X509KeyUsage.digitalSignature);
        }
        byteArrayOutputStream.write(bArr, i, 9 - i);
    }

    private void writeField(ByteArrayOutputStream byteArrayOutputStream, BigInteger bigInteger) {
        int bitLength = (bigInteger.bitLength() + 6) / 7;
        if (bitLength == 0) {
            byteArrayOutputStream.write(0);
            return;
        }
        int i;
        byte[] bArr = new byte[bitLength];
        for (i = bitLength - 1; i >= 0; i--) {
            bArr[i] = (byte) ((bigInteger.intValue() & CertificateBody.profileType) | X509KeyUsage.digitalSignature);
            bigInteger = bigInteger.shiftRight(7);
        }
        i = bitLength - 1;
        bArr[i] = (byte) (bArr[i] & CertificateBody.profileType);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        return !(aSN1Primitive instanceof ASN1ObjectIdentifier) ? false : this.identifier.equals(((ASN1ObjectIdentifier) aSN1Primitive).identifier);
    }

    public ASN1ObjectIdentifier branch(String str) {
        return new ASN1ObjectIdentifier(this, str);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        byte[] body = getBody();
        aSN1OutputStream.write(6);
        aSN1OutputStream.writeLength(body.length);
        aSN1OutputStream.write(body);
    }

    int encodedLength() {
        int length = getBody().length;
        return length + (StreamUtil.calculateBodyLength(length) + 1);
    }

    protected synchronized byte[] getBody() {
        if (this.body == null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doOutput(byteArrayOutputStream);
            this.body = byteArrayOutputStream.toByteArray();
        }
        return this.body;
    }

    public String getId() {
        return this.identifier;
    }

    public int hashCode() {
        return this.identifier.hashCode();
    }

    boolean isConstructed() {
        return false;
    }

    public boolean on(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        String id = getId();
        String id2 = aSN1ObjectIdentifier.getId();
        return id.length() > id2.length() && id.charAt(id2.length()) == '.' && id.startsWith(id2);
    }

    public String toString() {
        return getId();
    }
}
