package com.americanexpress.mobilepayments.hceclient.context;

import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import java.util.Map;

public class ApplicationInfoManager {
    public static final byte AMOUNT_STATUS_HIGH = (byte) -7;
    public static final byte AMOUNT_STATUS_LOW = (byte) -45;
    public static final byte AMOUNT_STATUS_UNKNOWN = (byte) -68;
    public static final String APPLICATION_CYPTOGRAM = "APPLICATION_CYPTOGRAM";
    public static final short APP_ALIAS = (short) -31176;
    public static final short APP_NONE = (short) -26794;
    public static final short APP_PRIMARY = (short) 9509;
    public static final String BLOCK_IN_CLOSE = "BLOCK_IN_CLOSE";
    public static final String EMV_CDOL_LIST = "EMV_CDOL_LIST";
    public static final byte EMV_MS = (byte) 57;
    public static final byte EMV_ONLY = (byte) -73;
    public static final String MOBILE_CVM_RESULTS = "MOBILE_CVM_RESULTS";
    public static final byte MOB_CVM_NOT_PERFORMED = (byte) 117;
    public static final byte MOB_CVM_PERFORMED = (byte) -102;
    public static final byte MOB_CVM_TYP_DEV_FINGERPRINT = (byte) 70;
    public static final byte MOB_CVM_TYP_DEV_PASSCODE = (byte) 35;
    public static final byte MOB_CVM_TYP_DEV_PASSWORD = (byte) 81;
    public static final byte MOB_CVM_TYP_DEV_PATTERN = (byte) 53;
    public static final byte MOB_CVM_TYP_MPVV = (byte) -104;
    public static final byte MOB_CVM_TYP_UNSPECIFIED = (byte) 104;
    public static final String MS_CDOL_LIST = "MS_CDOL_LIST";
    public static final byte MS_ONLY = (byte) -63;
    public static final String PDOL_LIST = "PDOL_LIST";
    public static final String SDK_CONTEXT_DATA_LEN = "SDK_CONTEXT_DATA_LEN";
    public static final short STATE_APP_COMPLETED = (short) 26772;
    public static final short STATE_APP_IDLE = (short) 14161;
    public static final short STATE_APP_INITIATED = (short) 31357;
    public static final short STATE_APP_SELECTED = (short) 13141;
    public static final short STATE_PPSE_SELECTED = (short) -23174;
    public static final byte TERMINAL_MODE_CL_EMV = (byte) 83;
    public static final byte TERMINAL_MODE_CL_MS = (byte) 66;
    public static final byte TERMINAL_MODE_NONE = (byte) 120;
    public static final byte TERM_XP1 = (byte) 88;
    public static final byte TERM_XP2 = (byte) 119;
    public static final byte TERM_XP3 = (byte) 51;
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

    public static Object getApplcationInfoValue(String str) {
        return ((DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false)).getAppInfoMap().get(str);
    }

    public static Object getApplcationInfoValue(String str, boolean z) {
        Map appInfoMap = ((DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false)).getAppInfoMap();
        Object obj = appInfoMap.get(str);
        if (z) {
            appInfoMap.remove(str);
        }
        return obj;
    }

    public static Object setApplcationInfoValue(String str, Object obj) {
        return ((DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false)).getAppInfoMap().put(str, obj);
    }

    public static void initTransient() {
        setApplcationInfoValue(TR_APP_STATE, Short.valueOf(STATE_APP_IDLE));
        setApplcationInfoValue(TR_MOB_CVM_STATUS, Byte.valueOf(MOB_CVM_NOT_PERFORMED));
        setApplcationInfoValue(TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_FALSE));
        setApplcationInfoValue(MOBILE_CVM_RESULTS, new byte[]{(byte) 0, (byte) 0, (byte) 0});
        setApplcationInfoValue(TR_TERMINAL_MODE, Byte.valueOf(TERMINAL_MODE_NONE));
        setApplcationInfoValue(TR_TXN_AMNT_STATUS, Byte.valueOf(AMOUNT_STATUS_UNKNOWN));
        setApplcationInfoValue(TR_ABORT_IN_GAC, Short.valueOf(Constants.MAGIC_FALSE));
        PaymentUtils.setOperationStatusInSession(OperationStatus.NO_FURTHER_ACTION_REQUIRED);
        setApplcationInfoValue(TID, "0000000000000000000000000000000000000000000000000000000000000000");
        setApplcationInfoValue(TR_RETURN_AMNT_IN_GAC, Short.valueOf(Constants.MAGIC_FALSE));
        DataContext dataContext = (DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false);
        String str = (String) dataContext.getDgiMap().get(CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI));
        String str2 = (String) dataContext.getDgiMap().get(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI));
        if (str != null && str2 != null) {
            setApplcationInfoValue(TR_APP_CAPABILITY, Byte.valueOf(EMV_MS));
        } else if (str == null && str2 != null) {
            setApplcationInfoValue(TR_APP_CAPABILITY, Byte.valueOf(EMV_ONLY));
        } else if (str != null && str2 == null) {
            setApplcationInfoValue(TR_APP_CAPABILITY, Byte.valueOf(MS_ONLY));
        }
        setApplcationInfoValue(TR_CUR_PPSE_RES_OBJ, null);
        setApplcationInfoValue(TR_NFC_LUPC_OBJ, null);
        setApplcationInfoValue(TR_MST_LUPC_OBJ, null);
        setApplcationInfoValue(BLOCK_IN_CLOSE, Short.valueOf(Constants.MAGIC_FALSE));
    }
}
