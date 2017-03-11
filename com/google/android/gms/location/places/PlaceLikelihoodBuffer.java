package com.google.android.gms.location.places;

import android.content.Context;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzqc;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.crypto.tls.ExtensionType;

public class PlaceLikelihoodBuffer extends AbstractDataBuffer<PlaceLikelihood> implements Result {
    private final Context mContext;
    private final Status zzHb;
    private final String zzanK;
    private final int zzanL;

    public static class zza {
        static int zzfD(int i) {
            switch (i) {
                case EncryptionAlgorithm.ESTREAM_SALSA20 /*100*/:
                case ExtensionType.negotiated_ff_dhe_groups /*101*/:
                case EncryptionAlgorithm.AEAD_CHACHA20_POLY1305 /*102*/:
                case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
                case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
                case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
                    return i;
                default:
                    throw new IllegalArgumentException("invalid source: " + i);
            }
        }
    }

    public PlaceLikelihoodBuffer(DataHolder dataHolder, int i, Context context) {
        super(dataHolder);
        this.mContext = context;
        this.zzHb = new Status(dataHolder.getStatusCode());
        this.zzanL = zza.zzfD(i);
        if (dataHolder == null || dataHolder.zziu() == null) {
            this.zzanK = null;
        } else {
            this.zzanK = dataHolder.zziu().getString("com.google.android.gms.location.places.PlaceLikelihoodBuffer.ATTRIBUTIONS_EXTRA_KEY");
        }
    }

    public PlaceLikelihood get(int i) {
        return new zzqc(this.zzMd, i, this.mContext);
    }

    public CharSequence getAttributions() {
        return this.zzanK;
    }

    public Status getStatus() {
        return this.zzHb;
    }

    public String toString() {
        return zzw.zzk(this).zza(CardMaster.COL_STATUS, getStatus()).zza("attributions", this.zzanK).toString();
    }
}
