package com.absolute.android.persistservice;

import com.absolute.android.persistence.IABTLogIterator.Stub;
import com.absolute.android.persistence.LogEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class t extends Stub {
    private v a;
    private int b;
    private File c;
    private long d;
    private boolean e = false;

    public LogEntry[] getNext(int i) {
        ArrayList arrayList = new ArrayList();
        while (!this.e) {
            boolean z;
            if (this.c == null || this.c.length() == 0) {
                z = true;
            } else if (this.a.b(this.c)) {
                synchronized (this.a) {
                    z = a(arrayList, i);
                }
            } else {
                z = a(arrayList, i);
            }
            if (z) {
                File a = this.a.a(this.c);
                if (a == null) {
                    this.e = true;
                } else {
                    this.c = a;
                    this.d = 0;
                }
            }
            if (arrayList.size() >= i) {
                break;
            }
        }
        if (arrayList.size() > 0) {
            return (LogEntry[]) arrayList.toArray(new LogEntry[arrayList.size()]);
        }
        return null;
    }

    protected t(int i, v vVar) {
        this.b = i;
        this.a = vVar;
        this.c = this.a.a(null);
        this.d = 0;
    }

    private boolean a(ArrayList arrayList, int i) {
        boolean z = false;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.c), 8192);
            bufferedReader.skip(this.d);
            int i2 = 0;
            while (i2 < i) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    z = true;
                    break;
                }
                if (readLine.length() != 0) {
                    LogEntry a = this.a.a(readLine, this.b);
                    if (a != null) {
                        arrayList.add(a);
                        i2++;
                    }
                }
                this.d = ((long) (readLine.length() + v.a.length())) + this.d;
            }
            bufferedReader.close();
            return z;
        } catch (Throwable e) {
            this.a.a("Got FileNotFoundException reading log file " + this.c.getName() + ", :", e);
            return true;
        } catch (Throwable e2) {
            this.a.a("Got IO exception reading log file " + this.c.getName() + ", :", e2);
            return true;
        }
    }
}
