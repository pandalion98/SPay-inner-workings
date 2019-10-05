/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.CardAttributes;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.h;

public class c
extends o {
    private ICardAttributeCallback kK;
    private String kL;
    private boolean kM;

    public c(Context context, String string, boolean bl, ICardAttributeCallback iCardAttributeCallback) {
        super(context);
        this.kK = iCardAttributeCallback;
        this.kL = string;
        this.kM = bl;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean J(String string) {
        int n5;
        int n2 = string.length();
        int n3 = n2 & 1;
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; l2 += (long)n5, ++i2) {
            int n4;
            n5 = n4 = Integer.parseInt((String)(string.charAt(i2) + ""));
            if ((n3 ^ i2 & 1) != 0 || (n5 *= 2) <= 9) continue;
            n5 -= 9;
        }
        if (l2 == 0L) {
            return false;
        }
        if (l2 % 10L != 0L) return false;
        return true;
        catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void process() {
        if (this.kK == null) {
            h.clearMemory(this.kL);
            return;
        }
        if (this.kL == null || this.kL.isEmpty() || this.kL.length() < 6) {
            Log.e("CardAttributeRetriever", "PAN is invalid");
            this.kK.onFail(-5);
            return;
        }
        DeviceInfo.cacheLocation(this.mContext);
        CardAttributes cardAttributes = new CardAttributes();
        BinAttribute binAttribute = BinAttribute.getBinAttribute(this.kL);
        if (binAttribute == null) {
            Log.e("CardAttributeRetriever", "PAN not supported: Bin Attr null.");
            this.kK.onFail(-10);
            return;
        }
        if (this.kM) {
            if (!this.J(this.kL)) {
                Log.e("CardAttributeRetriever", "PAN not supported: Luhn Check Failed.");
                this.kK.onFail(-10);
                return;
            }
            cardAttributes.setPanValidated(true);
        } else {
            cardAttributes.setPanValidated(false);
        }
        if (!(binAttribute.isCvvRequired() || binAttribute.isBillingInfoRequired() || binAttribute.isZipRequired() || binAttribute.isExpiryRequired())) {
            Log.e("CardAttributeRetriever", "PAN not supported : Token Request to TSP is blocked");
            this.kK.onFail(-207);
            return;
        }
        cardAttributes.setCardBrand(binAttribute.getCardBrand());
        cardAttributes.setCvvRequired(binAttribute.isCvvRequired());
        cardAttributes.setExpiryRequired(binAttribute.isExpiryRequired());
        cardAttributes.setZipRequired(binAttribute.isZipRequired());
        cardAttributes.setBillingInfoRequired(binAttribute.isBillingInfoRequired());
        Log.d("CardAttributeRetriever", "CardAttributeRetriever : " + cardAttributes.toString());
        this.kK.onSuccess(this.kL, cardAttributes);
        h.clearMemory(this.kL);
    }
}

