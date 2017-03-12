package com.samsung.android.smartclip;

import android.graphics.Rect;
import android.text.TextUtils;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;

/* compiled from: SmartClipDataRepositoryImpl */
class SmartClipDataRootElement extends SmartClipDataElementImpl {
    SmartClipDataRootElement() {
    }

    public String collectPlainTextTag() {
        SmartClipDataElementImpl element = this;
        String resultString = new String();
        Rect prevTextTagRect = new Rect();
        while (element != null) {
            String plainText = "";
            SlookSmartClipMetaTagArray textTags = element.getTag("plain_text");
            int textTagCount = textTags.size();
            Rect curTextTagRect = element.getMetaAreaRect();
            for (int i = 0; i < textTagCount; i++) {
                String curTagValue = ((SlookSmartClipMetaTag) textTags.get(i)).getValue();
                if (!(curTagValue == null || TextUtils.isEmpty(curTagValue))) {
                    plainText = plainText + curTagValue + " ";
                }
            }
            if (plainText != null && TextUtils.getTrimmedLength(plainText) > 0) {
                if (!(curTextTagRect == null || curTextTagRect.top < prevTextTagRect.bottom || TextUtils.isEmpty(resultString))) {
                    resultString = resultString + "\n";
                }
                resultString = resultString + plainText + " ";
                if (curTextTagRect != null) {
                    prevTextTagRect = curTextTagRect;
                }
            }
            element = element.traverseNextElement(this);
        }
        resultString = resultString.trim();
        if (TextUtils.isEmpty(resultString)) {
            return null;
        }
        return resultString;
    }
}
