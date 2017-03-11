package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback.Stub;
import java.util.List;

public abstract class GlobalMembershipCardExtractDetailCallback implements PaymentFrameworkConnection {
    private GlobalMembershipCardExtractDetailCallback cb;
    private PFGlobalMembershipCardExtractDetailCallback pfCb;

    private class PFGlobalMembershipCardExtractDetailCallback extends Stub {
        private PFGlobalMembershipCardExtractDetailCallback() {
        }

        public void onSuccess(List<GlobalMembershipCardDetail> list) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardExtractDetailCallback.this.cb);
            GlobalMembershipCardExtractDetailCallback.this.cb.onSuccess(list);
        }

        public void onFail(int i) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardExtractDetailCallback.this.cb);
            GlobalMembershipCardExtractDetailCallback.this.cb.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(List<GlobalMembershipCardDetail> list);

    public GlobalMembershipCardExtractDetailCallback() {
        this.cb = this;
        this.pfCb = new PFGlobalMembershipCardExtractDetailCallback();
    }

    IGlobalMembershipCardExtractDetailCallback getPFGlobalMembershipCardExtractDetailCb() {
        return this.pfCb;
    }
}
