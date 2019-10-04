/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.crypto.agreement.srp.SRP6StandardGroups;
import org.bouncycastle.crypto.params.SRP6GroupParameters;
import org.bouncycastle.crypto.tls.TlsSRPGroupVerifier;

public class DefaultTlsSRPGroupVerifier
implements TlsSRPGroupVerifier {
    protected static final Vector DEFAULT_GROUPS = new Vector();
    protected Vector groups;

    static {
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_1024);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_1536);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_2048);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_3072);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_4096);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_6144);
        DEFAULT_GROUPS.addElement((Object)SRP6StandardGroups.rfc5054_8192);
    }

    public DefaultTlsSRPGroupVerifier() {
        this(DEFAULT_GROUPS);
    }

    public DefaultTlsSRPGroupVerifier(Vector vector) {
        this.groups = vector;
    }

    @Override
    public boolean accept(SRP6GroupParameters sRP6GroupParameters) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = this.groups.size();
                    bl = false;
                    if (n2 >= n3) break block3;
                    if (!this.areGroupsEqual(sRP6GroupParameters, (SRP6GroupParameters)this.groups.elementAt(n2))) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    protected boolean areGroupsEqual(SRP6GroupParameters sRP6GroupParameters, SRP6GroupParameters sRP6GroupParameters2) {
        return sRP6GroupParameters == sRP6GroupParameters2 || this.areParametersEqual(sRP6GroupParameters.getN(), sRP6GroupParameters2.getN()) && this.areParametersEqual(sRP6GroupParameters.getG(), sRP6GroupParameters2.getG());
    }

    protected boolean areParametersEqual(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger == bigInteger2 || bigInteger.equals((Object)bigInteger2);
    }
}

