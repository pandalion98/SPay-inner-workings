package org.bouncycastle.crypto.engines;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mcbp.core.mpplite.MPPLiteInstruction;
import com.mastercard.mobile_api.payment.cld.CLD;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetResponse;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetDataApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetProcessingOptions;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutData80Apdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.ReadRecordApdu;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public final class TwofishEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int GF256_FDBK = 361;
    private static final int GF256_FDBK_2 = 180;
    private static final int GF256_FDBK_4 = 90;
    private static final int INPUT_WHITEN = 0;
    private static final int MAX_KEY_BITS = 256;
    private static final int MAX_ROUNDS = 16;
    private static final int OUTPUT_WHITEN = 4;
    private static final byte[][] f168P;
    private static final int P_00 = 1;
    private static final int P_01 = 0;
    private static final int P_02 = 0;
    private static final int P_03 = 1;
    private static final int P_04 = 1;
    private static final int P_10 = 0;
    private static final int P_11 = 0;
    private static final int P_12 = 1;
    private static final int P_13 = 1;
    private static final int P_14 = 0;
    private static final int P_20 = 1;
    private static final int P_21 = 1;
    private static final int P_22 = 0;
    private static final int P_23 = 0;
    private static final int P_24 = 0;
    private static final int P_30 = 0;
    private static final int P_31 = 1;
    private static final int P_32 = 1;
    private static final int P_33 = 0;
    private static final int P_34 = 1;
    private static final int ROUNDS = 16;
    private static final int ROUND_SUBKEYS = 8;
    private static final int RS_GF_FDBK = 333;
    private static final int SK_BUMP = 16843009;
    private static final int SK_ROTL = 9;
    private static final int SK_STEP = 33686018;
    private static final int TOTAL_SUBKEYS = 40;
    private boolean encrypting;
    private int[] gMDS0;
    private int[] gMDS1;
    private int[] gMDS2;
    private int[] gMDS3;
    private int[] gSBox;
    private int[] gSubKeys;
    private int k64Cnt;
    private byte[] workingKey;

    static {
        f168P = new byte[][]{new byte[]{(byte) -87, (byte) 103, (byte) -77, (byte) -24, (byte) 4, (byte) -3, (byte) -93, (byte) 118, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -110, VerifyPINApdu.P2_PLAINTEXT, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -28, (byte) -35, (byte) -47, (byte) 56, (byte) 13, (byte) -58, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CardSide.NO_PIN_TEXT_TAG, (byte) -9, (byte) -20, (byte) 108, (byte) 67, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 55, (byte) 38, (byte) -6, CardSide.BACKGROUND_TAG, (byte) -108, (byte) 72, EMVGetStatusApdu.INS, (byte) -48, (byte) -117, (byte) 48, PinChangeUnblockApdu.CLA, (byte) 84, (byte) -33, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 25, (byte) 91, (byte) 61, (byte) 89, (byte) -13, MPPLiteInstruction.INS_GENERATE_AC, (byte) -94, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) 99, (byte) 1, (byte) -125, (byte) 46, (byte) -39, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -101, (byte) 124, (byte) -90, (byte) -21, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -66, CardSide.ALWAYS_TEXT_TAG, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) -29, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, EMVGetResponse.INS, (byte) -116, (byte) 58, (byte) -11, (byte) 115, (byte) 44, (byte) 37, (byte) 11, (byte) -69, (byte) 78, (byte) -119, (byte) 107, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 106, (byte) -76, (byte) -15, (byte) -31, (byte) -26, (byte) -67, (byte) 69, (byte) -30, (byte) -12, (byte) -74, (byte) 102, (byte) -52, (byte) -107, (byte) 3, (byte) 86, GetTemplateApdu.INS, (byte) 28, (byte) 30, (byte) -41, (byte) -5, (byte) -61, (byte) -114, (byte) -75, (byte) -23, (byte) -49, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) -70, MPPLiteInstruction.INS_RELAY_RESISTANCE, ApplicationInfoManager.TERM_XP2, ApplicationInfoManager.EMV_MS, (byte) -81, ApplicationInfoManager.TERM_XP3, (byte) -55, (byte) 98, (byte) 113, TLVParser.BYTE_81, (byte) 121, (byte) 9, (byte) -83, PinChangeUnblockApdu.INS, (byte) -51, (byte) -7, (byte) -40, (byte) -27, (byte) -59, (byte) -71, (byte) 77, (byte) 68, (byte) 8, (byte) -122, (byte) -25, (byte) -95, (byte) 29, (byte) -86, (byte) -19, (byte) 6, (byte) 112, ReadRecordApdu.INS, PutTemplateApdu.INS, (byte) 65, (byte) 123, (byte) -96, CLD.VERSION_TAG, (byte) 49, (byte) -62, (byte) 39, SetResetParamApdu.CLA, VerifyPINApdu.INS, (byte) -10, (byte) 96, (byte) -1, (byte) -106, (byte) 92, (byte) -79, (byte) -85, (byte) -98, (byte) -100, (byte) 82, (byte) 27, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -109, (byte) 10, (byte) -17, (byte) -111, (byte) -123, (byte) 73, (byte) -18, SetResetParamApdu.INS, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -113, (byte) 59, (byte) 71, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) 109, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -42, (byte) 62, (byte) 105, (byte) 100, GenerateACApdu.INS, (byte) -50, (byte) -53, (byte) 47, (byte) -4, (byte) -105, (byte) 5, (byte) 122, (byte) -84, Byte.MAX_VALUE, (byte) -43, (byte) 26, (byte) 75, (byte) 14, (byte) -89, (byte) 90, (byte) 40, CardSide.PICTURE_TAG, (byte) 63, (byte) 41, VerifyPINApdu.P2_CIPHERED, (byte) 60, (byte) 76, (byte) 2, (byte) -72, PutData80Apdu.INS, (byte) -80, CardSide.PIN_TEXT_TAG, (byte) 85, (byte) 31, (byte) -118, (byte) 125, (byte) 87, (byte) -57, (byte) -115, (byte) 116, ApplicationInfoManager.EMV_ONLY, (byte) -60, (byte) -97, (byte) 114, (byte) 126, CardSide.CARD_ELEMENTS_TAG, (byte) 34, CLD.FORM_FACTOR_TAG, ApplicationInfoManager.TERM_XP1, (byte) 7, (byte) -103, (byte) 52, (byte) 110, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -34, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 101, PSSSigner.TRAILER_IMPLICIT, (byte) -37, (byte) -8, (byte) -56, GetProcessingOptions.INS, (byte) 43, EMVGetStatusApdu.P1, (byte) -36, (byte) -2, (byte) 50, ISO7816.INS_SELECT, GetDataApdu.INS, Tnaf.POW_2_WIDTH, (byte) 33, EMVSetStatusApdu.INS, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 93, (byte) 15, (byte) 0, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -99, (byte) 54, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 74, (byte) 94, ApplicationInfoManager.MS_ONLY, (byte) -32}, new byte[]{ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -13, (byte) -58, (byte) -12, (byte) -37, (byte) 123, (byte) -5, (byte) -56, (byte) 74, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -26, (byte) 107, (byte) 69, (byte) 125, (byte) -24, (byte) 75, (byte) -42, (byte) 50, (byte) -40, (byte) -3, (byte) 55, (byte) 113, (byte) -15, (byte) -31, (byte) 48, (byte) 15, (byte) -8, (byte) 27, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -6, (byte) 6, (byte) 63, (byte) 94, (byte) -70, MPPLiteInstruction.INS_GENERATE_AC, (byte) 91, (byte) -118, (byte) 0, PSSSigner.TRAILER_IMPLICIT, (byte) -99, (byte) 109, ApplicationInfoManager.MS_ONLY, (byte) -79, (byte) 14, VerifyPINApdu.P2_PLAINTEXT, (byte) 93, PutTemplateApdu.INS, (byte) -43, (byte) -96, PinChangeUnblockApdu.CLA, (byte) 7, CardSide.PICTURE_TAG, (byte) -75, SetResetParamApdu.CLA, (byte) 44, (byte) -93, ReadRecordApdu.INS, (byte) 115, (byte) 76, (byte) 84, (byte) -110, (byte) 116, (byte) 54, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) 56, (byte) -80, (byte) -67, (byte) 90, (byte) -4, (byte) 96, (byte) 98, (byte) -106, (byte) 108, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -9, Tnaf.POW_2_WIDTH, (byte) 124, (byte) 40, (byte) 39, (byte) -116, CardSide.BACKGROUND_TAG, (byte) -107, (byte) -100, (byte) -57, PinChangeUnblockApdu.INS, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 59, (byte) 112, GetDataApdu.INS, (byte) -29, (byte) -123, (byte) -53, CLD.VERSION_TAG, (byte) -48, (byte) -109, (byte) -72, (byte) -90, (byte) -125, VerifyPINApdu.INS, (byte) -1, (byte) -97, ApplicationInfoManager.TERM_XP2, (byte) -61, (byte) -52, (byte) 3, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, EMVGetStatusApdu.P1, (byte) -25, (byte) 43, (byte) -30, (byte) 121, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) -86, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) 65, (byte) 58, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -71, (byte) -28, ApplicationInfoManager.MOB_CVM_PERFORMED, ISO7816.INS_SELECT, (byte) -105, (byte) 126, PutData80Apdu.INS, (byte) 122, CardSide.PIN_TEXT_TAG, (byte) 102, (byte) -108, (byte) -95, (byte) 29, (byte) 61, EMVSetStatusApdu.INS, (byte) -34, (byte) -77, (byte) 11, (byte) 114, (byte) -89, (byte) 28, (byte) -17, (byte) -47, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 62, (byte) -113, ApplicationInfoManager.TERM_XP3, (byte) 38, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -20, (byte) 118, GenerateACApdu.INS, (byte) 73, TLVParser.BYTE_81, VerifyPINApdu.P2_CIPHERED, (byte) -18, (byte) 33, (byte) -60, (byte) 26, (byte) -21, (byte) -39, (byte) -59, ApplicationInfoManager.EMV_MS, (byte) -103, (byte) -51, (byte) -83, (byte) 49, (byte) -117, (byte) 1, CardSide.NO_PIN_TEXT_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -35, (byte) 31, (byte) 78, SetResetParamApdu.INS, (byte) -7, (byte) 72, GetTemplateApdu.TAG_DF_NAME_4F, EMVGetStatusApdu.INS, (byte) 101, (byte) -114, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 92, ApplicationInfoManager.TERM_XP1, (byte) 25, (byte) -115, (byte) -27, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) 87, (byte) 103, Byte.MAX_VALUE, (byte) 5, (byte) 100, (byte) -81, (byte) 99, (byte) -74, (byte) -2, (byte) -11, ApplicationInfoManager.EMV_ONLY, (byte) 60, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -50, (byte) -23, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 68, (byte) -32, (byte) 77, (byte) 67, (byte) 105, (byte) 41, (byte) 46, (byte) -84, CardSide.CARD_ELEMENTS_TAG, (byte) 89, GetProcessingOptions.INS, (byte) 10, (byte) -98, (byte) 110, (byte) 71, (byte) -33, (byte) 52, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 106, (byte) -49, (byte) -36, (byte) 34, (byte) -55, EMVGetResponse.INS, (byte) -101, (byte) -119, GetTemplateApdu.INS, (byte) -19, (byte) -85, CLD.FORM_FACTOR_TAG, (byte) -94, (byte) 13, (byte) 82, (byte) -69, (byte) 2, (byte) 47, (byte) -87, (byte) -41, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 30, (byte) -76, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 4, (byte) -10, (byte) -62, CardSide.ALWAYS_TEXT_TAG, (byte) 37, (byte) -122, (byte) 86, (byte) 85, (byte) 9, (byte) -66, (byte) -111}};
    }

    public TwofishEngine() {
        this.encrypting = false;
        this.gMDS0 = new int[MAX_KEY_BITS];
        this.gMDS1 = new int[MAX_KEY_BITS];
        this.gMDS2 = new int[MAX_KEY_BITS];
        this.gMDS3 = new int[MAX_KEY_BITS];
        this.k64Cnt = P_33;
        this.workingKey = null;
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        int[] iArr3 = new int[2];
        for (int i = P_33; i < MAX_KEY_BITS; i += P_34) {
            int i2 = f168P[P_33][i] & GF2Field.MASK;
            iArr[P_33] = i2;
            iArr2[P_33] = Mx_X(i2) & GF2Field.MASK;
            iArr3[P_33] = Mx_Y(i2) & GF2Field.MASK;
            i2 = f168P[P_34][i] & GF2Field.MASK;
            iArr[P_34] = i2;
            iArr2[P_34] = Mx_X(i2) & GF2Field.MASK;
            iArr3[P_34] = Mx_Y(i2) & GF2Field.MASK;
            this.gMDS0[i] = ((iArr[P_34] | (iArr2[P_34] << ROUND_SUBKEYS)) | (iArr3[P_34] << ROUNDS)) | (iArr3[P_34] << 24);
            this.gMDS1[i] = ((iArr3[P_33] | (iArr3[P_33] << ROUND_SUBKEYS)) | (iArr2[P_33] << ROUNDS)) | (iArr[P_33] << 24);
            this.gMDS2[i] = ((iArr2[P_34] | (iArr3[P_34] << ROUND_SUBKEYS)) | (iArr[P_34] << ROUNDS)) | (iArr3[P_34] << 24);
            this.gMDS3[i] = ((iArr2[P_33] | (iArr[P_33] << ROUND_SUBKEYS)) | (iArr3[P_33] << ROUNDS)) | (iArr2[P_33] << 24);
        }
    }

    private void Bits32ToBytes(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + P_34] = (byte) (i >> ROUND_SUBKEYS);
        bArr[i2 + 2] = (byte) (i >> ROUNDS);
        bArr[i2 + 3] = (byte) (i >> 24);
    }

    private int BytesTo32Bits(byte[] bArr, int i) {
        return (((bArr[i] & GF2Field.MASK) | ((bArr[i + P_34] & GF2Field.MASK) << ROUND_SUBKEYS)) | ((bArr[i + 2] & GF2Field.MASK) << ROUNDS)) | ((bArr[i + 3] & GF2Field.MASK) << 24);
    }

    private int F32(int i, int[] iArr) {
        int b0 = b0(i);
        int b1 = b1(i);
        int b2 = b2(i);
        int b3 = b3(i);
        int i2 = iArr[P_33];
        int i3 = iArr[P_34];
        int i4 = iArr[2];
        int i5 = iArr[3];
        switch (this.k64Cnt & 3) {
            case P_33 /*0*/:
                b0 = (f168P[P_34][b0] & GF2Field.MASK) ^ b0(i5);
                b1 = (f168P[P_33][b1] & GF2Field.MASK) ^ b1(i5);
                b2 = (f168P[P_33][b2] & GF2Field.MASK) ^ b2(i5);
                b3 = (f168P[P_34][b3] & GF2Field.MASK) ^ b3(i5);
                break;
            case P_34 /*1*/:
                return this.gMDS3[(f168P[P_34][b3] & GF2Field.MASK) ^ b3(i2)] ^ (this.gMDS2[(f168P[P_34][b2] & GF2Field.MASK) ^ b2(i2)] ^ (this.gMDS1[(f168P[P_33][b1] & GF2Field.MASK) ^ b1(i2)] ^ this.gMDS0[(f168P[P_33][b0] & GF2Field.MASK) ^ b0(i2)]));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                break;
            case F2m.PPB /*3*/:
                break;
            default:
                return P_33;
        }
        b0 = (f168P[P_34][b0] & GF2Field.MASK) ^ b0(i4);
        b1 = (f168P[P_34][b1] & GF2Field.MASK) ^ b1(i4);
        b2 = (f168P[P_33][b2] & GF2Field.MASK) ^ b2(i4);
        b3 = (f168P[P_33][b3] & GF2Field.MASK) ^ b3(i4);
        return this.gMDS3[(f168P[P_34][(f168P[P_34][b3] & GF2Field.MASK) ^ b3(i3)] & GF2Field.MASK) ^ b3(i2)] ^ (this.gMDS2[(f168P[P_34][(f168P[P_33][b2] & GF2Field.MASK) ^ b2(i3)] & GF2Field.MASK) ^ b2(i2)] ^ (this.gMDS1[(f168P[P_33][(f168P[P_34][b1] & GF2Field.MASK) ^ b1(i3)] & GF2Field.MASK) ^ b1(i2)] ^ this.gMDS0[(f168P[P_33][(f168P[P_33][b0] & GF2Field.MASK) ^ b0(i3)] & GF2Field.MASK) ^ b0(i2)]));
    }

    private int Fe32_0(int i) {
        return ((this.gSBox[((i & GF2Field.MASK) * 2) + P_33] ^ this.gSBox[(((i >>> ROUND_SUBKEYS) & GF2Field.MASK) * 2) + P_34]) ^ this.gSBox[(((i >>> ROUNDS) & GF2Field.MASK) * 2) + SkeinMac.SKEIN_512]) ^ this.gSBox[(((i >>> 24) & GF2Field.MASK) * 2) + McTACommands.MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT];
    }

    private int Fe32_3(int i) {
        return ((this.gSBox[(((i >>> 24) & GF2Field.MASK) * 2) + P_33] ^ this.gSBox[((i & GF2Field.MASK) * 2) + P_34]) ^ this.gSBox[(((i >>> ROUND_SUBKEYS) & GF2Field.MASK) * 2) + SkeinMac.SKEIN_512]) ^ this.gSBox[(((i >>> ROUNDS) & GF2Field.MASK) * 2) + McTACommands.MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT];
    }

    private int LFSR1(int i) {
        return ((i & P_34) != 0 ? GF256_FDBK_2 : P_33) ^ (i >> P_34);
    }

    private int LFSR2(int i) {
        int i2 = P_33;
        int i3 = ((i & 2) != 0 ? GF256_FDBK_2 : P_33) ^ (i >> 2);
        if ((i & P_34) != 0) {
            i2 = GF256_FDBK_4;
        }
        return i2 ^ i3;
    }

    private int Mx_X(int i) {
        return LFSR2(i) ^ i;
    }

    private int Mx_Y(int i) {
        return (LFSR1(i) ^ i) ^ LFSR2(i);
    }

    private int RS_MDS_Encode(int i, int i2) {
        int i3;
        int i4 = P_33;
        for (i3 = P_33; i3 < OUTPUT_WHITEN; i3 += P_34) {
            i2 = RS_rem(i2);
        }
        i3 = i2 ^ i;
        while (i4 < OUTPUT_WHITEN) {
            i3 = RS_rem(i3);
            i4 += P_34;
        }
        return i3;
    }

    private int RS_rem(int i) {
        int i2 = P_33;
        int i3 = (i >>> 24) & GF2Field.MASK;
        int i4 = (((i3 & X509KeyUsage.digitalSignature) != 0 ? RS_GF_FDBK : P_33) ^ (i3 << P_34)) & GF2Field.MASK;
        int i5 = i3 >>> P_34;
        if ((i3 & P_34) != 0) {
            i2 = CipherSuite.TLS_DH_anon_WITH_AES_128_GCM_SHA256;
        }
        i2 = (i2 ^ i5) ^ i4;
        return ((i2 << ROUND_SUBKEYS) ^ ((i4 << ROUNDS) ^ ((i << ROUND_SUBKEYS) ^ (i2 << 24)))) ^ i3;
    }

    private int b0(int i) {
        return i & GF2Field.MASK;
    }

    private int b1(int i) {
        return (i >>> ROUND_SUBKEYS) & GF2Field.MASK;
    }

    private int b2(int i) {
        return (i >>> ROUNDS) & GF2Field.MASK;
    }

    private int b3(int i) {
        return (i >>> 24) & GF2Field.MASK;
    }

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int BytesTo32Bits = BytesTo32Bits(bArr, i) ^ this.gSubKeys[OUTPUT_WHITEN];
        int BytesTo32Bits2 = BytesTo32Bits(bArr, i + OUTPUT_WHITEN) ^ this.gSubKeys[5];
        int BytesTo32Bits3 = BytesTo32Bits(bArr, i + ROUND_SUBKEYS) ^ this.gSubKeys[6];
        int BytesTo32Bits4 = BytesTo32Bits(bArr, i + 12) ^ this.gSubKeys[7];
        int i3 = 39;
        for (int i4 = P_33; i4 < ROUNDS; i4 += 2) {
            int Fe32_0 = Fe32_0(BytesTo32Bits);
            int Fe32_3 = Fe32_3(BytesTo32Bits2);
            int i5 = i3 - 1;
            i3 = (this.gSubKeys[i3] + ((Fe32_3 * 2) + Fe32_0)) ^ BytesTo32Bits4;
            BytesTo32Bits4 = (BytesTo32Bits3 << P_34) | (BytesTo32Bits3 >>> 31);
            BytesTo32Bits3 = Fe32_0 + Fe32_3;
            Fe32_3 = i5 - 1;
            BytesTo32Bits3 = (BytesTo32Bits3 + this.gSubKeys[i5]) ^ BytesTo32Bits4;
            BytesTo32Bits4 = (i3 >>> P_34) | (i3 << 31);
            i3 = Fe32_0(BytesTo32Bits3);
            Fe32_0 = Fe32_3(BytesTo32Bits4);
            i5 = Fe32_3 - 1;
            BytesTo32Bits2 ^= this.gSubKeys[Fe32_3] + ((Fe32_0 * 2) + i3);
            Fe32_0 += i3;
            i3 = i5 - 1;
            BytesTo32Bits = ((BytesTo32Bits >>> 31) | (BytesTo32Bits << P_34)) ^ (Fe32_0 + this.gSubKeys[i5]);
            BytesTo32Bits2 = (BytesTo32Bits2 << 31) | (BytesTo32Bits2 >>> P_34);
        }
        Bits32ToBytes(this.gSubKeys[P_33] ^ BytesTo32Bits3, bArr2, i2);
        Bits32ToBytes(this.gSubKeys[P_34] ^ BytesTo32Bits4, bArr2, i2 + OUTPUT_WHITEN);
        Bits32ToBytes(this.gSubKeys[2] ^ BytesTo32Bits, bArr2, i2 + ROUND_SUBKEYS);
        Bits32ToBytes(this.gSubKeys[3] ^ BytesTo32Bits2, bArr2, i2 + 12);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = P_33;
        int BytesTo32Bits = BytesTo32Bits(bArr, i) ^ this.gSubKeys[P_33];
        int BytesTo32Bits2 = BytesTo32Bits(bArr, i + OUTPUT_WHITEN) ^ this.gSubKeys[P_34];
        int BytesTo32Bits3 = BytesTo32Bits(bArr, i + ROUND_SUBKEYS) ^ this.gSubKeys[2];
        int BytesTo32Bits4 = this.gSubKeys[3] ^ BytesTo32Bits(bArr, i + 12);
        int i4 = ROUND_SUBKEYS;
        while (i3 < ROUNDS) {
            int Fe32_0 = Fe32_0(BytesTo32Bits);
            int Fe32_3 = Fe32_3(BytesTo32Bits2);
            int i5 = i4 + P_34;
            i4 = (this.gSubKeys[i4] + (Fe32_0 + Fe32_3)) ^ BytesTo32Bits3;
            BytesTo32Bits3 = (i4 >>> P_34) | (i4 << 31);
            Fe32_3 = i5 + P_34;
            BytesTo32Bits4 = (((Fe32_3 * 2) + Fe32_0) + this.gSubKeys[i5]) ^ ((BytesTo32Bits4 << P_34) | (BytesTo32Bits4 >>> 31));
            i4 = Fe32_0(BytesTo32Bits3);
            Fe32_0 = Fe32_3(BytesTo32Bits4);
            i5 = Fe32_3 + P_34;
            BytesTo32Bits ^= this.gSubKeys[Fe32_3] + (i4 + Fe32_0);
            BytesTo32Bits = (BytesTo32Bits << 31) | (BytesTo32Bits >>> P_34);
            Fe32_0 = (Fe32_0 * 2) + i4;
            i4 = i5 + P_34;
            BytesTo32Bits2 = ((BytesTo32Bits2 >>> 31) | (BytesTo32Bits2 << P_34)) ^ (Fe32_0 + this.gSubKeys[i5]);
            i3 += 2;
        }
        Bits32ToBytes(this.gSubKeys[OUTPUT_WHITEN] ^ BytesTo32Bits3, bArr2, i2);
        Bits32ToBytes(this.gSubKeys[5] ^ BytesTo32Bits4, bArr2, i2 + OUTPUT_WHITEN);
        Bits32ToBytes(this.gSubKeys[6] ^ BytesTo32Bits, bArr2, i2 + ROUND_SUBKEYS);
        Bits32ToBytes(this.gSubKeys[7] ^ BytesTo32Bits2, bArr2, i2 + 12);
    }

    private void setKey(byte[] bArr) {
        int[] iArr = new int[OUTPUT_WHITEN];
        int[] iArr2 = new int[OUTPUT_WHITEN];
        int[] iArr3 = new int[OUTPUT_WHITEN];
        this.gSubKeys = new int[TOTAL_SUBKEYS];
        if (this.k64Cnt < P_34) {
            throw new IllegalArgumentException("Key size less than 64 bits");
        } else if (this.k64Cnt > OUTPUT_WHITEN) {
            throw new IllegalArgumentException("Key size larger than 256 bits");
        } else {
            int i;
            int i2;
            int F32;
            for (i = P_33; i < this.k64Cnt; i += P_34) {
                i2 = i * ROUND_SUBKEYS;
                iArr[i] = BytesTo32Bits(bArr, i2);
                iArr2[i] = BytesTo32Bits(bArr, i2 + OUTPUT_WHITEN);
                iArr3[(this.k64Cnt - 1) - i] = RS_MDS_Encode(iArr[i], iArr2[i]);
            }
            for (i = P_33; i < 20; i += P_34) {
                i2 = SK_STEP * i;
                F32 = F32(i2, iArr);
                i2 = F32(i2 + SK_BUMP, iArr2);
                i2 = (i2 >>> 24) | (i2 << ROUND_SUBKEYS);
                F32 += i2;
                this.gSubKeys[i * 2] = F32;
                i2 += F32;
                this.gSubKeys[(i * 2) + P_34] = (i2 >>> 23) | (i2 << SK_ROTL);
            }
            F32 = iArr3[P_33];
            int i3 = iArr3[P_34];
            int i4 = iArr3[2];
            int i5 = iArr3[3];
            this.gSBox = new int[SkeinMac.SKEIN_1024];
            for (int i6 = P_33; i6 < MAX_KEY_BITS; i6 += P_34) {
                int b1;
                int b2;
                switch (this.k64Cnt & 3) {
                    case P_33 /*0*/:
                        i2 = (f168P[P_34][i6] & GF2Field.MASK) ^ b0(i5);
                        b1 = (f168P[P_33][i6] & GF2Field.MASK) ^ b1(i5);
                        b2 = b2(i5) ^ (f168P[P_33][i6] & GF2Field.MASK);
                        i = (f168P[P_34][i6] & GF2Field.MASK) ^ b3(i5);
                        break;
                    case P_34 /*1*/:
                        this.gSBox[i6 * 2] = this.gMDS0[(f168P[P_33][i6] & GF2Field.MASK) ^ b0(F32)];
                        this.gSBox[(i6 * 2) + P_34] = this.gMDS1[(f168P[P_33][i6] & GF2Field.MASK) ^ b1(F32)];
                        this.gSBox[(i6 * 2) + SkeinMac.SKEIN_512] = this.gMDS2[(f168P[P_34][i6] & GF2Field.MASK) ^ b2(F32)];
                        this.gSBox[(i6 * 2) + McTACommands.MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT] = this.gMDS3[(f168P[P_34][i6] & GF2Field.MASK) ^ b3(F32)];
                        continue;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        i = i6;
                        b2 = i6;
                        b1 = i6;
                        i2 = i6;
                        break;
                    case F2m.PPB /*3*/:
                        i = i6;
                        b2 = i6;
                        b1 = i6;
                        i2 = i6;
                        break;
                    default:
                        break;
                }
                i2 = (f168P[P_34][i2] & GF2Field.MASK) ^ b0(i4);
                b1 = (f168P[P_34][b1] & GF2Field.MASK) ^ b1(i4);
                b2 = (f168P[P_33][b2] & GF2Field.MASK) ^ b2(i4);
                i = (f168P[P_33][i] & GF2Field.MASK) ^ b3(i4);
                this.gSBox[i6 * 2] = this.gMDS0[(f168P[P_33][(f168P[P_33][i2] & GF2Field.MASK) ^ b0(i3)] & GF2Field.MASK) ^ b0(F32)];
                this.gSBox[(i6 * 2) + P_34] = this.gMDS1[(f168P[P_33][(f168P[P_34][b1] & GF2Field.MASK) ^ b1(i3)] & GF2Field.MASK) ^ b1(F32)];
                this.gSBox[(i6 * 2) + SkeinMac.SKEIN_512] = this.gMDS2[(f168P[P_34][(f168P[P_33][b2] & GF2Field.MASK) ^ b2(i3)] & GF2Field.MASK) ^ b2(F32)];
                this.gSBox[(i6 * 2) + McTACommands.MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT] = this.gMDS3[(f168P[P_34][(f168P[P_34][i] & GF2Field.MASK) ^ b3(i3)] & GF2Field.MASK) ^ b3(F32)];
            }
        }
    }

    public String getAlgorithmName() {
        return "Twofish";
    }

    public int getBlockSize() {
        return ROUNDS;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = z;
            this.workingKey = ((KeyParameter) cipherParameters).getKey();
            this.k64Cnt = this.workingKey.length / ROUND_SUBKEYS;
            setKey(this.workingKey);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Twofish init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Twofish not initialised");
        } else if (i + ROUNDS > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + ROUNDS > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.encrypting) {
                encryptBlock(bArr, i, bArr2, i2);
            } else {
                decryptBlock(bArr, i, bArr2, i2);
            }
            return ROUNDS;
        }
    }

    public void reset() {
        if (this.workingKey != null) {
            setKey(this.workingKey);
        }
    }
}
