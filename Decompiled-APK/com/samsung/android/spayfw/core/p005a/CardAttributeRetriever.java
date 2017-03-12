package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.CardAttributes;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

/* renamed from: com.samsung.android.spayfw.core.a.c */
public class CardAttributeRetriever extends Processor {
    private ICardAttributeCallback kK;
    private String kL;
    private boolean kM;

    public CardAttributeRetriever(Context context, String str, boolean z, ICardAttributeCallback iCardAttributeCallback) {
        super(context);
        this.kK = iCardAttributeCallback;
        this.kL = str;
        this.kM = z;
    }

    public void process() {
        if (this.kK == null) {
            Utils.clearMemory(this.kL);
        } else if (this.kL == null || this.kL.isEmpty() || this.kL.length() < 6) {
            Log.m286e("CardAttributeRetriever", "PAN is invalid");
            this.kK.onFail(-5);
        } else {
            DeviceInfo.cacheLocation(this.mContext);
            CardAttributes cardAttributes = new CardAttributes();
            BinAttribute binAttribute = BinAttribute.getBinAttribute(this.kL);
            if (binAttribute == null) {
                Log.m286e("CardAttributeRetriever", "PAN not supported: Bin Attr null.");
                this.kK.onFail(-10);
                return;
            }
            if (!this.kM) {
                cardAttributes.setPanValidated(false);
            } else if (m361J(this.kL)) {
                cardAttributes.setPanValidated(true);
            } else {
                Log.m286e("CardAttributeRetriever", "PAN not supported: Luhn Check Failed.");
                this.kK.onFail(-10);
                return;
            }
            if (binAttribute.isCvvRequired() || binAttribute.isBillingInfoRequired() || binAttribute.isZipRequired() || binAttribute.isExpiryRequired()) {
                cardAttributes.setCardBrand(binAttribute.getCardBrand());
                cardAttributes.setCvvRequired(binAttribute.isCvvRequired());
                cardAttributes.setExpiryRequired(binAttribute.isExpiryRequired());
                cardAttributes.setZipRequired(binAttribute.isZipRequired());
                cardAttributes.setBillingInfoRequired(binAttribute.isBillingInfoRequired());
                Log.m285d("CardAttributeRetriever", "CardAttributeRetriever : " + cardAttributes.toString());
                this.kK.onSuccess(this.kL, cardAttributes);
                Utils.clearMemory(this.kL);
                return;
            }
            Log.m286e("CardAttributeRetriever", "PAN not supported : Token Request to TSP is blocked");
            this.kK.onFail(PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED);
        }
    }

    private boolean m361J(String str) {
        int length = str.length();
        int i = length & 1;
        int i2 = 0;
        long j = 0;
        while (i2 < length) {
            try {
                int parseInt = Integer.parseInt(str.charAt(i2) + BuildConfig.FLAVOR);
                if (((i2 & 1) ^ i) == 0) {
                    parseInt *= 2;
                    if (parseInt > 9) {
                        parseInt -= 9;
                    }
                }
                j += (long) parseInt;
                i2++;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        boolean z = j == 0 ? false : j % 10 == 0;
        return z;
    }
}
