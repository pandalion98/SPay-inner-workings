/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class CardRiskManagementData {
    private ByteArray CRM_CountryCode;
    private ByteArray additionalCheckTable;

    public ByteArray getAdditionalCheckTable() {
        return this.additionalCheckTable;
    }

    public ByteArray getCRM_CountryCode() {
        return this.CRM_CountryCode;
    }

    public void setAdditionalCheckTable(ByteArray byteArray) {
        this.additionalCheckTable = byteArray;
    }

    public void setCRM_CountryCode(ByteArray byteArray) {
        this.CRM_CountryCode = byteArray;
    }

    public String toString() {
        return "CardRiskManagementData [additionalCheckTable=" + this.additionalCheckTable + ", CRM_CountryCode=" + this.CRM_CountryCode + "]";
    }

    public void wipe() {
        Utils.clearByteArray(this.additionalCheckTable);
        Utils.clearByteArray(this.CRM_CountryCode);
    }
}

