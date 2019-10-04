/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.c;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;

public class b
extends com.samsung.android.spayfw.payprovider.discover.payment.a {
    private com.samsung.android.spayfw.payprovider.discover.payment.a.a td;

    public b(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, e2, discoverPaymentCard);
        this.td = new com.samsung.android.spayfw.payprovider.discover.payment.a.a(byteBuffer);
    }

    /*
     * Enabled aggressive block sorting
     */
    private com.samsung.android.spayfw.payprovider.discover.payment.data.a a(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2;
        StringBuilder stringBuilder = new StringBuilder().append("processApdu, C-APDU GET DATA, tag ");
        String string = byteBuffer != null ? byteBuffer.toHexString() : null;
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverGetDataApduHandler", stringBuilder.append(string).toString());
        if (byteBuffer == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is null, tag.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27272);
        }
        if (c.A(byteBuffer)) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, profile tag found." + byteBuffer.toHexString());
            byteBuffer2 = c.a(byteBuffer, this.cM());
        } else if (c.B(byteBuffer)) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDDT tag found." + byteBuffer.toHexString());
            DiscoverIDDTag discoverIDDTag = c.b(byteBuffer, this.cM());
            byteBuffer2 = null;
            if (discoverIDDTag != null) {
                if ((64 & discoverIDDTag.getAccess()) == 64 && this.cN().dM() == null) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDDT tag requested, GPO access required, but not executed");
                    return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27010);
                }
                byteBuffer2 = discoverIDDTag.getData();
            }
        } else {
            byteBuffer2 = c.x(byteBuffer);
        }
        if (byteBuffer2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag data is null, tag = " + byteBuffer.toHexString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27272);
        }
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, return data for tag = " + byteBuffer.toHexString());
        com.samsung.android.spayfw.payprovider.discover.payment.a.e e2 = new com.samsung.android.spayfw.payprovider.discover.payment.a.e();
        e2.r(a.c(byteBuffer, byteBuffer2));
        return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(e2.dj());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public com.samsung.android.spayfw.payprovider.discover.payment.data.a cK() {
        ByteBuffer byteBuffer;
        if (this.td.dk() != 128) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, cla is not supported, cla = " + this.td.dk() + ", expected " + 128);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(28160);
        }
        if ((255 & this.td.getINS()) != 202) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, ins is not supported, ins = " + (255 & this.td.getINS()) + ", expected " + 202);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27904);
        }
        if (this.td.getP1() == 0) {
            byte[] arrby = new byte[]{this.td.getP2()};
            byteBuffer = new ByteBuffer(arrby);
        } else {
            byte[] arrby = new byte[]{this.td.getP1(), this.td.getP2()};
            byteBuffer = new ByteBuffer(arrby);
        }
        if (!c.w(byteBuffer)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is not supported, tag = " + byteBuffer.toHexString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27272);
        }
        if (this.td.dj().getByte(-1 + this.td.dj().getSize()) != 0) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, Lc != 0, Lc value " + this.td.dj().getByte(-1 + this.td.dj().getSize()));
            ByteBuffer byteBuffer2 = c.x(byteBuffer);
            if (byteBuffer2 == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag not found, Le = 0.");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27648);
            }
            short s2 = (short)byteBuffer2.getSize();
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, return tag size, Le = " + s2);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a((short)(s2 + 27648));
        }
        if (this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 2) && this.cN().dM() == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, GPO is not executed, required by CL ACO B2b2, 27013");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
        }
        if (c.y(byteBuffer)) {
            com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, counter/limit, tag = " + byteBuffer.toHexString());
            if (this.cN().dM() == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, GPO is not executed, return 27013");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
            }
            if (this.cM().getCaco().checkBit(1, 1)) {
                com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is retrievable, return tag = " + byteBuffer.toHexString());
                return this.a(byteBuffer);
            }
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, tag is not retrievable, tag = " + byteBuffer.toHexString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
        }
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, check IDDT tag...");
        if (!c.z(byteBuffer)) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, not IDD tag, return tag = " + byteBuffer.toHexString());
            return this.a(byteBuffer);
        }
        if (this.cM().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(2, 5)) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDD tag & IDDT enabled, return tag = " + byteBuffer.toHexString());
            return this.a(byteBuffer);
        }
        com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverGetDataApduHandler", "processApdu, C-APDU GET DATA, IDD tag & IDDT disabled, tag = " + byteBuffer.toHexString());
        return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27013);
    }
}

