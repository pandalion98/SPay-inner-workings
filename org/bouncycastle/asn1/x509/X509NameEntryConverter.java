package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.util.Strings;

public abstract class X509NameEntryConverter {
    protected boolean canBePrintable(String str) {
        return DERPrintableString.isPrintableString(str);
    }

    protected ASN1Primitive convertHexEncoded(String str, int i) {
        String toLowerCase = Strings.toLowerCase(str);
        byte[] bArr = new byte[((toLowerCase.length() - i) / 2)];
        for (int i2 = 0; i2 != bArr.length; i2++) {
            char charAt = toLowerCase.charAt((i2 * 2) + i);
            char charAt2 = toLowerCase.charAt(((i2 * 2) + i) + 1);
            if (charAt < 'a') {
                bArr[i2] = (byte) ((charAt - 48) << 4);
            } else {
                bArr[i2] = (byte) (((charAt - 97) + 10) << 4);
            }
            if (charAt2 < 'a') {
                bArr[i2] = (byte) (bArr[i2] | ((byte) (charAt2 - 48)));
            } else {
                bArr[i2] = (byte) (bArr[i2] | ((byte) ((charAt2 - 97) + 10)));
            }
        }
        return new ASN1InputStream(bArr).readObject();
    }

    public abstract ASN1Primitive getConvertedValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str);
}
