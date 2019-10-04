/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.lang.Object
 *  java.util.List
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.Settings;
import java.io.Closeable;
import java.util.List;
import okio.Buffer;

public interface FrameWriter
extends Closeable {
    public void ackSettings(Settings var1);

    public void connectionPreface();

    public void data(boolean var1, int var2, Buffer var3, int var4);

    public void flush();

    public void goAway(int var1, ErrorCode var2, byte[] var3);

    public void headers(int var1, List<Header> var2);

    public int maxDataLength();

    public void ping(boolean var1, int var2, int var3);

    public void pushPromise(int var1, int var2, List<Header> var3);

    public void rstStream(int var1, ErrorCode var2);

    public void settings(Settings var1);

    public void synReply(boolean var1, int var2, List<Header> var3);

    public void synStream(boolean var1, boolean var2, int var3, int var4, List<Header> var5);

    public void windowUpdate(int var1, long var2);
}

