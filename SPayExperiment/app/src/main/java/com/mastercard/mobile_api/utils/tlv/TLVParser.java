/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.NullPointerException
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.tlv;

import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.BERTLVUtils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;

public class TLVParser {
    public static final byte BYTE_81 = -127;

    public static int getTlvLength(byte[] arrby, int n2) {
        if (arrby[n2] == -127) {
            return 255 & Utils.readShort(arrby, n2);
        }
        return 255 & arrby[n2];
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void parseTLV(byte[] arrby, int n2, int n3, TLVHandler tLVHandler) {
        int n4 = n2 + n3;
        do {
            byte by;
            short s2;
            int n5;
            if (n2 >= n4) {
                return;
            }
            try {
                by = arrby[n2];
            }
            catch (NullPointerException nullPointerException) {
                throw new ParsingException();
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                throw new ParsingException();
            }
            if ((byte)(by & 31) == 31) {
                s2 = Utils.readShort(arrby, n2);
                n5 = n2 + 2;
            }
            n5 = n2 + 1;
            s2 = 0;
            int n6 = BERTLVUtils.getTLVLength(arrby, n5);
            int n7 = n5 + BERTLVUtils.getTLVLengthByte(arrby, n5);
            if (s2 == 0) {
                tLVHandler.parseTag(by, n6, arrby, n7);
            } else {
                tLVHandler.parseTag(s2, n6, arrby, n7);
            }
            n2 = n7 + n6;
        } while (true);
    }

    public static void parseTLVNoExtend(byte[] arrby, int n2, int n3, TLVHandler tLVHandler) {
        int n4 = n2 + n3;
        while (n2 < n4) {
            int n5 = n2 + 1;
            byte by = arrby[n2];
            int n6 = BERTLVUtils.getTLVLength(arrby, n5);
            int n7 = n5 + BERTLVUtils.getTLVLengthByte(arrby, n5);
            tLVHandler.parseTag(by, n6, arrby, n7);
            n2 = n7 + n6;
        }
        return;
    }
}

