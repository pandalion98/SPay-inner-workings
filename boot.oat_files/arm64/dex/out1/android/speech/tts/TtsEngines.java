package android.speech.tts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.provider.Settings.Secure;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class TtsEngines {
    private static final boolean DBG = false;
    private static final String LOCALE_DELIMITER_NEW = "_";
    private static final String LOCALE_DELIMITER_OLD = "-";
    private static final String TAG = "TtsEngines";
    private static final String XML_TAG_NAME = "tts-engine";
    private static final Map<String, String> sNormalizeCountry;
    private static final Map<String, String> sNormalizeLanguage;
    private final Context mContext;

    private static class EngineInfoComparator implements Comparator<EngineInfo> {
        static EngineInfoComparator INSTANCE = new EngineInfoComparator();

        private EngineInfoComparator() {
        }

        public int compare(EngineInfo lhs, EngineInfo rhs) {
            if (lhs.system && !rhs.system) {
                return -1;
            }
            if (!rhs.system || lhs.system) {
                return rhs.priority - lhs.priority;
            }
            return 1;
        }
    }

    private java.lang.String settingsActivityFromServiceInfo(android.content.pm.ServiceInfo r12, android.content.pm.PackageManager r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x00b0 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
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
        r11 = this;
        r7 = 0;
        r3 = 0;
        r8 = "android.speech.tts";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r3 = r12.loadXmlMetaData(r13, r8);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 != 0) goto L_0x0029;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
    L_0x000a:
        r8 = "TtsEngines";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9.<init>();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = "No meta-data found for :";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r12);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.toString();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        android.util.Log.w(r8, r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x0027;
    L_0x0024:
        r3.close();
    L_0x0027:
        r5 = r7;
    L_0x0028:
        return r5;
    L_0x0029:
        r8 = r12.applicationInfo;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r4 = r13.getResourcesForApplication(r8);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
    L_0x002f:
        r6 = r3.next();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r8 = 1;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r6 == r8) goto L_0x008b;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
    L_0x0036:
        r8 = 2;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r6 != r8) goto L_0x002f;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
    L_0x0039:
        r8 = "tts-engine";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r3.getName();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r8 = r8.equals(r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r8 != 0) goto L_0x0073;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
    L_0x0046:
        r8 = "TtsEngines";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9.<init>();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = "Package ";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r12);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = " uses unknown tag :";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = r3.getName();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.toString();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        android.util.Log.w(r8, r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x0071;
    L_0x006e:
        r3.close();
    L_0x0071:
        r5 = r7;
        goto L_0x0028;
    L_0x0073:
        r1 = android.util.Xml.asAttributeSet(r3);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r8 = com.android.internal.R.styleable.TextToSpeechEngine;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r0 = r4.obtainAttributes(r1, r8);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r8 = 0;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r5 = r0.getString(r8);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r0.recycle();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x0028;
    L_0x0087:
        r3.close();
        goto L_0x0028;
    L_0x008b:
        if (r3 == 0) goto L_0x0090;
    L_0x008d:
        r3.close();
    L_0x0090:
        r5 = r7;
        goto L_0x0028;
    L_0x0092:
        r2 = move-exception;
        r8 = "TtsEngines";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9.<init>();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = "Could not load resources for : ";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r12);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.toString();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        android.util.Log.w(r8, r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x00b0;
    L_0x00ad:
        r3.close();
    L_0x00b0:
        r5 = r7;
        goto L_0x0028;
    L_0x00b3:
        r2 = move-exception;
        r8 = "TtsEngines";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9.<init>();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = "Error parsing metadata for ";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r12);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = ":";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r2);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.toString();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        android.util.Log.w(r8, r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x00db;
    L_0x00d8:
        r3.close();
    L_0x00db:
        r5 = r7;
        goto L_0x0028;
    L_0x00de:
        r2 = move-exception;
        r8 = "TtsEngines";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9.<init>();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = "Error parsing metadata for ";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r12);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r10 = ":";	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r10);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.append(r2);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        r9 = r9.toString();	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        android.util.Log.w(r8, r9);	 Catch:{ NameNotFoundException -> 0x0092, XmlPullParserException -> 0x00b3, IOException -> 0x00de, all -> 0x0109 }
        if (r3 == 0) goto L_0x0106;
    L_0x0103:
        r3.close();
    L_0x0106:
        r5 = r7;
        goto L_0x0028;
    L_0x0109:
        r7 = move-exception;
        if (r3 == 0) goto L_0x010f;
    L_0x010c:
        r3.close();
    L_0x010f:
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.TtsEngines.settingsActivityFromServiceInfo(android.content.pm.ServiceInfo, android.content.pm.PackageManager):java.lang.String");
    }

    static {
        HashMap<String, String> normalizeLanguage = new HashMap();
        for (String language : Locale.getISOLanguages()) {
            try {
                normalizeLanguage.put(new Locale(language).getISO3Language(), language);
            } catch (MissingResourceException e) {
            }
        }
        sNormalizeLanguage = Collections.unmodifiableMap(normalizeLanguage);
        HashMap<String, String> normalizeCountry = new HashMap();
        for (String country : Locale.getISOCountries()) {
            try {
                normalizeCountry.put(new Locale("", country).getISO3Country(), country);
            } catch (MissingResourceException e2) {
            }
        }
        sNormalizeCountry = Collections.unmodifiableMap(normalizeCountry);
    }

    public TtsEngines(Context ctx) {
        this.mContext = ctx;
    }

    public String getDefaultEngine() {
        String engine = Secure.getString(this.mContext.getContentResolver(), "tts_default_synth");
        return isEngineInstalled(engine) ? engine : getHighestRankedEngineName();
    }

    public String getHighestRankedEngineName() {
        List<EngineInfo> engines = getEngines();
        if (engines.size() <= 0 || !((EngineInfo) engines.get(0)).system) {
            return null;
        }
        return ((EngineInfo) engines.get(0)).name;
    }

    public EngineInfo getEngineInfo(String packageName) {
        PackageManager pm = this.mContext.getPackageManager();
        Intent intent = new Intent(Engine.INTENT_ACTION_TTS_SERVICE);
        intent.setPackage(packageName);
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 65536);
        if (resolveInfos == null || resolveInfos.size() != 1) {
            return null;
        }
        return getEngineInfo((ResolveInfo) resolveInfos.get(0), pm);
    }

    public List<EngineInfo> getEngines() {
        PackageManager pm = this.mContext.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(new Intent(Engine.INTENT_ACTION_TTS_SERVICE), 65536);
        if (resolveInfos == null) {
            return Collections.emptyList();
        }
        List<EngineInfo> engines = new ArrayList(resolveInfos.size());
        for (ResolveInfo resolveInfo : resolveInfos) {
            EngineInfo engine = getEngineInfo(resolveInfo, pm);
            if (engine != null) {
                engines.add(engine);
            }
        }
        Collections.sort(engines, EngineInfoComparator.INSTANCE);
        return engines;
    }

    private boolean isSystemEngine(ServiceInfo info) {
        ApplicationInfo appInfo = info.applicationInfo;
        return (appInfo == null || (appInfo.flags & 1) == 0) ? false : true;
    }

    public boolean isEngineInstalled(String engine) {
        if (engine == null || getEngineInfo(engine) == null) {
            return false;
        }
        return true;
    }

    public Intent getSettingsIntent(String engine) {
        PackageManager pm = this.mContext.getPackageManager();
        Intent intent = new Intent(Engine.INTENT_ACTION_TTS_SERVICE);
        intent.setPackage(engine);
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 65664);
        if (resolveInfos != null && resolveInfos.size() == 1) {
            ServiceInfo service = ((ResolveInfo) resolveInfos.get(0)).serviceInfo;
            if (service != null) {
                String settings = settingsActivityFromServiceInfo(service, pm);
                if (settings != null) {
                    Intent i = new Intent();
                    i.setClassName(engine, settings);
                    return i;
                }
            }
        }
        return null;
    }

    private EngineInfo getEngineInfo(ResolveInfo resolve, PackageManager pm) {
        ServiceInfo service = resolve.serviceInfo;
        if (service == null) {
            return null;
        }
        EngineInfo engine = new EngineInfo();
        engine.name = service.packageName;
        CharSequence label = service.loadLabel(pm);
        engine.label = TextUtils.isEmpty(label) ? engine.name : label.toString();
        engine.icon = service.getIconResource();
        engine.priority = resolve.priority;
        engine.system = isSystemEngine(service);
        return engine;
    }

    public Locale getLocalePrefForEngine(String engineName) {
        return getLocalePrefForEngine(engineName, Secure.getString(this.mContext.getContentResolver(), "tts_default_locale"));
    }

    public Locale getLocalePrefForEngine(String engineName, String prefValue) {
        String localeString = parseEnginePrefFromList(prefValue, engineName);
        if (TextUtils.isEmpty(localeString)) {
            return Locale.getDefault();
        }
        if (!"com.google.android.tts".equalsIgnoreCase(engineName) && "spa-USA".equalsIgnoreCase(localeString)) {
            localeString = "spa-MEX";
        }
        Locale result = parseLocaleString(localeString);
        if (result != null) {
            return result;
        }
        Log.w(TAG, "Failed to parse locale " + localeString + ", returning en_US instead");
        return Locale.US;
    }

    public boolean isLocaleSetToDefaultForEngine(String engineName) {
        return TextUtils.isEmpty(parseEnginePrefFromList(Secure.getString(this.mContext.getContentResolver(), "tts_default_locale"), engineName));
    }

    public Locale parseLocaleString(String localeString) {
        String language = "";
        String country = "";
        String variant = "";
        if (!TextUtils.isEmpty(localeString)) {
            String[] split = localeString.split("[-_]");
            language = split[0].toLowerCase();
            if (split.length == 0) {
                Log.w(TAG, "Failed to convert " + localeString + " to a valid Locale object. Only" + " separators");
                return null;
            } else if (split.length > 3) {
                Log.w(TAG, "Failed to convert " + localeString + " to a valid Locale object. Too" + " many separators");
                return null;
            } else {
                if (split.length >= 2) {
                    country = split[1].toUpperCase();
                }
                if (split.length >= 3) {
                    variant = split[2];
                }
            }
        }
        String normalizedLanguage = (String) sNormalizeLanguage.get(language);
        if (normalizedLanguage != null) {
            language = normalizedLanguage;
        }
        String normalizedCountry = (String) sNormalizeCountry.get(country);
        if (normalizedCountry != null) {
            country = normalizedCountry;
        }
        Locale result = new Locale(language, country, variant);
        try {
            result.getISO3Language();
            result.getISO3Country();
            return result;
        } catch (MissingResourceException e) {
            Log.w(TAG, "Failed to convert " + localeString + " to a valid Locale object.");
            return null;
        }
    }

    public static Locale normalizeTTSLocale(Locale ttsLocale) {
        String language = ttsLocale.getLanguage();
        if (!TextUtils.isEmpty(language)) {
            String normalizedLanguage = (String) sNormalizeLanguage.get(language);
            if (normalizedLanguage != null) {
                language = normalizedLanguage;
            }
        }
        String country = ttsLocale.getCountry();
        if (!TextUtils.isEmpty(country)) {
            String normalizedCountry = (String) sNormalizeCountry.get(country);
            if (normalizedCountry != null) {
                country = normalizedCountry;
            }
        }
        return new Locale(language, country, ttsLocale.getVariant());
    }

    public static String[] toOldLocaleStringFormat(Locale locale) {
        String[] ret = new String[]{"", "", ""};
        try {
            ret[0] = locale.getISO3Language();
            ret[1] = locale.getISO3Country();
            ret[2] = locale.getVariant();
            return ret;
        } catch (MissingResourceException e) {
            return new String[]{"eng", "USA", ""};
        }
    }

    private static String parseEnginePrefFromList(String prefValue, String engineName) {
        if (TextUtils.isEmpty(prefValue)) {
            return null;
        }
        for (String value : prefValue.split(FingerprintManager.FINGER_PERMISSION_DELIMITER)) {
            int delimiter = value.indexOf(58);
            if (delimiter > 0 && engineName.equals(value.substring(0, delimiter))) {
                return value.substring(delimiter + 1);
            }
        }
        return null;
    }

    public synchronized void updateLocalePrefForEngine(String engineName, Locale newLocale) {
        Secure.putString(this.mContext.getContentResolver(), "tts_default_locale", updateValueInCommaSeparatedList(Secure.getString(this.mContext.getContentResolver(), "tts_default_locale"), engineName, newLocale != null ? newLocale.toString() : "").toString());
    }

    private String updateValueInCommaSeparatedList(String list, String key, String newValue) {
        StringBuilder newPrefList = new StringBuilder();
        if (TextUtils.isEmpty(list)) {
            newPrefList.append(key).append(':').append(newValue);
        } else {
            boolean first = true;
            boolean found = false;
            for (String value : list.split(FingerprintManager.FINGER_PERMISSION_DELIMITER)) {
                int delimiter = value.indexOf(58);
                if (delimiter > 0) {
                    if (key.equals(value.substring(0, delimiter))) {
                        if (first) {
                            first = false;
                        } else {
                            newPrefList.append(',');
                        }
                        found = true;
                        newPrefList.append(key).append(':').append(newValue);
                    } else {
                        if (first) {
                            first = false;
                        } else {
                            newPrefList.append(',');
                        }
                        newPrefList.append(value);
                    }
                }
            }
            if (!found) {
                newPrefList.append(',');
                newPrefList.append(key).append(':').append(newValue);
            }
        }
        return newPrefList.toString();
    }
}
