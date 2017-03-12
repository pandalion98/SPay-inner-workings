package com.samsung.android.cover;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CoverState implements Parcelable {
    public static final int COLOR_BLACK = 1;
    public static final int COLOR_BLUE = 5;
    public static final int COLOR_BLUSH_PINK = 8;
    public static final int COLOR_BRONZE = 14;
    public static final int COLOR_CARBON_METAL = 6;
    public static final int COLOR_CHARCOAL = 10;
    public static final int COLOR_CHARCOAL_GRAY = 10;
    public static final int COLOR_CLASSIC_WHITE = 2;
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_GOLD = 7;
    public static final int COLOR_GRAYISH_BLUE = 9;
    public static final int COLOR_GREEN = 11;
    public static final int COLOR_INDIGO_BLUE = 5;
    public static final int COLOR_JET_BLACK = 1;
    public static final int COLOR_MAGENTA = 3;
    public static final int COLOR_MINT = 9;
    public static final int COLOR_MINT_BLUE = 9;
    public static final int COLOR_MUSTARD_YELLOW = 12;
    public static final int COLOR_NAVY = 4;
    public static final int COLOR_NFC_SMART_COVER = 255;
    public static final int COLOR_OATMEAL = 12;
    public static final int COLOR_OATMEAL_BEIGE = 12;
    public static final int COLOR_ORANGE = 13;
    public static final int COLOR_PEAKCOCK_GREEN = 11;
    public static final int COLOR_PEARL_WHITE = 2;
    public static final int COLOR_PINK = 8;
    public static final int COLOR_PLUM = 3;
    public static final int COLOR_PLUM_RED = 3;
    public static final int COLOR_ROSE_GOLD = 7;
    public static final int COLOR_SILVER = 6;
    public static final int COLOR_SOFT_PINK = 8;
    public static final int COLOR_WHITE = 2;
    public static final int COLOR_WILD_ORANGE = 13;
    public static final int COLOR_YELLOW = 12;
    public static final boolean COVER_ATTACHED = true;
    public static final boolean COVER_DETACHED = false;
    public static final Creator<CoverState> CREATOR = new Creator<CoverState>() {
        public CoverState createFromParcel(Parcel parcel) {
            return new CoverState(parcel);
        }

        public CoverState[] newArray(int size) {
            return new CoverState[size];
        }
    };
    public static final int MODEL_DEFAULT = 0;
    public static final int MODEL_TB = 3;
    public static final int MODEL_TR = 2;
    public static final boolean SWITCH_STATE_COVER_CLOSE = false;
    public static final boolean SWITCH_STATE_COVER_OPEN = true;
    private static final String TAG = "CoverState";
    public static final int TYPE_BRAND_MONBLANC_COVER = 100;
    public static final int TYPE_CLEAR_COVER = 8;
    public static final int TYPE_FLIP_COVER = 0;
    public static final int TYPE_HEALTH_COVER = 4;
    public static final int TYPE_KEYBOARD_KOR_COVER = 9;
    public static final int TYPE_KEYBOARD_US_COVER = 10;
    public static final int TYPE_LED_COVER = 7;
    public static final int TYPE_NFC_SMART_COVER = 255;
    public static final int TYPE_NONE = 2;
    public static final int TYPE_SVIEW_CHARGER_COVER = 3;
    public static final int TYPE_SVIEW_COVER = 1;
    public static final int TYPE_S_CHARGER_COVER = 5;
    public static final int TYPE_S_VIEW_WALLET_COVER = 6;
    public boolean attached;
    public int color;
    public boolean fakeCover;
    public int heightPixel;
    public int model;
    public String serialNumber;
    public String smartCoverAppUri;
    public byte[] smartCoverCookie;
    public boolean switchState;
    public int type;
    public int widthPixel;

    public CoverState() {
        this.switchState = true;
        this.type = 2;
        this.color = 0;
        this.widthPixel = 0;
        this.heightPixel = 0;
        this.attached = false;
        this.model = 0;
        this.serialNumber = null;
        this.smartCoverCookie = null;
        this.serialNumber = null;
        this.fakeCover = false;
    }

    public CoverState(int defaultType, int defaultWidthPixel, int defaultHeightPixel) {
        this.switchState = true;
        this.type = defaultType;
        this.color = 0;
        this.widthPixel = defaultWidthPixel;
        this.heightPixel = defaultHeightPixel;
        this.attached = false;
        this.model = 0;
    }

    public CoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = false;
        this.model = 0;
    }

    public CoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = 0;
    }

    public CoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached, int model) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = model;
    }

    public CoverState(boolean switchState, int type, int color, boolean attached, int model, String installUri, byte[] vendorData, String serialNumber, boolean isFake) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.attached = attached;
        this.model = model;
        this.smartCoverAppUri = installUri;
        this.smartCoverCookie = vendorData;
        this.serialNumber = serialNumber;
        this.fakeCover = isFake;
    }

    public CoverState(Parcel src) {
        readFromParcel(src);
    }

    @Deprecated
    public int describeContents() {
        return 0;
    }

    @Deprecated
    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.switchState ? 1 : 0);
        dest.writeInt(this.type);
        dest.writeInt(this.color);
        dest.writeInt(this.widthPixel);
        dest.writeInt(this.heightPixel);
        if (this.attached) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.model);
        if (this.smartCoverAppUri == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(this.smartCoverAppUri);
        }
        if (this.smartCoverCookie == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeInt(this.smartCoverCookie.length);
            dest.writeByteArray(this.smartCoverCookie);
        }
        if (this.serialNumber == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(this.serialNumber);
        }
        if (!this.fakeCover) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    private void readFromParcel(Parcel src) {
        boolean z;
        boolean z2 = true;
        this.switchState = src.readInt() == 1;
        this.type = src.readInt();
        this.color = src.readInt();
        this.widthPixel = src.readInt();
        this.heightPixel = src.readInt();
        if (src.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.attached = z;
        this.model = src.readInt();
        if (src.readInt() == 1) {
            this.smartCoverAppUri = src.readString();
        }
        if (src.readInt() == 1) {
            this.smartCoverCookie = new byte[src.readInt()];
            src.readByteArray(this.smartCoverCookie);
        }
        if (src.readInt() == 1) {
            this.serialNumber = src.readString();
        }
        if (src.readInt() != 1) {
            z2 = false;
        }
        this.fakeCover = z2;
    }

    public String toString() {
        return String.format("CoverState(switchState=%b type=%d color=%d widthPixel=%d heightPixel=%d model=%d attached=%b)", new Object[]{Boolean.valueOf(this.switchState), Integer.valueOf(this.type), Integer.valueOf(this.color), Integer.valueOf(this.widthPixel), Integer.valueOf(this.heightPixel), Integer.valueOf(this.model), Boolean.valueOf(this.attached)});
    }

    public void updateCoverState(int type, int color, int widthPixel, int heightPixel) {
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
    }

    public void updateCoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
    }

    public void updateCoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
    }

    public void updateCoverState(int type, int color, int widthPixel, int heightPixel, int model) {
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.model = model;
    }

    public void updateCoverState(int type, int color, int widthPixel, int heightPixel, boolean attached, int model) {
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = model;
    }

    public void updateCoverWindowSize(int widthPixel, int heightPixel) {
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
    }

    public void copyFrom(CoverState o) {
        this.switchState = o.switchState;
        this.type = o.type;
        this.color = o.color;
        this.widthPixel = o.widthPixel;
        this.heightPixel = o.heightPixel;
        this.attached = o.attached;
        this.model = o.model;
        this.smartCoverAppUri = o.smartCoverAppUri;
        this.smartCoverCookie = o.smartCoverCookie;
        this.fakeCover = o.fakeCover;
        this.serialNumber = o.serialNumber;
    }

    public boolean getSwitchState() {
        return this.switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWindowWidth() {
        return this.widthPixel;
    }

    public void setWindowWidth(int width) {
        this.widthPixel = width;
    }

    public int getWindowHeight() {
        return this.heightPixel;
    }

    public void setWindowHeight(int height) {
        this.heightPixel = height;
    }

    public boolean getAttachState() {
        return this.attached;
    }

    public void setAttachState(boolean attached) {
        this.attached = attached;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public byte[] getSmartCoverCookie() {
        return this.smartCoverCookie;
    }

    public void setSmartCoverCookie(byte[] extraData) {
        this.smartCoverCookie = extraData;
    }

    public String getSmartCoverAppUri() {
        return this.smartCoverAppUri;
    }

    public void setSmartCoverAppUri(String uri) {
        this.smartCoverAppUri = uri;
    }

    public boolean isFakeCover() {
        return this.fakeCover;
    }

    public void setFakeCover(boolean faked) {
        this.fakeCover = faked;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serial) {
        this.serialNumber = serial;
    }
}
