/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.payprovider.discover.payment.DiscoverApduHandlerState;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.c;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.b;
import java.util.HashMap;
import java.util.List;

public class f
extends com.samsung.android.spayfw.payprovider.discover.payment.a {
    private com.samsung.android.spayfw.payprovider.discover.payment.a.a td;

    public f(ByteBuffer byteBuffer, e e2, DiscoverPaymentCard discoverPaymentCard) {
        super(byteBuffer, e2, discoverPaymentCard);
        this.td = new com.samsung.android.spayfw.payprovider.discover.payment.a.a(byteBuffer);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int c(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_", "findAliasAid, aid is null.");
            return -1;
        }
        List<ByteBuffer> list = this.cM().getAliasList();
        if (list == null || list.isEmpty()) {
            com.samsung.android.spayfw.b.c.e("DCSDK_", "findAliasAid, alias list is empty.");
            return -1;
        }
        int n2 = 0;
        while (n2 < list.size()) {
            ByteBuffer byteBuffer2 = (ByteBuffer)list.get(n2);
            if (byteBuffer2 != null && byteBuffer2.toHexString().startsWith(byteBuffer.toHexString())) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean a(ByteBuffer var1_1, ByteBuffer var2_2) {
        if (this.cM().getFciPpse() == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, checkAidInAdf PPSE FCI is null.");
            return false;
        }
        if (var2_2 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, checkAidInAdf FCI is null.");
            return false;
        }
        var3_3 = this.cM().getFciPpse().toHexString();
        com.samsung.android.spayfw.b.c.d("DCSDK_", "parsePPSE_FCI, parse FCI Template tag.");
        var4_4 = a.c(var2_2.getBytes(), 0, var2_2.getSize());
        if (var4_4 == null || var4_4.O(c.vO.getInt()) == null) ** GOTO lbl-1000
        var9_5 = var4_4.O(c.vO.getInt());
        if (var9_5 == null || var9_5.isEmpty() || var9_5.get(0) == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_", "checkAidInAdf, fci template list is empty.");
            return false;
        }
        var10_6 = a.c(((ByteBuffer)var9_5.get(0)).getBytes(), 0, ((ByteBuffer)var9_5.get(0)).getSize());
        com.samsung.android.spayfw.b.c.d("DCSDK_", "checkAidInAdf, returned parsed proprietaryTemplate.");
        if (var10_6 != null && var10_6.O(c.vP.getInt()) != null) {
            var11_7 = var10_6.O(c.vP.getInt());
            if (var11_7 == null || var11_7.isEmpty() || var11_7.get(0) == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_", "checkAidInAdf, df not found empty.");
                return false;
            }
            var5_8 = (ByteBuffer)var11_7.get(0);
        } else lbl-1000: // 2 sources:
        {
            var5_8 = null;
        }
        if (var5_8 == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_", "checkAidInAdf, df is null.");
            return false;
        }
        var6_9 = var5_8.toHexString();
        com.samsung.android.spayfw.b.c.i("DCSDK_", "checkAidInAdf, df: " + var6_9);
        var7_10 = var1_1.toHexString();
        if (var6_9.contains((CharSequence)var7_10) == false) return false;
        if (var3_3.contains((CharSequence)var7_10) == false) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public com.samsung.android.spayfw.payprovider.discover.payment.data.a cK() {
        ByteBuffer byteBuffer;
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select start...");
        if (this.td.dk() != 0) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, cla is not supported, cla = " + this.td.dk() + ", expected " + 0);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(28160);
        }
        if (this.td.getINS() != -92) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, ins is not supported, ins = " + this.td.getINS() + ", expected " + -92);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27904);
        }
        if (this.td.getP1() != 4) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong p1, p1 = " + this.td.getP1() + ", expected " + 4);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27270);
        }
        if (this.td.getP2() != 0 && this.td.getP2() != 2) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong p2, p2 = " + this.td.getP2() + ", expected " + 0 + " or " + 2);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27270);
        }
        ByteBuffer byteBuffer2 = this.td.getData();
        if (byteBuffer2 == null || this.td.getLc() != byteBuffer2.getSize()) {
            StringBuilder stringBuilder = new StringBuilder().append("processApdu, C-APDU Select, wrong length  = ").append(this.td.getLc()).append(", actual length ");
            Integer n2 = byteBuffer2 != null ? Integer.valueOf((int)byteBuffer2.getSize()) : null;
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", stringBuilder.append((Object)n2).toString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(26368);
        }
        if (this.td.dl() != 0) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, wrong Le = " + this.td.dl() + ", expected Le  = " + 0);
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(26368);
        }
        if (this.cM() == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Payment profile not found.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
        }
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select aid = " + byteBuffer2.toHexString());
        if (byteBuffer2.toHexString().startsWith(DiscoverCLTransactionContext.ui.toHexString())) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select ppse aid");
            byteBuffer = this.cM().getFciPpse();
        } else if (byteBuffer2.toHexString().startsWith(DiscoverCLTransactionContext.DiscoverClTransactionType.uj.getAid().toHexString())) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select emv aid");
            ByteBuffer byteBuffer3 = this.cM().getFciMainAid();
            this.cN().a(DiscoverCLTransactionContext.DiscoverClTransactionType.uj);
            byteBuffer = byteBuffer3;
        } else if (byteBuffer2.toHexString().startsWith(DiscoverCLTransactionContext.DiscoverClTransactionType.up.getAid().toHexString())) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select zip aid");
            this.cN().a(DiscoverCLTransactionContext.DiscoverClTransactionType.up);
            byteBuffer = this.cM().getFciZipAid();
        } else if (byteBuffer2.toHexString().startsWith(DiscoverCLTransactionContext.DiscoverClTransactionType.um.getAid().toHexString())) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select emv debit aid");
            ByteBuffer byteBuffer4 = this.cM().getFciDebitAid();
            this.cN().a(DiscoverCLTransactionContext.DiscoverClTransactionType.um);
            byteBuffer = byteBuffer4;
        } else {
            if (this.cM().getFciAltAid() == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, not supported aid, aid = " + byteBuffer2.toHexString());
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
            }
            com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select alt aid: " + byteBuffer2.toHexString());
            HashMap<String, ByteBuffer> hashMap = this.cM().getFciAltAid();
            if (hashMap == null) {
                com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select alt aid list is empty.");
                return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
            }
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt FCI selected");
            ByteBuffer byteBuffer5 = (ByteBuffer)hashMap.get((Object)byteBuffer2.toHexString());
            for (DiscoverCLTransactionContext.DiscoverClTransactionType discoverClTransactionType : DiscoverCLTransactionContext.DiscoverClTransactionType.values()) {
                if (discoverClTransactionType.getAid() == null || discoverClTransactionType.getAid().toHexString() == null || !byteBuffer2.toHexString().startsWith(discoverClTransactionType.getAid().toHexString())) continue;
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt AID found " + discoverClTransactionType.name());
                this.cN().a(discoverClTransactionType);
            }
            if (this.cN().dK() == null) {
                com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Alt AID unknown alias, set to emv.");
                this.cN().a(DiscoverCLTransactionContext.DiscoverClTransactionType.uj);
            }
            byteBuffer = byteBuffer5;
        }
        if (byteBuffer == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU fci is null.");
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
        }
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select fci = " + byteBuffer.toHexString());
        if (!byteBuffer2.equals(DiscoverCLTransactionContext.ui) && !this.a(byteBuffer2, byteBuffer)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, aid not found in adf, aid = " + byteBuffer2.toHexString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(27266);
        }
        List<ByteBuffer> list = this.cM().getDiscoverApplicationData().getApplicationBlockedList();
        if (list != null && !list.isEmpty() && list.contains((Object)byteBuffer2)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverSelectApduHandler", "processApdu, C-APDU Select, aid found in the blocked list: " + byteBuffer2.toHexString());
            return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(25219);
        }
        this.cN().N(this.c(byteBuffer2));
        return new com.samsung.android.spayfw.payprovider.discover.payment.data.a(new com.samsung.android.spayfw.payprovider.discover.payment.a.f(byteBuffer2, byteBuffer).dj(), DiscoverApduHandlerState.sX);
    }
}

