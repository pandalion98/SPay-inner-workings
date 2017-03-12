package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverGPOResponse;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.c */
public class GPORApdu extends ResponseApdu {
    public GPORApdu(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        ByteBuffer a = BERTLV.m1001a((byte) EMVSetStatusApdu.RESET_LOWEST_PRIORITY, byteBuffer);
        a.append(BERTLV.m1001a((byte) -108, byteBuffer2));
        m926r(BERTLV.m1001a((byte) ApplicationInfoManager.TERM_XP2, a));
    }

    public GPORApdu(DiscoverGPOResponse discoverGPOResponse) {
        m926r(BERTLV.m1001a((byte) ApplicationInfoManager.TERM_XP2, discoverGPOResponse.dW()));
    }
}
