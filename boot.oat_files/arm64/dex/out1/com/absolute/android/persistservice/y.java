package com.absolute.android.persistservice;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import com.absolute.android.utils.CommandUtil;
import com.absolute.android.utils.FileUtil;
import com.android.internal.os.SMProviderContract;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

public class y {
    static final /* synthetic */ boolean a = (!y.class.desiredAssertionStatus());
    private v b;
    private Context c;
    private Hashtable d = new Hashtable();
    private Hashtable e = new Hashtable();
    private aa f;

    protected y(Context context) {
        this.c = context;
    }

    protected void a(v vVar) {
        this.b = vVar;
    }

    protected synchronized void a(ac acVar, boolean z, boolean z2) {
        Throwable th;
        boolean z3;
        boolean z4 = false;
        synchronized (this) {
            if (z2) {
                if (!this.d.containsKey(acVar.f)) {
                    acVar.a(this);
                }
            }
            try {
                acVar.f();
                if (z2) {
                    a(acVar);
                }
            } catch (Throwable th2) {
                th = th2;
                if (this.b != null) {
                    this.b.a("Failed to read persisted data file: " + acVar.f + " in pre-3118 format.", th);
                }
                acVar.c();
                z4 = acVar.e();
                this.b.a("Failed to save initialized storage file " + acVar.f, null);
                a(acVar);
            }
        }
    }

    private void a(ac acVar) {
        this.d.put(acVar.f, new ab(this, acVar));
        File parentFile = new File(acVar.f).getParentFile();
        if (parentFile != null) {
            String str = parentFile.getAbsolutePath() + File.separatorChar;
            if (!this.e.containsKey(str)) {
                this.e.put(str, new z(this, str));
                parentFile = parentFile.getParentFile();
                if (parentFile != null) {
                    String str2 = parentFile.getAbsolutePath() + File.separatorChar;
                    if (!this.e.containsKey(str2)) {
                        this.e.put(str2, new z(this, str2));
                    }
                }
            }
        }
    }

    protected synchronized void a() {
        if (this.f == null) {
            HandlerThread handlerThread = new HandlerThread("ABT_PersistedStorageManager");
            handlerThread.start();
            this.f = new aa(this, handlerThread.getLooper());
        }
        b();
        a(false);
    }

    private synchronized void b() {
        Enumeration elements = this.d.elements();
        while (elements.hasMoreElements()) {
            a((ab) elements.nextElement());
        }
    }

    private synchronized void a(ab abVar) {
        abVar.startWatching();
    }

    private synchronized void a(boolean z) {
        Enumeration elements = this.e.elements();
        while (elements.hasMoreElements()) {
            a((z) elements.nextElement(), z);
        }
    }

    private synchronized void a(z zVar, boolean z) {
        if (z) {
            zVar.stopWatching();
            z zVar2 = new z(this, zVar.b);
            this.e.put(zVar.b, zVar2);
            zVar2.startWatching();
        } else {
            zVar.startWatching();
        }
    }

    protected synchronized void a(String str) {
        ab abVar = (ab) this.d.get(str);
        if (abVar != null) {
            if (!a && abVar.c) {
                throw new AssertionError();
            } else if (!abVar.c) {
                abVar.c = true;
                abVar.stopWatching();
            }
        }
    }

    protected synchronized void a(String str, boolean z, boolean z2) {
        ab abVar = (ab) this.d.get(str);
        if (abVar != null) {
            if (!a && !abVar.c) {
                throw new AssertionError();
            } else if (abVar.c) {
                abVar.c = false;
                if (this.f != null) {
                    if (z) {
                        if (z2) {
                            abVar.stopWatching();
                            ab abVar2 = new ab(this, abVar.b);
                            this.d.put(abVar.b.f, abVar2);
                            abVar2.startWatching();
                        } else {
                            abVar.startWatching();
                        }
                    } else if (this.b != null) {
                        this.b.a("PersistedFileObserver.onSelfUpdateEnd(), failed to save to path: " + str, null);
                    }
                }
            }
        }
    }

    private void a(String str, boolean z) {
        try {
            if (this.e.containsKey(str) && str.equals(ABTPersistenceService.a())) {
                if (this.b != null) {
                    this.b.b("Persisted directory has been removed.");
                }
                Message.obtain(this.f, 3, str).sendToTarget();
            } else if (this.d.containsKey(str)) {
                ab abVar = (ab) this.d.get(str);
                if (!z || abVar.b.i(abVar.b.f)) {
                    String str2 = str + " has been removed / modified.";
                    if (this.b != null) {
                        this.b.b(str2);
                    }
                    Message.obtain(this.f, 1, abVar).sendToTarget();
                }
            }
        } catch (Throwable th) {
            if (this.b != null) {
                this.b.a("Unable to restore persisted file: " + str + " Exception: ", th);
            }
        }
    }

    private synchronized void b(ab abVar) {
        if (abVar != null) {
            abVar.b.e();
        }
    }

    private synchronized void b(String str) {
        new File(str).mkdirs();
        Message.obtain(this.f, 4, str).sendToTarget();
        Message.obtain(this.f, 2).sendToTarget();
    }

    private synchronized void c() {
        Enumeration elements = this.d.elements();
        while (elements.hasMoreElements()) {
            b((ab) elements.nextElement());
        }
        a(true);
    }

    private void c(String str) {
        try {
            File file = new File(str);
            if (!file.canRead() || !file.canWrite() || (file.isDirectory() && !file.canExecute())) {
                if (this.b != null) {
                    this.b.b("No longer have access to file: " + str);
                }
                Message.obtain(this.f, 4, str).sendToTarget();
            }
        } catch (Throwable th) {
            if (this.b != null) {
                this.b.a("Unable to restore access to: " + str + " Exception: ", th);
            }
        }
    }

    private synchronized void d(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                String str2 = file.isDirectory() ? "750" : "600";
                try {
                    CommandUtil.executeCommand("chown system:system " + str, this.c);
                    CommandUtil.executeCommand("chmod " + str2 + " " + str, this.c);
                } catch (Throwable th) {
                }
                if (!(file.canRead() && file.canWrite() && (!file.isDirectory() || file.canExecute()))) {
                    if (this.e.containsKey(str)) {
                        ((PowerManager) this.c.getSystemService(SMProviderContract.KEY_POWER)).reboot("Absolute Persistence Service unable to access " + str);
                    } else {
                        if (this.b != null) {
                            this.b.b("Unable to set permission of persisted file / folder " + str + " Deleting file ...");
                        }
                        FileUtil.deleteFile(file);
                    }
                }
            }
        } catch (Throwable th2) {
            if (this.b != null) {
                this.b.a("doResetPermissions for : " + str + " threw exception: ", th2);
            }
        }
    }
}
