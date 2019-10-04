/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.agreement.jpake;

import java.math.BigInteger;

public class JPAKERound3Payload {
    private final BigInteger macTag;
    private final String participantId;

    public JPAKERound3Payload(String string, BigInteger bigInteger) {
        this.participantId = string;
        this.macTag = bigInteger;
    }

    public BigInteger getMacTag() {
        return this.macTag;
    }

    public String getParticipantId() {
        return this.participantId;
    }
}

