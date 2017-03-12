package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class CardRiskManagmentParserEmv extends CardRiskManagmentParser {
    public void init(String str) {
        super.init(str, Constants.CVM_EMV_START_TAG, Constants.CVM_EMV_END_TAG);
    }
}
