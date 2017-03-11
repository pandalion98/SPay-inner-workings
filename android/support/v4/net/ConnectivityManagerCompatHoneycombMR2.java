package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return true;
        }
        switch (activeNetworkInfo.getType()) {
            case ECCurve.COORD_AFFINE /*0*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case F2m.PPB /*3*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return true;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case ECCurve.COORD_SKEWED /*7*/:
            case NamedCurve.sect283k1 /*9*/:
                return false;
            default:
                return true;
        }
    }
}
