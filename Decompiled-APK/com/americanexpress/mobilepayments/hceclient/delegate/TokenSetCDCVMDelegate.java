package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;

public class TokenSetCDCVMDelegate extends OperationDelegate {
    private static final int CVM_METHOD_0 = 0;
    private static final int CVM_METHOD_1 = 1;
    private static final int CVM_METHOD_2 = 2;
    private static final int CVM_METHOD_3 = 3;
    private static final int CVM_METHOD_4 = 4;
    private static final int CVM_METHOD_5 = 5;

    public void doOperation() {
        try {
            byte[] bArr = (byte[]) SessionManager.getSession().getValue(SessionConstants.CDCVM_BLOB, true);
            if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() == ApplicationInfoManager.STATE_APP_INITIATED) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            byte[] bArr2 = new byte[CVM_METHOD_1];
            checkSCStatus(new SecureComponentImpl().verify(bArr, bArr2));
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS, Byte.valueOf(ApplicationInfoManager.MOB_CVM_PERFORMED));
            switch (bArr2[CVM_METHOD_0]) {
                case CVM_METHOD_0 /*0*/:
                case CVM_METHOD_4 /*4*/:
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS, Byte.valueOf(ApplicationInfoManager.MOB_CVM_NOT_PERFORMED));
                case CVM_METHOD_1 /*1*/:
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE, Byte.valueOf(ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE));
                case CVM_METHOD_2 /*2*/:
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE, Byte.valueOf(ApplicationInfoManager.MOB_CVM_TYP_MPVV));
                case CVM_METHOD_3 /*3*/:
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE, Byte.valueOf(ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD));
                case CVM_METHOD_5 /*5*/:
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE, Byte.valueOf(ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT));
                default:
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenSetCDCVMDelegate::catch::" + e.getMessage());
            throw e;
        }
    }
}
