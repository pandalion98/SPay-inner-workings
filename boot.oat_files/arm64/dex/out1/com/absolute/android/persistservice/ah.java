package com.absolute.android.persistservice;

import com.absolute.android.persistence.IABTPersistedFile.Stub;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ah extends Stub {
    protected String a;
    protected File b;
    private v c;
    private boolean d = false;
    private FileInputStream e = null;
    private FileOutputStream f = null;

    ah(String str, boolean z, v vVar) {
        this.c = vVar;
        this.a = str;
        this.d = z;
        this.b = new File(this.a);
        File parentFile = this.b.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
    }

    public boolean exists() {
        return this.b.exists();
    }

    public int write(byte[] bArr) {
        try {
            if (this.f == null) {
                this.f = new FileOutputStream(this.b, this.d);
            }
            this.f.write(bArr);
            return bArr.length;
        } catch (Throwable e) {
            this.c.a("FileNotFoundException for write() to Persisted File: " + this.a, e);
            return -1;
        } catch (Throwable e2) {
            this.c.a("IOException for write() to Persisted File: " + this.a, e2);
            return -1;
        }
    }

    public int writeWithCount(byte[] bArr, int i, int i2) {
        try {
            if (this.f == null) {
                this.f = new FileOutputStream(this.b, this.d);
            }
            this.f.write(bArr, i, i2);
            return i2;
        } catch (Throwable e) {
            this.c.a("FileNotFoundException for write() to Persisted File: " + this.a, e);
            return -1;
        } catch (Throwable e2) {
            this.c.a("IOException for write() to Persisted File: " + this.a, e2);
            return -1;
        }
    }

    public int read(byte[] bArr) {
        int i = -1;
        try {
            if (this.e == null) {
                this.e = new FileInputStream(this.b);
            }
            i = this.e.read(bArr);
        } catch (FileNotFoundException e) {
        } catch (Throwable e2) {
            this.c.a("IOException for read() from Persisted File: " + this.a, e2);
        }
        return i;
    }

    public long skip(long j) {
        long j2 = -1;
        try {
            if (this.e == null) {
                this.e = new FileInputStream(this.b);
            }
            j2 = this.e.skip(j);
        } catch (Throwable e) {
            this.c.a("FileNotFoundException for skip() on Persisted File: " + this.a, e);
        } catch (Throwable e2) {
            this.c.a("IOException for skip() on Persisted File: " + this.a, e2);
        }
        return j2;
    }

    public void close() {
        try {
            if (this.e != null) {
                this.e.close();
                this.e = null;
            }
            if (this.f != null) {
                this.f.close();
                this.f = null;
            }
        } catch (Throwable e) {
            this.c.a("IOException in close() for Persisted File: " + this.a, e);
        }
    }

    public boolean delete() {
        return this.b.delete();
    }

    protected void finalize() {
        close();
        super.finalize();
    }
}
