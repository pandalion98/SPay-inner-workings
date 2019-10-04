/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVException;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TagScreen;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollectorFactory;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TLVParser {
    /*
     * Enabled aggressive block sorting
     */
    public static DOLTag parseTLV(byte[] arrby, int n2, int n3, boolean bl) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8 = 0;
        if (n2 >= n3) {
            return null;
        }
        DOLTag dOLTag = new DOLTag();
        if ((32 & arrby[n2]) == 32) {
            dOLTag.setIsConstructed(true);
        } else {
            dOLTag.setIsConstructed(false);
            n8 = 0;
        }
        while (arrby[n2] == 0 || arrby[n2] == -1) {
            if (n2 >= -2 + arrby.length) {
                return null;
            }
            ++n2;
            ++n8;
        }
        if ((byte)(31 & arrby[n2]) == 31) {
            dOLTag.setTagName(HexUtils.byteArrayToHexString(arrby, n2, 2).toUpperCase());
            n7 = n2 + 2;
            n5 = n8 + 2;
        } else {
            n7 = n2 + 1;
            dOLTag.setTagName(HexUtils.byte2Hex(arrby[n2]).toUpperCase());
            n5 = n8 + 1;
        }
        if (arrby[n7] == -127) {
            int n9 = n7 + 1;
            dOLTag.setTagLength((short)(255 & arrby[n9]));
            n4 = n9 + 1;
            n6 = n5 + 2;
        } else if (arrby[n7] == -126) {
            int n10 = n7 + 1;
            dOLTag.setTagLength(HexUtils.getShort(arrby, n10));
            n4 = n10 + 2;
            n6 = n5 + 3;
        } else {
            int n11 = n7 + 1;
            dOLTag.setTagLength((short)(255 & arrby[n7]));
            n6 = n5 + 1;
            n4 = n11;
        }
        if (bl) {
            dOLTag.setTagValue(HexUtils.byteArrayToHexString(arrby, n4, dOLTag.getTagLength()));
        }
        dOLTag.setSkipLen(n6);
        return dOLTag;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static List<TagDetail> parseTLV(byte[] arrby, String string, boolean bl) {
        ArrayList arrayList = new ArrayList();
        int n2 = 0;
        while (n2 < arrby.length) {
            DOLTag dOLTag = TLVParser.parseTLV(arrby, n2, arrby.length, true);
            if (dOLTag == null) {
                throw new TLVException("::TLVHelper::parseTLV::Malformed Record Data");
            }
            String string2 = dOLTag.getTagName();
            byte[] arrby2 = HexUtils.hexStringToByteArray(dOLTag.getTagValue());
            TagDetail tagDetail = new TagDetail();
            TLVDataCollector tLVDataCollector = TLVDataCollectorFactory.getDataCollector(string, string2);
            if (tLVDataCollector != null) {
                arrayList.add((Object)tLVDataCollector.collectData(arrby2, string, string2));
            } else {
                TagKey tagKey = new TagKey();
                tagKey.setDgi(string);
                tagKey.setTag(string2.toUpperCase());
                if (TagScreen.containsTagKey(tagKey) || !bl) {
                    TagValue tagValue = new TagValue();
                    tagValue.setValue(HexUtils.hexByteArrayToString(arrby2).toUpperCase());
                    tagDetail.setTagKey(tagKey);
                    tagDetail.setTagValue(tagValue);
                    arrayList.add((Object)tagDetail);
                }
            }
            n2 += dOLTag.getSkipLen();
            if (dOLTag.isConstructed()) continue;
            n2 += dOLTag.getTagLength();
        }
        return arrayList;
    }

    public static Map<TagKey, TagValue> tagListToTagMap(List<TagDetail> list, boolean bl) {
        HashMap hashMap = new HashMap();
        for (TagDetail tagDetail : list) {
            if (bl) {
                if (!TagScreen.containsTagKey(tagDetail.getTagKey())) continue;
                hashMap.put((Object)tagDetail.getTagKey(), (Object)tagDetail.getTagValue());
                continue;
            }
            hashMap.put((Object)tagDetail.getTagKey(), (Object)tagDetail.getTagValue());
        }
        return hashMap;
    }
}

