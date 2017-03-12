package android.text;

import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Locale;
import java.util.Locale.Builder;

public class Hyphenator {
    private static final String[][] LOCALE_FALLBACK_DATA;
    private static String TAG = "Hyphenator";
    static final Hyphenator sEmptyHyphenator = new Hyphenator(StaticLayout.nLoadHyphenator(null, 0), null);
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    static final HashMap<Locale, Hyphenator> sMap = new HashMap();
    private final ByteBuffer mBuffer;
    private final long mNativePtr;

    static {
        String[][] strArr = new String[13][];
        strArr[0] = new String[]{"en-AS", "en-US"};
        strArr[1] = new String[]{"en-GU", "en-US"};
        strArr[2] = new String[]{"en-MH", "en-US"};
        strArr[3] = new String[]{"en-MP", "en-US"};
        strArr[4] = new String[]{"en-PR", "en-US"};
        strArr[5] = new String[]{"en-UM", "en-US"};
        strArr[6] = new String[]{"en-VI", "en-US"};
        strArr[7] = new String[]{"no", "nb"};
        strArr[8] = new String[]{"am", "und-Ethi"};
        strArr[9] = new String[]{"byn", "und-Ethi"};
        strArr[10] = new String[]{"gez", "und-Ethi"};
        strArr[11] = new String[]{"ti", "und-Ethi"};
        strArr[12] = new String[]{"wal", "und-Ethi"};
        LOCALE_FALLBACK_DATA = strArr;
    }

    private Hyphenator(long nativePtr, ByteBuffer b) {
        this.mNativePtr = nativePtr;
        this.mBuffer = b;
    }

    public long getNativePtr() {
        return this.mNativePtr;
    }

    public static Hyphenator get(Locale locale) {
        synchronized (sLock) {
            Hyphenator result = (Hyphenator) sMap.get(locale);
            if (result != null) {
                return result;
            }
            result = (Hyphenator) sMap.get(new Locale(locale.getLanguage()));
            if (result != null) {
                sMap.put(locale, result);
                return result;
            }
            String script = locale.getScript();
            if (!script.equals("")) {
                result = (Hyphenator) sMap.get(new Builder().setLanguage("und").setScript(script).build());
                if (result != null) {
                    sMap.put(locale, result);
                    return result;
                }
            }
            sMap.put(locale, sEmptyHyphenator);
            return sEmptyHyphenator;
        }
    }

    private static Hyphenator loadHyphenator(String languageTag) {
        File patternFile = new File(getSystemHyphenatorLocation(), "hyph-" + languageTag.toLowerCase(Locale.US) + ".hyb");
        RandomAccessFile f;
        try {
            f = new RandomAccessFile(patternFile, "r");
            FileChannel fc = f.getChannel();
            MappedByteBuffer buf = fc.map(MapMode.READ_ONLY, 0, fc.size());
            Hyphenator hyphenator = new Hyphenator(StaticLayout.nLoadHyphenator(buf, 0), buf);
            f.close();
            return hyphenator;
        } catch (IOException e) {
            Log.e(TAG, "error loading hyphenation " + patternFile, e);
            return null;
        } catch (Throwable th) {
            f.close();
        }
    }

    private static File getSystemHyphenatorLocation() {
        return new File("/system/usr/hyphen-data");
    }

    public static void init() {
        int i;
        sMap.put(null, null);
        String[] availableLanguages = new String[]{"en-US", "eu", "hu", "hy", "nb", "nn", "und-Ethi"};
        for (String languageTag : availableLanguages) {
            Hyphenator h = loadHyphenator(languageTag);
            if (h != null) {
                sMap.put(Locale.forLanguageTag(languageTag), h);
            }
        }
        for (i = 0; i < LOCALE_FALLBACK_DATA.length; i++) {
            sMap.put(Locale.forLanguageTag(LOCALE_FALLBACK_DATA[i][0]), sMap.get(Locale.forLanguageTag(LOCALE_FALLBACK_DATA[i][1])));
        }
    }
}
