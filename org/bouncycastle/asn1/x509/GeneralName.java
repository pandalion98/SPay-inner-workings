package org.bouncycastle.asn1.x509;

import java.io.IOException;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.util.IPAddress;

public class GeneralName extends ASN1Object implements ASN1Choice {
    public static final int dNSName = 2;
    public static final int directoryName = 4;
    public static final int ediPartyName = 5;
    public static final int iPAddress = 7;
    public static final int otherName = 0;
    public static final int registeredID = 8;
    public static final int rfc822Name = 1;
    public static final int uniformResourceIdentifier = 6;
    public static final int x400Address = 3;
    private ASN1Encodable obj;
    private int tag;

    public GeneralName(int i, String str) {
        this.tag = i;
        if (i == rfc822Name || i == dNSName || i == uniformResourceIdentifier) {
            this.obj = new DERIA5String(str);
        } else if (i == registeredID) {
            this.obj = new ASN1ObjectIdentifier(str);
        } else if (i == directoryName) {
            this.obj = new X500Name(str);
        } else if (i == iPAddress) {
            byte[] toGeneralNameEncoding = toGeneralNameEncoding(str);
            if (toGeneralNameEncoding != null) {
                this.obj = new DEROctetString(toGeneralNameEncoding);
                return;
            }
            throw new IllegalArgumentException("IP Address is invalid");
        } else {
            throw new IllegalArgumentException("can't process String for tag: " + i);
        }
    }

    public GeneralName(int i, ASN1Encodable aSN1Encodable) {
        this.obj = aSN1Encodable;
        this.tag = i;
    }

    public GeneralName(X500Name x500Name) {
        this.obj = x500Name;
        this.tag = directoryName;
    }

    public GeneralName(X509Name x509Name) {
        this.obj = X500Name.getInstance(x509Name);
        this.tag = directoryName;
    }

    private void copyInts(int[] iArr, byte[] bArr, int i) {
        for (int i2 = otherName; i2 != iArr.length; i2 += rfc822Name) {
            bArr[(i2 * dNSName) + i] = (byte) (iArr[i2] >> registeredID);
            bArr[((i2 * dNSName) + rfc822Name) + i] = (byte) iArr[i2];
        }
    }

    public static GeneralName getInstance(Object obj) {
        if (obj == null || (obj instanceof GeneralName)) {
            return (GeneralName) obj;
        }
        if (obj instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) obj;
            int tagNo = aSN1TaggedObject.getTagNo();
            switch (tagNo) {
                case otherName /*0*/:
                    return new GeneralName(tagNo, ASN1Sequence.getInstance(aSN1TaggedObject, false));
                case rfc822Name /*1*/:
                    return new GeneralName(tagNo, DERIA5String.getInstance(aSN1TaggedObject, false));
                case dNSName /*2*/:
                    return new GeneralName(tagNo, DERIA5String.getInstance(aSN1TaggedObject, false));
                case x400Address /*3*/:
                    throw new IllegalArgumentException("unknown tag: " + tagNo);
                case directoryName /*4*/:
                    return new GeneralName(tagNo, X500Name.getInstance(aSN1TaggedObject, true));
                case ediPartyName /*5*/:
                    return new GeneralName(tagNo, ASN1Sequence.getInstance(aSN1TaggedObject, false));
                case uniformResourceIdentifier /*6*/:
                    return new GeneralName(tagNo, DERIA5String.getInstance(aSN1TaggedObject, false));
                case iPAddress /*7*/:
                    return new GeneralName(tagNo, ASN1OctetString.getInstance(aSN1TaggedObject, false));
                case registeredID /*8*/:
                    return new GeneralName(tagNo, ASN1ObjectIdentifier.getInstance(aSN1TaggedObject, false));
            }
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("unable to parse encoded general name");
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    public static GeneralName getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1TaggedObject.getInstance(aSN1TaggedObject, true));
    }

    private void parseIPv4(String str, byte[] bArr, int i) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "./");
        int i2 = otherName;
        while (stringTokenizer.hasMoreTokens()) {
            int i3 = i2 + rfc822Name;
            bArr[i2 + i] = (byte) Integer.parseInt(stringTokenizer.nextToken());
            i2 = i3;
        }
    }

    private void parseIPv4Mask(String str, byte[] bArr, int i) {
        int parseInt = Integer.parseInt(str);
        for (int i2 = otherName; i2 != parseInt; i2 += rfc822Name) {
            int i3 = (i2 / registeredID) + i;
            bArr[i3] = (byte) (bArr[i3] | (rfc822Name << (7 - (i2 % registeredID))));
        }
    }

    private int[] parseIPv6(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ":", true);
        Object obj = new int[registeredID];
        if (str.charAt(otherName) == ':' && str.charAt(rfc822Name) == ':') {
            stringTokenizer.nextToken();
        }
        int i = -1;
        int i2 = otherName;
        while (stringTokenizer.hasMoreTokens()) {
            int i3;
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.equals(":")) {
                i = i2 + rfc822Name;
                obj[i2] = otherName;
                int i4 = i2;
                i2 = i;
                i = i4;
            } else if (nextToken.indexOf(46) < 0) {
                i3 = i2 + rfc822Name;
                obj[i2] = Integer.parseInt(nextToken, 16);
                if (stringTokenizer.hasMoreTokens()) {
                    stringTokenizer.nextToken();
                    i2 = i3;
                } else {
                    i2 = i3;
                }
            } else {
                StringTokenizer stringTokenizer2 = new StringTokenizer(nextToken, ".");
                int i5 = i2 + rfc822Name;
                obj[i2] = (Integer.parseInt(stringTokenizer2.nextToken()) << registeredID) | Integer.parseInt(stringTokenizer2.nextToken());
                i2 = i5 + rfc822Name;
                obj[i5] = Integer.parseInt(stringTokenizer2.nextToken()) | (Integer.parseInt(stringTokenizer2.nextToken()) << registeredID);
            }
        }
        if (i2 != obj.length) {
            System.arraycopy(obj, i, obj, obj.length - (i2 - i), i2 - i);
            for (i3 = i; i3 != obj.length - (i2 - i); i3 += rfc822Name) {
                obj[i3] = otherName;
            }
        }
        return obj;
    }

    private int[] parseMask(String str) {
        int[] iArr = new int[registeredID];
        int parseInt = Integer.parseInt(str);
        for (int i = otherName; i != parseInt; i += rfc822Name) {
            int i2 = i / 16;
            iArr[i2] = iArr[i2] | (rfc822Name << (15 - (i % 16)));
        }
        return iArr;
    }

    private byte[] toGeneralNameEncoding(String str) {
        byte[] bArr;
        if (IPAddress.isValidIPv6WithNetmask(str) || IPAddress.isValidIPv6(str)) {
            int indexOf = str.indexOf(47);
            if (indexOf < 0) {
                bArr = new byte[16];
                copyInts(parseIPv6(str), bArr, otherName);
                return bArr;
            }
            byte[] bArr2 = new byte[32];
            copyInts(parseIPv6(str.substring(otherName, indexOf)), bArr2, otherName);
            String substring = str.substring(indexOf + rfc822Name);
            copyInts(substring.indexOf(58) > 0 ? parseIPv6(substring) : parseMask(substring), bArr2, 16);
            return bArr2;
        } else if (!IPAddress.isValidIPv4WithNetmask(str) && !IPAddress.isValidIPv4(str)) {
            return null;
        } else {
            int indexOf2 = str.indexOf(47);
            if (indexOf2 < 0) {
                bArr = new byte[directoryName];
                parseIPv4(str, bArr, otherName);
                return bArr;
            }
            bArr = new byte[registeredID];
            parseIPv4(str.substring(otherName, indexOf2), bArr, otherName);
            String substring2 = str.substring(indexOf2 + rfc822Name);
            if (substring2.indexOf(46) > 0) {
                parseIPv4(substring2, bArr, directoryName);
                return bArr;
            }
            parseIPv4Mask(substring2, bArr, directoryName);
            return bArr;
        }
    }

    public ASN1Encodable getName() {
        return this.obj;
    }

    public int getTagNo() {
        return this.tag;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.tag == directoryName ? new DERTaggedObject(true, this.tag, this.obj) : new DERTaggedObject(false, this.tag, this.obj);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tag);
        stringBuffer.append(": ");
        switch (this.tag) {
            case rfc822Name /*1*/:
            case dNSName /*2*/:
            case uniformResourceIdentifier /*6*/:
                stringBuffer.append(DERIA5String.getInstance(this.obj).getString());
                break;
            case directoryName /*4*/:
                stringBuffer.append(X500Name.getInstance(this.obj).toString());
                break;
            default:
                stringBuffer.append(this.obj.toString());
                break;
        }
        return stringBuffer.toString();
    }
}
