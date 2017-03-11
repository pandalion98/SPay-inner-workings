package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback.Stub;

public abstract class GlobalMembershipCardRegisterCallback implements PaymentFrameworkConnection {
    private GlobalMembershipCardRegisterCallback cb;
    private PFGlobalMembershipCardRegisterCallback pfCb;

    private class PFGlobalMembershipCardRegisterCallback extends Stub {
        private PFGlobalMembershipCardRegisterCallback() {
        }

        public void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardRegisterCallback.this.cb);
            GlobalMembershipCardRegisterCallback.this.cb.onSuccess(globalMembershipCardRegisterResponseData);
        }

        public void onFail(int i, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardRegisterCallback.this.cb);
            GlobalMembershipCardRegisterCallback.this.cb.onFail(i, globalMembershipCardRegisterResponseData);
        }
    }

    public abstract void onFail(int i, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData);

    public abstract void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData);

    public GlobalMembershipCardRegisterCallback() {
        this.cb = this;
        this.pfCb = new PFGlobalMembershipCardRegisterCallback();
    }

    IGlobalMembershipCardRegisterCallback getPFGlobalMembershipCardRegisterCb() {
        return this.pfCb;
    }
}
