package com.samsung.android.visasdk.paywave;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.ISO7816;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.VerifyingEntity;
import com.samsung.android.visasdk.facade.data.VerifyingType;

public class Constant {
    public static final byte[] DA;
    public static final byte[] DB;
    public static final byte[] DD;
    public static final byte[] DE;
    public static final byte[] DF;
    public static final byte[] DG;
    public static final byte[] DH;
    public static final byte[] DI;
    public static final byte[] DJ;
    public static final CvmMode DK;
    public static final byte[] DM;
    public static final byte[] DN;
    public static final byte[] DO;
    public static final byte[] Dn;
    public static final byte[] Do;
    public static String Dp;
    public static final byte[] Dq;
    public static final byte[] Dr;
    public static final byte[] Ds;
    public static final byte[] Dt;
    public static final byte[] Du;
    public static final byte[] Dv;
    public static final byte[] Dw;
    public static final byte[] Dx;
    public static final byte[] Dy;
    public static final byte[] Dz;
    public static final byte[] iO;

    public enum State {
        READY,
        SELECT_PPSE,
        SELECT_AID,
        GPO,
        READ_RECORD,
        GET_DATA,
        UPDATE_PARAMS
    }

    public enum TransactionType {
        UNKNOWN_TYPE,
        MSD,
        QVSDC_NO_ODA,
        QVSDC_WITH_ODA_1,
        QVSDC_WITH_ODA_2,
        QVSDC_WITH_ODA_3,
        MST,
        INAPP,
        COMPLETED
    }

    static {
        Dn = new byte[]{(byte) 49, (byte) 56, (byte) 48, ApplicationInfoManager.EMV_MS, (byte) 49, (byte) 52, VerifyPINApdu.INS, (byte) 86, (byte) 67, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 67, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, VerifyPINApdu.INS, (byte) 49, (byte) 46, ApplicationInfoManager.TERM_XP3, (byte) 46, (byte) 48};
        Do = new byte[]{(byte) 1, (byte) 0};
        Dp = "4761739001010010";
        Dq = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1};
        Dr = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        iO = new byte[]{(byte) 0, ISO7816.INS_SELECT, (byte) 4, (byte) 0, (byte) 14, (byte) 50, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 65, (byte) 89, (byte) 46, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 89, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 46, (byte) 68, (byte) 68, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 48, (byte) 49, (byte) 0};
        Ds = new byte[]{(byte) 50, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 65, (byte) 89, (byte) 46, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 89, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 46, (byte) 68, (byte) 68, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 48, (byte) 49};
        Dt = new byte[]{(byte) -97, (byte) 102, (byte) 4, (byte) -97, (byte) 2, (byte) 6, (byte) -97, (byte) 3, (byte) 6, (byte) -97, (byte) 26, (byte) 2, (byte) -107, (byte) 5, MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, GenerateACApdu.INS, (byte) 2, ApplicationInfoManager.MOB_CVM_PERFORMED, (byte) 3, (byte) -100, (byte) 1, (byte) -97, (byte) 55, (byte) 4};
        Du = new byte[]{SetResetParamApdu.CLA, (byte) 0};
        Dv = new byte[]{(byte) 103, (byte) 0};
        Dw = new byte[]{(byte) 105, EMVSetStatusApdu.RESET_LOWEST_PRIORITY};
        Dx = new byte[]{(byte) 98, (byte) -125};
        Dy = new byte[]{(byte) 105, PinChangeUnblockApdu.CLA};
        Dz = new byte[]{(byte) 105, (byte) -123};
        DA = new byte[]{(byte) 105, (byte) -122};
        DB = new byte[]{(byte) 106, VerifyPINApdu.P2_PLAINTEXT};
        DD = new byte[]{(byte) 106, TLVParser.BYTE_81};
        DE = new byte[]{(byte) 106, EMVSetStatusApdu.RESET_LOWEST_PRIORITY};
        DF = new byte[]{(byte) 106, (byte) -125};
        DG = new byte[]{(byte) 106, (byte) -122};
        DH = new byte[]{(byte) 109, (byte) 0};
        DI = new byte[]{(byte) 110, (byte) 0};
        DJ = new byte[]{MCFCITemplate.TAG_FCI_TEMPLATE, (byte) 0};
        DK = new CvmMode(VerifyingEntity.MOBILE_APP, VerifyingType.PASSCODE);
        DM = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        DN = new byte[]{(byte) 8, (byte) 38};
        DO = new byte[]{(byte) 8, EMVGetStatusApdu.P1};
    }
}
