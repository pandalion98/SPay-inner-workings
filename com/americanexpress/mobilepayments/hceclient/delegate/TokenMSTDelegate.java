package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentCoreImpl;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class TokenMSTDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            if (PaymentUtils.checkXPMConfig((byte) 4, Tnaf.POW_2_WIDTH) == Constants.MAGIC_FALSE || 117 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else if (StateMode.BLOCKED == PaymentUtils.getTokenStatus() || StateMode.SUSPEND == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            } else {
                DataContext sessionInstance = DataContext.getSessionInstance();
                TagValue tagValue = TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TRACK1), false);
                TagValue tagValue2 = TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TRACK2), false);
                if (tagValue == null || tagValue2 == null) {
                    throw new HCEClientException("Static part of Track Data is missing");
                }
                int byteArrayToInt;
                Object hexStringToByteArray = HexUtils.hexStringToByteArray(tagValue.getValue());
                Object hexStringToByteArray2 = HexUtils.hexStringToByteArray(tagValue2.getValue());
                byte[] bArr = new byte[32];
                MSTLUPCTagValue mSTLUPCTagValue = (MSTLUPCTagValue) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MST_LUPC_OBJ);
                if (mSTLUPCTagValue != null) {
                    byteArrayToInt = Utility.byteArrayToInt(HexUtils.hexStringToByteArray(mSTLUPCTagValue.getAtc()));
                } else {
                    byteArrayToInt = Utility.byteArrayToInt(HexUtils.hexStringToByteArray(MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC)));
                }
                if (byteArrayToInt > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                    throw new HCEClientException("Maximum ATC value reached");
                }
                Object bytes = String.format("%05d", new Object[]{Integer.valueOf(byteArrayToInt)}).getBytes();
                System.arraycopy(bytes, 0, hexStringToByteArray, 39, bytes.length);
                System.arraycopy(bytes, 0, hexStringToByteArray2, 33, bytes.length);
                checkSCStatus(new SecureComponentCoreImpl().reqMST(hexStringToByteArray, hexStringToByteArray2, bArr));
                PaymentUtils.removeUsedLUPCAndAdvanceATC();
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TID, HexUtils.byteArrayToHexString(bArr));
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenMSTDelegate::catch::" + e.getMessage());
            throw e;
        }
    }
}
