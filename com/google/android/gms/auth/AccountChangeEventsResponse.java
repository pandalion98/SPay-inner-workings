package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.List;

public class AccountChangeEventsResponse implements SafeParcelable {
    public static final AccountChangeEventsResponseCreator CREATOR;
    final int zzHe;
    final List<AccountChangeEvent> zzmv;

    static {
        CREATOR = new AccountChangeEventsResponseCreator();
    }

    AccountChangeEventsResponse(int i, List<AccountChangeEvent> list) {
        this.zzHe = i;
        this.zzmv = (List) zzx.zzl(list);
    }

    public AccountChangeEventsResponse(List<AccountChangeEvent> list) {
        this.zzHe = 1;
        this.zzmv = (List) zzx.zzl(list);
    }

    public int describeContents() {
        return 0;
    }

    public List<AccountChangeEvent> getEvents() {
        return this.zzmv;
    }

    public void writeToParcel(Parcel parcel, int i) {
        AccountChangeEventsResponseCreator.zza(this, parcel, i);
    }
}
