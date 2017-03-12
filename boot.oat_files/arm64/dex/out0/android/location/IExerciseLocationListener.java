package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IExerciseLocationListener extends IInterface {

    public static abstract class Stub extends Binder implements IExerciseLocationListener {
        private static final String DESCRIPTOR = "android.location.IExerciseLocationListener";
        static final int TRANSACTION_onLocationChanged = 1;
        static final int TRANSACTION_onStatusChanged = 2;

        private static class Proxy implements IExerciseLocationListener {
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

            public void onLocationChanged(long[] timestamp, double[] latitude, double[] longtitude, float[] altitude, float[] pressure, float[] speed, double[] pedoDistance, double[] pedoSpeed, long[] pedoStepCount, int numOfBatch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLongArray(timestamp);
                    _data.writeDoubleArray(latitude);
                    _data.writeDoubleArray(longtitude);
                    _data.writeFloatArray(altitude);
                    _data.writeFloatArray(pressure);
                    _data.writeFloatArray(speed);
                    _data.writeDoubleArray(pedoDistance);
                    _data.writeDoubleArray(pedoSpeed);
                    _data.writeLongArray(pedoStepCount);
                    _data.writeInt(numOfBatch);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStatusChanged(int exercise_status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(exercise_status);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IExerciseLocationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IExerciseLocationListener)) {
                return new Proxy(obj);
            }
            return (IExerciseLocationListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onLocationChanged(data.createLongArray(), data.createDoubleArray(), data.createDoubleArray(), data.createFloatArray(), data.createFloatArray(), data.createFloatArray(), data.createDoubleArray(), data.createDoubleArray(), data.createLongArray(), data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onStatusChanged(data.readInt());
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onLocationChanged(long[] jArr, double[] dArr, double[] dArr2, float[] fArr, float[] fArr2, float[] fArr3, double[] dArr3, double[] dArr4, long[] jArr2, int i) throws RemoteException;

    void onStatusChanged(int i) throws RemoteException;
}
