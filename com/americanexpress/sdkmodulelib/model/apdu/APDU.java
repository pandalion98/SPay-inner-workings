package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.tlv.Util;

public abstract class APDU {
    private String apduClass;
    private String apduClassAndInstruction;
    private String apduCommand;
    private APDURequestCommand command;
    private String data;
    private int dataLength;
    private String instruction;
    private boolean isValid;
    private String lengthCommandData;
    private String lengthExpectedData;
    private String parameter1;
    private String parameter2;
    private String statusWord;

    public abstract APDUResponse buildResponse();

    public abstract String validate();

    public APDU() {
        this.dataLength = 0;
        this.isValid = false;
        this.command = null;
    }

    public String getApduCommand() {
        return this.apduCommand;
    }

    public void setApduCommand(String str) {
        this.apduCommand = str;
    }

    public String getApduClass() {
        return this.apduClass;
    }

    public void setApduClass(String str) {
        this.apduClass = str;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getApduClassAndInstruction() {
        return this.apduClassAndInstruction;
    }

    public void setApduClassAndInstruction(String str) {
        this.apduClassAndInstruction = str;
    }

    public void setInstruction(String str) {
        this.instruction = str;
    }

    public String getParameter1() {
        return this.parameter1;
    }

    public void setParameter1(String str) {
        this.parameter1 = str;
    }

    public String getParameter2() {
        return this.parameter2;
    }

    public void setParameter2(String str) {
        this.parameter2 = str;
    }

    public String getLengthCommandData() {
        return this.lengthCommandData;
    }

    public void setLengthCommandData(String str) {
        this.lengthCommandData = str;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public void setDataLength(int i) {
        this.dataLength = i;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }

    public String getLengthExpectedData() {
        return this.lengthExpectedData;
    }

    public void setLengthExpectedData(String str) {
        this.lengthExpectedData = str;
    }

    public String getStatusWord() {
        return this.statusWord;
    }

    public void setStatusWord(String str) {
        this.statusWord = str;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean z) {
        this.isValid = z;
    }

    public APDURequestCommand getCommand() {
        return this.command;
    }

    public void setCommand(APDURequestCommand aPDURequestCommand) {
        this.command = aPDURequestCommand;
    }

    public ParsedTokenRecord getParsedTokenRecord() {
        return SessionManager.getSession().getParsedTokenRecord();
    }

    public int getLengthCommandDataDecimal() {
        int i = 0;
        try {
            if (this.lengthCommandData != null) {
                i = Util.byteArrayToInt(Util.fromHexString(this.lengthCommandData));
            }
        } catch (Exception e) {
        }
        return i;
    }
}
