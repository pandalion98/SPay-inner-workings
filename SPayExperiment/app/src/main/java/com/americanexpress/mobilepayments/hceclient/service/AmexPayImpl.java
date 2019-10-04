/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.StackTraceElement
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 */
package com.americanexpress.mobilepayments.hceclient.service;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegateFactory;
import com.americanexpress.mobilepayments.hceclient.delegate.SecureChannelUnwrapDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenAPDUDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenChannelInitializeDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenChannelUpdateDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenInAppDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenLCMDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenRefreshStatusDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenSetCDCVMDelegate;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenUpdateDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenChannelInitializeResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenChannelUpdateResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenCloseResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenInAppResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.model.TokenPersoResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenRefreshStatusResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenUpdateResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.service.AmexPay;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;

public class AmexPayImpl
implements AmexPay {
    /*
     * Enabled aggressive block sorting
     */
    private void prepareResponse(TokenRefreshStatusResponse tokenRefreshStatusResponse) {
        tokenRefreshStatusResponse.setMaxATC(Integer.parseInt((String)MetaDataManager.getMetaDataValue("MAX_ATC"), (int)16));
        tokenRefreshStatusResponse.setLupcCount(Integer.parseInt((String)MetaDataManager.getMetaDataValue("LUPC_COUNT")));
        tokenRefreshStatusResponse.setRefreshRequired(Boolean.valueOf((String)MetaDataManager.getMetaDataValue("REFRESH_REQUIRED")));
        long l2 = MetaDataManager.getMetaDataValue("LUPC_REFRESH_CHECK_BACK") == null ? -1L : Long.parseLong((String)MetaDataManager.getMetaDataValue("LUPC_REFRESH_CHECK_BACK"));
        tokenRefreshStatusResponse.setLupcRefreshCheckBack(l2);
        tokenRefreshStatusResponse.setClientVersion("2.0.0");
        tokenRefreshStatusResponse.setTokenDataVersion(MetaDataManager.getMetaDataValue("TOKEN_DATA_VERSION"));
    }

    protected OperationStatus getOperationStatus(Exception exception) {
        OperationStatus operationStatus = OperationStatus.UNEXPECTED_ERROR;
        if (exception instanceof HCEClientException && ((HCEClientException)exception).getOperationStatus() != null) {
            return ((HCEClientException)exception).getOperationStatus();
        }
        return operationStatus;
    }

    protected void operationModeSupported(OperationMode operationMode) {
    }

    protected String prepareTokenConfiguration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INAPP_SUPPORTED").append("=").append(SessionManager.getSession().getValue("INAPP_SUPPORTED", true)).append(",");
        stringBuilder.append("TAP_PAYMENT_SUPPORTED").append("=").append(SessionManager.getSession().getValue("TAP_PAYMENT_SUPPORTED", true)).append(",");
        stringBuilder.append("ISSUER_COUNTRY_CODE").append("=").append(SessionManager.getSession().getValue("ISSUER_COUNTRY_CODE", true)).append(",");
        stringBuilder.append("MST_SUPPORTED").append("=").append(TagsMapUtil.getTagValue("MST_SUPPORTED")).append(",");
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setStatusInResponse(TokenOperationStatus tokenOperationStatus, OperationStatus operationStatus, String string) {
        String string2;
        OperationStatus operationStatus2 = operationStatus.getStatus(string);
        tokenOperationStatus.setReasonCode(operationStatus2.getReasonCode());
        tokenOperationStatus.setDetailCode(operationStatus2.getDetailCode());
        String string3 = operationStatus2.getDetailMessage();
        if (string.compareToIgnoreCase("07") == 0) {
            String string4;
            String string5 = (String)ApplicationInfoManager.getApplcationInfoValue("9F4E", true);
            String string6 = (String)ApplicationInfoManager.getApplcationInfoValue("9F15", true);
            if (Constants.MAGIC_TRUE == (Short)ApplicationInfoManager.getApplcationInfoValue("TR_RETURN_AMNT_IN_GAC") && (string4 = (String)ApplicationInfoManager.getApplcationInfoValue("9F02", true)) != null && string4.compareToIgnoreCase("") != 0) {
                string3 = string3 + ",AMOUNT=" + string4;
                ApplicationInfoManager.setApplcationInfoValue("TR_RETURN_AMNT_IN_GAC", Constants.MAGIC_FALSE);
            }
            String string7 = string3;
            if (string5 != null && string5.compareToIgnoreCase("") != 0) {
                string2 = string7 + ",MERCHANT_NAME=" + string5;
                ApplicationInfoManager.setApplcationInfoValue("TR_RETURN_AMNT_IN_GAC", Constants.MAGIC_TRUE);
            } else {
                string2 = string7;
            }
            if (string6 != null && string6.compareToIgnoreCase("") != 0) {
                string2 = string2 + ",MERCHANT_CODE=" + string6;
                ApplicationInfoManager.setApplcationInfoValue("TR_RETURN_AMNT_IN_GAC", Constants.MAGIC_TRUE);
            }
        } else {
            string2 = string3;
        }
        tokenOperationStatus.setDetailMessage(string2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenAPDUResponse tokenAPDU(byte[] var1_1) {
        var2_2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var3_3 = new TokenAPDUResponse();
        try {
            if (var1_1.length < 4) {
                throw new HCEClientException(OperationStatus.INVALID_DATA);
            }
            if (Operation.OPERATION.getMode() == null) throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            if (!OperationMode.PAYMENT.equals((Object)Operation.OPERATION.getMode()) && !OperationMode.TAP_PAYMENT.equals((Object)Operation.OPERATION.getMode())) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            var11_11 = SessionManager.getSession();
            var11_11.setValue("COMMAND_APDU_BYTES", var1_1);
            new TokenAPDUDelegate().doOperation();
            var6_5 = (TokenAPDUResponse)var11_11.getValue("RESPONSE_APDU", true);
            ** GOTO lbl20
        }
        catch (Exception var5_4) {
            block12 : {
                var6_5 = var3_3;
                var7_6 = var5_4;
                break block12;
                catch (Throwable var4_9) {}
                ** GOTO lbl-1000
lbl20: // 1 sources:
                try {
                    var14_12 = (OperationStatus)var11_11.getValue("OPERATION_STATUS", false);
                }
                catch (Throwable var13_13) {
                    var3_3 = var6_5;
                    var4_10 = var13_13;
                    ** GOTO lbl-1000
                }
                catch (Exception var7_7) {}
                this.setStatusInResponse(var6_5, var14_12, "07");
                SessionManager.getSession().cleanAPDU();
                return var6_5;
            }
            try {
                var6_5.setsSW((short)28416);
                Log.e((String)"core-hceclient", (String)("::tokenAPDU::catch::" + var7_6.getMessage()));
                var10_8 = this.getOperationStatus(var7_6);
            }
            catch (Throwable var8_14) {
                var3_3 = var6_5;
                var4_10 = var8_14;
            }
            this.setStatusInResponse(var6_5, var10_8, "07");
            SessionManager.getSession().cleanAPDU();
            return var6_5;
        }
lbl-1000: // 3 sources:
        {
            this.setStatusInResponse(var3_3, var2_2, "07");
            SessionManager.getSession().cleanAPDU();
            throw var4_10;
        }
    }

    @Override
    public TokenChannelInitializeResponse tokenChannelInitialize(byte[] arrby) {
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenChannelInitializeResponse tokenChannelInitializeResponse = new TokenChannelInitializeResponse();
        try {
            Session session = SessionManager.getSession();
            session.setValue("CHANNEL_PARAM", arrby);
            ((OperationDelegate)new TokenChannelInitializeDelegate()).doOperation();
            tokenChannelInitializeResponse.setEphemeralData((String)session.getValue("DEVICE_PUBLIC_KEY", false));
        }
        catch (Exception exception) {
            OperationStatus operationStatus2;
            try {
                Log.e((String)"core-hceclient", (String)("::tokenChannelInitialize::catch::" + exception.getMessage()));
                operationStatus2 = this.getOperationStatus(exception);
            }
            catch (Throwable throwable) {
                this.setStatusInResponse(tokenChannelInitializeResponse, operationStatus, "05");
                throw throwable;
            }
            this.setStatusInResponse(tokenChannelInitializeResponse, operationStatus2, "05");
            return tokenChannelInitializeResponse;
        }
        this.setStatusInResponse(tokenChannelInitializeResponse, operationStatus, "05");
        return tokenChannelInitializeResponse;
    }

    @Override
    public TokenChannelUpdateResponse tokenChannelUpdate(byte[] arrby) {
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenChannelUpdateResponse tokenChannelUpdateResponse = new TokenChannelUpdateResponse();
        try {
            SessionManager.getSession().setValue("CERTIFICATE", arrby);
            ((OperationDelegate)new TokenChannelUpdateDelegate()).doOperation();
        }
        catch (Exception exception) {
            OperationStatus operationStatus2;
            try {
                Log.e((String)"core-hceclient", (String)("::tokenChannelUpdate::catch::" + exception.getMessage()));
                operationStatus2 = this.getOperationStatus(exception);
            }
            catch (Throwable throwable) {
                this.setStatusInResponse(tokenChannelUpdateResponse, operationStatus, "06");
                throw throwable;
            }
            this.setStatusInResponse(tokenChannelUpdateResponse, operationStatus2, "06");
            return tokenChannelUpdateResponse;
        }
        this.setStatusInResponse(tokenChannelUpdateResponse, operationStatus, "06");
        return tokenChannelUpdateResponse;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenCloseResponse tokenClose() {
        var1_1 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var2_2 = SessionManager.getSession();
        var3_3 = new TokenCloseResponse();
        try {
            if (Operation.OPERATION.getMode() == null) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            ** GOTO lbl16
            {
                catch (Exception var7_4) {}
                Log.e((String)"core-hceclient", (String)("::tokenClose::catch::" + var7_4.getMessage()));
                var9_5 = this.getOperationStatus(var7_4);
                this.setStatusInResponse(var3_3, var9_5, "02");
                var2_2.clean();
                Operation.OPERATION.releaseOperation();
                return var3_3;
lbl16: // 1 sources:
                OperationDelegateFactory.getTokenCloseDelegate().doOperation();
                var3_3.setTransactionID((String)ApplicationInfoManager.getApplcationInfoValue("TID"));
            }
            return var3_3;
        }
        finally {
            this.setStatusInResponse(var3_3, var1_1, "02");
            var2_2.clean();
            Operation.OPERATION.releaseOperation();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenInAppResponse tokenInApp(String string, String string2) {
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenInAppResponse tokenInAppResponse = new TokenInAppResponse();
        try {
            block7 : {
                try {
                    if (OperationMode.PAYMENT.equals((Object)Operation.OPERATION.getMode())) break block7;
                    throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
                }
                catch (Exception exception) {
                    Log.e((String)"core-hceclient", (String)("::tokenInApp::catch::" + exception.getMessage()));
                    OperationStatus operationStatus2 = this.getOperationStatus(exception);
                    this.setStatusInResponse(tokenInAppResponse, operationStatus2, "09");
                    return tokenInAppResponse;
                }
            }
            if (string == null || string.trim().length() != 8 || string2 == null || Utility.isStringEmpty(string2)) {
                throw new HCEClientException(OperationStatus.INVALID_DATA);
            }
            Session session = SessionManager.getSession();
            ApplicationInfoManager.setApplcationInfoValue("UNPREDICTABLE_NUMBER", string);
            ApplicationInfoManager.setApplcationInfoValue("TRANSACTION_CONTEXT", string2);
            ((OperationDelegate)new TokenInAppDelegate()).doOperation();
            tokenInAppResponse.setPaymentPayload((String)session.getValue("INAPP_PAYLOAD", true));
        }
        catch (Throwable throwable) {
            this.setStatusInResponse(tokenInAppResponse, operationStatus, "09");
            throw throwable;
        }
        this.setStatusInResponse(tokenInAppResponse, operationStatus, "09");
        return tokenInAppResponse;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenOperationStatus tokenLCM(StateMode stateMode) {
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            block7 : {
                if (stateMode.isCanHCEClientSet()) break block7;
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            if (!OperationMode.LCM.equals((Object)Operation.OPERATION.getMode())) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            try {
                SessionManager.getSession().setValue("TOKEN_STATE", (Object)stateMode);
                ((OperationDelegate)new TokenLCMDelegate()).doOperation();
            }
            catch (Exception exception) {}
            Log.e((String)"core-hceclient", (String)("::tokenLCM::catch::" + exception.getMessage()));
            OperationStatus operationStatus2 = this.getOperationStatus(exception);
            this.setStatusInResponse(tokenOperationStatus, operationStatus2, "10");
            return tokenOperationStatus;
        }
        catch (Throwable throwable) {
            this.setStatusInResponse(tokenOperationStatus, operationStatus, "10");
            throw throwable;
        }
        this.setStatusInResponse(tokenOperationStatus, operationStatus, "10");
        return tokenOperationStatus;
    }

    @Override
    public TokenOperationStatus tokenOpen(OperationMode operationMode, String string) {
        Log.i((String)"core-hceclient", (String)"::SDK Build Version::2.0.0_Rev_1.8");
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            this.operationModeSupported(operationMode);
            Operation.OPERATION.getOperation(operationMode);
            Operation.OPERATION.setTokenRefId(string);
            OperationDelegateFactory.getOperationDelegate(operationMode).doOperation();
            return tokenOperationStatus;
        }
        catch (Exception exception) {
            Log.e((String)"core-hceclient", (String)("::tokenOpen::catch::" + exception.getMessage()));
            operationStatus = this.getOperationStatus(exception);
            Operation.OPERATION.releaseOperation();
            return tokenOperationStatus;
        }
        finally {
            this.setStatusInResponse(tokenOperationStatus, operationStatus, "01");
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenPersoResponse tokenPerso(String var1_1) {
        var2_2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var3_3 = new TokenPersoResponse();
        if (var1_1 != null) ** GOTO lbl12
        try {
            block8 : {
                try {
                    throw new HCEClientException(OperationStatus.REQUIRED_DATA_IS_NULL);
                }
                catch (Exception var7_4) {
                    Log.e((String)"core-hceclient", (String)("::tokenPerso::catch::" + var7_4.getMessage()));
                    var9_5 = this.getOperationStatus(var7_4);
                    this.setStatusInResponse(var3_3, var9_5, "03");
                    return var3_3;
                }
lbl12: // 1 sources:
                if (!OperationMode.PROVISION.equals((Object)Operation.OPERATION.getMode())) {
                    throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
                }
                var5_7 = SessionManager.getSession();
                var5_7.setValue("TOKEN_DATA", var1_1);
                new SecureChannelUnwrapDelegate().doOperation();
                if (!((Boolean)var5_7.getValue("IS_PROVISIONING_FLOW", true)).booleanValue()) break block8;
                OperationDelegateFactory.getTokenPersoDelegate().doOperation();
            }
            new TokenUpdateDelegate().doOperation();
        }
        catch (Throwable var4_6) {
            this.setStatusInResponse(var3_3, var2_2, "03");
            throw var4_6;
        }
        var3_3.setTokenDataSignature((String)var5_7.getValue("TOKEN_DATA_SIGNATURE", false));
        var3_3.setTokenConfiguration(this.prepareTokenConfiguration());
        this.setStatusInResponse(var3_3, var2_2, "03");
        return var3_3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenRefreshStatusResponse tokenRefreshStatus(long l2, String string) {
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        Session session = SessionManager.getSession();
        TokenRefreshStatusResponse tokenRefreshStatusResponse = new TokenRefreshStatusResponse();
        try {
            long l3;
            block7 : {
                block8 : {
                    if (Operation.OPERATION.getMode() == null) break block8;
                    throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
                }
                Operation.OPERATION.setTokenRefId(string);
                Operation.OPERATION.setRealTimestamp(l2);
                ((OperationDelegate)new TokenRefreshStatusDelegate()).doOperation();
                tokenRefreshStatusResponse.setMaxATC(Integer.parseInt((String)MetaDataManager.getMetaDataValue("MAX_ATC"), (int)16));
                tokenRefreshStatusResponse.setLupcCount(Integer.parseInt((String)MetaDataManager.getMetaDataValue("LUPC_COUNT")));
                tokenRefreshStatusResponse.setRefreshRequired(Boolean.valueOf((String)MetaDataManager.getMetaDataValue("REFRESH_REQUIRED")));
                if (MetaDataManager.getMetaDataValue("LUPC_REFRESH_CHECK_BACK") == null) {
                    l3 = -1L;
                    break block7;
                }
                try {
                    long l4;
                    l3 = l4 = Long.parseLong((String)MetaDataManager.getMetaDataValue("LUPC_REFRESH_CHECK_BACK"));
                }
                catch (Exception exception) {}
                exception.printStackTrace();
                Log.e((String)"core-hceclient", (String)("::tokenRefreshStatus::catch::" + exception.getMessage()));
                Log.e((String)"core-hceclient", (String)("::tokenRefreshStatus::catch::" + exception.getStackTrace().toString()));
                OperationStatus operationStatus2 = this.getOperationStatus(exception);
                this.setStatusInResponse(tokenRefreshStatusResponse, operationStatus2, "11");
                return tokenRefreshStatusResponse;
            }
            tokenRefreshStatusResponse.setLupcRefreshCheckBack(l3);
            tokenRefreshStatusResponse.setClientVersion("2.0.0");
            tokenRefreshStatusResponse.setTokenDataVersion(MetaDataManager.getMetaDataValue("TOKEN_DATA_VERSION"));
            tokenRefreshStatusResponse.setTokenState(MetaDataManager.getMetaDataValue("TOKEN_STATUS"));
            session.clean();
            Operation.OPERATION.releaseOperation();
        }
        catch (Throwable throwable) {
            this.setStatusInResponse(tokenRefreshStatusResponse, operationStatus, "11");
            throw throwable;
        }
        this.setStatusInResponse(tokenRefreshStatusResponse, operationStatus, "11");
        return tokenRefreshStatusResponse;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenOperationStatus tokenSetCDCVM(byte[] var1_1) {
        var2_2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var3_3 = new TokenOperationStatus();
        try {
            if (!OperationMode.PAYMENT.equals((Object)Operation.OPERATION.getMode()) && !OperationMode.TAP_PAYMENT.equals((Object)Operation.OPERATION.getMode())) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            ** GOTO lbl13
            {
                catch (Exception var5_4) {}
                Log.e((String)"core-hceclient", (String)("::tokenSetCDCVM::catch::" + var5_4.getMessage()));
                var7_5 = this.getOperationStatus(var5_4);
                this.setStatusInResponse(var3_3, var7_5, "08");
                return var3_3;
lbl13: // 1 sources:
                SessionManager.getSession().setValue("CDCVM_BLOB", var1_1);
                new TokenSetCDCVMDelegate().doOperation();
            }
            return var3_3;
        }
        finally {
            this.setStatusInResponse(var3_3, var2_2, "08");
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenUpdateResponse tokenUpdate(String var1_1) {
        var2_2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var3_3 = new TokenUpdateResponse();
        if (var1_1 != null) ** GOTO lbl12
        try {
            try {
                throw new HCEClientException(OperationStatus.REQUIRED_DATA_IS_NULL);
            }
            catch (Exception var7_4) {
                Log.e((String)"core-hceclient", (String)("::tokenUpdate::catch::" + var7_4.getMessage()));
                var9_5 = this.getOperationStatus(var7_4);
                this.setStatusInResponse(var3_3, var9_5, "04");
                return var3_3;
            }
lbl12: // 1 sources:
            if (!OperationMode.REFRESH.equals((Object)Operation.OPERATION.getMode())) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            var5_7 = SessionManager.getSession();
            var5_7.setValue("TOKEN_DATA", var1_1);
            new SecureChannelUnwrapDelegate().doOperation();
            new TokenUpdateDelegate().doOperation();
            var3_3.setTokenDataSignature((String)var5_7.getValue("TOKEN_DATA_SIGNATURE", false));
        }
        catch (Throwable var4_6) {
            this.setStatusInResponse(var3_3, var2_2, "04");
            throw var4_6;
        }
        this.setStatusInResponse(var3_3, var2_2, "04");
        return var3_3;
    }
}

