package org.bouncycastle.crypto.prng;

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
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Pack;

public class VMPCRandomGenerator implements RandomGenerator {
    private byte[] f252P;
    private byte f253n;
    private byte f254s;

    public VMPCRandomGenerator() {
        this.f253n = (byte) 0;
        this.f252P = new byte[]{(byte) -69, (byte) 44, (byte) 98, Byte.MAX_VALUE, (byte) -75, (byte) -86, GetTemplateApdu.INS, (byte) 13, TLVParser.BYTE_81, (byte) -2, ReadRecordApdu.INS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -53, (byte) -96, (byte) -95, (byte) 8, CardSide.NO_PIN_TEXT_TAG, (byte) 113, (byte) 86, (byte) -24, (byte) 73, (byte) 2, Tnaf.POW_2_WIDTH, (byte) -60, (byte) -34, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -20, VerifyPINApdu.P2_PLAINTEXT, CLD.FORM_FACTOR_TAG, (byte) -72, (byte) 105, PutData80Apdu.INS, (byte) 47, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -52, (byte) -94, (byte) 9, (byte) 54, (byte) 3, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, SetResetParamApdu.INS, (byte) -3, (byte) -32, (byte) -35, (byte) 5, (byte) 67, SetResetParamApdu.CLA, (byte) -83, (byte) -56, (byte) -31, (byte) -81, (byte) 87, (byte) -101, (byte) 76, (byte) -40, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, MPPLiteInstruction.INS_GENERATE_AC, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -123, (byte) 60, (byte) 10, (byte) -28, (byte) -13, (byte) -100, (byte) 38, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -55, (byte) -125, (byte) -105, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -79, (byte) -103, (byte) 100, (byte) 49, ApplicationInfoManager.TERM_XP2, (byte) -43, (byte) 29, (byte) -42, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -67, (byte) 94, (byte) -80, (byte) -118, (byte) 34, (byte) 56, (byte) -8, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 43, GenerateACApdu.INS, (byte) -59, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -9, PSSSigner.TRAILER_IMPLICIT, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -33, (byte) 4, (byte) -27, (byte) -107, (byte) 62, (byte) 37, (byte) -122, (byte) -90, (byte) 11, (byte) -113, (byte) -15, PinChangeUnblockApdu.INS, (byte) 14, (byte) -41, EMVGetStatusApdu.P1, (byte) -77, (byte) -49, (byte) 126, (byte) 6, CardSide.CARD_ELEMENTS_TAG, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 77, (byte) 28, (byte) -93, (byte) -37, (byte) 50, (byte) -110, ApplicationInfoManager.TERM_XP1, CLD.VERSION_TAG, (byte) 39, (byte) -12, (byte) 89, (byte) -48, (byte) 78, (byte) 106, CardSide.PIN_TEXT_TAG, (byte) 91, (byte) -84, (byte) -1, (byte) 7, EMVGetResponse.INS, (byte) 101, (byte) 121, (byte) -4, (byte) -57, (byte) -51, (byte) 118, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 93, (byte) -25, (byte) 58, (byte) 52, (byte) 122, (byte) 48, (byte) 40, (byte) 15, (byte) 115, (byte) 1, (byte) -7, (byte) -47, PutTemplateApdu.INS, (byte) 25, (byte) -23, (byte) -111, (byte) -71, (byte) 90, (byte) -19, (byte) 65, (byte) 109, (byte) -76, (byte) -61, (byte) -98, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) 99, (byte) -6, (byte) 31, ApplicationInfoManager.TERM_XP3, (byte) 96, (byte) 71, (byte) -119, EMVSetStatusApdu.INS, (byte) -106, (byte) 26, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -109, (byte) 61, (byte) 55, (byte) 75, (byte) -39, GetProcessingOptions.INS, ApplicationInfoManager.MS_ONLY, (byte) 27, (byte) -10, ApplicationInfoManager.EMV_MS, (byte) -117, ApplicationInfoManager.EMV_ONLY, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, VerifyPINApdu.INS, (byte) -50, VerifyPINApdu.P2_CIPHERED, (byte) 110, (byte) -74, (byte) 116, (byte) -114, (byte) -115, CardSide.ALWAYS_TEXT_TAG, (byte) 41, EMVGetStatusApdu.INS, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -11, (byte) -21, (byte) 112, (byte) -29, (byte) -5, (byte) 85, (byte) -97, (byte) -58, (byte) 68, (byte) 74, (byte) 69, (byte) 125, (byte) -30, (byte) 107, (byte) 92, (byte) 108, (byte) 102, (byte) -87, (byte) -116, (byte) -18, PinChangeUnblockApdu.CLA, CardSide.BACKGROUND_TAG, (byte) -89, (byte) 30, (byte) -99, (byte) -36, (byte) 103, (byte) 72, (byte) -70, (byte) 46, (byte) -26, ISO7816.INS_SELECT, (byte) -85, (byte) 124, (byte) -108, (byte) 0, (byte) 33, (byte) -17, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -66, GetDataApdu.INS, (byte) 114, GetTemplateApdu.TAG_DF_NAME_4F, (byte) 82, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) 63, (byte) -62, CardSide.PICTURE_TAG, (byte) 123, (byte) 59, (byte) 84};
        this.f254s = (byte) -66;
    }

    public void addSeedMaterial(long j) {
        addSeedMaterial(Pack.longToBigEndian(j));
    }

    public void addSeedMaterial(byte[] bArr) {
        for (byte b : bArr) {
            this.f254s = this.f252P[((this.f254s + this.f252P[this.f253n & GF2Field.MASK]) + b) & GF2Field.MASK];
            byte b2 = this.f252P[this.f253n & GF2Field.MASK];
            this.f252P[this.f253n & GF2Field.MASK] = this.f252P[this.f254s & GF2Field.MASK];
            this.f252P[this.f254s & GF2Field.MASK] = b2;
            this.f253n = (byte) ((this.f253n + 1) & GF2Field.MASK);
        }
    }

    public void nextBytes(byte[] bArr) {
        nextBytes(bArr, 0, bArr.length);
    }

    public void nextBytes(byte[] bArr, int i, int i2) {
        synchronized (this.f252P) {
            int i3 = i + i2;
            while (i != i3) {
                this.f254s = this.f252P[(this.f254s + this.f252P[this.f253n & GF2Field.MASK]) & GF2Field.MASK];
                bArr[i] = this.f252P[(this.f252P[this.f252P[this.f254s & GF2Field.MASK] & GF2Field.MASK] + 1) & GF2Field.MASK];
                byte b = this.f252P[this.f253n & GF2Field.MASK];
                this.f252P[this.f253n & GF2Field.MASK] = this.f252P[this.f254s & GF2Field.MASK];
                this.f252P[this.f254s & GF2Field.MASK] = b;
                this.f253n = (byte) ((this.f253n + 1) & GF2Field.MASK);
                i++;
            }
        }
    }
}
