package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class ApplicationSelectionParser extends DataGroup {
    public void init(String str) {
        super.init(str, Constants.FCI_TEMPLATE_START_TAG, Constants.FCI_TEMPLATE_END_TAG);
    }

    public void parseDataGroup() {
        if (!isDataGroupMalformed()) {
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}
