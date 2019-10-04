/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;

public class TokenSetCDCVMDelegate
extends OperationDelegate {
    private static final int CVM_METHOD_0 = 0;
    private static final int CVM_METHOD_1 = 1;
    private static final int CVM_METHOD_2 = 2;
    private static final int CVM_METHOD_3 = 3;
    private static final int CVM_METHOD_4 = 4;
    private static final int CVM_METHOD_5 = 5;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        var1_1 = SessionManager.getSession();
        var5_2 = (byte[])var1_1.getValue("CDCVM_BLOB", true);
        if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") != 31357) ** GOTO lbl10
        throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
        {
            catch (HCEClientException var3_3) {}
            Log.e((String)"core-hceclient", (String)("::TokenSetCDCVMDelegate::catch::" + var3_3.getMessage()));
            throw var3_3;
lbl10: // 1 sources:
            var6_5 = new SecureComponentImpl();
            var7_6 = new byte[1];
            this.checkSCStatus(var6_5.verify(var5_2, var7_6));
            ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_STATUS", (byte)-102);
            switch (var7_6[0]) {
                case 0: 
                case 4: {
                    ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_STATUS", (byte)117);
                    return;
                }
                case 1: {
                    ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_TYPE", (byte)35);
                    return;
                }
                case 2: {
                    ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_TYPE", (byte)-104);
                    return;
                }
                case 3: {
                    ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_TYPE", (byte)81);
                    return;
                }
                case 5: {
                    ApplicationInfoManager.setApplcationInfoValue("TR_MOB_CVM_TYPE", (byte)70);
                }
            }
            return;
        }
    }
}

