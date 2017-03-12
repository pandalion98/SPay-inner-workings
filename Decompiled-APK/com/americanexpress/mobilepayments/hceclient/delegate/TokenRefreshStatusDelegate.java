package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TokenRefreshStatusDelegate extends OperationDelegate {
    public void doOperation() {
        String dataFromStorage = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_META_DATA);
        if (dataFromStorage == null) {
            nonExistingTokenRefIdResponse();
            return;
        }
        DataContext.getSessionInstance().setMetaDataMap(TagsMapUtil.buildTagMap(LLVARUtil.llVarToObjects(dataFromStorage.getBytes())[0].toString()));
        checkRefreshConditions(MetaDataManager.getMetaDataMap(), purgeLUPCs());
        stashMetaData(MetaDataManager.getMetaDataMap());
    }

    private void nonExistingTokenRefIdResponse() {
        Map hashMap = new HashMap();
        TagValue tagValue = new TagValue();
        tagValue.setValue(HCEClientConstants.HEX_ZERO_BYTE);
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.LUPC_COUNT), tagValue);
        tagValue = new TagValue();
        tagValue.setValue("true");
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.REFRESH_REQUIRED), tagValue);
        tagValue = new TagValue();
        tagValue.setValue("0000");
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.MAX_ATC), tagValue);
        tagValue = new TagValue();
        tagValue.setValue(null);
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.LUPC_REFRESH_CHECK_BACK), tagValue);
        tagValue = new TagValue();
        tagValue.setValue(null);
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.TOKEN_DATA_VERSION), tagValue);
        tagValue = new TagValue();
        tagValue.setValue(HCEClientConstants.HEX_ZERO_BYTE);
        hashMap.put(TagsMapUtil.getTagKey(MetaDataManager.TOKEN_STATUS), tagValue);
        DataContext.getSessionInstance().setMetaDataMap(hashMap);
    }

    private Map<TagKey, TagValue> buildNewMetaDataMap() {
        List tagList = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        List tagList2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
        Map<TagKey, TagValue> hashMap = new HashMap();
        if (!(tagList == null || tagList2 == null || tagList.size() != tagList2.size())) {
            int parseInt = (Integer.parseInt(HexUtils.byteFromHexString(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_THRESHOLD), 0), 16) * 60) * 60;
            for (Entry entry : MetaDataManager.getMetaDataMap().entrySet()) {
                if (entry != null) {
                    if (entry.getValue() instanceof NFCLUPCTagValue) {
                        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) MetaDataManager.getMetaDataMap().get(entry.getKey());
                        if (Long.parseLong(nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp() >= ((long) parseInt)) {
                            hashMap.put(entry.getKey(), nFCLUPCTagValue);
                        }
                    } else {
                        hashMap.put((TagKey) entry.getKey(), (TagValue) entry.getValue());
                    }
                }
            }
        }
        return hashMap;
    }

    private int purgeLUPCs() {
        int i;
        int i2 = 0;
        List tagList = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
        List arrayList = new ArrayList();
        if (tagList == null || tagList.size() <= 0) {
            i = 0;
        } else {
            int parseInt = (Integer.parseInt(HexUtils.byteFromHexString(MetaDataManager.getMetaDataValue(MetaDataManager.LUPC_THRESHOLD), 0), 16) * 60) * 60;
            Iterator it = MetaDataManager.getMetaDataMap().entrySet().iterator();
            i = 0;
            while (it.hasNext()) {
                int i3;
                Entry entry = (Entry) it.next();
                if (entry != null && (entry.getValue() instanceof NFCLUPCTagValue)) {
                    NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) MetaDataManager.getMetaDataMap().get(entry.getKey());
                    long parseLong = Long.parseLong(nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp();
                    if (Long.parseLong(nFCLUPCTagValue.getEndDate()) <= Operation.OPERATION.getRealTimestamp()) {
                        it.remove();
                        arrayList.add(entry.getKey());
                        i3 = i;
                    } else if (!(entry.getValue() instanceof MSTLUPCTagValue) && Long.parseLong(nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp() < ((long) parseInt)) {
                        i3 = i + 1;
                    }
                    i = i3;
                }
                i3 = i;
                i = i3;
            }
        }
        if (arrayList.size() > 0) {
            while (i2 < arrayList.size()) {
                TagKey fromString = TagKey.fromString(arrayList.get(i2).toString());
                if (fromString != null) {
                    String dgi_tag;
                    TagKey fromString2;
                    if (CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI).equals(fromString.getDgi())) {
                        dgi_tag = CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI);
                        fromString2 = TagKey.fromString(dgi_tag + HCEClientConstants.TAG_KEY_SEPARATOR + CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG) + HCEClientConstants.TAG_KEY_SEPARATOR + fromString.getWeight());
                        if (((MSTLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), fromString2)) != null) {
                            MetaDataManager.removeMetaDataKey(fromString2);
                        }
                    } else if (CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI).equals(fromString.getDgi())) {
                        dgi_tag = CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI);
                        fromString2 = TagKey.fromString(dgi_tag + HCEClientConstants.TAG_KEY_SEPARATOR + CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG) + HCEClientConstants.TAG_KEY_SEPARATOR + fromString.getWeight());
                        if (((NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), fromString2)) != null) {
                            MetaDataManager.removeMetaDataKey(fromString2);
                        }
                    }
                }
                i2++;
            }
        }
        return i;
    }

    private void checkRefreshConditions(Map<TagKey, TagValue> map, int i) {
        boolean z;
        String dgi_tag = CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI);
        String dgi_tag2 = CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG);
        int size = TagsMapUtil.getTagList(map, dgi_tag, dgi_tag2).size();
        TagValue tagValue = new TagValue();
        tagValue.setValue(String.valueOf(size));
        map.put(TagsMapUtil.getTagKey(MetaDataManager.LUPC_COUNT), tagValue);
        int parseInt = Integer.parseInt(HexUtils.byteFromHexString(((TagValue) map.get(TagsMapUtil.getTagKey(MetaDataManager.LUPC_THRESHOLD))).getValue(), 2), 16);
        boolean z2 = size <= parseInt;
        if (z2) {
            z = z2;
        } else {
            if (size - i <= parseInt) {
                z2 = true;
            } else {
                z2 = false;
            }
            z = z2;
        }
        tagValue = new TagValue();
        tagValue.setValue(String.valueOf(z));
        map.put(TagsMapUtil.getTagKey(MetaDataManager.REFRESH_REQUIRED), tagValue);
        if (size != 0) {
            NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) TagsMapUtil.getTagValue(map, dgi_tag, dgi_tag2, true);
            map.put(TagsMapUtil.getTagKey(MetaDataManager.RUNNING_ATC), TagValue.fromString(nFCLUPCTagValue.getAtc()));
            if (!z) {
                int parseInt2 = (Integer.parseInt(HexUtils.byteFromHexString(((TagValue) map.get(TagsMapUtil.getTagKey(MetaDataManager.LUPC_THRESHOLD))).getValue(), 0), 16) * 60) * 60;
                TagValue tagValue2 = new TagValue();
                tagValue2.setValue(String.valueOf((Long.parseLong(nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp()) - ((long) parseInt2)));
                map.put(TagsMapUtil.getTagKey(MetaDataManager.LUPC_REFRESH_CHECK_BACK), tagValue2);
            }
        }
    }

    private void stashMetaData(Map<TagKey, TagValue> map) {
        new DataStashImpl().putDataIntoStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_META_DATA, new String(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + JSONUtil.toJSONString(map))));
    }
}
