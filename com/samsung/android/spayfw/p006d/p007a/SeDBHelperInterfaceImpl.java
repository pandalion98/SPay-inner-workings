package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperInterface;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.d.a.c */
public class SeDBHelperInterfaceImpl implements DBHelperInterface {
    private DBHelperInterface Br;

    /* renamed from: com.samsung.android.spayfw.d.a.c.1 */
    static /* synthetic */ class SeDBHelperInterfaceImpl {
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

    private SeDBHelperInterfaceImpl(DBHelperInterface dBHelperInterface) {
        this.Br = null;
        this.Br = dBHelperInterface;
    }

    public SQLiteDatabase getWritableDatabase(byte[] bArr) {
        return this.Br.getWritableDatabase(bArr);
    }

    public SQLiteDatabase getReadableDatabase(byte[] bArr) {
        return this.Br.getReadableDatabase(bArr);
    }

    public void m683a(DBHelperCallback dBHelperCallback) {
        this.Br.m291a(dBHelperCallback);
    }

    public static DBHelperInterface m682a(Context context, String str, int i, DBName dBName) {
        switch (SeDBHelperInterfaceImpl.Bs[dBName.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new SeDBHelperInterfaceImpl(SeCommonDBHelper.m681c(context, str, i));
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return new SeDBHelperInterfaceImpl(SeFraudDBHelper.m685X(context));
            case F2m.PPB /*3*/:
                return new SeDBHelperInterfaceImpl(SeVisaDBHelper.m688d(context, str, i));
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new SeDBHelperInterfaceImpl(SeMcDBHelper.m686Y(context));
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new SeDBHelperInterfaceImpl(SePlccDBHelper.m687Z(context));
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new SeDBHelperInterfaceImpl(SeDcDBHelper.m684W(context));
            default:
                return null;
        }
    }
}
