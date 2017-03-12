package com.samsung.android.spayfw.payprovider.discover.db.dao;

public interface DcCommonDao<T> {

    public enum DetailDataId {
        DC_DATA_ID_MIN(0),
        DC_CARD_PROFILE(1),
        DC_CONTACTLESS_PAYMENT_DATA(2),
        DC_INAPP_PAYMENT_DATA(3),
        DC_PAYMENT_SECUREOBJ(4),
        DC_PAYMENT_OTPKDATA(5),
        DC_PAYMENT_PTHDATA(6),
        DC_DATA_ID_MAX(99);
        
        private int mValue;

        private DetailDataId(int i) {
            this.mValue = i;
        }

        public int cJ() {
            return this.mValue;
        }
    }
}
