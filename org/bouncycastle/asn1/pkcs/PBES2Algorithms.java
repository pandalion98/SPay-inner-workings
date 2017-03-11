package org.bouncycastle.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBES2Algorithms extends AlgorithmIdentifier implements PKCSObjectIdentifiers {
    private KeyDerivationFunc func;
    private ASN1ObjectIdentifier objectId;
    private EncryptionScheme scheme;

    public PBES2Algorithms(ASN1Sequence aSN1Sequence) {
        super(aSN1Sequence);
        Enumeration objects = aSN1Sequence.getObjects();
        this.objectId = (ASN1ObjectIdentifier) objects.nextElement();
        objects = ((ASN1Sequence) objects.nextElement()).getObjects();
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence) objects.nextElement();
        if (aSN1Sequence2.getObjectAt(0).equals(id_PBKDF2)) {
            this.func = new KeyDerivationFunc(id_PBKDF2, PBKDF2Params.getInstance(aSN1Sequence2.getObjectAt(1)));
        } else {
            this.func = KeyDerivationFunc.getInstance(aSN1Sequence2);
        }
        this.scheme = EncryptionScheme.getInstance(objects.nextElement());
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

    public ASN1ObjectIdentifier getObjectId() {
        return this.objectId;
    }
}
