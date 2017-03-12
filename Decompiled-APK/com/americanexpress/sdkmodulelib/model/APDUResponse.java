package com.americanexpress.sdkmodulelib.model;

public class APDUResponse {
    private byte[] apduBytes;
    private String commandName;
    private TokenDataStatus status;

    public APDUResponse(byte[] bArr, TokenDataStatus tokenDataStatus) {
        this.apduBytes = bArr;
        this.status = tokenDataStatus;
    }

    public byte[] getApduBytes() {
        return this.apduBytes;
    }

    public void setApduBytes(byte[] bArr) {
        this.apduBytes = bArr;
    }

    public TokenDataStatus getStatus() {
        return this.status;
    }

    public void setStatus(TokenDataStatus tokenDataStatus) {
        this.status = tokenDataStatus;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String str) {
        this.commandName = str;
    }
}
