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
import org.bouncycastle.i18n.LocalizedMessage;

public class TextBundle
extends LocalizedMessage {
    public static final String TEXT_ENTRY = "text";

    public TextBundle(String string, String string2) {
        super(string, string2);
    }

    public TextBundle(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public TextBundle(String string, String string2, String string3, Object[] arrobject) {
        super(string, string2, string3, arrobject);
    }

    public TextBundle(String string, String string2, Object[] arrobject) {
        super(string, string2, arrobject);
    }

    public String getText(Locale locale) {
        return this.getEntry(TEXT_ENTRY, locale, TimeZone.getDefault());
    }

    public String getText(Locale locale, TimeZone timeZone) {
        return this.getEntry(TEXT_ENTRY, locale, timeZone);
    }
}

