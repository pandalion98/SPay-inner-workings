package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzw;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class SupportMapFragment extends Fragment {
    private GoogleMap zzapW;
    private final zzb zzaqH;

    static class zza implements MapLifecycleDelegate {
        private final Fragment zzPt;
        private final IMapFragmentDelegate zzapX;

        /* renamed from: com.google.android.gms.maps.SupportMapFragment.zza.1 */
        class C02611 extends com.google.android.gms.maps.internal.zzm.zza {
            final /* synthetic */ OnMapReadyCallback zzapY;
            final /* synthetic */ zza zzaqI;

            C02611(zza com_google_android_gms_maps_SupportMapFragment_zza, OnMapReadyCallback onMapReadyCallback) {
                this.zzaqI = com_google_android_gms_maps_SupportMapFragment_zza;
                this.zzapY = onMapReadyCallback;
            }

            public void zza(IGoogleMapDelegate iGoogleMapDelegate) {
                this.zzapY.onMapReady(new GoogleMap(iGoogleMapDelegate));
            }
        }

        public zza(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.zzapX = (IMapFragmentDelegate) zzx.zzl(iMapFragmentDelegate);
            this.zzPt = (Fragment) zzx.zzl(fragment);
        }

        public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            try {
                this.zzapX.getMapAsync(new C02611(this, onMapReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onCreate(Bundle bundle) {
            if (bundle == null) {
                try {
                    bundle = new Bundle();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
            Bundle arguments = this.zzPt.getArguments();
            if (arguments != null && arguments.containsKey("MapOptions")) {
                zzw.zza(bundle, "MapOptions", arguments.getParcelable("MapOptions"));
            }
            this.zzapX.onCreate(bundle);
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            try {
                return (View) zze.zzf(this.zzapX.onCreateView(zze.zzn(layoutInflater), zze.zzn(viewGroup), bundle));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.zzapX.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.zzapX.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            try {
                this.zzapX.onInflate(zze.zzn(activity), (GoogleMapOptions) bundle.getParcelable("MapOptions"), bundle2);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.zzapX.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.zzapX.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.zzapX.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.zzapX.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onStart() {
        }

        public void onStop() {
        }

        public IMapFragmentDelegate zzqr() {
            return this.zzapX;
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Fragment zzPt;
        protected zzf<zza> zzaqa;
        private final List<OnMapReadyCallback> zzaqb;
        private Activity zzoi;

        zzb(Fragment fragment) {
            this.zzaqb = new ArrayList();
            this.zzPt = fragment;
        }

        private void setActivity(Activity activity) {
            this.zzoi = activity;
            zzqs();
        }

        public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            if (zzlg() != null) {
                ((zza) zzlg()).getMapAsync(onMapReadyCallback);
            } else {
                this.zzaqb.add(onMapReadyCallback);
            }
        }

        protected void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportMapFragment_zza) {
            this.zzaqa = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_SupportMapFragment_zza;
            zzqs();
        }

        public void zzqs() {
            if (this.zzoi != null && this.zzaqa != null && zzlg() == null) {
                try {
                    MapsInitializer.initialize(this.zzoi);
                    this.zzaqa.zza(new zza(this.zzPt, com.google.android.gms.maps.internal.zzx.zzac(this.zzoi).zzj(zze.zzn(this.zzoi))));
                    for (OnMapReadyCallback mapAsync : this.zzaqb) {
                        ((zza) zzlg()).getMapAsync(mapAsync);
                    }
                    this.zzaqb.clear();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public SupportMapFragment() {
        this.zzaqH = new zzb(this);
    }

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    public static SupportMapFragment newInstance(GoogleMapOptions googleMapOptions) {
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", googleMapOptions);
        supportMapFragment.setArguments(bundle);
        return supportMapFragment;
    }

    @Deprecated
    public final GoogleMap getMap() {
        IMapFragmentDelegate zzqr = zzqr();
        if (zzqr == null) {
            return null;
        }
        try {
            IGoogleMapDelegate map = zzqr.getMap();
            if (map == null) {
                return null;
            }
            if (this.zzapW == null || this.zzapW.zzqg().asBinder() != map.asBinder()) {
                this.zzapW = new GoogleMap(map);
            }
            return this.zzapW;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        zzx.zzbd("getMapAsync must be called on the main thread.");
        this.zzaqH.getMapAsync(onMapReadyCallback);
    }

    public void onActivityCreated(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onActivityCreated(bundle);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.zzaqH.setActivity(activity);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzaqH.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.zzaqH.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onDestroy() {
        this.zzaqH.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaqH.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        this.zzaqH.setActivity(activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attributeSet);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("MapOptions", createFromAttributes);
        this.zzaqH.onInflate(activity, bundle2, bundle);
    }

    public void onLowMemory() {
        this.zzaqH.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaqH.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaqH.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(bundle);
        this.zzaqH.onSaveInstanceState(bundle);
    }

    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }

    protected IMapFragmentDelegate zzqr() {
        this.zzaqH.zzqs();
        return this.zzaqH.zzlg() == null ? null : ((zza) this.zzaqH.zzlg()).zzqr();
    }
}
