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

public class JPAKERound1Payload {
    private final BigInteger gx1;
    private final BigInteger gx2;
    private final BigInteger[] knowledgeProofForX1;
    private final BigInteger[] knowledgeProofForX2;
    private final String participantId;

    public JPAKERound1Payload(String string, BigInteger bigInteger, BigInteger bigInteger2, BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2) {
        JPAKEUtil.validateNotNull(string, "participantId");
        JPAKEUtil.validateNotNull((Object)bigInteger, "gx1");
        JPAKEUtil.validateNotNull((Object)bigInteger2, "gx2");
        JPAKEUtil.validateNotNull(arrbigInteger, "knowledgeProofForX1");
        JPAKEUtil.validateNotNull(arrbigInteger2, "knowledgeProofForX2");
        this.participantId = string;
        this.gx1 = bigInteger;
        this.gx2 = bigInteger2;
        this.knowledgeProofForX1 = Arrays.copyOf((BigInteger[])arrbigInteger, (int)arrbigInteger.length);
        this.knowledgeProofForX2 = Arrays.copyOf((BigInteger[])arrbigInteger2, (int)arrbigInteger2.length);
    }

    public BigInteger getGx1() {
        return this.gx1;
    }

    public BigInteger getGx2() {
        return this.gx2;
    }

    public BigInteger[] getKnowledgeProofForX1() {
        return Arrays.copyOf((BigInteger[])this.knowledgeProofForX1, (int)this.knowledgeProofForX1.length);
    }

    public BigInteger[] getKnowledgeProofForX2() {
        return Arrays.copyOf((BigInteger[])this.knowledgeProofForX2, (int)this.knowledgeProofForX2.length);
    }

    public String getParticipantId() {
        return this.participantId;
    }
}

