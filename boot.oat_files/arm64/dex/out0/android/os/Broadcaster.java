package android.os;

public class Broadcaster {
    private Registration mReg;

    private class Registration {
        Registration next;
        Registration prev;
        int senderWhat;
        int[] targetWhats;
        Handler[] targets;

        private Registration() {
        }
    }

    public void request(int senderWhat, Handler target, int targetWhat) {
        synchronized (this) {
            Registration registration;
            if (this.mReg == null) {
                Registration r = new Registration();
                try {
                    r.senderWhat = senderWhat;
                    r.targets = new Handler[1];
                    r.targetWhats = new int[1];
                    r.targets[0] = target;
                    r.targetWhats[0] = targetWhat;
                    this.mReg = r;
                    r.next = r;
                    r.prev = r;
                    registration = r;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    registration = r;
                    throw th2;
                }
            }
            int n;
            Registration start = this.mReg;
            registration = start;
            while (registration.senderWhat < senderWhat) {
                try {
                    registration = registration.next;
                    if (registration == start) {
                        break;
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                }
            }
            if (registration.senderWhat != senderWhat) {
                Registration reg = new Registration();
                reg.senderWhat = senderWhat;
                reg.targets = new Handler[1];
                reg.targetWhats = new int[1];
                reg.next = registration;
                reg.prev = registration.prev;
                registration.prev.next = reg;
                registration.prev = reg;
                if (registration == this.mReg && registration.senderWhat > reg.senderWhat) {
                    this.mReg = reg;
                }
                registration = reg;
                n = 0;
            } else {
                n = registration.targets.length;
                Handler[] oldTargets = registration.targets;
                int[] oldWhats = registration.targetWhats;
                int i = 0;
                while (i < n) {
                    if (oldTargets[i] == target && oldWhats[i] == targetWhat) {
                        return;
                    }
                    i++;
                }
                registration.targets = new Handler[(n + 1)];
                System.arraycopy(oldTargets, 0, registration.targets, 0, n);
                registration.targetWhats = new int[(n + 1)];
                System.arraycopy(oldWhats, 0, registration.targetWhats, 0, n);
            }
            registration.targets[n] = target;
            registration.targetWhats[n] = targetWhat;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancelRequest(int r11, android.os.Handler r12, int r13) {
        /*
        r10 = this;
        monitor-enter(r10);
        r4 = r10.mReg;	 Catch:{ all -> 0x0052 }
        r2 = r4;
        if (r2 != 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r10);	 Catch:{ all -> 0x0052 }
    L_0x0007:
        return;
    L_0x0008:
        r7 = r2.senderWhat;	 Catch:{ all -> 0x0052 }
        if (r7 < r11) goto L_0x0055;
    L_0x000c:
        r7 = r2.senderWhat;	 Catch:{ all -> 0x0052 }
        if (r7 != r11) goto L_0x0050;
    L_0x0010:
        r5 = r2.targets;	 Catch:{ all -> 0x0052 }
        r6 = r2.targetWhats;	 Catch:{ all -> 0x0052 }
        r1 = r5.length;	 Catch:{ all -> 0x0052 }
        r0 = 0;
    L_0x0016:
        if (r0 >= r1) goto L_0x0050;
    L_0x0018:
        r7 = r5[r0];	 Catch:{ all -> 0x0052 }
        if (r7 != r12) goto L_0x005a;
    L_0x001c:
        r7 = r6[r0];	 Catch:{ all -> 0x0052 }
        if (r7 != r13) goto L_0x005a;
    L_0x0020:
        r7 = r1 + -1;
        r7 = new android.os.Handler[r7];	 Catch:{ all -> 0x0052 }
        r2.targets = r7;	 Catch:{ all -> 0x0052 }
        r7 = r1 + -1;
        r7 = new int[r7];	 Catch:{ all -> 0x0052 }
        r2.targetWhats = r7;	 Catch:{ all -> 0x0052 }
        if (r0 <= 0) goto L_0x003c;
    L_0x002e:
        r7 = 0;
        r8 = r2.targets;	 Catch:{ all -> 0x0052 }
        r9 = 0;
        java.lang.System.arraycopy(r5, r7, r8, r9, r0);	 Catch:{ all -> 0x0052 }
        r7 = 0;
        r8 = r2.targetWhats;	 Catch:{ all -> 0x0052 }
        r9 = 0;
        java.lang.System.arraycopy(r6, r7, r8, r9, r0);	 Catch:{ all -> 0x0052 }
    L_0x003c:
        r7 = r1 - r0;
        r3 = r7 + -1;
        if (r3 == 0) goto L_0x0050;
    L_0x0042:
        r7 = r0 + 1;
        r8 = r2.targets;	 Catch:{ all -> 0x0052 }
        java.lang.System.arraycopy(r5, r7, r8, r0, r3);	 Catch:{ all -> 0x0052 }
        r7 = r0 + 1;
        r8 = r2.targetWhats;	 Catch:{ all -> 0x0052 }
        java.lang.System.arraycopy(r6, r7, r8, r0, r3);	 Catch:{ all -> 0x0052 }
    L_0x0050:
        monitor-exit(r10);	 Catch:{ all -> 0x0052 }
        goto L_0x0007;
    L_0x0052:
        r7 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0052 }
        throw r7;
    L_0x0055:
        r2 = r2.next;	 Catch:{ all -> 0x0052 }
        if (r2 != r4) goto L_0x0008;
    L_0x0059:
        goto L_0x000c;
    L_0x005a:
        r0 = r0 + 1;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Broadcaster.cancelRequest(int, android.os.Handler, int):void");
    }

    public void dumpRegistrations() {
        synchronized (this) {
            Registration start = this.mReg;
            System.out.println("Broadcaster " + this + " {");
            if (start != null) {
                Registration r = start;
                do {
                    System.out.println("    senderWhat=" + r.senderWhat);
                    int n = r.targets.length;
                    for (int i = 0; i < n; i++) {
                        System.out.println("        [" + r.targetWhats[i] + "] " + r.targets[i]);
                    }
                    r = r.next;
                } while (r != start);
            }
            System.out.println("}");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void broadcast(android.os.Message r11) {
        /*
        r10 = this;
        monitor-enter(r10);
        r9 = r10.mReg;	 Catch:{ all -> 0x0036 }
        if (r9 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r10);	 Catch:{ all -> 0x0036 }
    L_0x0006:
        return;
    L_0x0007:
        r4 = r11.what;	 Catch:{ all -> 0x0036 }
        r5 = r10.mReg;	 Catch:{ all -> 0x0036 }
        r3 = r5;
    L_0x000c:
        r9 = r3.senderWhat;	 Catch:{ all -> 0x0036 }
        if (r9 < r4) goto L_0x002f;
    L_0x0010:
        r9 = r3.senderWhat;	 Catch:{ all -> 0x0036 }
        if (r9 != r4) goto L_0x0034;
    L_0x0014:
        r7 = r3.targets;	 Catch:{ all -> 0x0036 }
        r8 = r3.targetWhats;	 Catch:{ all -> 0x0036 }
        r2 = r7.length;	 Catch:{ all -> 0x0036 }
        r0 = 0;
    L_0x001a:
        if (r0 >= r2) goto L_0x0034;
    L_0x001c:
        r6 = r7[r0];	 Catch:{ all -> 0x0036 }
        r1 = android.os.Message.obtain();	 Catch:{ all -> 0x0036 }
        r1.copyFrom(r11);	 Catch:{ all -> 0x0036 }
        r9 = r8[r0];	 Catch:{ all -> 0x0036 }
        r1.what = r9;	 Catch:{ all -> 0x0036 }
        r6.sendMessage(r1);	 Catch:{ all -> 0x0036 }
        r0 = r0 + 1;
        goto L_0x001a;
    L_0x002f:
        r3 = r3.next;	 Catch:{ all -> 0x0036 }
        if (r3 != r5) goto L_0x000c;
    L_0x0033:
        goto L_0x0010;
    L_0x0034:
        monitor-exit(r10);	 Catch:{ all -> 0x0036 }
        goto L_0x0006;
    L_0x0036:
        r9 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0036 }
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Broadcaster.broadcast(android.os.Message):void");
    }
}
