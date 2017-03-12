package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenDataVersionResponse;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.storage.StorageFactory;
import com.americanexpress.sdkmodulelib.storage.StorageManager;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TAErrorUtils;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;

public class TokenDataManagerImpl implements TokenDataManager {
    public TokenDataStatus updateTokenData(String str, String str2, String str3, String str4, String str5, String str6) {
        try {
            StorageManager storageManager = StorageFactory.getStorageManager();
            TokenDataRecord tokenDataRecord = new TokenDataRecord();
            tokenDataRecord.setTokenRefId(str);
            tokenDataRecord.setApduBlob(str2);
            tokenDataRecord.setNfcLUPCBlob(str3);
            tokenDataRecord.setOtherLUPCBlob(str4);
            tokenDataRecord.setMetaDataBlob(str5);
            tokenDataRecord.setLupcMetadataBlob(str6);
            storageManager.save(tokenDataRecord);
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.UPDATE_DETAIL_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public TokenDataStatus deleteToken(String str) {
        try {
            StorageFactory.getStorageManager().delete(str);
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.DELETE_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public TokenDataStatus suspendToken(String str) {
        try {
            TokenDataRecord fetch = StorageFactory.getStorageManager().fetch(str);
            fetch.setMetaDataBlob(TrustedAppFactory.getTrustedApp().suspendToken(fetch.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(fetch);
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.SUSPEND_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public TokenDataStatus resumeToken(String str) {
        try {
            TokenDataRecord fetch = StorageFactory.getStorageManager().fetch(str);
            fetch.setMetaDataBlob(TrustedAppFactory.getTrustedApp().resumeToken(fetch.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(fetch);
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.RESUME_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public TokenDataStatus activateToken(String str) {
        try {
            TokenDataRecord fetch = StorageFactory.getStorageManager().fetch(str);
            fetch.setMetaDataBlob(TrustedAppFactory.getTrustedApp().activateToken(fetch.getMetaDataBlob()));
            StorageFactory.getStorageManager().save(fetch);
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_FAILURE);
            }
            if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.ACTIVATE_TOKEN_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e));
        }
    }

    public TokenStatusResponse getTokenStatus(String str) {
        TokenStatusResponse tokenStatusResponse = new TokenStatusResponse();
        try {
            String decryptTokenData = TrustedAppFactory.getTrustedApp().decryptTokenData(StorageFactory.getStorageManager().fetch(str).getMetaDataBlob());
            TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
            tokenMetaInfoParser.init(decryptTokenData);
            decryptTokenData = tokenMetaInfoParser.getTokenStatus();
            tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_SUCCESS));
            tokenStatusResponse.setTokenStatus(decryptTokenData);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_FAILURE));
            } else if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e)));
            } else {
                tokenStatusResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_STATUS_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e)));
            }
        }
        return tokenStatusResponse;
    }

    public TokenDataVersionResponse getTokenDataVersion(String str) {
        TokenDataVersionResponse tokenDataVersionResponse = new TokenDataVersionResponse();
        try {
            String decryptTokenData = TrustedAppFactory.getTrustedApp().decryptTokenData(StorageFactory.getStorageManager().fetch(str).getMetaDataBlob());
            TokenMetaInfoParser tokenMetaInfoParser = new TokenMetaInfoParser();
            tokenMetaInfoParser.init(decryptTokenData);
            decryptTokenData = tokenMetaInfoParser.getTokenDataVersion();
            tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_SUCCESS));
            tokenDataVersionResponse.setTokenDataVersion(decryptTokenData);
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_FAILURE));
            } else if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e)));
            } else {
                tokenDataVersionResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_VERSION_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e)));
            }
        }
        return tokenDataVersionResponse;
    }

    public TokenDataResponse getTokenData(String str) {
        TokenDataResponse tokenDataResponse = new TokenDataResponse();
        try {
            TokenDataRecord fetch = StorageFactory.getStorageManager().fetch(str);
            if (fetch.getApduBlob() == null || fetch.getMetaDataBlob() == null || fetch.getNfcLUPCBlob() == null || fetch.getOtherLUPCBlob() == null || fetch.getLupcMetadataBlob() == null) {
                throw new Exception("Storage failure, missing blob");
            }
            tokenDataResponse.setApduBlob(fetch.getApduBlob());
            tokenDataResponse.setNfcLUPCBlob(fetch.getNfcLUPCBlob());
            tokenDataResponse.setOtherLUPCBlob(fetch.getOtherLUPCBlob());
            tokenDataResponse.setMetaDataBlob(fetch.getMetaDataBlob());
            tokenDataResponse.setLupcMetadataBlob(fetch.getLupcMetadataBlob());
            tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_SUCCESS));
            return tokenDataResponse;
        } catch (Exception e) {
            if (!TAErrorUtils.isTAError(e)) {
                tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_FAILURE));
            } else if (TAErrorUtils.isTrustedAppCommunicationError(e)) {
                tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(e)));
            } else {
                tokenDataResponse.setTokenDataStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GET_TOKEN_DATA_MESSAGE_TA_FAILURE, TAErrorUtils.getErrorCode(e)));
            }
        }
    }

    public String getClientVersion() {
        return "1.10";
    }
}
