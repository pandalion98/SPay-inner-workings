package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;

public abstract class AbstractDataBuffer<T> implements DataBuffer<T> {
    protected final DataHolder zzMd;

    protected AbstractDataBuffer(DataHolder dataHolder) {
        this.zzMd = dataHolder;
        if (this.zzMd != null) {
            this.zzMd.zzg(this);
        }
    }

    @Deprecated
    public final void close() {
        release();
    }

    public abstract T get(int i);

    public int getCount() {
        return this.zzMd == null ? 0 : this.zzMd.getCount();
    }

    @Deprecated
    public boolean isClosed() {
        return this.zzMd == null || this.zzMd.isClosed();
    }

    public Iterator<T> iterator() {
        return new zzc(this);
    }

    public void release() {
        if (this.zzMd != null) {
            this.zzMd.close();
        }
    }

    public Iterator<T> singleRefIterator() {
        return new zzh(this);
    }

    public Bundle zziu() {
        return this.zzMd.zziu();
    }
}
