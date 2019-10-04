/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards;

import com.mastercard.mobile_api.bytes.ByteArray;

public class CardRiskManagementData {
    private ByteArray CRM_CountryCode;
    private ByteArray additionalCheckTable;

    public ByteArray getAdditionalCheckTable() {
        return this.additionalCheckTable;
    }

    public ByteArray getCRM_CountryCode() {
        return this.CRM_CountryCode;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isValid() {
        return this.additionalCheckTable.getLength() == 18 && this.CRM_CountryCode.getLength() == 2;
    }

    public void setAdditionalCheckTable(ByteArray byteArray) {
        this.additionalCheckTable = byteArray;
    }

    public void setCRM_CountryCode(ByteArray byteArray) {
        this.CRM_CountryCode = byteArray;
    }
}

