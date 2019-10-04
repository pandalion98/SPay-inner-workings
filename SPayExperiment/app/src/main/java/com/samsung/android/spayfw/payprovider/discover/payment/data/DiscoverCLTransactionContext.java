/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.b;
import com.samsung.android.spayfw.payprovider.discover.payment.data.f;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfileData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverCLTransactionContext {
    public static final ByteBuffer ui = new ByteBuffer("2PAY.SYS.DDF01".getBytes());
    private ByteBuffer mPth = new ByteBuffer(2);
    private ByteBuffer tT = new ByteBuffer(8);
    private ByteBuffer tU;
    private int tV = 0;
    private int tW = 0;
    private DiscoverClTransactionType tX;
    private DiscoverRecord tY;
    private b tZ = null;
    private byte ua = 0;
    private byte ub = 1;
    private f uc;
    private long ud;
    private ByteBuffer ue;
    private ByteBuffer uf;
    private int ug;
    private DiscoverPaymentProfileData uh;

    public void L(int n2) {
        this.tV = n2;
    }

    public void M(int n2) {
        this.tW = n2;
    }

    public void N(int n2) {
        this.ug = n2;
    }

    public void a(DiscoverClTransactionType discoverClTransactionType) {
        this.tX = discoverClTransactionType;
    }

    public void a(b b2) {
        this.tZ = b2;
    }

    public void a(f f2) {
        this.uc = f2;
    }

    public void a(DiscoverPaymentProfile discoverPaymentProfile) {
        this.uh = new DiscoverPaymentProfileData(discoverPaymentProfile);
    }

    public void b(byte by) {
        this.ua = by;
    }

    public void b(DiscoverRecord discoverRecord) {
        this.tY = discoverRecord;
    }

    public void c(byte by) {
        this.ub = by;
    }

    public ByteBuffer dH() {
        return this.tT;
    }

    public ByteBuffer dI() {
        return this.tU;
    }

    public int dJ() {
        return this.tV;
    }

    public DiscoverClTransactionType dK() {
        return this.tX;
    }

    public DiscoverRecord dL() {
        return this.tY;
    }

    public b dM() {
        return this.tZ;
    }

    public byte dN() {
        return this.ua;
    }

    public byte dO() {
        return this.ub;
    }

    public f dP() {
        return this.uc;
    }

    public ByteBuffer dQ() {
        return this.ue;
    }

    public ByteBuffer dR() {
        return this.uf;
    }

    public int dS() {
        return this.ug;
    }

    public DiscoverPaymentProfile getPaymentProfile() {
        return this.uh.getPaymentProfile();
    }

    public ByteBuffer getPth() {
        return this.mPth;
    }

    public void s(ByteBuffer byteBuffer) {
        this.tU = byteBuffer;
    }

    public void setPth(ByteBuffer byteBuffer) {
        this.mPth = byteBuffer;
    }

    public void setSelectedPaymentProfile(DiscoverPaymentProfile discoverPaymentProfile) {
        this.uh.setSelectedPaymentProfile(discoverPaymentProfile);
    }

    public void t(long l2) {
        this.ud = l2;
    }

    public void t(ByteBuffer byteBuffer) {
        this.uf = byteBuffer;
    }

    public static final class DiscoverClTransactionType
    extends Enum<DiscoverClTransactionType> {
        public static final /* enum */ DiscoverClTransactionType uj = new DiscoverClTransactionType("A0000001523010");
        public static final /* enum */ DiscoverClTransactionType uk = new DiscoverClTransactionType("A0000002771010");
        public static final /* enum */ DiscoverClTransactionType ul = new DiscoverClTransactionType("A0000002772010");
        public static final /* enum */ DiscoverClTransactionType um = new DiscoverClTransactionType("A0000001524010");
        public static final /* enum */ DiscoverClTransactionType un = new DiscoverClTransactionType("");
        public static final /* enum */ DiscoverClTransactionType uo = new DiscoverClTransactionType("");
        public static final /* enum */ DiscoverClTransactionType up = new DiscoverClTransactionType("A0000003241010");
        public static final /* enum */ DiscoverClTransactionType uq = new DiscoverClTransactionType("A0000000");
        private static final /* synthetic */ DiscoverClTransactionType[] ur;
        private ByteBuffer mAid;

        static {
            DiscoverClTransactionType[] arrdiscoverClTransactionType = new DiscoverClTransactionType[]{uj, uk, ul, um, un, uo, up, uq};
            ur = arrdiscoverClTransactionType;
        }

        private DiscoverClTransactionType(String string2) {
            this.mAid = ByteBuffer.fromHexString(string2);
        }

        public static DiscoverClTransactionType valueOf(String string) {
            return (DiscoverClTransactionType)Enum.valueOf(DiscoverClTransactionType.class, (String)string);
        }

        public static DiscoverClTransactionType[] values() {
            return (DiscoverClTransactionType[])ur.clone();
        }

        public ByteBuffer getAid() {
            return this.mAid;
        }
    }

}

