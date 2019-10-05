/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.amex.a;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.plcc.PlccPayProvider;
import com.samsung.android.spayfw.payprovider.visa.b;
import com.samsung.android.spayfw.utils.h;

public class c {
    String iU;
    int iV;
    String iW;
    d iX;
    q iY;
    PaymentNetworkProvider iZ;
    String ja;
    String mCardBrand;
    String mEnrollmentId;

    public c(Context context, String string, String string2, String string3, int n2) {
        this.mCardBrand = string;
        this.iU = string2;
        this.iW = string3;
        this.iV = n2;
        this.iX = new d();
        this.iY = new q();
        this.g(context);
        if (this.iZ != null) {
            this.iZ.providerInit();
        }
    }

    public c(Context context, String string, String string2, String string3, int n2, q q2) {
        this.mCardBrand = string;
        this.iU = string2;
        this.iW = string3;
        this.iV = n2;
        this.iX = new d();
        this.iY = q2;
        this.g(context);
        if (this.iZ != null) {
            this.iZ.providerInit();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void g(Context context) {
        q q2 = this.iY;
        f f2 = null;
        if (q2 != null) {
            f f3 = this.iY.aQ();
            f2 = null;
            if (f3 != null) {
                f2 = this.iY.aQ();
            }
        }
        Log.d("Card", "Card Brand : " + this.mCardBrand);
        if ("VI".equals((Object)this.mCardBrand)) {
            Log.d("Card", "Card Type : " + this.iU);
            if (!"SAMSUNG_REWARD".equals((Object)this.iU)) {
                this.iZ = new b(context, this.mCardBrand, f2);
                return;
            }
            this.iZ = new com.samsung.android.spayfw.payprovider.a.a(context, this.mCardBrand, f2);
            return;
        } else {
            if ("MC".equals((Object)this.mCardBrand)) {
                this.iZ = new McProvider(context, this.mCardBrand, f2);
                return;
            }
            if ("AX".equals((Object)this.mCardBrand)) {
                if (h.fS()) {
                    this.iZ = new a(context, this.mCardBrand, f2);
                    return;
                }
                this.iZ = new com.samsung.android.spayfw.payprovider.amexv2.a(context, this.mCardBrand, f2);
                return;
            }
            if ("PL".equals((Object)this.mCardBrand)) {
                this.iZ = new PlccPayProvider(context, this.mCardBrand, f2);
                return;
            }
            if ("GI".equals((Object)this.mCardBrand)) {
                this.iZ = new com.samsung.android.spayfw.payprovider.b.a(context, this.mCardBrand, f2);
                return;
            }
            if ("LO".equals((Object)this.mCardBrand)) {
                this.iZ = new com.samsung.android.spayfw.payprovider.c.a(context, this.mCardBrand, f2);
                return;
            }
            if (this.mCardBrand.equals((Object)"DS")) {
                this.iZ = new com.samsung.android.spayfw.payprovider.discover.a(context, this.mCardBrand, f2);
                return;
            }
            if (!"GM".equals((Object)this.mCardBrand)) return;
            {
                this.iZ = new com.samsung.android.spayfw.payprovider.globalmembership.a(context, this.mCardBrand, f2);
                return;
            }
        }
    }

    public static String y(String string) {
        if (string == null) {
            return "";
        }
        if (string.equals((Object)"VI")) {
            return "credit/vi";
        }
        if (string.equals((Object)"AX")) {
            return "credit/ax";
        }
        if (string.equals((Object)"MC")) {
            return "credit/mc";
        }
        if (string.equals((Object)"PL")) {
            return "private/*";
        }
        if (string.equals((Object)"GI")) {
            return "gift/*";
        }
        if (string.equals((Object)"DS")) {
            return "credit/ds";
        }
        if (string.equals((Object)"LO")) {
            return "loyalty/*";
        }
        return "";
    }

    public void a(d d2) {
        this.iX = d2;
    }

    public void a(q q2) {
        this.iY = q2;
    }

    public int ab() {
        return this.iV;
    }

    public q ac() {
        return this.iY;
    }

    public PaymentNetworkProvider ad() {
        return this.iZ;
    }

    public void delete() {
        this.iZ.delete();
    }

    public String getCardBrand() {
        return this.mCardBrand;
    }

    public String getEnrollmentId() {
        return this.mEnrollmentId;
    }

    public String getSecurityCode() {
        return this.ja;
    }

    public void j(int n2) {
        this.iV = n2;
    }

    public boolean k(int n2) {
        if (this.iV == 0) {
            return this.iZ.isPayAllowedForPresentationMode(n2);
        }
        return (n2 & this.iV) == n2;
    }

    public void setCardBrand(String string) {
        this.mCardBrand = string;
    }

    public void setCardType(String string) {
        this.iU = string;
    }

    public void setEnrollmentId(String string) {
        this.mEnrollmentId = string;
    }

    public void setSecurityCode(String string) {
        this.ja = string;
    }
}

