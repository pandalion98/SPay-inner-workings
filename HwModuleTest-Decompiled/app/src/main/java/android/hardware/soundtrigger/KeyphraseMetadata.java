package android.hardware.soundtrigger;

import android.util.ArraySet;
import java.util.Locale;

public class KeyphraseMetadata {

    /* renamed from: id */
    public final int f0id;
    public final String keyphrase;
    public final int recognitionModeFlags;
    public final ArraySet<Locale> supportedLocales;

    public KeyphraseMetadata(int id, String keyphrase2, ArraySet<Locale> supportedLocales2, int recognitionModeFlags2) {
        this.f0id = id;
        this.keyphrase = keyphrase2;
        this.supportedLocales = supportedLocales2;
        this.recognitionModeFlags = recognitionModeFlags2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(this.f0id);
        sb.append(", keyphrase=");
        sb.append(this.keyphrase);
        sb.append(", supported-locales=");
        sb.append(this.supportedLocales);
        sb.append(", recognition-modes=");
        sb.append(this.recognitionModeFlags);
        return sb.toString();
    }

    public boolean supportsPhrase(String phrase) {
        return this.keyphrase.isEmpty() || this.keyphrase.equalsIgnoreCase(phrase);
    }

    public boolean supportsLocale(Locale locale) {
        return this.supportedLocales.isEmpty() || this.supportedLocales.contains(locale);
    }
}
