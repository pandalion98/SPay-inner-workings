/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class APDUResponse {
    private byte[] apduBytes;
    private String commandName;
    private TokenDataStatus status;

    public APDUResponse() {
    }

    public APDUResponse(byte[] arrby, TokenDataStatus tokenDataStatus) {
        this.apduBytes = arrby;
        this.status = tokenDataStatus;
    }

    public byte[] getApduBytes() {
        return this.apduBytes;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public TokenDataStatus getStatus() {
        return this.status;
    }

    public void setApduBytes(byte[] arrby) {
        this.apduBytes = arrby;
    }

    public void setCommandName(String string) {
        this.commandName = string;
    }

    public void setStatus(TokenDataStatus tokenDataStatus) {
        this.status = tokenDataStatus;
    }
}

