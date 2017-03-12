package android.telecom;

import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PhoneAccount implements Parcelable {
    public static final int CAPABILITY_CALL_PROVIDER = 2;
    public static final int CAPABILITY_CALL_SUBJECT = 64;
    public static final int CAPABILITY_CONNECTION_MANAGER = 1;
    public static final int CAPABILITY_MULTI_USER = 32;
    public static final int CAPABILITY_PLACE_EMERGENCY_CALLS = 16;
    public static final int CAPABILITY_SIM_SUBSCRIPTION = 4;
    public static final int CAPABILITY_VIDEO_CALLING = 8;
    public static final Creator<PhoneAccount> CREATOR = new Creator<PhoneAccount>() {
        public PhoneAccount createFromParcel(Parcel in) {
            return new PhoneAccount(in);
        }

        public PhoneAccount[] newArray(int size) {
            return new PhoneAccount[size];
        }
    };
    public static final int NO_HIGHLIGHT_COLOR = 0;
    public static final int NO_ICON_TINT = 0;
    public static final int NO_RESOURCE_ID = -1;
    public static final String SCHEME_SIP = "sip";
    public static final String SCHEME_TEL = "tel";
    public static final String SCHEME_VOICEMAIL = "voicemail";
    private final PhoneAccountHandle mAccountHandle;
    private final Uri mAddress;
    private final int mCapabilities;
    private final int mHighlightColor;
    private final Icon mIcon;
    private boolean mIsEnabled;
    private final CharSequence mLabel;
    private final CharSequence mShortDescription;
    private final Uri mSubscriptionAddress;
    private final List<String> mSupportedUriSchemes;

    public static class Builder {
        private PhoneAccountHandle mAccountHandle;
        private Uri mAddress;
        private int mCapabilities;
        private int mHighlightColor = 0;
        private Icon mIcon;
        private boolean mIsEnabled = false;
        private CharSequence mLabel;
        private CharSequence mShortDescription;
        private Uri mSubscriptionAddress;
        private List<String> mSupportedUriSchemes = new ArrayList();

        public Builder(PhoneAccountHandle accountHandle, CharSequence label) {
            this.mAccountHandle = accountHandle;
            this.mLabel = label;
        }

        public Builder(PhoneAccount phoneAccount) {
            this.mAccountHandle = phoneAccount.getAccountHandle();
            this.mAddress = phoneAccount.getAddress();
            this.mSubscriptionAddress = phoneAccount.getSubscriptionAddress();
            this.mCapabilities = phoneAccount.getCapabilities();
            this.mHighlightColor = phoneAccount.getHighlightColor();
            this.mLabel = phoneAccount.getLabel();
            this.mShortDescription = phoneAccount.getShortDescription();
            this.mSupportedUriSchemes.addAll(phoneAccount.getSupportedUriSchemes());
            this.mIcon = phoneAccount.getIcon();
            this.mIsEnabled = phoneAccount.isEnabled();
        }

        public Builder setAddress(Uri value) {
            this.mAddress = value;
            return this;
        }

        public Builder setSubscriptionAddress(Uri value) {
            this.mSubscriptionAddress = value;
            return this;
        }

        public Builder setCapabilities(int value) {
            this.mCapabilities = value;
            return this;
        }

        public Builder setIcon(Icon icon) {
            this.mIcon = icon;
            return this;
        }

        public Builder setHighlightColor(int value) {
            this.mHighlightColor = value;
            return this;
        }

        public Builder setShortDescription(CharSequence value) {
            this.mShortDescription = value;
            return this;
        }

        public Builder addSupportedUriScheme(String uriScheme) {
            if (!(TextUtils.isEmpty(uriScheme) || this.mSupportedUriSchemes.contains(uriScheme))) {
                this.mSupportedUriSchemes.add(uriScheme);
            }
            return this;
        }

        public Builder setSupportedUriSchemes(List<String> uriSchemes) {
            this.mSupportedUriSchemes.clear();
            if (!(uriSchemes == null || uriSchemes.isEmpty())) {
                for (String uriScheme : uriSchemes) {
                    addSupportedUriScheme(uriScheme);
                }
            }
            return this;
        }

        public Builder setIsEnabled(boolean isEnabled) {
            this.mIsEnabled = isEnabled;
            return this;
        }

        public PhoneAccount build() {
            if (this.mSupportedUriSchemes.isEmpty()) {
                addSupportedUriScheme(PhoneAccount.SCHEME_TEL);
            }
            return new PhoneAccount(this.mAccountHandle, this.mAddress, this.mSubscriptionAddress, this.mCapabilities, this.mIcon, this.mHighlightColor, this.mLabel, this.mShortDescription, this.mSupportedUriSchemes, this.mIsEnabled);
        }
    }

    private PhoneAccount(PhoneAccountHandle account, Uri address, Uri subscriptionAddress, int capabilities, Icon icon, int highlightColor, CharSequence label, CharSequence shortDescription, List<String> supportedUriSchemes, boolean isEnabled) {
        this.mAccountHandle = account;
        this.mAddress = address;
        this.mSubscriptionAddress = subscriptionAddress;
        this.mCapabilities = capabilities;
        this.mIcon = icon;
        this.mHighlightColor = highlightColor;
        this.mLabel = label;
        this.mShortDescription = shortDescription;
        this.mSupportedUriSchemes = Collections.unmodifiableList(supportedUriSchemes);
        this.mIsEnabled = isEnabled;
    }

    public static Builder builder(PhoneAccountHandle accountHandle, CharSequence label) {
        return new Builder(accountHandle, label);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public PhoneAccountHandle getAccountHandle() {
        return this.mAccountHandle;
    }

    public Uri getAddress() {
        return this.mAddress;
    }

    public Uri getSubscriptionAddress() {
        return this.mSubscriptionAddress;
    }

    public int getCapabilities() {
        return this.mCapabilities;
    }

    public boolean hasCapabilities(int capability) {
        return (this.mCapabilities & capability) == capability;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public CharSequence getShortDescription() {
        return this.mShortDescription;
    }

    public List<String> getSupportedUriSchemes() {
        return this.mSupportedUriSchemes;
    }

    public Icon getIcon() {
        return this.mIcon;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean supportsUriScheme(String uriScheme) {
        if (this.mSupportedUriSchemes == null || uriScheme == null) {
            return false;
        }
        for (String scheme : this.mSupportedUriSchemes) {
            if (scheme != null && scheme.equals(uriScheme)) {
                return true;
            }
        }
        return false;
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i = 1;
        if (this.mAccountHandle == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            this.mAccountHandle.writeToParcel(out, flags);
        }
        if (this.mAddress == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            this.mAddress.writeToParcel(out, flags);
        }
        if (this.mSubscriptionAddress == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            this.mSubscriptionAddress.writeToParcel(out, flags);
        }
        out.writeInt(this.mCapabilities);
        out.writeInt(this.mHighlightColor);
        out.writeCharSequence(this.mLabel);
        out.writeCharSequence(this.mShortDescription);
        out.writeStringList(this.mSupportedUriSchemes);
        if (this.mIcon == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            this.mIcon.writeToParcel(out, flags);
        }
        if (!this.mIsEnabled) {
            i = 0;
        }
        out.writeByte((byte) i);
    }

    private PhoneAccount(Parcel in) {
        if (in.readInt() > 0) {
            this.mAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(in);
        } else {
            this.mAccountHandle = null;
        }
        if (in.readInt() > 0) {
            this.mAddress = (Uri) Uri.CREATOR.createFromParcel(in);
        } else {
            this.mAddress = null;
        }
        if (in.readInt() > 0) {
            this.mSubscriptionAddress = (Uri) Uri.CREATOR.createFromParcel(in);
        } else {
            this.mSubscriptionAddress = null;
        }
        this.mCapabilities = in.readInt();
        this.mHighlightColor = in.readInt();
        this.mLabel = in.readCharSequence();
        this.mShortDescription = in.readCharSequence();
        this.mSupportedUriSchemes = Collections.unmodifiableList(in.createStringArrayList());
        if (in.readInt() > 0) {
            this.mIcon = (Icon) Icon.CREATOR.createFromParcel(in);
        } else {
            this.mIcon = null;
        }
        this.mIsEnabled = in.readByte() == (byte) 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append("[[").append(this.mIsEnabled ? 'X' : ' ').append("] PhoneAccount: ").append(this.mAccountHandle).append(" Capabilities: ").append(this.mCapabilities).append(" Schemes: ");
        for (String scheme : this.mSupportedUriSchemes) {
            sb.append(scheme).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
