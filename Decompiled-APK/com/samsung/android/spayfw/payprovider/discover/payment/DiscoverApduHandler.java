package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a */
public abstract class DiscoverApduHandler {
    protected ByteBuffer sR;
    private DiscoverTransactionContext sS;
    private DiscoverPaymentCard sT;
    private DiscoverContactlessPaymentData sU;

    public abstract DiscoverApduProcessingResult cK();

    protected DiscoverApduHandler(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        this.sR = byteBuffer;
        this.sS = discoverTransactionContext;
        this.sT = discoverPaymentCard;
        if (discoverPaymentCard != null) {
            this.sU = discoverPaymentCard.getDiscoverContactlessPaymentData();
        }
    }

    public DiscoverTransactionContext cL() {
        return this.sS;
    }

    public DiscoverContactlessPaymentData cM() {
        return this.sU;
    }

    public DiscoverCLTransactionContext cN() {
        return this.sS.ed();
    }

    public static DiscoverApduHandler m927a(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return new DiscoverSelectApduHandler(byteBuffer, discoverTransactionContext, discoverPaymentCard);
    }

    public static DiscoverApduHandler m928b(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return new DiscoverGpoApduHandler(byteBuffer, discoverTransactionContext, discoverPaymentCard);
    }

    public static DiscoverApduHandler m929c(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return new DiscoverReadRecordApduHandler(byteBuffer, discoverTransactionContext, discoverPaymentCard);
    }

    public static DiscoverApduHandler m930d(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return new DiscoverGetDataApduHandler(byteBuffer, discoverTransactionContext, discoverPaymentCard);
    }
}
