/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverPaymentProfile {
    private static final int COUNTER_MAX = 255;
    private ByteBuffer mAfl;
    private ByteBuffer mAip;
    private ByteBuffer mApplicationUsageControl;
    private CVM mCVM;
    private CL mCl;
    private ByteBuffer mCpr;
    private CRM mCrm;
    private ByteBuffer mCtq;
    private int mProfileId;
    private ByteBuffer mPru;

    public ByteBuffer getAfl() {
        return this.mAfl;
    }

    public ByteBuffer getAip() {
        return this.mAip;
    }

    public ByteBuffer getApplicationUsageControl() {
        return this.mApplicationUsageControl;
    }

    public CRM getCRM() {
        return this.mCrm;
    }

    public CVM getCVM() {
        return this.mCVM;
    }

    public CL getCl() {
        return this.mCl;
    }

    public ByteBuffer getCpr() {
        return this.mCpr;
    }

    public ByteBuffer getCtq() {
        return this.mCtq;
    }

    public int getProfileId() {
        return this.mProfileId;
    }

    public ByteBuffer getPru() {
        return this.mPru;
    }

    public void setAfl(ByteBuffer byteBuffer) {
        this.mAfl = byteBuffer;
    }

    public void setAip(ByteBuffer byteBuffer) {
        this.mAip = byteBuffer;
    }

    public void setApplicationUsageControl(ByteBuffer byteBuffer) {
        this.mApplicationUsageControl = byteBuffer;
    }

    public void setCRM(CRM cRM) {
        this.mCrm = cRM;
    }

    public void setCVM(CVM cVM) {
        this.mCVM = cVM;
    }

    public void setCl(CL cL) {
        this.mCl = cL;
    }

    public void setCpr(ByteBuffer byteBuffer) {
        this.mCpr = byteBuffer;
    }

    public void setCtq(ByteBuffer byteBuffer) {
        this.mCtq = byteBuffer;
    }

    public void setProfileId(int n2) {
        this.mProfileId = n2;
    }

    public void setPru(ByteBuffer byteBuffer) {
        this.mPru = byteBuffer;
    }

    public static class CL {
        private long mCL_Cons_Limit;
        private long mCL_Cum_Limit;
        private long mCL_STA_Limit;
        private long mClAccumulator;
        private long mClCounter;

        public long getCL_Cons_Limit() {
            return this.mCL_Cons_Limit;
        }

        public long getCL_Cum_Limit() {
            return this.mCL_Cum_Limit;
        }

        public long getCL_STA_Limit() {
            return this.mCL_STA_Limit;
        }

        public long getClAccumulator() {
            return this.mClAccumulator;
        }

        public long getClCounter() {
            return this.mClCounter;
        }

        public void incrementClCounter() {
            this.mClCounter = 1L + this.mClCounter;
        }

        public void setCL_Cons_Limit(long l2) {
            this.mCL_Cons_Limit = l2;
        }

        public void setCL_Cum_Limit(long l2) {
            this.mCL_Cum_Limit = l2;
        }

        public void setCL_STA_Limit(long l2) {
            this.mCL_STA_Limit = l2;
        }

        public void setClAccumulator(long l2) {
            this.mClAccumulator = l2;
        }

        public void setClCounter(long l2) {
            this.mClCounter = l2;
        }
    }

    public static class CRM {
        private ByteBuffer mCRM_CAC_Default;
        private ByteBuffer mCRM_CAC_Denial;
        private ByteBuffer mCRM_CAC_Online;
        private ByteBuffer mCRM_CAC_Switch_Interface;
        private long mCrmAccumulator;
        private long mCrmCounter;
        private long mLCOA;
        private long mLCOL;
        private long mSTA;
        private long mUCOA;
        private long mUCOL;

        public ByteBuffer getCRM_CAC_Default() {
            return this.mCRM_CAC_Default;
        }

        public ByteBuffer getCRM_CAC_Denial() {
            return this.mCRM_CAC_Denial;
        }

        public ByteBuffer getCRM_CAC_Online() {
            return this.mCRM_CAC_Online;
        }

        public ByteBuffer getCRM_CAC_Switch_Interface() {
            return this.mCRM_CAC_Switch_Interface;
        }

        public long getCrmAccumulator() {
            return this.mCrmAccumulator;
        }

        public long getCrmCounter() {
            return this.mCrmCounter;
        }

        public long getLCOA() {
            return this.mLCOA;
        }

        public long getLCOL() {
            return this.mLCOL;
        }

        public long getSTA() {
            return this.mSTA;
        }

        public long getUCOA() {
            return this.mUCOA;
        }

        public long getUCOL() {
            return this.mUCOL;
        }

        public void incrementCrmCounter() {
            this.mCrmCounter = 1L + this.mCrmCounter;
            if (this.mCrmCounter > 255L) {
                this.mCrmCounter = 255L;
            }
        }

        public void resetCrm() {
            this.mCrmAccumulator = 0L;
            this.mCrmCounter = 0L;
        }

        public void setCRM_CAC_Default(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Default = byteBuffer;
        }

        public void setCRM_CAC_Denial(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Denial = byteBuffer;
        }

        public void setCRM_CAC_Online(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Online = byteBuffer;
        }

        public void setCRM_CAC_Switch_Interface(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Switch_Interface = byteBuffer;
        }

        public void setCrmAccumulator(long l2) {
            this.mCrmAccumulator = l2;
        }

        public void setCrmCounter(long l2) {
            this.mCrmCounter = l2;
        }

        public void setLCOA(long l2) {
            this.mLCOA = l2;
        }

        public void setLCOL(long l2) {
            this.mLCOL = l2;
        }

        public void setSTA(long l2) {
            this.mSTA = l2;
        }

        public void setUCOA(long l2) {
            this.mUCOA = l2;
        }

        public void setUCOL(long l2) {
            this.mUCOL = l2;
        }
    }

    public static class CVM {
        private ByteBuffer mCVM_CAC_Online_PIN;
        private long mCVM_CAC_Signature;
        private long mCVM_Cons_Limit_1;
        private long mCVM_Cons_Limit_2;
        private long mCVM_Cum_Limit_1;
        private long mCVM_Cum_Limit_2;
        private long mCVM_Sta_Limit_1;
        private long mCVM_Sta_Limit_2;
        private long mCvmAccumulator;
        private long mCvmCounter;

        public ByteBuffer getCVM_CAC_Online_PIN() {
            return this.mCVM_CAC_Online_PIN;
        }

        public long getCVM_CAC_Signature() {
            return this.mCVM_CAC_Signature;
        }

        public long getCVM_Cons_Limit_1() {
            return this.mCVM_Cons_Limit_1;
        }

        public long getCVM_Cons_Limit_2() {
            return this.mCVM_Cons_Limit_2;
        }

        public long getCVM_Cum_Limit_1() {
            return this.mCVM_Cum_Limit_1;
        }

        public long getCVM_Cum_Limit_2() {
            return this.mCVM_Cum_Limit_2;
        }

        public long getCVM_Sta_Limit_1() {
            return this.mCVM_Sta_Limit_1;
        }

        public long getCVM_Sta_Limit_2() {
            return this.mCVM_Sta_Limit_2;
        }

        public long getCvmAccumulator() {
            return this.mCvmAccumulator;
        }

        public long getCvmCounter() {
            return this.mCvmCounter;
        }

        public void incrementCvmCounter() {
            this.mCvmCounter = 1L + this.mCvmCounter;
            if (this.mCvmCounter > 255L) {
                this.mCvmCounter = 255L;
            }
        }

        public void setCVM_CAC_Online_PIN(ByteBuffer byteBuffer) {
            this.mCVM_CAC_Online_PIN = byteBuffer;
        }

        public void setCVM_CAC_Signature(long l2) {
            this.mCVM_CAC_Signature = l2;
        }

        public void setCVM_Cons_Limit_1(long l2) {
            this.mCVM_Cons_Limit_1 = l2;
        }

        public void setCVM_Cons_Limit_2(long l2) {
            this.mCVM_Cons_Limit_2 = l2;
        }

        public void setCVM_Cum_Limit_1(long l2) {
            this.mCVM_Cum_Limit_1 = l2;
        }

        public void setCVM_Cum_Limit_2(long l2) {
            this.mCVM_Cum_Limit_2 = l2;
        }

        public void setCVM_Sta_Limit_1(long l2) {
            this.mCVM_Sta_Limit_1 = l2;
        }

        public void setCVM_Sta_Limit_2(long l2) {
            this.mCVM_Sta_Limit_2 = l2;
        }

        public void setCvmAccumulator(long l2) {
            this.mCvmAccumulator = l2;
        }

        public void setCvmCounter(long l2) {
            this.mCvmCounter = l2;
        }
    }

}

