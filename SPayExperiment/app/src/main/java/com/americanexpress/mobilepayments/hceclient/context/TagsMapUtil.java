/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 */
package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
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
import java.util.Set;

public class TagsMapUtil {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Map<TagKey, TagValue> buildTagMap(String string) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            Map map = (Map)new JSONUtil().parse(string);
            ArrayList arrayList = new ArrayList();
            for (String string2 : map.keySet()) {
                TagDetail tagDetail = new TagDetail();
                TagKey tagKey = TagKey.fromString(string2);
                tagDetail.setTagKey(tagKey);
                TagValue tagValue = new TagValue();
                TLVDataCollector tLVDataCollector = TLVDataCollectorFactory.getDataCollector(tagKey.getDgi(), tagKey.getTag());
                if (tLVDataCollector != null) {
                    if (tLVDataCollector instanceof NFCLUPCDataCollector) {
                        tagDetail.setTagValue(new NFCLUPCTagValue((String)map.get((Object)string2)));
                    } else if (tLVDataCollector instanceof MSTLUPCDataCollector) {
                        tagDetail.setTagValue(new MSTLUPCTagValue((String)map.get((Object)string2)));
                    } else if (tLVDataCollector instanceof PPSEDataCollector) {
                        tagDetail.setTagValue(PPSETagValue.fromString((String)map.get((Object)string2)));
                    }
                } else {
                    tagValue.setValue((String)map.get((Object)string2));
                    tagDetail.setTagValue(tagValue);
                }
                arrayList.add((Object)tagDetail);
            }
            linkedHashMap.putAll(TLVParser.tagListToTagMap((List<TagDetail>)arrayList, false));
            return linkedHashMap;
        }
        catch (Exception exception) {
            throw new HCEClientException(exception.getMessage());
        }
    }

    public static Map<String, String> getDGIMap() {
        return DataContext.getSessionInstance().getDgiMap();
    }

    public static String getDGIValue(String string) {
        boolean bl = TagsMapUtil.getDGIMap().containsKey((Object)string);
        String string2 = null;
        if (bl) {
            string2 = (String)TagsMapUtil.getDGIMap().get((Object)string);
        }
        return string2;
    }

    public static TagKey getTagKey(String string) {
        TagKey tagKey = new TagKey();
        tagKey.setKey(string);
        return tagKey;
    }

    public static TagKey getTagKey(Map<TagKey, TagValue> map, String string, String string2, boolean bl) {
        ArrayList arrayList = new ArrayList((Collection)map.keySet());
        ArrayList arrayList2 = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2);
        arrayList2.add((Object)tagKey);
        arrayList.retainAll((Collection)arrayList2);
        ArrayList arrayList3 = new ArrayList((Collection)arrayList);
        if (arrayList3 != null && arrayList3.size() > 0) {
            Collections.sort((List)arrayList3);
            if (bl) {
                return (TagKey)arrayList3.get(0);
            }
            return (TagKey)arrayList3.get(-1 + arrayList3.size());
        }
        return null;
    }

    public static List<TagKey> getTagList(Map<TagKey, TagValue> map, String string, String string2) {
        ArrayList arrayList = new ArrayList((Collection)map.keySet());
        ArrayList arrayList2 = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2);
        arrayList2.add((Object)tagKey);
        arrayList.retainAll((Collection)arrayList2);
        ArrayList arrayList3 = new ArrayList((Collection)arrayList);
        Collections.sort((List)arrayList3);
        return arrayList3;
    }

    public static TagValue getTagValue(Map<TagKey, TagValue> map, TagKey tagKey) {
        return (TagValue)map.get((Object)tagKey);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static TagValue getTagValue(Map<TagKey, TagValue> map, String string, String string2, boolean bl) {
        TagKey tagKey;
        ArrayList arrayList = new ArrayList((Collection)map.keySet());
        ArrayList arrayList2 = new ArrayList();
        TagKey tagKey2 = new TagKey();
        tagKey2.setDgi(string);
        tagKey2.setTag(string2);
        arrayList2.add((Object)tagKey2);
        arrayList.retainAll((Collection)arrayList2);
        ArrayList arrayList3 = new ArrayList((Collection)arrayList);
        if (arrayList3 == null || arrayList3.size() <= 0) return null;
        Collections.sort((List)arrayList3);
        if (bl) {
            tagKey = (TagKey)arrayList3.get(0);
            do {
                return (TagValue)map.get((Object)tagKey);
                break;
            } while (true);
        }
        tagKey = (TagKey)arrayList3.get(-1 + arrayList3.size());
        return (TagValue)map.get((Object)tagKey);
    }

    public static String getTagValue(String string) {
        boolean bl = TagsMapUtil.getTagsMap().containsKey((Object)TagsMapUtil.getTagKey(string));
        String string2 = null;
        if (bl) {
            string2 = ((TagValue)TagsMapUtil.getTagsMap().get((Object)TagsMapUtil.getTagKey(string))).getValue();
        }
        return string2;
    }

    public static Map<TagKey, TagValue> getTagsMap() {
        return DataContext.getSessionInstance().getTagMap();
    }

    public static void removeValue(String string) {
        if (TagsMapUtil.getTagsMap().containsKey((Object)TagsMapUtil.getTagKey(string))) {
            TagsMapUtil.getTagsMap().remove((Object)TagsMapUtil.getTagKey(string));
        }
    }

    public static void setTagValue(TagKey tagKey, TagValue tagValue) {
        TagsMapUtil.getTagsMap().put((Object)tagKey, (Object)tagValue);
    }

    public static void setTagValue(String string, TagValue tagValue) {
        TagsMapUtil.getTagsMap().put((Object)TagsMapUtil.getTagKey(string), (Object)tagValue);
    }
}

