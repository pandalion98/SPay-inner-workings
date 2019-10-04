/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.SpdyStream;

public interface IncomingStreamHandler {
    public static final IncomingStreamHandler REFUSE_INCOMING_STREAMS = new IncomingStreamHandler(){

        @Override
        public void receive(SpdyStream spdyStream) {
            spdyStream.close(ErrorCode.REFUSED_STREAM);
        }
    };

    public void receive(SpdyStream var1);

}

