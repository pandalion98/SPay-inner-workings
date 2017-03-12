package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.p002b.Log;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.core.o */
public class State {
    private static final State jZ;

    /* renamed from: com.samsung.android.spayfw.core.o.a */
    private static class State {
        private static int ka;

        private State() {
        }

        static {
            ka = 1;
        }

        private static String m649s(int i) {
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    return "NPAY_READY";
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    return "NPAY_SELECTED";
                case X509KeyUsage.keyAgreement /*8*/:
                    return "PAY_SEC_MST";
                case X509KeyUsage.dataEncipherment /*16*/:
                    return "PAY_IDLE";
                case X509KeyUsage.keyEncipherment /*32*/:
                    return "PAY_NFC";
                case X509KeyUsage.nonRepudiation /*64*/:
                    return "PAY_INIT_MST";
                case X509KeyUsage.digitalSignature /*128*/:
                    return "PAY_ECOMMERCE";
                case SkeinMac.SKEIN_256 /*256*/:
                    return "PAY_IDLE_STOPPING";
                case SkeinMac.SKEIN_512 /*512*/:
                    return "PAY_NFC_STOPPING";
                case SkeinMac.SKEIN_1024 /*1024*/:
                    return "PAY_INIT_MST_STOPPING";
                case PKIFailureInfo.wrongIntegrity /*2048*/:
                    return "PAY_SEC_MST_STOPPING";
                case 3072:
                    return "PAY_MST_STOPPING";
                case PKIFailureInfo.certConfirmed /*4096*/:
                    return "PAY_ECOMMERCE_STOPPING";
                case PKIFailureInfo.certRevoked /*8192*/:
                    return "PAY_EXTRACT_CARDDETAIL";
                case PKIFailureInfo.badPOP /*16384*/:
                    return "PAY_EXTRACT_CARDDETAIL_STOPPING";
                case 24320:
                    return "PAY_STOPPING";
                default:
                    return "UNKNOWN_STATE";
            }
        }

        private static void m648a(int i, int i2, boolean z) {
            if (z) {
                Log.m285d("StateMachine", "state has changed from " + State.m649s(i) + " to " + State.m649s(i2));
            } else {
                Log.m285d("StateMachine", "state cannot be changed from " + State.m649s(i) + " to " + State.m649s(i2));
            }
        }

        public boolean m652r(int i) {
            return m650a(i, false);
        }

        public boolean m651q(int i) {
            return m650a(i, true);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized boolean m650a(int r7, boolean r8) {
            /*
            r6 = this;
            r2 = 64;
            r3 = 8;
            r0 = 0;
            r1 = 1;
            monitor-enter(r6);
            r4 = com.samsung.android.spayfw.core.State.m655o(r7);	 Catch:{ all -> 0x0068 }
            if (r4 != 0) goto L_0x002d;
        L_0x000d:
            r1 = "StateMachine";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
            r2.<init>();	 Catch:{ all -> 0x0068 }
            r3 = "the new state ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r7);	 Catch:{ all -> 0x0068 }
            r3 = " is not valid";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r2 = r2.toString();	 Catch:{ all -> 0x0068 }
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);	 Catch:{ all -> 0x0068 }
        L_0x002b:
            monitor-exit(r6);
            return r0;
        L_0x002d:
            r4 = ka;	 Catch:{ all -> 0x0068 }
            r5 = com.samsung.android.spayfw.core.State.m653b(r4, r7);	 Catch:{ all -> 0x0068 }
            if (r5 == 0) goto L_0x006b;
        L_0x0035:
            if (r8 == 0) goto L_0x003d;
        L_0x0037:
            r0 = 1;
            com.samsung.android.spayfw.core.State.State.m648a(r4, r7, r0);	 Catch:{ all -> 0x0068 }
        L_0x003b:
            r0 = r1;
            goto L_0x002b;
        L_0x003d:
            r0 = "StateMachine";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
            r2.<init>();	 Catch:{ all -> 0x0068 }
            r3 = "check state from ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = com.samsung.android.spayfw.core.State.State.m649s(r4);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = " to ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = com.samsung.android.spayfw.core.State.State.m649s(r7);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r2 = r2.toString();	 Catch:{ all -> 0x0068 }
            com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0068 }
            goto L_0x003b;
        L_0x0068:
            r0 = move-exception;
            monitor-exit(r6);
            throw r0;
        L_0x006b:
            switch(r4) {
                case 1: goto L_0x0087;
                case 2: goto L_0x0096;
                case 8: goto L_0x00e4;
                case 16: goto L_0x009d;
                case 32: goto L_0x00b8;
                case 64: goto L_0x00d3;
                case 128: goto L_0x00f5;
                case 256: goto L_0x0103;
                case 512: goto L_0x0103;
                case 1024: goto L_0x0103;
                case 2048: goto L_0x0103;
                case 3072: goto L_0x0103;
                case 4096: goto L_0x0103;
                case 8192: goto L_0x00fc;
                case 16384: goto L_0x0103;
                case 24320: goto L_0x0103;
                default: goto L_0x006e;
            };
        L_0x006e:
            r1 = "StateMachine";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
            r2.<init>();	 Catch:{ all -> 0x0068 }
            r3 = "wrong state ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r7);	 Catch:{ all -> 0x0068 }
            r2 = r2.toString();	 Catch:{ all -> 0x0068 }
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);	 Catch:{ all -> 0x0068 }
            goto L_0x002b;
        L_0x0087:
            r0 = 2;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
        L_0x008c:
            if (r8 == 0) goto L_0x0109;
        L_0x008e:
            if (r0 == 0) goto L_0x0092;
        L_0x0090:
            ka = r7;	 Catch:{ all -> 0x0068 }
        L_0x0092:
            com.samsung.android.spayfw.core.State.State.m648a(r4, r7, r0);	 Catch:{ all -> 0x0068 }
            goto L_0x002b;
        L_0x0096:
            r0 = 24337; // 0x5f11 float:3.4103E-41 double:1.2024E-319;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            goto L_0x008c;
        L_0x009d:
            r0 = 32737; // 0x7fe1 float:4.5874E-41 double:1.6174E-319;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            r1 = 24320; // 0x5f00 float:3.408E-41 double:1.20157E-319;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x00ae;
        L_0x00ab:
            r7 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
            goto L_0x008c;
        L_0x00ae:
            r1 = 64;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x008c;
        L_0x00b6:
            r7 = r2;
            goto L_0x008c;
        L_0x00b8:
            r0 = 24329; // 0x5f09 float:3.4092E-41 double:1.202E-319;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            r1 = 24320; // 0x5f00 float:3.408E-41 double:1.20157E-319;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x00c9;
        L_0x00c6:
            r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
            goto L_0x008c;
        L_0x00c9:
            r1 = 8;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x008c;
        L_0x00d1:
            r7 = r3;
            goto L_0x008c;
        L_0x00d3:
            r0 = 24353; // 0x5f21 float:3.4126E-41 double:1.2032E-319;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            r1 = 24320; // 0x5f00 float:3.408E-41 double:1.20157E-319;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x008c;
        L_0x00e1:
            r7 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
            goto L_0x008c;
        L_0x00e4:
            r0 = 24321; // 0x5f01 float:3.4081E-41 double:1.2016E-319;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            r1 = 24320; // 0x5f00 float:3.408E-41 double:1.20157E-319;
            r1 = com.samsung.android.spayfw.core.State.m653b(r7, r1);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x008c;
        L_0x00f2:
            r7 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
            goto L_0x008c;
        L_0x00f5:
            r0 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            goto L_0x008c;
        L_0x00fc:
            r0 = 16387; // 0x4003 float:2.2963E-41 double:8.0963E-320;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            goto L_0x008c;
        L_0x0103:
            r0 = 1;
            r0 = com.samsung.android.spayfw.core.State.m653b(r7, r0);	 Catch:{ all -> 0x0068 }
            goto L_0x008c;
        L_0x0109:
            r1 = "StateMachine";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
            r2.<init>();	 Catch:{ all -> 0x0068 }
            r3 = "check state from ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = com.samsung.android.spayfw.core.State.State.m649s(r4);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = " to ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r3 = com.samsung.android.spayfw.core.State.State.m649s(r7);	 Catch:{ all -> 0x0068 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0068 }
            r2 = r2.toString();	 Catch:{ all -> 0x0068 }
            com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);	 Catch:{ all -> 0x0068 }
            goto L_0x002b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.o.a.a(int, boolean):boolean");
        }

        public int getState() {
            return ka;
        }
    }

    static {
        jZ = new State();
    }

    private static boolean m653b(int i, int i2) {
        if ((i & i2) == 0) {
            return false;
        }
        return true;
    }

    public static boolean m655o(int i) {
        if ((i & 32763) == 0) {
            return false;
        }
        return true;
    }

    public static int getState() {
        return jZ.getState();
    }

    public static boolean m656p(int i) {
        return State.m653b(State.getState(), i);
    }

    public static boolean aN() {
        return State.m653b(State.getState(), 32760);
    }

    public static boolean aO() {
        return State.m653b(State.getState(), 544);
    }

    public static boolean m657q(int i) {
        return jZ.m651q(i);
    }

    public static boolean m658r(int i) {
        return jZ.m652r(i);
    }
}
