package android.telephony;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.TelephonyProperties;
import com.samsung.android.telephony.MultiSimManager;

public abstract class CellLocation {
    public abstract void fillInNotifierBundle(Bundle bundle);

    public abstract boolean isEmpty();

    public static void requestLocationUpdate() {
        try {
            ITelephony phone = Stub.asInterface(ServiceManager.getService(PhoneConstants.PHONE_KEY));
            if (phone != null) {
                phone.updateServiceLocation();
            }
        } catch (RemoteException e) {
        }
    }

    public static CellLocation newFromBundle(Bundle bundle) {
        int subId = SubscriptionManager.getDefaultDataSubId();
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        if (TelephonyManager.getDefault().getPhoneCount() > 1 && "CTC".equals(salesCode)) {
            String str = IccCardConstants.INTENT_VALUE_ICC_READY;
            TelephonyManager.getDefault();
            if (!str.equals(TelephonyManager.getTelephonyProperty(MultiSimManager.getSubscriptionId(0)[0], TelephonyProperties.PROPERTY_SIM_STATE, ""))) {
                str = IccCardConstants.INTENT_VALUE_ICC_READY;
                TelephonyManager.getDefault();
                if (str.equals(TelephonyManager.getTelephonyProperty(MultiSimManager.getSubscriptionId(1)[0], TelephonyProperties.PROPERTY_SIM_STATE, ""))) {
                    subId = MultiSimManager.getSubscriptionId(1)[0];
                }
            }
            if (IccCardConstants.INTENT_VALUE_ICC_ABSENT.equals(MultiSimManager.getTelephonyProperty(TelephonyProperties.PROPERTY_SIM_STATE, 0, "")) && IccCardConstants.INTENT_VALUE_ICC_READY.equals(MultiSimManager.getTelephonyProperty(TelephonyProperties.PROPERTY_SIM_STATE, 1, ""))) {
                subId = MultiSimManager.getSubscriptionId(1)[0];
            }
        }
        switch (TelephonyManager.getDefault().getCurrentPhoneType(subId)) {
            case 1:
                return new GsmCellLocation(bundle);
            case 2:
                return new CdmaCellLocation(bundle);
            default:
                return null;
        }
    }

    public static CellLocation getEmpty() {
        switch (TelephonyManager.getDefault().getCurrentPhoneType()) {
            case 1:
                return new GsmCellLocation();
            case 2:
                return new CdmaCellLocation();
            default:
                return null;
        }
    }
}
