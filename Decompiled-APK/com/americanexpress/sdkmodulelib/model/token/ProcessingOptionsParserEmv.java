package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class ProcessingOptionsParserEmv extends ProcessingOptionsParser {
    public void init(String str) {
        super.init(str, Constants.APP_FILE_LOCATOR_EMV_START_TAG, Constants.APP_FILE_LOCATOR_EMV_END_TAG);
    }
}
