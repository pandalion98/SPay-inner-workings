package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class PPSEResponse {
    private static final byte[] _baPPSE_AID;
    private String availableAIDs;
    private String currentAID;
    private String sPPSEAIDEntry;
    private String sPPSERes;

    static {
        _baPPSE_AID = new byte[]{(byte) 50, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 65, (byte) 89, (byte) 46, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 89, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 46, (byte) 68, (byte) 68, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 48, (byte) 49};
    }

    public PPSEResponse() {
        String str = BuildConfig.FLAVOR;
        this.currentAID = str;
        this.sPPSEAIDEntry = str;
        this.sPPSERes = str;
        this.availableAIDs = str;
    }

    public byte isAIDPresent(byte[] bArr) {
        String[] split = this.availableAIDs.split("=");
        int i = 0;
        byte b = (byte) 0;
        while (i < split.length) {
            if (split[i].compareToIgnoreCase(BuildConfig.FLAVOR) != 0 && HexUtils.secureCompare(bArr, (short) 0, HexUtils.hexStringToByteArray(split[i].substring(2)), (short) 0, (short) bArr.length) == Constants.MAGIC_TRUE) {
                b = (byte) 1;
                if (split[i].startsWith(HCEClientConstants.HEX_ZERO_BYTE)) {
                    b = (byte) -127;
                }
                this.currentAID = split[i].substring(2);
            }
            i++;
        }
        return b;
    }

    public String getCurrentAID() {
        return this.currentAID;
    }

    public String getsPPSEAIDEntry() {
        return this.sPPSEAIDEntry;
    }

    public void buildPPSEResponse(boolean z) {
        DataContext sessionInstance = DataContext.getSessionInstance();
        PPSETagValue pPSETagValue = (PPSETagValue) TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V1), true);
        PPSETagValue pPSETagValue2 = (PPSETagValue) TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V2), false);
        String str = BuildConfig.FLAVOR;
        if (pPSETagValue != null) {
            str = pPSETagValue.getsDirectoryEntry();
            this.availableAIDs = HCEClientConstants.HEX_ZERO_BYTE + pPSETagValue.getsAID();
        }
        String str2 = str;
        if (z && pPSETagValue2 != null) {
            str2 = str2 + pPSETagValue2.getsDirectoryEntry();
            this.availableAIDs += "=01" + pPSETagValue2.getsAID();
        }
        if (str2.compareToIgnoreCase(BuildConfig.FLAVOR) != 0) {
            str2 = EMVConstants.FCI_PROP_TEMPLATE_TAG + Utility.constructLV(EMVConstants.ISSUER_DISCRETIONARY_DATA_TAG + Utility.constructLV(str2));
            this.sPPSEAIDEntry = EMVConstants.ISSUER_DISCRETIONARY_DATA_TAG + Utility.constructLV(str2);
        }
        this.sPPSERes = APDUConstants.SELECT_RESPONSE_FCI_TEMPLATE + Utility.constructLV((APDUConstants.SELECT_RESPONSE_DF_NAME + Utility.constructLV(HexUtils.byteArrayToHexString(_baPPSE_AID))) + str2);
    }

    public byte[] getPPSERes() {
        return HexUtils.hexStringToByteArray(this.sPPSERes);
    }
}
