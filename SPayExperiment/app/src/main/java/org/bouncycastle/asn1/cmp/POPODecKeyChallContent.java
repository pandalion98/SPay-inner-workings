/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cmp.Challenge;

public class POPODecKeyChallContent
extends ASN1Object {
    private ASN1Sequence content;

    private POPODecKeyChallContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static POPODecKeyChallContent getInstance(Object object) {
        if (object instanceof POPODecKeyChallContent) {
            return (POPODecKeyChallContent)object;
        }
        if (object != null) {
            return new POPODecKeyChallContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public Challenge[] toChallengeArray() {
        Challenge[] arrchallenge = new Challenge[this.content.size()];
        for (int i2 = 0; i2 != arrchallenge.length; ++i2) {
            arrchallenge[i2] = Challenge.getInstance(this.content.getObjectAt(i2));
        }
        return arrchallenge;
    }
}

