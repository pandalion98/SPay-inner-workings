package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.CardProfileParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;
import org.bouncycastle.crypto.macs.SkeinMac;

public class SecureChannelUnwrapDelegate extends OperationDelegate {
    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            invokeCloudDataUnwrap();
            boolean isPersoValid = isPersoValid((TokenDataHolder) session.getValue(SessionConstants.TOKEN_DATA_HOLDER, false));
            if (((Boolean) session.getValue(SessionConstants.IS_ALREADY_PROVSIONED, false)).booleanValue() && isPersoValid) {
                throw new HCEClientException(OperationStatus.TOKEN_REF_ID_ALREADY_PROVISIONED);
            }
            session.setValue(SessionConstants.IS_PROVISIONING_FLOW, Boolean.valueOf(isPersoValid));
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    private void invokeCloudDataUnwrap() {
        Session session = SessionManager.getSession();
        String str = (String) session.getValue(SessionConstants.TOKEN_DATA, true);
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[(bytes.length + SkeinMac.SKEIN_1024)];
        checkSCStatus(secureComponentImpl.unwrap(0, bytes, bytes.length, bArr, bArr.length));
        if (secureComponentImpl.isRetryExecuted()) {
            bArr = secureComponentImpl.getDestBuffer();
        }
        String obj = LLVARUtil.llVarToObjects(bArr)[0].toString();
        TokenDataHolder parseTokenData = new CardProfileParser().parseTokenData(obj);
        session.setValue(SessionConstants.TOKEN_DATA_HOLDER, parseTokenData);
        parseTokenData.setTlsClearTokenData(obj);
        Map dgisMap = parseTokenData.getDgisMap();
        Map tagsMap = parseTokenData.getTagsMap();
        JSONUtil.toJSONString(dgisMap);
        JSONUtil.toJSONString(tagsMap);
    }

    private boolean isPersoValid(TokenDataHolder tokenDataHolder) {
        if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI)) && tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI))) {
            runAFLChecks(tokenDataHolder, CPDLConfig.EMV_AIP_DGI, CPDLConfig.EMV_AFL_TAG, true);
            runAFLChecks(tokenDataHolder, CPDLConfig.MS_AIP_DGI, CPDLConfig.MS_AFL_TAG, false);
            Object obj = 57;
        } else if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI))) {
            runAFLChecks(tokenDataHolder, CPDLConfig.EMV_AIP_DGI, CPDLConfig.EMV_AFL_TAG, true);
            r3 = -73;
        } else if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI))) {
            runAFLChecks(tokenDataHolder, CPDLConfig.MS_AIP_DGI, CPDLConfig.MS_AFL_TAG, false);
            r3 = -63;
        } else {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::GPO NOT PRESENT::");
            return false;
        }
        if (obj == -73 || obj == 57) {
            if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_EMV_DGI))) {
                byte[] hexStringToByteArray = HexUtils.hexStringToByteArray((String) tokenDataHolder.getDgisMap().get(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_EMV_DGI)));
                if (hexStringToByteArray.length < 11) {
                    Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::EMV IAD Length not correct::");
                    return false;
                } else if (!(hexStringToByteArray[10] == (byte) 1 || hexStringToByteArray[10] == 2)) {
                    Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::IAD Format is not correct::");
                    return false;
                }
            }
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::EMV IAD is missing::");
            return false;
        }
        if ((obj == -63 || obj == 57) && !tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_MS_DGI))) {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::IAD not present::");
            return false;
        } else if (tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), true) != null) {
            return true;
        } else {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::isPersoValid::XPM CONFIGURATION NOT PRESENT::");
            return false;
        }
    }

    private boolean runAFLChecks(TokenDataHolder tokenDataHolder, String str, String str2, boolean z) {
        TagValue tagValue = TagsMapUtil.getTagValue(tokenDataHolder.getTagsMap(), CPDLConfig.getDGI_TAG(str), CPDLConfig.getDGI_TAG(str2), true);
        if (tagValue == null) {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::runAFLChecks::GPO Response DGI inconsistent::");
            return false;
        } else if (checkRecordsForAFL(tokenDataHolder, HexUtils.hexStringToByteArray(tagValue.getValue()), z)) {
            return true;
        } else {
            Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::runAFLChecks::AFL Inconsistent with record data present in perso::");
            return false;
        }
    }

    private boolean checkRecordsForAFL(TokenDataHolder tokenDataHolder, byte[] bArr, boolean z) {
        int i = 0;
        while (i < bArr.length) {
            int i2 = i + 1;
            byte b = (byte) (bArr[i] >> 3);
            int i3 = i2 + 1;
            byte b2 = bArr[i2];
            int i4 = i3 + 1;
            byte b3 = bArr[i3];
            byte b4 = b2;
            while (b4 <= b3) {
                String str = Utility.byte2Hex(b) + Utility.byte2Hex(b4);
                if (str.compareToIgnoreCase("0101") == 0) {
                    if (z) {
                        str = CPDLConfig.getDGI_TAG(CPDLConfig.SFI1_REC1_DGI_EMV);
                    } else {
                        str = CPDLConfig.getDGI_TAG(CPDLConfig.SFI1_REC1_DGI_MS);
                    }
                }
                if (tokenDataHolder.containsDGI(str)) {
                    b4 = (byte) (b4 + 1);
                } else {
                    Log.e(HCEClientConstants.TAG, "::SecureChannelUnwrapDelegate::checkRecordsForAFL::Record " + str + " not found corresponding to AFL");
                    return false;
                }
            }
            i = i4 + 1;
        }
        return true;
    }
}
