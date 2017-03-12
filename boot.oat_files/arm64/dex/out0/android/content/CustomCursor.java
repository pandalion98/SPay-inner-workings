package android.content;

import android.database.AbstractWindowedCursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CustomCursor extends AbstractWindowedCursor implements Parcelable {
    private static final String BUNDLE_KEY = "ColumnNames";
    public static final Creator<CustomCursor> CREATOR = new Creator<CustomCursor>() {
        public CustomCursor createFromParcel(Parcel in) {
            return new CustomCursor(in);
        }

        public CustomCursor[] newArray(int size) {
            return new CustomCursor[size];
        }
    };
    private static final String TAG = CustomCursor.class.getSimpleName();
    private boolean isAutoClose = false;
    private Bundle mBundle = new Bundle();
    private String[] mColumnNames = null;
    private int mCursorOwnerId = -1;

    public CustomCursor(CursorWindow window) {
        super.setWindow(window);
    }

    public CustomCursor(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(super.getWindow(), 0);
        this.mBundle.putStringArray(BUNDLE_KEY, this.mColumnNames);
        dest.writeBundle(this.mBundle);
        dest.writeInt(this.mCursorOwnerId);
        if ((flags & 1) != 0 || this.isAutoClose) {
            close();
        }
    }

    public void readFromParcel(Parcel in) {
        super.setWindow((CursorWindow) in.readParcelable(CursorWindow.class.getClassLoader()));
        this.mBundle = in.readBundle();
        if (this.mBundle != null) {
            this.mColumnNames = this.mBundle.getStringArray(BUNDLE_KEY);
        }
        this.mCursorOwnerId = in.readInt();
    }

    public void setColumnNames(String[] columnNames) {
        this.mColumnNames = columnNames;
    }

    public String[] getColumnNames() {
        return this.mColumnNames;
    }

    public int getCount() {
        return super.getWindow().getNumRows();
    }

    public int getCursorOwnerId() {
        return this.mCursorOwnerId;
    }

    public void setCursorOwnerId(int cursorOwnerId) {
        this.mCursorOwnerId = cursorOwnerId;
    }

    public void setAutoClose(boolean value) {
        this.isAutoClose = value;
    }
}
