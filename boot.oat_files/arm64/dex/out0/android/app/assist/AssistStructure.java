package android.app.assist;

import android.app.Activity;
import android.content.ComponentName;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PooledStringReader;
import android.os.PooledStringWriter;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.WindowManagerGlobal;
import java.util.ArrayList;

public class AssistStructure implements Parcelable {
    public static final Creator<AssistStructure> CREATOR = new Creator<AssistStructure>() {
        public AssistStructure createFromParcel(Parcel in) {
            return new AssistStructure(in);
        }

        public AssistStructure[] newArray(int size) {
            return new AssistStructure[size];
        }
    };
    static final boolean DEBUG_PARCEL = false;
    static final boolean DEBUG_PARCEL_CHILDREN = false;
    static final boolean DEBUG_PARCEL_TREE = false;
    static final String DESCRIPTOR = "android.app.AssistStructure";
    static final String TAG = "AssistStructure";
    static final int TRANSACTION_XFER = 2;
    static final int VALIDATE_VIEW_TOKEN = 572662306;
    static final int VALIDATE_WINDOW_TOKEN = 286331153;
    ComponentName mActivityComponent;
    boolean mHaveData;
    final ArrayList<ViewNodeBuilder> mPendingAsyncChildren;
    IBinder mReceiveChannel;
    SendChannel mSendChannel;
    Rect mTmpRect;
    final ArrayList<WindowNode> mWindowNodes;

    final class ParcelTransferReader {
        private final IBinder mChannel;
        private Parcel mCurParcel;
        int mNumReadViews;
        int mNumReadWindows;
        PooledStringReader mStringReader;
        final float[] mTmpMatrix = new float[9];
        private IBinder mTransferToken;

        ParcelTransferReader(IBinder channel) {
            this.mChannel = channel;
        }

        void go() {
            fetchData();
            AssistStructure.this.mActivityComponent = ComponentName.readFromParcel(this.mCurParcel);
            int N = this.mCurParcel.readInt();
            if (N > 0) {
                this.mStringReader = new PooledStringReader(this.mCurParcel);
                for (int i = 0; i < N; i++) {
                    AssistStructure.this.mWindowNodes.add(new WindowNode(this));
                }
            }
        }

        Parcel readParcel(int validateToken, int level) {
            int token = this.mCurParcel.readInt();
            if (token == 0) {
                this.mTransferToken = this.mCurParcel.readStrongBinder();
                if (this.mTransferToken == null) {
                    throw new IllegalStateException("Reached end of partial data without transfer token");
                }
                fetchData();
                this.mStringReader = new PooledStringReader(this.mCurParcel);
                this.mCurParcel.readInt();
                return this.mCurParcel;
            } else if (token == validateToken) {
                return this.mCurParcel;
            } else {
                throw new BadParcelableException("Got token " + Integer.toHexString(token) + ", expected token " + Integer.toHexString(validateToken));
            }
        }

        private void fetchData() {
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken(AssistStructure.DESCRIPTOR);
            data.writeStrongBinder(this.mTransferToken);
            if (this.mCurParcel != null) {
                this.mCurParcel.recycle();
            }
            this.mCurParcel = Parcel.obtain();
            try {
                this.mChannel.transact(2, data, this.mCurParcel, 0);
                data.recycle();
                this.mNumReadViews = 0;
                this.mNumReadWindows = 0;
            } catch (RemoteException e) {
                Log.w(AssistStructure.TAG, "Failure reading AssistStructure data", e);
                throw new IllegalStateException("Failure reading AssistStructure data: " + e);
            }
        }
    }

    static final class ParcelTransferWriter extends Binder {
        ViewStackEntry mCurViewStackEntry;
        int mCurViewStackPos;
        int mCurWindow;
        int mNumWindows;
        int mNumWrittenViews;
        int mNumWrittenWindows;
        final float[] mTmpMatrix = new float[9];
        final ArrayList<ViewStackEntry> mViewStack = new ArrayList();
        final boolean mWriteStructure;

        ParcelTransferWriter(AssistStructure as, Parcel out) {
            this.mWriteStructure = as.waitForReady();
            ComponentName.writeToParcel(as.mActivityComponent, out);
            this.mNumWindows = as.mWindowNodes.size();
            if (!this.mWriteStructure || this.mNumWindows <= 0) {
                out.writeInt(0);
            } else {
                out.writeInt(this.mNumWindows);
            }
        }

        void writeToParcel(AssistStructure as, Parcel out) {
            int start = out.dataPosition();
            this.mNumWrittenWindows = 0;
            this.mNumWrittenViews = 0;
            Log.i(AssistStructure.TAG, "Flattened " + (writeToParcelInner(as, out) ? "partial" : "final") + " assist data: " + (out.dataPosition() - start) + " bytes, containing " + this.mNumWrittenWindows + " windows, " + this.mNumWrittenViews + " views");
        }

        boolean writeToParcelInner(AssistStructure as, Parcel out) {
            if (this.mNumWindows == 0) {
                return false;
            }
            PooledStringWriter pwriter = new PooledStringWriter(out);
            while (writeNextEntryToParcel(as, out, pwriter)) {
                if (out.dataSize() > 65536) {
                    out.writeInt(0);
                    out.writeStrongBinder(this);
                    pwriter.finish();
                    return true;
                }
            }
            pwriter.finish();
            this.mViewStack.clear();
            return false;
        }

        void pushViewStackEntry(ViewNode node, int pos) {
            ViewStackEntry entry;
            if (pos >= this.mViewStack.size()) {
                entry = new ViewStackEntry();
                this.mViewStack.add(entry);
            } else {
                entry = (ViewStackEntry) this.mViewStack.get(pos);
            }
            entry.node = node;
            entry.numChildren = node.getChildCount();
            entry.curChild = 0;
            this.mCurViewStackEntry = entry;
        }

        void writeView(ViewNode child, Parcel out, PooledStringWriter pwriter, int levelAdj) {
            out.writeInt(AssistStructure.VALIDATE_VIEW_TOKEN);
            int flags = child.writeSelfToParcel(out, pwriter, this.mTmpMatrix);
            this.mNumWrittenViews++;
            if ((1048576 & flags) != 0) {
                out.writeInt(child.mChildren.length);
                int pos = this.mCurViewStackPos + 1;
                this.mCurViewStackPos = pos;
                pushViewStackEntry(child, pos);
            }
        }

        boolean writeNextEntryToParcel(AssistStructure as, Parcel out, PooledStringWriter pwriter) {
            int pos;
            if (this.mCurViewStackEntry == null) {
                pos = this.mCurWindow;
                if (pos >= this.mNumWindows) {
                    return false;
                }
                WindowNode win = (WindowNode) as.mWindowNodes.get(pos);
                this.mCurWindow++;
                out.writeInt(AssistStructure.VALIDATE_WINDOW_TOKEN);
                win.writeSelfToParcel(out, pwriter, this.mTmpMatrix);
                this.mNumWrittenWindows++;
                ViewNode root = win.mRoot;
                this.mCurViewStackPos = 0;
                writeView(root, out, pwriter, 0);
                return true;
            } else if (this.mCurViewStackEntry.curChild < this.mCurViewStackEntry.numChildren) {
                ViewNode child = this.mCurViewStackEntry.node.mChildren[this.mCurViewStackEntry.curChild];
                ViewStackEntry viewStackEntry = this.mCurViewStackEntry;
                viewStackEntry.curChild++;
                writeView(child, out, pwriter, 1);
                return true;
            } else {
                do {
                    pos = this.mCurViewStackPos - 1;
                    this.mCurViewStackPos = pos;
                    if (pos < 0) {
                        this.mCurViewStackEntry = null;
                        break;
                    }
                    this.mCurViewStackEntry = (ViewStackEntry) this.mViewStack.get(pos);
                } while (this.mCurViewStackEntry.curChild >= this.mCurViewStackEntry.numChildren);
                return true;
            }
        }
    }

    static final class SendChannel extends Binder {
        volatile AssistStructure mAssistStructure;

        SendChannel(AssistStructure as) {
            this.mAssistStructure = as;
        }

        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 2) {
                return super.onTransact(code, data, reply, flags);
            }
            AssistStructure as = this.mAssistStructure;
            if (as == null) {
                return true;
            }
            data.enforceInterface(AssistStructure.DESCRIPTOR);
            IBinder token = data.readStrongBinder();
            if (token == null) {
                new ParcelTransferWriter(as, reply).writeToParcel(as, reply);
                return true;
            } else if (token instanceof ParcelTransferWriter) {
                ((ParcelTransferWriter) token).writeToParcel(as, reply);
                return true;
            } else {
                Log.w(AssistStructure.TAG, "Caller supplied bad token type: " + token);
                return true;
            }
        }
    }

    public static class ViewNode {
        static final int FLAGS_ACCESSIBILITY_FOCUSED = 4096;
        static final int FLAGS_ACTIVATED = 8192;
        static final int FLAGS_ALL_CONTROL = -1048576;
        static final int FLAGS_ASSIST_BLOCKED = 128;
        static final int FLAGS_CHECKABLE = 256;
        static final int FLAGS_CHECKED = 512;
        static final int FLAGS_CLICKABLE = 1024;
        static final int FLAGS_CONTEXT_CLICKABLE = 16384;
        static final int FLAGS_DISABLED = 1;
        static final int FLAGS_FOCUSABLE = 16;
        static final int FLAGS_FOCUSED = 32;
        static final int FLAGS_HAS_ALPHA = 536870912;
        static final int FLAGS_HAS_CHILDREN = 1048576;
        static final int FLAGS_HAS_COMPLEX_TEXT = 8388608;
        static final int FLAGS_HAS_CONTENT_DESCRIPTION = 33554432;
        static final int FLAGS_HAS_ELEVATION = 268435456;
        static final int FLAGS_HAS_EXTRAS = 4194304;
        static final int FLAGS_HAS_ID = 2097152;
        static final int FLAGS_HAS_LARGE_COORDS = 67108864;
        static final int FLAGS_HAS_MATRIX = 1073741824;
        static final int FLAGS_HAS_SCROLL = 134217728;
        static final int FLAGS_HAS_TEXT = 16777216;
        static final int FLAGS_LONG_CLICKABLE = 2048;
        static final int FLAGS_SELECTED = 64;
        static final int FLAGS_VISIBILITY_MASK = 12;
        public static final int TEXT_COLOR_UNDEFINED = 1;
        public static final int TEXT_STYLE_BOLD = 1;
        public static final int TEXT_STYLE_ITALIC = 2;
        public static final int TEXT_STYLE_STRIKE_THRU = 8;
        public static final int TEXT_STYLE_UNDERLINE = 4;
        float mAlpha = 1.0f;
        ViewNode[] mChildren;
        String mClassName;
        CharSequence mContentDescription;
        float mElevation;
        Bundle mExtras;
        int mFlags;
        int mHeight;
        int mId = -1;
        String mIdEntry;
        String mIdPackage;
        String mIdType;
        Matrix mMatrix;
        int mScrollX;
        int mScrollY;
        ViewNodeText mText;
        int mWidth;
        int mX;
        int mY;

        ViewNode() {
        }

        ViewNode(ParcelTransferReader reader, int nestingLevel) {
            Parcel in = reader.readParcel(AssistStructure.VALIDATE_VIEW_TOKEN, nestingLevel);
            reader.mNumReadViews++;
            PooledStringReader preader = reader.mStringReader;
            this.mClassName = preader.readString();
            this.mFlags = in.readInt();
            int flags = this.mFlags;
            if ((2097152 & flags) != 0) {
                this.mId = in.readInt();
                if (this.mId != 0) {
                    this.mIdEntry = preader.readString();
                    if (this.mIdEntry != null) {
                        this.mIdType = preader.readString();
                        this.mIdPackage = preader.readString();
                    }
                }
            }
            if ((67108864 & flags) != 0) {
                this.mX = in.readInt();
                this.mY = in.readInt();
                this.mWidth = in.readInt();
                this.mHeight = in.readInt();
            } else {
                int val = in.readInt();
                this.mX = val & 32767;
                this.mY = (val >> 16) & 32767;
                val = in.readInt();
                this.mWidth = val & 32767;
                this.mHeight = (val >> 16) & 32767;
            }
            if ((134217728 & flags) != 0) {
                this.mScrollX = in.readInt();
                this.mScrollY = in.readInt();
            }
            if ((1073741824 & flags) != 0) {
                this.mMatrix = new Matrix();
                in.readFloatArray(reader.mTmpMatrix);
                this.mMatrix.setValues(reader.mTmpMatrix);
            }
            if ((268435456 & flags) != 0) {
                this.mElevation = in.readFloat();
            }
            if ((536870912 & flags) != 0) {
                this.mAlpha = in.readFloat();
            }
            if ((33554432 & flags) != 0) {
                this.mContentDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if ((16777216 & flags) != 0) {
                this.mText = new ViewNodeText(in, (8388608 & flags) == 0);
            }
            if ((4194304 & flags) != 0) {
                this.mExtras = in.readBundle();
            }
            if ((1048576 & flags) != 0) {
                int NCHILDREN = in.readInt();
                this.mChildren = new ViewNode[NCHILDREN];
                for (int i = 0; i < NCHILDREN; i++) {
                    this.mChildren[i] = new ViewNode(reader, nestingLevel + 1);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        int writeSelfToParcel(android.os.Parcel r9, android.os.PooledStringWriter r10, float[] r11) {
            /*
            r8 = this;
            r7 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
            r6 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
            r5 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
            r2 = 1;
            r3 = 0;
            r1 = r8.mFlags;
            r4 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
            r0 = r1 & r4;
            r1 = r8.mId;
            r4 = -1;
            if (r1 == r4) goto L_0x0015;
        L_0x0014:
            r0 = r0 | r5;
        L_0x0015:
            r1 = r8.mX;
            r1 = r1 & -32768;
            if (r1 != 0) goto L_0x0032;
        L_0x001b:
            r1 = r8.mY;
            r1 = r1 & -32768;
            if (r1 != 0) goto L_0x0032;
        L_0x0021:
            r1 = r8.mWidth;
            r1 = r1 & -32768;
            if (r1 == 0) goto L_0x0112;
        L_0x0027:
            r1 = r2;
        L_0x0028:
            r4 = r8.mHeight;
            r4 = r4 & -32768;
            if (r4 == 0) goto L_0x0115;
        L_0x002e:
            r4 = r2;
        L_0x002f:
            r1 = r1 | r4;
            if (r1 == 0) goto L_0x0035;
        L_0x0032:
            r1 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
            r0 = r0 | r1;
        L_0x0035:
            r1 = r8.mScrollX;
            if (r1 != 0) goto L_0x003d;
        L_0x0039:
            r1 = r8.mScrollY;
            if (r1 == 0) goto L_0x0040;
        L_0x003d:
            r1 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
            r0 = r0 | r1;
        L_0x0040:
            r1 = r8.mMatrix;
            if (r1 == 0) goto L_0x0047;
        L_0x0044:
            r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r0 = r0 | r1;
        L_0x0047:
            r1 = r8.mElevation;
            r4 = 0;
            r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
            if (r1 == 0) goto L_0x0051;
        L_0x004e:
            r1 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
            r0 = r0 | r1;
        L_0x0051:
            r1 = r8.mAlpha;
            r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
            if (r1 == 0) goto L_0x005c;
        L_0x0059:
            r1 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
            r0 = r0 | r1;
        L_0x005c:
            r1 = r8.mContentDescription;
            if (r1 == 0) goto L_0x0063;
        L_0x0060:
            r1 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
            r0 = r0 | r1;
        L_0x0063:
            r1 = r8.mText;
            if (r1 == 0) goto L_0x0073;
        L_0x0067:
            r1 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
            r0 = r0 | r1;
            r1 = r8.mText;
            r1 = r1.isSimple();
            if (r1 != 0) goto L_0x0073;
        L_0x0072:
            r0 = r0 | r7;
        L_0x0073:
            r1 = r8.mExtras;
            if (r1 == 0) goto L_0x0078;
        L_0x0077:
            r0 = r0 | r6;
        L_0x0078:
            r1 = r8.mChildren;
            if (r1 == 0) goto L_0x007f;
        L_0x007c:
            r1 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
            r0 = r0 | r1;
        L_0x007f:
            r1 = r8.mClassName;
            r10.writeString(r1);
            r9.writeInt(r0);
            r1 = r0 & r5;
            if (r1 == 0) goto L_0x00a7;
        L_0x008b:
            r1 = r8.mId;
            r9.writeInt(r1);
            r1 = r8.mId;
            if (r1 == 0) goto L_0x00a7;
        L_0x0094:
            r1 = r8.mIdEntry;
            r10.writeString(r1);
            r1 = r8.mIdEntry;
            if (r1 == 0) goto L_0x00a7;
        L_0x009d:
            r1 = r8.mIdType;
            r10.writeString(r1);
            r1 = r8.mIdPackage;
            r10.writeString(r1);
        L_0x00a7:
            r1 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x0118;
        L_0x00ac:
            r1 = r8.mX;
            r9.writeInt(r1);
            r1 = r8.mY;
            r9.writeInt(r1);
            r1 = r8.mWidth;
            r9.writeInt(r1);
            r1 = r8.mHeight;
            r9.writeInt(r1);
        L_0x00c0:
            r1 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x00cf;
        L_0x00c5:
            r1 = r8.mScrollX;
            r9.writeInt(r1);
            r1 = r8.mScrollY;
            r9.writeInt(r1);
        L_0x00cf:
            r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x00dc;
        L_0x00d4:
            r1 = r8.mMatrix;
            r1.getValues(r11);
            r9.writeFloatArray(r11);
        L_0x00dc:
            r1 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x00e6;
        L_0x00e1:
            r1 = r8.mElevation;
            r9.writeFloat(r1);
        L_0x00e6:
            r1 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x00f0;
        L_0x00eb:
            r1 = r8.mAlpha;
            r9.writeFloat(r1);
        L_0x00f0:
            r1 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x00fa;
        L_0x00f5:
            r1 = r8.mContentDescription;
            android.text.TextUtils.writeToParcel(r1, r9, r3);
        L_0x00fa:
            r1 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
            r1 = r1 & r0;
            if (r1 == 0) goto L_0x0108;
        L_0x00ff:
            r1 = r8.mText;
            r4 = r0 & r7;
            if (r4 != 0) goto L_0x012d;
        L_0x0105:
            r1.writeToParcel(r9, r2);
        L_0x0108:
            r1 = r0 & r6;
            if (r1 == 0) goto L_0x0111;
        L_0x010c:
            r1 = r8.mExtras;
            r9.writeBundle(r1);
        L_0x0111:
            return r0;
        L_0x0112:
            r1 = r3;
            goto L_0x0028;
        L_0x0115:
            r4 = r3;
            goto L_0x002f;
        L_0x0118:
            r1 = r8.mY;
            r1 = r1 << 16;
            r4 = r8.mX;
            r1 = r1 | r4;
            r9.writeInt(r1);
            r1 = r8.mHeight;
            r1 = r1 << 16;
            r4 = r8.mWidth;
            r1 = r1 | r4;
            r9.writeInt(r1);
            goto L_0x00c0;
        L_0x012d:
            r2 = r3;
            goto L_0x0105;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.assist.AssistStructure.ViewNode.writeSelfToParcel(android.os.Parcel, android.os.PooledStringWriter, float[]):int");
        }

        public int getId() {
            return this.mId;
        }

        public String getIdPackage() {
            return this.mIdPackage;
        }

        public String getIdType() {
            return this.mIdType;
        }

        public String getIdEntry() {
            return this.mIdEntry;
        }

        public int getLeft() {
            return this.mX;
        }

        public int getTop() {
            return this.mY;
        }

        public int getScrollX() {
            return this.mScrollX;
        }

        public int getScrollY() {
            return this.mScrollY;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public Matrix getTransformation() {
            return this.mMatrix;
        }

        public float getElevation() {
            return this.mElevation;
        }

        public float getAlpha() {
            return this.mAlpha;
        }

        public int getVisibility() {
            return this.mFlags & 12;
        }

        public boolean isAssistBlocked() {
            return (this.mFlags & 128) != 0;
        }

        public boolean isEnabled() {
            return (this.mFlags & 1) == 0;
        }

        public boolean isClickable() {
            return (this.mFlags & 1024) != 0;
        }

        public boolean isFocusable() {
            return (this.mFlags & 16) != 0;
        }

        public boolean isFocused() {
            return (this.mFlags & 32) != 0;
        }

        public boolean isAccessibilityFocused() {
            return (this.mFlags & 4096) != 0;
        }

        public boolean isCheckable() {
            return (this.mFlags & 256) != 0;
        }

        public boolean isChecked() {
            return (this.mFlags & 512) != 0;
        }

        public boolean isSelected() {
            return (this.mFlags & 64) != 0;
        }

        public boolean isActivated() {
            return (this.mFlags & 8192) != 0;
        }

        public boolean isLongClickable() {
            return (this.mFlags & 2048) != 0;
        }

        public boolean isContextClickable() {
            return (this.mFlags & 16384) != 0;
        }

        public String getClassName() {
            return this.mClassName;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        public CharSequence getText() {
            return this.mText != null ? this.mText.mText : null;
        }

        public int getTextSelectionStart() {
            return this.mText != null ? this.mText.mTextSelectionStart : -1;
        }

        public int getTextSelectionEnd() {
            return this.mText != null ? this.mText.mTextSelectionEnd : -1;
        }

        public int getTextColor() {
            return this.mText != null ? this.mText.mTextColor : 1;
        }

        public int getTextBackgroundColor() {
            return this.mText != null ? this.mText.mTextBackgroundColor : 1;
        }

        public float getTextSize() {
            return this.mText != null ? this.mText.mTextSize : 0.0f;
        }

        public int getTextStyle() {
            return this.mText != null ? this.mText.mTextStyle : 0;
        }

        public int[] getTextLineCharOffsets() {
            return this.mText != null ? this.mText.mLineCharOffsets : null;
        }

        public int[] getTextLineBaselines() {
            return this.mText != null ? this.mText.mLineBaselines : null;
        }

        public String getHint() {
            return this.mText != null ? this.mText.mHint : null;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int getChildCount() {
            return this.mChildren != null ? this.mChildren.length : 0;
        }

        public ViewNode getChildAt(int index) {
            return this.mChildren[index];
        }
    }

    static class ViewNodeBuilder extends ViewStructure {
        final AssistStructure mAssist;
        final boolean mAsync;
        final ViewNode mNode;

        ViewNodeBuilder(AssistStructure assist, ViewNode node, boolean async) {
            this.mAssist = assist;
            this.mNode = node;
            this.mAsync = async;
        }

        public void setId(int id, String packageName, String typeName, String entryName) {
            this.mNode.mId = id;
            this.mNode.mIdPackage = packageName;
            this.mNode.mIdType = typeName;
            this.mNode.mIdEntry = entryName;
        }

        public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
            this.mNode.mX = left;
            this.mNode.mY = top;
            this.mNode.mScrollX = scrollX;
            this.mNode.mScrollY = scrollY;
            this.mNode.mWidth = width;
            this.mNode.mHeight = height;
        }

        public void setTransformation(Matrix matrix) {
            if (matrix == null) {
                this.mNode.mMatrix = null;
                return;
            }
            this.mNode.mMatrix = new Matrix(matrix);
        }

        public void setElevation(float elevation) {
            this.mNode.mElevation = elevation;
        }

        public void setAlpha(float alpha) {
            this.mNode.mAlpha = alpha;
        }

        public void setVisibility(int visibility) {
            this.mNode.mFlags = (this.mNode.mFlags & -13) | visibility;
        }

        public void setAssistBlocked(boolean state) {
            this.mNode.mFlags = (state ? 128 : 0) | (this.mNode.mFlags & -129);
        }

        public void setEnabled(boolean state) {
            this.mNode.mFlags = (state ? 0 : 1) | (this.mNode.mFlags & -2);
        }

        public void setClickable(boolean state) {
            this.mNode.mFlags = (state ? 1024 : 0) | (this.mNode.mFlags & -1025);
        }

        public void setLongClickable(boolean state) {
            this.mNode.mFlags = (state ? 2048 : 0) | (this.mNode.mFlags & -2049);
        }

        public void setContextClickable(boolean state) {
            this.mNode.mFlags = (state ? 16384 : 0) | (this.mNode.mFlags & -16385);
        }

        public void setFocusable(boolean state) {
            this.mNode.mFlags = (state ? 16 : 0) | (this.mNode.mFlags & -17);
        }

        public void setFocused(boolean state) {
            this.mNode.mFlags = (state ? 32 : 0) | (this.mNode.mFlags & -33);
        }

        public void setAccessibilityFocused(boolean state) {
            this.mNode.mFlags = (state ? 4096 : 0) | (this.mNode.mFlags & -4097);
        }

        public void setCheckable(boolean state) {
            this.mNode.mFlags = (state ? 256 : 0) | (this.mNode.mFlags & -257);
        }

        public void setChecked(boolean state) {
            this.mNode.mFlags = (state ? 512 : 0) | (this.mNode.mFlags & -513);
        }

        public void setSelected(boolean state) {
            this.mNode.mFlags = (state ? 64 : 0) | (this.mNode.mFlags & -65);
        }

        public void setActivated(boolean state) {
            this.mNode.mFlags = (state ? 8192 : 0) | (this.mNode.mFlags & -8193);
        }

        public void setClassName(String className) {
            this.mNode.mClassName = className;
        }

        public void setContentDescription(CharSequence contentDescription) {
            this.mNode.mContentDescription = contentDescription;
        }

        private final ViewNodeText getNodeText() {
            if (this.mNode.mText != null) {
                return this.mNode.mText;
            }
            this.mNode.mText = new ViewNodeText();
            return this.mNode.mText;
        }

        public void setText(CharSequence text) {
            ViewNodeText t = getNodeText();
            t.mText = text;
            t.mTextSelectionEnd = -1;
            t.mTextSelectionStart = -1;
        }

        public void setText(CharSequence text, int selectionStart, int selectionEnd) {
            ViewNodeText t = getNodeText();
            t.mText = text;
            t.mTextSelectionStart = selectionStart;
            t.mTextSelectionEnd = selectionEnd;
        }

        public void setTextStyle(float size, int fgColor, int bgColor, int style) {
            ViewNodeText t = getNodeText();
            t.mTextColor = fgColor;
            t.mTextBackgroundColor = bgColor;
            t.mTextSize = size;
            t.mTextStyle = style;
        }

        public void setTextLines(int[] charOffsets, int[] baselines) {
            ViewNodeText t = getNodeText();
            t.mLineCharOffsets = charOffsets;
            t.mLineBaselines = baselines;
        }

        public void setHint(CharSequence hint) {
            getNodeText().mHint = hint != null ? hint.toString() : null;
        }

        public CharSequence getText() {
            return this.mNode.mText != null ? this.mNode.mText.mText : null;
        }

        public int getTextSelectionStart() {
            return this.mNode.mText != null ? this.mNode.mText.mTextSelectionStart : -1;
        }

        public int getTextSelectionEnd() {
            return this.mNode.mText != null ? this.mNode.mText.mTextSelectionEnd : -1;
        }

        public CharSequence getHint() {
            return this.mNode.mText != null ? this.mNode.mText.mHint : null;
        }

        public Bundle getExtras() {
            if (this.mNode.mExtras != null) {
                return this.mNode.mExtras;
            }
            this.mNode.mExtras = new Bundle();
            return this.mNode.mExtras;
        }

        public boolean hasExtras() {
            return this.mNode.mExtras != null;
        }

        public void setChildCount(int num) {
            this.mNode.mChildren = new ViewNode[num];
        }

        public int addChildCount(int num) {
            if (this.mNode.mChildren == null) {
                setChildCount(num);
                return 0;
            }
            int start = this.mNode.mChildren.length;
            ViewNode[] newArray = new ViewNode[(start + num)];
            System.arraycopy(this.mNode.mChildren, 0, newArray, 0, start);
            this.mNode.mChildren = newArray;
            return start;
        }

        public int getChildCount() {
            return this.mNode.mChildren != null ? this.mNode.mChildren.length : 0;
        }

        public ViewStructure newChild(int index) {
            ViewNode node = new ViewNode();
            this.mNode.mChildren[index] = node;
            return new ViewNodeBuilder(this.mAssist, node, false);
        }

        public ViewStructure asyncNewChild(int index) {
            ViewNodeBuilder builder;
            synchronized (this.mAssist) {
                ViewNode node = new ViewNode();
                this.mNode.mChildren[index] = node;
                builder = new ViewNodeBuilder(this.mAssist, node, true);
                this.mAssist.mPendingAsyncChildren.add(builder);
            }
            return builder;
        }

        public void asyncCommit() {
            synchronized (this.mAssist) {
                if (!this.mAsync) {
                    throw new IllegalStateException("Child " + this + " was not created with ViewStructure.asyncNewChild");
                } else if (this.mAssist.mPendingAsyncChildren.remove(this)) {
                    this.mAssist.notifyAll();
                } else {
                    throw new IllegalStateException("Child " + this + " already committed");
                }
            }
        }

        public Rect getTempRect() {
            return this.mAssist.mTmpRect;
        }
    }

    static final class ViewNodeText {
        String mHint;
        int[] mLineBaselines;
        int[] mLineCharOffsets;
        CharSequence mText;
        int mTextBackgroundColor = 1;
        int mTextColor = 1;
        int mTextSelectionEnd;
        int mTextSelectionStart;
        float mTextSize;
        int mTextStyle;

        ViewNodeText() {
        }

        boolean isSimple() {
            return this.mTextBackgroundColor == 1 && this.mTextSelectionStart == 0 && this.mTextSelectionEnd == 0 && this.mLineCharOffsets == null && this.mLineBaselines == null && this.mHint == null;
        }

        ViewNodeText(Parcel in, boolean simple) {
            this.mText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.mTextSize = in.readFloat();
            this.mTextStyle = in.readInt();
            this.mTextColor = in.readInt();
            if (!simple) {
                this.mTextBackgroundColor = in.readInt();
                this.mTextSelectionStart = in.readInt();
                this.mTextSelectionEnd = in.readInt();
                this.mLineCharOffsets = in.createIntArray();
                this.mLineBaselines = in.createIntArray();
                this.mHint = in.readString();
            }
        }

        void writeToParcel(Parcel out, boolean simple) {
            TextUtils.writeToParcel(this.mText, out, 0);
            out.writeFloat(this.mTextSize);
            out.writeInt(this.mTextStyle);
            out.writeInt(this.mTextColor);
            if (!simple) {
                out.writeInt(this.mTextBackgroundColor);
                out.writeInt(this.mTextSelectionStart);
                out.writeInt(this.mTextSelectionEnd);
                out.writeIntArray(this.mLineCharOffsets);
                out.writeIntArray(this.mLineBaselines);
                out.writeString(this.mHint);
            }
        }
    }

    static final class ViewStackEntry {
        int curChild;
        ViewNode node;
        int numChildren;

        ViewStackEntry() {
        }
    }

    public static class WindowNode {
        final int mDisplayId;
        final int mHeight;
        final ViewNode mRoot;
        final CharSequence mTitle;
        final int mWidth;
        final int mX;
        final int mY;

        WindowNode(AssistStructure assist, ViewRootImpl root) {
            View view = root.getView();
            Rect rect = new Rect();
            view.getBoundsOnScreen(rect);
            this.mX = rect.left - view.getLeft();
            this.mY = rect.top - view.getTop();
            this.mWidth = rect.width();
            this.mHeight = rect.height();
            this.mTitle = root.getTitle();
            this.mDisplayId = root.getDisplayId();
            this.mRoot = new ViewNode();
            ViewNodeBuilder builder = new ViewNodeBuilder(assist, this.mRoot, false);
            if ((root.getWindowFlags() & 8192) != 0) {
                view.onProvideStructure(builder);
                builder.setAssistBlocked(true);
                return;
            }
            view.dispatchProvideStructure(builder);
        }

        WindowNode(ParcelTransferReader reader) {
            Parcel in = reader.readParcel(AssistStructure.VALIDATE_WINDOW_TOKEN, 0);
            reader.mNumReadWindows++;
            this.mX = in.readInt();
            this.mY = in.readInt();
            this.mWidth = in.readInt();
            this.mHeight = in.readInt();
            this.mTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.mDisplayId = in.readInt();
            this.mRoot = new ViewNode(reader, 0);
        }

        void writeSelfToParcel(Parcel out, PooledStringWriter pwriter, float[] tmpMatrix) {
            out.writeInt(this.mX);
            out.writeInt(this.mY);
            out.writeInt(this.mWidth);
            out.writeInt(this.mHeight);
            TextUtils.writeToParcel(this.mTitle, out, 0);
            out.writeInt(this.mDisplayId);
        }

        public int getLeft() {
            return this.mX;
        }

        public int getTop() {
            return this.mY;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public int getDisplayId() {
            return this.mDisplayId;
        }

        public ViewNode getRootViewNode() {
            return this.mRoot;
        }
    }

    public AssistStructure(Activity activity) {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        this.mHaveData = true;
        this.mActivityComponent = activity.getComponentName();
        ArrayList<ViewRootImpl> views = WindowManagerGlobal.getInstance().getRootViews(activity.getActivityToken());
        for (int i = 0; i < views.size(); i++) {
            this.mWindowNodes.add(new WindowNode(this, (ViewRootImpl) views.get(i)));
        }
    }

    public AssistStructure() {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        this.mHaveData = true;
        this.mActivityComponent = null;
    }

    public AssistStructure(Parcel in) {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        this.mReceiveChannel = in.readStrongBinder();
    }

    public void dump() {
        Log.i(TAG, "Activity: " + this.mActivityComponent.flattenToShortString());
        int N = getWindowNodeCount();
        for (int i = 0; i < N; i++) {
            WindowNode node = getWindowNodeAt(i);
            Log.i(TAG, "Window #" + i + " [" + node.getLeft() + "," + node.getTop() + " " + node.getWidth() + "x" + node.getHeight() + "]" + " " + node.getTitle());
            dump("  ", node.getRootViewNode());
        }
    }

    void dump(String prefix, ViewNode node) {
        Log.i(TAG, prefix + "View [" + node.getLeft() + "," + node.getTop() + " " + node.getWidth() + "x" + node.getHeight() + "]" + " " + node.getClassName());
        int id = node.getId();
        if (id != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append("  ID: #");
            sb.append(Integer.toHexString(id));
            String entry = node.getIdEntry();
            if (entry != null) {
                String type = node.getIdType();
                String pkg = node.getIdPackage();
                sb.append(" ");
                sb.append(pkg);
                sb.append(":");
                sb.append(type);
                sb.append("/");
                sb.append(entry);
            }
            Log.i(TAG, sb.toString());
        }
        int scrollX = node.getScrollX();
        int scrollY = node.getScrollY();
        if (!(scrollX == 0 && scrollY == 0)) {
            Log.i(TAG, prefix + "  Scroll: " + scrollX + "," + scrollY);
        }
        Matrix matrix = node.getTransformation();
        if (matrix != null) {
            Log.i(TAG, prefix + "  Transformation: " + matrix);
        }
        float elevation = node.getElevation();
        if (elevation != 0.0f) {
            Log.i(TAG, prefix + "  Elevation: " + elevation);
        }
        if (node.getAlpha() != 0.0f) {
            Log.i(TAG, prefix + "  Alpha: " + elevation);
        }
        CharSequence contentDescription = node.getContentDescription();
        if (contentDescription != null) {
            Log.i(TAG, prefix + "  Content description: " + contentDescription);
        }
        CharSequence text = node.getText();
        if (text != null) {
            Log.i(TAG, prefix + "  Text (sel " + node.getTextSelectionStart() + "-" + node.getTextSelectionEnd() + "): " + text);
            Log.i(TAG, prefix + "  Text size: " + node.getTextSize() + " , style: #" + node.getTextStyle());
            Log.i(TAG, prefix + "  Text color fg: #" + Integer.toHexString(node.getTextColor()) + ", bg: #" + Integer.toHexString(node.getTextBackgroundColor()));
        }
        String hint = node.getHint();
        if (hint != null) {
            Log.i(TAG, prefix + "  Hint: " + hint);
        }
        Bundle extras = node.getExtras();
        if (extras != null) {
            Log.i(TAG, prefix + "  Extras: " + extras);
        }
        if (node.isAssistBlocked()) {
            Log.i(TAG, prefix + "  BLOCKED");
        }
        int NCHILDREN = node.getChildCount();
        if (NCHILDREN > 0) {
            Log.i(TAG, prefix + "  Children:");
            String cprefix = prefix + "    ";
            for (int i = 0; i < NCHILDREN; i++) {
                dump(cprefix, node.getChildAt(i));
            }
        }
    }

    public ComponentName getActivityComponent() {
        ensureData();
        return this.mActivityComponent;
    }

    public int getWindowNodeCount() {
        ensureData();
        return this.mWindowNodes.size();
    }

    public WindowNode getWindowNodeAt(int index) {
        ensureData();
        return (WindowNode) this.mWindowNodes.get(index);
    }

    public void ensureData() {
        if (!this.mHaveData) {
            this.mHaveData = true;
            new ParcelTransferReader(this.mReceiveChannel).go();
        }
    }

    boolean waitForReady() {
        boolean skipStructure = false;
        synchronized (this) {
            long endTime = SystemClock.uptimeMillis() + 5000;
            while (this.mPendingAsyncChildren.size() > 0) {
                long now = SystemClock.uptimeMillis();
                if (now < endTime) {
                    try {
                        wait(endTime - now);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (this.mPendingAsyncChildren.size() > 0) {
                Log.w(TAG, "Skipping assist structure, waiting too long for async children (have " + this.mPendingAsyncChildren.size() + " remaining");
                skipStructure = true;
            }
        }
        return !skipStructure;
    }

    public void clearSendChannel() {
        if (this.mSendChannel != null) {
            this.mSendChannel.mAssistStructure = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (this.mHaveData) {
            if (this.mSendChannel == null) {
                this.mSendChannel = new SendChannel(this);
            }
            out.writeStrongBinder(this.mSendChannel);
            return;
        }
        out.writeStrongBinder(this.mReceiveChannel);
    }
}
