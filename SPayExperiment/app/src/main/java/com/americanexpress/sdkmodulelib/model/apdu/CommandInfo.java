/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.apdu.APDUCommandFactory;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;

public class CommandInfo {
    private APDU apdu;
    private String apduCommand;
    private byte[] apduData;
    private boolean isValid;
    private String statusWord;

    public CommandInfo(byte[] arrby) {
        this.apduData = arrby;
        this.apduCommand = new String(arrby);
        this.init();
    }

    private void init() {
        try {
            this.apdu = APDUCommandFactory.createAPDU(new String(this.apduData).toUpperCase());
            this.isValid = this.apdu.isValid();
            this.statusWord = this.apdu.getStatusWord();
            return;
        }
        catch (Exception exception) {
            this.isValid = false;
            this.statusWord = "6985";
            return;
        }
    }

    public APDU getApdu() {
        return this.apdu;
    }

    public String getApduCommand() {
        return this.apduCommand;
    }

    public byte[] getApduData() {
        return this.apduData;
    }

    public String getStatusWord() {
        return this.statusWord;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setApdu(APDU aPDU) {
        this.apdu = aPDU;
    }

    public void setApduCommand(String string) {
        this.apduCommand = string;
    }

    public void setApduData(byte[] arrby) {
        this.apduData = arrby;
    }

    public void setStatusWord(String string) {
        this.statusWord = string;
    }

    public void setValid(boolean bl) {
        this.isValid = bl;
    }
}

