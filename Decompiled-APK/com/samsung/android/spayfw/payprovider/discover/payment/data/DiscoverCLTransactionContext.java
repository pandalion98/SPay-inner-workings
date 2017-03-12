package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfileData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class DiscoverCLTransactionContext {
    public static final ByteBuffer ui;
    private ByteBuffer mPth;
    private ByteBuffer tT;
    private ByteBuffer tU;
    private int tV;
    private int tW;
    private DiscoverClTransactionType tX;
    private DiscoverRecord tY;
    private DiscoverCryptoData tZ;
    private byte ua;
    private byte ub;
    private DiscoverTransactionLogData uc;
    private long ud;
    private ByteBuffer ue;
    private ByteBuffer uf;
    private int ug;
    private DiscoverPaymentProfileData uh;

    public enum DiscoverClTransactionType {
        DISCOVER_CL_EMV("A0000001523010"),
        DISCOVER_CL_EMV_ALIAS("A0000002771010"),
        DISCOVER_CL_EMV_ALIAS1("A0000002772010"),
        DISCOVER_CL_EMV_DEBIT("A0000001524010"),
        DISCOVER_CL_EMV_OTP(BuildConfig.FLAVOR),
        DISCOVER_CL_EMV_LOW_VALUE(BuildConfig.FLAVOR),
        DISCOVER_CL_ZIP("A0000003241010"),
        DISCOVER_ALT_PREFIX("A0000000");
        
        private ByteBuffer mAid;

        private DiscoverClTransactionType(String str) {
            this.mAid = ByteBuffer.fromHexString(str);
        }

        public ByteBuffer getAid() {
            return this.mAid;
        }
    }

    public DiscoverCLTransactionContext() {
        this.tZ = null;
        this.tT = new ByteBuffer(8);
        this.tV = 0;
        this.tW = 0;
        this.mPth = new ByteBuffer(2);
        this.ua = (byte) 0;
        this.ub = (byte) 1;
    }

    public ByteBuffer dH() {
        return this.tT;
    }

    public ByteBuffer dI() {
        return this.tU;
    }

    public void m954s(ByteBuffer byteBuffer) {
        this.tU = byteBuffer;
    }

    public int dJ() {
        return this.tV;
    }

    public void m944L(int i) {
        this.tV = i;
    }

    public ByteBuffer getPth() {
        return this.mPth;
    }

    public void setPth(ByteBuffer byteBuffer) {
        this.mPth = byteBuffer;
    }

    public void m945M(int i) {
        this.tW = i;
    }

    public void m947a(DiscoverClTransactionType discoverClTransactionType) {
        this.tX = discoverClTransactionType;
    }

    public DiscoverClTransactionType dK() {
        return this.tX;
    }

    public DiscoverRecord dL() {
        return this.tY;
    }

    public void m952b(DiscoverRecord discoverRecord) {
        this.tY = discoverRecord;
    }

    public DiscoverCryptoData dM() {
        return this.tZ;
    }

    public void m948a(DiscoverCryptoData discoverCryptoData) {
        this.tZ = discoverCryptoData;
    }

    public byte dN() {
        return this.ua;
    }

    public void m951b(byte b) {
        this.ua = b;
    }

    public byte dO() {
        return this.ub;
    }

    public void m953c(byte b) {
        this.ub = b;
    }

    public DiscoverTransactionLogData dP() {
        return this.uc;
    }

    public void m949a(DiscoverTransactionLogData discoverTransactionLogData) {
        this.uc = discoverTransactionLogData;
    }

    public ByteBuffer dQ() {
        return this.ue;
    }

    public DiscoverPaymentProfile getPaymentProfile() {
        return this.uh.getPaymentProfile();
    }

    public void m950a(DiscoverPaymentProfile discoverPaymentProfile) {
        this.uh = new DiscoverPaymentProfileData(discoverPaymentProfile);
    }

    public void setSelectedPaymentProfile(DiscoverPaymentProfile discoverPaymentProfile) {
        this.uh.setSelectedPaymentProfile(discoverPaymentProfile);
    }

    public ByteBuffer dR() {
        return this.uf;
    }

    public void m956t(ByteBuffer byteBuffer) {
        this.uf = byteBuffer;
    }

    public void m955t(long j) {
        this.ud = j;
    }

    public int dS() {
        return this.ug;
    }

    public void m946N(int i) {
        this.ug = i;
    }

    static {
        ui = new ByteBuffer("2PAY.SYS.DDF01".getBytes());
    }
}
