/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class a {
    public static ByteBuffer E(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        int n2 = byteBuffer.getSize();
        if (n2 <= 127) {
            ByteBuffer byteBuffer2 = new ByteBuffer(1);
            byteBuffer2.setByte(0, (byte)n2);
            return byteBuffer2;
        }
        if (n2 <= 255) {
            ByteBuffer byteBuffer3 = new ByteBuffer(2);
            byteBuffer3.setByte(0, (byte)-127);
            byteBuffer3.setByte(1, (byte)n2);
            return byteBuffer3;
        }
        if (n2 <= 65535) {
            ByteBuffer byteBuffer4 = new ByteBuffer(3);
            byteBuffer4.setByte(0, (byte)-126);
            byteBuffer4.setByte(1, (byte)n2);
            return byteBuffer4;
        }
        if (n2 <= 16777215) {
            ByteBuffer byteBuffer5 = new ByteBuffer(4);
            byteBuffer5.setByte(0, (byte)-125);
            byteBuffer5.setByte(1, (byte)n2);
            return byteBuffer5;
        }
        ByteBuffer byteBuffer6 = new ByteBuffer(4);
        byteBuffer6.setByte(0, (byte)-124);
        byteBuffer6.setByte(1, (byte)n2);
        return byteBuffer6;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static b F(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            Log.e("DCSDK_", "parseFCI, fci is null, parsing failed.");
            return null;
        }
        Log.d("DCSDK_", "parseFCI, parse FCI Template tag.");
        b b2 = a.c(byteBuffer.getBytes(), 0, byteBuffer.getSize());
        if (b2 != null && b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vO.getInt()) != null) {
            List<ByteBuffer> list = b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vO.getInt());
            if (list == null || list.isEmpty()) return null;
            {
                b b3 = a.c(((ByteBuffer)list.get(0)).getBytes(), 0, ((ByteBuffer)list.get(0)).getSize());
                Log.d("DCSDK_", "parseFCI, returned parsed FCI.");
                return b3;
            }
        }
        Log.e("DCSDK_", "parseFCI, FCI template not found.");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static List<ByteBuffer> G(ByteBuffer byteBuffer) {
        Iterator iterator;
        ArrayList arrayList = new ArrayList();
        if (byteBuffer == null || byteBuffer.getSize() == 0) {
            Log.e("DCSDK_", "parsePPSE_FCI, fci is null, parsing failed.");
            return null;
        }
        Log.d("DCSDK_", "parsePPSE_FCI, parse FCI Template tag.");
        b b2 = a.c(byteBuffer.getBytes(), 0, byteBuffer.getSize());
        if (b2 != null && b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vO.getInt()) != null) {
            List<ByteBuffer> list = b2.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vO.getInt());
            if (list == null || list.isEmpty()) {
                Log.e("DCSDK_", "parsePPSE_FCI, fci template list is empty.");
                return null;
            }
            ByteBuffer byteBuffer2 = (ByteBuffer)list.get(0);
            if (byteBuffer2 == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, fci template is empty.");
                return null;
            }
            Log.d("DCSDK_", "parsePPSE_FCI, fciTemplate: " + byteBuffer2.toHexString());
            b b3 = a.c(byteBuffer2.getBytes(), 0, byteBuffer2.getSize());
            Log.d("DCSDK_", "parsePPSE_FCI, returned parsed proprietaryTemplate.");
            if (b3 == null || b3.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vQ.getInt()) == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, proprietary template template is empty.");
                return null;
            }
            List<ByteBuffer> list2 = b3.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vQ.getInt());
            if (list2 == null || list2.isEmpty()) {
                Log.e("DCSDK_", "parsePPSE_FCI, proprietary template data list is empty.");
                return null;
            }
            ByteBuffer byteBuffer3 = (ByteBuffer)list2.get(0);
            if (byteBuffer3 == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, proprietary template data is empty.");
                return null;
            }
            Log.d("DCSDK_", "parsePPSE_FCI, proprietaryTemplateData: " + byteBuffer3.toHexString());
            b b4 = a.c(byteBuffer3.getBytes(), 0, byteBuffer3.getSize());
            Log.d("DCSDK_", "parsePPSE_FCI, returned parsed fci idd.");
            short s2 = (short)(65535 & (com.samsung.android.spayfw.payprovider.discover.payment.data.c.vR.getByte(0) << 8 | 255 & com.samsung.android.spayfw.payprovider.discover.payment.data.c.vR.getByte(1)));
            Log.d("DCSDK_", "parsePPSE_FCI, tag int: " + com.samsung.android.spayfw.payprovider.discover.payment.data.c.vR.getInt());
            Log.d("DCSDK_", "parsePPSE_FCI, tag short: " + s2);
            if (b4 == null || b4.O(s2) == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, FCI IDD is empty.");
                return null;
            }
            ByteBuffer byteBuffer4 = b4.O(s2) != null ? (ByteBuffer)b4.O(s2).get(0) : null;
            if (byteBuffer4 == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, FCI IDD is empty.");
                return null;
            }
            Log.d("DCSDK_", "parsePPSE_FCI, FCI_IDD_data: " + byteBuffer4.toHexString());
            b b5 = a.c(byteBuffer4.getBytes(), 0, byteBuffer4.getSize());
            if (b5 == null) return arrayList;
            Log.d("DCSDK_", "parsePPSE_FCI, directory entries.");
            HashMap<Integer, List<ByteBuffer>> hashMap = b5.ef();
            if (hashMap == null) {
                Log.e("DCSDK_", "parsePPSE_FCI, DE tags list is empty.");
                return null;
            }
            List list3 = (List)hashMap.get((Object)com.samsung.android.spayfw.payprovider.discover.payment.data.c.vT.getInt());
            if (list3 == null) return arrayList;
            iterator = list3.iterator();
        } else {
            Log.e("DCSDK_", "parseFCI, FCI template not found.");
            return arrayList;
        }
        while (iterator.hasNext()) {
            ByteBuffer byteBuffer5 = (ByteBuffer)iterator.next();
            Log.d("DCSDK_", "parsePPSE_FCI, tagValue: " + byteBuffer5);
            if (byteBuffer5 == null) continue;
            Log.d("DCSDK_", "parsePPSE_FCI, tagValue: " + byteBuffer5.toHexString());
            b b6 = a.c(byteBuffer5.getBytes(), 0, byteBuffer5.getSize());
            Log.d("DCSDK_", "parsePPSE_FCI, dfEntry: " + b6);
            if (b6 == null) continue;
            List<ByteBuffer> list = b6.O(com.samsung.android.spayfw.payprovider.discover.payment.data.c.vS.getInt());
            Log.d("DCSDK_", "parsePPSE_FCI, aid: " + list);
            if (list == null || list.isEmpty() || list.get(0) == null) continue;
            Log.d("DCSDK_", "parsePPSE_FCI, aid: " + ((ByteBuffer)list.get(0)).toHexString());
            if (((ByteBuffer)list.get(0)).toHexString().startsWith(DiscoverCLTransactionContext.DiscoverClTransactionType.up.getAid().toHexString())) {
                Log.d("DCSDK_", "parsePPSE_FCI, Skipping zip aid aid" + ((ByteBuffer)list.get(0)).toHexString());
                continue;
            }
            arrayList.add(list.get(0));
        }
        return arrayList;
    }

    public static int a(byte[] arrby, int n2) {
        if (arrby[n2] == -127) {
            return 2;
        }
        if (arrby[n2] == -126) {
            return 3;
        }
        if (arrby[n2] == -125) {
            return 4;
        }
        if (arrby[n2] == -124) {
            return 5;
        }
        return 1;
    }

    public static ByteBuffer a(byte by, ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer byteBuffer2 = a.E(byteBuffer);
        if (byteBuffer2 == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer byteBuffer3 = new ByteBuffer(1 + byteBuffer2.getSize() + byteBuffer.getSize());
        byteBuffer3.setByte(0, by);
        byteBuffer3.copyBytes(byteBuffer2, 0, 1, byteBuffer2.getSize());
        byteBuffer3.copyBytes(byteBuffer, 0, 1 + byteBuffer2.getSize(), byteBuffer.getSize());
        return byteBuffer3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void a(ByteBuffer byteBuffer, Map<Integer, Integer> map) {
        if (map == null || byteBuffer == null || byteBuffer.getSize() == 0) return;
        {
            int n2 = 0;
            try {
                while (n2 < byteBuffer.getSize()) {
                    int n3;
                    int n4 = byteBuffer.getByte(n2);
                    if ((n4 & 31) == 31) {
                        n4 = 65535 & (byteBuffer.getByte(n2) << 8 | 255 & byteBuffer.getByte(n2 + 1));
                        n3 = n2 + 2;
                    } else {
                        n3 = n2 + 1;
                    }
                    int n5 = n4 & 255;
                    int n6 = a.b(byteBuffer.getBytes(), n3);
                    int n7 = n3 + a.a(byteBuffer.getBytes(), n3);
                    map.put((Object)n5, (Object)n6);
                    n2 = n7;
                }
                return;
            }
            catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
                throw new ParseException(nullPointerException.getMessage(), 0);
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                arrayIndexOutOfBoundsException.printStackTrace();
                throw new ParseException(arrayIndexOutOfBoundsException.getMessage(), 0);
            }
        }
    }

    public static int b(byte[] arrby, int n2) {
        if (arrby[n2] > 0 && (255 & arrby[n2]) < 128) {
            return 255 & arrby[n2];
        }
        if (arrby[n2] == -127) {
            return 255 & arrby[n2 + 1];
        }
        if (arrby[n2] == -126) {
            return (255 & arrby[n2 + 1]) << 8 | 255 & arrby[n2 + 2];
        }
        if (arrby[n2] == -125) {
            return (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
        }
        if (arrby[n2] == -124) {
            return (255 & arrby[n2 + 0]) << 24 | (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
        }
        return 255 & arrby[n2];
    }

    public static ByteBuffer c(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer2 == null) {
            return new ByteBuffer(0);
        }
        ByteBuffer byteBuffer3 = new ByteBuffer(byteBuffer.getBytes());
        byteBuffer3.append(a.E(byteBuffer2));
        byteBuffer3.append(byteBuffer2);
        return byteBuffer3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static b c(byte[] arrby, int n2, int n3) {
        int n4 = n2 + n3;
        ByteBuffer byteBuffer = new ByteBuffer(arrby);
        b b2 = new b();
        Log.d("DCSDK_", "last_offset " + n4);
        while (n2 < n4) {
            int n5;
            int n6 = 255 & arrby[n2];
            if ((byte)(n6 & 31) == 31) {
                n6 = (short)(65535 & (arrby[n2] << 8 | 255 & arrby[n2 + 1]));
                n5 = n2 + 2;
            } else {
                n5 = n2 + 1;
            }
            int n7 = a.b(arrby, n5);
            int n8 = n5 + a.a(arrby, n5);
            b2.a(n6, byteBuffer.copyBytes(n8, n8 + n7));
            n2 = n8 + n7;
        }
        return b2;
    }
}

