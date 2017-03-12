package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;

public class PaymentUtils {
    public static short setCVR(byte b, byte b2) {
        byte[] bArr = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CVR);
        if (bArr == null) {
            return Constants.MAGIC_FALSE;
        }
        bArr[b] = (byte) (bArr[b] | b2);
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CVR, bArr);
        return Constants.MAGIC_TRUE;
    }

    public static short resetCVR(byte b, byte b2) {
        byte[] bArr = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CVR);
        if (bArr == null) {
            return Constants.MAGIC_FALSE;
        }
        bArr[b] = (byte) (bArr[b] & ((byte) (b2 ^ -1)));
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CVR, bArr);
        return Constants.MAGIC_TRUE;
    }

    public static short checkXPMConfig(byte b, byte b2) {
        byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(((DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false)).getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), false).getValue());
        if (hexStringToByteArray == null) {
            throw new HCEClientException("checkXPMConfig::XPM Configuration missing");
        } else if (((byte) (hexStringToByteArray[b] & b2)) == b2) {
            return Constants.MAGIC_TRUE;
        } else {
            return Constants.MAGIC_FALSE;
        }
    }

    public static short setMobCVMResByte(byte b, byte b2) {
        byte[] bArr = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.MOBILE_CVM_RESULTS);
        if (bArr == null) {
            return Constants.MAGIC_FALSE;
        }
        bArr[b] = b2;
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.MOBILE_CVM_RESULTS, bArr);
        return Constants.MAGIC_TRUE;
    }

    public static void removeUsedLUPCAndAdvanceATC() {
        if (ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_NFC_LUPC_OBJ) == null && ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MST_LUPC_OBJ) == null) {
            List tagList = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
            List tagList2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
            if (tagList != null && tagList.size() > 0) {
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_NFC_LUPC_OBJ, (NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true));
                MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true));
            }
            if (tagList2 != null && tagList2.size() > 0) {
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MST_LUPC_OBJ, (MSTLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG), true));
                MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG), true));
            }
            tagList = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
            if (tagList == null || tagList.size() <= 0) {
                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.BLOCK_IN_CLOSE, Short.valueOf(Constants.MAGIC_TRUE));
                return;
            }
            NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true);
            if (nFCLUPCTagValue != null) {
                MetaDataManager.setMetaDataValue(MetaDataManager.RUNNING_ATC, TagValue.fromString(nFCLUPCTagValue.getAtc()));
                if (nFCLUPCTagValue.getAtc().compareToIgnoreCase("FFFF") == 0) {
                    setTokenStatus(StateMode.TERMINATE);
                }
            }
        }
    }

    public static void setTokenStatus(StateMode stateMode) {
        if (StateMode.TERMINATE != getTokenStatus() && StateMode.DELETE != getTokenStatus()) {
            DataContext sessionInstance = DataContext.getSessionInstance();
            TagsMapUtil.getTagValue(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_STATUS_TAG), true);
            TagKey tagKey = TagsMapUtil.getTagKey(sessionInstance.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_STATUS_TAG), true);
            String int2Hex = Utility.int2Hex(stateMode.getLcmState());
            TagsMapUtil.setTagValue(tagKey, TagValue.fromString(int2Hex));
            MetaDataManager.getMetaDataValue(MetaDataManager.TOKEN_STATUS);
            MetaDataManager.setMetaDataValue(MetaDataManager.TOKEN_STATUS, TagValue.fromString(int2Hex));
        }
    }

    public static StateMode getTokenStatus() {
        return StateMode.getStateMode(Utility.hex2decimal(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_STATUS_TAG), true).getValue()));
    }

    public static void setOperationStatusInSession(OperationStatus operationStatus) {
        Session session = SessionManager.getSession();
        session.getValue(SessionConstants.OPERATION_STATUS, true);
        session.setValue(SessionConstants.OPERATION_STATUS, operationStatus);
    }

    public static void setCVRandCVMBasedOnCDCVMSTatus() {
        if (checkXPMConfig((byte) 4, (byte) 1) == Constants.MAGIC_TRUE) {
            if (-102 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                setMobCVMResByte((byte) 0, (byte) 1);
                setMobCVMResByte((byte) 2, (byte) 2);
                switch (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE)).byteValue()) {
                    case PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CATEGORY /*-104*/:
                        setCVR((byte) 1, (byte) 4);
                        resetCVR((byte) 1, (byte) 2);
                        break;
                    case ExtensionType.session_ticket /*35*/:
                    case EACTags.FILE_REFERENCE /*81*/:
                        setCVR((byte) 2, (byte) 1);
                        resetCVR((byte) 1, (byte) 4);
                        resetCVR((byte) 1, (byte) 2);
                        break;
                    case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA /*70*/:
                        setCVR((byte) 2, (byte) 1);
                        resetCVR((byte) 1, (byte) 4);
                        setCVR((byte) 1, (byte) 2);
                        break;
                }
            }
            setMobCVMResByte((byte) 0, (byte) 63);
            setMobCVMResByte((byte) 2, (byte) 0);
            if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ)).shortValue() == Constants.MAGIC_TRUE) {
                setMobCVMResByte((byte) 1, (byte) 3);
            } else {
                setMobCVMResByte((byte) 1, (byte) 0);
            }
        }
    }
}
