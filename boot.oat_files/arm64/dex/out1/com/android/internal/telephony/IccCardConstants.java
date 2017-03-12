package com.android.internal.telephony;

import com.sec.android.app.CscFeature;

public class IccCardConstants {
    public static final String INTENT_KEY_ICC_STATE = "ss";
    public static final String INTENT_KEY_ICC_TYPE = "ICC_TYPE";
    public static final String INTENT_KEY_LOCKED_REASON = "reason";
    public static final String INTENT_VALUE_ABSENT_ON_PERM_DISABLED = "PERM_DISABLED";
    public static final String INTENT_VALUE_ICC_ABSENT = "ABSENT";
    public static final String INTENT_VALUE_ICC_CARD_IO_ERROR = "CARD_IO_ERROR";
    public static final String INTENT_VALUE_ICC_ICCID = "ICCID";
    public static final String INTENT_VALUE_ICC_IMSI = "IMSI";
    public static final String INTENT_VALUE_ICC_INTERNAL_LOCKED = "INTERNAL_LOCKED";
    public static final String INTENT_VALUE_ICC_LOADED = "LOADED";
    public static final String INTENT_VALUE_ICC_LOCKED = "LOCKED";
    public static final String INTENT_VALUE_ICC_MDN = "MDN";
    public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
    public static final String INTENT_VALUE_ICC_READY = "READY";
    public static final String INTENT_VALUE_ICC_UNKNOWN = "UNKNOWN";
    public static final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";
    public static final String INTENT_VALUE_LOCKED_ON_PERSO = "PERSO";
    public static final String INTENT_VALUE_LOCKED_ON_PIN = "PIN";
    public static final String INTENT_VALUE_LOCKED_ON_PUK = "PUK";
    public static final String INTENT_VALUE_LOCKED_PERSO = "PERSO";
    public static final String INTENT_VALUE_LOCKED_SUBSET_NETWORK = "NETWORK_SUBSET";

    public enum State {
        UNKNOWN,
        ABSENT,
        PIN_REQUIRED,
        PUK_REQUIRED,
        NETWORK_LOCKED,
        READY,
        NOT_READY,
        PERM_DISABLED,
        CARD_IO_ERROR,
        PERSO_LOCKED,
        NETWORK_SUBSET_LOCKED,
        SIM_SERVICE_PROVIDER_LOCKED,
        DETECTED;

        public boolean isPinLocked() {
            if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_USIMPersonalizationKOR")) {
                if (this == PIN_REQUIRED || this == PUK_REQUIRED || this == PERSO_LOCKED) {
                    return true;
                }
                return false;
            } else if (this == PIN_REQUIRED || this == PUK_REQUIRED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean iccCardExist() {
            return this == PIN_REQUIRED || this == PUK_REQUIRED || this == NETWORK_LOCKED || this == READY || this == PERSO_LOCKED || this == DETECTED || this == PERM_DISABLED || this == CARD_IO_ERROR;
        }

        public static State intToState(int state) throws IllegalArgumentException {
            switch (state) {
                case 0:
                    return UNKNOWN;
                case 1:
                    return ABSENT;
                case 2:
                    return PIN_REQUIRED;
                case 3:
                    return PUK_REQUIRED;
                case 4:
                    return NETWORK_LOCKED;
                case 5:
                    return READY;
                case 6:
                    return NOT_READY;
                case 7:
                    return PERM_DISABLED;
                case 8:
                    return CARD_IO_ERROR;
                case 9:
                    return PERSO_LOCKED;
                case 10:
                    return NETWORK_SUBSET_LOCKED;
                case 11:
                    return SIM_SERVICE_PROVIDER_LOCKED;
                case 12:
                    return DETECTED;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
