package android.wipower;

import android.wipower.WipowerManager.PowerApplyEvent;
import android.wipower.WipowerManager.WipowerState;

public interface WipowerManagerCallback {
    void onPowerApply(PowerApplyEvent powerApplyEvent);

    void onWipowerAlert(byte b);

    void onWipowerData(WipowerDynamicParam wipowerDynamicParam);

    void onWipowerReady();

    void onWipowerStateChange(WipowerState wipowerState);
}
