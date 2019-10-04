/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandSet;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;

public class CommandAPDU {
    public static byte CASE_1 = 1;
    public static byte CASE_2 = (byte)2;
    public static byte CASE_3 = (byte)3;
    public static byte CASE_4 = (byte)4;
    private byte bCLA;
    private byte bCommandCase;
    private byte bINS;
    private byte bLc;
    private byte bLe;
    private byte bP1;
    private byte bP2;
    private byte[] baCData;
    private byte[] baFullApdu;

    public CommandAPDU() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public CommandAPDU(byte[] arrby, TokenAPDUResponse tokenAPDUResponse) {
        if (arrby == null || arrby.length < 4) {
            tokenAPDUResponse.setsSW((short)28416);
            throw new HCEClientException("Invalid APDU");
        }
        this.baFullApdu = arrby;
        short s2 = (short)(true ? 1 : 0);
        this.bCLA = arrby[0];
        short s3 = (short)(s2 + 1);
        this.bINS = arrby[s2];
        short s4 = (short)(s3 + 1);
        this.bP1 = arrby[s3];
        short s5 = (short)(s4 + 1);
        this.bP2 = arrby[s4];
        this.bCommandCase = CASE_1;
        if (arrby.length > 5) {
            this.bCommandCase = CASE_3;
            short s6 = (short)(s5 + 1);
            this.bLc = arrby[s5];
            if (arrby.length != 5 + this.bLc && arrby.length != 6 + this.bLc) {
                tokenAPDUResponse.setsSW((short)26368);
                throw new HCEClientException("Invalid APDU");
            }
            this.baCData = new byte[this.bLc];
            System.arraycopy((Object)arrby, (int)s6, (Object)this.baCData, (int)0, (int)this.bLc);
            if (arrby.length <= 5 + this.bLc) return;
            {
                this.bCommandCase = CASE_4;
                this.bLe = arrby[-1 + arrby.length];
                return;
            }
        } else {
            if (arrby.length != 5) return;
            {
                this.bCommandCase = CASE_2;
                (short)(s5 + 1);
                this.bLe = arrby[s5];
                return;
            }
        }
    }

    public static CommandAPDU getSessionInstance() {
        return (CommandAPDU)SessionManager.getSession().getValue("COMMAND_APDU", false);
    }

    public CommandSet classifier(TokenAPDUResponse tokenAPDUResponse) {
        tokenAPDUResponse.setsSW((short)28160);
        for (CommandSet commandSet : CommandSet.values()) {
            if (this.bCLA != commandSet.getbCLA()) continue;
            tokenAPDUResponse.setsSW((short)27904);
            if (this.bINS != commandSet.getbINS()) continue;
            tokenAPDUResponse.setsSW((short)-28672);
            return commandSet;
        }
        return null;
    }

    public byte[] getBaCData() {
        return this.baCData;
    }

    public byte[] getBaFullApdu() {
        return this.baFullApdu;
    }

    public byte getbCLA() {
        return this.bCLA;
    }

    public byte getbCommandCase() {
        return this.bCommandCase;
    }

    public byte getbINS() {
        return this.bINS;
    }

    public byte getbLc() {
        return this.bLc;
    }

    public byte getbLe() {
        return this.bLe;
    }

    public byte getbP1() {
        return this.bP1;
    }

    public byte getbP2() {
        return this.bP2;
    }

    public String toString() {
        return "CommandAPDU{bCLA='" + this.bCLA + '\'' + ", bINS='" + this.bINS + '\'' + ", bP1='" + this.bP1 + '\'' + ", bP2='" + this.bP2 + '\'' + ", bLc='" + this.bLc + '\'' + ", baCdata='" + this.baCData + '\'' + ", bLe='" + this.bLe + '\'' + '}';
    }
}

