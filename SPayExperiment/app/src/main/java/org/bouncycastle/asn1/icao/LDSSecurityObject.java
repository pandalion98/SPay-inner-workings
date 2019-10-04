/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.icao;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.icao.DataGroupHash;
import org.bouncycastle.asn1.icao.ICAOObjectIdentifiers;
import org.bouncycastle.asn1.icao.LDSVersionInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class LDSSecurityObject
extends ASN1Object
implements ICAOObjectIdentifiers {
    public static final int ub_DataGroups = 16;
    private DataGroupHash[] datagroupHash;
    private AlgorithmIdentifier digestAlgorithmIdentifier;
    private ASN1Integer version = new ASN1Integer(0L);
    private LDSVersionInfo versionInfo;

    private LDSSecurityObject(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence == null || aSN1Sequence.size() == 0) {
            throw new IllegalArgumentException("null or empty sequence passed.");
        }
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.version = ASN1Integer.getInstance(enumeration.nextElement());
        this.digestAlgorithmIdentifier = AlgorithmIdentifier.getInstance(enumeration.nextElement());
        ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(enumeration.nextElement());
        if (this.version.getValue().intValue() == 1) {
            this.versionInfo = LDSVersionInfo.getInstance(enumeration.nextElement());
        }
        this.checkDatagroupHashSeqSize(aSN1Sequence2.size());
        this.datagroupHash = new DataGroupHash[aSN1Sequence2.size()];
        for (int i2 = 0; i2 < aSN1Sequence2.size(); ++i2) {
            this.datagroupHash[i2] = DataGroupHash.getInstance(aSN1Sequence2.getObjectAt(i2));
        }
    }

    public LDSSecurityObject(AlgorithmIdentifier algorithmIdentifier, DataGroupHash[] arrdataGroupHash) {
        this.version = new ASN1Integer(0L);
        this.digestAlgorithmIdentifier = algorithmIdentifier;
        this.datagroupHash = arrdataGroupHash;
        this.checkDatagroupHashSeqSize(arrdataGroupHash.length);
    }

    public LDSSecurityObject(AlgorithmIdentifier algorithmIdentifier, DataGroupHash[] arrdataGroupHash, LDSVersionInfo lDSVersionInfo) {
        this.version = new ASN1Integer(1L);
        this.digestAlgorithmIdentifier = algorithmIdentifier;
        this.datagroupHash = arrdataGroupHash;
        this.versionInfo = lDSVersionInfo;
        this.checkDatagroupHashSeqSize(arrdataGroupHash.length);
    }

    private void checkDatagroupHashSeqSize(int n2) {
        if (n2 < 2 || n2 > 16) {
            throw new IllegalArgumentException("wrong size in DataGroupHashValues : not in (2..16)");
        }
    }

    public static LDSSecurityObject getInstance(Object object) {
        if (object instanceof LDSSecurityObject) {
            return (LDSSecurityObject)object;
        }
        if (object != null) {
            return new LDSSecurityObject(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public DataGroupHash[] getDatagroupHash() {
        return this.datagroupHash;
    }

    public AlgorithmIdentifier getDigestAlgorithmIdentifier() {
        return this.digestAlgorithmIdentifier;
    }

    public int getVersion() {
        return this.version.getValue().intValue();
    }

    public LDSVersionInfo getVersionInfo() {
        return this.versionInfo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.digestAlgorithmIdentifier);
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i2 = 0; i2 < this.datagroupHash.length; ++i2) {
            aSN1EncodableVector2.add(this.datagroupHash[i2]);
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        if (this.versionInfo != null) {
            aSN1EncodableVector.add(this.versionInfo);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

