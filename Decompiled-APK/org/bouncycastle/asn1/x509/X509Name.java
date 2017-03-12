package org.bouncycastle.asn1.x509;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class X509Name extends ASN1Object {
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier f63C;
    public static final ASN1ObjectIdentifier CN;
    public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
    public static final ASN1ObjectIdentifier DC;
    public static final ASN1ObjectIdentifier DMD_NAME;
    public static final ASN1ObjectIdentifier DN_QUALIFIER;
    public static final Hashtable DefaultLookUp;
    public static boolean DefaultReverse;
    public static final Hashtable DefaultSymbols;
    public static final ASN1ObjectIdentifier f64E;
    public static final ASN1ObjectIdentifier EmailAddress;
    private static final Boolean FALSE;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final ASN1ObjectIdentifier f65L;
    public static final ASN1ObjectIdentifier NAME;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier f66O;
    public static final Hashtable OIDLookUp;
    public static final ASN1ObjectIdentifier OU;
    public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
    public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
    public static final ASN1ObjectIdentifier POSTAL_CODE;
    public static final ASN1ObjectIdentifier PSEUDONYM;
    public static final Hashtable RFC1779Symbols;
    public static final Hashtable RFC2253Symbols;
    public static final ASN1ObjectIdentifier SERIALNUMBER;
    public static final ASN1ObjectIdentifier SN;
    public static final ASN1ObjectIdentifier ST;
    public static final ASN1ObjectIdentifier STREET;
    public static final ASN1ObjectIdentifier SURNAME;
    public static final Hashtable SymbolLookUp;
    public static final ASN1ObjectIdentifier f67T;
    public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
    private static final Boolean TRUE;
    public static final ASN1ObjectIdentifier UID;
    public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
    public static final ASN1ObjectIdentifier UnstructuredAddress;
    public static final ASN1ObjectIdentifier UnstructuredName;
    private Vector added;
    private X509NameEntryConverter converter;
    private int hashCodeValue;
    private boolean isHashCodeCalculated;
    private Vector ordering;
    private ASN1Sequence seq;
    private Vector values;

    static {
        f63C = new ASN1ObjectIdentifier("2.5.4.6");
        f66O = new ASN1ObjectIdentifier("2.5.4.10");
        OU = new ASN1ObjectIdentifier("2.5.4.11");
        f67T = new ASN1ObjectIdentifier("2.5.4.12");
        CN = new ASN1ObjectIdentifier("2.5.4.3");
        SN = new ASN1ObjectIdentifier("2.5.4.5");
        STREET = new ASN1ObjectIdentifier("2.5.4.9");
        SERIALNUMBER = SN;
        f65L = new ASN1ObjectIdentifier("2.5.4.7");
        ST = new ASN1ObjectIdentifier("2.5.4.8");
        SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
        GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
        INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
        GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
        UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
        BUSINESS_CATEGORY = new ASN1ObjectIdentifier("2.5.4.15");
        POSTAL_CODE = new ASN1ObjectIdentifier("2.5.4.17");
        DN_QUALIFIER = new ASN1ObjectIdentifier("2.5.4.46");
        PSEUDONYM = new ASN1ObjectIdentifier("2.5.4.65");
        DATE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.1");
        PLACE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.2");
        GENDER = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.3");
        COUNTRY_OF_CITIZENSHIP = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.4");
        COUNTRY_OF_RESIDENCE = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.5");
        NAME_AT_BIRTH = new ASN1ObjectIdentifier("1.3.36.8.3.14");
        POSTAL_ADDRESS = new ASN1ObjectIdentifier("2.5.4.16");
        DMD_NAME = new ASN1ObjectIdentifier("2.5.4.54");
        TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
        NAME = X509ObjectIdentifiers.id_at_name;
        EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
        UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
        UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
        f64E = EmailAddress;
        DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        DefaultReverse = false;
        DefaultSymbols = new Hashtable();
        RFC2253Symbols = new Hashtable();
        RFC1779Symbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        OIDLookUp = DefaultSymbols;
        SymbolLookUp = DefaultLookUp;
        TRUE = new Boolean(true);
        FALSE = new Boolean(false);
        DefaultSymbols.put(f63C, "C");
        DefaultSymbols.put(f66O, "O");
        DefaultSymbols.put(f67T, "T");
        DefaultSymbols.put(OU, "OU");
        DefaultSymbols.put(CN, "CN");
        DefaultSymbols.put(f65L, "L");
        DefaultSymbols.put(ST, "ST");
        DefaultSymbols.put(SN, "SERIALNUMBER");
        DefaultSymbols.put(EmailAddress, "E");
        DefaultSymbols.put(DC, "DC");
        DefaultSymbols.put(UID, "UID");
        DefaultSymbols.put(STREET, "STREET");
        DefaultSymbols.put(SURNAME, "SURNAME");
        DefaultSymbols.put(GIVENNAME, "GIVENNAME");
        DefaultSymbols.put(INITIALS, "INITIALS");
        DefaultSymbols.put(GENERATION, "GENERATION");
        DefaultSymbols.put(UnstructuredAddress, "unstructuredAddress");
        DefaultSymbols.put(UnstructuredName, "unstructuredName");
        DefaultSymbols.put(UNIQUE_IDENTIFIER, "UniqueIdentifier");
        DefaultSymbols.put(DN_QUALIFIER, "DN");
        DefaultSymbols.put(PSEUDONYM, "Pseudonym");
        DefaultSymbols.put(POSTAL_ADDRESS, "PostalAddress");
        DefaultSymbols.put(NAME_AT_BIRTH, "NameAtBirth");
        DefaultSymbols.put(COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
        DefaultSymbols.put(COUNTRY_OF_RESIDENCE, "CountryOfResidence");
        DefaultSymbols.put(GENDER, "Gender");
        DefaultSymbols.put(PLACE_OF_BIRTH, "PlaceOfBirth");
        DefaultSymbols.put(DATE_OF_BIRTH, "DateOfBirth");
        DefaultSymbols.put(POSTAL_CODE, "PostalCode");
        DefaultSymbols.put(BUSINESS_CATEGORY, "BusinessCategory");
        DefaultSymbols.put(TELEPHONE_NUMBER, "TelephoneNumber");
        DefaultSymbols.put(NAME, "Name");
        RFC2253Symbols.put(f63C, "C");
        RFC2253Symbols.put(f66O, "O");
        RFC2253Symbols.put(OU, "OU");
        RFC2253Symbols.put(CN, "CN");
        RFC2253Symbols.put(f65L, "L");
        RFC2253Symbols.put(ST, "ST");
        RFC2253Symbols.put(STREET, "STREET");
        RFC2253Symbols.put(DC, "DC");
        RFC2253Symbols.put(UID, "UID");
        RFC1779Symbols.put(f63C, "C");
        RFC1779Symbols.put(f66O, "O");
        RFC1779Symbols.put(OU, "OU");
        RFC1779Symbols.put(CN, "CN");
        RFC1779Symbols.put(f65L, "L");
        RFC1779Symbols.put(ST, "ST");
        RFC1779Symbols.put(STREET, "STREET");
        DefaultLookUp.put("c", f63C);
        DefaultLookUp.put("o", f66O);
        DefaultLookUp.put("t", f67T);
        DefaultLookUp.put("ou", OU);
        DefaultLookUp.put("cn", CN);
        DefaultLookUp.put("l", f65L);
        DefaultLookUp.put("st", ST);
        DefaultLookUp.put("sn", SN);
        DefaultLookUp.put("serialnumber", SN);
        DefaultLookUp.put("street", STREET);
        DefaultLookUp.put("emailaddress", f64E);
        DefaultLookUp.put("dc", DC);
        DefaultLookUp.put("e", f64E);
        DefaultLookUp.put("uid", UID);
        DefaultLookUp.put("surname", SURNAME);
        DefaultLookUp.put("givenname", GIVENNAME);
        DefaultLookUp.put("initials", INITIALS);
        DefaultLookUp.put("generation", GENERATION);
        DefaultLookUp.put("unstructuredaddress", UnstructuredAddress);
        DefaultLookUp.put("unstructuredname", UnstructuredName);
        DefaultLookUp.put("uniqueidentifier", UNIQUE_IDENTIFIER);
        DefaultLookUp.put("dn", DN_QUALIFIER);
        DefaultLookUp.put("pseudonym", PSEUDONYM);
        DefaultLookUp.put("postaladdress", POSTAL_ADDRESS);
        DefaultLookUp.put("nameofbirth", NAME_AT_BIRTH);
        DefaultLookUp.put("countryofcitizenship", COUNTRY_OF_CITIZENSHIP);
        DefaultLookUp.put("countryofresidence", COUNTRY_OF_RESIDENCE);
        DefaultLookUp.put("gender", GENDER);
        DefaultLookUp.put("placeofbirth", PLACE_OF_BIRTH);
        DefaultLookUp.put("dateofbirth", DATE_OF_BIRTH);
        DefaultLookUp.put("postalcode", POSTAL_CODE);
        DefaultLookUp.put("businesscategory", BUSINESS_CATEGORY);
        DefaultLookUp.put("telephonenumber", TELEPHONE_NUMBER);
        DefaultLookUp.put("name", NAME);
    }

    protected X509Name() {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
    }

    public X509Name(String str) {
        this(DefaultReverse, DefaultLookUp, str);
    }

    public X509Name(String str, X509NameEntryConverter x509NameEntryConverter) {
        this(DefaultReverse, DefaultLookUp, str, x509NameEntryConverter);
    }

    public X509Name(Hashtable hashtable) {
        this(null, hashtable);
    }

    public X509Name(Vector vector, Hashtable hashtable) {
        this(vector, hashtable, new X509DefaultEntryConverter());
    }

    public X509Name(Vector vector, Hashtable hashtable, X509NameEntryConverter x509NameEntryConverter) {
        int i = 0;
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = x509NameEntryConverter;
        if (vector != null) {
            for (int i2 = 0; i2 != vector.size(); i2++) {
                this.ordering.addElement(vector.elementAt(i2));
                this.added.addElement(FALSE);
            }
        } else {
            Enumeration keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                this.ordering.addElement(keys.nextElement());
                this.added.addElement(FALSE);
            }
        }
        while (i != this.ordering.size()) {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
            if (hashtable.get(aSN1ObjectIdentifier) == null) {
                throw new IllegalArgumentException("No attribute for object id - " + aSN1ObjectIdentifier.getId() + " - passed to distinguished name");
            }
            this.values.addElement(hashtable.get(aSN1ObjectIdentifier));
            i++;
        }
    }

    public X509Name(Vector vector, Vector vector2) {
        this(vector, vector2, new X509DefaultEntryConverter());
    }

    public X509Name(Vector vector, Vector vector2, X509NameEntryConverter x509NameEntryConverter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = x509NameEntryConverter;
        if (vector.size() != vector2.size()) {
            throw new IllegalArgumentException("oids vector must be same length as values.");
        }
        for (int i = 0; i < vector.size(); i++) {
            this.ordering.addElement(vector.elementAt(i));
            this.values.addElement(vector2.elementAt(i));
            this.added.addElement(FALSE);
        }
    }

    public X509Name(ASN1Sequence aSN1Sequence) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.seq = aSN1Sequence;
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1Set instance = ASN1Set.getInstance(((ASN1Encodable) objects.nextElement()).toASN1Primitive());
            int i = 0;
            while (i < instance.size()) {
                ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObjectAt(i).toASN1Primitive());
                if (instance2.size() != 2) {
                    throw new IllegalArgumentException("badly sized pair");
                }
                this.ordering.addElement(ASN1ObjectIdentifier.getInstance(instance2.getObjectAt(0)));
                ASN1Encodable objectAt = instance2.getObjectAt(1);
                if (!(objectAt instanceof ASN1String) || (objectAt instanceof DERUniversalString)) {
                    try {
                        this.values.addElement("#" + bytesToString(Hex.encode(objectAt.toASN1Primitive().getEncoded(ASN1Encoding.DER))));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("cannot encode value");
                    }
                }
                String string = ((ASN1String) objectAt).getString();
                if (string.length() <= 0 || string.charAt(0) != '#') {
                    this.values.addElement(string);
                } else {
                    this.values.addElement("\\" + string);
                }
                this.added.addElement(i != 0 ? TRUE : FALSE);
                i++;
            }
        }
    }

    public X509Name(boolean z, String str) {
        this(z, DefaultLookUp, str);
    }

    public X509Name(boolean z, String str, X509NameEntryConverter x509NameEntryConverter) {
        this(z, DefaultLookUp, str, x509NameEntryConverter);
    }

    public X509Name(boolean z, Hashtable hashtable, String str) {
        this(z, hashtable, str, new X509DefaultEntryConverter());
    }

    public X509Name(boolean z, Hashtable hashtable, String str, X509NameEntryConverter x509NameEntryConverter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = x509NameEntryConverter;
        X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(str);
        while (x509NameTokenizer.hasMoreTokens()) {
            String nextToken = x509NameTokenizer.nextToken();
            if (nextToken.indexOf(43) > 0) {
                X509NameTokenizer x509NameTokenizer2 = new X509NameTokenizer(nextToken, '+');
                addEntry(hashtable, x509NameTokenizer2.nextToken(), FALSE);
                while (x509NameTokenizer2.hasMoreTokens()) {
                    addEntry(hashtable, x509NameTokenizer2.nextToken(), TRUE);
                }
            } else {
                addEntry(hashtable, nextToken, FALSE);
            }
        }
        if (z) {
            Vector vector = new Vector();
            Vector vector2 = new Vector();
            Vector vector3 = new Vector();
            int i = 1;
            for (int i2 = 0; i2 < this.ordering.size(); i2++) {
                if (((Boolean) this.added.elementAt(i2)).booleanValue()) {
                    vector.insertElementAt(this.ordering.elementAt(i2), i);
                    vector2.insertElementAt(this.values.elementAt(i2), i);
                    vector3.insertElementAt(this.added.elementAt(i2), i);
                    i++;
                } else {
                    vector.insertElementAt(this.ordering.elementAt(i2), 0);
                    vector2.insertElementAt(this.values.elementAt(i2), 0);
                    vector3.insertElementAt(this.added.elementAt(i2), 0);
                    i = 1;
                }
            }
            this.ordering = vector;
            this.values = vector2;
            this.added = vector3;
        }
    }

    private void addEntry(Hashtable hashtable, String str, Boolean bool) {
        X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(str, '=');
        String nextToken = x509NameTokenizer.nextToken();
        if (x509NameTokenizer.hasMoreTokens()) {
            String nextToken2 = x509NameTokenizer.nextToken();
            this.ordering.addElement(decodeOID(nextToken, hashtable));
            this.values.addElement(unescape(nextToken2));
            this.added.addElement(bool);
            return;
        }
        throw new IllegalArgumentException("badly formatted directory string");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void appendValue(java.lang.StringBuffer r7, java.util.Hashtable r8, org.bouncycastle.asn1.ASN1ObjectIdentifier r9, java.lang.String r10) {
        /*
        r6 = this;
        r5 = 92;
        r4 = 32;
        r0 = r8.get(r9);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x004a;
    L_0x000c:
        r7.append(r0);
    L_0x000f:
        r0 = 61;
        r7.append(r0);
        r1 = r7.length();
        r7.append(r10);
        r0 = r7.length();
        r2 = r10.length();
        r3 = 2;
        if (r2 < r3) goto L_0x0038;
    L_0x0026:
        r2 = 0;
        r2 = r10.charAt(r2);
        if (r2 != r5) goto L_0x0038;
    L_0x002d:
        r2 = 1;
        r2 = r10.charAt(r2);
        r3 = 35;
        if (r2 != r3) goto L_0x0038;
    L_0x0036:
        r1 = r1 + 2;
    L_0x0038:
        if (r1 >= r0) goto L_0x0052;
    L_0x003a:
        r2 = r7.charAt(r1);
        if (r2 != r4) goto L_0x0052;
    L_0x0040:
        r2 = "\\";
        r7.insert(r1, r2);
        r1 = r1 + 2;
        r0 = r0 + 1;
        goto L_0x0038;
    L_0x004a:
        r0 = r9.getId();
        r7.append(r0);
        goto L_0x000f;
    L_0x0052:
        r0 = r0 + -1;
        if (r0 <= r1) goto L_0x0069;
    L_0x0056:
        r2 = r7.charAt(r0);
        if (r2 != r4) goto L_0x0069;
    L_0x005c:
        r7.insert(r0, r5);
        goto L_0x0052;
    L_0x0060:
        r2 = "\\";
        r7.insert(r1, r2);
        r1 = r1 + 2;
        r0 = r0 + 1;
    L_0x0069:
        if (r1 > r0) goto L_0x0075;
    L_0x006b:
        r2 = r7.charAt(r1);
        switch(r2) {
            case 34: goto L_0x0060;
            case 43: goto L_0x0060;
            case 44: goto L_0x0060;
            case 59: goto L_0x0060;
            case 60: goto L_0x0060;
            case 61: goto L_0x0060;
            case 62: goto L_0x0060;
            case 92: goto L_0x0060;
            default: goto L_0x0072;
        };
    L_0x0072:
        r1 = r1 + 1;
        goto L_0x0069;
    L_0x0075:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.asn1.x509.X509Name.appendValue(java.lang.StringBuffer, java.util.Hashtable, org.bouncycastle.asn1.ASN1ObjectIdentifier, java.lang.String):void");
    }

    private String bytesToString(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i != cArr.length; i++) {
            cArr[i] = (char) (bArr[i] & GF2Field.MASK);
        }
        return new String(cArr);
    }

    private String canonicalize(String str) {
        String toLowerCase = Strings.toLowerCase(str.trim());
        if (toLowerCase.length() > 0 && toLowerCase.charAt(0) == '#') {
            ASN1Primitive decodeObject = decodeObject(toLowerCase);
            if (decodeObject instanceof ASN1String) {
                return Strings.toLowerCase(((ASN1String) decodeObject).getString().trim());
            }
        }
        return toLowerCase;
    }

    private ASN1ObjectIdentifier decodeOID(String str, Hashtable hashtable) {
        String trim = str.trim();
        if (Strings.toUpperCase(trim).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(trim.substring(4));
        }
        if (trim.charAt(0) >= LLVARUtil.EMPTY_STRING && trim.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(trim);
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) hashtable.get(Strings.toLowerCase(trim));
        if (aSN1ObjectIdentifier != null) {
            return aSN1ObjectIdentifier;
        }
        throw new IllegalArgumentException("Unknown object id - " + trim + " - passed to distinguished name");
    }

    private ASN1Primitive decodeObject(String str) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(str.substring(1)));
        } catch (IOException e) {
            throw new IllegalStateException("unknown encoding in name: " + e);
        }
    }

    private boolean equivalentStrings(String str, String str2) {
        String canonicalize = canonicalize(str);
        String canonicalize2 = canonicalize(str2);
        return canonicalize.equals(canonicalize2) || stripInternalSpaces(canonicalize).equals(stripInternalSpaces(canonicalize2));
    }

    public static X509Name getInstance(Object obj) {
        return (obj == null || (obj instanceof X509Name)) ? (X509Name) obj : obj instanceof X500Name ? new X509Name(ASN1Sequence.getInstance(((X500Name) obj).toASN1Primitive())) : obj != null ? new X509Name(ASN1Sequence.getInstance(obj)) : null;
    }

    public static X509Name getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    private String stripInternalSpaces(String str) {
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

    private String unescape(String str) {
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
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (i = 
        /* Method generation error in method: org.bouncycastle.asn1.x509.X509Name.unescape(java.lang.String):java.lang.String
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r0_7 'i' int) = (r0_6 'i' int), (r0_19 'i' int) binds: {(r0_6 'i' int)=B:11:0x0036, (r0_19 'i' int)=B:42:0x0099} in method: org.bouncycastle.asn1.x509.X509Name.unescape(java.lang.String):java.lang.String
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
Caused by: jadx.core.utils.exceptions.CodegenException: Unknown instruction: PHI in method: org.bouncycastle.asn1.x509.X509Name.unescape(java.lang.String):java.lang.String
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:512)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:219)
	... 19 more
 */

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof X509Name) && !(obj instanceof ASN1Sequence)) {
                return false;
            }
            if (toASN1Primitive().equals(((ASN1Encodable) obj).toASN1Primitive())) {
                return true;
            }
            try {
                X509Name instance = getInstance(obj);
                int size = this.ordering.size();
                if (size != instance.ordering.size()) {
                    return false;
                }
                int i;
                int i2;
                int i3;
                boolean[] zArr = new boolean[size];
                if (this.ordering.elementAt(0).equals(instance.ordering.elementAt(0))) {
                    i = 1;
                    i2 = size;
                    i3 = 0;
                } else {
                    i3 = size - 1;
                    i = -1;
                    i2 = -1;
                }
                for (int i4 = i3; i4 != i2; i4 += i) {
                    boolean z;
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) this.ordering.elementAt(i4);
                    String str = (String) this.values.elementAt(i4);
                    int i5 = 0;
                    while (i5 < size) {
                        if (!zArr[i5] && aSN1ObjectIdentifier.equals((ASN1ObjectIdentifier) instance.ordering.elementAt(i5)) && equivalentStrings(str, (String) instance.values.elementAt(i5))) {
                            zArr[i5] = true;
                            z = true;
                            break;
                        }
                        i5++;
                    }
                    z = false;
                    if (!z) {
                        return false;
                    }
                }
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        public boolean equals(Object obj, boolean z) {
            if (!z) {
                return equals(obj);
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof X509Name) && !(obj instanceof ASN1Sequence)) {
                return false;
            }
            if (toASN1Primitive().equals(((ASN1Encodable) obj).toASN1Primitive())) {
                return true;
            }
            try {
                X509Name instance = getInstance(obj);
                int size = this.ordering.size();
                if (size != instance.ordering.size()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!((ASN1ObjectIdentifier) this.ordering.elementAt(i)).equals((ASN1ObjectIdentifier) instance.ordering.elementAt(i))) {
                        return false;
                    }
                    if (!equivalentStrings((String) this.values.elementAt(i), (String) instance.values.elementAt(i))) {
                        return false;
                    }
                }
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        public Vector getOIDs() {
            Vector vector = new Vector();
            for (int i = 0; i != this.ordering.size(); i++) {
                vector.addElement(this.ordering.elementAt(i));
            }
            return vector;
        }

        public Vector getValues() {
            Vector vector = new Vector();
            for (int i = 0; i != this.values.size(); i++) {
                vector.addElement(this.values.elementAt(i));
            }
            return vector;
        }

        public Vector getValues(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
            Vector vector = new Vector();
            for (int i = 0; i != this.values.size(); i++) {
                if (this.ordering.elementAt(i).equals(aSN1ObjectIdentifier)) {
                    String str = (String) this.values.elementAt(i);
                    if (str.length() > 2 && str.charAt(0) == '\\' && str.charAt(1) == '#') {
                        vector.addElement(str.substring(1));
                    } else {
                        vector.addElement(str);
                    }
                }
            }
            return vector;
        }

        public int hashCode() {
            if (this.isHashCodeCalculated) {
                return this.hashCodeValue;
            }
            this.isHashCodeCalculated = true;
            for (int i = 0; i != this.ordering.size(); i++) {
                String stripInternalSpaces = stripInternalSpaces(canonicalize((String) this.values.elementAt(i)));
                this.hashCodeValue ^= this.ordering.elementAt(i).hashCode();
                this.hashCodeValue = stripInternalSpaces.hashCode() ^ this.hashCodeValue;
            }
            return this.hashCodeValue;
        }

        public ASN1Primitive toASN1Primitive() {
            if (this.seq == null) {
                ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
                ASN1ObjectIdentifier aSN1ObjectIdentifier = null;
                ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
                int i = 0;
                while (i != this.ordering.size()) {
                    ASN1EncodableVector aSN1EncodableVector3;
                    ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
                    ASN1ObjectIdentifier aSN1ObjectIdentifier2 = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
                    aSN1EncodableVector4.add(aSN1ObjectIdentifier2);
                    aSN1EncodableVector4.add(this.converter.getConvertedValue(aSN1ObjectIdentifier2, (String) this.values.elementAt(i)));
                    if (aSN1ObjectIdentifier == null || ((Boolean) this.added.elementAt(i)).booleanValue()) {
                        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
                        aSN1EncodableVector3 = aSN1EncodableVector2;
                    } else {
                        aSN1EncodableVector.add(new DERSet(aSN1EncodableVector2));
                        aSN1EncodableVector3 = new ASN1EncodableVector();
                        aSN1EncodableVector3.add(new DERSequence(aSN1EncodableVector4));
                    }
                    i++;
                    aSN1EncodableVector2 = aSN1EncodableVector3;
                    aSN1ObjectIdentifier = aSN1ObjectIdentifier2;
                }
                aSN1EncodableVector.add(new DERSet(aSN1EncodableVector2));
                this.seq = new DERSequence(aSN1EncodableVector);
            }
            return this.seq;
        }

        public String toString() {
            return toString(DefaultReverse, DefaultSymbols);
        }

        public String toString(boolean z, Hashtable hashtable) {
            StringBuffer stringBuffer = new StringBuffer();
            Vector vector = new Vector();
            StringBuffer stringBuffer2 = null;
            int i = 0;
            while (i < this.ordering.size()) {
                StringBuffer stringBuffer3;
                if (((Boolean) this.added.elementAt(i)).booleanValue()) {
                    stringBuffer2.append('+');
                    appendValue(stringBuffer2, hashtable, (ASN1ObjectIdentifier) this.ordering.elementAt(i), (String) this.values.elementAt(i));
                    stringBuffer3 = stringBuffer2;
                } else {
                    stringBuffer2 = new StringBuffer();
                    appendValue(stringBuffer2, hashtable, (ASN1ObjectIdentifier) this.ordering.elementAt(i), (String) this.values.elementAt(i));
                    vector.addElement(stringBuffer2);
                    stringBuffer3 = stringBuffer2;
                }
                i++;
                stringBuffer2 = stringBuffer3;
            }
            if (z) {
                Object obj = 1;
                for (int size = vector.size() - 1; size >= 0; size--) {
                    if (obj != null) {
                        obj = null;
                    } else {
                        stringBuffer.append(',');
                    }
                    stringBuffer.append(vector.elementAt(size).toString());
                }
            } else {
                Object obj2 = 1;
                for (int i2 = 0; i2 < vector.size(); i2++) {
                    if (obj2 != null) {
                        obj2 = null;
                    } else {
                        stringBuffer.append(',');
                    }
                    stringBuffer.append(vector.elementAt(i2).toString());
                }
            }
            return stringBuffer.toString();
        }
    }
