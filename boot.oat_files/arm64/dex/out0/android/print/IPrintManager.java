package android.print;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.printservice.PrintServiceInfo;
import java.util.List;

public interface IPrintManager extends IInterface {

    public static abstract class Stub extends Binder implements IPrintManager {
        private static final String DESCRIPTOR = "android.print.IPrintManager";
        static final int TRANSACTION_addPrintJobStateChangeListener = 6;
        static final int TRANSACTION_cancelPrintJob = 4;
        static final int TRANSACTION_createPrinterDiscoverySession = 10;
        static final int TRANSACTION_destroyPrinterDiscoverySession = 16;
        static final int TRANSACTION_getEnabledPrintServices = 9;
        static final int TRANSACTION_getInstalledPrintServices = 8;
        static final int TRANSACTION_getPrintJobInfo = 2;
        static final int TRANSACTION_getPrintJobInfos = 1;
        static final int TRANSACTION_print = 3;
        static final int TRANSACTION_removePrintJobStateChangeListener = 7;
        static final int TRANSACTION_restartPrintJob = 5;
        static final int TRANSACTION_startPrinterDiscovery = 11;
        static final int TRANSACTION_startPrinterStateTracking = 14;
        static final int TRANSACTION_stopPrinterDiscovery = 12;
        static final int TRANSACTION_stopPrinterStateTracking = 15;
        static final int TRANSACTION_validatePrinters = 13;

        private static class Proxy implements IPrintManager {
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

            public List<PrintJobInfo> getPrintJobInfos(int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<PrintJobInfo> _result = _reply.createTypedArrayList(PrintJobInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    PrintJobInfo _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (PrintJobInfo) PrintJobInfo.CREATOR.createFromParcel(_reply);
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

            public Bundle print(String printJobName, IPrintDocumentAdapter printAdapter, PrintAttributes attributes, String packageName, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(printJobName);
                    _data.writeStrongBinder(printAdapter != null ? printAdapter.asBinder() : null);
                    if (attributes != null) {
                        _data.writeInt(1);
                        attributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
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

            public void cancelPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restartPrintJob(PrintJobId printJobId, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPrintJobStateChangeListener(IPrintJobStateChangeListener listener, int appId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePrintJobStateChangeListener(IPrintJobStateChangeListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PrintServiceInfo> getInstalledPrintServices(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<PrintServiceInfo> _result = _reply.createTypedArrayList(PrintServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PrintServiceInfo> getEnabledPrintServices(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    List<PrintServiceInfo> _result = _reply.createTypedArrayList(PrintServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startPrinterDiscovery(IPrinterDiscoveryObserver observer, List<PrinterId> priorityList, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeTypedList(priorityList);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopPrinterDiscovery(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void validatePrinters(List<PrinterId> printerIds, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(printerIds);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopPrinterStateTracking(PrinterId printerId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(16, _data, _reply, 0);
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

        public static IPrintManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintManager)) {
                return new Proxy(obj);
            }
            return (IPrintManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PrintJobId _arg0;
            List<PrintServiceInfo> _result;
            PrinterId _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    List<PrintJobInfo> _result2 = getPrintJobInfos(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    PrintJobInfo _result3 = getPrintJobInfo(_arg0, data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    PrintAttributes _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    IPrintDocumentAdapter _arg1 = android.print.IPrintDocumentAdapter.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = (PrintAttributes) PrintAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    Bundle _result4 = print(_arg03, _arg1, _arg2, data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    cancelPrintJob(_arg0, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PrintJobId) PrintJobId.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    restartPrintJob(_arg0, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    addPrintJobStateChangeListener(android.print.IPrintJobStateChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    removePrintJobStateChangeListener(android.print.IPrintJobStateChangeListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getInstalledPrintServices(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getEnabledPrintServices(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    createPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    startPrinterDiscovery(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.createTypedArrayList(PrinterId.CREATOR), data.readInt());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    stopPrinterDiscovery(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    validatePrinters(data.createTypedArrayList(PrinterId.CREATOR), data.readInt());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PrinterId) PrinterId.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    startPrinterStateTracking(_arg02, data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (PrinterId) PrinterId.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    stopPrinterStateTracking(_arg02, data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    destroyPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addPrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i, int i2) throws RemoteException;

    void cancelPrintJob(PrintJobId printJobId, int i, int i2) throws RemoteException;

    void createPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    List<PrintServiceInfo> getEnabledPrintServices(int i) throws RemoteException;

    List<PrintServiceInfo> getInstalledPrintServices(int i) throws RemoteException;

    PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int i, int i2) throws RemoteException;

    List<PrintJobInfo> getPrintJobInfos(int i, int i2) throws RemoteException;

    Bundle print(String str, IPrintDocumentAdapter iPrintDocumentAdapter, PrintAttributes printAttributes, String str2, int i, int i2) throws RemoteException;

    void removePrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i) throws RemoteException;

    void restartPrintJob(PrintJobId printJobId, int i, int i2) throws RemoteException;

    void startPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, List<PrinterId> list, int i) throws RemoteException;

    void startPrinterStateTracking(PrinterId printerId, int i) throws RemoteException;

    void stopPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) throws RemoteException;

    void stopPrinterStateTracking(PrinterId printerId, int i) throws RemoteException;

    void validatePrinters(List<PrinterId> list, int i) throws RemoteException;
}
