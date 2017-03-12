package org.bouncycastle.asn1.x500.style;

import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.i18n.MessageBundle;

public class RFC4519Style extends AbstractX500NameStyle {
    private static final Hashtable DefaultLookUp;
    private static final Hashtable DefaultSymbols;
    public static final X500NameStyle INSTANCE;
    public static final ASN1ObjectIdentifier businessCategory;
    public static final ASN1ObjectIdentifier f57c;
    public static final ASN1ObjectIdentifier cn;
    public static final ASN1ObjectIdentifier dc;
    public static final ASN1ObjectIdentifier description;
    public static final ASN1ObjectIdentifier destinationIndicator;
    public static final ASN1ObjectIdentifier distinguishedName;
    public static final ASN1ObjectIdentifier dnQualifier;
    public static final ASN1ObjectIdentifier enhancedSearchGuide;
    public static final ASN1ObjectIdentifier facsimileTelephoneNumber;
    public static final ASN1ObjectIdentifier generationQualifier;
    public static final ASN1ObjectIdentifier givenName;
    public static final ASN1ObjectIdentifier houseIdentifier;
    public static final ASN1ObjectIdentifier initials;
    public static final ASN1ObjectIdentifier internationalISDNNumber;
    public static final ASN1ObjectIdentifier f58l;
    public static final ASN1ObjectIdentifier member;
    public static final ASN1ObjectIdentifier name;
    public static final ASN1ObjectIdentifier f59o;
    public static final ASN1ObjectIdentifier ou;
    public static final ASN1ObjectIdentifier owner;
    public static final ASN1ObjectIdentifier physicalDeliveryOfficeName;
    public static final ASN1ObjectIdentifier postOfficeBox;
    public static final ASN1ObjectIdentifier postalAddress;
    public static final ASN1ObjectIdentifier postalCode;
    public static final ASN1ObjectIdentifier preferredDeliveryMethod;
    public static final ASN1ObjectIdentifier registeredAddress;
    public static final ASN1ObjectIdentifier roleOccupant;
    public static final ASN1ObjectIdentifier searchGuide;
    public static final ASN1ObjectIdentifier seeAlso;
    public static final ASN1ObjectIdentifier serialNumber;
    public static final ASN1ObjectIdentifier sn;
    public static final ASN1ObjectIdentifier st;
    public static final ASN1ObjectIdentifier street;
    public static final ASN1ObjectIdentifier telephoneNumber;
    public static final ASN1ObjectIdentifier teletexTerminalIdentifier;
    public static final ASN1ObjectIdentifier telexNumber;
    public static final ASN1ObjectIdentifier title;
    public static final ASN1ObjectIdentifier uid;
    public static final ASN1ObjectIdentifier uniqueMember;
    public static final ASN1ObjectIdentifier userPassword;
    public static final ASN1ObjectIdentifier x121Address;
    public static final ASN1ObjectIdentifier x500UniqueIdentifier;
    protected final Hashtable defaultLookUp;
    protected final Hashtable defaultSymbols;

    static {
        businessCategory = new ASN1ObjectIdentifier("2.5.4.15");
        f57c = new ASN1ObjectIdentifier("2.5.4.6");
        cn = new ASN1ObjectIdentifier("2.5.4.3");
        dc = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        description = new ASN1ObjectIdentifier("2.5.4.13");
        destinationIndicator = new ASN1ObjectIdentifier("2.5.4.27");
        distinguishedName = new ASN1ObjectIdentifier("2.5.4.49");
        dnQualifier = new ASN1ObjectIdentifier("2.5.4.46");
        enhancedSearchGuide = new ASN1ObjectIdentifier("2.5.4.47");
        facsimileTelephoneNumber = new ASN1ObjectIdentifier("2.5.4.23");
        generationQualifier = new ASN1ObjectIdentifier("2.5.4.44");
        givenName = new ASN1ObjectIdentifier("2.5.4.42");
        houseIdentifier = new ASN1ObjectIdentifier("2.5.4.51");
        initials = new ASN1ObjectIdentifier("2.5.4.43");
        internationalISDNNumber = new ASN1ObjectIdentifier("2.5.4.25");
        f58l = new ASN1ObjectIdentifier("2.5.4.7");
        member = new ASN1ObjectIdentifier("2.5.4.31");
        name = new ASN1ObjectIdentifier("2.5.4.41");
        f59o = new ASN1ObjectIdentifier("2.5.4.10");
        ou = new ASN1ObjectIdentifier("2.5.4.11");
        owner = new ASN1ObjectIdentifier("2.5.4.32");
        physicalDeliveryOfficeName = new ASN1ObjectIdentifier("2.5.4.19");
        postalAddress = new ASN1ObjectIdentifier("2.5.4.16");
        postalCode = new ASN1ObjectIdentifier("2.5.4.17");
        postOfficeBox = new ASN1ObjectIdentifier("2.5.4.18");
        preferredDeliveryMethod = new ASN1ObjectIdentifier("2.5.4.28");
        registeredAddress = new ASN1ObjectIdentifier("2.5.4.26");
        roleOccupant = new ASN1ObjectIdentifier("2.5.4.33");
        searchGuide = new ASN1ObjectIdentifier("2.5.4.14");
        seeAlso = new ASN1ObjectIdentifier("2.5.4.34");
        serialNumber = new ASN1ObjectIdentifier("2.5.4.5");
        sn = new ASN1ObjectIdentifier("2.5.4.4");
        st = new ASN1ObjectIdentifier("2.5.4.8");
        street = new ASN1ObjectIdentifier("2.5.4.9");
        telephoneNumber = new ASN1ObjectIdentifier("2.5.4.20");
        teletexTerminalIdentifier = new ASN1ObjectIdentifier("2.5.4.22");
        telexNumber = new ASN1ObjectIdentifier("2.5.4.21");
        title = new ASN1ObjectIdentifier("2.5.4.12");
        uid = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        uniqueMember = new ASN1ObjectIdentifier("2.5.4.50");
        userPassword = new ASN1ObjectIdentifier("2.5.4.35");
        x121Address = new ASN1ObjectIdentifier("2.5.4.24");
        x500UniqueIdentifier = new ASN1ObjectIdentifier("2.5.4.45");
        DefaultSymbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        DefaultSymbols.put(businessCategory, "businessCategory");
        DefaultSymbols.put(f57c, "c");
        DefaultSymbols.put(cn, "cn");
        DefaultSymbols.put(dc, "dc");
        DefaultSymbols.put(description, "description");
        DefaultSymbols.put(destinationIndicator, "destinationIndicator");
        DefaultSymbols.put(distinguishedName, "distinguishedName");
        DefaultSymbols.put(dnQualifier, "dnQualifier");
        DefaultSymbols.put(enhancedSearchGuide, "enhancedSearchGuide");
        DefaultSymbols.put(facsimileTelephoneNumber, "facsimileTelephoneNumber");
        DefaultSymbols.put(generationQualifier, "generationQualifier");
        DefaultSymbols.put(givenName, "givenName");
        DefaultSymbols.put(houseIdentifier, "houseIdentifier");
        DefaultSymbols.put(initials, "initials");
        DefaultSymbols.put(internationalISDNNumber, "internationalISDNNumber");
        DefaultSymbols.put(f58l, "l");
        DefaultSymbols.put(member, "member");
        DefaultSymbols.put(name, "name");
        DefaultSymbols.put(f59o, "o");
        DefaultSymbols.put(ou, "ou");
        DefaultSymbols.put(owner, "owner");
        DefaultSymbols.put(physicalDeliveryOfficeName, "physicalDeliveryOfficeName");
        DefaultSymbols.put(postalAddress, "postalAddress");
        DefaultSymbols.put(postalCode, "postalCode");
        DefaultSymbols.put(postOfficeBox, "postOfficeBox");
        DefaultSymbols.put(preferredDeliveryMethod, "preferredDeliveryMethod");
        DefaultSymbols.put(registeredAddress, "registeredAddress");
        DefaultSymbols.put(roleOccupant, "roleOccupant");
        DefaultSymbols.put(searchGuide, "searchGuide");
        DefaultSymbols.put(seeAlso, "seeAlso");
        DefaultSymbols.put(serialNumber, "serialNumber");
        DefaultSymbols.put(sn, "sn");
        DefaultSymbols.put(st, "st");
        DefaultSymbols.put(street, "street");
        DefaultSymbols.put(telephoneNumber, "telephoneNumber");
        DefaultSymbols.put(teletexTerminalIdentifier, "teletexTerminalIdentifier");
        DefaultSymbols.put(telexNumber, "telexNumber");
        DefaultSymbols.put(title, MessageBundle.TITLE_ENTRY);
        DefaultSymbols.put(uid, "uid");
        DefaultSymbols.put(uniqueMember, "uniqueMember");
        DefaultSymbols.put(userPassword, "userPassword");
        DefaultSymbols.put(x121Address, "x121Address");
        DefaultSymbols.put(x500UniqueIdentifier, "x500UniqueIdentifier");
        DefaultLookUp.put("businesscategory", businessCategory);
        DefaultLookUp.put("c", f57c);
        DefaultLookUp.put("cn", cn);
        DefaultLookUp.put("dc", dc);
        DefaultLookUp.put("description", description);
        DefaultLookUp.put("destinationindicator", destinationIndicator);
        DefaultLookUp.put("distinguishedname", distinguishedName);
        DefaultLookUp.put("dnqualifier", dnQualifier);
        DefaultLookUp.put("enhancedsearchguide", enhancedSearchGuide);
        DefaultLookUp.put("facsimiletelephonenumber", facsimileTelephoneNumber);
        DefaultLookUp.put("generationqualifier", generationQualifier);
        DefaultLookUp.put("givenname", givenName);
        DefaultLookUp.put("houseidentifier", houseIdentifier);
        DefaultLookUp.put("initials", initials);
        DefaultLookUp.put("internationalisdnnumber", internationalISDNNumber);
        DefaultLookUp.put("l", f58l);
        DefaultLookUp.put("member", member);
        DefaultLookUp.put("name", name);
        DefaultLookUp.put("o", f59o);
        DefaultLookUp.put("ou", ou);
        DefaultLookUp.put("owner", owner);
        DefaultLookUp.put("physicaldeliveryofficename", physicalDeliveryOfficeName);
        DefaultLookUp.put("postaladdress", postalAddress);
        DefaultLookUp.put("postalcode", postalCode);
        DefaultLookUp.put("postofficebox", postOfficeBox);
        DefaultLookUp.put("preferreddeliverymethod", preferredDeliveryMethod);
        DefaultLookUp.put("registeredaddress", registeredAddress);
        DefaultLookUp.put("roleoccupant", roleOccupant);
        DefaultLookUp.put("searchguide", searchGuide);
        DefaultLookUp.put("seealso", seeAlso);
        DefaultLookUp.put("serialnumber", serialNumber);
        DefaultLookUp.put("sn", sn);
        DefaultLookUp.put("st", st);
        DefaultLookUp.put("street", street);
        DefaultLookUp.put("telephonenumber", telephoneNumber);
        DefaultLookUp.put("teletexterminalidentifier", teletexTerminalIdentifier);
        DefaultLookUp.put("telexnumber", telexNumber);
        DefaultLookUp.put(MessageBundle.TITLE_ENTRY, title);
        DefaultLookUp.put("uid", uid);
        DefaultLookUp.put("uniquemember", uniqueMember);
        DefaultLookUp.put("userpassword", userPassword);
        DefaultLookUp.put("x121address", x121Address);
        DefaultLookUp.put("x500uniqueidentifier", x500UniqueIdentifier);
        INSTANCE = new RFC4519Style();
    }

    protected RFC4519Style() {
        this.defaultSymbols = AbstractX500NameStyle.copyHashTable(DefaultSymbols);
        this.defaultLookUp = AbstractX500NameStyle.copyHashTable(DefaultLookUp);
    }

    public ASN1ObjectIdentifier attrNameToOID(String str) {
        return IETFUtils.decodeAttrName(str, this.defaultLookUp);
    }

    protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str) {
        return aSN1ObjectIdentifier.equals(dc) ? new DERIA5String(str) : (aSN1ObjectIdentifier.equals(f57c) || aSN1ObjectIdentifier.equals(serialNumber) || aSN1ObjectIdentifier.equals(dnQualifier) || aSN1ObjectIdentifier.equals(telephoneNumber)) ? new DERPrintableString(str) : super.encodeStringValue(aSN1ObjectIdentifier, str);
    }

    public RDN[] fromString(String str) {
        RDN[] rDNsFromString = IETFUtils.rDNsFromString(str, this);
        RDN[] rdnArr = new RDN[rDNsFromString.length];
        for (int i = 0; i != rDNsFromString.length; i++) {
            rdnArr[(rdnArr.length - i) - 1] = rDNsFromString[i];
        }
        return rdnArr;
    }

    public String[] oidToAttrNames(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return IETFUtils.findAttrNamesForOID(aSN1ObjectIdentifier, this.defaultLookUp);
    }

    public String oidToDisplayName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (String) DefaultSymbols.get(aSN1ObjectIdentifier);
    }

    public String toString(X500Name x500Name) {
        StringBuffer stringBuffer = new StringBuffer();
        RDN[] rDNs = x500Name.getRDNs();
        Object obj = 1;
        for (int length = rDNs.length - 1; length >= 0; length--) {
            if (obj != null) {
                obj = null;
            } else {
                stringBuffer.append(',');
            }
            IETFUtils.appendRDN(stringBuffer, rDNs[length], this.defaultSymbols);
        }
        return stringBuffer.toString();
    }
}
