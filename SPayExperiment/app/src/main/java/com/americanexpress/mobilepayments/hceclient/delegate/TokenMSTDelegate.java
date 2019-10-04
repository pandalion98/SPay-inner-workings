/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Byte
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentCoreImpl;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TokenMSTDelegate
extends OperationDelegate {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void doOperation() {
        if (PaymentUtils.checkXPMConfig((byte)4, (byte)16) == Constants.MAGIC_FALSE) throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        if (117 != (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS")) ** GOTO lbl9
        throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        {
            catch (HCEClientException var2_1) {}
            Log.e((String)"core-hceclient", (String)("::TokenMSTDelegate::catch::" + var2_1.getMessage()));
            throw var2_1;
lbl9: // 1 sources:
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            if (StateMode.SUSPEND == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
        }
        var4_3 = DataContext.getSessionInstance();
        var5_4 = TagsMapUtil.getTagValue(var4_3.getTagMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TRACK1"), false);
        var6_5 = TagsMapUtil.getTagValue(var4_3.getTagMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TRACK2"), false);
        if (var5_4 == null) throw new HCEClientException("Static part of Track Data is missing");
        if (var6_5 == null) {
            throw new HCEClientException("Static part of Track Data is missing");
        }
        var7_6 = HexUtils.hexStringToByteArray(var5_4.getValue());
        var8_7 = HexUtils.hexStringToByteArray(var6_5.getValue());
        var9_8 = new byte[32];
        var10_9 = (MSTLUPCTagValue)ApplicationInfoManager.getApplcationInfoValue("TR_MST_LUPC_OBJ");
        var11_10 = var10_9 != null ? Utility.byteArrayToInt(HexUtils.hexStringToByteArray(var10_9.getAtc())) : Utility.byteArrayToInt(HexUtils.hexStringToByteArray(MetaDataManager.getMetaDataValue("RUNNING_ATC")));
        if (var11_10 > 65535) {
            throw new HCEClientException("Maximum ATC value reached");
        }
        var12_11 = new Object[]{var11_10};
        var13_12 = String.format((String)"%05d", (Object[])var12_11).getBytes();
        System.arraycopy((Object)var13_12, (int)0, (Object)var7_6, (int)39, (int)var13_12.length);
        System.arraycopy((Object)var13_12, (int)0, (Object)var8_7, (int)33, (int)var13_12.length);
        this.checkSCStatus(new SecureComponentCoreImpl().reqMST(var7_6, var8_7, var9_8));
        PaymentUtils.removeUsedLUPCAndAdvanceATC();
        ApplicationInfoManager.setApplcationInfoValue("TID", HexUtils.byteArrayToHexString(var9_8));
    }
}

