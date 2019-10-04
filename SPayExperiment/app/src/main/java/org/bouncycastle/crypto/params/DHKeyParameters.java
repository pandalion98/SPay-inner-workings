/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;

public class DHKeyParameters
extends AsymmetricKeyParameter {
    private DHParameters params;

    protected DHKeyParameters(boolean bl, DHParameters dHParameters) {
        super(bl);
        this.params = dHParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (!(object instanceof DHKeyParameters)) break block4;
                DHKeyParameters dHKeyParameters = (DHKeyParameters)object;
                if (this.params != null) {
                    return this.params.equals(dHKeyParameters.getParameters());
                }
                if (dHKeyParameters.getParameters() == null) break block5;
            }
            return false;
        }
        return true;
    }

    public DHParameters getParameters() {
        return this.params;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        int n2 = this.isPrivate() ? 0 : 1;
        if (this.params != null) {
            n2 ^= this.params.hashCode();
        }
        return n2;
    }
}

