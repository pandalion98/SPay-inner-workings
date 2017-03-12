package org.bouncycastle.asn1;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class ASN1GeneralizedTime extends ASN1Primitive {
    private byte[] time;

    public ASN1GeneralizedTime(String str) {
        this.time = Strings.toByteArray(str);
        try {
            getDate();
        } catch (ParseException e) {
            throw new IllegalArgumentException("invalid date string: " + e.getMessage());
        }
    }

    public ASN1GeneralizedTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }

    public ASN1GeneralizedTime(Date date, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'", locale);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }

    ASN1GeneralizedTime(byte[] bArr) {
        this.time = bArr;
    }

    private String calculateGMTOffset() {
        String str = "+";
        TimeZone timeZone = TimeZone.getDefault();
        int rawOffset = timeZone.getRawOffset();
        if (rawOffset < 0) {
            str = HCEClientConstants.TAG_KEY_SEPARATOR;
            rawOffset = -rawOffset;
        }
        int i = rawOffset / 3600000;
        int i2 = (rawOffset - (((i * 60) * 60) * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE)) / 60000;
        try {
            if (timeZone.useDaylightTime() && timeZone.inDaylightTime(getDate())) {
                rawOffset = (str.equals("+") ? 1 : -1) + i;
                return "GMT" + str + convert(rawOffset) + ":" + convert(i2);
            }
            rawOffset = i;
            return "GMT" + str + convert(rawOffset) + ":" + convert(i2);
        } catch (ParseException e) {
            rawOffset = i;
        }
    }

    private String convert(int i) {
        return i < 10 ? TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + i : Integer.toString(i);
    }

    public static ASN1GeneralizedTime getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1GeneralizedTime)) {
            return (ASN1GeneralizedTime) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (ASN1GeneralizedTime) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (Exception e) {
                throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1GeneralizedTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof ASN1GeneralizedTime)) ? getInstance(object) : new ASN1GeneralizedTime(((ASN1OctetString) object).getOctets());
    }

    private boolean hasFractionalSeconds() {
        int i = 0;
        while (i != this.time.length) {
            if (this.time[i] == 46 && i == 14) {
                return true;
            }
            i++;
        }
        return false;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        return !(aSN1Primitive instanceof ASN1GeneralizedTime) ? false : Arrays.areEqual(this.time, ((ASN1GeneralizedTime) aSN1Primitive).time);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(24, this.time);
    }

    int encodedLength() {
        int length = this.time.length;
        return length + (StreamUtil.calculateBodyLength(length) + 1);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Date getDate() {
        /*
        r10 = this;
        r3 = 1;
        r8 = 14;
        r7 = 0;
        r0 = r10.time;
        r1 = org.bouncycastle.util.Strings.fromByteArray(r0);
        r0 = "Z";
        r0 = r1.endsWith(r0);
        if (r0 == 0) goto L_0x008a;
    L_0x0012:
        r0 = r10.hasFractionalSeconds();
        if (r0 == 0) goto L_0x0082;
    L_0x0018:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmss.SSS'Z'";
        r0.<init>(r2);
    L_0x001f:
        r2 = new java.util.SimpleTimeZone;
        r4 = "Z";
        r2.<init>(r7, r4);
        r0.setTimeZone(r2);
        r9 = r1;
        r1 = r0;
        r0 = r9;
    L_0x002c:
        r2 = r10.hasFractionalSeconds();
        if (r2 == 0) goto L_0x007d;
    L_0x0032:
        r4 = r0.substring(r8);
        r2 = r3;
    L_0x0037:
        r5 = r4.length();
        if (r2 >= r5) goto L_0x0049;
    L_0x003d:
        r5 = r4.charAt(r2);
        r6 = 48;
        if (r6 > r5) goto L_0x0049;
    L_0x0045:
        r6 = 57;
        if (r5 <= r6) goto L_0x00ec;
    L_0x0049:
        r5 = r2 + -1;
        r6 = 3;
        if (r5 <= r6) goto L_0x00f0;
    L_0x004e:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = 4;
        r5 = r4.substring(r7, r5);
        r3 = r3.append(r5);
        r2 = r4.substring(r2);
        r2 = r3.append(r2);
        r2 = r2.toString();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r0.substring(r7, r8);
        r0 = r3.append(r0);
        r0 = r0.append(r2);
        r0 = r0.toString();
    L_0x007d:
        r0 = r1.parse(r0);
        return r0;
    L_0x0082:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmss'Z'";
        r0.<init>(r2);
        goto L_0x001f;
    L_0x008a:
        r0 = 45;
        r0 = r1.indexOf(r0);
        if (r0 > 0) goto L_0x009a;
    L_0x0092:
        r0 = 43;
        r0 = r1.indexOf(r0);
        if (r0 <= 0) goto L_0x00c2;
    L_0x009a:
        r1 = r10.getTime();
        r0 = r10.hasFractionalSeconds();
        if (r0 == 0) goto L_0x00ba;
    L_0x00a4:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmss.SSSz";
        r0.<init>(r2);
    L_0x00ab:
        r2 = new java.util.SimpleTimeZone;
        r4 = "Z";
        r2.<init>(r7, r4);
        r0.setTimeZone(r2);
        r9 = r1;
        r1 = r0;
        r0 = r9;
        goto L_0x002c;
    L_0x00ba:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmssz";
        r0.<init>(r2);
        goto L_0x00ab;
    L_0x00c2:
        r0 = r10.hasFractionalSeconds();
        if (r0 == 0) goto L_0x00e4;
    L_0x00c8:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmss.SSS";
        r0.<init>(r2);
    L_0x00cf:
        r2 = new java.util.SimpleTimeZone;
        r4 = java.util.TimeZone.getDefault();
        r4 = r4.getID();
        r2.<init>(r7, r4);
        r0.setTimeZone(r2);
        r9 = r1;
        r1 = r0;
        r0 = r9;
        goto L_0x002c;
    L_0x00e4:
        r0 = new java.text.SimpleDateFormat;
        r2 = "yyyyMMddHHmmss";
        r0.<init>(r2);
        goto L_0x00cf;
    L_0x00ec:
        r2 = r2 + 1;
        goto L_0x0037;
    L_0x00f0:
        r5 = r2 + -1;
        if (r5 != r3) goto L_0x012a;
    L_0x00f4:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = r4.substring(r7, r2);
        r3 = r3.append(r5);
        r5 = "00";
        r3 = r3.append(r5);
        r2 = r4.substring(r2);
        r2 = r3.append(r2);
        r2 = r2.toString();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r0.substring(r7, r8);
        r0 = r3.append(r0);
        r0 = r0.append(r2);
        r0 = r0.toString();
        goto L_0x007d;
    L_0x012a:
        r3 = r2 + -1;
        r5 = 2;
        if (r3 != r5) goto L_0x007d;
    L_0x012f:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = r4.substring(r7, r2);
        r3 = r3.append(r5);
        r5 = "0";
        r3 = r3.append(r5);
        r2 = r4.substring(r2);
        r2 = r3.append(r2);
        r2 = r2.toString();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r0 = r0.substring(r7, r8);
        r0 = r3.append(r0);
        r0 = r0.append(r2);
        r0 = r0.toString();
        goto L_0x007d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.asn1.ASN1GeneralizedTime.getDate():java.util.Date");
    }

    public String getTime() {
        String fromByteArray = Strings.fromByteArray(this.time);
        if (fromByteArray.charAt(fromByteArray.length() - 1) == Matrix.MATRIX_TYPE_ZERO) {
            return fromByteArray.substring(0, fromByteArray.length() - 1) + "GMT+00:00";
        }
        int length = fromByteArray.length() - 5;
        char charAt = fromByteArray.charAt(length);
        if (charAt == '-' || charAt == '+') {
            return fromByteArray.substring(0, length) + "GMT" + fromByteArray.substring(length, length + 3) + ":" + fromByteArray.substring(length + 3);
        }
        length = fromByteArray.length() - 3;
        charAt = fromByteArray.charAt(length);
        return (charAt == '-' || charAt == '+') ? fromByteArray.substring(0, length) + "GMT" + fromByteArray.substring(length) + ":00" : fromByteArray + calculateGMTOffset();
    }

    public String getTimeString() {
        return Strings.fromByteArray(this.time);
    }

    public int hashCode() {
        return Arrays.hashCode(this.time);
    }

    boolean isConstructed() {
        return false;
    }
}
