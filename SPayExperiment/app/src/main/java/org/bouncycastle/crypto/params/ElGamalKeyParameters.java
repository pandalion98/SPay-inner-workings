/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ElGamalParameters;

public class ElGamalKeyParameters
extends AsymmetricKeyParameter {
    private ElGamalParameters params;

    protected ElGamalKeyParameters(boolean bl, ElGamalParameters elGamalParameters) {
        super(bl);
        this.params = elGamalParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (!(object instanceof ElGamalKeyParameters)) break block4;
                ElGamalKeyParameters elGamalKeyParameters = (ElGamalKeyParameters)object;
                if (this.params != null) {
                    return this.params.equals(elGamalKeyParameters.getParameters());
                }
                if (elGamalKeyParameters.getParameters() == null) break block5;
            }
            return false;
        }
        return true;
    }

    public ElGamalParameters getParameters() {
        return this.params;
    }

    public int hashCode() {
        if (this.params != null) {
            return this.params.hashCode();
        }
        return 0;
    }
}

