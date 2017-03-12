package com.samsung.android.spayfw.utils;

import com.samsung.android.spayfw.p002b.Log;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class IOExceptionHandler {

    /* renamed from: com.samsung.android.spayfw.utils.IOExceptionHandler.a */
    public static abstract class C0403a<T> {
        public abstract T aJ();

        public abstract void m263c(T t);

        public abstract void m266f(T t);

        public void m265e(T t) {
        }

        public void m264d(T t) {
        }
    }

    private static class ExceptionChain extends RuntimeException {
        private ArrayList<Exception> exceptions;

        public ExceptionChain(ArrayList<Exception> arrayList) {
            String str;
            StringBuilder append = new StringBuilder().append("Exception Chain contains ");
            if (arrayList.size() > 1) {
                str = arrayList.size() + " exceptions";
            } else {
                str = arrayList.size() + " exception";
            }
            super(append.append(str).toString(), (Throwable) arrayList.get(0));
            this.exceptions = arrayList;
        }

        public void printStackTrace(PrintStream printStream) {
            synchronized (printStream) {
                super.printStackTrace(printStream);
                Iterator it = this.exceptions.iterator();
                int i = 0;
                while (it.hasNext()) {
                    Exception exception = (Exception) it.next();
                    i++;
                    printStream.println("Exception " + i + " :");
                    exception.printStackTrace(printStream);
                }
            }
        }

        public void printStackTrace(PrintWriter printWriter) {
            synchronized (printWriter) {
                super.printStackTrace(printWriter);
                Iterator it = this.exceptions.iterator();
                int i = 0;
                while (it.hasNext()) {
                    Exception exception = (Exception) it.next();
                    i++;
                    printWriter.println("Exception " + i + " :");
                    exception.printStackTrace(printWriter);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <S, T extends com.samsung.android.spayfw.utils.IOExceptionHandler.C0403a<S>> void m1261a(T r4) {
        /*
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = r4.aJ();	 Catch:{ IOException -> 0x0026 }
        r4.m266f(r2);	 Catch:{ IOException -> 0x0030 }
        r4.m265e(r2);	 Catch:{ IOException -> 0x0021 }
    L_0x000f:
        r4.m264d(r2);	 Catch:{ IOException -> 0x002b }
    L_0x0012:
        r4.m263c(r2);	 Catch:{ IOException -> 0x0026 }
    L_0x0015:
        r0 = r1.size();
        if (r0 == 0) goto L_0x005d;
    L_0x001b:
        r0 = new com.samsung.android.spayfw.utils.IOExceptionHandler$ExceptionChain;
        r0.<init>(r1);
        throw r0;
    L_0x0021:
        r0 = move-exception;
        r1.add(r0);	 Catch:{ IOException -> 0x0026 }
        goto L_0x000f;
    L_0x0026:
        r0 = move-exception;
        r1.add(r0);
        goto L_0x0015;
    L_0x002b:
        r0 = move-exception;
        r1.add(r0);	 Catch:{ IOException -> 0x0026 }
        goto L_0x0012;
    L_0x0030:
        r0 = move-exception;
        r1.add(r0);	 Catch:{ all -> 0x0048 }
        r4.m265e(r2);	 Catch:{ IOException -> 0x003e }
    L_0x0037:
        r4.m264d(r2);	 Catch:{ IOException -> 0x0043 }
    L_0x003a:
        r4.m263c(r2);	 Catch:{ IOException -> 0x0026 }
        goto L_0x0015;
    L_0x003e:
        r0 = move-exception;
        r1.add(r0);	 Catch:{ IOException -> 0x0026 }
        goto L_0x0037;
    L_0x0043:
        r0 = move-exception;
        r1.add(r0);	 Catch:{ IOException -> 0x0026 }
        goto L_0x003a;
    L_0x0048:
        r0 = move-exception;
        r4.m265e(r2);	 Catch:{ IOException -> 0x0053 }
    L_0x004c:
        r4.m264d(r2);	 Catch:{ IOException -> 0x0058 }
    L_0x004f:
        r4.m263c(r2);	 Catch:{ IOException -> 0x0026 }
        throw r0;	 Catch:{ IOException -> 0x0026 }
    L_0x0053:
        r3 = move-exception;
        r1.add(r3);	 Catch:{ IOException -> 0x0026 }
        goto L_0x004c;
    L_0x0058:
        r3 = move-exception;
        r1.add(r3);	 Catch:{ IOException -> 0x0026 }
        goto L_0x004f;
    L_0x005d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.IOExceptionHandler.a(com.samsung.android.spayfw.utils.IOExceptionHandler$a):void");
    }

    public static <S, T extends C0403a<S>> void m1262a(T t, boolean z) {
        try {
            m1261a(t);
        } catch (Throwable e) {
            if (z) {
                Log.m284c("IOProcessor", e.getMessage(), e);
                return;
            }
            throw e;
        }
    }
}
