/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.payment.a;
import com.samsung.android.spayfw.payprovider.discover.payment.a.d;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.Iterator;
import java.util.List;

public class e
extends a {
    private d th;

    public e(ByteBuffer byteBuffer, com.samsung.android.spayfw.payprovider.discover.payment.data.e e2, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, e2, discoverPaymentCard);
        this.th = new d(byteBuffer);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean a(DiscoverRecord discoverRecord) {
        ByteBuffer byteBuffer;
        if (DiscoverCLTransactionContext.DiscoverClTransactionType.up.equals((Object)this.cN().dK())) {
            c.i("DCSDK_DiscoverReadRecordApduHandler", "checkRecordInAFL: initialize ZIP afl...");
            byteBuffer = this.cM().getZipAfl();
        } else {
            byteBuffer = this.cN().getPaymentProfile().getAfl();
        }
        if (byteBuffer == null) {
            c.e("DCSDK_DiscoverReadRecordApduHandler", "checkRecordInAFL: record not found in afl, afl is null.");
            return false;
        }
        int n2 = 1;
        boolean bl = false;
        while (byteBuffer.getSize() >= n2 * 4) {
            ByteBuffer byteBuffer2 = byteBuffer.copyBytes(-4 + n2 * 4, n2 * 4);
            if (byteBuffer2.getByte(0) >> 3 == discoverRecord.getSFI().getByte(0) && discoverRecord.getRecordNumber().getByte(0) >= byteBuffer2.getByte(1) && discoverRecord.getRecordNumber().getByte(0) <= byteBuffer2.getByte(2)) {
                bl = true;
            }
            ++n2;
        }
        return bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public com.samsung.android.spayfw.payprovider.discover.payment.data.a cK() {
        if (this.th.dk() != 0) {
            c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, cla is not supported, cla = " + this.th.dk() + ", expected " + 0);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(28160);
        }
        if ((255 & this.th.getINS()) != 178) {
            c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, ins is not supported, ins = " + (255 & this.th.getINS()) + ", expected " + 178);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27904);
        }
        if (this.th.getP1() == 0 || (7 & this.th.getP2()) != 4) {
            c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, wrong p1 and/or p2, p1 = " + this.th.getP1() + ", p2 = " + this.th.getP2());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27270);
        }
        if (this.th.dl() != 0) {
            c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, wrong Le = " + this.th.dl() + ", expected Le  = " + 0);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(26368);
        }
        var1_1 = this.th.getSfiNumber();
        var2_2 = this.th.getRecordNumber();
        c.i("DCSDK_DiscoverReadRecordApduHandler", "Requested SFI: " + (var1_1 & 255));
        c.i("DCSDK_DiscoverReadRecordApduHandler", "Requested record: " + (var2_2 & 255));
        if (var1_1 != 1 || var2_2 != 1) {
            c.d("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: EMV transaction");
            if (var1_1 < 1 || var1_1 > 10) {
                c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not supported, SFI <  EMV_MIN or SFI > EMV_MAX.");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
            }
            if (var1_1 > 1) {
                c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, SFI is not supported.");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
            }
            var3_5 = this.cM().getRecords();
            if (var3_5 == null) {
                c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, cannot find records in the profile.");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27267);
            }
            c.i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, requested record, sfi = " + var1_1 + ", record number = " + var2_2);
            var4_6 = var3_5.iterator();
            var5_4 = false;
        } else {
            c.i("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: zip record requested...");
            if (this.cN().dL() != null) {
                var6_3 = this.cN().dL();
                c.i("DCSDK_DiscoverReadRecordApduHandler", "Read record processApdu: zip record found.");
                var5_4 = true;
            } else {
                var5_4 = true;
                var6_3 = null;
            }
lbl41: // 4 sources:
            do {
                if (!var5_4) {
                    c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, sfi not found, sfi = " + var1_1);
                    return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
                }
                if (var6_3 == null) {
                    c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not found, sfi = " + var1_1 + ", record = " + var2_2);
                    return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27267);
                }
                if (var1_1 >= 1 && var1_1 <= 10 && !this.a(var6_3)) {
                    c.e("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, record not found in AFL, sfi = " + var1_1 + ", record = " + var2_2);
                    return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
                }
                var7_8 = this.cL().ed().dJ();
                var8_9 = this.cL().ed();
                var7_8 - 1;
                var8_9.L(var7_8);
                if (this.cL().ed().dJ() == 0) {
                    if ((48 & this.cL().ed().dH().getByte(1)) == 1) {
                        c.i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, indicate CDA successful.");
                        this.cN().getPth().clearBit(1, 8);
                    }
                    c.i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, indicate transaction completed.");
                    this.cN().getPth().clearBit(1, 7);
                    if (this.cN().getPaymentProfile().getCpr().checkBit(1, 6)) {
                        c.i("DCSDK_DiscoverReadRecordApduHandler", "processApdu, C-APDU ReadRecord, loyality program indicated, reset PID counter.");
                        this.cN().M(0);
                    }
                }
                var10_10 = new com.samsung.android.spayfw.payprovider.discover.payment.a.e();
                var10_10.r(var6_3.getRecordData());
                var11_11 = new com.samsung.android.spayfw.payprovider.discover.payment.data.a(var10_10.dj());
                var11_11.dD();
                this.cL().a(var11_11);
                return var11_11;
                break;
            } while (true);
        }
        while (var4_6.hasNext()) {
            block21 : {
                block19 : {
                    block20 : {
                        var6_3 = (DiscoverRecord)var4_6.next();
                        if (var6_3.getSFI().getByte(0) != var1_1) break block19;
                        if (var6_3.getRecordNumber().getByte(0) != var2_2) break block20;
                        var5_4 = true;
                        ** GOTO lbl41
                    }
                    var12_7 = true;
                    break block21;
                }
                var12_7 = var5_4;
            }
            var5_4 = var12_7;
        }
        var6_3 = null;
        ** while (true)
    }
}

