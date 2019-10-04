/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 *  java.util.Locale
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.squareup.okhttp;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MediaType {
    private static final Pattern PARAMETER;
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final Pattern TYPE_SUBTYPE;
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;

    static {
        TYPE_SUBTYPE = Pattern.compile((String)"([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
        PARAMETER = Pattern.compile((String)";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    }

    private MediaType(String string, String string2, String string3, String string4) {
        this.mediaType = string;
        this.type = string2;
        this.subtype = string3;
        this.charset = string4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static MediaType parse(String string) {
        Matcher matcher = TYPE_SUBTYPE.matcher((CharSequence)string);
        if (matcher.lookingAt()) {
            String string2 = matcher.group(1).toLowerCase(Locale.US);
            String string3 = matcher.group(2).toLowerCase(Locale.US);
            Matcher matcher2 = PARAMETER.matcher((CharSequence)string);
            int n2 = matcher.end();
            String string4 = null;
            do {
                if (n2 >= string.length()) {
                    return new MediaType(string, string2, string3, string4);
                }
                matcher2.region(n2, string.length());
                if (!matcher2.lookingAt()) break;
                String string5 = matcher2.group(1);
                if (string5 != null && string5.equalsIgnoreCase("charset")) {
                    String string6 = matcher2.group(2) != null ? matcher2.group(2) : matcher2.group(3);
                    if (string4 != null && !string6.equalsIgnoreCase(string4)) {
                        throw new IllegalArgumentException("Multiple different charsets: " + string);
                    }
                    string4 = string6;
                }
                n2 = matcher2.end();
            } while (true);
        }
        return null;
    }

    public Charset charset() {
        if (this.charset != null) {
            return Charset.forName((String)this.charset);
        }
        return null;
    }

    public Charset charset(Charset charset) {
        if (this.charset != null) {
            charset = Charset.forName((String)this.charset);
        }
        return charset;
    }

    public boolean equals(Object object) {
        return object instanceof MediaType && ((MediaType)object).mediaType.equals((Object)this.mediaType);
    }

    public int hashCode() {
        return this.mediaType.hashCode();
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        return this.mediaType;
    }

    public String type() {
        return this.type;
    }
}

