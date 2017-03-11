package com.squareup.okhttp.internal.spdy;

public interface IncomingStreamHandler {
    public static final IncomingStreamHandler REFUSE_INCOMING_STREAMS;

    /* renamed from: com.squareup.okhttp.internal.spdy.IncomingStreamHandler.1 */
    static class C06531 implements IncomingStreamHandler {
        C06531() {
        }

        public void receive(SpdyStream spdyStream) {
            spdyStream.close(ErrorCode.REFUSED_STREAM);
        }
    }

    void receive(SpdyStream spdyStream);

    static {
        REFUSE_INCOMING_STREAMS = new C06531();
    }
}
