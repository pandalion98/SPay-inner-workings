/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.CardProfileParser;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class SecureChannelUnwrapDelegate
extends OperationDelegate {
    /*
     * Enabled aggressive block sorting
     */
    private boolean checkRecordsForAFL(TokenDataHolder tokenDataHolder, byte[] arrby, boolean bl) {
        int n2 = 0;
        while (n2 < arrby.length) {
            int n3 = n2 + 1;
            byte by = (byte)(arrby[n2] >> 3);
            int n4 = n3 + 1;
            byte by2 = arrby[n3];
            int n5 = n4 + 1;
            byte by3 = arrby[n4];
            for (byte by4 = by2; by4 <= by3; by4 = (byte)(by4 + 1)) {
                String string = Utility.byte2Hex(by) + Utility.byte2Hex(by4);
                if (string.compareToIgnoreCase("0101") == 0) {
                    string = bl ? CPDLConfig.getDGI_TAG("SFI1_REC1_DGI_EMV") : CPDLConfig.getDGI_TAG("SFI1_REC1_DGI_MS");
                }
                if (tokenDataHolder.containsDGI(string)) continue;
                Log.e((String)"core-hceclient", (String)("::SecureChannelUnwrapDelegate::checkRecordsForAFL::Record " + string + " not found corresponding to AFL"));
                return false;
            }
            n2 = n5 + 1;
        }
        return true;
    }

    private void invokeCloudDataUnwrap() {
        Session session = SessionManager.getSession();
        String string = (String)session.getValue("TOKEN_DATA", true);
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        byte[] arrby = string.getBytes();
        byte[] arrby2 = new byte[1024 + arrby.length];
        this.checkSCStatus(secureComponentImpl.unwrap(0, arrby, arrby.length, arrby2, arrby2.length));
        if (secureComponentImpl.isRetryExecuted()) {
            arrby2 = secureComponentImpl.getDestBuffer();
        }
        String string2 = LLVARUtil.llVarToObjects(arrby2)[0].toString();
        TokenDataHolder tokenDataHolder = new CardProfileParser().parseTokenData(string2);
        session.setValue("TOKEN_DATA_HOLDER", tokenDataHolder);
        tokenDataHolder.setTlsClearTokenData(string2);
        Map<String, String> map = tokenDataHolder.getDgisMap();
        Map<TagKey, TagValue> map2 = tokenDataHolder.getTagsMap();
        JSONUtil.toJSONString(map);
        JSONUtil.toJSONString(map2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isPersoValid(TokenDataHolder tokenDataHolder) {
        int n2;
        if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("EMV_AIP_DGI")) && tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("MS_AIP_DGI"))) {
            this.runAFLChecks(tokenDataHolder, "EMV_AIP_DGI", "EMV_AFL_TAG", true);
            this.runAFLChecks(tokenDataHolder, "MS_AIP_DGI", "MS_AFL_TAG", false);
            n2 = 57;
        } else if (tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("EMV_AIP_DGI"))) {
            this.runAFLChecks(tokenDataHolder, "EMV_AIP_DGI", "EMV_AFL_TAG", true);
            n2 = -73;
        } else {
            if (!tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("MS_AIP_DGI"))) {
                Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::GPO NOT PRESENT::");
                return false;
            }
            this.runAFLChecks(tokenDataHolder, "MS_AIP_DGI", "MS_AFL_TAG", false);
            n2 = -63;
        }
        if (n2 == -73 || n2 == 57) {
            if (!tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_EMV_DGI"))) {
                Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::EMV IAD is missing::");
                return false;
            }
            byte[] arrby = HexUtils.hexStringToByteArray((String)tokenDataHolder.getDgisMap().get((Object)CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_EMV_DGI")));
            if (arrby.length < 11) {
                Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::EMV IAD Length not correct::");
                return false;
            }
            if (arrby[10] != 1 && arrby[10] != 2) {
                Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::IAD Format is not correct::");
                return false;
            }
        }
        if (!(n2 != -63 && n2 != 57 || tokenDataHolder.containsDGI(CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_MS_DGI")))) {
            Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::IAD not present::");
            return false;
        }
        if (tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), true) == null) {
            Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::isPersoValid::XPM CONFIGURATION NOT PRESENT::");
            return false;
        }
        return true;
    }

    private boolean runAFLChecks(TokenDataHolder tokenDataHolder, String string, String string2, boolean bl) {
        TagValue tagValue = TagsMapUtil.getTagValue(tokenDataHolder.getTagsMap(), CPDLConfig.getDGI_TAG(string), CPDLConfig.getDGI_TAG(string2), true);
        if (tagValue == null) {
            Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::runAFLChecks::GPO Response DGI inconsistent::");
            return false;
        }
        if (!this.checkRecordsForAFL(tokenDataHolder, HexUtils.hexStringToByteArray(tagValue.getValue()), bl)) {
            Log.e((String)"core-hceclient", (String)"::SecureChannelUnwrapDelegate::runAFLChecks::AFL Inconsistent with record data present in perso::");
            return false;
        }
        return true;
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
        var1_1 = SessionManager.getSession();
        this.invokeCloudDataUnwrap();
        var5_2 = this.isPersoValid((TokenDataHolder)var1_1.getValue("TOKEN_DATA_HOLDER", false));
        if (!((Boolean)var1_1.getValue("IS_ALREADY_PROVSIONED", false)).booleanValue() || !var5_2) ** GOTO lbl11
        throw new HCEClientException(OperationStatus.TOKEN_REF_ID_ALREADY_PROVISIONED);
        {
            catch (HCEClientException var3_3) {}
            Log.e((String)"core-hceclient", (String)("::SecureChannelUnwrapDelegate::catch::" + var3_3.getMessage()));
            throw var3_3;
lbl11: // 1 sources:
            var1_1.setValue("IS_PROVISIONING_FLOW", var5_2);
            return;
        }
    }
}

