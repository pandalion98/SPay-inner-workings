package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Slog;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Deprecated
public class ContainerEncryptionParams implements Parcelable {
    public static final Creator<ContainerEncryptionParams> CREATOR = new Creator<ContainerEncryptionParams>() {
        public ContainerEncryptionParams createFromParcel(Parcel source) {
            try {
                return new ContainerEncryptionParams(source);
            } catch (InvalidAlgorithmParameterException e) {
                Slog.e(ContainerEncryptionParams.TAG, "Invalid algorithm parameters specified", e);
                return null;
            }
        }

        public ContainerEncryptionParams[] newArray(int size) {
            return new ContainerEncryptionParams[size];
        }
    };
    private static final int ENC_PARAMS_IV_PARAMETERS = 1;
    private static final int MAC_PARAMS_NONE = 1;
    protected static final String TAG = "ContainerEncryptionParams";
    private static final String TO_STRING_PREFIX = "ContainerEncryptionParams{";
    private final long mAuthenticatedDataStart;
    private final long mDataEnd;
    private final long mEncryptedDataStart;
    private final String mEncryptionAlgorithm;
    private final SecretKey mEncryptionKey;
    private final IvParameterSpec mEncryptionSpec;
    private final String mMacAlgorithm;
    private final SecretKey mMacKey;
    private final AlgorithmParameterSpec mMacSpec;
    private final byte[] mMacTag;

    public ContainerEncryptionParams(String encryptionAlgorithm, AlgorithmParameterSpec encryptionSpec, SecretKey encryptionKey) throws InvalidAlgorithmParameterException {
        this(encryptionAlgorithm, encryptionSpec, encryptionKey, null, null, null, null, -1, -1, -1);
    }

    public ContainerEncryptionParams(String encryptionAlgorithm, AlgorithmParameterSpec encryptionSpec, SecretKey encryptionKey, String macAlgorithm, AlgorithmParameterSpec macSpec, SecretKey macKey, byte[] macTag, long authenticatedDataStart, long encryptedDataStart, long dataEnd) throws InvalidAlgorithmParameterException {
        if (TextUtils.isEmpty(encryptionAlgorithm)) {
            throw new NullPointerException("algorithm == null");
        } else if (encryptionSpec == null) {
            throw new NullPointerException("encryptionSpec == null");
        } else if (encryptionKey == null) {
            throw new NullPointerException("encryptionKey == null");
        } else if (!TextUtils.isEmpty(macAlgorithm) && macKey == null) {
            throw new NullPointerException("macKey == null");
        } else if (encryptionSpec instanceof IvParameterSpec) {
            this.mEncryptionAlgorithm = encryptionAlgorithm;
            this.mEncryptionSpec = (IvParameterSpec) encryptionSpec;
            this.mEncryptionKey = encryptionKey;
            this.mMacAlgorithm = macAlgorithm;
            this.mMacSpec = macSpec;
            this.mMacKey = macKey;
            this.mMacTag = macTag;
            this.mAuthenticatedDataStart = authenticatedDataStart;
            this.mEncryptedDataStart = encryptedDataStart;
            this.mDataEnd = dataEnd;
        } else {
            throw new InvalidAlgorithmParameterException("Unknown parameter spec class; must be IvParameters");
        }
    }

    public String getEncryptionAlgorithm() {
        return this.mEncryptionAlgorithm;
    }

    public AlgorithmParameterSpec getEncryptionSpec() {
        return this.mEncryptionSpec;
    }

    public SecretKey getEncryptionKey() {
        return this.mEncryptionKey;
    }

    public String getMacAlgorithm() {
        return this.mMacAlgorithm;
    }

    public AlgorithmParameterSpec getMacSpec() {
        return this.mMacSpec;
    }

    public SecretKey getMacKey() {
        return this.mMacKey;
    }

    public byte[] getMacTag() {
        return this.mMacTag;
    }

    public long getAuthenticatedDataStart() {
        return this.mAuthenticatedDataStart;
    }

    public long getEncryptedDataStart() {
        return this.mEncryptedDataStart;
    }

    public long getDataEnd() {
        return this.mDataEnd;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContainerEncryptionParams)) {
            return false;
        }
        ContainerEncryptionParams other = (ContainerEncryptionParams) o;
        if (this.mAuthenticatedDataStart != other.mAuthenticatedDataStart || this.mEncryptedDataStart != other.mEncryptedDataStart || this.mDataEnd != other.mDataEnd) {
            return false;
        }
        if (!this.mEncryptionAlgorithm.equals(other.mEncryptionAlgorithm) || !this.mMacAlgorithm.equals(other.mMacAlgorithm)) {
            return false;
        }
        if (!isSecretKeyEqual(this.mEncryptionKey, other.mEncryptionKey) || !isSecretKeyEqual(this.mMacKey, other.mMacKey)) {
            return false;
        }
        if (Arrays.equals(this.mEncryptionSpec.getIV(), other.mEncryptionSpec.getIV()) && Arrays.equals(this.mMacTag, other.mMacTag) && this.mMacSpec == other.mMacSpec) {
            return true;
        }
        return false;
    }

    private static final boolean isSecretKeyEqual(SecretKey key1, SecretKey key2) {
        String keyFormat = key1.getFormat();
        String otherKeyFormat = key2.getFormat();
        if (keyFormat == null) {
            if (!(keyFormat == otherKeyFormat && key1.getEncoded() == key2.getEncoded())) {
                return false;
            }
        } else if (!keyFormat.equals(key2.getFormat())) {
            return false;
        } else {
            if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return (int) (((long) ((int) (((long) ((int) (((long) ((((((3 + (this.mEncryptionAlgorithm.hashCode() * 5)) + (Arrays.hashCode(this.mEncryptionSpec.getIV()) * 7)) + (this.mEncryptionKey.hashCode() * 11)) + (this.mMacAlgorithm.hashCode() * 13)) + (this.mMacKey.hashCode() * 17)) + (Arrays.hashCode(this.mMacTag) * 19))) + (23 * this.mAuthenticatedDataStart)))) + (29 * this.mEncryptedDataStart)))) + (31 * this.mDataEnd));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(TO_STRING_PREFIX);
        sb.append("mEncryptionAlgorithm=\"");
        sb.append(this.mEncryptionAlgorithm);
        sb.append("\",");
        if (this.mEncryptionSpec != null) {
            sb.append("mEncryptionSpec=");
            sb.append(this.mEncryptionSpec.toString());
        }
        if (this.mEncryptionKey != null) {
            sb.append("mEncryptionKey=");
            sb.append(this.mEncryptionKey.toString());
        }
        sb.append("mMacAlgorithm=\"");
        sb.append(this.mMacAlgorithm);
        sb.append("\",");
        if (this.mMacSpec != null) {
            sb.append("mMacSpec=");
            sb.append(this.mMacSpec.toString());
        }
        if (this.mMacKey != null) {
            sb.append("mMacKey=");
            sb.append(this.mMacKey.toString());
        }
        sb.append(",mAuthenticatedDataStart=");
        sb.append(this.mAuthenticatedDataStart);
        sb.append(",mEncryptedDataStart=");
        sb.append(this.mEncryptedDataStart);
        sb.append(",mDataEnd=");
        sb.append(this.mDataEnd);
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mEncryptionAlgorithm);
        dest.writeInt(1);
        dest.writeByteArray(this.mEncryptionSpec.getIV());
        dest.writeSerializable(this.mEncryptionKey);
        dest.writeString(this.mMacAlgorithm);
        dest.writeInt(1);
        dest.writeByteArray(new byte[0]);
        dest.writeSerializable(this.mMacKey);
        dest.writeByteArray(this.mMacTag);
        dest.writeLong(this.mAuthenticatedDataStart);
        dest.writeLong(this.mEncryptedDataStart);
        dest.writeLong(this.mDataEnd);
    }

    private ContainerEncryptionParams(Parcel source) throws InvalidAlgorithmParameterException {
        this.mEncryptionAlgorithm = source.readString();
        int encParamType = source.readInt();
        byte[] encParamsEncoded = source.createByteArray();
        this.mEncryptionKey = (SecretKey) source.readSerializable();
        this.mMacAlgorithm = source.readString();
        int macParamType = source.readInt();
        source.createByteArray();
        this.mMacKey = (SecretKey) source.readSerializable();
        this.mMacTag = source.createByteArray();
        this.mAuthenticatedDataStart = source.readLong();
        this.mEncryptedDataStart = source.readLong();
        this.mDataEnd = source.readLong();
        switch (encParamType) {
            case 1:
                this.mEncryptionSpec = new IvParameterSpec(encParamsEncoded);
                switch (macParamType) {
                    case 1:
                        this.mMacSpec = null;
                        if (this.mEncryptionKey == null) {
                            throw new NullPointerException("encryptionKey == null");
                        }
                        return;
                    default:
                        throw new InvalidAlgorithmParameterException("Unknown parameter type " + macParamType);
                }
            default:
                throw new InvalidAlgorithmParameterException("Unknown parameter type " + encParamType);
        }
    }
}
