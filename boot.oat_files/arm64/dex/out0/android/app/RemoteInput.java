package android.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class RemoteInput implements Parcelable {
    public static final Creator<RemoteInput> CREATOR = new Creator<RemoteInput>() {
        public RemoteInput createFromParcel(Parcel in) {
            return new RemoteInput(in);
        }

        public RemoteInput[] newArray(int size) {
            return new RemoteInput[size];
        }
    };
    private static final int DEFAULT_FLAGS = 1;
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final int FLAG_ALLOW_FREE_FORM_INPUT = 1;
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final int mFlags;
    private final CharSequence mLabel;
    private final String mResultKey;

    public static final class Builder {
        private CharSequence[] mChoices;
        private Bundle mExtras = new Bundle();
        private int mFlags = 1;
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String resultKey) {
            if (resultKey == null) {
                throw new IllegalArgumentException("Result key can't be null");
            }
            this.mResultKey = resultKey;
        }

        public Builder setLabel(CharSequence label) {
            this.mLabel = Notification.safeCharSequence(label);
            return this;
        }

        public Builder setChoices(CharSequence[] choices) {
            if (choices == null) {
                this.mChoices = null;
            } else {
                this.mChoices = new CharSequence[choices.length];
                for (int i = 0; i < choices.length; i++) {
                    this.mChoices[i] = Notification.safeCharSequence(choices[i]);
                }
            }
            return this;
        }

        public Builder setAllowFreeFormInput(boolean allowFreeFormInput) {
            setFlag(this.mFlags, allowFreeFormInput);
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                this.mExtras.putAll(extras);
            }
            return this;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= mask ^ -1;
            }
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mFlags, this.mExtras);
        }
    }

    private RemoteInput(String resultKey, CharSequence label, CharSequence[] choices, int flags, Bundle extras) {
        this.mResultKey = resultKey;
        this.mLabel = label;
        this.mChoices = choices;
        this.mFlags = flags;
        this.mExtras = extras;
    }

    public String getResultKey() {
        return this.mResultKey;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public boolean getAllowFreeFormInput() {
        return (this.mFlags & 1) != 0;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    private RemoteInput(Parcel in) {
        this.mResultKey = in.readString();
        this.mLabel = in.readCharSequence();
        this.mChoices = in.readCharSequenceArray();
        this.mFlags = in.readInt();
        this.mExtras = in.readBundle();
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return null;
        }
        ClipDescription clipDescription = clipData.getDescription();
        if (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) && clipDescription.getLabel().equals(RESULTS_CLIP_LABEL)) {
            return (Bundle) clipData.getItemAt(0).getIntent().getExtras().getParcelable(EXTRA_RESULTS_DATA);
        }
        return null;
    }

    public static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
        Bundle resultsBundle = new Bundle();
        for (RemoteInput remoteInput : remoteInputs) {
            Object result = results.get(remoteInput.getResultKey());
            if (result instanceof CharSequence) {
                resultsBundle.putCharSequence(remoteInput.getResultKey(), (CharSequence) result);
            }
        }
        Intent clipIntent = new Intent();
        clipIntent.putExtra(EXTRA_RESULTS_DATA, resultsBundle);
        intent.setClipData(ClipData.newIntent(RESULTS_CLIP_LABEL, clipIntent));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mResultKey);
        out.writeCharSequence(this.mLabel);
        out.writeCharSequenceArray(this.mChoices);
        out.writeInt(this.mFlags);
        out.writeBundle(this.mExtras);
    }
}
