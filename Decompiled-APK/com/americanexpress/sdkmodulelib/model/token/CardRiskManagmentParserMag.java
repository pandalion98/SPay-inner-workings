package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class CardRiskManagmentParserMag extends CardRiskManagmentParser {
    public void init(String str) {
        super.init(str, Constants.CVM_START_TAG, Constants.CVM_END_TAG);
    }
}
