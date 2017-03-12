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
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.List;

public class StreetViewPanoramaView extends FrameLayout {
    private final zzb zzaqC;
    private StreetViewPanorama zzaqq;

    static class zza implements StreetViewLifecycleDelegate {
        private final IStreetViewPanoramaViewDelegate zzaqD;
        private View zzaqE;
        private final ViewGroup zzaqd;

        /* renamed from: com.google.android.gms.maps.StreetViewPanoramaView.zza.1 */
        class C02601 extends com.google.android.gms.maps.internal.zzu.zza {
            final /* synthetic */ zza zzaqF;
            final /* synthetic */ OnStreetViewPanoramaReadyCallback zzaqs;

            C02601(zza com_google_android_gms_maps_StreetViewPanoramaView_zza, OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
                this.zzaqF = com_google_android_gms_maps_StreetViewPanoramaView_zza;
                this.zzaqs = onStreetViewPanoramaReadyCallback;
            }

            public void zza(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) {
                this.zzaqs.onStreetViewPanoramaReady(new StreetViewPanorama(iStreetViewPanoramaDelegate));
            }
        }

        public zza(ViewGroup viewGroup, IStreetViewPanoramaViewDelegate iStreetViewPanoramaViewDelegate) {
            this.zzaqD = (IStreetViewPanoramaViewDelegate) zzx.zzl(iStreetViewPanoramaViewDelegate);
            this.zzaqd = (ViewGroup) zzx.zzl(viewGroup);
        }

        public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            try {
                this.zzaqD.getStreetViewPanoramaAsync(new C02601(this, onStreetViewPanoramaReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onCreate(Bundle bundle) {
            try {
                this.zzaqD.onCreate(bundle);
                this.zzaqE = (View) zze.zzf(this.zzaqD.getView());
                this.zzaqd.removeAllViews();
                this.zzaqd.addView(this.zzaqE);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            throw new UnsupportedOperationException("onCreateView not allowed on StreetViewPanoramaViewDelegate");
        }

        public void onDestroy() {
            try {
                this.zzaqD.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on StreetViewPanoramaViewDelegate");
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            throw new UnsupportedOperationException("onInflate not allowed on StreetViewPanoramaViewDelegate");
        }

        public void onLowMemory() {
            try {
                this.zzaqD.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.zzaqD.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.zzaqD.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.zzaqD.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onStart() {
        }

        public void onStop() {
        }

        public IStreetViewPanoramaViewDelegate zzqz() {
            return this.zzaqD;
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Context mContext;
        private final StreetViewPanoramaOptions zzaqG;
        protected zzf<zza> zzaqa;
        private final ViewGroup zzaqh;
        private final List<OnStreetViewPanoramaReadyCallback> zzaqu;

        zzb(ViewGroup viewGroup, Context context, StreetViewPanoramaOptions streetViewPanoramaOptions) {
            this.zzaqu = new ArrayList();
            this.zzaqh = viewGroup;
            this.mContext = context;
            this.zzaqG = streetViewPanoramaOptions;
        }

        public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            if (zzlg() != null) {
                ((zza) zzlg()).getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
            } else {
                this.zzaqu.add(onStreetViewPanoramaReadyCallback);
            }
        }

        protected void zza(zzf<zza> com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaView_zza) {
            this.zzaqa = com_google_android_gms_dynamic_zzf_com_google_android_gms_maps_StreetViewPanoramaView_zza;
            zzqs();
        }

        public void zzqs() {
            if (this.zzaqa != null && zzlg() == null) {
                try {
                    this.zzaqa.zza(new zza(this.zzaqh, com.google.android.gms.maps.internal.zzx.zzac(this.mContext).zza(zze.zzn(this.mContext), this.zzaqG)));
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

    public StreetViewPanoramaView(Context context) {
        super(context);
        this.zzaqC = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzaqC = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzaqC = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, StreetViewPanoramaOptions streetViewPanoramaOptions) {
        super(context);
        this.zzaqC = new zzb(this, context, streetViewPanoramaOptions);
    }

    @Deprecated
    public final StreetViewPanorama getStreetViewPanorama() {
        if (this.zzaqq != null) {
            return this.zzaqq;
        }
        this.zzaqC.zzqs();
        if (this.zzaqC.zzlg() == null) {
            return null;
        }
        try {
            this.zzaqq = new StreetViewPanorama(((zza) this.zzaqC.zzlg()).zzqz().getStreetViewPanorama());
            return this.zzaqq;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
        zzx.zzbd("getStreetViewPanoramaAsync() must be called on the main thread");
        this.zzaqC.getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
    }

    public final void onCreate(Bundle bundle) {
        this.zzaqC.onCreate(bundle);
        if (this.zzaqC.zzlg() == null) {
            com.google.android.gms.dynamic.zza.zzb((FrameLayout) this);
        }
    }

    public final void onDestroy() {
        this.zzaqC.onDestroy();
    }

    public final void onLowMemory() {
        this.zzaqC.onLowMemory();
    }

    public final void onPause() {
        this.zzaqC.onPause();
    }

    public final void onResume() {
        this.zzaqC.onResume();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        this.zzaqC.onSaveInstanceState(bundle);
    }
}
