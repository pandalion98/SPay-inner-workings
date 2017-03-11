package com.samsung.android.spayfw.core;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.amex.AmexPayProvider;
import com.samsung.android.spayfw.payprovider.discover.DiscoverPayProvider;
import com.samsung.android.spayfw.payprovider.globalmembership.GlobalMembershipPayProvider;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.p014a.CashCardPayProvider;
import com.samsung.android.spayfw.payprovider.p015b.GiftCardPayProvider;
import com.samsung.android.spayfw.payprovider.p016c.LoyaltyCardPayProvider;
import com.samsung.android.spayfw.payprovider.plcc.PlccPayProvider;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.visa.VisaPayProvider;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

/* renamed from: com.samsung.android.spayfw.core.c */
public class Card {
    String iU;
    int iV;
    String iW;
    CardMetaData iX;
    Token iY;
    PaymentNetworkProvider iZ;
    String ja;
    String mCardBrand;
    String mEnrollmentId;

    public static String m574y(String str) {
        if (str == null) {
            return BuildConfig.FLAVOR;
        }
        if (str.equals(PaymentFramework.CARD_BRAND_VISA)) {
            return "credit/vi";
        }
        if (str.equals(PaymentFramework.CARD_BRAND_AMEX)) {
            return "credit/ax";
        }
        if (str.equals(PaymentFramework.CARD_BRAND_MASTERCARD)) {
            return "credit/mc";
        }
        if (str.equals(PlccConstants.BRAND)) {
            return "private/*";
        }
        if (str.equals(PaymentFramework.CARD_BRAND_GIFT)) {
            return "gift/*";
        }
        if (str.equals(PaymentFramework.CARD_BRAND_DISCOVER)) {
            return "credit/ds";
        }
        if (str.equals(PaymentFramework.CARD_BRAND_LOYALTY)) {
            return "loyalty/*";
        }
        return BuildConfig.FLAVOR;
    }

    public Card(Context context, String str, String str2, String str3, int i) {
        this.mCardBrand = str;
        this.iU = str2;
        this.iW = str3;
        this.iV = i;
        this.iX = new CardMetaData();
        this.iY = new Token();
        m573g(context);
        if (this.iZ != null) {
            this.iZ.providerInit();
        }
    }

    public Card(Context context, String str, String str2, String str3, int i, Token token) {
        this.mCardBrand = str;
        this.iU = str2;
        this.iW = str3;
        this.iV = i;
        this.iX = new CardMetaData();
        this.iY = token;
        m573g(context);
        if (this.iZ != null) {
            this.iZ.providerInit();
        }
    }

    public void m575a(CardMetaData cardMetaData) {
        this.iX = cardMetaData;
    }

    public String getEnrollmentId() {
        return this.mEnrollmentId;
    }

    public void setEnrollmentId(String str) {
        this.mEnrollmentId = str;
    }

    public String getCardBrand() {
        return this.mCardBrand;
    }

    public void setCardBrand(String str) {
        this.mCardBrand = str;
    }

    public void setCardType(String str) {
        this.iU = str;
    }

    public int ab() {
        return this.iV;
    }

    public void m577j(int i) {
        this.iV = i;
    }

    public Token ac() {
        return this.iY;
    }

    public void m576a(Token token) {
        this.iY = token;
    }

    public PaymentNetworkProvider ad() {
        return this.iZ;
    }

    public String getSecurityCode() {
        return this.ja;
    }

    public void setSecurityCode(String str) {
        this.ja = str;
    }

    public boolean m578k(int i) {
        if (this.iV == 0) {
            return this.iZ.isPayAllowedForPresentationMode(i);
        }
        return (this.iV & i) == i;
    }

    public void delete() {
        this.iZ.delete();
    }

    private void m573g(Context context) {
        ProviderTokenKey providerTokenKey = null;
        if (!(this.iY == null || this.iY.aQ() == null)) {
            providerTokenKey = this.iY.aQ();
        }
        Log.m285d("Card", "Card Brand : " + this.mCardBrand);
        if (PaymentFramework.CARD_BRAND_VISA.equals(this.mCardBrand)) {
            Log.m285d("Card", "Card Type : " + this.iU);
            if (PaymentFramework.CARD_TYPE_SAMSUNG_REWARD.equals(this.iU)) {
                this.iZ = new CashCardPayProvider(context, this.mCardBrand, providerTokenKey);
            } else {
                this.iZ = new VisaPayProvider(context, this.mCardBrand, providerTokenKey);
            }
        } else if (PaymentFramework.CARD_BRAND_MASTERCARD.equals(this.mCardBrand)) {
            this.iZ = new McProvider(context, this.mCardBrand, providerTokenKey);
        } else if (PaymentFramework.CARD_BRAND_AMEX.equals(this.mCardBrand)) {
            if (Utils.fS()) {
                this.iZ = new AmexPayProvider(context, this.mCardBrand, providerTokenKey);
            } else {
                this.iZ = new com.samsung.android.spayfw.payprovider.amexv2.AmexPayProvider(context, this.mCardBrand, providerTokenKey);
            }
        } else if (PlccConstants.BRAND.equals(this.mCardBrand)) {
            this.iZ = new PlccPayProvider(context, this.mCardBrand, providerTokenKey);
        } else if (PaymentFramework.CARD_BRAND_GIFT.equals(this.mCardBrand)) {
            this.iZ = new GiftCardPayProvider(context, this.mCardBrand, providerTokenKey);
        } else if (PaymentFramework.CARD_BRAND_LOYALTY.equals(this.mCardBrand)) {
            this.iZ = new LoyaltyCardPayProvider(context, this.mCardBrand, providerTokenKey);
        } else if (this.mCardBrand.equals(PaymentFramework.CARD_BRAND_DISCOVER)) {
            this.iZ = new DiscoverPayProvider(context, this.mCardBrand, providerTokenKey);
        } else if (PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP.equals(this.mCardBrand)) {
            this.iZ = new GlobalMembershipPayProvider(context, this.mCardBrand, providerTokenKey);
        }
    }
}
