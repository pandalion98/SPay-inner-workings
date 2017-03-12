package android.view;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.samsung.android.smartclip.SmartClipRemoteRequestInfo;

public interface IWindow extends IInterface {

    public static abstract class Stub extends Binder implements IWindow {
        private static final String DESCRIPTOR = "android.view.IWindow";
        static final int TRANSACTION_closeSystemDialogs = 8;
        static final int TRANSACTION_dispatchAppVisibility = 5;
        static final int TRANSACTION_dispatchAttachedDisplayChanged = 3;
        static final int TRANSACTION_dispatchCoverStateChanged = 17;
        static final int TRANSACTION_dispatchDragEvent = 11;
        static final int TRANSACTION_dispatchGetNewSurface = 6;
        static final int TRANSACTION_dispatchMultiWindowStateChanged = 19;
        static final int TRANSACTION_dispatchSmartClipRemoteRequest = 16;
        static final int TRANSACTION_dispatchSystemUiVisibilityChanged = 12;
        static final int TRANSACTION_dispatchWallpaperCommand = 10;
        static final int TRANSACTION_dispatchWallpaperOffsets = 9;
        static final int TRANSACTION_dispatchWindowShown = 15;
        static final int TRANSACTION_executeCommand = 1;
        static final int TRANSACTION_moved = 4;
        static final int TRANSACTION_onAnimationStarted = 13;
        static final int TRANSACTION_onAnimationStopped = 14;
        static final int TRANSACTION_onSurfaceDestroyDeferred = 18;
        static final int TRANSACTION_resized = 2;
        static final int TRANSACTION_windowFocusChanged = 7;

        private static class Proxy implements IWindow {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void executeCommand(String command, String parameters, ParcelFileDescriptor descriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    _data.writeString(parameters);
                    if (descriptor != null) {
                        _data.writeInt(1);
                        descriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, Configuration newConfig, Rect cocktailBarFrame) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (frame != null) {
                        _data.writeInt(1);
                        frame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (overscanInsets != null) {
                        _data.writeInt(1);
                        overscanInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (contentInsets != null) {
                        _data.writeInt(1);
                        contentInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (visibleInsets != null) {
                        _data.writeInt(1);
                        visibleInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (stableInsets != null) {
                        _data.writeInt(1);
                        stableInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (outsets != null) {
                        _data.writeInt(1);
                        outsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!reportDraw) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (newConfig != null) {
                        _data.writeInt(1);
                        newConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (cocktailBarFrame != null) {
                        _data.writeInt(1);
                        cocktailBarFrame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchAttachedDisplayChanged(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void moved(int newX, int newY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newX);
                    _data.writeInt(newY);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchAppVisibility(boolean visible) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!visible) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchGetNewSurface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void windowFocusChanged(boolean hasFocus, boolean inTouchMode, boolean focusedAppChanged) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    int i2;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasFocus ? 1 : 0);
                    if (inTouchMode) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    _data.writeInt(i2);
                    if (!focusedAppChanged) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    _data.writeFloat(xStep);
                    _data.writeFloat(yStep);
                    if (!sync) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    _data.writeInt(z);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!sync) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchDragEvent(DragEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    _data.writeInt(globalVisibility);
                    _data.writeInt(localValue);
                    _data.writeInt(localChanges);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onAnimationStarted(int remainingFrameCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(remainingFrameCount);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onAnimationStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWindowShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchSmartClipRemoteRequest(SmartClipRemoteRequestInfo request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchCoverStateChanged(boolean isOpen) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!isOpen) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onSurfaceDestroyDeferred() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchMultiWindowStateChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindow asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWindow)) {
                return new Proxy(obj);
            }
            return (IWindow) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg6 = false;
            String _arg0;
            boolean _arg02;
            switch (code) {
                case 1:
                    ParcelFileDescriptor _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    String _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    executeCommand(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    Rect _arg03;
                    Rect _arg12;
                    Rect _arg22;
                    Rect _arg3;
                    Rect _arg4;
                    Rect _arg5;
                    Configuration _arg7;
                    Rect _arg8;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg4 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg5 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = true;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = (Configuration) Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg8 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    resized(_arg03, _arg12, _arg22, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchAttachedDisplayChanged(data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    moved(data.readInt(), data.readInt());
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    dispatchAppVisibility(_arg02);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchGetNewSurface();
                    return true;
                case 7:
                    boolean _arg13;
                    boolean _arg23;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = true;
                    } else {
                        _arg13 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = true;
                    } else {
                        _arg23 = false;
                    }
                    windowFocusChanged(_arg02, _arg13, _arg23);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    closeSystemDialogs(data.readString());
                    return true;
                case 9:
                    boolean _arg42;
                    data.enforceInterface(DESCRIPTOR);
                    float _arg04 = data.readFloat();
                    float _arg14 = data.readFloat();
                    float _arg24 = data.readFloat();
                    float _arg32 = data.readFloat();
                    if (data.readInt() != 0) {
                        _arg42 = true;
                    } else {
                        _arg42 = false;
                    }
                    dispatchWallpaperOffsets(_arg04, _arg14, _arg24, _arg32, _arg42);
                    return true;
                case 10:
                    Bundle _arg43;
                    boolean _arg52;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg33 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg43 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg52 = true;
                    } else {
                        _arg52 = false;
                    }
                    dispatchWallpaperCommand(_arg0, _arg15, _arg25, _arg33, _arg43, _arg52);
                    return true;
                case 11:
                    DragEvent _arg05;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (DragEvent) DragEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    dispatchDragEvent(_arg05);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchSystemUiVisibilityChanged(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onAnimationStarted(data.readInt());
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onAnimationStopped();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchWindowShown();
                    return true;
                case 16:
                    SmartClipRemoteRequestInfo _arg06;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (SmartClipRemoteRequestInfo) SmartClipRemoteRequestInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    dispatchSmartClipRemoteRequest(_arg06);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    dispatchCoverStateChanged(_arg02);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onSurfaceDestroyDeferred();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchMultiWindowStateChanged(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void closeSystemDialogs(String str) throws RemoteException;

    void dispatchAppVisibility(boolean z) throws RemoteException;

    void dispatchAttachedDisplayChanged(int i) throws RemoteException;

    void dispatchCoverStateChanged(boolean z) throws RemoteException;

    void dispatchDragEvent(DragEvent dragEvent) throws RemoteException;

    void dispatchGetNewSurface() throws RemoteException;

    void dispatchMultiWindowStateChanged(int i) throws RemoteException;

    void dispatchSmartClipRemoteRequest(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) throws RemoteException;

    void dispatchSystemUiVisibilityChanged(int i, int i2, int i3, int i4) throws RemoteException;

    void dispatchWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) throws RemoteException;

    void dispatchWallpaperOffsets(float f, float f2, float f3, float f4, boolean z) throws RemoteException;

    void dispatchWindowShown() throws RemoteException;

    void executeCommand(String str, String str2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void moved(int i, int i2) throws RemoteException;

    void onAnimationStarted(int i) throws RemoteException;

    void onAnimationStopped() throws RemoteException;

    void onSurfaceDestroyDeferred() throws RemoteException;

    void resized(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, boolean z, Configuration configuration, Rect rect7) throws RemoteException;

    void windowFocusChanged(boolean z, boolean z2, boolean z3) throws RemoteException;
}
