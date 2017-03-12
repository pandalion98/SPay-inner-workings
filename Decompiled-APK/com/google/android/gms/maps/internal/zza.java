package com.google.android.gms.maps.internal;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public final class zza {
    public static Boolean zza(byte b) {
        switch (b) {
            case ECCurve.COORD_AFFINE /*0*/:
                return Boolean.FALSE;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    public static byte zzd(Boolean bool) {
        return bool != null ? bool.booleanValue() ? (byte) 1 : (byte) 0 : (byte) -1;
    }
}
