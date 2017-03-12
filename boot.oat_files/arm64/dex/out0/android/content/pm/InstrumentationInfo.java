package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class InstrumentationInfo extends PackageItemInfo implements Parcelable {
    public static final Creator<InstrumentationInfo> CREATOR = new Creator<InstrumentationInfo>() {
        public InstrumentationInfo createFromParcel(Parcel source) {
            return new InstrumentationInfo(source);
        }

        public InstrumentationInfo[] newArray(int size) {
            return new InstrumentationInfo[size];
        }
    };
    public String dataDir;
    public boolean functionalTest;
    public boolean handleProfiling;
    public String nativeLibraryDir;
    public String publicSourceDir;
    public String sourceDir;
    public String[] splitPublicSourceDirs;
    public String[] splitSourceDirs;
    public String targetPackage;

    public InstrumentationInfo(InstrumentationInfo orig) {
        super((PackageItemInfo) orig);
        this.targetPackage = orig.targetPackage;
        this.sourceDir = orig.sourceDir;
        this.publicSourceDir = orig.publicSourceDir;
        this.dataDir = orig.dataDir;
        this.nativeLibraryDir = orig.nativeLibraryDir;
        this.handleProfiling = orig.handleProfiling;
        this.functionalTest = orig.functionalTest;
    }

    public String toString() {
        return "InstrumentationInfo{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.packageName + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        int i;
        int i2 = 0;
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(this.targetPackage);
        dest.writeString(this.sourceDir);
        dest.writeString(this.publicSourceDir);
        dest.writeString(this.dataDir);
        dest.writeString(this.nativeLibraryDir);
        if (this.handleProfiling) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.functionalTest) {
            i2 = 1;
        }
        dest.writeInt(i2);
    }

    private InstrumentationInfo(Parcel source) {
        boolean z;
        boolean z2 = true;
        super(source);
        this.targetPackage = source.readString();
        this.sourceDir = source.readString();
        this.publicSourceDir = source.readString();
        this.dataDir = source.readString();
        this.nativeLibraryDir = source.readString();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.handleProfiling = z;
        if (source.readInt() == 0) {
            z2 = false;
        }
        this.functionalTest = z2;
    }
}
