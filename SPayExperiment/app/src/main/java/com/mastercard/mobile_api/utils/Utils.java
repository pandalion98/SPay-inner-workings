/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package com.mastercard.mobile_api.utils;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.secureelement.CardException;

public class Utils {
    private static final String HEX_PREFIX = "0x";
    public static final String ZERO_PADDING = "0000000000000000";

    public static byte[] PrependLengthToByteArray(byte[] arrby) {
        byte[] arrby2 = new byte[]{(byte)(arrby.length >>> 8), (byte)arrby.length};
        byte[] arrby3 = new byte[arrby.length + arrby2.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)arrby2.length, (int)arrby.length);
        return arrby3;
    }

    private static final void appendByte(int n2, StringBuffer stringBuffer) {
        String string = Integer.toHexString((int)(n2 & 255));
        if (string.length() < 2) {
            stringBuffer.append('0');
        }
        stringBuffer.append(string);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final boolean arrayCompare(byte[] arrby, int n2, byte[] arrby2, int n3, int n4) {
        if (n2 + n4 <= arrby.length && n3 + n4 <= arrby2.length) {
            int n5 = 0;
            do {
                if (n5 >= n4) {
                    return true;
                }
                if (arrby[n2 + n5] != arrby2[n3 + n5]) break;
                ++n5;
            } while (true);
        }
        return false;
    }

    public static String bcdAmountArrayToString(ByteArray byteArray) {
        return Utils.bcdAmountArrayToString(byteArray.getBytes(), 0, byteArray.getLength());
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String bcdAmountArrayToString(byte[] arrby, int n2, int n3) {
        if (n2 >= arrby.length) throw new ArrayIndexOutOfBoundsException();
        if (n2 + n3 > arrby.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n4 = n2;
        String string = "";
        boolean bl = true;
        do {
            boolean bl2;
            String string2;
            if (n4 >= n2 + n3) {
                if (string.equalsIgnoreCase("")) return "0" + string;
                if (string.charAt(0) != '.') return string;
                return "0" + string;
            }
            byte by = (byte)(15 & arrby[n4] >>> 4);
            byte by2 = (byte)(15 & arrby[n4]);
            if (by > 9) throw new IllegalArgumentException();
            if (by2 > 9) {
                throw new IllegalArgumentException();
            }
            Integer n5 = new Integer((int)by);
            Integer n6 = new Integer((int)by2);
            if (!bl || by != 0) {
                string2 = String.valueOf((Object)string) + n5.toString();
                bl2 = false;
            } else {
                boolean bl3 = bl;
                string2 = string;
                bl2 = bl3;
            }
            if (!bl2 || by2 != 0) {
                string2 = String.valueOf((Object)string2) + n6.toString();
                bl2 = false;
            }
            if (n4 == -2 + (n2 + n3)) {
                string2 = String.valueOf((Object)string2) + ".";
                bl2 = false;
            }
            ++n4;
            boolean bl4 = bl2;
            string = string2;
            bl = bl4;
        } while (true);
    }

    public static String bcdArrayToString(byte[] arrby, int n2, int n3) {
        String string = "";
        boolean bl = true;
        if (n2 >= arrby.length || n2 + n3 > arrby.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n4 = n2;
        while (n4 < n2 + n3) {
            byte by = (byte)(15 & arrby[n4] >>> 4);
            byte by2 = (byte)(15 & arrby[n4]);
            if (by > 9 || by2 > 9) {
                throw new IllegalArgumentException();
            }
            Integer n5 = new Integer((int)by);
            Integer n6 = new Integer((int)by2);
            if (!bl || by != 0) {
                string = String.valueOf((Object)string) + n5.toString();
                bl = false;
            }
            if (!bl || by2 != 0) {
                string = String.valueOf((Object)string) + n6.toString();
                bl = false;
            }
            ++n4;
        }
        return string;
    }

    public static void clearByteArray(ByteArray byteArray) {
        if (byteArray != null) {
            byteArray.clear();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void clearByteArray(byte[] arrby) {
        if (arrby != null) {
            int n2 = arrby.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                arrby[i2] = 0;
            }
        }
    }

    public static void clearCharArray(char[] arrc) {
        int n2 = arrc.length;
        int n3 = 0;
        while (n3 < n2) {
            arrc[n3] = '\u0000';
            ++n3;
        }
        return;
    }

    public static byte[] encodeByteArray(byte[] arrby) {
        int n2 = 0;
        int n3 = arrby.length;
        byte[] arrby2 = new byte[(n3 + n3 % 2) / 2];
        int n4 = 0;
        do {
            if (n4 > n3 - 2) {
                if (n3 % 2 == 1) {
                    arrby2[n2] = (byte)(15 | -48 + arrby[n4] << 4);
                }
                return arrby2;
            }
            int n5 = n2 + 1;
            int n6 = n4 + 1;
            byte by = (byte)(-48 + arrby[n4] << 4);
            n4 = n6 + 1;
            arrby2[n2] = (byte)(by | (byte)(-48 + arrby[n6]));
            n2 = n5;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean equals(byte[] arrby, byte[] arrby2) {
        if (arrby == null && arrby2 == null) {
            return true;
        }
        boolean bl = false;
        if (arrby == null) return bl;
        bl = false;
        if (arrby2 == null) return bl;
        int n2 = arrby.length;
        int n3 = arrby2.length;
        bl = false;
        if (n2 != n3) return bl;
        int n4 = 0;
        while (n4 < arrby.length) {
            byte by = arrby[n4];
            byte by2 = arrby2[n4];
            bl = false;
            if (by != by2) return bl;
            ++n4;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final boolean equals(byte[] arrby, byte[] arrby2, int n2, int n3, int n4) {
        if (arrby != null || arrby2 != null) {
            if (arrby != null && arrby2 != null && n2 + n4 <= arrby.length && n3 + n4 <= arrby2.length) {
            } else {
                return false;
            }
            for (int i2 = 0; i2 < n4; ++i2) {
                if (arrby[n2 + i2] == arrby2[n3 + i2]) continue;
                return false;
            }
        }
        return true;
    }

    public static int findTag(byte by, byte[] arrby, int n2, int n3) {
        int n4 = 0;
        do {
            if (n4 >= n3) {
                throw new CardException();
            }
            if (arrby[n2 + n4] == by) {
                return n4 + n2;
            }
            ++n4;
        } while (true);
    }

    public static int findTag(short s2, byte[] arrby, int n2, int n3) {
        int n4 = 0;
        do {
            if (n4 >= n3) {
                throw new CardException();
            }
            if (Utils.readShort(arrby, n2 + n4) == s2) {
                return n4 + n2;
            }
            ++n4;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String getAsHexString(int n2, boolean bl) {
        String string;
        if (bl) {
            string = HEX_PREFIX;
            do {
                return String.valueOf((Object)string) + Integer.toHexString((int)n2);
                break;
            } while (true);
        }
        string = "";
        return String.valueOf((Object)string) + Integer.toHexString((int)n2);
    }

    public static String getAsHexString(byte[] arrby) {
        return Utils.getAsHexString(arrby, false);
    }

    public static final String getAsHexString(byte[] arrby, int n2, int n3) {
        return Utils.getAsHexString(arrby, n2, n3, false);
    }

    public static final String getAsHexString(byte[] arrby, int n2, int n3, boolean bl) {
        StringBuffer stringBuffer = new StringBuffer();
        if (n2 + n3 > arrby.length) {
            n3 = arrby.length - n2;
        }
        int n4 = n2;
        while (n4 < n2 + n3) {
            Utils.appendByte(arrby[n4], stringBuffer);
            ++n4;
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static final String getAsHexString(byte[] arrby, boolean bl) {
        return Utils.getAsHexString(arrby, 0, arrby.length, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isZero(ByteArray byteArray) {
        if (byteArray != null) {
            int n2 = byteArray.getLength();
            byte[] arrby = byteArray.getBytes();
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    return true;
                }
                if (arrby[n3] != 0) break;
                ++n3;
            } while (true);
        }
        return false;
    }

    public static int lastIndexOf(String string, String string2) {
        int n2 = 0;
        int n3 = -1;
        while (n2 != -1) {
            n2 = string.indexOf(string2, n3 + 1);
            if (n2 == -1) continue;
            n3 = n2;
        }
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] longToBCD(long l2, int n2) {
        long l3 = l2;
        int n3 = 0;
        do {
            if (l3 == 0L) break;
            int n4 = n3 + 1;
            l3 /= 10L;
            n3 = n4;
        } while (true);
        int n5 = n3 % 2 == 0 ? n3 / 2 : (n3 + 1) / 2;
        boolean bl = n3 % 2 != 0;
        byte[] arrby = new byte[n5];
        int n6 = 0;
        do {
            if (n6 >= n3) break;
            byte by = (byte)(l2 % 10L);
            if (n6 == n3 - 1 && bl) {
                arrby[n6 / 2] = by;
            } else if (n6 % 2 == 0) {
                arrby[n6 / 2] = by;
            } else {
                byte by2 = (byte)(by << 4);
                int n7 = n6 / 2;
                arrby[n7] = (byte)(by2 | arrby[n7]);
            }
            l2 /= 10L;
            ++n6;
        } while (true);
        int n8 = 0;
        do {
            if (n8 >= n5 / 2) {
                if (n2 != n5) break;
                return arrby;
            }
            byte by = arrby[n8];
            arrby[n8] = arrby[-1 + (n5 - n8)];
            arrby[-1 + (n5 - n8)] = by;
            ++n8;
        } while (true);
        byte[] arrby2 = new byte[n2];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)(n2 - n5), (int)n5);
        return arrby2;
    }

    public static short makeShort(byte by, byte by2) {
        return (short)((short)((255 & (short)(by & 255)) << 8) | (short)(by2 & 255));
    }

    public static byte[] prependLengthToByteArray(byte[] arrby) {
        byte[] arrby2 = new byte[]{(byte)(arrby.length >>> 8), (byte)arrby.length};
        byte[] arrby3 = new byte[arrby.length + arrby2.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)arrby2.length, (int)arrby.length);
        return arrby3;
    }

    public static final byte[] readHexString(String string) {
        int n2 = 0;
        if (string == null || string.length() == 0 || string.equals((Object)HEX_PREFIX)) {
            return new byte[0];
        }
        if (string.startsWith(HEX_PREFIX)) {
            string = string.substring(2);
        }
        byte[] arrby = new byte[string.length() / 2];
        while (n2 < arrby.length) {
            arrby[n2] = (byte)(255 & Integer.parseInt((String)string.substring(n2 * 2, 2 + n2 * 2), (int)16));
            ++n2;
        }
        return arrby;
    }

    public static final int readInt(byte[] arrby, int n2) {
        return Utils.readInt(arrby, n2, false);
    }

    public static final int readInt(byte[] arrby, int n2, boolean bl) {
        int n3 = arrby.length - n2;
        if (n3 < 4) {
            byte[] arrby2 = new byte[4];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)(4 - n3), (int)n3);
            arrby = arrby2;
        }
        if (bl) {
            return (255 & arrby[n2 + 3]) << 24 | (255 & arrby[n2 + 2]) << 16 | (255 & arrby[n2 + 1]) << 8 | 255 & arrby[n2 + 0];
        }
        return (255 & arrby[n2 + 0]) << 24 | (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
    }

    public static int readShort(ByteArray byteArray) {
        return Utils.readShort(byteArray.getBytes(), 0);
    }

    public static final int readShort(byte[] arrby, int n2, boolean bl) {
        if (bl) {
            return 65535 & (arrby[n2 + 1] << 8 | 255 & arrby[n2]);
        }
        return 65535 & (arrby[n2] << 8 | 255 & arrby[n2 + 1]);
    }

    public static final short readShort(byte[] arrby, int n2) {
        return (short)Utils.readShort(arrby, n2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean superior(ByteArray byteArray, ByteArray byteArray2) {
        if (byteArray2 != null && byteArray != null) {
            for (int i2 = -1 + byteArray.getLength(); i2 >= 0; --i2) {
                int n2;
                int n3 = 255 & byteArray.getByte(i2);
                if (n3 == (n2 = 255 & byteArray2.getByte(i2))) continue;
                if (n3 <= n2) break;
                return true;
            }
        }
        return false;
    }

    public static String toHexString(byte[] arrby, int n2) {
        StringBuffer stringBuffer = new StringBuffer();
        int n3 = 0;
        while (n3 < n2) {
            byte by = arrby[n3];
            stringBuffer.append("0123456789ABCDEF".charAt(15 & by >> 4));
            stringBuffer.append("0123456789ABCDEF".charAt(by & 15));
            ++n3;
        }
        return stringBuffer.toString();
    }

    public static final void writeInt(ByteArray byteArray, int n2, long l2) {
        byteArray.setByte(n2 + 0, (byte)(255L & l2 >> 24));
        byteArray.setByte(n2 + 1, (byte)(255L & l2 >> 16));
        byteArray.setByte(n2 + 2, (byte)(255L & l2 >> 8));
        byteArray.setByte(n2 + 3, (byte)(l2 & 255L));
    }
}

