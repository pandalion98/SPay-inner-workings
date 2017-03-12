package android.mtp;

public class MtpServer implements Runnable {
    private long mNativeContext;

    private final native void native_add_storage(MtpStorage mtpStorage);

    private final native void native_cleanup();

    private final native void native_remove_storage(int i);

    private final native void native_run();

    private final native void native_send_device_property_changed(int i);

    private final native void native_send_object_added(int i);

    private final native void native_send_object_removed(int i);

    private final native void native_setup(MtpDatabase mtpDatabase, boolean z);

    static {
        System.loadLibrary("media_jni");
    }

    public MtpServer(MtpDatabase database, boolean usePtp) {
        native_setup(database, usePtp);
        database.setServer(this);
    }

    public void start() {
        new Thread(this, "MtpServer").start();
    }

    public void run() {
        native_run();
        native_cleanup();
    }

    public void sendObjectAdded(int handle) {
        native_send_object_added(handle);
    }

    public void sendObjectRemoved(int handle) {
        native_send_object_removed(handle);
    }

    public void sendDevicePropertyChanged(int property) {
        native_send_device_property_changed(property);
    }

    public void addStorage(MtpStorage storage) {
        native_add_storage(storage);
    }

    public void removeStorage(MtpStorage storage) {
        native_remove_storage(storage.getStorageId());
    }
}
