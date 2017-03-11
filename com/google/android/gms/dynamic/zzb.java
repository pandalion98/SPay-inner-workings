package com.google.android.gms.dynamic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.dynamic.zzc.zza;

public final class zzb extends zza {
    private Fragment zzXZ;

    private zzb(Fragment fragment) {
        this.zzXZ = fragment;
    }

    public static zzb zza(Fragment fragment) {
        return fragment != null ? new zzb(fragment) : null;
    }

    public Bundle getArguments() {
        return this.zzXZ.getArguments();
    }

    public int getId() {
        return this.zzXZ.getId();
    }

    public boolean getRetainInstance() {
        return this.zzXZ.getRetainInstance();
    }

    public String getTag() {
        return this.zzXZ.getTag();
    }

    public int getTargetRequestCode() {
        return this.zzXZ.getTargetRequestCode();
    }

    public boolean getUserVisibleHint() {
        return this.zzXZ.getUserVisibleHint();
    }

    public zzd getView() {
        return zze.zzn(this.zzXZ.getView());
    }

    public boolean isAdded() {
        return this.zzXZ.isAdded();
    }

    public boolean isDetached() {
        return this.zzXZ.isDetached();
    }

    public boolean isHidden() {
        return this.zzXZ.isHidden();
    }

    public boolean isInLayout() {
        return this.zzXZ.isInLayout();
    }

    public boolean isRemoving() {
        return this.zzXZ.isRemoving();
    }

    public boolean isResumed() {
        return this.zzXZ.isResumed();
    }

    public boolean isVisible() {
        return this.zzXZ.isVisible();
    }

    public void setHasOptionsMenu(boolean z) {
        this.zzXZ.setHasOptionsMenu(z);
    }

    public void setMenuVisibility(boolean z) {
        this.zzXZ.setMenuVisibility(z);
    }

    public void setRetainInstance(boolean z) {
        this.zzXZ.setRetainInstance(z);
    }

    public void setUserVisibleHint(boolean z) {
        this.zzXZ.setUserVisibleHint(z);
    }

    public void startActivity(Intent intent) {
        this.zzXZ.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int i) {
        this.zzXZ.startActivityForResult(intent, i);
    }

    public void zzd(zzd com_google_android_gms_dynamic_zzd) {
        this.zzXZ.registerForContextMenu((View) zze.zzf(com_google_android_gms_dynamic_zzd));
    }

    public void zze(zzd com_google_android_gms_dynamic_zzd) {
        this.zzXZ.unregisterForContextMenu((View) zze.zzf(com_google_android_gms_dynamic_zzd));
    }

    public zzd zzlh() {
        return zze.zzn(this.zzXZ.getActivity());
    }

    public zzc zzli() {
        return zza(this.zzXZ.getParentFragment());
    }

    public zzd zzlj() {
        return zze.zzn(this.zzXZ.getResources());
    }

    public zzc zzlk() {
        return zza(this.zzXZ.getTargetFragment());
    }
}
