/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.session.Session;
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
import java.util.List;
import java.util.Map;

public class PaymentUtils {
    public static short checkXPMConfig(byte by, byte by2) {
        byte[] arrby = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(((DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false)).getTagMap(), CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), false).getValue());
        if (arrby == null) {
            throw new HCEClientException("checkXPMConfig::XPM Configuration missing");
        }
        if ((byte)(by2 & arrby[by]) == by2) {
            return Constants.MAGIC_TRUE;
        }
        return Constants.MAGIC_FALSE;
    }

    public static StateMode getTokenStatus() {
        return StateMode.getStateMode(Utility.hex2decimal(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_STATUS_TAG"), true).getValue()));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void removeUsedLUPCAndAdvanceATC() {
        block6 : {
            block7 : {
                block5 : {
                    List<TagKey> list;
                    if (ApplicationInfoManager.getApplcationInfoValue("TR_NFC_LUPC_OBJ") != null || ApplicationInfoManager.getApplcationInfoValue("TR_MST_LUPC_OBJ") != null) break block5;
                    List<TagKey> list2 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"));
                    List<TagKey> list3 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"));
                    if (list2 != null && list2.size() > 0) {
                        ApplicationInfoManager.setApplcationInfoValue("TR_NFC_LUPC_OBJ", (NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true));
                        MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true));
                    }
                    if (list3 != null && list3.size() > 0) {
                        ApplicationInfoManager.setApplcationInfoValue("TR_MST_LUPC_OBJ", (MSTLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"), true));
                        MetaDataManager.removeMetaDataKey(TagsMapUtil.getTagKey(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"), true));
                    }
                    if ((list = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"))) == null || list.size() <= 0) break block6;
                    NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), true);
                    if (nFCLUPCTagValue == null) break block5;
                    MetaDataManager.setMetaDataValue("RUNNING_ATC", TagValue.fromString(nFCLUPCTagValue.getAtc()));
                    if (nFCLUPCTagValue.getAtc().compareToIgnoreCase("FFFF") == 0) break block7;
                }
                return;
            }
            PaymentUtils.setTokenStatus(StateMode.TERMINATE);
            return;
        }
        ApplicationInfoManager.setApplcationInfoValue("BLOCK_IN_CLOSE", Constants.MAGIC_TRUE);
    }

    public static short resetCVR(byte by, byte by2) {
        byte[] arrby = (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_CVR");
        if (arrby == null) {
            return Constants.MAGIC_FALSE;
        }
        arrby[by] = (byte)(arrby[by] & (byte)(~by2));
        ApplicationInfoManager.setApplcationInfoValue("TR_CVR", arrby);
        return Constants.MAGIC_TRUE;
    }

    public static short setCVR(byte by, byte by2) {
        byte[] arrby = (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_CVR");
        if (arrby == null) {
            return Constants.MAGIC_FALSE;
        }
        arrby[by] = (byte)(by2 | arrby[by]);
        ApplicationInfoManager.setApplcationInfoValue("TR_CVR", arrby);
        return Constants.MAGIC_TRUE;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setCVRandCVMBasedOnCDCVMSTatus() {
        block11 : {
            block10 : {
                if (PaymentUtils.checkXPMConfig((byte)4, (byte)1) != Constants.MAGIC_TRUE) break block10;
                if (-102 == (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS")) {
                    PaymentUtils.setMobCVMResByte((byte)0, (byte)1);
                    PaymentUtils.setMobCVMResByte((byte)2, (byte)2);
                    switch ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_TYPE")) {
                        case -104: {
                            PaymentUtils.setCVR((byte)1, (byte)4);
                            PaymentUtils.resetCVR((byte)1, (byte)2);
                            break;
                        }
                        case 35: 
                        case 81: {
                            PaymentUtils.setCVR((byte)2, (byte)1);
                            PaymentUtils.resetCVR((byte)1, (byte)4);
                            PaymentUtils.resetCVR((byte)1, (byte)2);
                            break;
                        }
                        case 70: {
                            PaymentUtils.setCVR((byte)2, (byte)1);
                            PaymentUtils.resetCVR((byte)1, (byte)4);
                            PaymentUtils.setCVR((byte)1, (byte)2);
                            break;
                        }
                    }
                } else {
                    PaymentUtils.setMobCVMResByte((byte)0, (byte)63);
                    PaymentUtils.setMobCVMResByte((byte)2, (byte)0);
                }
                if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_REQ") != Constants.MAGIC_TRUE) break block11;
                PaymentUtils.setMobCVMResByte((byte)1, (byte)3);
            }
            return;
        }
        PaymentUtils.setMobCVMResByte((byte)1, (byte)0);
    }

    public static short setMobCVMResByte(byte by, byte by2) {
        byte[] arrby = (byte[])ApplicationInfoManager.getApplcationInfoValue("MOBILE_CVM_RESULTS");
        if (arrby == null) {
            return Constants.MAGIC_FALSE;
        }
        arrby[by] = by2;
        ApplicationInfoManager.setApplcationInfoValue("MOBILE_CVM_RESULTS", arrby);
        return Constants.MAGIC_TRUE;
    }

    public static void setOperationStatusInSession(OperationStatus operationStatus) {
        Session session = SessionManager.getSession();
        session.getValue("OPERATION_STATUS", true);
        session.setValue("OPERATION_STATUS", (Object)operationStatus);
    }

    public static void setTokenStatus(StateMode stateMode) {
        if (StateMode.TERMINATE != PaymentUtils.getTokenStatus() && StateMode.DELETE != PaymentUtils.getTokenStatus()) {
            DataContext dataContext = DataContext.getSessionInstance();
            TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_STATUS_TAG"), true);
            TagKey tagKey = TagsMapUtil.getTagKey(dataContext.getTagMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_STATUS_TAG"), true);
            String string = Utility.int2Hex(stateMode.getLcmState());
            TagsMapUtil.setTagValue(tagKey, TagValue.fromString(string));
            MetaDataManager.getMetaDataValue("TOKEN_STATUS");
            MetaDataManager.setMetaDataValue("TOKEN_STATUS", TagValue.fromString(string));
        }
    }
}

