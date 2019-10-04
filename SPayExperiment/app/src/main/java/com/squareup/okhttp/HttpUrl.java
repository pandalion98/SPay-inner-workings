/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.AssertionError
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.net.IDN
 *  java.net.InetAddress
 *  java.net.MalformedURLException
 *  java.net.URI
 *  java.net.URISyntaxException
 *  java.net.URL
 *  java.net.UnknownHostException
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Locale
 *  java.util.Set
 */
package com.squareup.okhttp;

import java.net.IDN;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import okio.Buffer;

public final class HttpUrl {
    static final String FRAGMENT_ENCODE_SET = "";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]\\^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    private final String fragment;
    private final String host;
    private final String password;
    private final List<String> pathSegments;
    private final int port;
    private final List<String> queryNamesAndValues;
    private final String scheme;
    private final String url;
    private final String username;

    /*
     * Enabled aggressive block sorting
     */
    private HttpUrl(Builder builder) {
        this.scheme = builder.scheme;
        this.username = HttpUrl.percentDecode(builder.encodedUsername);
        this.password = HttpUrl.percentDecode(builder.encodedPassword);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = this.percentDecode(builder.encodedPathSegments);
        List<String> list = builder.encodedQueryNamesAndValues != null ? this.percentDecode(builder.encodedQueryNamesAndValues) : null;
        this.queryNamesAndValues = list;
        String string = builder.encodedFragment;
        String string2 = null;
        if (string != null) {
            string2 = HttpUrl.percentDecode(builder.encodedFragment);
        }
        this.fragment = string2;
        this.url = builder.toString();
    }

    static /* synthetic */ String access$100(HttpUrl httpUrl) {
        return httpUrl.scheme;
    }

    static /* synthetic */ String access$300(HttpUrl httpUrl) {
        return httpUrl.host;
    }

    static /* synthetic */ int access$400(HttpUrl httpUrl) {
        return httpUrl.port;
    }

    static String canonicalize(String string, int n2, int n3, String string2, boolean bl, boolean bl2) {
        int n4;
        for (int i2 = n2; i2 < n3; i2 += Character.charCount((int)n4)) {
            n4 = string.codePointAt(i2);
            if (n4 >= 32 && n4 < 127 && string2.indexOf(n4) == -1 && (n4 != 37 || bl) && (!bl2 || n4 != 43)) continue;
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string, n2, i2);
            HttpUrl.canonicalize(buffer, string, i2, n3, string2, bl, bl2);
            return buffer.readUtf8();
        }
        return string.substring(n2, n3);
    }

    static String canonicalize(String string, String string2, boolean bl, boolean bl2) {
        return HttpUrl.canonicalize(string, 0, string.length(), string2, bl, bl2);
    }

    /*
     * Enabled aggressive block sorting
     */
    static void canonicalize(Buffer buffer, String string, int n2, int n3, String string2, boolean bl, boolean bl2) {
        Buffer buffer2 = null;
        while (n2 < n3) {
            int n4 = string.codePointAt(n2);
            if (!bl || n4 != 9 && n4 != 10 && n4 != 12 && n4 != 13) {
                if (bl2 && n4 == 43) {
                    String string3 = bl ? "%20" : "%2B";
                    buffer.writeUtf8(string3);
                } else if (n4 < 32 || n4 >= 127 || string2.indexOf(n4) != -1 || n4 == 37 && !bl) {
                    if (buffer2 == null) {
                        buffer2 = new Buffer();
                    }
                    buffer2.writeUtf8CodePoint(n4);
                    while (!buffer2.exhausted()) {
                        int n5 = 255 & buffer2.readByte();
                        buffer.writeByte(37);
                        buffer.writeByte(HEX_DIGITS[15 & n5 >> 4]);
                        buffer.writeByte(HEX_DIGITS[n5 & 15]);
                    }
                } else {
                    buffer.writeUtf8CodePoint(n4);
                }
            }
            n2 += Character.charCount((int)n4);
        }
        return;
    }

    static int decodeHexDigit(char c2) {
        if (c2 >= '0' && c2 <= '9') {
            return c2 - 48;
        }
        if (c2 >= 'a' && c2 <= 'f') {
            return 10 + (c2 - 97);
        }
        if (c2 >= 'A' && c2 <= 'F') {
            return 10 + (c2 - 65);
        }
        return -1;
    }

    public static int defaultPort(String string) {
        if (string.equals((Object)"http")) {
            return 80;
        }
        if (string.equals((Object)"https")) {
            return 443;
        }
        return -1;
    }

    private static int delimiterOffset(String string, int n2, int n3, String string2) {
        int n4 = n2;
        do {
            block4 : {
                block3 : {
                    if (n4 >= n3) break block3;
                    if (string2.indexOf((int)string.charAt(n4)) == -1) break block4;
                    n3 = n4;
                }
                return n3;
            }
            ++n4;
        } while (true);
    }

    public static HttpUrl get(URI uRI) {
        return HttpUrl.parse(uRI.toString());
    }

    public static HttpUrl get(URL uRL) {
        return HttpUrl.parse(uRL.toString());
    }

    static void namesAndValuesToQueryString(StringBuilder stringBuilder, List<String> list) {
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; i2 += 2) {
            String string = (String)list.get(i2);
            String string2 = (String)list.get(i2 + 1);
            if (i2 > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(string);
            if (string2 == null) continue;
            stringBuilder.append('=');
            stringBuilder.append(string2);
        }
    }

    public static HttpUrl parse(String string) {
        return new Builder().parse(null, string);
    }

    static void pathSegmentsToString(StringBuilder stringBuilder, List<String> list) {
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append('/');
            stringBuilder.append((String)list.get(i2));
        }
    }

    static String percentDecode(String string) {
        return HttpUrl.percentDecode(string, 0, string.length());
    }

    static String percentDecode(String string, int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            if (string.charAt(i2) != '%') continue;
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string, n2, i2);
            HttpUrl.percentDecode(buffer, string, i2, n3);
            return buffer.readUtf8();
        }
        return string.substring(n2, n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private List<String> percentDecode(List<String> list) {
        ArrayList arrayList = new ArrayList(list.size());
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            String string2 = string != null ? HttpUrl.percentDecode(string) : null;
            arrayList.add((Object)string2);
        }
        return Collections.unmodifiableList((List)arrayList);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static void percentDecode(Buffer var0, String var1_1, int var2_2, int var3_3) {
        var4_4 = var2_2;
        while (var4_4 < var3_3) {
            var5_5 = var1_1.codePointAt(var4_4);
            if (var5_5 != 37 || var4_4 + 2 >= var3_3) ** GOTO lbl-1000
            var7_6 = HttpUrl.decodeHexDigit(var1_1.charAt(var4_4 + 1));
            var8_7 = HttpUrl.decodeHexDigit(var1_1.charAt(var4_4 + 2));
            if (var7_6 != -1 && var8_7 != -1) {
                var0.writeByte(var8_7 + (var7_6 << 4));
                var4_4 += 2;
            } else lbl-1000: // 2 sources:
            {
                var0.writeUtf8CodePoint(var5_5);
            }
            var4_4 += Character.charCount((int)var5_5);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    static List<String> queryStringToNamesAndValues(String string) {
        ArrayList arrayList = new ArrayList();
        int n2 = 0;
        while (n2 <= string.length()) {
            int n3;
            int n4 = string.indexOf(38, n2);
            if (n4 == -1) {
                n4 = string.length();
            }
            if ((n3 = string.indexOf(61, n2)) == -1 || n3 > n4) {
                arrayList.add((Object)string.substring(n2, n4));
                arrayList.add(null);
            } else {
                arrayList.add((Object)string.substring(n2, n3));
                arrayList.add((Object)string.substring(n3 + 1, n4));
            }
            n2 = n4 + 1;
        }
        return arrayList;
    }

    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        int n2 = 1 + this.url.indexOf(35);
        return this.url.substring(n2);
    }

    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return FRAGMENT_ENCODE_SET;
        }
        int n2 = 1 + this.url.indexOf(58, 3 + this.scheme.length());
        int n3 = this.url.indexOf(64);
        return this.url.substring(n2, n3);
    }

    public String encodedPath() {
        int n2 = this.url.indexOf(47, 3 + this.scheme.length());
        int n3 = HttpUrl.delimiterOffset(this.url, n2, this.url.length(), "?#");
        return this.url.substring(n2, n3);
    }

    public List<String> encodedPathSegments() {
        int n2 = this.url.indexOf(47, 3 + this.scheme.length());
        int n3 = HttpUrl.delimiterOffset(this.url, n2, this.url.length(), "?#");
        ArrayList arrayList = new ArrayList();
        while (n2 < n3) {
            int n4 = n2 + 1;
            n2 = HttpUrl.delimiterOffset(this.url, n4, n3, "/");
            arrayList.add((Object)this.url.substring(n4, n2));
        }
        return arrayList;
    }

    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int n2 = 1 + this.url.indexOf(63);
        int n3 = HttpUrl.delimiterOffset(this.url, n2 + 1, this.url.length(), "#");
        return this.url.substring(n2, n3);
    }

    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return FRAGMENT_ENCODE_SET;
        }
        int n2 = 3 + this.scheme.length();
        int n3 = HttpUrl.delimiterOffset(this.url, n2, this.url.length(), ":@");
        return this.url.substring(n2, n3);
    }

    public boolean equals(Object object) {
        return object instanceof HttpUrl && ((HttpUrl)object).url.equals((Object)this.url);
    }

    public String fragment() {
        return this.fragment;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public String host() {
        return this.host;
    }

    public boolean isHttps() {
        return this.scheme.equals((Object)"https");
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = this.encodedUsername();
        builder.encodedPassword = this.encodedPassword();
        builder.host = this.host;
        builder.port = this.port;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.encodedFragment = this.encodedFragment();
        return builder;
    }

    public String password() {
        return this.password;
    }

    public List<String> pathSegments() {
        return this.pathSegments;
    }

    public int pathSize() {
        return this.pathSegments.size();
    }

    public int port() {
        return this.port;
    }

    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        HttpUrl.namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    public String queryParameter(String string) {
        if (this.queryNamesAndValues != null) {
            int n2 = this.queryNamesAndValues.size();
            for (int i2 = 0; i2 < n2; i2 += 2) {
                if (!string.equals(this.queryNamesAndValues.get(i2))) continue;
                return (String)this.queryNamesAndValues.get(i2 + 1);
            }
        }
        return null;
    }

    public String queryParameterName(int n2) {
        return (String)this.queryNamesAndValues.get(n2 * 2);
    }

    public Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return Collections.emptySet();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int n2 = this.queryNamesAndValues.size();
        for (int i2 = 0; i2 < n2; i2 += 2) {
            linkedHashSet.add(this.queryNamesAndValues.get(i2));
        }
        return Collections.unmodifiableSet((Set)linkedHashSet);
    }

    public String queryParameterValue(int n2) {
        return (String)this.queryNamesAndValues.get(1 + n2 * 2);
    }

    public List<String> queryParameterValues(String string) {
        if (this.queryNamesAndValues == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        int n2 = this.queryNamesAndValues.size();
        for (int i2 = 0; i2 < n2; i2 += 2) {
            if (!string.equals(this.queryNamesAndValues.get(i2))) continue;
            arrayList.add(this.queryNamesAndValues.get(i2 + 1));
        }
        return Collections.unmodifiableList((List)arrayList);
    }

    public int querySize() {
        if (this.queryNamesAndValues != null) {
            return this.queryNamesAndValues.size() / 2;
        }
        return 0;
    }

    public HttpUrl resolve(String string) {
        return new Builder().parse(this, string);
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.url;
    }

    public URI uri() {
        try {
            URI uRI = new URI(this.url);
            return uRI;
        }
        catch (URISyntaxException uRISyntaxException) {
            throw new IllegalStateException("not valid as a java.net.URI: " + this.url);
        }
    }

    public URL url() {
        try {
            URL uRL = new URL(this.url);
            return uRL;
        }
        catch (MalformedURLException malformedURLException) {
            throw new RuntimeException((Throwable)malformedURLException);
        }
    }

    public String username() {
        return this.username;
    }

    public static final class Builder {
        String encodedFragment;
        String encodedPassword = "";
        final List<String> encodedPathSegments = new ArrayList();
        List<String> encodedQueryNamesAndValues;
        String encodedUsername = "";
        String host;
        int port = -1;
        String scheme;

        public Builder() {
            this.encodedPathSegments.add((Object)HttpUrl.FRAGMENT_ENCODE_SET);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private static String canonicalizeHost(String string, int n2, int n3) {
            String string2;
            String string3 = HttpUrl.percentDecode(string, n2, n3);
            if (string3.startsWith("[") && string3.endsWith("]")) {
                InetAddress inetAddress = Builder.decodeIpv6(string3, 1, -1 + string3.length());
                string2 = null;
                if (inetAddress == null) return string2;
                return inetAddress.getHostAddress();
            }
            String string4 = Builder.domainToAscii(string3);
            string2 = null;
            if (string4 == null) return string2;
            int n4 = string4.length();
            int n5 = HttpUrl.delimiterOffset(string4, 0, n4, "\u0000\t\n\r #%/:?@[\\]");
            string2 = null;
            if (n5 != n4) return string2;
            return string4;
        }

        private static boolean decodeIpv4Suffix(String string, int n2, int n3, byte[] arrby, int n4) {
            int n5 = n2;
            int n6 = n4;
            while (n5 < n3) {
                if (n6 == arrby.length) {
                    return false;
                }
                if (n6 != n4 && string.charAt(n5) != '.') {
                    return false;
                }
                int n7 = 0;
                int n8 = ++n5;
                do {
                    char c2;
                    if (n8 >= n3 || (c2 = string.charAt(n8)) < '0' || c2 > '9') {
                        if (n8 - n5 != 0) break;
                        return false;
                    }
                    if (n7 == 0 && n5 != n8) {
                        return false;
                    }
                    if ((n7 = -48 + (c2 + n7 * 10)) > 255) {
                        return false;
                    }
                    ++n8;
                } while (true);
                int n9 = n6 + 1;
                arrby[n6] = (byte)n7;
                n6 = n9;
                n5 = n8;
            }
            return n6 == n4 + 4;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private static InetAddress decodeIpv6(String string, int n2, int n3) {
            byte[] arrby = new byte[16];
            int n4 = n2;
            int n5 = -1;
            int n6 = -1;
            int n7 = 0;
            do {
                block14 : {
                    block11 : {
                        block13 : {
                            block12 : {
                                if (n4 >= n3) break block11;
                                if (n7 == arrby.length) {
                                    return null;
                                }
                                if (n4 + 2 > n3 || !string.regionMatches(n4, "::", 0, 2)) break block12;
                                if (n6 != -1) {
                                    return null;
                                }
                                n6 = n7 + 2;
                                if ((n4 += 2) != n3) break block13;
                                n7 = n6;
                                break block11;
                            }
                            if (n7 == 0 || string.regionMatches(n4, ":", 0, 1)) break block14;
                            if (!string.regionMatches(n4, ".", 0, 1)) {
                                return null;
                            }
                            if (!Builder.decodeIpv4Suffix(string, n5, n3, arrby, n7 - 2)) {
                                return null;
                            }
                            n7 += 2;
                            break block11;
                        }
                        n7 = n6;
                        break block14;
                    }
                    if (n7 != arrby.length) {
                        if (n6 == -1) {
                            return null;
                        }
                        System.arraycopy((Object)arrby, (int)n6, (Object)arrby, (int)(arrby.length - (n7 - n6)), (int)(n7 - n6));
                        Arrays.fill((byte[])arrby, (int)n6, (int)(n6 + (arrby.length - n7)), (byte)0);
                    }
                    try {
                        return InetAddress.getByAddress((byte[])arrby);
                    }
                    catch (UnknownHostException unknownHostException) {
                        throw new AssertionError();
                    }
                }
                int n8 = 0;
                int n9 = ++n4;
                do {
                    int n10;
                    if (n9 >= n3 || (n10 = HttpUrl.decodeHexDigit(string.charAt(n9))) == -1) {
                        int n11 = n9 - n4;
                        if (n11 == 0) return null;
                        if (n11 <= 4) break;
                        return null;
                    }
                    n8 = n10 + (n8 << 4);
                    ++n9;
                } while (true);
                int n12 = n7 + 1;
                arrby[n7] = (byte)(255 & n8 >>> 8);
                n7 = n12 + 1;
                arrby[n12] = (byte)(n8 & 255);
                n5 = n4;
                n4 = n9;
            } while (true);
        }

        private static String domainToAscii(String string) {
            try {
                String string2 = IDN.toASCII((String)string).toLowerCase(Locale.US);
                boolean bl = string2.isEmpty();
                if (bl) {
                    return null;
                }
                return string2;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                return null;
            }
        }

        private boolean isDot(String string) {
            return string.equals((Object)".") || string.equalsIgnoreCase("%2e");
        }

        private boolean isDotDot(String string) {
            return string.equals((Object)"..") || string.equalsIgnoreCase("%2e.") || string.equalsIgnoreCase(".%2e") || string.equalsIgnoreCase("%2e%2e");
        }

        private static int parsePort(String string, int n2, int n3) {
            try {
                int n4 = Integer.parseInt((String)HttpUrl.canonicalize(string, n2, n3, HttpUrl.FRAGMENT_ENCODE_SET, false, false));
                if (n4 > 0 && n4 <= 65535) {
                    return n4;
                }
                return -1;
            }
            catch (NumberFormatException numberFormatException) {
                return -1;
            }
        }

        private void pop() {
            if (((String)this.encodedPathSegments.remove(-1 + this.encodedPathSegments.size())).isEmpty() && !this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.set(-1 + this.encodedPathSegments.size(), (Object)HttpUrl.FRAGMENT_ENCODE_SET);
                return;
            }
            this.encodedPathSegments.add((Object)HttpUrl.FRAGMENT_ENCODE_SET);
        }

        /*
         * Exception decompiling
         */
        private static int portColonOffset(String var0, int var1_1, int var2_2) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
            // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:478)
            // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:61)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:372)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
            // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
            // org.benf.cfr.reader.entities.g.p(Method.java:396)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
            // org.benf.cfr.reader.entities.d.c(ClassFile.java:773)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:870)
            // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
            // org.benf.cfr.reader.b.a(Driver.java:128)
            // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
            // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
            // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
            // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
            // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
            // java.lang.Thread.run(Thread.java:764)
            throw new IllegalStateException("Decompilation failed");
        }

        /*
         * Enabled aggressive block sorting
         */
        private void push(String string, int n2, int n3, boolean bl, boolean bl2) {
            block7 : {
                block6 : {
                    String string2 = HttpUrl.canonicalize(string, n2, n3, HttpUrl.PATH_SEGMENT_ENCODE_SET, bl2, false);
                    if (this.isDot(string2)) break block6;
                    if (this.isDotDot(string2)) {
                        this.pop();
                        return;
                    }
                    if (((String)this.encodedPathSegments.get(-1 + this.encodedPathSegments.size())).isEmpty()) {
                        this.encodedPathSegments.set(-1 + this.encodedPathSegments.size(), (Object)string2);
                    } else {
                        this.encodedPathSegments.add((Object)string2);
                    }
                    if (bl) break block7;
                }
                return;
            }
            this.encodedPathSegments.add((Object)HttpUrl.FRAGMENT_ENCODE_SET);
        }

        private void removeAllCanonicalQueryParameters(String string) {
            int n2 = -2 + this.encodedQueryNamesAndValues.size();
            do {
                block4 : {
                    block3 : {
                        if (n2 < 0) break block3;
                        if (!string.equals(this.encodedQueryNamesAndValues.get(n2))) break block4;
                        this.encodedQueryNamesAndValues.remove(n2 + 1);
                        this.encodedQueryNamesAndValues.remove(n2);
                        if (!this.encodedQueryNamesAndValues.isEmpty()) break block4;
                        this.encodedQueryNamesAndValues = null;
                    }
                    return;
                }
                n2 -= 2;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void resolvePath(String string, int n2, int n3) {
            if (n2 != n3) {
                char c2 = string.charAt(n2);
                if (c2 == '/' || c2 == '\\') {
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.add((Object)HttpUrl.FRAGMENT_ENCODE_SET);
                    ++n2;
                } else {
                    this.encodedPathSegments.set(-1 + this.encodedPathSegments.size(), (Object)HttpUrl.FRAGMENT_ENCODE_SET);
                }
                int n4 = n2;
                while (n4 < n3) {
                    int n5 = HttpUrl.delimiterOffset(string, n4, n3, "/\\");
                    boolean bl = n5 < n3;
                    this.push(string, n4, n5, bl, true);
                    if (bl) {
                        // empty if block
                    }
                    n4 = ++n5;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private static int schemeDelimiterOffset(String string, int n2, int n3) {
            int n4;
            block3 : {
                char c2;
                if (n3 - n2 >= 2 && ((c2 = string.charAt(n2)) >= 'a' && c2 <= 'z' || c2 >= 'A' && c2 <= 'Z')) {
                    for (n4 = n2 + 1; n4 < n3; ++n4) {
                        char c3 = string.charAt(n4);
                        if (c3 >= 'a' && c3 <= 'z' || c3 >= 'A' && c3 <= 'Z' || c3 == '+' || c3 == '-' || c3 == '.') {
                            continue;
                        }
                        if (c3 == ':') break block3;
                    }
                }
                return -1;
            }
            return n4;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private int skipLeadingAsciiWhitespace(String string, int n2, int n3) {
            int n4 = n2;
            while (n4 < n3) {
                switch (string.charAt(n4)) {
                    default: {
                        return n4;
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                }
                ++n4;
            }
            return n3;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private int skipTrailingAsciiWhitespace(String string, int n2, int n3) {
            int n4 = n3 - 1;
            while (n4 >= n2) {
                switch (string.charAt(n4)) {
                    default: {
                        return n4 + 1;
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                }
                --n4;
            }
            return n2;
        }

        private static int slashCount(String string, int n2, int n3) {
            char c2;
            int n4 = 0;
            while (n2 < n3 && ((c2 = string.charAt(n2)) == '\\' || c2 == '/')) {
                ++n4;
                ++n2;
            }
            return n4;
        }

        public Builder addEncodedPathSegment(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedPathSegment == null");
            }
            this.push(string, 0, string.length(), false, true);
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder addEncodedQueryParameter(String string, String string2) {
            if (string == null) {
                throw new IllegalArgumentException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add((Object)HttpUrl.canonicalize(string, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, true));
            List<String> list = this.encodedQueryNamesAndValues;
            String string3 = string2 != null ? HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, true) : null;
            list.add((Object)string3);
            return this;
        }

        public Builder addPathSegment(String string) {
            if (string == null) {
                throw new IllegalArgumentException("pathSegment == null");
            }
            this.push(string, 0, string.length(), false, false);
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder addQueryParameter(String string, String string2) {
            if (string == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add((Object)HttpUrl.canonicalize(string, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, true));
            List<String> list = this.encodedQueryNamesAndValues;
            String string3 = string2 != null ? HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, true) : null;
            list.add((Object)string3);
            return this;
        }

        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            }
            if (this.host == null) {
                throw new IllegalStateException("host == null");
            }
            return new HttpUrl(this);
        }

        int effectivePort() {
            if (this.port != -1) {
                return this.port;
            }
            return HttpUrl.defaultPort(this.scheme);
        }

        public Builder encodedFragment(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedFragment == null");
            }
            this.encodedFragment = HttpUrl.canonicalize(string, HttpUrl.FRAGMENT_ENCODE_SET, true, false);
            return this;
        }

        public Builder encodedPassword(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedPassword == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(string, HttpUrl.PASSWORD_ENCODE_SET, true, false);
            return this;
        }

        public Builder encodedPath(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedPath == null");
            }
            if (!string.startsWith("/")) {
                throw new IllegalArgumentException("unexpected encodedPath: " + string);
            }
            this.resolvePath(string, 0, string.length());
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder encodedQuery(String string) {
            List<String> list = string != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(string, HttpUrl.QUERY_ENCODE_SET, true, true)) : null;
            this.encodedQueryNamesAndValues = list;
            return this;
        }

        public Builder encodedUsername(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedUsername == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(string, HttpUrl.USERNAME_ENCODE_SET, true, false);
            return this;
        }

        public Builder fragment(String string) {
            if (string == null) {
                throw new IllegalArgumentException("fragment == null");
            }
            this.encodedFragment = HttpUrl.canonicalize(string, HttpUrl.FRAGMENT_ENCODE_SET, false, false);
            return this;
        }

        public Builder host(String string) {
            if (string == null) {
                throw new IllegalArgumentException("host == null");
            }
            String string2 = Builder.canonicalizeHost(string, 0, string.length());
            if (string2 == null) {
                throw new IllegalArgumentException("unexpected host: " + string);
            }
            this.host = string2;
            return this;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        HttpUrl parse(HttpUrl var1_1, String var2_2) {
            block21 : {
                block20 : {
                    var3_3 = this.skipLeadingAsciiWhitespace(var2_2, 0, var2_2.length());
                    if (Builder.schemeDelimiterOffset(var2_2, var3_3, var4_4 = this.skipTrailingAsciiWhitespace(var2_2, var3_3, var2_2.length())) != -1) {
                        if (var2_2.regionMatches(true, var3_3, "https:", 0, 6)) {
                            this.scheme = "https";
                            var3_3 += "https:".length();
                        } else {
                            if (var2_2.regionMatches(true, var3_3, "http:", 0, 5) == false) return null;
                            this.scheme = "http";
                            var3_3 += "http:".length();
                        }
                    } else {
                        if (var1_1 == null) return null;
                        this.scheme = HttpUrl.access$100(var1_1);
                    }
                    if ((var5_5 = Builder.slashCount(var2_2, var3_3, var4_4)) >= 2 || var1_1 == null || !HttpUrl.access$100(var1_1).equals((Object)this.scheme)) break block20;
                    this.encodedUsername = var1_1.encodedUsername();
                    this.encodedPassword = var1_1.encodedPassword();
                    this.host = HttpUrl.access$300(var1_1);
                    this.port = HttpUrl.access$400(var1_1);
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.addAll(var1_1.encodedPathSegments());
                    if (var3_3 == var4_4 || var2_2.charAt(var3_3) == '#') {
                        this.encodedQuery(var1_1.encodedQuery());
                    }
                    break block21;
                }
                var6_6 = var3_3 + var5_5;
                var7_7 = false;
                var8_8 = false;
                var9_9 = var6_6;
                block4 : do {
                    var11_11 = (var10_10 = HttpUrl.access$200(var2_2, var9_9, var4_4, "@/\\?#")) != var4_4 ? (int)var2_2.charAt(var10_10) : -1;
                    switch (var11_11) {
                        default: {
                            var16_13 = var7_7;
                            var15_12 = var9_9;
                            var17_14 = var8_8;
                            ** GOTO lbl52
                        }
                        case 64: {
                            if (!var7_7) {
                                var18_15 = HttpUrl.access$200(var2_2, var9_9, var10_10, ":");
                                var19_16 = HttpUrl.canonicalize(var2_2, var9_9, var18_15, " \"':;<=>@[]^`{}|/\\?#", true, false);
                                if (var8_8) {
                                    var19_16 = this.encodedUsername + "%40" + var19_16;
                                }
                                this.encodedUsername = var19_16;
                                if (var18_15 != var10_10) {
                                    var7_7 = true;
                                    this.encodedPassword = HttpUrl.canonicalize(var2_2, var18_15 + 1, var10_10, " \"':;<=>@[]\\^`{}|/\\?#", true, false);
                                }
                                var8_8 = true;
                            } else {
                                this.encodedPassword = this.encodedPassword + "%40" + HttpUrl.canonicalize(var2_2, var9_9, var10_10, " \"':;<=>@[]\\^`{}|/\\?#", true, false);
                            }
                            var15_12 = var10_10 + 1;
                            var16_13 = var7_7;
                            var17_14 = var8_8;
lbl52: // 2 sources:
                            var7_7 = var16_13;
                            var8_8 = var17_14;
                            var9_9 = var15_12;
                            continue block4;
                        }
                        case -1: 
                        case 35: 
                        case 47: 
                        case 63: 
                        case 92: 
                    }
                    break;
                } while (true);
                var12_17 = Builder.portColonOffset(var2_2, var9_9, var10_10);
                if (var12_17 + 1 < var10_10) {
                    this.host = Builder.canonicalizeHost(var2_2, var9_9, var12_17);
                    this.port = Builder.parsePort(var2_2, var12_17 + 1, var10_10);
                    if (this.port == -1) {
                        return null;
                    }
                } else {
                    this.host = Builder.canonicalizeHost(var2_2, var9_9, var12_17);
                    this.port = HttpUrl.defaultPort(this.scheme);
                }
                if (this.host == null) {
                    return null;
                }
                var3_3 = var10_10;
            }
            var13_18 = HttpUrl.access$200(var2_2, var3_3, var4_4, "?#");
            this.resolvePath(var2_2, var3_3, var13_18);
            if (var13_18 < var4_4 && var2_2.charAt(var13_18) == '?') {
                var14_19 = HttpUrl.access$200(var2_2, var13_18, var4_4, "#");
                this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(var2_2, var13_18 + 1, var14_19, " \"'<>#", true, true));
            } else {
                var14_19 = var13_18;
            }
            if (var14_19 >= var4_4) return this.build();
            if (var2_2.charAt(var14_19) != '#') return this.build();
            this.encodedFragment = HttpUrl.canonicalize(var2_2, var14_19 + 1, var4_4, "", true, false);
            return this.build();
        }

        public Builder password(String string) {
            if (string == null) {
                throw new IllegalArgumentException("password == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(string, HttpUrl.PASSWORD_ENCODE_SET, false, false);
            return this;
        }

        public Builder port(int n2) {
            if (n2 <= 0 || n2 > 65535) {
                throw new IllegalArgumentException("unexpected port: " + n2);
            }
            this.port = n2;
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder query(String string) {
            List<String> list = string != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(string, HttpUrl.QUERY_ENCODE_SET, false, true)) : null;
            this.encodedQueryNamesAndValues = list;
            return this;
        }

        public Builder removeAllEncodedQueryParameters(String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(string, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, true));
            return this;
        }

        public Builder removeAllQueryParameters(String string) {
            if (string == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(string, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, true));
            return this;
        }

        public Builder removePathSegment(int n2) {
            this.encodedPathSegments.remove(n2);
            if (this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add((Object)HttpUrl.FRAGMENT_ENCODE_SET);
            }
            return this;
        }

        public Builder scheme(String string) {
            if (string == null) {
                throw new IllegalArgumentException("scheme == null");
            }
            if (string.equalsIgnoreCase("http")) {
                this.scheme = "http";
                return this;
            }
            if (string.equalsIgnoreCase("https")) {
                this.scheme = "https";
                return this;
            }
            throw new IllegalArgumentException("unexpected scheme: " + string);
        }

        public Builder setEncodedPathSegment(int n2, String string) {
            if (string == null) {
                throw new IllegalArgumentException("encodedPathSegment == null");
            }
            String string2 = HttpUrl.canonicalize(string, 0, string.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false);
            this.encodedPathSegments.set(n2, (Object)string2);
            if (this.isDot(string2) || this.isDotDot(string2)) {
                throw new IllegalArgumentException("unexpected path segment: " + string);
            }
            return this;
        }

        public Builder setEncodedQueryParameter(String string, String string2) {
            this.removeAllEncodedQueryParameters(string);
            this.addEncodedQueryParameter(string, string2);
            return this;
        }

        public Builder setPathSegment(int n2, String string) {
            if (string == null) {
                throw new IllegalArgumentException("pathSegment == null");
            }
            String string2 = HttpUrl.canonicalize(string, 0, string.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false);
            if (this.isDot(string2) || this.isDotDot(string2)) {
                throw new IllegalArgumentException("unexpected path segment: " + string);
            }
            this.encodedPathSegments.set(n2, (Object)string2);
            return this;
        }

        public Builder setQueryParameter(String string, String string2) {
            this.removeAllQueryParameters(string);
            this.addQueryParameter(string, string2);
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public String toString() {
            int n2;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.scheme);
            stringBuilder.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                stringBuilder.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    stringBuilder.append(':');
                    stringBuilder.append(this.encodedPassword);
                }
                stringBuilder.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                stringBuilder.append('[');
                stringBuilder.append(this.host);
                stringBuilder.append(']');
            } else {
                stringBuilder.append(this.host);
            }
            if ((n2 = this.effectivePort()) != HttpUrl.defaultPort(this.scheme)) {
                stringBuilder.append(':');
                stringBuilder.append(n2);
            }
            HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                stringBuilder.append('?');
                HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                stringBuilder.append('#');
                stringBuilder.append(this.encodedFragment);
            }
            return stringBuilder.toString();
        }

        public Builder username(String string) {
            if (string == null) {
                throw new IllegalArgumentException("username == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(string, HttpUrl.USERNAME_ENCODE_SET, false, false);
            return this;
        }
    }

}

