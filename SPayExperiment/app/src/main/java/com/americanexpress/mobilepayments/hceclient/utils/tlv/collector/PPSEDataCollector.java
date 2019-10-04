/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;

public class PPSEDataCollector
implements TLVDataCollector {
    @Override
    public TagDetail collectData(byte[] arrby, String string, String string2) {
        List<TagDetail> list = TLVParser.parseTLV(arrby, string, false);
        TagDetail tagDetail = new TagDetail();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2.toUpperCase());
        tagDetail.setTagKey(tagKey);
        PPSETagValue pPSETagValue = new PPSETagValue();
        for (TagDetail tagDetail2 : list) {
            if (!tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("APPLICATION_INDICATOR"))) continue;
            pPSETagValue.setsAID(tagDetail2.getTagValue().getValue());
        }
        pPSETagValue.setsDirectoryEntry(HexUtils.byteArrayToHexString(arrby));
        tagDetail.setTagValue(pPSETagValue);
        return tagDetail;
    }
}

