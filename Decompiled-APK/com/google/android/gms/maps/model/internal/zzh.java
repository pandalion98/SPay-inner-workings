package com.google.android.gms.maps.model.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.maps.model.LatLng;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzh extends IInterface {

    public static abstract class zza extends Binder implements zzh {

        private static class zza implements zzh {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public LatLng getCenter() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    LatLng zzdW = obtain2.readInt() != 0 ? LatLng.CREATOR.zzdW(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzdW;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getFillColor() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getId() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public double getRadius() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    double readDouble = obtain2.readDouble();
                    return readDouble;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getStrokeColor() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getStrokeWidth() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getZIndex() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int hashCodeRemote() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isVisible() {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void remove() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCenter(LatLng latLng) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (latLng != null) {
                        obtain.writeInt(1);
                        latLng.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFillColor(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeInt(i);
                    this.zzle.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setRadius(double d) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeDouble(d);
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setStrokeColor(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeInt(i);
                    this.zzle.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setStrokeWidth(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setVisible(boolean z) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzle.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setZIndex(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean zza(zzh com_google_android_gms_maps_model_internal_zzh) {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzh != null ? com_google_android_gms_maps_model_internal_zzh.asBinder() : null);
                    this.zzle.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzh zzbX(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzh)) ? new zza(iBinder) : (zzh) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            int i3 = 0;
            float strokeWidth;
            boolean isVisible;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    remove();
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    String id = getId();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setCenter(parcel.readInt() != 0 ? LatLng.CREATOR.zzdW(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    LatLng center = getCenter();
                    parcel2.writeNoException();
                    if (center != null) {
                        parcel2.writeInt(1);
                        center.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setRadius(parcel.readDouble());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    double radius = getRadius();
                    parcel2.writeNoException();
                    parcel2.writeDouble(radius);
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setStrokeWidth(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    strokeWidth = getStrokeWidth();
                    parcel2.writeNoException();
                    parcel2.writeFloat(strokeWidth);
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setStrokeColor(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect283r1 /*10*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i3 = getStrokeColor();
                    parcel2.writeNoException();
                    parcel2.writeInt(i3);
                    return true;
                case CertStatus.UNREVOKED /*11*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setFillColor(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case CertStatus.UNDETERMINED /*12*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i3 = getFillColor();
                    parcel2.writeNoException();
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.sect571k1 /*13*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    setZIndex(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect571r1 /*14*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    strokeWidth = getZIndex();
                    parcel2.writeNoException();
                    parcel2.writeFloat(strokeWidth);
                    return true;
                case NamedCurve.secp160k1 /*15*/:
                    boolean z;
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setVisible(z);
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.dataEncipherment /*16*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    isVisible = isVisible();
                    parcel2.writeNoException();
                    if (isVisible) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.secp160r2 /*17*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    isVisible = zza(zzbX(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (isVisible) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.secp192k1 /*18*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    i3 = hashCodeRemote();
                    parcel2.writeNoException();
                    parcel2.writeInt(i3);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.model.internal.ICircleDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    LatLng getCenter();

    int getFillColor();

    String getId();

    double getRadius();

    int getStrokeColor();

    float getStrokeWidth();

    float getZIndex();

    int hashCodeRemote();

    boolean isVisible();

    void remove();

    void setCenter(LatLng latLng);

    void setFillColor(int i);

    void setRadius(double d);

    void setStrokeColor(int i);

    void setStrokeWidth(float f);

    void setVisible(boolean z);

    void setZIndex(float f);

    boolean zza(zzh com_google_android_gms_maps_model_internal_zzh);
}
