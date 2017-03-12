package android.app;

import android.os.Binder;
import android.os.IBinder;

public abstract class ApplicationThreadNative extends Binder implements IApplicationThread {
    public boolean onTransact(int r137, android.os.Parcel r138, android.os.Parcel r139, int r140) throws android.os.RemoteException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:238:0x0998
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.modifyBlocksTree(BlockProcessor.java:248)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r136 = this;
        switch(r137) {
            case 1: goto L_0x0008;
            case 3: goto L_0x0039;
            case 4: goto L_0x005c;
            case 5: goto L_0x009b;
            case 6: goto L_0x00da;
            case 7: goto L_0x00f7;
            case 8: goto L_0x021a;
            case 9: goto L_0x0237;
            case 10: goto L_0x027c;
            case 11: goto L_0x02ce;
            case 12: goto L_0x03c4;
            case 13: goto L_0x03d9;
            case 14: goto L_0x0490;
            case 16: goto L_0x04aa;
            case 17: goto L_0x038b;
            case 18: goto L_0x04c5;
            case 19: goto L_0x0554;
            case 20: goto L_0x033a;
            case 21: goto L_0x036c;
            case 22: goto L_0x0561;
            case 23: goto L_0x05b1;
            case 24: goto L_0x0604;
            case 25: goto L_0x0611;
            case 26: goto L_0x01ac;
            case 27: goto L_0x007b;
            case 28: goto L_0x07b8;
            case 29: goto L_0x07f1;
            case 30: goto L_0x0806;
            case 31: goto L_0x0833;
            case 32: goto L_0x0a52;
            case 33: goto L_0x049d;
            case 34: goto L_0x085a;
            case 35: goto L_0x0875;
            case 36: goto L_0x088a;
            case 37: goto L_0x08c3;
            case 38: goto L_0x04d2;
            case 39: goto L_0x04df;
            case 40: goto L_0x08f1;
            case 41: goto L_0x0906;
            case 42: goto L_0x0927;
            case 43: goto L_0x093c;
            case 44: goto L_0x099c;
            case 45: goto L_0x0589;
            case 46: goto L_0x09c6;
            case 47: goto L_0x09f0;
            case 48: goto L_0x0a08;
            case 49: goto L_0x0a2c;
            case 50: goto L_0x0a77;
            case 51: goto L_0x0a8f;
            case 52: goto L_0x0ae3;
            case 53: goto L_0x0b01;
            case 54: goto L_0x0b19;
            case 55: goto L_0x0b3e;
            case 56: goto L_0x0b56;
            case 1001: goto L_0x0638;
            case 1002: goto L_0x0661;
            case 1003: goto L_0x0694;
            case 1501: goto L_0x050c;
            case 2000: goto L_0x0aad;
            case 2001: goto L_0x025b;
            case 2501: goto L_0x0ac8;
            case 3002: goto L_0x06ac;
            case 3003: goto L_0x06e2;
            case 3004: goto L_0x02ff;
            case 3005: goto L_0x070a;
            case 3006: goto L_0x06ca;
            case 3007: goto L_0x0757;
            case 3008: goto L_0x0776;
            case 3009: goto L_0x079a;
            case 3010: goto L_0x0539;
            case 3021: goto L_0x00c7;
            default: goto L_0x0003;
        };
    L_0x0003:
        r5 = super.onTransact(r137, r138, r139, r140);
    L_0x0007:
        return r5;
    L_0x0008:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0033;
    L_0x0019:
        r7 = 1;
    L_0x001a:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0035;
    L_0x0020:
        r8 = 1;
    L_0x0021:
        r9 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0037;
    L_0x002b:
        r10 = 1;
    L_0x002c:
        r5 = r136;
        r5.schedulePauseActivity(r6, r7, r8, r9, r10);
        r5 = 1;
        goto L_0x0007;
    L_0x0033:
        r7 = 0;
        goto L_0x001a;
    L_0x0035:
        r8 = 0;
        goto L_0x0021;
    L_0x0037:
        r10 = 0;
        goto L_0x002c;
    L_0x0039:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0059;
    L_0x004a:
        r129 = 1;
    L_0x004c:
        r9 = r138.readInt();
        r0 = r136;
        r1 = r129;
        r0.scheduleStopActivity(r6, r1, r9);
        r5 = 1;
        goto L_0x0007;
    L_0x0059:
        r129 = 0;
        goto L_0x004c;
    L_0x005c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0078;
    L_0x006d:
        r129 = 1;
    L_0x006f:
        r0 = r136;
        r1 = r129;
        r0.scheduleWindowVisibility(r6, r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0078:
        r129 = 0;
        goto L_0x006f;
    L_0x007b:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0098;
    L_0x008c:
        r131 = 1;
    L_0x008e:
        r0 = r136;
        r1 = r131;
        r0.scheduleSleeping(r6, r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0098:
        r131 = 0;
        goto L_0x008e;
    L_0x009b:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r21 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x00c4;
    L_0x00b0:
        r27 = 1;
    L_0x00b2:
        r126 = r138.readBundle();
        r0 = r136;
        r1 = r21;
        r2 = r27;
        r3 = r126;
        r0.scheduleResumeActivity(r6, r1, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x00c4:
        r27 = 0;
        goto L_0x00b2;
    L_0x00c7:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r0 = r136;
        r0.forceCallResumeActivity(r6);
        r5 = 1;
        goto L_0x0007;
    L_0x00da:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = android.app.ResultInfo.CREATOR;
        r0 = r138;
        r24 = r0.createTypedArrayList(r5);
        r0 = r136;
        r1 = r24;
        r0.scheduleSendResult(r6, r1);
        r5 = 1;
        goto L_0x0007;
    L_0x00f7:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r12 = r5.createFromParcel(r0);
        r12 = (android.content.Intent) r12;
        r6 = r138.readStrongBinder();
        r14 = r138.readInt();
        r5 = android.content.pm.ActivityInfo.CREATOR;
        r0 = r138;
        r15 = r5.createFromParcel(r0);
        r15 = (android.content.pm.ActivityInfo) r15;
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r16 = r5.createFromParcel(r0);
        r16 = (android.content.res.Configuration) r16;
        r17 = 0;
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0136;
    L_0x012c:
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r17 = r5.createFromParcel(r0);
        r17 = (android.content.res.Configuration) r17;
    L_0x0136:
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r19 = r138.readString();
        r5 = r138.readStrongBinder();
        r20 = com.android.internal.app.IVoiceInteractor.Stub.asInterface(r5);
        r21 = r138.readInt();
        r22 = r138.readBundle();
        r23 = r138.readPersistableBundle();
        r5 = android.app.ResultInfo.CREATOR;
        r0 = r138;
        r24 = r0.createTypedArrayList(r5);
        r5 = com.android.internal.content.ReferrerIntent.CREATOR;
        r0 = r138;
        r25 = r0.createTypedArrayList(r5);
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x01a3;
    L_0x016e:
        r26 = 1;
    L_0x0170:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x01a6;
    L_0x0176:
        r27 = 1;
    L_0x0178:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x01a9;
    L_0x017e:
        r5 = android.app.ProfilerInfo.CREATOR;
        r0 = r138;
        r5 = r5.createFromParcel(r0);
        r5 = (android.app.ProfilerInfo) r5;
        r28 = r5;
    L_0x018a:
        r29 = 0;
        r5 = com.samsung.android.multiwindow.MultiWindowStyle.CREATOR;
        r0 = r138;
        r29 = r5.createFromParcel(r0);
        r29 = (com.samsung.android.multiwindow.MultiWindowStyle) r29;
        r30 = r138.readInt();
        r11 = r136;
        r13 = r6;
        r11.scheduleLaunchActivity(r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30);
        r5 = 1;
        goto L_0x0007;
    L_0x01a3:
        r26 = 0;
        goto L_0x0170;
    L_0x01a6:
        r27 = 0;
        goto L_0x0178;
    L_0x01a9:
        r28 = 0;
        goto L_0x018a;
    L_0x01ac:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = android.app.ResultInfo.CREATOR;
        r0 = r138;
        r24 = r0.createTypedArrayList(r5);
        r5 = com.android.internal.content.ReferrerIntent.CREATOR;
        r0 = r138;
        r25 = r0.createTypedArrayList(r5);
        r9 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0217;
    L_0x01d1:
        r26 = 1;
    L_0x01d3:
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r37 = r5.createFromParcel(r0);
        r37 = (android.content.res.Configuration) r37;
        r17 = 0;
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x01ef;
    L_0x01e5:
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r17 = r5.createFromParcel(r0);
        r17 = (android.content.res.Configuration) r17;
    L_0x01ef:
        r29 = 0;
        r5 = com.samsung.android.multiwindow.MultiWindowStyle.CREATOR;
        r0 = r138;
        r29 = r5.createFromParcel(r0);
        r29 = (com.samsung.android.multiwindow.MultiWindowStyle) r29;
        r30 = r138.readInt();
        r31 = r136;
        r32 = r6;
        r33 = r24;
        r34 = r25;
        r35 = r9;
        r36 = r26;
        r38 = r17;
        r39 = r29;
        r40 = r30;
        r31.scheduleRelaunchActivity(r32, r33, r34, r35, r36, r37, r38, r39, r40);
        r5 = 1;
        goto L_0x0007;
    L_0x0217:
        r26 = 0;
        goto L_0x01d3;
    L_0x021a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = com.android.internal.content.ReferrerIntent.CREATOR;
        r0 = r138;
        r25 = r0.createTypedArrayList(r5);
        r6 = r138.readStrongBinder();
        r0 = r136;
        r1 = r25;
        r0.scheduleNewIntent(r1, r6);
        r5 = 1;
        goto L_0x0007;
    L_0x0237:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0258;
    L_0x0248:
        r105 = 1;
    L_0x024a:
        r9 = r138.readInt();
        r0 = r136;
        r1 = r105;
        r0.scheduleDestroyActivity(r6, r1, r9);
        r5 = 1;
        goto L_0x0007;
    L_0x0258:
        r105 = 0;
        goto L_0x024a;
    L_0x025b:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r117 = r138.readString();
        r56 = r138.readString();
        r110 = r138.readInt();
        r0 = r136;
        r1 = r117;
        r2 = r56;
        r3 = r110;
        r0.updateOverlayPath(r1, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x027c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r12 = r5.createFromParcel(r0);
        r12 = (android.content.Intent) r12;
        r5 = android.content.pm.ActivityInfo.CREATOR;
        r0 = r138;
        r15 = r5.createFromParcel(r0);
        r15 = (android.content.pm.ActivityInfo) r15;
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r42 = r138.readInt();
        r43 = r138.readString();
        r44 = r138.readBundle();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x02cb;
    L_0x02b3:
        r45 = 1;
    L_0x02b5:
        r46 = r138.readInt();
        r47 = r138.readInt();
        r38 = r136;
        r39 = r12;
        r40 = r15;
        r41 = r18;
        r38.scheduleReceiver(r39, r40, r41, r42, r43, r44, r45, r46, r47);
        r5 = 1;
        goto L_0x0007;
    L_0x02cb:
        r45 = 0;
        goto L_0x02b5;
    L_0x02ce:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = android.content.pm.ServiceInfo.CREATOR;
        r0 = r138;
        r15 = r5.createFromParcel(r0);
        r15 = (android.content.pm.ServiceInfo) r15;
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r47 = r138.readInt();
        r0 = r136;
        r1 = r49;
        r2 = r18;
        r3 = r47;
        r0.scheduleCreateService(r1, r15, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x02ff:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = android.content.pm.ServiceInfo.CREATOR;
        r0 = r138;
        r15 = r5.createFromParcel(r0);
        r15 = (android.content.pm.ServiceInfo) r15;
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r47 = r138.readInt();
        r30 = r138.readInt();
        r54 = r138.readStrongBinder();
        r48 = r136;
        r50 = r15;
        r51 = r18;
        r52 = r47;
        r53 = r30;
        r48.scheduleCreateService(r49, r50, r51, r52, r53, r54);
        r5 = 1;
        goto L_0x0007;
    L_0x033a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r12 = r5.createFromParcel(r0);
        r12 = (android.content.Intent) r12;
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0369;
    L_0x0355:
        r123 = 1;
    L_0x0357:
        r47 = r138.readInt();
        r0 = r136;
        r1 = r49;
        r2 = r123;
        r3 = r47;
        r0.scheduleBindService(r1, r12, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x0369:
        r123 = 0;
        goto L_0x0357;
    L_0x036c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r12 = r5.createFromParcel(r0);
        r12 = (android.content.Intent) r12;
        r0 = r136;
        r1 = r49;
        r0.scheduleUnbindService(r1, r12);
        r5 = 1;
        goto L_0x0007;
    L_0x038b:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x03be;
    L_0x039c:
        r50 = 1;
    L_0x039e:
        r51 = r138.readInt();
        r52 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x03c1;
    L_0x03ac:
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r53 = r5.createFromParcel(r0);
        r53 = (android.content.Intent) r53;
    L_0x03b6:
        r48 = r136;
        r48.scheduleServiceArgs(r49, r50, r51, r52, r53);
        r5 = 1;
        goto L_0x0007;
    L_0x03be:
        r50 = 0;
        goto L_0x039e;
    L_0x03c1:
        r53 = 0;
        goto L_0x03b6;
    L_0x03c4:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r0 = r136;
        r1 = r49;
        r0.scheduleStopService(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x03d9:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r56 = r138.readString();
        r5 = android.content.pm.ApplicationInfo.CREATOR;
        r0 = r138;
        r15 = r5.createFromParcel(r0);
        r15 = (android.content.pm.ApplicationInfo) r15;
        r5 = android.content.pm.ProviderInfo.CREATOR;
        r0 = r138;
        r58 = r0.createTypedArrayList(r5);
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0481;
    L_0x03fc:
        r59 = new android.content.ComponentName;
        r0 = r59;
        r1 = r138;
        r0.<init>(r1);
    L_0x0405:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0484;
    L_0x040b:
        r5 = android.app.ProfilerInfo.CREATOR;
        r0 = r138;
        r5 = r5.createFromParcel(r0);
        r5 = (android.app.ProfilerInfo) r5;
        r28 = r5;
    L_0x0417:
        r61 = r138.readBundle();
        r99 = r138.readStrongBinder();
        r62 = android.app.IInstrumentationWatcher.Stub.asInterface(r99);
        r99 = r138.readStrongBinder();
        r63 = android.app.IUiAutomationConnection.Stub.asInterface(r99);
        r64 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0487;
    L_0x0435:
        r65 = 1;
    L_0x0437:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x048a;
    L_0x043d:
        r66 = 1;
    L_0x043f:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x048d;
    L_0x0445:
        r67 = 1;
    L_0x0447:
        r68 = new com.samsung.android.multidisplay.common.UnRestrictedArrayList;
        r68.<init>();
        r5 = 0;
        r11 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r11 = r11.createFromParcel(r0);
        r0 = r68;
        r0.set(r5, r11);
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r5 = 0;
        r0 = r138;
        r70 = r0.readHashMap(r5);
        r71 = r138.readBundle();
        r30 = 0;
        r55 = r136;
        r57 = r15;
        r60 = r28;
        r69 = r18;
        r72 = r30;
        r55.bindApplication(r56, r57, r58, r59, r60, r61, r62, r63, r64, r65, r66, r67, r68, r69, r70, r71, r72);
        r5 = 1;
        goto L_0x0007;
    L_0x0481:
        r59 = 0;
        goto L_0x0405;
    L_0x0484:
        r28 = 0;
        goto L_0x0417;
    L_0x0487:
        r65 = 0;
        goto L_0x0437;
    L_0x048a:
        r66 = 0;
        goto L_0x043f;
    L_0x048d:
        r67 = 0;
        goto L_0x0447;
    L_0x0490:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.scheduleExit();
        r5 = 1;
        goto L_0x0007;
    L_0x049d:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.scheduleSuicide();
        r5 = 1;
        goto L_0x0007;
    L_0x04aa:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r37 = r5.createFromParcel(r0);
        r37 = (android.content.res.Configuration) r37;
        r0 = r136;
        r1 = r37;
        r0.scheduleConfigurationChanged(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x04c5:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.updateTimeZone();
        r5 = 1;
        goto L_0x0007;
    L_0x04d2:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.clearDnsCache();
        r5 = 1;
        goto L_0x0007;
    L_0x04df:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r73 = r138.readString();
        r74 = r138.readString();
        r77 = r138.readString();
        r5 = android.net.Uri.CREATOR;
        r0 = r138;
        r78 = r5.createFromParcel(r0);
        r78 = (android.net.Uri) r78;
        r0 = r136;
        r1 = r73;
        r2 = r74;
        r3 = r77;
        r4 = r78;
        r0.setHttpProxy(r1, r2, r3, r4);
        r5 = 1;
        goto L_0x0007;
    L_0x050c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r73 = r138.readString();
        r74 = r138.readString();
        r75 = r138.readString();
        r76 = r138.readString();
        r77 = r138.readString();
        r5 = android.net.Uri.CREATOR;
        r0 = r138;
        r78 = r5.createFromParcel(r0);
        r78 = (android.net.Uri) r78;
        r72 = r136;
        r72.setHttpProxy(r73, r74, r75, r76, r77, r78);
        r5 = 1;
        goto L_0x0007;
    L_0x0539:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.net.ProxyInfo.CREATOR;
        r0 = r138;
        r122 = r5.createFromParcel(r0);
        r122 = (android.net.ProxyInfo) r122;
        r0 = r136;
        r1 = r122;
        r0.setHttpProxy(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0554:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.processInBackground();
        r5 = 1;
        goto L_0x0007;
    L_0x0561:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r127 = r138.readStrongBinder();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x0586;
    L_0x0576:
        r5 = r104.getFileDescriptor();
        r0 = r136;
        r1 = r127;
        r2 = r53;
        r0.dumpService(r5, r1, r2);
        r104.close();	 Catch:{ IOException -> 0x0b6e }
    L_0x0586:
        r5 = 1;
        goto L_0x0007;
    L_0x0589:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r127 = r138.readStrongBinder();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x05ae;
    L_0x059e:
        r5 = r104.getFileDescriptor();
        r0 = r136;
        r1 = r127;
        r2 = r53;
        r0.dumpProvider(r5, r1, r2);
        r104.close();	 Catch:{ IOException -> 0x0b71 }
    L_0x05ae:
        r5 = 1;
        goto L_0x0007;
    L_0x05b1:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = r138.readStrongBinder();
        r80 = android.content.IIntentReceiver.Stub.asInterface(r5);
        r5 = android.content.Intent.CREATOR;
        r0 = r138;
        r12 = r5.createFromParcel(r0);
        r12 = (android.content.Intent) r12;
        r42 = r138.readInt();
        r83 = r138.readString();
        r84 = r138.readBundle();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x05fe;
    L_0x05dc:
        r85 = 1;
    L_0x05de:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0601;
    L_0x05e4:
        r86 = 1;
    L_0x05e6:
        r46 = r138.readInt();
        r47 = r138.readInt();
        r79 = r136;
        r81 = r12;
        r82 = r42;
        r87 = r46;
        r88 = r47;
        r79.scheduleRegisteredReceiver(r80, r81, r82, r83, r84, r85, r86, r87, r88);
        r5 = 1;
        goto L_0x0007;
    L_0x05fe:
        r85 = 0;
        goto L_0x05de;
    L_0x0601:
        r86 = 0;
        goto L_0x05e6;
    L_0x0604:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r136.scheduleLowMemory();
        r5 = 1;
        goto L_0x0007;
    L_0x0611:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r6 = r138.readStrongBinder();
        r17 = 0;
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x062e;
    L_0x0624:
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r17 = r5.createFromParcel(r0);
        r17 = (android.content.res.Configuration) r17;
    L_0x062e:
        r0 = r136;
        r1 = r17;
        r0.scheduleActivityConfigurationChanged(r6, r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0638:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r96 = r138.readStrongBinder();
        r133 = new com.samsung.android.multiwindow.MultiWindowStyle;
        r0 = r133;
        r1 = r138;
        r0.<init>(r1);
        r114 = r138.readInt();
        r0 = r136;
        r1 = r96;
        r2 = r133;
        r3 = r114;
        r0.scheduleMultiWindowStyleChanged(r1, r2, r3);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0661:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r96 = r138.readStrongBinder();
        r13 = r138.readInt();
        r5 = r138.readInt();
        r11 = 1;
        if (r5 != r11) goto L_0x0690;
    L_0x0677:
        r5 = 1;
    L_0x0678:
        r11 = r138.readInt();
        r31 = 1;
        r0 = r31;
        if (r11 != r0) goto L_0x0692;
    L_0x0682:
        r11 = 1;
    L_0x0683:
        r0 = r136;
        r1 = r96;
        r0.scheduleMultiWindowFocusChanged(r1, r13, r5, r11);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0690:
        r5 = 0;
        goto L_0x0678;
    L_0x0692:
        r11 = 0;
        goto L_0x0683;
    L_0x0694:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r96 = r138.readStrongBinder();
        r0 = r136;
        r1 = r96;
        r0.scheduleMultiWindowExitByCloseBtn(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x06ac:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r30 = r138.readInt();
        r0 = r136;
        r1 = r49;
        r2 = r30;
        r0.scheduleActivityDisplayIdChanged(r1, r2);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x06ca:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r30 = r138.readInt();
        r0 = r136;
        r1 = r30;
        r0.scheduleApplicationDisplayIdChanged(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x06e2:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r119 = r138.readString();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x0707;
    L_0x06f7:
        r5 = r104.getFileDescriptor();
        r0 = r136;
        r1 = r119;
        r2 = r53;
        r0.dumpContextRelationInfo(r5, r1, r2);
        r104.close();	 Catch:{ IOException -> 0x0b74 }
    L_0x0707:
        r5 = 1;
        goto L_0x0007;
    L_0x070a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r68 = new com.samsung.android.multidisplay.common.UnRestrictedArrayList;
        r68.<init>();
        r108 = 0;
    L_0x0718:
        r5 = 4;
        r0 = r108;
        if (r0 >= r5) goto L_0x073f;
    L_0x071d:
        r5 = android.content.res.Configuration.CREATOR;
        r0 = r138;
        r37 = r5.createFromParcel(r0);
        r37 = (android.content.res.Configuration) r37;
        if (r37 == 0) goto L_0x073f;
    L_0x0729:
        r0 = r37;
        r5 = r0.displayId;
        r0 = r68;
        r5 = r0.get(r5);
        if (r5 != 0) goto L_0x073f;
    L_0x0735:
        r5 = r68.size();
        r0 = r37;
        r11 = r0.displayId;
        if (r5 <= r11) goto L_0x0749;
    L_0x073f:
        r0 = r136;
        r1 = r68;
        r0.scheduleConfigurationsChanged(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0749:
        r0 = r37;
        r5 = r0.displayId;
        r0 = r68;
        r1 = r37;
        r0.set(r5, r1);
        r108 = r108 + 1;
        goto L_0x0718;
    L_0x0757:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0773;
    L_0x0764:
        r130 = 1;
    L_0x0766:
        r0 = r136;
        r1 = r130;
        r0.setShrinkRequestedState(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0773:
        r130 = 0;
        goto L_0x0766;
    L_0x0776:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r135 = r138.readInt();
        r114 = r138.readInt();
        r0 = r136;
        r1 = r49;
        r2 = r135;
        r3 = r114;
        r0.scheduleSendShrinkRequest(r1, r2, r3);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x079a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r114 = r138.readInt();
        r0 = r136;
        r1 = r49;
        r2 = r114;
        r0.scheduleSendExpandRequest(r1, r2);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x07b8:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x07eb;
    L_0x07c5:
        r132 = 1;
    L_0x07c7:
        r120 = r138.readInt();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x07ee;
    L_0x07d1:
        r5 = android.app.ProfilerInfo.CREATOR;
        r0 = r138;
        r5 = r5.createFromParcel(r0);
        r5 = (android.app.ProfilerInfo) r5;
        r28 = r5;
    L_0x07dd:
        r0 = r136;
        r1 = r132;
        r2 = r28;
        r3 = r120;
        r0.profilerControl(r1, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x07eb:
        r132 = 0;
        goto L_0x07c7;
    L_0x07ee:
        r28 = 0;
        goto L_0x07dd;
    L_0x07f1:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r107 = r138.readInt();
        r0 = r136;
        r1 = r107;
        r0.setSchedulingGroup(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0806:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.pm.ApplicationInfo.CREATOR;
        r0 = r138;
        r97 = r5.createFromParcel(r0);
        r97 = (android.content.pm.ApplicationInfo) r97;
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r98 = r138.readInt();
        r0 = r136;
        r1 = r97;
        r2 = r18;
        r3 = r98;
        r0.scheduleCreateBackupAgent(r1, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x0833:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.pm.ApplicationInfo.CREATOR;
        r0 = r138;
        r97 = r5.createFromParcel(r0);
        r97 = (android.content.pm.ApplicationInfo) r97;
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r18 = r5.createFromParcel(r0);
        r18 = (android.content.res.CompatibilityInfo) r18;
        r0 = r136;
        r1 = r97;
        r2 = r18;
        r0.scheduleDestroyBackupAgent(r1, r2);
        r5 = 1;
        goto L_0x0007;
    L_0x085a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r100 = r138.readInt();
        r116 = r138.readStringArray();
        r0 = r136;
        r1 = r100;
        r2 = r116;
        r0.dispatchPackageBroadcast(r1, r2);
        r5 = 1;
        goto L_0x0007;
    L_0x0875:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r113 = r138.readString();
        r0 = r136;
        r1 = r113;
        r0.scheduleCrash(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x088a:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x08bd;
    L_0x0897:
        r112 = 1;
    L_0x0899:
        r117 = r138.readString();
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x08c0;
    L_0x08a3:
        r5 = android.os.ParcelFileDescriptor.CREATOR;
        r0 = r138;
        r5 = r5.createFromParcel(r0);
        r5 = (android.os.ParcelFileDescriptor) r5;
        r104 = r5;
    L_0x08af:
        r0 = r136;
        r1 = r112;
        r2 = r117;
        r3 = r104;
        r0.dumpHeap(r1, r2, r3);
        r5 = 1;
        goto L_0x0007;
    L_0x08bd:
        r112 = 0;
        goto L_0x0899;
    L_0x08c0:
        r104 = 0;
        goto L_0x08af;
    L_0x08c3:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r95 = r138.readStrongBinder();
        r119 = r138.readString();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x08ee;
    L_0x08dc:
        r5 = r104.getFileDescriptor();
        r0 = r136;
        r1 = r95;
        r2 = r119;
        r3 = r53;
        r0.dumpActivity(r5, r1, r2, r3);
        r104.close();	 Catch:{ IOException -> 0x0b77 }
    L_0x08ee:
        r5 = 1;
        goto L_0x0007;
    L_0x08f1:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r128 = r138.readBundle();
        r0 = r136;
        r1 = r128;
        r0.setCoreSettings(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0906:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r118 = r138.readString();
        r5 = android.content.res.CompatibilityInfo.CREATOR;
        r0 = r138;
        r101 = r5.createFromParcel(r0);
        r101 = (android.content.res.CompatibilityInfo) r101;
        r0 = r136;
        r1 = r118;
        r2 = r101;
        r0.updatePackageCompatibilityInfo(r1, r2);
        r5 = 1;
        goto L_0x0007;
    L_0x0927:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r111 = r138.readInt();
        r0 = r136;
        r1 = r111;
        r0.scheduleTrimMemory(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x093c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r5 = android.os.Debug.MemoryInfo.CREATOR;
        r0 = r138;
        r89 = r5.createFromParcel(r0);
        r89 = (android.os.Debug.MemoryInfo) r89;
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x098b;
    L_0x0957:
        r90 = 1;
    L_0x0959:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x098e;
    L_0x095f:
        r91 = 1;
    L_0x0961:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0991;
    L_0x0967:
        r92 = 1;
    L_0x0969:
        r5 = r138.readInt();
        if (r5 == 0) goto L_0x0994;
    L_0x096f:
        r93 = 1;
    L_0x0971:
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x0985;
    L_0x0977:
        r88 = r104.getFileDescriptor();	 Catch:{ all -> 0x0997 }
        r87 = r136;	 Catch:{ all -> 0x0997 }
        r94 = r53;	 Catch:{ all -> 0x0997 }
        r87.dumpMemInfo(r88, r89, r90, r91, r92, r93, r94);	 Catch:{ all -> 0x0997 }
        r104.close();
    L_0x0985:
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x098b:
        r90 = 0;
        goto L_0x0959;
    L_0x098e:
        r91 = 0;
        goto L_0x0961;
    L_0x0991:
        r92 = 0;
        goto L_0x0969;
    L_0x0994:
        r93 = 0;
        goto L_0x0971;
    L_0x0997:
        r5 = move-exception;
        r104.close();	 Catch:{ IOException -> 0x0b7d }
    L_0x099b:
        throw r5;
    L_0x099c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x09bb;
    L_0x09ad:
        r5 = r104.getFileDescriptor();	 Catch:{ all -> 0x09c1 }
        r0 = r136;	 Catch:{ all -> 0x09c1 }
        r1 = r53;	 Catch:{ all -> 0x09c1 }
        r0.dumpGfxInfo(r5, r1);	 Catch:{ all -> 0x09c1 }
        r104.close();
    L_0x09bb:
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x09c1:
        r5 = move-exception;
        r104.close();	 Catch:{ IOException -> 0x0b83 }
    L_0x09c5:
        throw r5;
    L_0x09c6:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r104 = r138.readFileDescriptor();
        r53 = r138.readStringArray();
        if (r104 == 0) goto L_0x09e5;
    L_0x09d7:
        r5 = r104.getFileDescriptor();	 Catch:{ all -> 0x09eb }
        r0 = r136;	 Catch:{ all -> 0x09eb }
        r1 = r53;	 Catch:{ all -> 0x09eb }
        r0.dumpDbInfo(r5, r1);	 Catch:{ all -> 0x09eb }
        r104.close();
    L_0x09e5:
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x09eb:
        r5 = move-exception;
        r104.close();	 Catch:{ IOException -> 0x0b89 }
    L_0x09ef:
        throw r5;
    L_0x09f0:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r121 = r138.readStrongBinder();
        r0 = r136;
        r1 = r121;
        r0.unstableProviderDied(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0a08:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r96 = r138.readStrongBinder();
        r124 = r138.readStrongBinder();
        r125 = r138.readInt();
        r0 = r136;
        r1 = r96;
        r2 = r124;
        r3 = r125;
        r0.requestAssistContextExtras(r1, r2, r3);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0a2c:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = r138.readInt();
        r11 = 1;
        if (r5 != r11) goto L_0x0a4f;
    L_0x0a3e:
        r134 = 1;
    L_0x0a40:
        r0 = r136;
        r1 = r49;
        r2 = r134;
        r0.scheduleTranslucentConversionComplete(r1, r2);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0a4f:
        r134 = 0;
        goto L_0x0a40;
    L_0x0a52:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r115 = new android.app.ActivityOptions;
        r5 = r138.readBundle();
        r0 = r115;
        r0.<init>(r5);
        r0 = r136;
        r1 = r49;
        r2 = r115;
        r0.scheduleOnNewActivityOptions(r1, r2);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0a77:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r22 = r138.readInt();
        r0 = r136;
        r1 = r22;
        r0.setProcessState(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0a8f:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.content.pm.ProviderInfo.CREATOR;
        r0 = r138;
        r121 = r5.createFromParcel(r0);
        r121 = (android.content.pm.ProviderInfo) r121;
        r0 = r136;
        r1 = r121;
        r0.scheduleInstallProvider(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0aad:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r96 = r138.readStrongBinder();
        r113 = r138.readString();
        r0 = r136;
        r1 = r96;
        r2 = r113;
        r0.scheduleResetTargetHeapUtilization(r1, r2);
        r5 = 1;
        goto L_0x0007;
    L_0x0ac8:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r5 = android.view.DisplayInfo.CREATOR;
        r0 = r138;
        r102 = r5.createFromParcel(r0);
        r102 = (android.view.DisplayInfo) r102;
        r0 = r136;
        r1 = r102;
        r0.scheduleDisplayMetricsChanged(r1);
        r5 = 1;
        goto L_0x0007;
    L_0x0ae3:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r109 = r138.readByte();
        r5 = 1;
        r0 = r109;
        if (r0 != r5) goto L_0x0aff;
    L_0x0af3:
        r5 = 1;
    L_0x0af4:
        r0 = r136;
        r0.updateTimePrefs(r5);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0aff:
        r5 = 0;
        goto L_0x0af4;
    L_0x0b01:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r0 = r136;
        r1 = r49;
        r0.scheduleCancelVisibleBehind(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0b19:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r5 = r138.readInt();
        if (r5 <= 0) goto L_0x0b3b;
    L_0x0b2a:
        r103 = 1;
    L_0x0b2c:
        r0 = r136;
        r1 = r49;
        r2 = r103;
        r0.scheduleBackgroundVisibleBehindChanged(r1, r2);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0b3b:
        r103 = 0;
        goto L_0x0b2c;
    L_0x0b3e:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r49 = r138.readStrongBinder();
        r0 = r136;
        r1 = r49;
        r0.scheduleEnterAnimationComplete(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0b56:
        r5 = "android.app.IApplicationThread";
        r0 = r138;
        r0.enforceInterface(r5);
        r106 = r138.createByteArray();
        r0 = r136;
        r1 = r106;
        r0.notifyCleartextNetwork(r1);
        r139.writeNoException();
        r5 = 1;
        goto L_0x0007;
    L_0x0b6e:
        r5 = move-exception;
        goto L_0x0586;
    L_0x0b71:
        r5 = move-exception;
        goto L_0x05ae;
    L_0x0b74:
        r5 = move-exception;
        goto L_0x0707;
    L_0x0b77:
        r5 = move-exception;
        goto L_0x08ee;
    L_0x0b7a:
        r5 = move-exception;
        goto L_0x0985;
    L_0x0b7d:
        r11 = move-exception;
        goto L_0x099b;
    L_0x0b80:
        r5 = move-exception;
        goto L_0x09bb;
    L_0x0b83:
        r11 = move-exception;
        goto L_0x09c5;
    L_0x0b86:
        r5 = move-exception;
        goto L_0x09e5;
    L_0x0b89:
        r11 = move-exception;
        goto L_0x09ef;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationThreadNative.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
    }

    public static IApplicationThread asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IApplicationThread in = (IApplicationThread) obj.queryLocalInterface(IApplicationThread.descriptor);
        return in == null ? new ApplicationThreadProxy(obj) : in;
    }

    public ApplicationThreadNative() {
        attachInterface(this, IApplicationThread.descriptor);
    }

    public IBinder asBinder() {
        return this;
    }
}
