/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
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
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TokenDataHolder {
    private String dgisJSON = null;
    private Map<String, String> dgisMap = new LinkedHashMap();
    private String tagsJSON = null;
    private Map<TagKey, TagValue> tagsMap = new LinkedHashMap();
    private String tlsClearTokenData = null;

    public boolean containsDGI(String string) {
        return this.dgisMap.containsKey((Object)string);
    }

    public String getDGIValue(String string) {
        return (String)this.dgisMap.get((Object)string);
    }

    public String getDgisJSON() {
        return this.dgisJSON;
    }

    public Map<String, String> getDgisMap() {
        return this.dgisMap;
    }

    public TagKey getTagKey(String string, String string2, boolean bl) {
        Set set = this.getTagsMap().keySet();
        ArrayList arrayList = new ArrayList();
        TagKey tagKey = new TagKey();
        tagKey.setDgi(string);
        tagKey.setTag(string2);
        arrayList.add((Object)tagKey);
        set.retainAll((Collection)arrayList);
        ArrayList arrayList2 = new ArrayList((Collection)set);
        if (arrayList2 != null && arrayList2.size() > 0) {
            Collections.sort((List)arrayList2);
            if (bl) {
                return (TagKey)arrayList2.get(0);
            }
            return (TagKey)arrayList2.get(-1 + arrayList2.size());
        }
        return null;
    }

    public List<TagKey> getTagList(String string, String string2) {
        ArrayList arrayList = new ArrayList((Collection)this.getTagsMap().keySet());
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

    public TagValue getTagValue(TagKey tagKey) {
        return (TagValue)this.tagsMap.get((Object)tagKey);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public TagValue getTagValue(String string, String string2, boolean bl) {
        TagKey tagKey;
        ArrayList arrayList = new ArrayList((Collection)this.getTagsMap().keySet());
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
                return (TagValue)this.tagsMap.get((Object)tagKey);
                break;
            } while (true);
        }
        tagKey = (TagKey)arrayList3.get(-1 + arrayList3.size());
        return (TagValue)this.tagsMap.get((Object)tagKey);
    }

    public String getTagsJSON() {
        return this.tagsJSON;
    }

    public Map<TagKey, TagValue> getTagsMap() {
        return this.tagsMap;
    }

    public String getTlsClearTokenData() {
        return this.tlsClearTokenData;
    }

    public void setDgisJSON(String string) {
        this.dgisJSON = string;
    }

    public void setDgisMap(Map<String, String> map) {
        this.dgisMap = map;
    }

    public void setTagsJSON(String string) {
        this.tagsJSON = string;
    }

    public void setTagsMap(Map<TagKey, TagValue> map) {
        this.tagsMap = map;
    }

    public void setTlsClearTokenData(String string) {
        this.tlsClearTokenData = string;
    }
}

