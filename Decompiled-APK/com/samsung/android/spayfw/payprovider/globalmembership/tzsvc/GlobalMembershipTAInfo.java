package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccTAInfo;
import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAInfo;

/* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.d */
public class GlobalMembershipTAInfo extends TAInfo {
    public static final GlobalMembershipCommands zC;

    static {
        zC = new GlobalMembershipCommands();
    }

    public GlobalMembershipTAInfo() {
        super(5, TAInfo.SPAY_TA_TECH_TEE, GlobalMembershipTAController.class, zC, KrccTAInfo.CONFIG_LSI_UUID, KrccTAInfo.CONFIG_LSI_PATH, SpayTuiTAInfo.TA_INFO_QC_ROOT, KrccTAInfo.CONFIG_QC_PROCESS, KrccTAInfo.CONFIG_QC_PATH, true);
    }
}
