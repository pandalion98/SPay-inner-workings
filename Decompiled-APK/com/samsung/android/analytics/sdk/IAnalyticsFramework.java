package com.samsung.android.analytics.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.analytics.sdk.a */
public interface IAnalyticsFramework extends IInterface {

    /* renamed from: com.samsung.android.analytics.sdk.a.a */
    public static abstract class IAnalyticsFramework extends Binder implements IAnalyticsFramework {
        public IAnalyticsFramework() {
            attachInterface(this, "com.samsung.android.analytics.sdk.IAnalyticsFramework");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            boolean z = false;
            AnalyticContext analyticContext;
            int a;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    AnalyticEvent analyticEvent;
                    boolean z2;
                    parcel.enforceInterface("com.samsung.android.analytics.sdk.IAnalyticsFramework");
                    if (parcel.readInt() != 0) {
                        analyticEvent = (AnalyticEvent) AnalyticEvent.CREATOR.createFromParcel(parcel);
                    } else {
                        analyticEvent = null;
                    }
                    if (parcel.readInt() != 0) {
                        analyticContext = (AnalyticContext) AnalyticContext.CREATOR.createFromParcel(parcel);
                    } else {
                        analyticContext = null;
                    }
                    if (parcel.readInt() != 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    a = m140a(analyticEvent, analyticContext, z2);
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.samsung.android.analytics.sdk.IAnalyticsFramework");
                    List createTypedArrayList = parcel.createTypedArrayList(AnalyticEvent.CREATOR);
                    if (parcel.readInt() != 0) {
                        analyticContext = (AnalyticContext) AnalyticContext.CREATOR.createFromParcel(parcel);
                    } else {
                        analyticContext = null;
                    }
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    a = m141a(createTypedArrayList, analyticContext, z);
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.samsung.android.analytics.sdk.IAnalyticsFramework");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int m140a(AnalyticEvent analyticEvent, AnalyticContext analyticContext, boolean z);

    int m141a(List<AnalyticEvent> list, AnalyticContext analyticContext, boolean z);
}
