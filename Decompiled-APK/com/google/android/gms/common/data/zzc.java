package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class zzc<T> implements Iterator<T> {
    protected final DataBuffer<T> zzNN;
    protected int zzNO;

    public zzc(DataBuffer<T> dataBuffer) {
        this.zzNN = (DataBuffer) zzx.zzl(dataBuffer);
        this.zzNO = -1;
    }

    public boolean hasNext() {
        return this.zzNO < this.zzNN.getCount() + -1;
    }

    public T next() {
        if (hasNext()) {
            DataBuffer dataBuffer = this.zzNN;
            int i = this.zzNO + 1;
            this.zzNO = i;
            return dataBuffer.get(i);
        }
        throw new NoSuchElementException("Cannot advance the iterator beyond " + this.zzNO);
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
