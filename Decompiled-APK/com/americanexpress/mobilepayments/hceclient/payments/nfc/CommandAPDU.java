package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class CommandAPDU {
    public static byte CASE_1;
    public static byte CASE_2;
    public static byte CASE_3;
    public static byte CASE_4;
    private byte bCLA;
    private byte bCommandCase;
    private byte bINS;
    private byte bLc;
    private byte bLe;
    private byte bP1;
    private byte bP2;
    private byte[] baCData;
    private byte[] baFullApdu;

    static {
        CASE_1 = (byte) 1;
        CASE_2 = (byte) 2;
        CASE_3 = (byte) 3;
        CASE_4 = (byte) 4;
    }

    public CommandAPDU(byte[] bArr, TokenAPDUResponse tokenAPDUResponse) {
        if (bArr == null || bArr.length < 4) {
            tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            throw new HCEClientException("Invalid APDU");
        }
        this.baFullApdu = bArr;
        short s = (short) 1;
        this.bCLA = bArr[0];
        short s2 = (short) (s + 1);
        this.bINS = bArr[s];
        s = (short) (s2 + 1);
        this.bP1 = bArr[s2];
        s2 = (short) (s + 1);
        this.bP2 = bArr[s];
        this.bCommandCase = CASE_1;
        if (bArr.length > 5) {
            this.bCommandCase = CASE_3;
            s = (short) (s2 + 1);
            this.bLc = bArr[s2];
            if (bArr.length == this.bLc + 5 || bArr.length == this.bLc + 6) {
                this.baCData = new byte[this.bLc];
                System.arraycopy(bArr, s, this.baCData, 0, this.bLc);
                if (bArr.length > this.bLc + 5) {
                    this.bCommandCase = CASE_4;
                    this.bLe = bArr[bArr.length - 1];
                    return;
                }
                return;
            }
            tokenAPDUResponse.setsSW(ISO7816.SW_WRONG_LENGTH);
            throw new HCEClientException("Invalid APDU");
        } else if (bArr.length == 5) {
            this.bCommandCase = CASE_2;
            s = (short) (s2 + 1);
            this.bLe = bArr[s2];
        }
    }

    public byte[] getBaFullApdu() {
        return this.baFullApdu;
    }

    public byte getbCLA() {
        return this.bCLA;
    }

    public byte getbINS() {
        return this.bINS;
    }

    public byte getbP1() {
        return this.bP1;
    }

    public byte getbP2() {
        return this.bP2;
    }

    public byte getbCommandCase() {
        return this.bCommandCase;
    }

    public byte getbLc() {
        return this.bLc;
    }

    public byte getbLe() {
        return this.bLe;
    }

    public byte[] getBaCData() {
        return this.baCData;
    }

    public CommandSet classifier(TokenAPDUResponse tokenAPDUResponse) {
        tokenAPDUResponse.setsSW(ISO7816.SW_CLA_NOT_SUPPORTED);
        for (CommandSet commandSet : CommandSet.values()) {
            if (this.bCLA == commandSet.getbCLA()) {
                tokenAPDUResponse.setsSW(ISO7816.SW_INS_NOT_SUPPORTED);
                if (this.bINS == commandSet.getbINS()) {
                    tokenAPDUResponse.setsSW(ISO7816.SW_NO_ERROR);
                    return commandSet;
                }
            }
        }
        return null;
    }

    public static CommandAPDU getSessionInstance() {
        return (CommandAPDU) SessionManager.getSession().getValue(SessionConstants.COMMAND_APDU, false);
    }

    public String toString() {
        return "CommandAPDU{bCLA='" + this.bCLA + '\'' + ", bINS='" + this.bINS + '\'' + ", bP1='" + this.bP1 + '\'' + ", bP2='" + this.bP2 + '\'' + ", bLc='" + this.bLc + '\'' + ", baCdata='" + this.baCData + '\'' + ", bLe='" + this.bLe + '\'' + '}';
    }
}
