package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TokenDataHolder {
    private String dgisJSON;
    private Map<String, String> dgisMap;
    private String tagsJSON;
    private Map<TagKey, TagValue> tagsMap;
    private String tlsClearTokenData;

    public TokenDataHolder() {
        this.dgisMap = new LinkedHashMap();
        this.tagsMap = new LinkedHashMap();
        this.tlsClearTokenData = null;
        this.dgisJSON = null;
        this.tagsJSON = null;
    }

    public String getTlsClearTokenData() {
        return this.tlsClearTokenData;
    }

    public void setTlsClearTokenData(String str) {
        this.tlsClearTokenData = str;
    }

    public String getTagsJSON() {
        return this.tagsJSON;
    }

    public void setTagsJSON(String str) {
        this.tagsJSON = str;
    }

    public Map<String, String> getDgisMap() {
        return this.dgisMap;
    }

    public void setDgisMap(Map<String, String> map) {
        this.dgisMap = map;
    }

    public Map<TagKey, TagValue> getTagsMap() {
        return this.tagsMap;
    }

    public void setTagsMap(Map<TagKey, TagValue> map) {
        this.tagsMap = map;
    }

    public String getDgisJSON() {
        return this.dgisJSON;
    }

    public void setDgisJSON(String str) {
        this.dgisJSON = str;
    }

    public String getDGIValue(String str) {
        return (String) this.dgisMap.get(str);
    }

    public boolean containsDGI(String str) {
        return this.dgisMap.containsKey(str);
    }

    public TagKey getTagKey(String str, String str2, boolean z) {
        Collection keySet = getTagsMap().keySet();
        Collection arrayList = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(str);
        tagKey.setTag(str2);
        arrayList.add(tagKey);
        keySet.retainAll(arrayList);
        List arrayList2 = new ArrayList(keySet);
        if (arrayList2 == null || arrayList2.size() <= 0) {
            return null;
        }
        Collections.sort(arrayList2);
        if (z) {
            return (TagKey) arrayList2.get(0);
        }
        return (TagKey) arrayList2.get(arrayList2.size() - 1);
    }

    public TagValue getTagValue(String str, String str2, boolean z) {
        Collection arrayList = new ArrayList(getTagsMap().keySet());
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
        return (TagValue) this.tagsMap.get(obj);
    }

    public TagValue getTagValue(TagKey tagKey) {
        return (TagValue) this.tagsMap.get(tagKey);
    }

    public List<TagKey> getTagList(String str, String str2) {
        Collection arrayList = new ArrayList(getTagsMap().keySet());
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
}
