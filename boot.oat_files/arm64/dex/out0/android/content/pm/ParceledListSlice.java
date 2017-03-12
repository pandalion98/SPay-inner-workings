package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class ParceledListSlice<T extends Parcelable> implements Parcelable {
    public static final ClassLoaderCreator<ParceledListSlice> CREATOR = new ClassLoaderCreator<ParceledListSlice>() {
        public ParceledListSlice createFromParcel(Parcel in) {
            return new ParceledListSlice(in, null);
        }

        public ParceledListSlice createFromParcel(Parcel in, ClassLoader loader) {
            return new ParceledListSlice(in, loader);
        }

        public ParceledListSlice[] newArray(int size) {
            return new ParceledListSlice[size];
        }
    };
    private static boolean DEBUG = false;
    private static final int MAX_IPC_SIZE = 65536;
    private static String TAG = "ParceledListSlice";
    private final List<T> mList;

    public ParceledListSlice(List<T> list) {
        this.mList = list;
    }

    private ParceledListSlice(Parcel p, ClassLoader loader) {
        int N = p.readInt();
        this.mList = new ArrayList(N);
        if (DEBUG) {
            Log.d(TAG, "Retrieving " + N + " items");
        }
        if (N > 0) {
            T parcelable;
            Creator<?> creator = p.readParcelableCreator(loader);
            Class<?> listElementClass = null;
            int i = 0;
            while (i < N && p.readInt() != 0) {
                parcelable = p.readCreator(creator, loader);
                if (listElementClass == null) {
                    listElementClass = parcelable.getClass();
                } else {
                    verifySameType(listElementClass, parcelable.getClass());
                }
                this.mList.add(parcelable);
                if (DEBUG) {
                    Log.d(TAG, "Read inline #" + i + ": " + this.mList.get(this.mList.size() - 1));
                }
                i++;
            }
            if (i < N) {
                IBinder retriever = p.readStrongBinder();
                while (i < N) {
                    if (DEBUG) {
                        Log.d(TAG, "Reading more @" + i + " of " + N + ": retriever=" + retriever);
                    }
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeInt(i);
                    try {
                        retriever.transact(1, data, reply, 0);
                        while (i < N && reply.readInt() != 0) {
                            parcelable = reply.readCreator(creator, loader);
                            verifySameType(listElementClass, parcelable.getClass());
                            this.mList.add(parcelable);
                            if (DEBUG) {
                                Log.d(TAG, "Read extra #" + i + ": " + this.mList.get(this.mList.size() - 1));
                            }
                            i++;
                        }
                        reply.recycle();
                        data.recycle();
                    } catch (RemoteException e) {
                        Log.w(TAG, "Failure retrieving array; only received " + i + " of " + N, e);
                        return;
                    }
                }
            }
        }
    }

    private static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            throw new IllegalArgumentException("Can't unparcel type " + actual.getName() + " in list of type " + expected.getName());
        }
    }

    public List<T> getList() {
        return this.mList;
    }

    public int describeContents() {
        int contents = 0;
        for (int i = 0; i < this.mList.size(); i++) {
            contents |= ((Parcelable) this.mList.get(i)).describeContents();
        }
        return contents;
    }

    public void writeToParcel(Parcel dest, int flags) {
        final int N = this.mList.size();
        final int callFlags = flags;
        dest.writeInt(N);
        if (DEBUG) {
            Log.d(TAG, "Writing " + N + " items");
        }
        if (N > 0) {
            final Class<?> listElementClass = ((Parcelable) this.mList.get(0)).getClass();
            dest.writeParcelableCreator((Parcelable) this.mList.get(0));
            int i = 0;
            while (i < N && dest.dataSize() < 65536) {
                dest.writeInt(1);
                Parcelable parcelable = (Parcelable) this.mList.get(i);
                verifySameType(listElementClass, parcelable.getClass());
                parcelable.writeToParcel(dest, callFlags);
                if (DEBUG) {
                    Log.d(TAG, "Wrote inline #" + i + ": " + this.mList.get(i));
                }
                i++;
            }
            if (i < N) {
                dest.writeInt(0);
                Binder retriever = new Binder() {
                    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                        if (code != 1) {
                            return super.onTransact(code, data, reply, flags);
                        }
                        int i = data.readInt();
                        if (ParceledListSlice.DEBUG) {
                            Log.d(ParceledListSlice.TAG, "Writing more @" + i + " of " + N);
                        }
                        while (i < N && reply.dataSize() < 65536) {
                            reply.writeInt(1);
                            Parcelable parcelable = (Parcelable) ParceledListSlice.this.mList.get(i);
                            ParceledListSlice.verifySameType(listElementClass, parcelable.getClass());
                            parcelable.writeToParcel(reply, callFlags);
                            if (ParceledListSlice.DEBUG) {
                                Log.d(ParceledListSlice.TAG, "Wrote extra #" + i + ": " + ParceledListSlice.this.mList.get(i));
                            }
                            i++;
                        }
                        if (i >= N) {
                            return true;
                        }
                        if (ParceledListSlice.DEBUG) {
                            Log.d(ParceledListSlice.TAG, "Breaking @" + i + " of " + N);
                        }
                        reply.writeInt(0);
                        return true;
                    }
                };
                if (DEBUG) {
                    Log.d(TAG, "Breaking @" + i + " of " + N + ": retriever=" + retriever);
                }
                dest.writeStrongBinder(retriever);
            }
        }
    }
}
