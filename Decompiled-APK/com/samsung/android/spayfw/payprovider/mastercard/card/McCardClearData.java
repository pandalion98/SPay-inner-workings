package com.samsung.android.spayfw.payprovider.mastercard.card;

import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.CardRiskManagementData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_BL;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil;
import com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil.Mctags;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class McCardClearData {
    private static final int A002_ADDITIONAL_CHECK_TABLE_LEN = 18;
    private static final int A002_ADDITIONAL_CHECK_TABLE_OFFSET = 78;
    private static final int A002_CDOL_DATA_LEN = 1;
    private static final int A002_CDOL_DATA_OFFSET = 96;
    private static final int A002_CIAC_DECLINE_ARQC_LEN = 3;
    private static final int A002_CIAC_DECLINE_ARQC_MANAGEMENT_OFFSET = 170;
    private static final int A002_CIAC_DECLINE_ARQC_OFFSET = 173;
    private static final int A002_CRM_COUNTRY_CODE_LEN = 2;
    private static final int A002_CRM_COUNTRY_CODE_OFFSET = 101;
    private static final int A002_CVM_RESET_TIMEOUT_LEN = 2;
    private static final int A002_CVM_RESET_TIMEOUT_OFFSET = 197;
    private static final int A002_DUALTAP_RESET_TIMEOUT_LEN = 2;
    private static final int A002_DUALTAP_RESET_TIMEOUT_OFFSET = 201;
    private static final int A002_KEYDERIVATION_INDEX_LEN = 1;
    private static final int A002_KEYDERIVATION_INDEX_OFFSET = 194;
    private static final int A003_CIAC_DECLINE_PPMS_LEN = 2;
    private static final int A003_CIAC_DECLINE_PPMS_OFFSET = 2;
    private static final int A404_CARD_ISSUER_APPLICATION_CODE_LEN = 3;
    private static final int A404_CIAC_ARQC_ALTERNATE_CL_OFFSET = 16;
    private static final int A504_ALT_KEY_DERIVATION_INDEX_LENGTH = 1;
    private static final int A601_CV_RESULT_MASK_LEN = 6;
    private static final int A602_CVR_MASK_LEN = 6;
    private static final int A604_LEN = 6;
    private static final int B005_AIP_LEN = 2;
    private static final int B009_APPLICATION_INTERCHANGE_PROFILE = 2;
    private static final int B100_RAPDU_TIME_LEN = 2;
    private static final byte CVN_POSITION = (byte) 1;
    private static final byte CVN_RP_VALUE = (byte) 20;
    private static final byte CVN_VALUE = (byte) 21;
    private static final byte CVR_MASK_VALUE = (byte) -1;
    private static final int GPO_CONST = 6;
    private static final byte[] GPO_MASK;
    private static final int GPO_OFFSET_AFL = 7;
    private static final int GPO_OFFSET_AFL_CONST = 1;
    private static final int GPO_OFFSET_AIP = 4;
    private static final byte IAD_FF = (byte) -1;
    private static final byte IAD_FF_POSITION = (byte) 17;
    private static final int IAD_LENGTH = 26;
    private static final String PRIMARY_AID = "A0000000041010";
    private static final String TAG = "McCardClearData";
    private static final byte[] TAG_EXPIRATION_DATE;
    private static final byte TAG_EXPIRATION_DATE_LEN = (byte) 3;
    private static final byte TAG_PAN = (byte) 90;
    private static final int TAG_PAN_LEN_MAX = 10;
    private static final byte[] TAG_PAN_SN;
    private static final byte TAG_PAN_SN_LEN = (byte) 1;
    private static final ByteArrayFactory baf;
    private static HashMap<String, AlternateProfileData> mAltProfileMap;
    private final int B007_REMOTE_PAYMENT_AIP_LEN;
    private List<RecordDGI> RecordDGIArr;
    private byte[] a404CardIssuerActionCodeARQC;
    private byte a502RemotePaymentKeyDerivationIndex;
    private byte[] a504AltKeyDerivationIndex;
    private byte[] a602RemotePaymentCVRMask;
    private byte[] a604CardVerificationResultsMask;
    private byte[] addionalCheckTable;
    private byte[] aflB005;
    private byte[] aipB005;
    private byte[] b004AlternateAid;
    private byte[] b007RemotePaymentAip;
    private byte[] b009ApplicationFileLocator;
    private byte[] b009ApplicationInterchangeProfile;
    private byte[] c400Profiles;
    private byte[] cdolData;
    private byte[] ciacDeclineArqc;
    private byte[] ciacDeclineArqcManagement;
    private byte[] ciacDeclinePPMS;
    private byte[] contactLessCvmResetTimeout;
    private byte[] contactLessDualTapResetTime;
    private byte[] contactLessKeyDerivationIndex;
    private byte[] crmCountryCode;
    private byte[] cvResultMaskA601;
    private byte[] dgiB021_ppse_fci_Data;
    private byte[] fciAlternateAIDA104;
    private byte[] fciShortAID;
    private byte[] iccKey;
    private byte[] icc_key_a_array;
    private byte[] icc_key_dp_array;
    private byte[] icc_key_dq_array;
    private byte[] icc_key_p_array;
    private byte[] icc_key_q_array;
    private DPANData mDpanData;
    private byte[] mPar;
    private Map<ClearDataDGI, byte[]> mUnusedDgiElementsMap;
    private byte[] magstripeCvmIssuerOptions;
    private byte[] maxTimeRRAPDU;
    private byte[] minTimeRRAPDU;
    private byte[] txTimeRRAPDU;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData.1 */
    static /* synthetic */ class C05581 {
        static final /* synthetic */ int[] f10x236d376b;

        static {
            f10x236d376b = new int[ClearDataDGI.values().length];
            try {
                f10x236d376b[ClearDataDGI.DGI8201.ordinal()] = McCardClearData.GPO_OFFSET_AFL_CONST;
            } catch (NoSuchFieldError e) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGI8202.ordinal()] = McCardClearData.B100_RAPDU_TIME_LEN;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGI8203.ordinal()] = McCardClearData.A404_CARD_ISSUER_APPLICATION_CODE_LEN;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGI8204.ordinal()] = McCardClearData.GPO_OFFSET_AIP;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGI8205.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA002.ordinal()] = McCardClearData.GPO_CONST;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA003.ordinal()] = McCardClearData.GPO_OFFSET_AFL;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA102.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA104.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA404.ordinal()] = McCardClearData.TAG_PAN_LEN_MAX;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA502.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA601.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA602.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA604.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB004.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB005.ordinal()] = McCardClearData.A404_CIAC_ARQC_ALTERNATE_CL_OFFSET;
            } catch (NoSuchFieldError e16) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB007.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB009.ordinal()] = McCardClearData.A002_ADDITIONAL_CHECK_TABLE_LEN;
            } catch (NoSuchFieldError e18) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB021.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIB100.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIC400.ordinal()] = 21;
            } catch (NoSuchFieldError e21) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIRECODRS.ordinal()] = 22;
            } catch (NoSuchFieldError e22) {
            }
            try {
                f10x236d376b[ClearDataDGI.DGIA504.ordinal()] = 23;
            } catch (NoSuchFieldError e23) {
            }
        }
    }

    private enum AlternateProfileData {
        US_MAESTRO_AID_ALT_PROFILE("A0000000042203", "4445424954", Constants.SERVICE_CODE_LENGTH, "9F3501", "1980", "1801010020010201", "2D00000000000200000300000000000089000806000089000806000000", "FAFFFFFFFFFF"),
        BR_COMBO_AID_ALT_PROFILE("A0000000043060", "4D41455354524F", Constants.SERVICE_CODE_LENGTH, "9F3501", "1B80", "1801010020010201", "2D00000000000200000300000000000089000806000089000806000000", "FFFFFFFFFFFF");
        
        private String mAFL;
        private String mAID;
        private String mAIP;
        private String mAppLabel;
        private String mAppPriorityIndicator;
        private String mCIACDecline;
        private String mCVRMaskAnd;
        private String mPDOL;

        private AlternateProfileData(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
            this.mAID = str;
            this.mAppLabel = str2;
            this.mAppPriorityIndicator = str3;
            this.mPDOL = str4;
            this.mAIP = str5;
            this.mAFL = str6;
            this.mCIACDecline = str7;
            this.mCVRMaskAnd = str8;
        }

        public ByteArray getAID() {
            return McCardClearData.baf.fromHexString(this.mAID);
        }

        public ByteArray getAppLabel() {
            return McCardClearData.baf.fromHexString(this.mAppLabel);
        }

        public ByteArray getmAppPriorityIndicator() {
            return McCardClearData.baf.fromHexString(this.mAppPriorityIndicator);
        }

        public ByteArray getPDOL() {
            return McCardClearData.baf.fromHexString(this.mPDOL);
        }

        public ByteArray getAIP() {
            return McCardClearData.baf.fromHexString(this.mAIP);
        }

        public ByteArray getAFL() {
            return McCardClearData.baf.fromHexString(this.mAFL);
        }

        public ByteArray getCIACDecline() {
            return McCardClearData.baf.fromHexString(this.mCIACDecline);
        }

        public ByteArray getCVRMaskAnd() {
            return McCardClearData.baf.fromHexString(this.mCVRMaskAnd);
        }
    }

    public enum ClearDataDGI {
        DGI8201("8201"),
        DGI8202("8202"),
        DGI8203("8203"),
        DGI8204("8204"),
        DGI8205("8205"),
        DGIA002("a002"),
        DGIA003("a003"),
        DGIA102("a102"),
        DGIA104("a104"),
        DGIA404("a404"),
        DGIA502("a502"),
        DGIA504("a504"),
        DGIA601("a601"),
        DGIA602("a602"),
        DGIA604("a604"),
        DGIB004("b004"),
        DGIB005("b005"),
        DGIB007("b007"),
        DGIB009("b009"),
        DGIB021("b021"),
        DGIB100("b100"),
        DGIC400("c400"),
        DGIRECODRS("1010");
        
        private static Map<String, ClearDataDGI> mClearDgiMap;
        private String mType;

        static {
            mClearDgiMap = new HashMap();
            ClearDataDGI[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                ClearDataDGI clearDataDGI = values[i];
                mClearDgiMap.put(clearDataDGI.getDgiTypeValue(), clearDataDGI);
                i += McCardClearData.GPO_OFFSET_AFL_CONST;
            }
        }

        private ClearDataDGI(String str) {
            this.mType = str;
        }

        public String getDgiTypeValue() {
            return this.mType;
        }

        public static ClearDataDGI getDgiType(byte b, byte b2) {
            int unsignedByteToInt = McUtils.unsignedByteToInt(b);
            int unsignedByteToInt2 = McUtils.unsignedByteToInt(b2);
            if (unsignedByteToInt >= McCardClearData.GPO_OFFSET_AFL_CONST && unsignedByteToInt <= 30 && unsignedByteToInt2 >= McCardClearData.GPO_OFFSET_AFL_CONST && unsignedByteToInt2 <= GF2Field.MASK) {
                return DGIRECODRS;
            }
            String str = McUtils.byteToHex(b) + McUtils.byteToHex(b2);
            Log.m285d("ClearDataDGI", "DGI Type : " + str);
            return (ClearDataDGI) mClearDgiMap.get(str);
        }
    }

    public static class DPANData {
        private ByteArray expirationDate;
        private ByteArray pan;
        private byte panSn;

        public DPANData() {
            this.expirationDate = null;
            this.pan = null;
            this.panSn = (byte) 0;
        }

        public void setExpirationDate(ByteArray byteArray) {
            this.expirationDate = byteArray;
        }

        public void setPan(ByteArray byteArray) {
            this.pan = byteArray;
        }

        public void setPanSN(byte b) {
            this.panSn = b;
        }

        public ByteArray getExpirationDate() {
            return this.expirationDate;
        }

        public ByteArray getPan() {
            return this.pan;
        }

        public byte getPanSn() {
            return this.panSn;
        }
    }

    public static class RecordDGI {
        byte[] mData;
        byte[] mType;

        public RecordDGI(byte[] bArr, byte[] bArr2) {
            this.mType = bArr;
            this.mData = bArr2;
        }
    }

    public McCardClearData() {
        this.cvResultMaskA601 = new byte[]{IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF};
        this.a602RemotePaymentCVRMask = new byte[]{IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF};
        this.a604CardVerificationResultsMask = new byte[]{IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF, IAD_FF};
        this.B007_REMOTE_PAYMENT_AIP_LEN = B100_RAPDU_TIME_LEN;
        this.mDpanData = null;
    }

    static {
        baf = ByteArrayFactory.getInstance();
        GPO_MASK = new byte[]{ApplicationInfoManager.TERM_XP2, (byte) 0, EMVSetStatusApdu.RESET_LOWEST_PRIORITY, (byte) 2, (byte) 0, (byte) 0, (byte) -108, (byte) 0};
        TAG_PAN_SN = new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 52};
        TAG_EXPIRATION_DATE = new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, PinChangeUnblockApdu.INS};
        mAltProfileMap = new HashMap();
        mAltProfileMap.put(AlternateProfileData.US_MAESTRO_AID_ALT_PROFILE.getAID().getHexString(), AlternateProfileData.US_MAESTRO_AID_ALT_PROFILE);
        mAltProfileMap.put(AlternateProfileData.BR_COMBO_AID_ALT_PROFILE.getAID().getHexString(), AlternateProfileData.BR_COMBO_AID_ALT_PROFILE);
    }

    public boolean loadClearDataElement(byte[] bArr) {
        Log.m285d(TAG, "Input DGI : " + byteArrayToHex(bArr));
        if (bArr.length <= B100_RAPDU_TIME_LEN) {
            return false;
        }
        byte b = bArr[0];
        byte b2 = bArr[GPO_OFFSET_AFL_CONST];
        String str = "0000";
        Object obj = new byte[B100_RAPDU_TIME_LEN];
        System.arraycopy(bArr, 0, obj, 0, B100_RAPDU_TIME_LEN);
        String byteArrayToHex = byteArrayToHex(generateClearDataElement(B100_RAPDU_TIME_LEN, obj, 0));
        int unsignedByteToInt = McUtils.unsignedByteToInt(bArr[B100_RAPDU_TIME_LEN]);
        Log.m285d(TAG, "loadClearDataElement : inpute dgi type : " + byteArrayToHex + " , size : " + unsignedByteToInt);
        if ("6f11".equals(byteArrayToHex) || str.endsWith(byteArrayToHex)) {
            Log.m285d(TAG, "SELECT or INIT_UPDATE RADPU : " + byteArrayToHex);
            return true;
        }
        ClearDataDGI dgiType = ClearDataDGI.getDgiType(b, b2);
        if (dgiType == null) {
            Log.m285d(TAG, "Not supported DGI : " + byteArrayToHex(bArr));
            return true;
        }
        saveDgisWithUnusedElements(dgiType, bArr);
        try {
            switch (C05581.f10x236d376b[dgiType.ordinal()]) {
                case GPO_OFFSET_AFL_CONST /*1*/:
                    this.icc_key_a_array = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case B100_RAPDU_TIME_LEN /*2*/:
                    this.icc_key_dq_array = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case A404_CARD_ISSUER_APPLICATION_CODE_LEN /*3*/:
                    this.icc_key_dp_array = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case GPO_OFFSET_AIP /*4*/:
                    this.icc_key_q_array = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    this.icc_key_p_array = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case GPO_CONST /*6*/:
                    this.addionalCheckTable = generateClearDataElement(A002_ADDITIONAL_CHECK_TABLE_LEN, bArr, 81);
                    this.cdolData = generateClearDataElement(GPO_OFFSET_AFL_CONST, bArr, 99);
                    this.crmCountryCode = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256);
                    this.ciacDeclineArqcManagement = generateClearDataElement(A404_CARD_ISSUER_APPLICATION_CODE_LEN, bArr, A002_CIAC_DECLINE_ARQC_OFFSET);
                    this.ciacDeclineArqc = generateClearDataElement(A404_CARD_ISSUER_APPLICATION_CODE_LEN, bArr, CipherSuite.TLS_PSK_WITH_NULL_SHA256);
                    this.contactLessKeyDerivationIndex = generateClearDataElement(GPO_OFFSET_AFL_CONST, bArr, A002_CVM_RESET_TIMEOUT_OFFSET);
                    this.contactLessCvmResetTimeout = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE);
                    this.contactLessDualTapResetTime = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, 204);
                    break;
                case GPO_OFFSET_AFL /*7*/:
                    this.ciacDeclinePPMS = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, 5);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    this.fciShortAID = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    this.fciAlternateAIDA104 = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case TAG_PAN_LEN_MAX /*10*/:
                    this.a404CardIssuerActionCodeARQC = generateClearDataElement(A404_CARD_ISSUER_APPLICATION_CODE_LEN, bArr, 19);
                    Log.m285d(TAG, "a404CardIssuerActionCodeARQC : " + McUtils.byteArrayToHex(this.a404CardIssuerActionCodeARQC) + ", ARQC_Offset : " + 0);
                    break;
                case CertStatus.UNREVOKED /*11*/:
                    this.a502RemotePaymentKeyDerivationIndex = bArr[A404_CARD_ISSUER_APPLICATION_CODE_LEN];
                    Log.m285d(TAG, "a502RemotePaymentKeyDerivationIndex : " + McUtils.byteToHex(this.a502RemotePaymentKeyDerivationIndex));
                    break;
                case CertStatus.UNDETERMINED /*12*/:
                    this.cvResultMaskA601 = generateClearDataElement(GPO_CONST, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    Log.m285d(TAG, "cvResultMaskA601 : " + byteArrayToHex(this.cvResultMaskA601));
                    break;
                case NamedCurve.sect571k1 /*13*/:
                    this.a602RemotePaymentCVRMask = generateClearDataElement(GPO_CONST, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    Log.m285d(TAG, "a602RemotePaymentCVRMask : " + byteArrayToHex(this.a602RemotePaymentCVRMask));
                    break;
                case NamedCurve.sect571r1 /*14*/:
                    this.a604CardVerificationResultsMask = generateClearDataElement(GPO_CONST, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case NamedCurve.secp160k1 /*15*/:
                    this.b004AlternateAid = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case A404_CIAC_ARQC_ALTERNATE_CL_OFFSET /*16*/:
                    this.aipB005 = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    this.aflB005 = generateClearDataElement(unsignedByteToInt - 2, bArr, 5);
                    break;
                case NamedCurve.secp160r2 /*17*/:
                    this.b007RemotePaymentAip = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    Log.m285d(TAG, "b007RemotePaymentAip : " + byteArrayToHex(this.b007RemotePaymentAip));
                    break;
                case A002_ADDITIONAL_CHECK_TABLE_LEN /*18*/:
                    this.b009ApplicationInterchangeProfile = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    this.b009ApplicationFileLocator = generateClearDataElement(unsignedByteToInt - 2, bArr, 5);
                    break;
                case NamedCurve.secp192r1 /*19*/:
                    this.dgiB021_ppse_fci_Data = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                    this.minTimeRRAPDU = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    this.maxTimeRRAPDU = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, 5);
                    this.txTimeRRAPDU = generateClearDataElement(B100_RAPDU_TIME_LEN, bArr, GPO_OFFSET_AFL);
                    break;
                case NamedCurve.secp224r1 /*21*/:
                    this.c400Profiles = generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
                case NamedCurve.secp256k1 /*22*/:
                    if (this.RecordDGIArr == null) {
                        this.RecordDGIArr = new ArrayList();
                    }
                    Object obj2 = new byte[B100_RAPDU_TIME_LEN];
                    System.arraycopy(bArr, 0, obj2, 0, B100_RAPDU_TIME_LEN);
                    this.RecordDGIArr.add(new RecordDGI(obj2, generateClearDataElement(unsignedByteToInt, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN)));
                    break;
                case NamedCurve.secp256r1 /*23*/:
                    this.a504AltKeyDerivationIndex = generateClearDataElement(GPO_OFFSET_AFL_CONST, bArr, A404_CARD_ISSUER_APPLICATION_CODE_LEN);
                    break;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DC_CP getPaymentProfile() {
        DC_CP dc_cp = new DC_CP();
        dc_cp.setCL_Supported(true);
        dc_cp.setRP_Supported(true);
        DC_CP_MPP dc_cp_mpp = new DC_CP_MPP();
        dc_cp.setDC_CP_MPP(dc_cp_mpp);
        DC_CP_BL dc_cp_bl = new DC_CP_BL();
        populateDC_CP_BL(dc_cp_bl);
        dc_cp.setDC_CP_BL(dc_cp_bl);
        CardRiskManagementData cardRiskManagementData = new CardRiskManagementData();
        populateCardRiskMngtData(cardRiskManagementData);
        dc_cp_mpp.setCardRiskManagementData(cardRiskManagementData);
        ContactlessPaymentData contactlessPaymentData = new ContactlessPaymentData();
        populateContactlessPayment(contactlessPaymentData);
        dc_cp_mpp.setContactlessPaymentData(contactlessPaymentData);
        RemotePaymentData remotePaymentData = new RemotePaymentData();
        try {
            populateRemotePaymentData(remotePaymentData);
        } catch (Exception e) {
            Log.m286e(TAG, "DSRP DATA Missing !!!!!" + e.getMessage());
            e.printStackTrace();
            remotePaymentData = null;
            dc_cp.setRP_Supported(false);
        }
        dc_cp_mpp.setRemotePaymentData(remotePaymentData);
        return dc_cp;
    }

    public MCProfilesTable getProfilesTable() {
        if (this.c400Profiles == null) {
            Log.m285d(TAG, "getProfilesTable : c400Profiles is null");
            return null;
        }
        Log.m285d(TAG, "c400Profiles : " + byteArrayToHex(this.c400Profiles));
        return new MCProfilesTable(this.c400Profiles);
    }

    private void populateDC_CP_BL(DC_CP_BL dc_cp_bl) {
        if (this.contactLessCvmResetTimeout != null) {
            dc_cp_bl.setCVM_ResetTimeout(bytesToInt(this.contactLessCvmResetTimeout));
        }
        if (this.contactLessDualTapResetTime != null) {
            dc_cp_bl.setDualTapResetTimeout(bytesToInt(this.contactLessDualTapResetTime));
        }
        dc_cp_bl.setSecurityWord(null);
        dc_cp_bl.setMagstripeCVM_IssuerOptions(null);
        dc_cp_bl.setmChipCVM_IssuerOptions(null);
    }

    private void populateCardRiskMngtData(CardRiskManagementData cardRiskManagementData) {
        if (this.addionalCheckTable != null) {
            cardRiskManagementData.setAdditionalCheckTable(toByteArray(this.addionalCheckTable));
        }
        if (this.crmCountryCode != null) {
            cardRiskManagementData.setCRM_CountryCode(toByteArray(this.crmCountryCode));
        }
    }

    private void populateContactlessPayment(ContactlessPaymentData contactlessPaymentData) {
        ByteArray primaryAid;
        String hexString;
        Object obj;
        Object obj2;
        String str = null;
        if (this.dgiB021_ppse_fci_Data != null) {
            contactlessPaymentData.setPPSE_FCI(toByteArray(this.dgiB021_ppse_fci_Data));
            MCFCITemplate mCFCITemplate = new MCFCITemplate(toByteArray(this.dgiB021_ppse_fci_Data));
            mCFCITemplate.parseFCI_PPSE();
            primaryAid = mCFCITemplate.getPrimaryAid();
        } else {
            primaryAid = null;
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData = new AlternateContactlessPaymentData();
        if (this.b004AlternateAid != null && this.b004AlternateAid.length > 0) {
            alternateContactlessPaymentData.setAID(toByteArray(this.b004AlternateAid));
        } else if (this.fciAlternateAIDA104 != null && this.fciAlternateAIDA104.length > 0) {
            MCFCITemplate mCFCITemplate2 = new MCFCITemplate(toByteArray(this.fciAlternateAIDA104));
            mCFCITemplate2.parseFCI_AID();
            alternateContactlessPaymentData.setAID(mCFCITemplate2.getAID());
        }
        String str2 = TAG;
        StringBuilder append = new StringBuilder().append("alt aid: ");
        if (alternateContactlessPaymentData.getAID() != null) {
            hexString = alternateContactlessPaymentData.getAID().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        if (this.fciAlternateAIDA104 != null && this.fciAlternateAIDA104.length > 0) {
            alternateContactlessPaymentData.setPaymentFCI(toByteArray(this.fciAlternateAIDA104));
        }
        str2 = TAG;
        append = new StringBuilder().append("fci aid: ");
        if (alternateContactlessPaymentData.getPaymentFCI() != null) {
            hexString = alternateContactlessPaymentData.getPaymentFCI().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        if (this.a604CardVerificationResultsMask != null) {
            alternateContactlessPaymentData.setCVR_MaskAnd(toByteArray(this.a604CardVerificationResultsMask));
            Log.m285d(TAG, "CVR mask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
        } else if (this.cvResultMaskA601 != null) {
            alternateContactlessPaymentData.setCVR_MaskAnd(toByteArray(this.cvResultMaskA601));
            Log.m285d(TAG, "CVR cvResultMaskA601 mask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
        } else {
            this.a604CardVerificationResultsMask = new byte[GPO_CONST];
            Arrays.fill(this.a604CardVerificationResultsMask, (byte) IAD_FF);
            alternateContactlessPaymentData.setCVR_MaskAnd(toByteArray(this.a604CardVerificationResultsMask));
            Log.m285d(TAG, "CVR a604CardVerificationResultsMask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
        }
        if (this.a404CardIssuerActionCodeARQC != null) {
            alternateContactlessPaymentData.setCIAC_Decline(toByteArray(this.a404CardIssuerActionCodeARQC));
            Log.m285d(TAG, "a404CardIssuerActionCodeARQC: " + alternateContactlessPaymentData.getCIAC_Decline().getHexString());
        } else if (this.ciacDeclineArqc != null) {
            alternateContactlessPaymentData.setCIAC_Decline(toByteArray(this.ciacDeclineArqc));
            Log.m285d(TAG, "alt: ciacDeclineArqc: " + alternateContactlessPaymentData.getCIAC_Decline().getHexString());
        }
        if (!(this.b009ApplicationInterchangeProfile == null || this.b009ApplicationFileLocator == null)) {
            obj = new byte[(GPO_MASK.length + this.b009ApplicationFileLocator.length)];
            System.arraycopy(GPO_MASK, 0, obj, 0, GPO_MASK.length);
            int length = this.b009ApplicationFileLocator.length;
            Object obj3 = new byte[GPO_OFFSET_AFL_CONST];
            obj3[0] = (byte) (length + GPO_CONST);
            System.arraycopy(obj3, 0, obj, GPO_OFFSET_AFL_CONST, GPO_OFFSET_AFL_CONST);
            System.arraycopy(this.b009ApplicationInterchangeProfile, 0, obj, GPO_OFFSET_AIP, B100_RAPDU_TIME_LEN);
            obj3 = new byte[GPO_OFFSET_AFL_CONST];
            obj3[0] = (byte) length;
            System.arraycopy(obj3, 0, obj, GPO_OFFSET_AFL, GPO_OFFSET_AFL_CONST);
            System.arraycopy(this.b009ApplicationFileLocator, 0, obj, GPO_MASK.length, this.b009ApplicationFileLocator.length);
            alternateContactlessPaymentData.setGPO_Response(toByteArray(obj));
            Log.m285d(TAG, "alternate GPO response: " + toByteArray(obj).getHexString());
        }
        obj = new byte[IAD_LENGTH];
        obj[GPO_OFFSET_AFL_CONST] = CVN_VALUE;
        obj[17] = -1;
        obj[25] = -1;
        str2 = TAG;
        append = new StringBuilder().append("Alt key derivation index: ");
        if (this.a504AltKeyDerivationIndex != null) {
            str = baf.getByteArray(this.a504AltKeyDerivationIndex, this.a504AltKeyDerivationIndex.length).getHexString();
        }
        Log.m285d(str2, append.append(str).toString());
        if (this.a504AltKeyDerivationIndex != null) {
            System.arraycopy(this.a504AltKeyDerivationIndex, 0, obj, 0, GPO_OFFSET_AFL_CONST);
            alternateContactlessPaymentData.setIssuerApplicationData(toByteArray(obj));
        } else if (this.contactLessKeyDerivationIndex != null) {
            System.arraycopy(this.contactLessKeyDerivationIndex, 0, obj, 0, GPO_OFFSET_AFL_CONST);
            alternateContactlessPaymentData.setIssuerApplicationData(toByteArray(obj));
        }
        contactlessPaymentData.setAlternateContactlessPaymentData(alternateContactlessPaymentData);
        if (this.cdolData != null) {
            contactlessPaymentData.setCDOL1_RelatedDataLength(McUtils.unsignedByteToInt(this.cdolData[0]));
        }
        if (this.ciacDeclineArqc != null) {
            contactlessPaymentData.setCIAC_Decline(toByteArray(this.ciacDeclineArqc));
        }
        if (this.ciacDeclinePPMS != null) {
            contactlessPaymentData.setCIAC_DeclineOnPPMS(toByteArray(this.ciacDeclinePPMS));
        }
        if (this.cvResultMaskA601 == null) {
            this.cvResultMaskA601 = new byte[GPO_CONST];
            Arrays.fill(this.cvResultMaskA601, (byte) IAD_FF);
        }
        contactlessPaymentData.setCVR_MaskAnd(toByteArray(this.cvResultMaskA601));
        if (this.minTimeRRAPDU != null) {
            contactlessPaymentData.setMinRRTime(toByteArray(this.minTimeRRAPDU));
        }
        if (this.maxTimeRRAPDU != null) {
            contactlessPaymentData.setMaxRRTime(toByteArray(this.maxTimeRRAPDU));
        }
        if (this.txTimeRRAPDU != null) {
            contactlessPaymentData.setTransmissionRRTime(toByteArray(this.txTimeRRAPDU));
        }
        if (!(this.aflB005 == null || this.aipB005 == null)) {
            obj2 = new byte[(GPO_MASK.length + this.aflB005.length)];
            System.arraycopy(GPO_MASK, 0, obj2, 0, GPO_MASK.length);
            int length2 = this.aflB005.length;
            Object obj4 = new byte[GPO_OFFSET_AFL_CONST];
            obj4[0] = (byte) (length2 + GPO_CONST);
            System.arraycopy(obj4, 0, obj2, GPO_OFFSET_AFL_CONST, GPO_OFFSET_AFL_CONST);
            System.arraycopy(this.aipB005, 0, obj2, GPO_OFFSET_AIP, B100_RAPDU_TIME_LEN);
            obj4 = new byte[GPO_OFFSET_AFL_CONST];
            obj4[0] = (byte) length2;
            System.arraycopy(obj4, 0, obj2, GPO_OFFSET_AFL, GPO_OFFSET_AFL_CONST);
            System.arraycopy(this.aflB005, 0, obj2, GPO_MASK.length, this.aflB005.length);
            contactlessPaymentData.setGPO_Response(toByteArray(obj2));
            Log.m285d(TAG, "GPO response: " + toByteArray(obj2).getHexString());
        }
        if (this.icc_key_a_array != null) {
            contactlessPaymentData.setICC_privateKey_a(toByteArray(this.icc_key_a_array));
        }
        if (this.icc_key_dp_array != null) {
            contactlessPaymentData.setICC_privateKey_dp(toByteArray(this.icc_key_dp_array));
        }
        if (this.icc_key_dq_array != null) {
            contactlessPaymentData.setICC_privateKey_dq(toByteArray(this.icc_key_dq_array));
        }
        if (this.icc_key_p_array != null) {
            contactlessPaymentData.setICC_privateKey_p(toByteArray(this.icc_key_p_array));
        }
        if (this.icc_key_q_array != null) {
            contactlessPaymentData.setICC_privateKey_q(toByteArray(this.icc_key_q_array));
        }
        obj2 = new byte[IAD_LENGTH];
        obj2[GPO_OFFSET_AFL_CONST] = CVN_VALUE;
        obj2[17] = -1;
        obj2[25] = -1;
        if (this.contactLessKeyDerivationIndex != null) {
            System.arraycopy(this.contactLessKeyDerivationIndex, 0, obj2, 0, GPO_OFFSET_AFL_CONST);
            contactlessPaymentData.setIssuerApplicationData(toByteArray(obj2));
        }
        if (primaryAid != null) {
            Log.m285d(TAG, "Primary AID parsed: " + primaryAid.getHexString());
            contactlessPaymentData.setAID(primaryAid);
        } else {
            contactlessPaymentData.setAID(toByteArray(strToByteArray(PRIMARY_AID)).clone());
        }
        if (this.fciShortAID != null) {
            contactlessPaymentData.setPaymentFCI(toByteArray(this.fciShortAID));
            mCFCITemplate = new MCFCITemplate(toByteArray(this.fciShortAID));
            mCFCITemplate.parseFCI_AID();
            primaryAid = mCFCITemplate.getAID();
            if (primaryAid != null) {
                contactlessPaymentData.setAID(primaryAid.clone());
            } else {
                Log.m286e(TAG, "Primary AID parsing issue : Using default value");
            }
        }
        if (contactlessPaymentData.getAID() != null) {
            Log.m285d(TAG, "Primary AID parsed: " + contactlessPaymentData.getAID().getHexString());
        }
        if (this.RecordDGIArr != null && this.RecordDGIArr.size() != 0) {
            Records[] recordsArr = new Records[this.RecordDGIArr.size()];
            for (int i = 0; i < recordsArr.length; i += GPO_OFFSET_AFL_CONST) {
                Records records = new Records();
                RecordDGI recordDGI = (RecordDGI) this.RecordDGIArr.get(i);
                byte[] bArr = new byte[GPO_OFFSET_AFL_CONST];
                bArr[0] = recordDGI.mType[0];
                records.setSFI(toByteArray(bArr));
                bArr = new byte[GPO_OFFSET_AFL_CONST];
                bArr[0] = recordDGI.mType[GPO_OFFSET_AFL_CONST];
                records.setRecordNumber(toByteArray(bArr));
                records.setRecordValue(toByteArray(recordDGI.mData));
                Log.m285d(TAG, "Record: sfi=" + recordDGI.mType[0] + "; number=" + recordDGI.mType[GPO_OFFSET_AFL_CONST]);
                recordsArr[i] = records;
                if (records.getRecordNumber() == TAG_PAN_SN_LEN && records.getSFI() == (byte) 2) {
                    this.mDpanData = parseSDF2Record1(records.getRecordValue());
                    List extractTagData = TlvParserUtil.extractTagData(records.getRecordValue().getBytes(), Mctags.PAR);
                    if (!(extractTagData == null || extractTagData.isEmpty())) {
                        String str3 = (String) extractTagData.get(0);
                        if (!TextUtils.isEmpty(str3)) {
                            this.mPar = McUtils.convertStirngToByteArray(str3);
                        }
                    }
                }
            }
            Log.m285d(TAG, "Records length: " + recordsArr.length);
            contactlessPaymentData.setRecords(recordsArr);
        }
    }

    public void populateRemotePaymentData(RemotePaymentData remotePaymentData) {
        if (this.mDpanData == null) {
            Log.m285d(TAG, "RP DPAN data is empty");
            return;
        }
        remotePaymentData.setAIP(toByteArray(this.b007RemotePaymentAip));
        Log.m285d(TAG, "RP AIP:" + remotePaymentData.getAIP().getHexString());
        remotePaymentData.setCIAC_Decline(toByteArray(this.ciacDeclineArqcManagement));
        Log.m285d(TAG, "RP CIAC Decline:" + remotePaymentData.getCIAC_Decline().getHexString());
        remotePaymentData.setCVR_MASK_AND(toByteArray(this.a602RemotePaymentCVRMask));
        Log.m285d(TAG, "RP CVR Mask:" + remotePaymentData.getCVR_MaskAnd().getHexString());
        Object obj = new byte[IAD_LENGTH];
        obj[GPO_OFFSET_AFL_CONST] = CVN_RP_VALUE;
        obj[17] = -1;
        obj[25] = -1;
        Object obj2 = new byte[GPO_OFFSET_AFL_CONST];
        obj2[0] = this.a502RemotePaymentKeyDerivationIndex;
        System.arraycopy(obj2, 0, obj, 0, GPO_OFFSET_AFL_CONST);
        remotePaymentData.setIssuerApplicationData(toByteArray(obj));
        Log.m285d(TAG, "RP IAD:" + remotePaymentData.getIssuerApplicationData().getHexString());
        remotePaymentData.setPAN(this.mDpanData.getPan());
        Log.m285d(TAG, "RP PAN:" + remotePaymentData.getPAN().getHexString());
        byte[] bArr = new byte[GPO_OFFSET_AFL_CONST];
        bArr[0] = this.mDpanData.getPanSn();
        remotePaymentData.setPANSequenceNumber(toByteArray(bArr));
        Log.m285d(TAG, "RP PAN SN:" + remotePaymentData.getPAN_SequenceNumber().getHexString());
        remotePaymentData.setApplicationExpiryDate(this.mDpanData.getExpirationDate());
        Log.m285d(TAG, "RP exp date:" + remotePaymentData.getApplicationExpiryDate().getHexString());
        if (this.mPar != null && this.mPar.length != 0) {
            remotePaymentData.setPaymentAccountReference(toByteArray(this.mPar));
            Log.m285d(TAG, "PAR : " + remotePaymentData.getPaymentAccountReference().getHexString());
        }
    }

    public static void fillRemotePaymentData(RemotePaymentData remotePaymentData, ByteArray byteArray) {
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        if (remotePaymentData.getAIP() == null) {
            Log.m285d(TAG, "Fill AIP value...");
            remotePaymentData.setAIP(instance.getByteArray(new byte[]{(byte) 2, VerifyPINApdu.P2_PLAINTEXT}, B100_RAPDU_TIME_LEN));
        }
        if (remotePaymentData.getCIAC_Decline() == null) {
            Log.m285d(TAG, "Fill getCIAC_Decline value...");
            remotePaymentData.setCIAC_Decline(instance.getByteArray(new byte[]{(byte) 0, (byte) 0, (byte) 0}, A404_CARD_ISSUER_APPLICATION_CODE_LEN));
        }
        if (remotePaymentData.getCVR_MaskAnd() == null) {
            Log.m285d(TAG, "Fill CVR_MaskAnd value...");
            remotePaymentData.setCVR_MASK_AND(instance.getByteArray(new byte[]{IAD_FF, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, GPO_CONST));
        }
        Object obj = new byte[IAD_LENGTH];
        obj[GPO_OFFSET_AFL_CONST] = CVN_RP_VALUE;
        obj[17] = -1;
        obj[25] = -1;
        Object obj2 = new byte[GPO_OFFSET_AFL_CONST];
        obj2[0] = B100_RAPDU_TIME_LEN;
        System.arraycopy(obj2, 0, obj, 0, GPO_OFFSET_AFL_CONST);
        remotePaymentData.setIssuerApplicationData(instance.getByteArray(obj, obj.length));
        DPANData parseSDF2Record1 = parseSDF2Record1(byteArray);
        if (parseSDF2Record1 != null) {
            Log.m285d(TAG, "Fill CVR_MaskAnd value...");
            remotePaymentData.setApplicationExpiryDate(parseSDF2Record1.getExpirationDate());
            remotePaymentData.setPAN(parseSDF2Record1.getPan());
            byte[] bArr = new byte[GPO_OFFSET_AFL_CONST];
            bArr[0] = parseSDF2Record1.getPanSn();
            remotePaymentData.setPANSequenceNumber(instance.getByteArray(bArr, GPO_OFFSET_AFL_CONST));
        }
    }

    public static boolean fillAlternateProfile(ContactlessPaymentData contactlessPaymentData) {
        if (contactlessPaymentData == null) {
            Log.m286e(TAG, "fillAlternateProfile: contactless payment data is null.");
            return false;
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData = contactlessPaymentData.getAlternateContactlessPaymentData();
        ByteArray ppse_fci = contactlessPaymentData.getPPSE_FCI();
        if (ppse_fci == null) {
            Log.m286e(TAG, "fillAlternateProfile: fciPPSE is null.");
            return false;
        }
        MCFCITemplate mCFCITemplate = new MCFCITemplate(ppse_fci);
        mCFCITemplate.parseFCI_PPSE();
        if (mCFCITemplate.getSecondaryAid() == null) {
            Log.m286e(TAG, "fillAlternateProfile: secondary AID is not supported.");
            return false;
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData2;
        if (alternateContactlessPaymentData == null) {
            Log.m285d(TAG, "fillAlternateProfile: create alternate payment data.");
            alternateContactlessPaymentData2 = new AlternateContactlessPaymentData();
        } else {
            alternateContactlessPaymentData2 = alternateContactlessPaymentData;
        }
        logAltData(alternateContactlessPaymentData2);
        Log.m285d(TAG, "fillAlternateProfile: create alternate payment data.:" + mAltProfileMap.size());
        AlternateProfileData alternateProfileData = (AlternateProfileData) mAltProfileMap.get(mCFCITemplate.getSecondaryAid().getHexString());
        if (alternateProfileData == null) {
            Log.m286e(TAG, "fillAlternateProfile: cannot find alt profile for AID " + mCFCITemplate.getSecondaryAid().getHexString());
            return false;
        }
        if (alternateContactlessPaymentData2.getAID() == null) {
            Log.m285d(TAG, "fillAlternateProfile: secondary aid: " + mCFCITemplate.getSecondaryAid().getHexString());
            alternateContactlessPaymentData2.setAID(alternateProfileData.getAID());
        }
        if (alternateContactlessPaymentData2.getGPO_Response() == null) {
            alternateContactlessPaymentData2.setGPO_Response(constructGPOResponse(alternateProfileData.getAIP(), alternateProfileData.getAFL()));
        }
        if (alternateContactlessPaymentData2.getPaymentFCI() == null) {
            Log.m285d(TAG, "fillAlternateProfile: alt fci is null, restore...");
            ByteArray restoreAlternateFCI = MCFCITemplate.restoreAlternateFCI(contactlessPaymentData.getPaymentFCI(), alternateProfileData.getAID(), alternateProfileData.getAppLabel(), alternateProfileData.getPDOL());
            if (restoreAlternateFCI == null) {
                Log.m285d(TAG, "fillAlternateProfile: alt fci: alt fci is not restored.");
                return false;
            }
            Log.m285d(TAG, "fillAlternateProfile: alt fci: alt fci " + restoreAlternateFCI.getHexString());
            alternateContactlessPaymentData2.setPaymentFCI(restoreAlternateFCI);
        }
        if (alternateContactlessPaymentData2.getCIAC_Decline() == null && alternateProfileData.getCIACDecline() != null) {
            alternateContactlessPaymentData2.setCIAC_Decline(alternateProfileData.getCIACDecline().clone());
            Log.m285d(TAG, "fillAlternateProfile: CIAC Decline: " + alternateContactlessPaymentData2.getCIAC_Decline());
        }
        if (alternateContactlessPaymentData2.getCVR_MaskAnd() == null && alternateProfileData.getCVRMaskAnd() != null) {
            alternateContactlessPaymentData2.setCVR_MaskAnd(alternateProfileData.getCVRMaskAnd().clone());
            Log.m285d(TAG, "fillAlternateProfile: CVR_MaskAnd: " + alternateContactlessPaymentData2.getCVR_MaskAnd());
        }
        if (alternateContactlessPaymentData2.getIssuerApplicationData() == null && contactlessPaymentData.getIssuerApplicationData() != null) {
            Log.m285d(TAG, "fillAlternateProfile: set issuer application data ");
            alternateContactlessPaymentData2.setIssuerApplicationData(contactlessPaymentData.getIssuerApplicationData().clone());
        }
        contactlessPaymentData.setAlternateContactlessPaymentData(alternateContactlessPaymentData2);
        logAltData(alternateContactlessPaymentData2);
        return true;
    }

    private static ByteArray constructGPOResponse(ByteArray byteArray, ByteArray byteArray2) {
        if (byteArray == null || byteArray2 == null) {
            Log.m286e(TAG, "constructGPOResponse: wrong input data: " + (byteArray == null ? "aip" : "afl") + " is null");
            return null;
        }
        Object obj = new byte[(GPO_MASK.length + byteArray2.getLength())];
        System.arraycopy(GPO_MASK, 0, obj, 0, GPO_MASK.length);
        int length = byteArray2.getLength();
        Object obj2 = new byte[GPO_OFFSET_AFL_CONST];
        obj2[0] = (byte) (length + GPO_CONST);
        System.arraycopy(obj2, 0, obj, GPO_OFFSET_AFL_CONST, GPO_OFFSET_AFL_CONST);
        System.arraycopy(byteArray.getBytes(), 0, obj, GPO_OFFSET_AIP, byteArray.getLength());
        obj2 = new byte[GPO_OFFSET_AFL_CONST];
        obj2[0] = (byte) length;
        System.arraycopy(obj2, 0, obj, GPO_OFFSET_AFL, GPO_OFFSET_AFL_CONST);
        System.arraycopy(byteArray2.getBytes(), 0, obj, GPO_MASK.length, byteArray2.getLength());
        ByteArray toByteArray = toByteArray(obj);
        Log.m285d(TAG, "constructGPOResponse: " + toByteArray.getHexString());
        return toByteArray;
    }

    private static void logAltData(AlternateContactlessPaymentData alternateContactlessPaymentData) {
        String str = null;
        if (alternateContactlessPaymentData == null) {
            Log.m285d(TAG, "alternateData is null");
            return;
        }
        String hexString;
        Log.m285d(TAG, "alternateData: aid " + (alternateContactlessPaymentData.getAID() != null ? alternateContactlessPaymentData.getAID().getHexString() : null));
        String str2 = TAG;
        StringBuilder append = new StringBuilder().append("alternateData: fci aid ");
        if (alternateContactlessPaymentData.getPaymentFCI() != null) {
            hexString = alternateContactlessPaymentData.getPaymentFCI().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        str2 = TAG;
        append = new StringBuilder().append("alternateData: cvr mask and ");
        if (alternateContactlessPaymentData.getCVR_MaskAnd() != null) {
            hexString = alternateContactlessPaymentData.getCVR_MaskAnd().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        str2 = TAG;
        append = new StringBuilder().append("alternateData: getCIAC_Decline ");
        if (alternateContactlessPaymentData.getCIAC_Decline() != null) {
            hexString = alternateContactlessPaymentData.getCIAC_Decline().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        str2 = TAG;
        append = new StringBuilder().append("alternateData: getGPO_Response ");
        if (alternateContactlessPaymentData.getGPO_Response() != null) {
            hexString = alternateContactlessPaymentData.getGPO_Response().getHexString();
        } else {
            hexString = null;
        }
        Log.m285d(str2, append.append(hexString).toString());
        hexString = TAG;
        StringBuilder append2 = new StringBuilder().append("alternateData: iad ");
        if (alternateContactlessPaymentData.getIssuerApplicationData() != null) {
            str = alternateContactlessPaymentData.getIssuerApplicationData().getHexString();
        }
        Log.m285d(hexString, append2.append(str).toString());
    }

    private static DPANData parseSDF2Record1(ByteArray byteArray) {
        int i = 0;
        if (byteArray == null) {
            return null;
        }
        DPANData dPANData;
        DPANData dPANData2 = new DPANData();
        while (i < byteArray.getLength() - 1) {
            try {
                byte b = byteArray.getByte(i);
                if (b == 90) {
                    i += GPO_OFFSET_AFL_CONST;
                    b = byteArray.getByte(i);
                    int i2 = b & GF2Field.MASK;
                    if (i2 > TAG_PAN_LEN_MAX || i2 <= 0) {
                        Log.m286e(TAG, "Invalid DPan Length received : " + i2);
                        return null;
                    }
                    i += GPO_OFFSET_AFL_CONST;
                    Log.m285d(TAG, "recordData: " + byteArray.getHexString() + "; i = " + i + "; length = " + i2);
                    dPANData2.setPan(byteArray.copyOfRange(i, i2 + i));
                    Log.m285d(TAG, "recordData: DPAN : " + dPANData2.getPan().getHexString());
                }
                byte b2 = b;
                int i3 = i;
                if (b2 == TAG_PAN_SN[0]) {
                    i3 += GPO_OFFSET_AFL_CONST;
                    byte b3 = byteArray.getByte(i3);
                    i3 += GPO_OFFSET_AFL_CONST;
                    byte b4 = byteArray.getByte(i3);
                    if (b3 == TAG_PAN_SN[GPO_OFFSET_AFL_CONST] && b4 == GPO_OFFSET_AFL_CONST) {
                        i3 += GPO_OFFSET_AFL_CONST;
                        dPANData2.setPanSN(byteArray.getByte(i3));
                    } else if (b3 == TAG_EXPIRATION_DATE[GPO_OFFSET_AFL_CONST] && b4 == A404_CARD_ISSUER_APPLICATION_CODE_LEN) {
                        i3 += GPO_OFFSET_AFL_CONST;
                        dPANData2.setExpirationDate(byteArray.copyOfRange(i3, i3 + A404_CARD_ISSUER_APPLICATION_CODE_LEN));
                    }
                }
                i = i3 + GPO_OFFSET_AFL_CONST;
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.m286e(TAG, "recordData: Exception !!! : " + e.getMessage());
                dPANData = null;
            } catch (IllegalArgumentException e2) {
                Log.m286e(TAG, "recordData: Exception !!! : " + e2.getMessage());
                dPANData = null;
            } catch (NullPointerException e3) {
                Log.m286e(TAG, "recordData: Exception !!! : " + e3.getMessage());
                dPANData = null;
            }
        }
        dPANData = dPANData2;
        return dPANData;
    }

    private void saveDgisWithUnusedElements(ClearDataDGI clearDataDGI, byte[] bArr) {
        if (clearDataDGI == ClearDataDGI.DGIA002 || clearDataDGI == ClearDataDGI.DGIA003 || clearDataDGI == ClearDataDGI.DGIA404 || clearDataDGI == ClearDataDGI.DGIA502 || clearDataDGI == ClearDataDGI.DGIA504 || clearDataDGI == ClearDataDGI.DGIB007) {
            if (this.mUnusedDgiElementsMap == null) {
                this.mUnusedDgiElementsMap = new HashMap();
            }
            this.mUnusedDgiElementsMap.put(clearDataDGI, bArr);
        }
    }

    public MCUnusedDGIElements getMCUnusedDGIElements() {
        if (this.mUnusedDgiElementsMap != null) {
            return new MCUnusedDGIElements(this.mUnusedDgiElementsMap);
        }
        Log.m285d(TAG, "UnusedDgiElements Map is null");
        return null;
    }

    private byte[] generateClearDataElement(int i, byte[] bArr, int i2) {
        Object obj = new byte[i];
        System.arraycopy(bArr, i2, obj, 0, i);
        Log.m285d(TAG, "Parsed DGI : " + byteArrayToHex(obj));
        return obj;
    }

    private int bytesToInt(byte[] bArr) {
        if (bArr == null || bArr.length != B100_RAPDU_TIME_LEN) {
            return 0;
        }
        return ((bArr[0] & GF2Field.MASK) << 8) | (bArr[GPO_OFFSET_AFL_CONST] & GF2Field.MASK);
    }

    public boolean checkCVRMask(byte[] bArr) {
        int length = bArr.length;
        for (int i = 0; i < length; i += GPO_OFFSET_AFL_CONST) {
            if (bArr[i] != null) {
                return true;
            }
        }
        return false;
    }

    public static String byteArrayToHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * B100_RAPDU_TIME_LEN);
        int length = bArr.length;
        for (int i = 0; i < length; i += GPO_OFFSET_AFL_CONST) {
            Object[] objArr = new Object[GPO_OFFSET_AFL_CONST];
            objArr[0] = Integer.valueOf(bArr[i] & GF2Field.MASK);
            stringBuilder.append(String.format("%02x", objArr));
        }
        return stringBuilder.toString();
    }

    private static ByteArray toByteArray(byte[] bArr) {
        return baf.getByteArray(bArr, bArr.length);
    }

    public byte[] strToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / B100_RAPDU_TIME_LEN)];
        for (int i = 0; i < length; i += B100_RAPDU_TIME_LEN) {
            bArr[i / B100_RAPDU_TIME_LEN] = (byte) ((Character.digit(str.charAt(i), A404_CIAC_ARQC_ALTERNATE_CL_OFFSET) << GPO_OFFSET_AIP) + Character.digit(str.charAt(i + GPO_OFFSET_AFL_CONST), A404_CIAC_ARQC_ALTERNATE_CL_OFFSET));
        }
        return bArr;
    }
}
