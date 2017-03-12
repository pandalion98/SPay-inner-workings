package com.google.android.gms.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.internal.zzw;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class StreetViewPanoramaFragment extends Fragment {
    private final zzb zzaqp;
    private StreetViewPanorama zzaqq;

    static class zza implements StreetViewLifecycleDelegate {
        private final Fragment zzXZ;
        private final IStreetViewPanoramaFragmentDelegate zzaqr;

        /* renamed from: com.google.android.gms.maps.StreetViewPanoramaFragment.zza.1 */
        class C02591 extends com.google.android.gms.maps.internal.zzu.zza {
            final /* synthetic */ OnStreetViewPanoramaReadyCallback zzaqs;
            final /* synthetic */ zza zzaqt;

            C02591(zza com_google_android_gms_maps_StreetViewPanoramaFragment_zza, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
                this.zzaqt = com_google_android_gms_maps_StreetViewPanoramaFragment_zza;
                this.zzaqs = onStreetViewPanoramaReadyCallback;
            }

            public void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) {
                this.zzaqs.onStreetViewPanoramaReady(new StreetViewPanorama(iStreetViewPanoramaDelegate));
            }
        }

        public zza(Fragment fragment, IStreetViewPanoramaFragmentDelegate iStreetViewPanoramaFragmentDelegate) {
            this.zzaqr = (IStreetViewPanoramaFragmentDelegate) zzx.zzl(iStreetViewPanoramaFragmentDelegate);
            this.zzXZ = (Fragment) zzx.zzl(fragment);
        }

        public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            try {
                this.zzaqr.getStreetViewPanoramaAsync(new C02591(this, onStreetViewPanoramaReadyCallback));
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
            Bundle arguments = this.zzXZ.getArguments();
            if (arguments != null && arguments.containsKey("StreetViewPanoramaOptions")) {
                zzw.zza(bundle, "StreetViewPanoramaOptions", arguments.getParcelable("StreetViewPanoramaOptions"));
            }
            this.zzaqr.onCreate(bundle);
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            try {
                return (View) zze.zzf(this.zzaqr.onCreateView(zze.zzn(layoutInflater), zze.zzn(viewGroup), bundle));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.zzaqr.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.zzaqr.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            try {
                this.zzaqr.onInflate(zze.zzn(activity), null, bundle2);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.zzaqr.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.zzaqr.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.zzaqr.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.zzaqr.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onStart() {
        }

        public void onStop() {
        }

        public IStreetViewPanoramaFragmentDelegate zzqv() {
            return this.zzaqr;
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Fragment zzXZ;
        protected zzf<zza> zzaqa;
        private final List<OnStreetViewPanoramaReadyCallback> zzaqu;
        private Activity zzoi;

        zzb(Fragment fragment) {
            this.zzaqu = new ArrayList();
            this.zzXZ = fragment;
        }

        private void setActivity(Activity activity) {
            this.zzoi = activity;
            zzqs();
        }

        public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            if (zzlg() != null) {
                ((zza) zzlg()).getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
            } else {
                this.zzaqu.add(onStreetViewPanoramaReadyCallback);
            }
        }

        protected void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaFragment_zza) {
            this.zzaqa = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaFragment_zza;
            zzqs();
        }

        public void zzqs() {
            if (this.zzoi != null && this.zzaqa != null && zzlg() == null) {
                try {
                    MapsInitializer.initialize(this.zzoi);
                    this.zzaqa.zza(new zza(this.zzXZ, com.google.android.gms.maps.internal.zzx.zzac(this.zzoi).zzk(zze.zzn(this.zzoi))));
                    for (OnStreetViewPanoramaReadyCallback streetViewPanoramaAsync : this.zzaqu) {
                        ((zza) zzlg()).getStreetViewPanoramaAsync(streetViewPanoramaAsync);
                    }
                    this.zzaqu.clear();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public StreetViewPanoramaFragment() {
        this.zzaqp = new zzb(this);
    }

    public static StreetViewPanoramaFragment newInstance() {
        return new StreetViewPanoramaFragment();
    }

    public static StreetViewPanoramaFragment newInstance(StreetViewPanoramaOptions streetViewPanoramaOptions) {
        StreetViewPanoramaFragment streetViewPanoramaFragment = new StreetViewPanoramaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("StreetViewPanoramaOptions", streetViewPanoramaOptions);
        streetViewPanoramaFragment.setArguments(bundle);
        return streetViewPanoramaFragment;
    }

    @Deprecated
    public final StreetViewPanorama getStreetViewPanorama() {
        IStreetViewPanoramaFragmentDelegate zzqv = zzqv();
        if (zzqv == null) {
            return null;
        }
        try {
            IStreetViewPanoramaDelegate streetViewPanorama = zzqv.getStreetViewPanorama();
            if (streetViewPanorama == null) {
                return null;
            }
            if (this.zzaqq == null || this.zzaqq.zzqu().asBinder() != streetViewPanorama.asBinder()) {
                this.zzaqq = new StreetViewPanorama(streetViewPanorama);
            }
            return this.zzaqq;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
        zzx.zzbd("getStreetViewPanoramaAsync() must be called on the main thread");
        this.zzaqp.getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
    }

    public void onActivityCreated(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onActivityCreated(bundle);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.zzaqp.setActivity(activity);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzaqp.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.zzaqp.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onDestroy() {
        this.zzaqp.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.zzaqp.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        this.zzaqp.setActivity(activity);
        this.zzaqp.onInflate(activity, new Bundle(), bundle);
    }

    public void onLowMemory() {
        this.zzaqp.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.zzaqp.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.zzaqp.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(StreetViewPanoramaFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(bundle);
        this.zzaqp.onSaveInstanceState(bundle);
    }

    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }

    protected IStreetViewPanoramaFragmentDelegate zzqv() {
        this.zzaqp.zzqs();
        return this.zzaqp.zzlg() == null ? null : ((zza) this.zzaqp.zzlg()).zzqv();
    }
}
