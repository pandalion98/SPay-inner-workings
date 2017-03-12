package com.samsung.android.smartclip;

import android.view.View;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;

public interface SmartClipDataExtractionListener {
    public static final int EXTRACTION_DEFAULT = 1;
    public static final int EXTRACTION_DISCARD = 0;

    int onExtractSmartClipData(View view, SlookSmartClipDataElement slookSmartClipDataElement, SlookSmartClipCroppedArea slookSmartClipCroppedArea);
}
