package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.contextservice.p029b.CSlog;
import java.util.ArrayList;

/* renamed from: com.samsung.contextservice.server.f */
class PolicyConnPolicy extends RemoteConnPolicy<PolicyRequest> {
    private final ArrayList<Integer> GP;
    private final int GQ;
    private final long GR;

    public PolicyConnPolicy(Context context, String str, long j, int i) {
        super(context, str, 1, "policygap", "policycap");
        this.GP = new ArrayList();
        this.GR = j;
        this.GQ = i;
        CSlog.m1408d("RemoteConnPolicy", "cap=" + this.GQ + ", minDelay=" + this.GR);
    }

    protected long gt() {
        return this.GR;
    }

    protected int gu() {
        return this.GQ;
    }

    protected synchronized void execute() {
        if (m1423e(System.currentTimeMillis(), (long) gu())) {
            if (gy()) {
                CSlog.m1408d("RemoteConnPolicy", "policy polling reach maximum, cap=" + gu());
            } else {
                while (!isEmpty()) {
                    try {
                        RequestBundle gx = gx();
                        if (gx != null) {
                            PolicyRequest policyRequest = (PolicyRequest) gx.getRequest();
                            C0413a gz = gx.gz();
                            if (!(policyRequest == null || gz == null)) {
                                policyRequest.m836a(gz);
                                CSlog.m1408d("RemoteConnPolicy", "policy queue items number:" + size() + ", lastPing=" + gw());
                                clear();
                                break;
                            }
                        }
                    } catch (Throwable e) {
                        CSlog.m1403a("RemoteConnPolicy", "policy execute,", e);
                    }
                }
            }
        }
    }
}
