package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class MapView extends FrameLayout {
    private GoogleMap zzapW;
    private final zzb zzaqc;

    static class zza implements MapLifecycleDelegate {
        private final ViewGroup zzaqd;
        private final IMapViewDelegate zzaqe;
        private View zzaqf;

        /* renamed from: com.google.android.gms.maps.MapView.zza.1 */
        class C02541 extends com.google.android.gms.maps.internal.zzm.zza {
            final /* synthetic */ OnMapReadyCallback zzapY;
            final /* synthetic */ zza zzaqg;

            C02541(zza com_google_android_gms_maps_MapView_zza, OnMapReadyCallback onMapReadyCallback) {
                this.zzaqg = com_google_android_gms_maps_MapView_zza;
                this.zzapY = onMapReadyCallback;
            }

            public void zza(IGoogleMapDelegate iGoogleMapDelegate) {
                this.zzapY.onMapReady(new GoogleMap(iGoogleMapDelegate));
            }
        }

        public zza(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.zzaqe = (IMapViewDelegate) zzx.zzl(iMapViewDelegate);
            this.zzaqd = (ViewGroup) zzx.zzl(viewGroup);
        }

        public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            try {
                this.zzaqe.getMapAsync(new C02541(this, onMapReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onCreate(Bundle bundle) {
            try {
                this.zzaqe.onCreate(bundle);
                this.zzaqf = (View) zze.zzf(this.zzaqe.getView());
                this.zzaqd.removeAllViews();
                this.zzaqd.addView(this.zzaqf);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        public void onDestroy() {
            try {
                this.zzaqe.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        public void onLowMemory() {
            try {
                this.zzaqe.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.zzaqe.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.zzaqe.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.zzaqe.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onStart() {
        }

        public void onStop() {
        }

        public IMapViewDelegate zzqt() {
            return this.zzaqe;
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Context mContext;
        protected zzf<zza> zzaqa;
        private final List<OnMapReadyCallback> zzaqb;
        private final ViewGroup zzaqh;
        private final GoogleMapOptions zzaqi;

        zzb(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.zzaqb = new ArrayList();
            this.zzaqh = viewGroup;
            this.mContext = context;
            this.zzaqi = googleMapOptions;
        }

        public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            if (zzlg() != null) {
                ((zza) zzlg()).getMapAsync(onMapReadyCallback);
            } else {
                this.zzaqb.add(onMapReadyCallback);
            }
        }

        protected void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapView_zza) {
            this.zzaqa = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_MapView_zza;
            zzqs();
        }

        public void zzqs() {
            if (this.zzaqa != null && zzlg() == null) {
                try {
                    this.zzaqa.zza(new zza(this.zzaqh, com.google.android.gms.maps.internal.zzx.zzac(this.mContext).zza(zze.zzn(this.mContext), this.zzaqi)));
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

    public MapView(Context context) {
        super(context);
        this.zzaqc = new zzb(this, context, null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzaqc = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzaqc = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
    }

    public MapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context);
        this.zzaqc = new zzb(this, context, googleMapOptions);
    }

    @Deprecated
    public final GoogleMap getMap() {
        if (this.zzapW != null) {
            return this.zzapW;
        }
        this.zzaqc.zzqs();
        if (this.zzaqc.zzlg() == null) {
            return null;
        }
        try {
            this.zzapW = new GoogleMap(((zza) this.zzaqc.zzlg()).zzqt().getMap());
            return this.zzapW;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        zzx.zzbd("getMapAsync() must be called on the main thread");
        this.zzaqc.getMapAsync(onMapReadyCallback);
    }

    public final void onCreate(Bundle bundle) {
        this.zzaqc.onCreate(bundle);
        if (this.zzaqc.zzlg() == null) {
            com.google.android.gms.dynamic.zza.zzb((FrameLayout) this);
        }
    }

    public final void onDestroy() {
        this.zzaqc.onDestroy();
    }

    public final void onLowMemory() {
        this.zzaqc.onLowMemory();
    }

    public final void onPause() {
        this.zzaqc.onPause();
    }

    public final void onResume() {
        this.zzaqc.onResume();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        this.zzaqc.onSaveInstanceState(bundle);
    }
}
