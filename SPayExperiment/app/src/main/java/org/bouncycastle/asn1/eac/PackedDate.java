/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Locale
 *  java.util.SimpleTimeZone
 *  java.util.TimeZone
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1.eac;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.bouncycastle.util.Arrays;

public class PackedDate {
    private byte[] time;

    public PackedDate(String string) {
        this.time = this.convert(string);
    }

    public PackedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd'Z'");
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = this.convert(simpleDateFormat.format(date));
    }

    public PackedDate(Date date, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd'Z'", locale);
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = this.convert(simpleDateFormat.format(date));
    }

    PackedDate(byte[] arrby) {
        this.time = arrby;
    }

    private byte[] convert(String string) {
        char[] arrc = string.toCharArray();
        byte[] arrby = new byte[6];
        for (int i2 = 0; i2 != 6; ++i2) {
            arrby[i2] = (byte)(-48 + arrc[i2]);
        }
        return arrby;
    }

    public boolean equals(Object object) {
        if (!(object instanceof PackedDate)) {
            return false;
        }
        PackedDate packedDate = (PackedDate)object;
        return Arrays.areEqual((byte[])this.time, (byte[])packedDate.time);
    }

    public Date getDate() {
        return new SimpleDateFormat("yyyyMMdd").parse("20" + this.toString());
    }

    public byte[] getEncoding() {
        return this.time;
    }

    public int hashCode() {
        return Arrays.hashCode((byte[])this.time);
    }

    public String toString() {
        char[] arrc = new char[this.time.length];
        for (int i2 = 0; i2 != arrc.length; ++i2) {
            arrc[i2] = (char)(48 + (255 & this.time[i2]));
        }
        return new String(arrc);
    }
}

