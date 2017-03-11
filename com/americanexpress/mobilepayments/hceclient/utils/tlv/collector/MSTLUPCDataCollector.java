package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;

public class MSTLUPCDataCollector implements TLVDataCollector {
    public TagDetail collectData(byte[] bArr, String str, String str2) {
        List<TagDetail> parseTLV = TLVParser.parseTLV(bArr, str, false);
        TagDetail tagDetail = new TagDetail();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2.toUpperCase());
        tagDetail.setTagKey(tagKey);
        TagValue mSTLUPCTagValue = new MSTLUPCTagValue();
        for (TagDetail tagDetail2 : parseTLV) {
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_ATC))) {
                mSTLUPCTagValue.setAtc(tagDetail2.getTagValue().getValue());
                tagKey.setWeight(Integer.parseInt(tagDetail2.getTagValue().getValue(), 16));
            } else if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_START_DT))) {
                mSTLUPCTagValue.setStartDate(tagDetail2.getTagValue().getValue());
            } else if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DYNAMIC_DATA))) {
                mSTLUPCTagValue.setMstDynamicData(tagDetail2.getTagValue().getValue());
            } else if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_END_DT))) {
                mSTLUPCTagValue.setEndDate(tagDetail2.getTagValue().getValue());
            }
        }
        tagDetail.setTagValue(mSTLUPCTagValue);
        return tagDetail;
    }
}
