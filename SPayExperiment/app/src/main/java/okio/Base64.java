/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.AssertionError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package okio;

import java.io.UnsupportedEncodingException;

final class Base64 {
    private static final byte[] MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

    private Base64() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] decode(String string) {
        int n2 = string.length();
        do {
            char c2;
            if (n2 <= 0 || (c2 = string.charAt(n2 - 1)) != '=' && c2 != '\n' && c2 != '\r' && c2 != ' ' && c2 != '\t') break;
            --n2;
        } while (true);
        byte[] arrby = new byte[(int)(6L * (long)n2 / 8L)];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        do {
            int n7;
            int n8;
            int n9;
            block17 : {
                block18 : {
                    block10 : {
                        char c3;
                        block16 : {
                            int n10;
                            block12 : {
                                block15 : {
                                    block14 : {
                                        block13 : {
                                            block11 : {
                                                if (n3 >= n2) break block10;
                                                c3 = string.charAt(n3);
                                                if (c3 < 'A' || c3 > 'Z') break block11;
                                                n10 = c3 - 65;
                                                break block12;
                                            }
                                            if (c3 < 'a' || c3 > 'z') break block13;
                                            n10 = c3 - 71;
                                            break block12;
                                        }
                                        if (c3 < '0' || c3 > '9') break block14;
                                        n10 = c3 + 4;
                                        break block12;
                                    }
                                    if (c3 != '+' && c3 != '-') break block15;
                                    n10 = 62;
                                    break block12;
                                }
                                if (c3 != '/' && c3 != '_') break block16;
                                n10 = 63;
                            }
                            n9 = n4 << 6 | (byte)n10;
                            n8 = n5 + 1;
                            if (n8 % 4 == 0) {
                                int n11 = n6 + 1;
                                arrby[n6] = (byte)(n9 >> 16);
                                int n12 = n11 + 1;
                                arrby[n11] = (byte)(n9 >> 8);
                                n7 = n12 + 1;
                                arrby[n12] = (byte)n9;
                            } else {
                                n7 = n6;
                            }
                            break block17;
                        }
                        if (c3 == '\n' || c3 == '\r' || c3 == ' ') break block18;
                        if (c3 != '\t') {
                            return null;
                        }
                        n9 = n4;
                        n8 = n5;
                        n7 = n6;
                        break block17;
                    }
                    int n13 = n5 % 4;
                    if (n13 == 1) {
                        return null;
                    }
                    if (n13 == 2) {
                        int n14 = n4 << 12;
                        int n15 = n6 + 1;
                        arrby[n6] = (byte)(n14 >> 16);
                        n6 = n15;
                    } else if (n13 == 3) {
                        int n16 = n4 << 6;
                        int n17 = n6 + 1;
                        arrby[n6] = (byte)(n16 >> 16);
                        n6 = n17 + 1;
                        arrby[n17] = (byte)(n16 >> 8);
                    }
                    if (n6 == arrby.length) {
                        return arrby;
                    }
                    byte[] arrby2 = new byte[n6];
                    System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n6);
                    return arrby2;
                }
                n9 = n4;
                n8 = n5;
                n7 = n6;
            }
            ++n3;
            n6 = n7;
            n5 = n8;
            n4 = n9;
        } while (true);
    }

    public static String encode(byte[] arrby) {
        return Base64.encode(arrby, MAP);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static String encode(byte[] arrby, byte[] arrby2) {
        int n2 = 0;
        byte[] arrby3 = new byte[4 * (2 + arrby.length) / 3];
        int n3 = arrby.length - arrby.length % 3;
        for (int i2 = 0; i2 < n3; i2 += 3) {
            int n4 = n2 + 1;
            arrby3[n2] = arrby2[(255 & arrby[i2]) >> 2];
            int n5 = n4 + 1;
            arrby3[n4] = arrby2[(3 & arrby[i2]) << 4 | (255 & arrby[i2 + 1]) >> 4];
            int n6 = n5 + 1;
            arrby3[n5] = arrby2[(15 & arrby[i2 + 1]) << 2 | (255 & arrby[i2 + 2]) >> 6];
            int n7 = n6 + 1;
            arrby3[n6] = arrby2[63 & arrby[i2 + 2]];
            n2 = n7;
        }
        switch (arrby.length % 3) {
            case 1: {
                int n8 = n2 + 1;
                arrby3[n2] = arrby2[(255 & arrby[n3]) >> 2];
                int n9 = n8 + 1;
                arrby3[n8] = arrby2[(3 & arrby[n3]) << 4];
                int n10 = n9 + 1;
                arrby3[n9] = 61;
                n2 = n10 + 1;
                arrby3[n10] = 61;
            }
            default: {
                break;
            }
            case 2: {
                int n11 = n2 + 1;
                arrby3[n2] = arrby2[(255 & arrby[n3]) >> 2];
                int n12 = n11 + 1;
                arrby3[n11] = arrby2[(3 & arrby[n3]) << 4 | (255 & arrby[n3 + 1]) >> 4];
                int n13 = n12 + 1;
                arrby3[n12] = arrby2[(15 & arrby[n3 + 1]) << 2];
                n2 = n13 + 1;
                arrby3[n13] = 61;
            }
        }
        try {
            return new String(arrby3, 0, n2, "US-ASCII");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)unsupportedEncodingException);
        }
    }

    public static String encodeUrl(byte[] arrby) {
        return Base64.encode(arrby, URL_MAP);
    }
}

