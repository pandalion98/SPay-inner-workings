package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class ProcessingOptionsParserMag extends ProcessingOptionsParser {
    public void init(String str) {
        super.init(str, Constants.APP_FILE_LOCATOR_START_TAG, Constants.APP_FILE_LOCATOR_END_TAG);
    }
}
