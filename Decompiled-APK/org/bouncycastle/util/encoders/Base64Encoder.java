package org.bouncycastle.util.encoders;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class Base64Encoder implements Encoder {
    protected final byte[] decodingTable;
    protected final byte[] encodingTable;
    protected byte padding;

    public Base64Encoder() {
        this.encodingTable = new byte[]{(byte) 65, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 67, (byte) 68, (byte) 69, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, GetTemplateApdu.TAG_DF_NAME_4F, GetTemplateApdu.TAG_APPLICATION_LABEL_50, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) 82, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 84, (byte) 85, (byte) 86, (byte) 87, ApplicationInfoManager.TERM_XP1, (byte) 89, (byte) 90, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 118, ApplicationInfoManager.TERM_XP2, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, ApplicationInfoManager.TERM_XP3, (byte) 52, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 54, (byte) 55, (byte) 56, ApplicationInfoManager.EMV_MS, (byte) 43, (byte) 47};
        this.padding = (byte) 61;
        this.decodingTable = new byte[X509KeyUsage.digitalSignature];
        initialiseDecodingTable();
    }

    private int decodeLastBlock(OutputStream outputStream, char c, char c2, char c3, char c4) {
        byte b;
        byte b2;
        if (c3 == this.padding) {
            b = this.decodingTable[c];
            b2 = this.decodingTable[c2];
            if ((b | b2) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            outputStream.write((b << 2) | (b2 >> 4));
            return 1;
        } else if (c4 == this.padding) {
            b = this.decodingTable[c];
            b2 = this.decodingTable[c2];
            r2 = this.decodingTable[c3];
            if (((b | b2) | r2) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (r2 >> 2));
            return 2;
        } else {
            b = this.decodingTable[c];
            b2 = this.decodingTable[c2];
            r2 = this.decodingTable[c3];
            byte b3 = this.decodingTable[c4];
            if ((((b | b2) | r2) | b3) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (r2 >> 2));
            outputStream.write((r2 << 6) | b3);
            return 3;
        }
    }

    private boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    private int nextI(String str, int i, int i2) {
        while (i < i2 && ignore(str.charAt(i))) {
            i++;
        }
        return i;
    }

    private int nextI(byte[] bArr, int i, int i2) {
        while (i < i2 && ignore((char) bArr[i])) {
            i++;
        }
        return i;
    }

    public int decode(String str, OutputStream outputStream) {
        int length = str.length();
        while (length > 0 && ignore(str.charAt(length - 1))) {
            length--;
        }
        int i = length - 4;
        int nextI = nextI(str, 0, i);
        int i2 = 0;
        while (nextI < i) {
            int i3 = nextI + 1;
            byte b = this.decodingTable[str.charAt(nextI)];
            int nextI2 = nextI(str, i3, i);
            int i4 = nextI2 + 1;
            byte b2 = this.decodingTable[str.charAt(nextI2)];
            i3 = nextI(str, i4, i);
            int i5 = i3 + 1;
            byte b3 = this.decodingTable[str.charAt(i3)];
            i4 = nextI(str, i5, i);
            int i6 = i4 + 1;
            byte b4 = this.decodingTable[str.charAt(i4)];
            if ((((b | b2) | b3) | b4) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            nextI2 = i2 + 3;
            nextI = nextI(str, i6, i);
            i2 = nextI2;
        }
        return decodeLastBlock(outputStream, str.charAt(length - 4), str.charAt(length - 3), str.charAt(length - 2), str.charAt(length - 1)) + i2;
    }

    public int decode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        int i3 = i + i2;
        while (i3 > i && ignore((char) bArr[i3 - 1])) {
            i3--;
        }
        int i4 = i3 - 4;
        int nextI = nextI(bArr, i, i4);
        int i5 = 0;
        while (nextI < i4) {
            int i6 = nextI + 1;
            byte b = this.decodingTable[bArr[nextI]];
            int nextI2 = nextI(bArr, i6, i4);
            int i7 = nextI2 + 1;
            byte b2 = this.decodingTable[bArr[nextI2]];
            i6 = nextI(bArr, i7, i4);
            int i8 = i6 + 1;
            byte b3 = this.decodingTable[bArr[i6]];
            i7 = nextI(bArr, i8, i4);
            int i9 = i7 + 1;
            byte b4 = this.decodingTable[bArr[i7]];
            if ((((b | b2) | b3) | b4) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            nextI2 = i5 + 3;
            nextI = nextI(bArr, i9, i4);
            i5 = nextI2;
        }
        return decodeLastBlock(outputStream, (char) bArr[i3 - 4], (char) bArr[i3 - 3], (char) bArr[i3 - 2], (char) bArr[i3 - 1]) + i5;
    }

    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        int i3;
        int i4 = i2 % 3;
        int i5 = i2 - i4;
        for (i3 = i; i3 < i + i5; i3 += 3) {
            int i6 = bArr[i3] & GF2Field.MASK;
            int i7 = bArr[i3 + 1] & GF2Field.MASK;
            int i8 = bArr[i3 + 2] & GF2Field.MASK;
            outputStream.write(this.encodingTable[(i6 >>> 2) & 63]);
            outputStream.write(this.encodingTable[((i6 << 4) | (i7 >>> 4)) & 63]);
            outputStream.write(this.encodingTable[((i7 << 2) | (i8 >>> 6)) & 63]);
            outputStream.write(this.encodingTable[i8 & 63]);
        }
        switch (i4) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                i3 = bArr[i + i5] & GF2Field.MASK;
                i6 = (i3 >>> 2) & 63;
                i3 = (i3 << 4) & 63;
                outputStream.write(this.encodingTable[i6]);
                outputStream.write(this.encodingTable[i3]);
                outputStream.write(this.padding);
                outputStream.write(this.padding);
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                i3 = bArr[i + i5] & GF2Field.MASK;
                i6 = bArr[(i + i5) + 1] & GF2Field.MASK;
                i7 = (i3 >>> 2) & 63;
                i3 = ((i3 << 4) | (i6 >>> 4)) & 63;
                i6 = (i6 << 2) & 63;
                outputStream.write(this.encodingTable[i7]);
                outputStream.write(this.encodingTable[i3]);
                outputStream.write(this.encodingTable[i6]);
                outputStream.write(this.padding);
                break;
        }
        return (i4 == 0 ? 0 : 4) + ((i5 / 3) * 4);
    }

    protected void initialiseDecodingTable() {
        int i = 0;
        for (int i2 = 0; i2 < this.decodingTable.length; i2++) {
            this.decodingTable[i2] = (byte) -1;
        }
        while (i < this.encodingTable.length) {
            this.decodingTable[this.encodingTable[i]] = (byte) i;
            i++;
        }
    }
}
