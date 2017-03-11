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
import java.lang.reflect.Array;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class AESLightEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[] f148S;
    private static final byte[] Si;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey;
    private boolean forEncryption;

    static {
        f148S = new byte[]{(byte) 99, (byte) 124, ApplicationInfoManager.TERM_XP2, (byte) 123, EMVGetStatusApdu.INS, (byte) 107, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -59, (byte) 48, (byte) 1, (byte) 103, (byte) 43, (byte) -2, (byte) -41, (byte) -85, (byte) 118, GetDataApdu.INS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -55, (byte) 125, (byte) -6, (byte) 89, (byte) 71, EMVSetStatusApdu.INS, (byte) -83, GetTemplateApdu.INS, (byte) -94, (byte) -81, (byte) -100, ISO7816.INS_SELECT, (byte) 114, EMVGetResponse.INS, ApplicationInfoManager.EMV_ONLY, (byte) -3, (byte) -109, (byte) 38, (byte) 54, (byte) 63, (byte) -9, (byte) -52, (byte) 52, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -27, (byte) -15, (byte) 113, (byte) -40, (byte) 49, CardSide.CARD_ELEMENTS_TAG, (byte) 4, (byte) -57, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -61, CardSide.NO_PIN_TEXT_TAG, (byte) -106, (byte) 5, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 7, CLD.FORM_FACTOR_TAG, VerifyPINApdu.P2_PLAINTEXT, (byte) -30, (byte) -21, (byte) 39, ReadRecordApdu.INS, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 9, (byte) -125, (byte) 44, (byte) 26, (byte) 27, (byte) 110, (byte) 90, (byte) -96, (byte) 82, (byte) 59, (byte) -42, (byte) -77, (byte) 41, (byte) -29, (byte) 47, PinChangeUnblockApdu.CLA, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -47, (byte) 0, (byte) -19, VerifyPINApdu.INS, (byte) -4, (byte) -79, (byte) 91, (byte) 106, (byte) -53, (byte) -66, ApplicationInfoManager.EMV_MS, (byte) 74, (byte) 76, ApplicationInfoManager.TERM_XP1, (byte) -49, (byte) -48, (byte) -17, (byte) -86, (byte) -5, (byte) 67, (byte) 77, ApplicationInfoManager.TERM_XP3, (byte) -123, (byte) 69, (byte) -7, (byte) 2, Byte.MAX_VALUE, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 60, (byte) -97, GetProcessingOptions.INS, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -93, EMVGetStatusApdu.P1, (byte) -113, (byte) -110, (byte) -99, (byte) 56, (byte) -11, PSSSigner.TRAILER_IMPLICIT, (byte) -74, PutData80Apdu.INS, (byte) 33, Tnaf.POW_2_WIDTH, (byte) -1, (byte) -13, PutTemplateApdu.INS, (byte) -51, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, CardSide.BACKGROUND_TAG, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -105, (byte) 68, CardSide.PIN_TEXT_TAG, (byte) -60, (byte) -89, (byte) 126, (byte) 61, (byte) 100, (byte) 93, (byte) 25, (byte) 115, (byte) 96, TLVParser.BYTE_81, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -36, (byte) 34, GenerateACApdu.INS, SetResetParamApdu.CLA, VerifyPINApdu.P2_CIPHERED, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -18, (byte) -72, CardSide.PICTURE_TAG, (byte) -34, (byte) 94, (byte) 11, (byte) -37, (byte) -32, (byte) 50, (byte) 58, (byte) 10, (byte) 73, (byte) 6, PinChangeUnblockApdu.INS, (byte) 92, (byte) -62, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -84, (byte) 98, (byte) -111, (byte) -107, (byte) -28, (byte) 121, (byte) -25, (byte) -56, (byte) 55, (byte) 109, (byte) -115, (byte) -43, (byte) 78, (byte) -87, (byte) 108, (byte) 86, (byte) -12, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 101, (byte) 122, MPPLiteInstruction.INS_GENERATE_AC, (byte) 8, (byte) -70, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 37, (byte) 46, (byte) 28, (byte) -90, (byte) -76, (byte) -58, (byte) -24, (byte) -35, (byte) 116, (byte) 31, (byte) 75, (byte) -67, (byte) -117, (byte) -118, (byte) 112, (byte) 62, (byte) -75, (byte) 102, (byte) 72, (byte) 3, (byte) -10, (byte) 14, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 87, (byte) -71, (byte) -122, ApplicationInfoManager.MS_ONLY, (byte) 29, (byte) -98, (byte) -31, (byte) -8, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CLD.VERSION_TAG, (byte) 105, (byte) -39, (byte) -114, (byte) -108, (byte) -101, (byte) 30, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -23, (byte) -50, (byte) 85, (byte) 40, (byte) -33, (byte) -116, (byte) -95, (byte) -119, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) -26, MCFCITemplate.TAG_FCI_ISSUER_IIN, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 65, (byte) -103, SetResetParamApdu.INS, (byte) 15, (byte) -80, (byte) 84, (byte) -69, CardSide.ALWAYS_TEXT_TAG};
        Si = new byte[]{(byte) 82, (byte) 9, (byte) 106, (byte) -43, (byte) 48, (byte) 54, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) 56, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, EMVGetStatusApdu.P1, (byte) -93, (byte) -98, TLVParser.BYTE_81, (byte) -13, (byte) -41, (byte) -5, (byte) 124, (byte) -29, ApplicationInfoManager.EMV_MS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -101, (byte) 47, (byte) -1, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) 52, (byte) -114, (byte) 67, (byte) 68, (byte) -60, (byte) -34, (byte) -23, (byte) -53, (byte) 84, (byte) 123, (byte) -108, (byte) 50, (byte) -90, (byte) -62, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 61, (byte) -18, (byte) 76, (byte) -107, (byte) 11, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -6, (byte) -61, (byte) 78, (byte) 8, (byte) 46, (byte) -95, (byte) 102, (byte) 40, (byte) -39, PinChangeUnblockApdu.INS, ReadRecordApdu.INS, (byte) 118, (byte) 91, (byte) -94, (byte) 73, (byte) 109, (byte) -117, (byte) -47, (byte) 37, (byte) 114, (byte) -8, (byte) -10, (byte) 100, (byte) -122, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CardSide.ALWAYS_TEXT_TAG, GetTemplateApdu.INS, ISO7816.INS_SELECT, (byte) 92, (byte) -52, (byte) 93, (byte) 101, (byte) -74, (byte) -110, (byte) 108, (byte) 112, (byte) 72, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -3, (byte) -19, (byte) -71, PutData80Apdu.INS, (byte) 94, CardSide.CARD_ELEMENTS_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 87, (byte) -89, (byte) -115, (byte) -99, PinChangeUnblockApdu.CLA, SetResetParamApdu.CLA, (byte) -40, (byte) -85, (byte) 0, (byte) -116, PSSSigner.TRAILER_IMPLICIT, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 10, (byte) -9, (byte) -28, ApplicationInfoManager.TERM_XP1, (byte) 5, (byte) -72, (byte) -77, (byte) 69, (byte) 6, (byte) -48, (byte) 44, (byte) 30, (byte) -113, GetDataApdu.INS, (byte) 63, (byte) 15, (byte) 2, ApplicationInfoManager.MS_ONLY, (byte) -81, (byte) -67, (byte) 3, (byte) 1, CardSide.BACKGROUND_TAG, (byte) -118, (byte) 107, (byte) 58, (byte) -111, CLD.VERSION_TAG, (byte) 65, GetTemplateApdu.TAG_DF_NAME_4F, (byte) 103, (byte) -36, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -105, EMVGetStatusApdu.INS, (byte) -49, (byte) -50, EMVSetStatusApdu.INS, (byte) -76, (byte) -26, (byte) 115, (byte) -106, (byte) -84, (byte) 116, (byte) 34, (byte) -25, (byte) -83, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) -123, (byte) -30, (byte) -7, (byte) 55, (byte) -24, (byte) 28, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -33, (byte) 110, (byte) 71, (byte) -15, (byte) 26, (byte) 113, (byte) 29, (byte) 41, (byte) -59, (byte) -119, MCFCITemplate.TAG_FCI_TEMPLATE, ApplicationInfoManager.EMV_ONLY, (byte) 98, (byte) 14, (byte) -86, CardSide.NO_PIN_TEXT_TAG, (byte) -66, (byte) 27, (byte) -4, (byte) 86, (byte) 62, (byte) 75, (byte) -58, PutTemplateApdu.INS, (byte) 121, VerifyPINApdu.INS, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -37, EMVGetResponse.INS, (byte) -2, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -51, (byte) 90, (byte) -12, (byte) 31, (byte) -35, GetProcessingOptions.INS, ApplicationInfoManager.TERM_XP3, VerifyPINApdu.P2_CIPHERED, (byte) 7, (byte) -57, (byte) 49, (byte) -79, CLD.FORM_FACTOR_TAG, Tnaf.POW_2_WIDTH, (byte) 89, (byte) 39, VerifyPINApdu.P2_PLAINTEXT, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 96, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, Byte.MAX_VALUE, (byte) -87, (byte) 25, (byte) -75, (byte) 74, (byte) 13, SetResetParamApdu.INS, (byte) -27, (byte) 122, (byte) -97, (byte) -109, (byte) -55, (byte) -100, (byte) -17, (byte) -96, (byte) -32, (byte) 59, (byte) 77, MPPLiteInstruction.INS_GENERATE_AC, GenerateACApdu.INS, (byte) -11, (byte) -80, (byte) -56, (byte) -21, (byte) -69, (byte) 60, (byte) -125, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -103, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, CardSide.PIN_TEXT_TAG, (byte) 43, (byte) 4, (byte) 126, (byte) -70, ApplicationInfoManager.TERM_XP2, (byte) -42, (byte) 38, (byte) -31, (byte) 105, CardSide.PICTURE_TAG, (byte) 99, (byte) 85, (byte) 33, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 125};
        rcon = new int[]{1, 2, 4, 8, BLOCK_SIZE, 32, 64, X509KeyUsage.digitalSignature, m3, 54, CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA256, 216, CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384, 77, CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA, 47, 94, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256, 99, 198, CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA, 53, CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, 212, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384, EACTags.SECURE_MESSAGING_TEMPLATE, 250, 239, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA};
    }

    public AESLightEngine() {
        this.WorkingKey = (int[][]) null;
    }

    private static int FFmulX(int i) {
        return ((m2 & i) << 1) ^ (((m1 & i) >>> 7) * m3);
    }

    private void decryptBlock(int[][] iArr) {
        int inv_mcol;
        int inv_mcol2;
        int inv_mcol3;
        int i = this.C0 ^ iArr[this.ROUNDS][0];
        int i2 = this.C1 ^ iArr[this.ROUNDS][1];
        int i3 = this.C2 ^ iArr[this.ROUNDS][2];
        int i4 = this.ROUNDS - 1;
        int i5 = this.C3 ^ iArr[this.ROUNDS][3];
        while (i4 > 1) {
            inv_mcol = inv_mcol((((Si[i & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][0];
            inv_mcol2 = inv_mcol((((Si[i2 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][1];
            inv_mcol3 = inv_mcol((((Si[i3 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][2];
            int i6 = i4 - 1;
            i5 = inv_mcol((((Si[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][3];
            i = inv_mcol((((Si[inv_mcol & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][0];
            i2 = inv_mcol((((Si[inv_mcol2 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][1];
            i3 = iArr[i6][2] ^ inv_mcol((((Si[inv_mcol3 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i5 >> 24) & GF2Field.MASK] << 24));
            i4 = i6 - 1;
            i5 = inv_mcol((((Si[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][3];
        }
        inv_mcol = inv_mcol((((Si[i & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][0];
        inv_mcol2 = inv_mcol((((Si[i2 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][1];
        inv_mcol3 = inv_mcol((((Si[i3 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][2];
        i5 = inv_mcol((((Si[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i >> 24) & GF2Field.MASK] << 24)) ^ iArr[i4][3];
        this.C0 = ((((Si[inv_mcol & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][0];
        this.C1 = ((((Si[inv_mcol2 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][1];
        this.C2 = ((((Si[inv_mcol3 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][2];
        this.C3 = ((((Si[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((Si[(inv_mcol3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((Si[(inv_mcol2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (Si[(inv_mcol >> 24) & GF2Field.MASK] << 24)) ^ iArr[0][3];
    }

    private void encryptBlock(int[][] iArr) {
        int mcol;
        int mcol2;
        int mcol3;
        int i = this.C0 ^ iArr[0][0];
        int i2 = this.C1 ^ iArr[0][1];
        int i3 = iArr[0][2] ^ this.C2;
        int i4 = this.C3 ^ iArr[0][3];
        int i5 = i;
        i = i2;
        i2 = i3;
        i3 = 1;
        while (i3 < this.ROUNDS - 1) {
            mcol = mcol((((f148S[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i4 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][0];
            mcol2 = mcol((((f148S[i & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i4 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][1];
            mcol3 = mcol((((f148S[i2 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i4 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][2];
            int i6 = i3 + 1;
            i4 = mcol((((f148S[i4 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][3];
            i5 = mcol((((f148S[mcol & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i4 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][0];
            i = mcol((((f148S[mcol2 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i4 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][1];
            i2 = iArr[i6][2] ^ mcol((((f148S[mcol3 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i4 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol2 >> 24) & GF2Field.MASK] << 24));
            i3 = i6 + 1;
            i4 = mcol((((f148S[i4 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i6][3];
        }
        mcol = mcol((((f148S[i5 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i4 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][0];
        mcol2 = mcol((((f148S[i & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i4 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i5 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][1];
        mcol3 = mcol((((f148S[i2 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i4 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i5 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][2];
        i2 = i3 + 1;
        i4 = mcol((((f148S[i4 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i5 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i3][3];
        this.C0 = ((((f148S[mcol & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol2 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol3 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(i4 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i2][0];
        this.C1 = ((((f148S[mcol2 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol3 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(i4 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol >> 24) & GF2Field.MASK] << 24)) ^ iArr[i2][1];
        this.C2 = ((((f148S[mcol3 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(i4 >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol2 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i2][2];
        this.C3 = ((((f148S[i4 & GF2Field.MASK] & GF2Field.MASK) ^ ((f148S[(mcol >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) ^ ((f148S[(mcol2 >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) ^ (f148S[(mcol3 >> 24) & GF2Field.MASK] << 24)) ^ iArr[i2][3];
    }

    private int[][] generateWorkingKey(byte[] bArr, boolean z) {
        int length = bArr.length / 4;
        if ((length == 4 || length == 6 || length == 8) && length * 4 == bArr.length) {
            this.ROUNDS = length + 6;
            int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.ROUNDS + 1, 4});
            int i = 0;
            int i2 = 0;
            while (i < bArr.length) {
                iArr[i2 >> 2][i2 & 3] = (((bArr[i] & GF2Field.MASK) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | ((bArr[i + 2] & GF2Field.MASK) << BLOCK_SIZE)) | (bArr[i + 3] << 24);
                i += 4;
                i2++;
            }
            int i3 = (this.ROUNDS + 1) << 2;
            i2 = length;
            while (i2 < i3) {
                i = iArr[(i2 - 1) >> 2][(i2 - 1) & 3];
                if (i2 % length == 0) {
                    i = subWord(shift(i, 8)) ^ rcon[(i2 / length) - 1];
                } else if (length > 6 && i2 % length == 4) {
                    i = subWord(i);
                }
                iArr[i2 >> 2][i2 & 3] = i ^ iArr[(i2 - length) >> 2][(i2 - length) & 3];
                i2++;
            }
            if (!z) {
                for (i = 1; i < this.ROUNDS; i++) {
                    for (i2 = 0; i2 < 4; i2++) {
                        iArr[i][i2] = inv_mcol(iArr[i][i2]);
                    }
                }
            }
            return iArr;
        }
        throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    }

    private static int inv_mcol(int i) {
        int FFmulX = FFmulX(i);
        int FFmulX2 = FFmulX(FFmulX);
        int FFmulX3 = FFmulX(FFmulX2);
        int i2 = i ^ FFmulX3;
        return ((shift(FFmulX ^ i2, 8) ^ (FFmulX3 ^ (FFmulX ^ FFmulX2))) ^ shift(FFmulX2 ^ i2, BLOCK_SIZE)) ^ shift(i2, 24);
    }

    private static int mcol(int i) {
        int FFmulX = FFmulX(i);
        return ((FFmulX ^ shift(i ^ FFmulX, 8)) ^ shift(i, BLOCK_SIZE)) ^ shift(i, 24);
    }

    private void packBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) this.C0;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (this.C0 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C0 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C0 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C1;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C1 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C1 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C1 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C2;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C2 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C2 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C2 >> 24);
        i2 = i3 + 1;
        bArr[i3] = (byte) this.C3;
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C3 >> 8);
        i2 = i3 + 1;
        bArr[i3] = (byte) (this.C3 >> BLOCK_SIZE);
        i3 = i2 + 1;
        bArr[i2] = (byte) (this.C3 >> 24);
    }

    private static int shift(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private static int subWord(int i) {
        return (((f148S[i & GF2Field.MASK] & GF2Field.MASK) | ((f148S[(i >> 8) & GF2Field.MASK] & GF2Field.MASK) << 8)) | ((f148S[(i >> BLOCK_SIZE) & GF2Field.MASK] & GF2Field.MASK) << BLOCK_SIZE)) | (f148S[(i >> 24) & GF2Field.MASK] << 24);
    }

    private void unpackBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        this.C0 = bArr[i] & GF2Field.MASK;
        int i3 = i2 + 1;
        this.C0 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C0;
        int i4 = i3 + 1;
        this.C0 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C0 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C1 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C1 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C1;
        i4 = i3 + 1;
        this.C1 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C1 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C2 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C2 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C2;
        i4 = i3 + 1;
        this.C2 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C2 |= bArr[i4] << 24;
        i2 = i3 + 1;
        this.C3 = bArr[i3] & GF2Field.MASK;
        i3 = i2 + 1;
        this.C3 = ((bArr[i2] & GF2Field.MASK) << 8) | this.C3;
        i4 = i3 + 1;
        this.C3 |= (bArr[i3] & GF2Field.MASK) << BLOCK_SIZE;
        i3 = i4 + 1;
        this.C3 |= bArr[i4] << 24;
    }

    public String getAlgorithmName() {
        return "AES";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = generateWorkingKey(((KeyParameter) cipherParameters).getKey(), z);
            this.forEncryption = z;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.forEncryption) {
                unpackBlock(bArr, i);
                encryptBlock(this.WorkingKey);
                packBlock(bArr2, i2);
            } else {
                unpackBlock(bArr, i);
                decryptBlock(this.WorkingKey);
                packBlock(bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
