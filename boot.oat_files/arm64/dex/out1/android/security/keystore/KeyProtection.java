package android.security.keystore;

import java.security.KeyStore.ProtectionParameter;
import java.util.Date;

public final class KeyProtection implements ProtectionParameter {
    private final String[] mBlockModes;
    private final String[] mDigests;
    private final String[] mEncryptionPaddings;
    private final Date mKeyValidityForConsumptionEnd;
    private final Date mKeyValidityForOriginationEnd;
    private final Date mKeyValidityStart;
    private final int mPurposes;
    private final boolean mRandomizedEncryptionRequired;
    private final String[] mSignaturePaddings;
    private final boolean mUserAuthenticationRequired;
    private final int mUserAuthenticationValidityDurationSeconds;

    public static final class Builder {
        private String[] mBlockModes;
        private String[] mDigests;
        private String[] mEncryptionPaddings;
        private Date mKeyValidityForConsumptionEnd;
        private Date mKeyValidityForOriginationEnd;
        private Date mKeyValidityStart;
        private int mPurposes;
        private boolean mRandomizedEncryptionRequired = true;
        private String[] mSignaturePaddings;
        private boolean mUserAuthenticationRequired;
        private int mUserAuthenticationValidityDurationSeconds = -1;

        public Builder(int purposes) {
            this.mPurposes = purposes;
        }

        public Builder setKeyValidityStart(Date startDate) {
            this.mKeyValidityStart = Utils.cloneIfNotNull(startDate);
            return this;
        }

        public Builder setKeyValidityEnd(Date endDate) {
            setKeyValidityForOriginationEnd(endDate);
            setKeyValidityForConsumptionEnd(endDate);
            return this;
        }

        public Builder setKeyValidityForOriginationEnd(Date endDate) {
            this.mKeyValidityForOriginationEnd = Utils.cloneIfNotNull(endDate);
            return this;
        }

        public Builder setKeyValidityForConsumptionEnd(Date endDate) {
            this.mKeyValidityForConsumptionEnd = Utils.cloneIfNotNull(endDate);
            return this;
        }

        public Builder setEncryptionPaddings(String... paddings) {
            this.mEncryptionPaddings = ArrayUtils.cloneIfNotEmpty(paddings);
            return this;
        }

        public Builder setSignaturePaddings(String... paddings) {
            this.mSignaturePaddings = ArrayUtils.cloneIfNotEmpty(paddings);
            return this;
        }

        public Builder setDigests(String... digests) {
            this.mDigests = ArrayUtils.cloneIfNotEmpty(digests);
            return this;
        }

        public Builder setBlockModes(String... blockModes) {
            this.mBlockModes = ArrayUtils.cloneIfNotEmpty(blockModes);
            return this;
        }

        public Builder setRandomizedEncryptionRequired(boolean required) {
            this.mRandomizedEncryptionRequired = required;
            return this;
        }

        public Builder setUserAuthenticationRequired(boolean required) {
            this.mUserAuthenticationRequired = required;
            return this;
        }

        public Builder setUserAuthenticationValidityDurationSeconds(int seconds) {
            if (seconds < -1) {
                throw new IllegalArgumentException("seconds must be -1 or larger");
            }
            this.mUserAuthenticationValidityDurationSeconds = seconds;
            return this;
        }

        public KeyProtection build() {
            return new KeyProtection(this.mKeyValidityStart, this.mKeyValidityForOriginationEnd, this.mKeyValidityForConsumptionEnd, this.mPurposes, this.mEncryptionPaddings, this.mSignaturePaddings, this.mDigests, this.mBlockModes, this.mRandomizedEncryptionRequired, this.mUserAuthenticationRequired, this.mUserAuthenticationValidityDurationSeconds);
        }
    }

    private KeyProtection(Date keyValidityStart, Date keyValidityForOriginationEnd, Date keyValidityForConsumptionEnd, int purposes, String[] encryptionPaddings, String[] signaturePaddings, String[] digests, String[] blockModes, boolean randomizedEncryptionRequired, boolean userAuthenticationRequired, int userAuthenticationValidityDurationSeconds) {
        this.mKeyValidityStart = Utils.cloneIfNotNull(keyValidityStart);
        this.mKeyValidityForOriginationEnd = Utils.cloneIfNotNull(keyValidityForOriginationEnd);
        this.mKeyValidityForConsumptionEnd = Utils.cloneIfNotNull(keyValidityForConsumptionEnd);
        this.mPurposes = purposes;
        this.mEncryptionPaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(encryptionPaddings));
        this.mSignaturePaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(signaturePaddings));
        this.mDigests = ArrayUtils.cloneIfNotEmpty(digests);
        this.mBlockModes = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(blockModes));
        this.mRandomizedEncryptionRequired = randomizedEncryptionRequired;
        this.mUserAuthenticationRequired = userAuthenticationRequired;
        this.mUserAuthenticationValidityDurationSeconds = userAuthenticationValidityDurationSeconds;
    }

    public Date getKeyValidityStart() {
        return Utils.cloneIfNotNull(this.mKeyValidityStart);
    }

    public Date getKeyValidityForConsumptionEnd() {
        return Utils.cloneIfNotNull(this.mKeyValidityForConsumptionEnd);
    }

    public Date getKeyValidityForOriginationEnd() {
        return Utils.cloneIfNotNull(this.mKeyValidityForOriginationEnd);
    }

    public int getPurposes() {
        return this.mPurposes;
    }

    public String[] getEncryptionPaddings() {
        return ArrayUtils.cloneIfNotEmpty(this.mEncryptionPaddings);
    }

    public String[] getSignaturePaddings() {
        return ArrayUtils.cloneIfNotEmpty(this.mSignaturePaddings);
    }

    public String[] getDigests() {
        if (this.mDigests != null) {
            return ArrayUtils.cloneIfNotEmpty(this.mDigests);
        }
        throw new IllegalStateException("Digests not specified");
    }

    public boolean isDigestsSpecified() {
        return this.mDigests != null;
    }

    public String[] getBlockModes() {
        return ArrayUtils.cloneIfNotEmpty(this.mBlockModes);
    }

    public boolean isRandomizedEncryptionRequired() {
        return this.mRandomizedEncryptionRequired;
    }

    public boolean isUserAuthenticationRequired() {
        return this.mUserAuthenticationRequired;
    }

    public int getUserAuthenticationValidityDurationSeconds() {
        return this.mUserAuthenticationValidityDurationSeconds;
    }
}
