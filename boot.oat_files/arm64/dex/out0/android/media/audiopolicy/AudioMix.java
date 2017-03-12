package android.media.audiopolicy;

import android.media.AudioFormat;
import android.media.AudioSystem;
import java.util.Objects;

public class AudioMix {
    private static final int CALLBACK_FLAGS_ALL = 1;
    public static final int CALLBACK_FLAG_NOTIFY_ACTIVITY = 1;
    public static final int MIX_STATE_DISABLED = -1;
    public static final int MIX_STATE_IDLE = 0;
    public static final int MIX_STATE_MIXING = 1;
    public static final int MIX_TYPE_INVALID = -1;
    public static final int MIX_TYPE_PLAYERS = 0;
    public static final int MIX_TYPE_RECORDERS = 1;
    public static final int ROUTE_FLAG_LOOP_BACK = 2;
    public static final int ROUTE_FLAG_RENDER = 1;
    int mCallbackFlags;
    private AudioFormat mFormat;
    int mMixState;
    private int mMixType;
    private String mRegistrationId;
    private int mRouteFlags;
    private AudioMixingRule mRule;

    public static class Builder {
        private int mCallbackFlags = 0;
        private AudioFormat mFormat = null;
        private int mRouteFlags = 0;
        private AudioMixingRule mRule = null;

        Builder() {
        }

        public Builder(AudioMixingRule rule) throws IllegalArgumentException {
            if (rule == null) {
                throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
            }
            this.mRule = rule;
        }

        public Builder setMixingRule(AudioMixingRule rule) throws IllegalArgumentException {
            if (rule == null) {
                throw new IllegalArgumentException("Illegal null AudioMixingRule argument");
            }
            this.mRule = rule;
            return this;
        }

        public Builder setCallbackFlags(int flags) throws IllegalArgumentException {
            if (flags == 0 || (flags & 1) != 0) {
                this.mCallbackFlags = flags;
                return this;
            }
            throw new IllegalArgumentException("Illegal callback flags 0x" + Integer.toHexString(flags).toUpperCase());
        }

        public Builder setFormat(AudioFormat format) throws IllegalArgumentException {
            if (format == null) {
                throw new IllegalArgumentException("Illegal null AudioFormat argument");
            }
            this.mFormat = format;
            return this;
        }

        public Builder setRouteFlags(int routeFlags) throws IllegalArgumentException {
            if (routeFlags == 0) {
                throw new IllegalArgumentException("Illegal empty route flags");
            } else if ((routeFlags & 3) == 0) {
                throw new IllegalArgumentException("Invalid route flags 0x" + Integer.toHexString(routeFlags) + "when creating an AudioMix");
            } else {
                this.mRouteFlags = routeFlags;
                return this;
            }
        }

        public AudioMix build() throws IllegalArgumentException {
            if (this.mRule == null) {
                throw new IllegalArgumentException("Illegal null AudioMixingRule");
            }
            if (this.mRouteFlags == 0) {
                this.mRouteFlags = 1;
            }
            if (this.mFormat == null) {
                int rate = AudioSystem.getPrimaryOutputSamplingRate();
                if (rate <= 0) {
                    rate = 44100;
                }
                this.mFormat = new android.media.AudioFormat.Builder().setSampleRate(rate).build();
            }
            return new AudioMix(this.mRule, this.mFormat, this.mRouteFlags, this.mCallbackFlags);
        }
    }

    private AudioMix(AudioMixingRule rule, AudioFormat format, int routeFlags, int callbackFlags) {
        this.mMixType = -1;
        this.mMixState = -1;
        this.mRule = rule;
        this.mFormat = format;
        this.mRouteFlags = routeFlags;
        this.mRegistrationId = null;
        this.mMixType = rule.getTargetMixType();
        this.mCallbackFlags = callbackFlags;
    }

    public int getMixState() {
        return this.mMixState;
    }

    int getRouteFlags() {
        return this.mRouteFlags;
    }

    AudioFormat getFormat() {
        return this.mFormat;
    }

    AudioMixingRule getRule() {
        return this.mRule;
    }

    public int getMixType() {
        return this.mMixType;
    }

    void setRegistration(String regId) {
        this.mRegistrationId = regId;
    }

    public String getRegistration() {
        return this.mRegistrationId;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRouteFlags), this.mRule, Integer.valueOf(this.mMixType), this.mFormat});
    }
}
