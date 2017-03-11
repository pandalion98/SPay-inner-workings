package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;
import com.google.android.gms.maps.model.internal.zzo;
import com.google.android.gms.maps.model.internal.zzo.zza;

public final class TileOverlayOptions implements SafeParcelable {
    public static final zzw CREATOR;
    private final int zzFG;
    private zzo zzarH;
    private TileProvider zzarI;
    private boolean zzarJ;
    private float zzarb;
    private boolean zzarc;

    /* renamed from: com.google.android.gms.maps.model.TileOverlayOptions.1 */
    class C02631 implements TileProvider {
        private final zzo zzarK;
        final /* synthetic */ TileOverlayOptions zzarL;

        C02631(TileOverlayOptions tileOverlayOptions) {
            this.zzarL = tileOverlayOptions;
            this.zzarK = this.zzarL.zzarH;
        }

        public Tile getTile(int i, int i2, int i3) {
            try {
                return this.zzarK.getTile(i, i2, i3);
            } catch (RemoteException e) {
                return null;
            }
        }
    }

    /* renamed from: com.google.android.gms.maps.model.TileOverlayOptions.2 */
    class C02642 extends zza {
        final /* synthetic */ TileOverlayOptions zzarL;
        final /* synthetic */ TileProvider zzarM;

        C02642(TileOverlayOptions tileOverlayOptions, TileProvider tileProvider) {
            this.zzarL = tileOverlayOptions;
            this.zzarM = tileProvider;
        }

        public Tile getTile(int i, int i2, int i3) {
            return this.zzarM.getTile(i, i2, i3);
        }
    }

    static {
        CREATOR = new zzw();
    }

    public TileOverlayOptions() {
        this.zzarc = true;
        this.zzarJ = true;
        this.zzFG = 1;
    }

    TileOverlayOptions(int i, IBinder iBinder, boolean z, float f, boolean z2) {
        this.zzarc = true;
        this.zzarJ = true;
        this.zzFG = i;
        this.zzarH = zza.zzcf(iBinder);
        this.zzarI = this.zzarH == null ? null : new C02631(this);
        this.zzarc = z;
        this.zzarb = f;
        this.zzarJ = z2;
    }

    public int describeContents() {
        return 0;
    }

    public TileOverlayOptions fadeIn(boolean z) {
        this.zzarJ = z;
        return this;
    }

    public boolean getFadeIn() {
        return this.zzarJ;
    }

    public TileProvider getTileProvider() {
        return this.zzarI;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public float getZIndex() {
        return this.zzarb;
    }

    public boolean isVisible() {
        return this.zzarc;
    }

    public TileOverlayOptions tileProvider(TileProvider tileProvider) {
        this.zzarI = tileProvider;
        this.zzarH = this.zzarI == null ? null : new C02642(this, tileProvider);
        return this;
    }

    public TileOverlayOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzx.zza(this, parcel, i);
        } else {
            zzw.zza(this, parcel, i);
        }
    }

    public TileOverlayOptions zIndex(float f) {
        this.zzarb = f;
        return this;
    }

    IBinder zzqK() {
        return this.zzarH.asBinder();
    }
}
