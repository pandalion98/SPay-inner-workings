package org.bouncycastle.crypto.params;

public class ElGamalKeyParameters extends AsymmetricKeyParameter {
    private ElGamalParameters params;

    protected ElGamalKeyParameters(boolean z, ElGamalParameters elGamalParameters) {
        super(z);
        this.params = elGamalParameters;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalKeyParameters)) {
            return false;
        }
        ElGamalKeyParameters elGamalKeyParameters = (ElGamalKeyParameters) obj;
        return this.params == null ? elGamalKeyParameters.getParameters() == null : this.params.equals(elGamalKeyParameters.getParameters());
    }

    public ElGamalParameters getParameters() {
        return this.params;
    }

    public int hashCode() {
        return this.params != null ? this.params.hashCode() : 0;
    }
}
