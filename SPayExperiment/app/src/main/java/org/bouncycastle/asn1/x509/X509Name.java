/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Boolean
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
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
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
import org.bouncycastle.asn1.x509.X509DefaultEntryConverter;
import org.bouncycastle.asn1.x509.X509NameEntryConverter;
import org.bouncycastle.asn1.x509.X509NameTokenizer;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class X509Name
extends ASN1Object {
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier C;
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
    public static final ASN1ObjectIdentifier E;
    public static final ASN1ObjectIdentifier EmailAddress;
    private static final Boolean FALSE;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final ASN1ObjectIdentifier L;
    public static final ASN1ObjectIdentifier NAME;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier O;
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
    public static final ASN1ObjectIdentifier T;
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
        C = new ASN1ObjectIdentifier("2.5.4.6");
        O = new ASN1ObjectIdentifier("2.5.4.10");
        OU = new ASN1ObjectIdentifier("2.5.4.11");
        T = new ASN1ObjectIdentifier("2.5.4.12");
        CN = new ASN1ObjectIdentifier("2.5.4.3");
        SN = new ASN1ObjectIdentifier("2.5.4.5");
        STREET = new ASN1ObjectIdentifier("2.5.4.9");
        SERIALNUMBER = SN;
        L = new ASN1ObjectIdentifier("2.5.4.7");
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
        E = EmailAddress;
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
        DefaultSymbols.put((Object)C, (Object)"C");
        DefaultSymbols.put((Object)O, (Object)"O");
        DefaultSymbols.put((Object)T, (Object)"T");
        DefaultSymbols.put((Object)OU, (Object)"OU");
        DefaultSymbols.put((Object)CN, (Object)"CN");
        DefaultSymbols.put((Object)L, (Object)"L");
        DefaultSymbols.put((Object)ST, (Object)"ST");
        DefaultSymbols.put((Object)SN, (Object)"SERIALNUMBER");
        DefaultSymbols.put((Object)EmailAddress, (Object)"E");
        DefaultSymbols.put((Object)DC, (Object)"DC");
        DefaultSymbols.put((Object)UID, (Object)"UID");
        DefaultSymbols.put((Object)STREET, (Object)"STREET");
        DefaultSymbols.put((Object)SURNAME, (Object)"SURNAME");
        DefaultSymbols.put((Object)GIVENNAME, (Object)"GIVENNAME");
        DefaultSymbols.put((Object)INITIALS, (Object)"INITIALS");
        DefaultSymbols.put((Object)GENERATION, (Object)"GENERATION");
        DefaultSymbols.put((Object)UnstructuredAddress, (Object)"unstructuredAddress");
        DefaultSymbols.put((Object)UnstructuredName, (Object)"unstructuredName");
        DefaultSymbols.put((Object)UNIQUE_IDENTIFIER, (Object)"UniqueIdentifier");
        DefaultSymbols.put((Object)DN_QUALIFIER, (Object)"DN");
        DefaultSymbols.put((Object)PSEUDONYM, (Object)"Pseudonym");
        DefaultSymbols.put((Object)POSTAL_ADDRESS, (Object)"PostalAddress");
        DefaultSymbols.put((Object)NAME_AT_BIRTH, (Object)"NameAtBirth");
        DefaultSymbols.put((Object)COUNTRY_OF_CITIZENSHIP, (Object)"CountryOfCitizenship");
        DefaultSymbols.put((Object)COUNTRY_OF_RESIDENCE, (Object)"CountryOfResidence");
        DefaultSymbols.put((Object)GENDER, (Object)"Gender");
        DefaultSymbols.put((Object)PLACE_OF_BIRTH, (Object)"PlaceOfBirth");
        DefaultSymbols.put((Object)DATE_OF_BIRTH, (Object)"DateOfBirth");
        DefaultSymbols.put((Object)POSTAL_CODE, (Object)"PostalCode");
        DefaultSymbols.put((Object)BUSINESS_CATEGORY, (Object)"BusinessCategory");
        DefaultSymbols.put((Object)TELEPHONE_NUMBER, (Object)"TelephoneNumber");
        DefaultSymbols.put((Object)NAME, (Object)"Name");
        RFC2253Symbols.put((Object)C, (Object)"C");
        RFC2253Symbols.put((Object)O, (Object)"O");
        RFC2253Symbols.put((Object)OU, (Object)"OU");
        RFC2253Symbols.put((Object)CN, (Object)"CN");
        RFC2253Symbols.put((Object)L, (Object)"L");
        RFC2253Symbols.put((Object)ST, (Object)"ST");
        RFC2253Symbols.put((Object)STREET, (Object)"STREET");
        RFC2253Symbols.put((Object)DC, (Object)"DC");
        RFC2253Symbols.put((Object)UID, (Object)"UID");
        RFC1779Symbols.put((Object)C, (Object)"C");
        RFC1779Symbols.put((Object)O, (Object)"O");
        RFC1779Symbols.put((Object)OU, (Object)"OU");
        RFC1779Symbols.put((Object)CN, (Object)"CN");
        RFC1779Symbols.put((Object)L, (Object)"L");
        RFC1779Symbols.put((Object)ST, (Object)"ST");
        RFC1779Symbols.put((Object)STREET, (Object)"STREET");
        DefaultLookUp.put((Object)"c", (Object)C);
        DefaultLookUp.put((Object)"o", (Object)O);
        DefaultLookUp.put((Object)"t", (Object)T);
        DefaultLookUp.put((Object)"ou", (Object)OU);
        DefaultLookUp.put((Object)"cn", (Object)CN);
        DefaultLookUp.put((Object)"l", (Object)L);
        DefaultLookUp.put((Object)"st", (Object)ST);
        DefaultLookUp.put((Object)"sn", (Object)SN);
        DefaultLookUp.put((Object)"serialnumber", (Object)SN);
        DefaultLookUp.put((Object)"street", (Object)STREET);
        DefaultLookUp.put((Object)"emailaddress", (Object)E);
        DefaultLookUp.put((Object)"dc", (Object)DC);
        DefaultLookUp.put((Object)"e", (Object)E);
        DefaultLookUp.put((Object)"uid", (Object)UID);
        DefaultLookUp.put((Object)"surname", (Object)SURNAME);
        DefaultLookUp.put((Object)"givenname", (Object)GIVENNAME);
        DefaultLookUp.put((Object)"initials", (Object)INITIALS);
        DefaultLookUp.put((Object)"generation", (Object)GENERATION);
        DefaultLookUp.put((Object)"unstructuredaddress", (Object)UnstructuredAddress);
        DefaultLookUp.put((Object)"unstructuredname", (Object)UnstructuredName);
        DefaultLookUp.put((Object)"uniqueidentifier", (Object)UNIQUE_IDENTIFIER);
        DefaultLookUp.put((Object)"dn", (Object)DN_QUALIFIER);
        DefaultLookUp.put((Object)"pseudonym", (Object)PSEUDONYM);
        DefaultLookUp.put((Object)"postaladdress", (Object)POSTAL_ADDRESS);
        DefaultLookUp.put((Object)"nameofbirth", (Object)NAME_AT_BIRTH);
        DefaultLookUp.put((Object)"countryofcitizenship", (Object)COUNTRY_OF_CITIZENSHIP);
        DefaultLookUp.put((Object)"countryofresidence", (Object)COUNTRY_OF_RESIDENCE);
        DefaultLookUp.put((Object)"gender", (Object)GENDER);
        DefaultLookUp.put((Object)"placeofbirth", (Object)PLACE_OF_BIRTH);
        DefaultLookUp.put((Object)"dateofbirth", (Object)DATE_OF_BIRTH);
        DefaultLookUp.put((Object)"postalcode", (Object)POSTAL_CODE);
        DefaultLookUp.put((Object)"businesscategory", (Object)BUSINESS_CATEGORY);
        DefaultLookUp.put((Object)"telephonenumber", (Object)TELEPHONE_NUMBER);
        DefaultLookUp.put((Object)"name", (Object)NAME);
    }

    protected X509Name() {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
    }

    public X509Name(String string) {
        this(DefaultReverse, DefaultLookUp, string);
    }

    public X509Name(String string, X509NameEntryConverter x509NameEntryConverter) {
        this(DefaultReverse, DefaultLookUp, string, x509NameEntryConverter);
    }

    public X509Name(Hashtable hashtable) {
        this(null, hashtable);
    }

    public X509Name(Vector vector, Hashtable hashtable) {
        this(vector, hashtable, (X509NameEntryConverter)new X509DefaultEntryConverter());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public X509Name(Vector var1_1, Hashtable var2_2, X509NameEntryConverter var3_3) {
        block4 : {
            super();
            this.converter = null;
            this.ordering = new Vector();
            this.values = new Vector();
            this.added = new Vector();
            this.converter = var3_3;
            if (var1_1 == null) break block4;
            var8_4 = 0;
            do {
                var9_6 = var1_1.size();
                var6_5 = 0;
                if (var8_4 != var9_6) {
                    this.ordering.addElement(var1_1.elementAt(var8_4));
                    this.added.addElement((Object)X509Name.FALSE);
                    ++var8_4;
                    continue;
                }
                ** GOTO lbl29
                break;
            } while (true);
        }
        var4_7 = var2_2.keys();
        do {
            var5_8 = var4_7.hasMoreElements();
            var6_5 = 0;
            if (!var5_8) ** GOTO lbl29
            this.ordering.addElement(var4_7.nextElement());
            this.added.addElement((Object)X509Name.FALSE);
        } while (true);
lbl-1000: // 1 sources:
        {
            this.values.addElement(var2_2.get((Object)var7_9));
            ++var6_5;
lbl29: // 3 sources:
            if (var6_5 == this.ordering.size()) return;
            ** while (var2_2.get((Object)(var7_9 = (ASN1ObjectIdentifier)this.ordering.elementAt((int)var6_5))) != null)
        }
lbl31: // 1 sources:
        throw new IllegalArgumentException("No attribute for object id - " + var7_9.getId() + " - passed to distinguished name");
    }

    public X509Name(Vector vector, Vector vector2) {
        this(vector, vector2, (X509NameEntryConverter)new X509DefaultEntryConverter());
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
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            this.ordering.addElement(vector.elementAt(i2));
            this.values.addElement(vector2.elementAt(i2));
            this.added.addElement((Object)FALSE);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public X509Name(ASN1Sequence aSN1Sequence) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.seq = aSN1Sequence;
        Enumeration enumeration = aSN1Sequence.getObjects();
        block2 : do {
            if (!enumeration.hasMoreElements()) return;
            ASN1Set aSN1Set = ASN1Set.getInstance(((ASN1Encodable)enumeration.nextElement()).toASN1Primitive());
            int n2 = 0;
            do {
                if (n2 >= aSN1Set.size()) continue block2;
                ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Set.getObjectAt(n2).toASN1Primitive());
                if (aSN1Sequence2.size() != 2) {
                    throw new IllegalArgumentException("badly sized pair");
                }
                this.ordering.addElement((Object)ASN1ObjectIdentifier.getInstance(aSN1Sequence2.getObjectAt(0)));
                ASN1Encodable aSN1Encodable = aSN1Sequence2.getObjectAt(1);
                if (aSN1Encodable instanceof ASN1String && !(aSN1Encodable instanceof DERUniversalString)) {
                    String string = ((ASN1String)((Object)aSN1Encodable)).getString();
                    if (string.length() > 0 && string.charAt(0) == '#') {
                        this.values.addElement((Object)("\\" + string));
                    } else {
                        this.values.addElement((Object)string);
                    }
                } else {
                    this.values.addElement((Object)("#" + this.bytesToString(Hex.encode((byte[])aSN1Encodable.toASN1Primitive().getEncoded("DER")))));
                }
                Vector vector = this.added;
                Boolean bl = n2 != 0 ? TRUE : FALSE;
                vector.addElement((Object)bl);
                ++n2;
            } while (true);
            break;
        } while (true);
        catch (IOException iOException) {
            throw new IllegalArgumentException("cannot encode value");
        }
    }

    public X509Name(boolean bl, String string) {
        this(bl, DefaultLookUp, string);
    }

    public X509Name(boolean bl, String string, X509NameEntryConverter x509NameEntryConverter) {
        this(bl, DefaultLookUp, string, x509NameEntryConverter);
    }

    public X509Name(boolean bl, Hashtable hashtable, String string) {
        this(bl, hashtable, string, new X509DefaultEntryConverter());
    }

    /*
     * Enabled aggressive block sorting
     */
    public X509Name(boolean bl, Hashtable hashtable, String string, X509NameEntryConverter x509NameEntryConverter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = x509NameEntryConverter;
        X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(string);
        while (x509NameTokenizer.hasMoreTokens()) {
            String string2 = x509NameTokenizer.nextToken();
            if (string2.indexOf(43) > 0) {
                X509NameTokenizer x509NameTokenizer2 = new X509NameTokenizer(string2, '+');
                this.addEntry(hashtable, x509NameTokenizer2.nextToken(), FALSE);
                while (x509NameTokenizer2.hasMoreTokens()) {
                    this.addEntry(hashtable, x509NameTokenizer2.nextToken(), TRUE);
                }
                continue;
            }
            this.addEntry(hashtable, string2, FALSE);
        }
        if (bl) {
            Vector vector = new Vector();
            Vector vector2 = new Vector();
            Vector vector3 = new Vector();
            int n2 = 1;
            for (int i2 = 0; i2 < this.ordering.size(); ++i2) {
                if (((Boolean)this.added.elementAt(i2)).booleanValue()) {
                    vector.insertElementAt(this.ordering.elementAt(i2), n2);
                    vector2.insertElementAt(this.values.elementAt(i2), n2);
                    vector3.insertElementAt(this.added.elementAt(i2), n2);
                    ++n2;
                    continue;
                }
                vector.insertElementAt(this.ordering.elementAt(i2), 0);
                vector2.insertElementAt(this.values.elementAt(i2), 0);
                vector3.insertElementAt(this.added.elementAt(i2), 0);
                n2 = 1;
            }
            this.ordering = vector;
            this.values = vector2;
            this.added = vector3;
        }
    }

    private void addEntry(Hashtable hashtable, String string, Boolean bl) {
        X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(string, '=');
        String string2 = x509NameTokenizer.nextToken();
        if (!x509NameTokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("badly formatted directory string");
        }
        String string3 = x509NameTokenizer.nextToken();
        ASN1ObjectIdentifier aSN1ObjectIdentifier = this.decodeOID(string2, hashtable);
        this.ordering.addElement((Object)aSN1ObjectIdentifier);
        this.values.addElement((Object)this.unescape(string3));
        this.added.addElement((Object)bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void appendValue(StringBuffer stringBuffer, Hashtable hashtable, ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        String string2 = (String)hashtable.get((Object)aSN1ObjectIdentifier);
        if (string2 != null) {
            stringBuffer.append(string2);
        } else {
            stringBuffer.append(aSN1ObjectIdentifier.getId());
        }
        stringBuffer.append('=');
        int n2 = stringBuffer.length();
        stringBuffer.append(string);
        int n3 = stringBuffer.length();
        if (string.length() >= 2 && string.charAt(0) == '\\' && string.charAt(1) == '#') {
            n2 += 2;
        }
        while (n2 < n3 && stringBuffer.charAt(n2) == ' ') {
            stringBuffer.insert(n2, "\\");
            n2 += 2;
            ++n3;
        }
        while (--n3 > n2 && stringBuffer.charAt(n3) == ' ') {
            stringBuffer.insert(n3, '\\');
        }
        block5 : while (n2 <= n3) {
            switch (stringBuffer.charAt(n2)) {
                case '\"': 
                case '+': 
                case ',': 
                case ';': 
                case '<': 
                case '=': 
                case '>': 
                case '\\': {
                    stringBuffer.insert(n2, "\\");
                    n2 += 2;
                    ++n3;
                    continue block5;
                }
            }
            ++n2;
        }
        return;
    }

    private String bytesToString(byte[] arrby) {
        char[] arrc = new char[arrby.length];
        for (int i2 = 0; i2 != arrc.length; ++i2) {
            arrc[i2] = (char)(255 & arrby[i2]);
        }
        return new String(arrc);
    }

    private String canonicalize(String string) {
        ASN1Primitive aSN1Primitive;
        String string2 = Strings.toLowerCase((String)string.trim());
        if (string2.length() > 0 && string2.charAt(0) == '#' && (aSN1Primitive = this.decodeObject(string2)) instanceof ASN1String) {
            return Strings.toLowerCase((String)((ASN1String)((Object)aSN1Primitive)).getString().trim());
        }
        return string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private ASN1ObjectIdentifier decodeOID(String string, Hashtable hashtable) {
        String string2 = string.trim();
        if (Strings.toUpperCase((String)string2).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(string2.substring(4));
        }
        if (string2.charAt(0) >= '0' && string2.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(string2);
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)hashtable.get((Object)Strings.toLowerCase((String)string2));
        if (aSN1ObjectIdentifier != null) return aSN1ObjectIdentifier;
        throw new IllegalArgumentException("Unknown object id - " + string2 + " - passed to distinguished name");
    }

    private ASN1Primitive decodeObject(String string) {
        try {
            ASN1Primitive aSN1Primitive = ASN1Primitive.fromByteArray(Hex.decode((String)string.substring(1)));
            return aSN1Primitive;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unknown encoding in name: " + (Object)((Object)iOException));
        }
    }

    private boolean equivalentStrings(String string, String string2) {
        String string3;
        String string4 = this.canonicalize(string);
        return string4.equals((Object)(string3 = this.canonicalize(string2))) || this.stripInternalSpaces(string4).equals((Object)this.stripInternalSpaces(string3));
    }

    public static X509Name getInstance(Object object) {
        if (object == null || object instanceof X509Name) {
            return (X509Name)object;
        }
        if (object instanceof X500Name) {
            return new X509Name(ASN1Sequence.getInstance(((X500Name)object).toASN1Primitive()));
        }
        if (object != null) {
            return new X509Name(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static X509Name getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return X509Name.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    private String stripInternalSpaces(String string) {
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

    /*
     * Enabled aggressive block sorting
     */
    private String unescape(String string) {
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
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        while (n2 != arrc.length) {
            char c2 = arrc[n2];
            if (c2 != ' ') {
                bl = true;
            }
            if (c2 == '\"') {
                if (!bl3) {
                    bl2 = !bl2;
                } else {
                    stringBuffer.append(c2);
                }
                bl3 = false;
            } else if (c2 == '\\' && !bl3 && !bl2) {
                n3 = stringBuffer.length();
                bl3 = true;
            } else if (c2 != ' ' || bl3 || bl) {
                stringBuffer.append(c2);
                bl3 = false;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean equals(Object object) {
        int n2;
        boolean bl;
        int n3;
        X509Name x509Name;
        int n4;
        if (object == this) {
            return true;
        }
        if (!(object instanceof X509Name)) {
            boolean bl2 = object instanceof ASN1Sequence;
            bl = false;
            if (!bl2) return bl;
        }
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
        if (this.toASN1Primitive().equals(aSN1Primitive)) {
            return true;
        }
        try {
            x509Name = X509Name.getInstance(object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
        int n5 = this.ordering.size();
        int n6 = x509Name.ordering.size();
        bl = false;
        if (n5 != n6) return bl;
        boolean[] arrbl = new boolean[n5];
        if (this.ordering.elementAt(0).equals(x509Name.ordering.elementAt(0))) {
            n3 = 1;
            n2 = n5;
            n4 = 0;
        } else {
            n4 = n5 - 1;
            n3 = -1;
            n2 = -1;
        }
        int n7 = n4;
        do {
            block11 : {
                if (n7 == n2) {
                    return true;
                }
                ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)this.ordering.elementAt(n7);
                String string = (String)this.values.elementAt(n7);
                int n8 = 0;
                while (n8 < n5) {
                    if (arrbl[n8] || !aSN1ObjectIdentifier.equals((ASN1ObjectIdentifier)x509Name.ordering.elementAt(n8)) || !this.equivalentStrings(string, (String)x509Name.values.elementAt(n8))) {
                        ++n8;
                        continue;
                    }
                    break block11;
                }
                return false;
            }
            arrbl[n8] = true;
            boolean bl3 = true;
            bl = false;
            if (!bl3) return bl;
            n7 += n3;
        } while (true);
    }

    public boolean equals(Object object, boolean bl) {
        X509Name x509Name;
        if (!bl) {
            return this.equals(object);
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof X509Name) && !(object instanceof ASN1Sequence)) {
            return false;
        }
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
        if (this.toASN1Primitive().equals(aSN1Primitive)) {
            return true;
        }
        try {
            x509Name = X509Name.getInstance(object);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
        int n2 = this.ordering.size();
        if (n2 != x509Name.ordering.size()) {
            return false;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            if (((ASN1ObjectIdentifier)this.ordering.elementAt(i2)).equals((ASN1ObjectIdentifier)x509Name.ordering.elementAt(i2))) {
                if (this.equivalentStrings((String)this.values.elementAt(i2), (String)x509Name.values.elementAt(i2))) continue;
                return false;
            }
            return false;
        }
        return true;
    }

    public Vector getOIDs() {
        Vector vector = new Vector();
        for (int i2 = 0; i2 != this.ordering.size(); ++i2) {
            vector.addElement(this.ordering.elementAt(i2));
        }
        return vector;
    }

    public Vector getValues() {
        Vector vector = new Vector();
        for (int i2 = 0; i2 != this.values.size(); ++i2) {
            vector.addElement(this.values.elementAt(i2));
        }
        return vector;
    }

    /*
     * Enabled aggressive block sorting
     */
    public Vector getValues(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Vector vector = new Vector();
        int n2 = 0;
        while (n2 != this.values.size()) {
            if (this.ordering.elementAt(n2).equals((Object)aSN1ObjectIdentifier)) {
                String string = (String)this.values.elementAt(n2);
                if (string.length() > 2 && string.charAt(0) == '\\' && string.charAt(1) == '#') {
                    vector.addElement((Object)string.substring(1));
                } else {
                    vector.addElement((Object)string);
                }
            }
            ++n2;
        }
        return vector;
    }

    @Override
    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        for (int i2 = 0; i2 != this.ordering.size(); ++i2) {
            String string = this.stripInternalSpaces(this.canonicalize((String)this.values.elementAt(i2)));
            this.hashCodeValue ^= this.ordering.elementAt(i2).hashCode();
            this.hashCodeValue ^= string.hashCode();
        }
        return this.hashCodeValue;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.seq == null) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            ASN1ObjectIdentifier aSN1ObjectIdentifier = null;
            ASN1EncodableVector aSN1EncodableVector3 = aSN1EncodableVector2;
            for (int i2 = 0; i2 != this.ordering.size(); ++i2) {
                ASN1EncodableVector aSN1EncodableVector4;
                ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
                ASN1ObjectIdentifier aSN1ObjectIdentifier2 = (ASN1ObjectIdentifier)this.ordering.elementAt(i2);
                aSN1EncodableVector5.add(aSN1ObjectIdentifier2);
                String string = (String)this.values.elementAt(i2);
                aSN1EncodableVector5.add(this.converter.getConvertedValue(aSN1ObjectIdentifier2, string));
                if (aSN1ObjectIdentifier == null || ((Boolean)this.added.elementAt(i2)).booleanValue()) {
                    aSN1EncodableVector3.add(new DERSequence(aSN1EncodableVector5));
                    aSN1EncodableVector4 = aSN1EncodableVector3;
                } else {
                    aSN1EncodableVector.add(new DERSet(aSN1EncodableVector3));
                    aSN1EncodableVector4 = new ASN1EncodableVector();
                    aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector5));
                }
                aSN1EncodableVector3 = aSN1EncodableVector4;
                aSN1ObjectIdentifier = aSN1ObjectIdentifier2;
            }
            aSN1EncodableVector.add(new DERSet(aSN1EncodableVector3));
            this.seq = new DERSequence(aSN1EncodableVector);
        }
        return this.seq;
    }

    public String toString() {
        return this.toString(DefaultReverse, DefaultSymbols);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString(boolean bl, Hashtable hashtable) {
        boolean bl2;
        StringBuffer stringBuffer = new StringBuffer();
        Vector vector = new Vector();
        StringBuffer stringBuffer2 = null;
        for (int i2 = 0; i2 < this.ordering.size(); ++i2) {
            StringBuffer stringBuffer3;
            if (((Boolean)this.added.elementAt(i2)).booleanValue()) {
                stringBuffer2.append('+');
                this.appendValue(stringBuffer2, hashtable, (ASN1ObjectIdentifier)this.ordering.elementAt(i2), (String)this.values.elementAt(i2));
                stringBuffer3 = stringBuffer2;
            } else {
                StringBuffer stringBuffer4 = new StringBuffer();
                this.appendValue(stringBuffer4, hashtable, (ASN1ObjectIdentifier)this.ordering.elementAt(i2), (String)this.values.elementAt(i2));
                vector.addElement((Object)stringBuffer4);
                stringBuffer3 = stringBuffer4;
            }
            stringBuffer2 = stringBuffer3;
        }
        if (!bl) {
            bl2 = true;
        } else {
            boolean bl3 = true;
            for (int i3 = -1 + vector.size(); i3 >= 0; --i3) {
                if (bl3) {
                    bl3 = false;
                } else {
                    stringBuffer.append(',');
                }
                stringBuffer.append(vector.elementAt(i3).toString());
            }
            return stringBuffer.toString();
        }
        for (int i4 = 0; i4 < vector.size(); ++i4) {
            if (bl2) {
                bl2 = false;
            } else {
                stringBuffer.append(',');
            }
            stringBuffer.append(vector.elementAt(i4).toString());
        }
        return stringBuffer.toString();
    }
}

