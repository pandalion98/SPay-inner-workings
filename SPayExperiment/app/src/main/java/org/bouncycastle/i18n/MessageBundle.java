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
import org.bouncycastle.i18n.TextBundle;

public class MessageBundle
extends TextBundle {
    public static final String TITLE_ENTRY = "title";

    public MessageBundle(String string, String string2) {
        super(string, string2);
    }

    public MessageBundle(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public MessageBundle(String string, String string2, String string3, Object[] arrobject) {
        super(string, string2, string3, arrobject);
    }

    public MessageBundle(String string, String string2, Object[] arrobject) {
        super(string, string2, arrobject);
    }

    public String getTitle(Locale locale) {
        return this.getEntry(TITLE_ENTRY, locale, TimeZone.getDefault());
    }

    public String getTitle(Locale locale, TimeZone timeZone) {
        return this.getEntry(TITLE_ENTRY, locale, timeZone);
    }
}

