package com.americanexpress.mobilepayments.hceclient.context;

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

    public static String getMetaDataValue(String str) {
        if (getMetaDataMap().containsKey(TagsMapUtil.getTagKey(str))) {
            return ((TagValue) getMetaDataMap().get(TagsMapUtil.getTagKey(str))).getValue();
        }
        return null;
    }

    public static void setMetaDataValue(String str, TagValue tagValue) {
        getMetaDataMap().put(TagsMapUtil.getTagKey(str), tagValue);
    }

    public static TagValue removeMetaDataKey(TagKey tagKey) {
        return (TagValue) DataContext.getSessionInstance().getMetaDataMap().remove(tagKey);
    }

    public static Map<TagKey, TagValue> getMetaDataMap() {
        return DataContext.getSessionInstance().getMetaDataMap();
    }

    public static void setMetaDataMap(Map<TagKey, TagValue> map) {
        DataContext.getSessionInstance().setMetaDataMap(map);
    }

    public static String getDKIForATC(String str) {
        TagKey tagKey = new TagKey();
        tagKey.setDgi(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI));
        tagKey.setTag(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        tagKey.setWeight(Integer.parseInt(str, 16));
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) getMetaDataMap().get(tagKey);
        if (nFCLUPCTagValue != null) {
            return nFCLUPCTagValue.getDki();
        }
        return null;
    }

    public static String getSKDDForATC(String str) {
        TagKey tagKey = new TagKey();
        tagKey.setDgi(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI));
        tagKey.setTag(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        tagKey.setWeight(Integer.parseInt(str, 16));
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) getMetaDataMap().get(tagKey);
        if (nFCLUPCTagValue != null) {
            return nFCLUPCTagValue.getStartDate();
        }
        return null;
    }
}
