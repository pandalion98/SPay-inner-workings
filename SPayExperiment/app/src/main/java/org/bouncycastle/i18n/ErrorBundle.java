/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Locale
 *  java.util.TimeZone
 */
package org.bouncycastle.i18n;

import java.util.Locale;
import java.util.TimeZone;
import org.bouncycastle.i18n.MessageBundle;

public class ErrorBundle
extends MessageBundle {
    public static final String DETAIL_ENTRY = "details";
    public static final String SUMMARY_ENTRY = "summary";

    public ErrorBundle(String string, String string2) {
        super(string, string2);
    }

    public ErrorBundle(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public ErrorBundle(String string, String string2, String string3, Object[] arrobject) {
        super(string, string2, string3, arrobject);
    }

    public ErrorBundle(String string, String string2, Object[] arrobject) {
        super(string, string2, arrobject);
    }

    public String getDetail(Locale locale) {
        return this.getEntry(DETAIL_ENTRY, locale, TimeZone.getDefault());
    }

    public String getDetail(Locale locale, TimeZone timeZone) {
        return this.getEntry(DETAIL_ENTRY, locale, timeZone);
    }

    public String getSummary(Locale locale) {
        return this.getEntry(SUMMARY_ENTRY, locale, TimeZone.getDefault());
    }

    public String getSummary(Locale locale, TimeZone timeZone) {
        return this.getEntry(SUMMARY_ENTRY, locale, timeZone);
    }
}

