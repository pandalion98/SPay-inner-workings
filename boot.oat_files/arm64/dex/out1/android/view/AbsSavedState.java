package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public abstract class AbsSavedState implements Parcelable {
    public static final Creator<AbsSavedState> CREATOR = new Creator<AbsSavedState>() {
        public AbsSavedState createFromParcel(Parcel in) {
            if (in.readParcelable(null) == null) {
                return AbsSavedState.EMPTY_STATE;
            }
            throw new IllegalStateException("superState must be null");
        }

        public AbsSavedState[] newArray(int size) {
            return new AbsSavedState[size];
        }
    };
    public static final AbsSavedState EMPTY_STATE = new AbsSavedState() {
    };
    private final Parcelable mSuperState;

    private AbsSavedState() {
        this.mSuperState = null;
    }

    protected AbsSavedState(Parcelable superState) {
        if (superState == null) {
            throw new IllegalArgumentException("superState must not be null");
        }
        if (superState == EMPTY_STATE) {
            superState = null;
        }
        this.mSuperState = superState;
    }

    protected AbsSavedState(Parcel source) {
        Parcelable superState = source.readParcelable(null);
        if (superState == null) {
            superState = EMPTY_STATE;
        }
        this.mSuperState = superState;
    }

    protected AbsSavedState(Parcel source, ClassLoader classloader) {
        Parcelable superState = source.readParcelable(classloader);
        if (superState == null) {
            superState = EMPTY_STATE;
        }
        this.mSuperState = superState;
    }

    public final Parcelable getSuperState() {
        return this.mSuperState;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mSuperState, flags);
    }
}
