/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenPersoDelegate;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TokenPersoChildDelegate
extends TokenPersoDelegate {
    @Override
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
        TagValue tagValue6 = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("DEVICE_ID_TAG"), true);
        byte[] arrby = LLVARUtil.objectsToLLVar(string, string2, "201", string3, string4, string5, '2' + tagValue6.getValue());
        this.checkSCStatus(new SecureComponentImpl().init(arrby));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
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
}

