/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.spdy.FrameReader;
import com.squareup.okhttp.internal.spdy.FrameWriter;
import okio.BufferedSink;
import okio.BufferedSource;

public interface Variant {
    public Protocol getProtocol();

    public FrameReader newReader(BufferedSource var1, boolean var2);

    public FrameWriter newWriter(BufferedSink var1, boolean var2);
}

