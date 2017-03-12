package com.google.android.gms.maps;

import android.graphics.Point;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;
import com.google.android.gms.maps.internal.zzr;
import com.google.android.gms.maps.internal.zzs.zza;
import com.google.android.gms.maps.internal.zzt;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

public class StreetViewPanorama {
    private final IStreetViewPanoramaDelegate zzaqk;

    /* renamed from: com.google.android.gms.maps.StreetViewPanorama.1 */
    class C02561 extends zza {
        final /* synthetic */ OnStreetViewPanoramaChangeListener zzaql;
        final /* synthetic */ StreetViewPanorama zzaqm;

        C02561(StreetViewPanorama streetViewPanorama, OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener) {
            this.zzaqm = streetViewPanorama;
            this.zzaql = onStreetViewPanoramaChangeListener;
        }

        public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
            this.zzaql.onStreetViewPanoramaChange(streetViewPanoramaLocation);
        }
    }

    /* renamed from: com.google.android.gms.maps.StreetViewPanorama.2 */
    class C02572 extends zzr.zza {
        final /* synthetic */ StreetViewPanorama zzaqm;
        final /* synthetic */ OnStreetViewPanoramaCameraChangeListener zzaqn;

        C02572(StreetViewPanorama streetViewPanorama, OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener) {
            this.zzaqm = streetViewPanorama;
            this.zzaqn = onStreetViewPanoramaCameraChangeListener;
        }

        public void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera) {
            this.zzaqn.onStreetViewPanoramaCameraChange(streetViewPanoramaCamera);
        }
    }

    /* renamed from: com.google.android.gms.maps.StreetViewPanorama.3 */
    class C02583 extends zzt.zza {
        final /* synthetic */ StreetViewPanorama zzaqm;
        final /* synthetic */ OnStreetViewPanoramaClickListener zzaqo;

        C02583(StreetViewPanorama streetViewPanorama, OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener) {
            this.zzaqm = streetViewPanorama;
            this.zzaqo = onStreetViewPanoramaClickListener;
        }

        public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
            this.zzaqo.onStreetViewPanoramaClick(streetViewPanoramaOrientation);
        }
    }

    public interface OnStreetViewPanoramaCameraChangeListener {
        void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera);
    }

    public interface OnStreetViewPanoramaChangeListener {
        void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation);
    }

    public interface OnStreetViewPanoramaClickListener {
        void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation);
    }

    protected StreetViewPanorama(IStreetViewPanoramaDelegate iStreetViewPanoramaDelegate) {
        this.zzaqk = (IStreetViewPanoramaDelegate) zzx.zzl(iStreetViewPanoramaDelegate);
    }

    public void animateTo(StreetViewPanoramaCamera streetViewPanoramaCamera, long j) {
        try {
            this.zzaqk.animateTo(streetViewPanoramaCamera, j);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaLocation getLocation() {
        try {
            return this.zzaqk.getStreetViewPanoramaLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaCamera getPanoramaCamera() {
        try {
            return this.zzaqk.getPanoramaCamera();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isPanningGesturesEnabled() {
        try {
            return this.zzaqk.isPanningGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isStreetNamesEnabled() {
        try {
            return this.zzaqk.isStreetNamesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isUserNavigationEnabled() {
        try {
            return this.zzaqk.isUserNavigationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public boolean isZoomGesturesEnabled() {
        try {
            return this.zzaqk.isZoomGesturesEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public Point orientationToPoint(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
        try {
            zzd orientationToPoint = this.zzaqk.orientationToPoint(streetViewPanoramaOrientation);
            return orientationToPoint == null ? null : (Point) zze.zzf(orientationToPoint);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public StreetViewPanoramaOrientation pointToOrientation(Point point) {
        try {
            return this.zzaqk.pointToOrientation(zze.zzn(point));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnStreetViewPanoramaCameraChangeListener(OnStreetViewPanoramaCameraChangeListener onStreetViewPanoramaCameraChangeListener) {
        if (onStreetViewPanoramaCameraChangeListener == null) {
            try {
                this.zzaqk.setOnStreetViewPanoramaCameraChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzaqk.setOnStreetViewPanoramaCameraChangeListener(new C02572(this, onStreetViewPanoramaCameraChangeListener));
    }

    public final void setOnStreetViewPanoramaChangeListener(OnStreetViewPanoramaChangeListener onStreetViewPanoramaChangeListener) {
        if (onStreetViewPanoramaChangeListener == null) {
            try {
                this.zzaqk.setOnStreetViewPanoramaChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzaqk.setOnStreetViewPanoramaChangeListener(new C02561(this, onStreetViewPanoramaChangeListener));
    }

    public final void setOnStreetViewPanoramaClickListener(OnStreetViewPanoramaClickListener onStreetViewPanoramaClickListener) {
        if (onStreetViewPanoramaClickListener == null) {
            try {
                this.zzaqk.setOnStreetViewPanoramaClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzaqk.setOnStreetViewPanoramaClickListener(new C02583(this, onStreetViewPanoramaClickListener));
    }

    public void setPanningGesturesEnabled(boolean z) {
        try {
            this.zzaqk.enablePanning(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(LatLng latLng) {
        try {
            this.zzaqk.setPosition(latLng);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(LatLng latLng, int i) {
        try {
            this.zzaqk.setPositionWithRadius(latLng, i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setPosition(String str) {
        try {
            this.zzaqk.setPositionWithID(str);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setStreetNamesEnabled(boolean z) {
        try {
            this.zzaqk.enableStreetNames(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setUserNavigationEnabled(boolean z) {
        try {
            this.zzaqk.enableUserNavigation(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setZoomGesturesEnabled(boolean z) {
        try {
            this.zzaqk.enableZoom(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    IStreetViewPanoramaDelegate zzqu() {
        return this.zzaqk;
    }
}
