/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenDataVersionResponse;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.payment.TokenDataManager;
import com.americanexpress.sdkmodulelib.storage.StorageFactory;
import com.americanexpress.sdkmodulelib.storage.StorageManager;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TAErrorUtils;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;

public class TokenDataManagerImpl
implements TokenDataManager {
    @Override
    public TokenDataStatus activateToken(String string) {
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            tokenDataRecord.setMetaDataBlob(TrustedAppFactory.getTrustedApp().activateToken(tokenDataRecord.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(tokenDataRecord);
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_SUCCESS);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_FAILURE);
        }
    }

    @Override
    public TokenDataStatus deleteToken(String string) {
        try {
            StorageFactory.getStorageManager().delete(string);
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_SUCCESS);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_FAILURE);
        }
    }

    @Override
    public String getClientVersion() {
        return "1.10";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenDataResponse getTokenData(String string) {
        TokenDataResponse tokenDataResponse;
        block4 : {
            Exception exception2;
            block5 : {
                tokenDataResponse = new TokenDataResponse();
                try {
                    TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
                    if (tokenDataRecord.getApduBlob() == null || tokenDataRecord.getMetaDataBlob() == null || tokenDataRecord.getNfcLUPCBlob() == null || tokenDataRecord.getOtherLUPCBlob() == null || tokenDataRecord.getLupcMetadataBlob() == null) {
                        throw new Exception("Storage failure, missing blob");
                    }
                    tokenDataResponse.setApduBlob(tokenDataRecord.getApduBlob());
                    tokenDataResponse.setNfcLUPCBlob(tokenDataRecord.getNfcLUPCBlob());
                    tokenDataResponse.setOtherLUPCBlob(tokenDataRecord.getOtherLUPCBlob());
                    tokenDataResponse.setMetaDataBlob(tokenDataRecord.getMetaDataBlob());
                    tokenDataResponse.setLupcMetadataBlob(tokenDataRecord.getLupcMetadataBlob());
                    tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_SUCCESS));
                    return tokenDataResponse;
                }
                catch (Exception exception2) {
                    if (!TAErrorUtils.isTAError(exception2)) break block4;
                    if (!TAErrorUtils.isTrustedAppCommunicationError(exception2)) break block5;
                    tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception2)));
                    return tokenDataResponse;
                }
            }
            tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception2)));
            return tokenDataResponse;
        }
        tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_FAILURE));
        return tokenDataResponse;
    }

    @Override
    public TokenDataVersionResponse getTokenDataVersion(String string) {
        TokenDataVersionResponse tokenDataVersionResponse = new TokenDataVersionResponse();
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            String string2 = TrustedAppFactory.getTrustedApp().decryptTokenData(tokenDataRecord.getMetaDataBlob());
            TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
            tokenMetaInfoParser.init(string2);
            String string3 = tokenMetaInfoParser.getTokenDataVersion();
            tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_SUCCESS));
            tokenDataVersionResponse.setTokenDataVersion(string3);
            return tokenDataVersionResponse;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception)));
                    return tokenDataVersionResponse;
                }
                tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception)));
                return tokenDataVersionResponse;
            }
            tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_FAILURE));
            return tokenDataVersionResponse;
        }
    }

    @Override
    public TokenStatusResponse getTokenStatus(String string) {
        TokenStatusResponse tokenStatusResponse = new TokenStatusResponse();
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            String string2 = TrustedAppFactory.getTrustedApp().decryptTokenData(tokenDataRecord.getMetaDataBlob());
            TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
            tokenMetaInfoParser.init(string2);
            String string3 = tokenMetaInfoParser.getTokenStatus();
            tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_SUCCESS));
            tokenStatusResponse.setTokenStatus(string3);
            return tokenStatusResponse;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception)));
                    return tokenStatusResponse;
                }
                tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception)));
                return tokenStatusResponse;
            }
            tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_FAILURE));
            return tokenStatusResponse;
        }
    }

    @Override
    public TokenDataStatus resumeToken(String string) {
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            tokenDataRecord.setMetaDataBlob(TrustedAppFactory.getTrustedApp().resumeToken(tokenDataRecord.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(tokenDataRecord);
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_SUCCESS);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_FAILURE);
        }
    }

    @Override
    public TokenDataStatus suspendToken(String string) {
        try {
            TokenDataRecord tokenDataRecord = StorageFactory.getStorageManager().fetch(string);
            tokenDataRecord.setMetaDataBlob(TrustedAppFactory.getTrustedApp().suspendToken(tokenDataRecord.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(tokenDataRecord);
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_SUCCESS);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_FAILURE);
        }
    }

    @Override
    public TokenDataStatus updateTokenData(String string, String string2, String string3, String string4, String string5, String string6) {
        try {
            StorageManager storageManager = StorageFactory.getStorageManager();
            TokenDataRecord tokenDataRecord = new TokenDataRecord();
            tokenDataRecord.setTokenRefId(string);
            tokenDataRecord.setApduBlob(string2);
            tokenDataRecord.setNfcLUPCBlob(string3);
            tokenDataRecord.setOtherLUPCBlob(string4);
            tokenDataRecord.setMetaDataBlob(string5);
            tokenDataRecord.setLupcMetadataBlob(string6);
            storageManager.save(tokenDataRecord);
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_SUCCESS);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_FAILURE);
        }
    }
}

