package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;

public class PPSEDataCollector implements TLVDataCollector {
    public TagDetail collectData(byte[] bArr, String str, String str2) {
        List<TagDetail> parseTLV = TLVParser.parseTLV(bArr, str, false);
        TagDetail tagDetail = new TagDetail();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2.toUpperCase());
        tagDetail.setTagKey(tagKey);
        TagValue pPSETagValue = new PPSETagValue();
        for (TagDetail tagDetail2 : parseTLV) {
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG(CPDLConfig.APPLICATION_INDICATOR))) {
                pPSETagValue.setsAID(tagDetail2.getTagValue().getValue());
            }
        }
        pPSETagValue.setsDirectoryEntry(HexUtils.byteArrayToHexString(bArr));
        tagDetail.setTagValue(pPSETagValue);
        return tagDetail;
    }
}
