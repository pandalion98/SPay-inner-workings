package com.samsung.android.spayfw.payprovider.discover.payment;

import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public enum DiscoverApduHandlerState {
    DiscoverAPDUStateGetData {
        public DiscoverApduProcessingResult m900g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sV, sY, ta});
        }
    },
    DiscoverAPDUStateInitiated {
        public DiscoverApduProcessingResult m901g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sV, sY, sX, sW, ta});
        }
    },
    DiscoverAPDUStateSelected {
        public DiscoverApduProcessingResult m902h(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sV, sY, sX, sW, ta});
        }
    },
    DiscoverAPDUStateReady {
        public DiscoverApduProcessingResult m903g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public DiscoverApduProcessingResult m904h(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sV, sX, sZ, ta});
        }
    },
    DiscoverAPDUStateInitial {
        public DiscoverApduProcessingResult m905f(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public DiscoverApduProcessingResult m906g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public DiscoverApduProcessingResult m907h(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sY, sZ, ta});
        }
    },
    DiscoverAPDUStateTestUpdate {
        public DiscoverApduProcessingResult m908g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public DiscoverApduProcessingResult m909h(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public DiscoverApduProcessingResult m910i(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
            return cO();
        }

        public List<DiscoverApduHandlerState> cP() {
            return Arrays.asList(new DiscoverApduHandlerState[]{sY, ta, sX, sW});
        }
    };

    public abstract List<DiscoverApduHandlerState> cP();

    public DiscoverApduProcessingResult m895e(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        Log.m287i("DCSDK_DiscoverApduHandlerState", "state: " + name());
        if (byteBuffer == null) {
            Log.m286e("DCSDK_DiscoverApduHandlerState", "process: apdu is null");
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
        } else if (discoverTransactionContext == null) {
            Log.m286e("DCSDK_DiscoverApduHandlerState", "process: context is null, not  ready for payment.");
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
        } else if (discoverPaymentCard == null) {
            Log.m286e("DCSDK_DiscoverApduHandlerState", "process: profile is null, not  ready for payment.");
            return new DiscoverApduProcessingResult((short) ISO7816.SW_WRONG_DATA);
        } else {
            byte b = byteBuffer.getByte(1);
            Log.m287i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: " + (b & GF2Field.MASK));
            switch (b) {
                case (byte) -92:
                    Log.m287i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: SELECT APDU received");
                    return m896f(byteBuffer, discoverTransactionContext, discoverPaymentCard);
                case (byte) -88:
                    Log.m287i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: GPO APDU received");
                    return m897g(byteBuffer, discoverTransactionContext, discoverPaymentCard);
                case (byte) -78:
                    Log.m287i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: READ RECORD APDU received");
                    return m898h(byteBuffer, discoverTransactionContext, discoverPaymentCard);
                case (byte) -54:
                    Log.m287i("DCSDK_DiscoverApduHandlerState", "handleApdu, APDU INS: GET DATA APDU received");
                    return m899i(byteBuffer, discoverTransactionContext, discoverPaymentCard);
                default:
                    return new DiscoverApduProcessingResult((short) ISO7816.SW_INS_NOT_SUPPORTED);
            }
        }
    }

    private DiscoverApduProcessingResult m893a(DiscoverApduHandler discoverApduHandler) {
        DiscoverApduProcessingResult cK = discoverApduHandler.cK();
        m894a(discoverApduHandler, cK);
        return cK;
    }

    public DiscoverApduProcessingResult m896f(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return m893a(DiscoverApduHandler.m927a(byteBuffer, discoverTransactionContext, discoverPaymentCard));
    }

    public DiscoverApduProcessingResult m897g(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return m893a(DiscoverApduHandler.m928b(byteBuffer, discoverTransactionContext, discoverPaymentCard));
    }

    public DiscoverApduProcessingResult m898h(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return m893a(DiscoverApduHandler.m929c(byteBuffer, discoverTransactionContext, discoverPaymentCard));
    }

    public DiscoverApduProcessingResult m899i(ByteBuffer byteBuffer, DiscoverTransactionContext discoverTransactionContext, DiscoverPaymentCard discoverPaymentCard) {
        return m893a(DiscoverApduHandler.m930d(byteBuffer, discoverTransactionContext, discoverPaymentCard));
    }

    public void m894a(DiscoverApduHandler discoverApduHandler, DiscoverApduProcessingResult discoverApduProcessingResult) {
        DiscoverApduHandlerState dA = discoverApduProcessingResult.dA();
        if (dA == null) {
            dA = this;
        }
        if (cP().contains(dA)) {
            discoverApduProcessingResult.m962a(dA);
            return;
        }
        Log.m286e("DCSDK_DiscoverApduHandlerState", "processApduResultWithHandler, wrong state requested: " + dA.name() + ", current state: " + name());
        cO();
    }

    public DiscoverApduProcessingResult cO() {
        return new DiscoverApduProcessingResult((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
    }
}
