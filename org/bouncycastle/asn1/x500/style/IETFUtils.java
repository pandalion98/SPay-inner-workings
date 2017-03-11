package org.bouncycastle.asn1.x500.style;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class IETFUtils {
    public static void appendRDN(StringBuffer stringBuffer, RDN rdn, Hashtable hashtable) {
        if (rdn.isMultiValued()) {
            AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
            Object obj = 1;
            for (int i = 0; i != typesAndValues.length; i++) {
                if (obj != null) {
                    obj = null;
                } else {
                    stringBuffer.append('+');
                }
                appendTypeAndValue(stringBuffer, typesAndValues[i], hashtable);
            }
        } else if (rdn.getFirst() != null) {
            appendTypeAndValue(stringBuffer, rdn.getFirst(), hashtable);
        }
    }

    public static void appendTypeAndValue(StringBuffer stringBuffer, AttributeTypeAndValue attributeTypeAndValue, Hashtable hashtable) {
        String str = (String) hashtable.get(attributeTypeAndValue.getType());
        if (str != null) {
            stringBuffer.append(str);
        } else {
            stringBuffer.append(attributeTypeAndValue.getType().getId());
        }
        stringBuffer.append('=');
        stringBuffer.append(valueToString(attributeTypeAndValue.getValue()));
    }

    private static boolean atvAreEqual(AttributeTypeAndValue attributeTypeAndValue, AttributeTypeAndValue attributeTypeAndValue2) {
        return attributeTypeAndValue == attributeTypeAndValue2 ? true : attributeTypeAndValue == null ? false : attributeTypeAndValue2 == null ? false : !attributeTypeAndValue.getType().equals(attributeTypeAndValue2.getType()) ? false : canonicalize(valueToString(attributeTypeAndValue.getValue())).equals(canonicalize(valueToString(attributeTypeAndValue2.getValue())));
    }

    private static String bytesToString(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i != cArr.length; i++) {
            cArr[i] = (char) (bArr[i] & GF2Field.MASK);
        }
        return new String(cArr);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String canonicalize(java.lang.String r6) {
        /*
        r5 = 92;
        r4 = 32;
        r2 = 0;
        r1 = org.bouncycastle.util.Strings.toLowerCase(r6);
        r0 = r1.length();
        if (r0 <= 0) goto L_0x007a;
    L_0x000f:
        r0 = r1.charAt(r2);
        r3 = 35;
        if (r0 != r3) goto L_0x007a;
    L_0x0017:
        r0 = decodeObject(r1);
        r3 = r0 instanceof org.bouncycastle.asn1.ASN1String;
        if (r3 == 0) goto L_0x007a;
    L_0x001f:
        r0 = (org.bouncycastle.asn1.ASN1String) r0;
        r0 = r0.getString();
        r0 = org.bouncycastle.util.Strings.toLowerCase(r0);
    L_0x0029:
        r1 = r0.length();
        r3 = 1;
        if (r1 <= r3) goto L_0x0075;
    L_0x0030:
        r1 = r2;
    L_0x0031:
        r2 = r1 + 1;
        r3 = r0.length();
        if (r2 >= r3) goto L_0x004a;
    L_0x0039:
        r2 = r0.charAt(r1);
        if (r2 != r5) goto L_0x004a;
    L_0x003f:
        r2 = r1 + 1;
        r2 = r0.charAt(r2);
        if (r2 != r4) goto L_0x004a;
    L_0x0047:
        r1 = r1 + 2;
        goto L_0x0031;
    L_0x004a:
        r2 = r0.length();
        r2 = r2 + -1;
    L_0x0050:
        r3 = r2 + -1;
        if (r3 <= 0) goto L_0x0065;
    L_0x0054:
        r3 = r2 + -1;
        r3 = r0.charAt(r3);
        if (r3 != r5) goto L_0x0065;
    L_0x005c:
        r3 = r0.charAt(r2);
        if (r3 != r4) goto L_0x0065;
    L_0x0062:
        r2 = r2 + -2;
        goto L_0x0050;
    L_0x0065:
        if (r1 > 0) goto L_0x006f;
    L_0x0067:
        r3 = r0.length();
        r3 = r3 + -1;
        if (r2 >= r3) goto L_0x0075;
    L_0x006f:
        r2 = r2 + 1;
        r0 = r0.substring(r1, r2);
    L_0x0075:
        r0 = stripInternalSpaces(r0);
        return r0;
    L_0x007a:
        r0 = r1;
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.asn1.x500.style.IETFUtils.canonicalize(java.lang.String):java.lang.String");
    }

    private static int convertHex(char c) {
        return (LLVARUtil.EMPTY_STRING > c || c > '9') ? ('a' > c || c > 'f') ? (c - 65) + 10 : (c - 97) + 10 : c - 48;
    }

    public static ASN1ObjectIdentifier decodeAttrName(String str, Hashtable hashtable) {
        if (Strings.toUpperCase(str).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(str.substring(4));
        }
        if (str.charAt(0) >= LLVARUtil.EMPTY_STRING && str.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(str);
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) hashtable.get(Strings.toLowerCase(str));
        if (aSN1ObjectIdentifier != null) {
            return aSN1ObjectIdentifier;
        }
        throw new IllegalArgumentException("Unknown object id - " + str + " - passed to distinguished name");
    }

    private static ASN1Primitive decodeObject(String str) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(str.substring(1)));
        } catch (IOException e) {
            throw new IllegalStateException("unknown encoding in name: " + e);
        }
    }

    public static String[] findAttrNamesForOID(ASN1ObjectIdentifier aSN1ObjectIdentifier, Hashtable hashtable) {
        int i = 0;
        Enumeration elements = hashtable.elements();
        int i2 = 0;
        while (elements.hasMoreElements()) {
            if (aSN1ObjectIdentifier.equals(elements.nextElement())) {
                i2++;
            }
        }
        String[] strArr = new String[i2];
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            if (aSN1ObjectIdentifier.equals(hashtable.get(str))) {
                int i3 = i + 1;
                strArr[i] = str;
                i = i3;
            }
        }
        return strArr;
    }

    private static boolean isHexDigit(char c) {
        return (LLVARUtil.EMPTY_STRING <= c && c <= '9') || (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F'));
    }

    public static boolean rDNAreEqual(RDN rdn, RDN rdn2) {
        if (!rdn.isMultiValued()) {
            return !rdn2.isMultiValued() ? atvAreEqual(rdn.getFirst(), rdn2.getFirst()) : false;
        } else {
            if (!rdn2.isMultiValued()) {
                return false;
            }
            AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
            AttributeTypeAndValue[] typesAndValues2 = rdn2.getTypesAndValues();
            if (typesAndValues.length != typesAndValues2.length) {
                return false;
            }
            for (int i = 0; i != typesAndValues.length; i++) {
                if (!atvAreEqual(typesAndValues[i], typesAndValues2[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public static RDN[] rDNsFromString(String str, X500NameStyle x500NameStyle) {
        X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(str);
        X500NameBuilder x500NameBuilder = new X500NameBuilder(x500NameStyle);
        while (x500NameTokenizer.hasMoreTokens()) {
            String nextToken = x500NameTokenizer.nextToken();
            X500NameTokenizer x500NameTokenizer2;
            if (nextToken.indexOf(43) > 0) {
                x500NameTokenizer2 = new X500NameTokenizer(nextToken, '+');
                X500NameTokenizer x500NameTokenizer3 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                String nextToken2 = x500NameTokenizer3.nextToken();
                if (x500NameTokenizer3.hasMoreTokens()) {
                    nextToken = x500NameTokenizer3.nextToken();
                    ASN1ObjectIdentifier attrNameToOID = x500NameStyle.attrNameToOID(nextToken2.trim());
                    if (x500NameTokenizer2.hasMoreTokens()) {
                        Vector vector = new Vector();
                        Vector vector2 = new Vector();
                        vector.addElement(attrNameToOID);
                        vector2.addElement(unescape(nextToken));
                        while (x500NameTokenizer2.hasMoreTokens()) {
                            x500NameTokenizer3 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                            nextToken2 = x500NameTokenizer3.nextToken();
                            if (x500NameTokenizer3.hasMoreTokens()) {
                                nextToken = x500NameTokenizer3.nextToken();
                                vector.addElement(x500NameStyle.attrNameToOID(nextToken2.trim()));
                                vector2.addElement(unescape(nextToken));
                            } else {
                                throw new IllegalArgumentException("badly formatted directory string");
                            }
                        }
                        x500NameBuilder.addMultiValuedRDN(toOIDArray(vector), toValueArray(vector2));
                    } else {
                        x500NameBuilder.addRDN(attrNameToOID, unescape(nextToken));
                    }
                } else {
                    throw new IllegalArgumentException("badly formatted directory string");
                }
            }
            x500NameTokenizer2 = new X500NameTokenizer(nextToken, '=');
            nextToken = x500NameTokenizer2.nextToken();
            if (x500NameTokenizer2.hasMoreTokens()) {
                x500NameBuilder.addRDN(x500NameStyle.attrNameToOID(nextToken.trim()), unescape(x500NameTokenizer2.nextToken()));
            } else {
                throw new IllegalArgumentException("badly formatted directory string");
            }
        }
        return x500NameBuilder.build().getRDNs();
    }

    public static String stripInternalSpaces(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() != 0) {
            char charAt = str.charAt(0);
            stringBuffer.append(charAt);
            int i = 1;
            while (i < str.length()) {
                char charAt2 = str.charAt(i);
                if (charAt != ' ' || charAt2 != ' ') {
                    stringBuffer.append(charAt2);
                }
                i++;
                charAt = charAt2;
            }
        }
        return stringBuffer.toString();
    }

    private static ASN1ObjectIdentifier[] toOIDArray(Vector vector) {
        ASN1ObjectIdentifier[] aSN1ObjectIdentifierArr = new ASN1ObjectIdentifier[vector.size()];
        for (int i = 0; i != aSN1ObjectIdentifierArr.length; i++) {
            aSN1ObjectIdentifierArr[i] = (ASN1ObjectIdentifier) vector.elementAt(i);
        }
        return aSN1ObjectIdentifierArr;
    }

    private static String[] toValueArray(Vector vector) {
        String[] strArr = new String[vector.size()];
        for (int i = 0; i != strArr.length; i++) {
            strArr[i] = (String) vector.elementAt(i);
        }
        return strArr;
    }

    private static String unescape(String str) {
        if (str.length() == 0 || (str.indexOf(92) < 0 && str.indexOf(34) < 0)) {
            return str.trim();
        }
        int i;
        char[] toCharArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer(str.length());
        if (toCharArray[0] == '\\' && toCharArray[1] == '#') {
            i = 2;
            stringBuffer.append("\\#");
        } else {
            i = 0;
        }
        char c = '\u0000';
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (i = 
        /* Method generation error in method: org.bouncycastle.asn1.x500.style.IETFUtils.unescape(java.lang.String):java.lang.String
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r0_7 'i' int) = (r0_6 'i' int), (r0_19 'i' int) binds: {(r0_6 'i' int)=B:11:0x0036, (r0_19 'i' int)=B:48:0x00b9} in method: org.bouncycastle.asn1.x500.style.IETFUtils.unescape(java.lang.String):java.lang.String
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:225)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:177)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:324)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:263)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:116)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:81)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.CodegenException: Unknown instruction: PHI in method: org.bouncycastle.asn1.x500.style.IETFUtils.unescape(java.lang.String):java.lang.String
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:512)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:219)
	... 19 more
 */

        public static ASN1Encodable valueFromHexString(String str, int i) {
            byte[] bArr = new byte[((str.length() - i) / 2)];
            for (int i2 = 0; i2 != bArr.length; i2++) {
                bArr[i2] = (byte) ((convertHex(str.charAt((i2 * 2) + i)) << 4) | convertHex(str.charAt(((i2 * 2) + i) + 1)));
            }
            return ASN1Primitive.fromByteArray(bArr);
        }

        public static String valueToString(ASN1Encodable aSN1Encodable) {
            int i = 2;
            StringBuffer stringBuffer = new StringBuffer();
            if (!(aSN1Encodable instanceof ASN1String) || (aSN1Encodable instanceof DERUniversalString)) {
                try {
                    stringBuffer.append("#" + bytesToString(Hex.encode(aSN1Encodable.toASN1Primitive().getEncoded(ASN1Encoding.DER))));
                } catch (IOException e) {
                    throw new IllegalArgumentException("Other value has no encoded form");
                }
            }
            String string = ((ASN1String) aSN1Encodable).getString();
            if (string.length() <= 0 || string.charAt(0) != '#') {
                stringBuffer.append(string);
            } else {
                stringBuffer.append("\\" + string);
            }
            int length = stringBuffer.length();
            if (!(stringBuffer.length() >= 2 && stringBuffer.charAt(0) == '\\' && stringBuffer.charAt(1) == '#')) {
                i = 0;
            }
            while (i != length) {
                if (stringBuffer.charAt(i) == ',' || stringBuffer.charAt(i) == '\"' || stringBuffer.charAt(i) == '\\' || stringBuffer.charAt(i) == '+' || stringBuffer.charAt(i) == '=' || stringBuffer.charAt(i) == '<' || stringBuffer.charAt(i) == '>' || stringBuffer.charAt(i) == ';') {
                    stringBuffer.insert(i, "\\");
                    i++;
                    length++;
                }
                i++;
            }
            if (stringBuffer.length() > 0) {
                i = 0;
                while (stringBuffer.length() > i && stringBuffer.charAt(i) == ' ') {
                    stringBuffer.insert(i, "\\");
                    i += 2;
                }
            }
            i = stringBuffer.length() - 1;
            while (i >= 0 && stringBuffer.charAt(i) == ' ') {
                stringBuffer.insert(i, '\\');
                i--;
            }
            return stringBuffer.toString();
        }
    }
