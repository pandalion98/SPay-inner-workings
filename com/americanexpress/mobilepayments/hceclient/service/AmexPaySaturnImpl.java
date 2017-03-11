package com.americanexpress.mobilepayments.hceclient.service;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenMSTDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.sdkmodulelib.util.Constants;

public class AmexPaySaturnImpl extends AmexPayImpl implements AmexPaySaturn {
    public TokenOperationStatus tokenMST() {
        String str = Constants.CLIENT_MINOR_VERSION;
        String str2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            if (OperationMode.PAYMENT.equals(Operation.OPERATION.getMode())) {
                new TokenMSTDelegate().doOperation();
                return tokenOperationStatus;
            }
            throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenMST::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str3 = Constants.CLIENT_MINOR_VERSION;
            setStatusInResponse(tokenOperationStatus, str2, str3);
            str3 = str2;
            str3 = str2;
        }
    }

    protected String prepareTokenConfiguration() {
        String str = "=";
        String str2 = ",";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HCEClientConstants.INAPP_SUPPORTED).append(str).append(SessionManager.getSession().getValue(HCEClientConstants.INAPP_SUPPORTED, true)).append(str2);
        stringBuilder.append(HCEClientConstants.TAP_PAYMENT_SUPPORTED).append(str).append(SessionManager.getSession().getValue(HCEClientConstants.TAP_PAYMENT_SUPPORTED, true)).append(str2);
        stringBuilder.append(HCEClientConstants.ISSUER_COUNTRY_CODE).append(str).append(SessionManager.getSession().getValue(HCEClientConstants.ISSUER_COUNTRY_CODE, true)).append(str2);
        stringBuilder.append(HCEClientConstants.MST_SUPPORTED).append(str).append(TagsMapUtil.getTagValue(HCEClientConstants.MST_SUPPORTED)).append(str2);
        return stringBuilder.toString();
    }
}
