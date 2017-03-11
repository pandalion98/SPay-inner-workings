package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class IssuerApplicationParserEmv extends IssuerApplicationParser {
    public void init(String str) {
        super.init(str, Constants.ISSUER_DATA_EMV_START_TAG, Constants.ISSUER_DATA_EMV_END_TAG);
    }
}
