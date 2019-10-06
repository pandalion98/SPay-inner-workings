package android.hardware.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRecognitionStatusCallback extends IInterface {

    public static abstract class Stub extends Binder implements IRecognitionStatusCallback {
        private static final String DESCRIPTOR = "android.hardware.soundtrigger.IRecognitionStatusCallback";
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onGenericSoundTriggerDetected = 2;
        static final int TRANSACTION_onKeyphraseDetected = 1;
        static final int TRANSACTION_onRecognitionPaused = 4;
        static final int TRANSACTION_onRecognitionResumed = 5;

        private static class Proxy implements IRecognitionStatusCallback {
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

            public void onKeyphraseDetected(KeyphraseRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGenericSoundTriggerDetected(GenericRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRecognitionPaused() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onRecognitionResumed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecognitionStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRecognitionStatusCallback)) {
                return new Proxy(obj);
            }
            return (IRecognitionStatusCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r0v2 */
        /* JADX WARNING: type inference failed for: r0v3, types: [android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent] */
        /* JADX WARNING: type inference failed for: r0v6, types: [android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent] */
        /* JADX WARNING: type inference failed for: r0v7, types: [android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent] */
        /* JADX WARNING: type inference failed for: r0v10, types: [android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent] */
        /* JADX WARNING: type inference failed for: r0v16 */
        /* JADX WARNING: type inference failed for: r0v17 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v2
          assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent, android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent]
          uses: [android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent, android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent]
          mth insns count: 41
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 3 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r4, android.os.Parcel r5, android.os.Parcel r6, int r7) throws android.os.RemoteException {
            /*
                r3 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                if (r4 == r0) goto L_0x0060
                r0 = 0
                switch(r4) {
                    case 1: goto L_0x0047;
                    case 2: goto L_0x002e;
                    case 3: goto L_0x0021;
                    case 4: goto L_0x0018;
                    case 5: goto L_0x000f;
                    default: goto L_0x000a;
                }
            L_0x000a:
                boolean r0 = super.onTransact(r4, r5, r6, r7)
                return r0
            L_0x000f:
                java.lang.String r0 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r5.enforceInterface(r0)
                r3.onRecognitionResumed()
                return r1
            L_0x0018:
                java.lang.String r0 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r5.enforceInterface(r0)
                r3.onRecognitionPaused()
                return r1
            L_0x0021:
                java.lang.String r0 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r5.enforceInterface(r0)
                int r0 = r5.readInt()
                r3.onError(r0)
                return r1
            L_0x002e:
                java.lang.String r2 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r5.enforceInterface(r2)
                int r2 = r5.readInt()
                if (r2 == 0) goto L_0x0042
                android.os.Parcelable$Creator<android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent> r0 = android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r5)
                android.hardware.soundtrigger.SoundTrigger$GenericRecognitionEvent r0 = (android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent) r0
                goto L_0x0043
            L_0x0042:
            L_0x0043:
                r3.onGenericSoundTriggerDetected(r0)
                return r1
            L_0x0047:
                java.lang.String r2 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r5.enforceInterface(r2)
                int r2 = r5.readInt()
                if (r2 == 0) goto L_0x005b
                android.os.Parcelable$Creator<android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent> r0 = android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r5)
                android.hardware.soundtrigger.SoundTrigger$KeyphraseRecognitionEvent r0 = (android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent) r0
                goto L_0x005c
            L_0x005b:
            L_0x005c:
                r3.onKeyphraseDetected(r0)
                return r1
            L_0x0060:
                java.lang.String r0 = "android.hardware.soundtrigger.IRecognitionStatusCallback"
                r6.writeString(r0)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.soundtrigger.IRecognitionStatusCallback.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    void onError(int i) throws RemoteException;

    void onGenericSoundTriggerDetected(GenericRecognitionEvent genericRecognitionEvent) throws RemoteException;

    void onKeyphraseDetected(KeyphraseRecognitionEvent keyphraseRecognitionEvent) throws RemoteException;

    void onRecognitionPaused() throws RemoteException;

    void onRecognitionResumed() throws RemoteException;
}
