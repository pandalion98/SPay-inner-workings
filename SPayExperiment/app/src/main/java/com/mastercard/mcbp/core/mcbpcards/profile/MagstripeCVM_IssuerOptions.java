/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;

public class MagstripeCVM_IssuerOptions {
    private boolean ACK_AlwaysRequiredIfCurrencyNotProvided;
    private boolean ACK_AlwaysRequiredIfCurrencyProvided;
    private boolean ACK_AutomaticallyResetByApplication;
    private boolean ACK_PreEntryAllowed;
    private boolean PIN_AlwaysRequiredIfCurrencyNotProvided;
    private boolean PIN_AlwaysRequiredIfCurrencyProvided;
    private boolean PIN_AutomaticallyResetByApplication;
    private boolean PIN_PreEntryAllowed;

    public static byte setBit(byte by, int n2) {
        return (byte)(by | 1 << n2);
    }

    public boolean getACK_AlwaysRequiredIfCurrencyNotProvided() {
        return this.ACK_AlwaysRequiredIfCurrencyNotProvided;
    }

    public boolean getACK_AlwaysRequiredIfCurrencyProvided() {
        return this.ACK_AlwaysRequiredIfCurrencyProvided;
    }

    public boolean getACK_AutomaticallyResetByApplication() {
        return this.ACK_AutomaticallyResetByApplication;
    }

    public boolean getACK_PreEntryAllowed() {
        return this.ACK_PreEntryAllowed;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ByteArray getMPAObject() {
        byte by = Boolean.valueOf((boolean)this.getACK_AlwaysRequiredIfCurrencyProvided()) != false ? MagstripeCVM_IssuerOptions.setBit((byte)0, 7) : (byte)0;
        if (Boolean.valueOf((boolean)this.getACK_AlwaysRequiredIfCurrencyNotProvided()).booleanValue()) {
            by = MagstripeCVM_IssuerOptions.setBit(by, 6);
        }
        if (Boolean.valueOf((boolean)this.getPIN_AlwaysRequiredIfCurrencyProvided()).booleanValue()) {
            by = MagstripeCVM_IssuerOptions.setBit(by, 4);
        }
        if (Boolean.valueOf((boolean)this.getPIN_AlwaysRequiredIfCurrencyNotProvided()).booleanValue()) {
            by = MagstripeCVM_IssuerOptions.setBit(by, 3);
        }
        return new DefaultByteArrayImpl(new byte[]{by});
    }

    public boolean getPIN_AlwaysRequiredIfCurrencyNotProvided() {
        return this.PIN_AlwaysRequiredIfCurrencyNotProvided;
    }

    public boolean getPIN_AlwaysRequiredIfCurrencyProvided() {
        return this.PIN_AlwaysRequiredIfCurrencyProvided;
    }

    public boolean getPIN_AutomaticallyResetByApplication() {
        return this.PIN_AutomaticallyResetByApplication;
    }

    public boolean getPIN_PreEntryAllowed() {
        return this.PIN_PreEntryAllowed;
    }

    public void setACK_AlwaysRequiredIfCurrencyNotProvided(boolean bl) {
        this.ACK_AlwaysRequiredIfCurrencyNotProvided = bl;
    }

    public void setACK_AlwaysRequiredIfCurrencyProvided(boolean bl) {
        this.ACK_AlwaysRequiredIfCurrencyProvided = bl;
    }

    public void setACK_AutomaticallyResetByApplication(boolean bl) {
        this.ACK_AutomaticallyResetByApplication = bl;
    }

    public void setACK_PreEntryAllowed(boolean bl) {
        this.ACK_PreEntryAllowed = bl;
    }

    public void setPIN_AlwaysRequiredIfCurrencyNotProvided(boolean bl) {
        this.PIN_AlwaysRequiredIfCurrencyNotProvided = bl;
    }

    public void setPIN_AlwaysRequiredIfCurrencyProvided(boolean bl) {
        this.PIN_AlwaysRequiredIfCurrencyProvided = bl;
    }

    public void setPIN_AutomaticallyResetByApplication(boolean bl) {
        this.PIN_AutomaticallyResetByApplication = bl;
    }

    public void setPIN_PreEntryAllowed(boolean bl) {
        this.PIN_PreEntryAllowed = bl;
    }
}

