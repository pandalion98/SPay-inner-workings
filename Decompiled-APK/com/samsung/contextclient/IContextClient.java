package com.samsung.contextclient;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.p027a.IPoiListener.IPoiListener;
import java.util.List;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.contextclient.a */
public interface IContextClient extends IInterface {

    /* renamed from: com.samsung.contextclient.a.a */
    public static abstract class IContextClient extends Binder implements IContextClient {
        public IContextClient() {
            attachInterface(this, "com.samsung.contextclient.IContextClient");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1378a(parcel.createTypedArrayList(Poi.CREATOR), IPoiListener.m1385b(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1379d(parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    gs();
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1377a(parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    Location location;
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    if (parcel.readInt() != 0) {
                        location = (Location) Location.CREATOR.createFromParcel(parcel);
                    } else {
                        location = null;
                    }
                    List a = m1376a(location, parcel.readDouble());
                    parcel2.writeNoException();
                    parcel2.writeTypedList(a);
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1380e(parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1381f(parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    m1382g(parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.samsung.contextclient.IContextClient");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    List<Poi> m1376a(Location location, double d);

    void m1377a(String str, int i, String str2);

    void m1378a(List<Poi> list, com.samsung.contextclient.p027a.IPoiListener iPoiListener);

    void m1379d(List<Poi> list);

    void m1380e(List<Poi> list);

    void m1381f(List<Poi> list);

    void m1382g(List<Poi> list);

    void gs();
}
