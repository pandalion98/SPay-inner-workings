/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.RandomNumberGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.TLVParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.PPSETagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;

public class TokenPersoDelegate
extends OperationDelegate {
    private static final int NO_OF_DATA_ELEMENTS_WITHOUT_MST = 4;
    private static final int NO_OF_DATA_ELEMENTS_WITH_MST = 5;

    private void cleanTokenDataHolder(TokenDataHolder tokenDataHolder) {
        tokenDataHolder.getDgisMap().clear();
        tokenDataHolder.getTagsMap().clear();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void prepareFCIResponse() {
        String string;
        byte by;
        DataContext dataContext = DataContext.getSessionInstance();
        PPSETagValue pPSETagValue = (PPSETagValue)TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V1"), true);
        PPSETagValue pPSETagValue2 = (PPSETagValue)TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V2"), false);
        String string2 = "";
        if (pPSETagValue != null) {
            string2 = pPSETagValue.getsAID();
        }
        String string3 = pPSETagValue2 != null ? pPSETagValue2.getsAID() : "";
        String string4 = (String)dataContext.getDgiMap().get((Object)CPDLConfig.getDGI_TAG("SELECT_AID_DGI"));
        TagValue tagValue = TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PDOL_TAG"), false);
        if (tagValue != null) {
            string = "9F38" + Utility.constructLV(tagValue.getValue());
            by = (byte)(string.length() / 2);
        } else {
            string = "";
            by = 0;
        }
        byte[] arrby = HexUtils.hexStringToByteArray(string4);
        String string5 = "";
        byte by2 = by;
        int n2 = 0;
        do {
            String string6;
            byte by3;
            if (n2 >= arrby.length) {
                byte[] arrby2 = HexUtils.hexStringToByteArray(string5);
                arrby2[1] = (byte)(by2 + arrby2[1]);
                String string7 = HexUtils.byteArrayToHexString(arrby2);
                String string8 = "6F" + Utility.constructLV(new StringBuilder().append("84").append(Utility.constructLV(string2)).append(string4).toString());
                String string9 = "6F" + Utility.constructLV(new StringBuilder().append("84").append(Utility.constructLV(string3)).append(string7).toString());
                TagsMapUtil.setTagValue("PRIMARY_AID_FCI", TagValue.fromString(string8));
                TagsMapUtil.setTagValue("ALIAS_AID_FCI", TagValue.fromString(string9));
                return;
            }
            DOLTag dOLTag = TLVParser.parseTLV(arrby, n2, arrby.length, true);
            if (dOLTag == null) {
                throw new HCEClientException(OperationStatus.TLV_PARSING_FAILURE);
            }
            int n3 = n2 + dOLTag.getSkipLen();
            if (!dOLTag.isConstructed()) {
                String string10;
                if (dOLTag.getTagName().compareToIgnoreCase("9F38") == 0 && string.compareToIgnoreCase("") != 0) {
                    string10 = string5 + string;
                    by2 = (byte)(by2 - (3 + dOLTag.getTagLength()));
                } else {
                    String string11 = string5 + dOLTag.getTagName().toUpperCase();
                    String string12 = string11 + HexUtils.short2Hex(dOLTag.getTagLength()).toUpperCase();
                    string10 = string12 + dOLTag.getTagValue().toUpperCase();
                }
                int n4 = n3 + dOLTag.getTagLength();
                by3 = by2;
                string6 = string10;
                n2 = n4;
            } else {
                String string13 = string5 + dOLTag.getTagName().toUpperCase();
                String string14 = string13 + HexUtils.short2Hex(dOLTag.getTagLength()).toUpperCase();
                by3 = by2;
                string6 = string14;
                n2 = n3;
            }
            string5 = string6;
            by2 = by3;
        } while (true);
    }

    private void setCVN() {
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_MS_DGI"), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_TAG"), true);
        TagValue tagValue2 = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_EMV_DGI"), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_TAG"), true);
        if (tagValue != null) {
            TagsMapUtil.setTagValue("MS_CVN_VALUE", TagValue.fromString(HexUtils.nBytesFromHexString(tagValue.getValue(), 2, 1)));
        }
        if (tagValue2 != null) {
            TagsMapUtil.setTagValue("EMV_CVN_VALUE", TagValue.fromString(HexUtils.nBytesFromHexString(tagValue2.getValue(), 2, 1)));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setServiceCdMSOffset() {
        String string = TagsMapUtil.getDGIValue(CPDLConfig.getDGI_TAG("SFI1_REC1_DGI_MS"));
        if (string == null) return;
        {
            byte[] arrby = HexUtils.hexStringToByteArray(string);
            int n2 = 0;
            block3 : while (n2 < arrby.length) {
                boolean bl;
                int n3;
                short s2;
                int n4;
                short s3;
                int n5;
                if ((32 & arrby[n2]) == 32) {
                    n4 = n2;
                    bl = true;
                } else {
                    n4 = n2;
                    bl = false;
                }
                while (arrby[n4] == 0 || arrby[n4] == -1) {
                    if (n4 >= -2 + arrby.length) {
                        throw new HCEClientException("core-hceclient", "::TokenPersoDelegate::setServiceCdMSOffset::Malformed Record Data");
                    }
                    ++n4;
                }
                if ((byte)(31 & arrby[n4]) == 31) {
                    short s4 = HexUtils.getShort(arrby, n4);
                    n5 = n4 + 2;
                    s3 = s4;
                } else {
                    n5 = n4 + 1;
                    s3 = (short)(255 & arrby[n4]);
                }
                if (arrby[n5] == -127) {
                    int n6 = n5 + 1;
                    short s5 = (short)(255 & arrby[n6]);
                    n3 = n6 + 1;
                    s2 = s5;
                } else if (arrby[n5] == -126) {
                    int n7 = n5 + 1;
                    short s6 = HexUtils.getShort(arrby, n7);
                    n3 = n7 + 2;
                    s2 = s6;
                } else {
                    int n8 = n5 + 1;
                    short s7 = (short)(255 & arrby[n5]);
                    n3 = n8;
                    s2 = s7;
                }
                switch (s3) {
                    default: {
                        if (bl) break;
                        n2 = n3 + s2;
                        continue block3;
                    }
                    case 87: {
                        TagsMapUtil.setTagValue("SERVICE_CODE_MS_OFFSET", TagValue.fromString(String.valueOf((int)(1 + HexUtils.byteArrayToHexString(arrby, n3, s2).toUpperCase().indexOf(68) + (4 + n3 * 2)))));
                        return;
                    }
                }
                n2 = n3;
            }
        }
    }

    private void tokenActivation() {
        PaymentUtils.setTokenStatus(StateMode.ACTIVE);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void buildMetaData(TokenDataHolder tokenDataHolder) {
        Map<TagKey, TagValue> map = tokenDataHolder.getTagsMap();
        boolean bl = this.mstDataAvailable(tokenDataHolder);
        List<TagKey> list = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        List<TagKey> list2 = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
        MetaDataManager.setMetaDataValue("MAX_ATC", TagValue.fromString(((NFCLUPCTagValue)tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), false)).getAtc()));
        MetaDataManager.setMetaDataValue("LUPC_COUNT", TagValue.fromString(String.valueOf((int)list.size())));
        boolean bl2 = list != null;
        if (bl) {
            bl2 = list != null && list2 != null && list.size() == list2.size();
        }
        int n2 = 0;
        if (!bl2) {
            Log.e((String)"core-hceclient", (String)"::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
            throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
        }
        do {
            if (n2 >= list.size()) {
                this.updateMetaDataMap(tokenDataHolder);
                this.executeLUPCThreshold(tokenDataHolder);
                return;
            }
            TagKey tagKey = (TagKey)list.get(n2);
            MetaDataManager.getMetaDataMap().put((Object)tagKey, map.remove((Object)tagKey));
            if (bl) {
                TagKey tagKey2 = (TagKey)list2.get(n2);
                MetaDataManager.getMetaDataMap().put((Object)tagKey2, map.remove((Object)tagKey2));
            }
            ++n2;
        } while (true);
    }

    protected void cleanDGIMap(TokenDataHolder tokenDataHolder) {
        try {
            tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"));
            if (this.isPersoReqd(tokenDataHolder)) {
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getENC_DGI_TAG("ICC_CRT_dP1"));
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getENC_DGI_TAG("ICC_CRT_dQ1"));
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getENC_DGI_TAG("ICC_CRT_P"));
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getENC_DGI_TAG("ICC_CRT_Q"));
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getENC_DGI_TAG("ICC_CRT_PQ"));
            }
            if (this.mstDataAvailable(tokenDataHolder)) {
                tokenDataHolder.getDgisMap().remove((Object)CPDLConfig.getDGI_TAG("MST_LUPC_DGI"));
            }
            return;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("cleanDGIMap::error::" + hCEClientException.getMessage()));
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void doOperation() {
        block14 : {
            block13 : {
                var1_1 = SessionManager.getSession();
                try {
                    var2_2 = new SecureComponentImpl();
                    var5_3 = (TokenDataHolder)var1_1.getValue("TOKEN_DATA_HOLDER", false);
                    this.invokeInit(var5_3);
                    if (this.isPersoReqd(var5_3)) {
                        var32_4 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dP1"));
                        var33_5 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dQ1"));
                        var34_6 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_P"));
                        var35_7 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_Q"));
                        var36_8 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_PQ"));
                        var37_9 = "0";
                        if (var5_3.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_MOD"))) {
                            var37_9 = '2' + var5_3.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_MOD"));
                        }
                        var38_10 = var5_3.getTagValue(CPDLConfig.getDGI_TAG("ICC_DYNAMIC_AUTHENTICATION_DGI"), CPDLConfig.getDGI_TAG("ICC_PUBLIC_KEY_EXPONENT_TAG"), true);
                        var39_11 = LLVARUtil.objectsToLLVar(new Object[]{var32_4, var33_5, var34_6, var35_7, var36_8, var37_9, '2' + var38_10.getValue()});
                        var40_12 = Integer.parseInt((String)var5_3.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), true).getValue(), (int)16);
                        var41_13 = RandomNumberGenerator.random(8);
                        var42_14 = Utility.byteArrayToHexString(var41_13);
                        var43_15 = new TagValue();
                        var43_15.setValue(var42_14);
                        var5_3.getTagsMap().put((Object)TagsMapUtil.getTagKey("LOCK_CODE"), (Object)var43_15);
                        this.checkSCStatus(var2_2.perso(var40_12, var39_11, var41_13));
                    }
                    var6_16 = this.mstDataAvailable(var5_3);
                    var7_17 = new TagValue();
                    var7_17.setValue(String.valueOf((boolean)var6_16));
                    var5_3.getTagsMap().put((Object)TagsMapUtil.getTagKey("MST_SUPPORTED"), (Object)var7_17);
                    var9_18 = var5_3.getTagList(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
                    var10_19 = var5_3.getTagList(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
                    var11_20 = var9_18 != null;
                    var12_21 = 4;
                    if (var6_16) {
                        var31_22 = var9_18 != null && var10_19 != null && var9_18.size() == var10_19.size();
                        var11_20 = var31_22;
                        var12_21 = 5;
                    }
                    if (var11_20) {
                        var13_23 = new String[var12_21 * var9_18.size()];
                        var14_24 = null;
                        var15_25 = 0;
                        var16_26 = 0;
                    } else {
                        Log.e((String)"core-hceclient", (String)"::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
                        throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
                    }
lbl44: // 2 sources:
                    do {
                        if (var16_26 >= var9_18.size()) break;
                        var22_27 = (NFCLUPCTagValue)var5_3.getTagValue((TagKey)var9_18.get(var16_26));
                        if (var6_16) {
                            var14_24 = (MSTLUPCTagValue)var5_3.getTagValue((TagKey)var10_19.get(var16_26));
                            if (!var22_27.getAtc().equals((Object)var14_24.getAtc())) {
                                throw new HCEClientException(OperationStatus.NFC_ATC_AND_MST_ATC_MISMATCH);
                            }
                        }
                        var23_28 = var14_24;
                        var24_29 = var22_27.getLupc();
                        var25_30 = var15_25 + 1;
                        var13_23[var15_25] = '2' + Integer.toHexString((int)(var24_29.length() / 2));
                        var26_31 = var25_30 + 1;
                        var13_23[var25_30] = '2' + var24_29;
                        var27_32 = var26_31 + 1;
                        var13_23[var26_31] = '2' + var22_27.getKcv();
                        var28_33 = var27_32 + 1;
                        var13_23[var27_32] = '2' + var22_27.getAtc();
                        if (!var6_16) break block13;
                        var29_34 = var28_33 + 1;
                        var13_23[var28_33] = '2' + var23_28.getMstDynamicData();
                        break block14;
                        break;
                    } while (true);
                }
                catch (HCEClientException var3_35) {
                    Log.e((String)"core-hceclient", (String)("::TokenPersoDelegate::catch::" + var3_35.getMessage()));
                    throw var3_35;
                }
                this.checkSCStatus(var2_2.update(LLVARUtil.objectsToLLVar(var13_23)));
                var17_36 = HashUtils.computeSHA256(var5_3.getTlsClearTokenData());
                var18_37 = new Object[]{'1' + var17_36};
                var19_38 = LLVARUtil.objectsToLLVar(var18_37);
                var20_39 = new byte[this.computeDestBufferSize(var19_38.length)];
                this.checkSCStatus(var2_2.getSignatureData(var19_38, var20_39));
                if (var2_2.isRetryExecuted()) {
                    var20_39 = var2_2.getDestBuffer();
                }
                var1_1.setValue("TOKEN_DATA_SIGNATURE", LLVARUtil.llVarToObjects(var20_39)[0].toString());
                this.buildMetaData(var5_3);
                this.cleanDGIMap(var5_3);
                this.setDataContext(var5_3);
                this.setTokenConfiguration();
                this.setServiceCdMSOffset();
                this.setCVN();
                this.prepareFCIResponse();
                this.tokenActivation();
                return;
            }
            var29_34 = var28_33;
        }
        ++var16_26;
        var15_25 = var29_34;
        var14_24 = var23_28;
        ** while (true)
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void executeLUPCThreshold(TokenDataHolder tokenDataHolder) {
        int n2;
        List<TagKey> list = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        List<TagKey> list2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
        int n3 = Integer.parseInt((String)HexUtils.nBytesFromHexString(MetaDataManager.getMetaDataValue("LUPC_THRESHOLD"), 1, 1), (int)16);
        boolean bl = this.mstDataAvailable(tokenDataHolder);
        boolean bl2 = list != null;
        if (bl) {
            bl2 = list != null && list2 != null && list.size() == list2.size();
        }
        if (!bl2) {
            Log.e((String)"core-hceclient", (String)"::TokenPersoDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
            throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
        }
        int n4 = list.size();
        if (n4 > n3) {
            int n5 = n4 - n3;
            for (int i2 = 0; i2 < n5; ++i2) {
                MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true));
            }
        }
        if (bl && (n2 = list2.size()) > n3) {
            int n6 = n2 - n3;
            for (int i3 = 0; i3 < n6; ++i3) {
                MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"), true));
            }
        }
        MetaDataManager.setMetaDataValue("RUNNING_ATC", TagValue.fromString(((NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true)).getAtc()));
        MetaDataManager.setMetaDataValue("MAX_ATC", TagValue.fromString(((NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), false)).getAtc()));
    }

    protected byte[] getXPMConfigBytes(TokenDataHolder tokenDataHolder) {
        return HexUtils.hexStringToByteArray(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), true).getValue());
    }

    protected void invokeInit(TokenDataHolder tokenDataHolder) {
        TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("STARTING_ATC_TAG"), true);
        String string = '2' + tagValue.getValue();
        TagValue tagValue2 = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("EMV_AIP_DGI"), CPDLConfig.getDGI_TAG("EMV_AIP_TAG"), true);
        String string2 = "20000";
        if (tagValue2 != null) {
            string2 = '2' + tagValue2.getValue();
        }
        TagValue tagValue3 = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("CRYPTOGRAM_INFORMATION_DATA_TAG"), true);
        String string3 = '2' + tagValue3.getValue();
        TagValue tagValue4 = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("LUPC_THRESHOLD_TAG"), true);
        String string4 = '2' + HexUtils.nBytesFromHexString(tagValue4.getValue(), 1, 1);
        String string5 = "200";
        if (this.mstDataAvailable(tokenDataHolder)) {
            TagValue tagValue5 = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("LUPC_THRESHOLD_TAG"), true);
            string5 = '2' + HexUtils.nBytesFromHexString(tagValue5.getValue(), 1, 1);
        }
        byte[] arrby = LLVARUtil.objectsToLLVar(string, string2, "201", string3, string4, string5);
        this.checkSCStatus(new SecureComponentImpl().init(arrby));
    }

    protected boolean isPersoReqd(TokenDataHolder tokenDataHolder) {
        TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), true);
        }
        return tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dP1")) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dQ1")) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_P")) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_Q")) && tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_PQ")) && tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("ICC_DYNAMIC_AUTHENTICATION_DGI")) && tagValue != null && tagValue.getValue() != null;
    }

    protected boolean mstDataAvailable(TokenDataHolder tokenDataHolder) {
        byte by = (byte)(16 & this.getXPMConfigBytes(tokenDataHolder)[4]);
        boolean bl = false;
        if (by == 16) {
            boolean bl2 = tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"));
            bl = false;
            if (bl2) {
                bl = true;
            }
        }
        return bl;
    }

    protected void setDataContext(TokenDataHolder tokenDataHolder) {
        DataContext dataContext = (DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false);
        dataContext.getDgiMap().putAll(tokenDataHolder.getDgisMap());
        dataContext.getTagMap().putAll(tokenDataHolder.getTagsMap());
        this.cleanTokenDataHolder(tokenDataHolder);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setTokenConfiguration() {
        boolean bl = true;
        Session session = SessionManager.getSession();
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("ISSUER_COUNTRY_CODE"), bl);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("ISSUER_COUNTRY_CODE"), bl);
        }
        session.setValue("ISSUER_COUNTRY_CODE", tagValue.getValue());
        byte[] arrby = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), bl).getValue());
        boolean bl2 = (byte)(16 & arrby[3]) == 16 ? bl : false;
        session.setValue("INAPP_SUPPORTED", String.valueOf((boolean)bl2));
        if ((byte)(8 & arrby[4]) != 8) {
            bl = false;
        }
        session.setValue("TAP_PAYMENT_SUPPORTED", String.valueOf((boolean)bl));
    }

    protected void updateMetaDataMap(TokenDataHolder tokenDataHolder) {
        MetaDataManager.setMetaDataValue("RUNNING_ATC", tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("STARTING_ATC_TAG"), false));
        MetaDataManager.setMetaDataValue("TOKEN_DATA_VERSION", tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_DATA_VERSION_TAG"), false));
        MetaDataManager.setMetaDataValue("LUPC_THRESHOLD", tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("LUPC_THRESHOLD_TAG"), false));
        MetaDataManager.setMetaDataValue("TOKEN_STATUS", tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_STATUS_TAG"), true));
    }
}

