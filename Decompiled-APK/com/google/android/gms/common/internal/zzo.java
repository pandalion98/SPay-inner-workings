package com.google.android.gms.common.internal;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.samsung.android.spayfw.appinterface.PushMessage;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class zzo {
    private static final Uri zzQo;
    private static final Uri zzQp;

    static {
        zzQo = Uri.parse("http://plus.google.com/");
        zzQp = zzQo.buildUpon().appendPath("circles").appendPath("find").build();
    }

    public static Intent zzbj(String str) {
        Uri fromParts = Uri.fromParts("package", str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(fromParts);
        return intent;
    }

    private static Uri zzbk(String str) {
        return Uri.parse("market://details").buildUpon().appendQueryParameter(PushMessage.JSON_KEY_ID, str).build();
    }

    public static Intent zzbl(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(zzbk(str));
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        intent.addFlags(PKIFailureInfo.signerNotTrusted);
        return intent;
    }

    public static Intent zzjl() {
        Intent intent = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
        intent.setPackage("com.google.android.wearable.app");
        return intent;
    }
}
