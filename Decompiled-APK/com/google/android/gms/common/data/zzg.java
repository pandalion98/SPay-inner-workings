package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class zzg<T> extends AbstractDataBuffer<T> {
    private boolean zzOi;
    private ArrayList<Integer> zzOj;

    protected zzg(DataHolder dataHolder) {
        super(dataHolder);
        this.zzOi = false;
    }

    private void zziC() {
        synchronized (this) {
            if (!this.zzOi) {
                int count = this.zzMd.getCount();
                this.zzOj = new ArrayList();
                if (count > 0) {
                    this.zzOj.add(Integer.valueOf(0));
                    String zziB = zziB();
                    String zzc = this.zzMd.zzc(zziB, 0, this.zzMd.zzax(0));
                    int i = 1;
                    while (i < count) {
                        int zzax = this.zzMd.zzax(i);
                        String zzc2 = this.zzMd.zzc(zziB, i, zzax);
                        if (zzc2 == null) {
                            throw new NullPointerException("Missing value for markerColumn: " + zziB + ", at row: " + i + ", for window: " + zzax);
                        }
                        if (zzc2.equals(zzc)) {
                            zzc2 = zzc;
                        } else {
                            this.zzOj.add(Integer.valueOf(i));
                        }
                        i++;
                        zzc = zzc2;
                    }
                }
                this.zzOi = true;
            }
        }
    }

    public final T get(int i) {
        zziC();
        return zzj(zzaA(i), zzaB(i));
    }

    public int getCount() {
        zziC();
        return this.zzOj.size();
    }

    int zzaA(int i) {
        if (i >= 0 && i < this.zzOj.size()) {
            return ((Integer) this.zzOj.get(i)).intValue();
        }
        throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
    }

    protected int zzaB(int i) {
        if (i < 0 || i == this.zzOj.size()) {
            return 0;
        }
        int count = i == this.zzOj.size() + -1 ? this.zzMd.getCount() - ((Integer) this.zzOj.get(i)).intValue() : ((Integer) this.zzOj.get(i + 1)).intValue() - ((Integer) this.zzOj.get(i)).intValue();
        if (count != 1) {
            return count;
        }
        int zzaA = zzaA(i);
        int zzax = this.zzMd.zzax(zzaA);
        String zziD = zziD();
        return (zziD == null || this.zzMd.zzc(zziD, zzaA, zzax) != null) ? count : 0;
    }

    protected abstract String zziB();

    protected String zziD() {
        return null;
    }

    protected abstract T zzj(int i, int i2);
}
