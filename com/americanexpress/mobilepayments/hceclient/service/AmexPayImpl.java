package com.americanexpress.mobilepayments.hceclient.service;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
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
import com.americanexpress.mobilepayments.hceclient.payments.nfc.EMVConstants;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.data.TransactionStatus;

public class AmexPayImpl implements AmexPay {
    public TokenOperationStatus tokenOpen(OperationMode operationMode, String str) {
        Log.i(HCEClientConstants.TAG, "::SDK Build Version::2.0.0_Rev_1.8");
        String str2 = HCEClientConstants.API_INDEX_TOKEN_OPEN;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            operationModeSupported(operationMode);
            Operation.OPERATION.getOperation(operationMode);
            Operation.OPERATION.setTokenRefId(str);
            OperationDelegateFactory.getOperationDelegate(operationMode).doOperation();
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenOpen::catch::" + e.getMessage());
            operationStatus = getOperationStatus(e);
            Operation.OPERATION.releaseOperation();
        } finally {
            setStatusInResponse(tokenOperationStatus, operationStatus, HCEClientConstants.API_INDEX_TOKEN_OPEN);
        }
        return tokenOperationStatus;
    }

    public TokenCloseResponse tokenClose() {
        String str = Constants.SERVICE_CODE_LENGTH;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        Session session = SessionManager.getSession();
        TokenOperationStatus tokenCloseResponse = new TokenCloseResponse();
        try {
            if (Operation.OPERATION.getMode() == null) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            OperationDelegateFactory.getTokenCloseDelegate().doOperation();
            tokenCloseResponse.setTransactionID((String) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TID));
            return tokenCloseResponse;
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenClose::catch::" + e.getMessage());
            OperationStatus operationStatus2 = getOperationStatus(e);
        } finally {
            String str2 = Constants.SERVICE_CODE_LENGTH;
            setStatusInResponse(tokenCloseResponse, operationStatus, str2);
            session.clean();
            String str3 = Operation.OPERATION;
            str3.releaseOperation();
            str2 = str3;
            str2 = str3;
        }
    }

    public TokenPersoResponse tokenPerso(String str) {
        String str2 = HCEClientConstants.API_INDEX_TOKEN_PERSO;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenPersoResponse = new TokenPersoResponse();
        if (str == null) {
            try {
                throw new HCEClientException(OperationStatus.REQUIRED_DATA_IS_NULL);
            } catch (Exception e) {
                Log.e(HCEClientConstants.TAG, "::tokenPerso::catch::" + e.getMessage());
                OperationStatus operationStatus2 = getOperationStatus(e);
            } finally {
                String str3 = HCEClientConstants.API_INDEX_TOKEN_PERSO;
                setStatusInResponse(tokenPersoResponse, operationStatus, r3);
                r3 = operationStatus;
                OperationStatus operationStatus3 = operationStatus;
            }
        } else if (OperationMode.PROVISION.equals(Operation.OPERATION.getMode())) {
            Session session = SessionManager.getSession();
            session.setValue(SessionConstants.TOKEN_DATA, str);
            new SecureChannelUnwrapDelegate().doOperation();
            if (((Boolean) session.getValue(SessionConstants.IS_PROVISIONING_FLOW, true)).booleanValue()) {
                OperationDelegateFactory.getTokenPersoDelegate().doOperation();
            } else {
                new TokenUpdateDelegate().doOperation();
            }
            tokenPersoResponse.setTokenDataSignature((String) session.getValue(SessionConstants.TOKEN_DATA_SIGNATURE, false));
            tokenPersoResponse.setTokenConfiguration(prepareTokenConfiguration());
            setStatusInResponse(tokenPersoResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_PERSO);
            return tokenPersoResponse;
        } else {
            throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        }
    }

    public TokenUpdateResponse tokenUpdate(String str) {
        String str2 = HCEClientConstants.API_INDEX_TOKEN_UPDATE;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenUpdateResponse = new TokenUpdateResponse();
        if (str == null) {
            try {
                throw new HCEClientException(OperationStatus.REQUIRED_DATA_IS_NULL);
            } catch (Exception e) {
                Log.e(HCEClientConstants.TAG, "::tokenUpdate::catch::" + e.getMessage());
                OperationStatus operationStatus2 = getOperationStatus(e);
            } finally {
                String str3 = HCEClientConstants.API_INDEX_TOKEN_UPDATE;
                setStatusInResponse(tokenUpdateResponse, operationStatus, r3);
                r3 = operationStatus;
                OperationStatus operationStatus3 = operationStatus;
            }
        } else if (OperationMode.REFRESH.equals(Operation.OPERATION.getMode())) {
            Session session = SessionManager.getSession();
            session.setValue(SessionConstants.TOKEN_DATA, str);
            new SecureChannelUnwrapDelegate().doOperation();
            new TokenUpdateDelegate().doOperation();
            tokenUpdateResponse.setTokenDataSignature((String) session.getValue(SessionConstants.TOKEN_DATA_SIGNATURE, false));
            setStatusInResponse(tokenUpdateResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_UPDATE);
            return tokenUpdateResponse;
        } else {
            throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        }
    }

    public TokenChannelInitializeResponse tokenChannelInitialize(byte[] bArr) {
        String str = HCEClientConstants.API_INDEX_TOKEN_CHANNEL_INIT;
        String str2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenChannelInitializeResponse = new TokenChannelInitializeResponse();
        try {
            Session session = SessionManager.getSession();
            session.setValue(HCEClientConstants.CHANNEL_PARAM, bArr);
            new TokenChannelInitializeDelegate().doOperation();
            tokenChannelInitializeResponse.setEphemeralData((String) session.getValue(SessionConstants.DEVICE_PUBLIC_KEY, false));
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenChannelInitialize::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str3 = HCEClientConstants.API_INDEX_TOKEN_CHANNEL_INIT;
            setStatusInResponse(tokenChannelInitializeResponse, str2, str3);
            str3 = str2;
            str3 = str2;
        }
        return tokenChannelInitializeResponse;
    }

    public TokenChannelUpdateResponse tokenChannelUpdate(byte[] bArr) {
        String str = HCEClientConstants.API_INDEX_TOKEN_CHANNEL_UPDATE;
        String str2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenChannelUpdateResponse = new TokenChannelUpdateResponse();
        try {
            SessionManager.getSession().setValue(SessionConstants.CERTIFICATE, bArr);
            new TokenChannelUpdateDelegate().doOperation();
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenChannelUpdate::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str3 = HCEClientConstants.API_INDEX_TOKEN_CHANNEL_UPDATE;
            setStatusInResponse(tokenChannelUpdateResponse, str2, str3);
            str3 = str2;
            str3 = str2;
        }
        return tokenChannelUpdateResponse;
    }

    public TokenRefreshStatusResponse tokenRefreshStatus(long j, String str) {
        String str2 = HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        Session session = SessionManager.getSession();
        TokenOperationStatus tokenRefreshStatusResponse = new TokenRefreshStatusResponse();
        try {
            if (Operation.OPERATION.getMode() != null) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            long j2;
            Operation.OPERATION.setTokenRefId(str);
            Operation.OPERATION.setRealTimestamp(j);
            new TokenRefreshStatusDelegate().doOperation();
            tokenRefreshStatusResponse.setMaxATC(Integer.parseInt(MetaDataManager.getMetaDataValue(MetaDataManager.MAX_ATC), 16));
            tokenRefreshStatusResponse.setLupcCount(Integer.parseInt(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_COUNT)));
            tokenRefreshStatusResponse.setRefreshRequired(Boolean.valueOf(MetaDataManager.getMetaDataValue(MetaDataManager.REFRESH_REQUIRED)).booleanValue());
            if (MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_REFRESH_CHECK_BACK) == null) {
                j2 = -1;
            } else {
                j2 = Long.parseLong(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_REFRESH_CHECK_BACK));
            }
            tokenRefreshStatusResponse.setLupcRefreshCheckBack(j2);
            tokenRefreshStatusResponse.setClientVersion(HCEClientConstants.SDK_VERSION);
            tokenRefreshStatusResponse.setTokenDataVersion(MetaDataManager.getMetaDataValue(MetaDataManager.TOKEN_DATA_VERSION));
            tokenRefreshStatusResponse.setTokenState(MetaDataManager.getMetaDataValue(MetaDataManager.TOKEN_STATUS));
            session.clean();
            Operation.OPERATION.releaseOperation();
            return tokenRefreshStatusResponse;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(HCEClientConstants.TAG, "::tokenRefreshStatus::catch::" + e.getMessage());
            Log.e(HCEClientConstants.TAG, "::tokenRefreshStatus::catch::" + e.getStackTrace().toString());
            OperationStatus operationStatus2 = getOperationStatus(e);
        } finally {
            setStatusInResponse(tokenRefreshStatusResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS);
        }
    }

    public TokenAPDUResponse tokenAPDU(byte[] bArr) {
        Exception e;
        Throwable th;
        Throwable th2;
        String str = HCEClientConstants.API_INDEX_TOKEN_APDU;
        OperationStatus operationStatus = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenAPDUResponse = new TokenAPDUResponse();
        TokenOperationStatus tokenOperationStatus;
        try {
            if (bArr.length < 4) {
                throw new HCEClientException(OperationStatus.INVALID_DATA);
            } else if (Operation.OPERATION.getMode() == null || !(OperationMode.PAYMENT.equals(Operation.OPERATION.getMode()) || OperationMode.TAP_PAYMENT.equals(Operation.OPERATION.getMode()))) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else {
                Session session = SessionManager.getSession();
                session.setValue(SessionConstants.COMMAND_APDU_BYTES, bArr);
                new TokenAPDUDelegate().doOperation();
                tokenOperationStatus = (TokenAPDUResponse) session.getValue(SessionConstants.RESPONSE_APDU, true);
                try {
                    setStatusInResponse(tokenOperationStatus, (OperationStatus) session.getValue(SessionConstants.OPERATION_STATUS, false), HCEClientConstants.API_INDEX_TOKEN_APDU);
                    SessionManager.getSession().cleanAPDU();
                } catch (Exception e2) {
                    e = e2;
                    try {
                        tokenOperationStatus.setsSW(ISO7816.SW_UNKNOWN);
                        Log.e(HCEClientConstants.TAG, "::tokenAPDU::catch::" + e.getMessage());
                        setStatusInResponse(tokenOperationStatus, getOperationStatus(e), HCEClientConstants.API_INDEX_TOKEN_APDU);
                        SessionManager.getSession().cleanAPDU();
                        return r0;
                    } catch (Throwable th3) {
                        th = th3;
                        tokenAPDUResponse = tokenOperationStatus;
                        th2 = th;
                        setStatusInResponse(tokenAPDUResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_APDU);
                        SessionManager.getSession().cleanAPDU();
                        throw th2;
                    }
                } catch (Throwable th32) {
                    th = th32;
                    tokenAPDUResponse = tokenOperationStatus;
                    th2 = th;
                    setStatusInResponse(tokenAPDUResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_APDU);
                    SessionManager.getSession().cleanAPDU();
                    throw th2;
                }
                return r0;
            }
        } catch (Exception e3) {
            Exception exception = e3;
            tokenOperationStatus = tokenAPDUResponse;
            e = exception;
            tokenOperationStatus.setsSW(ISO7816.SW_UNKNOWN);
            Log.e(HCEClientConstants.TAG, "::tokenAPDU::catch::" + e.getMessage());
            setStatusInResponse(tokenOperationStatus, getOperationStatus(e), HCEClientConstants.API_INDEX_TOKEN_APDU);
            SessionManager.getSession().cleanAPDU();
            return r0;
        } catch (Throwable th4) {
            th2 = th4;
            setStatusInResponse(tokenAPDUResponse, operationStatus, HCEClientConstants.API_INDEX_TOKEN_APDU);
            SessionManager.getSession().cleanAPDU();
            throw th2;
        }
    }

    public TokenOperationStatus tokenSetCDCVM(byte[] bArr) {
        String str = HCEClientConstants.API_INDEX_TOKEN_SETCDCVM;
        String str2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            if (OperationMode.PAYMENT.equals(Operation.OPERATION.getMode()) || OperationMode.TAP_PAYMENT.equals(Operation.OPERATION.getMode())) {
                SessionManager.getSession().setValue(SessionConstants.CDCVM_BLOB, bArr);
                new TokenSetCDCVMDelegate().doOperation();
                return tokenOperationStatus;
            }
            throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenSetCDCVM::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str3 = HCEClientConstants.API_INDEX_TOKEN_SETCDCVM;
            setStatusInResponse(tokenOperationStatus, str2, str3);
            str3 = str2;
            str3 = str2;
        }
    }

    public TokenInAppResponse tokenInApp(String str, String str2) {
        String str3 = HCEClientConstants.API_INDEX_TOKEN_INAPP;
        String str4 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenInAppResponse = new TokenInAppResponse();
        try {
            if (OperationMode.PAYMENT.equals(Operation.OPERATION.getMode())) {
                if (str != null) {
                    if (!(str.trim().length() != 8 || str2 == null || Utility.isStringEmpty(str2))) {
                        Session session = SessionManager.getSession();
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.UNPREDICTABLE_NUMBER, str);
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TRANSACTION_CONTEXT, str2);
                        new TokenInAppDelegate().doOperation();
                        tokenInAppResponse.setPaymentPayload((String) session.getValue(SessionConstants.INAPP_PAYLOAD, true));
                        return tokenInAppResponse;
                    }
                }
                throw new HCEClientException(OperationStatus.INVALID_DATA);
            }
            throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenInApp::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str5 = HCEClientConstants.API_INDEX_TOKEN_INAPP;
            setStatusInResponse(tokenInAppResponse, str4, str5);
            str5 = str4;
            str5 = str4;
        }
    }

    public TokenOperationStatus tokenLCM(StateMode stateMode) {
        String str = Constants.CLIENT_MINOR_VERSION;
        String str2 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        TokenOperationStatus tokenOperationStatus = new TokenOperationStatus();
        try {
            if (!stateMode.isCanHCEClientSet()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else if (OperationMode.LCM.equals(Operation.OPERATION.getMode())) {
                SessionManager.getSession().setValue(SessionConstants.TOKEN_STATE, stateMode);
                new TokenLCMDelegate().doOperation();
                return tokenOperationStatus;
            } else {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::tokenLCM::catch::" + e.getMessage());
            OperationStatus operationStatus = getOperationStatus(e);
        } finally {
            String str3 = Constants.CLIENT_MINOR_VERSION;
            setStatusInResponse(tokenOperationStatus, str2, str3);
            str3 = str2;
            str3 = str2;
        }
    }

    protected void setStatusInResponse(TokenOperationStatus tokenOperationStatus, OperationStatus operationStatus, String str) {
        String str2;
        OperationStatus status = operationStatus.getStatus(str);
        tokenOperationStatus.setReasonCode(status.getReasonCode());
        tokenOperationStatus.setDetailCode(status.getDetailCode());
        String detailMessage = status.getDetailMessage();
        if (str.compareToIgnoreCase(HCEClientConstants.API_INDEX_TOKEN_APDU) == 0) {
            String str3;
            str2 = (String) ApplicationInfoManager.getApplcationInfoValue(TransactionStatus.EXTRA_MERCHANT_NAME_TAG, true);
            String str4 = (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.MERCHANT_CODE, true);
            if (com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants.MAGIC_TRUE == ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_RETURN_AMNT_IN_GAC)).shortValue()) {
                str3 = (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_AMOUNT_AUTH, true);
                if (!(str3 == null || str3.compareToIgnoreCase(BuildConfig.FLAVOR) == 0)) {
                    detailMessage = detailMessage + ",AMOUNT=" + str3;
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_RETURN_AMNT_IN_GAC, Short.valueOf(com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants.MAGIC_FALSE));
                }
            }
            str3 = detailMessage;
            if (str2 == null || str2.compareToIgnoreCase(BuildConfig.FLAVOR) == 0) {
                str2 = str3;
            } else {
                str2 = str3 + ",MERCHANT_NAME=" + str2;
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_RETURN_AMNT_IN_GAC, Short.valueOf(com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants.MAGIC_TRUE));
            }
            if (!(str4 == null || str4.compareToIgnoreCase(BuildConfig.FLAVOR) == 0)) {
                str2 = str2 + ",MERCHANT_CODE=" + str4;
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_RETURN_AMNT_IN_GAC, Short.valueOf(com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants.MAGIC_TRUE));
            }
        } else {
            str2 = detailMessage;
        }
        tokenOperationStatus.setDetailMessage(str2);
    }

    private void prepareResponse(TokenRefreshStatusResponse tokenRefreshStatusResponse) {
        long j;
        tokenRefreshStatusResponse.setMaxATC(Integer.parseInt(MetaDataManager.getMetaDataValue(MetaDataManager.MAX_ATC), 16));
        tokenRefreshStatusResponse.setLupcCount(Integer.parseInt(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_COUNT)));
        tokenRefreshStatusResponse.setRefreshRequired(Boolean.valueOf(MetaDataManager.getMetaDataValue(MetaDataManager.REFRESH_REQUIRED)).booleanValue());
        if (MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_REFRESH_CHECK_BACK) == null) {
            j = -1;
        } else {
            j = Long.parseLong(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_REFRESH_CHECK_BACK));
        }
        tokenRefreshStatusResponse.setLupcRefreshCheckBack(j);
        tokenRefreshStatusResponse.setClientVersion(HCEClientConstants.SDK_VERSION);
        tokenRefreshStatusResponse.setTokenDataVersion(MetaDataManager.getMetaDataValue(MetaDataManager.TOKEN_DATA_VERSION));
    }

    protected OperationStatus getOperationStatus(Exception exception) {
        OperationStatus operationStatus = OperationStatus.UNEXPECTED_ERROR;
        if (!(exception instanceof HCEClientException) || ((HCEClientException) exception).getOperationStatus() == null) {
            return operationStatus;
        }
        return ((HCEClientException) exception).getOperationStatus();
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

    protected void operationModeSupported(OperationMode operationMode) {
    }
}
