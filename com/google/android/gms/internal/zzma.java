package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.location.places.Place;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class zzma {
    private static final Pattern zzRP;
    private static final Pattern zzRQ;

    static {
        zzRP = Pattern.compile("\\\\.");
        zzRQ = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }

    public static String zzbt(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = zzRQ.matcher(str);
        StringBuffer stringBuffer = null;
        while (matcher.find()) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            switch (matcher.group().charAt(0)) {
                case X509KeyUsage.keyAgreement /*8*/:
                    matcher.appendReplacement(stringBuffer, "\\\\b");
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    matcher.appendReplacement(stringBuffer, "\\\\t");
                    break;
                case NamedCurve.sect283r1 /*10*/:
                    matcher.appendReplacement(stringBuffer, "\\\\n");
                    break;
                case CertStatus.UNDETERMINED /*12*/:
                    matcher.appendReplacement(stringBuffer, "\\\\f");
                    break;
                case NamedCurve.sect571k1 /*13*/:
                    matcher.appendReplacement(stringBuffer, "\\\\r");
                    break;
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\"");
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    matcher.appendReplacement(stringBuffer, "\\\\/");
                    break;
                case EACTags.TAG_LIST /*92*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\\");
                    break;
                default:
                    break;
            }
        }
        if (stringBuffer == null) {
            return str;
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean zzd(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if ((obj instanceof JSONObject) && (obj2 instanceof JSONObject)) {
            JSONObject jSONObject = (JSONObject) obj;
            JSONObject jSONObject2 = (JSONObject) obj2;
            if (jSONObject.length() != jSONObject2.length()) {
                return false;
            }
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!jSONObject2.has(str)) {
                    return false;
                }
                try {
                    if (!zzd(jSONObject.get(str), jSONObject2.get(str))) {
                        return false;
                    }
                } catch (JSONException e) {
                    return false;
                }
            }
            return true;
        } else if (!(obj instanceof JSONArray) || !(obj2 instanceof JSONArray)) {
            return obj.equals(obj2);
        } else {
            JSONArray jSONArray = (JSONArray) obj;
            JSONArray jSONArray2 = (JSONArray) obj2;
            if (jSONArray.length() != jSONArray2.length()) {
                return false;
            }
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    if (!zzd(jSONArray.get(i), jSONArray2.get(i))) {
                        return false;
                    }
                    i++;
                } catch (JSONException e2) {
                    return false;
                }
            }
            return true;
        }
    }
}
