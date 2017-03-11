package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzi;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.zzj;
import com.google.android.gms.maps.model.internal.zzl;
import com.google.android.gms.maps.model.internal.zzn;

public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate zzapq;
    private UiSettings zzapr;

    /* renamed from: com.google.android.gms.maps.GoogleMap.10 */
    class AnonymousClass10 extends com.google.android.gms.maps.internal.zzn.zza {
        final /* synthetic */ OnMarkerClickListener zzapE;
        final /* synthetic */ GoogleMap zzapt;

        AnonymousClass10(GoogleMap googleMap, OnMarkerClickListener onMarkerClickListener) {
            this.zzapt = googleMap;
            this.zzapE = onMarkerClickListener;
        }

        public boolean zza(zzl com_google_android_gms_maps_model_internal_zzl) {
            return this.zzapE.onMarkerClick(new Marker(com_google_android_gms_maps_model_internal_zzl));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.11 */
    class AnonymousClass11 extends com.google.android.gms.maps.internal.zzo.zza {
        final /* synthetic */ OnMarkerDragListener zzapF;
        final /* synthetic */ GoogleMap zzapt;

        AnonymousClass11(GoogleMap googleMap, OnMarkerDragListener onMarkerDragListener) {
            this.zzapt = googleMap;
            this.zzapF = onMarkerDragListener;
        }

        public void zzb(zzl com_google_android_gms_maps_model_internal_zzl) {
            this.zzapF.onMarkerDragStart(new Marker(com_google_android_gms_maps_model_internal_zzl));
        }

        public void zzc(zzl com_google_android_gms_maps_model_internal_zzl) {
            this.zzapF.onMarkerDragEnd(new Marker(com_google_android_gms_maps_model_internal_zzl));
        }

        public void zzd(zzl com_google_android_gms_maps_model_internal_zzl) {
            this.zzapF.onMarkerDrag(new Marker(com_google_android_gms_maps_model_internal_zzl));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.12 */
    class AnonymousClass12 extends com.google.android.gms.maps.internal.zzh.zza {
        final /* synthetic */ OnInfoWindowClickListener zzapG;
        final /* synthetic */ GoogleMap zzapt;

        AnonymousClass12(GoogleMap googleMap, OnInfoWindowClickListener onInfoWindowClickListener) {
            this.zzapt = googleMap;
            this.zzapG = onInfoWindowClickListener;
        }

        public void zze(zzl com_google_android_gms_maps_model_internal_zzl) {
            this.zzapG.onInfoWindowClick(new Marker(com_google_android_gms_maps_model_internal_zzl));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.13 */
    class AnonymousClass13 extends com.google.android.gms.maps.internal.zzd.zza {
        final /* synthetic */ InfoWindowAdapter zzapH;
        final /* synthetic */ GoogleMap zzapt;

        AnonymousClass13(GoogleMap googleMap, InfoWindowAdapter infoWindowAdapter) {
            this.zzapt = googleMap;
            this.zzapH = infoWindowAdapter;
        }

        public zzd zzf(zzl com_google_android_gms_maps_model_internal_zzl) {
            return zze.zzn(this.zzapH.getInfoWindow(new Marker(com_google_android_gms_maps_model_internal_zzl)));
        }

        public zzd zzg(zzl com_google_android_gms_maps_model_internal_zzl) {
            return zze.zzn(this.zzapH.getInfoContents(new Marker(com_google_android_gms_maps_model_internal_zzl)));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.1 */
    class C02431 extends com.google.android.gms.maps.internal.zzg.zza {
        final /* synthetic */ OnIndoorStateChangeListener zzaps;
        final /* synthetic */ GoogleMap zzapt;

        C02431(GoogleMap googleMap, OnIndoorStateChangeListener onIndoorStateChangeListener) {
            this.zzapt = googleMap;
            this.zzaps = onIndoorStateChangeListener;
        }

        public void onIndoorBuildingFocused() {
            this.zzaps.onIndoorBuildingFocused();
        }

        public void zza(zzj com_google_android_gms_maps_model_internal_zzj) {
            this.zzaps.onIndoorLevelActivated(new IndoorBuilding(com_google_android_gms_maps_model_internal_zzj));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.2 */
    class C02442 extends com.google.android.gms.maps.internal.zzq.zza {
        final /* synthetic */ GoogleMap zzapt;
        final /* synthetic */ OnMyLocationChangeListener zzapu;

        C02442(GoogleMap googleMap, OnMyLocationChangeListener onMyLocationChangeListener) {
            this.zzapt = googleMap;
            this.zzapu = onMyLocationChangeListener;
        }

        public void zzc(Location location) {
            this.zzapu.onMyLocationChange(location);
        }

        public void zzg(zzd com_google_android_gms_dynamic_zzd) {
            this.zzapu.onMyLocationChange((Location) zze.zzf(com_google_android_gms_dynamic_zzd));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.3 */
    class C02453 extends com.google.android.gms.maps.internal.zzp.zza {
        final /* synthetic */ GoogleMap zzapt;
        final /* synthetic */ OnMyLocationButtonClickListener zzapv;

        C02453(GoogleMap googleMap, OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
            this.zzapt = googleMap;
            this.zzapv = onMyLocationButtonClickListener;
        }

        public boolean onMyLocationButtonClick() {
            return this.zzapv.onMyLocationButtonClick();
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.4 */
    class C02464 extends com.google.android.gms.maps.internal.zzk.zza {
        final /* synthetic */ GoogleMap zzapt;
        final /* synthetic */ OnMapLoadedCallback zzapw;

        C02464(GoogleMap googleMap, OnMapLoadedCallback onMapLoadedCallback) {
            this.zzapt = googleMap;
            this.zzapw = onMapLoadedCallback;
        }

        public void onMapLoaded() {
            this.zzapw.onMapLoaded();
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.5 */
    class C02475 extends com.google.android.gms.maps.internal.zzv.zza {
        final /* synthetic */ GoogleMap zzapt;
        final /* synthetic */ SnapshotReadyCallback zzapx;

        C02475(GoogleMap googleMap, SnapshotReadyCallback snapshotReadyCallback) {
            this.zzapt = googleMap;
            this.zzapx = snapshotReadyCallback;
        }

        public void onSnapshotReady(Bitmap bitmap) {
            this.zzapx.onSnapshotReady(bitmap);
        }

        public void zzh(zzd com_google_android_gms_dynamic_zzd) {
            this.zzapx.onSnapshotReady((Bitmap) zze.zzf(com_google_android_gms_dynamic_zzd));
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.6 */
    class C02496 extends com.google.android.gms.maps.internal.ILocationSourceDelegate.zza {
        final /* synthetic */ GoogleMap zzapt;
        final /* synthetic */ LocationSource zzapy;

        /* renamed from: com.google.android.gms.maps.GoogleMap.6.1 */
        class C02481 implements OnLocationChangedListener {
            final /* synthetic */ C02496 zzapA;
            final /* synthetic */ zzi zzapz;

            C02481(C02496 c02496, zzi com_google_android_gms_maps_internal_zzi) {
                this.zzapA = c02496;
                this.zzapz = com_google_android_gms_maps_internal_zzi;
            }

            public void onLocationChanged(Location location) {
                try {
                    this.zzapz.zzd(location);
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
        }

        C02496(GoogleMap googleMap, LocationSource locationSource) {
            this.zzapt = googleMap;
            this.zzapy = locationSource;
        }

        public void activate(zzi com_google_android_gms_maps_internal_zzi) {
            this.zzapy.activate(new C02481(this, com_google_android_gms_maps_internal_zzi));
        }

        public void deactivate() {
            this.zzapy.deactivate();
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.7 */
    class C02507 extends com.google.android.gms.maps.internal.zzf.zza {
        final /* synthetic */ OnCameraChangeListener zzapB;
        final /* synthetic */ GoogleMap zzapt;

        C02507(GoogleMap googleMap, OnCameraChangeListener onCameraChangeListener) {
            this.zzapt = googleMap;
            this.zzapB = onCameraChangeListener;
        }

        public void onCameraChange(CameraPosition cameraPosition) {
            this.zzapB.onCameraChange(cameraPosition);
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.8 */
    class C02518 extends com.google.android.gms.maps.internal.zzj.zza {
        final /* synthetic */ OnMapClickListener zzapC;
        final /* synthetic */ GoogleMap zzapt;

        C02518(GoogleMap googleMap, OnMapClickListener onMapClickListener) {
            this.zzapt = googleMap;
            this.zzapC = onMapClickListener;
        }

        public void onMapClick(LatLng latLng) {
            this.zzapC.onMapClick(latLng);
        }
    }

    /* renamed from: com.google.android.gms.maps.GoogleMap.9 */
    class C02529 extends com.google.android.gms.maps.internal.zzl.zza {
        final /* synthetic */ OnMapLongClickListener zzapD;
        final /* synthetic */ GoogleMap zzapt;

        C02529(GoogleMap googleMap, OnMapLongClickListener onMapLongClickListener) {
            this.zzapt = googleMap;
            this.zzapD = onMapLongClickListener;
        }

        public void onMapLongClick(LatLng latLng) {
            this.zzapD.onMapLongClick(latLng);
        }
    }

    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    public interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    public interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    public interface OnIndoorStateChangeListener {
        void onIndoorBuildingFocused();

        void onIndoorLevelActivated(IndoorBuilding indoorBuilding);
    }

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    public interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    private static final class zza extends com.google.android.gms.maps.internal.zzb.zza {
        private final CancelableCallback zzapI;

        zza(CancelableCallback cancelableCallback) {
            this.zzapI = cancelableCallback;
        }

        public void onCancel() {
            this.zzapI.onCancel();
        }

        public void onFinish() {
            this.zzapI.onFinish();
        }
    }

    protected GoogleMap(IGoogleMapDelegate iGoogleMapDelegate) {
        this.zzapq = (IGoogleMapDelegate) zzx.zzl(iGoogleMapDelegate);
    }

    public final Circle addCircle(CircleOptions circleOptions) {
        try {
            return new Circle(this.zzapq.addCircle(circleOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        try {
            com.google.android.gms.maps.model.internal.zzi addGroundOverlay = this.zzapq.addGroundOverlay(groundOverlayOptions);
            return addGroundOverlay != null ? new GroundOverlay(addGroundOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Marker addMarker(MarkerOptions markerOptions) {
        try {
            zzl addMarker = this.zzapq.addMarker(markerOptions);
            return addMarker != null ? new Marker(addMarker) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polygon addPolygon(PolygonOptions polygonOptions) {
        try {
            return new Polygon(this.zzapq.addPolygon(polygonOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions polylineOptions) {
        try {
            return new Polyline(this.zzapq.addPolyline(polylineOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        try {
            zzn addTileOverlay = this.zzapq.addTileOverlay(tileOverlayOptions);
            return addTileOverlay != null ? new TileOverlay(addTileOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzapq.animateCamera(cameraUpdate.zzqe());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, int i, CancelableCallback cancelableCallback) {
        try {
            this.zzapq.animateCameraWithDurationAndCallback(cameraUpdate.zzqe(), i, cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback) {
        try {
            this.zzapq.animateCameraWithCallback(cameraUpdate.zzqe(), cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.zzapq.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.zzapq.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public IndoorBuilding getFocusedBuilding() {
        try {
            zzj focusedBuilding = this.zzapq.getFocusedBuilding();
            return focusedBuilding != null ? new IndoorBuilding(focusedBuilding) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getMapType() {
        try {
            return this.zzapq.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.zzapq.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.zzapq.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.zzapq.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.zzapq.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.zzapr == null) {
                this.zzapr = new UiSettings(this.zzapq.getUiSettings());
            }
            return this.zzapr;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isBuildingsEnabled() {
        try {
            return this.zzapq.isBuildingsEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.zzapq.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.zzapq.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.zzapq.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzapq.moveCamera(cameraUpdate.zzqe());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        try {
            this.zzapq.setBuildingsEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setContentDescription(String str) {
        try {
            this.zzapq.setContentDescription(str);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean z) {
        try {
            return this.zzapq.setIndoorEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter) {
        if (infoWindowAdapter == null) {
            try {
                this.zzapq.setInfoWindowAdapter(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setInfoWindowAdapter(new AnonymousClass13(this, infoWindowAdapter));
    }

    public final void setLocationSource(LocationSource locationSource) {
        if (locationSource == null) {
            try {
                this.zzapq.setLocationSource(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setLocationSource(new C02496(this, locationSource));
    }

    public final void setMapType(int i) {
        try {
            this.zzapq.setMapType(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMyLocationEnabled(boolean z) {
        try {
            this.zzapq.setMyLocationEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
        if (onCameraChangeListener == null) {
            try {
                this.zzapq.setOnCameraChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnCameraChangeListener(new C02507(this, onCameraChangeListener));
    }

    public final void setOnIndoorStateChangeListener(OnIndoorStateChangeListener onIndoorStateChangeListener) {
        if (onIndoorStateChangeListener == null) {
            try {
                this.zzapq.setOnIndoorStateChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnIndoorStateChangeListener(new C02431(this, onIndoorStateChangeListener));
    }

    public final void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
        if (onInfoWindowClickListener == null) {
            try {
                this.zzapq.setOnInfoWindowClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnInfoWindowClickListener(new AnonymousClass12(this, onInfoWindowClickListener));
    }

    public final void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        if (onMapClickListener == null) {
            try {
                this.zzapq.setOnMapClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMapClickListener(new C02518(this, onMapClickListener));
    }

    public void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        if (onMapLoadedCallback == null) {
            try {
                this.zzapq.setOnMapLoadedCallback(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMapLoadedCallback(new C02464(this, onMapLoadedCallback));
    }

    public final void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        if (onMapLongClickListener == null) {
            try {
                this.zzapq.setOnMapLongClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMapLongClickListener(new C02529(this, onMapLongClickListener));
    }

    public final void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener == null) {
            try {
                this.zzapq.setOnMarkerClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMarkerClickListener(new AnonymousClass10(this, onMarkerClickListener));
    }

    public final void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        if (onMarkerDragListener == null) {
            try {
                this.zzapq.setOnMarkerDragListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMarkerDragListener(new AnonymousClass11(this, onMarkerDragListener));
    }

    public final void setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
        if (onMyLocationButtonClickListener == null) {
            try {
                this.zzapq.setOnMyLocationButtonClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMyLocationButtonClickListener(new C02453(this, onMyLocationButtonClickListener));
    }

    @Deprecated
    public final void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener) {
        if (onMyLocationChangeListener == null) {
            try {
                this.zzapq.setOnMyLocationChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.zzapq.setOnMyLocationChangeListener(new C02442(this, onMyLocationChangeListener));
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
        try {
            this.zzapq.setPadding(i, i2, i3, i4);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean z) {
        try {
            this.zzapq.setTrafficEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        snapshot(snapshotReadyCallback, null);
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback, Bitmap bitmap) {
        try {
            this.zzapq.snapshot(new C02475(this, snapshotReadyCallback), (zze) (bitmap != null ? zze.zzn(bitmap) : null));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void stopAnimation() {
        try {
            this.zzapq.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    IGoogleMapDelegate zzqg() {
        return this.zzapq;
    }
}
