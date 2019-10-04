/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class GenerateAppCryptoApduInfo
extends APDU {
    public GenerateAppCryptoApduInfo() {
        this.setCommand(APDURequestCommand.GENAC);
    }

    private String validateEMV() {
        this.setValid(false);
        if (!("00".equals((Object)this.getParameter1()) || "40".equals((Object)this.getParameter1()) || "80".equals((Object)this.getParameter1()))) {
            return "6A86";
        }
        if (!"00".equals((Object)this.getParameter2())) {
            return "6A86";
        }
        if (this.getLengthCommandData() != null && "00".equals((Object)this.getLengthCommandData())) {
            return "6700";
        }
        if (!"1d".equalsIgnoreCase(this.getLengthCommandData())) {
            return "6700";
        }
        if (this.getLengthExpectedData() != null && !"00".equals((Object)this.getLengthExpectedData())) {
            return "6700";
        }
        if (TokenDataParser.isClientVersionUpdateRequired(this.getParsedTokenRecord())) {
            return "6986";
        }
        this.setValid(true);
        return "9000";
    }

    private String validateMAG() {
        this.setValid(false);
        if (!("00".equals((Object)this.getParameter1()) || "40".equals((Object)this.getParameter1()) || "80".equals((Object)this.getParameter1()))) {
            return "6A86";
        }
        if (!"00".equals((Object)this.getParameter2())) {
            return "6A86";
        }
        if (this.getLengthCommandData() != null && "00".equals((Object)this.getLengthCommandData())) {
            return "6700";
        }
        if (!"04".equals((Object)this.getLengthCommandData())) {
            return "6700";
        }
        if (this.getLengthExpectedData() != null && !"00".equals((Object)this.getLengthExpectedData())) {
            return "6700";
        }
        if (TokenDataParser.isClientVersionUpdateRequired(this.getParsedTokenRecord())) {
            return "6986";
        }
        this.setValid(true);
        return "9000";
    }

    @Override
    public APDUResponse buildResponse() {
        return null;
    }

    public String getRandomNumber() {
        return this.getData();
    }

    @Override
    public String validate() {
        try {
            if (SessionManager.getSession().isEMVTransaction()) {
                return this.validateEMV();
            }
            String string = this.validateMAG();
            return string;
        }
        catch (Exception exception) {
            this.setValid(false);
            return "6985";
        }
    }
}

