/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Locale
 *  java.util.SimpleTimeZone
 *  java.util.TimeZone
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.asn1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class ASN1UTCTime
extends ASN1Primitive {
    private byte[] time;

    public ASN1UTCTime(String string) {
        this.time = Strings.toByteArray((String)string);
        try {
            this.getDate();
            return;
        }
        catch (ParseException parseException) {
            throw new IllegalArgumentException("invalid date string: " + parseException.getMessage());
        }
    }

    public ASN1UTCTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray((String)simpleDateFormat.format(date));
    }

    public ASN1UTCTime(Date date, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'", locale);
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray((String)simpleDateFormat.format(date));
    }

    ASN1UTCTime(byte[] arrby) {
        this.time = arrby;
    }

    public static ASN1UTCTime getInstance(Object object) {
        if (object == null || object instanceof ASN1UTCTime) {
            return (ASN1UTCTime)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1UTCTime aSN1UTCTime = (ASN1UTCTime)ASN1UTCTime.fromByteArray((byte[])object);
                return aSN1UTCTime;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1UTCTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1UTCTime) {
            return ASN1UTCTime.getInstance(aSN1Primitive);
        }
        return new ASN1UTCTime(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1UTCTime)) {
            return false;
        }
        return Arrays.areEqual((byte[])this.time, (byte[])((ASN1UTCTime)aSN1Primitive).time);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.write(23);
        int n2 = this.time.length;
        aSN1OutputStream.writeLength(n2);
        for (int i2 = 0; i2 != n2; ++i2) {
            aSN1OutputStream.write(this.time[i2]);
        }
    }

    @Override
    int encodedLength() {
        int n2 = this.time.length;
        return n2 + (1 + StreamUtil.calculateBodyLength(n2));
    }

    public Date getAdjustedDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        return simpleDateFormat.parse(this.getAdjustedTime());
    }

    public String getAdjustedTime() {
        String string = this.getTime();
        if (string.charAt(0) < '5') {
            return "20" + string;
        }
        return "19" + string;
    }

    public Date getDate() {
        return new SimpleDateFormat("yyMMddHHmmssz").parse(this.getTime());
    }

    public String getTime() {
        String string = Strings.fromByteArray((byte[])this.time);
        if (string.indexOf(45) < 0 && string.indexOf(43) < 0) {
            if (string.length() == 11) {
                return string.substring(0, 10) + "00GMT+00:00";
            }
            return string.substring(0, 12) + "GMT+00:00";
        }
        int n2 = string.indexOf(45);
        if (n2 < 0) {
            n2 = string.indexOf(43);
        }
        if (n2 == -3 + string.length()) {
            string = string + "00";
        }
        if (n2 == 10) {
            return string.substring(0, 10) + "00GMT" + string.substring(10, 13) + ":" + string.substring(13, 15);
        }
        return string.substring(0, 12) + "GMT" + string.substring(12, 15) + ":" + string.substring(15, 17);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((byte[])this.time);
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return Strings.fromByteArray((byte[])this.time);
    }
}

