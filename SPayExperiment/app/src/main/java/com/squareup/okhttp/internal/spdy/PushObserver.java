/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.Header;
import java.util.List;
import okio.BufferedSource;

public interface PushObserver {
    public static final PushObserver CANCEL = new PushObserver(){

        @Override
        public boolean onData(int n2, BufferedSource bufferedSource, int n3, boolean bl) {
            bufferedSource.skip(n3);
            return true;
        }

        @Override
        public boolean onHeaders(int n2, List<Header> list, boolean bl) {
            return true;
        }

        @Override
        public boolean onRequest(int n2, List<Header> list) {
            return true;
        }

        @Override
        public void onReset(int n2, ErrorCode errorCode) {
        }
    };

    public boolean onData(int var1, BufferedSource var2, int var3, boolean var4);

    public boolean onHeaders(int var1, List<Header> var2, boolean var3);

    public boolean onRequest(int var1, List<Header> var2);

    public void onReset(int var1, ErrorCode var2);

}

