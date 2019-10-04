/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Strings
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.X500NameTokenizer;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class IETFUtils {
    /*
     * Enabled aggressive block sorting
     */
    public static void appendRDN(StringBuffer stringBuffer, RDN rDN, Hashtable hashtable) {
        if (!rDN.isMultiValued()) {
            if (rDN.getFirst() == null) return;
            {
                IETFUtils.appendTypeAndValue(stringBuffer, rDN.getFirst(), hashtable);
            }
            return;
        } else {
            AttributeTypeAndValue[] arrattributeTypeAndValue = rDN.getTypesAndValues();
            boolean bl = true;
            for (int i2 = 0; i2 != arrattributeTypeAndValue.length; ++i2) {
                if (bl) {
                    bl = false;
                } else {
                    stringBuffer.append('+');
                }
                IETFUtils.appendTypeAndValue(stringBuffer, arrattributeTypeAndValue[i2], hashtable);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void appendTypeAndValue(StringBuffer stringBuffer, AttributeTypeAndValue attributeTypeAndValue, Hashtable hashtable) {
        String string = (String)hashtable.get((Object)attributeTypeAndValue.getType());
        if (string != null) {
            stringBuffer.append(string);
        } else {
            stringBuffer.append(attributeTypeAndValue.getType().getId());
        }
        stringBuffer.append('=');
        stringBuffer.append(IETFUtils.valueToString(attributeTypeAndValue.getValue()));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean atvAreEqual(AttributeTypeAndValue attributeTypeAndValue, AttributeTypeAndValue attributeTypeAndValue2) {
        block7 : {
            block6 : {
                if (attributeTypeAndValue == attributeTypeAndValue2) break block6;
                if (attributeTypeAndValue == null) {
                    return false;
                }
                if (attributeTypeAndValue2 == null) {
                    return false;
                }
                if (!attributeTypeAndValue.getType().equals(attributeTypeAndValue2.getType())) {
                    return false;
                }
                if (!IETFUtils.canonicalize(IETFUtils.valueToString(attributeTypeAndValue.getValue())).equals((Object)IETFUtils.canonicalize(IETFUtils.valueToString(attributeTypeAndValue2.getValue())))) break block7;
            }
            return true;
        }
        return false;
    }

    private static String bytesToString(byte[] arrby) {
        char[] arrc = new char[arrby.length];
        for (int i2 = 0; i2 != arrc.length; ++i2) {
            arrc[i2] = (char)(255 & arrby[i2]);
        }
        return new String(arrc);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String canonicalize(String string) {
        ASN1Primitive aSN1Primitive;
        String string2 = Strings.toLowerCase((String)string);
        String string3 = string2.length() > 0 && string2.charAt(0) == '#' && (aSN1Primitive = IETFUtils.decodeObject(string2)) instanceof ASN1String ? Strings.toLowerCase((String)((ASN1String)((Object)aSN1Primitive)).getString()) : string2;
        if (string3.length() > 1) {
            int n2 = 0;
            while (n2 + 1 < string3.length() && string3.charAt(n2) == '\\' && string3.charAt(n2 + 1) == ' ') {
                n2 += 2;
            }
            int n3 = -1 + string3.length();
            while (n3 - 1 > 0 && string3.charAt(n3 - 1) == '\\' && string3.charAt(n3) == ' ') {
                n3 -= 2;
            }
            if (n2 > 0 || n3 < -1 + string3.length()) {
                string3 = string3.substring(n2, n3 + 1);
            }
        }
        return IETFUtils.stripInternalSpaces(string3);
    }

    private static int convertHex(char c2) {
        if ('0' <= c2 && c2 <= '9') {
            return c2 - 48;
        }
        if ('a' <= c2 && c2 <= 'f') {
            return 10 + (c2 - 97);
        }
        return 10 + (c2 - 65);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ASN1ObjectIdentifier decodeAttrName(String string, Hashtable hashtable) {
        if (Strings.toUpperCase((String)string).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(string.substring(4));
        }
        if (string.charAt(0) >= '0' && string.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(string);
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)hashtable.get((Object)Strings.toLowerCase((String)string));
        if (aSN1ObjectIdentifier != null) return aSN1ObjectIdentifier;
        throw new IllegalArgumentException("Unknown object id - " + string + " - passed to distinguished name");
    }

    private static ASN1Primitive decodeObject(String string) {
        try {
            ASN1Primitive aSN1Primitive = ASN1Primitive.fromByteArray(Hex.decode((String)string.substring(1)));
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unknown encoding in name: " + (Object)((Object)iOException));
        }
    }

    public static String[] findAttrNamesForOID(ASN1ObjectIdentifier aSN1ObjectIdentifier, Hashtable hashtable) {
        int n2 = 0;
        Enumeration enumeration = hashtable.elements();
        int n3 = 0;
        while (enumeration.hasMoreElements()) {
            if (!aSN1ObjectIdentifier.equals(enumeration.nextElement())) continue;
            ++n3;
        }
        String[] arrstring = new String[n3];
        Enumeration enumeration2 = hashtable.keys();
        while (enumeration2.hasMoreElements()) {
            String string = (String)enumeration2.nextElement();
            if (!aSN1ObjectIdentifier.equals(hashtable.get((Object)string))) continue;
            int n4 = n2 + 1;
            arrstring[n2] = string;
            n2 = n4;
        }
        return arrstring;
    }

    private static boolean isHexDigit(char c2) {
        return '0' <= c2 && c2 <= '9' || 'a' <= c2 && c2 <= 'f' || 'A' <= c2 && c2 <= 'F';
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean rDNAreEqual(RDN rDN, RDN rDN2) {
        if (rDN.isMultiValued()) {
            AttributeTypeAndValue[] arrattributeTypeAndValue;
            AttributeTypeAndValue[] arrattributeTypeAndValue2;
            if (!rDN2.isMultiValued() || (arrattributeTypeAndValue2 = rDN.getTypesAndValues()).length != (arrattributeTypeAndValue = rDN2.getTypesAndValues()).length) return false;
            int n2 = 0;
            do {
                if (n2 == arrattributeTypeAndValue2.length) {
                    return true;
                }
                if (!IETFUtils.atvAreEqual(arrattributeTypeAndValue2[n2], arrattributeTypeAndValue[n2])) return false;
                {
                    ++n2;
                    continue;
                }
                break;
            } while (true);
        }
        if (!rDN2.isMultiValued()) return IETFUtils.atvAreEqual(rDN.getFirst(), rDN2.getFirst());
        return false;
    }

    public static RDN[] rDNsFromString(String string, X500NameStyle x500NameStyle) {
        X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(string);
        X500NameBuilder x500NameBuilder = new X500NameBuilder(x500NameStyle);
        while (x500NameTokenizer.hasMoreTokens()) {
            String string2 = x500NameTokenizer.nextToken();
            if (string2.indexOf(43) > 0) {
                X500NameTokenizer x500NameTokenizer2 = new X500NameTokenizer(string2, '+');
                X500NameTokenizer x500NameTokenizer3 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                String string3 = x500NameTokenizer3.nextToken();
                if (!x500NameTokenizer3.hasMoreTokens()) {
                    throw new IllegalArgumentException("badly formatted directory string");
                }
                String string4 = x500NameTokenizer3.nextToken();
                ASN1ObjectIdentifier aSN1ObjectIdentifier = x500NameStyle.attrNameToOID(string3.trim());
                if (x500NameTokenizer2.hasMoreTokens()) {
                    Vector vector = new Vector();
                    Vector vector2 = new Vector();
                    vector.addElement((Object)aSN1ObjectIdentifier);
                    vector2.addElement((Object)IETFUtils.unescape(string4));
                    while (x500NameTokenizer2.hasMoreTokens()) {
                        X500NameTokenizer x500NameTokenizer4 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                        String string5 = x500NameTokenizer4.nextToken();
                        if (!x500NameTokenizer4.hasMoreTokens()) {
                            throw new IllegalArgumentException("badly formatted directory string");
                        }
                        String string6 = x500NameTokenizer4.nextToken();
                        vector.addElement((Object)x500NameStyle.attrNameToOID(string5.trim()));
                        vector2.addElement((Object)IETFUtils.unescape(string6));
                    }
                    x500NameBuilder.addMultiValuedRDN(IETFUtils.toOIDArray(vector), IETFUtils.toValueArray(vector2));
                    continue;
                }
                x500NameBuilder.addRDN(aSN1ObjectIdentifier, IETFUtils.unescape(string4));
                continue;
            }
            X500NameTokenizer x500NameTokenizer5 = new X500NameTokenizer(string2, '=');
            String string7 = x500NameTokenizer5.nextToken();
            if (!x500NameTokenizer5.hasMoreTokens()) {
                throw new IllegalArgumentException("badly formatted directory string");
            }
            String string8 = x500NameTokenizer5.nextToken();
            x500NameBuilder.addRDN(x500NameStyle.attrNameToOID(string7.trim()), IETFUtils.unescape(string8));
        }
        return x500NameBuilder.build().getRDNs();
    }

    public static String stripInternalSpaces(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        if (string.length() != 0) {
            char c2 = string.charAt(0);
            stringBuffer.append(c2);
            for (int i2 = 1; i2 < string.length(); ++i2) {
                char c3 = string.charAt(i2);
                if (c2 != ' ' || c3 != ' ') {
                    stringBuffer.append(c3);
                }
                c2 = c3;
            }
        }
        return stringBuffer.toString();
    }

    private static ASN1ObjectIdentifier[] toOIDArray(Vector vector) {
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[vector.size()];
        for (int i2 = 0; i2 != arraSN1ObjectIdentifier.length; ++i2) {
            arraSN1ObjectIdentifier[i2] = (ASN1ObjectIdentifier)vector.elementAt(i2);
        }
        return arraSN1ObjectIdentifier;
    }

    private static String[] toValueArray(Vector vector) {
        String[] arrstring = new String[vector.size()];
        for (int i2 = 0; i2 != arrstring.length; ++i2) {
            arrstring[i2] = (String)vector.elementAt(i2);
        }
        return arrstring;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String unescape(String string) {
        int n2;
        if (string.length() == 0 || string.indexOf(92) < 0 && string.indexOf(34) < 0) {
            return string.trim();
        }
        char[] arrc = string.toCharArray();
        StringBuffer stringBuffer = new StringBuffer(string.length());
        if (arrc[0] == '\\' && arrc[1] == '#') {
            n2 = 2;
            stringBuffer.append("\\#");
        } else {
            n2 = 0;
        }
        char c2 = '\u0000';
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        while (n2 != arrc.length) {
            char c3 = arrc[n2];
            if (c3 != ' ') {
                bl = true;
            }
            if (c3 == '\"') {
                if (!bl3) {
                    bl2 = !bl2;
                } else {
                    stringBuffer.append(c3);
                }
                bl3 = false;
            } else if (c3 == '\\' && !bl3 && !bl2) {
                n3 = stringBuffer.length();
                bl3 = true;
            } else if (c3 != ' ' || bl3 || bl) {
                if (bl3 && IETFUtils.isHexDigit(c3)) {
                    if (c2 != '\u0000') {
                        stringBuffer.append((char)(16 * IETFUtils.convertHex(c2) + IETFUtils.convertHex(c3)));
                        c2 = '\u0000';
                        bl3 = false;
                    } else {
                        c2 = c3;
                    }
                } else {
                    stringBuffer.append(c3);
                    bl3 = false;
                }
            }
            ++n2;
        }
        if (stringBuffer.length() > 0) {
            while (stringBuffer.charAt(-1 + stringBuffer.length()) == ' ' && n3 != -1 + stringBuffer.length()) {
                stringBuffer.setLength(-1 + stringBuffer.length());
            }
        }
        return stringBuffer.toString();
    }

    public static ASN1Encodable valueFromHexString(String string, int n2) {
        byte[] arrby = new byte[(string.length() - n2) / 2];
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            char c2 = string.charAt(n2 + i2 * 2);
            char c3 = string.charAt(1 + (n2 + i2 * 2));
            arrby[i2] = (byte)(IETFUtils.convertHex(c2) << 4 | IETFUtils.convertHex(c3));
        }
        return ASN1Primitive.fromByteArray(arrby);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static String valueToString(ASN1Encodable aSN1Encodable) {
        int n2;
        int n3;
        StringBuffer stringBuffer;
        block12 : {
            block11 : {
                n3 = 2;
                stringBuffer = new StringBuffer();
                if (aSN1Encodable instanceof ASN1String && !(aSN1Encodable instanceof DERUniversalString)) {
                    String string = ((ASN1String)((Object)aSN1Encodable)).getString();
                    if (string.length() > 0 && string.charAt(0) == '#') {
                        stringBuffer.append("\\" + string);
                    } else {
                        stringBuffer.append(string);
                    }
                } else {
                    stringBuffer.append("#" + IETFUtils.bytesToString(Hex.encode((byte[])aSN1Encodable.toASN1Primitive().getEncoded("DER"))));
                }
                n2 = stringBuffer.length();
                if (stringBuffer.length() < n3 || stringBuffer.charAt(0) != '\\' || stringBuffer.charAt(1) != '#') break block11;
                break block12;
                catch (IOException iOException) {
                    throw new IllegalArgumentException("Other value has no encoded form");
                }
            }
            n3 = 0;
        }
        while (n3 != n2) {
            if (stringBuffer.charAt(n3) == ',' || stringBuffer.charAt(n3) == '\"' || stringBuffer.charAt(n3) == '\\' || stringBuffer.charAt(n3) == '+' || stringBuffer.charAt(n3) == '=' || stringBuffer.charAt(n3) == '<' || stringBuffer.charAt(n3) == '>' || stringBuffer.charAt(n3) == ';') {
                stringBuffer.insert(n3, "\\");
                ++n3;
                ++n2;
            }
            ++n3;
        }
        if (stringBuffer.length() > 0) {
            for (int i2 = 0; stringBuffer.length() > i2 && stringBuffer.charAt(i2) == ' '; i2 += 2) {
                stringBuffer.insert(i2, "\\");
            }
        }
        int i3 = -1 + stringBuffer.length();
        while (i3 >= 0) {
            if (stringBuffer.charAt(i3) != ' ') return stringBuffer.toString();
            stringBuffer.insert(i3, '\\');
            --i3;
        }
        return stringBuffer.toString();
    }
}

