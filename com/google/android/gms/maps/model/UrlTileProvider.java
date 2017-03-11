package com.google.android.gms.maps.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public abstract class UrlTileProvider implements TileProvider {
    private final int zzli;
    private final int zzlj;

    public UrlTileProvider(int i, int i2) {
        this.zzli = i;
        this.zzlj = i2;
    }

    private static long zzb(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[PKIFailureInfo.certConfirmed];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    private static byte[] zze(InputStream inputStream) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zzb(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public final Tile getTile(int i, int i2, int i3) {
        URL tileUrl = getTileUrl(i, i2, i3);
        if (tileUrl == null) {
            return NO_TILE;
        }
        try {
            return new Tile(this.zzli, this.zzlj, zze(tileUrl.openStream()));
        } catch (IOException e) {
            return null;
        }
    }

    public abstract URL getTileUrl(int i, int i2, int i3);
}
