package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.apdu.APDUCommandFactory;
import com.americanexpress.sdkmodulelib.util.APDUConstants;

public class CommandInfo {
    private APDU apdu;
    private String apduCommand;
    private byte[] apduData;
    private boolean isValid;
    private String statusWord;

    public CommandInfo(byte[] bArr) {
        this.apduData = bArr;
        this.apduCommand = new String(bArr);
        init();
    }

    private void init() {
        try {
            this.apdu = APDUCommandFactory.createAPDU(new String(this.apduData).toUpperCase());
            this.isValid = this.apdu.isValid();
            this.statusWord = this.apdu.getStatusWord();
        } catch (Exception e) {
            this.isValid = false;
            this.statusWord = APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE;
        }
    }

    public byte[] getApduData() {
        return this.apduData;
    }

    public void setApduData(byte[] bArr) {
        this.apduData = bArr;
    }

    public String getApduCommand() {
        return this.apduCommand;
    }

    public void setApduCommand(String str) {
        this.apduCommand = str;
    }

    public APDU getApdu() {
        return this.apdu;
    }

    public void setApdu(APDU apdu) {
        this.apdu = apdu;
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
}
