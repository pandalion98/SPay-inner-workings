package org.bouncycastle.crypto.engines;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
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
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RC2Engine implements BlockCipher {
    private static final int BLOCK_SIZE = 8;
    private static byte[] piTable;
    private boolean encrypting;
    private int[] workingKey;

    static {
        piTable = new byte[]{(byte) -39, ApplicationInfoManager.TERMINAL_MODE_NONE, (byte) -7, (byte) -60, (byte) 25, (byte) -35, (byte) -75, (byte) -19, (byte) 40, (byte) -23, (byte) -3, (byte) 121, (byte) 74, (byte) -96, (byte) -40, (byte) -99, (byte) -58, (byte) 126, (byte) 55, (byte) -125, (byte) 43, (byte) 118, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) -114, (byte) 98, (byte) 76, (byte) 100, VerifyPINApdu.P2_CIPHERED, (byte) 68, (byte) -117, (byte) -5, (byte) -94, CardSide.PIN_TEXT_TAG, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 89, (byte) -11, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, (byte) -77, GetTemplateApdu.TAG_DF_NAME_4F, CardSide.BACKGROUND_TAG, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 69, (byte) 109, (byte) -115, (byte) 9, TLVParser.BYTE_81, (byte) 125, (byte) 50, (byte) -67, (byte) -113, EMVGetStatusApdu.P1, (byte) -21, (byte) -122, ApplicationInfoManager.EMV_ONLY, (byte) 123, (byte) 11, EMVSetStatusApdu.INS, (byte) -107, (byte) 33, (byte) 34, (byte) 92, (byte) 107, (byte) 78, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) 84, (byte) -42, (byte) 101, (byte) -109, (byte) -50, (byte) 96, ReadRecordApdu.INS, (byte) 28, (byte) 115, (byte) 86, EMVGetResponse.INS, CardSide.PICTURE_TAG, (byte) -89, (byte) -116, (byte) -15, (byte) -36, CLD.FORM_FACTOR_TAG, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, GetDataApdu.INS, (byte) 31, (byte) 59, (byte) -66, (byte) -28, (byte) -47, MCFCITemplate.TAG_FCI_ISSUER_IIN, (byte) 61, GetTemplateApdu.INS, (byte) 48, (byte) -93, (byte) 60, (byte) -74, (byte) 38, MCFCITemplate.TAG_FCI_TEMPLATE, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) 14, PutData80Apdu.INS, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 105, (byte) 7, (byte) 87, (byte) 39, EMVGetStatusApdu.INS, (byte) 29, (byte) -101, PSSSigner.TRAILER_IMPLICIT, (byte) -108, (byte) 67, (byte) 3, (byte) -8, CLD.VERSION_TAG, (byte) -57, (byte) -10, SetResetParamApdu.CLA, (byte) -17, (byte) 62, (byte) -25, (byte) 6, (byte) -61, (byte) -43, (byte) 47, (byte) -56, (byte) 102, (byte) 30, (byte) -41, (byte) 8, (byte) -24, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) -34, VerifyPINApdu.P2_PLAINTEXT, (byte) 82, (byte) -18, (byte) -9, PinChangeUnblockApdu.CLA, (byte) -86, (byte) 114, (byte) -84, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 77, (byte) 106, GenerateACApdu.INS, (byte) -106, (byte) 26, PutTemplateApdu.INS, (byte) 113, (byte) 90, CardSide.CARD_ELEMENTS_TAG, (byte) 73, (byte) 116, (byte) 75, (byte) -97, (byte) -48, (byte) 94, (byte) 4, CardSide.NO_PIN_TEXT_TAG, ISO7816.INS_SELECT, (byte) -20, (byte) -62, (byte) -32, (byte) 65, (byte) 110, (byte) 15, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -53, (byte) -52, PinChangeUnblockApdu.INS, (byte) -111, (byte) -81, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -95, (byte) -12, (byte) 112, ApplicationInfoManager.EMV_MS, (byte) -103, (byte) 124, (byte) 58, (byte) -123, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -72, (byte) -76, (byte) 122, (byte) -4, (byte) 2, (byte) 54, (byte) 91, (byte) 37, (byte) 85, (byte) -105, (byte) 49, SetResetParamApdu.INS, (byte) 93, (byte) -6, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) -29, (byte) -118, (byte) -110, MPPLiteInstruction.INS_GENERATE_AC, (byte) 5, (byte) -33, (byte) 41, Tnaf.POW_2_WIDTH, (byte) 103, (byte) 108, (byte) -70, (byte) -55, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) 0, (byte) -26, (byte) -49, (byte) -31, (byte) -98, GetProcessingOptions.INS, (byte) 44, (byte) 99, CardSide.ALWAYS_TEXT_TAG, (byte) 1, (byte) 63, ApplicationInfoManager.TERM_XP1, (byte) -30, (byte) -119, (byte) -87, (byte) 13, (byte) 56, (byte) 52, (byte) 27, (byte) -85, ApplicationInfoManager.TERM_XP3, (byte) -1, (byte) -80, (byte) -69, (byte) 72, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) -71, (byte) -79, (byte) -51, (byte) 46, (byte) -59, (byte) -13, (byte) -37, (byte) 71, (byte) -27, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -100, ApplicationInfoManager.TERM_XP2, (byte) 10, (byte) -90, VerifyPINApdu.INS, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) -2, Byte.MAX_VALUE, ApplicationInfoManager.MS_ONLY, (byte) -83};
    }

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3;
        int i4 = ((bArr[i + 7] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 6] & GF2Field.MASK);
        int i5 = ((bArr[i + 5] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 4] & GF2Field.MASK);
        int i6 = ((bArr[i + 3] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 2] & GF2Field.MASK);
        int i7 = (bArr[i + 0] & GF2Field.MASK) + ((bArr[i + 1] & GF2Field.MASK) << BLOCK_SIZE);
        for (i3 = 60; i3 >= 44; i3 -= 4) {
            i4 = rotateWordLeft(i4, 11) - ((((i5 ^ -1) & i7) + (i6 & i5)) + this.workingKey[i3 + 3]);
            i5 = rotateWordLeft(i5, 13) - ((((i6 ^ -1) & i4) + (i7 & i6)) + this.workingKey[i3 + 2]);
            i6 = rotateWordLeft(i6, 14) - ((((i7 ^ -1) & i5) + (i4 & i7)) + this.workingKey[i3 + 1]);
            i7 = rotateWordLeft(i7, 15) - ((((i4 ^ -1) & i6) + (i5 & i4)) + this.workingKey[i3]);
        }
        i4 -= this.workingKey[i5 & 63];
        i5 -= this.workingKey[i6 & 63];
        i6 -= this.workingKey[i7 & 63];
        i7 -= this.workingKey[i4 & 63];
        for (i3 = 40; i3 >= 20; i3 -= 4) {
            i4 = rotateWordLeft(i4, 11) - ((((i5 ^ -1) & i7) + (i6 & i5)) + this.workingKey[i3 + 3]);
            i5 = rotateWordLeft(i5, 13) - ((((i6 ^ -1) & i4) + (i7 & i6)) + this.workingKey[i3 + 2]);
            i6 = rotateWordLeft(i6, 14) - ((((i7 ^ -1) & i5) + (i4 & i7)) + this.workingKey[i3 + 1]);
            i7 = rotateWordLeft(i7, 15) - ((((i4 ^ -1) & i6) + (i5 & i4)) + this.workingKey[i3]);
        }
        i4 -= this.workingKey[i5 & 63];
        i5 -= this.workingKey[i6 & 63];
        i6 -= this.workingKey[i7 & 63];
        i7 -= this.workingKey[i4 & 63];
        for (i3 = 16; i3 >= 0; i3 -= 4) {
            i4 = rotateWordLeft(i4, 11) - ((((i5 ^ -1) & i7) + (i6 & i5)) + this.workingKey[i3 + 3]);
            i5 = rotateWordLeft(i5, 13) - ((((i6 ^ -1) & i4) + (i7 & i6)) + this.workingKey[i3 + 2]);
            i6 = rotateWordLeft(i6, 14) - ((((i7 ^ -1) & i5) + (i4 & i7)) + this.workingKey[i3 + 1]);
            i7 = rotateWordLeft(i7, 15) - ((((i4 ^ -1) & i6) + (i5 & i4)) + this.workingKey[i3]);
        }
        bArr2[i2 + 0] = (byte) i7;
        bArr2[i2 + 1] = (byte) (i7 >> BLOCK_SIZE);
        bArr2[i2 + 2] = (byte) i6;
        bArr2[i2 + 3] = (byte) (i6 >> BLOCK_SIZE);
        bArr2[i2 + 4] = (byte) i5;
        bArr2[i2 + 5] = (byte) (i5 >> BLOCK_SIZE);
        bArr2[i2 + 6] = (byte) i4;
        bArr2[i2 + 7] = (byte) (i4 >> BLOCK_SIZE);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3;
        int i4 = ((bArr[i + 7] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 6] & GF2Field.MASK);
        int i5 = ((bArr[i + 5] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 4] & GF2Field.MASK);
        int i6 = ((bArr[i + 3] & GF2Field.MASK) << BLOCK_SIZE) + (bArr[i + 2] & GF2Field.MASK);
        int i7 = (bArr[i + 0] & GF2Field.MASK) + ((bArr[i + 1] & GF2Field.MASK) << BLOCK_SIZE);
        for (i3 = 0; i3 <= 16; i3 += 4) {
            i7 = rotateWordLeft(((i7 + ((i4 ^ -1) & i6)) + (i5 & i4)) + this.workingKey[i3], 1);
            i6 = rotateWordLeft(((i6 + ((i7 ^ -1) & i5)) + (i4 & i7)) + this.workingKey[i3 + 1], 2);
            i5 = rotateWordLeft(((i5 + ((i6 ^ -1) & i4)) + (i7 & i6)) + this.workingKey[i3 + 2], 3);
            i4 = rotateWordLeft(((i4 + ((i5 ^ -1) & i7)) + (i6 & i5)) + this.workingKey[i3 + 3], 5);
        }
        i7 += this.workingKey[i4 & 63];
        i6 += this.workingKey[i7 & 63];
        i5 += this.workingKey[i6 & 63];
        i4 += this.workingKey[i5 & 63];
        for (i3 = 20; i3 <= 40; i3 += 4) {
            i7 = rotateWordLeft(((i7 + ((i4 ^ -1) & i6)) + (i5 & i4)) + this.workingKey[i3], 1);
            i6 = rotateWordLeft(((i6 + ((i7 ^ -1) & i5)) + (i4 & i7)) + this.workingKey[i3 + 1], 2);
            i5 = rotateWordLeft(((i5 + ((i6 ^ -1) & i4)) + (i7 & i6)) + this.workingKey[i3 + 2], 3);
            i4 = rotateWordLeft(((i4 + ((i5 ^ -1) & i7)) + (i6 & i5)) + this.workingKey[i3 + 3], 5);
        }
        i7 += this.workingKey[i4 & 63];
        i6 += this.workingKey[i7 & 63];
        i5 += this.workingKey[i6 & 63];
        i4 += this.workingKey[i5 & 63];
        for (i3 = 44; i3 < 64; i3 += 4) {
            i7 = rotateWordLeft(((i7 + ((i4 ^ -1) & i6)) + (i5 & i4)) + this.workingKey[i3], 1);
            i6 = rotateWordLeft(((i6 + ((i7 ^ -1) & i5)) + (i4 & i7)) + this.workingKey[i3 + 1], 2);
            i5 = rotateWordLeft(((i5 + ((i6 ^ -1) & i4)) + (i7 & i6)) + this.workingKey[i3 + 2], 3);
            i4 = rotateWordLeft(((i4 + ((i5 ^ -1) & i7)) + (i6 & i5)) + this.workingKey[i3 + 3], 5);
        }
        bArr2[i2 + 0] = (byte) i7;
        bArr2[i2 + 1] = (byte) (i7 >> BLOCK_SIZE);
        bArr2[i2 + 2] = (byte) i6;
        bArr2[i2 + 3] = (byte) (i6 >> BLOCK_SIZE);
        bArr2[i2 + 4] = (byte) i5;
        bArr2[i2 + 5] = (byte) (i5 >> BLOCK_SIZE);
        bArr2[i2 + 6] = (byte) i4;
        bArr2[i2 + 7] = (byte) (i4 >> BLOCK_SIZE);
    }

    private int[] generateWorkingKey(byte[] bArr, int i) {
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        int[] iArr = new int[X509KeyUsage.digitalSignature];
        for (i2 = 0; i2 != bArr.length; i2++) {
            iArr[i2] = bArr[i2] & GF2Field.MASK;
        }
        i2 = bArr.length;
        if (i2 < X509KeyUsage.digitalSignature) {
            i3 = i2;
            int i6 = iArr[i2 - 1];
            i2 = 0;
            while (true) {
                i4 = i2 + 1;
                i6 = piTable[(iArr[i2] + i6) & GF2Field.MASK] & GF2Field.MASK;
                i2 = i3 + 1;
                iArr[i3] = i6;
                if (i2 >= X509KeyUsage.digitalSignature) {
                    break;
                }
                i3 = i2;
                i2 = i4;
            }
        }
        i3 = (i + 7) >> 3;
        i4 = piTable[iArr[128 - i3] & (GF2Field.MASK >> ((-i) & 7))] & GF2Field.MASK;
        iArr[128 - i3] = i4;
        for (i2 = (128 - i3) - 1; i2 >= 0; i2--) {
            i4 = piTable[i4 ^ iArr[i2 + i3]] & GF2Field.MASK;
            iArr[i2] = i4;
        }
        int[] iArr2 = new int[64];
        while (i5 != iArr2.length) {
            iArr2[i5] = iArr[i5 * 2] + (iArr[(i5 * 2) + 1] << BLOCK_SIZE);
            i5++;
        }
        return iArr2;
    }

    private int rotateWordLeft(int i, int i2) {
        int i3 = HCEClientConstants.HIGHEST_ATC_DEC_VALUE & i;
        return (i3 >> (16 - i2)) | (i3 << i2);
    }

    public String getAlgorithmName() {
        return "RC2";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.encrypting = z;
        if (cipherParameters instanceof RC2Parameters) {
            RC2Parameters rC2Parameters = (RC2Parameters) cipherParameters;
            this.workingKey = generateWorkingKey(rC2Parameters.getKey(), rC2Parameters.getEffectiveKeyBits());
        } else if (cipherParameters instanceof KeyParameter) {
            byte[] key = ((KeyParameter) cipherParameters).getKey();
            this.workingKey = generateWorkingKey(key, key.length * BLOCK_SIZE);
        } else {
            throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + cipherParameters.getClass().getName());
        }
    }

    public final int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.encrypting) {
                encryptBlock(bArr, i, bArr2, i2);
            } else {
                decryptBlock(bArr, i, bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
