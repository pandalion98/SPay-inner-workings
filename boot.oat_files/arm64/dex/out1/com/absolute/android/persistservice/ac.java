package com.absolute.android.persistservice;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.absolute.android.crypt.Crypt;
import com.absolute.android.utils.DeviceUtil;
import com.android.internal.os.SMProviderContract;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.UUID;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public abstract class ac {
    protected Hashtable c = new Hashtable();
    protected Context d;
    protected v e;
    protected String f;
    protected y g;

    protected abstract void c();

    public abstract boolean i(String str);

    ac(Context context, v vVar, String str) {
        this.d = context;
        this.e = vVar;
        this.f = ABTPersistenceService.a() + str;
    }

    protected void a(y yVar) {
        this.g = yVar;
    }

    protected boolean e() {
        return j(this.f);
    }

    protected synchronized boolean j(String str) {
        WakeLock newWakeLock;
        FileOutputStream fileOutputStream;
        CipherOutputStream cipherOutputStream;
        Throwable e;
        boolean z;
        FileOutputStream fileOutputStream2;
        WakeLock wakeLock;
        ObjectOutputStream objectOutputStream;
        CipherOutputStream cipherOutputStream2;
        boolean z2;
        ObjectOutputStream objectOutputStream2 = null;
        boolean z3 = false;
        synchronized (this) {
            try {
                if (this.g != null) {
                    this.g.a(str);
                }
                String b = b();
                byte[] a = a();
                newWakeLock = ((PowerManager) this.d.getSystemService(SMProviderContract.KEY_POWER)).newWakeLock(1, "Absolute_PersistedStore");
                try {
                    newWakeLock.acquire();
                    boolean z4 = !new File(str).exists();
                    try {
                        fileOutputStream = new FileOutputStream(str);
                        try {
                            fileOutputStream.write(b.getBytes(), 0, 36);
                            fileOutputStream.write(a, 0, 24);
                            cipherOutputStream = new CipherOutputStream(fileOutputStream, Crypt.getCipher(a, 1, Crypt.CIPHER_TRANSFORM, b));
                        } catch (Exception e2) {
                            e = e2;
                            z = z4;
                            fileOutputStream2 = fileOutputStream;
                            wakeLock = newWakeLock;
                            objectOutputStream = null;
                            try {
                                this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                                if (objectOutputStream != null) {
                                    try {
                                        objectOutputStream.close();
                                    } catch (Throwable th) {
                                        this.e.a("Exception closing persisted file: " + str + " in savetoPath(). Exception:", th);
                                    }
                                }
                                if (cipherOutputStream2 != null) {
                                    try {
                                        cipherOutputStream2.close();
                                    } catch (Throwable th2) {
                                    }
                                }
                                if (fileOutputStream2 != null) {
                                    try {
                                        fileOutputStream2.close();
                                    } catch (Throwable th3) {
                                    }
                                }
                                if (wakeLock != null) {
                                    if (wakeLock.isHeld()) {
                                        wakeLock.release();
                                    }
                                }
                                if (this.g != null) {
                                    this.g.a(str, false, z);
                                }
                                z2 = false;
                                return z2;
                            } catch (Throwable th4) {
                                e = th4;
                                z3 = z;
                                fileOutputStream = fileOutputStream2;
                                newWakeLock = wakeLock;
                                cipherOutputStream = cipherOutputStream2;
                                objectOutputStream2 = objectOutputStream;
                                if (objectOutputStream2 != null) {
                                    try {
                                        objectOutputStream2.close();
                                    } catch (Throwable th5) {
                                        this.e.a("Exception closing persisted file: " + str + " in savetoPath(). Exception:", th5);
                                    }
                                }
                                if (cipherOutputStream != null) {
                                    try {
                                        cipherOutputStream.close();
                                    } catch (Throwable th6) {
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th7) {
                                    }
                                }
                                if (newWakeLock != null) {
                                    if (newWakeLock.isHeld()) {
                                        newWakeLock.release();
                                    }
                                }
                                if (this.g != null) {
                                    this.g.a(str, false, z3);
                                }
                                throw e;
                            }
                        } catch (Throwable th8) {
                            e = th8;
                            z3 = z4;
                            cipherOutputStream = null;
                            if (objectOutputStream2 != null) {
                                objectOutputStream2.close();
                            }
                            if (cipherOutputStream != null) {
                                cipherOutputStream.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (newWakeLock != null) {
                                if (newWakeLock.isHeld()) {
                                    newWakeLock.release();
                                }
                            }
                            if (this.g != null) {
                                this.g.a(str, false, z3);
                            }
                            throw e;
                        }
                        try {
                            ObjectOutputStream objectOutputStream3 = new ObjectOutputStream(cipherOutputStream);
                            try {
                                objectOutputStream3.writeObject(this.c);
                                if (objectOutputStream3 != null) {
                                    try {
                                        objectOutputStream3.close();
                                    } catch (Throwable th9) {
                                        this.e.a("Exception closing persisted file: " + str + " in savetoPath(). Exception:", th9);
                                    }
                                }
                                if (cipherOutputStream != null) {
                                    try {
                                        cipherOutputStream.close();
                                    } catch (Throwable th10) {
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th11) {
                                    }
                                }
                                if (newWakeLock != null) {
                                    if (newWakeLock.isHeld()) {
                                        newWakeLock.release();
                                    }
                                }
                                if (this.g != null) {
                                    this.g.a(str, true, z4);
                                }
                                z2 = true;
                            } catch (Exception e3) {
                                e = e3;
                                z = z4;
                                cipherOutputStream2 = cipherOutputStream;
                                objectOutputStream = objectOutputStream3;
                                wakeLock = newWakeLock;
                                fileOutputStream2 = fileOutputStream;
                                this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                                if (objectOutputStream != null) {
                                    objectOutputStream.close();
                                }
                                if (cipherOutputStream2 != null) {
                                    cipherOutputStream2.close();
                                }
                                if (fileOutputStream2 != null) {
                                    fileOutputStream2.close();
                                }
                                if (wakeLock != null) {
                                    if (wakeLock.isHeld()) {
                                        wakeLock.release();
                                    }
                                }
                                if (this.g != null) {
                                    this.g.a(str, false, z);
                                }
                                z2 = false;
                                return z2;
                            } catch (Throwable th12) {
                                e = th12;
                                z3 = z4;
                                objectOutputStream2 = objectOutputStream3;
                                if (objectOutputStream2 != null) {
                                    objectOutputStream2.close();
                                }
                                if (cipherOutputStream != null) {
                                    cipherOutputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                if (newWakeLock != null) {
                                    if (newWakeLock.isHeld()) {
                                        newWakeLock.release();
                                    }
                                }
                                if (this.g != null) {
                                    this.g.a(str, false, z3);
                                }
                                throw e;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            z = z4;
                            fileOutputStream2 = fileOutputStream;
                            objectOutputStream = null;
                            cipherOutputStream2 = cipherOutputStream;
                            wakeLock = newWakeLock;
                            this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                            if (objectOutputStream != null) {
                                objectOutputStream.close();
                            }
                            if (cipherOutputStream2 != null) {
                                cipherOutputStream2.close();
                            }
                            if (fileOutputStream2 != null) {
                                fileOutputStream2.close();
                            }
                            if (wakeLock != null) {
                                if (wakeLock.isHeld()) {
                                    wakeLock.release();
                                }
                            }
                            if (this.g != null) {
                                this.g.a(str, false, z);
                            }
                            z2 = false;
                            return z2;
                        } catch (Throwable th13) {
                            e = th13;
                            z3 = z4;
                            if (objectOutputStream2 != null) {
                                objectOutputStream2.close();
                            }
                            if (cipherOutputStream != null) {
                                cipherOutputStream.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (newWakeLock != null) {
                                if (newWakeLock.isHeld()) {
                                    newWakeLock.release();
                                }
                            }
                            if (this.g != null) {
                                this.g.a(str, false, z3);
                            }
                            throw e;
                        }
                    } catch (Exception e5) {
                        e = e5;
                        z = z4;
                        fileOutputStream2 = null;
                        wakeLock = newWakeLock;
                        objectOutputStream = null;
                        this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                        }
                        if (cipherOutputStream2 != null) {
                            cipherOutputStream2.close();
                        }
                        if (fileOutputStream2 != null) {
                            fileOutputStream2.close();
                        }
                        if (wakeLock != null) {
                            if (wakeLock.isHeld()) {
                                wakeLock.release();
                            }
                        }
                        if (this.g != null) {
                            this.g.a(str, false, z);
                        }
                        z2 = false;
                        return z2;
                    } catch (Throwable th14) {
                        e = th14;
                        z3 = z4;
                        cipherOutputStream = null;
                        fileOutputStream = null;
                        if (objectOutputStream2 != null) {
                            objectOutputStream2.close();
                        }
                        if (cipherOutputStream != null) {
                            cipherOutputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (newWakeLock != null) {
                            if (newWakeLock.isHeld()) {
                                newWakeLock.release();
                            }
                        }
                        if (this.g != null) {
                            this.g.a(str, false, z3);
                        }
                        throw e;
                    }
                } catch (Exception e6) {
                    e = e6;
                    z = false;
                    objectOutputStream = null;
                    fileOutputStream2 = null;
                    wakeLock = newWakeLock;
                    this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (cipherOutputStream2 != null) {
                        cipherOutputStream2.close();
                    }
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                    if (wakeLock != null) {
                        if (wakeLock.isHeld()) {
                            wakeLock.release();
                        }
                    }
                    if (this.g != null) {
                        this.g.a(str, false, z);
                    }
                    z2 = false;
                    return z2;
                } catch (Throwable th15) {
                    e = th15;
                    cipherOutputStream = null;
                    fileOutputStream = null;
                    if (objectOutputStream2 != null) {
                        objectOutputStream2.close();
                    }
                    if (cipherOutputStream != null) {
                        cipherOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (newWakeLock != null) {
                        if (newWakeLock.isHeld()) {
                            newWakeLock.release();
                        }
                    }
                    if (this.g != null) {
                        this.g.a(str, false, z3);
                    }
                    throw e;
                }
            } catch (Exception e7) {
                e = e7;
                z = false;
                objectOutputStream = null;
                fileOutputStream2 = null;
                wakeLock = null;
                this.e.a("Unable to write persisted data to file: " + str + " Exception:", e);
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (cipherOutputStream2 != null) {
                    cipherOutputStream2.close();
                }
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                }
                if (wakeLock != null) {
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                }
                if (this.g != null) {
                    this.g.a(str, false, z);
                }
                z2 = false;
                return z2;
            } catch (Throwable th16) {
                e = th16;
                cipherOutputStream = null;
                fileOutputStream = null;
                newWakeLock = null;
                if (objectOutputStream2 != null) {
                    objectOutputStream2.close();
                }
                if (cipherOutputStream != null) {
                    cipherOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (newWakeLock != null) {
                    if (newWakeLock.isHeld()) {
                        newWakeLock.release();
                    }
                }
                if (this.g != null) {
                    this.g.a(str, false, z3);
                }
                throw e;
            }
        }
        return z2;
    }

    protected void f() {
        k(this.f);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected synchronized void k(java.lang.String r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r1 = 0;
        r0 = r4.d;	 Catch:{ Throwable -> 0x0027 }
        r2 = "power";
        r0 = r0.getSystemService(r2);	 Catch:{ Throwable -> 0x0027 }
        r0 = (android.os.PowerManager) r0;	 Catch:{ Throwable -> 0x0027 }
        r2 = 1;
        r3 = "Absolute_PersistedStore";
        r1 = r0.newWakeLock(r2, r3);	 Catch:{ Throwable -> 0x0027 }
        r1.acquire();	 Catch:{ Throwable -> 0x0027 }
        r4.a(r5);	 Catch:{ Throwable -> 0x0027 }
        if (r1 == 0) goto L_0x0025;
    L_0x001c:
        r0 = r1.isHeld();	 Catch:{ all -> 0x0036 }
        if (r0 == 0) goto L_0x0025;
    L_0x0022:
        r1.release();	 Catch:{ all -> 0x0036 }
    L_0x0025:
        monitor-exit(r4);
        return;
    L_0x0027:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0029 }
    L_0x0029:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0035;
    L_0x002c:
        r2 = r1.isHeld();	 Catch:{ all -> 0x0036 }
        if (r2 == 0) goto L_0x0035;
    L_0x0032:
        r1.release();	 Catch:{ all -> 0x0036 }
    L_0x0035:
        throw r0;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.absolute.android.persistservice.ac.k(java.lang.String):void");
    }

    private synchronized void a(String str) {
        CipherInputStream cipherInputStream;
        ObjectInputStream objectInputStream;
        Throwable th;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream2 = null;
        synchronized (this) {
            FileInputStream fileInputStream2;
            try {
                fileInputStream2 = new FileInputStream(str);
                try {
                    Hashtable hashtable;
                    CipherInputStream cipherInputStream2;
                    byte[] bArr = new byte[36];
                    if (fileInputStream2.read(bArr, 0, 36) == 36) {
                        byte[] bArr2 = new byte[24];
                        if (fileInputStream2.read(bArr2, 0, 24) == 24) {
                            cipherInputStream = new CipherInputStream(fileInputStream2, Crypt.getCipher(bArr2, 2, Crypt.CIPHER_TRANSFORM, new String(bArr)));
                            try {
                                objectInputStream = new ObjectInputStream(cipherInputStream);
                                try {
                                    hashtable = (Hashtable) objectInputStream.readObject();
                                    if (hashtable != null) {
                                        this.c = hashtable;
                                    }
                                    cipherInputStream2 = cipherInputStream;
                                    if (hashtable != null) {
                                        try {
                                            throw new IOException("Cannot read data file - unexpected format / corrupt.");
                                        } catch (Throwable th2) {
                                            th = th2;
                                            cipherInputStream = cipherInputStream2;
                                            objectInputStream2 = objectInputStream;
                                            if (objectInputStream2 != null) {
                                                do {
                                                    try {
                                                    } catch (Throwable th3) {
                                                        this.e.a("Exception closing persisted file: " + str + " in loadNewVersion(). Exception:", th3);
                                                        if (cipherInputStream != null) {
                                                            try {
                                                                cipherInputStream.close();
                                                            } catch (Throwable th4) {
                                                            }
                                                        }
                                                        if (fileInputStream2 != null) {
                                                            try {
                                                                fileInputStream2.close();
                                                            } catch (Throwable th5) {
                                                            }
                                                        }
                                                        throw th;
                                                    }
                                                } while (objectInputStream2.read() != -1);
                                                objectInputStream2.close();
                                            }
                                            if (cipherInputStream != null) {
                                                cipherInputStream.close();
                                            }
                                            if (fileInputStream2 != null) {
                                                fileInputStream2.close();
                                            }
                                            throw th;
                                        }
                                    } else {
                                        if (objectInputStream != null) {
                                            while (objectInputStream.read() != -1) {
                                            }
                                            try {
                                                objectInputStream.close();
                                            } catch (Throwable th6) {
                                                this.e.a("Exception closing persisted file: " + str + " in loadNewVersion(). Exception:", th6);
                                            }
                                        }
                                        if (cipherInputStream2 != null) {
                                            try {
                                                cipherInputStream2.close();
                                            } catch (Throwable th7) {
                                            }
                                        }
                                        if (fileInputStream2 != null) {
                                            try {
                                                fileInputStream2.close();
                                            } catch (Throwable th8) {
                                            }
                                        }
                                    }
                                } catch (Throwable th9) {
                                    th6 = th9;
                                    objectInputStream2 = objectInputStream;
                                    if (objectInputStream2 != null) {
                                        do {
                                        } while (objectInputStream2.read() != -1);
                                        objectInputStream2.close();
                                    }
                                    if (cipherInputStream != null) {
                                        cipherInputStream.close();
                                    }
                                    if (fileInputStream2 != null) {
                                        fileInputStream2.close();
                                    }
                                    throw th6;
                                }
                            } catch (Throwable th10) {
                                th6 = th10;
                                if (objectInputStream2 != null) {
                                    do {
                                    } while (objectInputStream2.read() != -1);
                                    objectInputStream2.close();
                                }
                                if (cipherInputStream != null) {
                                    cipherInputStream.close();
                                }
                                if (fileInputStream2 != null) {
                                    fileInputStream2.close();
                                }
                                throw th6;
                            }
                        }
                    }
                    hashtable = null;
                    objectInputStream = null;
                    if (hashtable != null) {
                        if (objectInputStream != null) {
                            while (objectInputStream.read() != -1) {
                            }
                            objectInputStream.close();
                        }
                        if (cipherInputStream2 != null) {
                            cipherInputStream2.close();
                        }
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                    } else {
                        throw new IOException("Cannot read data file - unexpected format / corrupt.");
                    }
                } catch (Throwable th11) {
                    th6 = th11;
                    cipherInputStream = null;
                    if (objectInputStream2 != null) {
                        do {
                        } while (objectInputStream2.read() != -1);
                        objectInputStream2.close();
                    }
                    if (cipherInputStream != null) {
                        cipherInputStream.close();
                    }
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                    }
                    throw th6;
                }
            } catch (Throwable th12) {
                th6 = th12;
                cipherInputStream = null;
                fileInputStream2 = null;
                if (objectInputStream2 != null) {
                    do {
                    } while (objectInputStream2.read() != -1);
                    objectInputStream2.close();
                }
                if (cipherInputStream != null) {
                    cipherInputStream.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                throw th6;
            }
        }
        return;
    }

    protected synchronized void l(String str) {
        ObjectInputStream objectInputStream;
        Throwable th;
        InputStream inputStream;
        CipherInputStream cipherInputStream;
        ObjectInputStream objectInputStream2 = null;
        synchronized (this) {
            try {
                InputStream cipherInputStream2 = new CipherInputStream(new FileInputStream(str), Crypt.getCipher(d(), 2, Crypt.CIPHER_TRANSFORM, g()));
                try {
                    objectInputStream = new ObjectInputStream(cipherInputStream2);
                } catch (Throwable th2) {
                    th = th2;
                    if (objectInputStream2 != null) {
                        do {
                            try {
                            } catch (Throwable th3) {
                                this.e.b("Exception closing persisted file: " + str + " in loadOldVersion(). Exception: " + th3.getMessage());
                                if (cipherInputStream != null) {
                                    try {
                                        cipherInputStream.close();
                                    } catch (Throwable th4) {
                                    }
                                }
                                throw th;
                            }
                        } while (objectInputStream2.read() != -1);
                        objectInputStream2.close();
                    }
                    if (cipherInputStream != null) {
                        cipherInputStream.close();
                    }
                    throw th;
                }
                try {
                    Hashtable hashtable = (Hashtable) objectInputStream.readObject();
                    if (hashtable != null) {
                        this.c = hashtable;
                    }
                    if (hashtable == null) {
                        throw new IOException("Cannot read data file in old format.");
                    }
                    if (objectInputStream != null) {
                        while (objectInputStream.read() != -1) {
                        }
                        try {
                            objectInputStream.close();
                        } catch (Throwable th5) {
                            this.e.b("Exception closing persisted file: " + str + " in loadOldVersion(). Exception: " + th5.getMessage());
                        }
                    }
                    if (cipherInputStream2 != null) {
                        try {
                            cipherInputStream2.close();
                        } catch (Throwable th6) {
                        }
                    }
                } catch (Throwable th7) {
                    th5 = th7;
                    objectInputStream2 = objectInputStream;
                    if (objectInputStream2 != null) {
                        do {
                        } while (objectInputStream2.read() != -1);
                        objectInputStream2.close();
                    }
                    if (cipherInputStream != null) {
                        cipherInputStream.close();
                    }
                    throw th5;
                }
            } catch (Throwable th8) {
                th5 = th8;
                cipherInputStream = null;
                if (objectInputStream2 != null) {
                    do {
                    } while (objectInputStream2.read() != -1);
                    objectInputStream2.close();
                }
                if (cipherInputStream != null) {
                    cipherInputStream.close();
                }
                throw th5;
            }
        }
        return;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ac)) {
            return false;
        }
        ac acVar = (ac) obj;
        if (this.c.size() == acVar.c.size()) {
            return this.c.equals(acVar.c);
        }
        return false;
    }

    private byte[] a() {
        byte[] bArr = new byte[24];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    private String b() {
        return UUID.randomUUID().toString();
    }

    private byte[] d() {
        String manufacturer = DeviceUtil.getManufacturer();
        if (manufacturer.equals("LENOVO")) {
            manufacturer = "Lenovo";
        }
        manufacturer = DeviceUtil.getSerialNumber() + DeviceUtil.getModel() + manufacturer;
        while (manufacturer.length() < 24) {
            manufacturer = manufacturer + '#';
        }
        return manufacturer.getBytes();
    }

    private String g() {
        String str = "0EB8B69D04F2451E8B59C47D";
        return "0EB8B69D04F2451E8B59C47D";
    }
}
