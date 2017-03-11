package com.americanexpress.sdkmodulelib.exception;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class TokenDataParseException extends Exception {
    private TokenDataStatus tokenDataStatus;

    public TokenDataParseException(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }
}
