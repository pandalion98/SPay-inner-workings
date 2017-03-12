package com.google.android.gms.common.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class zza implements SafeParcelable {
    public static final Creator<zza> CREATOR;
    final int zzFG;
    final int zzJp;
    ParcelFileDescriptor zzNJ;
    private Bitmap zzNK;
    private boolean zzNL;
    private File zzNM;

    static {
        CREATOR = new zzb();
    }

    zza(int i, ParcelFileDescriptor parcelFileDescriptor, int i2) {
        this.zzFG = i;
        this.zzNJ = parcelFileDescriptor;
        this.zzJp = i2;
        this.zzNK = null;
        this.zzNL = false;
    }

    public zza(Bitmap bitmap) {
        this.zzFG = 1;
        this.zzNJ = null;
        this.zzJp = 0;
        this.zzNK = bitmap;
        this.zzNL = true;
    }

    private void zza(Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable e) {
            Log.w("BitmapTeleporter", "Could not close stream", e);
        }
    }

    private FileOutputStream zziw() {
        if (this.zzNM == null) {
            throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
        }
        try {
            File createTempFile = File.createTempFile("teleporter", ".tmp", this.zzNM);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
                this.zzNJ = ParcelFileDescriptor.open(createTempFile, 268435456);
                createTempFile.delete();
                return fileOutputStream;
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Temporary file is somehow already deleted");
            }
        } catch (Throwable e2) {
            throw new IllegalStateException("Could not create temporary file", e2);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void release() {
        if (!this.zzNL) {
            zza(this.zzNJ);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.zzNJ == null) {
            Bitmap bitmap = this.zzNK;
            Buffer allocate = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            Closeable dataOutputStream = new DataOutputStream(zziw());
            try {
                dataOutputStream.writeInt(array.length);
                dataOutputStream.writeInt(bitmap.getWidth());
                dataOutputStream.writeInt(bitmap.getHeight());
                dataOutputStream.writeUTF(bitmap.getConfig().toString());
                dataOutputStream.write(array);
                zza(dataOutputStream);
            } catch (Throwable e) {
                throw new IllegalStateException("Could not write into unlinked file", e);
            } catch (Throwable th) {
                zza(dataOutputStream);
            }
        }
        zzb.zza(this, parcel, i | 1);
        this.zzNJ = null;
    }

    public void zza(File file) {
        if (file == null) {
            throw new NullPointerException("Cannot set null temp directory");
        }
        this.zzNM = file;
    }

    public Bitmap zziv() {
        if (!this.zzNL) {
            Closeable dataInputStream = new DataInputStream(new AutoCloseInputStream(this.zzNJ));
            try {
                byte[] bArr = new byte[dataInputStream.readInt()];
                int readInt = dataInputStream.readInt();
                int readInt2 = dataInputStream.readInt();
                Config valueOf = Config.valueOf(dataInputStream.readUTF());
                dataInputStream.read(bArr);
                zza(dataInputStream);
                Buffer wrap = ByteBuffer.wrap(bArr);
                Bitmap createBitmap = Bitmap.createBitmap(readInt, readInt2, valueOf);
                createBitmap.copyPixelsFromBuffer(wrap);
                this.zzNK = createBitmap;
                this.zzNL = true;
            } catch (Throwable e) {
                throw new IllegalStateException("Could not read from parcel file descriptor", e);
            } catch (Throwable th) {
                zza(dataInputStream);
            }
        }
        return this.zzNK;
    }
}
