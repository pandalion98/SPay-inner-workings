package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class Track2InfoEmv extends Track2Info {
    public void init(String str) {
        super.init(str, Constants.TRACK2_EMV_START_TAG, Constants.TRACK2_EMV_END_TAG);
    }
}
