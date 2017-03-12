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

    public static class CL {
        private long mCL_Cons_Limit;
        private long mCL_Cum_Limit;
        private long mCL_STA_Limit;
        private long mClAccumulator;
        private long mClCounter;

        public long getClAccumulator() {
            return this.mClAccumulator;
        }

        public void setClAccumulator(long j) {
            this.mClAccumulator = j;
        }

        public long getClCounter() {
            return this.mClCounter;
        }

        public void setClCounter(long j) {
            this.mClCounter = j;
        }

        public void incrementClCounter() {
            this.mClCounter++;
        }

        public long getCL_Cons_Limit() {
            return this.mCL_Cons_Limit;
        }

        public void setCL_Cons_Limit(long j) {
            this.mCL_Cons_Limit = j;
        }

        public long getCL_Cum_Limit() {
            return this.mCL_Cum_Limit;
        }

        public void setCL_Cum_Limit(long j) {
            this.mCL_Cum_Limit = j;
        }

        public long getCL_STA_Limit() {
            return this.mCL_STA_Limit;
        }

        public void setCL_STA_Limit(long j) {
            this.mCL_STA_Limit = j;
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

        public void setCrmAccumulator(long j) {
            this.mCrmAccumulator = j;
        }

        public long getCrmAccumulator() {
            return this.mCrmAccumulator;
        }

        public void incrementCrmCounter() {
            this.mCrmCounter++;
            if (this.mCrmCounter > 255) {
                this.mCrmCounter = 255;
            }
        }

        public long getCrmCounter() {
            return this.mCrmCounter;
        }

        public void setCrmCounter(long j) {
            this.mCrmCounter = j;
        }

        public long getLCOL() {
            return this.mLCOL;
        }

        public void setLCOL(long j) {
            this.mLCOL = j;
        }

        public long getUCOL() {
            return this.mUCOL;
        }

        public void setUCOL(long j) {
            this.mUCOL = j;
        }

        public long getLCOA() {
            return this.mLCOA;
        }

        public void setLCOA(long j) {
            this.mLCOA = j;
        }

        public long getUCOA() {
            return this.mUCOA;
        }

        public void setUCOA(long j) {
            this.mUCOA = j;
        }

        public long getSTA() {
            return this.mSTA;
        }

        public void setSTA(long j) {
            this.mSTA = j;
        }

        public ByteBuffer getCRM_CAC_Switch_Interface() {
            return this.mCRM_CAC_Switch_Interface;
        }

        public void setCRM_CAC_Switch_Interface(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Switch_Interface = byteBuffer;
        }

        public ByteBuffer getCRM_CAC_Denial() {
            return this.mCRM_CAC_Denial;
        }

        public void setCRM_CAC_Denial(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Denial = byteBuffer;
        }

        public ByteBuffer getCRM_CAC_Online() {
            return this.mCRM_CAC_Online;
        }

        public void setCRM_CAC_Online(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Online = byteBuffer;
        }

        public ByteBuffer getCRM_CAC_Default() {
            return this.mCRM_CAC_Default;
        }

        public void setCRM_CAC_Default(ByteBuffer byteBuffer) {
            this.mCRM_CAC_Default = byteBuffer;
        }

        public void resetCrm() {
            this.mCrmAccumulator = 0;
            this.mCrmCounter = 0;
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

        public long getCvmAccumulator() {
            return this.mCvmAccumulator;
        }

        public void setCvmAccumulator(long j) {
            this.mCvmAccumulator = j;
        }

        public long getCvmCounter() {
            return this.mCvmCounter;
        }

        public void incrementCvmCounter() {
            this.mCvmCounter++;
            if (this.mCvmCounter > 255) {
                this.mCvmCounter = 255;
            }
        }

        public void setCvmCounter(long j) {
            this.mCvmCounter = j;
        }

        public ByteBuffer getCVM_CAC_Online_PIN() {
            return this.mCVM_CAC_Online_PIN;
        }

        public void setCVM_CAC_Online_PIN(ByteBuffer byteBuffer) {
            this.mCVM_CAC_Online_PIN = byteBuffer;
        }

        public long getCVM_CAC_Signature() {
            return this.mCVM_CAC_Signature;
        }

        public void setCVM_CAC_Signature(long j) {
            this.mCVM_CAC_Signature = j;
        }

        public long getCVM_Cons_Limit_1() {
            return this.mCVM_Cons_Limit_1;
        }

        public void setCVM_Cons_Limit_1(long j) {
            this.mCVM_Cons_Limit_1 = j;
        }

        public long getCVM_Cons_Limit_2() {
            return this.mCVM_Cons_Limit_2;
        }

        public void setCVM_Cons_Limit_2(long j) {
            this.mCVM_Cons_Limit_2 = j;
        }

        public long getCVM_Cum_Limit_1() {
            return this.mCVM_Cum_Limit_1;
        }

        public void setCVM_Cum_Limit_1(long j) {
            this.mCVM_Cum_Limit_1 = j;
        }

        public long getCVM_Cum_Limit_2() {
            return this.mCVM_Cum_Limit_2;
        }

        public void setCVM_Cum_Limit_2(long j) {
            this.mCVM_Cum_Limit_2 = j;
        }

        public long getCVM_Sta_Limit_1() {
            return this.mCVM_Sta_Limit_1;
        }

        public void setCVM_Sta_Limit_1(long j) {
            this.mCVM_Sta_Limit_1 = j;
        }

        public void setCVM_Sta_Limit_2(long j) {
            this.mCVM_Sta_Limit_2 = j;
        }

        public long getCVM_Sta_Limit_2() {
            return this.mCVM_Sta_Limit_2;
        }
    }

    public void setCtq(ByteBuffer byteBuffer) {
        this.mCtq = byteBuffer;
    }

    public ByteBuffer getCtq() {
        return this.mCtq;
    }

    public CRM getCRM() {
        return this.mCrm;
    }

    public void setCRM(CRM crm) {
        this.mCrm = crm;
    }

    public CVM getCVM() {
        return this.mCVM;
    }

    public void setCVM(CVM cvm) {
        this.mCVM = cvm;
    }

    public CL getCl() {
        return this.mCl;
    }

    public void setCl(CL cl) {
        this.mCl = cl;
    }

    public ByteBuffer getApplicationUsageControl() {
        return this.mApplicationUsageControl;
    }

    public void setApplicationUsageControl(ByteBuffer byteBuffer) {
        this.mApplicationUsageControl = byteBuffer;
    }

    public void setProfileId(int i) {
        this.mProfileId = i;
    }

    public int getProfileId() {
        return this.mProfileId;
    }

    public ByteBuffer getPru() {
        return this.mPru;
    }

    public void setPru(ByteBuffer byteBuffer) {
        this.mPru = byteBuffer;
    }

    public void setAip(ByteBuffer byteBuffer) {
        this.mAip = byteBuffer;
    }

    public ByteBuffer getAip() {
        return this.mAip;
    }

    public void setAfl(ByteBuffer byteBuffer) {
        this.mAfl = byteBuffer;
    }

    public ByteBuffer getAfl() {
        return this.mAfl;
    }

    public ByteBuffer getCpr() {
        return this.mCpr;
    }

    public void setCpr(ByteBuffer byteBuffer) {
        this.mCpr = byteBuffer;
    }
}
