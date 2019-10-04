/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Byte
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.TIDGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class TokenInAppDelegate
extends OperationDelegate {
    private static final String AMOUNT_AUTHORIZED = "000000000000";
    private static final String AMOUNT_OTHER = "000000000000";
    private static final String APPLICATION_INTERCHANGE_PROFILE = "0000";
    private static final String TERMINAL_COUNTRY_CODE = "0000";
    private static final String TERMINAL_VERIFICATION_RESULTS = "8000000000";
    private static final String TRANSACTION_CURRENCY_CODE = "0000";
    private static final String TRANSACTION_DATE = "140101";
    private static final String TRANSACTION_TYPE = "00";

    private byte[] computeCVRBytes() {
        ApplicationInfoManager.setApplcationInfoValue("TR_CVR", new byte[]{3, -128, 0, 0});
        PaymentUtils.setCVRandCVMBasedOnCDCVMSTatus();
        return (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_CVR");
    }

    private String computeCryptoInputData(String string, String string2, String string3) {
        String string4 = "00000000000000000000000000008000000000000014010100" + string;
        return Utility.int2Hex(string4.length() / 2) + string4 + "0000" + string2 + string3;
    }

    private byte[] computeCryptogram(String string) {
        Session session = SessionManager.getSession();
        byte[] arrby = HexUtils.hexStringToByteArray(string);
        byte[] arrby2 = new byte[8];
        int n2 = new SecureComponentImpl().computeAC(arrby, arrby2);
        this.checkSCStatus(n2);
        if (n2 < 0) {
            throw new HCEClientException(27013);
        }
        if (n2 == 65535) {
            PaymentUtils.setTokenStatus(StateMode.TERMINATE);
            Log.e((String)"core-hceclient", (String)"::TokenInAppDelegate::HIGHEST ATC REACHED - APPLICATION TERMINATED!!");
            throw new HCEClientException(27013);
        }
        ApplicationInfoManager.setApplcationInfoValue("APPLICATION_CYPTOGRAM", HexUtils.byteArrayToHexString(arrby2));
        session.setValue("INAPP_USED_ATC", n2);
        return arrby2;
    }

    private byte[] computeInAppPayload(byte[] arrby, String string) {
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        byte[] arrby2 = HexUtils.hexStringToByteArray(string);
        byte[] arrby3 = new byte[65];
        byte[] arrby4 = new byte[20];
        byte[] arrby5 = this.computeTokenInfo();
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby3, (int)0, (int)arrby5.length);
        int n2 = 0 + arrby5.length;
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)n2, (int)arrby.length);
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)(n2 + arrby.length), (int)arrby4.length);
        this.checkSCStatus(secureComponentImpl.reqInApp(arrby2, arrby3));
        return arrby3;
    }

    private byte[] computeTokenDataBlockA(byte[] arrby, String string, String string2, String string3) {
        Session session = SessionManager.getSession();
        byte[] arrby2 = new byte[20];
        arrby2[0] = 38;
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)1, (int)arrby.length);
        int n2 = 1 + arrby.length;
        byte[] arrby3 = HexUtils.hexStringToByteArray(string);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n2, (int)arrby3.length);
        int n3 = n2 + arrby3.length;
        byte[] arrby4 = HexUtils.hexStringToByteArray(string2);
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)n3, (int)arrby4.length);
        int n4 = n3 + arrby4.length;
        byte[] arrby5 = HexUtils.hexStringToByteArray(string3);
        System.arraycopy((Object)arrby5, (int)1, (Object)arrby2, (int)n4, (int)(-1 + arrby5.length));
        int n5 = -1 + (n4 + arrby5.length);
        String string4 = MetaDataManager.getDKIForATC(String.format((String)"%04x", (Object[])new Object[]{(Integer)session.getValue("INAPP_USED_ATC", true)}));
        if (string4 == null) {
            throw new HCEClientException(27013);
        }
        arrby2[n5] = HexUtils.hexStringToByteArray(string4)[0];
        int n6 = n5 + 1;
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("PAN_SEQUENCE_NUMBER"), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("PAN_SEQUENCE_NUMBER"), true);
        }
        arrby2[n6] = HexUtils.hexStringToByteArray(tagValue.getValue())[0];
        return arrby2;
    }

    private byte[] computeTokenInfo() {
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), true);
        }
        String string = tagValue.getValue();
        String string2 = string.substring(0, -1 + string.length());
        TagValue tagValue2 = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("TOKEN_EXPIRY"), true);
        if (tagValue2 == null) {
            tagValue2 = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("TOKEN_EXPIRY"), true);
        }
        String string3 = tagValue2.getValue();
        return (string2 + string3).getBytes();
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
        if (PaymentUtils.checkXPMConfig((byte)3, (byte)16) == Constants.MAGIC_FALSE) throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        if (117 != (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS")) ** GOTO lbl10
        throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        {
            catch (HCEClientException var3_2) {}
            Log.e((String)"core-hceclient", (String)("::TokenInAppDelegate::catch::" + var3_2.getMessage()));
            throw var3_2;
lbl10: // 1 sources:
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
        }
        var5_4 = (String)ApplicationInfoManager.getApplcationInfoValue("UNPREDICTABLE_NUMBER");
        var6_5 = (String)ApplicationInfoManager.getApplcationInfoValue("TRANSACTION_CONTEXT");
        var7_6 = MetaDataManager.getMetaDataValue("RUNNING_ATC");
        var8_7 = HexUtils.hexByteArrayToString(this.computeCVRBytes());
        var9_8 = this.computeTokenDataBlockA(this.computeCryptogram(this.computeCryptoInputData(var5_4, var7_6, var8_7)), var5_4, var7_6, var8_7);
        PaymentUtils.removeUsedLUPCAndAdvanceATC();
        var1_1.setValue("INAPP_PAYLOAD", HexUtils.byteArrayToHexString(this.computeInAppPayload(var9_8, var6_5)));
        ApplicationInfoManager.setApplcationInfoValue("TID", TIDGenerator.getTransactionID((byte)83));
    }
}

