/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Locale
 *  java.util.SimpleTimeZone
 *  java.util.TimeZone
 */
package org.bouncycastle.asn1.cms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERUTCTime;

public class Time
extends ASN1Object
implements ASN1Choice {
    ASN1Primitive time;

    public Time(Date date) {
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(0, "Z");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone((TimeZone)simpleTimeZone);
        String string = simpleDateFormat.format(date) + "Z";
        int n2 = Integer.parseInt((String)string.substring(0, 4));
        if (n2 < 1950 || n2 > 2049) {
            this.time = new DERGeneralizedTime(string);
            return;
        }
        this.time = new DERUTCTime(string.substring(2));
    }

    public Time(Date date, Locale locale) {
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(0, "Z");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", locale);
        simpleDateFormat.setTimeZone((TimeZone)simpleTimeZone);
        String string = simpleDateFormat.format(date) + "Z";
        int n2 = Integer.parseInt((String)string.substring(0, 4));
        if (n2 < 1950 || n2 > 2049) {
            this.time = new DERGeneralizedTime(string);
            return;
        }
        this.time = new DERUTCTime(string.substring(2));
    }

    public Time(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1UTCTime) && !(aSN1Primitive instanceof ASN1GeneralizedTime)) {
            throw new IllegalArgumentException("unknown object passed to Time");
        }
        this.time = aSN1Primitive;
    }

    public static Time getInstance(Object object) {
        if (object == null || object instanceof Time) {
            return (Time)object;
        }
        if (object instanceof ASN1UTCTime) {
            return new Time((ASN1UTCTime)object);
        }
        if (object instanceof ASN1GeneralizedTime) {
            return new Time((ASN1GeneralizedTime)object);
        }
        throw new IllegalArgumentException("unknown object in factory: " + object.getClass().getName());
    }

    public static Time getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return Time.getInstance(aSN1TaggedObject.getObject());
    }

    public Date getDate() {
        try {
            if (this.time instanceof ASN1UTCTime) {
                return ((ASN1UTCTime)this.time).getAdjustedDate();
            }
            Date date = ((ASN1GeneralizedTime)this.time).getDate();
            return date;
        }
        catch (ParseException parseException) {
            throw new IllegalStateException("invalid date string: " + parseException.getMessage());
        }
    }

    public String getTime() {
        if (this.time instanceof ASN1UTCTime) {
            return ((ASN1UTCTime)this.time).getAdjustedTime();
        }
        return ((ASN1GeneralizedTime)this.time).getTime();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.time;
    }
}

