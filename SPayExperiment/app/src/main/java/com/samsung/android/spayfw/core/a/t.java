/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.h;
import com.samsung.android.spayfw.core.a.m;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.o;
import com.samsung.android.spayfw.core.q;
import java.util.Arrays;

public class t
extends m {
    private static t mi;
    private boolean mj = false;

    private t(Context context, n n2) {
        super(context, n2);
    }

    public static final h a(Context context, n n2) {
        Class<t> class_ = t.class;
        synchronized (t.class) {
            if (mi == null) {
                mi = new t(context, n2);
            }
            t t2 = mi;
            // ** MonitorExit[var4_2] (shouldn't be in output)
            return t2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void a(ICommonCallback iCommonCallback) {
        t t2 = this;
        synchronized (t2) {
            if (this.mj) {
                com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "No user initiated Payment to stop, Tap N Go is in progress");
                iCommonCallback.onSuccess(null);
            } else {
                super.a(iCommonCallback);
            }
            return;
        }
    }

    protected boolean bf() {
        return o.p(1);
    }

    protected void bg() {
        block3 : {
            block2 : {
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "Restricted Payment select ppse, TapNGo case");
                if (!o.p(1)) break block2;
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "Restricted Payment NPAY_READY state");
                c c2 = super.X();
                if (c2 == null || c2.ac() == null) break block3;
                super.a(c2.ac().getTokenId(), new b(), true);
            }
            return;
        }
        com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "Restricted Payment No default card avail");
    }

    protected void bh() {
        com.samsung.android.spayfw.b.c.i("TapAndGoDecorator", "startRestrictedPayment time = " + System.currentTimeMillis());
        PayConfig payConfig = new PayConfig();
        payConfig.setPayType(1);
        payConfig.setMstTransmitTime(2000);
        super.a(null, payConfig, new a(), true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void clearCard() {
        t t2 = this;
        synchronized (t2) {
            if (this.mj) {
                com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "clearCard(): tapNGo card is in progress, nothing to clear ");
            } else {
                super.clearCard();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public byte[] processApdu(byte[] arrby, Bundle bundle) {
        t t2 = this;
        synchronized (t2) {
            h h2;
            h h3 = h2 = this.ly;
            synchronized (h3) {
                if (arrby == null) {
                    com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "command apdu is null");
                    return com.samsung.android.spayfw.core.b.iR;
                }
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "processApdu");
                if (n.a(arrby, (short)0) != 164) return super.processApdu(arrby, bundle);
                if (!Arrays.equals((byte[])com.samsung.android.spayfw.core.b.iO, (byte[])arrby)) return super.processApdu(arrby, bundle);
                if (!this.bf()) return super.processApdu(arrby, bundle);
                if (!com.samsung.android.spayfw.utils.h.ap(this.mContext)) {
                    this.bg();
                    return super.processApdu(arrby, bundle);
                }
                c c2 = super.X();
                if (c2 == null || c2.ac() == null || c2.ac().getTokenId() == null) {
                    com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "No default card - Samsung pay activity in foreground");
                    return super.processApdu(arrby, bundle);
                }
                com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "abort Tap n go - Other Samsung pay activity in foreground");
                Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                intent.putExtra("notiType", "tapNGoState");
                intent.putExtra("tokenId", c2.ac().getTokenId());
                intent.putExtra("status", -106);
                PaymentFrameworkApp.a(intent);
                return com.samsung.android.spayfw.core.b.iS;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public ApduReasonCode u(int n2) {
        t t2 = this;
        synchronized (t2) {
            h h2;
            h h3 = h2 = this.ly;
            synchronized (h3) {
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "onDeactivated");
                ApduReasonCode apduReasonCode = super.u(n2);
                if (apduReasonCode != null) return apduReasonCode;
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "onDeactivated null");
                return null;
            }
        }
    }

    private class a
    extends IPayCallback.Stub {
        private a() {
        }

        @Override
        public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
            com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "TapNGoPayCallback onExtractGiftCardDetail");
        }

        @Override
        public void onFail(String string, int n2) {
            com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "TapNGoPayCallback onFail " + string + " error: " + n2);
            t.this.mj = false;
        }

        @Override
        public void onFinish(String string, int n2, ApduReasonCode apduReasonCode) {
            com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "TapNGoPayCallback onFinish " + string);
            t.this.mj = false;
            if (apduReasonCode == null) {
                com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "TapNGoPayCallback onFinish arc null ");
                return;
            }
            Bundle bundle = apduReasonCode.getExtraData();
            if (bundle == null || !bundle.containsKey("status")) {
                com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "No TapNGo transaction status ");
                return;
            }
            int n3 = bundle.getInt("status");
            com.samsung.android.spayfw.b.c.i("TapAndGoDecorator", "PAYFW_KEY_TAP_N_GO_TRANSACTION_ERROR_CODE : " + n3 + ", tokenId: " + string);
            Bundle bundle2 = bundle.getBundle("pdolValues");
            Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
            intent.putExtra("notiType", "tapNGoState");
            intent.putExtra("tokenId", string);
            intent.putExtra("status", n3);
            if (bundle2 != null) {
                com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "pdolValues " + bundle2.toString());
                intent.putExtra("pdolValues", bundle2);
            }
            PaymentFrameworkApp.a(intent);
        }

        @Override
        public void onPay(String string, int n2, int n3) {
            com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "TapNGoPayCallback onPay " + string);
        }

        @Override
        public void onPaySwitch(String string, int n2, int n3) {
            com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "TapNGoPayCallback onPaySwitch " + string);
        }

        @Override
        public void onRetry(String string, int n2, int n3) {
            com.samsung.android.spayfw.b.c.w("TapAndGoDecorator", "TapNGoPayCallback onRetry " + string);
        }
    }

    private class b
    extends ISelectCardCallback.Stub {
        private b() {
        }

        @Override
        public void onFail(String string, int n2) {
            com.samsung.android.spayfw.b.c.e("TapAndGoDecorator", "TapNGoSelectCardCallback onFail " + n2);
        }

        @Override
        public void onSuccess(String string, SelectCardResult selectCardResult) {
            com.samsung.android.spayfw.b.c.d("TapAndGoDecorator", "TapNGoSelectCardCallback onSuccess");
            t.this.mj = true;
            t.this.bh();
        }
    }

}

