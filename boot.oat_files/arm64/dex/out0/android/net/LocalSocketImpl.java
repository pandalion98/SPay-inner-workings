package android.net;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class LocalSocketImpl {
    private FileDescriptor fd;
    private SocketInputStream fis;
    private SocketOutputStream fos;
    FileDescriptor[] inboundFileDescriptors;
    private boolean mFdCreatedInternally;
    FileDescriptor[] outboundFileDescriptors;
    private Object readMonitor = new Object();
    private Object writeMonitor = new Object();

    class SocketInputStream extends InputStream {
        SocketInputStream() {
        }

        public int available() throws IOException {
            FileDescriptor myFd = LocalSocketImpl.this.fd;
            if (myFd != null) {
                return LocalSocketImpl.this.available_native(myFd);
            }
            throw new IOException("socket closed");
        }

        public void close() throws IOException {
            LocalSocketImpl.this.close();
        }

        public int read() throws IOException {
            int ret;
            synchronized (LocalSocketImpl.this.readMonitor) {
                FileDescriptor myFd = LocalSocketImpl.this.fd;
                if (myFd == null) {
                    throw new IOException("socket closed");
                }
                ret = LocalSocketImpl.this.read_native(myFd);
            }
            return ret;
        }

        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int ret;
            synchronized (LocalSocketImpl.this.readMonitor) {
                FileDescriptor myFd = LocalSocketImpl.this.fd;
                if (myFd == null) {
                    throw new IOException("socket closed");
                }
                if (off >= 0 && len >= 0) {
                    if (off + len <= b.length) {
                        ret = LocalSocketImpl.this.readba_native(b, off, len, myFd);
                    }
                }
                throw new ArrayIndexOutOfBoundsException();
            }
            return ret;
        }
    }

    class SocketOutputStream extends OutputStream {
        SocketOutputStream() {
        }

        public void close() throws IOException {
            LocalSocketImpl.this.close();
        }

        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            synchronized (LocalSocketImpl.this.writeMonitor) {
                FileDescriptor myFd = LocalSocketImpl.this.fd;
                if (myFd == null) {
                    throw new IOException("socket closed");
                }
                if (off >= 0 && len >= 0) {
                    if (off + len <= b.length) {
                        LocalSocketImpl.this.writeba_native(b, off, len, myFd);
                    }
                }
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public void write(int b) throws IOException {
            synchronized (LocalSocketImpl.this.writeMonitor) {
                FileDescriptor myFd = LocalSocketImpl.this.fd;
                if (myFd == null) {
                    throw new IOException("socket closed");
                }
                LocalSocketImpl.this.write_native(b, myFd);
            }
        }

        public void flush() throws IOException {
            FileDescriptor myFd = LocalSocketImpl.this.fd;
            if (myFd == null) {
                throw new IOException("socket closed");
            }
            while (LocalSocketImpl.this.pending_native(myFd) > 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private native FileDescriptor accept(FileDescriptor fileDescriptor, LocalSocketImpl localSocketImpl) throws IOException;

    private native int available_native(FileDescriptor fileDescriptor) throws IOException;

    private native void bindLocal(FileDescriptor fileDescriptor, String str, int i) throws IOException;

    private native void connectLocal(FileDescriptor fileDescriptor, String str, int i) throws IOException;

    private native int getOption_native(FileDescriptor fileDescriptor, int i) throws IOException;

    private native Credentials getPeerCredentials_native(FileDescriptor fileDescriptor) throws IOException;

    private native void listen_native(FileDescriptor fileDescriptor, int i) throws IOException;

    private native int pending_native(FileDescriptor fileDescriptor) throws IOException;

    private native int read_native(FileDescriptor fileDescriptor) throws IOException;

    private native int readba_native(byte[] bArr, int i, int i2, FileDescriptor fileDescriptor) throws IOException;

    private native void setOption_native(FileDescriptor fileDescriptor, int i, int i2, int i3) throws IOException;

    private native void shutdown(FileDescriptor fileDescriptor, boolean z);

    private native void write_native(int i, FileDescriptor fileDescriptor) throws IOException;

    private native void writeba_native(byte[] bArr, int i, int i2, FileDescriptor fileDescriptor) throws IOException;

    LocalSocketImpl() {
    }

    LocalSocketImpl(FileDescriptor fd) throws IOException {
        this.fd = fd;
    }

    public String toString() {
        return super.toString() + " fd:" + this.fd;
    }

    public void create(int sockType) throws IOException {
        if (this.fd == null) {
            int osType;
            switch (sockType) {
                case 1:
                    osType = OsConstants.SOCK_DGRAM;
                    break;
                case 2:
                    osType = OsConstants.SOCK_STREAM;
                    break;
                case 3:
                    osType = OsConstants.SOCK_SEQPACKET;
                    break;
                default:
                    throw new IllegalStateException("unknown sockType");
            }
            try {
                this.fd = Os.socket(OsConstants.AF_UNIX, osType, 0);
                this.mFdCreatedInternally = true;
            } catch (ErrnoException e) {
                e.rethrowAsIOException();
            }
        }
    }

    public void close() throws IOException {
        synchronized (this) {
            if (this.fd == null || !this.mFdCreatedInternally) {
                this.fd = null;
                return;
            }
            try {
                Os.close(this.fd);
            } catch (ErrnoException e) {
                e.rethrowAsIOException();
            }
            this.fd = null;
        }
    }

    protected void connect(LocalSocketAddress address, int timeout) throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        connectLocal(this.fd, address.getName(), address.getNamespace().getId());
    }

    public void bind(LocalSocketAddress endpoint) throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        bindLocal(this.fd, endpoint.getName(), endpoint.getNamespace().getId());
    }

    protected void listen(int backlog) throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        listen_native(this.fd, backlog);
    }

    protected void accept(LocalSocketImpl s) throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        s.fd = accept(this.fd, s);
        s.mFdCreatedInternally = true;
    }

    protected InputStream getInputStream() throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        InputStream inputStream;
        synchronized (this) {
            if (this.fis == null) {
                this.fis = new SocketInputStream();
            }
            inputStream = this.fis;
        }
        return inputStream;
    }

    protected OutputStream getOutputStream() throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        OutputStream outputStream;
        synchronized (this) {
            if (this.fos == null) {
                this.fos = new SocketOutputStream();
            }
            outputStream = this.fos;
        }
        return outputStream;
    }

    protected int available() throws IOException {
        return getInputStream().available();
    }

    protected void shutdownInput() throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        shutdown(this.fd, true);
    }

    protected void shutdownOutput() throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        shutdown(this.fd, false);
    }

    protected FileDescriptor getFileDescriptor() {
        return this.fd;
    }

    protected boolean supportsUrgentData() {
        return false;
    }

    protected void sendUrgentData(int data) throws IOException {
        throw new RuntimeException("not impled");
    }

    public Object getOption(int optID) throws IOException {
        if (this.fd == null) {
            throw new IOException("socket not created");
        } else if (optID == 4102) {
            return Integer.valueOf(0);
        } else {
            int value = getOption_native(this.fd, optID);
            switch (optID) {
                case 4097:
                case NetworkPolicyManager.POLICY_ALLOW_WHITELIST_IN_ROAMING /*4098*/:
                    return Integer.valueOf(value);
                default:
                    return Integer.valueOf(value);
            }
        }
    }

    public void setOption(int optID, Object value) throws IOException {
        int boolValue = -1;
        int intValue = 0;
        if (this.fd == null) {
            throw new IOException("socket not created");
        }
        if (value instanceof Integer) {
            intValue = ((Integer) value).intValue();
        } else if (value instanceof Boolean) {
            boolValue = ((Boolean) value).booleanValue() ? 1 : 0;
        } else {
            throw new IOException("bad value: " + value);
        }
        setOption_native(this.fd, optID, boolValue, intValue);
    }

    public void setFileDescriptorsForSend(FileDescriptor[] fds) {
        synchronized (this.writeMonitor) {
            this.outboundFileDescriptors = fds;
        }
    }

    public FileDescriptor[] getAncillaryFileDescriptors() throws IOException {
        FileDescriptor[] result;
        synchronized (this.readMonitor) {
            result = this.inboundFileDescriptors;
            this.inboundFileDescriptors = null;
        }
        return result;
    }

    public Credentials getPeerCredentials() throws IOException {
        return getPeerCredentials_native(this.fd);
    }

    public LocalSocketAddress getSockAddress() throws IOException {
        return null;
    }

    protected void finalize() throws IOException {
        close();
    }
}
