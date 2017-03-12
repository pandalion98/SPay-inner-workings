package com.americanexpress.sdkmodulelib.tlv;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TLVUtil {

    /* renamed from: com.americanexpress.sdkmodulelib.tlv.TLVUtil.1 */
    static /* synthetic */ class C00751 {
        static final /* synthetic */ int[] $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType;

        static {
            $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType = new int[TagValueType.values().length];
            try {
                $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[TagValueType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[TagValueType.NUMERIC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[TagValueType.BINARY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[TagValueType.MIXED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[TagValueType.DOL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private static Tag searchTagById(byte[] bArr) {
        return EMVTags.getNotNull(bArr);
    }

    private static Tag searchTagById(ByteArrayInputStream byteArrayInputStream) {
        return searchTagById(readTagIdBytes(byteArrayInputStream));
    }

    public static String getFormattedTagAndLength(byte[] bArr, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        String spaces = Util.getSpaces(i);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Object obj = 1;
        while (byteArrayInputStream.available() > 0) {
            if (obj != null) {
                obj = null;
            } else {
                stringBuilder.append("\n");
            }
            stringBuilder.append(spaces);
            Tag searchTagById = searchTagById(byteArrayInputStream);
            int readTagLength = readTagLength(byteArrayInputStream);
            stringBuilder.append(Util.prettyPrintHex(searchTagById.getTagBytes()));
            stringBuilder.append(" ");
            stringBuilder.append(Util.byteArrayToHexString(Util.intToByteArray(readTagLength)));
            stringBuilder.append(" -- ");
            stringBuilder.append(searchTagById.getName());
        }
        return stringBuilder.toString();
    }

    public static byte[] readTagIdBytes(ByteArrayInputStream byteArrayInputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte read = (byte) byteArrayInputStream.read();
        byteArrayOutputStream.write(read);
        if ((read & 31) == 31) {
            while (true) {
                int read2 = byteArrayInputStream.read();
                if (read2 >= 0) {
                    read = (byte) read2;
                    byteArrayOutputStream.write(read);
                    if (Util.isBitSet(read, 8)) {
                        if (Util.isBitSet(read, 8) && (read & CertificateBody.profileType) == 0) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static int readTagLength(ByteArrayInputStream byteArrayInputStream) {
        int i = 0;
        int read = byteArrayInputStream.read();
        if (read < 0) {
            throw new TLVException("Negative length: " + read);
        }
        if (read > CertificateBody.profileType && read != X509KeyUsage.digitalSignature) {
            int i2 = read & CertificateBody.profileType;
            read = 0;
            while (i < i2) {
                int read2 = byteArrayInputStream.read();
                if (read2 < 0) {
                    throw new TLVException("EOS when reading length bytes");
                }
                i++;
                read = read2 | (read << 8);
            }
        }
        return read;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.americanexpress.sdkmodulelib.tlv.BERTLV getNextTLV(java.io.ByteArrayInputStream r8) {
        /*
        r4 = 2;
        r0 = 1;
        r7 = -1;
        r1 = 0;
        r2 = r8.available();
        if (r2 >= r4) goto L_0x0027;
    L_0x000a:
        r0 = new com.americanexpress.sdkmodulelib.tlv.TLVException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Error parsing data. Available bytes < 2 . Length=";
        r1 = r1.append(r2);
        r2 = r8.available();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0027:
        r8.mark(r1);
        r3 = r8.read();
        r2 = (byte) r3;
    L_0x002f:
        if (r3 == r7) goto L_0x003e;
    L_0x0031:
        if (r2 == r7) goto L_0x0035;
    L_0x0033:
        if (r2 != 0) goto L_0x003e;
    L_0x0035:
        r8.mark(r1);
        r3 = r8.read();
        r2 = (byte) r3;
        goto L_0x002f;
    L_0x003e:
        r8.reset();
        r2 = r8.available();
        if (r2 >= r4) goto L_0x0064;
    L_0x0047:
        r0 = new com.americanexpress.sdkmodulelib.tlv.TLVException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Error parsing data. Available bytes < 2 . Length=";
        r1 = r1.append(r2);
        r2 = r8.available();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0064:
        r3 = readTagIdBytes(r8);
        r8.mark(r1);
        r4 = r8.available();
        r2 = readTagLength(r8);
        r5 = r8.available();
        r8.reset();
        r4 = r4 - r5;
        r5 = new byte[r4];
        r4 = r5.length;
        if (r4 < r0) goto L_0x0084;
    L_0x0080:
        r4 = r5.length;
        r6 = 4;
        if (r4 <= r6) goto L_0x009e;
    L_0x0084:
        r0 = new com.americanexpress.sdkmodulelib.tlv.TLVException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Number of length bytes must be from 1 to 4. Found ";
        r1 = r1.append(r2);
        r2 = r5.length;
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x009e:
        r4 = r5.length;
        r8.read(r5, r1, r4);
        r4 = com.americanexpress.sdkmodulelib.tlv.Util.byteArrayToInt(r5);
        r6 = searchTagById(r3);
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r4 != r3) goto L_0x00ff;
    L_0x00ae:
        r8.mark(r1);
        r2 = r0;
        r0 = r1;
    L_0x00b3:
        r0 = r0 + 1;
        r3 = r8.read();
        if (r3 >= 0) goto L_0x00d8;
    L_0x00bb:
        r0 = new com.americanexpress.sdkmodulelib.tlv.TLVException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Error parsing data. TLV length byte indicated indefinite length, but EOS was reached before 0x0000 was found";
        r1 = r1.append(r2);
        r2 = r8.available();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00d8:
        if (r2 != 0) goto L_0x00fd;
    L_0x00da:
        if (r3 != 0) goto L_0x00fd;
    L_0x00dc:
        r2 = r0 + -2;
        r0 = new byte[r2];
        r8.reset();
        r8.read(r0, r1, r2);
    L_0x00e6:
        r8.mark(r1);
        r4 = r8.read();
        r3 = (byte) r4;
    L_0x00ee:
        if (r4 == r7) goto L_0x014d;
    L_0x00f0:
        if (r3 == r7) goto L_0x00f4;
    L_0x00f2:
        if (r3 != 0) goto L_0x014d;
    L_0x00f4:
        r8.mark(r1);
        r4 = r8.read();
        r3 = (byte) r4;
        goto L_0x00ee;
    L_0x00fd:
        r2 = r3;
        goto L_0x00b3;
    L_0x00ff:
        r3 = r8.available();
        if (r3 >= r2) goto L_0x0147;
    L_0x0105:
        r1 = new com.americanexpress.sdkmodulelib.tlv.TLVException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Length byte(s) indicated ";
        r3 = r3.append(r4);
        r2 = r3.append(r2);
        r3 = " value bytes, but only ";
        r2 = r2.append(r3);
        r3 = r8.available();
        r2 = r2.append(r3);
        r3 = " ";
        r2 = r2.append(r3);
        r3 = r8.available();
        if (r3 <= r0) goto L_0x0144;
    L_0x0130:
        r0 = "are";
    L_0x0132:
        r0 = r2.append(r0);
        r2 = " available";
        r0 = r0.append(r2);
        r0 = r0.toString();
        r1.<init>(r0);
        throw r1;
    L_0x0144:
        r0 = "is";
        goto L_0x0132;
    L_0x0147:
        r0 = new byte[r2];
        r8.read(r0, r1, r2);
        goto L_0x00e6;
    L_0x014d:
        r8.reset();
        r1 = new com.americanexpress.sdkmodulelib.tlv.BERTLV;
        r1.<init>(r6, r2, r5, r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.americanexpress.sdkmodulelib.tlv.TLVUtil.getNextTLV(java.io.ByteArrayInputStream):com.americanexpress.sdkmodulelib.tlv.BERTLV");
    }

    private static String getTagValueAsString(Tag tag, byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (C00751.$SwitchMap$com$americanexpress$sdkmodulelib$tlv$TagValueType[tag.getTagValueType().ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                stringBuilder.append("=");
                stringBuilder.append(new String(bArr));
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                stringBuilder.append("NUMERIC");
                break;
            case F2m.PPB /*3*/:
                stringBuilder.append("BINARY");
                break;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                stringBuilder.append("=");
                stringBuilder.append(Util.getSafePrintChars(bArr));
                break;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                stringBuilder.append(BuildConfig.FLAVOR);
                break;
        }
        return stringBuilder.toString();
    }

    public static List<TagAndLength> parseTagAndLength(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        List<TagAndLength> arrayList = new ArrayList();
        while (byteArrayInputStream.available() > 0) {
            if (byteArrayInputStream.available() < 2) {
                throw new TLVException("Data length < 2 : " + byteArrayInputStream.available());
            }
            byte[] readTagIdBytes = readTagIdBytes(byteArrayInputStream);
            arrayList.add(new TagAndLength(searchTagById(readTagIdBytes), readTagLength(byteArrayInputStream)));
        }
        return arrayList;
    }

    public static String prettyPrintAPDUResponse(byte[] bArr) {
        return prettyPrintAPDUResponse(bArr, 0);
    }

    public static String prettyPrintAPDUResponse(byte[] bArr, int i, int i2) {
        Object obj = new byte[(i2 - i)];
        System.arraycopy(bArr, i, obj, 0, i2);
        return prettyPrintAPDUResponse(obj, 0);
    }

    public static String prettyPrintAPDUResponse(byte[] bArr, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        while (byteArrayInputStream.available() > 0) {
            stringBuilder.append("\n");
            stringBuilder.append(Util.getSpaces(i));
            BERTLV nextTLV = getNextTLV(byteArrayInputStream);
            byte[] tagBytes = nextTLV.getTagBytes();
            byte[] rawEncodedLengthBytes = nextTLV.getRawEncodedLengthBytes();
            byte[] valueBytes = nextTLV.getValueBytes();
            Tag tag = nextTLV.getTag();
            stringBuilder.append(Util.prettyPrintHex(tagBytes));
            stringBuilder.append(" ");
            stringBuilder.append(Util.prettyPrintHex(rawEncodedLengthBytes));
            stringBuilder.append(" -- ");
            stringBuilder.append(tag.getName());
            int length = (tagBytes.length * 3) + (rawEncodedLengthBytes.length * 3);
            if (tag.isConstructed()) {
                stringBuilder.append(prettyPrintAPDUResponse(valueBytes, i + length));
            } else {
                stringBuilder.append("\n");
                if (tag.getTagValueType() == TagValueType.DOL) {
                    stringBuilder.append(getFormattedTagAndLength(valueBytes, i + length));
                } else {
                    stringBuilder.append(Util.getSpaces(i + length));
                    stringBuilder.append(Util.prettyPrintHex(Util.byteArrayToHexString(valueBytes), length + i));
                    stringBuilder.append(" (");
                    stringBuilder.append(getTagValueAsString(tag, valueBytes));
                    stringBuilder.append(")");
                }
            }
        }
        return stringBuilder.toString();
    }
}
