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
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.tlv.Util;

public abstract class APDU {
    private String apduClass;
    private String apduClassAndInstruction;
    private String apduCommand;
    private APDURequestCommand command = null;
    private String data;
    private int dataLength = 0;
    private String instruction;
    private boolean isValid = false;
    private String lengthCommandData;
    private String lengthExpectedData;
    private String parameter1;
    private String parameter2;
    private String statusWord;

    public abstract APDUResponse buildResponse();

    public String getApduClass() {
        return this.apduClass;
    }

    public String getApduClassAndInstruction() {
        return this.apduClassAndInstruction;
    }

    public String getApduCommand() {
        return this.apduCommand;
    }

    public APDURequestCommand getCommand() {
        return this.command;
    }

    public String getData() {
        return this.data;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getLengthCommandData() {
        return this.lengthCommandData;
    }

    public int getLengthCommandDataDecimal() {
        int n2;
        block3 : {
            int n3;
            try {
                String string = this.lengthCommandData;
                n2 = 0;
                if (string == null) break block3;
            }
            catch (Exception exception) {
                return 0;
            }
            n2 = n3 = Util.byteArrayToInt(Util.fromHexString(this.lengthCommandData));
        }
        return n2;
    }

    public String getLengthExpectedData() {
        return this.lengthExpectedData;
    }

    public String getParameter1() {
        return this.parameter1;
    }

    public String getParameter2() {
        return this.parameter2;
    }

    public ParsedTokenRecord getParsedTokenRecord() {
        return SessionManager.getSession().getParsedTokenRecord();
    }

    public String getStatusWord() {
        return this.statusWord;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setApduClass(String string) {
        this.apduClass = string;
    }

    public void setApduClassAndInstruction(String string) {
        this.apduClassAndInstruction = string;
    }

    public void setApduCommand(String string) {
        this.apduCommand = string;
    }

    public void setCommand(APDURequestCommand aPDURequestCommand) {
        this.command = aPDURequestCommand;
    }

    public void setData(String string) {
        this.data = string;
    }

    public void setDataLength(int n2) {
        this.dataLength = n2;
    }

    public void setInstruction(String string) {
        this.instruction = string;
    }

    public void setLengthCommandData(String string) {
        this.lengthCommandData = string;
    }

    public void setLengthExpectedData(String string) {
        this.lengthExpectedData = string;
    }

    public void setParameter1(String string) {
        this.parameter1 = string;
    }

    public void setParameter2(String string) {
        this.parameter2 = string;
    }

    public void setStatusWord(String string) {
        this.statusWord = string;
    }

    public void setValid(boolean bl) {
        this.isValid = bl;
    }

    public abstract String validate();
}

