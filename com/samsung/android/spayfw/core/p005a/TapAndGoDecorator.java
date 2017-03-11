package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback.Stub;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.core.ApduHelper;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.State;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import java.util.Arrays;

/* renamed from: com.samsung.android.spayfw.core.a.t */
public class TapAndGoDecorator extends PaymentDecorator {
    private static TapAndGoDecorator mi;
    private boolean mj;

    /* renamed from: com.samsung.android.spayfw.core.a.t.a */
    private class TapAndGoDecorator extends Stub {
        final /* synthetic */ TapAndGoDecorator mk;

        private TapAndGoDecorator(TapAndGoDecorator tapAndGoDecorator) {
            this.mk = tapAndGoDecorator;
        }

        public void onFinish(String str, int i, ApduReasonCode apduReasonCode) {
            Log.m285d("TapAndGoDecorator", "TapNGoPayCallback onFinish " + str);
            this.mk.mj = false;
            if (apduReasonCode == null) {
                Log.m286e("TapAndGoDecorator", "TapNGoPayCallback onFinish arc null ");
                return;
            }
            Bundle extraData = apduReasonCode.getExtraData();
            if (extraData == null || !extraData.containsKey(CardMaster.COL_STATUS)) {
                Log.m286e("TapAndGoDecorator", "No TapNGo transaction status ");
                return;
            }
            int i2 = extraData.getInt(CardMaster.COL_STATUS);
            Log.m287i("TapAndGoDecorator", "PAYFW_KEY_TAP_N_GO_TRANSACTION_ERROR_CODE : " + i2 + ", tokenId: " + str);
            extraData = extraData.getBundle(TransactionStatus.EXTRA_PDOL_VALUES);
            Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
            intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TAP_N_GO_STATE);
            intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, str);
            intent.putExtra(CardMaster.COL_STATUS, i2);
            if (extraData != null) {
                Log.m285d("TapAndGoDecorator", "pdolValues " + extraData.toString());
                intent.putExtra(TransactionStatus.EXTRA_PDOL_VALUES, extraData);
            }
            PaymentFrameworkApp.m315a(intent);
        }

        public void onFail(String str, int i) {
            Log.m290w("TapAndGoDecorator", "TapNGoPayCallback onFail " + str + " error: " + i);
            this.mk.mj = false;
        }

        public void onPay(String str, int i, int i2) {
            Log.m290w("TapAndGoDecorator", "TapNGoPayCallback onPay " + str);
        }

        public void onPaySwitch(String str, int i, int i2) {
            Log.m290w("TapAndGoDecorator", "TapNGoPayCallback onPaySwitch " + str);
        }

        public void onRetry(String str, int i, int i2) {
            Log.m290w("TapAndGoDecorator", "TapNGoPayCallback onRetry " + str);
        }

        public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
            Log.m290w("TapAndGoDecorator", "TapNGoPayCallback onExtractGiftCardDetail");
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.t.b */
    private class TapAndGoDecorator extends ISelectCardCallback.Stub {
        final /* synthetic */ TapAndGoDecorator mk;

        private TapAndGoDecorator(TapAndGoDecorator tapAndGoDecorator) {
            this.mk = tapAndGoDecorator;
        }

        public void onSuccess(String str, SelectCardResult selectCardResult) {
            Log.m285d("TapAndGoDecorator", "TapNGoSelectCardCallback onSuccess");
            this.mk.mj = true;
            this.mk.bh();
        }

        public void onFail(String str, int i) {
            Log.m286e("TapAndGoDecorator", "TapNGoSelectCardCallback onFail " + i);
        }
    }

    public static final synchronized IPaymentProcessor m513a(Context context, PaymentProcessor paymentProcessor) {
        IPaymentProcessor iPaymentProcessor;
        synchronized (TapAndGoDecorator.class) {
            if (mi == null) {
                mi = new TapAndGoDecorator(context, paymentProcessor);
            }
            iPaymentProcessor = mi;
        }
        return iPaymentProcessor;
    }

    private TapAndGoDecorator(Context context, PaymentProcessor paymentProcessor) {
        super(context, paymentProcessor);
        this.mj = false;
    }

    public synchronized void clearCard() {
        if (this.mj) {
            Log.m286e("TapAndGoDecorator", "clearCard(): tapNGo card is in progress, nothing to clear ");
        } else {
            super.clearCard();
        }
    }

    public synchronized ApduReasonCode m516u(int i) {
        ApduReasonCode u;
        synchronized (this.ly) {
            Log.m285d("TapAndGoDecorator", "onDeactivated");
            u = super.m445u(i);
            if (u == null) {
                Log.m285d("TapAndGoDecorator", "onDeactivated null");
                u = null;
            }
        }
        return u;
    }

    public synchronized byte[] processApdu(byte[] bArr, Bundle bundle) {
        byte[] bArr2;
        synchronized (this.ly) {
            if (bArr == null) {
                Log.m286e("TapAndGoDecorator", "command apdu is null");
                bArr2 = ApduHelper.iR;
            } else {
                Log.m285d("TapAndGoDecorator", "processApdu");
                if (PaymentProcessor.m460a(bArr, (short) 0) == ISO7816.SELECT && Arrays.equals(ApduHelper.iO, bArr) && bf()) {
                    if (Utils.ap(this.mContext)) {
                        Card X = super.m440X();
                        if (X == null || X.ac() == null || X.ac().getTokenId() == null) {
                            Log.m290w("TapAndGoDecorator", "No default card - Samsung pay activity in foreground");
                        } else {
                            Log.m290w("TapAndGoDecorator", "abort Tap n go - Other Samsung pay activity in foreground");
                            Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                            intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TAP_N_GO_STATE);
                            intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, X.ac().getTokenId());
                            intent.putExtra(CardMaster.COL_STATUS, PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_APP_STATE);
                            PaymentFrameworkApp.m315a(intent);
                            bArr2 = ApduHelper.iS;
                        }
                    } else {
                        bg();
                    }
                }
                bArr2 = super.processApdu(bArr, bundle);
            }
        }
        return bArr2;
    }

    public synchronized void m515a(ICommonCallback iCommonCallback) {
        if (this.mj) {
            Log.m286e("TapAndGoDecorator", "No user initiated Payment to stop, Tap N Go is in progress");
            iCommonCallback.onSuccess(null);
        } else {
            super.m441a(iCommonCallback);
        }
    }

    protected boolean bf() {
        return State.m656p(1);
    }

    protected void bg() {
        Log.m285d("TapAndGoDecorator", "Restricted Payment select ppse, TapNGo case");
        if (State.m656p(1)) {
            Log.m285d("TapAndGoDecorator", "Restricted Payment NPAY_READY state");
            Card X = super.m440X();
            if (X == null || X.ac() == null) {
                Log.m285d("TapAndGoDecorator", "Restricted Payment No default card avail");
            } else {
                super.m443a(X.ac().getTokenId(), new TapAndGoDecorator(), true);
            }
        }
    }

    protected void bh() {
        Log.m287i("TapAndGoDecorator", "startRestrictedPayment time = " + System.currentTimeMillis());
        PayConfig payConfig = new PayConfig();
        payConfig.setPayType(1);
        payConfig.setMstTransmitTime(2000);
        super.m442a(null, payConfig, new TapAndGoDecorator(), true);
    }
}
