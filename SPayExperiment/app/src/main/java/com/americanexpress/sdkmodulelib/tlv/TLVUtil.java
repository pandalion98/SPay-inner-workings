/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.BERTLV;
import com.americanexpress.sdkmodulelib.tlv.EMVTags;
import com.americanexpress.sdkmodulelib.tlv.TLVException;
import com.americanexpress.sdkmodulelib.tlv.Tag;
import com.americanexpress.sdkmodulelib.tlv.TagAndLength;
import com.americanexpress.sdkmodulelib.tlv.TagValueType;
import com.americanexpress.sdkmodulelib.tlv.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TLVUtil {
    /*
     * Enabled aggressive block sorting
     */
    public static String getFormattedTagAndLength(byte[] arrby, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        String string = Util.getSpaces(n2);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        boolean bl = true;
        while (byteArrayInputStream.available() > 0) {
            if (bl) {
                bl = false;
            } else {
                stringBuilder.append("\n");
            }
            stringBuilder.append(string);
            Tag tag = TLVUtil.searchTagById(byteArrayInputStream);
            int n3 = TLVUtil.readTagLength(byteArrayInputStream);
            stringBuilder.append(Util.prettyPrintHex(tag.getTagBytes()));
            stringBuilder.append(" ");
            stringBuilder.append(Util.byteArrayToHexString(Util.intToByteArray(n3)));
            stringBuilder.append(" -- ");
            stringBuilder.append(tag.getName());
        }
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static BERTLV getNextTLV(ByteArrayInputStream var0) {
        block10 : {
            if (var0.available() < 2) {
                throw new TLVException("Error parsing data. Available bytes < 2 . Length=" + var0.available());
            }
            var0.mark(0);
            var1_1 = var0.read();
            var2_2 = (byte)var1_1;
            while (var1_1 != -1 && (var2_2 == -1 || var2_2 == 0)) {
                var0.mark(0);
                var1_1 = var0.read();
                var2_2 = (byte)var1_1;
            }
            var0.reset();
            if (var0.available() < 2) {
                throw new TLVException("Error parsing data. Available bytes < 2 . Length=" + var0.available());
            }
            var3_3 = TLVUtil.readTagIdBytes(var0);
            var0.mark(0);
            var4_4 = var0.available();
            var5_5 = TLVUtil.readTagLength(var0);
            var6_6 = var0.available();
            var0.reset();
            var7_7 = new byte[var4_4 - var6_6];
            if (var7_7.length < 1) throw new TLVException("Number of length bytes must be from 1 to 4. Found " + var7_7.length);
            if (var7_7.length > 4) {
                throw new TLVException("Number of length bytes must be from 1 to 4. Found " + var7_7.length);
            }
            var0.read(var7_7, 0, var7_7.length);
            var9_8 = Util.byteArrayToInt(var7_7);
            var10_9 = TLVUtil.searchTagById(var3_3);
            if (var9_8 == 128) break block10;
            if (var0.available() < var5_5) {
                var15_16 = new StringBuilder().append("Length byte(s) indicated ").append(var5_5).append(" value bytes, but only ").append(var0.available()).append(" ");
                if (var0.available() > 1) {
                    var16_17 = "are";
                    throw new TLVException(var15_16.append(var16_17).append(" available").toString());
                }
                var16_17 = "is";
                throw new TLVException(var15_16.append(var16_17).append(" available").toString());
            }
            var11_13 = new byte[var5_5];
            var0.read(var11_13, 0, var5_5);
            ** GOTO lbl52
        }
        var0.mark(0);
        var17_10 = 1;
        var18_11 = 0;
        do {
            block11 : {
                ++var18_11;
                var19_12 = var0.read();
                if (var19_12 < 0) {
                    throw new TLVException("Error parsing data. TLV length byte indicated indefinite length, but EOS was reached before 0x0000 was found" + var0.available());
                }
                if (var17_10 != 0 || var19_12 != 0) break block11;
                var5_5 = var18_11 - 2;
                var11_13 = new byte[var5_5];
                var0.reset();
                var0.read(var11_13, 0, var5_5);
lbl52: // 2 sources:
                var0.mark(0);
                var13_14 = var0.read();
                var14_15 = (byte)var13_14;
                while (var13_14 != -1 && (var14_15 == -1 || var14_15 == 0)) {
                    var0.mark(0);
                    var13_14 = var0.read();
                    var14_15 = (byte)var13_14;
                }
                break;
            }
            var17_10 = var19_12;
        } while (true);
        var0.reset();
        return new BERTLV(var10_9, var5_5, var7_7, var11_13);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String getTagValueAsString(Tag tag, byte[] arrby) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (1.$SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[tag.getTagValueType().ordinal()]) {
            case 1: {
                stringBuilder.append("=");
                stringBuilder.append(new String(arrby));
                return stringBuilder.toString();
            }
            case 2: {
                stringBuilder.append("NUMERIC");
                return stringBuilder.toString();
            }
            case 3: {
                stringBuilder.append("BINARY");
                return stringBuilder.toString();
            }
            case 4: {
                stringBuilder.append("=");
                stringBuilder.append(Util.getSafePrintChars(arrby));
                return stringBuilder.toString();
            }
            case 5: {
                stringBuilder.append("");
                break;
            }
        }
        return stringBuilder.toString();
    }

    public static List<TagAndLength> parseTagAndLength(byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        ArrayList arrayList = new ArrayList();
        while (byteArrayInputStream.available() > 0) {
            if (byteArrayInputStream.available() < 2) {
                throw new TLVException("Data length < 2 : " + byteArrayInputStream.available());
            }
            byte[] arrby2 = TLVUtil.readTagIdBytes(byteArrayInputStream);
            int n2 = TLVUtil.readTagLength(byteArrayInputStream);
            arrayList.add((Object)new TagAndLength(TLVUtil.searchTagById(arrby2), n2));
        }
        return arrayList;
    }

    public static String prettyPrintAPDUResponse(byte[] arrby) {
        return TLVUtil.prettyPrintAPDUResponse(arrby, 0);
    }

    public static String prettyPrintAPDUResponse(byte[] arrby, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        while (byteArrayInputStream.available() > 0) {
            stringBuilder.append("\n");
            stringBuilder.append(Util.getSpaces(n2));
            BERTLV bERTLV = TLVUtil.getNextTLV(byteArrayInputStream);
            byte[] arrby2 = bERTLV.getTagBytes();
            byte[] arrby3 = bERTLV.getRawEncodedLengthBytes();
            byte[] arrby4 = bERTLV.getValueBytes();
            Tag tag = bERTLV.getTag();
            stringBuilder.append(Util.prettyPrintHex(arrby2));
            stringBuilder.append(" ");
            stringBuilder.append(Util.prettyPrintHex(arrby3));
            stringBuilder.append(" -- ");
            stringBuilder.append(tag.getName());
            int n3 = 3 * arrby3.length + 3 * arrby2.length;
            if (tag.isConstructed()) {
                stringBuilder.append(TLVUtil.prettyPrintAPDUResponse(arrby4, n2 + n3));
                continue;
            }
            stringBuilder.append("\n");
            if (tag.getTagValueType() == TagValueType.DOL) {
                stringBuilder.append(TLVUtil.getFormattedTagAndLength(arrby4, n2 + n3));
                continue;
            }
            stringBuilder.append(Util.getSpaces(n2 + n3));
            stringBuilder.append(Util.prettyPrintHex(Util.byteArrayToHexString(arrby4), n3 + n2));
            stringBuilder.append(" (");
            stringBuilder.append(TLVUtil.getTagValueAsString(tag, arrby4));
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public static String prettyPrintAPDUResponse(byte[] arrby, int n2, int n3) {
        byte[] arrby2 = new byte[n3 - n2];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        return TLVUtil.prettyPrintAPDUResponse(arrby2, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] readTagIdBytes(ByteArrayInputStream byteArrayInputStream) {
        byte by;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte by2 = (byte)byteArrayInputStream.read();
        byteArrayOutputStream.write((int)by2);
        if ((by2 & 31) != 31) return byteArrayOutputStream.toByteArray();
        do {
            int n2;
            if ((n2 = byteArrayInputStream.read()) < 0) {
                return byteArrayOutputStream.toByteArray();
            }
            by = (byte)n2;
            byteArrayOutputStream.write((int)by);
            if (!Util.isBitSet(by, 8)) return byteArrayOutputStream.toByteArray();
        } while (!Util.isBitSet(by, 8) || (by & 127) != 0);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int readTagLength(ByteArrayInputStream byteArrayInputStream) {
        int n2 = byteArrayInputStream.read();
        if (n2 < 0) {
            throw new TLVException("Negative length: " + n2);
        }
        if (n2 > 127 && n2 != 128) {
            int n3 = n2 & 127;
            n2 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4 = byteArrayInputStream.read();
                if (n4 < 0) {
                    throw new TLVException("EOS when reading length bytes");
                }
                int n5 = n4 | n2 << 8;
                n2 = n5;
            }
        }
        return n2;
    }

    private static Tag searchTagById(ByteArrayInputStream byteArrayInputStream) {
        return TLVUtil.searchTagById(TLVUtil.readTagIdBytes(byteArrayInputStream));
    }

    private static Tag searchTagById(byte[] arrby) {
        return EMVTags.getNotNull(arrby);
    }

}

