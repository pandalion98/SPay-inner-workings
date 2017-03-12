package android.view.accessibility;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.LongArray;
import android.util.Pools.SynchronizedPool;

public final class AccessibilityWindowInfo implements Parcelable {
    private static final int BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED = 4;
    private static final int BOOLEAN_PROPERTY_ACTIVE = 1;
    private static final int BOOLEAN_PROPERTY_FOCUSED = 2;
    public static final Creator<AccessibilityWindowInfo> CREATOR = new Creator<AccessibilityWindowInfo>() {
        public AccessibilityWindowInfo createFromParcel(Parcel parcel) {
            AccessibilityWindowInfo info = AccessibilityWindowInfo.obtain();
            info.initFromParcel(parcel);
            return info;
        }

        public AccessibilityWindowInfo[] newArray(int size) {
            return new AccessibilityWindowInfo[size];
        }
    };
    private static final boolean DEBUG = false;
    private static final int MAX_POOL_SIZE = 10;
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private static final SynchronizedPool<AccessibilityWindowInfo> sPool = new SynchronizedPool(10);
    private int mBooleanProperties;
    private final Rect mBoundsInScreen = new Rect();
    private LongArray mChildIds;
    private int mConnectionId = -1;
    private int mId = -1;
    private int mLayer = -1;
    private int mParentId = -1;
    private int mType = -1;

    private AccessibilityWindowInfo() {
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getLayer() {
        return this.mLayer;
    }

    public void setLayer(int layer) {
        this.mLayer = layer;
    }

    public AccessibilityNodeInfo getRoot() {
        if (this.mConnectionId == -1) {
            return null;
        }
        return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(this.mConnectionId, this.mId, AccessibilityNodeInfo.ROOT_NODE_ID, true, 4);
    }

    public AccessibilityWindowInfo getParent() {
        if (this.mConnectionId == -1 || this.mParentId == -1) {
            return null;
        }
        return AccessibilityInteractionClient.getInstance().getWindow(this.mConnectionId, this.mParentId);
    }

    public void setParentId(int parentId) {
        this.mParentId = parentId;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setConnectionId(int connectionId) {
        this.mConnectionId = connectionId;
    }

    public void getBoundsInScreen(Rect outBounds) {
        outBounds.set(this.mBoundsInScreen);
    }

    public void setBoundsInScreen(Rect bounds) {
        this.mBoundsInScreen.set(bounds);
    }

    public boolean isActive() {
        return getBooleanProperty(1);
    }

    public void setActive(boolean active) {
        setBooleanProperty(1, active);
    }

    public boolean isFocused() {
        return getBooleanProperty(2);
    }

    public void setFocused(boolean focused) {
        setBooleanProperty(2, focused);
    }

    public boolean isAccessibilityFocused() {
        return getBooleanProperty(4);
    }

    public void setAccessibilityFocused(boolean focused) {
        setBooleanProperty(4, focused);
    }

    public int getChildCount() {
        return this.mChildIds != null ? this.mChildIds.size() : 0;
    }

    public AccessibilityWindowInfo getChild(int index) {
        if (this.mChildIds == null) {
            throw new IndexOutOfBoundsException();
        } else if (this.mConnectionId == -1) {
            return null;
        } else {
            return AccessibilityInteractionClient.getInstance().getWindow(this.mConnectionId, (int) this.mChildIds.get(index));
        }
    }

    public void addChild(int childId) {
        if (this.mChildIds == null) {
            this.mChildIds = new LongArray();
        }
        this.mChildIds.add((long) childId);
    }

    public static AccessibilityWindowInfo obtain() {
        AccessibilityWindowInfo info = (AccessibilityWindowInfo) sPool.acquire();
        if (info == null) {
            return new AccessibilityWindowInfo();
        }
        return info;
    }

    public static AccessibilityWindowInfo obtain(AccessibilityWindowInfo info) {
        AccessibilityWindowInfo infoClone = obtain();
        infoClone.mType = info.mType;
        infoClone.mLayer = info.mLayer;
        infoClone.mBooleanProperties = info.mBooleanProperties;
        infoClone.mId = info.mId;
        infoClone.mParentId = info.mParentId;
        infoClone.mBoundsInScreen.set(info.mBoundsInScreen);
        if (info.mChildIds != null && info.mChildIds.size() > 0) {
            if (infoClone.mChildIds == null) {
                infoClone.mChildIds = info.mChildIds.clone();
            } else {
                infoClone.mChildIds.addAll(info.mChildIds);
            }
        }
        infoClone.mConnectionId = info.mConnectionId;
        return infoClone;
    }

    public void recycle() {
        clear();
        sPool.release(this);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mLayer);
        parcel.writeInt(this.mBooleanProperties);
        parcel.writeInt(this.mId);
        parcel.writeInt(this.mParentId);
        this.mBoundsInScreen.writeToParcel(parcel, flags);
        LongArray childIds = this.mChildIds;
        if (childIds == null) {
            parcel.writeInt(0);
        } else {
            int childCount = childIds.size();
            parcel.writeInt(childCount);
            for (int i = 0; i < childCount; i++) {
                parcel.writeInt((int) childIds.get(i));
            }
        }
        parcel.writeInt(this.mConnectionId);
    }

    private void initFromParcel(Parcel parcel) {
        this.mType = parcel.readInt();
        this.mLayer = parcel.readInt();
        this.mBooleanProperties = parcel.readInt();
        this.mId = parcel.readInt();
        this.mParentId = parcel.readInt();
        this.mBoundsInScreen.readFromParcel(parcel);
        int childCount = parcel.readInt();
        if (childCount > 0) {
            if (this.mChildIds == null) {
                this.mChildIds = new LongArray(childCount);
            }
            for (int i = 0; i < childCount; i++) {
                this.mChildIds.add((long) parcel.readInt());
            }
        }
        this.mConnectionId = parcel.readInt();
    }

    public int hashCode() {
        return this.mId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.mId != ((AccessibilityWindowInfo) obj).mId) {
            return false;
        }
        return true;
    }

    public String toString() {
        boolean z = true;
        StringBuilder builder = new StringBuilder();
        builder.append("AccessibilityWindowInfo[");
        builder.append("id=").append(this.mId);
        builder.append(", type=").append(typeToString(this.mType));
        builder.append(", layer=").append(this.mLayer);
        builder.append(", bounds=").append(this.mBoundsInScreen);
        builder.append(", focused=").append(isFocused());
        builder.append(", active=").append(isActive());
        builder.append(", hasParent=").append(this.mParentId != -1);
        StringBuilder append = builder.append(", hasChildren=");
        if (this.mChildIds == null || this.mChildIds.size() <= 0) {
            z = false;
        }
        append.append(z);
        builder.append(']');
        return builder.toString();
    }

    private void clear() {
        this.mType = -1;
        this.mLayer = -1;
        this.mBooleanProperties = 0;
        this.mId = -1;
        this.mParentId = -1;
        this.mBoundsInScreen.setEmpty();
        if (this.mChildIds != null) {
            this.mChildIds.clear();
        }
        this.mConnectionId = -1;
    }

    private boolean getBooleanProperty(int property) {
        return (this.mBooleanProperties & property) != 0;
    }

    private void setBooleanProperty(int property, boolean value) {
        if (value) {
            this.mBooleanProperties |= property;
        } else {
            this.mBooleanProperties &= property ^ -1;
        }
    }

    private static String typeToString(int type) {
        switch (type) {
            case 1:
                return "TYPE_APPLICATION";
            case 2:
                return "TYPE_INPUT_METHOD";
            case 3:
                return "TYPE_SYSTEM";
            case 4:
                return "TYPE_ACCESSIBILITY_OVERLAY";
            default:
                return "<UNKNOWN>";
        }
    }

    public boolean changed(AccessibilityWindowInfo other) {
        if (other.mId != this.mId) {
            throw new IllegalArgumentException("Not same window.");
        } else if (other.mType != this.mType) {
            throw new IllegalArgumentException("Not same type.");
        } else if (!this.mBoundsInScreen.equals(other.mBoundsInScreen) || this.mLayer != other.mLayer || this.mBooleanProperties != other.mBooleanProperties || this.mParentId != other.mParentId) {
            return true;
        } else {
            if (this.mChildIds == null) {
                if (other.mChildIds != null) {
                    return true;
                }
            } else if (!this.mChildIds.equals(other.mChildIds)) {
                return true;
            }
            return false;
        }
    }
}
