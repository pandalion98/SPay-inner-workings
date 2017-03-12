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

    public static byte setBit(byte b, int i) {
        return (byte) ((1 << i) | b);
    }

    public boolean getACK_AlwaysRequiredIfCurrencyProvided() {
        return this.ACK_AlwaysRequiredIfCurrencyProvided;
    }

    public void setACK_AlwaysRequiredIfCurrencyProvided(boolean z) {
        this.ACK_AlwaysRequiredIfCurrencyProvided = z;
    }

    public boolean getPIN_PreEntryAllowed() {
        return this.PIN_PreEntryAllowed;
    }

    public void setPIN_PreEntryAllowed(boolean z) {
        this.PIN_PreEntryAllowed = z;
    }

    public boolean getPIN_AlwaysRequiredIfCurrencyNotProvided() {
        return this.PIN_AlwaysRequiredIfCurrencyNotProvided;
    }

    public void setPIN_AlwaysRequiredIfCurrencyNotProvided(boolean z) {
        this.PIN_AlwaysRequiredIfCurrencyNotProvided = z;
    }

    public boolean getACK_AlwaysRequiredIfCurrencyNotProvided() {
        return this.ACK_AlwaysRequiredIfCurrencyNotProvided;
    }

    public void setACK_AlwaysRequiredIfCurrencyNotProvided(boolean z) {
        this.ACK_AlwaysRequiredIfCurrencyNotProvided = z;
    }

    public boolean getACK_PreEntryAllowed() {
        return this.ACK_PreEntryAllowed;
    }

    public void setACK_PreEntryAllowed(boolean z) {
        this.ACK_PreEntryAllowed = z;
    }

    public boolean getACK_AutomaticallyResetByApplication() {
        return this.ACK_AutomaticallyResetByApplication;
    }

    public void setACK_AutomaticallyResetByApplication(boolean z) {
        this.ACK_AutomaticallyResetByApplication = z;
    }

    public boolean getPIN_AutomaticallyResetByApplication() {
        return this.PIN_AutomaticallyResetByApplication;
    }

    public void setPIN_AutomaticallyResetByApplication(boolean z) {
        this.PIN_AutomaticallyResetByApplication = z;
    }

    public boolean getPIN_AlwaysRequiredIfCurrencyProvided() {
        return this.PIN_AlwaysRequiredIfCurrencyProvided;
    }

    public void setPIN_AlwaysRequiredIfCurrencyProvided(boolean z) {
        this.PIN_AlwaysRequiredIfCurrencyProvided = z;
    }

    public ByteArray getMPAObject() {
        byte bit;
        if (Boolean.valueOf(getACK_AlwaysRequiredIfCurrencyProvided()).booleanValue()) {
            bit = setBit((byte) 0, 7);
        } else {
            bit = (byte) 0;
        }
        if (Boolean.valueOf(getACK_AlwaysRequiredIfCurrencyNotProvided()).booleanValue()) {
            bit = setBit(bit, 6);
        }
        if (Boolean.valueOf(getPIN_AlwaysRequiredIfCurrencyProvided()).booleanValue()) {
            bit = setBit(bit, 4);
        }
        if (Boolean.valueOf(getPIN_AlwaysRequiredIfCurrencyNotProvided()).booleanValue()) {
            bit = setBit(bit, 3);
        }
        return new DefaultByteArrayImpl(new byte[]{bit});
    }
}
