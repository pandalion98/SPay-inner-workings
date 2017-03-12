package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.MSTLUPCDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.NFCLUPCDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.PPSEDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollectorFactory;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TagsMapUtil {
    public static TagKey getTagKey(Map<TagKey, TagValue> map, String str, String str2, boolean z) {
        Collection arrayList = new ArrayList(map.keySet());
        Collection arrayList2 = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2);
        arrayList2.add(tagKey);
        arrayList.retainAll(arrayList2);
        List arrayList3 = new ArrayList(arrayList);
        if (arrayList3 == null || arrayList3.size() <= 0) {
            return null;
        }
        Collections.sort(arrayList3);
        if (z) {
            return (TagKey) arrayList3.get(0);
        }
        return (TagKey) arrayList3.get(arrayList3.size() - 1);
    }

    public static TagValue getTagValue(Map<TagKey, TagValue> map, String str, String str2, boolean z) {
        Collection arrayList = new ArrayList(map.keySet());
        Collection arrayList2 = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2);
        arrayList2.add(tagKey);
        arrayList.retainAll(arrayList2);
        List arrayList3 = new ArrayList(arrayList);
        if (arrayList3 == null || arrayList3.size() <= 0) {
            return null;
        }
        Object obj;
        Collections.sort(arrayList3);
        if (z) {
            obj = (TagKey) arrayList3.get(0);
        } else {
            TagKey tagKey2 = (TagKey) arrayList3.get(arrayList3.size() - 1);
        }
        return (TagValue) map.get(obj);
    }

    public static TagValue getTagValue(Map<TagKey, TagValue> map, TagKey tagKey) {
        return (TagValue) map.get(tagKey);
    }

    public static List<TagKey> getTagList(Map<TagKey, TagValue> map, String str, String str2) {
        Collection arrayList = new ArrayList(map.keySet());
        Collection arrayList2 = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2);
        arrayList2.add(tagKey);
        arrayList.retainAll(arrayList2);
        List<TagKey> arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3);
        return arrayList3;
    }

    public static Map<TagKey, TagValue> buildTagMap(String str) {
        Map<TagKey, TagValue> linkedHashMap = new LinkedHashMap();
        try {
            Map map = (Map) new JSONUtil().parse(str);
            List arrayList = new ArrayList();
            for (String str2 : map.keySet()) {
                TagDetail tagDetail = new TagDetail();
                TagKey fromString = TagKey.fromString(str2);
                tagDetail.setTagKey(fromString);
                TagValue tagValue = new TagValue();
                TLVDataCollector dataCollector = TLVDataCollectorFactory.getDataCollector(fromString.getDgi(), fromString.getTag());
                if (dataCollector == null) {
                    tagValue.setValue((String) map.get(str2));
                    tagDetail.setTagValue(tagValue);
                } else if (dataCollector instanceof NFCLUPCDataCollector) {
                    tagDetail.setTagValue(new NFCLUPCTagValue((String) map.get(str2)));
                } else if (dataCollector instanceof MSTLUPCDataCollector) {
                    tagDetail.setTagValue(new MSTLUPCTagValue((String) map.get(str2)));
                } else if (dataCollector instanceof PPSEDataCollector) {
                    tagDetail.setTagValue(PPSETagValue.fromString((String) map.get(str2)));
                }
                arrayList.add(tagDetail);
            }
            linkedHashMap.putAll(TLVParser.tagListToTagMap(arrayList, false));
            return linkedHashMap;
        } catch (Exception e) {
            throw new HCEClientException(e.getMessage());
        }
    }

    public static TagKey getTagKey(String str) {
        TagKey tagKey = new TagKey();
        tagKey.setKey(str);
        return tagKey;
    }

    public static void setTagValue(TagKey tagKey, TagValue tagValue) {
        getTagsMap().put(tagKey, tagValue);
    }

    public static void setTagValue(String str, TagValue tagValue) {
        getTagsMap().put(getTagKey(str), tagValue);
    }

    public static String getTagValue(String str) {
        if (getTagsMap().containsKey(getTagKey(str))) {
            return ((TagValue) getTagsMap().get(getTagKey(str))).getValue();
        }
        return null;
    }

    public static Map<TagKey, TagValue> getTagsMap() {
        return DataContext.getSessionInstance().getTagMap();
    }

    public static Map<String, String> getDGIMap() {
        return DataContext.getSessionInstance().getDgiMap();
    }

    public static String getDGIValue(String str) {
        if (getDGIMap().containsKey(str)) {
            return (String) getDGIMap().get(str);
        }
        return null;
    }

    public static void removeValue(String str) {
        if (getTagsMap().containsKey(getTagKey(str))) {
            getTagsMap().remove(getTagKey(str));
        }
    }
}
