/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Boolean
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenPersoDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.RandomNumberGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;

public class TokenUpdateDelegate
extends TokenPersoDelegate {
    private void compareATC(TokenDataHolder tokenDataHolder) {
        if (Integer.parseInt((String)((NFCLUPCTagValue)TagsMapUtil.getTagValue(tokenDataHolder.getTagsMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true)).getAtc(), (int)16) <= Integer.parseInt((String)MetaDataManager.getMetaDataValue("MAX_ATC"), (int)16)) {
            throw new HCEClientException(OperationStatus.NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        boolean bl;
        Session session;
        TokenDataHolder tokenDataHolder;
        SecureComponentImpl secureComponentImpl;
        block13 : {
            session = SessionManager.getSession();
            try {
                secureComponentImpl = new SecureComponentImpl();
                tokenDataHolder = (TokenDataHolder)session.getValue("TOKEN_DATA_HOLDER", false);
                this.compareATC(tokenDataHolder);
                bl = this.mstDataAvailable(tokenDataHolder);
                boolean bl2 = Boolean.valueOf((String)TagsMapUtil.getTagValue("MST_SUPPORTED"));
                if (bl2) {
                    if (!bl) throw new HCEClientException(OperationStatus.MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA);
                }
                if (bl2 || !bl) break block13;
            }
            catch (HCEClientException hCEClientException) {
                Log.e((String)"core-hceclient", (String)("::TokenUpdateDelegate::catch::" + hCEClientException.getMessage()));
                throw hCEClientException;
            }
            throw new HCEClientException(OperationStatus.MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA);
        }
        if (this.isPersoReqd(tokenDataHolder)) {
            String string = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dP1"));
            String string2 = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_dQ1"));
            String string3 = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_P"));
            String string4 = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_Q"));
            String string5 = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_PQ"));
            String string6 = "0";
            if (tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG("ICC_CRT_MOD"))) {
                string6 = '2' + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG("ICC_CRT_MOD"));
            }
            TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("ICC_DYNAMIC_AUTHENTICATION_DGI"), CPDLConfig.getDGI_TAG("ICC_PUBLIC_KEY_EXPONENT_TAG"), true);
            byte[] arrby = LLVARUtil.objectsToLLVar(string, string2, string3, string4, string5, string6, '2' + tagValue.getValue());
            int n2 = Integer.parseInt((String)TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), true).getValue(), (int)16);
            byte[] arrby2 = RandomNumberGenerator.random(8);
            String string7 = Utility.byteArrayToHexString(arrby2);
            TagValue tagValue2 = new TagValue();
            tagValue2.setValue(string7);
            TagsMapUtil.setTagValue("LOCK_CODE", tagValue2);
            this.checkSCStatus(secureComponentImpl.perso(n2, arrby, arrby2));
        }
        List<TagKey> list = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
        List<TagKey> list2 = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
        boolean bl3 = list != null;
        int n3 = 4;
        if (bl) {
            boolean bl4 = list != null && list2 != null && list.size() == list2.size();
            bl3 = bl4;
            n3 = 5;
        }
        if (!bl3) {
            Log.e((String)"core-hceclient", (String)"::TokenUpdateDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
            throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
        }
        Object[] arrobject = new String[n3 * list.size()];
        MSTLUPCTagValue mSTLUPCTagValue = null;
        int n4 = 0;
        int n5 = 0;
        do {
            int n6;
            MSTLUPCTagValue mSTLUPCTagValue2;
            block16 : {
                int n7;
                block15 : {
                    block14 : {
                        if (n5 >= list.size()) break block14;
                        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)tokenDataHolder.getTagValue((TagKey)list.get(n5));
                        if (bl) {
                            mSTLUPCTagValue = (MSTLUPCTagValue)tokenDataHolder.getTagValue((TagKey)list2.get(n5));
                            if (!nFCLUPCTagValue.getAtc().equals((Object)mSTLUPCTagValue.getAtc())) {
                                throw new HCEClientException(OperationStatus.NFC_ATC_AND_MST_ATC_MISMATCH);
                            }
                        }
                        mSTLUPCTagValue2 = mSTLUPCTagValue;
                        String string = nFCLUPCTagValue.getLupc();
                        int n8 = n4 + 1;
                        arrobject[n4] = '2' + Integer.toHexString((int)(string.length() / 2));
                        int n9 = n8 + 1;
                        arrobject[n8] = '2' + string;
                        int n10 = n9 + 1;
                        arrobject[n9] = '2' + nFCLUPCTagValue.getKcv();
                        n7 = n10 + 1;
                        arrobject[n10] = '2' + nFCLUPCTagValue.getAtc();
                        if (!bl) break block15;
                        n6 = n7 + 1;
                        arrobject[n7] = '2' + mSTLUPCTagValue2.getMstDynamicData();
                        break block16;
                    }
                    this.checkSCStatus(secureComponentImpl.update(LLVARUtil.objectsToLLVar(arrobject)));
                    String string = HashUtils.computeSHA256(tokenDataHolder.getTlsClearTokenData());
                    Object[] arrobject2 = new Object[]{'1' + string};
                    byte[] arrby = LLVARUtil.objectsToLLVar(arrobject2);
                    byte[] arrby3 = new byte[this.computeDestBufferSize(arrby.length)];
                    this.checkSCStatus(secureComponentImpl.getSignatureData(arrby, arrby3));
                    if (secureComponentImpl.isRetryExecuted()) {
                        arrby3 = secureComponentImpl.getDestBuffer();
                    }
                    session.setValue("TOKEN_DATA_SIGNATURE", LLVARUtil.llVarToObjects(arrby3)[0].toString());
                    this.buildMetaData(tokenDataHolder);
                    this.cleanDGIMap(tokenDataHolder);
                    this.setDataContext(tokenDataHolder);
                    List<TagKey> list3 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
                    if (list3 == null) return;
                    if (list3.size() <= 0) return;
                    if (StateMode.BLOCKED != PaymentUtils.getTokenStatus()) {
                        if (StateMode.SUSPEND != PaymentUtils.getTokenStatus()) return;
                    }
                    PaymentUtils.setTokenStatus(StateMode.ACTIVE);
                    return;
                }
                n6 = n7;
            }
            ++n5;
            n4 = n6;
            mSTLUPCTagValue = mSTLUPCTagValue2;
        } while (true);
    }

    @Override
    protected byte[] getXPMConfigBytes(TokenDataHolder tokenDataHolder) {
        return HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), true).getValue());
    }

    @Override
    protected void updateMetaDataMap(TokenDataHolder tokenDataHolder) {
        TagValue tagValue = new TagValue();
        tagValue.setValue("false");
        MetaDataManager.getMetaDataMap().put((Object)TagsMapUtil.getTagKey("REFRESH_REQUIRED"), (Object)tagValue);
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true);
        TagValue tagValue2 = new TagValue();
        tagValue2.setValue(nFCLUPCTagValue.getAtc());
        MetaDataManager.getMetaDataMap().put((Object)TagsMapUtil.getTagKey("RUNNING_ATC"), (Object)tagValue2);
    }
}

