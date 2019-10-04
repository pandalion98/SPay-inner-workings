/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;

public class MSTLUPCDataCollector
implements TLVDataCollector {
    @Override
    public TagDetail collectData(byte[] arrby, String string, String string2) {
        List<TagDetail> list = TLVParser.parseTLV(arrby, string, false);
        TagDetail tagDetail = new TagDetail();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2.toUpperCase());
        tagDetail.setTagKey(tagKey);
        MSTLUPCTagValue mSTLUPCTagValue = new MSTLUPCTagValue();
        for (TagDetail tagDetail2 : list) {
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("MST_LUPC_ATC"))) {
                mSTLUPCTagValue.setAtc(tagDetail2.getTagValue().getValue());
                tagKey.setWeight(Integer.parseInt((String)tagDetail2.getTagValue().getValue(), (int)16));
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("MST_LUPC_START_DT"))) {
                mSTLUPCTagValue.setStartDate(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("MST_LUPC_DYNAMIC_DATA"))) {
                mSTLUPCTagValue.setMstDynamicData(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (!tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("MST_LUPC_END_DT"))) continue;
            mSTLUPCTagValue.setEndDate(tagDetail2.getTagValue().getValue());
        }
        tagDetail.setTagValue(mSTLUPCTagValue);
        return tagDetail;
    }
}

