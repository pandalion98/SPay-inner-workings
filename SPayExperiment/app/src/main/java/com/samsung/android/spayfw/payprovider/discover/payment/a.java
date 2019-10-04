/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.payprovider.discover.payment.b;
import com.samsung.android.spayfw.payprovider.discover.payment.c;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.f;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public abstract class a {
    protected ByteBuffer sR;
    private e sS;
    private DiscoverPaymentCard sT;
    private DiscoverContactlessPaymentData sU;

    protected a(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        this.sR = byteBuffer;
        this.sS = e2;
        this.sT = discoverPaymentCard;
        if (discoverPaymentCard != null) {
            this.sU = discoverPaymentCard.getDiscoverContactlessPaymentData();
        }
    }

    public static a a(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return new f(byteBuffer, e2, discoverPaymentCard);
    }

    public static a b(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return new c(byteBuffer, e2, discoverPaymentCard);
    }

    public static a c(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return new com.samsung.android.spayfw.payprovider.discover.payment.e(byteBuffer, e2, discoverPaymentCard);
    }

    public static a d(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        return new b(byteBuffer, e2, discoverPaymentCard);
    }

    public abstract com.samsung.android.spayfw.payprovider.discover.payment.data.a cK();

    public e cL() {
        return this.sS;
    }

    public DiscoverContactlessPaymentData cM() {
        return this.sU;
    }

    public DiscoverCLTransactionContext cN() {
        return this.sS.ed();
    }
}

