package android.print;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class PrintJobInfo implements Parcelable {
    public static final Creator<PrintJobInfo> CREATOR = new Creator<PrintJobInfo>() {
        public PrintJobInfo createFromParcel(Parcel parcel) {
            return new PrintJobInfo(parcel);
        }

        public PrintJobInfo[] newArray(int size) {
            return new PrintJobInfo[size];
        }
    };
    public static final int STATE_ANY = -1;
    public static final int STATE_ANY_ACTIVE = -3;
    public static final int STATE_ANY_SCHEDULED = -4;
    public static final int STATE_ANY_VISIBLE_TO_CLIENTS = -2;
    public static final int STATE_BLOCKED = 4;
    public static final int STATE_CANCELED = 7;
    public static final int STATE_COMPLETED = 5;
    public static final int STATE_CREATED = 1;
    public static final int STATE_FAILED = 6;
    public static final int STATE_QUEUED = 2;
    public static final int STATE_STARTED = 3;
    private Bundle mAdvancedOptions;
    private int mAppId;
    private PrintAttributes mAttributes;
    private boolean mCanceling;
    private int mCopies;
    private long mCreationTime;
    private PrintDocumentInfo mDocumentInfo;
    private PrintJobId mId;
    private String mLabel;
    private PageRange[] mPageRanges;
    private PrinterId mPrinterId;
    private String mPrinterName;
    private int mState;
    private String mStateReason;
    private String mTag;

    public static final class Builder {
        private final PrintJobInfo mPrototype;

        public Builder(PrintJobInfo prototype) {
            this.mPrototype = prototype != null ? new PrintJobInfo(prototype) : new PrintJobInfo();
        }

        public void setCopies(int copies) {
            this.mPrototype.mCopies = copies;
        }

        public void setAttributes(PrintAttributes attributes) {
            this.mPrototype.mAttributes = attributes;
        }

        public void setPages(PageRange[] pages) {
            this.mPrototype.mPageRanges = pages;
        }

        public void putAdvancedOption(String key, String value) {
            if (this.mPrototype.mAdvancedOptions == null) {
                this.mPrototype.mAdvancedOptions = new Bundle();
            }
            this.mPrototype.mAdvancedOptions.putString(key, value);
        }

        public void putAdvancedOption(String key, int value) {
            if (this.mPrototype.mAdvancedOptions == null) {
                this.mPrototype.mAdvancedOptions = new Bundle();
            }
            this.mPrototype.mAdvancedOptions.putInt(key, value);
        }

        public PrintJobInfo build() {
            return this.mPrototype;
        }
    }

    public PrintJobInfo(PrintJobInfo other) {
        this.mId = other.mId;
        this.mLabel = other.mLabel;
        this.mPrinterId = other.mPrinterId;
        this.mPrinterName = other.mPrinterName;
        this.mState = other.mState;
        this.mAppId = other.mAppId;
        this.mTag = other.mTag;
        this.mCreationTime = other.mCreationTime;
        this.mCopies = other.mCopies;
        this.mStateReason = other.mStateReason;
        this.mPageRanges = other.mPageRanges;
        this.mAttributes = other.mAttributes;
        this.mDocumentInfo = other.mDocumentInfo;
        this.mCanceling = other.mCanceling;
        this.mAdvancedOptions = other.mAdvancedOptions;
    }

    private PrintJobInfo(Parcel parcel) {
        this.mId = (PrintJobId) parcel.readParcelable(null);
        this.mLabel = parcel.readString();
        this.mPrinterId = (PrinterId) parcel.readParcelable(null);
        this.mPrinterName = parcel.readString();
        this.mState = parcel.readInt();
        this.mAppId = parcel.readInt();
        this.mTag = parcel.readString();
        this.mCreationTime = parcel.readLong();
        this.mCopies = parcel.readInt();
        this.mStateReason = parcel.readString();
        Parcelable[] parcelables = parcel.readParcelableArray(null);
        if (parcelables != null) {
            this.mPageRanges = new PageRange[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                this.mPageRanges[i] = (PageRange) parcelables[i];
            }
        }
        this.mAttributes = (PrintAttributes) parcel.readParcelable(null);
        this.mDocumentInfo = (PrintDocumentInfo) parcel.readParcelable(null);
        this.mCanceling = parcel.readInt() == 1;
        this.mAdvancedOptions = parcel.readBundle();
    }

    public PrintJobId getId() {
        return this.mId;
    }

    public void setId(PrintJobId id) {
        this.mId = id;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public PrinterId getPrinterId() {
        return this.mPrinterId;
    }

    public void setPrinterId(PrinterId printerId) {
        this.mPrinterId = printerId;
    }

    public String getPrinterName() {
        return this.mPrinterName;
    }

    public void setPrinterName(String printerName) {
        this.mPrinterName = printerName;
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public int getAppId() {
        return this.mAppId;
    }

    public void setAppId(int appId) {
        this.mAppId = appId;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public long getCreationTime() {
        return this.mCreationTime;
    }

    public void setCreationTime(long creationTime) {
        if (creationTime < 0) {
            throw new IllegalArgumentException("creationTime must be non-negative.");
        }
        this.mCreationTime = creationTime;
    }

    public int getCopies() {
        return this.mCopies;
    }

    public void setCopies(int copyCount) {
        if (copyCount < 1) {
            throw new IllegalArgumentException("Copies must be more than one.");
        }
        this.mCopies = copyCount;
    }

    public String getStateReason() {
        return this.mStateReason;
    }

    public void setStateReason(String stateReason) {
        this.mStateReason = stateReason;
    }

    public PageRange[] getPages() {
        return this.mPageRanges;
    }

    public void setPages(PageRange[] pageRanges) {
        this.mPageRanges = pageRanges;
    }

    public PrintAttributes getAttributes() {
        return this.mAttributes;
    }

    public void setAttributes(PrintAttributes attributes) {
        this.mAttributes = attributes;
    }

    public PrintDocumentInfo getDocumentInfo() {
        return this.mDocumentInfo;
    }

    public void setDocumentInfo(PrintDocumentInfo info) {
        this.mDocumentInfo = info;
    }

    public boolean isCancelling() {
        return this.mCanceling;
    }

    public void setCancelling(boolean cancelling) {
        this.mCanceling = cancelling;
    }

    public boolean hasAdvancedOption(String key) {
        return this.mAdvancedOptions != null && this.mAdvancedOptions.containsKey(key);
    }

    public String getAdvancedStringOption(String key) {
        if (this.mAdvancedOptions != null) {
            return this.mAdvancedOptions.getString(key);
        }
        return null;
    }

    public int getAdvancedIntOption(String key) {
        if (this.mAdvancedOptions != null) {
            return this.mAdvancedOptions.getInt(key);
        }
        return 0;
    }

    public Bundle getAdvancedOptions() {
        return this.mAdvancedOptions;
    }

    public void setAdvancedOptions(Bundle options) {
        this.mAdvancedOptions = options;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int i = 0;
        parcel.writeParcelable(this.mId, flags);
        parcel.writeString(this.mLabel);
        parcel.writeParcelable(this.mPrinterId, flags);
        parcel.writeString(this.mPrinterName);
        parcel.writeInt(this.mState);
        parcel.writeInt(this.mAppId);
        parcel.writeString(this.mTag);
        parcel.writeLong(this.mCreationTime);
        parcel.writeInt(this.mCopies);
        parcel.writeString(this.mStateReason);
        parcel.writeParcelableArray(this.mPageRanges, flags);
        parcel.writeParcelable(this.mAttributes, flags);
        parcel.writeParcelable(this.mDocumentInfo, 0);
        if (this.mCanceling) {
            i = 1;
        }
        parcel.writeInt(i);
        parcel.writeBundle(this.mAdvancedOptions);
    }

    public String toString() {
        String printDocumentInfo;
        String str = null;
        StringBuilder builder = new StringBuilder();
        builder.append("PrintJobInfo{");
        builder.append("label: ").append(this.mLabel);
        builder.append(", id: ").append(this.mId);
        builder.append(", state: ").append(stateToString(this.mState));
        builder.append(", printer: " + this.mPrinterId);
        builder.append(", tag: ").append(this.mTag);
        builder.append(", creationTime: " + this.mCreationTime);
        builder.append(", copies: ").append(this.mCopies);
        builder.append(", attributes: " + (this.mAttributes != null ? this.mAttributes.toString() : null));
        StringBuilder append = new StringBuilder().append(", documentInfo: ");
        if (this.mDocumentInfo != null) {
            printDocumentInfo = this.mDocumentInfo.toString();
        } else {
            printDocumentInfo = null;
        }
        builder.append(append.append(printDocumentInfo).toString());
        builder.append(", cancelling: " + this.mCanceling);
        StringBuilder append2 = new StringBuilder().append(", pages: ");
        if (this.mPageRanges != null) {
            str = Arrays.toString(this.mPageRanges);
        }
        builder.append(append2.append(str).toString());
        builder.append(", hasAdvancedOptions: " + (this.mAdvancedOptions != null));
        builder.append("}");
        return builder.toString();
    }

    public static String stateToString(int state) {
        switch (state) {
            case 1:
                return "STATE_CREATED";
            case 2:
                return "STATE_QUEUED";
            case 3:
                return "STATE_STARTED";
            case 4:
                return "STATE_BLOCKED";
            case 5:
                return "STATE_COMPLETED";
            case 6:
                return "STATE_FAILED";
            case 7:
                return "STATE_CANCELED";
            default:
                return "STATE_UNKNOWN";
        }
    }
}
