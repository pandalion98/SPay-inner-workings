package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.apdu.APDUCommandProcessorFactory;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoEmv;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoMag;
import com.americanexpress.sdkmodulelib.storage.StorageFactory;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.AxpeLogUtils;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TAErrorUtils;
import com.americanexpress.sdkmodulelib.util.TIDUtil;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.crypto.macs.SkeinMac;

public class NFCPaymentProviderProxyImpl implements NFCPaymentProviderProxy {
    public TokenDataStatus startTransaction(String str, int i, int i2, String str2) {
        AxpeLogUtils.log("authenticationPerformed=" + i);
        AxpeLogUtils.log("authenticationType=" + i2);
        try {
            SessionManager.cleanSession();
            TokenDataRecord loadFromStorage = TokenDataParser.loadFromStorage(str);
            String decryptTokenData = TrustedAppFactory.getTrustedApp().decryptTokenData(loadFromStorage.getApduBlob());
            ParsedTokenRecord parseToken = TokenDataParser.parseToken((decryptTokenData + TrustedAppFactory.getTrustedApp().decryptTokenData(loadFromStorage.getMetaDataBlob())).toUpperCase());
            TokenDataStatus validateTokenData = validateTokenData(parseToken);
            if (!ErrorConstants.START_TRANS_SUCCESS[0].equals(validateTokenData.getReasonCode())) {
                return validateTokenData;
            }
            SessionManager.createSession().setParsedTokenRecord(parseToken);
            SessionManager.getSession().setAuthenticationPerformed(i);
            SessionManager.getSession().setAuthenticationType(i2);
            return validateTokenData;
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_GENERIC_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public APDUResponse generateAPDU(byte[] bArr) {
        Throwable th;
        SessionManager.getSession().setIsProcessOther(false);
        APDUResponse aPDUResponse = null;
        if (bArr == null || bArr.length == 0) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        String byteArrayToHexString = Util.byteArrayToHexString(bArr);
        AxpeLogUtils.log("* Apdu Request=" + byteArrayToHexString);
        byte[] bytes = byteArrayToHexString.getBytes();
        if (bytes == null || bytes.length > SkeinMac.SKEIN_1024) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        APDUResponse apduResponse;
        try {
            CommandInfo commandInfo = new CommandInfo(bytes);
            if (commandInfo.isValid()) {
                apduResponse = APDUCommandProcessorFactory.getCommandProcessor(commandInfo).getApduResponse(commandInfo, SessionManager.getSession().getParsedTokenRecord(), SessionManager.getSession());
                if (apduResponse == null) {
                    apduResponse = new APDUResponse();
                    apduResponse.setApduBytes(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE));
                    apduResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
                }
                AxpeLogUtils.log("* Apdu Response=" + Util.byteArrayToHexString(apduResponse.getApduBytes()));
                return apduResponse;
            }
            apduResponse = new APDUResponse(Util.fromHexString(commandInfo.getStatusWord().toUpperCase()), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            if (apduResponse != null) {
                return apduResponse;
            }
            aPDUResponse = new APDUResponse();
            aPDUResponse.setApduBytes(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE));
            aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            return apduResponse;
        } catch (Exception e) {
            AxpeLogUtils.log(e.getMessage());
            AxpeLogUtils.log(e.toString());
            if (null == null) {
                apduResponse = new APDUResponse();
            } else {
                apduResponse = null;
            }
            apduResponse.setApduBytes(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE));
            apduResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            if (apduResponse == null) {
                apduResponse = new APDUResponse();
                apduResponse.setApduBytes(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE));
                apduResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            aPDUResponse = apduResponse;
            th = th3;
            if (aPDUResponse == null) {
                aPDUResponse = new APDUResponse();
                aPDUResponse.setApduBytes(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE));
                aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            }
            throw th;
        }
    }

    public TokenDataStatus processOther() {
        try {
            Session session = SessionManager.getSession();
            if (!session.isProcessTransactionComplete()) {
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(2, session.getTokenDataRecord()));
                session.setProcessTransactionComplete(true);
            }
            TokenDataStatus buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_RESPONSE);
            SessionManager.getSession().setIsProcessOther(true);
            SessionManager.getSession().setIsMSTInvokedAtleastOnce(true);
            return buildTokenDataStatus;
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public ProcessInAppPaymentResponse processInAppPayment(Object obj, String str) {
        TokenDataStatus buildTokenDataStatus;
        ProcessInAppPaymentResponse processInAppPaymentResponse = new ProcessInAppPaymentResponse();
        try {
            Session session = SessionManager.getSession();
            SessionManager.getSession().setIsInAppTransaction(true);
            if (!session.isProcessTransactionComplete()) {
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(3, session.getTokenDataRecord()));
                session.setProcessTransactionComplete(true);
            }
            TokenDataRecord generateInAppPaymentPayload = TrustedAppFactory.getTrustedApp().generateInAppPaymentPayload(obj, str, session.getTokenDataRecord());
            session.setTokenDataRecord(generateInAppPaymentPayload);
            processInAppPaymentResponse.setPaymentPayload(generateInAppPaymentPayload.getPaymentPayload());
            session.setIsInAppTransactionSuccess(true);
            buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_RESPONSE);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_FAILURE);
            } else if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            } else {
                buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_TA_FAILURE, TAErrorUtils.getErrorCode(e));
            }
        }
        processInAppPaymentResponse.setTokenDataStatus(buildTokenDataStatus);
        return processInAppPaymentResponse;
    }

    public EndTransactionResponse endTransaction() {
        EndTransactionResponse endTransactionResponse = new EndTransactionResponse();
        Session session = SessionManager.getSession();
        if (session == null) {
            endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_SESSION_FAILURE));
        } else if (session.getTokenDataRecord() == null) {
            endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_SESSION_FAILURE));
            try {
                TrustedAppFactory.getTrustedApp().clearLUPC();
            } catch (Exception e) {
            }
            SessionManager.cleanSession();
            StorageFactory.closeStorageConnection();
        } else if (!session.isInAppTransaction() || session.isInAppTransactionSuccess()) {
            try {
                TokenDataStatus buildTokenDataStatus;
                if (session.checkWorkflowCompleted()) {
                    buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_SUCCESS);
                    endTransactionResponse.setLupcMetaDataBlob(session.getTokenDataRecord().getLupcMetadataBlob());
                    try {
                        calculateTID(endTransactionResponse);
                    } catch (Exception e2) {
                        buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_PARTIAL_SUCCESS);
                    }
                    endTransactionResponse.setTokenDataStatus(buildTokenDataStatus);
                    StorageFactory.getStorageManager().save(session.getTokenDataRecord());
                    TrustedAppFactory.getTrustedApp().clearLUPC();
                    try {
                        TrustedAppFactory.getTrustedApp().clearLUPC();
                    } catch (Exception e3) {
                    }
                    SessionManager.cleanSession();
                    StorageFactory.closeStorageConnection();
                } else {
                    buildTokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_TAP2PAY_FAILURE);
                    if (session.isMSTInvokedAtleastOnce()) {
                        endTransactionResponse.setLupcMetaDataBlob(session.getTokenDataRecord().getLupcMetadataBlob());
                        try {
                            calculateTID(endTransactionResponse);
                        } catch (Exception e4) {
                        }
                        StorageFactory.getStorageManager().save(session.getTokenDataRecord());
                        TrustedAppFactory.getTrustedApp().clearLUPC();
                    }
                    endTransactionResponse.setTokenDataStatus(buildTokenDataStatus);
                    try {
                        TrustedAppFactory.getTrustedApp().clearLUPC();
                    } catch (Exception e5) {
                    }
                    SessionManager.cleanSession();
                    StorageFactory.closeStorageConnection();
                }
            } catch (Exception e6) {
                if (!TAErrorUtils.isTAError(e6)) {
                    endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_FAILURE));
                } else if (TAErrorUtils.isTrustedAppCommunicationError(e6)) {
                    endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e6)));
                } else {
                    endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_TA_FAILURE, TAErrorUtils.getErrorCode(e6)));
                }
            } finally {
                try {
                    TrustedAppFactory.getTrustedApp().clearLUPC();
                } catch (Exception e7) {
                }
                SessionManager.cleanSession();
                StorageFactory.closeStorageConnection();
            }
        } else {
            endTransactionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.END_TRANS_SESSION_FAILURE));
            try {
                TrustedAppFactory.getTrustedApp().clearLUPC();
            } catch (Exception e8) {
            }
            SessionManager.cleanSession();
            StorageFactory.closeStorageConnection();
        }
        return endTransactionResponse;
    }

    private TokenDataStatus validateTokenData(ParsedTokenRecord parsedTokenRecord) {
        if (parsedTokenRecord.isTokenMalformed()) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TOKEN_DATA_MALFORMED_FAILURE);
        }
        if (parsedTokenRecord.isTokenPartiallyMalformed()) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_PARTIAL_TOKEN_DATA_MALFORMED_FAILURE);
        }
        if (TokenDataParser.isClientVersionUpdateRequired(parsedTokenRecord)) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_CLIENT_VERSION_FAILURE);
        }
        if (TokenDataParser.isUpdateTokenData(parsedTokenRecord)) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TOKEN_VERSION_FAILURE);
        }
        return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_SUCCESS);
    }

    private void calculateTID(EndTransactionResponse endTransactionResponse) {
        try {
            Session session = SessionManager.getSession();
            if (session.isInAppTransaction()) {
                String inAppTID = session.getTokenDataRecord().getInAppTID();
                endTransactionResponse.setInAppTransactionId(inAppTID);
                if (inAppTID == null || BuildConfig.FLAVOR.equals(inAppTID)) {
                    throw new Exception("Failed to calculate TID");
                }
                return;
            }
            String accountRefNumber;
            if (session.isEMVTransaction()) {
                accountRefNumber = ((Track2InfoEmv) session.getParsedTokenRecord().getDataGroups().get(Track2InfoEmv.class)).getAccountRefNumber();
            } else {
                accountRefNumber = ((Track2InfoMag) session.getParsedTokenRecord().getDataGroups().get(Track2InfoMag.class)).getAccountRefNumber();
            }
            String deviceId = ((TokenMetaInfoParser) session.getParsedTokenRecord().getDataGroups().get(TokenMetaInfoParser.class)).getDeviceId();
            String nfcUnpredictableNumber = session.getTokenDataRecord().getNfcUnpredictableNumber();
            String nfcCryptogram = session.getTokenDataRecord().getNfcCryptogram();
            String otherTID = session.getTokenDataRecord().getOtherTID();
            endTransactionResponse.setOtherTransactionId(otherTID);
            Object obj = null;
            if (!(nfcUnpredictableNumber == null || nfcCryptogram == null)) {
                obj = TIDUtil.generateHash(session.isEMVTransaction(), accountRefNumber, deviceId, nfcUnpredictableNumber, nfcCryptogram);
                endTransactionResponse.setNfcTransactionId(obj);
            }
            if (obj != null && !BuildConfig.FLAVOR.equals(obj)) {
                return;
            }
            if (otherTID == null || BuildConfig.FLAVOR.equals(otherTID)) {
                throw new Exception("Failed to calculate TID");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
