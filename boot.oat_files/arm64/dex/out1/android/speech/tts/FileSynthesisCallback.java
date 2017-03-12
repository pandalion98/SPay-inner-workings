package android.speech.tts;

import android.media.AudioFormat;
import android.util.Log;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

class FileSynthesisCallback extends AbstractSynthesisCallback {
    private static final boolean DBG = false;
    private static final int MAX_AUDIO_BUFFER_SIZE = 8192;
    private static final String TAG = "FileSynthesisRequest";
    private static final short WAV_FORMAT_PCM = (short) 1;
    private static final int WAV_HEADER_LENGTH = 44;
    private int mAudioFormat;
    private final Object mCallerIdentity;
    private int mChannelCount;
    private final UtteranceProgressDispatcher mDispatcher;
    private boolean mDone = false;
    private FileChannel mFileChannel;
    private int mSampleRateInHz;
    private boolean mStarted = false;
    private final Object mStateLock = new Object();
    protected int mStatusCode;

    FileSynthesisCallback(FileChannel fileChannel, UtteranceProgressDispatcher dispatcher, Object callerIdentity, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        this.mFileChannel = fileChannel;
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
        this.mStatusCode = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void stop() {
        /*
        r3 = this;
        r2 = -2;
        r1 = r3.mStateLock;
        monitor-enter(r1);
        r0 = r3.mDone;	 Catch:{ all -> 0x0010 }
        if (r0 == 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
    L_0x0009:
        return;
    L_0x000a:
        r0 = r3.mStatusCode;	 Catch:{ all -> 0x0010 }
        if (r0 != r2) goto L_0x0013;
    L_0x000e:
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
        goto L_0x0009;
    L_0x0010:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
        throw r0;
    L_0x0013:
        r0 = -2;
        r3.mStatusCode = r0;	 Catch:{ all -> 0x0010 }
        r3.cleanUp();	 Catch:{ all -> 0x0010 }
        r0 = r3.mDispatcher;	 Catch:{ all -> 0x0010 }
        if (r0 == 0) goto L_0x0022;
    L_0x001d:
        r0 = r3.mDispatcher;	 Catch:{ all -> 0x0010 }
        r0.dispatchOnStop();	 Catch:{ all -> 0x0010 }
    L_0x0022:
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.FileSynthesisCallback.stop():void");
    }

    private void cleanUp() {
        closeFile();
    }

    private void closeFile() {
        this.mFileChannel = null;
    }

    public int getMaxBufferSize() {
        return 8192;
    }

    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        synchronized (this.mStateLock) {
            if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mStarted) {
                Log.e(TAG, "Start called twice");
                return -1;
            } else {
                this.mStarted = true;
                this.mSampleRateInHz = sampleRateInHz;
                this.mAudioFormat = audioFormat;
                this.mChannelCount = channelCount;
                if (this.mDispatcher != null) {
                    this.mDispatcher.dispatchOnStart();
                }
                FileChannel fileChannel = this.mFileChannel;
                try {
                    fileChannel.write(ByteBuffer.allocate(44));
                    return 0;
                } catch (IOException ex) {
                    Log.e(TAG, "Failed to write wav header to output file descriptor", ex);
                    synchronized (this.mStateLock) {
                        cleanUp();
                        this.mStatusCode = -5;
                        return -1;
                    }
                }
            }
        }
    }

    public int audioAvailable(byte[] buffer, int offset, int length) {
        synchronized (this.mStateLock) {
            if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mFileChannel == null) {
                Log.e(TAG, "File not open");
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStarted) {
                FileChannel fileChannel = this.mFileChannel;
                try {
                    fileChannel.write(ByteBuffer.wrap(buffer, offset, length));
                    return 0;
                } catch (IOException ex) {
                    Log.e(TAG, "Failed to write to output file descriptor", ex);
                    synchronized (this.mStateLock) {
                        cleanUp();
                        this.mStatusCode = -5;
                        return -1;
                    }
                }
            } else {
                Log.e(TAG, "Start method was not called");
                return -1;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int done() {
        /*
        r12 = this;
        r9 = -2;
        r6 = -1;
        r4 = 0;
        r5 = 0;
        r0 = 0;
        r1 = 0;
        r7 = r12.mStateLock;
        monitor-enter(r7);
        r8 = r12.mDone;	 Catch:{ all -> 0x0020 }
        if (r8 == 0) goto L_0x0016;
    L_0x000d:
        r8 = "FileSynthesisRequest";
        r9 = "Duplicate call to done()";
        android.util.Log.w(r8, r9);	 Catch:{ all -> 0x0020 }
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
    L_0x0015:
        return r6;
    L_0x0016:
        r8 = r12.mStatusCode;	 Catch:{ all -> 0x0020 }
        if (r8 != r9) goto L_0x0023;
    L_0x001a:
        r6 = r12.errorCodeOnStop();	 Catch:{ all -> 0x0020 }
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
        goto L_0x0015;
    L_0x0020:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
        throw r6;
    L_0x0023:
        r8 = r12.mDispatcher;	 Catch:{ all -> 0x0020 }
        if (r8 == 0) goto L_0x0038;
    L_0x0027:
        r8 = r12.mStatusCode;	 Catch:{ all -> 0x0020 }
        if (r8 == 0) goto L_0x0038;
    L_0x002b:
        r8 = r12.mStatusCode;	 Catch:{ all -> 0x0020 }
        if (r8 == r9) goto L_0x0038;
    L_0x002f:
        r8 = r12.mDispatcher;	 Catch:{ all -> 0x0020 }
        r9 = r12.mStatusCode;	 Catch:{ all -> 0x0020 }
        r8.dispatchOnError(r9);	 Catch:{ all -> 0x0020 }
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
        goto L_0x0015;
    L_0x0038:
        r8 = r12.mFileChannel;	 Catch:{ all -> 0x0020 }
        if (r8 != 0) goto L_0x0045;
    L_0x003c:
        r8 = "FileSynthesisRequest";
        r9 = "File not open";
        android.util.Log.e(r8, r9);	 Catch:{ all -> 0x0020 }
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
        goto L_0x0015;
    L_0x0045:
        r8 = 1;
        r12.mDone = r8;	 Catch:{ all -> 0x0020 }
        r4 = r12.mFileChannel;	 Catch:{ all -> 0x0020 }
        r5 = r12.mSampleRateInHz;	 Catch:{ all -> 0x0020 }
        r0 = r12.mAudioFormat;	 Catch:{ all -> 0x0020 }
        r1 = r12.mChannelCount;	 Catch:{ all -> 0x0020 }
        monitor-exit(r7);	 Catch:{ all -> 0x0020 }
        r8 = 0;
        r4.position(r8);	 Catch:{ IOException -> 0x007b }
        r8 = r4.size();	 Catch:{ IOException -> 0x007b }
        r10 = 44;
        r8 = r8 - r10;
        r2 = (int) r8;	 Catch:{ IOException -> 0x007b }
        r7 = r12.makeWavHeader(r5, r0, r1, r2);	 Catch:{ IOException -> 0x007b }
        r4.write(r7);	 Catch:{ IOException -> 0x007b }
        r8 = r12.mStateLock;	 Catch:{ IOException -> 0x007b }
        monitor-enter(r8);	 Catch:{ IOException -> 0x007b }
        r12.closeFile();	 Catch:{ all -> 0x0078 }
        r7 = r12.mDispatcher;	 Catch:{ all -> 0x0078 }
        if (r7 == 0) goto L_0x0074;
    L_0x006f:
        r7 = r12.mDispatcher;	 Catch:{ all -> 0x0078 }
        r7.dispatchOnSuccess();	 Catch:{ all -> 0x0078 }
    L_0x0074:
        r7 = 0;
        monitor-exit(r8);	 Catch:{ all -> 0x0078 }
        r6 = r7;
        goto L_0x0015;
    L_0x0078:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0078 }
        throw r7;	 Catch:{ IOException -> 0x007b }
    L_0x007b:
        r3 = move-exception;
        r7 = "FileSynthesisRequest";
        r8 = "Failed to write to output file descriptor";
        android.util.Log.e(r7, r8, r3);
        r7 = r12.mStateLock;
        monitor-enter(r7);
        r12.cleanUp();	 Catch:{ all -> 0x008b }
        monitor-exit(r7);	 Catch:{ all -> 0x008b }
        goto L_0x0015;
    L_0x008b:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x008b }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.FileSynthesisCallback.done():int");
    }

    public void error() {
        error(-3);
    }

    public void error(int errorCode) {
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            cleanUp();
            this.mStatusCode = errorCode;
        }
    }

    public boolean hasStarted() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mStarted;
        }
        return z;
    }

    public boolean hasFinished() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mDone;
        }
        return z;
    }

    private ByteBuffer makeWavHeader(int sampleRateInHz, int audioFormat, int channelCount, int dataLength) {
        int sampleSizeInBytes = AudioFormat.getBytesPerSample(audioFormat);
        int byteRate = (sampleRateInHz * sampleSizeInBytes) * channelCount;
        short blockAlign = (short) (sampleSizeInBytes * channelCount);
        short bitsPerSample = (short) (sampleSizeInBytes * 8);
        ByteBuffer header = ByteBuffer.wrap(new byte[44]);
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.put(new byte[]{(byte) 82, (byte) 73, (byte) 70, (byte) 70});
        header.putInt((dataLength + 44) - 8);
        header.put(new byte[]{(byte) 87, (byte) 65, (byte) 86, (byte) 69});
        header.put(new byte[]{ISensorHubCmdProtocol.TYPE_CARRYING_STATUS_MONITOR_SERVICE, (byte) 109, (byte) 116, (byte) 32});
        header.putInt(16);
        header.putShort(WAV_FORMAT_PCM);
        header.putShort((short) channelCount);
        header.putInt(sampleRateInHz);
        header.putInt(byteRate);
        header.putShort(blockAlign);
        header.putShort(bitsPerSample);
        header.put(new byte[]{(byte) 100, (byte) 97, (byte) 116, (byte) 97});
        header.putInt(dataLength);
        header.flip();
        return header;
    }
}
