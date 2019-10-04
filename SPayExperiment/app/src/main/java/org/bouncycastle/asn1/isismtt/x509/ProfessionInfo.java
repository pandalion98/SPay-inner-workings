/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.isismtt.x509.NamingAuthority;
import org.bouncycastle.asn1.x500.DirectoryString;

public class ProfessionInfo
extends ASN1Object {
    public static final ASN1ObjectIdentifier Notar;
    public static final ASN1ObjectIdentifier Notariatsverwalter;
    public static final ASN1ObjectIdentifier Notariatsverwalterin;
    public static final ASN1ObjectIdentifier Notarin;
    public static final ASN1ObjectIdentifier Notarvertreter;
    public static final ASN1ObjectIdentifier Notarvertreterin;
    public static final ASN1ObjectIdentifier Patentanwalt;
    public static final ASN1ObjectIdentifier Patentanwltin;
    public static final ASN1ObjectIdentifier Rechtsanwalt;
    public static final ASN1ObjectIdentifier Rechtsanwltin;
    public static final ASN1ObjectIdentifier Rechtsbeistand;
    public static final ASN1ObjectIdentifier Steuerberater;
    public static final ASN1ObjectIdentifier Steuerberaterin;
    public static final ASN1ObjectIdentifier Steuerbevollmchtigte;
    public static final ASN1ObjectIdentifier Steuerbevollmchtigter;
    public static final ASN1ObjectIdentifier VereidigteBuchprferin;
    public static final ASN1ObjectIdentifier VereidigterBuchprfer;
    public static final ASN1ObjectIdentifier Wirtschaftsprfer;
    public static final ASN1ObjectIdentifier Wirtschaftsprferin;
    private ASN1OctetString addProfessionInfo;
    private NamingAuthority namingAuthority;
    private ASN1Sequence professionItems;
    private ASN1Sequence professionOIDs;
    private String registrationNumber;

    static {
        Rechtsanwltin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".1");
        Rechtsanwalt = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".2");
        Rechtsbeistand = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".3");
        Steuerberaterin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".4");
        Steuerberater = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".5");
        Steuerbevollmchtigte = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".6");
        Steuerbevollmchtigter = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".7");
        Notarin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".8");
        Notar = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".9");
        Notarvertreterin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".10");
        Notarvertreter = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".11");
        Notariatsverwalterin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".12");
        Notariatsverwalter = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".13");
        Wirtschaftsprferin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".14");
        Wirtschaftsprfer = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".15");
        VereidigteBuchprferin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".16");
        VereidigterBuchprfer = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".17");
        Patentanwltin = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".18");
        Patentanwalt = new ASN1ObjectIdentifier(NamingAuthority.id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern + ".19");
    }

    /*
     * Enabled aggressive block sorting
     */
    private ProfessionInfo(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 5) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration enumeration = aSN1Sequence.getObjects();
        ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
        if (aSN1Encodable instanceof ASN1TaggedObject) {
            if (((ASN1TaggedObject)aSN1Encodable).getTagNo() != 0) {
                throw new IllegalArgumentException("Bad tag number: " + ((ASN1TaggedObject)aSN1Encodable).getTagNo());
            }
            this.namingAuthority = NamingAuthority.getInstance((ASN1TaggedObject)aSN1Encodable, true);
            aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
        }
        this.professionItems = ASN1Sequence.getInstance(aSN1Encodable);
        if (enumeration.hasMoreElements()) {
            ASN1Encodable aSN1Encodable2 = (ASN1Encodable)enumeration.nextElement();
            if (aSN1Encodable2 instanceof ASN1Sequence) {
                this.professionOIDs = ASN1Sequence.getInstance(aSN1Encodable2);
            } else if (aSN1Encodable2 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(aSN1Encodable2).getString();
            } else {
                if (!(aSN1Encodable2 instanceof ASN1OctetString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + (Object)aSN1Encodable2.getClass());
                }
                this.addProfessionInfo = ASN1OctetString.getInstance(aSN1Encodable2);
            }
        }
        if (enumeration.hasMoreElements()) {
            ASN1Encodable aSN1Encodable3 = (ASN1Encodable)enumeration.nextElement();
            if (aSN1Encodable3 instanceof DERPrintableString) {
                this.registrationNumber = DERPrintableString.getInstance(aSN1Encodable3).getString();
            } else {
                if (!(aSN1Encodable3 instanceof DEROctetString)) {
                    throw new IllegalArgumentException("Bad object encountered: " + (Object)aSN1Encodable3.getClass());
                }
                this.addProfessionInfo = (DEROctetString)aSN1Encodable3;
            }
        }
        if (enumeration.hasMoreElements()) {
            ASN1Encodable aSN1Encodable4 = (ASN1Encodable)enumeration.nextElement();
            if (!(aSN1Encodable4 instanceof DEROctetString)) {
                throw new IllegalArgumentException("Bad object encountered: " + (Object)aSN1Encodable4.getClass());
            }
            this.addProfessionInfo = (DEROctetString)aSN1Encodable4;
        }
    }

    public ProfessionInfo(NamingAuthority namingAuthority, DirectoryString[] arrdirectoryString, ASN1ObjectIdentifier[] arraSN1ObjectIdentifier, String string, ASN1OctetString aSN1OctetString) {
        int n2 = 0;
        this.namingAuthority = namingAuthority;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != arrdirectoryString.length; ++i2) {
            aSN1EncodableVector.add(arrdirectoryString[i2]);
        }
        this.professionItems = new DERSequence(aSN1EncodableVector);
        if (arraSN1ObjectIdentifier != null) {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            while (n2 != arraSN1ObjectIdentifier.length) {
                aSN1EncodableVector2.add(arraSN1ObjectIdentifier[n2]);
                ++n2;
            }
            this.professionOIDs = new DERSequence(aSN1EncodableVector2);
        }
        this.registrationNumber = string;
        this.addProfessionInfo = aSN1OctetString;
    }

    public static ProfessionInfo getInstance(Object object) {
        if (object == null || object instanceof ProfessionInfo) {
            return (ProfessionInfo)object;
        }
        if (object instanceof ASN1Sequence) {
            return new ProfessionInfo((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public ASN1OctetString getAddProfessionInfo() {
        return this.addProfessionInfo;
    }

    public NamingAuthority getNamingAuthority() {
        return this.namingAuthority;
    }

    public DirectoryString[] getProfessionItems() {
        DirectoryString[] arrdirectoryString = new DirectoryString[this.professionItems.size()];
        int n2 = 0;
        Enumeration enumeration = this.professionItems.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arrdirectoryString[n2] = DirectoryString.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arrdirectoryString;
    }

    public ASN1ObjectIdentifier[] getProfessionOIDs() {
        int n2 = 0;
        if (this.professionOIDs == null) {
            return new ASN1ObjectIdentifier[0];
        }
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[this.professionOIDs.size()];
        Enumeration enumeration = this.professionOIDs.getObjects();
        while (enumeration.hasMoreElements()) {
            int n3 = n2 + 1;
            arraSN1ObjectIdentifier[n2] = ASN1ObjectIdentifier.getInstance(enumeration.nextElement());
            n2 = n3;
        }
        return arraSN1ObjectIdentifier;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.namingAuthority != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.namingAuthority));
        }
        aSN1EncodableVector.add(this.professionItems);
        if (this.professionOIDs != null) {
            aSN1EncodableVector.add(this.professionOIDs);
        }
        if (this.registrationNumber != null) {
            aSN1EncodableVector.add(new DERPrintableString(this.registrationNumber, true));
        }
        if (this.addProfessionInfo != null) {
            aSN1EncodableVector.add(this.addProfessionInfo);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

