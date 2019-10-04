/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
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

public class ASN1GeneralizedTime
extends ASN1Primitive {
    private byte[] time;

    public ASN1GeneralizedTime(String string) {
        this.time = Strings.toByteArray((String)string);
        try {
            this.getDate();
            return;
        }
        catch (ParseException parseException) {
            throw new IllegalArgumentException("invalid date string: " + parseException.getMessage());
        }
    }

    public ASN1GeneralizedTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray((String)simpleDateFormat.format(date));
    }

    public ASN1GeneralizedTime(Date date, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'", locale);
        simpleDateFormat.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray((String)simpleDateFormat.format(date));
    }

    ASN1GeneralizedTime(byte[] arrby) {
        this.time = arrby;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    private String calculateGMTOffset() {
        block5 : {
            block6 : {
                var1_1 = "+";
                var2_2 = TimeZone.getDefault();
                var3_3 = var2_2.getRawOffset();
                if (var3_3 < 0) {
                    var1_1 = "-";
                    var3_3 = -var3_3;
                }
                var4_4 = var3_3 / 3600000;
                var5_5 = (var3_3 - 1000 * (60 * (var4_4 * 60))) / 60000;
                try {
                    if (!var2_2.useDaylightTime() || !var2_2.inDaylightTime(this.getDate())) break block5;
                    var8_6 = var1_1.equals((Object)"+");
                    if (!var8_6) break block6;
                    var9_7 = 1;
                }
                catch (ParseException var6_9) {
                    var7_8 = var4_4;
                    ** GOTO lbl16
                }
lbl14: // 2 sources:
                do {
                    var7_8 = var9_7 + var4_4;
lbl16: // 3 sources:
                    do {
                        return "GMT" + var1_1 + this.convert(var7_8) + ":" + this.convert(var5_5);
                        break;
                    } while (true);
                    break;
                } while (true);
            }
            var9_7 = -1;
            ** while (true)
        }
        var7_8 = var4_4;
        ** while (true)
    }

    private String convert(int n2) {
        if (n2 < 10) {
            return "0" + n2;
        }
        return Integer.toString((int)n2);
    }

    public static ASN1GeneralizedTime getInstance(Object object) {
        if (object == null || object instanceof ASN1GeneralizedTime) {
            return (ASN1GeneralizedTime)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1GeneralizedTime aSN1GeneralizedTime = (ASN1GeneralizedTime)ASN1GeneralizedTime.fromByteArray((byte[])object);
                return aSN1GeneralizedTime;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1GeneralizedTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1GeneralizedTime) {
            return ASN1GeneralizedTime.getInstance(aSN1Primitive);
        }
        return new ASN1GeneralizedTime(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    private boolean hasFractionalSeconds() {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = this.time.length;
                    bl = false;
                    if (n2 == n3) break block3;
                    if (this.time[n2] != 46 || n2 != 14) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1GeneralizedTime)) {
            return false;
        }
        return Arrays.areEqual((byte[])this.time, (byte[])((ASN1GeneralizedTime)aSN1Primitive).time);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(24, this.time);
    }

    @Override
    int encodedLength() {
        int n2 = this.time.length;
        return n2 + (1 + StreamUtil.calculateBodyLength(n2));
    }

    /*
     * Enabled aggressive block sorting
     */
    public Date getDate() {
        SimpleDateFormat simpleDateFormat;
        String string;
        String string2 = Strings.fromByteArray((byte[])this.time);
        if (string2.endsWith("Z")) {
            SimpleDateFormat simpleDateFormat2 = this.hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'") : new SimpleDateFormat("yyyyMMddHHmmss'Z'");
            simpleDateFormat2.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
            simpleDateFormat = simpleDateFormat2;
            string = string2;
        } else if (string2.indexOf(45) > 0 || string2.indexOf(43) > 0) {
            String string3 = this.getTime();
            SimpleDateFormat simpleDateFormat3 = this.hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSSz") : new SimpleDateFormat("yyyyMMddHHmmssz");
            simpleDateFormat3.setTimeZone((TimeZone)new SimpleTimeZone(0, "Z"));
            simpleDateFormat = simpleDateFormat3;
            string = string3;
        } else {
            SimpleDateFormat simpleDateFormat4 = this.hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSS") : new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat4.setTimeZone((TimeZone)new SimpleTimeZone(0, TimeZone.getDefault().getID()));
            simpleDateFormat = simpleDateFormat4;
            string = string2;
        }
        if (!this.hasFractionalSeconds()) return simpleDateFormat.parse(string);
        String string4 = string.substring(14);
        int n2 = 1;
        do {
            char c2;
            if (n2 >= string4.length() || '0' > (c2 = string4.charAt(n2)) || c2 > '9') {
                if (n2 - 1 <= 3) break;
                String string5 = string4.substring(0, 4) + string4.substring(n2);
                string = string.substring(0, 14) + string5;
                return simpleDateFormat.parse(string);
            }
            ++n2;
        } while (true);
        if (n2 - 1 == 1) {
            String string6 = string4.substring(0, n2) + "00" + string4.substring(n2);
            string = string.substring(0, 14) + string6;
            return simpleDateFormat.parse(string);
        }
        if (n2 - 1 != 2) return simpleDateFormat.parse(string);
        String string7 = string4.substring(0, n2) + "0" + string4.substring(n2);
        string = string.substring(0, 14) + string7;
        return simpleDateFormat.parse(string);
    }

    public String getTime() {
        String string = Strings.fromByteArray((byte[])this.time);
        if (string.charAt(-1 + string.length()) == 'Z') {
            return string.substring(0, -1 + string.length()) + "GMT+00:00";
        }
        int n2 = -5 + string.length();
        char c2 = string.charAt(n2);
        if (c2 == '-' || c2 == '+') {
            return string.substring(0, n2) + "GMT" + string.substring(n2, n2 + 3) + ":" + string.substring(n2 + 3);
        }
        int n3 = -3 + string.length();
        char c3 = string.charAt(n3);
        if (c3 == '-' || c3 == '+') {
            return string.substring(0, n3) + "GMT" + string.substring(n3) + ":00";
        }
        return string + this.calculateGMTOffset();
    }

    public String getTimeString() {
        return Strings.fromByteArray((byte[])this.time);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((byte[])this.time);
    }

    @Override
    boolean isConstructed() {
        return false;
    }
}

