package com.google.android.gms.maps.model.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.LatLng;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzl extends IInterface {

    public static abstract class zza extends Binder implements zzl {

        private static class zza implements zzl {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public float getAlpha() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(26, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
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

            public float getRotation() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getSnippet() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getTitle() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int hashCodeRemote() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void hideInfoWindow() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDraggable() {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(10, obtain, obtain2, 0);
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

            public boolean isFlat() {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(21, obtain, obtain2, 0);
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

            public boolean isInfoWindowShown() {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(13, obtain, obtain2, 0);
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

            public boolean isVisible() {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(15, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAlpha(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAnchor(float f, float f2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    this.zzle.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDraggable(boolean z) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzle.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFlat(boolean z) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzle.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setInfoWindowAnchor(float f, float f2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    this.zzle.transact(24, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
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

            public void setRotation(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSnippet(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeString(str);
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTitle(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeString(str);
                    this.zzle.transact(5, obtain, obtain2, 0);
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
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzle.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showInfoWindow() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    this.zzle.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zza com_google_android_gms_maps_model_internal_zza) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (com_google_android_gms_maps_model_internal_zza != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_maps_model_internal_zza.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean zzh(zzl com_google_android_gms_maps_model_internal_zzl) {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
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

            public void zzn(zzd com_google_android_gms_dynamic_zzd) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzl zzcb(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzl)) ? new zza(iBinder) : (zzl) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            zza com_google_android_gms_maps_model_internal_zza = null;
            int i3 = 0;
            String id;
            LatLng zzdW;
            boolean isDraggable;
            boolean z;
            float rotation;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    remove();
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getId();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (parcel.readInt() != 0) {
                        zzdW = LatLng.CREATOR.zzdW(parcel);
                    }
                    setPosition(zzdW);
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
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
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setTitle(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getTitle();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setSnippet(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    id = getSnippet();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setDraggable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect283r1 /*10*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isDraggable();
                    parcel2.writeNoException();
                    if (isDraggable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case CertStatus.UNREVOKED /*11*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    showInfoWindow();
                    parcel2.writeNoException();
                    return true;
                case CertStatus.UNDETERMINED /*12*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    hideInfoWindow();
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect571k1 /*13*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isInfoWindowShown();
                    parcel2.writeNoException();
                    if (isDraggable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.sect571r1 /*14*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setVisible(z);
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp160k1 /*15*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isVisible();
                    parcel2.writeNoException();
                    if (isDraggable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case X509KeyUsage.dataEncipherment /*16*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = zzh(zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (isDraggable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.secp160r2 /*17*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    int hashCodeRemote = hashCodeRemote();
                    parcel2.writeNoException();
                    parcel2.writeInt(hashCodeRemote);
                    return true;
                case NamedCurve.secp192k1 /*18*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    zzn(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp192r1 /*19*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setAnchor(parcel.readFloat(), parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setFlat(z);
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp224r1 /*21*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    isDraggable = isFlat();
                    parcel2.writeNoException();
                    if (isDraggable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case NamedCurve.secp256k1 /*22*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setRotation(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp256r1 /*23*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    rotation = getRotation();
                    parcel2.writeNoException();
                    parcel2.writeFloat(rotation);
                    return true;
                case NamedCurve.secp384r1 /*24*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setInfoWindowAnchor(parcel.readFloat(), parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.secp521r1 /*25*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    setAlpha(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.brainpoolP256r1 /*26*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    rotation = getAlpha();
                    parcel2.writeNoException();
                    parcel2.writeFloat(rotation);
                    return true;
                case NamedCurve.brainpoolP512r1 /*28*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_maps_model_internal_zza = zza.CREATOR.zzeh(parcel);
                    }
                    zzb(com_google_android_gms_maps_model_internal_zza);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.model.internal.IMarkerDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    float getAlpha();

    String getId();

    LatLng getPosition();

    float getRotation();

    String getSnippet();

    String getTitle();

    int hashCodeRemote();

    void hideInfoWindow();

    boolean isDraggable();

    boolean isFlat();

    boolean isInfoWindowShown();

    boolean isVisible();

    void remove();

    void setAlpha(float f);

    void setAnchor(float f, float f2);

    void setDraggable(boolean z);

    void setFlat(boolean z);

    void setInfoWindowAnchor(float f, float f2);

    void setPosition(LatLng latLng);

    void setRotation(float f);

    void setSnippet(String str);

    void setTitle(String str);

    void setVisible(boolean z);

    void showInfoWindow();

    void zzb(zza com_google_android_gms_maps_model_internal_zza);

    boolean zzh(zzl com_google_android_gms_maps_model_internal_zzl);

    void zzn(zzd com_google_android_gms_dynamic_zzd);
}
