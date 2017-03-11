package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext.DiscoverClTransactionType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverDataTags;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.utils.a */
public class BERTLV {
    public static ByteBuffer m1004c(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer2 == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer byteBuffer3 = new ByteBuffer(byteBuffer.getBytes());
        byteBuffer3.append(BERTLV.m997E(byteBuffer2));
        byteBuffer3.append(byteBuffer2);
        return byteBuffer3;
    }

    public static ByteBuffer m1001a(byte b, ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer E = BERTLV.m997E(byteBuffer);
        if (E == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer byteBuffer2 = new ByteBuffer(byteBuffer.getSize() + (E.getSize() + 1));
        byteBuffer2.setByte(0, b);
        byteBuffer2.copyBytes(E, 0, 1, E.getSize());
        byteBuffer2.copyBytes(byteBuffer, 0, E.getSize() + 1, byteBuffer.getSize());
        return byteBuffer2;
    }

    public static ByteBuffer m997E(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        int size = byteBuffer.getSize();
        ByteBuffer byteBuffer2;
        if (size <= CertificateBody.profileType) {
            byteBuffer2 = new ByteBuffer(1);
            byteBuffer2.setByte(0, (byte) size);
            return byteBuffer2;
        } else if (size <= GF2Field.MASK) {
            byteBuffer2 = new ByteBuffer(2);
            byteBuffer2.setByte(0, TLVParser.BYTE_81);
            byteBuffer2.setByte(1, (byte) size);
            return byteBuffer2;
        } else if (size <= HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            byteBuffer2 = new ByteBuffer(3);
            byteBuffer2.setByte(0, EMVSetStatusApdu.RESET_LOWEST_PRIORITY);
            byteBuffer2.setByte(1, (byte) size);
            return byteBuffer2;
        } else if (size <= ViewCompat.MEASURED_SIZE_MASK) {
            byteBuffer2 = new ByteBuffer(4);
            byteBuffer2.setByte(0, (byte) -125);
            byteBuffer2.setByte(1, (byte) size);
            return byteBuffer2;
        } else {
            byteBuffer2 = new ByteBuffer(4);
            byteBuffer2.setByte(0, PinChangeUnblockApdu.CLA);
            byteBuffer2.setByte(1, (byte) size);
            return byteBuffer2;
        }
    }

    public static TLVData m1005c(byte[] bArr, int i, int i2) {
        int i3 = i + i2;
        ByteBuffer byteBuffer = new ByteBuffer(bArr);
        TLVData tLVData = new TLVData();
        Log.m285d("DCSDK_", "last_offset " + i3);
        while (i < i3) {
            int i4;
            int i5 = bArr[i] & GF2Field.MASK;
            if (((byte) (i5 & 31)) == 31) {
                i5 = (short) (((bArr[i] << 8) | (bArr[i + 1] & GF2Field.MASK)) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE);
                i4 = i + 2;
            } else {
                i4 = i + 1;
            }
            int b = BERTLV.m1003b(bArr, i4);
            i4 += BERTLV.m1000a(bArr, i4);
            tLVData.m1007a(i5, byteBuffer.copyBytes(i4, i4 + b));
            i = i4 + b;
        }
        return tLVData;
    }

    public static int m1000a(byte[] bArr, int i) {
        if (bArr[i] == -127) {
            return 2;
        }
        if (bArr[i] == -126) {
            return 3;
        }
        if (bArr[i] == -125) {
            return 4;
        }
        if (bArr[i] == -124) {
            return 5;
        }
        return 1;
    }

    public static int m1003b(byte[] bArr, int i) {
        if (bArr[i] > null && (bArr[i] & GF2Field.MASK) < X509KeyUsage.digitalSignature) {
            return bArr[i] & GF2Field.MASK;
        }
        if (bArr[i] == -127) {
            return bArr[i + 1] & GF2Field.MASK;
        }
        if (bArr[i] == -126) {
            return ((bArr[i + 1] & GF2Field.MASK) << 8) | (bArr[i + 2] & GF2Field.MASK);
        }
        if (bArr[i] == -125) {
            return (((bArr[i + 1] & GF2Field.MASK) << 16) | ((bArr[i + 2] & GF2Field.MASK) << 8)) | (bArr[i + 3] & GF2Field.MASK);
        }
        if (bArr[i] == -124) {
            return ((((bArr[i + 0] & GF2Field.MASK) << 24) | ((bArr[i + 1] & GF2Field.MASK) << 16)) | ((bArr[i + 2] & GF2Field.MASK) << 8)) | (bArr[i + 3] & GF2Field.MASK);
        }
        return bArr[i] & GF2Field.MASK;
    }

    public static void m1002a(ByteBuffer byteBuffer, Map<Integer, Integer> map) {
        if (map != null && byteBuffer != null && byteBuffer.getSize() != 0) {
            int i = 0;
            while (i < byteBuffer.getSize()) {
                try {
                    int i2 = byteBuffer.getByte(i);
                    if ((i2 & 31) == 31) {
                        i2 = ((byteBuffer.getByte(i) << 8) | (byteBuffer.getByte(i + 1) & GF2Field.MASK)) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
                        i += 2;
                    } else {
                        i++;
                    }
                    i2 = BERTLV.m1000a(byteBuffer.getBytes(), i) + i;
                    map.put(Integer.valueOf(i2 & GF2Field.MASK), Integer.valueOf(BERTLV.m1003b(byteBuffer.getBytes(), i)));
                    i = i2;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    throw new ParseException(e.getMessage(), 0);
                } catch (ArrayIndexOutOfBoundsException e2) {
                    e2.printStackTrace();
                    throw new ParseException(e2.getMessage(), 0);
                }
            }
        }
    }

    public static TLVData m998F(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            Log.m286e("DCSDK_", "parseFCI, fci is null, parsing failed.");
            return null;
        }
        Log.m285d("DCSDK_", "parseFCI, parse FCI Template tag.");
        TLVData c = BERTLV.m1005c(byteBuffer.getBytes(), 0, byteBuffer.getSize());
        if (c == null || c.m1006O(DiscoverDataTags.vO.getInt()) == null) {
            Log.m286e("DCSDK_", "parseFCI, FCI template not found.");
            return null;
        }
        List O = c.m1006O(DiscoverDataTags.vO.getInt());
        if (O == null || O.isEmpty()) {
            return null;
        }
        TLVData c2 = BERTLV.m1005c(((ByteBuffer) O.get(0)).getBytes(), 0, ((ByteBuffer) O.get(0)).getSize());
        Log.m285d("DCSDK_", "parseFCI, returned parsed FCI.");
        return c2;
    }

    public static List<ByteBuffer> m999G(ByteBuffer byteBuffer) {
        List<ByteBuffer> arrayList = new ArrayList();
        if (byteBuffer == null || byteBuffer.getSize() == 0) {
            Log.m286e("DCSDK_", "parsePPSE_FCI, fci is null, parsing failed.");
            return null;
        }
        Log.m285d("DCSDK_", "parsePPSE_FCI, parse FCI Template tag.");
        TLVData c = BERTLV.m1005c(byteBuffer.getBytes(), 0, byteBuffer.getSize());
        if (c == null || c.m1006O(DiscoverDataTags.vO.getInt()) == null) {
            Log.m286e("DCSDK_", "parseFCI, FCI template not found.");
        } else {
            List O = c.m1006O(DiscoverDataTags.vO.getInt());
            if (O == null || O.isEmpty()) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, fci template list is empty.");
                return null;
            }
            ByteBuffer byteBuffer2 = (ByteBuffer) O.get(0);
            if (byteBuffer2 == null) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, fci template is empty.");
                return null;
            }
            Log.m285d("DCSDK_", "parsePPSE_FCI, fciTemplate: " + byteBuffer2.toHexString());
            c = BERTLV.m1005c(byteBuffer2.getBytes(), 0, byteBuffer2.getSize());
            Log.m285d("DCSDK_", "parsePPSE_FCI, returned parsed proprietaryTemplate.");
            if (c == null || c.m1006O(DiscoverDataTags.vQ.getInt()) == null) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, proprietary template template is empty.");
                return null;
            }
            O = c.m1006O(DiscoverDataTags.vQ.getInt());
            if (O == null || O.isEmpty()) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, proprietary template data list is empty.");
                return null;
            }
            byteBuffer2 = (ByteBuffer) O.get(0);
            if (byteBuffer2 == null) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, proprietary template data is empty.");
                return null;
            }
            Log.m285d("DCSDK_", "parsePPSE_FCI, proprietaryTemplateData: " + byteBuffer2.toHexString());
            c = BERTLV.m1005c(byteBuffer2.getBytes(), 0, byteBuffer2.getSize());
            Log.m285d("DCSDK_", "parsePPSE_FCI, returned parsed fci idd.");
            short s = (short) (((DiscoverDataTags.vR.getByte(0) << 8) | (DiscoverDataTags.vR.getByte(1) & GF2Field.MASK)) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE);
            Log.m285d("DCSDK_", "parsePPSE_FCI, tag int: " + DiscoverDataTags.vR.getInt());
            Log.m285d("DCSDK_", "parsePPSE_FCI, tag short: " + s);
            if (c == null || c.m1006O(s) == null) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, FCI IDD is empty.");
                return null;
            }
            if (c.m1006O(s) != null) {
                byteBuffer2 = (ByteBuffer) c.m1006O(s).get(0);
            } else {
                byteBuffer2 = null;
            }
            if (byteBuffer2 == null) {
                Log.m286e("DCSDK_", "parsePPSE_FCI, FCI IDD is empty.");
                return null;
            }
            Log.m285d("DCSDK_", "parsePPSE_FCI, FCI_IDD_data: " + byteBuffer2.toHexString());
            c = BERTLV.m1005c(byteBuffer2.getBytes(), 0, byteBuffer2.getSize());
            if (c != null) {
                Log.m285d("DCSDK_", "parsePPSE_FCI, directory entries.");
                Map ef = c.ef();
                if (ef == null) {
                    Log.m286e("DCSDK_", "parsePPSE_FCI, DE tags list is empty.");
                    return null;
                }
                List<ByteBuffer> list = (List) ef.get(Integer.valueOf(DiscoverDataTags.vT.getInt()));
                if (list != null) {
                    for (ByteBuffer byteBuffer22 : list) {
                        Log.m285d("DCSDK_", "parsePPSE_FCI, tagValue: " + byteBuffer22);
                        if (byteBuffer22 != null) {
                            Log.m285d("DCSDK_", "parsePPSE_FCI, tagValue: " + byteBuffer22.toHexString());
                            c = BERTLV.m1005c(byteBuffer22.getBytes(), 0, byteBuffer22.getSize());
                            Log.m285d("DCSDK_", "parsePPSE_FCI, dfEntry: " + c);
                            if (c != null) {
                                List O2 = c.m1006O(DiscoverDataTags.vS.getInt());
                                Log.m285d("DCSDK_", "parsePPSE_FCI, aid: " + O2);
                                if (!(O2 == null || O2.isEmpty() || O2.get(0) == null)) {
                                    Log.m285d("DCSDK_", "parsePPSE_FCI, aid: " + ((ByteBuffer) O2.get(0)).toHexString());
                                    if (((ByteBuffer) O2.get(0)).toHexString().startsWith(DiscoverClTransactionType.DISCOVER_CL_ZIP.getAid().toHexString())) {
                                        Log.m285d("DCSDK_", "parsePPSE_FCI, Skipping zip aid aid" + ((ByteBuffer) O2.get(0)).toHexString());
                                    } else {
                                        arrayList.add(O2.get(0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
