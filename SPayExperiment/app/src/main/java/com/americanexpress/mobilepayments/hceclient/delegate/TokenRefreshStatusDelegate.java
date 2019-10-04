/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
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
import java.util.Set;

public class TokenRefreshStatusDelegate
extends OperationDelegate {
    private Map<TagKey, TagValue> buildNewMetaDataMap() {
        List<TagKey> list = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        List<TagKey> list2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
        HashMap hashMap = new HashMap();
        if (list != null && list2 != null && list.size() == list2.size()) {
            int n2 = 60 * (60 * Integer.parseInt((String)HexUtils.byteFromHexString(MetaDataManager.getMetaDataValue("LUPC_THRESHOLD"), 0), (int)16));
            for (Map.Entry entry : MetaDataManager.getMetaDataMap().entrySet()) {
                if (entry == null) continue;
                if (entry.getValue() instanceof NFCLUPCTagValue) {
                    NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)MetaDataManager.getMetaDataMap().get(entry.getKey());
                    if (Long.parseLong((String)nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp() < (long)n2) continue;
                    hashMap.put(entry.getKey(), (Object)nFCLUPCTagValue);
                    continue;
                }
                hashMap.put((Object)((TagKey)entry.getKey()), (Object)((TagValue)entry.getValue()));
            }
        }
        return hashMap;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void checkRefreshConditions(Map<TagKey, TagValue> map, int n2) {
        boolean bl;
        String string = CPDLConfig.getDGI_TAG("NFC_LUPC_DGI");
        String string2 = CPDLConfig.getDGI_TAG("NFC_LUPC_TAG");
        int n3 = TagsMapUtil.getTagList(map, string, string2).size();
        TagValue tagValue = new TagValue();
        tagValue.setValue(String.valueOf((int)n3));
        map.put((Object)TagsMapUtil.getTagKey("LUPC_COUNT"), (Object)tagValue);
        int n4 = Integer.parseInt((String)HexUtils.byteFromHexString(((TagValue)map.get((Object)TagsMapUtil.getTagKey("LUPC_THRESHOLD"))).getValue(), 2), (int)16);
        boolean bl2 = n3 <= n4;
        if (!bl2) {
            boolean bl3 = n3 - n2 <= n4;
            bl = bl3;
        } else {
            bl = bl2;
        }
        TagValue tagValue2 = new TagValue();
        tagValue2.setValue(String.valueOf((boolean)bl));
        map.put((Object)TagsMapUtil.getTagKey("REFRESH_REQUIRED"), (Object)tagValue2);
        if (n3 != 0) {
            NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)TagsMapUtil.getTagValue(map, string, string2, true);
            map.put((Object)TagsMapUtil.getTagKey("RUNNING_ATC"), (Object)TagValue.fromString(nFCLUPCTagValue.getAtc()));
            if (!bl) {
                int n5 = 60 * (60 * Integer.parseInt((String)HexUtils.byteFromHexString(((TagValue)map.get((Object)TagsMapUtil.getTagKey("LUPC_THRESHOLD"))).getValue(), 0), (int)16));
                TagValue tagValue3 = new TagValue();
                tagValue3.setValue(String.valueOf((long)(Long.parseLong((String)nFCLUPCTagValue.getEndDate()) - Operation.OPERATION.getRealTimestamp() - (long)n5)));
                map.put((Object)TagsMapUtil.getTagKey("LUPC_REFRESH_CHECK_BACK"), (Object)tagValue3);
            }
        }
    }

    private void nonExistingTokenRefIdResponse() {
        HashMap hashMap = new HashMap();
        TagValue tagValue = new TagValue();
        tagValue.setValue("00");
        hashMap.put((Object)TagsMapUtil.getTagKey("LUPC_COUNT"), (Object)tagValue);
        TagValue tagValue2 = new TagValue();
        tagValue2.setValue("true");
        hashMap.put((Object)TagsMapUtil.getTagKey("REFRESH_REQUIRED"), (Object)tagValue2);
        TagValue tagValue3 = new TagValue();
        tagValue3.setValue("0000");
        hashMap.put((Object)TagsMapUtil.getTagKey("MAX_ATC"), (Object)tagValue3);
        TagValue tagValue4 = new TagValue();
        tagValue4.setValue(null);
        hashMap.put((Object)TagsMapUtil.getTagKey("LUPC_REFRESH_CHECK_BACK"), (Object)tagValue4);
        TagValue tagValue5 = new TagValue();
        tagValue5.setValue(null);
        hashMap.put((Object)TagsMapUtil.getTagKey("TOKEN_DATA_VERSION"), (Object)tagValue5);
        TagValue tagValue6 = new TagValue();
        tagValue6.setValue("00");
        hashMap.put((Object)TagsMapUtil.getTagKey("TOKEN_STATUS"), (Object)tagValue6);
        DataContext.getSessionInstance().setMetaDataMap((Map<TagKey, TagValue>)hashMap);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int purgeLUPCs() {
        block13 : {
            block12 : {
                var1_1 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
                TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
                var3_2 = new ArrayList();
                if (var1_1 != null && var1_1.size() > 0) break block12;
                var4_5 = 0;
                break block13;
            }
            var16_3 = 60 * (60 * Integer.parseInt((String)HexUtils.byteFromHexString(MetaDataManager.getMetaDataValue("LUPC_THRESHOLD"), 0), (int)16));
            var17_4 = MetaDataManager.getMetaDataMap().entrySet().iterator();
            var4_5 = 0;
            do {
                if (!var17_4.hasNext()) break;
                var18_6 = (Map.Entry)var17_4.next();
                if (var18_6 == null || !(var18_6.getValue() instanceof NFCLUPCTagValue)) ** GOTO lbl-1000
                var20_8 = (NFCLUPCTagValue)MetaDataManager.getMetaDataMap().get(var18_6.getKey());
                Long.parseLong((String)var20_8.getEndDate()) - Operation.OPERATION.getRealTimestamp();
                if (Long.parseLong((String)var20_8.getEndDate()) <= Operation.OPERATION.getRealTimestamp()) {
                    var17_4.remove();
                    var3_2.add(var18_6.getKey());
                    var19_7 = var4_5;
                } else if (!(var18_6.getValue() instanceof MSTLUPCTagValue) && Long.parseLong((String)var20_8.getEndDate()) - Operation.OPERATION.getRealTimestamp() < (long)var16_3) {
                    var19_7 = var4_5 + 1;
                } else lbl-1000: // 2 sources:
                {
                    var19_7 = var4_5;
                }
                var4_5 = var19_7;
            } while (true);
        }
        var5_9 = var3_2.size();
        var6_10 = 0;
        if (var5_9 <= 0) return var4_5;
        while (var6_10 < var3_2.size()) {
            var7_11 = TagKey.fromString(var3_2.get(var6_10).toString());
            if (var7_11 != null) {
                if (CPDLConfig.getDGI_TAG("NFC_LUPC_DGI").equals((Object)var7_11.getDgi())) {
                    var12_15 = CPDLConfig.getDGI_TAG("MST_LUPC_DGI");
                    var13_16 = CPDLConfig.getDGI_TAG("MST_LUPC_TAG");
                    var14_17 = TagKey.fromString(var12_15 + "-" + var13_16 + "-" + var7_11.getWeight());
                    if ((MSTLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), var14_17) != null) {
                        MetaDataManager.removeMetaDataKey(var14_17);
                    }
                } else if (CPDLConfig.getDGI_TAG("MST_LUPC_DGI").equals((Object)var7_11.getDgi())) {
                    var8_12 = CPDLConfig.getDGI_TAG("NFC_LUPC_DGI");
                    var9_13 = CPDLConfig.getDGI_TAG("NFC_LUPC_TAG");
                    var10_14 = TagKey.fromString(var8_12 + "-" + var9_13 + "-" + var7_11.getWeight());
                    if ((NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), var10_14) != null) {
                        MetaDataManager.removeMetaDataKey(var10_14);
                    }
                }
            }
            ++var6_10;
        }
        return var4_5;
    }

    private void stashMetaData(Map<TagKey, TagValue> map) {
        DataStashImpl dataStashImpl = new DataStashImpl();
        Object[] arrobject = new Object[]{'1' + JSONUtil.toJSONString(map)};
        String string = new String(LLVARUtil.objectsToLLVar(arrobject));
        dataStashImpl.putDataIntoStorage(Operation.OPERATION.getTokenRefId(), "HCECLIENT_META_DATA", string);
    }

    @Override
    public void doOperation() {
        String string = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), "HCECLIENT_META_DATA");
        if (string == null) {
            this.nonExistingTokenRefIdResponse();
            return;
        }
        Map<TagKey, TagValue> map = TagsMapUtil.buildTagMap(LLVARUtil.llVarToObjects(string.getBytes())[0].toString());
        DataContext.getSessionInstance().setMetaDataMap(map);
        int n2 = this.purgeLUPCs();
        this.checkRefreshConditions(MetaDataManager.getMetaDataMap(), n2);
        this.stashMetaData(MetaDataManager.getMetaDataMap());
    }
}

