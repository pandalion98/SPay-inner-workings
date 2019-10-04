/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.LinkedHashMap
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataContext {
    private String appInfo;
    private Map<String, Object> appInfoMap = new LinkedHashMap();
    private String dgiJson;
    private Map<String, String> dgiMap = new LinkedHashMap();
    private String metaData;
    private Map<TagKey, TagValue> metaDataMap = new LinkedHashMap();
    private String tagJson;
    private Map<TagKey, TagValue> tagMap = new LinkedHashMap();

    public static DataContext getSessionInstance() {
        Session session = SessionManager.getSession();
        DataContext dataContext = (DataContext)session.getValue("DATA_CONTEXT", false);
        if (dataContext == null) {
            dataContext = new DataContext();
            session.setValue("DATA_CONTEXT", dataContext);
        }
        return dataContext;
    }

    public String getAppInfo() {
        return this.appInfo;
    }

    public Map<String, Object> getAppInfoMap() {
        return this.appInfoMap;
    }

    public String getDgiJson() {
        return this.dgiJson;
    }

    public Map<String, String> getDgiMap() {
        return this.dgiMap;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public Map<TagKey, TagValue> getMetaDataMap() {
        return this.metaDataMap;
    }

    public String getTagJson() {
        return this.tagJson;
    }

    public Map<TagKey, TagValue> getTagMap() {
        return this.tagMap;
    }

    public void setAppInfo(String string) {
        this.appInfo = string;
    }

    public void setAppInfoMap(Map<String, Object> map) {
        this.appInfoMap = map;
    }

    public void setDgiJson(String string) {
        this.dgiJson = string;
    }

    public void setDgiMap(Map<String, String> map) {
        this.dgiMap = map;
    }

    public void setMetaData(String string) {
        this.metaData = string;
    }

    public void setMetaDataMap(Map<TagKey, TagValue> map) {
        this.metaDataMap = map;
    }

    public void setTagJson(String string) {
        this.tagJson = string;
    }

    public void setTagMap(Map<TagKey, TagValue> map) {
        this.tagMap = map;
    }
}

