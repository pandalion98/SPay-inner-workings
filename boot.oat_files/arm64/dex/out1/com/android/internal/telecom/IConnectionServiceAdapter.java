package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.ConnectionRequest;
import android.telecom.DisconnectCause;
import android.telecom.ParcelableConference;
import android.telecom.ParcelableConnection;
import android.telecom.StatusHints;
import java.util.List;

public interface IConnectionServiceAdapter extends IInterface {

    public static abstract class Stub extends Binder implements IConnectionServiceAdapter {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IConnectionServiceAdapter";
        static final int TRANSACTION_addConferenceCall = 11;
        static final int TRANSACTION_addExistingConnection = 23;
        static final int TRANSACTION_handleCreateConnectionComplete = 1;
        static final int TRANSACTION_onPostDialChar = 14;
        static final int TRANSACTION_onPostDialWait = 13;
        static final int TRANSACTION_queryRemoteConnectionServices = 15;
        static final int TRANSACTION_removeCall = 12;
        static final int TRANSACTION_setActive = 2;
        static final int TRANSACTION_setAddress = 20;
        static final int TRANSACTION_setCallerDisplayName = 21;
        static final int TRANSACTION_setConferenceMergeFailed = 10;
        static final int TRANSACTION_setConferenceableConnections = 22;
        static final int TRANSACTION_setConnectionCapabilities = 8;
        static final int TRANSACTION_setDialing = 4;
        static final int TRANSACTION_setDisconnected = 5;
        static final int TRANSACTION_setExtras = 24;
        static final int TRANSACTION_setIsConferenced = 9;
        static final int TRANSACTION_setIsVoipAudioMode = 18;
        static final int TRANSACTION_setOnHold = 6;
        static final int TRANSACTION_setRingbackRequested = 7;
        static final int TRANSACTION_setRinging = 3;
        static final int TRANSACTION_setStatusHints = 19;
        static final int TRANSACTION_setVideoProvider = 16;
        static final int TRANSACTION_setVideoState = 17;

        private static class Proxy implements IConnectionServiceAdapter {
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

            public void handleCreateConnectionComplete(String callId, ConnectionRequest request, ParcelableConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (connection != null) {
                        _data.writeInt(1);
                        connection.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setActive(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setRinging(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setDialing(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setDisconnected(String callId, DisconnectCause disconnectCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (disconnectCause != null) {
                        _data.writeInt(1);
                        disconnectCause.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setOnHold(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setRingbackRequested(String callId, boolean ringing) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (!ringing) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setConnectionCapabilities(String callId, int connectionCapabilities) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(connectionCapabilities);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setIsConferenced(String callId, String conferenceCallId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(conferenceCallId);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setConferenceMergeFailed(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void addConferenceCall(String callId, ParcelableConference conference) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (conference != null) {
                        _data.writeInt(1);
                        conference.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void removeCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onPostDialWait(String callId, String remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(remaining);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onPostDialChar(String callId, char nextChar) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(nextChar);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void queryRemoteConnectionServices(RemoteServiceCallback callback) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setVideoProvider(String callId, IVideoProvider videoProvider) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (videoProvider != null) {
                        iBinder = videoProvider.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setVideoState(String callId, int videoState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(videoState);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setIsVoipAudioMode(String callId, boolean isVoip) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (!isVoip) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setStatusHints(String callId, StatusHints statusHints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (statusHints != null) {
                        _data.writeInt(1);
                        statusHints.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setAddress(String callId, Uri address, int presentation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(presentation);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setCallerDisplayName(String callId, String callerDisplayName, int presentation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(callerDisplayName);
                    _data.writeInt(presentation);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setConferenceableConnections(String callId, List<String> conferenceableCallIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeStringList(conferenceableCallIds);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void addExistingConnection(String callId, ParcelableConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (connection != null) {
                        _data.writeInt(1);
                        connection.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setExtras(String callId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IConnectionServiceAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IConnectionServiceAdapter)) {
                return new Proxy(obj);
            }
            return (IConnectionServiceAdapter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1 = false;
            String _arg0;
            switch (code) {
                case 1:
                    ConnectionRequest _arg12;
                    ParcelableConnection _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (ConnectionRequest) ConnectionRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (ParcelableConnection) ParcelableConnection.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    handleCreateConnectionComplete(_arg0, _arg12, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    setActive(data.readString());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setRinging(data.readString());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setDialing(data.readString());
                    return true;
                case 5:
                    DisconnectCause _arg13;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = (DisconnectCause) DisconnectCause.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setDisconnected(_arg0, _arg13);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    setOnHold(data.readString());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    }
                    setRingbackRequested(_arg0, _arg1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setConnectionCapabilities(data.readString(), data.readInt());
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setIsConferenced(data.readString(), data.readString());
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    setConferenceMergeFailed(data.readString());
                    return true;
                case 11:
                    ParcelableConference _arg14;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg14 = (ParcelableConference) ParcelableConference.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    addConferenceCall(_arg0, _arg14);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    removeCall(data.readString());
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onPostDialWait(data.readString(), data.readString());
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onPostDialChar(data.readString(), (char) data.readInt());
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    queryRemoteConnectionServices(com.android.internal.telecom.RemoteServiceCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    setVideoProvider(data.readString(), com.android.internal.telecom.IVideoProvider.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    setVideoState(data.readString(), data.readInt());
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    }
                    setIsVoipAudioMode(_arg0, _arg1);
                    return true;
                case 19:
                    StatusHints _arg15;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg15 = (StatusHints) StatusHints.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    setStatusHints(_arg0, _arg15);
                    return true;
                case 20:
                    Uri _arg16;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg16 = (Uri) Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    setAddress(_arg0, _arg16, data.readInt());
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    setCallerDisplayName(data.readString(), data.readString(), data.readInt());
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    setConferenceableConnections(data.readString(), data.createStringArrayList());
                    return true;
                case 23:
                    ParcelableConnection _arg17;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg17 = (ParcelableConnection) ParcelableConnection.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    addExistingConnection(_arg0, _arg17);
                    return true;
                case 24:
                    Bundle _arg18;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg18 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg18 = null;
                    }
                    setExtras(_arg0, _arg18);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addConferenceCall(String str, ParcelableConference parcelableConference) throws RemoteException;

    void addExistingConnection(String str, ParcelableConnection parcelableConnection) throws RemoteException;

    void handleCreateConnectionComplete(String str, ConnectionRequest connectionRequest, ParcelableConnection parcelableConnection) throws RemoteException;

    void onPostDialChar(String str, char c) throws RemoteException;

    void onPostDialWait(String str, String str2) throws RemoteException;

    void queryRemoteConnectionServices(RemoteServiceCallback remoteServiceCallback) throws RemoteException;

    void removeCall(String str) throws RemoteException;

    void setActive(String str) throws RemoteException;

    void setAddress(String str, Uri uri, int i) throws RemoteException;

    void setCallerDisplayName(String str, String str2, int i) throws RemoteException;

    void setConferenceMergeFailed(String str) throws RemoteException;

    void setConferenceableConnections(String str, List<String> list) throws RemoteException;

    void setConnectionCapabilities(String str, int i) throws RemoteException;

    void setDialing(String str) throws RemoteException;

    void setDisconnected(String str, DisconnectCause disconnectCause) throws RemoteException;

    void setExtras(String str, Bundle bundle) throws RemoteException;

    void setIsConferenced(String str, String str2) throws RemoteException;

    void setIsVoipAudioMode(String str, boolean z) throws RemoteException;

    void setOnHold(String str) throws RemoteException;

    void setRingbackRequested(String str, boolean z) throws RemoteException;

    void setRinging(String str) throws RemoteException;

    void setStatusHints(String str, StatusHints statusHints) throws RemoteException;

    void setVideoProvider(String str, IVideoProvider iVideoProvider) throws RemoteException;

    void setVideoState(String str, int i) throws RemoteException;
}
