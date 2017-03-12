package com.samsung.android.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;
import java.util.Iterator;

public class SmartClipMetaTagArrayImpl extends SlookSmartClipMetaTagArray implements Parcelable {
    public static final Creator<SmartClipMetaTagArrayImpl> CREATOR = new Creator<SmartClipMetaTagArrayImpl>() {
        public SmartClipMetaTagArrayImpl createFromParcel(Parcel in) {
            Log.d(SmartClipMetaTagArrayImpl.TAG, "SmartClipMetaTagArrayImpl.createFromParcel called");
            SmartClipMetaTagArrayImpl data = new SmartClipMetaTagArrayImpl();
            data.readFromParcel(in);
            return data;
        }

        public SmartClipMetaTagArrayImpl[] newArray(int size) {
            return new SmartClipMetaTagArrayImpl[size];
        }
    };
    private static final String TAG = "SmartClipMetaTagArrayImpl";

    public void removeTag(String tagType) {
        for (int i = size() - 1; i >= 0; i--) {
            if (((SlookSmartClipMetaTag) get(i)).getType().equals(tagType)) {
                remove(i);
            }
        }
    }

    public SlookSmartClipMetaTagArray getTag(String tagType) {
        SmartClipMetaTagArrayImpl resultArray = new SmartClipMetaTagArrayImpl();
        int arrayLen = size();
        for (int i = 0; i < arrayLen; i++) {
            SlookSmartClipMetaTag tag = (SlookSmartClipMetaTag) get(i);
            if (tag.getType().equals(tagType)) {
                resultArray.add(tag);
            }
        }
        return resultArray;
    }

    public void addTag(SlookSmartClipMetaTag tag) {
        add(tag);
    }

    public void addTag(SlookSmartClipMetaTagArray tagArray) {
        if (tagArray != null) {
            Iterator i$ = tagArray.iterator();
            while (i$.hasNext()) {
                add((SlookSmartClipMetaTag) i$.next());
            }
        }
    }

    public SmartClipMetaTagArrayImpl getCopy() {
        SmartClipMetaTagArrayImpl copy = new SmartClipMetaTagArrayImpl();
        int arrayLen = size();
        for (int i = 0; i < arrayLen; i++) {
            copy.add((SlookSmartClipMetaTag) get(i));
        }
        return copy;
    }

    public void dump() {
        int tagCount = size();
        for (int i = 0; i < tagCount; i++) {
            SlookSmartClipMetaTag tag = (SlookSmartClipMetaTag) get(i);
            String type = tag.getType();
            String value = tag.getValue();
            String extra = "";
            if (value == null) {
                value = new String("null");
            }
            if (tag instanceof SmartClipMetaTagImpl) {
                SmartClipMetaTagImpl tagImpl = (SmartClipMetaTagImpl) tag;
                if (tagImpl.getExtraData() != null) {
                    extra = ", Extra data size = " + tagImpl.getExtraData().length;
                }
            }
            Log.d(TAG, type + "(" + value + extra + ")");
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int count = size();
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            SlookSmartClipMetaTag tag = (SlookSmartClipMetaTag) get(i);
            if (tag instanceof SmartClipMetaTagImpl) {
                SmartClipMetaTagImpl tagImpl = (SmartClipMetaTagImpl) tag;
                out.writeString("ParcelableMetaTag");
                out.writeParcelable(tagImpl, 0);
            } else {
                out.writeString("BasicMetaTag");
                out.writeString(tag.getType());
                out.writeString(tag.getValue());
            }
        }
    }

    public void readFromParcel(Parcel in) {
        int tagCount = in.readInt();
        int i = 0;
        while (i < tagCount) {
            String objId = in.readString();
            SlookSmartClipMetaTag tag = null;
            if (objId.equals("BasicMetaTag")) {
                tag = new SlookSmartClipMetaTag(in.readString(), in.readString());
            } else if (objId.equals("ParcelableMetaTag")) {
                tag = (SlookSmartClipMetaTag) in.readParcelable(null);
            } else {
                Log.e(TAG, "readFromParcel : Unknown meta tag type!!! : " + objId);
            }
            if (tag == null) {
                Log.e(TAG, "readFromParcel : Could not read tag!!");
                return;
            } else {
                add(tag);
                i++;
            }
        }
    }
}
