/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERSet;

class BERFactory {
    static final BERSequence EMPTY_SEQUENCE = new BERSequence();
    static final BERSet EMPTY_SET = new BERSet();

    BERFactory() {
    }

    static BERSequence createSequence(ASN1EncodableVector aSN1EncodableVector) {
        if (aSN1EncodableVector.size() < 1) {
            return EMPTY_SEQUENCE;
        }
        return new BERSequence(aSN1EncodableVector);
    }

    static BERSet createSet(ASN1EncodableVector aSN1EncodableVector) {
        if (aSN1EncodableVector.size() < 1) {
            return EMPTY_SET;
        }
        return new BERSet(aSN1EncodableVector);
    }
}

