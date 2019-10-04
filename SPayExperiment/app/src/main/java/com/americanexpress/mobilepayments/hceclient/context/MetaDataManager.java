/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class MetaDataManager {
    public static final String LUPC_COUNT = "LUPC_COUNT";
    public static final String LUPC_REFRESH_CHECK_BACK = "LUPC_REFRESH_CHECK_BACK";
    public static final String LUPC_THRESHOLD = "LUPC_THRESHOLD";
    public static final String MAX_ATC = "MAX_ATC";
    public static final String REFRESH_REQUIRED = "REFRESH_REQUIRED";
    public static final String RUNNING_ATC = "RUNNING_ATC";
    public static final String TOKEN_DATA_VERSION = "TOKEN_DATA_VERSION";
    public static final String TOKEN_STATUS = "TOKEN_STATUS";

    public static String getDKIForATC(String string) {
        TagKey tagKey = new TagKey();
        tagKey.setDgi(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"));
        tagKey.setTag(CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        tagKey.setWeight(Integer.parseInt((String)string, (int)16));
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)MetaDataManager.getMetaDataMap().get((Object)tagKey);
        if (nFCLUPCTagValue != null) {
            return nFCLUPCTagValue.getDki();
        }
        return null;
    }

    public static Map<TagKey, TagValue> getMetaDataMap() {
        return DataContext.getSessionInstance().getMetaDataMap();
    }

    public static String getMetaDataValue(String string) {
        boolean bl = MetaDataManager.getMetaDataMap().containsKey((Object)TagsMapUtil.getTagKey(string));
        String string2 = null;
        if (bl) {
            string2 = ((TagValue)MetaDataManager.getMetaDataMap().get((Object)TagsMapUtil.getTagKey(string))).getValue();
        }
        return string2;
    }

    public static String getSKDDForATC(String string) {
        TagKey tagKey = new TagKey();
        tagKey.setDgi(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"));
        tagKey.setTag(CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        tagKey.setWeight(Integer.parseInt((String)string, (int)16));
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)MetaDataManager.getMetaDataMap().get((Object)tagKey);
        if (nFCLUPCTagValue != null) {
            return nFCLUPCTagValue.getStartDate();
        }
        return null;
    }

    public static TagValue removeMetaDataKey(TagKey tagKey) {
        return (TagValue)DataContext.getSessionInstance().getMetaDataMap().remove((Object)tagKey);
    }

    public static void setMetaDataMap(Map<TagKey, TagValue> map) {
        DataContext.getSessionInstance().setMetaDataMap(map);
    }

    public static void setMetaDataValue(String string, TagValue tagValue) {
        MetaDataManager.getMetaDataMap().put((Object)TagsMapUtil.getTagKey(string), (Object)tagValue);
    }
}

