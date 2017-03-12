package org.bouncycastle.crypto.digests;

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
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Memoable;

public class MD2Digest implements ExtendedDigest, Memoable {
    private static final int DIGEST_LENGTH = 16;
    private static final byte[] f119S;
    private byte[] f120C;
    private int COff;
    private byte[] f121M;
    private byte[] f122X;
    private int mOff;
    private int xOff;

    static {
        f119S = new byte[]{(byte) 41, (byte) 46, (byte) 67, (byte) -55, (byte) -94, (byte) -40, (byte) 124, (byte) 1, (byte) 61, (byte) 54, (byte) 84, (byte) -95, (byte) -20, EMVSetStatusApdu.INS, (byte) 6, CardSide.BACKGROUND_TAG, (byte) 98, (byte) -89, (byte) 5, (byte) -13, EMVGetResponse.INS, (byte) -57, (byte) 115, (byte) -116, ApplicationInfoManager.MOB_CVM_TYP_MPVV, (byte) -109, (byte) 43, (byte) -39, PSSSigner.TRAILER_IMPLICIT, (byte) 76, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, GetDataApdu.INS, (byte) 30, (byte) -101, (byte) 87, (byte) 60, (byte) -3, GetTemplateApdu.INS, (byte) -32, CardSide.ALWAYS_TEXT_TAG, (byte) 103, MCFCITemplate.TAG_FCI_ISSUER_IIN, MCFCITemplate.TAG_FCI_TEMPLATE, CardSide.NO_PIN_TEXT_TAG, (byte) -118, CardSide.PIN_TEXT_TAG, (byte) -27, CLD.FORM_FACTOR_TAG, (byte) -66, (byte) 78, (byte) -60, (byte) -42, PutData80Apdu.INS, (byte) -98, (byte) -34, (byte) 73, (byte) -96, (byte) -5, (byte) -11, (byte) -114, (byte) -69, (byte) 47, (byte) -18, (byte) 122, (byte) -87, ApplicationInfoManager.MOB_CVM_TYP_UNSPECIFIED, (byte) 121, (byte) -111, CardSide.CARD_ELEMENTS_TAG, ReadRecordApdu.INS, (byte) 7, (byte) 63, (byte) -108, (byte) -62, Tnaf.POW_2_WIDTH, (byte) -119, (byte) 11, (byte) 34, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 33, VerifyPINApdu.P2_PLAINTEXT, Byte.MAX_VALUE, (byte) 93, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 90, SetResetParamApdu.CLA, (byte) 50, (byte) 39, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 62, (byte) -52, (byte) -25, PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, (byte) -9, (byte) -105, (byte) 3, (byte) -1, (byte) 25, (byte) 48, (byte) -77, (byte) 72, PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG, (byte) -75, (byte) -47, (byte) -41, (byte) 94, (byte) -110, GenerateACApdu.INS, (byte) -84, (byte) 86, (byte) -86, (byte) -58, GetTemplateApdu.TAG_DF_NAME_4F, (byte) -72, (byte) 56, PutTemplateApdu.INS, (byte) -106, ISO7816.INS_SELECT, (byte) 125, (byte) -74, (byte) 118, (byte) -4, (byte) 107, (byte) -30, (byte) -100, (byte) 116, (byte) 4, (byte) -15, (byte) 69, (byte) -99, (byte) 112, (byte) 89, (byte) 100, (byte) 113, GetTemplateApdu.TAG_PRIORITY_INDICATOR_87, VerifyPINApdu.INS, (byte) -122, (byte) 91, (byte) -49, (byte) 101, (byte) -26, SetResetParamApdu.INS, GetProcessingOptions.INS, (byte) 2, (byte) 27, (byte) 96, (byte) 37, (byte) -83, MPPLiteInstruction.INS_GENERATE_AC, (byte) -80, (byte) -71, (byte) -10, (byte) 28, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 105, (byte) 52, EMVGetStatusApdu.P1, (byte) 126, (byte) 15, (byte) 85, (byte) 71, (byte) -93, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -35, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD, (byte) -81, (byte) 58, (byte) -61, (byte) 92, (byte) -7, (byte) -50, (byte) -70, (byte) -59, MPPLiteInstruction.INS_RELAY_RESISTANCE, (byte) 38, (byte) 44, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 13, (byte) 110, (byte) -123, (byte) 40, PinChangeUnblockApdu.CLA, (byte) 9, ApplicationInfoManager.AMOUNT_STATUS_LOW, (byte) -33, (byte) -51, (byte) -12, (byte) 65, TLVParser.BYTE_81, (byte) 77, (byte) 82, (byte) 106, (byte) -36, (byte) 55, (byte) -56, (byte) 108, ApplicationInfoManager.MS_ONLY, (byte) -85, (byte) -6, PinChangeUnblockApdu.INS, (byte) -31, (byte) 123, (byte) 8, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG, (byte) -67, (byte) -79, (byte) 74, ApplicationInfoManager.TERMINAL_MODE_NONE, VerifyPINApdu.P2_CIPHERED, (byte) -107, (byte) -117, (byte) -29, (byte) 99, (byte) -24, (byte) 109, (byte) -23, (byte) -53, (byte) -43, (byte) -2, (byte) 59, (byte) 0, (byte) 29, ApplicationInfoManager.EMV_MS, EMVGetStatusApdu.INS, (byte) -17, ApplicationInfoManager.EMV_ONLY, (byte) 14, (byte) 102, ApplicationInfoManager.TERM_XP1, (byte) -48, (byte) -28, (byte) -90, ApplicationInfoManager.TERM_XP2, (byte) 114, (byte) -8, (byte) -21, ApplicationInfoManager.MOB_CVM_NOT_PERFORMED, (byte) 75, (byte) 10, (byte) 49, (byte) 68, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) -76, (byte) -113, (byte) -19, (byte) 31, (byte) 26, (byte) -37, (byte) -103, (byte) -115, ApplicationInfoManager.TERM_XP3, (byte) -97, CLD.VERSION_TAG, (byte) -125, CardSide.PICTURE_TAG};
    }

    public MD2Digest() {
        this.f122X = new byte[48];
        this.f121M = new byte[DIGEST_LENGTH];
        this.f120C = new byte[DIGEST_LENGTH];
        reset();
    }

    public MD2Digest(MD2Digest mD2Digest) {
        this.f122X = new byte[48];
        this.f121M = new byte[DIGEST_LENGTH];
        this.f120C = new byte[DIGEST_LENGTH];
        copyIn(mD2Digest);
    }

    private void copyIn(MD2Digest mD2Digest) {
        System.arraycopy(mD2Digest.f122X, 0, this.f122X, 0, mD2Digest.f122X.length);
        this.xOff = mD2Digest.xOff;
        System.arraycopy(mD2Digest.f121M, 0, this.f121M, 0, mD2Digest.f121M.length);
        this.mOff = mD2Digest.mOff;
        System.arraycopy(mD2Digest.f120C, 0, this.f120C, 0, mD2Digest.f120C.length);
        this.COff = mD2Digest.COff;
    }

    public Memoable copy() {
        return new MD2Digest(this);
    }

    public int doFinal(byte[] bArr, int i) {
        byte length = (byte) (this.f121M.length - this.mOff);
        for (int i2 = this.mOff; i2 < this.f121M.length; i2++) {
            this.f121M[i2] = length;
        }
        processCheckSum(this.f121M);
        processBlock(this.f121M);
        processBlock(this.f120C);
        System.arraycopy(this.f122X, this.xOff, bArr, i, DIGEST_LENGTH);
        reset();
        return DIGEST_LENGTH;
    }

    public String getAlgorithmName() {
        return "MD2";
    }

    public int getByteLength() {
        return DIGEST_LENGTH;
    }

    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    protected void processBlock(byte[] bArr) {
        int i;
        for (i = 0; i < DIGEST_LENGTH; i++) {
            this.f122X[i + DIGEST_LENGTH] = bArr[i];
            this.f122X[i + 32] = (byte) (bArr[i] ^ this.f122X[i]);
        }
        int i2 = 0;
        i = 0;
        while (i2 < 18) {
            int i3 = i;
            for (i = 0; i < 48; i++) {
                byte[] bArr2 = this.f122X;
                byte b = (byte) (f119S[i3] ^ bArr2[i]);
                bArr2[i] = b;
                i3 = b & GF2Field.MASK;
            }
            i3 = (i3 + i2) % SkeinMac.SKEIN_256;
            i2++;
            i = i3;
        }
    }

    protected void processCheckSum(byte[] bArr) {
        int i = this.f120C[15];
        for (int i2 = 0; i2 < DIGEST_LENGTH; i2++) {
            byte[] bArr2 = this.f120C;
            bArr2[i2] = (byte) (f119S[(i ^ bArr[i2]) & GF2Field.MASK] ^ bArr2[i2]);
            i = this.f120C[i2];
        }
    }

    public void reset() {
        int i;
        this.xOff = 0;
        for (i = 0; i != this.f122X.length; i++) {
            this.f122X[i] = (byte) 0;
        }
        this.mOff = 0;
        for (i = 0; i != this.f121M.length; i++) {
            this.f121M[i] = (byte) 0;
        }
        this.COff = 0;
        for (i = 0; i != this.f120C.length; i++) {
            this.f120C[i] = (byte) 0;
        }
    }

    public void reset(Memoable memoable) {
        copyIn((MD2Digest) memoable);
    }

    public void update(byte b) {
        byte[] bArr = this.f121M;
        int i = this.mOff;
        this.mOff = i + 1;
        bArr[i] = b;
        if (this.mOff == DIGEST_LENGTH) {
            processCheckSum(this.f121M);
            processBlock(this.f121M);
            this.mOff = 0;
        }
    }

    public void update(byte[] bArr, int i, int i2) {
        while (this.mOff != 0 && i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
        int i3 = i2;
        int i4 = i;
        while (i3 > DIGEST_LENGTH) {
            System.arraycopy(bArr, i4, this.f121M, 0, DIGEST_LENGTH);
            processCheckSum(this.f121M);
            processBlock(this.f121M);
            i3 -= 16;
            i4 += DIGEST_LENGTH;
        }
        while (i3 > 0) {
            update(bArr[i4]);
            i4++;
            i3--;
        }
    }
}
