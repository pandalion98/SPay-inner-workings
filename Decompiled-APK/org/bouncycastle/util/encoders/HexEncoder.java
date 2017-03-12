package org.bouncycastle.util.encoders;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class HexEncoder implements Encoder {
    protected final byte[] decodingTable;
    protected final byte[] encodingTable;

    public HexEncoder() {
        this.encodingTable = new byte[]{(byte) 48, (byte) 49, (byte) 50, ApplicationInfoManager.TERM_XP3, (byte) 52, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 54, (byte) 55, (byte) 56, ApplicationInfoManager.EMV_MS, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102};
        this.decodingTable = new byte[X509KeyUsage.digitalSignature];
        initialiseDecodingTable();
    }

    private static boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    public int decode(String str, OutputStream outputStream) {
        int i = 0;
        int length = str.length();
        while (length > 0 && ignore(str.charAt(length - 1))) {
            length--;
        }
        int i2 = 0;
        while (i < length) {
            int i3 = i;
            while (i3 < length && ignore(str.charAt(i3))) {
                i3++;
            }
            i = i3 + 1;
            byte b = this.decodingTable[str.charAt(i3)];
            while (i < length && ignore(str.charAt(i))) {
                i++;
            }
            i3 = i + 1;
            byte b2 = this.decodingTable[str.charAt(i)];
            if ((b | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }
            outputStream.write(b2 | (b << 4));
            i2++;
            i = i3;
        }
        return i2;
    }

    public int decode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        int i3 = i + i2;
        while (i3 > i && ignore((char) bArr[i3 - 1])) {
            i3--;
        }
        int i4 = 0;
        int i5 = i;
        while (i5 < i3) {
            int i6 = i5;
            while (i6 < i3 && ignore((char) bArr[i6])) {
                i6++;
            }
            i5 = i6 + 1;
            byte b = this.decodingTable[bArr[i6]];
            while (i5 < i3 && ignore((char) bArr[i5])) {
                i5++;
            }
            i = i5 + 1;
            byte b2 = this.decodingTable[bArr[i5]];
            if ((b | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex data");
            }
            outputStream.write(b2 | (b << 4));
            i4++;
            i5 = i;
        }
        return i4;
    }

    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        for (int i3 = i; i3 < i + i2; i3++) {
            int i4 = bArr[i3] & GF2Field.MASK;
            outputStream.write(this.encodingTable[i4 >>> 4]);
            outputStream.write(this.encodingTable[i4 & 15]);
        }
        return i2 * 2;
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
        this.decodingTable[65] = this.decodingTable[97];
        this.decodingTable[66] = this.decodingTable[98];
        this.decodingTable[67] = this.decodingTable[99];
        this.decodingTable[68] = this.decodingTable[100];
        this.decodingTable[69] = this.decodingTable[ExtensionType.negotiated_ff_dhe_groups];
        this.decodingTable[70] = this.decodingTable[EncryptionAlgorithm.AEAD_CHACHA20_POLY1305];
    }
}
