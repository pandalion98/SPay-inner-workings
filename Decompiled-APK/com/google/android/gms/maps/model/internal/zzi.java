package com.google.android.gms.maps.model.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzi extends IInterface {

    public static abstract class zza extends Binder implements zzi {

        private static class zza implements zzi {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public float getBearing() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LatLngBounds getBounds() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    LatLngBounds zzdV = obtain2.readInt() != 0 ? LatLngBounds.CREATOR.zzdV(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzdV;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getHeight() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getId() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LatLng getPosition() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
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

            public float getTransparency() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getWidth() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(7, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(20, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBearing(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDimensions(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPosition(LatLng latLng) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
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

            public void setPositionFromBounds(LatLngBounds latLngBounds) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    if (latLngBounds != null) {
                        obtain.writeInt(1);
                        latLngBounds.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTransparency(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(17, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(float f, float f2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zza com_google_android_gms_maps_model_internal_zza) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    if (com_google_android_gms_maps_model_internal_zza != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_maps_model_internal_zza.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean zza(zzi com_google_android_gms_maps_model_internal_zzi) {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzi != null ? com_google_android_gms_maps_model_internal_zzi.asBinder() : null);
                    this.zzle.transact(19, obtain, obtain2, 0);
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

            public void zzm(zzd com_google_android_gms_dynamic_zzd) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzi zzbY(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzi)) ? new zza(iBinder) : (zzi) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            zza com_google_android_gms_maps_model_internal_zza = null;
            int i3 = 0;
            LatLng zzdW;
            float width;
            LatLngBounds zzdV;
            boolean isVisible;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    remove();
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    String id = getId();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    if (parcel.readInt() != 0) {
                        zzdW = LatLng.CREATOR.zzdW(parcel);
                    }
                    setPosition(zzdW);
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    zzdW = getPosition();
                    parcel2.writeNoException();
                    if (zzdW != null) {
                        parcel2.writeInt(1);
                        zzdW.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    setDimensions(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    zza(parcel.readFloat(), parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    width = getWidth();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    width = getHeight();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    if (parcel.readInt() != 0) {
                        zzdV = LatLngBounds.CREATOR.zzdV(parcel);
                    }
                    setPositionFromBounds(zzdV);
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect283r1 /*10*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    zzdV = getBounds();
                    parcel2.writeNoException();
                    if (zzdV != null) {
                        parcel2.writeInt(1);
                        zzdV.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case CertStatus.UNREVOKED /*11*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    setBearing(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case CertStatus.UNDETERMINED /*12*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    width = getBearing();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    return true;
                case NamedCurve.sect571k1 /*13*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    setZIndex(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect571r1 /*14*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    width = getZIndex();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    return true;
                case NamedCurve.secp160k1 /*15*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    setVisible(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.dataEncipherment /*16*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    isVisible = isVisible();
                    parcel2.writeNoException();
                    if (isVisible) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.secp160r2 /*17*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    setTransparency(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp192k1 /*18*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    width = getTransparency();
                    parcel2.writeNoException();
                    parcel2.writeFloat(width);
                    return true;
                case NamedCurve.secp192r1 /*19*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    isVisible = zza(zzbY(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (isVisible) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    int hashCodeRemote = hashCodeRemote();
                    parcel2.writeNoException();
                    parcel2.writeInt(hashCodeRemote);
                    return true;
                case NamedCurve.secp224r1 /*21*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    zzm(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp256k1 /*22*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_maps_model_internal_zza = zza.CREATOR.zzeh(parcel);
                    }
                    zza(com_google_android_gms_maps_model_internal_zza);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.model.internal.IGroundOverlayDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    float getBearing();

    LatLngBounds getBounds();

    float getHeight();

    String getId();

    LatLng getPosition();

    float getTransparency();

    float getWidth();

    float getZIndex();

    int hashCodeRemote();

    boolean isVisible();

    void remove();

    void setBearing(float f);

    void setDimensions(float f);

    void setPosition(LatLng latLng);

    void setPositionFromBounds(LatLngBounds latLngBounds);

    void setTransparency(float f);

    void setVisible(boolean z);

    void setZIndex(float f);

    void zza(float f, float f2);

    void zza(zza com_google_android_gms_maps_model_internal_zza);

    boolean zza(zzi com_google_android_gms_maps_model_internal_zzi);

    void zzm(zzd com_google_android_gms_dynamic_zzd);
}
