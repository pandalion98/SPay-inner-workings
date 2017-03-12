package android.hardware.soundtrigger;

import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import java.util.Arrays;
import java.util.Locale;

public class KeyphraseEnrollmentInfo {
    public static final String ACTION_MANAGE_VOICE_KEYPHRASES = "com.android.intent.action.MANAGE_VOICE_KEYPHRASES";
    public static final String EXTRA_VOICE_KEYPHRASE_ACTION = "com.android.intent.extra.VOICE_KEYPHRASE_ACTION";
    public static final String EXTRA_VOICE_KEYPHRASE_HINT_TEXT = "com.android.intent.extra.VOICE_KEYPHRASE_HINT_TEXT";
    public static final String EXTRA_VOICE_KEYPHRASE_LOCALE = "com.android.intent.extra.VOICE_KEYPHRASE_LOCALE";
    private static final String TAG = "KeyphraseEnrollmentInfo";
    private static final String VOICE_KEYPHRASE_META_DATA = "android.voice_enrollment";
    private String mEnrollmentPackage;
    private KeyphraseMetadata[] mKeyphrases;
    private String mParseError;

    public KeyphraseEnrollmentInfo(android.content.pm.PackageManager r17) {
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
        r16 = this;
        r16.<init>();
        r13 = new android.content.Intent;
        r14 = "com.android.intent.action.MANAGE_VOICE_KEYPHRASES";
        r13.<init>(r14);
        r14 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r0 = r17;
        r11 = r0.queryIntentActivities(r13, r14);
        if (r11 == 0) goto L_0x001a;
    L_0x0014:
        r13 = r11.isEmpty();
        if (r13 == 0) goto L_0x0021;
    L_0x001a:
        r13 = "No enrollment application found";
        r0 = r16;
        r0.mParseError = r13;
    L_0x0020:
        return;
    L_0x0021:
        r5 = 0;
        r1 = 0;
        r6 = r11.iterator();
    L_0x0027:
        r13 = r6.hasNext();
        if (r13 == 0) goto L_0x0096;
    L_0x002d:
        r10 = r6.next();
        r10 = (android.content.pm.ResolveInfo) r10;
        r13 = r10.activityInfo;	 Catch:{ NameNotFoundException -> 0x0061 }
        r13 = r13.packageName;	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = 128; // 0x80 float:1.794E-43 double:6.32E-322;	 Catch:{ NameNotFoundException -> 0x0061 }
        r0 = r17;	 Catch:{ NameNotFoundException -> 0x0061 }
        r1 = r0.getApplicationInfo(r13, r14);	 Catch:{ NameNotFoundException -> 0x0061 }
        r13 = r1.privateFlags;	 Catch:{ NameNotFoundException -> 0x0061 }
        r13 = r13 & 8;	 Catch:{ NameNotFoundException -> 0x0061 }
        if (r13 != 0) goto L_0x006a;	 Catch:{ NameNotFoundException -> 0x0061 }
    L_0x0045:
        r13 = "KeyphraseEnrollmentInfo";	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0061 }
        r14.<init>();	 Catch:{ NameNotFoundException -> 0x0061 }
        r15 = r1.packageName;	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.append(r15);	 Catch:{ NameNotFoundException -> 0x0061 }
        r15 = "is not a privileged system app";	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.append(r15);	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.toString();	 Catch:{ NameNotFoundException -> 0x0061 }
        android.util.Slog.w(r13, r14);	 Catch:{ NameNotFoundException -> 0x0061 }
        goto L_0x0027;
    L_0x0061:
        r4 = move-exception;
        r13 = "KeyphraseEnrollmentInfo";
        r14 = "error parsing voice enrollment meta-data";
        android.util.Slog.w(r13, r14, r4);
        goto L_0x0027;
    L_0x006a:
        r13 = "android.permission.MANAGE_VOICE_KEYPHRASES";	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r1.permission;	 Catch:{ NameNotFoundException -> 0x0061 }
        r13 = r13.equals(r14);	 Catch:{ NameNotFoundException -> 0x0061 }
        if (r13 != 0) goto L_0x008f;	 Catch:{ NameNotFoundException -> 0x0061 }
    L_0x0074:
        r13 = "KeyphraseEnrollmentInfo";	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0061 }
        r14.<init>();	 Catch:{ NameNotFoundException -> 0x0061 }
        r15 = r1.packageName;	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.append(r15);	 Catch:{ NameNotFoundException -> 0x0061 }
        r15 = " does not require MANAGE_VOICE_KEYPHRASES";	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.append(r15);	 Catch:{ NameNotFoundException -> 0x0061 }
        r14 = r14.toString();	 Catch:{ NameNotFoundException -> 0x0061 }
        android.util.Slog.w(r13, r14);	 Catch:{ NameNotFoundException -> 0x0061 }
        goto L_0x0027;	 Catch:{ NameNotFoundException -> 0x0061 }
    L_0x008f:
        r13 = r1.packageName;	 Catch:{ NameNotFoundException -> 0x0061 }
        r0 = r16;	 Catch:{ NameNotFoundException -> 0x0061 }
        r0.mEnrollmentPackage = r13;	 Catch:{ NameNotFoundException -> 0x0061 }
        r5 = 1;
    L_0x0096:
        if (r5 != 0) goto L_0x00a5;
    L_0x0098:
        r13 = 0;
        r0 = r16;
        r0.mKeyphrases = r13;
        r13 = "No suitable enrollment application found";
        r0 = r16;
        r0.mParseError = r13;
        goto L_0x0020;
    L_0x00a5:
        r8 = 0;
        r13 = "android.voice_enrollment";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r17;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r8 = r1.loadXmlMetaData(r0, r13);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 != 0) goto L_0x00d0;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
    L_0x00b0:
        r13 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "No android.voice_enrollment meta-data for ";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = r1.packageName;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.toString();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.mParseError = r13;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x00cb:
        r8.close();
        goto L_0x0020;
    L_0x00d0:
        r0 = r17;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r9 = r0.getResourcesForApplication(r1);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r3 = android.util.Xml.asAttributeSet(r8);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
    L_0x00da:
        r12 = r8.next();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = 1;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r12 == r13) goto L_0x00e4;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
    L_0x00e1:
        r13 = 2;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r12 != r13) goto L_0x00da;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
    L_0x00e4:
        r7 = r8.getName();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = "voice-enrollment-application";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.equals(r7);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r13 != 0) goto L_0x00fe;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
    L_0x00f1:
        r13 = "Meta-data does not start with voice-enrollment-application tag";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.mParseError = r13;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x00f9:
        r8.close();
        goto L_0x0020;
    L_0x00fe:
        r13 = com.android.internal.R.styleable.VoiceEnrollmentApplication;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r2 = r9.obtainAttributes(r3, r13);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.initializeKeyphrasesFromTypedArray(r2);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r2.recycle();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x010e:
        r8.close();
        goto L_0x0020;
    L_0x0113:
        r4 = move-exception;
        r13 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "Error parsing keyphrase enrollment meta-data: ";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.toString();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.mParseError = r13;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = "KeyphraseEnrollmentInfo";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "error parsing keyphrase enrollment meta-data";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        android.util.Slog.w(r13, r14, r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x0134:
        r8.close();
        goto L_0x0020;
    L_0x0139:
        r4 = move-exception;
        r13 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "Error parsing keyphrase enrollment meta-data: ";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.toString();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.mParseError = r13;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = "KeyphraseEnrollmentInfo";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "error parsing keyphrase enrollment meta-data";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        android.util.Slog.w(r13, r14, r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x015a:
        r8.close();
        goto L_0x0020;
    L_0x015f:
        r4 = move-exception;
        r13 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "Error parsing keyphrase enrollment meta-data: ";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r14);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.append(r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = r13.toString();	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0 = r16;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r0.mParseError = r13;	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r13 = "KeyphraseEnrollmentInfo";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        r14 = "error parsing keyphrase enrollment meta-data";	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        android.util.Slog.w(r13, r14, r4);	 Catch:{ XmlPullParserException -> 0x0113, IOException -> 0x0139, NameNotFoundException -> 0x015f, all -> 0x0185 }
        if (r8 == 0) goto L_0x0020;
    L_0x0180:
        r8.close();
        goto L_0x0020;
    L_0x0185:
        r13 = move-exception;
        if (r8 == 0) goto L_0x018b;
    L_0x0188:
        r8.close();
    L_0x018b:
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.soundtrigger.KeyphraseEnrollmentInfo.<init>(android.content.pm.PackageManager):void");
    }

    private void initializeKeyphrasesFromTypedArray(TypedArray array) {
        int searchKeyphraseId = array.getInt(0, -1);
        if (searchKeyphraseId <= 0) {
            this.mParseError = "No valid searchKeyphraseId specified in meta-data";
            Slog.w(TAG, this.mParseError);
            return;
        }
        String searchKeyphrase = array.getString(1);
        if (searchKeyphrase == null) {
            this.mParseError = "No valid searchKeyphrase specified in meta-data";
            Slog.w(TAG, this.mParseError);
            return;
        }
        String searchKeyphraseSupportedLocales = array.getString(2);
        if (searchKeyphraseSupportedLocales == null) {
            this.mParseError = "No valid searchKeyphraseSupportedLocales specified in meta-data";
            Slog.w(TAG, this.mParseError);
            return;
        }
        ArraySet<Locale> locales = new ArraySet();
        if (!TextUtils.isEmpty(searchKeyphraseSupportedLocales)) {
            try {
                String[] supportedLocalesDelimited = searchKeyphraseSupportedLocales.split(",");
                for (String forLanguageTag : supportedLocalesDelimited) {
                    locales.add(Locale.forLanguageTag(forLanguageTag));
                }
            } catch (Exception ex) {
                this.mParseError = "Error reading searchKeyphraseSupportedLocales from meta-data";
                Slog.w(TAG, this.mParseError, ex);
                return;
            }
        }
        int recognitionModes = array.getInt(3, -1);
        if (recognitionModes < 0) {
            this.mParseError = "No valid searchKeyphraseRecognitionFlags specified in meta-data";
            Slog.w(TAG, this.mParseError);
            return;
        }
        this.mKeyphrases = new KeyphraseMetadata[1];
        this.mKeyphrases[0] = new KeyphraseMetadata(searchKeyphraseId, searchKeyphrase, locales, recognitionModes);
    }

    public String getParseError() {
        return this.mParseError;
    }

    public KeyphraseMetadata[] listKeyphraseMetadata() {
        return this.mKeyphrases;
    }

    public Intent getManageKeyphraseIntent(int action, String keyphrase, Locale locale) {
        if (this.mEnrollmentPackage == null || this.mEnrollmentPackage.isEmpty()) {
            Slog.w(TAG, "No enrollment application exists");
            return null;
        } else if (getKeyphraseMetadata(keyphrase, locale) != null) {
            return new Intent(ACTION_MANAGE_VOICE_KEYPHRASES).setPackage(this.mEnrollmentPackage).putExtra(EXTRA_VOICE_KEYPHRASE_HINT_TEXT, keyphrase).putExtra(EXTRA_VOICE_KEYPHRASE_LOCALE, locale.toLanguageTag()).putExtra(EXTRA_VOICE_KEYPHRASE_ACTION, action);
        } else {
            return null;
        }
    }

    public KeyphraseMetadata getKeyphraseMetadata(String keyphrase, Locale locale) {
        if (this.mKeyphrases == null || this.mKeyphrases.length == 0) {
            Slog.w(TAG, "Enrollment application doesn't support keyphrases");
            return null;
        }
        for (KeyphraseMetadata keyphraseMetadata : this.mKeyphrases) {
            if (keyphraseMetadata.supportsPhrase(keyphrase) && keyphraseMetadata.supportsLocale(locale)) {
                return keyphraseMetadata;
            }
        }
        Slog.w(TAG, "Enrollment application doesn't support the given keyphrase/locale");
        return null;
    }

    public String toString() {
        return "KeyphraseEnrollmentInfo [Keyphrases=" + Arrays.toString(this.mKeyphrases) + ", EnrollmentPackage=" + this.mEnrollmentPackage + ", ParseError=" + this.mParseError + "]";
    }
}
