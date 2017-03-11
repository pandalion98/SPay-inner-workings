package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks;
import com.google.android.gms.common.internal.zzx;

public final class zzus implements Optional {
    public static final zzus zzawP;
    private final boolean zzawQ;
    private final boolean zzawR;
    private final String zzawS;
    private final ServerAuthCodeCallbacks zzawT;

    public static final class zza {
        private String zzauh;
        private boolean zzawU;
        private boolean zzawV;
        private ServerAuthCodeCallbacks zzawW;

        private String zzcH(String str) {
            zzx.zzl(str);
            boolean z = this.zzauh == null || this.zzauh.equals(str);
            zzx.zzb(z, (Object) "two different server client ids provided");
            return str;
        }

        public zza zza(String str, ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzawU = true;
            this.zzawV = true;
            this.zzauh = zzcH(str);
            this.zzawW = (ServerAuthCodeCallbacks) zzx.zzl(serverAuthCodeCallbacks);
            return this;
        }

        public zzus zzsy() {
            return new zzus(this.zzawV, this.zzauh, this.zzawW, null);
        }
    }

    static {
        zzawP = new zza().zzsy();
    }

    private zzus(boolean z, boolean z2, String str, ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
        this.zzawQ = z;
        this.zzawR = z2;
        this.zzawS = str;
        this.zzawT = serverAuthCodeCallbacks;
    }

    public String zzrN() {
        return this.zzawS;
    }

    public boolean zzsv() {
        return this.zzawQ;
    }

    public boolean zzsw() {
        return this.zzawR;
    }

    public ServerAuthCodeCallbacks zzsx() {
        return this.zzawT;
    }
}
