/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class GlobalMembershipCardRegisterCallback
implements PaymentFrameworkConnection {
    private GlobalMembershipCardRegisterCallback cb = this;
    private PFGlobalMembershipCardRegisterCallback pfCb = new PFGlobalMembershipCardRegisterCallback();

    IGlobalMembershipCardRegisterCallback getPFGlobalMembershipCardRegisterCb() {
        return this.pfCb;
    }

    public abstract void onFail(int var1, GlobalMembershipCardRegisterResponseData var2);

    public abstract void onSuccess(GlobalMembershipCardRegisterResponseData var1);

    private class PFGlobalMembershipCardRegisterCallback
    extends IGlobalMembershipCardRegisterCallback.Stub {
        private PFGlobalMembershipCardRegisterCallback() {
        }

        @Override
        public void onFail(int n2, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardRegisterCallback.this.cb);
            GlobalMembershipCardRegisterCallback.this.cb.onFail(n2, globalMembershipCardRegisterResponseData);
        }

        @Override
        public void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardRegisterCallback.this.cb);
            GlobalMembershipCardRegisterCallback.this.cb.onSuccess(globalMembershipCardRegisterResponseData);
        }
    }

}

