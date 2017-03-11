package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperInterface;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.c.a.c */
public class SdlDBHelperInterfaceImpl implements DBHelperInterface {
    private DBHelperInterface Br;

    /* renamed from: com.samsung.android.spayfw.c.a.c.1 */
    static /* synthetic */ class SdlDBHelperInterfaceImpl {
        static final /* synthetic */ int[] Bs;

        static {
            Bs = new int[DBName.values().length];
            try {
                Bs[DBName.spayfw_enc.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                Bs[DBName.collector_enc.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                Bs[DBName.cbp_jan_enc.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                Bs[DBName.mc_enc.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                Bs[DBName.PlccCardData_enc.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                Bs[DBName.dc_provider.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    private SdlDBHelperInterfaceImpl(DBHelperInterface dBHelperInterface) {
        this.Br = null;
        this.Br = dBHelperInterface;
    }

    public SQLiteDatabase getWritableDatabase(byte[] bArr) {
        return this.Br.getWritableDatabase(bArr);
    }

    public SQLiteDatabase getReadableDatabase(byte[] bArr) {
        return this.Br.getReadableDatabase(bArr);
    }

    public void m295a(DBHelperCallback dBHelperCallback) {
        this.Br.m291a(dBHelperCallback);
    }

    public static DBHelperInterface m294a(Context context, String str, int i, DBName dBName) {
        switch (SdlDBHelperInterfaceImpl.Bs[dBName.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new SdlDBHelperInterfaceImpl(SdlCommonDBHelper.m293a(context, str, i));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new SdlDBHelperInterfaceImpl(SdlFraudDBHelper.m297T(context));
            case F2m.PPB /*3*/:
                return new SdlDBHelperInterfaceImpl(SdlVisaDBHelper.m300b(context, str, i));
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new SdlDBHelperInterfaceImpl(SdlMcDBHelper.m298U(context));
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new SdlDBHelperInterfaceImpl(SdlPlccDBHelper.m299V(context));
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new SdlDBHelperInterfaceImpl(SdlDcDBHelper.m296S(context));
            default:
                return null;
        }
    }
}
