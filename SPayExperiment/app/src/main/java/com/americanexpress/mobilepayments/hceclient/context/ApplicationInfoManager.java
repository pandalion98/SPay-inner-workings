/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import java.util.Map;

public class ApplicationInfoManager {
    public static final byte AMOUNT_STATUS_HIGH = -7;
    public static final byte AMOUNT_STATUS_LOW = -45;
    public static final byte AMOUNT_STATUS_UNKNOWN = -68;
    public static final String APPLICATION_CYPTOGRAM = "APPLICATION_CYPTOGRAM";
    public static final short APP_ALIAS = -31176;
    public static final short APP_NONE = -26794;
    public static final short APP_PRIMARY = 9509;
    public static final String BLOCK_IN_CLOSE = "BLOCK_IN_CLOSE";
    public static final String EMV_CDOL_LIST = "EMV_CDOL_LIST";
    public static final byte EMV_MS = 57;
    public static final byte EMV_ONLY = -73;
    public static final String MOBILE_CVM_RESULTS = "MOBILE_CVM_RESULTS";
    public static final byte MOB_CVM_NOT_PERFORMED = 117;
    public static final byte MOB_CVM_PERFORMED = -102;
    public static final byte MOB_CVM_TYP_DEV_FINGERPRINT = 70;
    public static final byte MOB_CVM_TYP_DEV_PASSCODE = 35;
    public static final byte MOB_CVM_TYP_DEV_PASSWORD = 81;
    public static final byte MOB_CVM_TYP_DEV_PATTERN = 53;
    public static final byte MOB_CVM_TYP_MPVV = -104;
    public static final byte MOB_CVM_TYP_UNSPECIFIED = 104;
    public static final String MS_CDOL_LIST = "MS_CDOL_LIST";
    public static final byte MS_ONLY = -63;
    public static final String PDOL_LIST = "PDOL_LIST";
    public static final String SDK_CONTEXT_DATA_LEN = "SDK_CONTEXT_DATA_LEN";
    public static final short STATE_APP_COMPLETED = 26772;
    public static final short STATE_APP_IDLE = 14161;
    public static final short STATE_APP_INITIATED = 31357;
    public static final short STATE_APP_SELECTED = 13141;
    public static final short STATE_PPSE_SELECTED = -23174;
    public static final byte TERMINAL_MODE_CL_EMV = 83;
    public static final byte TERMINAL_MODE_CL_MS = 66;
    public static final byte TERMINAL_MODE_NONE = 120;
    public static final byte TERM_XP1 = 88;
    public static final byte TERM_XP2 = 119;
    public static final byte TERM_XP3 = 51;
    public static final String TID = "TID";
    public static final String TRANSACTION_CONTEXT = "TRANSACTION_CONTEXT";
    public static final String TR_ABORT_IN_GAC = "TR_ABORT_IN_GAC";
    public static final String TR_ACTIVE_AFL = "TR_ACTIVE_AFL";
    public static final String TR_ACTIVE_AIP = "TR_ACTIVE_AIP";
    public static final String TR_ACTIVE_APP = "TR_ACTIVE_APP";
    public static final String TR_APP_CAPABILITY = "TR_APP_CAPABILITY";
    public static final String TR_APP_STATE = "TR_APP_STATE";
    public static final String TR_CDA_TXN_HASH_OBJ = "TR_CDA_TXN_HASH_OBJ";
    public static final String TR_CUR_PPSE_RES_OBJ = "TR_CUR_PPSE_RES_OBJ";
    public static final String TR_CVR = "TR_CVR";
    public static final String TR_MOB_CVM_REQ = "TR_MOB_CVM_REQ";
    public static final String TR_MOB_CVM_STATUS = "TR_MOB_CVM_STATUS";
    public static final String TR_MOB_CVM_TYPE = "TR_MOB_CVM_TYPE";
    public static final String TR_MST_LUPC_OBJ = "TR_MST_LUPC_OBJ";
    public static final String TR_NFC_LUPC_OBJ = "TR_NFC_LUPC_OBJ";
    public static final String TR_RETURN_AMNT_IN_GAC = "TR_RETURN_AMNT_IN_GAC";
    public static final String TR_TERMINAL_MODE = "TR_TERMINAL_MODE";
    public static final String TR_TERMINAL_ONLINE_CAPABILITY = "TR_TERMINAL_ONLINE_CAPABILITY";
    public static final String TR_TERMINAL_TYPE = "TR_TERMINAL_TYPE";
    public static final String TR_TXN_AMNT_STATUS = "TR_TXN_AMNT_STATUS";
    public static final String UNPREDICTABLE_NUMBER = "UNPREDICTABLE_NUMBER";

    public static Object getApplcationInfoValue(String string) {
        return ((DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false)).getAppInfoMap().get((Object)string);
    }

    public static Object getApplcationInfoValue(String string, boolean bl) {
        Map<String, Object> map = ((DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false)).getAppInfoMap();
        Object object = map.get((Object)string);
        if (bl) {
            map.remove((Object)string);
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void initTransient() {
        ApplicationInfoManager.setApplcationInfoValue(TR_APP_STATE, (short)14161);
        ApplicationInfoManager.setApplcationInfoValue(TR_MOB_CVM_STATUS, (byte)117);
        ApplicationInfoManager.setApplcationInfoValue(TR_MOB_CVM_REQ, Constants.MAGIC_FALSE);
        ApplicationInfoManager.setApplcationInfoValue(MOBILE_CVM_RESULTS, new byte[]{0, 0, 0});
        ApplicationInfoManager.setApplcationInfoValue(TR_TERMINAL_MODE, (byte)120);
        ApplicationInfoManager.setApplcationInfoValue(TR_TXN_AMNT_STATUS, (byte)-68);
        ApplicationInfoManager.setApplcationInfoValue(TR_ABORT_IN_GAC, Constants.MAGIC_FALSE);
        PaymentUtils.setOperationStatusInSession(OperationStatus.NO_FURTHER_ACTION_REQUIRED);
        ApplicationInfoManager.setApplcationInfoValue(TID, "0000000000000000000000000000000000000000000000000000000000000000");
        ApplicationInfoManager.setApplcationInfoValue(TR_RETURN_AMNT_IN_GAC, Constants.MAGIC_FALSE);
        DataContext dataContext = (DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false);
        String string = (String)dataContext.getDgiMap().get((Object)CPDLConfig.getDGI_TAG("MS_AIP_DGI"));
        String string2 = (String)dataContext.getDgiMap().get((Object)CPDLConfig.getDGI_TAG("EMV_AIP_DGI"));
        if (string != null && string2 != null) {
            ApplicationInfoManager.setApplcationInfoValue(TR_APP_CAPABILITY, (byte)57);
        } else if (string == null && string2 != null) {
            ApplicationInfoManager.setApplcationInfoValue(TR_APP_CAPABILITY, (byte)-73);
        } else if (string != null && string2 == null) {
            ApplicationInfoManager.setApplcationInfoValue(TR_APP_CAPABILITY, (byte)-63);
        }
        ApplicationInfoManager.setApplcationInfoValue(TR_CUR_PPSE_RES_OBJ, null);
        ApplicationInfoManager.setApplcationInfoValue(TR_NFC_LUPC_OBJ, null);
        ApplicationInfoManager.setApplcationInfoValue(TR_MST_LUPC_OBJ, null);
        ApplicationInfoManager.setApplcationInfoValue(BLOCK_IN_CLOSE, Constants.MAGIC_FALSE);
    }

    public static Object setApplcationInfoValue(String string, Object object) {
        return ((DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false)).getAppInfoMap().put((Object)string, object);
    }
}

