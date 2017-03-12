package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DragEvent;
import android.widget.RemoteViews;
import java.util.List;

public interface ICocktailBarService extends IInterface {

    public static abstract class Stub extends Binder implements ICocktailBarService {
        private static final String DESCRIPTOR = "com.samsung.android.cocktailbar.ICocktailBarService";
        static final int TRANSACTION_activateCocktailBar = 43;
        static final int TRANSACTION_bindRemoteViewsService = 20;
        static final int TRANSACTION_closeCocktail = 12;
        static final int TRANSACTION_cocktailBarreboot = 62;
        static final int TRANSACTION_cocktailBarshutdown = 61;
        static final int TRANSACTION_deactivateCocktailBar = 44;
        static final int TRANSACTION_disableCocktail = 14;
        static final int TRANSACTION_getAllCocktailIds = 6;
        static final int TRANSACTION_getCocktaiBarWakeUpState = 56;
        static final int TRANSACTION_getCocktail = 7;
        static final int TRANSACTION_getCocktailBarStateInfo = 40;
        static final int TRANSACTION_getCocktailBarVisibility = 39;
        static final int TRANSACTION_getCocktailId = 13;
        static final int TRANSACTION_getCocktailIds = 15;
        static final int TRANSACTION_getEnabledCocktailIds = 5;
        static final int TRANSACTION_getWindowType = 42;
        static final int TRANSACTION_isAllowTransientBarCocktailBar = 58;
        static final int TRANSACTION_isBoundCocktailPackage = 16;
        static final int TRANSACTION_isEnabledCocktail = 17;
        static final int TRANSACTION_notifyCocktailViewDataChanged = 18;
        static final int TRANSACTION_notifyCocktailVisibiltyChanged = 27;
        static final int TRANSACTION_notifyKeyguardState = 26;
        static final int TRANSACTION_partiallyUpdateCocktail = 9;
        static final int TRANSACTION_partiallyUpdateHelpView = 10;
        static final int TRANSACTION_registerClient = 45;
        static final int TRANSACTION_registerCocktailBarFeedsListenerCallback = 37;
        static final int TRANSACTION_registerCocktailBarStateListenerCallback = 35;
        static final int TRANSACTION_removeCocktailUIService = 60;
        static final int TRANSACTION_requestToDisableCocktail = 23;
        static final int TRANSACTION_requestToDisableCocktailByCategory = 25;
        static final int TRANSACTION_requestToUpdateCocktail = 22;
        static final int TRANSACTION_requestToUpdateCocktailByCategory = 24;
        static final int TRANSACTION_sendDragEvent = 28;
        static final int TRANSACTION_sendExtraDataToCocktailBar = 59;
        static final int TRANSACTION_setCocktailBarStatus = 34;
        static final int TRANSACTION_setCocktailBarWakeUpState = 55;
        static final int TRANSACTION_setCocktailHostCallbacks = 1;
        static final int TRANSACTION_setDisableTickerView = 47;
        static final int TRANSACTION_setEnabledCocktailIds = 4;
        static final int TRANSACTION_showAndLockCocktailBar = 29;
        static final int TRANSACTION_showCocktail = 11;
        static final int TRANSACTION_startListening = 2;
        static final int TRANSACTION_stopListening = 3;
        static final int TRANSACTION_switchDefaultCocktail = 57;
        static final int TRANSACTION_unbindRemoteViewsService = 21;
        static final int TRANSACTION_unlockCocktailBar = 30;
        static final int TRANSACTION_unregisterClient = 46;
        static final int TRANSACTION_unregisterCocktailBarFeedsListenerCallback = 38;
        static final int TRANSACTION_unregisterCocktailBarStateListenerCallback = 36;
        static final int TRANSACTION_updateCocktail = 8;
        static final int TRANSACTION_updateCocktailBarPosition = 33;
        static final int TRANSACTION_updateCocktailBarStateFromSystem = 32;
        static final int TRANSACTION_updateCocktailBarVisibility = 31;
        static final int TRANSACTION_updateCocktailBarWindowType = 41;
        static final int TRANSACTION_updateFeeds = 19;
        static final int TRANSACTION_updateLongpressGesture = 50;
        static final int TRANSACTION_updateSysfsBarLength = 52;
        static final int TRANSACTION_updateSysfsDeadZone = 51;
        static final int TRANSACTION_updateSysfsGripDisable = 53;
        static final int TRANSACTION_updateWakeupArea = 49;
        static final int TRANSACTION_updateWakeupGesture = 48;
        static final int TRANSACTION_wakeupCocktailBar = 54;

        private static class Proxy implements ICocktailBarService {
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

            public void setCocktailHostCallbacks(ICocktailHost host, String packageName, int category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(host != null ? host.asBinder() : null);
                    _data.writeString(packageName);
                    _data.writeInt(category);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startListening(ICocktailHost host, String packageName, int category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(host != null ? host.asBinder() : null);
                    _data.writeString(packageName);
                    _data.writeInt(category);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopListening(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEnabledCocktailIds(int[] cocktailIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(cocktailIds);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getEnabledCocktailIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getAllCocktailIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Cocktail getCocktail(int cocktailId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Cocktail _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Cocktail) Cocktail.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCocktail(String callingPackage, CocktailInfo cocktailInfo, int cocktailId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (cocktailInfo != null) {
                        _data.writeInt(1);
                        cocktailInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partiallyUpdateCocktail(String callingPackage, RemoteViews contentView, int cocktailId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (contentView != null) {
                        _data.writeInt(1);
                        contentView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partiallyUpdateHelpView(String callingPackage, RemoteViews helpView, int cocktailId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (helpView != null) {
                        _data.writeInt(1);
                        helpView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showCocktail(String callingPackage, int cocktailId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeCocktail(String callingPackage, int cocktailId, int category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(cocktailId);
                    _data.writeInt(category);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCocktailId(String callingPackage, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableCocktail(String callingPackage, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getCocktailIds(String callingPackage, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBoundCocktailPackage(String packageName, int userId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnabledCocktail(String callingPackage, ComponentName provider) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCocktailViewDataChanged(String callingPackage, int cocktailId, int viewId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(cocktailId);
                    _data.writeInt(viewId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateFeeds(String callingPackage, int cocktailId, List<FeedsInfo> feedsInfoList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(cocktailId);
                    _data.writeTypedList(feedsInfoList);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void bindRemoteViewsService(String callingPakcage, int cocktailId, Intent intent, IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPakcage);
                    _data.writeInt(cocktailId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(connection);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unbindRemoteViewsService(String callingPackage, int cocktailId, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(cocktailId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestToUpdateCocktail(int cocktailId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestToDisableCocktail(int cocktailId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestToUpdateCocktailByCategory(int category) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(category);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestToDisableCocktailByCategory(int category) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(category);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyKeyguardState(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCocktailVisibiltyChanged(int cocktailId, int visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeInt(visibility);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDragEvent(int cocktailId, DragEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showAndLockCocktailBar() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unlockCocktailBar(int visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visibility);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCocktailBarVisibility(int visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visibility);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCocktailBarStateFromSystem(int windowType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowType);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCocktailBarPosition(int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCocktailBarStatus(boolean shift, boolean transparent) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(shift ? 1 : 0);
                    if (!transparent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCocktailBarStateListenerCallback(IBinder callback, ComponentName cm) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    if (cm != null) {
                        _data.writeInt(1);
                        cm.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCocktailBarStateListenerCallback(IBinder callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCocktailBarFeedsListenerCallback(IBinder callback, ComponentName cm) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    if (cm != null) {
                        _data.writeInt(1);
                        cm.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCocktailBarFeedsListenerCallback(IBinder callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCocktailBarVisibility() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CocktailBarStateInfo getCocktailBarStateInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CocktailBarStateInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CocktailBarStateInfo) CocktailBarStateInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCocktailBarWindowType(String callingPackage, int windowType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(windowType);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getWindowType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activateCocktailBar() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deactivateCocktailBar() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerClient(IBinder client, ComponentName cm) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    if (cm != null) {
                        _data.writeInt(1);
                        cm.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterClient(IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDisableTickerView(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWakeupGesture(int gestureType, boolean bEnable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gestureType);
                    if (bEnable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWakeupArea(int area) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(area);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateLongpressGesture(boolean bEnable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bEnable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSysfsDeadZone(int deadzone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deadzone);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSysfsBarLength(int barLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(barLength);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSysfsGripDisable(boolean bDisable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bDisable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wakeupCocktailBar(boolean bShowMainScreen, int keyCode, int reason) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bShowMainScreen) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(keyCode);
                    _data.writeInt(reason);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCocktailBarWakeUpState(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getCocktaiBarWakeUpState() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchDefaultCocktail() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAllowTransientBarCocktailBar() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendExtraDataToCocktailBar(Bundle extraData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extraData != null) {
                        _data.writeInt(1);
                        extraData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeCocktailUIService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cocktailBarshutdown(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cocktailBarreboot(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICocktailBarService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICocktailBarService)) {
                return new Proxy(obj);
            }
            return (ICocktailBarService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int[] _result;
            String _arg0;
            RemoteViews _arg1;
            ComponentName _arg12;
            int _result2;
            boolean _result3;
            int _arg13;
            Intent _arg2;
            boolean _arg02;
            int _arg03;
            boolean _arg14;
            IBinder _arg04;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setCocktailHostCallbacks(com.samsung.android.cocktailbar.ICocktailHost.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    startListening(com.samsung.android.cocktailbar.ICocktailHost.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    stopListening(data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setEnabledCocktailIds(data.createIntArray());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getEnabledCocktailIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getAllCocktailIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    Cocktail _result4 = getCocktail(data.readInt());
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 8:
                    CocktailInfo _arg15;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg15 = (CocktailInfo) CocktailInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    updateCocktail(_arg0, _arg15, data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    partiallyUpdateCocktail(_arg0, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    partiallyUpdateHelpView(_arg0, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    showCocktail(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    closeCocktail(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    _result2 = getCocktailId(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    disableCocktail(_arg0, _arg12);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    _result = getCocktailIds(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isBoundCocktailPackage(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    _result3 = isEnabledCocktail(_arg0, _arg12);
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    notifyCocktailViewDataChanged(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    updateFeeds(data.readString(), data.readInt(), data.createTypedArrayList(FeedsInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg13 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    bindRemoteViewsService(_arg0, _arg13, _arg2, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    _arg13 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    unbindRemoteViewsService(_arg0, _arg13, _arg2);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = requestToUpdateCocktail(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = requestToDisableCocktail(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = requestToUpdateCocktailByCategory(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = requestToDisableCocktailByCategory(data.readInt());
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    notifyKeyguardState(_arg02);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    notifyCocktailVisibiltyChanged(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 28:
                    DragEvent _arg16;
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg16 = (DragEvent) DragEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    sendDragEvent(_arg03, _arg16);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    showAndLockCocktailBar();
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    unlockCocktailBar(data.readInt());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    updateCocktailBarVisibility(data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    updateCocktailBarStateFromSystem(data.readInt());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    updateCocktailBarPosition(data.readInt());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg14 = true;
                    } else {
                        _arg14 = false;
                    }
                    setCocktailBarStatus(_arg02, _arg14);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _arg04 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    registerCocktailBarStateListenerCallback(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCocktailBarStateListenerCallback(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _arg04 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    registerCocktailBarFeedsListenerCallback(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCocktailBarFeedsListenerCallback(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCocktailBarVisibility();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    CocktailBarStateInfo _result5 = getCocktailBarStateInfo();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    updateCocktailBarWindowType(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getWindowType();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    activateCocktailBar();
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    deactivateCocktailBar();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _arg04 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    registerClient(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterClient(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    setDisableTickerView(data.readInt());
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = true;
                    } else {
                        _arg14 = false;
                    }
                    updateWakeupGesture(_arg03, _arg14);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    updateWakeupArea(data.readInt());
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    updateLongpressGesture(_arg02);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    updateSysfsDeadZone(data.readInt());
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    updateSysfsBarLength(data.readInt());
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    updateSysfsGripDisable(_arg02);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    wakeupCocktailBar(_arg02, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = true;
                    } else {
                        _arg02 = false;
                    }
                    setCocktailBarWakeUpState(_arg02);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCocktaiBarWakeUpState();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    switchDefaultCocktail();
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = isAllowTransientBarCocktailBar();
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 59:
                    Bundle _arg05;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    sendExtraDataToCocktailBar(_arg05);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    removeCocktailUIService();
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    cocktailBarshutdown(data.readString());
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    cocktailBarreboot(data.readString());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void activateCocktailBar() throws RemoteException;

    void bindRemoteViewsService(String str, int i, Intent intent, IBinder iBinder) throws RemoteException;

    void closeCocktail(String str, int i, int i2) throws RemoteException;

    void cocktailBarreboot(String str) throws RemoteException;

    void cocktailBarshutdown(String str) throws RemoteException;

    void deactivateCocktailBar() throws RemoteException;

    void disableCocktail(String str, ComponentName componentName) throws RemoteException;

    int[] getAllCocktailIds() throws RemoteException;

    boolean getCocktaiBarWakeUpState() throws RemoteException;

    Cocktail getCocktail(int i) throws RemoteException;

    CocktailBarStateInfo getCocktailBarStateInfo() throws RemoteException;

    int getCocktailBarVisibility() throws RemoteException;

    int getCocktailId(String str, ComponentName componentName) throws RemoteException;

    int[] getCocktailIds(String str, ComponentName componentName) throws RemoteException;

    int[] getEnabledCocktailIds() throws RemoteException;

    int getWindowType() throws RemoteException;

    boolean isAllowTransientBarCocktailBar() throws RemoteException;

    boolean isBoundCocktailPackage(String str, int i) throws RemoteException;

    boolean isEnabledCocktail(String str, ComponentName componentName) throws RemoteException;

    void notifyCocktailViewDataChanged(String str, int i, int i2) throws RemoteException;

    void notifyCocktailVisibiltyChanged(int i, int i2) throws RemoteException;

    void notifyKeyguardState(boolean z) throws RemoteException;

    void partiallyUpdateCocktail(String str, RemoteViews remoteViews, int i) throws RemoteException;

    void partiallyUpdateHelpView(String str, RemoteViews remoteViews, int i) throws RemoteException;

    void registerClient(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void registerCocktailBarFeedsListenerCallback(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void registerCocktailBarStateListenerCallback(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void removeCocktailUIService() throws RemoteException;

    boolean requestToDisableCocktail(int i) throws RemoteException;

    boolean requestToDisableCocktailByCategory(int i) throws RemoteException;

    boolean requestToUpdateCocktail(int i) throws RemoteException;

    boolean requestToUpdateCocktailByCategory(int i) throws RemoteException;

    void sendDragEvent(int i, DragEvent dragEvent) throws RemoteException;

    void sendExtraDataToCocktailBar(Bundle bundle) throws RemoteException;

    void setCocktailBarStatus(boolean z, boolean z2) throws RemoteException;

    void setCocktailBarWakeUpState(boolean z) throws RemoteException;

    void setCocktailHostCallbacks(ICocktailHost iCocktailHost, String str, int i) throws RemoteException;

    void setDisableTickerView(int i) throws RemoteException;

    void setEnabledCocktailIds(int[] iArr) throws RemoteException;

    void showAndLockCocktailBar() throws RemoteException;

    void showCocktail(String str, int i) throws RemoteException;

    void startListening(ICocktailHost iCocktailHost, String str, int i) throws RemoteException;

    void stopListening(String str) throws RemoteException;

    void switchDefaultCocktail() throws RemoteException;

    void unbindRemoteViewsService(String str, int i, Intent intent) throws RemoteException;

    void unlockCocktailBar(int i) throws RemoteException;

    void unregisterClient(IBinder iBinder) throws RemoteException;

    void unregisterCocktailBarFeedsListenerCallback(IBinder iBinder) throws RemoteException;

    void unregisterCocktailBarStateListenerCallback(IBinder iBinder) throws RemoteException;

    void updateCocktail(String str, CocktailInfo cocktailInfo, int i) throws RemoteException;

    void updateCocktailBarPosition(int i) throws RemoteException;

    void updateCocktailBarStateFromSystem(int i) throws RemoteException;

    void updateCocktailBarVisibility(int i) throws RemoteException;

    void updateCocktailBarWindowType(String str, int i) throws RemoteException;

    void updateFeeds(String str, int i, List<FeedsInfo> list) throws RemoteException;

    void updateLongpressGesture(boolean z) throws RemoteException;

    void updateSysfsBarLength(int i) throws RemoteException;

    void updateSysfsDeadZone(int i) throws RemoteException;

    void updateSysfsGripDisable(boolean z) throws RemoteException;

    void updateWakeupArea(int i) throws RemoteException;

    void updateWakeupGesture(int i, boolean z) throws RemoteException;

    void wakeupCocktailBar(boolean z, int i, int i2) throws RemoteException;
}
