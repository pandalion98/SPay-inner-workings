/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.agreement.jpake;

import java.math.BigInteger;
import org.bouncycastle.crypto.agreement.jpake.JPAKEUtil;
import org.bouncycastle.util.Arrays;

public class JPAKERound2Payload {
    private final BigInteger a;
    private final BigInteger[] knowledgeProofForX2s;
    private final String participantId;

    public JPAKERound2Payload(String string, BigInteger bigInteger, BigInteger[] arrbigInteger) {
        JPAKEUtil.validateNotNull(string, "participantId");
        JPAKEUtil.validateNotNull((Object)bigInteger, "a");
        JPAKEUtil.validateNotNull(arrbigInteger, "knowledgeProofForX2s");
        this.participantId = string;
        this.a = bigInteger;
        this.knowledgeProofForX2s = Arrays.copyOf((BigInteger[])arrbigInteger, (int)arrbigInteger.length);
    }

    public BigInteger getA() {
        return this.a;
    }

    public BigInteger[] getKnowledgeProofForX2s() {
        return Arrays.copyOf((BigInteger[])this.knowledgeProofForX2s, (int)this.knowledgeProofForX2s.length);
    }

    public String getParticipantId() {
        return this.participantId;
    }
}

