/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.Hashtable
 */
package org.bouncycastle.asn1.x500.style;

import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.AbstractX500NameStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;

public class BCStyle
extends AbstractX500NameStyle {
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier C;
    public static final ASN1ObjectIdentifier CN;
    public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
    public static final ASN1ObjectIdentifier DC;
    public static final ASN1ObjectIdentifier DMD_NAME;
    public static final ASN1ObjectIdentifier DN_QUALIFIER;
    private static final Hashtable DefaultLookUp;
    private static final Hashtable DefaultSymbols;
    public static final ASN1ObjectIdentifier E;
    public static final ASN1ObjectIdentifier EmailAddress;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final X500NameStyle INSTANCE;
    public static final ASN1ObjectIdentifier L;
    public static final ASN1ObjectIdentifier NAME;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier O;
    public static final ASN1ObjectIdentifier OU;
    public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
    public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
    public static final ASN1ObjectIdentifier POSTAL_CODE;
    public static final ASN1ObjectIdentifier PSEUDONYM;
    public static final ASN1ObjectIdentifier SERIALNUMBER;
    public static final ASN1ObjectIdentifier SN;
    public static final ASN1ObjectIdentifier ST;
    public static final ASN1ObjectIdentifier STREET;
    public static final ASN1ObjectIdentifier SURNAME;
    public static final ASN1ObjectIdentifier T;
    public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
    public static final ASN1ObjectIdentifier UID;
    public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
    public static final ASN1ObjectIdentifier UnstructuredAddress;
    public static final ASN1ObjectIdentifier UnstructuredName;
    protected final Hashtable defaultLookUp = BCStyle.copyHashTable(DefaultLookUp);
    protected final Hashtable defaultSymbols = BCStyle.copyHashTable(DefaultSymbols);

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
        DefaultSymbols = new Hashtable();
        DefaultLookUp = new Hashtable();
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
        INSTANCE = new BCStyle();
    }

    protected BCStyle() {
    }

    @Override
    public ASN1ObjectIdentifier attrNameToOID(String string) {
        return IETFUtils.decodeAttrName(string, this.defaultLookUp);
    }

    @Override
    protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (aSN1ObjectIdentifier.equals(EmailAddress) || aSN1ObjectIdentifier.equals(DC)) {
            return new DERIA5String(string);
        }
        if (aSN1ObjectIdentifier.equals(DATE_OF_BIRTH)) {
            return new ASN1GeneralizedTime(string);
        }
        if (aSN1ObjectIdentifier.equals(C) || aSN1ObjectIdentifier.equals(SN) || aSN1ObjectIdentifier.equals(DN_QUALIFIER) || aSN1ObjectIdentifier.equals(TELEPHONE_NUMBER)) {
            return new DERPrintableString(string);
        }
        return super.encodeStringValue(aSN1ObjectIdentifier, string);
    }

    @Override
    public RDN[] fromString(String string) {
        return IETFUtils.rDNsFromString(string, this);
    }

    @Override
    public String[] oidToAttrNames(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return IETFUtils.findAttrNamesForOID(aSN1ObjectIdentifier, this.defaultLookUp);
    }

    @Override
    public String oidToDisplayName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (String)DefaultSymbols.get((Object)aSN1ObjectIdentifier);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String toString(X500Name x500Name) {
        StringBuffer stringBuffer = new StringBuffer();
        RDN[] arrrDN = x500Name.getRDNs();
        boolean bl = true;
        int n2 = 0;
        while (n2 < arrrDN.length) {
            if (bl) {
                bl = false;
            } else {
                stringBuffer.append(',');
            }
            IETFUtils.appendRDN(stringBuffer, arrrDN[n2], this.defaultSymbols);
            ++n2;
        }
        return stringBuffer.toString();
    }
}

