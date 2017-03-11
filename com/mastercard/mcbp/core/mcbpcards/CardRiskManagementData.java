package com.mastercard.mcbp.core.mcbpcards;

import com.mastercard.mobile_api.bytes.ByteArray;

public class CardRiskManagementData {
    private ByteArray CRM_CountryCode;
    private ByteArray additionalCheckTable;

    public boolean isValid() {
        if (this.additionalCheckTable.getLength() == 18 && this.CRM_CountryCode.getLength() == 2) {
            return true;
        }
        return false;
    }

    public ByteArray getAdditionalCheckTable() {
        return this.additionalCheckTable;
    }

    public void setAdditionalCheckTable(ByteArray byteArray) {
        this.additionalCheckTable = byteArray;
    }

    public ByteArray getCRM_CountryCode() {
        return this.CRM_CountryCode;
    }

    public void setCRM_CountryCode(ByteArray byteArray) {
        this.CRM_CountryCode = byteArray;
    }
}
