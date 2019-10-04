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
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.AbstractX500NameStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public class RFC4519Style
extends AbstractX500NameStyle {
    private static final Hashtable DefaultLookUp;
    private static final Hashtable DefaultSymbols;
    public static final X500NameStyle INSTANCE;
    public static final ASN1ObjectIdentifier businessCategory;
    public static final ASN1ObjectIdentifier c;
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
    public static final ASN1ObjectIdentifier l;
    public static final ASN1ObjectIdentifier member;
    public static final ASN1ObjectIdentifier name;
    public static final ASN1ObjectIdentifier o;
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
    protected final Hashtable defaultLookUp = RFC4519Style.copyHashTable(DefaultLookUp);
    protected final Hashtable defaultSymbols = RFC4519Style.copyHashTable(DefaultSymbols);

    static {
        businessCategory = new ASN1ObjectIdentifier("2.5.4.15");
        c = new ASN1ObjectIdentifier("2.5.4.6");
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
        l = new ASN1ObjectIdentifier("2.5.4.7");
        member = new ASN1ObjectIdentifier("2.5.4.31");
        name = new ASN1ObjectIdentifier("2.5.4.41");
        o = new ASN1ObjectIdentifier("2.5.4.10");
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
        DefaultSymbols.put((Object)businessCategory, (Object)"businessCategory");
        DefaultSymbols.put((Object)c, (Object)"c");
        DefaultSymbols.put((Object)cn, (Object)"cn");
        DefaultSymbols.put((Object)dc, (Object)"dc");
        DefaultSymbols.put((Object)description, (Object)"description");
        DefaultSymbols.put((Object)destinationIndicator, (Object)"destinationIndicator");
        DefaultSymbols.put((Object)distinguishedName, (Object)"distinguishedName");
        DefaultSymbols.put((Object)dnQualifier, (Object)"dnQualifier");
        DefaultSymbols.put((Object)enhancedSearchGuide, (Object)"enhancedSearchGuide");
        DefaultSymbols.put((Object)facsimileTelephoneNumber, (Object)"facsimileTelephoneNumber");
        DefaultSymbols.put((Object)generationQualifier, (Object)"generationQualifier");
        DefaultSymbols.put((Object)givenName, (Object)"givenName");
        DefaultSymbols.put((Object)houseIdentifier, (Object)"houseIdentifier");
        DefaultSymbols.put((Object)initials, (Object)"initials");
        DefaultSymbols.put((Object)internationalISDNNumber, (Object)"internationalISDNNumber");
        DefaultSymbols.put((Object)l, (Object)"l");
        DefaultSymbols.put((Object)member, (Object)"member");
        DefaultSymbols.put((Object)name, (Object)"name");
        DefaultSymbols.put((Object)o, (Object)"o");
        DefaultSymbols.put((Object)ou, (Object)"ou");
        DefaultSymbols.put((Object)owner, (Object)"owner");
        DefaultSymbols.put((Object)physicalDeliveryOfficeName, (Object)"physicalDeliveryOfficeName");
        DefaultSymbols.put((Object)postalAddress, (Object)"postalAddress");
        DefaultSymbols.put((Object)postalCode, (Object)"postalCode");
        DefaultSymbols.put((Object)postOfficeBox, (Object)"postOfficeBox");
        DefaultSymbols.put((Object)preferredDeliveryMethod, (Object)"preferredDeliveryMethod");
        DefaultSymbols.put((Object)registeredAddress, (Object)"registeredAddress");
        DefaultSymbols.put((Object)roleOccupant, (Object)"roleOccupant");
        DefaultSymbols.put((Object)searchGuide, (Object)"searchGuide");
        DefaultSymbols.put((Object)seeAlso, (Object)"seeAlso");
        DefaultSymbols.put((Object)serialNumber, (Object)"serialNumber");
        DefaultSymbols.put((Object)sn, (Object)"sn");
        DefaultSymbols.put((Object)st, (Object)"st");
        DefaultSymbols.put((Object)street, (Object)"street");
        DefaultSymbols.put((Object)telephoneNumber, (Object)"telephoneNumber");
        DefaultSymbols.put((Object)teletexTerminalIdentifier, (Object)"teletexTerminalIdentifier");
        DefaultSymbols.put((Object)telexNumber, (Object)"telexNumber");
        DefaultSymbols.put((Object)title, (Object)"title");
        DefaultSymbols.put((Object)uid, (Object)"uid");
        DefaultSymbols.put((Object)uniqueMember, (Object)"uniqueMember");
        DefaultSymbols.put((Object)userPassword, (Object)"userPassword");
        DefaultSymbols.put((Object)x121Address, (Object)"x121Address");
        DefaultSymbols.put((Object)x500UniqueIdentifier, (Object)"x500UniqueIdentifier");
        DefaultLookUp.put((Object)"businesscategory", (Object)businessCategory);
        DefaultLookUp.put((Object)"c", (Object)c);
        DefaultLookUp.put((Object)"cn", (Object)cn);
        DefaultLookUp.put((Object)"dc", (Object)dc);
        DefaultLookUp.put((Object)"description", (Object)description);
        DefaultLookUp.put((Object)"destinationindicator", (Object)destinationIndicator);
        DefaultLookUp.put((Object)"distinguishedname", (Object)distinguishedName);
        DefaultLookUp.put((Object)"dnqualifier", (Object)dnQualifier);
        DefaultLookUp.put((Object)"enhancedsearchguide", (Object)enhancedSearchGuide);
        DefaultLookUp.put((Object)"facsimiletelephonenumber", (Object)facsimileTelephoneNumber);
        DefaultLookUp.put((Object)"generationqualifier", (Object)generationQualifier);
        DefaultLookUp.put((Object)"givenname", (Object)givenName);
        DefaultLookUp.put((Object)"houseidentifier", (Object)houseIdentifier);
        DefaultLookUp.put((Object)"initials", (Object)initials);
        DefaultLookUp.put((Object)"internationalisdnnumber", (Object)internationalISDNNumber);
        DefaultLookUp.put((Object)"l", (Object)l);
        DefaultLookUp.put((Object)"member", (Object)member);
        DefaultLookUp.put((Object)"name", (Object)name);
        DefaultLookUp.put((Object)"o", (Object)o);
        DefaultLookUp.put((Object)"ou", (Object)ou);
        DefaultLookUp.put((Object)"owner", (Object)owner);
        DefaultLookUp.put((Object)"physicaldeliveryofficename", (Object)physicalDeliveryOfficeName);
        DefaultLookUp.put((Object)"postaladdress", (Object)postalAddress);
        DefaultLookUp.put((Object)"postalcode", (Object)postalCode);
        DefaultLookUp.put((Object)"postofficebox", (Object)postOfficeBox);
        DefaultLookUp.put((Object)"preferreddeliverymethod", (Object)preferredDeliveryMethod);
        DefaultLookUp.put((Object)"registeredaddress", (Object)registeredAddress);
        DefaultLookUp.put((Object)"roleoccupant", (Object)roleOccupant);
        DefaultLookUp.put((Object)"searchguide", (Object)searchGuide);
        DefaultLookUp.put((Object)"seealso", (Object)seeAlso);
        DefaultLookUp.put((Object)"serialnumber", (Object)serialNumber);
        DefaultLookUp.put((Object)"sn", (Object)sn);
        DefaultLookUp.put((Object)"st", (Object)st);
        DefaultLookUp.put((Object)"street", (Object)street);
        DefaultLookUp.put((Object)"telephonenumber", (Object)telephoneNumber);
        DefaultLookUp.put((Object)"teletexterminalidentifier", (Object)teletexTerminalIdentifier);
        DefaultLookUp.put((Object)"telexnumber", (Object)telexNumber);
        DefaultLookUp.put((Object)"title", (Object)title);
        DefaultLookUp.put((Object)"uid", (Object)uid);
        DefaultLookUp.put((Object)"uniquemember", (Object)uniqueMember);
        DefaultLookUp.put((Object)"userpassword", (Object)userPassword);
        DefaultLookUp.put((Object)"x121address", (Object)x121Address);
        DefaultLookUp.put((Object)"x500uniqueidentifier", (Object)x500UniqueIdentifier);
        INSTANCE = new RFC4519Style();
    }

    protected RFC4519Style() {
    }

    @Override
    public ASN1ObjectIdentifier attrNameToOID(String string) {
        return IETFUtils.decodeAttrName(string, this.defaultLookUp);
    }

    @Override
    protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (aSN1ObjectIdentifier.equals(dc)) {
            return new DERIA5String(string);
        }
        if (aSN1ObjectIdentifier.equals(c) || aSN1ObjectIdentifier.equals(serialNumber) || aSN1ObjectIdentifier.equals(dnQualifier) || aSN1ObjectIdentifier.equals(telephoneNumber)) {
            return new DERPrintableString(string);
        }
        return super.encodeStringValue(aSN1ObjectIdentifier, string);
    }

    @Override
    public RDN[] fromString(String string) {
        RDN[] arrrDN = IETFUtils.rDNsFromString(string, this);
        RDN[] arrrDN2 = new RDN[arrrDN.length];
        for (int i2 = 0; i2 != arrrDN.length; ++i2) {
            arrrDN2[-1 + (arrrDN2.length - i2)] = arrrDN[i2];
        }
        return arrrDN2;
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
        int n2 = -1 + arrrDN.length;
        boolean bl = true;
        int n3 = n2;
        while (n3 >= 0) {
            if (bl) {
                bl = false;
            } else {
                stringBuffer.append(',');
            }
            IETFUtils.appendRDN(stringBuffer, arrrDN[n3], this.defaultSymbols);
            --n3;
        }
        return stringBuffer.toString();
    }
}

