package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.widget.ImageView;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public final class zzkl extends ImageView {
    private Uri zzPe;
    private int zzPf;
    private int zzPg;
    private zza zzPh;
    private int zzPi;
    private float zzPj;

    public interface zza {
        Path zzk(int i, int i2);
    }

    protected void onDraw(Canvas canvas) {
        if (this.zzPh != null) {
            canvas.clipPath(this.zzPh.zzk(getWidth(), getHeight()));
        }
        super.onDraw(canvas);
        if (this.zzPg != 0) {
            canvas.drawColor(this.zzPg);
        }
    }

    protected void onMeasure(int i, int i2) {
        int measuredHeight;
        int i3;
        super.onMeasure(i, i2);
        switch (this.zzPi) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                measuredHeight = getMeasuredHeight();
                i3 = (int) (((float) measuredHeight) * this.zzPj);
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                i3 = getMeasuredWidth();
                measuredHeight = (int) (((float) i3) / this.zzPj);
                break;
            default:
                return;
        }
        setMeasuredDimension(i3, measuredHeight);
    }

    public void zzaE(int i) {
        this.zzPf = i;
    }

    public void zzi(Uri uri) {
        this.zzPe = uri;
    }

    public int zziK() {
        return this.zzPf;
    }
}
