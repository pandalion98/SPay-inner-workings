/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Character
 *  java.lang.Integer
 *  java.lang.String
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.Locale
 */
package com.samsung.sensorframework.sda.c;

import android.content.Context;
import com.samsung.sensorframework.sda.c.a;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public abstract class b
extends a {
    public b(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    protected String cc(String string) {
        String string2 = "";
        for (int i2 = 0; i2 < string.length(); ++i2) {
            if (!Character.isDigit((char)string.charAt(i2))) continue;
            string2 = string2 + string.charAt(i2);
        }
        return string2;
    }

    protected String cd(String string) {
        block5 : {
            block4 : {
                if (!com.samsung.sensorframework.sda.a.b.gO().gQ()) break block4;
                if (string != null && string.length() != 0) break block5;
                string = "";
            }
            return string;
        }
        String string2 = this.cc(string);
        if (string2.length() > 10) {
            string2 = string2.substring(-10 + string2.length(), string2.length());
        }
        return this.ce(string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected String ce(String string) {
        MessageDigest messageDigest;
        if (!com.samsung.sensorframework.sda.a.b.gO().gQ()) return string;
        try {
            MessageDigest messageDigest2;
            messageDigest = messageDigest2 = MessageDigest.getInstance((String)"SHA-256");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            messageDigest = null;
        }
        messageDigest.reset();
        byte[] arrby = messageDigest.digest(string.getBytes());
        String string2 = "";
        int n2 = 0;
        while (n2 < arrby.length) {
            String string3 = Integer.toHexString((int)(255 & arrby[n2]));
            if (string3.length() == 1) {
                string3 = "0" + string3;
            }
            string2 = string2 + string3.toUpperCase(Locale.ENGLISH);
            ++n2;
        }
        return string2;
    }
}

