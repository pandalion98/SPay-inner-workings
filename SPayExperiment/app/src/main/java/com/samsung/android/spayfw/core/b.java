/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import java.util.Arrays;

public class b {
    private static final char[] hexArray;
    private static boolean iK;
    private static short iL;
    private static short iM;
    private static int iN;
    public static final byte[] iO;
    public static final byte[] iP;
    public static final byte[] iQ;
    public static final byte[] iR;
    public static final byte[] iS;
    private static final ApduReasonCode iT;

    static {
        iK = false;
        iM = iL = 0;
        iN = 0;
        hexArray = "0123456789ABCDEF".toCharArray();
        iO = new byte[]{0, -92, 4, 0, 14, 50, 80, 65, 89, 46, 83, 89, 83, 46, 68, 68, 70, 48, 49, 0};
        iP = new byte[]{0, -92, 4, 0, 9, -96, 0, 0, 4, -123, 16, 1, 1, 1, 0};
        iQ = new byte[]{0, -92, 4, 0, 9, -96, 0, 0, 4, 118, -48, 0, 1, 1, 0};
        iR = new byte[]{106, -126};
        iS = new byte[]{105, -123};
        iT = new ApduReasonCode(1, -28672);
    }

    public static ApduReasonCode Y() {
        return iT;
    }

    public static final void Z() {
        iT.reset();
        iK = false;
        iM = iL;
        iN = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String a(int n2, byte[] arrby, long l2) {
        if (arrby == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (n2 == 0) {
            stringBuilder.append(">>\n");
        } else if (n2 == 1) {
            stringBuilder.append("<<\n");
        }
        int n3 = 1 + (-1 + arrby.length) / 16;
        int n4 = 0;
        do {
            block13 : {
                int n5;
                char[] arrc;
                block14 : {
                    block12 : {
                        if (n4 >= n3 * 16) break block12;
                        if (n4 < arrby.length) {
                            int n6 = 255 & arrby[n4];
                            stringBuilder.append(hexArray[n6 >>> 4]);
                            stringBuilder.append(hexArray[n6 & 15]);
                            stringBuilder.append(' ');
                        } else {
                            stringBuilder.append("   ");
                        }
                        if ((n4 + 1) % 16 != 0) break block13;
                        stringBuilder.append("   ");
                        arrc = new char[16];
                        n5 = -16 + (n4 + 1);
                        break block14;
                    }
                    if (n2 == 1) {
                        stringBuilder.append('(');
                        stringBuilder.append(l2);
                        stringBuilder.append(" ms)");
                    }
                    return stringBuilder.toString();
                }
                for (int i2 = 0; i2 < 16; ++i2, ++n5) {
                    if (n5 < arrby.length) {
                        int n7 = 255 & arrby[n5];
                        if (n7 > 126 || n7 < 32) {
                            arrc[i2] = 46;
                            continue;
                        }
                        arrc[i2] = (char)arrby[n5];
                        continue;
                    }
                    arrc[i2] = 32;
                }
                stringBuilder.append(arrc);
                if (n4 < arrby.length) {
                    stringBuilder.append('\n');
                }
            }
            ++n4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static final String a(short s2, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (s2) {
            default: {
                stringBuilder.append("Unknown APDU: ");
                break;
            }
            case 164: {
                stringBuilder.append("SelectCard: ");
                break;
            }
            case 165: {
                stringBuilder.append("SelectPPSE: ");
                break;
            }
            case -32600: {
                stringBuilder.append("GPO: ");
                break;
            }
            case 178: {
                stringBuilder.append("ReadRecord: ");
                break;
            }
            case -32566: {
                stringBuilder.append("GetData: ");
                break;
            }
            case -32594: {
                stringBuilder.append("Generate AC: ");
            }
        }
        switch (n2) {
            default: {
                stringBuilder.append("Unexpected error code (");
                stringBuilder.append(Integer.toHexString((int)(n2 / 256)).replace(' ', '0').toUpperCase());
                stringBuilder.append(",");
                stringBuilder.append(Integer.toHexString((int)(n2 % 256)).replace(' ', '0').toUpperCase());
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
            case -28672: {
                stringBuilder.append("No error (90,00)");
                return stringBuilder.toString();
            }
            case 26368: {
                stringBuilder.append("Wrong length (67,00)");
                return stringBuilder.toString();
            }
            case 27010: {
                stringBuilder.append("Security status not satisfied (69,82)");
                return stringBuilder.toString();
            }
            case 27012: {
                stringBuilder.append("Data invalid (69,84)");
                return stringBuilder.toString();
            }
            case 27013: {
                stringBuilder.append("Conditions not satisfied (69,85)");
                return stringBuilder.toString();
            }
            case 27014: {
                stringBuilder.append("Command not allowed (69,86)");
                return stringBuilder.toString();
            }
            case 27264: {
                stringBuilder.append("Wrong data (6A,80)");
                return stringBuilder.toString();
            }
            case 27265: {
                stringBuilder.append("Function not supported (6A,81)");
                return stringBuilder.toString();
            }
            case 27266: {
                stringBuilder.append("File not found (6A,82)");
                return stringBuilder.toString();
            }
            case 27267: {
                stringBuilder.append("Record not found (6A,83)");
                return stringBuilder.toString();
            }
            case 27270: {
                stringBuilder.append("Incorrect P1P2 (6A,86)");
                return stringBuilder.toString();
            }
            case 27904: {
                stringBuilder.append("INS not supported (6D,00)");
                return stringBuilder.toString();
            }
            case 28160: {
                stringBuilder.append("CLA not supported (6E,00)");
                return stringBuilder.toString();
            }
            case 28416: {
                stringBuilder.append("Unknown error response (6F,00)");
                return stringBuilder.toString();
            }
            case 25219: {
                stringBuilder.append("Selected file invalidated (62,83)");
                return stringBuilder.toString();
            }
            case 25344: {
                stringBuilder.append("Authentication failed (63,00)");
                return stringBuilder.toString();
            }
            case 27015: {
                stringBuilder.append("SM missing (69,87)");
                return stringBuilder.toString();
            }
            case 27016: {
                stringBuilder.append("SM incorrect (69,88)");
                return stringBuilder.toString();
            }
            case 27272: 
        }
        stringBuilder.append("HCE invalid select or CPS unknown DGI (6A,88)");
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String a(short s2, byte[] arrby) {
        short s3;
        block7 : {
            block6 : {
                if (arrby == null || arrby.length < 2) {
                    return null;
                }
                if (s2 == 165) {
                    iK = true;
                }
                byte[] arrby2 = new byte[]{arrby[-2 + arrby.length], arrby[-1 + arrby.length]};
                s3 = b.getShort(arrby2, 0);
                if (iT.getCommand() != 1 || !b.i(s3)) break block6;
                if (!iK) break block7;
                iT.setCommand(s2);
                iT.setCode(s3);
                com.samsung.android.spayfw.b.c.d("ApduHelper", "Apdu message error: command=" + s2 + ", code=" + s3);
            }
            do {
                return b.a(s2, (int)s3);
                break;
            } while (true);
        }
        com.samsung.android.spayfw.b.c.i("ApduHelper", "NFC terminal is in discover mode");
        return b.a(s2, (int)s3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void a(short s2) {
        if (s2 == 2) {
            com.samsung.android.spayfw.b.c.i("APDU_LOG", "set apdu error command to TRANSACTION_COMPLETE");
            iT.setCommand((short)2);
            iT.setCode((short)-28672);
            return;
        }
        if (iT.getCode() == -28672) {
            switch (s2) {
                default: {
                    com.samsung.android.spayfw.b.c.i("APDU_LOG", "set apdu error command to NO_ERROR by default");
                    iT.setCommand((short)1);
                    break;
                }
                case 3: {
                    com.samsung.android.spayfw.b.c.i("APDU_LOG", "set apdu error command to NFC_TERMINAL_DETACHED");
                    iT.setCommand((short)3);
                    break;
                }
                case 5: {
                    com.samsung.android.spayfw.b.c.i("APDU_LOG", "set apdu error command to NFC_IN_DISCOVER_MODE");
                    iT.setCommand((short)5);
                    break;
                }
            }
        } else {
            com.samsung.android.spayfw.b.c.d("APDU_LOG", "apdu error: command=" + iT.getCommand() + ", code=" + iT.getCode());
        }
        iM = iL;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final boolean a(c c2, int n2) {
        boolean bl = true;
        if (c2 == null) return false;
        if (c2.getCardBrand() == null) {
            return false;
        }
        String string = c2.getCardBrand();
        if (string.equals((Object)"VI")) {
            if (iM == 178) return bl;
            return false;
        }
        if (string.equals((Object)"AX")) {
            if (iM == -32594) return bl;
            return false;
        }
        if (string.equals((Object)"MC")) {
            if (iM == -32594) return bl;
            if (iM == -32726) return bl;
            return false;
        }
        if (string.equals((Object)"PL")) {
            return false;
        }
        if (string.equals((Object)"GI")) {
            return false;
        }
        if (!string.equals((Object)"DS")) return false;
        if (c2.ad() == null) return false;
        if (c2.ad().isTransactionComplete() != null) return c2.ad().isTransactionComplete().getBoolean("TRANSACTION_COMPLETE_STATE");
        return false;
    }

    public static final int aa() {
        return iN;
    }

    public static final void b(short s2) {
        iM = s2;
        if (s2 == 165) {
            iN = 1 + iN;
        }
    }

    public static final boolean g(byte[] arrby) {
        return Arrays.equals((byte[])iQ, (byte[])arrby) || Arrays.equals((byte[])iP, (byte[])arrby);
    }

    public static final short getShort(byte[] arrby, int n2) {
        return (short)(((short)arrby[n2] << 8) + (255 & (short)arrby[(short)(n2 + 1)]));
    }

    public static boolean hasError() {
        int n2 = b.Y().getCode();
        com.samsung.android.spayfw.b.c.d("APDU_LOG", "status error " + (short)n2);
        return b.i(n2);
    }

    private static boolean i(int n2) {
        switch (n2) {
            default: {
                com.samsung.android.spayfw.b.c.d("APDU_LOG", "isUnrecoverableError " + (short)n2);
                return true;
            }
            case -28672: 
            case 25219: 
            case 27266: 
        }
        com.samsung.android.spayfw.b.c.d("APDU_LOG", "not error " + (short)n2);
        return false;
    }
}

