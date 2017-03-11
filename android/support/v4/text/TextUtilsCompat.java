package android.support.v4.text;

import com.google.android.gms.location.places.Place;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Locale;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TextUtilsCompat {
    private static String ARAB_SCRIPT_SUBTAG;
    private static String HEBR_SCRIPT_SUBTAG;
    public static final Locale ROOT;

    public static String htmlEncode(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    stringBuilder.append("&quot;");
                    break;
                case Place.TYPE_FOOD /*38*/:
                    stringBuilder.append("&amp;");
                    break;
                case Place.TYPE_FUNERAL_HOME /*39*/:
                    stringBuilder.append("&#39;");
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
                    stringBuilder.append("&lt;");
                    break;
                case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
                    stringBuilder.append("&gt;");
                    break;
                default:
                    stringBuilder.append(charAt);
                    break;
            }
        }
        return stringBuilder.toString();
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (!(locale == null || locale.equals(ROOT))) {
            String script = ICUCompat.getScript(ICUCompat.addLikelySubtags(locale.toString()));
            if (script == null) {
                return getLayoutDirectionFromFirstChar(locale);
            }
            if (script.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || script.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
                return 1;
            }
        }
        return 0;
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return 1;
            default:
                return 0;
        }
    }

    static {
        ROOT = new Locale(BuildConfig.FLAVOR, BuildConfig.FLAVOR);
        ARAB_SCRIPT_SUBTAG = "Arab";
        HEBR_SCRIPT_SUBTAG = "Hebr";
    }
}
