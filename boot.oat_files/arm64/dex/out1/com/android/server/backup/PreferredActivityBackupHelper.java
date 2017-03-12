package com.android.server.backup;

import android.app.backup.BlobBackupHelper;

public class PreferredActivityBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_DEFAULT_APPS = "default-apps";
    private static final String KEY_INTENT_VERIFICATION = "intent-verification";
    private static final String KEY_PREFERRED = "preferred-activity";
    private static final int STATE_VERSION = 3;
    private static final String TAG = "PreferredBackup";

    public PreferredActivityBackupHelper() {
        super(3, new String[]{KEY_PREFERRED, KEY_DEFAULT_APPS, KEY_INTENT_VERIFICATION});
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected byte[] getBackupPayload(java.lang.String r6) {
        /*
        r5 = this;
        r2 = 0;
        r1 = android.app.AppGlobals.getPackageManager();
        r3 = -1;
        r4 = r6.hashCode();	 Catch:{ Exception -> 0x005b }
        switch(r4) {
            case -696985986: goto L_0x0035;
            case -429170260: goto L_0x003f;
            case 1336142555: goto L_0x002b;
            default: goto L_0x000d;
        };	 Catch:{ Exception -> 0x005b }
    L_0x000d:
        r2 = r3;
    L_0x000e:
        switch(r2) {
            case 0: goto L_0x0049;
            case 1: goto L_0x004f;
            case 2: goto L_0x0055;
            default: goto L_0x0011;
        };	 Catch:{ Exception -> 0x005b }
    L_0x0011:
        r2 = "PreferredBackup";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005b }
        r3.<init>();	 Catch:{ Exception -> 0x005b }
        r4 = "Unexpected backup key ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x005b }
        r3 = r3.append(r6);	 Catch:{ Exception -> 0x005b }
        r3 = r3.toString();	 Catch:{ Exception -> 0x005b }
        android.util.Slog.w(r2, r3);	 Catch:{ Exception -> 0x005b }
    L_0x0029:
        r2 = 0;
    L_0x002a:
        return r2;
    L_0x002b:
        r4 = "preferred-activity";
        r4 = r6.equals(r4);	 Catch:{ Exception -> 0x005b }
        if (r4 == 0) goto L_0x000d;
    L_0x0034:
        goto L_0x000e;
    L_0x0035:
        r2 = "default-apps";
        r2 = r6.equals(r2);	 Catch:{ Exception -> 0x005b }
        if (r2 == 0) goto L_0x000d;
    L_0x003d:
        r2 = 1;
        goto L_0x000e;
    L_0x003f:
        r2 = "intent-verification";
        r2 = r6.equals(r2);	 Catch:{ Exception -> 0x005b }
        if (r2 == 0) goto L_0x000d;
    L_0x0047:
        r2 = 2;
        goto L_0x000e;
    L_0x0049:
        r2 = 0;
        r2 = r1.getPreferredActivityBackup(r2);	 Catch:{ Exception -> 0x005b }
        goto L_0x002a;
    L_0x004f:
        r2 = 0;
        r2 = r1.getDefaultAppsBackup(r2);	 Catch:{ Exception -> 0x005b }
        goto L_0x002a;
    L_0x0055:
        r2 = 0;
        r2 = r1.getIntentFilterVerificationBackup(r2);	 Catch:{ Exception -> 0x005b }
        goto L_0x002a;
    L_0x005b:
        r0 = move-exception;
        r2 = "PreferredBackup";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unable to store payload ";
        r3 = r3.append(r4);
        r3 = r3.append(r6);
        r3 = r3.toString();
        android.util.Slog.e(r2, r3);
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.getBackupPayload(java.lang.String):byte[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void applyRestoredPayload(java.lang.String r6, byte[] r7) {
        /*
        r5 = this;
        r2 = 0;
        r1 = android.app.AppGlobals.getPackageManager();
        r3 = -1;
        r4 = r6.hashCode();	 Catch:{ Exception -> 0x004d }
        switch(r4) {
            case -696985986: goto L_0x0034;
            case -429170260: goto L_0x003e;
            case 1336142555: goto L_0x002a;
            default: goto L_0x000d;
        };	 Catch:{ Exception -> 0x004d }
    L_0x000d:
        r2 = r3;
    L_0x000e:
        switch(r2) {
            case 0: goto L_0x0048;
            case 1: goto L_0x0067;
            case 2: goto L_0x006c;
            default: goto L_0x0011;
        };	 Catch:{ Exception -> 0x004d }
    L_0x0011:
        r2 = "PreferredBackup";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004d }
        r3.<init>();	 Catch:{ Exception -> 0x004d }
        r4 = "Unexpected restore key ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x004d }
        r3 = r3.append(r6);	 Catch:{ Exception -> 0x004d }
        r3 = r3.toString();	 Catch:{ Exception -> 0x004d }
        android.util.Slog.w(r2, r3);	 Catch:{ Exception -> 0x004d }
    L_0x0029:
        return;
    L_0x002a:
        r4 = "preferred-activity";
        r4 = r6.equals(r4);	 Catch:{ Exception -> 0x004d }
        if (r4 == 0) goto L_0x000d;
    L_0x0033:
        goto L_0x000e;
    L_0x0034:
        r2 = "default-apps";
        r2 = r6.equals(r2);	 Catch:{ Exception -> 0x004d }
        if (r2 == 0) goto L_0x000d;
    L_0x003c:
        r2 = 1;
        goto L_0x000e;
    L_0x003e:
        r2 = "intent-verification";
        r2 = r6.equals(r2);	 Catch:{ Exception -> 0x004d }
        if (r2 == 0) goto L_0x000d;
    L_0x0046:
        r2 = 2;
        goto L_0x000e;
    L_0x0048:
        r2 = 0;
        r1.restorePreferredActivities(r7, r2);	 Catch:{ Exception -> 0x004d }
        goto L_0x0029;
    L_0x004d:
        r0 = move-exception;
        r2 = "PreferredBackup";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unable to restore key ";
        r3 = r3.append(r4);
        r3 = r3.append(r6);
        r3 = r3.toString();
        android.util.Slog.w(r2, r3);
        goto L_0x0029;
    L_0x0067:
        r2 = 0;
        r1.restoreDefaultApps(r7, r2);	 Catch:{ Exception -> 0x004d }
        goto L_0x0029;
    L_0x006c:
        r2 = 0;
        r1.restoreIntentFilterVerification(r7, r2);	 Catch:{ Exception -> 0x004d }
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.applyRestoredPayload(java.lang.String, byte[]):void");
    }
}
