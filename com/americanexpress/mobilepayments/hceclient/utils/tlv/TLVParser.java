package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollectorFactory;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class TLVParser {
    public static List<TagDetail> parseTLV(byte[] bArr, String str, boolean z) {
        List<TagDetail> arrayList = new ArrayList();
        int i = 0;
        while (i < bArr.length) {
            DOLTag parseTLV = parseTLV(bArr, i, bArr.length, true);
            if (parseTLV == null) {
                throw new TLVException("::TLVHelper::parseTLV::Malformed Record Data");
            }
            String tagName = parseTLV.getTagName();
            byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(parseTLV.getTagValue());
            TagDetail tagDetail = new TagDetail();
            TLVDataCollector dataCollector = TLVDataCollectorFactory.getDataCollector(str, tagName);
            if (dataCollector != null) {
                arrayList.add(dataCollector.collectData(hexStringToByteArray, str, tagName));
            } else {
                TagKey tagKey = new TagKey();
                tagKey.setDgi(str);
                tagKey.setTag(tagName.toUpperCase());
                if (TagScreen.containsTagKey(tagKey) || !z) {
                    TagValue tagValue = new TagValue();
                    tagValue.setValue(HexUtils.hexByteArrayToString(hexStringToByteArray).toUpperCase());
                    tagDetail.setTagKey(tagKey);
                    tagDetail.setTagValue(tagValue);
                    arrayList.add(tagDetail);
                }
            }
            i += parseTLV.getSkipLen();
            if (!parseTLV.isConstructed()) {
                i += parseTLV.getTagLength();
            }
        }
        return arrayList;
    }

    public static DOLTag parseTLV(byte[] bArr, int i, int i2, boolean z) {
        int i3 = 0;
        if (i >= i2) {
            return null;
        }
        int i4;
        DOLTag dOLTag = new DOLTag();
        if ((bArr[i] & 32) == 32) {
            dOLTag.setIsConstructed(true);
        } else {
            dOLTag.setIsConstructed(false);
        }
        while (true) {
            if (bArr[i] != null && bArr[i] != -1) {
                break;
            } else if (i >= bArr.length - 2) {
                return null;
            } else {
                i++;
                i3++;
            }
        }
        if (((byte) (bArr[i] & 31)) == 31) {
            dOLTag.setTagName(HexUtils.byteArrayToHexString(bArr, i, 2).toUpperCase());
            i4 = i + 2;
            i3 += 2;
        } else {
            i4 = i + 1;
            dOLTag.setTagName(HexUtils.byte2Hex(bArr[i]).toUpperCase());
            i3++;
        }
        if (bArr[i4] == -127) {
            i4++;
            dOLTag.setTagLength((short) (bArr[i4] & GF2Field.MASK));
            i4++;
            i3 += 2;
        } else if (bArr[i4] == -126) {
            i4++;
            dOLTag.setTagLength(HexUtils.getShort(bArr, i4));
            i4 += 2;
            i3 += 3;
        } else {
            int i5 = i4 + 1;
            dOLTag.setTagLength((short) (bArr[i4] & GF2Field.MASK));
            i3++;
            i4 = i5;
        }
        if (z) {
            dOLTag.setTagValue(HexUtils.byteArrayToHexString(bArr, i4, dOLTag.getTagLength()));
        }
        dOLTag.setSkipLen(i3);
        return dOLTag;
    }

    public static Map<TagKey, TagValue> tagListToTagMap(List<TagDetail> list, boolean z) {
        Map<TagKey, TagValue> hashMap = new HashMap();
        for (TagDetail tagDetail : list) {
            if (!z) {
                hashMap.put(tagDetail.getTagKey(), tagDetail.getTagValue());
            } else if (TagScreen.containsTagKey(tagDetail.getTagKey())) {
                hashMap.put(tagDetail.getTagKey(), tagDetail.getTagValue());
            }
        }
        return hashMap;
    }
}
