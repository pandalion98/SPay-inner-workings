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
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RijndaelEngine implements BlockCipher {
    private static final int MAXKC = 64;
    private static final int MAXROUNDS = 14;
    private static final byte[] f164S;
    private static final byte[] Si;
    private static final byte[] aLogtable;
    private static final byte[] logtable;
    private static final int[] rcon;
    static byte[][] shifts0;
    static byte[][] shifts1;
    private long A0;
    private long A1;
    private long A2;
    private long A3;
    private int BC;
    private long BC_MASK;
    private int ROUNDS;
    private int blockBits;
    private boolean forEncryption;
    private byte[] shifts0SC;
    private byte[] shifts1SC;
    private long[][] workingKey;

    static {
        logtable = new byte[]{(byte) 0, (byte) 0, (byte) 25, (byte) 1, (byte) 50, (byte) 2, (byte) 26, (byte) -58, (byte) 75, (byte) -57, (byte) 27, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, ApplicationInfoManager.TERM_XP3, (byte) -18, (byte) -33, (byte) 3, (byte) 100, (byte) 4, (byte) -32, (byte) 14, (byte) 52, (byte) -115, TLVParser.BYTE_81, (byte) -17, (byte) 76, (byte) 113, (byte) 8, (byte) -56, (byte) -8, (byte) 105, (byte) 28, ApplicationInfoManager.MS_ONLY, (byte) 125, (byte) -62, (byte) 29, (byte) -75, (byte) -7, (byte) -71, (byte) 39, (byte) 106, (byte) 77, (byte) -28, (byte) -90, (byte) 114, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -55, (byte) 9, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 101, (byte) 47, (byte) -118, (byte) 5, (byte) 33, (byte) 15, (byte) -31, PinChangeUnblockApdu.INS, CLD.FORM_FACTOR_TAG, EMVSetStatusApdu.INS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) 69, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) -109, PutData80Apdu.INS, (byte) -114, (byte) -106, (byte) -113, (byte) -37, (byte) -67, (byte) 54, (byte) -48, (byte) -50, (byte) -108, CardSide.BACKGROUND_TAG, (byte) 92, PutTemplateApdu.INS, (byte) -15, EMVGetStatusApdu.P1, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -125, (byte) 56, (byte) 102, (byte) -35, (byte) -3, (byte) 48, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) 6, (byte) -117, (byte) 98, (byte) -77, (byte) 37, (byte) -30, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) 34, VerifyPINApdu.P2_CIPHERED, (byte) -111, Tnaf.POW_2_WIDTH, (byte) 126, (byte) 110, (byte) 72, (byte) -61, (byte) -93, (byte) -74, (byte) 30, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 58, (byte) 107, (byte) 40, (byte) 84, (byte) -6, (byte) -123, (byte) 61, (byte) -70, (byte) 43, (byte) 121, (byte) 10, CardSide.CARD_ELEMENTS_TAG, (byte) -101, (byte) -97, (byte) 94, GetDataApdu.INS, (byte) 78, GetTemplateApdu.INS, (byte) -84, (byte) -27, (byte) -13, (byte) 115, (byte) -89, (byte) 87, (byte) -81, ApplicationInfoManager.TERM_XP1, GetProcessingOptions.INS, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -12, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -42, (byte) 116, GetTemplateApdu.TAG_DF_NAME_4F, MPPLiteInstruction.INS_GENERATE_AC, (byte) -23, (byte) -43, (byte) -25, (byte) -26, (byte) -83, (byte) -24, (byte) 44, (byte) -41, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 122, (byte) -21, CardSide.ALWAYS_TEXT_TAG, (byte) 11, (byte) -11, (byte) 89, (byte) -53, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -80, (byte) -100, (byte) -87, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -96, Byte.MAX_VALUE, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) -10, MCFCITemplate.TAG_FCI_TEMPLATE, CardSide.PIN_TEXT_TAG, (byte) -60, (byte) 73, (byte) -20, (byte) -40, (byte) 67, (byte) 31, SetResetParamApdu.INS, ISO7816.INS_SELECT, (byte) 118, (byte) 123, ApplicationInfoManager.EMV_ONLY, (byte) -52, (byte) -69, (byte) 62, (byte) 90, (byte) -5, (byte) 96, (byte) -79, (byte) -122, (byte) 59, (byte) 82, (byte) -95, (byte) 108, (byte) -86, (byte) 85, (byte) 41, (byte) -99, (byte) -105, ReadRecordApdu.INS, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, SetResetParamApdu.CLA, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) -66, (byte) -36, (byte) -4, PSSSigner.TRAILER_IMPLICIT, (byte) -107, (byte) -49, (byte) -51, (byte) 55, (byte) 63, (byte) 91, (byte) -47, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, ApplicationInfoManager.EMV_MS, PinChangeUnblockApdu.CLA, (byte) 60, (byte) 65, (byte) -94, (byte) 109, (byte) 71, CardSide.PICTURE_TAG, GenerateACApdu.INS, (byte) -98, (byte) 93, (byte) 86, EMVGetStatusApdu.INS, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -85, (byte) 68, CLD.VERSION_TAG, (byte) -110, (byte) -39, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, VerifyPINApdu.INS, (byte) 46, (byte) -119, (byte) -76, (byte) 124, (byte) -72, (byte) 38, ApplicationInfoManager.TERM_XP2, (byte) -103, (byte) -29, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) 103, (byte) 74, (byte) -19, (byte) -34, (byte) -59, (byte) 49, (byte) -2, CardSide.NO_PIN_TEXT_TAG, (byte) 13, (byte) 99, (byte) -116, VerifyPINApdu.P2_PLAINTEXT, EMVGetResponse.INS, (byte) -9, (byte) 112, (byte) 7};
        aLogtable = new byte[]{(byte) 0, (byte) 3, (byte) 5, (byte) 15, CLD.VERSION_TAG, ApplicationInfoManager.TERM_XP3, (byte) 85, (byte) -1, (byte) 26, (byte) 46, (byte) 114, (byte) -106, (byte) -95, (byte) -8, CardSide.BACKGROUND_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -31, (byte) 56, (byte) 72, (byte) -40, (byte) 115, (byte) -107, ISO7816.INS_SELECT, (byte) -9, (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102, (byte) -86, (byte) -27, (byte) 52, (byte) 92, (byte) -28, (byte) 55, (byte) 89, (byte) -21, (byte) 38, (byte) 106, (byte) -66, (byte) -39, (byte) 112, SetResetParamApdu.CLA, (byte) -85, (byte) -26, (byte) 49, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -11, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, CardSide.PICTURE_TAG, (byte) 60, (byte) 68, (byte) -52, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -47, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) -72, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 110, ReadRecordApdu.INS, (byte) -51, (byte) 76, GetTemplateApdu.INS, (byte) 103, (byte) -87, (byte) -32, (byte) 59, (byte) 77, (byte) -41, (byte) 98, (byte) -90, (byte) -15, (byte) 8, CardSide.NO_PIN_TEXT_TAG, (byte) 40, ApplicationInfoManager.TERMINAL_MODE_NONE, VerifyPINApdu.P2_CIPHERED, (byte) -125, (byte) -98, (byte) -71, (byte) -48, (byte) 107, (byte) -67, (byte) -36, Byte.MAX_VALUE, TLVParser.BYTE_81, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) -77, (byte) -50, (byte) 73, (byte) -37, (byte) 118, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -75, (byte) -60, (byte) 87, (byte) -7, Tnaf.POW_2_WIDTH, (byte) 48, GetTemplateApdu.TAG_APPLICATION_LABEL_50, EMVSetStatusApdu.INS, (byte) 11, (byte) 29, (byte) 39, (byte) 105, (byte) -69, (byte) -42, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) -93, (byte) -2, (byte) 25, (byte) 43, (byte) 125, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -110, (byte) -83, (byte) -20, (byte) 47, (byte) 113, (byte) -109, MPPLiteInstruction.INS_GENERATE_AC, (byte) -23, VerifyPINApdu.INS, (byte) 96, (byte) -96, (byte) -5, CardSide.ALWAYS_TEXT_TAG, (byte) 58, (byte) 78, PutTemplateApdu.INS, (byte) 109, ApplicationInfoManager.EMV_ONLY, (byte) -62, (byte) 93, (byte) -25, (byte) 50, (byte) 86, (byte) -6, CardSide.CARD_ELEMENTS_TAG, (byte) 63, (byte) 65, (byte) -61, (byte) 94, (byte) -30, (byte) 61, (byte) 71, (byte) -55, EMVGetStatusApdu.P1, EMVGetResponse.INS, (byte) 91, (byte) -19, (byte) 44, (byte) 116, (byte) -100, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, PutData80Apdu.INS, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -97, (byte) -70, (byte) -43, (byte) 100, (byte) -84, (byte) -17, GenerateACApdu.INS, (byte) 126, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -99, PSSSigner.TRAILER_IMPLICIT, (byte) -33, (byte) 122, (byte) -114, (byte) -119, VerifyPINApdu.P2_PLAINTEXT, (byte) -101, (byte) -74, ApplicationInfoManager.MS_ONLY, ApplicationInfoManager.TERM_XP1, (byte) -24, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 101, (byte) -81, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 37, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -79, (byte) -56, (byte) 67, (byte) -59, (byte) 84, (byte) -4, (byte) 31, (byte) 33, (byte) 99, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -12, (byte) 7, (byte) 9, (byte) 27, SetResetParamApdu.INS, ApplicationInfoManager.TERM_XP2, (byte) -103, (byte) -80, (byte) -53, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, GetDataApdu.INS, (byte) 69, (byte) -49, (byte) 74, (byte) -34, (byte) 121, (byte) -117, (byte) -122, (byte) -111, GetProcessingOptions.INS, (byte) -29, (byte) 62, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -58, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -13, (byte) 14, CLD.FORM_FACTOR_TAG, (byte) 54, (byte) 90, (byte) -18, (byte) 41, (byte) 123, (byte) -115, (byte) -116, (byte) -113, (byte) -118, (byte) -123, (byte) -108, (byte) -89, EMVGetStatusApdu.INS, (byte) 13, CardSide.PIN_TEXT_TAG, ApplicationInfoManager.EMV_MS, (byte) 75, (byte) -35, (byte) 124, PinChangeUnblockApdu.CLA, (byte) -105, (byte) -94, (byte) -3, (byte) 28, PinChangeUnblockApdu.INS, (byte) 108, (byte) -76, (byte) -57, (byte) 82, (byte) -10, (byte) 1, (byte) 3, (byte) 5, (byte) 15, CLD.VERSION_TAG, ApplicationInfoManager.TERM_XP3, (byte) 85, (byte) -1, (byte) 26, (byte) 46, (byte) 114, (byte) -106, (byte) -95, (byte) -8, CardSide.BACKGROUND_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -31, (byte) 56, (byte) 72, (byte) -40, (byte) 115, (byte) -107, ISO7816.INS_SELECT, (byte) -9, (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102, (byte) -86, (byte) -27, (byte) 52, (byte) 92, (byte) -28, (byte) 55, (byte) 89, (byte) -21, (byte) 38, (byte) 106, (byte) -66, (byte) -39, (byte) 112, SetResetParamApdu.CLA, (byte) -85, (byte) -26, (byte) 49, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -11, (byte) 4, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, CardSide.PICTURE_TAG, (byte) 60, (byte) 68, (byte) -52, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -47, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) -72, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 110, ReadRecordApdu.INS, (byte) -51, (byte) 76, GetTemplateApdu.INS, (byte) 103, (byte) -87, (byte) -32, (byte) 59, (byte) 77, (byte) -41, (byte) 98, (byte) -90, (byte) -15, (byte) 8, CardSide.NO_PIN_TEXT_TAG, (byte) 40, ApplicationInfoManager.TERMINAL_MODE_NONE, VerifyPINApdu.P2_CIPHERED, (byte) -125, (byte) -98, (byte) -71, (byte) -48, (byte) 107, (byte) -67, (byte) -36, Byte.MAX_VALUE, TLVParser.BYTE_81, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) -77, (byte) -50, (byte) 73, (byte) -37, (byte) 118, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -75, (byte) -60, (byte) 87, (byte) -7, Tnaf.POW_2_WIDTH, (byte) 48, GetTemplateApdu.TAG_APPLICATION_LABEL_50, EMVSetStatusApdu.INS, (byte) 11, (byte) 29, (byte) 39, (byte) 105, (byte) -69, (byte) -42, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) -93, (byte) -2, (byte) 25, (byte) 43, (byte) 125, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -110, (byte) -83, (byte) -20, (byte) 47, (byte) 113, (byte) -109, MPPLiteInstruction.INS_GENERATE_AC, (byte) -23, VerifyPINApdu.INS, (byte) 96, (byte) -96, (byte) -5, CardSide.ALWAYS_TEXT_TAG, (byte) 58, (byte) 78, PutTemplateApdu.INS, (byte) 109, ApplicationInfoManager.EMV_ONLY, (byte) -62, (byte) 93, (byte) -25, (byte) 50, (byte) 86, (byte) -6, CardSide.CARD_ELEMENTS_TAG, (byte) 63, (byte) 65, (byte) -61, (byte) 94, (byte) -30, (byte) 61, (byte) 71, (byte) -55, EMVGetStatusApdu.P1, EMVGetResponse.INS, (byte) 91, (byte) -19, (byte) 44, (byte) 116, (byte) -100, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, PutData80Apdu.INS, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -97, (byte) -70, (byte) -43, (byte) 100, (byte) -84, (byte) -17, GenerateACApdu.INS, (byte) 126, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -99, PSSSigner.TRAILER_IMPLICIT, (byte) -33, (byte) 122, (byte) -114, (byte) -119, VerifyPINApdu.P2_PLAINTEXT, (byte) -101, (byte) -74, ApplicationInfoManager.MS_ONLY, ApplicationInfoManager.TERM_XP1, (byte) -24, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 101, (byte) -81, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 37, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -79, (byte) -56, (byte) 67, (byte) -59, (byte) 84, (byte) -4, (byte) 31, (byte) 33, (byte) 99, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -12, (byte) 7, (byte) 9, (byte) 27, SetResetParamApdu.INS, ApplicationInfoManager.TERM_XP2, (byte) -103, (byte) -80, (byte) -53, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, GetDataApdu.INS, (byte) 69, (byte) -49, (byte) 74, (byte) -34, (byte) 121, (byte) -117, (byte) -122, (byte) -111, GetProcessingOptions.INS, (byte) -29, (byte) 62, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -58, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -13, (byte) 14, CLD.FORM_FACTOR_TAG, (byte) 54, (byte) 90, (byte) -18, (byte) 41, (byte) 123, (byte) -115, (byte) -116, (byte) -113, (byte) -118, (byte) -123, (byte) -108, (byte) -89, EMVGetStatusApdu.INS, (byte) 13, CardSide.PIN_TEXT_TAG, ApplicationInfoManager.EMV_MS, (byte) 75, (byte) -35, (byte) 124, PinChangeUnblockApdu.CLA, (byte) -105, (byte) -94, (byte) -3, (byte) 28, PinChangeUnblockApdu.INS, (byte) 108, (byte) -76, (byte) -57, (byte) 82, (byte) -10, (byte) 1};
        f164S = new byte[]{(byte) 99, (byte) 124, ApplicationInfoManager.TERM_XP2, (byte) 123, EMVGetStatusApdu.INS, (byte) 107, MCFCITemplate.TAG_FCI_TEMPLATE, (byte) -59, (byte) 48, (byte) 1, (byte) 103, (byte) 43, (byte) -2, (byte) -41, (byte) -85, (byte) 118, GetDataApdu.INS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -55, (byte) 125, (byte) -6, (byte) 89, (byte) 71, EMVSetStatusApdu.INS, (byte) -83, GetTemplateApdu.INS, (byte) -94, (byte) -81, (byte) -100, ISO7816.INS_SELECT, (byte) 114, EMVGetResponse.INS, ApplicationInfoManager.EMV_ONLY, (byte) -3, (byte) -109, (byte) 38, (byte) 54, (byte) 63, (byte) -9, (byte) -52, (byte) 52, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -27, (byte) -15, (byte) 113, (byte) -40, (byte) 49, CardSide.CARD_ELEMENTS_TAG, (byte) 4, (byte) -57, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -61, CardSide.NO_PIN_TEXT_TAG, (byte) -106, (byte) 5, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 7, CLD.FORM_FACTOR_TAG, VerifyPINApdu.P2_PLAINTEXT, (byte) -30, (byte) -21, (byte) 39, ReadRecordApdu.INS, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 9, (byte) -125, (byte) 44, (byte) 26, (byte) 27, (byte) 110, (byte) 90, (byte) -96, (byte) 82, (byte) 59, (byte) -42, (byte) -77, (byte) 41, (byte) -29, (byte) 47, PinChangeUnblockApdu.CLA, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -47, (byte) 0, (byte) -19, VerifyPINApdu.INS, (byte) -4, (byte) -79, (byte) 91, (byte) 106, (byte) -53, (byte) -66, ApplicationInfoManager.EMV_MS, (byte) 74, (byte) 76, ApplicationInfoManager.TERM_XP1, (byte) -49, (byte) -48, (byte) -17, (byte) -86, (byte) -5, (byte) 67, (byte) 77, ApplicationInfoManager.TERM_XP3, (byte) -123, (byte) 69, (byte) -7, (byte) 2, Byte.MAX_VALUE, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 60, (byte) -97, GetProcessingOptions.INS, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -93, EMVGetStatusApdu.P1, (byte) -113, (byte) -110, (byte) -99, (byte) 56, (byte) -11, PSSSigner.TRAILER_IMPLICIT, (byte) -74, PutData80Apdu.INS, (byte) 33, Tnaf.POW_2_WIDTH, (byte) -1, (byte) -13, PutTemplateApdu.INS, (byte) -51, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, CardSide.BACKGROUND_TAG, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -105, (byte) 68, CardSide.PIN_TEXT_TAG, (byte) -60, (byte) -89, (byte) 126, (byte) 61, (byte) 100, (byte) 93, (byte) 25, (byte) 115, (byte) 96, TLVParser.BYTE_81, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -36, (byte) 34, GenerateACApdu.INS, SetResetParamApdu.CLA, VerifyPINApdu.P2_CIPHERED, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -18, (byte) -72, CardSide.PICTURE_TAG, (byte) -34, (byte) 94, (byte) 11, (byte) -37, (byte) -32, (byte) 50, (byte) 58, (byte) 10, (byte) 73, (byte) 6, PinChangeUnblockApdu.INS, (byte) 92, (byte) -62, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -84, (byte) 98, (byte) -111, (byte) -107, (byte) -28, (byte) 121, (byte) -25, (byte) -56, (byte) 55, (byte) 109, (byte) -115, (byte) -43, (byte) 78, (byte) -87, (byte) 108, (byte) 86, (byte) -12, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 101, (byte) 122, MPPLiteInstruction.INS_GENERATE_AC, (byte) 8, (byte) -70, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) 37, (byte) 46, (byte) 28, (byte) -90, (byte) -76, (byte) -58, (byte) -24, (byte) -35, (byte) 116, (byte) 31, (byte) 75, (byte) -67, (byte) -117, (byte) -118, (byte) 112, (byte) 62, (byte) -75, (byte) 102, (byte) 72, (byte) 3, (byte) -10, (byte) 14, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 87, (byte) -71, (byte) -122, ApplicationInfoManager.MS_ONLY, (byte) 29, (byte) -98, (byte) -31, (byte) -8, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CLD.VERSION_TAG, (byte) 105, (byte) -39, (byte) -114, (byte) -108, (byte) -101, (byte) 30, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -23, (byte) -50, (byte) 85, (byte) 40, (byte) -33, (byte) -116, (byte) -95, (byte) -119, (byte) 13, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) -26, MCFCITemplate.TAG_FCI_ISSUER_IIN, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 65, (byte) -103, SetResetParamApdu.INS, (byte) 15, (byte) -80, (byte) 84, (byte) -69, CardSide.ALWAYS_TEXT_TAG};
        Si = new byte[]{(byte) 82, (byte) 9, (byte) 106, (byte) -43, (byte) 48, (byte) 54, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) 56, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, EMVGetStatusApdu.P1, (byte) -93, (byte) -98, TLVParser.BYTE_81, (byte) -13, (byte) -41, (byte) -5, (byte) 124, (byte) -29, ApplicationInfoManager.EMV_MS, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) -101, (byte) 47, (byte) -1, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) 52, (byte) -114, (byte) 67, (byte) 68, (byte) -60, (byte) -34, (byte) -23, (byte) -53, (byte) 84, (byte) 123, (byte) -108, (byte) 50, (byte) -90, (byte) -62, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) 61, (byte) -18, (byte) 76, (byte) -107, (byte) 11, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) -6, (byte) -61, (byte) 78, (byte) 8, (byte) 46, (byte) -95, (byte) 102, (byte) 40, (byte) -39, PinChangeUnblockApdu.INS, ReadRecordApdu.INS, (byte) 118, (byte) 91, (byte) -94, (byte) 73, (byte) 109, (byte) -117, (byte) -47, (byte) 37, (byte) 114, (byte) -8, (byte) -10, (byte) 100, (byte) -122, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, ApplicationInfoManager.MOB_CVM_TYP_MPVV, CardSide.ALWAYS_TEXT_TAG, GetTemplateApdu.INS, ISO7816.INS_SELECT, (byte) 92, (byte) -52, (byte) 93, (byte) 101, (byte) -74, (byte) -110, (byte) 108, (byte) 112, (byte) 72, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -3, (byte) -19, (byte) -71, PutData80Apdu.INS, (byte) 94, CardSide.CARD_ELEMENTS_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 87, (byte) -89, (byte) -115, (byte) -99, PinChangeUnblockApdu.CLA, SetResetParamApdu.CLA, (byte) -40, (byte) -85, (byte) 0, (byte) -116, PSSSigner.TRAILER_IMPLICIT, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 10, (byte) -9, (byte) -28, ApplicationInfoManager.TERM_XP1, (byte) 5, (byte) -72, (byte) -77, (byte) 69, (byte) 6, (byte) -48, (byte) 44, (byte) 30, (byte) -113, GetDataApdu.INS, (byte) 63, (byte) 15, (byte) 2, ApplicationInfoManager.MS_ONLY, (byte) -81, (byte) -67, (byte) 3, (byte) 1, CardSide.BACKGROUND_TAG, (byte) -118, (byte) 107, (byte) 58, (byte) -111, CLD.VERSION_TAG, (byte) 65, GetTemplateApdu.TAG_DF_NAME_4F, (byte) 103, (byte) -36, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -105, EMVGetStatusApdu.INS, (byte) -49, (byte) -50, EMVSetStatusApdu.INS, (byte) -76, (byte) -26, (byte) 115, (byte) -106, (byte) -84, (byte) 116, (byte) 34, (byte) -25, (byte) -83, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) -123, (byte) -30, (byte) -7, (byte) 55, (byte) -24, (byte) 28, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) -33, (byte) 110, (byte) 71, (byte) -15, (byte) 26, (byte) 113, (byte) 29, (byte) 41, (byte) -59, (byte) -119, MCFCITemplate.TAG_FCI_TEMPLATE, ApplicationInfoManager.EMV_ONLY, (byte) 98, (byte) 14, (byte) -86, CardSide.NO_PIN_TEXT_TAG, (byte) -66, (byte) 27, (byte) -4, (byte) 86, (byte) 62, (byte) 75, (byte) -58, PutTemplateApdu.INS, (byte) 121, VerifyPINApdu.INS, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) -37, EMVGetResponse.INS, (byte) -2, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -51, (byte) 90, (byte) -12, (byte) 31, (byte) -35, GetProcessingOptions.INS, ApplicationInfoManager.TERM_XP3, VerifyPINApdu.P2_CIPHERED, (byte) 7, (byte) -57, (byte) 49, (byte) -79, CLD.FORM_FACTOR_TAG, Tnaf.POW_2_WIDTH, (byte) 89, (byte) 39, VerifyPINApdu.P2_PLAINTEXT, (byte) -20, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 96, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, Byte.MAX_VALUE, (byte) -87, (byte) 25, (byte) -75, (byte) 74, (byte) 13, SetResetParamApdu.INS, (byte) -27, (byte) 122, (byte) -97, (byte) -109, (byte) -55, (byte) -100, (byte) -17, (byte) -96, (byte) -32, (byte) 59, (byte) 77, MPPLiteInstruction.INS_GENERATE_AC, GenerateACApdu.INS, (byte) -11, (byte) -80, (byte) -56, (byte) -21, (byte) -69, (byte) 60, (byte) -125, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -103, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, CardSide.PIN_TEXT_TAG, (byte) 43, (byte) 4, (byte) 126, (byte) -70, ApplicationInfoManager.TERM_XP2, (byte) -42, (byte) 38, (byte) -31, (byte) 105, CardSide.PICTURE_TAG, (byte) 99, (byte) 85, (byte) 33, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) 125};
        rcon = new int[]{1, 2, 4, 8, 16, 32, MAXKC, X509KeyUsage.digitalSignature, 27, 54, CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA256, 216, CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384, 77, CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA, 47, 94, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256, 99, 198, CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA, 53, CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, 212, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384, EACTags.SECURE_MESSAGING_TEMPLATE, 250, 239, CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256, CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA};
        shifts0 = new byte[][]{new byte[]{(byte) 0, (byte) 8, Tnaf.POW_2_WIDTH, CardSide.NO_PIN_TEXT_TAG}, new byte[]{(byte) 0, (byte) 8, Tnaf.POW_2_WIDTH, CardSide.NO_PIN_TEXT_TAG}, new byte[]{(byte) 0, (byte) 8, Tnaf.POW_2_WIDTH, CardSide.NO_PIN_TEXT_TAG}, new byte[]{(byte) 0, (byte) 8, Tnaf.POW_2_WIDTH, VerifyPINApdu.INS}, new byte[]{(byte) 0, (byte) 8, CardSide.NO_PIN_TEXT_TAG, VerifyPINApdu.INS}};
        shifts1 = new byte[][]{new byte[]{(byte) 0, CardSide.NO_PIN_TEXT_TAG, Tnaf.POW_2_WIDTH, (byte) 8}, new byte[]{(byte) 0, VerifyPINApdu.INS, CardSide.NO_PIN_TEXT_TAG, Tnaf.POW_2_WIDTH}, new byte[]{(byte) 0, (byte) 40, VerifyPINApdu.INS, CardSide.NO_PIN_TEXT_TAG}, new byte[]{(byte) 0, (byte) 48, (byte) 40, CardSide.NO_PIN_TEXT_TAG}, new byte[]{(byte) 0, (byte) 56, (byte) 40, VerifyPINApdu.INS}};
    }

    public RijndaelEngine() {
        this(X509KeyUsage.digitalSignature);
    }

    public RijndaelEngine(int i) {
        switch (i) {
            case X509KeyUsage.digitalSignature /*128*/:
                this.BC = 32;
                this.BC_MASK = 4294967295L;
                this.shifts0SC = shifts0[0];
                this.shifts1SC = shifts1[0];
                break;
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
                this.BC = 40;
                this.BC_MASK = 1099511627775L;
                this.shifts0SC = shifts0[1];
                this.shifts1SC = shifts1[1];
                break;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
                this.BC = 48;
                this.BC_MASK = 281474976710655L;
                this.shifts0SC = shifts0[2];
                this.shifts1SC = shifts1[2];
                break;
            case 224:
                this.BC = 56;
                this.BC_MASK = 72057594037927935L;
                this.shifts0SC = shifts0[3];
                this.shifts1SC = shifts1[3];
                break;
            case SkeinMac.SKEIN_256 /*256*/:
                this.BC = MAXKC;
                this.BC_MASK = -1;
                this.shifts0SC = shifts0[4];
                this.shifts1SC = shifts1[4];
                break;
            default:
                throw new IllegalArgumentException("unknown blocksize to Rijndael");
        }
        this.blockBits = i;
    }

    private void InvMixColumn() {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (int i = 0; i < this.BC; i += 8) {
            int i2 = (int) ((this.A0 >> i) & 255);
            int i3 = (int) ((this.A1 >> i) & 255);
            int i4 = (int) ((this.A2 >> i) & 255);
            int i5 = (int) ((this.A3 >> i) & 255);
            int i6 = i2 != 0 ? logtable[i2 & GF2Field.MASK] & GF2Field.MASK : -1;
            int i7 = i3 != 0 ? logtable[i3 & GF2Field.MASK] & GF2Field.MASK : -1;
            i3 = i4 != 0 ? logtable[i4 & GF2Field.MASK] & GF2Field.MASK : -1;
            i2 = i5 != 0 ? logtable[i5 & GF2Field.MASK] & GF2Field.MASK : -1;
            j4 |= ((long) ((((mul0xe(i6) ^ mul0xb(i7)) ^ mul0xd(i3)) ^ mul0x9(i2)) & GF2Field.MASK)) << i;
            j3 |= ((long) ((((mul0xe(i7) ^ mul0xb(i3)) ^ mul0xd(i2)) ^ mul0x9(i6)) & GF2Field.MASK)) << i;
            j2 |= ((long) ((((mul0xe(i3) ^ mul0xb(i2)) ^ mul0xd(i6)) ^ mul0x9(i7)) & GF2Field.MASK)) << i;
            j |= ((long) ((((mul0xe(i2) ^ mul0xb(i6)) ^ mul0xd(i7)) ^ mul0x9(i3)) & GF2Field.MASK)) << i;
        }
        this.A0 = j4;
        this.A1 = j3;
        this.A2 = j2;
        this.A3 = j;
    }

    private void KeyAddition(long[] jArr) {
        this.A0 ^= jArr[0];
        this.A1 ^= jArr[1];
        this.A2 ^= jArr[2];
        this.A3 ^= jArr[3];
    }

    private void MixColumn() {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (int i = 0; i < this.BC; i += 8) {
            int i2 = (int) ((this.A0 >> i) & 255);
            int i3 = (int) ((this.A1 >> i) & 255);
            int i4 = (int) ((this.A2 >> i) & 255);
            int i5 = (int) ((this.A3 >> i) & 255);
            j4 |= ((long) ((((mul0x2(i2) ^ mul0x3(i3)) ^ i4) ^ i5) & GF2Field.MASK)) << i;
            j3 |= ((long) ((((mul0x2(i3) ^ mul0x3(i4)) ^ i5) ^ i2) & GF2Field.MASK)) << i;
            j2 |= ((long) ((((mul0x2(i4) ^ mul0x3(i5)) ^ i2) ^ i3) & GF2Field.MASK)) << i;
            j |= ((long) ((((mul0x3(i2) ^ mul0x2(i5)) ^ i3) ^ i4) & GF2Field.MASK)) << i;
        }
        this.A0 = j4;
        this.A1 = j3;
        this.A2 = j2;
        this.A3 = j;
    }

    private void ShiftRow(byte[] bArr) {
        this.A1 = shift(this.A1, bArr[1]);
        this.A2 = shift(this.A2, bArr[2]);
        this.A3 = shift(this.A3, bArr[3]);
    }

    private void Substitution(byte[] bArr) {
        this.A0 = applyS(this.A0, bArr);
        this.A1 = applyS(this.A1, bArr);
        this.A2 = applyS(this.A2, bArr);
        this.A3 = applyS(this.A3, bArr);
    }

    private long applyS(long j, byte[] bArr) {
        long j2 = 0;
        for (int i = 0; i < this.BC; i += 8) {
            j2 |= ((long) (bArr[(int) ((j >> i) & 255)] & GF2Field.MASK)) << i;
        }
        return j2;
    }

    private void decryptBlock(long[][] jArr) {
        KeyAddition(jArr[this.ROUNDS]);
        Substitution(Si);
        ShiftRow(this.shifts1SC);
        for (int i = this.ROUNDS - 1; i > 0; i--) {
            KeyAddition(jArr[i]);
            InvMixColumn();
            Substitution(Si);
            ShiftRow(this.shifts1SC);
        }
        KeyAddition(jArr[0]);
    }

    private void encryptBlock(long[][] jArr) {
        KeyAddition(jArr[0]);
        for (int i = 1; i < this.ROUNDS; i++) {
            Substitution(f164S);
            ShiftRow(this.shifts0SC);
            MixColumn();
            KeyAddition(jArr[i]);
        }
        Substitution(f164S);
        ShiftRow(this.shifts0SC);
        KeyAddition(jArr[this.ROUNDS]);
    }

    private long[][] generateWorkingKey(byte[] bArr) {
        int i;
        int i2;
        int length = bArr.length * 8;
        byte[][] bArr2 = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, MAXKC});
        long[][] jArr = (long[][]) Array.newInstance(Long.TYPE, new int[]{15, 4});
        switch (length) {
            case X509KeyUsage.digitalSignature /*128*/:
                i = 4;
                break;
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
                i = 5;
                break;
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
                i = 6;
                break;
            case 224:
                i = 7;
                break;
            case SkeinMac.SKEIN_256 /*256*/:
                i = 8;
                break;
            default:
                throw new IllegalArgumentException("Key length not 128/160/192/224/256 bits.");
        }
        if (length >= this.blockBits) {
            this.ROUNDS = i + 6;
        } else {
            this.ROUNDS = (this.BC / 8) + 6;
        }
        int i3 = 0;
        length = 0;
        while (length < bArr.length) {
            i2 = i3 + 1;
            bArr2[length % 4][length / 4] = bArr[i3];
            length++;
            i3 = i2;
        }
        length = 0;
        for (i2 = 0; i2 < i && length < (this.ROUNDS + 1) * (this.BC / 8); i2++) {
            for (i3 = 0; i3 < 4; i3++) {
                long[] jArr2 = jArr[length / (this.BC / 8)];
                jArr2[i3] = jArr2[i3] | (((long) (bArr2[i3][i2] & GF2Field.MASK)) << ((length * 8) % this.BC));
            }
            length++;
        }
        i2 = length;
        length = 0;
        while (i2 < (this.ROUNDS + 1) * (this.BC / 8)) {
            byte[] bArr3;
            int i4;
            for (i3 = 0; i3 < 4; i3++) {
                bArr3 = bArr2[i3];
                bArr3[0] = (byte) (bArr3[0] ^ f164S[bArr2[(i3 + 1) % 4][i - 1] & GF2Field.MASK]);
            }
            bArr3 = bArr2[0];
            i3 = length + 1;
            bArr3[0] = (byte) (rcon[length] ^ bArr3[0]);
            byte[] bArr4;
            if (i <= 6) {
                for (i4 = 1; i4 < i; i4++) {
                    for (length = 0; length < 4; length++) {
                        bArr4 = bArr2[length];
                        bArr4[i4] = (byte) (bArr4[i4] ^ bArr2[length][i4 - 1]);
                    }
                }
            } else {
                for (i4 = 1; i4 < 4; i4++) {
                    for (length = 0; length < 4; length++) {
                        bArr4 = bArr2[length];
                        bArr4[i4] = (byte) (bArr4[i4] ^ bArr2[length][i4 - 1]);
                    }
                }
                for (length = 0; length < 4; length++) {
                    bArr3 = bArr2[length];
                    bArr3[4] = (byte) (bArr3[4] ^ f164S[bArr2[length][3] & GF2Field.MASK]);
                }
                for (i4 = 5; i4 < i; i4++) {
                    for (length = 0; length < 4; length++) {
                        bArr4 = bArr2[length];
                        bArr4[i4] = (byte) (bArr4[i4] ^ bArr2[length][i4 - 1]);
                    }
                }
            }
            length = i2;
            for (i4 = 0; i4 < i && length < (this.ROUNDS + 1) * (this.BC / 8); i4++) {
                for (i2 = 0; i2 < 4; i2++) {
                    jArr2 = jArr[length / (this.BC / 8)];
                    jArr2[i2] = jArr2[i2] | (((long) (bArr2[i2][i4] & GF2Field.MASK)) << ((length * 8) % this.BC));
                }
                length++;
            }
            i2 = length;
            length = i3;
        }
        return jArr;
    }

    private byte mul0x2(int i) {
        return i != 0 ? aLogtable[(logtable[i] & GF2Field.MASK) + 25] : (byte) 0;
    }

    private byte mul0x3(int i) {
        return i != 0 ? aLogtable[(logtable[i] & GF2Field.MASK) + 1] : (byte) 0;
    }

    private byte mul0x9(int i) {
        return i >= 0 ? aLogtable[i + 199] : (byte) 0;
    }

    private byte mul0xb(int i) {
        return i >= 0 ? aLogtable[i + CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256] : (byte) 0;
    }

    private byte mul0xd(int i) {
        return i >= 0 ? aLogtable[i + 238] : (byte) 0;
    }

    private byte mul0xe(int i) {
        return i >= 0 ? aLogtable[i + 223] : (byte) 0;
    }

    private void packBlock(byte[] bArr, int i) {
        for (int i2 = 0; i2 != this.BC; i2 += 8) {
            int i3 = i + 1;
            bArr[i] = (byte) ((int) (this.A0 >> i2));
            int i4 = i3 + 1;
            bArr[i3] = (byte) ((int) (this.A1 >> i2));
            i3 = i4 + 1;
            bArr[i4] = (byte) ((int) (this.A2 >> i2));
            i = i3 + 1;
            bArr[i3] = (byte) ((int) (this.A3 >> i2));
        }
    }

    private long shift(long j, int i) {
        return ((j >>> i) | (j << (this.BC - i))) & this.BC_MASK;
    }

    private void unpackBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        this.A0 = (long) (bArr[i] & GF2Field.MASK);
        int i3 = i2 + 1;
        this.A1 = (long) (bArr[i2] & GF2Field.MASK);
        i2 = i3 + 1;
        this.A2 = (long) (bArr[i3] & GF2Field.MASK);
        i3 = i2 + 1;
        this.A3 = (long) (bArr[i2] & GF2Field.MASK);
        for (i2 = 8; i2 != this.BC; i2 += 8) {
            int i4 = i3 + 1;
            this.A0 |= ((long) (bArr[i3] & GF2Field.MASK)) << i2;
            i3 = i4 + 1;
            this.A1 |= ((long) (bArr[i4] & GF2Field.MASK)) << i2;
            i4 = i3 + 1;
            this.A2 |= ((long) (bArr[i3] & GF2Field.MASK)) << i2;
            i3 = i4 + 1;
            this.A3 |= ((long) (bArr[i4] & GF2Field.MASK)) << i2;
        }
    }

    public String getAlgorithmName() {
        return "Rijndael";
    }

    public int getBlockSize() {
        return this.BC / 2;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = generateWorkingKey(((KeyParameter) cipherParameters).getKey());
            this.forEncryption = z;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Rijndael init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Rijndael engine not initialised");
        } else if ((this.BC / 2) + i > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if ((this.BC / 2) + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.forEncryption) {
                unpackBlock(bArr, i);
                encryptBlock(this.workingKey);
                packBlock(bArr2, i2);
            } else {
                unpackBlock(bArr, i);
                decryptBlock(this.workingKey);
                packBlock(bArr2, i2);
            }
            return this.BC / 2;
        }
    }

    public void reset() {
    }
}
