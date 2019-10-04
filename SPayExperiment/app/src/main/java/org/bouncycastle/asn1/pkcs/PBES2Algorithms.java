/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.EncryptionScheme;
import org.bouncycastle.asn1.pkcs.KeyDerivationFunc;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBES2Algorithms
extends AlgorithmIdentifier
implements PKCSObjectIdentifiers {
    private KeyDerivationFunc func;
    private ASN1ObjectIdentifier objectId;
    private EncryptionScheme scheme;

    /*
     * Enabled aggressive block sorting
     */
    public PBES2Algorithms(ASN1Sequence aSN1Sequence) {
        super(aSN1Sequence);
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.objectId = (ASN1ObjectIdentifier)enumeration.nextElement();
        Enumeration enumeration2 = ((ASN1Sequence)enumeration.nextElement()).getObjects();
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)enumeration2.nextElement();
        this.func = aSN1Sequence2.getObjectAt(0).equals((Object)id_PBKDF2) ? new KeyDerivationFunc(id_PBKDF2, PBKDF2Params.getInstance(aSN1Sequence2.getObjectAt(1))) : KeyDerivationFunc.getInstance(aSN1Sequence2);
        this.scheme = EncryptionScheme.getInstance(enumeration2.nextElement());
    }

    public ASN1Primitive getASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.objectId);
        aSN1EncodableVector2.add(this.func);
        aSN1EncodableVector2.add(this.scheme);
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }

    public EncryptionScheme getEncryptionScheme() {
        return this.scheme;
    }

    public KeyDerivationFunc getKeyDerivationFunc() {
        return this.func;
    }

    @Override
    public ASN1ObjectIdentifier getObjectId() {
        return this.objectId;
    }
}

