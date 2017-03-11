package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

public class zzh<T> extends zzc<T> {
    private T zzOk;

    public zzh(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    public T next() {
        if (hasNext()) {
            this.zzNO++;
            if (this.zzNO == 0) {
                this.zzOk = this.zzNN.get(0);
                if (!(this.zzOk instanceof zzd)) {
                    throw new IllegalStateException("DataBuffer reference of type " + this.zzOk.getClass() + " is not movable");
                }
            }
            ((zzd) this.zzOk).zzav(this.zzNO);
            return this.zzOk;
        }
        throw new NoSuchElementException("Cannot advance the iterator beyond " + this.zzNO);
    }
}
