/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 *  java.nio.charset.StandardCharsets
 *  java.security.InvalidParameterException
 */
package com.visa.tainterface;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

public class a {
    private byte[] LK;
    private String LL;
    private String LM;
    private String LN;
    private String LO;
    private String LP;
    private String LQ;
    private String LR;

    public void N(byte[] arrby) {
        if (arrby.length > 256) {
            Object[] arrobject = new Object[]{256};
            throw new InvalidParameterException(String.format((String)"token data must be %d bytes long", (Object[])arrobject));
        }
        this.LK = arrby;
    }

    public void cA(String string) {
        if (string.length() != 2) {
            Object[] arrobject = new Object[]{2};
            throw new InvalidParameterException(String.format((String)"Sequence counter must be %d bytes long and of the format CC", (Object[])arrobject));
        }
        this.LO = new String(string);
    }

    public void cB(String string) {
        if (string.length() != 4) {
            Object[] arrobject = new Object[]{4};
            throw new InvalidParameterException(String.format((String)"ATC must be %d bytes long and of the format AAAA", (Object[])arrobject));
        }
        this.LP = new String(string);
    }

    public void cC(String string) {
        if (string.length() != 8) {
            Object[] arrobject = new Object[]{8};
            throw new InvalidParameterException(String.format((String)"only %d bytes reserved", (Object[])arrobject));
        }
        this.LR = new String(string);
    }

    public void cz(String string) {
        if (string.length() != 4) {
            Object[] arrobject = new Object[]{4};
            throw new InvalidParameterException(String.format((String)"token expiration data must be %d bytes long and of the format YYMM", (Object[])arrobject));
        }
        this.LL = new String(string);
    }

    public byte[] ii() {
        return this.LK;
    }

    public byte[] ij() {
        return this.LL.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] ik() {
        return this.LM.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] il() {
        return this.LN.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] im() {
        return this.LO.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] in() {
        return this.LP.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] io() {
        return this.LQ.getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] ip() {
        return this.LR.getBytes(StandardCharsets.US_ASCII);
    }

    public void setCVV(String string) {
        if (string.length() != 3) {
            Object[] arrobject = new Object[]{3};
            throw new InvalidParameterException(String.format((String)"CVV must be %d bytes long", (Object[])arrobject));
        }
        this.LQ = new String(string);
    }

    public void setServiceCode(String string) {
        if (string.length() != 3) {
            Object[] arrobject = new Object[]{3};
            throw new InvalidParameterException(String.format((String)"Service code must be %d bytes long", (Object[])arrobject));
        }
        this.LM = new String(string);
    }

    public void setTimestamp(String string) {
        if (string.length() != 4) {
            Object[] arrobject = new Object[]{4};
            throw new InvalidParameterException(String.format((String)"Timestamp must be %d bytes long and of the format HHHH", (Object[])arrobject));
        }
        this.LN = new String(string);
    }
}

