/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import java.util.List;

public abstract class GlobalMembershipCardExtractDetailCallback
implements PaymentFrameworkConnection {
    private GlobalMembershipCardExtractDetailCallback cb = this;
    private PFGlobalMembershipCardExtractDetailCallback pfCb = new PFGlobalMembershipCardExtractDetailCallback();

    IGlobalMembershipCardExtractDetailCallback getPFGlobalMembershipCardExtractDetailCb() {
        return this.pfCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(List<GlobalMembershipCardDetail> var1);

    private class PFGlobalMembershipCardExtractDetailCallback
    extends IGlobalMembershipCardExtractDetailCallback.Stub {
        private PFGlobalMembershipCardExtractDetailCallback() {
        }

        @Override
        public void onFail(int n2) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardExtractDetailCallback.this.cb);
            GlobalMembershipCardExtractDetailCallback.this.cb.onFail(n2);
        }

        @Override
        public void onSuccess(List<GlobalMembershipCardDetail> list) {
            PaymentFramework.removeFromTrackMap(GlobalMembershipCardExtractDetailCallback.this.cb);
            GlobalMembershipCardExtractDetailCallback.this.cb.onSuccess(list);
        }
    }

}

