package com.mastercard.mobile_api.utils.tlv;

import com.mastercard.mobile_api.utils.Utils;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class TLVParser {
    public static final byte BYTE_81 = (byte) -127;

    public static void parseTLV(byte[] bArr, int i, int i2, TLVHandler tLVHandler) {
        int i3 = i + i2;
        while (i < i3) {
            try {
                int i4;
                byte b = bArr[i];
                short s = (short) 0;
                if (((byte) (b & 31)) == 31) {
                    s = Utils.readShort(bArr, i);
                    i4 = i + 2;
                } else {
                    i4 = i + 1;
                }
                int tLVLength = BERTLVUtils.getTLVLength(bArr, i4);
                i4 += BERTLVUtils.getTLVLengthByte(bArr, i4);
                if (s == (short) 0) {
                    tLVHandler.parseTag(b, tLVLength, bArr, i4);
                } else {
                    tLVHandler.parseTag(s, tLVLength, bArr, i4);
                }
                i = i4 + tLVLength;
            } catch (NullPointerException e) {
                throw new ParsingException();
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new ParsingException();
            }
        }
    }

    public static int getTlvLength(byte[] bArr, int i) {
        if (bArr[i] == -127) {
            return Utils.readShort(bArr, i) & GF2Field.MASK;
        }
        return bArr[i] & GF2Field.MASK;
    }

    public static void parseTLVNoExtend(byte[] bArr, int i, int i2, TLVHandler tLVHandler) {
        int i3 = i + i2;
        while (i < i3) {
            int i4 = i + 1;
            byte b = bArr[i];
            int tLVLength = BERTLVUtils.getTLVLength(bArr, i4);
            i4 += BERTLVUtils.getTLVLengthByte(bArr, i4);
            tLVHandler.parseTag(b, tLVLength, bArr, i4);
            i = i4 + tLVLength;
        }
    }
}
