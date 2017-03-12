package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;

public interface TLVDataCollector {
    TagDetail collectData(byte[] bArr, String str, String str2);
}
