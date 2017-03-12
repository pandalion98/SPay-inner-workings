package android.text.util;

import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Patterns;
import android.webkit.WebView;
import android.widget.TextView;
import com.android.i18n.phonenumbers.PhoneNumberMatch;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.PhoneNumberUtil.Leniency;
import com.sec.android.app.CscFeature;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linkify {
    public static final int ALL = 15;
    public static final int ALL_EX = 20490;
    public static final int ALL_KR = 24586;
    public static final int EMAIL_ADDRESSES = 2;
    public static final int MAP_ADDRESSES = 8;
    public static final int PHONE_NUMBERS = 4;
    public static final int PHONE_NUMBERS_EX = 32768;
    public static final int PHONE_NUMBERS_KR = 16384;
    private static final int PHONE_NUMBER_MINIMUM_DIGITS = 5;
    public static final int WEB_URLS = 1;
    public static final int WEB_URLS_EX = 4096;
    public static final int WEB_URLS_KR = 8192;
    public static final MatchFilter sPhoneNumberMatchFilter = new MatchFilter() {
        public final boolean acceptMatch(CharSequence s, int start, int end) {
            int digitCount = 0;
            for (int i = start; i < end; i++) {
                if (Character.isDigit(s.charAt(i))) {
                    digitCount++;
                    if (digitCount >= 5) {
                        return true;
                    }
                }
            }
            return false;
        }
    };
    public static final MatchFilter sPhoneNumberMatchFilterEX = new MatchFilter() {
        public final boolean acceptMatch(CharSequence s, int start, int end) {
            int i;
            int digitCount = 0;
            for (i = start; i < end; i++) {
                if (Character.isDigit(s.charAt(i))) {
                    digitCount++;
                }
            }
            int phoneNumberMinimumDigit = 5;
            int phoneNumberMaximumDigit = 13;
            String digitsByCSCFeature = CscFeature.getInstance().getString("CscFeature_Framework_ConfigLinkifyDigit");
            if (!TextUtils.isEmpty(digitsByCSCFeature)) {
                i = 0;
                int[] digits = new int[]{5, 13};
                SimpleStringSplitter splitter = new SimpleStringSplitter(';');
                splitter.setString(digitsByCSCFeature);
                while (splitter.hasNext()) {
                    try {
                        String str = splitter.next();
                        if (str != null) {
                            digits[i] = Integer.parseInt(str);
                            i++;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e("Linkify", "ArrayIndexOutOfBoundsException occured. CSC value is invalid.");
                    } catch (NumberFormatException e2) {
                        Log.e("Linkify", "NumberFormatException occured. CSC value is invalid.");
                    }
                }
                phoneNumberMinimumDigit = digits[0];
                phoneNumberMaximumDigit = digits[1];
                if (phoneNumberMinimumDigit <= 0) {
                    phoneNumberMinimumDigit = 1;
                }
            }
            if (digitCount < phoneNumberMinimumDigit || digitCount > phoneNumberMaximumDigit) {
                return false;
            }
            return true;
        }
    };
    public static final TransformFilter sPhoneNumberTransformFilter = new TransformFilter() {
        public final String transformUrl(Matcher match, String url) {
            return Patterns.digitsAndPlusOnly(match);
        }
    };
    public static final MatchFilter sUrlMatchFilter = new MatchFilter() {
        public final boolean acceptMatch(CharSequence s, int start, int end) {
            if (start != 0 && s.charAt(start - 1) == '@') {
                return false;
            }
            return true;
        }
    };

    public interface MatchFilter {
        boolean acceptMatch(CharSequence charSequence, int i, int i2);
    }

    public interface TransformFilter {
        String transformUrl(Matcher matcher, String str);
    }

    public static final boolean addLinks(Spannable text, int mask) {
        if (mask == 0) {
            return false;
        }
        int i;
        Iterator i$;
        LinkSpec link;
        String linkedText;
        int position;
        char[] chars;
        URLSpan[] old = (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class);
        for (i = old.length - 1; i >= 0; i--) {
            text.removeSpan(old[i]);
        }
        ArrayList<LinkSpec> links = new ArrayList();
        if ((mask & 1) != 0) {
            Spannable spannable = text;
            gatherLinks(links, spannable, Patterns.WEB_URL, new String[]{"http://", "https://", "rtsp://"}, sUrlMatchFilter, null);
        }
        if ((mask & 4096) != 0) {
            spannable = text;
            gatherLinks(links, spannable, Patterns.WEB_URL_EX, new String[]{"http://", "https://", "rtsp://", "ftp://"}, sUrlMatchFilter, null);
            i$ = links.iterator();
            while (i$.hasNext()) {
                link = (LinkSpec) i$.next();
                linkedText = text.toString().substring(link.start, link.end).toLowerCase();
                if (linkedText.contains("www.") && !linkedText.startsWith("www.") && !linkedText.startsWith("http://") && !linkedText.startsWith("https://") && !linkedText.startsWith("rtsp://") && !linkedText.startsWith("ftp://")) {
                    link.start += linkedText.indexOf("www.");
                    link.url = link.url.substring(0, link.url.indexOf("://") + 3) + text.toString().substring(link.start, link.end);
                } else if (!(!linkedText.contains("wap.") || linkedText.startsWith("wap.") || linkedText.startsWith("http://") || linkedText.startsWith("https://") || linkedText.startsWith("rtsp://") || linkedText.startsWith("ftp://"))) {
                    link.start += linkedText.indexOf("wap.");
                    link.url = link.url.substring(0, link.url.indexOf("://") + 3) + text.toString().substring(link.start, link.end);
                }
                position = linkedText.lastIndexOf(".");
                if (position >= 0 && position < linkedText.length() - 1 && !linkedText.startsWith("http://api.map.baidu.com/marker?location=")) {
                    chars = linkedText.substring(position + 1).toCharArray();
                    i = 0;
                    while (i < chars.length && chars[i] < '') {
                        i++;
                    }
                    if (i < chars.length) {
                        link.end -= chars.length - i;
                        link.url = link.url.substring(0, link.url.length() - (chars.length - i));
                    }
                }
            }
        }
        if ((mask & 8192) != 0) {
            spannable = text;
            gatherLinks(links, spannable, Patterns.WEB_URL_KR, new String[]{"http://", "https://", "rtsp://", "ftp://"}, sUrlMatchFilter, null);
            int j = 0;
            while (j < links.size()) {
                link = (LinkSpec) links.get(j);
                linkedText = text.toString().substring(link.start, link.end).toLowerCase();
                if (linkedText.endsWith(".ht") && text.length() >= link.end + 2 && "tp".equals(text.toString().substring(link.end, link.end + 2).toLowerCase())) {
                    if (links.size() > j + 1) {
                        LinkSpec linknext = (LinkSpec) links.get(j + 1);
                        if ((linknext.start == link.end + 5 && text.length() > link.end + 5 && "tp://".equals(text.toString().substring(link.end, link.end + 5).toLowerCase())) || (linknext.start == link.end + 6 && text.length() > link.end + 6 && "tps://".equals(text.toString().substring(link.end, link.end + 6).toLowerCase()))) {
                            linknext.start = link.end - 2;
                            links.set(j + 1, linknext);
                        }
                    }
                    links.remove(link);
                    if (j > 0) {
                        j--;
                    }
                } else {
                    if (!(!linkedText.contains("www.") || linkedText.startsWith("www.") || linkedText.startsWith("http://") || linkedText.startsWith("https://") || linkedText.startsWith("rtsp://") || linkedText.startsWith("ftp://"))) {
                        link.start += linkedText.indexOf("www.");
                        link.url = link.url.substring(0, link.url.indexOf("://") + 3) + text.toString().substring(link.start, link.end);
                    }
                    position = linkedText.lastIndexOf(".");
                    if (position >= 0 && position < linkedText.length() - 1) {
                        chars = linkedText.substring(position + 1).toCharArray();
                        i = 0;
                        while (i < chars.length && chars[i] < '') {
                            i++;
                        }
                        if (i < chars.length && i > 0 && chars[i - 1] != '/') {
                            link.end -= chars.length - i;
                            link.url = link.url.substring(0, link.url.length() - (chars.length - i));
                        }
                    }
                    j++;
                }
            }
        }
        if ((mask & 2) != 0) {
            gatherLinks(links, text, Patterns.EMAIL_ADDRESS, new String[]{WebView.SCHEME_MAILTO}, null, null);
        }
        if ((mask & 4) != 0) {
            gatherTelLinks(links, text);
        }
        if ((mask & 16384) != 0) {
            spannable = text;
            gatherLinks(links, spannable, Patterns.PHONE, new String[]{WebView.SCHEME_TEL}, sPhoneNumberMatchFilter, sPhoneNumberTransformFilter);
        }
        if ((32768 & mask) != 0) {
            spannable = text;
            gatherLinks(links, spannable, Patterns.PHONE, new String[]{WebView.SCHEME_TEL}, sPhoneNumberMatchFilterEX, sPhoneNumberTransformFilter);
        }
        if ((mask & 8) != 0) {
            gatherMapLinks(links, text);
        }
        pruneOverlaps(links);
        if (links.size() == 0) {
            return false;
        }
        i$ = links.iterator();
        while (i$.hasNext()) {
            link = (LinkSpec) i$.next();
            applyLink(link.url, link.start, link.end, text);
        }
        return true;
    }

    public static final boolean addLinks(TextView text, int mask) {
        if (mask == 0) {
            return false;
        }
        CharSequence t = text.getText();
        if (!(t instanceof Spannable)) {
            CharSequence s = SpannableString.valueOf(t);
            if (!addLinks((Spannable) s, mask)) {
                return false;
            }
            addLinkMovementMethod(text);
            text.setText(s);
            return true;
        } else if (!addLinks((Spannable) t, mask)) {
            return false;
        } else {
            addLinkMovementMethod(text);
            return true;
        }
    }

    private static final void addLinkMovementMethod(TextView t) {
        MovementMethod m = t.getMovementMethod();
        if ((m == null || !(m instanceof LinkMovementMethod)) && t.getLinksClickable()) {
            t.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static final void addLinks(TextView text, Pattern pattern, String scheme) {
        addLinks(text, pattern, scheme, null, null);
    }

    public static final void addLinks(TextView text, Pattern p, String scheme, MatchFilter matchFilter, TransformFilter transformFilter) {
        CharSequence s = SpannableString.valueOf(text.getText());
        if (addLinks((Spannable) s, p, scheme, matchFilter, transformFilter)) {
            text.setText(s);
            addLinkMovementMethod(text);
        }
    }

    public static final boolean addLinks(Spannable text, Pattern pattern, String scheme) {
        return addLinks(text, pattern, scheme, null, null);
    }

    public static final boolean addLinks(Spannable s, Pattern p, String scheme, MatchFilter matchFilter, TransformFilter transformFilter) {
        String prefix;
        boolean hasMatches = false;
        if (scheme == null) {
            prefix = "";
        } else {
            prefix = scheme.toLowerCase(Locale.ROOT);
        }
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            boolean allowed = true;
            if (matchFilter != null) {
                allowed = matchFilter.acceptMatch(s, start, end);
            }
            if (allowed) {
                applyLink(makeUrl(m.group(0), new String[]{prefix}, m, transformFilter), start, end, s);
                hasMatches = true;
            }
        }
        return hasMatches;
    }

    private static final void applyLink(String url, int start, int end, Spannable text) {
        text.setSpan(new URLSpan(url), start, end, 33);
    }

    private static final String makeUrl(String url, String[] prefixes, Matcher m, TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(m, url);
        }
        boolean hasPrefix = false;
        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0, prefixes[i].length())) {
                hasPrefix = true;
                if (!url.regionMatches(false, 0, prefixes[i], 0, prefixes[i].length())) {
                    url = prefixes[i] + url.substring(prefixes[i].length());
                }
                if (hasPrefix) {
                    return prefixes[0] + url;
                }
                return url;
            }
        }
        if (hasPrefix) {
            return url;
        }
        return prefixes[0] + url;
    }

    private static final void gatherLinks(ArrayList<LinkSpec> links, Spannable s, Pattern pattern, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (matchFilter == null || matchFilter.acceptMatch(s, start, end)) {
                LinkSpec spec = new LinkSpec();
                spec.url = makeUrl(m.group(0), schemes, m, transformFilter);
                spec.start = start;
                spec.end = end;
                links.add(spec);
            }
        }
    }

    private static final void gatherTelLinks(ArrayList<LinkSpec> links, Spannable s) {
        for (PhoneNumberMatch match : PhoneNumberUtil.getInstance().findNumbers(s.toString(), Locale.getDefault().getCountry(), Leniency.POSSIBLE, Long.MAX_VALUE)) {
            LinkSpec spec = new LinkSpec();
            spec.url = WebView.SCHEME_TEL + PhoneNumberUtils.normalizeNumber(match.rawString());
            spec.start = match.start();
            spec.end = match.end();
            links.add(spec);
        }
    }

    private static final void gatherMapLinks(ArrayList<LinkSpec> links, Spannable s) {
        String string = s.toString();
        int base = 0;
        while (true) {
            String address = WebView.findAddress(string);
            if (address != null) {
                int start = string.indexOf(address);
                if (start >= 0) {
                    LinkSpec spec = new LinkSpec();
                    int end = start + address.length();
                    spec.start = base + start;
                    spec.end = base + end;
                    string = string.substring(end);
                    base += end;
                    try {
                    } catch (UnsupportedEncodingException e) {
                    }
                    try {
                        spec.url = WebView.SCHEME_GEO + URLEncoder.encode(address, "UTF-8");
                        links.add(spec);
                    } catch (UnsupportedOperationException e2) {
                        return;
                    }
                }
                return;
            }
            return;
        }
    }

    private static final void pruneOverlaps(ArrayList<LinkSpec> links) {
        Collections.sort(links, new Comparator<LinkSpec>() {
            public final int compare(LinkSpec a, LinkSpec b) {
                if (a.start < b.start) {
                    return -1;
                }
                if (a.start > b.start) {
                    return 1;
                }
                if (a.end < b.end) {
                    return 1;
                }
                if (a.end <= b.end) {
                    return 0;
                }
                return -1;
            }
        });
        int len = links.size();
        int i = 0;
        while (i < len - 1) {
            LinkSpec a = (LinkSpec) links.get(i);
            LinkSpec b = (LinkSpec) links.get(i + 1);
            int remove = -1;
            if (a.start <= b.start && a.end > b.start) {
                if (b.end <= a.end) {
                    remove = i + 1;
                } else if (a.end - a.start > b.end - b.start) {
                    remove = i + 1;
                } else if (a.end - a.start < b.end - b.start) {
                    remove = i;
                }
                if (remove != -1) {
                    links.remove(remove);
                    len--;
                }
            }
            i++;
        }
    }
}
