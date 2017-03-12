package android.os;

import android.os.Parcelable.ClassLoaderCreator;

public class ParcelableParcel implements Parcelable {
    public static final ClassLoaderCreator<ParcelableParcel> CREATOR = new ClassLoaderCreator<ParcelableParcel>() {
        public ParcelableParcel createFromParcel(Parcel in) {
            return new ParcelableParcel(in, null);
        }

        public ParcelableParcel createFromParcel(Parcel in, ClassLoader loader) {
            return new ParcelableParcel(in, loader);
        }

        public ParcelableParcel[] newArray(int size) {
            return new ParcelableParcel[size];
        }
    };
    final ClassLoader mClassLoader;
    final Parcel mParcel = Parcel.obtain();

    public ParcelableParcel(ClassLoader loader) {
        this.mClassLoader = loader;
    }

    public ParcelableParcel(Parcel src, ClassLoader loader) {
        this.mClassLoader = loader;
        int size = src.readInt();
        int pos = src.dataPosition();
        this.mParcel.appendFrom(src, src.dataPosition(), size);
        src.setDataPosition(pos + size);
    }

    public Parcel getParcel() {
        this.mParcel.setDataPosition(0);
        return this.mParcel;
    }

    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mParcel.dataSize());
        dest.appendFrom(this.mParcel, 0, this.mParcel.dataSize());
    }
}
