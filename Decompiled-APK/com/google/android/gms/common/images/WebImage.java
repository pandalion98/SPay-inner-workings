package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.samsung.android.spayfw.appinterface.PushMessage;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebImage implements SafeParcelable {
    public static final Creator<WebImage> CREATOR;
    private final int zzFG;
    private final Uri zzOL;
    private final int zzli;
    private final int zzlj;

    static {
        CREATOR = new zzb();
    }

    WebImage(int i, Uri uri, int i2, int i3) {
        this.zzFG = i;
        this.zzOL = uri;
        this.zzli = i2;
        this.zzlj = i3;
    }

    public WebImage(Uri uri) {
        this(uri, 0, 0);
    }

    public WebImage(Uri uri, int i, int i2) {
        this(1, uri, i, i2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public WebImage(JSONObject jSONObject) {
        this(zze(jSONObject), jSONObject.optInt("width", 0), jSONObject.optInt("height", 0));
    }

    private static Uri zze(JSONObject jSONObject) {
        Uri uri = null;
        if (jSONObject.has(PushMessage.JSON_KEY_URL)) {
            try {
                uri = Uri.parse(jSONObject.getString(PushMessage.JSON_KEY_URL));
            } catch (JSONException e) {
            }
        }
        return uri;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) obj;
        return zzw.equal(this.zzOL, webImage.zzOL) && this.zzli == webImage.zzli && this.zzlj == webImage.zzlj;
    }

    public int getHeight() {
        return this.zzlj;
    }

    public Uri getUrl() {
        return this.zzOL;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int getWidth() {
        return this.zzli;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzOL, Integer.valueOf(this.zzli), Integer.valueOf(this.zzlj));
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(PushMessage.JSON_KEY_URL, this.zzOL.toString());
            jSONObject.put("width", this.zzli);
            jSONObject.put("height", this.zzlj);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        return String.format("Image %dx%d %s", new Object[]{Integer.valueOf(this.zzli), Integer.valueOf(this.zzlj), this.zzOL.toString()});
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }
}
