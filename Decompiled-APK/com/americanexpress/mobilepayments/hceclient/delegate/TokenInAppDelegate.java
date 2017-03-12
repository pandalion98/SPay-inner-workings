package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponent;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.TIDGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class TokenInAppDelegate extends OperationDelegate {
    private static final String AMOUNT_AUTHORIZED = "000000000000";
    private static final String AMOUNT_OTHER = "000000000000";
    private static final String APPLICATION_INTERCHANGE_PROFILE = "0000";
    private static final String TERMINAL_COUNTRY_CODE = "0000";
    private static final String TERMINAL_VERIFICATION_RESULTS = "8000000000";
    private static final String TRANSACTION_CURRENCY_CODE = "0000";
    private static final String TRANSACTION_DATE = "140101";
    private static final String TRANSACTION_TYPE = "00";

    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            if (PaymentUtils.checkXPMConfig((byte) 3, Tnaf.POW_2_WIDTH) == Constants.MAGIC_FALSE || 117 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else {
                String str = (String) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.UNPREDICTABLE_NUMBER);
                String str2 = (String) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TRANSACTION_CONTEXT);
                String metaDataValue = MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC);
                String hexByteArrayToString = HexUtils.hexByteArrayToString(computeCVRBytes());
                byte[] computeTokenDataBlockA = computeTokenDataBlockA(computeCryptogram(computeCryptoInputData(str, metaDataValue, hexByteArrayToString)), str, metaDataValue, hexByteArrayToString);
                PaymentUtils.removeUsedLUPCAndAdvanceATC();
                session.setValue(SessionConstants.INAPP_PAYLOAD, HexUtils.byteArrayToHexString(computeInAppPayload(computeTokenDataBlockA, str2)));
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TID, TIDGenerator.getTransactionID(ApplicationInfoManager.TERMINAL_MODE_CL_EMV));
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenInAppDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    private byte[] computeTokenInfo() {
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), true);
        }
        String value = tagValue.getValue();
        String substring = value.substring(0, value.length() - 1);
        tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_EXPIRY), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_EXPIRY), true);
        }
        return (substring + tagValue.getValue()).getBytes();
    }

    private byte[] computeCVRBytes() {
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CVR, new byte[]{(byte) 3, VerifyPINApdu.P2_PLAINTEXT, (byte) 0, (byte) 0});
        PaymentUtils.setCVRandCVMBasedOnCDCVMSTatus();
        return (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CVR);
    }

    private String computeCryptoInputData(String str, String str2, String str3) {
        String str4 = "00000000000000000000000000008000000000000014010100" + str;
        return Utility.int2Hex(str4.length() / 2) + str4 + TRANSACTION_CURRENCY_CODE + str2 + str3;
    }

    private byte[] computeCryptogram(String str) {
        Session session = SessionManager.getSession();
        byte[] bArr = new byte[8];
        int computeAC = new SecureComponentImpl().computeAC(HexUtils.hexStringToByteArray(str), bArr);
        checkSCStatus(computeAC);
        if (computeAC < 0) {
            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        } else if (computeAC == HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            PaymentUtils.setTokenStatus(StateMode.TERMINATE);
            Log.e(HCEClientConstants.TAG, "::TokenInAppDelegate::HIGHEST ATC REACHED - APPLICATION TERMINATED!!");
            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        } else {
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.APPLICATION_CYPTOGRAM, HexUtils.byteArrayToHexString(bArr));
            session.setValue(SessionConstants.INAPP_USED_ATC, Integer.valueOf(computeAC));
            return bArr;
        }
    }

    private byte[] computeTokenDataBlockA(byte[] bArr, String str, String str2, String str3) {
        Session session = SessionManager.getSession();
        Object obj = new byte[20];
        obj[0] = (byte) 38;
        System.arraycopy(bArr, 0, obj, 1, bArr.length);
        int length = bArr.length + 1;
        Object hexStringToByteArray = HexUtils.hexStringToByteArray(str);
        System.arraycopy(hexStringToByteArray, 0, obj, length, hexStringToByteArray.length);
        length += hexStringToByteArray.length;
        hexStringToByteArray = HexUtils.hexStringToByteArray(str2);
        System.arraycopy(hexStringToByteArray, 0, obj, length, hexStringToByteArray.length);
        length += hexStringToByteArray.length;
        hexStringToByteArray = HexUtils.hexStringToByteArray(str3);
        System.arraycopy(hexStringToByteArray, 1, obj, length, hexStringToByteArray.length - 1);
        length = (length + hexStringToByteArray.length) - 1;
        String dKIForATC = MetaDataManager.getDKIForATC(String.format("%04x", new Object[]{(Integer) session.getValue(SessionConstants.INAPP_USED_ATC, true)}));
        if (dKIForATC == null) {
            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        }
        obj[length] = HexUtils.hexStringToByteArray(dKIForATC)[0];
        length++;
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAN_SEQUENCE_NUMBER), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAN_SEQUENCE_NUMBER), true);
        }
        obj[length] = HexUtils.hexStringToByteArray(tagValue.getValue())[0];
        return obj;
    }

    private byte[] computeInAppPayload(byte[] bArr, String str) {
        SecureComponent secureComponentImpl = new SecureComponentImpl();
        byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(str);
        Object obj = new byte[65];
        Object obj2 = new byte[20];
        Object computeTokenInfo = computeTokenInfo();
        System.arraycopy(computeTokenInfo, 0, obj, 0, computeTokenInfo.length);
        int length = computeTokenInfo.length + 0;
        System.arraycopy(bArr, 0, obj, length, bArr.length);
        System.arraycopy(obj2, 0, obj, length + bArr.length, obj2.length);
        checkSCStatus(secureComponentImpl.reqInApp(hexStringToByteArray, obj));
        return obj;
    }
}
