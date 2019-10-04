/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bouncycastle.util.Arrays
 */
package com.samsung.android.spayfw.payprovider.mastercard.card;

import android.text.TextUtils;
import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.CardRiskManagementData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_BL;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.MChipCVM_IssuerOptions;
import com.mastercard.mcbp.core.mcbpcards.profile.MagstripeCVM_IssuerOptions;
import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final byte CVN_POSITION = 1;
    private static final byte CVN_RP_VALUE = 20;
    private static final byte CVN_VALUE = 21;
    private static final byte CVR_MASK_VALUE = -1;
    private static final int GPO_CONST = 6;
    private static final byte[] GPO_MASK;
    private static final int GPO_OFFSET_AFL = 7;
    private static final int GPO_OFFSET_AFL_CONST = 1;
    private static final int GPO_OFFSET_AIP = 4;
    private static final byte IAD_FF = -1;
    private static final byte IAD_FF_POSITION = 17;
    private static final int IAD_LENGTH = 26;
    private static final String PRIMARY_AID = "A0000000041010";
    private static final String TAG = "McCardClearData";
    private static final byte[] TAG_EXPIRATION_DATE;
    private static final byte TAG_EXPIRATION_DATE_LEN = 3;
    private static final byte TAG_PAN = 90;
    private static final int TAG_PAN_LEN_MAX = 10;
    private static final byte[] TAG_PAN_SN;
    private static final byte TAG_PAN_SN_LEN = 1;
    private static final ByteArrayFactory baf;
    private static HashMap<String, AlternateProfileData> mAltProfileMap;
    private final int B007_REMOTE_PAYMENT_AIP_LEN = 2;
    private List<RecordDGI> RecordDGIArr;
    private byte[] a404CardIssuerActionCodeARQC;
    private byte a502RemotePaymentKeyDerivationIndex;
    private byte[] a504AltKeyDerivationIndex;
    private byte[] a602RemotePaymentCVRMask = new byte[]{-1, -1, -1, -1, -1, -1};
    private byte[] a604CardVerificationResultsMask = new byte[]{-1, -1, -1, -1, -1, -1};
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
    private byte[] cvResultMaskA601 = new byte[]{-1, -1, -1, -1, -1, -1};
    private byte[] dgiB021_ppse_fci_Data;
    private byte[] fciAlternateAIDA104;
    private byte[] fciShortAID;
    private byte[] iccKey;
    private byte[] icc_key_a_array;
    private byte[] icc_key_dp_array;
    private byte[] icc_key_dq_array;
    private byte[] icc_key_p_array;
    private byte[] icc_key_q_array;
    private DPANData mDpanData = null;
    private byte[] mPar;
    private Map<ClearDataDGI, byte[]> mUnusedDgiElementsMap;
    private byte[] magstripeCvmIssuerOptions;
    private byte[] maxTimeRRAPDU;
    private byte[] minTimeRRAPDU;
    private byte[] txTimeRRAPDU;

    static {
        baf = ByteArrayFactory.getInstance();
        GPO_MASK = new byte[]{119, 0, -126, 2, 0, 0, -108, 0};
        TAG_PAN_SN = new byte[]{95, 52};
        TAG_EXPIRATION_DATE = new byte[]{95, 36};
        mAltProfileMap = new HashMap();
        mAltProfileMap.put((Object)AlternateProfileData.US_MAESTRO_AID_ALT_PROFILE.getAID().getHexString(), (Object)AlternateProfileData.US_MAESTRO_AID_ALT_PROFILE);
        mAltProfileMap.put((Object)AlternateProfileData.BR_COMBO_AID_ALT_PROFILE.getAID().getHexString(), (Object)AlternateProfileData.BR_COMBO_AID_ALT_PROFILE);
    }

    public static String byteArrayToHex(byte[] arrby) {
        StringBuilder stringBuilder = new StringBuilder(2 * arrby.length);
        for (byte by : arrby) {
            Object[] arrobject = new Object[]{by & 255};
            stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        }
        return stringBuilder.toString();
    }

    private int bytesToInt(byte[] arrby) {
        if (arrby == null || arrby.length != 2) {
            return 0;
        }
        return (255 & arrby[0]) << 8 | 255 & arrby[1];
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ByteArray constructGPOResponse(ByteArray byteArray, ByteArray byteArray2) {
        if (byteArray != null && byteArray2 != null) {
            byte[] arrby = new byte[GPO_MASK.length + byteArray2.getLength()];
            System.arraycopy((Object)GPO_MASK, (int)0, (Object)arrby, (int)0, (int)GPO_MASK.length);
            int n2 = byteArray2.getLength();
            byte[] arrby2 = new byte[]{(byte)(n2 + 6)};
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)1, (int)1);
            System.arraycopy((Object)byteArray.getBytes(), (int)0, (Object)arrby, (int)4, (int)byteArray.getLength());
            byte[] arrby3 = new byte[]{(byte)n2};
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby, (int)7, (int)1);
            System.arraycopy((Object)byteArray2.getBytes(), (int)0, (Object)arrby, (int)GPO_MASK.length, (int)byteArray2.getLength());
            ByteArray byteArray3 = McCardClearData.toByteArray(arrby);
            c.d(TAG, "constructGPOResponse: " + byteArray3.getHexString());
            return byteArray3;
        }
        StringBuilder stringBuilder = new StringBuilder().append("constructGPOResponse: wrong input data: ");
        String string = byteArray == null ? "aip" : "afl";
        c.e(TAG, stringBuilder.append(string).append(" is null").toString());
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean fillAlternateProfile(ContactlessPaymentData contactlessPaymentData) {
        AlternateContactlessPaymentData alternateContactlessPaymentData;
        if (contactlessPaymentData == null) {
            c.e(TAG, "fillAlternateProfile: contactless payment data is null.");
            return false;
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData2 = contactlessPaymentData.getAlternateContactlessPaymentData();
        ByteArray byteArray = contactlessPaymentData.getPPSE_FCI();
        if (byteArray == null) {
            c.e(TAG, "fillAlternateProfile: fciPPSE is null.");
            return false;
        }
        MCFCITemplate mCFCITemplate = new MCFCITemplate(byteArray);
        mCFCITemplate.parseFCI_PPSE();
        if (mCFCITemplate.getSecondaryAid() == null) {
            c.e(TAG, "fillAlternateProfile: secondary AID is not supported.");
            return false;
        }
        if (alternateContactlessPaymentData2 == null) {
            c.d(TAG, "fillAlternateProfile: create alternate payment data.");
            alternateContactlessPaymentData = new AlternateContactlessPaymentData();
        } else {
            alternateContactlessPaymentData = alternateContactlessPaymentData2;
        }
        McCardClearData.logAltData(alternateContactlessPaymentData);
        c.d(TAG, "fillAlternateProfile: create alternate payment data.:" + mAltProfileMap.size());
        AlternateProfileData alternateProfileData = (AlternateProfileData)((Object)mAltProfileMap.get((Object)mCFCITemplate.getSecondaryAid().getHexString()));
        if (alternateProfileData == null) {
            c.e(TAG, "fillAlternateProfile: cannot find alt profile for AID " + mCFCITemplate.getSecondaryAid().getHexString());
            return false;
        }
        if (alternateContactlessPaymentData.getAID() == null) {
            c.d(TAG, "fillAlternateProfile: secondary aid: " + mCFCITemplate.getSecondaryAid().getHexString());
            alternateContactlessPaymentData.setAID(alternateProfileData.getAID());
        }
        if (alternateContactlessPaymentData.getGPO_Response() == null) {
            alternateContactlessPaymentData.setGPO_Response(McCardClearData.constructGPOResponse(alternateProfileData.getAIP(), alternateProfileData.getAFL()));
        }
        if (alternateContactlessPaymentData.getPaymentFCI() == null) {
            c.d(TAG, "fillAlternateProfile: alt fci is null, restore...");
            ByteArray byteArray2 = MCFCITemplate.restoreAlternateFCI(contactlessPaymentData.getPaymentFCI(), alternateProfileData.getAID(), alternateProfileData.getAppLabel(), alternateProfileData.getPDOL());
            if (byteArray2 == null) {
                c.d(TAG, "fillAlternateProfile: alt fci: alt fci is not restored.");
                return false;
            }
            c.d(TAG, "fillAlternateProfile: alt fci: alt fci " + byteArray2.getHexString());
            alternateContactlessPaymentData.setPaymentFCI(byteArray2);
        }
        if (alternateContactlessPaymentData.getCIAC_Decline() == null && alternateProfileData.getCIACDecline() != null) {
            alternateContactlessPaymentData.setCIAC_Decline(alternateProfileData.getCIACDecline().clone());
            c.d(TAG, "fillAlternateProfile: CIAC Decline: " + alternateContactlessPaymentData.getCIAC_Decline());
        }
        if (alternateContactlessPaymentData.getCVR_MaskAnd() == null && alternateProfileData.getCVRMaskAnd() != null) {
            alternateContactlessPaymentData.setCVR_MaskAnd(alternateProfileData.getCVRMaskAnd().clone());
            c.d(TAG, "fillAlternateProfile: CVR_MaskAnd: " + alternateContactlessPaymentData.getCVR_MaskAnd());
        }
        if (alternateContactlessPaymentData.getIssuerApplicationData() == null && contactlessPaymentData.getIssuerApplicationData() != null) {
            c.d(TAG, "fillAlternateProfile: set issuer application data ");
            alternateContactlessPaymentData.setIssuerApplicationData(contactlessPaymentData.getIssuerApplicationData().clone());
        }
        contactlessPaymentData.setAlternateContactlessPaymentData(alternateContactlessPaymentData);
        McCardClearData.logAltData(alternateContactlessPaymentData);
        return true;
    }

    public static void fillRemotePaymentData(RemotePaymentData remotePaymentData, ByteArray byteArray) {
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        if (remotePaymentData.getAIP() == null) {
            c.d(TAG, "Fill AIP value...");
            remotePaymentData.setAIP(byteArrayFactory.getByteArray(new byte[]{2, -128}, 2));
        }
        if (remotePaymentData.getCIAC_Decline() == null) {
            c.d(TAG, "Fill getCIAC_Decline value...");
            remotePaymentData.setCIAC_Decline(byteArrayFactory.getByteArray(new byte[]{0, 0, 0}, 3));
        }
        if (remotePaymentData.getCVR_MaskAnd() == null) {
            c.d(TAG, "Fill CVR_MaskAnd value...");
            remotePaymentData.setCVR_MASK_AND(byteArrayFactory.getByteArray(new byte[]{-1, 0, 0, 0, 0, 0}, 6));
        }
        byte[] arrby = new byte[26];
        arrby[1] = 20;
        arrby[17] = -1;
        arrby[25] = -1;
        System.arraycopy((Object)new byte[]{2}, (int)0, (Object)arrby, (int)0, (int)1);
        remotePaymentData.setIssuerApplicationData(byteArrayFactory.getByteArray(arrby, arrby.length));
        DPANData dPANData = McCardClearData.parseSDF2Record1(byteArray);
        if (dPANData != null) {
            c.d(TAG, "Fill CVR_MaskAnd value...");
            remotePaymentData.setApplicationExpiryDate(dPANData.getExpirationDate());
            remotePaymentData.setPAN(dPANData.getPan());
            byte[] arrby2 = new byte[]{dPANData.getPanSn()};
            remotePaymentData.setPANSequenceNumber(byteArrayFactory.getByteArray(arrby2, 1));
        }
    }

    private byte[] generateClearDataElement(int n2, byte[] arrby, int n3) {
        byte[] arrby2 = new byte[n2];
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)n2);
        c.d(TAG, "Parsed DGI : " + McCardClearData.byteArrayToHex(arrby2));
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void logAltData(AlternateContactlessPaymentData alternateContactlessPaymentData) {
        if (alternateContactlessPaymentData == null) {
            c.d(TAG, "alternateData is null");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder().append("alternateData: aid ");
        String string = alternateContactlessPaymentData.getAID() != null ? alternateContactlessPaymentData.getAID().getHexString() : null;
        c.d(TAG, stringBuilder.append(string).toString());
        StringBuilder stringBuilder2 = new StringBuilder().append("alternateData: fci aid ");
        String string2 = alternateContactlessPaymentData.getPaymentFCI() != null ? alternateContactlessPaymentData.getPaymentFCI().getHexString() : null;
        c.d(TAG, stringBuilder2.append(string2).toString());
        StringBuilder stringBuilder3 = new StringBuilder().append("alternateData: cvr mask and ");
        String string3 = alternateContactlessPaymentData.getCVR_MaskAnd() != null ? alternateContactlessPaymentData.getCVR_MaskAnd().getHexString() : null;
        c.d(TAG, stringBuilder3.append(string3).toString());
        StringBuilder stringBuilder4 = new StringBuilder().append("alternateData: getCIAC_Decline ");
        String string4 = alternateContactlessPaymentData.getCIAC_Decline() != null ? alternateContactlessPaymentData.getCIAC_Decline().getHexString() : null;
        c.d(TAG, stringBuilder4.append(string4).toString());
        StringBuilder stringBuilder5 = new StringBuilder().append("alternateData: getGPO_Response ");
        String string5 = alternateContactlessPaymentData.getGPO_Response() != null ? alternateContactlessPaymentData.getGPO_Response().getHexString() : null;
        c.d(TAG, stringBuilder5.append(string5).toString());
        StringBuilder stringBuilder6 = new StringBuilder().append("alternateData: iad ");
        ByteArray byteArray = alternateContactlessPaymentData.getIssuerApplicationData();
        String string6 = null;
        if (byteArray != null) {
            string6 = alternateContactlessPaymentData.getIssuerApplicationData().getHexString();
        }
        c.d(TAG, stringBuilder6.append(string6).toString());
    }

    /*
     * Exception decompiling
     */
    private static DPANData parseSDF2Record1(ByteArray var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 9[SIMPLE_IF_TAKEN]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private void populateCardRiskMngtData(CardRiskManagementData cardRiskManagementData) {
        if (this.addionalCheckTable != null) {
            cardRiskManagementData.setAdditionalCheckTable(McCardClearData.toByteArray(this.addionalCheckTable));
        }
        if (this.crmCountryCode != null) {
            cardRiskManagementData.setCRM_CountryCode(McCardClearData.toByteArray(this.crmCountryCode));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void populateContactlessPayment(ContactlessPaymentData contactlessPaymentData) {
        ByteArray byteArray;
        if (this.dgiB021_ppse_fci_Data != null) {
            contactlessPaymentData.setPPSE_FCI(McCardClearData.toByteArray(this.dgiB021_ppse_fci_Data));
            MCFCITemplate mCFCITemplate = new MCFCITemplate(McCardClearData.toByteArray(this.dgiB021_ppse_fci_Data));
            mCFCITemplate.parseFCI_PPSE();
            byteArray = mCFCITemplate.getPrimaryAid();
        } else {
            byteArray = null;
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData = new AlternateContactlessPaymentData();
        if (this.b004AlternateAid != null && this.b004AlternateAid.length > 0) {
            alternateContactlessPaymentData.setAID(McCardClearData.toByteArray(this.b004AlternateAid));
        } else if (this.fciAlternateAIDA104 != null && this.fciAlternateAIDA104.length > 0) {
            MCFCITemplate mCFCITemplate = new MCFCITemplate(McCardClearData.toByteArray(this.fciAlternateAIDA104));
            mCFCITemplate.parseFCI_AID();
            alternateContactlessPaymentData.setAID(mCFCITemplate.getAID());
        }
        StringBuilder stringBuilder = new StringBuilder().append("alt aid: ");
        String string = alternateContactlessPaymentData.getAID() != null ? alternateContactlessPaymentData.getAID().getHexString() : null;
        c.d(TAG, stringBuilder.append(string).toString());
        if (this.fciAlternateAIDA104 != null && this.fciAlternateAIDA104.length > 0) {
            alternateContactlessPaymentData.setPaymentFCI(McCardClearData.toByteArray(this.fciAlternateAIDA104));
        }
        StringBuilder stringBuilder2 = new StringBuilder().append("fci aid: ");
        String string2 = alternateContactlessPaymentData.getPaymentFCI() != null ? alternateContactlessPaymentData.getPaymentFCI().getHexString() : null;
        c.d(TAG, stringBuilder2.append(string2).toString());
        if (this.a604CardVerificationResultsMask == null) {
            if (this.cvResultMaskA601 != null) {
                alternateContactlessPaymentData.setCVR_MaskAnd(McCardClearData.toByteArray(this.cvResultMaskA601));
                c.d(TAG, "CVR cvResultMaskA601 mask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
            } else {
                this.a604CardVerificationResultsMask = new byte[6];
                Arrays.fill((byte[])this.a604CardVerificationResultsMask, (byte)-1);
                alternateContactlessPaymentData.setCVR_MaskAnd(McCardClearData.toByteArray(this.a604CardVerificationResultsMask));
                c.d(TAG, "CVR a604CardVerificationResultsMask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
            }
        } else {
            alternateContactlessPaymentData.setCVR_MaskAnd(McCardClearData.toByteArray(this.a604CardVerificationResultsMask));
            c.d(TAG, "CVR mask: " + alternateContactlessPaymentData.getCVR_MaskAnd().getHexString());
        }
        if (this.a404CardIssuerActionCodeARQC != null) {
            alternateContactlessPaymentData.setCIAC_Decline(McCardClearData.toByteArray(this.a404CardIssuerActionCodeARQC));
            c.d(TAG, "a404CardIssuerActionCodeARQC: " + alternateContactlessPaymentData.getCIAC_Decline().getHexString());
        } else if (this.ciacDeclineArqc != null) {
            alternateContactlessPaymentData.setCIAC_Decline(McCardClearData.toByteArray(this.ciacDeclineArqc));
            c.d(TAG, "alt: ciacDeclineArqc: " + alternateContactlessPaymentData.getCIAC_Decline().getHexString());
        }
        if (this.b009ApplicationInterchangeProfile != null && this.b009ApplicationFileLocator != null) {
            byte[] arrby = new byte[GPO_MASK.length + this.b009ApplicationFileLocator.length];
            System.arraycopy((Object)GPO_MASK, (int)0, (Object)arrby, (int)0, (int)GPO_MASK.length);
            int n2 = this.b009ApplicationFileLocator.length;
            byte[] arrby2 = new byte[]{(byte)(n2 + 6)};
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)1, (int)1);
            System.arraycopy((Object)this.b009ApplicationInterchangeProfile, (int)0, (Object)arrby, (int)4, (int)2);
            byte[] arrby3 = new byte[]{(byte)n2};
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby, (int)7, (int)1);
            System.arraycopy((Object)this.b009ApplicationFileLocator, (int)0, (Object)arrby, (int)GPO_MASK.length, (int)this.b009ApplicationFileLocator.length);
            alternateContactlessPaymentData.setGPO_Response(McCardClearData.toByteArray(arrby));
            c.d(TAG, "alternate GPO response: " + McCardClearData.toByteArray(arrby).getHexString());
        }
        byte[] arrby = new byte[26];
        arrby[1] = 21;
        arrby[17] = -1;
        arrby[25] = -1;
        StringBuilder stringBuilder3 = new StringBuilder().append("Alt key derivation index: ");
        byte[] arrby4 = this.a504AltKeyDerivationIndex;
        String string3 = null;
        if (arrby4 != null) {
            string3 = baf.getByteArray(this.a504AltKeyDerivationIndex, this.a504AltKeyDerivationIndex.length).getHexString();
        }
        c.d(TAG, stringBuilder3.append(string3).toString());
        if (this.a504AltKeyDerivationIndex != null) {
            System.arraycopy((Object)this.a504AltKeyDerivationIndex, (int)0, (Object)arrby, (int)0, (int)1);
            alternateContactlessPaymentData.setIssuerApplicationData(McCardClearData.toByteArray(arrby));
        } else if (this.contactLessKeyDerivationIndex != null) {
            System.arraycopy((Object)this.contactLessKeyDerivationIndex, (int)0, (Object)arrby, (int)0, (int)1);
            alternateContactlessPaymentData.setIssuerApplicationData(McCardClearData.toByteArray(arrby));
        }
        contactlessPaymentData.setAlternateContactlessPaymentData(alternateContactlessPaymentData);
        if (this.cdolData != null) {
            contactlessPaymentData.setCDOL1_RelatedDataLength(McUtils.unsignedByteToInt(this.cdolData[0]));
        }
        if (this.ciacDeclineArqc != null) {
            contactlessPaymentData.setCIAC_Decline(McCardClearData.toByteArray(this.ciacDeclineArqc));
        }
        if (this.ciacDeclinePPMS != null) {
            contactlessPaymentData.setCIAC_DeclineOnPPMS(McCardClearData.toByteArray(this.ciacDeclinePPMS));
        }
        if (this.cvResultMaskA601 == null) {
            this.cvResultMaskA601 = new byte[6];
            Arrays.fill((byte[])this.cvResultMaskA601, (byte)-1);
        }
        contactlessPaymentData.setCVR_MaskAnd(McCardClearData.toByteArray(this.cvResultMaskA601));
        if (this.minTimeRRAPDU != null) {
            contactlessPaymentData.setMinRRTime(McCardClearData.toByteArray(this.minTimeRRAPDU));
        }
        if (this.maxTimeRRAPDU != null) {
            contactlessPaymentData.setMaxRRTime(McCardClearData.toByteArray(this.maxTimeRRAPDU));
        }
        if (this.txTimeRRAPDU != null) {
            contactlessPaymentData.setTransmissionRRTime(McCardClearData.toByteArray(this.txTimeRRAPDU));
        }
        if (this.aflB005 != null && this.aipB005 != null) {
            byte[] arrby5 = new byte[GPO_MASK.length + this.aflB005.length];
            System.arraycopy((Object)GPO_MASK, (int)0, (Object)arrby5, (int)0, (int)GPO_MASK.length);
            int n3 = this.aflB005.length;
            byte[] arrby6 = new byte[]{(byte)(n3 + 6)};
            System.arraycopy((Object)arrby6, (int)0, (Object)arrby5, (int)1, (int)1);
            System.arraycopy((Object)this.aipB005, (int)0, (Object)arrby5, (int)4, (int)2);
            byte[] arrby7 = new byte[]{(byte)n3};
            System.arraycopy((Object)arrby7, (int)0, (Object)arrby5, (int)7, (int)1);
            System.arraycopy((Object)this.aflB005, (int)0, (Object)arrby5, (int)GPO_MASK.length, (int)this.aflB005.length);
            contactlessPaymentData.setGPO_Response(McCardClearData.toByteArray(arrby5));
            c.d(TAG, "GPO response: " + McCardClearData.toByteArray(arrby5).getHexString());
        }
        if (this.icc_key_a_array != null) {
            contactlessPaymentData.setICC_privateKey_a(McCardClearData.toByteArray(this.icc_key_a_array));
        }
        if (this.icc_key_dp_array != null) {
            contactlessPaymentData.setICC_privateKey_dp(McCardClearData.toByteArray(this.icc_key_dp_array));
        }
        if (this.icc_key_dq_array != null) {
            contactlessPaymentData.setICC_privateKey_dq(McCardClearData.toByteArray(this.icc_key_dq_array));
        }
        if (this.icc_key_p_array != null) {
            contactlessPaymentData.setICC_privateKey_p(McCardClearData.toByteArray(this.icc_key_p_array));
        }
        if (this.icc_key_q_array != null) {
            contactlessPaymentData.setICC_privateKey_q(McCardClearData.toByteArray(this.icc_key_q_array));
        }
        byte[] arrby8 = new byte[26];
        arrby8[1] = 21;
        arrby8[17] = -1;
        arrby8[25] = -1;
        if (this.contactLessKeyDerivationIndex != null) {
            System.arraycopy((Object)this.contactLessKeyDerivationIndex, (int)0, (Object)arrby8, (int)0, (int)1);
            contactlessPaymentData.setIssuerApplicationData(McCardClearData.toByteArray(arrby8));
        }
        if (byteArray != null) {
            c.d(TAG, "Primary AID parsed: " + byteArray.getHexString());
            contactlessPaymentData.setAID(byteArray);
        } else {
            contactlessPaymentData.setAID(McCardClearData.toByteArray(this.strToByteArray(PRIMARY_AID)).clone());
        }
        if (this.fciShortAID != null) {
            contactlessPaymentData.setPaymentFCI(McCardClearData.toByteArray(this.fciShortAID));
            MCFCITemplate mCFCITemplate = new MCFCITemplate(McCardClearData.toByteArray(this.fciShortAID));
            mCFCITemplate.parseFCI_AID();
            ByteArray byteArray2 = mCFCITemplate.getAID();
            if (byteArray2 != null) {
                contactlessPaymentData.setAID(byteArray2.clone());
            } else {
                c.e(TAG, "Primary AID parsing issue : Using default value");
            }
        }
        if (contactlessPaymentData.getAID() != null) {
            c.d(TAG, "Primary AID parsed: " + contactlessPaymentData.getAID().getHexString());
        }
        if (this.RecordDGIArr != null && this.RecordDGIArr.size() != 0) {
            Records[] arrrecords = new Records[this.RecordDGIArr.size()];
            for (int i2 = 0; i2 < arrrecords.length; ++i2) {
                String string4;
                Records records = new Records();
                RecordDGI recordDGI = (RecordDGI)this.RecordDGIArr.get(i2);
                byte[] arrby9 = new byte[]{recordDGI.mType[0]};
                records.setSFI(McCardClearData.toByteArray(arrby9));
                byte[] arrby10 = new byte[]{recordDGI.mType[1]};
                records.setRecordNumber(McCardClearData.toByteArray(arrby10));
                records.setRecordValue(McCardClearData.toByteArray(recordDGI.mData));
                c.d(TAG, "Record: sfi=" + recordDGI.mType[0] + "; number=" + recordDGI.mType[1]);
                arrrecords[i2] = records;
                if (records.getRecordNumber() != 1 || records.getSFI() != 2) continue;
                this.mDpanData = McCardClearData.parseSDF2Record1(records.getRecordValue());
                List<String> list = TlvParserUtil.extractTagData(records.getRecordValue().getBytes(), TlvParserUtil.Mctags.PAR);
                if (list == null || list.isEmpty() || TextUtils.isEmpty((CharSequence)(string4 = (String)list.get(0)))) continue;
                this.mPar = McUtils.convertStirngToByteArray(string4);
            }
            c.d(TAG, "Records length: " + arrrecords.length);
            contactlessPaymentData.setRecords(arrrecords);
        }
    }

    private void populateDC_CP_BL(DC_CP_BL dC_CP_BL) {
        if (this.contactLessCvmResetTimeout != null) {
            dC_CP_BL.setCVM_ResetTimeout(this.bytesToInt(this.contactLessCvmResetTimeout));
        }
        if (this.contactLessDualTapResetTime != null) {
            dC_CP_BL.setDualTapResetTimeout(this.bytesToInt(this.contactLessDualTapResetTime));
        }
        dC_CP_BL.setSecurityWord(null);
        dC_CP_BL.setMagstripeCVM_IssuerOptions(null);
        dC_CP_BL.setmChipCVM_IssuerOptions(null);
    }

    private void saveDgisWithUnusedElements(ClearDataDGI clearDataDGI, byte[] arrby) {
        if (clearDataDGI == ClearDataDGI.DGIA002 || clearDataDGI == ClearDataDGI.DGIA003 || clearDataDGI == ClearDataDGI.DGIA404 || clearDataDGI == ClearDataDGI.DGIA502 || clearDataDGI == ClearDataDGI.DGIA504 || clearDataDGI == ClearDataDGI.DGIB007) {
            if (this.mUnusedDgiElementsMap == null) {
                this.mUnusedDgiElementsMap = new HashMap();
            }
            this.mUnusedDgiElementsMap.put((Object)clearDataDGI, (Object)arrby);
        }
    }

    private static ByteArray toByteArray(byte[] arrby) {
        return baf.getByteArray(arrby, arrby.length);
    }

    public boolean checkCVRMask(byte[] arrby) {
        int n2 = arrby.length;
        int n3 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    bl = false;
                    if (n3 >= n2) break block3;
                    if (arrby[n3] == 0) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n3;
        } while (true);
    }

    public MCUnusedDGIElements getMCUnusedDGIElements() {
        if (this.mUnusedDgiElementsMap == null) {
            c.d(TAG, "UnusedDgiElements Map is null");
            return null;
        }
        return new MCUnusedDGIElements(this.mUnusedDgiElementsMap);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DC_CP getPaymentProfile() {
        DC_CP dC_CP = new DC_CP();
        dC_CP.setCL_Supported(true);
        dC_CP.setRP_Supported(true);
        DC_CP_MPP dC_CP_MPP = new DC_CP_MPP();
        dC_CP.setDC_CP_MPP(dC_CP_MPP);
        DC_CP_BL dC_CP_BL = new DC_CP_BL();
        this.populateDC_CP_BL(dC_CP_BL);
        dC_CP.setDC_CP_BL(dC_CP_BL);
        CardRiskManagementData cardRiskManagementData = new CardRiskManagementData();
        this.populateCardRiskMngtData(cardRiskManagementData);
        dC_CP_MPP.setCardRiskManagementData(cardRiskManagementData);
        ContactlessPaymentData contactlessPaymentData = new ContactlessPaymentData();
        this.populateContactlessPayment(contactlessPaymentData);
        dC_CP_MPP.setContactlessPaymentData(contactlessPaymentData);
        RemotePaymentData remotePaymentData = new RemotePaymentData();
        try {
            this.populateRemotePaymentData(remotePaymentData);
        }
        catch (Exception exception) {
            c.e(TAG, "DSRP DATA Missing !!!!!" + exception.getMessage());
            exception.printStackTrace();
            dC_CP.setRP_Supported(false);
            remotePaymentData = null;
        }
        dC_CP_MPP.setRemotePaymentData(remotePaymentData);
        return dC_CP;
    }

    public MCProfilesTable getProfilesTable() {
        if (this.c400Profiles == null) {
            c.d(TAG, "getProfilesTable : c400Profiles is null");
            return null;
        }
        c.d(TAG, "c400Profiles : " + McCardClearData.byteArrayToHex(this.c400Profiles));
        return new MCProfilesTable(this.c400Profiles);
    }

    /*
     * Exception decompiling
     */
    public boolean loadClearDataElement(byte[] var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 4[CASE]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void populateRemotePaymentData(RemotePaymentData remotePaymentData) {
        if (this.mDpanData == null) {
            c.d(TAG, "RP DPAN data is empty");
            return;
        } else {
            remotePaymentData.setAIP(McCardClearData.toByteArray(this.b007RemotePaymentAip));
            c.d(TAG, "RP AIP:" + remotePaymentData.getAIP().getHexString());
            remotePaymentData.setCIAC_Decline(McCardClearData.toByteArray(this.ciacDeclineArqcManagement));
            c.d(TAG, "RP CIAC Decline:" + remotePaymentData.getCIAC_Decline().getHexString());
            remotePaymentData.setCVR_MASK_AND(McCardClearData.toByteArray(this.a602RemotePaymentCVRMask));
            c.d(TAG, "RP CVR Mask:" + remotePaymentData.getCVR_MaskAnd().getHexString());
            byte[] arrby = new byte[26];
            arrby[1] = 20;
            arrby[17] = -1;
            arrby[25] = -1;
            byte[] arrby2 = new byte[]{this.a502RemotePaymentKeyDerivationIndex};
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)0, (int)1);
            remotePaymentData.setIssuerApplicationData(McCardClearData.toByteArray(arrby));
            c.d(TAG, "RP IAD:" + remotePaymentData.getIssuerApplicationData().getHexString());
            remotePaymentData.setPAN(this.mDpanData.getPan());
            c.d(TAG, "RP PAN:" + remotePaymentData.getPAN().getHexString());
            byte[] arrby3 = new byte[]{this.mDpanData.getPanSn()};
            remotePaymentData.setPANSequenceNumber(McCardClearData.toByteArray(arrby3));
            c.d(TAG, "RP PAN SN:" + remotePaymentData.getPAN_SequenceNumber().getHexString());
            remotePaymentData.setApplicationExpiryDate(this.mDpanData.getExpirationDate());
            c.d(TAG, "RP exp date:" + remotePaymentData.getApplicationExpiryDate().getHexString());
            if (this.mPar == null || this.mPar.length == 0) return;
            {
                remotePaymentData.setPaymentAccountReference(McCardClearData.toByteArray(this.mPar));
                c.d(TAG, "PAR : " + remotePaymentData.getPaymentAccountReference().getHexString());
                return;
            }
        }
    }

    public byte[] strToByteArray(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    private static final class AlternateProfileData
    extends Enum<AlternateProfileData> {
        private static final /* synthetic */ AlternateProfileData[] $VALUES;
        public static final /* enum */ AlternateProfileData BR_COMBO_AID_ALT_PROFILE;
        public static final /* enum */ AlternateProfileData US_MAESTRO_AID_ALT_PROFILE;
        private String mAFL;
        private String mAID;
        private String mAIP;
        private String mAppLabel;
        private String mAppPriorityIndicator;
        private String mCIACDecline;
        private String mCVRMaskAnd;
        private String mPDOL;

        static {
            US_MAESTRO_AID_ALT_PROFILE = new AlternateProfileData("A0000000042203", "4445424954", "02", "9F3501", "1980", "1801010020010201", "2D00000000000200000300000000000089000806000089000806000000", "FAFFFFFFFFFF");
            BR_COMBO_AID_ALT_PROFILE = new AlternateProfileData("A0000000043060", "4D41455354524F", "02", "9F3501", "1B80", "1801010020010201", "2D00000000000200000300000000000089000806000089000806000000", "FFFFFFFFFFFF");
            AlternateProfileData[] arralternateProfileData = new AlternateProfileData[]{US_MAESTRO_AID_ALT_PROFILE, BR_COMBO_AID_ALT_PROFILE};
            $VALUES = arralternateProfileData;
        }

        private AlternateProfileData(String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9) {
            this.mAID = string2;
            this.mAppLabel = string3;
            this.mAppPriorityIndicator = string4;
            this.mPDOL = string5;
            this.mAIP = string6;
            this.mAFL = string7;
            this.mCIACDecline = string8;
            this.mCVRMaskAnd = string9;
        }

        public static AlternateProfileData valueOf(String string) {
            return (AlternateProfileData)Enum.valueOf(AlternateProfileData.class, (String)string);
        }

        public static AlternateProfileData[] values() {
            return (AlternateProfileData[])$VALUES.clone();
        }

        public ByteArray getAFL() {
            return baf.fromHexString(this.mAFL);
        }

        public ByteArray getAID() {
            return baf.fromHexString(this.mAID);
        }

        public ByteArray getAIP() {
            return baf.fromHexString(this.mAIP);
        }

        public ByteArray getAppLabel() {
            return baf.fromHexString(this.mAppLabel);
        }

        public ByteArray getCIACDecline() {
            return baf.fromHexString(this.mCIACDecline);
        }

        public ByteArray getCVRMaskAnd() {
            return baf.fromHexString(this.mCVRMaskAnd);
        }

        public ByteArray getPDOL() {
            return baf.fromHexString(this.mPDOL);
        }

        public ByteArray getmAppPriorityIndicator() {
            return baf.fromHexString(this.mAppPriorityIndicator);
        }
    }

    public static final class ClearDataDGI
    extends Enum<ClearDataDGI> {
        private static final /* synthetic */ ClearDataDGI[] $VALUES;
        public static final /* enum */ ClearDataDGI DGI8201 = new ClearDataDGI("8201");
        public static final /* enum */ ClearDataDGI DGI8202 = new ClearDataDGI("8202");
        public static final /* enum */ ClearDataDGI DGI8203 = new ClearDataDGI("8203");
        public static final /* enum */ ClearDataDGI DGI8204 = new ClearDataDGI("8204");
        public static final /* enum */ ClearDataDGI DGI8205 = new ClearDataDGI("8205");
        public static final /* enum */ ClearDataDGI DGIA002 = new ClearDataDGI("a002");
        public static final /* enum */ ClearDataDGI DGIA003 = new ClearDataDGI("a003");
        public static final /* enum */ ClearDataDGI DGIA102 = new ClearDataDGI("a102");
        public static final /* enum */ ClearDataDGI DGIA104 = new ClearDataDGI("a104");
        public static final /* enum */ ClearDataDGI DGIA404 = new ClearDataDGI("a404");
        public static final /* enum */ ClearDataDGI DGIA502 = new ClearDataDGI("a502");
        public static final /* enum */ ClearDataDGI DGIA504 = new ClearDataDGI("a504");
        public static final /* enum */ ClearDataDGI DGIA601 = new ClearDataDGI("a601");
        public static final /* enum */ ClearDataDGI DGIA602 = new ClearDataDGI("a602");
        public static final /* enum */ ClearDataDGI DGIA604 = new ClearDataDGI("a604");
        public static final /* enum */ ClearDataDGI DGIB004 = new ClearDataDGI("b004");
        public static final /* enum */ ClearDataDGI DGIB005 = new ClearDataDGI("b005");
        public static final /* enum */ ClearDataDGI DGIB007 = new ClearDataDGI("b007");
        public static final /* enum */ ClearDataDGI DGIB009 = new ClearDataDGI("b009");
        public static final /* enum */ ClearDataDGI DGIB021 = new ClearDataDGI("b021");
        public static final /* enum */ ClearDataDGI DGIB100 = new ClearDataDGI("b100");
        public static final /* enum */ ClearDataDGI DGIC400 = new ClearDataDGI("c400");
        public static final /* enum */ ClearDataDGI DGIRECODRS = new ClearDataDGI("1010");
        private static Map<String, ClearDataDGI> mClearDgiMap;
        private String mType;

        static {
            ClearDataDGI[] arrclearDataDGI = new ClearDataDGI[]{DGI8201, DGI8202, DGI8203, DGI8204, DGI8205, DGIA002, DGIA003, DGIA102, DGIA104, DGIA404, DGIA502, DGIA504, DGIA601, DGIA602, DGIA604, DGIB004, DGIB005, DGIB007, DGIB009, DGIB021, DGIB100, DGIC400, DGIRECODRS};
            $VALUES = arrclearDataDGI;
            mClearDgiMap = new HashMap();
            for (ClearDataDGI clearDataDGI : ClearDataDGI.values()) {
                mClearDgiMap.put((Object)clearDataDGI.getDgiTypeValue(), (Object)clearDataDGI);
            }
        }

        private ClearDataDGI(String string2) {
            this.mType = string2;
        }

        public static ClearDataDGI getDgiType(byte by, byte by2) {
            int n2 = McUtils.unsignedByteToInt(by);
            int n3 = McUtils.unsignedByteToInt(by2);
            if (n2 >= 1 && n2 <= 30 && n3 >= 1 && n3 <= 255) {
                return DGIRECODRS;
            }
            String string = McUtils.byteToHex(by) + McUtils.byteToHex(by2);
            c.d("ClearDataDGI", "DGI Type : " + string);
            return (ClearDataDGI)((Object)mClearDgiMap.get((Object)string));
        }

        public static ClearDataDGI valueOf(String string) {
            return (ClearDataDGI)Enum.valueOf(ClearDataDGI.class, (String)string);
        }

        public static ClearDataDGI[] values() {
            return (ClearDataDGI[])$VALUES.clone();
        }

        public String getDgiTypeValue() {
            return this.mType;
        }
    }

    public static class DPANData {
        private ByteArray expirationDate = null;
        private ByteArray pan = null;
        private byte panSn = 0;

        public ByteArray getExpirationDate() {
            return this.expirationDate;
        }

        public ByteArray getPan() {
            return this.pan;
        }

        public byte getPanSn() {
            return this.panSn;
        }

        public void setExpirationDate(ByteArray byteArray) {
            this.expirationDate = byteArray;
        }

        public void setPan(ByteArray byteArray) {
            this.pan = byteArray;
        }

        public void setPanSN(byte by) {
            this.panSn = by;
        }
    }

    public static class RecordDGI {
        byte[] mData;
        byte[] mType;

        public RecordDGI(byte[] arrby, byte[] arrby2) {
            this.mType = arrby;
            this.mData = arrby2;
        }
    }

}

