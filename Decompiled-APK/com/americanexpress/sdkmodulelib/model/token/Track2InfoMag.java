package com.americanexpress.sdkmodulelib.model.token;

import com.americanexpress.sdkmodulelib.util.Constants;

public class Track2InfoMag extends Track2Info {
    public void init(String str) {
        if (isUsingDeprecateBlob(str)) {
            super.init(str, Constants.TRACK2_START_TAG_DEPRECATE, Constants.TRACK2_END_TAG_DEPRECATE);
        } else {
            super.init(str, Constants.TRACK2_START_TAG, Constants.TRACK2_END_TAG);
        }
    }

    private boolean isUsingDeprecateBlob(String str) {
        int indexOf = str.indexOf(Constants.TRACK2_START_TAG_DEPRECATE);
        int indexOf2 = str.indexOf(Constants.TRACK2_END_TAG_DEPRECATE);
        if (indexOf == -1 || indexOf2 == -1) {
            return false;
        }
        return true;
    }
}
