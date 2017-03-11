package com.samsung.android.spayfw.core;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import java.util.Arrays;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.core.b */
public class ApduHelper {
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
        iL = (short) 0;
        iM = iL;
        iN = 0;
        hexArray = "0123456789ABCDEF".toCharArray();
        iO = new byte[]{(byte) 0, ISO7816.INS_SELECT, (byte) 4, (byte) 0, (byte) 14, (byte) 50, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 65, (byte) 89, (byte) 46, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 89, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 46, (byte) 68, (byte) 68, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 48, (byte) 49, (byte) 0};
        iP = new byte[]{(byte) 0, ISO7816.INS_SELECT, (byte) 4, (byte) 0, (byte) 9, (byte) -96, (byte) 0, (byte) 0, (byte) 4, (byte) -123, Tnaf.POW_2_WIDTH, (byte) 1, (byte) 1, (byte) 1, (byte) 0};
        iQ = new byte[]{(byte) 0, ISO7816.INS_SELECT, (byte) 4, (byte) 0, (byte) 9, (byte) -96, (byte) 0, (byte) 0, (byte) 4, (byte) 118, (byte) -48, (byte) 0, (byte) 1, (byte) 1, (byte) 0};
        iR = new byte[]{(byte) 106, EMVSetStatusApdu.RESET_LOWEST_PRIORITY};
        iS = new byte[]{(byte) 105, (byte) -123};
        iT = new ApduReasonCode((short) 1, (short) com.samsung.android.spayfw.appinterface.ISO7816.SW_NO_ERROR);
    }

    public static boolean hasError() {
        int code = ApduHelper.m563Y().getCode();
        Log.m285d("APDU_LOG", "status error " + ((short) code));
        return ApduHelper.m572i(code);
    }

    private static boolean m572i(int i) {
        switch (i) {
            case -28672:
            case 25219:
            case 27266:
                Log.m285d("APDU_LOG", "not error " + ((short) i));
                return false;
            default:
                Log.m285d("APDU_LOG", "isUnrecoverableError " + ((short) i));
                return true;
        }
    }

    public static void m568a(short s) {
        if (s == (short) 2) {
            Log.m287i("APDU_LOG", "set apdu error command to TRANSACTION_COMPLETE");
            iT.setCommand((short) 2);
            iT.setCode(com.samsung.android.spayfw.appinterface.ISO7816.SW_NO_ERROR);
            return;
        }
        if (iT.getCode() == -28672) {
            switch (s) {
                case F2m.PPB /*3*/:
                    Log.m287i("APDU_LOG", "set apdu error command to NFC_TERMINAL_DETACHED");
                    iT.setCommand((short) 3);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    Log.m287i("APDU_LOG", "set apdu error command to NFC_IN_DISCOVER_MODE");
                    iT.setCommand((short) 5);
                    break;
                default:
                    Log.m287i("APDU_LOG", "set apdu error command to NO_ERROR by default");
                    iT.setCommand((short) 1);
                    break;
            }
        }
        Log.m285d("APDU_LOG", "apdu error: command=" + iT.getCommand() + ", code=" + iT.getCode());
        iM = iL;
    }

    public static ApduReasonCode m563Y() {
        return iT;
    }

    private static final String m566a(short s, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (s) {
            case (short) -32600:
                stringBuilder.append("GPO: ");
                break;
            case (short) -32594:
                stringBuilder.append("Generate AC: ");
                break;
            case (short) -32566:
                stringBuilder.append("GetData: ");
                break;
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
                stringBuilder.append("SelectCard: ");
                break;
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
                stringBuilder.append("SelectPPSE: ");
                break;
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
                stringBuilder.append("ReadRecord: ");
                break;
            default:
                stringBuilder.append("Unknown APDU: ");
                break;
        }
        switch (i) {
            case -28672:
                stringBuilder.append("No error (90,00)");
                break;
            case 25219:
                stringBuilder.append("Selected file invalidated (62,83)");
                break;
            case 25344:
                stringBuilder.append("Authentication failed (63,00)");
                break;
            case 26368:
                stringBuilder.append("Wrong length (67,00)");
                break;
            case 27010:
                stringBuilder.append("Security status not satisfied (69,82)");
                break;
            case 27012:
                stringBuilder.append("Data invalid (69,84)");
                break;
            case 27013:
                stringBuilder.append("Conditions not satisfied (69,85)");
                break;
            case 27014:
                stringBuilder.append("Command not allowed (69,86)");
                break;
            case 27015:
                stringBuilder.append("SM missing (69,87)");
                break;
            case 27016:
                stringBuilder.append("SM incorrect (69,88)");
                break;
            case 27264:
                stringBuilder.append("Wrong data (6A,80)");
                break;
            case 27265:
                stringBuilder.append("Function not supported (6A,81)");
                break;
            case 27266:
                stringBuilder.append("File not found (6A,82)");
                break;
            case 27267:
                stringBuilder.append("Record not found (6A,83)");
                break;
            case 27270:
                stringBuilder.append("Incorrect P1P2 (6A,86)");
                break;
            case 27272:
                stringBuilder.append("HCE invalid select or CPS unknown DGI (6A,88)");
                break;
            case 27904:
                stringBuilder.append("INS not supported (6D,00)");
                break;
            case 28160:
                stringBuilder.append("CLA not supported (6E,00)");
                break;
            case 28416:
                stringBuilder.append("Unknown error response (6F,00)");
                break;
            default:
                stringBuilder.append("Unexpected error code (");
                stringBuilder.append(Integer.toHexString(i / SkeinMac.SKEIN_256).replace(' ', LLVARUtil.EMPTY_STRING).toUpperCase());
                stringBuilder.append(",");
                stringBuilder.append(Integer.toHexString(i % SkeinMac.SKEIN_256).replace(' ', LLVARUtil.EMPTY_STRING).toUpperCase());
                stringBuilder.append(")");
                break;
        }
        return stringBuilder.toString();
    }

    public static final boolean m571g(byte[] bArr) {
        if (Arrays.equals(iQ, bArr) || Arrays.equals(iP, bArr)) {
            return true;
        }
        return false;
    }

    public static final String m567a(short s, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            return null;
        }
        if (s == MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE) {
            iK = true;
        }
        int i = ApduHelper.getShort(new byte[]{bArr[bArr.length - 2], bArr[bArr.length - 1]}, 0);
        if (iT.getCommand() == 1 && ApduHelper.m572i(i)) {
            if (iK) {
                iT.setCommand(s);
                iT.setCode(i);
                Log.m285d("ApduHelper", "Apdu message error: command=" + s + ", code=" + i);
            } else {
                Log.m287i("ApduHelper", "NFC terminal is in discover mode");
            }
        }
        return ApduHelper.m566a(s, i);
    }

    public static final short getShort(byte[] bArr, int i) {
        return (short) ((((short) bArr[i]) << 8) + (((short) bArr[(short) (i + 1)]) & GF2Field.MASK));
    }

    public static final void m564Z() {
        iT.reset();
        iK = false;
        iM = iL;
        iN = 0;
    }

    public static String m565a(int i, byte[] bArr, long j) {
        if (bArr == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (i == 0) {
            stringBuilder.append(">>\n");
        } else if (i == 1) {
            stringBuilder.append("<<\n");
        }
        int length = ((bArr.length - 1) / 16) + 1;
        for (int i2 = 0; i2 < length * 16; i2++) {
            if (i2 < bArr.length) {
                int i3 = bArr[i2] & GF2Field.MASK;
                stringBuilder.append(hexArray[i3 >>> 4]);
                stringBuilder.append(hexArray[i3 & 15]);
                stringBuilder.append(' ');
            } else {
                stringBuilder.append("   ");
            }
            if ((i2 + 1) % 16 == 0) {
                stringBuilder.append("   ");
                char[] cArr = new char[16];
                i3 = (i2 + 1) - 16;
                int i4 = 0;
                while (i4 < 16) {
                    if (i3 < bArr.length) {
                        int i5 = bArr[i3] & GF2Field.MASK;
                        if (i5 > EACTags.NON_INTERINDUSTRY_DATA_OBJECT_NESTING_TEMPLATE || i5 < 32) {
                            cArr[i4] = '.';
                        } else {
                            cArr[i4] = (char) bArr[i3];
                        }
                    } else {
                        cArr[i4] = ' ';
                    }
                    i4++;
                    i3++;
                }
                stringBuilder.append(cArr);
                if (i2 < bArr.length) {
                    stringBuilder.append('\n');
                }
            }
        }
        if (i == 1) {
            stringBuilder.append('(');
            stringBuilder.append(j);
            stringBuilder.append(" ms)");
        }
        return stringBuilder.toString();
    }

    public static final void m570b(short s) {
        iM = s;
        if (s == MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE) {
            iN++;
        }
    }

    public static final boolean m569a(Card card, int i) {
        if (card == null || card.getCardBrand() == null) {
            return false;
        }
        String cardBrand = card.getCardBrand();
        if (cardBrand.equals(PaymentFramework.CARD_BRAND_VISA)) {
            if (iM != com.samsung.android.spayfw.appinterface.ISO7816.READ_RECORD) {
                return false;
            }
            return true;
        } else if (cardBrand.equals(PaymentFramework.CARD_BRAND_AMEX)) {
            if (iM != com.samsung.android.spayfw.appinterface.ISO7816.GENERATE_AC) {
                return false;
            }
            return true;
        } else if (cardBrand.equals(PaymentFramework.CARD_BRAND_MASTERCARD)) {
            if (iM == com.samsung.android.spayfw.appinterface.ISO7816.GENERATE_AC || iM == com.samsung.android.spayfw.appinterface.ISO7816.COMPUTE_CRYPTO_CHECKSUM) {
                return true;
            }
            return false;
        } else if (cardBrand.equals(PlccConstants.BRAND)) {
            return false;
        } else {
            if (cardBrand.equals(PaymentFramework.CARD_BRAND_GIFT)) {
                return false;
            }
            if (!cardBrand.equals(PaymentFramework.CARD_BRAND_DISCOVER)) {
                return false;
            }
            if (card.ad() == null || card.ad().isTransactionComplete() == null) {
                return false;
            }
            return card.ad().isTransactionComplete().getBoolean("TRANSACTION_COMPLETE_STATE");
        }
    }

    public static final int aa() {
        return iN;
    }
}
