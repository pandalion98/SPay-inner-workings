package android.service.voice;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;

public class VoiceInteractionServiceInfo {
    static final String TAG = "VoiceInteractionServiceInfo";
    private String mParseError;
    private String mRecognitionService;
    private ServiceInfo mServiceInfo;
    private String mSessionService;
    private String mSettingsActivity;
    private boolean mSupportsAssist;
    private boolean mSupportsLaunchFromKeyguard;

    public VoiceInteractionServiceInfo(android.content.pm.PackageManager r12, android.content.pm.ServiceInfo r13) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r10 = 2;
        r9 = 1;
        r11.<init>();
        if (r13 != 0) goto L_0x0013;
    L_0x0007:
        r7 = "Service info is null ";
        r11.mParseError = r7;
        r7 = "VoiceInteractionServiceInfo";
        r8 = "VoiceInteractionServiceInfo init error si is null";
        android.util.Log.e(r7, r8);
    L_0x0012:
        return;
    L_0x0013:
        r7 = "android.permission.BIND_VOICE_INTERACTION";
        r8 = r13.permission;
        r7 = r7.equals(r8);
        if (r7 != 0) goto L_0x0022;
    L_0x001d:
        r7 = "Service does not require permission android.permission.BIND_VOICE_INTERACTION";
        r11.mParseError = r7;
        goto L_0x0012;
    L_0x0022:
        r4 = 0;
        r7 = "android.voice_interaction";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r4 = r13.loadXmlMetaData(r12, r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 != 0) goto L_0x0048;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x002b:
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "No android.voice_interaction meta-data for ";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = r13.packageName;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.toString();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x0044:
        r4.close();
        goto L_0x0012;
    L_0x0048:
        r7 = r13.applicationInfo;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r5 = r12.getResourcesForApplication(r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r1 = android.util.Xml.asAttributeSet(r4);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x0052:
        r6 = r4.next();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r6 == r9) goto L_0x005a;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x0058:
        if (r6 != r10) goto L_0x0052;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x005a:
        r3 = r4.getName();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = "voice-interaction-service";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.equals(r3);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r7 != 0) goto L_0x0071;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x0067:
        r7 = "Meta-data does not start with voice-interaction-service tag";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x006d:
        r4.close();
        goto L_0x0012;
    L_0x0071:
        r7 = com.android.internal.R.styleable.VoiceInteractionService;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r0 = r5.obtainAttributes(r1, r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = 1;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r0.getString(r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mSessionService = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = 2;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r0.getString(r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mRecognitionService = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = 0;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r0.getString(r7);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mSettingsActivity = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = 3;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = 0;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r0.getBoolean(r7, r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mSupportsAssist = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = 4;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = 0;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r0.getBoolean(r7, r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mSupportsLaunchFromKeyguard = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r0.recycle();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r11.mSessionService;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r7 != 0) goto L_0x00ae;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x00a3:
        r7 = "No sessionService specified";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x00a9:
        r4.close();
        goto L_0x0012;
    L_0x00ae:
        r7 = r11.mRecognitionService;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r7 != 0) goto L_0x00bd;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
    L_0x00b2:
        r7 = "No recognitionService specified";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x00b8:
        r4.close();
        goto L_0x0012;
    L_0x00bd:
        if (r4 == 0) goto L_0x00c2;
    L_0x00bf:
        r4.close();
    L_0x00c2:
        r11.mServiceInfo = r13;
        goto L_0x0012;
    L_0x00c6:
        r2 = move-exception;
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "Error parsing voice interation service meta-data: ";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.toString();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = "VoiceInteractionServiceInfo";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "error parsing voice interaction service meta-data";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        android.util.Log.w(r7, r8, r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x00e5:
        r4.close();
        goto L_0x0012;
    L_0x00ea:
        r2 = move-exception;
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "Error parsing voice interation service meta-data: ";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.toString();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = "VoiceInteractionServiceInfo";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "error parsing voice interaction service meta-data";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        android.util.Log.w(r7, r8, r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x0109:
        r4.close();
        goto L_0x0012;
    L_0x010e:
        r2 = move-exception;
        r7 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7.<init>();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "Error parsing voice interation service meta-data: ";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r8);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.append(r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = r7.toString();	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r11.mParseError = r7;	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r7 = "VoiceInteractionServiceInfo";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        r8 = "error parsing voice interaction service meta-data";	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        android.util.Log.w(r7, r8, r2);	 Catch:{ XmlPullParserException -> 0x00c6, IOException -> 0x00ea, NameNotFoundException -> 0x010e, all -> 0x0132 }
        if (r4 == 0) goto L_0x0012;
    L_0x012d:
        r4.close();
        goto L_0x0012;
    L_0x0132:
        r7 = move-exception;
        if (r4 == 0) goto L_0x0138;
    L_0x0135:
        r4.close();
    L_0x0138:
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.voice.VoiceInteractionServiceInfo.<init>(android.content.pm.PackageManager, android.content.pm.ServiceInfo):void");
    }

    public VoiceInteractionServiceInfo(PackageManager pm, ComponentName comp) throws NameNotFoundException {
        this(pm, pm.getServiceInfo(comp, 128));
    }

    public VoiceInteractionServiceInfo(PackageManager pm, ComponentName comp, int userHandle) throws NameNotFoundException, RemoteException {
        this(pm, AppGlobals.getPackageManager().getServiceInfo(comp, 128, userHandle));
    }

    public String getParseError() {
        return this.mParseError;
    }

    public ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public String getSessionService() {
        return this.mSessionService;
    }

    public String getRecognitionService() {
        return this.mRecognitionService;
    }

    public String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public boolean getSupportsAssist() {
        return this.mSupportsAssist;
    }

    public boolean getSupportsLaunchFromKeyguard() {
        return this.mSupportsLaunchFromKeyguard;
    }
}
