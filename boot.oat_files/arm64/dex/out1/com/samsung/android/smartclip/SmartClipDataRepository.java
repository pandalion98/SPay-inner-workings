package com.samsung.android.smartclip;

import android.content.Context;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;

public interface SmartClipDataRepository {
    SlookSmartClipMetaTagArray extractMetaTagFromString(Context context, String str);

    SlookSmartClipMetaTagArray getAllMetaTag();

    String getCapturedImageFilePath();

    SlookSmartClipMetaTagArray getMetaTag(String str);
}
