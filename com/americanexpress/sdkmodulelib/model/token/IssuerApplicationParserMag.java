package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class IssuerApplicationParserMag extends IssuerApplicationParser {
    public void init(String str) {
        super.init(str, Constants.ISSUER_DATA_START_TAG, Constants.ISSUER_DATA_END_TAG);
    }
}
