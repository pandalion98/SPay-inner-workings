package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataContext {
    private String appInfo;
    private Map<String, Object> appInfoMap;
    private String dgiJson;
    private Map<String, String> dgiMap;
    private String metaData;
    private Map<TagKey, TagValue> metaDataMap;
    private String tagJson;
    private Map<TagKey, TagValue> tagMap;

    public DataContext() {
        this.dgiMap = new LinkedHashMap();
        this.tagMap = new LinkedHashMap();
        this.appInfoMap = new LinkedHashMap();
        this.metaDataMap = new LinkedHashMap();
    }

    public String getMetaData() {
        return this.metaData;
    }

    public void setMetaData(String str) {
        this.metaData = str;
    }

    public String getTagJson() {
        return this.tagJson;
    }

    public void setTagJson(String str) {
        this.tagJson = str;
    }

    public String getDgiJson() {
        return this.dgiJson;
    }

    public void setDgiJson(String str) {
        this.dgiJson = str;
    }

    public String getAppInfo() {
        return this.appInfo;
    }

    public void setAppInfo(String str) {
        this.appInfo = str;
    }

    public Map<String, String> getDgiMap() {
        return this.dgiMap;
    }

    public void setDgiMap(Map<String, String> map) {
        this.dgiMap = map;
    }

    public Map<TagKey, TagValue> getTagMap() {
        return this.tagMap;
    }

    public void setTagMap(Map<TagKey, TagValue> map) {
        this.tagMap = map;
    }

    public Map<String, Object> getAppInfoMap() {
        return this.appInfoMap;
    }

    public void setAppInfoMap(Map<String, Object> map) {
        this.appInfoMap = map;
    }

    public Map<TagKey, TagValue> getMetaDataMap() {
        return this.metaDataMap;
    }

    public void setMetaDataMap(Map<TagKey, TagValue> map) {
        this.metaDataMap = map;
    }

    public static DataContext getSessionInstance() {
        Session session = SessionManager.getSession();
        DataContext dataContext = (DataContext) session.getValue(SessionConstants.DATA_CONTEXT, false);
        if (dataContext != null) {
            return dataContext;
        }
        dataContext = new DataContext();
        session.setValue(SessionConstants.DATA_CONTEXT, dataContext);
        return dataContext;
    }
}
