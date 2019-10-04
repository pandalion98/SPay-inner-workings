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
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;

public class NFCLUPCDataCollector
implements TLVDataCollector {
    @Override
    public TagDetail collectData(byte[] arrby, String string, String string2) {
        List<TagDetail> list = TLVParser.parseTLV(arrby, string, false);
        TagDetail tagDetail = new TagDetail();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2.toUpperCase());
        tagDetail.setTagKey(tagKey);
        NFCLUPCTagValue nFCLUPCTagValue = new NFCLUPCTagValue();
        for (TagDetail tagDetail2 : list) {
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_ATC"))) {
                nFCLUPCTagValue.setAtc(tagDetail2.getTagValue().getValue());
                tagKey.setWeight(Integer.parseInt((String)tagDetail2.getTagValue().getValue(), (int)16));
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_LUPC"))) {
                nFCLUPCTagValue.setLupc(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_KCV"))) {
                nFCLUPCTagValue.setKcv(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_DKI"))) {
                nFCLUPCTagValue.setDki(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_START_DT"))) {
                nFCLUPCTagValue.setStartDate(tagDetail2.getTagValue().getValue());
                continue;
            }
            if (!tagDetail2.getTagKey().getTag().equalsIgnoreCase(CPDLConfig.getDGI_TAG("NFC_LUPC_END_DT"))) continue;
            nFCLUPCTagValue.setEndDate(tagDetail2.getTagValue().getValue());
        }
        tagDetail.setTagValue(nFCLUPCTagValue);
        return tagDetail;
    }
}

