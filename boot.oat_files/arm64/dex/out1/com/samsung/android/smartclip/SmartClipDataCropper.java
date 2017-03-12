package com.samsung.android.smartclip;

import android.view.View;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;

public abstract class SmartClipDataCropper {
    public abstract SmartClipDataRepository getSmartClipDataRepository();

    public abstract boolean setPendingExtractionResult(View view, SlookSmartClipDataElement slookSmartClipDataElement);
}
