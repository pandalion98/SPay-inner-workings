/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509.qualified;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.qualified.TypeOfBiometricData;

public class BiometricData
extends ASN1Object {
    private ASN1OctetString biometricDataHash;
    private AlgorithmIdentifier hashAlgorithm;
    private DERIA5String sourceDataUri;
    private TypeOfBiometricData typeOfBiometricData;

    private BiometricData(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.typeOfBiometricData = TypeOfBiometricData.getInstance(enumeration.nextElement());
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(enumeration.nextElement());
        this.biometricDataHash = ASN1OctetString.getInstance(enumeration.nextElement());
        if (enumeration.hasMoreElements()) {
            this.sourceDataUri = DERIA5String.getInstance(enumeration.nextElement());
        }
    }

    public BiometricData(TypeOfBiometricData typeOfBiometricData, AlgorithmIdentifier algorithmIdentifier, ASN1OctetString aSN1OctetString) {
        this.typeOfBiometricData = typeOfBiometricData;
        this.hashAlgorithm = algorithmIdentifier;
        this.biometricDataHash = aSN1OctetString;
        this.sourceDataUri = null;
    }

    public BiometricData(TypeOfBiometricData typeOfBiometricData, AlgorithmIdentifier algorithmIdentifier, ASN1OctetString aSN1OctetString, DERIA5String dERIA5String) {
        this.typeOfBiometricData = typeOfBiometricData;
        this.hashAlgorithm = algorithmIdentifier;
        this.biometricDataHash = aSN1OctetString;
        this.sourceDataUri = dERIA5String;
    }

    public static BiometricData getInstance(Object object) {
        if (object instanceof BiometricData) {
            return (BiometricData)object;
        }
        if (object != null) {
            return new BiometricData(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1OctetString getBiometricDataHash() {
        return this.biometricDataHash;
    }

    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    public DERIA5String getSourceDataUri() {
        return this.sourceDataUri;
    }

    public TypeOfBiometricData getTypeOfBiometricData() {
        return this.typeOfBiometricData;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.typeOfBiometricData);
        aSN1EncodableVector.add(this.hashAlgorithm);
        aSN1EncodableVector.add(this.biometricDataHash);
        if (this.sourceDataUri != null) {
            aSN1EncodableVector.add(this.sourceDataUri);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

