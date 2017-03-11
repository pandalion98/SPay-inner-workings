package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.zzc;
import com.google.android.gms.location.places.zzf;
import com.google.android.gms.location.places.zzj;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public interface zzpu extends IInterface {

    public static abstract class zza extends Binder implements zzpu {

        private static class zza implements zzpu {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public void zza(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(10, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(zzqo com_google_android_gms_internal_zzqo, String str, String str2, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_internal_zzqo != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(16, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(AddPlaceRequest addPlaceRequest, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (addPlaceRequest != null) {
                        obtain.writeInt(1);
                        addPlaceRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(14, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(PlaceReport placeReport, zzqh com_google_android_gms_internal_zzqh) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (placeReport != null) {
                        obtain.writeInt(1);
                        placeReport.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(15, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(zzc com_google_android_gms_location_places_zzc, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_location_places_zzc != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_location_places_zzc.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(11, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(zzf com_google_android_gms_location_places_zzf, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_location_places_zzf != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_location_places_zzf.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(9, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(zzj com_google_android_gms_location_places_zzj, LatLngBounds latLngBounds, List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_location_places_zzj != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_location_places_zzj.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (latLngBounds != null) {
                        obtain.writeInt(1);
                        latLngBounds.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringList(list);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(8, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(LatLng latLng, PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (latLng != null) {
                        obtain.writeInt(1);
                        latLng.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (placeFilter != null) {
                        obtain.writeInt(1);
                        placeFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(LatLngBounds latLngBounds, int i, String str, PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (latLngBounds != null) {
                        obtain.writeInt(1);
                        latLngBounds.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (placeFilter != null) {
                        obtain.writeInt(1);
                        placeFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    obtain.writeString(str);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    obtain.writeString(str);
                    if (latLngBounds != null) {
                        obtain.writeInt(1);
                        latLngBounds.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (autocompleteFilter != null) {
                        obtain.writeInt(1);
                        autocompleteFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(13, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    obtain.writeStringList(list);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzb(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(12, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzb(PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    if (placeFilter != null) {
                        obtain.writeInt(1);
                        placeFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzb(String str, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    obtain.writeString(str);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zzb(List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    obtain.writeStringList(list);
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzpv != null) {
                        iBinder = com_google_android_gms_internal_zzpv.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.zzle.transact(17, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zzpu zzbo(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzpu)) ? new zza(iBinder) : (zzpu) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            zzqh com_google_android_gms_internal_zzqh = null;
            String readString;
            List createStringArrayList;
            switch (i) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    LatLngBounds zzdV = parcel.readInt() != 0 ? LatLngBounds.CREATOR.zzdV(parcel) : null;
                    int readInt = parcel.readInt();
                    String readString2 = parcel.readString();
                    PlaceFilter zzdB = parcel.readInt() != 0 ? PlaceFilter.CREATOR.zzdB(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(zzdV, readInt, readString2, zzdB, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(readString, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    LatLng zzdW = parcel.readInt() != 0 ? LatLng.CREATOR.zzdW(parcel) : null;
                    PlaceFilter zzdB2 = parcel.readInt() != 0 ? PlaceFilter.CREATOR.zzdB(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(zzdW, zzdB2, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    PlaceFilter zzdB3 = parcel.readInt() != 0 ? PlaceFilter.CREATOR.zzdB(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zzb(zzdB3, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zzb(readString, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    createStringArrayList = parcel.createStringArrayList();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(createStringArrayList, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readInt() != 0 ? zzj.CREATOR.zzdD(parcel) : null, parcel.readInt() != 0 ? LatLngBounds.CREATOR.zzdV(parcel) : null, parcel.createStringArrayList(), parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readInt() != 0 ? zzf.CREATOR.zzdC(parcel) : null, parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case NamedCurve.sect283r1 /*10*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case CertStatus.UNREVOKED /*11*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readInt() != 0 ? zzc.CREATOR.zzdA(parcel) : null, parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case CertStatus.UNDETERMINED /*12*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zzb(parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case NamedCurve.sect571k1 /*13*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readString(), parcel.readInt() != 0 ? LatLngBounds.CREATOR.zzdV(parcel) : null, parcel.readInt() != 0 ? AutocompleteFilter.CREATOR.zzdz(parcel) : null, parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case NamedCurve.sect571r1 /*14*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    AddPlaceRequest addPlaceRequest = parcel.readInt() != 0 ? (AddPlaceRequest) AddPlaceRequest.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(addPlaceRequest, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case NamedCurve.secp160k1 /*15*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    PlaceReport createFromParcel = parcel.readInt() != 0 ? PlaceReport.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(createFromParcel, com_google_android_gms_internal_zzqh);
                    return true;
                case X509KeyUsage.dataEncipherment /*16*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    zza(parcel.readInt() != 0 ? zzqo.CREATOR.zzdM(parcel) : null, parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case NamedCurve.secp160r2 /*17*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    createStringArrayList = parcel.createStringArrayList();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zzb(createStringArrayList, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.location.places.internal.IGooglePlacesService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent);

    void zza(zzqo com_google_android_gms_internal_zzqo, String str, String str2, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(AddPlaceRequest addPlaceRequest, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(PlaceReport placeReport, zzqh com_google_android_gms_internal_zzqh);

    void zza(zzc com_google_android_gms_location_places_zzc, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent);

    void zza(zzf com_google_android_gms_location_places_zzf, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent);

    void zza(zzj com_google_android_gms_location_places_zzj, LatLngBounds latLngBounds, List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(LatLng latLng, PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(LatLngBounds latLngBounds, int i, String str, PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(String str, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zzb(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent);

    void zzb(PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zzb(String str, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zzb(List<String> list, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);
}
