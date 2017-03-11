package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.core.hce.SPayHCEService;
import com.samsung.android.spayfw.p002b.Log;

public class PackageStateReceiver extends BroadcastReceiver {

    /* renamed from: com.samsung.android.spayfw.core.PackageStateReceiver.1 */
    class C04061 implements ICommonCallback {
        final /* synthetic */ PackageStateReceiver ju;

        C04061(PackageStateReceiver packageStateReceiver) {
            this.ju = packageStateReceiver;
        }

        public IBinder asBinder() {
            return null;
        }

        public void onSuccess(String str) {
            Log.m287i("PackageStateReceiver", "onSuccess");
        }

        public void onFail(String str, int i) {
            Log.m287i("PackageStateReceiver", "onFail : " + i);
        }
    }

    public void onReceive(Context context, Intent intent) {
        String packageName = getPackageName(intent);
        Log.m287i("PackageStateReceiver", "PackageName : " + packageName);
        Log.m287i("PackageStateReceiver", "Data Removed : " + intent.getBooleanExtra("android.intent.extra.DATA_REMOVED", false));
        if (packageName != null && "com.samsung.android.spay".equals(packageName) && intent.getBooleanExtra("android.intent.extra.DATA_REMOVED", false)) {
            Log.m287i("PackageStateReceiver", "Initiate Reset");
            Log.m287i("PackageStateReceiver", "PF detects wallet app uninstall and triggers reset notification");
            packageName = "PFE0BR01";
            PaymentFrameworkApp.az().sendMessage(PaymentFrameworkMessage.m620a(13, "FACTORY_RESET:PFE0BR01", new C04061(this)));
            PaymentFrameworkApp.m320d(PackageStateReceiver.class);
            SPayHCEService.disable();
            SPayHCEReceiver.disable();
        }
    }

    String getPackageName(Intent intent) {
        Uri data = intent.getData();
        return data != null ? data.getSchemeSpecificPart() : null;
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(PackageStateReceiver.class);
    }
}
