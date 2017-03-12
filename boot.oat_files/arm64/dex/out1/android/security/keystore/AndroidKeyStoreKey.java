package android.security.keystore;

import java.security.Key;

public class AndroidKeyStoreKey implements Key {
    private final String mAlgorithm;
    private final String mAlias;

    public AndroidKeyStoreKey(String alias, String algorithm) {
        this.mAlias = alias;
        this.mAlgorithm = algorithm;
    }

    String getAlias() {
        return this.mAlias;
    }

    public String getAlgorithm() {
        return this.mAlgorithm;
    }

    public String getFormat() {
        return null;
    }

    public byte[] getEncoded() {
        return null;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.mAlgorithm == null ? 0 : this.mAlgorithm.hashCode()) + 31) * 31;
        if (this.mAlias != null) {
            i = this.mAlias.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AndroidKeyStoreKey other = (AndroidKeyStoreKey) obj;
        if (this.mAlgorithm == null) {
            if (other.mAlgorithm != null) {
                return false;
            }
        } else if (!this.mAlgorithm.equals(other.mAlgorithm)) {
            return false;
        }
        if (this.mAlias == null) {
            if (other.mAlias != null) {
                return false;
            }
            return true;
        } else if (this.mAlias.equals(other.mAlias)) {
            return true;
        } else {
            return false;
        }
    }
}
